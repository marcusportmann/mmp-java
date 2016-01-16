/*
 * Copyright 2016 Marcus Portmann
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package guru.mmp.application.sms;

import guru.mmp.application.sms.SMS.Status;
import guru.mmp.common.persistence.DAOException;
import guru.mmp.common.persistence.DataAccessObject;
import guru.mmp.common.persistence.IDGenerator;
import guru.mmp.common.persistence.TransactionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.*;

/**
 * The <code>SMSDAO</code> class implements the persistence operations for the SMS infrastructure.
 *
 * @author Marcus Portmann
 */
@ApplicationScoped
@Default
public class SMSDAO
  implements ISMSDAO
{
  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(SMSDAO.class);

  private String createSMSSQL;

  /**
   * The data source used to provide connections to the database.
   */
  private DataSource dataSource;

  private String deleteSMSSQL;

  private String getNextSMSForProcessingSQL;

  private String getSMSByIdSQL;

  /**
   * The ID generator used to generate unique numeric IDs for the DAO.
   */
  private IDGenerator idGenerator;

  private String incrementSMSSendAttemptsSQL;

  private String lockSMSSQL;

  private String resetSMSLocksSQL;

  private String setSMSStatusSQL;

  private String unlockSMSSQL;

  /**
   * Constructs a new <code>SMSDAO</code>.
   */
  @SuppressWarnings("unused")
  public SMSDAO() {}

  /**
   * Create the entry for the SMS in the database.
   *
   * @param sms the <code>SMS</code> instance containing the information for the SMS
   *
   * @throws DAOException
   */
  public void createSMS(SMS sms)
    throws DAOException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(createSMSSQL))
    {
      long id = idGenerator.next("Application.SMSId");

      statement.setLong(1, id);
      statement.setString(2, sms.getMobileNumber());
      statement.setString(3, sms.getMessage());
      statement.setInt(4, sms.getStatus().getCode());

      if (statement.executeUpdate() != 1)
      {
        throw new DAOException(
          String.format("No rows were affected as a result of executing the SQL statement (%s)",
            createSMSSQL));
      }

      sms.setId(id);
    }
    catch (Throwable e)
    {
      throw new DAOException("Failed to add the SMS to the database", e);
    }
  }

  /**
   * Delete the existing SMS.
   *
   * @param id the ID uniquely identifying the SMS
   *
   * @return <code>true</code> if the SMS was deleted or <code>false</code> otherwise
   *
   * @throws DAOException
   */
  public boolean deleteSMS(long id)
    throws DAOException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(deleteSMSSQL))
    {
      statement.setLong(1, id);

      return (statement.executeUpdate() > 0);
    }
    catch (Throwable e)
    {
      throw new DAOException(String.format("Failed to delete the SMS (%d) in the database", id), e);
    }
  }

  /**
   * Retrieve the next SMS that has been queued for sending.
   * <p/>
   * The SMS will be locked to prevent duplicate sending.
   *
   * @param sendRetryDelay the delay in milliseconds to wait before re-attempting to send a SMS
   * @param lockName       the name of the lock that should be applied to the message queued for
   *                       processing when it is retrieved
   *
   * @return the next SMS that has been queued for sending or <code>null</code> if no SMSs are
   * currently queued for sending
   *
   * @throws DAOException
   */
  public SMS getNextSMSQueuedForSending(int sendRetryDelay, String lockName)
    throws DAOException
  {
    // Retrieve the Transaction Manager
    TransactionManager transactionManager = TransactionManager.getTransactionManager();
    javax.transaction.Transaction existingTransaction = null;

    try
    {
      if (transactionManager.isTransactionActive())
      {
        existingTransaction = transactionManager.beginNew();
      }
      else
      {
        transactionManager.begin();
      }

      SMS sms = null;

      try (Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(getNextSMSForProcessingSQL))
      {
        Timestamp processedBefore = new Timestamp(System.currentTimeMillis() - sendRetryDelay);

        statement.setInt(1, SMS.Status.QUEUED_FOR_SENDING.getCode());
        statement.setTimestamp(2, processedBefore);

        try (ResultSet rs = statement.executeQuery())
        {
          if (rs.next())
          {
            sms = getSMS(rs);

            sms.setStatus(SMS.Status.SENDING);
            sms.setLockName(lockName);

            try (PreparedStatement updateStatement = connection.prepareStatement(lockSMSSQL))
            {
              updateStatement.setInt(1, SMS.Status.SENDING.getCode());
              updateStatement.setString(2, lockName);
              updateStatement.setLong(3, sms.getId());

              if (updateStatement.executeUpdate() != 1)
              {
                throw new DAOException(String.format(
                  "No rows were affected as a result of executing the SQL statement (%s)",
                  lockSMSSQL));
              }
            }
          }
        }
      }

      transactionManager.commit();

      return sms;
    }
    catch (Throwable e)
    {
      try
      {
        transactionManager.rollback();
      }
      catch (Throwable f)
      {
        logger.error(
          "Failed to rollback the transaction while retrieving the next SMS that has been queued " +
            "for sending from the database", f);
      }

      throw new DAOException(
        "Failed to retrieve the next SMS that has been queued for sending from the database", e);
    }
    finally
    {
      try
      {
        if (existingTransaction != null)
        {
          transactionManager.resume(existingTransaction);
        }
      }
      catch (Throwable e)
      {
        logger.error(
          "Failed to resume the original transaction while retrieving the next SMS that has been " +
            "queued for sending from the database", e);
      }
    }
  }

  /**
   * Retrieve the SMS.
   *
   * @param id the ID uniquely identifying the SMS
   *
   * @return the SMS or <code>null</code> if the SMS could not be found
   *
   * @throws DAOException
   */
  public SMS getSMS(long id)
    throws DAOException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(getSMSByIdSQL))
    {
      statement.setLong(1, id);

      try (ResultSet rs = statement.executeQuery())
      {
        if (rs.next())
        {
          return getSMS(rs);
        }
        else
        {
          return null;
        }
      }
    }
    catch (Throwable e)
    {
      throw new DAOException(String.format("Failed to retrieve the SMS (%d) from the database", id),
        e);
    }
  }

  /**
   * Increment the send attempts for the SMS.
   *
   * @param sms the SMS whose send attempts should be incremented
   *
   * @throws DAOException
   */
  public void incrementSMSSendAttempts(SMS sms)
    throws DAOException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(incrementSMSSendAttemptsSQL))
    {
      Timestamp currentTime = new Timestamp(System.currentTimeMillis());

      statement.setTimestamp(1, currentTime);
      statement.setLong(2, sms.getId());

      if (statement.executeUpdate() != 1)
      {
        throw new DAOException(
          "No rows were affected as a result of executing the SQL statement (" +
            incrementSMSSendAttemptsSQL + ")");
      }

      sms.setSendAttempts(sms.getSendAttempts() + 1);
      sms.setLastProcessed(currentTime);
    }
    catch (Throwable e)
    {
      throw new DAOException(
        String.format("Failed to increment the send attempts for the SMS (%d) in the database",
          sms.getId()), e);
    }
  }

  /**
   * Initialise the <code>SMSDAO</code> instance.
   */
  @PostConstruct
  public void init()
  {
    try
    {
      dataSource = InitialContext.doLookup("java:app/jdbc/ApplicationDataSource");
    }
    catch (Throwable ignored)
    {
    }

    if (dataSource == null)
    {
      try
      {
        dataSource = InitialContext.doLookup("java:comp/env/jdbc/ApplicationDataSource");
      }
      catch (Throwable ignored)
      {
      }
    }

    if (dataSource == null)
    {
      throw new DAOException(
        "Failed to retrieve the application data source using the JNDI names " +
          "(java:app/jdbc/ApplicationDataSource) and (java:comp/env/jdbc/ApplicationDataSource)");
    }

    try
    {
      // Retrieve the database meta data
      String schemaSeparator;

      try (Connection connection = dataSource.getConnection())
      {
        DatabaseMetaData metaData = connection.getMetaData();

        // Retrieve the schema separator for the database
        schemaSeparator = metaData.getCatalogSeparator();

        if ((schemaSeparator == null) || (schemaSeparator.length() == 0))
        {
          schemaSeparator = ".";
        }
      }

      // Determine the schema prefix
      String schemaPrefix = DataAccessObject.MMP_DATABASE_SCHEMA + schemaSeparator;

      // Build the SQL statements for the DAO
      buildStatements(schemaPrefix);
    }
    catch (Throwable e)
    {
      throw new DAOException(
        String.format("Failed to initialise the %s data access object: %s", getClass().getName(),
          e.getMessage()), e);
    }

    idGenerator = new IDGenerator(dataSource, DataAccessObject.MMP_DATABASE_SCHEMA);
  }

  /**
   * Reset the SMS locks.
   *
   * @param lockName  the name of the lock applied by the entity that has locked the SMSs
   * @param status    the current status of the SMSs that have been locked
   * @param newStatus the new status for the SMSs that have been unlocked
   *
   * @throws DAOException
   */
  public void resetSMSLocks(String lockName, SMS.Status status, SMS.Status newStatus)
    throws DAOException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(resetSMSLocksSQL))
    {
      statement.setInt(1, newStatus.getCode());
      statement.setString(2, lockName);
      statement.setInt(3, status.getCode());

      statement.executeUpdate();
    }
    catch (Throwable e)
    {
      throw new DAOException(String.format(
        "Failed to reset the locks for the SMSs with the status (%s) that have been locked using " +
          "the lock name (%s)", status, lockName), e);
    }
  }

  /**
   * Set the status for the SMS.
   *
   * @param id     the ID uniquely identifying the SMS
   * @param status the new status for the SMS
   *
   * @throws DAOException
   */
  public void setSMSStatus(long id, SMS.Status status)
    throws DAOException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(setSMSStatusSQL))
    {
      statement.setInt(1, status.getCode());
      statement.setLong(2, id);

      if (statement.executeUpdate() != 1)
      {
        throw new DAOException(
          String.format("No rows were affected as a result of executing the SQL statement (%s)",
            setSMSStatusSQL));
      }
    }
    catch (Throwable e)
    {
      throw new DAOException(
        String.format("Failed to set the status for the SMS (%d) to (%s) in the database", id,
          status.toString()), e);
    }
  }

  /**
   * Unlock the SMS.
   *
   * @param id     the ID uniquely identifying the SMS
   * @param status the new status for the unlocked SMS
   *
   * @throws DAOException
   */
  public void unlockSMS(long id, SMS.Status status)
    throws DAOException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(unlockSMSSQL))
    {
      statement.setInt(1, status.getCode());
      statement.setLong(2, id);

      if (statement.executeUpdate() != 1)
      {
        throw new DAOException(
          String.format("No rows were affected as a result of executing the SQL statement (%s)",
            unlockSMSSQL));
      }
    }
    catch (Throwable e)
    {
      throw new DAOException(String.format(
        "Failed to unlock and set the status for the SMS (%d) to (%s) in the database", id,
        status.toString()), e);
    }
  }

  /**
   * Build the SQL statements for the DAO.
   *
   * @param schemaPrefix the schema prefix to prepend to database objects referenced by the DAO
   */
  protected void buildStatements(String schemaPrefix)
  {
    // createSMSSQL
    createSMSSQL = "INSERT INTO " + schemaPrefix + "SMS" + " (ID, MOBILE_NUMBER, MESSAGE, STATUS," +
      " SEND_ATTEMPTS)" + " VALUES (?, ?, ?, ?, 0)";

    // deleteSMSSQL
    deleteSMSSQL = "DELETE FROM " + schemaPrefix + "SMS WHERE ID=?";

    // getNextSMSForProcessingSQL
    getNextSMSForProcessingSQL = "SELECT ID, MOBILE_NUMBER, MESSAGE, STATUS, SEND_ATTEMPTS," + " " +
      "LOCK_NAME, LAST_PROCESSED FROM " + schemaPrefix + "SMS" + " WHERE STATUS=? AND " +
      "(LAST_PROCESSED<? OR LAST_PROCESSED IS NULL)" + " FETCH FIRST 1 ROWS ONLY FOR UPDATE";

    // getSMSByIdSQL
    getSMSByIdSQL = "SELECT ID, MOBILE_NUMBER, MESSAGE, STATUS, SEND_ATTEMPTS, LOCK_NAME," + " " +
      "LAST_PROCESSED FROM " + schemaPrefix + "SMS WHERE ID=?";

    // incrementSMSSendAttemptsSQL
    incrementSMSSendAttemptsSQL = "UPDATE " + schemaPrefix + "SMS" + " SET " +
      "SEND_ATTEMPTS=SEND_ATTEMPTS + 1, LAST_PROCESSED=? WHERE ID=?";

    // lockSMSSQL
    lockSMSSQL = "UPDATE " + schemaPrefix + "SMS" + " SET STATUS=?, LOCK_NAME=? WHERE ID=?";

    // resetSMSLocksSQL
    resetSMSLocksSQL = "UPDATE " + schemaPrefix + "SMS" + " SET STATUS=?, LOCK_NAME=NULL WHERE " +
      "LOCK_NAME=? AND STATUS=?";

    // setSMSStatusSQL
    setSMSStatusSQL = "UPDATE " + schemaPrefix + "SMS" + " SET STATUS=? WHERE ID=?";

    // unlockSMSSQL
    unlockSMSSQL = "UPDATE " + schemaPrefix + "SMS" + " SET STATUS=?, LOCK_NAME=NULL WHERE ID=?";
  }

  private SMS getSMS(ResultSet rs)
    throws SQLException
  {
    return new SMS(rs.getLong(1), rs.getString(2), rs.getString(3), Status.fromCode(rs.getInt(4)),
      rs.getInt(5), rs.getString(6), rs.getTimestamp(7));
  }
}

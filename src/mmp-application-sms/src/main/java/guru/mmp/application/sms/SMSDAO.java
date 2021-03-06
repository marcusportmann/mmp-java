/*
 * Copyright 2017 Marcus Portmann
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

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.persistence.IDGenerator;
import guru.mmp.application.sms.SMS.Status;
import guru.mmp.common.persistence.DAOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.*;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>SMSDAO</code> class implements the persistence operations for the SMS infrastructure.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
@Repository
public class SMSDAO
  implements ISMSDAO
{
  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(SMSDAO.class);

  /**
   * The data source used to provide connections to the application database.
   */
  @Inject
  @Qualifier("applicationDataSource")
  private DataSource dataSource;

  /**
   * The ID Generator.
   */
  @Inject
  private IDGenerator idGenerator;

  /**
   * Constructs a new <code>SMSDAO</code>.
   */
  @SuppressWarnings("unused")
  public SMSDAO() {}

  /**
   * Create the entry for the SMS in the database.
   *
   * @param sms the <code>SMS</code> instance containing the information for the SMS
   */
  public void createSMS(SMS sms)
    throws DAOException
  {
    String createSMSSQL =
        "INSERT INTO SMS.SMS (ID, MOBILE_NUMBER, MESSAGE, STATUS, SEND_ATTEMPTS) "
        + "VALUES (?, ?, ?, ?, 0)";

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
        throw new DAOException(String.format(
            "No rows were affected as a result of executing the SQL statement (%s)", createSMSSQL));
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
   */
  public void deleteSMS(long id)
    throws DAOException
  {
    String deleteSMSSQL = "DELETE FROM SMS.SMS WHERE ID=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(deleteSMSSQL))
    {
      statement.setLong(1, id);

      statement.executeUpdate();
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
   * @param lockName       the name of the lock that should be applied to the SMS queued for
   *                       sending when it is retrieved
   *
   * @return the next SMS that has been queued for sending or <code>null</code> if no SMSs are
   *         currently queued for sending
   */
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public SMS getNextSMSQueuedForSending(int sendRetryDelay, String lockName)
    throws DAOException
  {
    String getNextSMSQueuedForSendingSQL =
        "SELECT ID, MOBILE_NUMBER, MESSAGE, STATUS, SEND_ATTEMPTS, LOCK_NAME, LAST_PROCESSED FROM "
        + "SMS.SMS WHERE STATUS=? AND (LAST_PROCESSED<? OR LAST_PROCESSED IS NULL) "
        + "FETCH FIRST 1 ROWS ONLY FOR UPDATE";

    String lockSMSSQL = "UPDATE SMS.SMS SET STATUS=?, LOCK_NAME=? WHERE ID=?";

    try
    {
      SMS sms = null;

      try (Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(getNextSMSQueuedForSendingSQL))
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

      return sms;
    }
    catch (Throwable e)
    {
      throw new DAOException(
          "Failed to retrieve the next SMS that has been queued for sending from the database", e);
    }
  }

  /**
   * Retrieve the SMS.
   *
   * @param id the ID uniquely identifying the SMS
   *
   * @return the SMS or <code>null</code> if the SMS could not be found
   */
  public SMS getSMS(long id)
    throws DAOException
  {
    String getSMSByIdSQL =
        "SELECT ID, MOBILE_NUMBER, MESSAGE, STATUS, SEND_ATTEMPTS, LOCK_NAME, LAST_PROCESSED FROM "
        + "SMS.SMS WHERE ID=?";

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
      throw new DAOException(String.format("Failed to retrieve the SMS (%d) from the database",
          id), e);
    }
  }

  /**
   * Increment the send attempts for the SMS.
   *
   * @param sms the SMS whose send attempts should be incremented
   */
  public void incrementSMSSendAttempts(SMS sms)
    throws DAOException
  {
    String incrementSMSSendAttemptsSQL =
        "UPDATE SMS.SMS SET SEND_ATTEMPTS=SEND_ATTEMPTS + 1, LAST_PROCESSED=? WHERE ID=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(incrementSMSSendAttemptsSQL))
    {
      Timestamp currentTime = new Timestamp(System.currentTimeMillis());

      statement.setTimestamp(1, currentTime);
      statement.setLong(2, sms.getId());

      if (statement.executeUpdate() != 1)
      {
        throw new DAOException("No rows were affected as a result of executing the SQL statement ("
            + incrementSMSSendAttemptsSQL + ")");
      }

      sms.setSendAttempts(sms.getSendAttempts() + 1);
      sms.setLastProcessed(currentTime);
    }
    catch (Throwable e)
    {
      throw new DAOException(String.format(
          "Failed to increment the send attempts for the SMS (%d) in the database", sms.getId()),
          e);
    }
  }

  /**
   * Reset the SMS locks.
   *
   * @param lockName  the name of the lock applied by the entity that has locked the SMSs
   * @param status    the current status of the SMSs that have been locked
   * @param newStatus the new status for the SMSs that have been unlocked
   */
  public void resetSMSLocks(String lockName, SMS.Status status, SMS.Status newStatus)
    throws DAOException
  {
    String resetSMSLocksSQL =
        "UPDATE SMS.SMS SET STATUS=?, LOCK_NAME=NULL WHERE LOCK_NAME=? AND STATUS=?";

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
          "Failed to reset the locks for the SMSs with the status (%s) that have been locked using "
          + "the lock name (%s)", status, lockName), e);
    }
  }

  /**
   * Set the status for the SMS.
   *
   * @param id     the ID uniquely identifying the SMS
   * @param status the new status for the SMS
   */
  public void setSMSStatus(long id, SMS.Status status)
    throws DAOException
  {
    String setSMSStatusSQL = "UPDATE SMS.SMS SET STATUS=? WHERE ID=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(setSMSStatusSQL))
    {
      statement.setInt(1, status.getCode());
      statement.setLong(2, id);

      if (statement.executeUpdate() != 1)
      {
        throw new DAOException(String.format(
            "No rows were affected as a result of executing the SQL statement (%s)",
            setSMSStatusSQL));
      }
    }
    catch (Throwable e)
    {
      throw new DAOException(String.format(
          "Failed to set the status for the SMS (%d) to (%s) in the database", id,
          status.toString()), e);
    }
  }

  /**
   * Unlock the SMS.
   *
   * @param id     the ID uniquely identifying the SMS
   * @param status the new status for the unlocked SMS
   */
  public void unlockSMS(long id, SMS.Status status)
    throws DAOException
  {
    String unlockSMSSQL = "UPDATE SMS.SMS SET STATUS=?, LOCK_NAME=NULL WHERE ID=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(unlockSMSSQL))
    {
      statement.setInt(1, status.getCode());
      statement.setLong(2, id);

      if (statement.executeUpdate() != 1)
      {
        throw new DAOException(String.format(
            "No rows were affected as a result of executing the SQL statement (%s)", unlockSMSSQL));
      }
    }
    catch (Throwable e)
    {
      throw new DAOException(String.format(
          "Failed to unlock and set the status for the SMS (%d) to (%s) in the database", id,
          status.toString()), e);
    }
  }

  private SMS getSMS(ResultSet rs)
    throws SQLException
  {
    return new SMS(rs.getLong(1), rs.getString(2), rs.getString(3), Status.fromCode(rs.getInt(4)),
        rs.getInt(5), rs.getString(6), rs.getTimestamp(7));
  }
}

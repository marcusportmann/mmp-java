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

package guru.mmp.application.process;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.common.persistence.DAOException;
import guru.mmp.common.persistence.DAOUtil;
import guru.mmp.common.persistence.DataAccessObject;
import guru.mmp.common.persistence.TransactionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>ProcessDAO</code> class implements the process-related persistence operations.
 *
 * @author Marcus Portmann
 */
@ApplicationScoped
@Default
public class ProcessDAO
  implements IProcessDAO
{
  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(ProcessDAO.class);
  private String createProcessDefinitionSQL;
  private String createProcessInstanceSQL;

  /**
   * The data source used to provide connections to the database.
   */
  private DataSource dataSource;
  private String deleteProcessDefinitionSQL;
  private String deleteProcessInstanceSQL;
  private String getCurrentProcessDefinitionSummariesSQL;
  private String getCurrentProcessDefinitionsSQL;
  private String getNextProcessInstanceScheduledForExecutionSQL;
  private String getNumberOfProcessDefinitionSQL;
  private String getNumberOfProcessInstancesSQL;
  private String getProcessDefinitionByIdAndVersionSQL;
  private String getProcessDefinitionSummaryByIdAndVersionSQL;
  private String getProcessInstanceByIdSQL;
  private String getProcessInstanceSummariesSQL;
  private String getProcessInstanceSummaryByIdSQL;
  private String lockProcessInstanceSQL;
  private String processDefinitionExistsSQL;
  private String processInstanceExistsSQL;
  private String resetProcessInstanceLocksSQL;
  private String unlockProcessInstanceSQL;
  private String updateProcessInstanceDataSQL;

  /**
   * Constructs a new <code>ProcessDAO</code>.
   */
  public ProcessDAO() {}

  /**
   * Create the new process definition.
   *
   * @param processDefinition the <code>ProcessDefinition</code> instance containing the information
   *                          for the new process definition
   */
  public void createProcessDefinition(ProcessDefinition processDefinition)
    throws DAOException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(createProcessDefinitionSQL))
    {
      statement.setObject(1, processDefinition.getId());
      statement.setInt(2, processDefinition.getVersion());
      statement.setString(3, processDefinition.getName());
      statement.setBytes(4, processDefinition.getData());

      if (statement.executeUpdate() != 1)
      {
        throw new DAOException(String.format(
            "No rows were affected as a result of executing the SQL statement (%s)",
            createProcessDefinitionSQL));
      }
    }
    catch (Throwable e)
    {
      throw new DAOException(String.format(
          "Failed to add the process definition (%s) with ID (%s) and version (%d) to the database",
          processDefinition.getName(), processDefinition.getId(), processDefinition.getVersion()),
          e);
    }
  }

  /**
   * Create the new process instance.
   *
   * @param processInstance the <code>ProcessInstance</code> instance containing the information
   *                        for the new process instance
   */
  public void createProcessInstance(ProcessInstance processInstance)
    throws DAOException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(createProcessInstanceSQL))
    {
      statement.setObject(1, processInstance.getId());
      statement.setObject(2, processInstance.getDefinitionId());
      statement.setInt(3, processInstance.getDefinitionVersion());
      statement.setBytes(4, processInstance.getData());
      statement.setInt(5, processInstance.getStatus().getCode());

      if (processInstance.getNextExecution() == null)
      {
        statement.setNull(6, Types.TIMESTAMP);
      }
      else
      {
        statement.setTimestamp(6, new Timestamp(processInstance.getNextExecution().getTime()));
      }

      if (statement.executeUpdate() != 1)
      {
        throw new DAOException(String.format(
            "No rows were affected as a result of executing the SQL statement (%s)",
            createProcessInstanceSQL));
      }
    }
    catch (Throwable e)
    {
      throw new DAOException(String.format(
          "Failed to add the process instance (%s) to the database", processInstance.getId()), e);
    }
  }

  /**
   * Delete all versions of the existing process definition.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the process
   *           definition
   */
  public void deleteProcessDefinition(UUID id)
    throws DAOException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(deleteProcessDefinitionSQL))
    {
      statement.setObject(1, id);

      if (statement.executeUpdate() <= 0)
      {
        throw new DAOException(String.format(
            "No rows were affected as a result of executing the SQL statement (%s)",
            deleteProcessDefinitionSQL));
      }
    }
    catch (Throwable e)
    {
      throw new DAOException(String.format(
          "Failed to delete all versions of the process definition (%s) in the database", id), e);
    }
  }

  /**
   * Delete the process instance.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the process
   *           instance
   */
  public void deleteProcessInstance(UUID id)
    throws DAOException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(deleteProcessInstanceSQL))
    {
      statement.setObject(1, id);

      if (statement.executeUpdate() != 1)
      {
        throw new DAOException(String.format(
            "No rows were affected as a result of executing the SQL statement (%s)",
            deleteProcessInstanceSQL));
      }
    }
    catch (Throwable e)
    {
      throw new DAOException(String.format(
          "Failed to delete the process instance (%s) in the database", id), e);
    }
  }

  /**
   * Returns the summaries for the current versions of all the process definitions.
   *
   * @return the summaries for the current versions of all the process definitions
   */
  public List<ProcessDefinitionSummary> getCurrentProcessDefinitionSummaries()
    throws DAOException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(
          getCurrentProcessDefinitionSummariesSQL))
    {
      List<ProcessDefinitionSummary> processDefinitionSummaries = new ArrayList<>();

      try (ResultSet rs = statement.executeQuery())
      {
        while (rs.next())
        {
          processDefinitionSummaries.add(getProcessDefinitionSummary(rs));
        }
      }

      return processDefinitionSummaries;
    }
    catch (Throwable e)
    {
      throw new DAOException(
          "Failed to retrieve the summaries for the current versions of the process definitions "
          + "from the database", e);
    }
  }

  /**
   * Returns the current versions of all the process definitions.
   *
   * @return the current versions of all the process definitions
   */
  public List<ProcessDefinition> getCurrentProcessDefinitions()
    throws DAOException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(getCurrentProcessDefinitionsSQL))
    {
      List<ProcessDefinition> processDefinitions = new ArrayList<>();

      try (ResultSet rs = statement.executeQuery())
      {
        while (rs.next())
        {
          processDefinitions.add(getProcessDefinition(rs));
        }
      }

      return processDefinitions;
    }
    catch (Throwable e)
    {
      throw new DAOException(
          "Failed to retrieve the current versions of the process definitions from the database",
          e);
    }
  }

  /**
   * Retrieve the next process instance that is scheduled for execution.
   * <p/>
   * The process instance will be locked to prevent duplicate processing.
   *
   * @param lockName the name of the lock that should be applied to the process instance scheduled
   *                 for execution when it is retrieved
   *
   * @return the next process instance that is scheduled for execution or <code>null</code> if no
   * process instances are currently scheduled for execution
   */
  public ProcessInstance getNextProcessInstanceScheduledForExecution(String lockName)
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

      ProcessInstance processInstance = null;

      try (Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(
            getNextProcessInstanceScheduledForExecutionSQL))
      {
        statement.setInt(1, ProcessInstance.Status.SCHEDULED.getCode());
        statement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));

        try (ResultSet rs = statement.executeQuery())
        {
          if (rs.next())
          {
            processInstance = getProcessInstance(rs);

            processInstance.setStatus(ProcessInstance.Status.EXECUTING);
            processInstance.setLockName(lockName);

            try (PreparedStatement updateStatement = connection.prepareStatement(
                lockProcessInstanceSQL))
            {
              updateStatement.setInt(1, ProcessInstance.Status.EXECUTING.getCode());
              updateStatement.setString(2, lockName);
              updateStatement.setObject(3, processInstance.getId());

              if (updateStatement.executeUpdate() != 1)
              {
                throw new DAOException(String.format(
                    "No rows were affected as a result of executing the SQL statement (%s)",
                    lockProcessInstanceSQL));
              }
            }
          }
        }
      }

      transactionManager.commit();

      return processInstance;
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
            "Failed to rollback the transaction while retrieving the next process instance that has"
            + " been scheduled for execution from the database", f);
      }

      throw new DAOException(
          "Failed to retrieve the next process instance that has been scheduled for execution from "
          + "the database", e);
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
            "Failed to resume the transaction while retrieving the next process instance that has "
            + "been scheduled for execution from the database", e);
      }
    }
  }

  /**
   * Returns the number of process definitions.
   *
   * @return the number of process definitions
   */
  public int getNumberOfProcessDefinitions()
    throws DAOException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(getNumberOfProcessDefinitionSQL))
    {
      try (ResultSet rs = statement.executeQuery())
      {
        if (rs.next())
        {
          return rs.getInt(1);
        }
        else
        {
          throw new DAOException(String.format(
              "No results were returned as a result of executing the SQL statement (%s)",
              getNumberOfProcessDefinitionSQL));
        }
      }
    }
    catch (Throwable e)
    {
      throw new DAOException(
          "Failed to retrieve the number of process definitions from the database", e);
    }
  }

  /**
   * Returns the number of process instances.
   *
   * @return the number of process instances
   */
  public int getNumberOfProcessInstances()
    throws DAOException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(getNumberOfProcessInstancesSQL))
    {
      try (ResultSet rs = statement.executeQuery())
      {
        if (rs.next())
        {
          return rs.getInt(1);
        }
        else
        {
          throw new DAOException(String.format(
              "No results were returned as a result of executing the SQL statement (%s)",
              getNumberOfProcessInstancesSQL));
        }
      }
    }
    catch (Throwable e)
    {
      throw new DAOException(
          "Failed to retrieve the number of process instances from the database", e);
    }
  }

  /**
   * Retrieve the process definition and version.
   *
   * @param id      the Universally Unique Identifier (UUID) used to uniquely identify the process
   *                definition
   * @param version the version of the process definition
   *
   * @return the process definition and version or <code>null</code> if the process definition
   * could not be found
   */
  public ProcessDefinition getProcessDefinition(UUID id, int version)
    throws DAOException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(
          getProcessDefinitionByIdAndVersionSQL))
    {
      statement.setObject(1, id);
      statement.setInt(2, version);

      try (ResultSet rs = statement.executeQuery())
      {
        if (rs.next())
        {
          return getProcessDefinition(rs);
        }
        else
        {
          return null;
        }
      }
    }
    catch (Throwable e)
    {
      throw new DAOException(String.format(
          "Failed to retrieve the process definition with ID (%s) and version (%d) from the database",
          id, version), e);
    }
  }

  /**
   * Retrieve the summary for the process definition and version.
   *
   * @param id      the Universally Unique Identifier (UUID) used to uniquely identify the
   *                process definition
   * @param version the version of the process definition
   *
   * @return the summary for the process definition and version or <code>null</code> if the process
   * definition could not be found
   */
  public ProcessDefinitionSummary getProcessDefinitionSummary(UUID id, int version)
    throws DAOException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(
          getProcessDefinitionSummaryByIdAndVersionSQL))
    {
      statement.setObject(1, id);
      statement.setInt(2, version);

      try (ResultSet rs = statement.executeQuery())
      {
        if (rs.next())
        {
          return getProcessDefinitionSummary(rs);
        }
        else
        {
          return null;
        }
      }
    }
    catch (Throwable e)
    {
      throw new DAOException(String.format(
          "Failed to retrieve the summary for the process definition with ID (%s) and version (%d) "
          + "from the database", id, version), e);
    }
  }

  /**
   * Retrieve the process instance.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the process
   *           instance
   *
   * @return the process instance or <code>null</code> if the process instance could not be found
   */
  public ProcessInstance getProcessInstance(UUID id)
    throws DAOException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(getProcessInstanceByIdSQL))
    {
      statement.setObject(1, id);

      try (ResultSet rs = statement.executeQuery())
      {
        if (rs.next())
        {
          return getProcessInstance(rs);
        }
        else
        {
          return null;
        }
      }
    }
    catch (Throwable e)
    {
      throw new DAOException(String.format(
          "Failed to retrieve the process instance (%s) from the database", id), e);
    }
  }

  /**
   * Returns the summaries for the all the process instances.
   *
   * @return the summaries for the all the process instances
   */
  public List<ProcessInstanceSummary> getProcessInstanceSummaries()
    throws DAOException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(getProcessInstanceSummariesSQL))
    {
      List<ProcessInstanceSummary> processInstanceSummaries = new ArrayList<>();

      try (ResultSet rs = statement.executeQuery())
      {
        while (rs.next())
        {
          processInstanceSummaries.add(getProcessInstanceSummary(rs));
        }
      }

      return processInstanceSummaries;
    }
    catch (Throwable e)
    {
      throw new DAOException(
          "Failed to retrieve the summaries for the process instances from the database", e);
    }
  }

  /**
   * Retrieve the summary for the process instance.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the process
   *           instance
   *
   * @return the summary for the process instance or <code>null</code> if the process definition
   * could not be found
   */
  public ProcessInstanceSummary getProcessInstanceSummary(UUID id)
    throws DAOException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(getProcessInstanceSummaryByIdSQL))
    {
      statement.setObject(1, id);

      try (ResultSet rs = statement.executeQuery())
      {
        if (rs.next())
        {
          return getProcessInstanceSummary(rs);
        }
        else
        {
          return null;
        }
      }
    }
    catch (Throwable e)
    {
      throw new DAOException(String.format(
          "Failed to retrieve the summary for the process instance (%s) from the database", id), e);
    }
  }

  /**
   * Initialise the <code>ProcessDAO</code> instance.
   */
  @PostConstruct
  public void init()
  {
    try
    {
      dataSource = InitialContext.doLookup("java:app/jdbc/ApplicationDataSource");
    }
    catch (Throwable ignored) {}

    if (dataSource == null)
    {
      try
      {
        dataSource = InitialContext.doLookup("java:comp/env/jdbc/ApplicationDataSource");
      }
      catch (Throwable ignored) {}
    }

    if (dataSource == null)
    {
      throw new DAOException("Failed to retrieve the application data source using the JNDI names "
          + "(java:app/jdbc/ApplicationDataSource) and (java:comp/env/jdbc/ApplicationDataSource)");
    }

    try
    {
      // Determine the schema prefix
      String schemaPrefix = DataAccessObject.MMP_DATABASE_SCHEMA + DAOUtil.getSchemaSeparator(
          dataSource);

      // Build the SQL statements for the DAO
      buildStatements(schemaPrefix);
    }
    catch (Throwable e)
    {
      throw new DAOException(String.format("Failed to initialise the %s data access object: %s",
          getClass().getName(), e.getMessage()), e);
    }
  }

  /**
   * Check whether the process definition and version exists in the database.
   *
   * @param id      the Universally Unique Identifier (UUID) used to uniquely identify the process
   *                definition
   * @param version the version of the process definition
   *
   * @return <code>true</code> if the process definition exists or <code>false</code> otherwise
   */
  public boolean processDefinitionExists(UUID id, int version)
    throws DAOException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(processDefinitionExistsSQL))
    {
      statement.setObject(1, id);
      statement.setInt(2, version);

      try (ResultSet rs = statement.executeQuery())
      {
        if (rs.next())
        {
          return (rs.getInt(1) > 0);
        }
        else
        {
          throw new DAOException(String.format(
              "No results were returned as a result of executing the SQL statement (%s)",
              processDefinitionExistsSQL));
        }
      }
    }
    catch (Throwable e)
    {
      throw new DAOException(String.format(
          "Failed to check whether the process definition with ID (%s) and version (%d) exists in "
          + "the database", id, version), e);
    }
  }

  /**
   * Check whether the process instance exists in the database.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the process
   *           instance
   *
   * @return <code>true</code> if the process instance exists or <code>false</code> otherwise
   */
  public boolean processInstanceExists(UUID id)
    throws DAOException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(processInstanceExistsSQL))
    {
      statement.setObject(1, id);

      try (ResultSet rs = statement.executeQuery())
      {
        if (rs.next())
        {
          return (rs.getInt(1) > 0);
        }
        else
        {
          throw new DAOException(String.format(
              "No results were returned as a result of executing the SQL statement (%s)",
              processInstanceExistsSQL));
        }
      }
    }
    catch (Throwable e)
    {
      throw new DAOException(String.format(
          "Failed to check whether the process instance (%s) exists in the database", id), e);
    }
  }

  /**
   * Reset the process instance locks.
   *
   * @param lockName  the name of the lock applied by the entity that has locked the
   *                  process instances
   * @param status    the current status of the process instances that have been locked
   * @param newStatus the new status for the process instances that have been unlocked
   *
   * @return the number of process instance locks reset
   */
  public int resetProcessInstanceLocks(String lockName, ProcessInstance.Status status,
      ProcessInstance.Status newStatus)
    throws DAOException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(resetProcessInstanceLocksSQL))
    {
      statement.setInt(1, newStatus.getCode());
      statement.setString(2, lockName);
      statement.setInt(3, status.getCode());

      return statement.executeUpdate();
    }
    catch (Throwable e)
    {
      throw new DAOException(String.format(
          "Failed to reset the locks for the process instances with status (%s) that have been "
          + "locked using the lock name (%s)", status, lockName), e);
    }
  }

  /**
   * Unlock a locked process instance.
   *
   * @param id     the Universally Unique Identifier (UUID) used to uniquely identify the process
   *               instance
   * @param status the new status for the unlocked process instance
   */
  public void unlockProcessInstance(UUID id, ProcessInstance.Status status)
    throws DAOException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(unlockProcessInstanceSQL))
    {
      statement.setInt(1, status.getCode());
      statement.setObject(3, id);

      if (statement.executeUpdate() != 1)
      {
        throw new DAOException(String.format(
            "No rows were affected as a result  of executing the SQL statement (%s)",
            unlockProcessInstanceSQL));
      }
    }
    catch (Throwable e)
    {
      throw new DAOException(String.format(
          "Failed to unlock and set the status for the process instance (%s) to (%s) in the database",
          id, status), e);
    }
  }

  /**
   * Update the state for process instance.
   *
   * @param id   the Universally Unique Identifier (UUID) used to uniquely identify the process
   *             instance
   * @param data the data giving the current execution state for the process instance
   */
  public void updateProcessInstanceData(UUID id, byte[] data)
    throws DAOException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(updateProcessInstanceDataSQL))
    {
      statement.setBytes(1, data);
      statement.setObject(2, id);

      if (statement.executeUpdate() != 1)
      {
        throw new DAOException(String.format(
            "No rows were affected as a result of executing the SQL statement (%s)",
            updateProcessInstanceDataSQL));
      }
    }
    catch (Throwable e)
    {
      throw new DAOException(String.format(
          "Failed to update the data for the process instance (%s) in the database", id), e);
    }
  }

  /**
   * Build the SQL statements for the DAO.
   *
   * @param schemaPrefix the schema prefix to prepend to database objects referenced by the DAO
   */
  protected void buildStatements(String schemaPrefix)
  {
    // createProcessDefinitionSQL
    createProcessDefinitionSQL = "INSERT INTO " + schemaPrefix + "PROCESS_DEFINITIONS (ID, "
        + "VERSION, NAME, DATA) VALUES (?, ?, ?, ?)";

    // createProcessInstanceSQL
    createProcessInstanceSQL = "INSERT INTO " + schemaPrefix + "PROCESS_INSTANCES (ID, "
        + "DEFINITION_ID, DEFINITION_VERSION, DATA, STATUS, NEXT_EXECUTION)" + " VALUES (?, ?, ?, "
        + "?, ?, ?)";

    // deleteProcessDefinitionSQL
    deleteProcessDefinitionSQL = "DELETE FROM " + schemaPrefix + "PROCESS_DEFINITIONS PD WHERE PD"
        + ".ID=?";

    // deleteProcessInstanceSQL
    deleteProcessInstanceSQL = "DELETE FROM " + schemaPrefix + "PROCESS_INSTANCES PI WHERE PI.ID=?";

    // getCurrentProcessDefinitionsSQL
    getCurrentProcessDefinitionsSQL = "SELECT PD.ID, PD.VERSION, PD.NAME, PD.DATA FROM "
        + schemaPrefix + "PROCESS_DEFINITIONS PD " + "INNER JOIN (SELECT ID AS CURRENT_ID, MAX"
        + "(VERSION) AS CURRENT_VERSION FROM " + schemaPrefix + "PROCESS_DEFINITIONS GROUP BY "
        + "CURRENT_ID) PDC" + " ON PD.ID = PDC.CURRENT_ID AND PD.VERSION = PDC.CURRENT_VERSION";

    // getCurrentProcessDefinitionSummariesSQL
    getCurrentProcessDefinitionSummariesSQL = "SELECT PD.ID, PD.VERSION, PD.NAME FROM "
        + schemaPrefix + "PROCESS_DEFINITIONS PD " + "INNER JOIN (SELECT ID AS CURRENT_ID, MAX"
        + "(VERSION) AS CURRENT_VERSION FROM " + schemaPrefix + "PROCESS_DEFINITIONS GROUP BY "
        + "CURRENT_ID) PDC" + " ON PD.ID = PDC.CURRENT_ID AND PD.VERSION = PDC.CURRENT_VERSION";

    // getNextProcessInstanceScheduledForExecutionSQL
    getNextProcessInstanceScheduledForExecutionSQL = "SELECT PI.ID, PI.DEFINITION_ID, PI"
        + ".DEFINITION_VERSION, PI.DATA, PI.STATUS, " + "PI.NEXT_EXECUTION, PI.LOCK_NAME FROM "
        + schemaPrefix + "PROCESS_INSTANCES PI WHERE PI.STATUS=? AND PI.NEXT_EXECUTION <= ?" + " "
        + "FETCH FIRST 1 ROWS ONLY FOR UPDATE";

    // getNumberOfProcessDefinitionSQL
    getNumberOfProcessDefinitionSQL = "SELECT COUNT(DISTINCT PD.ID) FROM " + schemaPrefix
        + "PROCESS_DEFINITIONS PD";

    // getNumberOfProcessInstancesSQL
    getNumberOfProcessInstancesSQL = "SELECT COUNT(PI.ID)" + " FROM " + schemaPrefix
        + "PROCESS_INSTANCES PI, " + schemaPrefix
        + "PROCESS_DEFINITIONS PD WHERE PI.DEFINITION_ID " + "= PD.ID"
        + " AND PI.DEFINITION_VERSION = PD.VERSION";

    // getProcessDefinitionByIdAndVersionSQL
    getProcessDefinitionByIdAndVersionSQL = "SELECT PD.ID, PD.VERSION, PD.NAME, PD.DATA FROM "
        + schemaPrefix + "PROCESS_DEFINITIONS PD WHERE PD.ID=? AND PD.VERSION = ?";

    // getProcessDefinitionSummaryByIdAndVersionSQL
    getProcessDefinitionSummaryByIdAndVersionSQL = "SELECT PD.ID, PD.VERSION, PD.NAME FROM "
        + schemaPrefix + "PROCESS_DEFINITIONS PD WHERE ID=? AND VERSION=?";

    // getProcessInstanceByIdSQL
    getProcessInstanceByIdSQL = "SELECT PI.ID, PI.DEFINITION_ID, PI.DEFINITION_VERSION, PI.DATA, "
        + "" + "PI.STATUS, PI.NEXT_EXECUTION, PI.LOCK_NAME FROM " + schemaPrefix
        + "PROCESS_INSTANCES PI WHERE PI.ID=?";

    // getProcessInstanceSummariesSQL
    getProcessInstanceSummariesSQL = "SELECT PI.ID, PI.DEFINITION_ID," + " PI.DEFINITION_VERSION,"
        + " PD.NAME AS NAME, PI.STATUS," + " PI.NEXT_EXECUTION, PI.LOCK_NAME FROM " + schemaPrefix
        + "PROCESS_INSTANCES PI, " + schemaPrefix + "PROCESS_DEFINITIONS PD WHERE PI"
        + ".DEFINITION_ID = PD.ID" + " AND PI.DEFINITION_VERSION = PD.VERSION";

    // getProcessInstanceSummaryByIdSQL
    getProcessInstanceSummaryByIdSQL = "SELECT PI.ID, PI.DEFINITION_ID," + " PI"
        + ".DEFINITION_VERSION, PD.NAME AS NAME, PI.STATUS," + " PI.NEXT_EXECUTION, PI.LOCK_NAME "
        + "FROM " + schemaPrefix + "PROCESS_INSTANCES PI, " + schemaPrefix + "PROCESS_DEFINITIONS "
        + "PD WHERE PI.DEFINITION_ID = PD.ID"
        + " AND PI.DEFINITION_VERSION = PD.VERSION AND PI.ID " + "= ?";

    // lockProcesInstanceSQL
    lockProcessInstanceSQL = "UPDATE " + schemaPrefix + "PROCESS_INSTANCES AS PI" + " SET PI"
        + ".STATUS=?, PI.LOCK_NAME=? WHERE PI.ID=?";

    // processDefinitionExistsSQL
    processDefinitionExistsSQL = "SELECT COUNT(PD.ID) FROM " + schemaPrefix
        + "PROCESS_DEFINITIONS PD WHERE PD.ID=? AND PD.VERSION=?";

    // processInstanceExistsSQL
    processInstanceExistsSQL = "SELECT COUNT(PI.ID) FROM " + schemaPrefix + "PROCESS_INSTANCES PI"
        + " WHERE PI.ID=?";

    // resetProcessInstanceLocksSQL
    resetProcessInstanceLocksSQL = "UPDATE " + schemaPrefix + "PROCESS_INSTANCES PI" + " SET PI"
        + ".STATUS=?, PI.LOCK_NAME=NULL WHERE PI.LOCK_NAME=? AND PI.STATUS=?";

    // unlockProcessInstanceSQL
    unlockProcessInstanceSQL = "UPDATE " + schemaPrefix + "PROCESS_INSTANCES AS PI" + " SET PI"
        + ".STATUS=?, PI.LOCK_NAME=NULL WHERE PI.ID=?";

    // updateProcessInstanceDataSQL
    updateProcessInstanceDataSQL = "UPDATE " + schemaPrefix + "PROCESS_INSTANCES AS PI" + " SET PI"
        + ".DATA=? WHERE PI.ID=?";
  }

  private ProcessDefinition getProcessDefinition(ResultSet rs)
    throws SQLException
  {
    return new ProcessDefinition((UUID) rs.getObject(1), rs.getInt(2), rs.getString(3), rs.getBytes(
        4));
  }

  private ProcessDefinitionSummary getProcessDefinitionSummary(ResultSet rs)
    throws SQLException
  {
    return new ProcessDefinitionSummary((UUID) rs.getObject(1), rs.getInt(2), rs.getString(3));
  }

  private ProcessInstance getProcessInstance(ResultSet rs)
    throws SQLException
  {
    return new ProcessInstance((UUID) rs.getObject(1), (UUID) rs.getObject(2), rs.getInt(3),
        rs.getBytes(4), ProcessInstance.Status.fromCode(rs.getInt(5)), rs.getTimestamp(6),
        rs.getString(7));
  }

  private ProcessInstanceSummary getProcessInstanceSummary(ResultSet rs)
    throws SQLException
  {
    return new ProcessInstanceSummary(rs.getString(1), rs.getString(2), rs.getInt(3), rs.getString(
        4), ProcessInstance.Status.fromCode(rs.getInt(5)), rs.getTimestamp(6), rs.getString(7));
  }
}

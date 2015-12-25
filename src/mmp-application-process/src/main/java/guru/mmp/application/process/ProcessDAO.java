/*
 * Copyright 2015 Marcus Portmann
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
import guru.mmp.common.persistence.DataAccessObject;
import guru.mmp.common.persistence.TransactionManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//~--- JDK imports ------------------------------------------------------------

import java.sql.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.PostConstruct;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;

import javax.naming.InitialContext;

import javax.sql.DataSource;

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

  /** The data source used to provide connections to the database. */
  private DataSource dataSource;
  private String deleteProcessDefinitionSQL;
  private String deleteProcessInstanceSQL;
  private String getCurrentProcessDefinitionSummariesForOrganisationSQL;
  private String getCurrentProcessDefinitionsForOrganisationSQL;
  private String getNextProcessInstanceScheduledForExecutionSQL;
  private String getNumberOfProcessDefinitionsForOrganisationSQL;
  private String getNumberOfProcessInstancesForOrganisationSQL;
  private String getProcessDefinitionByIdAndVersionSQL;
  private String getProcessDefinitionSummaryByIdAndVersionSQL;
  private String getProcessInstanceByIdSQL;
  private String getProcessInstanceSummariesForOrganisationSQL;
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
  @SuppressWarnings("unused")
  public ProcessDAO() {}

  /**
   * Constructs a new <code>ProcessDAO</code>.
   *
   * @param dataSource the data source to use
   *
   * @throws DAOException
   */
  @SuppressWarnings("unused")
  public ProcessDAO(DataSource dataSource)
    throws DAOException
  {
    if (dataSource == null)
    {
      throw new DAOException("Failed to initialise the " + getClass().getName()
          + " data access object:" + " The specified data source is NULL");
    }

    this.dataSource = dataSource;

    init();
  }

  /**
   * Constructs a new <code>ProcessDAO</code>.
   *
   * @param dataSourceJndiName the JNDI name of the data source used to access the database
   *
   * @throws DAOException
   */
  @SuppressWarnings("unused")
  public ProcessDAO(String dataSourceJndiName)
    throws DAOException
  {
    try
    {
      InitialContext ic = new InitialContext();

      try
      {
        this.dataSource = (DataSource) ic.lookup(dataSourceJndiName);
      }
      finally
      {
        try
        {
          ic.close();
        }
        catch (Throwable e)
        {
          // Do nothing
        }
      }
    }
    catch (Throwable e)
    {
      throw new DAOException("Failed to initialise the " + getClass().getName()
          + " data access object: Failed to lookup the data source (" + dataSourceJndiName
          + ") using JNDI: " + e.getMessage(), e);
    }

    init();
  }

  /**
   * Create the new process definition.
   *
   * @param processDefinition the <code>ProcessDefinition</code> instance containing the information
   *                         for the new process definition
   *
   * @throws DAOException
   */
  public void createProcessDefinition(ProcessDefinition processDefinition)
    throws DAOException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(createProcessDefinitionSQL))
    {
      statement.setObject(1, processDefinition.getId());
      statement.setInt(2, processDefinition.getVersion());
      statement.setObject(3, processDefinition.getOrganisationId());
      statement.setString(4, processDefinition.getName());
      statement.setBytes(5, processDefinition.getData());

      if (statement.executeUpdate() != 1)
      {
        throw new DAOException("No rows were affected as a result of executing the SQL statement ("
            + createProcessDefinitionSQL + ")");
      }
    }
    catch (Throwable e)
    {
      throw new DAOException("Failed to add the process definition (" + processDefinition.getName()
          + ") with ID (" + processDefinition.getId() + ") and version ("
          + processDefinition.getVersion() + ") to the database", e);
    }
  }

  /**
   * Create the new process instance.
   *
   * @param processInstance the <code>ProcessInstance</code> instance containing the information
   *                        for the new process instance
   *
   * @throws DAOException
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
        throw new DAOException("No rows were affected as a result of executing the SQL statement ("
            + createProcessInstanceSQL + ")");
      }
    }
    catch (Throwable e)
    {
      throw new DAOException("Failed to add the process instance (" + processInstance.getId()
          + ") to the database", e);
    }
  }

  /**
   * Delete all versions of the existing process definition.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the process
   *           definition
   *
   * @throws DAOException
   */
  public void deleteProcessDefinition(UUID id)
    throws DAOException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(deleteProcessDefinitionSQL))
    {
      statement.setObject(1, id);

      if (statement.executeUpdate() != 1)
      {
        throw new DAOException("No rows were affected as a result of executing the SQL statement ("
            + deleteProcessDefinitionSQL + ")");
      }
    }
    catch (Throwable e)
    {
      throw new DAOException("Failed to delete all versions of the process definition (" + id
          + ") in the database", e);
    }
  }

  /**
   * Delete the process instance.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the process
   *           instance
   *
   * @throws DAOException
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
        throw new DAOException("No rows were affected as a result of executing the SQL statement ("
            + deleteProcessInstanceSQL + ")");
      }
    }
    catch (Throwable e)
    {
      throw new DAOException("Failed to delete the process instance (" + id + ") in the database",
          e);
    }
  }

  /**
   * Returns the summaries for the current versions of all the process definitions associated with
   * the organisation.
   *
   * @param organisationId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                       organisation the process definitions are associated with
   *
   * @return the summaries for the current versions of all the process definitions associated with
   *         the organisation
   *
   * @throws DAOException
   */
  public List<ProcessDefinitionSummary> getCurrentProcessDefinitionSummariesForOrganisation(
      UUID organisationId)
    throws DAOException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement =
          connection.prepareStatement(getCurrentProcessDefinitionSummariesForOrganisationSQL))
    {
      statement.setObject(1, organisationId);

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
      throw new DAOException("Failed to retrieve the summaries for the current versions of the"
          + " process definitions for the organisation (" + organisationId
          + ") from the database", e);
    }
  }

  /**
   * Returns the current versions of all the process definitions associated with the organisation.
   *
   * @param organisationId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                       organisation the process definition is associated with
   *
   * @return the current versions of all the process definitions associated with the organisation
   *
   * @throws DAOException
   */
  public List<ProcessDefinition> getCurrentProcessDefinitionsForOrganisation(UUID organisationId)
    throws DAOException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement =
          connection.prepareStatement(getCurrentProcessDefinitionsForOrganisationSQL))
    {
      statement.setObject(1, organisationId);

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
      throw new DAOException("Failed to retrieve the current versions of the process definitions"
          + " for the organisation (" + organisationId + ") from the database", e);
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
   *         process instances are currently scheduled for execution
   *
   * @throws DAOException
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
        PreparedStatement statement =
            connection.prepareStatement(getNextProcessInstanceScheduledForExecutionSQL))
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

            try (PreparedStatement updateStatement =
                connection.prepareStatement(lockProcessInstanceSQL))
            {
              updateStatement.setInt(1, ProcessInstance.Status.EXECUTING.getCode());
              updateStatement.setString(2, lockName);
              updateStatement.setObject(3, processInstance.getId());

              if (updateStatement.executeUpdate() != 1)
              {
                throw new DAOException(
                    "No rows were affected as a result of executing the SQL statement ("
                    + lockProcessInstanceSQL + ")");
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
        logger.error("Failed to rollback the transaction while retrieving the next process "
            + "instance that has been scheduled for execution from the database", f);
      }

      throw new DAOException(
          "Failed to retrieve the next process instance that has been scheduled "
          + "for execution from the database", e);
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
        logger.error("Failed to resume the original transaction while retrieving the next process "
            + "instance that has been scheduled for execution from the database", e);
      }
    }
  }

  /**
   * Returns the number of process definitions associated with the organisation.
   *
   * @param organisationId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                       organisation the process definitions are associated with
   *
   * @return the number of process definitions associated with the organisation
   *
   * @throws DAOException
   */
  public int getNumberOfProcessDefinitionsForOrganisation(UUID organisationId)
    throws DAOException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement =
          connection.prepareStatement(getNumberOfProcessDefinitionsForOrganisationSQL))
    {
      statement.setObject(1, organisationId);

      try (ResultSet rs = statement.executeQuery())
      {
        if (rs.next())
        {
          return rs.getInt(1);
        }
        else
        {
          throw new DAOException(
              "No results were returned as a result of executing the SQL statement ("
              + getNumberOfProcessDefinitionsForOrganisationSQL + ")");
        }
      }
    }
    catch (Throwable e)
    {
      throw new DAOException(
          "Failed to retrieve the number of process definitions for the organisation ("
          + organisationId + ") from the database", e);
    }
  }

  /**
   * Returns the number of process instances associated with the organisation.
   *
   * @param organisationId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                       organisation the process definition is associated with
   *
   * @return the number of process instances associated with the organisation
   *
   * @throws DAOException
   */
  public int getNumberOfProcessInstancesForOrganisation(UUID organisationId)
    throws DAOException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement =
          connection.prepareStatement(getNumberOfProcessInstancesForOrganisationSQL))
    {
      statement.setObject(1, organisationId);

      try (ResultSet rs = statement.executeQuery())
      {
        if (rs.next())
        {
          return rs.getInt(1);
        }
        else
        {
          throw new DAOException(
              "No results were returned as a result of executing the SQL statement ("
              + getNumberOfProcessInstancesForOrganisationSQL + ")");
        }
      }
    }
    catch (Throwable e)
    {
      throw new DAOException(
          "Failed to retrieve the number of process instances for the organisation ("
          + organisationId + ") from the database", e);
    }
  }

  /**
   * Retrieve the process definition and version.
   *
   * @param id      the Universally Unique Identifier (UUID) used to uniquely identify the
   *                process definition
   * @param version the version of the process definition
   *
   * @return the process definition and version or <code>null</code> if the process definition
   * could not be found
   *
   * @throws DAOException
   */
  public ProcessDefinition getProcessDefinition(UUID id, int version)
    throws DAOException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement =
          connection.prepareStatement(getProcessDefinitionByIdAndVersionSQL))
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
      throw new DAOException("Failed to retrieve the process definition with ID (" + id
          + ") and version (" + version + ") from the database", e);
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
   *         definition could not be found
   *
   * @throws DAOException
   */
  public ProcessDefinitionSummary getProcessDefinitionSummary(UUID id, int version)
    throws DAOException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement =
          connection.prepareStatement(getProcessDefinitionSummaryByIdAndVersionSQL))
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
      throw new DAOException("Failed to retrieve the summary for the process definition with ID ("
          + id + ") and version (" + version + ") from the database", e);
    }
  }

  /**
   * Retrieve the process instance.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the process
   *           instance
   *
   * @return the process instance or <code>null</code> if the process instance could not be found
   *
   * @throws DAOException
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
      throw new DAOException("Failed to retrieve the process instance (" + id
          + ") from the database", e);
    }
  }

  /**
   * Returns the summaries for the all the process instances associated with the organisation.
   *
   * @param organisationId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                       organisation the process definition is associated with
   *
   * @return the summaries for the all the process instances associated with the organisation
   *
   * @throws DAOException
   */
  public List<ProcessInstanceSummary> getProcessInstanceSummariesForOrganisation(
      UUID organisationId)
    throws DAOException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement =
          connection.prepareStatement(getProcessInstanceSummariesForOrganisationSQL))
    {
      statement.setObject(1, organisationId);

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
          "Failed to retrieve the summaries for the process instances for the organisation ("
          + organisationId + ") from the database", e);
    }
  }

  /**
   * Retrieve the summary for the process instance.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the process
   *           instance
   *
   * @return the summary for the process instance or <code>null</code> if the process definition
   *         could not be found
   *
   * @throws DAOException
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
      throw new DAOException("Failed to retrieve the summary for the process instance (" + id
          + ") from the database", e);
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
      throw new DAOException("Failed to retrieve the application data source"
          + " using the JNDI names (java:app/jdbc/ApplicationDataSource) and"
          + " (java:comp/env/jdbc/ApplicationDataSource)");
    }

    try
    {
      // Retrieve the database meta data
      String schemaSeparator;
      String idQuote;

      try (Connection connection = dataSource.getConnection())
      {
        DatabaseMetaData metaData = connection.getMetaData();

        // Retrieve the schema separator for the database
        schemaSeparator = metaData.getCatalogSeparator();

        if ((schemaSeparator == null) || (schemaSeparator.length() == 0))
        {
          schemaSeparator = ".";
        }

        // Retrieve the identifier enquoting string for the database
        idQuote = metaData.getIdentifierQuoteString();

        if ((idQuote == null) || (idQuote.length() == 0))
        {
          idQuote = "\"";
        }
      }

      // Determine the schema prefix
      String schemaPrefix = idQuote + DataAccessObject.DEFAULT_DATABASE_SCHEMA + idQuote
        + schemaSeparator;

      // Build the SQL statements for the DAO
      buildStatements(schemaPrefix);
    }
    catch (Throwable e)
    {
      throw new DAOException("Failed to initialise the " + getClass().getName()
          + " data access object: " + e.getMessage(), e);
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
   *
   * @throws DAOException
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
          throw new DAOException(
              "No results were returned as a result of executing the SQL statement ("
              + processDefinitionExistsSQL + ")");
        }
      }
    }
    catch (Throwable e)
    {
      throw new DAOException("Failed to check whether the process definition with ID (" + id
          + ") and version (" + version + ") exists in the database", e);
    }
  }

  /**
   * Check whether the process instance exists in the database.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the process
   *           instance
   *
   * @return <code>true</code> if the process instance exists or <code>false</code> otherwise
   *
   * @throws DAOException
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
          throw new DAOException(
              "No results were returned as a result of executing the SQL statement ("
              + processInstanceExistsSQL + ")");
        }
      }
    }
    catch (Throwable e)
    {
      throw new DAOException("Failed to check whether the process instance (" + id
          + ") exists in the database", e);
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
   *
   * @throws DAOException
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
      throw new DAOException("Failed to reset the locks for the process instances with status ("
          + status + ") that have been locked using the lock name (" + lockName + ")", e);
    }
  }

  /**
   * Unlock a locked process instance.
   *
   * @param id     the Universally Unique Identifier (UUID) used to uniquely identify the process
   *               instance
   * @param status the new status for the unlocked process instance
   *
   * @throws DAOException
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
        throw new DAOException(
            "No rows were affected as a result  of executing the SQL statement ("
            + unlockProcessInstanceSQL + ")");
      }
    }
    catch (Throwable e)
    {
      throw new DAOException("Failed to unlock and set the status for the process instance (" + id
          + ") to (" + status + ") in the database", e);
    }
  }

  /**
   * Update the state for process instance.
   *
   * @param id   the Universally Unique Identifier (UUID) used to uniquely identify the process
   *             instance
   * @param data the data giving the current execution state for the process instance
   *
   * @throws DAOException
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
        throw new DAOException("No rows were affected as a result of executing the SQL statement ("
            + updateProcessInstanceDataSQL + ")");
      }
    }
    catch (Throwable e)
    {
      throw new DAOException("Failed to update the data for the process instance (" + id
          + ") in the database", e);
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
    createProcessDefinitionSQL = "INSERT INTO " + schemaPrefix
        + "PROCESS_DEFINITIONS (ID, VERSION, ORGANISATION_ID, NAME, DATA) VALUES (?, ?, ?, ?, ?)";

    // createProcessInstanceSQL
    createProcessInstanceSQL = "INSERT INTO " + schemaPrefix
        + "PROCESS_INSTANCES (ID, DEFINITION_ID, DEFINITION_VERSION, DATA, STATUS, NEXT_EXECUTION)"
        + " VALUES (?, ?, ?, ?, ?, ?)";

    // deleteProcessDefinitionSQL
    deleteProcessDefinitionSQL = "DELETE FROM " + schemaPrefix
        + "PROCESS_DEFINITIONS PD WHERE PD.ID=?";

    // deleteProcessInstanceSQL
    deleteProcessInstanceSQL = "DELETE FROM " + schemaPrefix + "PROCESS_INSTANCES PI WHERE PI.ID=?";

    // getCurrentProcessDefinitionsForOrganisationSQL
    getCurrentProcessDefinitionsForOrganisationSQL =
      "SELECT PD.ID, PD.VERSION, PD.ORGANISATION_ID, PD.NAME, PD.DATA FROM " + schemaPrefix
      + "PROCESS_DEFINITIONS PD "
      + "INNER JOIN (SELECT ID AS CURRENT_ID, MAX(VERSION) AS CURRENT_VERSION FROM " + schemaPrefix
      + "PROCESS_DEFINITIONS WHERE ORGANISATION_ID=? GROUP BY CURRENT_ID) PDC"
      + " ON PD.ID = PDC.CURRENT_ID AND PD.VERSION = PDC.CURRENT_VERSION";

    // getCurrentProcessDefinitionSummariesForOrganisationSQL
    getCurrentProcessDefinitionSummariesForOrganisationSQL =
      "SELECT PD.ID, PD.VERSION, PD.ORGANISATION_ID, PD.NAME FROM " + schemaPrefix
      + "PROCESS_DEFINITIONS PD "
      + "INNER JOIN (SELECT ID AS CURRENT_ID, MAX(VERSION) AS CURRENT_VERSION FROM " + schemaPrefix
      + "PROCESS_DEFINITIONS WHERE ORGANISATION_ID=? GROUP BY CURRENT_ID) PDC"
      + " ON PD.ID = PDC.CURRENT_ID AND PD.VERSION = PDC.CURRENT_VERSION";

    // getNextProcessInstanceScheduledForExecutionSQL
    getNextProcessInstanceScheduledForExecutionSQL =
      "SELECT PI.ID, PI.DEFINITION_ID, PI.DEFINITION_VERSION, PI.DATA, PI.STATUS, "
      + "PI.NEXT_EXECUTION, PI.LOCK_NAME FROM " + schemaPrefix
      + "PROCESS_INSTANCES PI WHERE PI.STATUS=? AND PI.NEXT_EXECUTION <= ?"
      + " FETCH FIRST 1 ROWS ONLY FOR UPDATE";

    // getNumberOfProcessDefinitionsForOrganisationSQL
    getNumberOfProcessDefinitionsForOrganisationSQL = "SELECT COUNT(DISTINCT PD.ID) FROM "
        + schemaPrefix + "PROCESS_DEFINITIONS PD WHERE PD.ORGANISATION_ID=?";

    // getNumberOfProcessInstancesForOrganisationSQL
    getNumberOfProcessInstancesForOrganisationSQL = "SELECT COUNT(PI.ID)" + " FROM " + schemaPrefix
        + "PROCESS_INSTANCES PI, " + schemaPrefix
        + "PROCESS_DEFINITIONS PD WHERE PI.DEFINITION_ID = PD.ID"
        + " AND PI.DEFINITION_VERSION = PD.VERSION AND PD.ORGANISATION_ID = ?";

    // getProcessDefinitionByIdAndVersionSQL
    getProcessDefinitionByIdAndVersionSQL = "SELECT PD.ID, PD.VERSION, PD.ORGANISATION_ID, "
        + "PD.NAME, PD.DATA FROM " + schemaPrefix
        + "PROCESS_DEFINITIONS PD WHERE PD.ID=? AND PD.VERSION = ?";

    // getProcessDefinitionSummaryByIdAndVersionSQL
    getProcessDefinitionSummaryByIdAndVersionSQL = "SELECT PD.ID, PD.VERSION, PD.ORGANISATION_ID,"
        + " PD.NAME FROM " + schemaPrefix + "PROCESS_DEFINITIONS PD WHERE ID=? AND VERSION=?";

    // getProcessInstanceByIdSQL
    getProcessInstanceByIdSQL = "SELECT PI.ID, PI.DEFINITION_ID, PI.DEFINITION_VERSION, PI.DATA, "
        + "PI.STATUS, PI.NEXT_EXECUTION, PI.LOCK_NAME FROM " + schemaPrefix
        + "PROCESS_INSTANCES PI WHERE PI.ID=?";

    // getProcessInstanceSummariesForOrganisationSQL
    getProcessInstanceSummariesForOrganisationSQL = "SELECT PI.ID, PI.DEFINITION_ID,"
        + " PI.DEFINITION_VERSION, PD.NAME AS NAME, PI.STATUS,"
        + " PI.NEXT_EXECUTION, PI.LOCK_NAME FROM " + schemaPrefix + "PROCESS_INSTANCES PI, "
        + schemaPrefix + "PROCESS_DEFINITIONS PD WHERE PI.DEFINITION_ID = PD.ID"
        + " AND PI.DEFINITION_VERSION = PD.VERSION AND PD.ORGANISATION_ID = ?";

    // getProcessInstanceSummaryByIdSQL
    getProcessInstanceSummaryByIdSQL = "SELECT PI.ID, PI.DEFINITION_ID,"
        + " PI.DEFINITION_VERSION, PD.NAME AS NAME, PI.STATUS,"
        + " PI.NEXT_EXECUTION, PI.LOCK_NAME FROM " + schemaPrefix + "PROCESS_INSTANCES PI, "
        + schemaPrefix + "PROCESS_DEFINITIONS PD WHERE PI.DEFINITION_ID = PD.ID"
        + " AND PI.DEFINITION_VERSION = PD.VERSION AND PI.ID = ?";

    // lockProcesInstanceSQL
    lockProcessInstanceSQL = "UPDATE " + schemaPrefix + "PROCESS_INSTANCES PI"
        + " SET PI.STATUS=?, PI.LOCK_NAME=? WHERE PI.ID=?";

    // processDefinitionExistsSQL
    processDefinitionExistsSQL = "SELECT COUNT(PD.ID) FROM " + schemaPrefix
        + "PROCESS_DEFINITIONS PD WHERE PD.ID=? AND PD.VERSION=?";

    // processInstanceExistsSQL
    processInstanceExistsSQL = "SELECT COUNT(PI.ID) FROM " + schemaPrefix
        + "PROCESS_INSTANCES PI WHERE PI.ID=?";

    // resetProcessInstanceLocksSQL
    resetProcessInstanceLocksSQL = "UPDATE " + schemaPrefix + "PROCESS_INSTANCES PI"
        + " SET PI.STATUS=?, PI.LOCK_NAME=NULL WHERE PI.LOCK_NAME=? AND PI.STATUS=?";

    // unlockProcessInstanceSQL
    unlockProcessInstanceSQL = "UPDATE " + schemaPrefix + "PROCESS_INSTANCES PI"
        + " SET PI.STATUS=?, PI.LOCK_NAME=NULL WHERE PI.ID=?";

    // updateProcessInstanceDataSQL
    updateProcessInstanceDataSQL = "UPDATE " + schemaPrefix + "PROCESS_INSTANCES PI"
        + " SET PI.DATA=? WHERE PI.ID=?";
  }

  private ProcessDefinition getProcessDefinition(ResultSet rs)
    throws SQLException
  {
    return new ProcessDefinition((UUID)rs.getObject(1), rs.getInt(2), (UUID)rs.getObject(3), rs.getString(4),
        rs.getBytes(5));
  }

  private ProcessDefinitionSummary getProcessDefinitionSummary(ResultSet rs)
    throws SQLException
  {
    return new ProcessDefinitionSummary((UUID)rs.getObject(1), rs.getInt(2), (UUID)rs.getObject(3),
        rs.getString(4));
  }

  private ProcessInstance getProcessInstance(ResultSet rs)
    throws SQLException
  {
    return new ProcessInstance((UUID)rs.getObject(1), (UUID)rs.getObject(2), rs.getInt(3), rs.getBytes(4),
        ProcessInstance.Status.fromCode(rs.getInt(5)), rs.getTimestamp(6), rs.getString(7));
  }

  private ProcessInstanceSummary getProcessInstanceSummary(ResultSet rs)
    throws SQLException
  {
    return new ProcessInstanceSummary(rs.getString(1), rs.getString(2), rs.getInt(3),
        rs.getString(4), ProcessInstance.Status.fromCode(rs.getInt(5)), rs.getTimestamp(6),
        rs.getString(7));
  }
}

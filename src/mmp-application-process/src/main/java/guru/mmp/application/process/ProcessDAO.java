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

import guru.mmp.application.persistence.DAOException;
import guru.mmp.application.persistence.DataAccessObject;

//~--- JDK imports ------------------------------------------------------------

import java.sql.*;

import java.util.ArrayList;
import java.util.List;

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
  private String createProcessDefinitionSQL;

  /** The data source used to provide connections to the database. */
  private DataSource dataSource;
  private String deleteProcessDefinitionSQL;
  private String getNumberOfProcessDefinitionsForOrganisationSQL;
  private String getProcessDefinitionByIdSQL;
  private String getProcessDefinitionSummariesForOrganisationSQL;
  private String getProcessDefinitionSummaryByIdSQL;
  private String getProcessDefinitionsForOrganisationSQL;
  private String processDefinitionExistsSQL;
  private String updateProcessDefinitionSQL;

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
          + " data access object:" + " Failed to lookup the data source (" + dataSourceJndiName
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
      statement.setString(1, processDefinition.getId());
      statement.setString(2, processDefinition.getOrganisation());
      statement.setString(3, processDefinition.getName());
      statement.setBytes(4, processDefinition.getData());

      if (statement.executeUpdate() != 1)
      {
        throw new DAOException("Failed to add the process definition to the database: "
            + "No rows were affected as a result of executing the SQL statement ("
            + createProcessDefinitionSQL + ")");
      }
    }
    catch (Throwable e)
    {
      throw new DAOException("Failed to add the process definition (" + processDefinition.getName()
          + ") with ID (" + processDefinition.getId() + ") to the database", e);
    }
  }

  /**
   * Delete the existing process definition.
   *
   * @param id the ID uniquely identifying the process definition
   *
   * @throws DAOException
   */
  public void deleteProcessDefinition(String id)
    throws DAOException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(deleteProcessDefinitionSQL))
    {
      statement.setString(1, id);

      if (statement.executeUpdate() != 1)
      {
        throw new DAOException("Failed to delete the process definition (" + id
            + ") in the database:"
            + " No rows were affected as a result of executing the SQL statement ("
            + deleteProcessDefinitionSQL + ")");
      }
    }
    catch (DAOException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new DAOException("Failed to delete the process definition (" + id
          + ") in the database", e);
    }
  }

  /**
   * Returns the number of process definitions associated with the organisation identified by the
   * specified organisation code.
   *
   * @param organisation the organisation code identifying the organisation
   *
   * @return the number of process definitions associated with the organisation identified by the
   *         specified organisation code
   *
   * @throws DAOException
   */
  public int getNumberOfProcessDefinitionsForOrganisation(String organisation)
    throws DAOException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement =
          connection.prepareStatement(getNumberOfProcessDefinitionsForOrganisationSQL))
    {
      statement.setString(1, organisation);

      try (ResultSet rs = statement.executeQuery())
      {
        if (rs.next())
        {
          return rs.getInt(1);
        }
        else
        {
          throw new DAOException(
              "Failed to retrieve the number of process definitions for the organisation ("
              + organisation + ") in the database:"
              + " No results were returned as a result of executing the SQL statement ("
              + getNumberOfProcessDefinitionsForOrganisationSQL + ")");
        }
      }
    }
    catch (DAOException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new DAOException(
          "Failed to retrieve the number of process definitions for the organisation ("
          + organisation + ") from the database", e);
    }
  }

  /**
   * Retrieve the process definition with the specified ID.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the
   *           process definition
   *
   * @return the process definition with the specified ID or <code>null</code> if the process
   *         definition could not be found
   *
   * @throws DAOException
   */
  public ProcessDefinition getProcessDefinition(String id)
    throws DAOException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(getProcessDefinitionByIdSQL))
    {
      statement.setString(1, id);

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
    catch (DAOException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new DAOException("Failed to retrieve the process definition (" + id
          + ") from the database", e);
    }
  }

  /**
   * Returns the summaries for all the process definitions associated with the organisation
   * identified by the specified organisation code.
   *
   * @param organisation the organisation code identifying the organisation
   *
   * @return the summaries for all the process definitions associated with the organisation
   *         identified by the specified organisation code
   *
   * @throws DAOException
   */
  public List<ProcessDefinitionSummary> getProcessDefinitionSummariesForOrganisation(
      String organisation)
    throws DAOException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement =
          connection.prepareStatement(getProcessDefinitionSummariesForOrganisationSQL))
    {
      statement.setString(1, organisation);

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
    catch (DAOException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new DAOException("Failed to retrieve the process definitions for the organisation ("
          + organisation + ") from the database", e);
    }
  }

  /**
   * Retrieve the summary for the process definition with the specified ID.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the
   *           process definition
   *
   * @return the summary for the process definition with the specified ID or <code>null</code> if
   *         the process definition could not be found
   *
   * @throws DAOException
   */
  public ProcessDefinitionSummary getProcessDefinitionSummary(String id)
    throws DAOException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(getProcessDefinitionSummaryByIdSQL))
    {
      statement.setString(1, id);

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
    catch (DAOException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new DAOException("Failed to retrieve the summary for the process definition (" + id
          + ") from the database", e);
    }
  }

  /**
   * Returns all the process definitions associated with the organisation identified by the
   * specified organisation code.
   *
   * @param organisation the organisation code identifying the organisation
   *
   * @return all the process definitions associated with the organisation identified by the
   *         specified organisation code
   *
   * @throws DAOException
   */
  public List<ProcessDefinition> getProcessDefinitionsForOrganisation(String organisation)
    throws DAOException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement =
          connection.prepareStatement(getProcessDefinitionsForOrganisationSQL))
    {
      statement.setString(1, organisation);

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
    catch (DAOException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new DAOException("Failed to retrieve the process definitions for the organisation ("
          + organisation + ") from the database", e);
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
      String schemaPrefix = idQuote + DataAccessObject.DEFAULT_APPLICATION_DATABASE_SCHEMA
        + idQuote + schemaSeparator;

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
   * Check whether the process definition with the specified ID exists in the database.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the process
   *           definition
   *
   * @return <code>true</code> if the process definition exists or <code>false</code> otherwise
   *
   * @throws DAOException
   */
  public boolean processDefinitionExists(String id)
    throws DAOException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(processDefinitionExistsSQL))
    {
      statement.setString(1, id);

      try (ResultSet rs = statement.executeQuery())
      {
        if (rs.next())
        {
          return (rs.getInt(1) > 0);
        }
        else
        {
          throw new DAOException("Failed to check whether the process definition (" + id
              + ") exists in the database:"
              + " No results were returned as a result of executing the SQL statement ("
              + processDefinitionExistsSQL + ")");
        }
      }
    }
    catch (DAOException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new DAOException("Failed to check whether the process definition (" + id
          + ") exists in the database", e);
    }
  }

  /**
   * Update the process definition.
   *
   * @param processDefinition the <code>ProcessDefinition</code> instance containing the updated
   *                         information for the process definition
   * @param updatedBy        the username identifying the user that updated the process definition
   *
   * @return the updated process definition
   *
   * @throws DAOException
   */
  public ProcessDefinition updateProcessDefinition(ProcessDefinition processDefinition,
      String updatedBy)
    throws DAOException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(updateProcessDefinitionSQL))
    {
      statement.setString(1, processDefinition.getOrganisation());
      statement.setString(2, processDefinition.getName());
      statement.setBytes(3, processDefinition.getData());
      statement.setString(4, processDefinition.getId());

      if (statement.executeUpdate() != 1)
      {
        throw new DAOException("Failed to update the process definition in the database: "
            + "No rows were affected as a result of executing the SQL statement ("
            + updateProcessDefinitionSQL + ")");
      }

      return processDefinition;
    }
    catch (Throwable e)
    {
      throw new DAOException("Failed to update the process definition ("
          + processDefinition.getId() + ") in the database", e);
    }
  }

  /**
   * Generate the SQL statements for the DAO.
   *
   * @param schemaPrefix the schema prefix to append to database objects reference by the DAO
   *
   * @throws SQLException if a database error occurs
   */
  protected void buildStatements(String schemaPrefix)
    throws SQLException
  {
    // createProcessDefinitionSQL
    createProcessDefinitionSQL = "INSERT INTO " + schemaPrefix
        + "PROCESS_DEFINITIONS (ID, ORGANISATION, NAME, DATA) VALUES (?, ?, ?, ?)";

    // deleteProcessDefinitionSQL
    deleteProcessDefinitionSQL = "DELETE FROM " + schemaPrefix + "PROCESS_DEFINITIONS WHERE ID=?";

    // getNumberOfProcessDefinitionsForOrganisationSQL
    getNumberOfProcessDefinitionsForOrganisationSQL = "SELECT COUNT(ID)" + " FROM " + schemaPrefix
        + "PROCESS_DEFINITIONS WHERE ORGANISATION=?";

    // getProcessDefinitionByIdSQL
    getProcessDefinitionByIdSQL = "SELECT ID, ORGANISATION, NAME, DATA FROM " + schemaPrefix
        + "PROCESS_DEFINITIONS WHERE ID=?";

    // getProcessDefinitionSummariesForOrganisationSQL
    getProcessDefinitionSummariesForOrganisationSQL = "SELECT ID, ORGANISATION, NAME FROM "
        + schemaPrefix + "PROCESS_DEFINITIONS WHERE ORGANISATION=?";

    // getProcessDefinitionSummaryByIdSQL
    getProcessDefinitionSummaryByIdSQL = "SELECT ID, ORGANISATION, NAME FROM " + schemaPrefix
        + "PROCESS_DEFINITIONS WHERE ID=?";

    // getProcessDefinitionsForOrganisationSQL
    getProcessDefinitionsForOrganisationSQL = "SELECT ID, ORGANISATION, NAME, DATA FROM "
        + schemaPrefix + "PROCESS_DEFINITIONS WHERE ORGANISATION=?";

    // processDefinitionExistsSQL
    processDefinitionExistsSQL = "SELECT COUNT(ID) FROM " + schemaPrefix
        + "PROCESS_DEFINITIONS WHERE ID=?";

    // updateProcessDefinitionSQL
    updateProcessDefinitionSQL = "UPDATE " + schemaPrefix
        + "PROCESS_DEFINITIONS SET ORGANISATION=?, NAME=?, DATA=? WHERE ID=?";
  }

  private ProcessDefinition getProcessDefinition(ResultSet rs)
    throws SQLException
  {
    return new ProcessDefinition(rs.getString(1), rs.getString(2), rs.getString(3), rs.getBytes(4));
  }

  private ProcessDefinitionSummary getProcessDefinitionSummary(ResultSet rs)
    throws SQLException
  {
    return new ProcessDefinitionSummary(rs.getString(1), rs.getString(2), rs.getString(3));
  }
}

/*
 * Copyright 2014 Marcus Portmann
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

package guru.mmp.application.reporting;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.persistence.DAOException;
import guru.mmp.application.persistence.DataAccessObject;
import guru.mmp.common.persistence.DAOUtil;

//~--- JDK imports ------------------------------------------------------------

import java.sql.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;

import javax.naming.InitialContext;

import javax.sql.DataSource;

/**
 * The <code>ReportingDAO</code> class implements the persistence operations for the
 * reporting infrastructure.
 *
 * @author Marcus Portmann
 */
@ApplicationScoped
@Default
public class ReportingDAO
  implements IReportingDAO
{
  private String createReportDefinitionSQL;

  /** The data source used to provide connections to the database. */
  private DataSource dataSource;
  private String deleteReportDefinitionSQL;
  private String getNumberOfReportDefinitionsForOrganisationSQL;
  private String getReportDefinitionByIdSQL;
  private String getReportDefinitionsForOrganisationSQL;
  private String reportDefinitionExistsSQL;
  private String updateReportDefinitionSQL;

  /**
   * Constructs a new <code>ReportingDAO</code>.
   */
  public ReportingDAO() {}

  /**
   * Constructs a new <code>ReportingDAO</code>.
   *
   * @param dataSource the data source to use
   *
   * @throws DAOException
   */
  public ReportingDAO(DataSource dataSource)
    throws DAOException
  {
    if (dataSource == null)
    {
      throw new DAOException("Failed to initialise the reporting DAO:"
          + " The specified data source is NULL");
    }

    this.dataSource = dataSource;

    init();
  }

  /**
   * Constructs a new <code>ReportingDAO</code>.
   *
   * @param dataSourceJndiName the JNDI name of the data source used to access the database
   *
   * @throws DAOException
   */
  public ReportingDAO(String dataSourceJndiName)
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
      throw new DAOException("Failed to initialise the reporting DAO:"
          + " Failed to lookup the data source (" + dataSourceJndiName + ") using JNDI: "
          + e.getMessage(), e);
    }

    init();
  }

  /**
   * Create the new report definition.
   *
   * @param reportDefinition the <code>ReportDefinition</code> instance containing the information
   *                         for the new report definition
   *
   * @throws DAOException
   */
  public void createReportDefinition(ReportDefinition reportDefinition)
    throws DAOException
  {
    Connection connection = null;
    PreparedStatement statement = null;

    try
    {
      connection = dataSource.getConnection();

      statement = connection.prepareStatement(createReportDefinitionSQL);

      statement.setString(1, reportDefinition.getId());
      statement.setString(2, reportDefinition.getOrganisation());
      statement.setString(3, reportDefinition.getName());
      statement.setBytes(4, reportDefinition.getTemplate());

      if (statement.executeUpdate() != 1)
      {
        throw new DAOException("Failed to add the report definition to the database: "
            + "No rows were affected as a result of executing the SQL statement ("
            + createReportDefinitionSQL + ")");
      }
    }
    catch (Throwable e)
    {
      throw new DAOException("Failed to add the report definition (" + reportDefinition.getName()
          + ") with ID (" + reportDefinition.getId() + ") to the database", e);
    }
    finally
    {
      DAOUtil.close(statement);
      DAOUtil.close(connection);
    }
  }

  /**
   * Delete the existing report definition.
   *
   * @param id the ID uniquely identifying the report definition
   *
   * @throws DAOException
   */
  public void deleteReportDefinition(String id)
    throws DAOException
  {
    Connection connection = null;
    PreparedStatement statement = null;

    try
    {
      connection = dataSource.getConnection();

      statement = connection.prepareStatement(deleteReportDefinitionSQL);

      statement.setString(1, id);

      if (statement.executeUpdate() != 1)
      {
        throw new DAOException("Failed to delete the report definition (" + id
            + ") in the database:"
            + " No rows were affected as a result of executing the SQL statement ("
            + deleteReportDefinitionSQL + ")");
      }
    }
    catch (DAOException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new DAOException("Failed to delete the report definition (" + id + ") in the database",
          e);
    }
    finally
    {
      DAOUtil.close(statement);
      DAOUtil.close(connection);
    }
  }

  /**
   * Returns the number of report definitions associated with the organisation identified by the
   * specified organisation code.
   *
   * @param organisation the organisation code identifying the organisation
   *
   * @return the number of report definitions associated with the organisation identified by the
   *         specified organisation code
   *
   * @throws DAOException
   */
  public int getNumberOfReportDefinitionsForOrganisation(String organisation)
    throws DAOException
  {
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet rs = null;

    try
    {
      connection = dataSource.getConnection();

      statement = connection.prepareStatement(getNumberOfReportDefinitionsForOrganisationSQL);

      statement.setString(1, organisation);

      rs = statement.executeQuery();

      if (rs.next())
      {
        return rs.getInt(1);
      }
      else
      {
        throw new DAOException(
            "Failed to retrieve the number of report definitions for the organisation ("
            + organisation + ") in the database:"
            + " No results were returned as a result of executing the SQL statement ("
            + getNumberOfReportDefinitionsForOrganisationSQL + ")");
      }
    }
    catch (DAOException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new DAOException(
          "Failed to retrieve the number of report definitions for the organisation ("
          + organisation + ") from the database", e);
    }
    finally
    {
      DAOUtil.close(rs);
      DAOUtil.close(statement);
      DAOUtil.close(connection);
    }
  }

  /**
   * Retrieve the report definition with the specified ID.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the
   *           report definition
   *
   * @return the report definition with the specified ID or <code>null</code> if the report
   *         definition could not be found
   *
   * @throws DAOException
   */
  public ReportDefinition getReportDefinition(String id)
    throws DAOException
  {
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet rs = null;

    try
    {
      connection = dataSource.getConnection();

      statement = connection.prepareStatement(getReportDefinitionByIdSQL);

      statement.setString(1, id);

      rs = statement.executeQuery();

      if (rs.next())
      {
        return getReportDefinition(rs);
      }
      else
      {
        return null;
      }
    }
    catch (DAOException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new DAOException("Failed to retrieve the report definition (" + id
          + ") from the database", e);
    }
    finally
    {
      DAOUtil.close(rs);
      DAOUtil.close(statement);
      DAOUtil.close(connection);
    }
  }

  /**
   * Returns all the report definitions associated with the organisation identified by the specified
   * organisation code.
   *
   * @param organisation the organisation code identifying the organisation
   *
   * @return all the report definitions associated with the organisation identified by the specified
   *         organisation code
   *
   * @throws DAOException
   */
  public List<ReportDefinition> getReportDefinitionsForOrganisation(String organisation)
    throws DAOException
  {
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet rs = null;

    try
    {
      connection = dataSource.getConnection();

      statement = connection.prepareStatement(getReportDefinitionsForOrganisationSQL);

      statement.setString(1, organisation);

      rs = statement.executeQuery();

      List<ReportDefinition> reportDefinitions = new ArrayList<>();

      while (rs.next())
      {
        reportDefinitions.add(getReportDefinition(rs));
      }

      return reportDefinitions;
    }
    catch (DAOException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new DAOException("Failed to retrieve the report definitions for the organisation ("
          + organisation + ") from the database", e);
    }
    finally
    {
      DAOUtil.close(rs);
      DAOUtil.close(statement);
      DAOUtil.close(connection);
    }
  }

  /**
   * Initialise the <code>DataAccessObject</code>.
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

    Connection connection = null;

    try
    {
      // Retrieve the database meta data
      String schemaSeparator = ".";
      String idQuote = "\"";

      try
      {
        connection = dataSource.getConnection();

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
      finally
      {
        DAOUtil.close(connection);
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
   * Check whether the report definition with the specified ID exists in the database.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the report
   *           definition
   *
   * @return <code>true</code> if the report definition exists or <code>false</code> otherwise
   *
   * @throws DAOException
   */
  public boolean reportDefinitionExists(String id)
    throws DAOException
  {
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet rs = null;

    try
    {
      connection = dataSource.getConnection();

      statement = connection.prepareStatement(reportDefinitionExistsSQL);

      statement.setString(1, id);

      rs = statement.executeQuery();

      if (rs.next())
      {
        return (rs.getInt(1) > 0);
      }
      else
      {
        throw new DAOException("Failed to check whether the report definition (" + id
            + ") exists in the database:"
            + " No results were returned as a result of executing the SQL statement ("
            + reportDefinitionExistsSQL + ")");
      }
    }
    catch (DAOException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new DAOException("Failed to check whether the report definition (" + id
          + ") exists in the database", e);
    }
    finally
    {
      DAOUtil.close(rs);
      DAOUtil.close(statement);
      DAOUtil.close(connection);
    }
  }

  /**
   * Update the report definition.
   *
   * @param reportDefinition the <code>ReportDefinition</code> instance containing the updated
   *                         information for the report definition
   * @param updatedBy        the username identifying the user that updated the report definition
   *
   * @return the updated report definition
   *
   * @throws DAOException
   */
  public ReportDefinition updateReportDefinition(ReportDefinition reportDefinition,
      String updatedBy)
    throws DAOException
  {
    Connection connection = null;
    PreparedStatement statement = null;

    try
    {
      connection = dataSource.getConnection();

      statement = connection.prepareStatement(updateReportDefinitionSQL);

      Date updated = new Date();

      statement.setString(1, reportDefinition.getOrganisation());
      statement.setString(2, reportDefinition.getName());
      statement.setBytes(3, reportDefinition.getTemplate());
      statement.setString(4, reportDefinition.getId());

      if (statement.executeUpdate() != 1)
      {
        throw new DAOException("Failed to update the report definition in the database: "
            + "No rows were affected as a result of executing the SQL statement ("
            + updateReportDefinitionSQL + ")");
      }

      return reportDefinition;
    }
    catch (Throwable e)
    {
      throw new DAOException("Failed to update the report definition (" + reportDefinition.getId()
          + ") in the database", e);
    }
    finally
    {
      DAOUtil.close(statement);
      DAOUtil.close(connection);
    }
  }

  /**
   * This method must be implemented by all classes derived from <code>DataAccessObject</code> and
   * should contain the code to generate the SQL statements for the DAO.
   *
   * @param schemaPrefix    the schema prefix to append to database objects reference by the DAO
   *
   * @throws SQLException if a database error occurs
   */
  protected void buildStatements(String schemaPrefix)
    throws SQLException
  {
    // createReportDefinitionSQL
    createReportDefinitionSQL = "INSERT INTO " + schemaPrefix
        + "REPORT_DEFINITIONS (ID, ORGANISATION, NAME, TEMPLATE) VALUES (?, ?, ?, ?)";

    // deleteReportDefinitionSQL
    deleteReportDefinitionSQL = "DELETE FROM " + schemaPrefix + "REPORT_DEFINITIONS WHERE ID=?";

    // getNumberOfReportDefinitionsForOrganisationSQL
    getNumberOfReportDefinitionsForOrganisationSQL = "SELECT COUNT(ID)" + " FROM " + schemaPrefix
        + "REPORT_DEFINITIONS WHERE ORGANISATION=?";

    // getReportDefinitionByIdSQL
    getReportDefinitionByIdSQL = "SELECT ID, ORGANISATION, NAME, TEMPLATE FROM " + schemaPrefix
        + "REPORT_DEFINITIONS WHERE ID=?";

    // getReportDefinitionsForOrganisationSQL
    getReportDefinitionsForOrganisationSQL = "SELECT ID, ORGANISATION, NAME, TEMPLATE FROM "
        + schemaPrefix + "REPORT_DEFINITIONS WHERE ORGANISATION=?";

    // reportDefinitionExistsSQL
    reportDefinitionExistsSQL = "SELECT COUNT(ID) FROM " + schemaPrefix
        + "REPORT_DEFINITIONS WHERE ID=?";

    // updateReportDefinitionSQL
    updateReportDefinitionSQL = "UPDATE " + schemaPrefix
        + "REPORT_DEFINITIONS SET ORGANISATION=?, NAME=?, TEMPLATE=? WHERE ID=?";
  }

  private ReportDefinition getReportDefinition(ResultSet rs)
    throws SQLException
  {
    return new ReportDefinition(rs.getString(1), rs.getString(2), rs.getString(3), rs.getBytes(4));
  }
}

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

package guru.mmp.application.reporting;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.common.persistence.DAOException;
import guru.mmp.common.persistence.DataAccessObject;

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

  /**
   * The data source used to provide connections to the application database.
   */
  private DataSource dataSource;
  private String deleteReportDefinitionSQL;
  private String getNumberOfReportDefinitionsForOrganisationSQL;
  private String getReportDefinitionByIdSQL;
  private String getReportDefinitionSummariesForOrganisationSQL;
  private String getReportDefinitionSummaryByIdSQL;
  private String getReportDefinitionsForOrganisationSQL;
  private String reportDefinitionExistsSQL;
  private String updateReportDefinitionSQL;

  /**
   * Constructs a new <code>ReportingDAO</code>.
   */
  public ReportingDAO() {}

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
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(createReportDefinitionSQL))
    {
      statement.setObject(1, reportDefinition.getId());
      statement.setObject(2, reportDefinition.getOrganisationId());
      statement.setString(3, reportDefinition.getName());
      statement.setBytes(4, reportDefinition.getTemplate());

      if (statement.executeUpdate() != 1)
      {
        throw new DAOException("No rows were affected as a result of executing the SQL statement ("
            + createReportDefinitionSQL + ")");
      }
    }
    catch (Throwable e)
    {
      throw new DAOException("Failed to add the report definition (" + reportDefinition.getName()
          + ") with ID (" + reportDefinition.getId() + ") to the database", e);
    }
  }

  /**
   * Delete the existing report definition.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the report
   *           definition
   *
   * @throws DAOException
   */
  public void deleteReportDefinition(UUID id)
    throws DAOException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(deleteReportDefinitionSQL))
    {
      statement.setObject(1, id);

      if (statement.executeUpdate() != 1)
      {
        throw new DAOException("No rows were affected as a result of executing the SQL statement ("
            + deleteReportDefinitionSQL + ")");
      }
    }
    catch (Throwable e)
    {
      throw new DAOException("Failed to delete the report definition (" + id + ") in the database",
          e);
    }
  }

  /**
   * Returns the data source used to provide connections to the application database.
   *
   * @return the data source used to provide connections to the application database
   */
  public DataSource getDataSource()
  {
    return dataSource;
  }

  /**
   * Returns the number of report definitions for the organisation.
   *
   * @param organisationId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                       organisation
   *
   * @return the number of report definitions for the organisation
   *
   * @throws DAOException
   */
  public int getNumberOfReportDefinitionsForOrganisation(UUID organisationId)
    throws DAOException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement =
          connection.prepareStatement(getNumberOfReportDefinitionsForOrganisationSQL))
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
              + getNumberOfReportDefinitionsForOrganisationSQL + ")");
        }
      }
    }
    catch (Throwable e)
    {
      throw new DAOException(
          "Failed to retrieve the number of report definitions for the organisation ("
          + organisationId + ") from the database", e);
    }
  }

  /**
   * Retrieve the report definition.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the
   *           report definition
   *
   * @return the report definition or <code>null</code> if the report
   *         definition could not be found
   *
   * @throws DAOException
   */
  public ReportDefinition getReportDefinition(UUID id)
    throws DAOException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(getReportDefinitionByIdSQL))
    {
      statement.setObject(1, id);

      try (ResultSet rs = statement.executeQuery())
      {
        if (rs.next())
        {
          return getReportDefinition(rs);
        }
        else
        {
          return null;
        }
      }
    }
    catch (Throwable e)
    {
      throw new DAOException("Failed to retrieve the report definition (" + id
          + ") from the database", e);
    }
  }

  /**
   * Returns the summaries for all the report definitions for the organisation.
   *
   * @param organisationId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                       organisation
   *
   * @return the summaries for all the report definitions for the organisation
   *
   * @throws DAOException
   */
  public List<ReportDefinitionSummary> getReportDefinitionSummariesForOrganisation(
      UUID organisationId)
    throws DAOException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement =
          connection.prepareStatement(getReportDefinitionSummariesForOrganisationSQL))
    {
      statement.setObject(1, organisationId);

      List<ReportDefinitionSummary> reportDefinitionSummaries = new ArrayList<>();

      try (ResultSet rs = statement.executeQuery())
      {
        while (rs.next())
        {
          reportDefinitionSummaries.add(getReportDefinitionSummary(rs));
        }
      }

      return reportDefinitionSummaries;
    }
    catch (Throwable e)
    {
      throw new DAOException(
          "Failed to retrieve the summaries for the report definitions for the organisation ("
          + organisationId + ") from the database", e);
    }
  }

  /**
   * Retrieve the summary for the report definition.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the report
   *           definition
   *
   * @return the summary for the report definition or <code>null</code> if the report definition
   *         could not be found
   *
   * @throws DAOException
   */
  public ReportDefinitionSummary getReportDefinitionSummary(UUID id)
    throws DAOException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(getReportDefinitionSummaryByIdSQL))
    {
      statement.setObject(1, id);

      try (ResultSet rs = statement.executeQuery())
      {
        if (rs.next())
        {
          return getReportDefinitionSummary(rs);
        }
        else
        {
          return null;
        }
      }
    }
    catch (Throwable e)
    {
      throw new DAOException("Failed to retrieve the summary for the report definition (" + id
          + ") from the database", e);
    }
  }

  /**
   * Returns all the report definitions for the organisation.
   *
   * @param organisationId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                       organisation
   *
   * @return all the report definitions for the organisation
   *
   * @throws DAOException
   */
  public List<ReportDefinition> getReportDefinitionsForOrganisation(UUID organisationId)
    throws DAOException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement =
          connection.prepareStatement(getReportDefinitionsForOrganisationSQL))
    {
      statement.setObject(1, organisationId);

      List<ReportDefinition> reportDefinitions = new ArrayList<>();

      try (ResultSet rs = statement.executeQuery())
      {
        while (rs.next())
        {
          reportDefinitions.add(getReportDefinition(rs));
        }
      }

      return reportDefinitions;
    }
    catch (Throwable e)
    {
      throw new DAOException("Failed to retrieve the report definitions for the organisation ("
          + organisationId + ") from the database", e);
    }
  }

  /**
   * Initialise the <code>ReportingDAO</code> instance.
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
   * Check whether the report definition exists in the database.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the report
   *           definition
   *
   * @return <code>true</code> if the report definition exists or <code>false</code> otherwise
   *
   * @throws DAOException
   */
  public boolean reportDefinitionExists(UUID id)
    throws DAOException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(reportDefinitionExistsSQL))
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
              + reportDefinitionExistsSQL + ")");
        }
      }
    }
    catch (Throwable e)
    {
      throw new DAOException("Failed to check whether the report definition (" + id
          + ") exists in the database", e);
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
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(updateReportDefinitionSQL))
    {
      statement.setString(1, reportDefinition.getName());
      statement.setBytes(2, reportDefinition.getTemplate());
      statement.setObject(3, reportDefinition.getId());

      if (statement.executeUpdate() != 1)
      {
        throw new DAOException("No rows were affected as a result of executing the SQL statement ("
            + updateReportDefinitionSQL + ")");
      }

      return reportDefinition;
    }
    catch (Throwable e)
    {
      throw new DAOException("Failed to update the report definition (" + reportDefinition.getId()
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
    // createReportDefinitionSQL
    createReportDefinitionSQL = "INSERT INTO " + schemaPrefix
        + "REPORT_DEFINITIONS (ID, ORGANISATION_ID, NAME, TEMPLATE) VALUES (?, ?, ?, ?)";

    // deleteReportDefinitionSQL
    deleteReportDefinitionSQL = "DELETE FROM " + schemaPrefix
        + "REPORT_DEFINITIONS RD WHERE RD.ID=?";

    // getNumberOfReportDefinitionsForOrganisationSQL
    getNumberOfReportDefinitionsForOrganisationSQL = "SELECT COUNT(RD.ID)" + " FROM "
        + schemaPrefix + "REPORT_DEFINITIONS RD WHERE RD.ORGANISATION_ID=?";

    // getReportDefinitionByIdSQL
    getReportDefinitionByIdSQL = "SELECT RD.ID, RD.ORGANISATION_ID, RD.NAME, RD.TEMPLATE FROM "
        + schemaPrefix + "REPORT_DEFINITIONS RD WHERE RD.ID=?";

    // getReportDefinitionSummariesForOrganisationSQL
    getReportDefinitionSummariesForOrganisationSQL = "SELECT RD.ID, RD.ORGANISATION_ID, RD.NAME"
        + " FROM " + schemaPrefix + "REPORT_DEFINITIONS RD WHERE RD.ORGANISATION=?";

    // getReportDefinitionSummaryByIdSQL
    getReportDefinitionSummaryByIdSQL = "SELECT RD.ID, RD.ORGANISATION_ID, RD.NAME FROM "
        + schemaPrefix + "REPORT_DEFINITIONS RD WHERE RD.ID=?";

    // getReportDefinitionsForOrganisationSQL
    getReportDefinitionsForOrganisationSQL =
      "SELECT RD.ID, RD.ORGANISATION_ID, RD.NAME, RD.TEMPLATE FROM " + schemaPrefix
      + "REPORT_DEFINITIONS RD WHERE RD.ORGANISATION_ID=?";

    // reportDefinitionExistsSQL
    reportDefinitionExistsSQL = "SELECT COUNT(RD.ID) FROM " + schemaPrefix
        + "REPORT_DEFINITIONS RD WHERE RD.ID=?";

    // updateReportDefinitionSQL
    updateReportDefinitionSQL = "UPDATE " + schemaPrefix
        + "REPORT_DEFINITIONS RD SET RD.NAME=?, RD.TEMPLATE=? WHERE RD.ID=?";
  }

  private ReportDefinition getReportDefinition(ResultSet rs)
    throws SQLException
  {
    return new ReportDefinition((UUID) rs.getObject(1), (UUID) rs.getObject(2), rs.getString(3),
        rs.getBytes(4));
  }

  private ReportDefinitionSummary getReportDefinitionSummary(ResultSet rs)
    throws SQLException
  {
    return new ReportDefinitionSummary(rs.getString(1), rs.getString(2), rs.getString(3));
  }
}

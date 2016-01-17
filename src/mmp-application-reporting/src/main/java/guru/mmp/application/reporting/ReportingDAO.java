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

package guru.mmp.application.reporting;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.common.persistence.DAOException;
import guru.mmp.common.persistence.DAOUtil;
import guru.mmp.common.persistence.DataAccessObject;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//~--- JDK imports ------------------------------------------------------------

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
  private String getNumberOfReportDefinitionsSQL;
  private String getReportDefinitionByIdSQL;
  private String getReportDefinitionSummariesSQL;
  private String getReportDefinitionSummaryByIdSQL;
  private String getReportDefinitionsSQL;
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
      statement.setString(2, reportDefinition.getName());
      statement.setBytes(3, reportDefinition.getTemplate());

      if (statement.executeUpdate() != 1)
      {
        throw new DAOException(String.format(
            "No rows were affected as a result of executing the SQL statement (%s)",
            createReportDefinitionSQL));
      }
    }
    catch (Throwable e)
    {
      throw new DAOException(String.format(
          "Failed to add the report definition (%s) with ID (%s) to the database",
          reportDefinition.getName(), reportDefinition.getId()), e);
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
        throw new DAOException(String.format(
            "No rows were affected as a result of executing the SQL statement (%s)",
            deleteReportDefinitionSQL));
      }
    }
    catch (Throwable e)
    {
      throw new DAOException(String.format(
          "Failed to delete the report definition (%s) in the database", id), e);
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
   * Returns the number of report definitions.
   *
   * @return the number of report definitions
   *
   * @throws DAOException
   */
  public int getNumberOfReportDefinitions()
    throws DAOException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(getNumberOfReportDefinitionsSQL))
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
              getNumberOfReportDefinitionsSQL));
        }
      }
    }
    catch (Throwable e)
    {
      throw new DAOException(
          "Failed to retrieve the number of report definitions from the database", e);
    }
  }

  /**
   * Retrieve the report definition.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the
   *           report definition
   *
   * @return the report definition or <code>null</code> if the report
   * definition could not be found
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
      throw new DAOException(String.format(
          "Failed to retrieve the report definition (%s) from the database", id), e);
    }
  }

  /**
   * Returns the summaries for all the report definitions.
   *
   * @return the summaries for all the report definitions
   *
   * @throws DAOException
   */
  public List<ReportDefinitionSummary> getReportDefinitionSummaries()
    throws DAOException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(getReportDefinitionSummariesSQL))
    {
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
          "Failed to retrieve the summaries for the report definitions from the database", e);
    }
  }

  /**
   * Retrieve the summary for the report definition.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the report
   *           definition
   *
   * @return the summary for the report definition or <code>null</code> if the report definition
   * could not be found
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
      throw new DAOException(String.format(
          "Failed to retrieve the summary for the report definition (%s) from the database", id),
          e);
    }
  }

  /**
   * Returns all the report definitions.
   *
   * @return all the report definitions
   *
   * @throws DAOException
   */
  public List<ReportDefinition> getReportDefinitions()
    throws DAOException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(getReportDefinitionsSQL))
    {
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
      throw new DAOException("Failed to retrieve the report definitions from the database", e);
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
          throw new DAOException(String.format(
              "No results were returned as a result of executing the SQL statement (%s)",
              reportDefinitionExistsSQL));
        }
      }
    }
    catch (Throwable e)
    {
      throw new DAOException(String.format(
          "Failed to check whether the report definition (%s) exists in the database", id), e);
    }
  }

  /**
   * Update the report definition.
   *
   * @param reportDefinition the <code>ReportDefinition</code> instance containing the updated
   *                         information for the report definition
   *
   * @return the updated report definition
   *
   * @throws DAOException
   */
  public ReportDefinition updateReportDefinition(ReportDefinition reportDefinition)
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
        throw new DAOException(String.format(
            "No rows were affected as a result of executing the SQL statement (%s)",
            updateReportDefinitionSQL));
      }

      return reportDefinition;
    }
    catch (Throwable e)
    {
      throw new DAOException(String.format(
          "Failed to update the report definition (%s) in the database", reportDefinition.getId()),
          e);
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
    createReportDefinitionSQL = "INSERT INTO " + schemaPrefix + "REPORT_DEFINITIONS (ID, NAME, "
        + "TEMPLATE) VALUES (?, ?, ?)";

    // deleteReportDefinitionSQL
    deleteReportDefinitionSQL = "DELETE FROM " + schemaPrefix + "REPORT_DEFINITIONS RD WHERE RD"
        + ".ID=?";

    // getNumberOfReportDefinitionsSQL
    getNumberOfReportDefinitionsSQL = "SELECT COUNT(RD.ID)" + " FROM " + schemaPrefix
        + "REPORT_DEFINITIONS RD";

    // getReportDefinitionByIdSQL
    getReportDefinitionByIdSQL = "SELECT RD.ID, RD.NAME, RD.TEMPLATE FROM " + schemaPrefix
        + "REPORT_DEFINITIONS RD WHERE RD.ID=?";

    // getReportDefinitionSummariesSQL
    getReportDefinitionSummariesSQL = "SELECT RD.ID, RD.NAME" + " FROM " + schemaPrefix
        + "REPORT_DEFINITIONS RD";

    // getReportDefinitionSummaryByIdSQL
    getReportDefinitionSummaryByIdSQL = "SELECT RD.ID, RD.NAME FROM " + schemaPrefix
        + "REPORT_DEFINITIONS RD WHERE RD.ID=?";

    // getReportDefinitionsSQL
    getReportDefinitionsSQL = "SELECT RD.ID, RD.NAME, RD.TEMPLATE FROM " + schemaPrefix
        + "REPORT_DEFINITIONS RD";

    // reportDefinitionExistsSQL
    reportDefinitionExistsSQL = "SELECT COUNT(RD.ID) FROM " + schemaPrefix + "REPORT_DEFINITIONS "
        + "RD WHERE RD.ID=?";

    // updateReportDefinitionSQL
    updateReportDefinitionSQL = "UPDATE " + schemaPrefix + "REPORT_DEFINITIONS RD SET RD.NAME=?, "
        + "RD.TEMPLATE=? WHERE RD.ID=?";
  }

  private ReportDefinition getReportDefinition(ResultSet rs)
    throws SQLException
  {
    return new ReportDefinition((UUID) rs.getObject(1), rs.getString(2), rs.getBytes(3));
  }

  private ReportDefinitionSummary getReportDefinitionSummary(ResultSet rs)
    throws SQLException
  {
    return new ReportDefinitionSummary((UUID) rs.getObject(1), rs.getString(2));
  }
}

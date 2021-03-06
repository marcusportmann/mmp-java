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

package guru.mmp.application.reporting;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.common.persistence.DAOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
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
@SuppressWarnings("unused")
@Repository
public class ReportingDAO
  implements IReportingDAO
{
  /**
   * The data source used to provide connections to the application database.
   */
  @Inject
  @Qualifier("applicationDataSource")
  private DataSource dataSource;

  /**
   * Constructs a new <code>ReportingDAO</code>.
   */
  public ReportingDAO() {}

  /**
   * Create the new report definition.
   *
   * @param reportDefinition the <code>ReportDefinition</code> instance containing the information
   *                         for the new report definition
   */
  public void createReportDefinition(ReportDefinition reportDefinition)
    throws DAOException
  {
    String createReportDefinitionSQL =
        "INSERT INTO REPORTING.REPORT_DEFINITIONS (ID, NAME, TEMPLATE) VALUES (?, ?, ?)";

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
   */
  public void deleteReportDefinition(UUID id)
    throws DAOException
  {
    String deleteReportDefinitionSQL = "DELETE FROM REPORTING.REPORT_DEFINITIONS WHERE ID=?";

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
   */
  public int getNumberOfReportDefinitions()
    throws DAOException
  {
    String getNumberOfReportDefinitionsSQL =
        "SELECT COUNT(RD.ID) FROM REPORTING.REPORT_DEFINITIONS RD";

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
   */
  public ReportDefinition getReportDefinition(UUID id)
    throws DAOException
  {
    String getReportDefinitionByIdSQL =
        "SELECT RD.ID, RD.NAME, RD.TEMPLATE FROM REPORTING.REPORT_DEFINITIONS RD WHERE RD.ID=?";

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
   */
  public List<ReportDefinitionSummary> getReportDefinitionSummaries()
    throws DAOException
  {
    String getReportDefinitionSummariesSQL =
        "SELECT RD.ID, RD.NAME FROM REPORTING.REPORT_DEFINITIONS RD";

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
   */
  public ReportDefinitionSummary getReportDefinitionSummary(UUID id)
    throws DAOException
  {
    String getReportDefinitionSummaryByIdSQL =
        "SELECT RD.ID, RD.NAME FROM REPORTING.REPORT_DEFINITIONS RD WHERE RD.ID=?";

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
   */
  public List<ReportDefinition> getReportDefinitions()
    throws DAOException
  {
    String getReportDefinitionsSQL =
        "SELECT RD.ID, RD.NAME, RD.TEMPLATE FROM REPORTING.REPORT_DEFINITIONS RD";

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
   * Check whether the report definition exists in the database.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the report
   *           definition
   *
   * @return <code>true</code> if the report definition exists or <code>false</code> otherwise
   */
  public boolean reportDefinitionExists(UUID id)
    throws DAOException
  {
    String reportDefinitionExistsSQL =
        "SELECT COUNT(RD.ID) FROM REPORTING.REPORT_DEFINITIONS RD WHERE RD.ID=?";

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
   */
  public ReportDefinition updateReportDefinition(ReportDefinition reportDefinition)
    throws DAOException
  {
    String updateReportDefinitionSQL =
        "UPDATE REPORTING.REPORT_DEFINITIONS SET NAME=?, TEMPLATE=? WHERE ID=?";

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

  private ReportDefinition getReportDefinition(ResultSet rs)
    throws SQLException
  {
    return new ReportDefinition(UUID.fromString(rs.getString(1)), rs.getString(2), rs.getBytes(3));
  }

  private ReportDefinitionSummary getReportDefinitionSummary(ResultSet rs)
    throws SQLException
  {
    return new ReportDefinitionSummary(UUID.fromString(rs.getString(1)), rs.getString(2));
  }
}

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

import javax.sql.DataSource;
import java.util.List;
import java.util.UUID;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>IReportingDAO</code> interface defines the persistence operations for the
 * reporting infrastructure.
 *
 * @author Marcus Portmann
 */
public interface IReportingDAO
{
  /**
   * Create the new report definition.
   *
   * @param reportDefinition the <code>ReportDefinition</code> instance containing the information
   *                         for the new report definition
   *
   * @throws DAOException
   */
  void createReportDefinition(ReportDefinition reportDefinition)
    throws DAOException;

  /**
   * Delete the existing report definition.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the report
   *           definition
   *
   * @throws DAOException
   */
  void deleteReportDefinition(UUID id)
    throws DAOException;

  /**
   * Returns the data source used to provide connections to the application database.
   *
   * @return the data source used to provide connections to the application database
   */
  DataSource getDataSource();

  /**
   * Returns the number of report definitions.
   *
   * @return the number of report definitions
   *
   * @throws DAOException
   */
  int getNumberOfReportDefinitions()
    throws DAOException;

  /**
   * Retrieve the report definition.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the report
   *           definition
   *
   * @return the report definition or <code>null</code> if the report definition could not be found
   *
   * @throws DAOException
   */
  ReportDefinition getReportDefinition(UUID id)
    throws DAOException;

  /**
   * Returns the summaries for all the report definitions.
   *
   * @return the summaries for all the report definitions
   *
   * @throws DAOException
   */
  List<ReportDefinitionSummary> getReportDefinitionSummaries()
    throws DAOException;

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
  ReportDefinitionSummary getReportDefinitionSummary(UUID id)
    throws DAOException;

  /**
   * Returns all the report definitions.
   *
   * @return all the report definitions
   *
   * @throws DAOException
   */
  List<ReportDefinition> getReportDefinitions()
    throws DAOException;

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
  boolean reportDefinitionExists(UUID id)
    throws DAOException;

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
  ReportDefinition updateReportDefinition(ReportDefinition reportDefinition)
    throws DAOException;
}

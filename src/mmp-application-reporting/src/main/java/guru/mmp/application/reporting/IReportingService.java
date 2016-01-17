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

import org.w3c.dom.Document;

import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>IReportingService</code> interface defines the functionality provided by a Reporting
 * Service implementation.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public interface IReportingService
{
  /**
   * The username used to identify operations performed by the system.
   */
  String SYSTEM_USERNAME = "SYSTEM";

  /**
   * Create a PDF for the report using a connection retrieved from the application data source.
   *
   * @param definitionId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                     report definition
   * @param parameters   the parameters for the report
   *
   * @return the PDF data for the report
   *
   * @throws ReportingServiceException
   */
  byte[] createReportPDF(UUID definitionId, Map<String, Object> parameters)
    throws ReportingServiceException;

  /**
   * Create a PDF for the report.
   *
   * @param definitionId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                     report definition
   * @param parameters   the parameters for the report
   * @param connection   the database connection used to retrieve the report data
   *
   * @return the PDF data for the report
   *
   * @throws ReportingServiceException
   */
  byte[] createReportPDF(UUID definitionId, Map<String, Object> parameters, Connection connection)
    throws ReportingServiceException;

  /**
   * Create a PDF for the report.
   *
   * @param definitionId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                     report definition
   * @param parameters   the parameters for the report
   * @param document     the XML document containing the report data
   *
   * @return the PDF data for the report
   *
   * @throws ReportingServiceException
   */
  byte[] createReportPDF(UUID definitionId, Map<String, Object> parameters, Document document)
    throws ReportingServiceException;

  /**
   * Delete the existing report definition.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the
   *           report definition
   *
   * @throws ReportingServiceException
   */
  void deleteReportDefinition(UUID id)
    throws ReportingServiceException;

  /**
   * Returns the real path to the folder where the local Jasper reports are stored.
   *
   * @return the real path to the folder where the local Jasper reports are stored
   */
  String getLocalReportFolderPath();

  /**
   * Returns the number of report definitions.
   *
   * @return the number of report definitions
   *
   * @throws ReportingServiceException
   */
  int getNumberOfReportDefinitions()
    throws ReportingServiceException;

  /**
   * Retrieve the report definition.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the report
   *           definition
   *
   * @return the report definition or <code>null</code> if the report definition could not be found
   *
   * @throws ReportingServiceException
   */
  ReportDefinition getReportDefinition(UUID id)
    throws ReportingServiceException;

  /**
   * Returns the summaries for all the report definitions.
   *
   * @return the summaries for all the report definitions
   *
   * @throws ReportingServiceException
   */
  List<ReportDefinitionSummary> getReportDefinitionSummaries()
    throws ReportingServiceException;

  /**
   * Retrieve the summary for the report definition.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the report
   *           definition
   *
   * @return the summary for the report definition or <code>null</code> if the report definition
   * could not be found
   *
   * @throws ReportingServiceException
   */
  ReportDefinitionSummary getReportDefinitionSummary(UUID id)
    throws ReportingServiceException;

  /**
   * Returns all the report definitions.
   *
   * @return all the report definitions
   *
   * @throws ReportingServiceException
   */
  List<ReportDefinition> getReportDefinitions()
    throws ReportingServiceException;

  /**
   * Check whether the report definition exists.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the report
   *           definition
   *
   * @return <code>true</code> if the report definition exists or <code>false</code> otherwise
   *
   * @throws ReportingServiceException
   */
  boolean reportDefinitionExists(UUID id)
    throws ReportingServiceException;

  /**
   * Save the report definition.
   * <p/>
   * This will create a new entry for the report definition in the database or update the
   * existing entry.
   *
   * @param reportDefinition the <code>ReportDefinition</code> instance containing the information
   *                         for the report definition
   *
   * @return the saved report definition
   *
   * @throws ReportingServiceException
   */
  ReportDefinition saveReportDefinition(ReportDefinition reportDefinition)
    throws ReportingServiceException;

  /**
   * Set the real path to the folder where the local Jasper reports are stored.
   *
   * @param localReportFolderPath the real path to the folder where the local Jasper reports are
   *                              stored
   */
  void setLocalReportFolderPath(String localReportFolderPath);
}

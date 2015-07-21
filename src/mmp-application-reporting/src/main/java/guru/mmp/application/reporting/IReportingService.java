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

import org.w3c.dom.Document;

//~--- JDK imports ------------------------------------------------------------

import java.sql.Connection;

import java.util.List;
import java.util.Map;

/**
 * The <code>IReportingService</code> interface defines the functionality that must be provided by
 * a Reporting Service implementation.
 *
 * @author Marcus Portmann
 */
public interface IReportingService
{
  /**
   * The username used to identify operations performed by the system.
   */
  public static final String SYSTEM_USERNAME = "SYSTEM";

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
  public byte[] createReportPDF(String definitionId, Map<String, Object> parameters)
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
  public byte[] createReportPDF(String definitionId, Map<String, Object> parameters,
      Connection connection)
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
  public byte[] createReportPDF(String definitionId, Map<String, Object> parameters,
      Document document)
    throws ReportingServiceException;

  /**
   * Delete the existing report definition.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the
   *           report definition
   *
   * @throws ReportingServiceException
   */
  public void deleteReportDefinition(String id)
    throws ReportingServiceException;

  /**
   * Returns the real path to the folder where the local Jasper reports are stored.
   *
   * @return the real path to the folder where the local Jasper reports are stored
   */
  public String getLocalReportFolderPath();

  /**
   * Returns the number of report definitions associated with the organisation identified by the
   * specified organisation code.
   *
   * @param organisation the organisation code identifying the organisation
   *
   * @return the number of report definitions associated with the organisation identified by the
   *         specified organisation code
   *
   * @throws ReportingServiceException
   */
  public int getNumberOfReportDefinitionsForOrganisation(String organisation)
    throws ReportingServiceException;

  /**
   * Retrieve the report definition with the specified ID.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the
   *           report definition
   *
   * @return the report definition with the specified ID or <code>null</code> if the report
   *         definition could not be found
   *
   * @throws ReportingServiceException
   */
  public ReportDefinition getReportDefinition(String id)
    throws ReportingServiceException;

  /**
   * Returns all the report definitions associated with the organisation identified by the specified
   * organisation code.
   *
   * @param organisation the organisation code identifying the organisation
   *
   * @return all the report definitions associated with the organisation identified by the specified
   *         organisation code
   *
   * @throws ReportingServiceException
   */
  public List<ReportDefinition> getReportDefinitionsForOrganisation(String organisation)
    throws ReportingServiceException;

  /**
   * Check whether the report definition with the specified ID exists in the database.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the report
   *           definition
   *
   * @return <code>true</code> if the report definition exists or <code>false</code> otherwise
   *
   * @throws ReportingServiceException
   */
  public boolean reportDefinitionExists(String id)
    throws ReportingServiceException;

  /**
   * Save the report definition.
   * <p/>
   * This will create a new entry for the report definition in the database or update the
   * existing entry.
   *
   * @param reportDefinition the <code>ReportDefinition</code> instance containing the information
   *                         for the report definition
   * @param savedBy          the username identifying the user that saved the report definition
   *
   * @return the saved report definition
   *
   * @throws ReportingServiceException
   */
  public ReportDefinition saveReportDefinition(ReportDefinition reportDefinition, String savedBy)
    throws ReportingServiceException;

  /**
   * Set the real path to the folder where the local Jasper reports are stored.
   *
   * @param localReportFolderPath the real path to the folder where the local Jasper reports are
   *                              stored
   */
  public void setLocalReportFolderPath(String localReportFolderPath);
}

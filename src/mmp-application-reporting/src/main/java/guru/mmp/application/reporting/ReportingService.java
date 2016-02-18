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

import guru.mmp.application.util.ServiceUtil;
import guru.mmp.common.util.StringUtil;

import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.query.JRXPathQueryExecuterFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.w3c.dom.Document;

//~--- JDK imports ------------------------------------------------------------

import java.io.ByteArrayInputStream;

import java.sql.Connection;

import java.util.*;

import javax.annotation.PostConstruct;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;

import javax.inject.Inject;

/**
 * The <code>ReportingService</code> class provides the Reporting Service implementation.
 *
 * @author Marcus Portmann
 */
@ApplicationScoped
@Default
public class ReportingService
  implements IReportingService
{
  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(ReportingService.class);

  /* The real path to the folder where the local Jasper reports are stored. */
  private String localReportFolderPath;

  /* The DAO providing persistence capabilities for the reporting infrastructure. */
  @Inject
  private IReportingDAO reportingDAO;

  /**
   * Constructs a new <code>ReportingService</code>.
   */
  public ReportingService() {}

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
  public byte[] createReportPDF(UUID definitionId, Map<String, Object> parameters)
    throws ReportingServiceException
  {
    try (Connection connection = reportingDAO.getDataSource().getConnection())
    {
      ReportDefinition reportDefinition = getReportDefinition(definitionId);

      if (reportDefinition == null)
      {
        throw new ReportingServiceException("Failed to find the report definition (" + definitionId
            + ")");
      }

      Map<String, Object> localParameters = new HashMap<>();

      if (StringUtil.isNullOrEmpty(getLocalReportFolderPath()))
      {
        localParameters.put("SUBREPORT_DIR", getLocalReportFolderPath());
      }

      for (String name : parameters.keySet())
      {
        localParameters.put(name, parameters.get(name));
      }

      JasperPrint jasperPrint = JasperFillManager.fillReport(new ByteArrayInputStream(
          reportDefinition.getTemplate()), localParameters, connection);

      return JasperExportManager.exportReportToPdf(jasperPrint);
    }
    catch (Throwable e)
    {
      throw new ReportingServiceException(String.format(
          "Failed to create the PDF for the report using the report definintion (%s)",
          definitionId), e);
    }
  }

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
  public byte[] createReportPDF(UUID definitionId, Map<String, Object> parameters,
      Connection connection)
    throws ReportingServiceException
  {
    try
    {
      ReportDefinition reportDefinition = getReportDefinition(definitionId);

      if (reportDefinition == null)
      {
        throw new ReportingServiceException(String.format(
            "Failed to find the report definition (%s)", definitionId));
      }

      Map<String, Object> localParameters = new HashMap<>();

      if (StringUtil.isNullOrEmpty(getLocalReportFolderPath()))
      {
        localParameters.put("SUBREPORT_DIR", getLocalReportFolderPath());
      }

      for (String name : parameters.keySet())
      {
        localParameters.put(name, parameters.get(name));
      }

      JasperPrint jasperPrint = JasperFillManager.fillReport(new ByteArrayInputStream(
          reportDefinition.getTemplate()), localParameters, connection);

      return JasperExportManager.exportReportToPdf(jasperPrint);
    }
    catch (Throwable e)
    {
      throw new ReportingServiceException(String.format(
          "Failed to create the PDF for the report using the report definintion (%s)",
          definitionId), e);
    }
  }

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
  public byte[] createReportPDF(UUID definitionId, Map<String, Object> parameters,
      Document document)
    throws ReportingServiceException
  {
    try
    {
      ReportDefinition reportDefinition = getReportDefinition(definitionId);

      if (reportDefinition == null)
      {
        throw new ReportingServiceException(String.format(
            "Failed to find the report definition (%s)", definitionId));
      }

      Map<String, Object> localParameters = new HashMap<>();

      localParameters.put(JRXPathQueryExecuterFactory.PARAMETER_XML_DATA_DOCUMENT, document);
      localParameters.put(JRXPathQueryExecuterFactory.XML_DATE_PATTERN, "yyyy-MM-dd");
      localParameters.put(JRXPathQueryExecuterFactory.XML_NUMBER_PATTERN, "#,##0.##");
      localParameters.put(JRXPathQueryExecuterFactory.XML_LOCALE, Locale.ENGLISH);
      localParameters.put(JRParameter.REPORT_LOCALE, Locale.US);

      if (StringUtil.isNullOrEmpty(getLocalReportFolderPath()))
      {
        localParameters.put("SUBREPORT_DIR", getLocalReportFolderPath());
      }

      for (String name : parameters.keySet())
      {
        localParameters.put(name, parameters.get(name));
      }

      JasperPrint jasperPrint = JasperFillManager.fillReport(new ByteArrayInputStream(
          reportDefinition.getTemplate()), localParameters);

      return JasperExportManager.exportReportToPdf(jasperPrint);
    }
    catch (Throwable e)
    {
      throw new ReportingServiceException(String.format(
          "Failed to create the PDF for the report using the report definintion (%s)",
          definitionId), e);
    }
  }

  /**
   * Delete the existing report definition.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the report
   *           definition
   *
   * @throws ReportingServiceException
   */
  public void deleteReportDefinition(UUID id)
    throws ReportingServiceException
  {
    try
    {
      reportingDAO.deleteReportDefinition(id);
    }
    catch (Throwable e)
    {
      throw new ReportingServiceException(String.format(
          "Failed to delete the report definition with ID (%s)", id), e);
    }
  }

  /**
   * Returns the real path to the folder where the local Jasper reports are stored.
   *
   * @return the real path to the folder where the local Jasper reports are stored
   */
  public String getLocalReportFolderPath()
  {
    return localReportFolderPath;
  }

  /**
   * Returns the number of report definitions.
   *
   * @return the number of report definitions
   *
   * @throws ReportingServiceException
   */
  public int getNumberOfReportDefinitions()
    throws ReportingServiceException
  {
    try
    {
      return reportingDAO.getNumberOfReportDefinitions();
    }
    catch (Throwable e)
    {
      throw new ReportingServiceException("Failed to retrieve the number of report definitions", e);
    }
  }

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
  public ReportDefinition getReportDefinition(UUID id)
    throws ReportingServiceException
  {
    try
    {
      return reportingDAO.getReportDefinition(id);
    }
    catch (Throwable e)
    {
      throw new ReportingServiceException(String.format(
          "Failed to retrieve the report definition with ID (%s)", id), e);
    }
  }

  /**
   * Returns the summaries for all the report definitions.
   *
   * @return the summaries for all the report definitions
   *
   * @throws ReportingServiceException
   */
  public List<ReportDefinitionSummary> getReportDefinitionSummaries()
    throws ReportingServiceException
  {
    try
    {
      return reportingDAO.getReportDefinitionSummaries();
    }
    catch (Throwable e)
    {
      throw new ReportingServiceException(
          "Failed to retrieve the summaries for all the report definitions", e);
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
   * @throws ReportingServiceException
   */
  public ReportDefinitionSummary getReportDefinitionSummary(UUID id)
    throws ReportingServiceException
  {
    try
    {
      return reportingDAO.getReportDefinitionSummary(id);
    }
    catch (Throwable e)
    {
      throw new ReportingServiceException(String.format(
          "Failed to retrieve the summary for report definition with ID (%s)", id), e);
    }
  }

  /**
   * Returns all the report definitions.
   *
   * @return all the report definitions
   *
   * @throws ReportingServiceException
   */
  public List<ReportDefinition> getReportDefinitions()
    throws ReportingServiceException
  {
    try
    {
      return reportingDAO.getReportDefinitions();
    }
    catch (Throwable e)
    {
      throw new ReportingServiceException("Failed to retrieve all the report definitions", e);
    }
  }

  /**
   * Initialise the Reporting Service.
   */
  @PostConstruct
  public void init()
  {
    logger.info("Initialising the Reporting Service");

    try
    {
      // Initialise the configuration for the Reporting Service
      initConfiguration();
    }
    catch (Throwable e)
    {
      throw new RuntimeException("Failed to initialise the Reporting Service", e);
    }
  }

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
  public boolean reportDefinitionExists(UUID id)
    throws ReportingServiceException
  {
    try
    {
      return reportingDAO.reportDefinitionExists(id);
    }
    catch (Throwable e)
    {
      throw new ReportingServiceException(String.format(
          "Failed to check whether the report definition with ID (%s) exists", id), e);
    }
  }

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
  public ReportDefinition saveReportDefinition(ReportDefinition reportDefinition)
    throws ReportingServiceException
  {
    try
    {
      if (reportingDAO.reportDefinitionExists(reportDefinition.getId()))
      {
        reportDefinition = reportingDAO.updateReportDefinition(reportDefinition);
      }
      else
      {
        reportingDAO.createReportDefinition(reportDefinition);
      }

      return reportDefinition;
    }
    catch (Throwable e)
    {
      throw new ReportingServiceException(String.format(
          "Failed to save the report definition with ID (%s)", reportDefinition.getId()), e);
    }
  }

  /**
   * Set the real path to the folder where the local Jasper reports are stored.
   *
   * @param localReportFolderPath the real path to the folder where the local Jasper reports are
   *                              stored
   */
  public void setLocalReportFolderPath(String localReportFolderPath)
  {
    this.localReportFolderPath = localReportFolderPath;
  }

  /**
   * Initialise the configuration for the Reporting Service.
   *
   * @throws ReportingServiceException
   */
  private void initConfiguration()
    throws ReportingServiceException
  {
    try {}
    catch (Throwable e)
    {
      throw new ReportingServiceException(
          "Failed to initialise the configuration for the Reporting Service", e);
    }
  }
}

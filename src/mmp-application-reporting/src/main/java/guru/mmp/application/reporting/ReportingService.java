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
import guru.mmp.common.persistence.DAOUtil;
import guru.mmp.common.util.StringUtil;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.query.JRXPathQueryExecuterFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.util.*;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>ReportService</code> class provides the Reporting Service implementation.
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

  /** The data source used to provide connections to the database. */
  private DataSource dataSource;

  /* The name of the reporting service instance. */
  private String instanceName;

  /* The real path to the folder where the local Jasper reports are stored. */
  private String localReportFolderPath;

  /* The DAO providing persistence capabilities for the reporting infrastructure. */
  @Inject
  private IReportingDAO reportingDAO;

  /**
   * Constructs a new <code>ReportService</code>.
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
  public byte[] createReportPDF(String definitionId, Map<String, Object> parameters)
    throws ReportingServiceException
  {
    Connection connection = null;

    try
    {
      connection = dataSource.getConnection();

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

      JasperPrint jasperPrint =
        JasperFillManager.fillReport(new ByteArrayInputStream(reportDefinition.getTemplate()),
          localParameters, connection);

      return JasperExportManager.exportReportToPdf(jasperPrint);
    }
    catch (Throwable e)
    {
      throw new ReportingServiceException("Failed to create the PDF for the report using the"
          + " report definintion (" + definitionId + ")", e);
    }
    finally
    {
      DAOUtil.close(connection);
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
  public byte[] createReportPDF(String definitionId, Map<String, Object> parameters,
      Connection connection)
    throws ReportingServiceException
  {
    try
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

      JasperPrint jasperPrint =
        JasperFillManager.fillReport(new ByteArrayInputStream(reportDefinition.getTemplate()),
          localParameters, connection);

      return JasperExportManager.exportReportToPdf(jasperPrint);
    }
    catch (Throwable e)
    {
      throw new ReportingServiceException("Failed to create the PDF for the report using the"
          + " report definintion (" + definitionId + ")", e);
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
  public byte[] createReportPDF(String definitionId, Map<String, Object> parameters,
      Document document)
    throws ReportingServiceException
  {
    try
    {
      ReportDefinition reportDefinition = getReportDefinition(definitionId);

      if (reportDefinition == null)
      {
        throw new ReportingServiceException("Failed to find the report definition (" + definitionId
            + ")");
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

      JasperPrint jasperPrint =
        JasperFillManager.fillReport(new ByteArrayInputStream(reportDefinition.getTemplate()),
          localParameters);

      return JasperExportManager.exportReportToPdf(jasperPrint);
    }
    catch (Throwable e)
    {
      throw new ReportingServiceException("Failed to create the PDF for the report using the"
          + " report definintion (" + definitionId + ")", e);
    }
  }

  /**
   * Delete the existing report definition.
   *
   * @param reportDefinition the report definition to delete
   *
   *
   * @throws ReportingServiceException
   */
  public void deleteReportDefinition(ReportDefinition reportDefinition)
    throws ReportingServiceException
  {
    try
    {
      reportingDAO.deleteReportDefinition(reportDefinition.getId());
    }
    catch (Throwable e)
    {
      throw new ReportingServiceException("Failed to delete the report definition with ID ("
          + reportDefinition.getId() + ")", e);
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
    throws ReportingServiceException
  {
    try
    {
      return reportingDAO.getNumberOfReportDefinitionsForOrganisation(organisation);
    }
    catch (Throwable e)
    {
      throw new ReportingServiceException(
          "Failed to retrieve the number of report definitions for the organisation ("
          + organisation + ")", e);
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
   * @throws ReportingServiceException
   */
  public ReportDefinition getReportDefinition(String id)
    throws ReportingServiceException
  {
    try
    {
      return reportingDAO.getReportDefinition(id);
    }
    catch (Throwable e)
    {
      throw new ReportingServiceException("Failed to retrieve the report definition with ID (" + id
          + ")", e);
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
   * @throws ReportingServiceException
   */
  public List<ReportDefinition> getReportDefinitionsForOrganisation(String organisation)
    throws ReportingServiceException
  {
    try
    {
      return reportingDAO.getReportDefinitionsForOrganisation(organisation);
    }
    catch (Throwable e)
    {
      throw new ReportingServiceException(
          "Failed to retrieve all the report definitions for the organisation (" + organisation
          + ")", e);
    }
  }

  /**
   * Initialise the ReportingService instance.
   */
  @PostConstruct
  public void init()
  {
    logger.info("Initialising the ReportingService instance (" + getInstanceName() + ")");

    try
    {
      // Initialise the configuration for the ReportingService instance
      initConfiguration();
    }
    catch (Exception e)
    {
      throw new RuntimeException("Failed to initialise the ReportingService instance", e);
    }
    catch (Throwable e)
    {
      throw new RuntimeException("Failed to initialise the ReportingService instance: "
          + e.getMessage());
    }

    try
    {
      dataSource = InitialContext.doLookup("java:app/jdbc/ApplicationDataSource");
    }
    catch (Throwable ignored) {}

    if (dataSource == null)
    {
      throw new DAOException("Failed to initialise the ReportingService instance:"
          + " Failed to retrieve the data source using the JNDI lookup"
          + " (java:app/jdbc/ApplicationDataSource)");
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
   * @throws ReportingServiceException
   */
  public boolean reportDefinitionExists(String id)
    throws ReportingServiceException
  {
    try
    {
      return reportingDAO.reportDefinitionExists(id);
    }
    catch (Throwable e)
    {
      throw new ReportingServiceException("Failed to check whether the report definition with ID ("
          + id + ") exists", e);
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
   * @param savedBy          the username identifying the user that saved the report definition
   *
   * @return the saved report definition
   *
   * @throws ReportingServiceException
   */
  public ReportDefinition saveReportDefinition(ReportDefinition reportDefinition, String savedBy)
    throws ReportingServiceException
  {
    try
    {
      if (reportingDAO.reportDefinitionExists(reportDefinition.getId()))
      {
        reportDefinition = reportingDAO.updateReportDefinition(reportDefinition, savedBy);
      }
      else
      {
        Date created = new Date();

        reportDefinition.setCreated(created);
        reportDefinition.setCreatedBy(savedBy);
        reportDefinition.setUpdated(created);
        reportDefinition.setUpdatedBy(savedBy);

        reportingDAO.createReportDefinition(reportDefinition);
      }

      return reportDefinition;
    }
    catch (Throwable e)
    {
      throw new ReportingServiceException("Failed to save the report definition with ID ("
          + reportDefinition.getId() + ")", e);
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
   * Retrieves the name of the reporting service instance.
   */
  private String getInstanceName()
  {
    if (instanceName == null)
    {
      String applicationName = null;

      try
      {
        applicationName = InitialContext.doLookup("java:app/AppName");
      }
      catch (Throwable ignored) {}

      if (applicationName == null)
      {
        logger.error("Failed to retrieve the application name from JNDI using the path ("
            + "java:app/AppName) while constructing the reporting service instance name");

        applicationName = "Unknown";
      }

      instanceName = applicationName + "::";

      try
      {
        java.net.InetAddress localMachine = java.net.InetAddress.getLocalHost();

        instanceName += localMachine.getHostName().toLowerCase();
      }
      catch (Throwable e)
      {
        logger.error("Failed to retrieve the server hostname while constructing the report"
            + " service instance name", e);
        instanceName = "Unknown";
      }

      // Check if we are running under JBoss and if so retrieve the server name
      if (System.getProperty("jboss.server.name") != null)
      {
        instanceName = instanceName + "::" + System.getProperty("jboss.server.name");
      }

      // Check if we are running under Glassfish and if so retrieve the server name
      else if (System.getProperty("glassfish.version") != null)
      {
        instanceName = instanceName + "::" + System.getProperty("com.sun.aas.instanceName");
      }

      // Check if we are running under WebSphere Application Server Community Edition (Geronimo)
      else if (System.getProperty("org.apache.geronimo.server.dir") != null)
      {
        instanceName = instanceName + "::Geronimo";
      }

      // Check if we are running under WebSphere Application Server Liberty Profile
      else if (System.getProperty("wlp.user.dir") != null)
      {
        instanceName = instanceName + "::WLP";
      }

      /*
       * Check if we are running under WebSphere and if so execute the code below to retrieve the
       * server name.
       */
      else
      {
        try
        {
          instanceName = instanceName + "::" + InitialContext.doLookup("servername").toString();
        }
        catch (Throwable e)
        {
          logger.error("Failed to retrieve the name of the WebSphere server instance from JNDI"
              + " while constructing the reporting service instance name", e);
          instanceName = instanceName + "::Unknown";
        }
      }
    }

    return instanceName;
  }

  /**
   * Initialise the configuration for the ReportingService instance.
   *
   * @throws ReportingServiceException
   */
  private void initConfiguration()
    throws ReportingServiceException
  {
//  try
//  {
//  }
//  catch (Throwable e)
//  {
//    throw new ReportingServiceException(
//        "Failed to initialise the configuration for the ReportingService instance", e);
//  }
  }
}

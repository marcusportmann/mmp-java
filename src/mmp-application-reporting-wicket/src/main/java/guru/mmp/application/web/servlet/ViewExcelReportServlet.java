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

package guru.mmp.application.web.servlet;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.persistence.DAOException;
import guru.mmp.application.reporting.IReportingService;
import guru.mmp.application.reporting.ReportDefinition;
import guru.mmp.application.reporting.ReportType;
import guru.mmp.application.web.WebSession;
import guru.mmp.application.web.template.TemplateReportingSecurity;
import guru.mmp.common.persistence.DAOUtil;
import guru.mmp.common.util.StringUtil;

import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//~--- JDK imports ------------------------------------------------------------

import java.io.*;

import java.sql.Connection;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import javax.naming.InitialContext;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.sql.DataSource;

/**
 * The <code>ViewExcelReportServlet</code> class implements the servlet used to view Excel reports.
 *
 * @author Marcus Portmann
 */
public class ViewExcelReportServlet extends HttpServlet
{
  private static final long serialVersionUID = 1000000;

  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(ViewExcelReportServlet.class);

  /** The data source used to provide connections to the database. */
  private DataSource dataSource;

  /** The real path to the WEB-INF/report folder where the local Jasper reports are stored. */
  private String localReportFolderPath;

  /* Reporting Service */
  @Inject
  private IReportingService reportingService;

  /**
   * @see HttpServlet#HttpServlet()
   */
  public ViewExcelReportServlet()
  {
    super();

    System.setProperty("swing.defaultlaf", "javax.swing.plaf.metal.MetalLookAndFeel");
  }

  /**
   * Called by the servlet container to indicate to a servlet that the servlet is being placed into
   * service.
   *
   * @throws ServletException
   */
  @Override
  public void init()
    throws ServletException
  {
    super.init();

    try
    {
      dataSource = InitialContext.doLookup("java:app/jdbc/ApplicationDataSource");
    }
    catch (Throwable ignored) {}

    if (dataSource == null)
    {
      throw new DAOException("Failed to initialise the ViewExcelReportServlet:"
          + " Failed to retrieve the data source using the JNDI lookup"
          + " (java:app/jdbc/ApplicationDataSource)");
    }
  }

  /**
   * Called by the servlet container to indicate to a servlet that the servlet is being placed into
   * service.
   *
   * @param servletConfig  a <code>ServletConfig</code> object containing the servlet's
   *                       configuration and initialization parameters
   *
   * @throws ServletException
   */
  @Override
  public void init(ServletConfig servletConfig)
    throws ServletException
  {
    super.init(servletConfig);

    try
    {
      dataSource = InitialContext.doLookup("java:app/jdbc/ApplicationDataSource");
    }
    catch (Throwable ignored) {}

    if (dataSource == null)
    {
      throw new DAOException("Failed to initialise the process DAO:"
          + " Failed to retrieve the data source using the JNDI lookup"
          + " (java:app/jdbc/ApplicationDataSource)");
    }
  }

  /**
   * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
   */
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    String viewReportParametersId = request.getParameter("viewReportParametersId");

    if ((!StringUtil.isNullOrEmpty(viewReportParametersId)) && WebSession.exists())
    {
      WebSession webSession = (WebSession) WebSession.get();

      if (webSession != null)
      {
        try
        {
          // Retrieve the ViewReportParameters from the user's web session
          ViewReportParameters viewReportParameters =
            webSession.getViewReportParameters(viewReportParametersId);

          if (viewReportParameters != null)
          {
            if (webSession.hasAcccessToFunction(
                TemplateReportingSecurity.FUNCTION_CODE_VIEW_REPORT))
            {
//            // Check for a report logo and if one is not present then setup the default
//            if (!viewReportParameters.getReportParameters().containsKey("reportLogo"))
//            {
//              byte[] defaultReportLogo = getClasspathResource(
//                  "guru/mmp/application/web/template/resource/theme/mmp/reportLogo.png");
//
//              viewReportParameters.getReportParameters().put("reportLogo",
//                  new ByteArrayInputStream(defaultReportLogo));
//            }

              // Local Report
              if (viewReportParameters.getReportType() == ReportType.LOCAL)
              {
                Connection connection = null;

                try
                {
                  connection = dataSource.getConnection();

                  Map<String, Object> parameters = new HashMap<>();

                  parameters.put("SUBREPORT_DIR", getLocalReportFolderPath());

                  for (Map.Entry<String, Object> reportParameter :
                      viewReportParameters.getReportParameters().entrySet())
                  {
                    parameters.put(reportParameter.getKey(), reportParameter.getValue());
                  }

                  // Generate the report
                  JasperPrint jasperPrint = JasperFillManager.fillReport(
                      new ByteArrayInputStream(
                        getLocalReportTemplate(
                          viewReportParameters.getReportFileNameOrId())), parameters, connection);

                  response.addHeader("content-disposition",
                      "filename=" + viewReportParameters.getReportName() + ".xlsx");
                  response.addHeader("Accept-Ranges", "none");

                  response.setContentType(
                      "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

                  ServletOutputStream out = response.getOutputStream();

                  JRXlsxExporter exporter = new JRXlsxExporter();

                  SimpleXlsxExporterConfiguration exporterConfiguration =
                    new SimpleXlsxExporterConfiguration();

                  exporter.setConfiguration(exporterConfiguration);

                  SimpleXlsxReportConfiguration reportConfiguration =
                    new SimpleXlsxReportConfiguration();

                  reportConfiguration.setDetectCellType(Boolean.TRUE);
                  reportConfiguration.setOnePagePerSheet(Boolean.FALSE);
                  reportConfiguration.setWhitePageBackground(Boolean.TRUE);

                  exporter.setConfiguration(reportConfiguration);

                  exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                  exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(out));

                  exporter.exportReport();

                  out.flush();

                  return;
                }
                finally
                {
                  DAOUtil.close(connection);
                }
              }

              // Database Report
              else if (viewReportParameters.getReportType() == ReportType.DATABASE)
              {
                ReportDefinition reportDefinition = reportingService.getReportDefinition(
                    viewReportParameters.getReportFileNameOrId());

                if (reportDefinition != null)
                {
                  Connection connection = null;

                  try
                  {
                    // Retrieve a connection from the application data source
                    connection = dataSource.getConnection();

                    // Setup the report parameters
                    Map<String, Object> parameters = new HashMap<>();

                    parameters.put("SUBREPORT_DIR", getLocalReportFolderPath());

                    for (Map.Entry<String, Object> reportParameter :
                        viewReportParameters.getReportParameters().entrySet())
                    {
                      parameters.put(reportParameter.getKey(), reportParameter.getValue());
                    }

                    // Generate the report
                    JasperPrint jasperPrint = JasperFillManager.fillReport(
                        new ByteArrayInputStream(reportDefinition.getTemplate()), parameters,
                        connection);

                    response.addHeader("content-disposition",
                        "filename=" + viewReportParameters.getReportName() + ".xls");
                    response.addHeader("Accept-Ranges", "none");

                    response.setContentType("application/ms-excel");

                    ServletOutputStream out = response.getOutputStream();

                    JRXlsExporter exporter = new JRXlsExporter();

                    SimpleXlsExporterConfiguration exporterConfiguration =
                      new SimpleXlsExporterConfiguration();

                    exporter.setConfiguration(exporterConfiguration);

                    SimpleXlsReportConfiguration reportConfiguration =
                      new SimpleXlsReportConfiguration();

                    reportConfiguration.setCollapseRowSpan(Boolean.TRUE);
                    reportConfiguration.setRemoveEmptySpaceBetweenColumns(Boolean.TRUE);
                    reportConfiguration.setRemoveEmptySpaceBetweenRows(Boolean.TRUE);
                    reportConfiguration.setDetectCellType(Boolean.FALSE);
                    reportConfiguration.setOnePagePerSheet(Boolean.FALSE);
                    reportConfiguration.setWhitePageBackground(Boolean.FALSE);
                    reportConfiguration.setIgnoreGraphics(Boolean.TRUE);

                    exporter.setConfiguration(reportConfiguration);

                    exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                    exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(out));

                    exporter.exportReport();

                    out.flush();

                    return;
                  }
                  finally
                  {
                    DAOUtil.close(connection);
                  }
                }
                else
                {
                  throw new ServletException("Failed to view the report ("
                      + viewReportParameters.getReportFileNameOrId()
                      + "): The report could not be found");
                }
              }
              else
              {
                throw new ServletException("Failed to view the report ("
                    + viewReportParameters.getReportFileNameOrId() + "): Unknown report type ("
                    + viewReportParameters.getReportType() + ")");
              }
            }

            // Unknown Report
            else
            {
              throw new ServletException("Access denied when attempting to view the report ("
                  + viewReportParameters.getReportFileNameOrId() + ")");
            }
          }
          else
          {
            logger.warn("Failed to retrieve the ViewReportParameters (" + viewReportParametersId
                + ")");
          }
        }
        catch (Throwable e)
        {
          logger.error("Failed to view the report", e);
        }
        finally
        {
          webSession.removeViewReportParameters(viewReportParametersId);
        }
      }
    }

    try
    {
      response.setContentType("image/png");

      byte[] data =
        getClasspathResource("guru/mmp/application/web/template/resource/image/reportError.png");

      OutputStream out = response.getOutputStream();

      out.write(data);
      out.flush();
    }
    catch (Throwable ignored) {}
  }

  /**
   * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
   */
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    doGet(request, response);
  }

  private byte[] getClasspathResource(String path)
  {
    InputStream is = null;

    try
    {
      is = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);

      ByteArrayOutputStream baos = new ByteArrayOutputStream();

      byte[] buffer = new byte[4096];
      int numberOfBytesRead;

      while ((numberOfBytesRead = is.read(buffer)) != -1)
      {
        baos.write(buffer, 0, numberOfBytesRead);
      }

      return baos.toByteArray();
    }
    catch (Throwable e)
    {
      throw new RuntimeException("Failed to read the classpath resource (" + path + ")", e);
    }
    finally
    {
      try
      {
        if (is != null)
        {
          is.close();
        }
      }
      catch (Throwable e)
      {
        logger.error("Failed to close the input stream for the classpath resource (" + path + ")",
            e);
      }
    }
  }

  private String getLocalReportFolderPath()
  {
    if (localReportFolderPath == null)
    {
      localReportFolderPath = getServletContext().getRealPath("WEB-INF/report") + "/";
    }

    return localReportFolderPath;
  }

  private byte[] getLocalReportTemplate(String reportFileName)
    throws ServletException
  {
    // TODO: Cache report templates

    byte[] reportTemplate = null;

    InputStream is = null;

    try
    {
      String localReportPath = getLocalReportFolderPath() + reportFileName;

      File file = new File(localReportPath);

      if (!file.exists())
      {
        throw new ServletException("The local report template (" + localReportPath
            + ") does not exist");
      }

      is = new FileInputStream(localReportPath);

      ByteArrayOutputStream baos = new ByteArrayOutputStream();

      byte[] buffer = new byte[4096];
      int numberOfBytesRead;

      while ((numberOfBytesRead = is.read(buffer)) != -1)
      {
        baos.write(buffer, 0, numberOfBytesRead);
      }

      reportTemplate = baos.toByteArray();
    }
    catch (Throwable e)
    {
      throw new ServletException("Failed to load the local report template (" + reportFileName
          + ")", e);
    }
    finally
    {
      try
      {
        if (is != null)
        {
          is.close();
        }
      }
      catch (Throwable e)
      {
        logger.error("Failed to close the input stream while loading the local report template ("
            + reportFileName + ")", e);
      }
    }

    return reportTemplate;
  }
}

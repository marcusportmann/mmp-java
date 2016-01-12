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

package guru.mmp.application.web.servlets;

import guru.mmp.application.reporting.IReportingDAO;
import guru.mmp.application.reporting.IReportingService;
import guru.mmp.application.reporting.ReportDefinition;
import guru.mmp.application.reporting.ReportType;
import guru.mmp.application.web.WebSession;
import guru.mmp.application.web.template.TemplateReportingSecurity;
import guru.mmp.common.util.ResourceUtil;
import guru.mmp.common.util.StringUtil;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * The <code>ViewReportServlet</code> class implements the servlet used to view reports.
 *
 * @author Marcus Portmann
 */
public class ViewReportServlet
  extends HttpServlet
{
  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(ViewReportServlet.class);

  private static final long serialVersionUID = 1000000;

  /**
   * The real path to the WEB-INF/report folder where the local Jasper reports are stored.
   */
  private String localReportFolderPath;

  /* Reporting DAO */
  @Inject
  private IReportingDAO reportingDAO;

  /* Reporting Service */
  @Inject
  private IReportingService reportingService;

  /**
   * @see HttpServlet#HttpServlet()
   */
  public ViewReportServlet()
  {
    super();

    System.setProperty("swing.defaultlaf", "javax.swing.plaf.metal.MetalLookAndFeel");
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
          ViewReportParameters viewReportParameters = webSession.getViewReportParameters(
            viewReportParametersId);

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
                try (Connection connection = reportingDAO.getDataSource().getConnection())
                {
                  Map<String, Object> parameters = new HashMap<>();

                  parameters.put("SUBREPORT_DIR", getLocalReportFolderPath());

                  for (Map.Entry<String, Object> reportParameter : viewReportParameters
                    .getReportParameters().entrySet())
                  {
                    parameters.put(reportParameter.getKey(), reportParameter.getValue());
                  }

                  // Generate the report
                  JasperPrint jasperPrint = JasperFillManager.fillReport(new ByteArrayInputStream(
                      getLocalReportTemplate(viewReportParameters.getReportFileNameOrId())),
                    parameters, connection);

                  response.addHeader("content-disposition",
                    "filename=" + viewReportParameters.getReportName() + ".pdf");
                  response.addHeader("Accept-Ranges", "none");

                  response.setContentType("application/pdf");

                  OutputStream out = response.getOutputStream();

                  out.write(JasperExportManager.exportReportToPdf(jasperPrint));
                  out.flush();

                  return;
                }
              }

              // Database Report
              else if (viewReportParameters.getReportType() == ReportType.DATABASE)
              {
                ReportDefinition reportDefinition = reportingService.getReportDefinition(
                  UUID.fromString(viewReportParameters.getReportFileNameOrId()));

                if (reportDefinition != null)
                {
                  try (Connection connection = reportingDAO.getDataSource().getConnection())
                  {
                    // Setup the report parameters
                    Map<String, Object> parameters = new HashMap<>();

                    parameters.put("SUBREPORT_DIR", getLocalReportFolderPath());

                    for (Map.Entry<String, Object> reportParameter : viewReportParameters
                      .getReportParameters().entrySet())
                    {
                      parameters.put(reportParameter.getKey(), reportParameter.getValue());
                    }

                    // Generate the report
                    JasperPrint jasperPrint = JasperFillManager.fillReport(
                      new ByteArrayInputStream(reportDefinition.getTemplate()), parameters,
                      connection);

                    response.addHeader("content-disposition",
                      "filename=" + viewReportParameters.getReportName() + ".pdf");
                    response.addHeader("Accept-Ranges", "none");

                    response.setContentType("application/pdf");

                    OutputStream out = response.getOutputStream();

                    out.write(JasperExportManager.exportReportToPdf(jasperPrint));
                    out.flush();

                    return;
                  }
                }
                else
                {
                  throw new ServletException(
                    String.format("Failed to view the report (%s): The report could not be found",
                      viewReportParameters.getReportFileNameOrId()));
                }
              }
              else
              {
                throw new ServletException(
                  String.format("Failed to view the report (%s): Unknown report type (%s)",
                    viewReportParameters.getReportFileNameOrId(),
                    viewReportParameters.getReportType()));
              }
            }

            // Unknown Report
            else
            {
              throw new ServletException(
                String.format("Access denied when attempting to view the report (%s)",
                  viewReportParameters.getReportFileNameOrId()));
            }
          }
          else
          {
            logger.warn(String.format("Failed to retrieve the ViewReportParameters (%s)",
              viewReportParametersId));
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

      byte[] data = ResourceUtil.getClasspathResource(
        "guru/mmp/application/web/template/resource/image/reportError.png");

      OutputStream out = response.getOutputStream();

      out.write(data);
      out.flush();
    }
    catch (Throwable ignored)
    {
    }
  }

  /**
   * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
   */
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    doGet(request, response);
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
        throw new ServletException(
          String.format("The local report template (%s) does not exist", localReportPath));
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
      throw new ServletException(
        String.format("Failed to load the local report template (%s)", reportFileName), e);
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
        logger.error(String.format(
          "Failed to close the input stream while loading the local report template (%s)",
          reportFileName), e);
      }
    }

    return reportTemplate;
  }
}

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

package guru.mmp.application.web.servlet;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.reporting.ReportType;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>ViewReportParameters</code> class holds the information describing what report should
 * be  rendered by the <code>ViewReportServlet</code> and what parameters should be passed to the
 * report.
 * <p/>
 * NOTE: This class is contained in the <code>mmp-application-wicket</code> project so that it can
 * be referenced by the <code>WebSession</code> class.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public class ViewReportParameters
  implements Serializable
{
  private static final long serialVersionUID = 1000000;

  /**
   * The system-generated ID uniquely identifying the report parameters.
   */
  private String id;

  /**
   * The name of the local report file under the WEB-INF/report folder for the web project or the
   * ID of the report stored in the database.
   */
  private String reportFileNameOrId;

  /**
   * The name of the report.
   */
  private String reportName;

  /**
   * The report parameters.
   */
  private Map<String, Object> reportParameters;

  /**
   * The type of report being rendered i.e. a local report under the WEB-INF/report folder for the
   * web project or a report stored in the database.
   */
  private ReportType reportType;

  /**
   * Constructs a new <code>ViewReportParameters</code>.
   *
   * @param reportName         the name of the report
   * @param reportType         the type of report being rendered i.e. a local report under the
   *                           WEB-INF/report folder for the web project or a report stored in the
   *                           database
   * @param reportFileNameOrId the name of the local report file under the WEB-INF/report folder for
   *                           the web project or the ID of the report stored in the database
   * @param reportParameters   the report parameters
   */
  public ViewReportParameters(String reportName, ReportType reportType, String reportFileNameOrId,
      Map<String, Object> reportParameters)
  {
    this.id = "ReportParameters-" + UUID.randomUUID().toString();
    this.reportName = reportName;
    this.reportType = reportType;
    this.reportFileNameOrId = reportFileNameOrId;
    this.reportParameters = reportParameters;
  }

  /**
   * Returns the system-generated ID uniquely identifying the report parameters.
   *
   * @return the system-generated ID uniquely identifying the report parameters
   */
  public String getId()
  {
    return id;
  }

  /**
   * Returns the name of the local report file under the WEB-INF/report folder for the web project
   * or the ID of the report stored in the database.
   *
   * @return the name of the local report file under the WEB-INF/report folder for the web project
   *         or the ID of the report stored in the database
   */
  public String getReportFileNameOrId()
  {
    return reportFileNameOrId;
  }

  /**
   * Returns the name of the report.
   *
   * @return the name of the report
   */
  public String getReportName()
  {
    return reportName;
  }

  /**
   * Returns the report parameters.
   *
   * @return the report parameters
   */
  public Map<String, Object> getReportParameters()
  {
    return reportParameters;
  }

  /**
   * Returns the type of report being rendered i.e. a local report under the WEB-INF/report folder
   * for the web project or a report stored in the database.
   *
   * @return the type of report being rendered i.e. a local report under the WEB-INF/report folder
   *         for the web project or a report stored in the database
   */
  public ReportType getReportType()
  {
    return reportType;
  }

  /**
   * Set the name of the local report file under the WEB-INF/report folder for the web project or
   * the ID of the report stored in the database.
   *
   * @param reportFileNameOrId the name of the local report file under the WEB-INF/report folder for
   *                           the web project or the ID of the report stored in the database
   */
  public void setReportFileNameOrIdx(String reportFileNameOrId)
  {
    this.reportFileNameOrId = reportFileNameOrId;
  }

  /**
   * Set the name of the report.
   *
   * @param reportName the name of the report
   */
  public void setReportName(String reportName)
  {
    this.reportName = reportName;
  }

  /**
   * Set the report parameters.
   *
   * @param reportParameters the report parameters
   */
  public void setReportParameters(Map<String, Object> reportParameters)
  {
    this.reportParameters = reportParameters;
  }

  /**
   * Set the type of report being rendered i.e. a local report under the WEB-INF/report folder for
   * the web project or a report stored in the database.
   *
   * @param reportType the type of report being rendered i.e. a local report under the
   *                   WEB-INF/report folder for the web project or a report stored in the database
   */
  public void setReportType(ReportType reportType)
  {
    this.reportType = reportType;
  }
}

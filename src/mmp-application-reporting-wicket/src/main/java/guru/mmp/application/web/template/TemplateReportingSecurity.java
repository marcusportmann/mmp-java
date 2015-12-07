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

package guru.mmp.application.web.template;

/**
 * The <code>TemplateReportingSecurity</code> class provides access to the security-related
 * information and functionality for the extensions to the Web Application Template provided by the
 * <code>mmp-application-reporting-wicket</code> library.
 *
 * @author Marcus Portmann
 */
public class TemplateReportingSecurity
{
  /** The Application.AddReportDefinition function code. */
  public static final String FUNCTION_CODE_ADD_REPORT_DEFINITION =
    "ApplicationReporting.AddReportDefinition";

  /** The Application.RemoveReportDefinition function code. */
  public static final String FUNCTION_CODE_REMOVE_REPORT_DEFINITION =
    "ApplicationReporting.RemoveReportDefinition";

  /** The Application.ReportDefinitionAdministration function code. */
  public static final String FUNCTION_CODE_REPORT_DEFINITION_ADMINISTRATION =
    "ApplicationReporting.ReportDefinitionAdministration";

  /** The Application.UpdateReportDefinition function code. */
  public static final String FUNCTION_CODE_UPDATE_REPORT_DEFINITION =
    "ApplicationReporting.UpdateReportDefinition";

  /** The Application.ViewReport function code. */
  public static final String FUNCTION_CODE_VIEW_REPORT = "ApplicationReporting.ViewReport";
}
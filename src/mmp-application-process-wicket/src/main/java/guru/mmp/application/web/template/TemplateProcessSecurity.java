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
 * The <code>TemplateProcessSecurity</code> class provides access to the security-related
 * information and functionality for the extensions to the Web Application Template provided by the
 * <code>mmp-application-process-wicket</code> library.
 *
 * @author Marcus Portmann
 */
public class TemplateProcessSecurity
{
  /** The Application.AddProcessDefinition function code. */
  public static final String FUNCTION_CODE_ADD_PROCESS_DEFINITION =
    "ApplicationProcess.AddProcessDefinition";

  /** The Application.RemoveProcessDefinition function code. */
  public static final String FUNCTION_CODE_REMOVE_PROCESS_DEFINITION =
    "ApplicationProcess.RemoveProcessDefinition";

  /** The Application.ProcessDefinitionAdministration function code. */
  public static final String FUNCTION_CODE_PROCESS_DEFINITION_ADMINISTRATION =
    "ApplicationProcess.ProcessDefinitionAdministration";

  /** The Application.UpdateProcessDefinition function code. */
  public static final String FUNCTION_CODE_UPDATE_PROCESS_DEFINITION =
    "ApplicationProcess.UpdateProcessDefinition";

  /** The Application.ViewProcess function code. */
  public static final String FUNCTION_CODE_VIEW_PROCESS = "ApplicationProcess.ViewProcess";
}

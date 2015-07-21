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
 * The <code>TemplateSecurity</code> class provides access to the security-related information
 * and functionality for the Web Application Template. This includes a list of the authorised
 * function codes.
 *
 * @author Marcus Portmann
 */
public class TemplateSecurity
{
  /** The unique ID for the <b>Administrators</b> group. */
  public static final int ADMINISTRATORS_GROUP_ID = 1;

  /** The group name for the <b>Administrators</b> group. */
  public static final String ADMINISTRATORS_GROUP_NAME = "Administrators";

  /** The Application.AddCode function code. */
  public static final String FUNCTION_CODE_ADD_CODE = "Application.AddCode";

  /** The Application.AddCodeCategory function code. */
  public static final String FUNCTION_CODE_ADD_CODE_CATEGORY = "Application.AddCodeCategory";

  /** The Application.AddGroup function code. */
  public static final String FUNCTION_CODE_ADD_GROUP = "Application.AddGroup";

  /** The Application.AddOrganisation function code. */
  public static final String FUNCTION_CODE_ADD_ORGANISATION = "Application.AddOrganisation";

  /** The Application.AddUser function code. */
  public static final String FUNCTION_CODE_ADD_USER = "Application.AddUser";

  /** The Application.CodeAdministration function code. */
  public static final String FUNCTION_CODE_CODE_ADMINISTRATION = "Application.CodeAdministration";

  /** The Application.CodeCategoryAdministration function code. */
  public static final String FUNCTION_CODE_CODE_CATEGORY_ADMINISTRATION =
    "Application.CodeCategoryAdministration";

  /** The Application.Dashboard function code. */
  public static final String FUNCTION_CODE_DASHBOARD = "Application.Dashboard";

  /** The Application.GroupAdministration function code. */
  public static final String FUNCTION_CODE_GROUP_ADMINISTRATION = "Application.GroupAdministration";

  /** The Application.OrganisationAdministration function code. */
  public static final String FUNCTION_CODE_ORGANISATION_ADMINISTRATION =
    "Application.OrganisationAdministration";

  /** The Application.RemoveCode function code. */
  public static final String FUNCTION_CODE_REMOVE_CODE = "Application.RemoveCode";

  /** The Application.RemoveCodeCategory function code. */
  public static final String FUNCTION_CODE_REMOVE_CODE_CATEGORY = "Application.RemoveCodeCategory";

  /** The Application.RemoveGroup function code. */
  public static final String FUNCTION_CODE_REMOVE_GROUP = "Application.RemoveGroup";

  /** The Application.RemoveOrganisation function code. */
  public static final String FUNCTION_CODE_REMOVE_ORGANISATION = "Application.RemoveOrganisation";

  /** The Application.RemoveUser function code. */
  public static final String FUNCTION_CODE_REMOVE_USER = "Application.RemoveUser";

  /** The Application.ResetUserPassword function code. */
  public static final String FUNCTION_CODE_RESET_USER_PASSWORD = "Application.ResetUserPassword";

  /** The Application.SecureHome function code. */
  public static final String FUNCTION_CODE_SECURE_HOME = "Application.SecureHome";

  /** The Application.UpdateCode function code. */
  public static final String FUNCTION_CODE_UPDATE_CODE = "Application.UpdateCode";

  /** The Application.UpdateCodeCategory function code. */
  public static final String FUNCTION_CODE_UPDATE_CODE_CATEGORY = "Application.UpdateCodeCategory";

  /** The Application.UpdateGroup function code. */
  public static final String FUNCTION_CODE_UPDATE_GROUP = "Application.UpdateGroup";

  /** The Application.UpdateOrganisation function code. */
  public static final String FUNCTION_CODE_UPDATE_ORGANISATION = "Application.UpdateOrganisation";

  /** The Application.UpdateUser function code. */
  public static final String FUNCTION_CODE_UPDATE_USER = "Application.UpdateUser";

  /** The Application.UserAdministration function code. */
  public static final String FUNCTION_CODE_USER_ADMINISTRATION = "Application.UserAdministration";

  /** The Application.UserGroups function code. */
  public static final String FUNCTION_CODE_USER_GROUPS = "Application.UserGroups";

  /** The unique ID for the <b>Organisation Administrators</b> group. */
  public static final int ORGANISATION_ADMINISTRATORS_GROUP_ID = 2;

  /** The group name for the <b>Organisation Administrators</b> group. */
  public static final String ORGANISATION_ADMINISTRATORS_GROUP_NAME = "Organisation Administrators";
}

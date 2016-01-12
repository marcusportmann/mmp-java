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

package guru.mmp.application.web.template.components;

import guru.mmp.application.security.UserDirectory;
import guru.mmp.application.web.WebApplicationException;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import java.util.Map;

/**
 * The <code>UserDirectoryAdministrationPanel</code> class implements the Wicket component used to
 * administer the configuration for the internal user directory type.
 *
 * @author Marcus Portmann
 */
public class InternalUserDirectoryAdministrationPanel
  extends UserDirectoryAdministrationPanel
{
  /**
   * Constructs a new <code>InternalUserDirectoryAdministrationPanel</code>.
   *
   * @param id                 the non-null id of this component
   * @param userDirectoryModel the model for the user directory
   */
  public InternalUserDirectoryAdministrationPanel(
    String id, IModel<UserDirectory> userDirectoryModel)
  {
    super(id, userDirectoryModel);

    try
    {
      // The "maxPasswordAttempts" field
      TextField<String> maxPasswordAttemptsField = new TextFieldWithFeedback<>(
        "maxPasswordAttempts",
        new PropertyModel<>(userDirectoryModel, "parameters.MaxPasswordAttempts"));
      maxPasswordAttemptsField.setType(String.class);
      maxPasswordAttemptsField.setRequired(true);
      add(maxPasswordAttemptsField);

      // The "passwordExpiryMonths" field
      TextField<String> passwordExpiryMonthsField = new TextFieldWithFeedback<>(
        "passwordExpiryMonths",
        new PropertyModel<>(userDirectoryModel, "parameters.PasswordExpiryMonths"));
      passwordExpiryMonthsField.setType(String.class);
      passwordExpiryMonthsField.setRequired(true);
      add(passwordExpiryMonthsField);

      // The "passwordHistoryMonths" field
      TextField<String> passwordHistoryMonthsField = new TextFieldWithFeedback<>(
        "passwordHistoryMonths",
        new PropertyModel<>(userDirectoryModel, "parameters.PasswordHistoryMonths"));
      passwordHistoryMonthsField.setType(String.class);
      passwordHistoryMonthsField.setRequired(true);
      add(passwordHistoryMonthsField);

      // The "maxFilteredUsers" field
      TextField<String> maxFilteredUsersField = new TextFieldWithFeedback<>("maxFilteredUsers",
        new PropertyModel<>(userDirectoryModel, "parameters.MaxFilteredUsers"));
      maxFilteredUsersField.setType(String.class);
      maxFilteredUsersField.setRequired(true);
      add(maxFilteredUsersField);
    }
    catch (Throwable e)
    {
      throw new WebApplicationException(
        "Failed to initialise the InternalUserDirectoryAdministrationPanel", e);
    }
  }

  /**
   * Initialise the user directory parameters.
   *
   * @param parameters the user directory parameters
   */
  protected void initParameters(Map<String, String> parameters)
  {
    if (!parameters.containsKey("MaxPasswordAttempts"))
    {
      parameters.put("MaxPasswordAttempts", "5");
    }

    if (!parameters.containsKey("PasswordExpiryMonths"))
    {
      parameters.put("PasswordExpiryMonths", "12");
    }

    if (!parameters.containsKey("PasswordHistoryMonths"))
    {
      parameters.put("PasswordHistoryMonths", "24");
    }

    if (!parameters.containsKey("MaxFilteredUsers"))
    {
      parameters.put("MaxFilteredUsers", "100");
    }
  }
}

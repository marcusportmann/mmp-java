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

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.security.UserDirectory;
import guru.mmp.application.web.WebApplicationException;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import java.util.Map;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>LDAPUserDirectoryAdministrationPanel</code> class implements the Wicket component used
 * to administer the configuration for the LDAP user directory type.
 *
 * @author Marcus Portmann
 */
public class LDAPUserDirectoryAdministrationPanel extends UserDirectoryAdministrationPanel
{
  /**
   * Constructs a new <code>LDAPUserDirectoryAdministrationPanel</code>.
   *
   * @param id                 the non-null id of this component
   * @param userDirectoryModel the model for the user directory
   */
  public LDAPUserDirectoryAdministrationPanel(String id, IModel<UserDirectory> userDirectoryModel)
  {
    super(id, userDirectoryModel);

    try
    {
      // The "host" field
      TextField<String> hostField = new TextFieldWithFeedback<>("host", new PropertyModel<>(
          userDirectoryModel, "parameters.Host"));
      hostField.setType(String.class);
      hostField.setRequired(true);
      add(hostField);

      // The "port" field
      TextField<String> portField = new TextFieldWithFeedback<>("port", new PropertyModel<>(
          userDirectoryModel, "parameters.Port"));
      portField.setType(String.class);
      portField.setRequired(true);
      add(portField);

      // The "useSSL" field
      TextField<String> useSSLField = new TextFieldWithFeedback<>("useSSL", new PropertyModel<>(
          userDirectoryModel, "parameters.UseSSL"));
      useSSLField.setType(String.class);
      useSSLField.setRequired(true);
      add(useSSLField);

      // The "bindDN" field
      TextField<String> bindDNField = new TextFieldWithFeedback<>("bindDN", new PropertyModel<>(
          userDirectoryModel, "parameters.BindDN"));
      bindDNField.setType(String.class);
      bindDNField.setRequired(true);
      add(bindDNField);

      // The "bindPassword" field
      TextField<String> bindPasswordField = new TextFieldWithFeedback<>("bindPassword",
          new PropertyModel<>(userDirectoryModel, "parameters.BindPassword"));
      bindPasswordField.setType(String.class);
      bindPasswordField.setRequired(true);
      add(bindPasswordField);

      // The "baseDN" field
      TextField<String> baseDNField = new TextFieldWithFeedback<>("baseDN", new PropertyModel<>(
          userDirectoryModel, "parameters.BaseDN"));
      baseDNField.setType(String.class);
      baseDNField.setRequired(true);
      add(baseDNField);

      // The "userBaseDN" field
      TextField<String> userBaseDNField = new TextFieldWithFeedback<>("userBaseDN",
          new PropertyModel<>(userDirectoryModel, "parameters.UserBaseDN"));
      userBaseDNField.setType(String.class);
      userBaseDNField.setRequired(true);
      add(userBaseDNField);

      // The "groupBaseDN" field
      TextField<String> groupBaseDNField = new TextFieldWithFeedback<>("groupBaseDN",
          new PropertyModel<>(userDirectoryModel, "parameters.GroupBaseDN"));
      groupBaseDNField.setType(String.class);
      groupBaseDNField.setRequired(true);
      add(groupBaseDNField);

      // The "sharedBaseDN" field
      TextField<String> sharedBaseDNField = new TextFieldWithFeedback<>("sharedBaseDN",
          new PropertyModel<>(userDirectoryModel, "parameters.SharedBaseDN"));
      sharedBaseDNField.setType(String.class);
      sharedBaseDNField.setRequired(false);
      add(sharedBaseDNField);

      // The "userObjectClass" field
      TextField<String> userObjectClassField = new TextFieldWithFeedback<>("userObjectClass",
          new PropertyModel<>(userDirectoryModel, "parameters.UserObjectClass"));
      userObjectClassField.setType(String.class);
      userObjectClassField.setRequired(true);
      add(userObjectClassField);

      // The "userUsernameAttribute" field
      TextField<String> userUsernameAttributeField = new TextFieldWithFeedback<>(
          "userUsernameAttribute", new PropertyModel<>(userDirectoryModel,
          "parameters.UserUsernameAttribute"));
      userUsernameAttributeField.setType(String.class);
      userUsernameAttributeField.setRequired(true);
      add(userUsernameAttributeField);

      // The "userPasswordExpiryAttribute" field
      TextField<String> userPasswordExpiryAttributeField = new TextFieldWithFeedback<>(
          "userPasswordExpiryAttribute", new PropertyModel<>(userDirectoryModel,
          "parameters.UserPasswordExpiryAttribute"));
      userPasswordExpiryAttributeField.setType(String.class);
      userPasswordExpiryAttributeField.setRequired(true);
      add(userPasswordExpiryAttributeField);

      // The "userPasswordAttemptsAttribute" field
      TextField<String> userPasswordAttemptsAttributeField = new TextFieldWithFeedback<>(
          "userPasswordAttemptsAttribute", new PropertyModel<>(userDirectoryModel,
          "parameters.UserPasswordAttemptsAttribute"));
      userPasswordAttemptsAttributeField.setType(String.class);
      userPasswordAttemptsAttributeField.setRequired(true);
      add(userPasswordAttemptsAttributeField);

      // The "userTitleAttribute" field
      TextField<String> userTitleAttributeField = new TextFieldWithFeedback<>(
        "userTitleAttribute", new PropertyModel<>(userDirectoryModel,
        "parameters.UserTitleAttribute"));
      userTitleAttributeField.setType(String.class);
      userTitleAttributeField.setRequired(true);
      add(userTitleAttributeField);

      // The "userFirstNamesAttribute" field
      TextField<String> userFirstNamesAttributeField = new TextFieldWithFeedback<>(
          "userFirstNamesAttribute", new PropertyModel<>(userDirectoryModel,
          "parameters.UserFirstNamesAttribute"));
      userFirstNamesAttributeField.setType(String.class);
      userFirstNamesAttributeField.setRequired(true);
      add(userFirstNamesAttributeField);

      // The "userLastNameAttribute" field
      TextField<String> userLastNameAttributeField = new TextFieldWithFeedback<>(
          "userLastNameAttribute", new PropertyModel<>(userDirectoryModel,
          "parameters.UserLastNameAttribute"));
      userLastNameAttributeField.setType(String.class);
      userLastNameAttributeField.setRequired(true);
      add(userLastNameAttributeField);

      // The "userPhoneNumberAttribute" field
      TextField<String> userPhoneNumberAttributeField = new TextFieldWithFeedback<>(
        "userPhoneNumberAttribute", new PropertyModel<>(userDirectoryModel,
        "parameters.UserPhoneNumberAttribute"));
      userPhoneNumberAttributeField.setType(String.class);
      userPhoneNumberAttributeField.setRequired(true);
      add(userPhoneNumberAttributeField);

      // The "userMobileNumberAttribute" field
      TextField<String> userMobileNumberAttributeField = new TextFieldWithFeedback<>(
          "userMobileNumberAttribute", new PropertyModel<>(userDirectoryModel,
          "parameters.UserMobileNumberAttribute"));
      userMobileNumberAttributeField.setType(String.class);
      userMobileNumberAttributeField.setRequired(true);
      add(userMobileNumberAttributeField);

      // The "userEmailAttribute" field
      TextField<String> userEmailAttributeField = new TextFieldWithFeedback<>("userEmailAttribute",
          new PropertyModel<>(userDirectoryModel, "parameters.UserEmailAttribute"));
      userEmailAttributeField.setType(String.class);
      userEmailAttributeField.setRequired(true);
      add(userEmailAttributeField);

      // The "userPasswordHistoryAttribute" field
      TextField<String> userPasswordHistoryAttributeField = new TextFieldWithFeedback<>(
          "userPasswordHistoryAttribute", new PropertyModel<>(userDirectoryModel,
          "parameters.UserPasswordHistoryAttribute"));
      userPasswordHistoryAttributeField.setType(String.class);
      userPasswordHistoryAttributeField.setRequired(true);
      add(userPasswordHistoryAttributeField);

      // The "groupObjectClass" field
      TextField<String> groupObjectClassField = new TextFieldWithFeedback<>("groupObjectClass",
          new PropertyModel<>(userDirectoryModel, "parameters.GroupObjectClass"));
      groupObjectClassField.setType(String.class);
      groupObjectClassField.setRequired(true);
      add(groupObjectClassField);

      // The "groupNameAttribute" field
      TextField<String> groupNameAttributeField = new TextFieldWithFeedback<>("groupNameAttribute",
          new PropertyModel<>(userDirectoryModel, "parameters.GroupNameAttribute"));
      groupNameAttributeField.setType(String.class);
      groupNameAttributeField.setRequired(true);
      add(groupNameAttributeField);

      // The "groupMemberAttribute" field
      TextField<String> groupMemberAttributeField = new TextFieldWithFeedback<>(
          "groupMemberAttribute", new PropertyModel<>(userDirectoryModel,
          "parameters.GroupMemberAttribute"));
      groupMemberAttributeField.setType(String.class);
      groupMemberAttributeField.setRequired(true);
      add(groupMemberAttributeField);

      // The "groupDescriptionAttribute" field
      TextField<String> groupDescriptionAttributeField = new TextFieldWithFeedback<>(
          "groupDescriptionAttribute", new PropertyModel<>(userDirectoryModel,
          "parameters.GroupDescriptionAttribute"));
      groupDescriptionAttributeField.setType(String.class);
      groupDescriptionAttributeField.setRequired(true);
      add(groupDescriptionAttributeField);

      // The "maxPasswordAttempts" field
      TextField<String> maxPasswordAttemptsField = new TextFieldWithFeedback<>(
          "maxPasswordAttempts", new PropertyModel<>(userDirectoryModel,
          "parameters.MaxPasswordAttempts"));
      maxPasswordAttemptsField.setType(String.class);
      maxPasswordAttemptsField.setRequired(true);
      add(maxPasswordAttemptsField);

      // The "passwordExpiryMonths" field
      TextField<String> passwordExpiryMonthsField = new TextFieldWithFeedback<>(
          "passwordExpiryMonths", new PropertyModel<>(userDirectoryModel,
          "parameters.PasswordExpiryMonths"));
      passwordExpiryMonthsField.setType(String.class);
      passwordExpiryMonthsField.setRequired(true);
      add(passwordExpiryMonthsField);

      // The "supportPasswordHistory" field
      TextField<String> supportPasswordHistoryField = new TextFieldWithFeedback<>(
          "supportPasswordHistory", new PropertyModel<>(userDirectoryModel,
          "parameters.SupportPasswordHistory"));
      supportPasswordHistoryField.setType(String.class);
      supportPasswordHistoryField.setRequired(true);
      add(supportPasswordHistoryField);

      // The "passwordHistoryMonths" field
      TextField<String> passwordHistoryMonthsField = new TextFieldWithFeedback<>(
          "passwordHistoryMonths", new PropertyModel<>(userDirectoryModel,
          "parameters.PasswordHistoryMonths"));
      passwordHistoryMonthsField.setType(String.class);
      passwordHistoryMonthsField.setRequired(true);
      add(passwordHistoryMonthsField);

      // The "passwordHistoryMaxLength" field
      TextField<String> passwordHistoryMaxLengthField = new TextFieldWithFeedback<>(
          "passwordHistoryMaxLength", new PropertyModel<>(userDirectoryModel,
          "parameters.PasswordHistoryMaxLength"));
      passwordHistoryMaxLengthField.setType(String.class);
      passwordHistoryMaxLengthField.setRequired(true);
      add(passwordHistoryMaxLengthField);

      // The "maxFilteredUsers" field
      TextField<String> maxFilteredUsersField = new TextFieldWithFeedback<>("maxFilteredUsers",
          new PropertyModel<>(userDirectoryModel, "parameters.MaxFilteredUsers"));
      maxFilteredUsersField.setType(String.class);
      maxFilteredUsersField.setRequired(true);
      add(maxFilteredUsersField);

      // The "maxFilteredGroups" field
      TextField<String> maxFilteredGroupsField = new TextFieldWithFeedback<>("maxFilteredGroups",
          new PropertyModel<>(userDirectoryModel, "parameters.MaxFilteredGroups"));
      maxFilteredGroupsField.setType(String.class);
      maxFilteredGroupsField.setRequired(true);
      add(maxFilteredGroupsField);
    }
    catch (Throwable e)
    {
      throw new WebApplicationException(
          "Failed to initialise the LDAPUserDirectoryAdministrationPanel", e);
    }
  }

  /**
   * Initialise the user directory parameters.
   *
   * @param parameters the user directory parameters
   */
  protected void initParameters(Map<String, String> parameters)
  {
    if (!parameters.containsKey("Host"))
    {
      parameters.put("Host", "localhost");
    }

    if (!parameters.containsKey("Port"))
    {
      parameters.put("Port", "389");
    }

    if (!parameters.containsKey("UseSSL"))
    {
      parameters.put("UseSSL", "false");
    }

    if (!parameters.containsKey("BindDN"))
    {
      parameters.put("BindDN", "cn=root");
    }

    if (!parameters.containsKey("BindPassword"))
    {
      parameters.put("BindPassword", "Password1");
    }

    if (!parameters.containsKey("BaseDN"))
    {
      parameters.put("BaseDN", "");
    }

    if (!parameters.containsKey("UserBaseDN"))
    {
      parameters.put("UserBaseDN", "");
    }

    if (!parameters.containsKey("GroupBaseDN"))
    {
      parameters.put("GroupBaseDN", "");
    }

    if (!parameters.containsKey("SharedBaseDN"))
    {
      parameters.put("SharedBaseDN", "");
    }

    if (!parameters.containsKey("UserObjectClass"))
    {
      parameters.put("UserObjectClass", "inetOrgPerson");
    }

    if (!parameters.containsKey("UsernameAttribute"))
    {
      parameters.put("UsernameAttribute", "uid");
    }

    if (!parameters.containsKey("UserPasswordExpiryAttribute"))
    {
      parameters.put("UserPasswordExpiryAttribute", "passwordexpiry");
    }

    if (!parameters.containsKey("UserPasswordAttemptsAttribute"))
    {
      parameters.put("UserPasswordAttemptsAttribute", "passwordattempts");
    }

    if (!parameters.containsKey("UserPasswordHistoryAttribute"))
    {
      parameters.put("UserPasswordHistoryAttribute", "passwordhistory");
    }

    if (!parameters.containsKey("UserTitleAttribute"))
    {
      parameters.put("UserTitleAttribute", "title");
    }

    if (!parameters.containsKey("UserFirstNamesAttribute"))
    {
      parameters.put("UserFirstNamesAttribute", "givenName");
    }

    if (!parameters.containsKey("UserLastNameAttribute"))
    {
      parameters.put("UserLastNameAttribute", "sn");
    }

    if (!parameters.containsKey("UserPhoneNumberAttribute"))
    {
      parameters.put("UserPhoneNumberAttribute", "telephoneNumber");
    }

    if (!parameters.containsKey("UserMobileNumberAttribute"))
    {
      parameters.put("UserMobileNumberAttribute", "mobile");
    }

    if (!parameters.containsKey("UserEmailAttribute"))
    {
      parameters.put("UserEmailAttribute", "mail");
    }

    if (!parameters.containsKey("GroupObjectClass"))
    {
      parameters.put("GroupObjectClass", "groupOfNames");
    }

    if (!parameters.containsKey("GroupNameAttribute"))
    {
      parameters.put("GroupNameAttribute", "cn");
    }

    if (!parameters.containsKey("GroupMemberAttribute"))
    {
      parameters.put("GroupMemberAttribute", "member");
    }

    if (!parameters.containsKey("GroupDescriptionAttribute"))
    {
      parameters.put("GroupDescriptionAttribute", "description");
    }

    if (!parameters.containsKey("MaxPasswordAttempts"))
    {
      parameters.put("MaxPasswordAttempts", "5");
    }

    if (!parameters.containsKey("PasswordExpiryMonths"))
    {
      parameters.put("PasswordExpiryMonths", "12");
    }

    if (!parameters.containsKey("SupportPasswordHistory"))
    {
      parameters.put("SupportPasswordHistory", "true");
    }

    if (!parameters.containsKey("PasswordHistoryMonths"))
    {
      parameters.put("PasswordHistoryMonths", "24");
    }

    if (!parameters.containsKey("PasswordHistoryMaxLength"))
    {
      parameters.put("PasswordHistoryMaxLength", "128");
    }

    if (!parameters.containsKey("MaxFilteredUsers"))
    {
      parameters.put("MaxFilteredUsers", "100");
    }

    if (!parameters.containsKey("MaxFilteredGroups"))
    {
      parameters.put("MaxFilteredGroups", "100");
    }
  }
}

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

package guru.mmp.application.web.template.component;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.security.UserDirectory;

import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

//~--- JDK imports ------------------------------------------------------------

import java.util.Map;

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

    // The "host" field
    TextField<String> hostField = new TextFieldWithFeedback<>("host",
      new PropertyModel<>(userDirectoryModel, "parameters.Host"));
    hostField.setType(String.class);
    hostField.setRequired(true);
    add(hostField);

    // The "port" field
    TextField<String> portField = new TextFieldWithFeedback<>("port",
      new PropertyModel<>(userDirectoryModel, "parameters.Port"));
    portField.setType(String.class);
    portField.setRequired(true);
    add(portField);

    // The "useSSL" field
    TextField<String> useSSLField = new TextFieldWithFeedback<>("useSSL",
      new PropertyModel<>(userDirectoryModel, "parameters.UseSSL"));
    useSSLField.setType(String.class);
    useSSLField.setRequired(true);
    add(useSSLField);

    // The "bindDN" field
    TextField<String> bindDNField = new TextFieldWithFeedback<>("bindDN",
      new PropertyModel<>(userDirectoryModel, "parameters.BindDN"));
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
    TextField<String> baseDNField = new TextFieldWithFeedback<>("baseDN",
      new PropertyModel<>(userDirectoryModel, "parameters.BaseDN"));
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

    // The "usernameAttribute" field
    TextField<String> usernameAttributeField = new TextFieldWithFeedback<>("usernameAttribute",
      new PropertyModel<>(userDirectoryModel, "parameters.UsernameAttribute"));
    usernameAttributeField.setType(String.class);
    usernameAttributeField.setRequired(true);
    add(usernameAttributeField);

    // The "passwordExpiryAttribute" field
    TextField<String> passwordExpiryAttributeField =
      new TextFieldWithFeedback<>("passwordExpiryAttribute",
        new PropertyModel<>(userDirectoryModel, "parameters.PasswordExpiryAttribute"));
    passwordExpiryAttributeField.setType(String.class);
    passwordExpiryAttributeField.setRequired(true);
    add(passwordExpiryAttributeField);

    // The "passwordAttemptsAttribute" field
    TextField<String> passwordAttemptsAttributeField =
      new TextFieldWithFeedback<>("passwordAttemptsAttribute",
        new PropertyModel<>(userDirectoryModel, "parameters.PasswordAttemptsAttribute"));
    passwordAttemptsAttributeField.setType(String.class);
    passwordAttemptsAttributeField.setRequired(true);
    add(passwordAttemptsAttributeField);

    // The "titleAttribute" field
    TextField<String> titleAttributeField = new TextFieldWithFeedback<>("titleAttribute",
      new PropertyModel<>(userDirectoryModel, "parameters.TitleAttribute"));
    titleAttributeField.setType(String.class);
    titleAttributeField.setRequired(false);
    add(titleAttributeField);

    // The "firstNamesAttribute" field
    TextField<String> firstNamesAttributeField = new TextFieldWithFeedback<>("firstNamesAttribute",
      new PropertyModel<>(userDirectoryModel, "parameters.FirstNamesAttribute"));
    firstNamesAttributeField.setType(String.class);
    firstNamesAttributeField.setRequired(true);
    add(firstNamesAttributeField);

    // The "lastNameAttribute" field
    TextField<String> lastNameAttributeField = new TextFieldWithFeedback<>("lastNameAttribute",
      new PropertyModel<>(userDirectoryModel, "parameters.LastNameAttribute"));
    lastNameAttributeField.setType(String.class);
    lastNameAttributeField.setRequired(true);
    add(lastNameAttributeField);

    // The "phoneNumberAttribute" field
    TextField<String> phoneNumberAttributeField =
      new TextFieldWithFeedback<>("phoneNumberAttribute",
        new PropertyModel<>(userDirectoryModel, "parameters.PhoneNumberAttribute"));
    phoneNumberAttributeField.setType(String.class);
    phoneNumberAttributeField.setRequired(false);
    add(phoneNumberAttributeField);

    // The "faxNumberAttribute" field
    TextField<String> faxNumberAttributeField = new TextFieldWithFeedback<>("faxNumberAttribute",
      new PropertyModel<>(userDirectoryModel, "parameters.FaxNumberAttribute"));
    faxNumberAttributeField.setType(String.class);
    faxNumberAttributeField.setRequired(false);
    add(faxNumberAttributeField);

    // The "mobileNumberAttribute" field
    TextField<String> mobileNumberAttributeField =
      new TextFieldWithFeedback<>("mobileNumberAttribute",
        new PropertyModel<>(userDirectoryModel, "parameters.MobileNumberAttribute"));
    mobileNumberAttributeField.setType(String.class);
    mobileNumberAttributeField.setRequired(true);
    add(mobileNumberAttributeField);

    // The "emailAttribute" field
    TextField<String> emailAttributeField = new TextFieldWithFeedback<>("emailAttribute",
      new PropertyModel<>(userDirectoryModel, "parameters.EmailAttribute"));
    emailAttributeField.setType(String.class);
    emailAttributeField.setRequired(true);
    add(emailAttributeField);

    // The "descriptionAttribute" field
    TextField<String> descriptionAttributeField =
      new TextFieldWithFeedback<>("descriptionAttribute",
        new PropertyModel<>(userDirectoryModel, "parameters.DescriptionAttribute"));
    descriptionAttributeField.setType(String.class);
    descriptionAttributeField.setRequired(false);
    add(descriptionAttributeField);

    // The "passwordHistoryAttribute" field
    TextField<String> passwordHistoryAttributeField =
      new TextFieldWithFeedback<>("passwordHistoryAttribute",
        new PropertyModel<>(userDirectoryModel, "parameters.PasswordHistoryAttribute"));
    passwordHistoryAttributeField.setType(String.class);
    passwordHistoryAttributeField.setRequired(true);
    add(passwordHistoryAttributeField);

    // The "groupObjectClass" field
    TextField<String> groupObjectClassField = new TextFieldWithFeedback<>("groupObjectClass",
      new PropertyModel<>(userDirectoryModel, "parameters.GroupObjectClass"));
    groupObjectClassField.setType(String.class);
    groupObjectClassField.setRequired(true);
    add(groupObjectClassField);

    // The "groupAttribute" field
    TextField<String> groupAttributeField = new TextFieldWithFeedback<>("groupAttribute",
      new PropertyModel<>(userDirectoryModel, "parameters.GroupAttribute"));
    groupAttributeField.setType(String.class);
    groupAttributeField.setRequired(true);
    add(groupAttributeField);

    // The "groupMemberAttribute" field
    TextField<String> groupMemberAttributeField =
      new TextFieldWithFeedback<>("groupMemberAttribute",
        new PropertyModel<>(userDirectoryModel, "parameters.GroupMemberAttribute"));
    groupMemberAttributeField.setType(String.class);
    groupMemberAttributeField.setRequired(true);
    add(groupMemberAttributeField);

    // The "maxPasswordAttempts" field
    TextField<String> maxPasswordAttemptsField = new TextFieldWithFeedback<>("maxPasswordAttempts",
      new PropertyModel<>(userDirectoryModel, "parameters.MaxPasswordAttempts"));
    maxPasswordAttemptsField.setType(String.class);
    maxPasswordAttemptsField.setRequired(true);
    add(maxPasswordAttemptsField);

    // The "passwordExpiryMonths" field
    TextField<String> passwordExpiryMonthsField =
      new TextFieldWithFeedback<>("passwordExpiryMonths",
        new PropertyModel<>(userDirectoryModel, "parameters.PasswordExpiryMonths"));
    passwordExpiryMonthsField.setType(String.class);
    passwordExpiryMonthsField.setRequired(true);
    add(passwordExpiryMonthsField);

    // The "supportPasswordHistory" field
    TextField<String> supportPasswordHistoryField =
      new TextFieldWithFeedback<>("supportPasswordHistory",
        new PropertyModel<>(userDirectoryModel, "parameters.SupportPasswordHistory"));
    supportPasswordHistoryField.setType(String.class);
    supportPasswordHistoryField.setRequired(true);
    add(supportPasswordHistoryField);

    // The "passwordHistoryMonths" field
    TextField<String> passwordHistoryMonthsField =
      new TextFieldWithFeedback<>("passwordHistoryMonths",
        new PropertyModel<>(userDirectoryModel, "parameters.PasswordHistoryMonths"));
    passwordHistoryMonthsField.setType(String.class);
    passwordHistoryMonthsField.setRequired(true);
    add(passwordHistoryMonthsField);

    // The "passwordHistoryMaxLength" field
    TextField<String> passwordHistoryMaxLengthField =
      new TextFieldWithFeedback<>("passwordHistoryMaxLength",
        new PropertyModel<>(userDirectoryModel, "parameters.PasswordHistoryMaxLength"));
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

    if (!parameters.containsKey("PasswordExpiryAttribute"))
    {
      parameters.put("PasswordExpiryAttribute", "passwordexpiry");
    }

    if (!parameters.containsKey("PasswordAttemptsAttribute"))
    {
      parameters.put("PasswordAttemptsAttribute", "passwordattempts");
    }

    if (!parameters.containsKey("PasswordHistoryAttribute"))
    {
      parameters.put("PasswordHistoryAttribute", "passwordhistory");
    }

    if (!parameters.containsKey("TitleAttribute"))
    {
      parameters.put("TitleAttribute", "title");
    }

    if (!parameters.containsKey("FirstNamesAttribute"))
    {
      parameters.put("FirstNamesAttribute", "givenName");
    }

    if (!parameters.containsKey("LastNameAttribute"))
    {
      parameters.put("LastNameAttribute", "sn");
    }

    if (!parameters.containsKey("PhoneNumberAttribute"))
    {
      parameters.put("PhoneNumberAttribute", "telephoneNumber");
    }

    if (!parameters.containsKey("FaxNumberAttribute"))
    {
      parameters.put("FaxNumberAttribute", "facsimileTelephoneNumber");
    }

    if (!parameters.containsKey("MobileNumberAttribute"))
    {
      parameters.put("MobileNumberAttribute", "mobile");
    }

    if (!parameters.containsKey("EmailAttribute"))
    {
      parameters.put("EmailAttribute", "mail");
    }

    if (!parameters.containsKey("DescriptionAttribute"))
    {
      parameters.put("DescriptionAttribute", "cn");
    }

    if (!parameters.containsKey("GroupObjectClass"))
    {
      parameters.put("GroupObjectClass", "groupOfNames");
    }

    if (!parameters.containsKey("GroupAttribute"))
    {
      parameters.put("GroupAttribute", "cn");
    }

    if (!parameters.containsKey("GroupMemberAttribute"))
    {
      parameters.put("GroupMemberAttribute", "member");
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

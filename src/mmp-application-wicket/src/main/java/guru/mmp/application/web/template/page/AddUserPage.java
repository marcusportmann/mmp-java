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

package guru.mmp.application.web.template.page;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.security.Group;
import guru.mmp.application.security.ISecurityService;
import guru.mmp.application.security.User;
import guru.mmp.application.security.UserNotFoundException;
import guru.mmp.application.web.WebApplicationException;
import guru.mmp.application.web.WebSession;
import guru.mmp.application.web.component.DropDownChoiceWithFeedback;
import guru.mmp.application.web.component.PasswordTextFieldWithFeedback;
import guru.mmp.application.web.component.TextFieldWithFeedback;
import guru.mmp.application.web.page.WebPageSecurity;
import guru.mmp.application.web.template.TemplateSecurity;
import guru.mmp.application.web.template.TemplateWebApplication;
import guru.mmp.application.web.validation.PasswordPolicyValidator;
import guru.mmp.common.util.StringUtil;
import org.apache.wicket.PageReference;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.StringValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>AddUserPage</code> class implements the
 * "Add User" page for the Web Application Template.
 *
 * @author Marcus Portmann
 */
@WebPageSecurity(TemplateSecurity.FUNCTION_CODE_ADD_USER)
public class AddUserPage extends TemplateWebPage
{
  private static final long serialVersionUID = 1000000;

  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(AddUserPage.class);

  /* Should the user be created with an expired password */
  @SuppressWarnings("unused")
  private boolean expiredPassword;

  /* The initial group for the user */
  @SuppressWarnings("unused")
  private String groupName;

  /* Security Service */
  @Inject
  private ISecurityService securityService;

  /* Should the user be created with a locked password */
  @SuppressWarnings("unused")
  private boolean userLocked;

  /**
   * Constructs a new <code>AddUserPage</code>.
   *
   * @param previousPage the previous page
   */
  public AddUserPage(final PageReference previousPage)
  {
    super("Add User", "Add User");
    setTitle(((TemplateWebApplication) getApplication()).getDisplayName() + " | Add User");

    final IModel<User> userModel = new Model<>(new User());

    try
    {
      Form<User> addForm = new Form<>("addForm", new CompoundPropertyModel<>(userModel));

      // The "username" field
      TextField<String> usernameField = new TextFieldWithFeedback<>("username");
      usernameField.setRequired(true);
      addForm.add(usernameField);

      // The "title" field
      DropDownChoice<String> titleField = new DropDownChoiceWithFeedback<>("title",
        getTitleOptions());
      titleField.setRequired(true);
      addForm.add(titleField);

      // The "firstNames" field
      TextField<String> firstNamesField = new TextFieldWithFeedback<>("firstNames");
      firstNamesField.setRequired(true);
      addForm.add(firstNamesField);

      // The "lastName" field
      TextField<String> lastNameField = new TextFieldWithFeedback<>("lastName");
      lastNameField.setRequired(true);
      addForm.add(lastNameField);

      // The "email" field
      TextField<String> emailField = new TextFieldWithFeedback<>("email");
      emailField.setRequired(true);
      addForm.add(emailField);

      // The "phoneNumber" field
      TextField<String> phoneNumberField = new TextFieldWithFeedback<>("phoneNumber");
      phoneNumberField.setRequired(false);
      addForm.add(phoneNumberField);

      // The "faxNumber" field
      TextField<String> faxNumberField = new TextFieldWithFeedback<>("faxNumber");
      faxNumberField.setRequired(false);
      addForm.add(faxNumberField);

      // The "mobileNumber" field
      TextField<String> mobileNumberField = new TextFieldWithFeedback<>("mobileNumber");
      mobileNumberField.setRequired(false);
      addForm.add(mobileNumberField);

      // The "description" field
      TextField<String> descriptionField = new TextFieldWithFeedback<>("description");
      descriptionField.setRequired(false);
      addForm.add(descriptionField);

      // The "password" field
      PasswordTextFieldWithFeedback passwordField = new PasswordTextFieldWithFeedback("password");
      passwordField.setRequired(true);
      passwordField.add(StringValidator.minimumLength(6));
      passwordField.add(new PasswordPolicyValidator());
      passwordField.setLabel(Model.of("Password"));
      addForm.add(passwordField);

      // The "confirmPassword" field
      PasswordTextFieldWithFeedback confirmPasswordField =
        new PasswordTextFieldWithFeedback("confirmPassword", Model.of(""));
      confirmPasswordField.setRequired(true);
      addForm.add(confirmPasswordField);

      addForm.add(new EqualPasswordInputValidator(passwordField, confirmPasswordField));

      // The "expiredPassword" field
      CheckBox expiredPasswordCheckbox = new CheckBox("expiredPassword",
        new PropertyModel<>(this, "expiredPassword"));
      expiredPasswordCheckbox.setRequired(false);
      addForm.add(expiredPasswordCheckbox);

      // The "userLocked" field
      CheckBox userLockedCheckbox = new CheckBox("userLocked",
        new PropertyModel<>(this, "userLocked"));
      userLockedCheckbox.setRequired(false);
      addForm.add(userLockedCheckbox);

      // The "groupName" field
      DropDownChoice<String> groupNameField = new DropDownChoiceWithFeedback<>("groupName",
        new PropertyModel<>(this, "groupName"), getGroupOptions());
      groupNameField.setRequired(false);
      addForm.add(groupNameField);

      // The "addButton" button
      Button addButton = new Button("addButton")
      {
        private static final long serialVersionUID = 1000000;

        @Override
        public void onSubmit()
        {
          try
          {
            WebSession session = getWebApplicationSession();

            User user = userModel.getObject();

            // Check if a user with the specified username already exists and if so return an error
            try
            {
              securityService.getUser(user.getUsername(), getRemoteAddress());

              error("A user with the specified username already exists.");

              return;
            }
            catch (UserNotFoundException ignored) {}

            securityService.createUser(user, expiredPassword, userLocked, getRemoteAddress());

            securityService.addUserToOrganisation(user.getUsername(), session.getOrganisation(),
                getRemoteAddress());

            if (!StringUtil.isNullOrEmpty(groupName))
            {
              securityService.addUserToGroup(user.getUsername(), groupName,
                  session.getOrganisation(), getRemoteAddress());
            }

            setResponsePage(previousPage.getPage());
          }
          catch (Throwable e)
          {
            logger.error("Failed to add the user: " + e.getMessage(), e);

            AddUserPage.this.error("Failed to add the user");
          }
        }
      };
      addButton.setDefaultFormProcessing(true);
      addForm.add(addButton);

      // The "cancelButton" button
      Button cancelButton = new Button("cancelButton")
      {
        private static final long serialVersionUID = 1000000;

        @Override
        public void onSubmit()
        {
          setResponsePage(previousPage.getPage());
        }
      };
      cancelButton.setDefaultFormProcessing(false);
      addForm.add(cancelButton);

      add(addForm);
    }
    catch (Throwable e)
    {
      throw new WebApplicationException("Failed to initialise the AddUserPage", e);
    }
  }

  /**
   * Hidden <code>AddUserPage</code> constructor.
   */
  protected AddUserPage() {}

  /**
   * Returns the user title options e.g. Mr, Mrs, Ms, etc.
   *
   * @return the user title options e.g. Mr, Mrs, Ms, etc
   */
  public static List<String> getTitleOptions()
  {
    List<String> titleOptions = new ArrayList<>();

    titleOptions.add("Mr");
    titleOptions.add("Mrs");
    titleOptions.add("Ms");
    titleOptions.add("Dr");

    return titleOptions;
  }

  private List<String> getGroupOptions()
    throws guru.mmp.application.security.SecurityException
  {
    WebSession session = getWebApplicationSession();

    // Retrieve a complete list of groups for the organisation
    List<Group> groups = securityService.getGroups(getRemoteAddress());

    // Filter the list of available groups for the user
    List<String> groupOptions = new ArrayList<>();

    for (Group group : groups)
    {
      boolean isAvailableUserGroup = true;

      if (group.getGroupName().equalsIgnoreCase(TemplateSecurity.ADMINISTRATORS_GROUP_NAME))
      {
        if (!session.hasAcccessToFunction(TemplateSecurity.FUNCTION_CODE_ADD_ORGANISATION))
        {
          isAvailableUserGroup = false;
        }
      }

      if (isAvailableUserGroup)
      {
        groupOptions.add(group.getGroupName());
      }
    }

    return groupOptions;
  }
}

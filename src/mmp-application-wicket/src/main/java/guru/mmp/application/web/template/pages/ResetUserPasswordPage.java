/*
 * Copyright 2017 Marcus Portmann
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

package guru.mmp.application.web.template.pages;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.security.ISecurityService;
import guru.mmp.application.security.PasswordChangeReason;
import guru.mmp.application.security.User;
import guru.mmp.application.web.WebApplicationException;
import guru.mmp.application.web.pages.WebPageSecurity;
import guru.mmp.application.web.template.TemplateSecurity;
import guru.mmp.application.web.template.components.PasswordTextFieldWithFeedback;
import guru.mmp.application.web.template.components.TextFieldWithFeedback;
import guru.mmp.application.web.validators.PasswordPolicyValidator;
import org.apache.wicket.PageReference;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.StringValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>ResetPasswordPage</code> class implements the
 * "Reset User Password" page for the Web Application Template.
 *
 * @author Marcus Portmann
 */
@WebPageSecurity(TemplateSecurity.FUNCTION_CODE_RESET_USER_PASSWORD)
class ResetUserPasswordPage extends TemplateWebPage
{
  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(ResetUserPasswordPage.class);
  private static final long serialVersionUID = 1000000;

  /* Should the user's password be expired */
  @SuppressWarnings("unused")
  private boolean expiredPassword;

  /* Security Service */
  @Inject
  private ISecurityService securityService;

  /* Should the user's password be locked */
  @SuppressWarnings("unused")
  private boolean userLocked;

  /**
   * Constructs a new <code>ResetUserPasswordPage</code>.
   *
   * @param previousPage the previous page
   * @param userModel    the model for the user
   */
  ResetUserPasswordPage(PageReference previousPage, IModel<User> userModel)
  {
    super("Reset User Password");

    try
    {
      Form<User> resetForm = new Form<>("resetForm", new CompoundPropertyModel<>(userModel));

      // The "username" field
      TextField<String> usernameField = new TextFieldWithFeedback<>("username");
      usernameField.setRequired(true);
      usernameField.setEnabled(false);
      resetForm.add(usernameField);

      // The "firstName" field
      TextField<String> firstNameField = new TextFieldWithFeedback<>("firstName");
      firstNameField.setRequired(true);
      firstNameField.setEnabled(false);
      resetForm.add(firstNameField);

      // The "lastName" field
      TextField<String> lastNameField = new TextFieldWithFeedback<>("lastName");
      lastNameField.setRequired(true);
      lastNameField.setEnabled(false);
      resetForm.add(lastNameField);

      // The "password" field
      PasswordTextFieldWithFeedback passwordField = new PasswordTextFieldWithFeedback("password");
      passwordField.setRequired(true);
      passwordField.add(StringValidator.minimumLength(6));
      passwordField.add(new PasswordPolicyValidator());
      passwordField.setLabel(Model.of("Password"));
      resetForm.add(passwordField);

      // The "confirmPassword" field
      PasswordTextFieldWithFeedback confirmPasswordField = new PasswordTextFieldWithFeedback(
          "confirmPassword", Model.of(""));
      confirmPasswordField.setRequired(true);
      resetForm.add(confirmPasswordField);

      resetForm.add(new EqualPasswordInputValidator(passwordField, confirmPasswordField));

      // The "expiredPassword" field
      CheckBox expiredPasswordCheckbox = new CheckBox("expiredPassword", new PropertyModel<>(this,
          "expiredPassword"));
      expiredPasswordCheckbox.setRequired(false);
      resetForm.add(expiredPasswordCheckbox);

      // The "userLocked" field
      CheckBox userLockedCheckbox = new CheckBox("userLocked", new PropertyModel<>(this,
          "userLocked"));
      userLockedCheckbox.setRequired(false);
      resetForm.add(userLockedCheckbox);

      // The "resetButton" button
      Button resetButton = new Button("resetButton")
      {
        private static final long serialVersionUID = 1000000;

        @Override
        public void onSubmit()
        {
          try
          {
            User user = userModel.getObject();

            securityService.adminChangePassword(user.getUserDirectoryId(), user.getUsername(),
                user.getPassword(), expiredPassword, userLocked, false, PasswordChangeReason
                .ADMINISTRATIVE);

            setResponsePage(previousPage.getPage());
          }
          catch (Throwable e)
          {
            logger.error("Failed to reset the password for the user: " + e.getMessage(), e);

            ResetUserPasswordPage.this.error("Failed to reset the password for the user");
          }
        }
      };
      resetButton.setDefaultFormProcessing(true);
      resetForm.add(resetButton);

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
      resetForm.add(cancelButton);

      add(resetForm);
    }
    catch (Throwable e)
    {
      throw new WebApplicationException("Failed to initialise the ResetUserPasswordPage", e);
    }
  }
}

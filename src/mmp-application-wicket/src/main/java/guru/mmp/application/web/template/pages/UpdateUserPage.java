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
import guru.mmp.application.security.User;
import guru.mmp.application.web.WebApplicationException;
import guru.mmp.application.web.pages.WebPageSecurity;
import guru.mmp.application.web.template.TemplateSecurity;
import guru.mmp.application.web.template.components.TextFieldWithFeedback;
import org.apache.wicket.PageReference;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>UpdateUserPage</code> class implements the
 * "Update User" page for the Web Application Template.
 *
 * @author Marcus Portmann
 */
@WebPageSecurity(TemplateSecurity.FUNCTION_CODE_USER_ADMINISTRATION)
class UpdateUserPage extends TemplateWebPage
{
  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(UpdateUserPage.class);
  private static final long serialVersionUID = 1000000;

  /* Security Service */
  @Inject
  private ISecurityService securityService;

  /**
   * Constructs a new <code>UpdateUserPage</code>.
   *
   * @param previousPage the previous page
   * @param userModel    the model for the user
   */
  UpdateUserPage(PageReference previousPage, IModel<User> userModel)
  {
    super("Update User");

    try
    {
      Form<User> updateForm = new Form<>("updateForm", new CompoundPropertyModel<>(userModel));

      // The "username" field
      TextField<String> usernameField = new TextFieldWithFeedback<>("username");
      usernameField.setRequired(true);
      usernameField.setEnabled(false);
      updateForm.add(usernameField);

      // The "firstName" field
      TextField<String> firstNameField = new TextFieldWithFeedback<>("firstName");
      firstNameField.setRequired(true);
      updateForm.add(firstNameField);

      // The "lastName" field
      TextField<String> lastNameField = new TextFieldWithFeedback<>("lastName");
      lastNameField.setRequired(true);
      updateForm.add(lastNameField);

      // The "email" field
      TextField<String> emailField = new TextFieldWithFeedback<>("email");
      emailField.add(EmailAddressValidator.getInstance());
      emailField.setRequired(true);
      updateForm.add(emailField);

      // The "phoneNumber" field
      TextField<String> phoneNumberField = new TextFieldWithFeedback<>("phoneNumber");
      phoneNumberField.setRequired(false);
      updateForm.add(phoneNumberField);

      // The "mobileNumber" field
      TextField<String> mobileNumberField = new TextFieldWithFeedback<>("mobileNumber");
      mobileNumberField.setRequired(false);
      updateForm.add(mobileNumberField);

      // The "updateButton" button
      Button updateButton = new Button("updateButton")
      {
        private static final long serialVersionUID = 1000000;

        @Override
        public void onSubmit()
        {
          try
          {
            User user = updateForm.getModelObject();

            securityService.updateUser(user.getUserDirectoryId(), user, false, false);

            setResponsePage(previousPage.getPage());
          }
          catch (Throwable e)
          {
            logger.error("Failed to update the user: " + e.getMessage(), e);

            UpdateUserPage.this.error("Failed to update the user");
          }
        }
      };
      updateButton.setDefaultFormProcessing(true);
      updateForm.add(updateButton);

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
      updateForm.add(cancelButton);

      add(updateForm);
    }
    catch (Throwable e)
    {
      throw new WebApplicationException("Failed to initialise the UpdateUserPage", e);
    }
  }
}

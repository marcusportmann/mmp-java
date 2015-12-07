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

package guru.mmp.application.web.template.page;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.security.Group;
import guru.mmp.application.security.ISecurityService;
import guru.mmp.application.security.User;
import guru.mmp.application.security.UserNotFoundException;
import guru.mmp.application.web.WebApplicationException;
import guru.mmp.application.web.WebSession;
import guru.mmp.application.web.page.WebPageSecurity;
import guru.mmp.application.web.template.TemplateSecurity;
import guru.mmp.application.web.template.component.DropDownChoiceWithFeedback;
import guru.mmp.application.web.template.component.PasswordTextFieldWithFeedback;
import guru.mmp.application.web.template.component.TextFieldWithFeedback;
import guru.mmp.application.web.validation.PasswordPolicyValidator;
import guru.mmp.common.util.StringUtil;
import org.apache.wicket.PageReference;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.StringValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import guru.mmp.application.security.UserDirectory;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>AddUserDirectoryPage</code> class implements the
 * "Add User Directory" page for the Web Application Template.
 *
 * @author Marcus Portmann
 */
//@WebPageSecurity(TemplateSecurity.FUNCTION_CODE_SECURITY_ADMINISTRATION)
public class AddUserDirectoryPage extends TemplateWebPage
{
  private static final long serialVersionUID = 1000000;

  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(AddUserDirectoryPage.class);

  /* Security Service */
  @Inject
  private ISecurityService securityService;

  /**
   * Constructs a new <code>AddUserDirectoryPage</code>.
   *
   * @param previousPage the previous page
   */
  public AddUserDirectoryPage(PageReference previousPage)
  {
    super("Add User Directory");

    try
    {
      UserDirectory userDirectory = new UserDirectory();
      userDirectory.setUserDirectoryClass("BLAH!!!");

      Form<UserDirectory> addForm = new Form<>("addForm",
        new CompoundPropertyModel<>(new Model<>(userDirectory)));

      // The "name" field
      TextField<String> nameField = new TextFieldWithFeedback<>("name");
      nameField.setRequired(true);
      addForm.add(nameField);

      // The "description" field
      TextField<String> descriptionField = new TextFieldWithFeedback<>("description");
      descriptionField.setRequired(false);
      addForm.add(descriptionField);

      // The "userDirectoryClass" field
      TextField<String> userDirectoryClassField = new TextFieldWithFeedback<>("userDirectoryClass");
      userDirectoryClassField.setRequired(true);
      userDirectoryClassField.setEnabled(false);
      addForm.add(userDirectoryClassField);

      // The "addButton" button
      Button addButton = new Button("addButton")
      {
        private static final long serialVersionUID = 1000000;

        @Override
        public void onSubmit()
        {
          try
          {
            UserDirectory userDirectory = addForm.getModelObject();

            logger.info("Adding the user directory (" + userDirectory.getName() + ")");


            setResponsePage(previousPage.getPage());
          }
          catch (Throwable e)
          {
            logger.error("Failed to add the user directory: " + e.getMessage(), e);

            AddUserDirectoryPage.this.error("Failed to add the user directory");
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
      throw new WebApplicationException("Failed to initialise the AddUserDirectoryPage", e);
    }
  }

  /**
   * Hidden <code>AddUserDirectoryPage</code> constructor.
   */
  @SuppressWarnings("unused")
  protected AddUserDirectoryPage() {}
}

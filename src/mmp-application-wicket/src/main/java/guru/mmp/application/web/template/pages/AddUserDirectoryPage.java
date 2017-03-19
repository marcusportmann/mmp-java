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
import guru.mmp.application.security.UserDirectory;
import guru.mmp.application.security.UserDirectoryType;
import guru.mmp.application.web.WebApplicationException;
import guru.mmp.application.web.pages.WebPageSecurity;
import guru.mmp.application.web.template.TemplateSecurity;
import guru.mmp.application.web.template.components.TextFieldWithFeedback;
import guru.mmp.application.web.template.components.UserDirectoryAdministrationPanel;
import org.apache.wicket.PageReference;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.lang.reflect.Constructor;
import java.util.UUID;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>AddUserDirectoryPage</code> class implements the
 * "Add User Directory" page for the Web Application Template.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("CdiManagedBeanInconsistencyInspection")
@WebPageSecurity(TemplateSecurity.FUNCTION_CODE_SECURITY_ADMINISTRATION)
class AddUserDirectoryPage extends TemplateWebPage
{
  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(AddUserDirectoryPage.class);
  private static final long serialVersionUID = 1000000;

  /* Security Service */
  @Inject
  private ISecurityService securityService;

  /**
   * Constructs a new <code>AddUserDirectoryPage</code>.
   *
   * @param previousPage      the previous page
   * @param userDirectoryType the user directory type
   */
  AddUserDirectoryPage(PageReference previousPage, UserDirectoryType userDirectoryType)
  {
    super("Add User Directory");

    try
    {
      UserDirectory userDirectory = new UserDirectory();
      userDirectory.setTypeId(userDirectoryType.getId());
      userDirectory.setType(userDirectory.getType());

      IModel<UserDirectory> userDirectoryModel = new Model<>(userDirectory);

      Form<UserDirectory> addForm = new Form<>("addForm", new CompoundPropertyModel<>(
          userDirectoryModel));

      addForm.getModelObject().setId(UUID.randomUUID());

      // The "id" field
      TextField<UUID> idField = new TextFieldWithFeedback<>("id");
      idField.setRequired(true);
      addForm.add(idField);

      // The "name" field
      TextField<String> nameField = new TextFieldWithFeedback<>("name");
      nameField.setRequired(true);
      addForm.add(nameField);

      // The "userDirectoryTypeName" field
      TextField<String> userDirectoryTypeNameField = new TextField<>("userDirectoryTypeName",
          new Model<>(userDirectoryType.getName()));
      userDirectoryTypeNameField.setRequired(false);
      userDirectoryTypeNameField.setEnabled(false);
      addForm.add(userDirectoryTypeNameField);

      Class<? extends UserDirectoryAdministrationPanel> userDirectoryAdministrationPanelClass =
          userDirectoryType.getAdministrationClass().asSubclass(
          UserDirectoryAdministrationPanel.class);

      Constructor<? extends UserDirectoryAdministrationPanel> constructor =
          userDirectoryAdministrationPanelClass.getConstructor(String.class, IModel.class);

      UserDirectoryAdministrationPanel userDirectoryAdministrationPanel = constructor.newInstance(
          "userDirectoryAdministrationPanel", userDirectoryModel);

      addForm.add(userDirectoryAdministrationPanel);

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

            securityService.createUserDirectory(userDirectory);

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
}

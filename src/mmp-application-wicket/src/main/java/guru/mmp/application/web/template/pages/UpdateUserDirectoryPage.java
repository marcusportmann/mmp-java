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

package guru.mmp.application.web.template.pages;

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

/**
 * The <code>UpdateUserDirectoryPage</code> class implements the
 * "Update User Directory" page for the Web Application Template.
 *
 * @author Marcus Portmann
 */
@WebPageSecurity(TemplateSecurity.FUNCTION_CODE_SECURITY_ADMINISTRATION)
public class UpdateUserDirectoryPage
  extends TemplateWebPage
{
  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(UpdateUserDirectoryPage.class);

  private static final long serialVersionUID = 1000000;

  /* Security Service */
  @Inject
  private ISecurityService securityService;

  /**
   * Constructs a new <code>UpdateUserDirectoryPage</code>.
   *
   * @param previousPage       the previous page
   * @param userDirectoryModel the model for the user directory
   */
  public UpdateUserDirectoryPage(
    PageReference previousPage, IModel<UserDirectory> userDirectoryModel)
  {
    super("Update User Directory");

    try
    {
      UserDirectoryType userDirectoryType = userDirectoryModel.getObject().getType();

      Form<UserDirectory> updateForm = new Form<>("updateForm",
        new CompoundPropertyModel<>(userDirectoryModel));

      // The "id" field
      TextField<UUID> idField = new TextFieldWithFeedback<>("id");
      idField.setRequired(true);
      idField.setEnabled(false);
      updateForm.add(idField);

      // The "name" field
      TextField<String> nameField = new TextFieldWithFeedback<>("name");
      nameField.setRequired(true);
      updateForm.add(nameField);

      // The "description" field
      TextField<String> descriptionField = new TextFieldWithFeedback<>("description");
      descriptionField.setRequired(false);
      updateForm.add(descriptionField);

      // The "userDirectoryTypeName" field
      TextField<String> userDirectoryTypeNameField = new TextField<>("userDirectoryTypeName",
        new Model<>(userDirectoryType.getName()));
      userDirectoryTypeNameField.setRequired(false);
      userDirectoryTypeNameField.setEnabled(false);
      updateForm.add(userDirectoryTypeNameField);

      Class<? extends UserDirectoryAdministrationPanel> userDirectoryAdministrationPanelClass =
        userDirectoryType.getAdministrationClass().asSubclass(
        UserDirectoryAdministrationPanel.class);

      Constructor<? extends UserDirectoryAdministrationPanel> constructor =
        userDirectoryAdministrationPanelClass.getConstructor(
        String.class, IModel.class);

      UserDirectoryAdministrationPanel userDirectoryAdministrationPanel = constructor.newInstance(
        "userDirectoryAdministrationPanel", userDirectoryModel);

      updateForm.add(userDirectoryAdministrationPanel);

      // The "updateButton" button
      Button updateButton = new Button("updateButton")
      {
        private static final long serialVersionUID = 1000000;

        @Override
        public void onSubmit()
        {
          try
          {
            UserDirectory userDirectory = updateForm.getModelObject();

            securityService.updateUserDirectory(userDirectory);

            setResponsePage(previousPage.getPage());
          }
          catch (Throwable e)
          {
            logger.error("Failed to update the user directory: " + e.getMessage(), e);

            UpdateUserDirectoryPage.this.error("Failed to update the user directory");
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
      throw new WebApplicationException("Failed to initialise the UpdateUserDirectoryPage", e);
    }
  }

  /**
   * Hidden <code>UpdateUserDirectoryPage</code> constructor.
   */
  @SuppressWarnings("unused")
  protected UpdateUserDirectoryPage() {}
}

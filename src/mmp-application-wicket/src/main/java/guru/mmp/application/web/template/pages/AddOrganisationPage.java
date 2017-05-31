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

import guru.mmp.application.security.DuplicateOrganisationException;
import guru.mmp.application.security.ISecurityService;
import guru.mmp.application.security.Organisation;
import guru.mmp.application.security.OrganisationStatus;
import guru.mmp.application.web.WebApplicationException;
import guru.mmp.application.web.pages.WebPageSecurity;
import guru.mmp.application.web.template.TemplateSecurity;
import guru.mmp.application.web.template.components.TextFieldWithFeedback;
import org.apache.wicket.PageReference;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.inject.Inject;
import java.util.UUID;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>AddOrganisationPage</code> class implements the
 * "Add Organisation" page for the Web Application Template.
 *
 * @author Marcus Portmann
 */
@WebPageSecurity(TemplateSecurity.FUNCTION_CODE_ORGANISATION_ADMINISTRATION)
class AddOrganisationPage extends TemplateWebPage
{
  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(AddOrganisationPage.class);
  private static final long serialVersionUID = 1000000;
  @SuppressWarnings("unused")
  private boolean createUserDirectory;

  /* Security Service */
  @Inject
  private ISecurityService securityService;

  /**
   * Constructs a new <code>AddOrganisationPage</code>.
   *
   * @param previousPage the previous page
   */
  AddOrganisationPage(PageReference previousPage)
  {
    super("Add Organisation");

    try
    {
      Form<Organisation> addForm = new Form<>("addForm", new CompoundPropertyModel<>(new Model<>(
          new Organisation(UUID.randomUUID(), "", OrganisationStatus.ACTIVE))));

      // The "id" field
      TextField<UUID> idField = new TextFieldWithFeedback<>("id");
      idField.setRequired(true);
      addForm.add(idField);

      // The "name" field
      TextField<String> nameField = new TextFieldWithFeedback<>("name");
      nameField.setRequired(true);
      addForm.add(nameField);

      // The "createUserDirectory" field
      CheckBox createUserDirectoryCheckbox = new CheckBox("createUserDirectory",
          new PropertyModel<>(this, "createUserDirectory"));
      createUserDirectoryCheckbox.setRequired(false);
      addForm.add(createUserDirectoryCheckbox);

      // The "addButton" button
      Button addButton = new Button("addButton")
      {
        private static final long serialVersionUID = 1000000;

        @Override
        public void onSubmit()
        {
          try
          {
            Organisation organisation = addForm.getModelObject();

            try
            {
              securityService.createOrganisation(organisation, createUserDirectory);
            }
            catch (DuplicateOrganisationException e)
            {
              AddOrganisationPage.this.error(
                  "An organisation with the specified code already exists.");

              return;
            }

            setResponsePage(previousPage.getPage());
          }
          catch (Throwable e)
          {
            logger.error("Failed to add the organisation: " + e.getMessage(), e);

            AddOrganisationPage.this.error("Failed to add the organisation");
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
      throw new WebApplicationException("Failed to initialise the AddOrganisationPage", e);
    }
  }
}

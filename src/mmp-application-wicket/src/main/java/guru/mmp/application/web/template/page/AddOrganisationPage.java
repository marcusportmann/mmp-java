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

package guru.mmp.application.web.template.page;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.security.ISecurityService;
import guru.mmp.application.security.Organisation;
import guru.mmp.application.security.OrganisationNotFoundException;
import guru.mmp.application.web.WebApplicationException;
import guru.mmp.application.web.page.WebPageSecurity;
import guru.mmp.application.web.template.TemplateSecurity;
import guru.mmp.application.web.template.component.TextFieldWithFeedback;

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

//~--- JDK imports ------------------------------------------------------------

import javax.inject.Inject;

/**
 * The <code>AddOrganisationPage</code> class implements the
 * "Add Organisation" page for the Web Application Template.
 *
 * @author Marcus Portmann
 */
@WebPageSecurity(TemplateSecurity.FUNCTION_CODE_ADD_ORGANISATION)
public class AddOrganisationPage extends TemplateWebPage
{
  private static final long serialVersionUID = 1000000;

  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(AddOrganisationPage.class);

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
  public AddOrganisationPage(PageReference previousPage)
  {
    super("Add Organisation");

    try
    {
      Form<Organisation> addForm = new Form<>("addForm",
        new CompoundPropertyModel<>(new Model<>(new Organisation())));

      // The "code" field
      TextField<String> codeField = new TextFieldWithFeedback<>("code");
      codeField.setRequired(true);
      addForm.add(codeField);

      // The "name" field
      TextField<String> nameField = new TextFieldWithFeedback<>("name");
      nameField.setRequired(true);
      addForm.add(nameField);

      // The "description" field
      TextField<String> descriptionField = new TextFieldWithFeedback<>("description");
      descriptionField.setRequired(false);
      addForm.add(descriptionField);

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

            /*
             * Check if an organisation with the specified code already exists and if so return an
             * error.
             */
            try
            {
              securityService.getOrganisation(organisation.getId());

              AddOrganisationPage.this.error(
                  "An organisation with the specified code already exists.");

              return;
            }
            catch (OrganisationNotFoundException e)
            {
              // Do nothing, this is not an error
            }

            securityService.createOrganisation(organisation, createUserDirectory);

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

  /**
   * Hidden <code>AddOrganisationPage</code> constructor.
   */
  @SuppressWarnings("unused")
  protected AddOrganisationPage() {}
}

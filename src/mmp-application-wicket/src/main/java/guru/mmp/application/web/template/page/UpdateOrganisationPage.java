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

import guru.mmp.application.security.ISecurityService;
import guru.mmp.application.security.Organisation;
import guru.mmp.application.web.WebApplicationException;
import guru.mmp.application.web.page.WebPageSecurity;
import guru.mmp.application.web.template.TemplateSecurity;
import guru.mmp.application.web.template.TemplateWebApplication;
import guru.mmp.application.web.template.component.OrganisationInputPanel;
import org.apache.wicket.PageReference;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>UpdateOrganisationPage</code> class implements the
 * "Update Organisation" page for the Web Application Template.
 *
 * @author Marcus Portmann
 */
//@WebPageSecurity(TemplateSecurity.FUNCTION_CODE_UPDATE_ORGANISATION)
public class UpdateOrganisationPage extends TemplateWebPage
{
  private static final long serialVersionUID = 1000000;

  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(UpdateOrganisationPage.class);

  /* Security Service */
  @Inject
  private ISecurityService securityService;

  /**
   * Constructs a new <code>UpdateOrganisationPage</code>.
   *
   * @param previousPage      the previous page
   * @param organisationModel the model for the organisation
   */
  public UpdateOrganisationPage(final PageReference previousPage,
      final IModel<Organisation> organisationModel)
  {
    super("Update Organisation", "Update Organisation");
    setTitle(((TemplateWebApplication) getApplication()).getDisplayName()
        + " | Update Organisation");

    try
    {
      Form<Organisation> updateForm = new Form<>("updateForm",
        new CompoundPropertyModel<>(organisationModel));

      updateForm.add(new OrganisationInputPanel("organisation", organisationModel, true));

      // The "updateButton" button
      Button updateButton = new Button("updateButton")
      {
        private static final long serialVersionUID = 1000000;

        @Override
        public void onSubmit()
        {
          try
          {
            Organisation organisation = organisationModel.getObject();

            securityService.updateOrganisation(organisation, getRemoteAddress());

            setResponsePage(previousPage.getPage());
          }
          catch (Throwable e)
          {
            logger.error("Failed to update the organisation: " + e.getMessage(), e);

            UpdateOrganisationPage.this.error("Failed to update the organisation");
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
      throw new WebApplicationException("Failed to initialise the UpdateOrganisationPage", e);
    }
  }

  /**
   * Hidden <code>UpdateOrganisationPage</code> constructor.
   */
  protected UpdateOrganisationPage() {}
}

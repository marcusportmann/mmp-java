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

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.configuration.Configuration;
import guru.mmp.application.configuration.IConfigurationService;
import guru.mmp.application.web.WebApplicationException;
import guru.mmp.application.web.pages.WebPageSecurity;
import guru.mmp.application.web.template.TemplateSecurity;
import guru.mmp.application.web.template.components.ConfigurationInputPanel;
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
 * The <code>UpdateConfigurationPage</code> class implements the
 * "Update Configuration Value" page for the Web Application Template.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("CdiManagedBeanInconsistencyInspection")
@WebPageSecurity(TemplateSecurity.FUNCTION_CODE_CONFIGURATION_ADMINISTRATION)
public class UpdateConfigurationPage
  extends TemplateWebPage
{
  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(UpdateConfigurationPage.class);
  private static final long serialVersionUID = 1000000;

  /* Configuration Service */
  @Inject
  private IConfigurationService configurationService;

  /**
   * Constructs a new <code>UpdateConfigurationPage</code>.
   *
   * @param previousPage       the previous page
   * @param configurationModel the model for the configuration value
   */
  public UpdateConfigurationPage(PageReference previousPage,
      IModel<Configuration> configurationModel)
  {
    super("Update Configuration");

    try
    {
      Form<Configuration> updateForm = new Form<>("updateForm", new CompoundPropertyModel<>(
          configurationModel));

      updateForm.add(new ConfigurationInputPanel("configuration", true));

      // The "updateButton" button
      Button updateButton = new Button("updateButton")
      {
        private static final long serialVersionUID = 1000000;

        @Override
        public void onSubmit()
        {
          try
          {
            Configuration configuration = updateForm.getModelObject();

            configurationService.setValue(configuration.getKey(),
                configuration.getValue(), configuration.getDescription());

            setResponsePage(previousPage.getPage());
          }
          catch (Throwable e)
          {
            logger.error("Failed to update the configuration value: " + e.getMessage(), e);

            UpdateConfigurationPage.this.error("Failed to update the configuration");
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
      throw new WebApplicationException("Failed to initialise the UpdateConfigurationPage", e);
    }
  }
}

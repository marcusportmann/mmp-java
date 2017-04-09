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

import guru.mmp.application.configuration.ConfigurationValue;
import guru.mmp.application.configuration.IConfigurationService;
import guru.mmp.application.web.WebApplicationException;
import guru.mmp.application.web.pages.WebPageSecurity;
import guru.mmp.application.web.template.TemplateSecurity;
import guru.mmp.application.web.template.components.ConfigurationValueInputPanel;
import org.apache.wicket.PageReference;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <configurationValue>AddConfigurationPage</configurationValue> class implements the
 * "Add Configuration Value" page for the Web Application Template.
 *
 * @author Marcus Portmann
 */
@WebPageSecurity(TemplateSecurity.FUNCTION_CODE_CONFIGURATION_ADMINISTRATION)
class AddConfigurationValuePage extends TemplateWebPage
{
  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(AddConfigurationValuePage.class);
  private static final long serialVersionUID = 1000000;

  /* Configuration Service */
  @Autowired
  private IConfigurationService configurationService;

  /**
   * Constructs a new <configurationValue>AddConfigurationPage</configurationValue>.
   *
   * @param previousPage the previous page
   */
  AddConfigurationValuePage(PageReference previousPage)
  {
    super("Add Configuration Value");

    try
    {
      Form<ConfigurationValue> addForm = new Form<>("addForm", new CompoundPropertyModel<>(
          new Model<>(new ConfigurationValue())));

      addForm.add(new ConfigurationValueInputPanel("configurationValue", false));

      // The "addButton" button
      Button addButton = new Button("addButton")
      {
        private static final long serialVersionUID = 1000000;

        @Override
        public void onSubmit()
        {
          try
          {
            ConfigurationValue configurationValue = addForm.getModelObject();

            configurationService.setValue(configurationValue.getKey(),
                configurationValue.getValue(), configurationValue.getDescription());

            setResponsePage(previousPage.getPage());
          }
          catch (Throwable e)
          {
            logger.error("Failed to add the configuration value: " + e.getMessage(), e);
            AddConfigurationValuePage.this.error("Failed to add the configuration value");
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
      throw new WebApplicationException("Failed to initialise the AddConfigurationValuePage", e);
    }
  }
}

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
import guru.mmp.application.web.template.components.Dialog;
import guru.mmp.application.web.template.components.PagingNavigator;
import guru.mmp.application.web.template.data.FilteredConfigurationValueDataProvider;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.ReuseIfModelsEqualStrategy;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.inject.Inject;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>ConfigurationAdministrationPage</code> class implements the
 * "Configuration Administration" page for the Web Application Template.
 *
 * @author Marcus Portmann
 */
@WebPageSecurity(TemplateSecurity.FUNCTION_CODE_CONFIGURATION_ADMINISTRATION)
public class ConfigurationAdministrationPage extends TemplateWebPage
{
  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(
      ConfigurationAdministrationPage.class);
  private static final long serialVersionUID = 1000000;

  /* Configuration Service */
  @Inject
  private IConfigurationService configurationService;

  /**
   * Constructs a new <code>ConfigurationAdministrationPage</code>.
   */
  public ConfigurationAdministrationPage()
  {
    super("Configuration");

    try
    {
      /*
       * The table container, which allows the table and its associated navigator to be updated
       * using AJAX.
       */
      WebMarkupContainer tableContainer = new WebMarkupContainer("tableContainer");
      tableContainer.setOutputMarkupId(true);
      add(tableContainer);

      // The dialog used to confirm the removal of a configuration value
      RemoveDialog removeDialog = new RemoveDialog(tableContainer);
      add(removeDialog);

      // The "addLink" used to add a new configuration value
      Link<Void> addLink = new Link<Void>("addLink")
      {
        private static final long serialVersionUID = 1000000;

        @Override
        public void onClick()
        {
          setResponsePage(new AddConfigurationValuePage(getPageReference()));
        }
      };
      tableContainer.add(addLink);

      FilteredConfigurationValueDataProvider dataProvider =
          new FilteredConfigurationValueDataProvider();

      // The "filterForm" form
      Form<Void> filterForm = new Form<>("filterForm");
      filterForm.setMarkupId("filterForm");
      filterForm.setOutputMarkupId(true);

      // The "filter" field
      TextField<String> filterField = new TextField<>("filter", new PropertyModel<>(dataProvider,
          "filter"));
      filterForm.add(filterField);

      // The "filterButton" button
      Button filterButton = new Button("filterButton")
      {
        private static final long serialVersionUID = 1000000;

        @Override
        public void onSubmit() {}
      };
      filterButton.setDefaultFormProcessing(true);
      filterForm.add(filterButton);

      // The "resetButton" button
      Button resetButton = new Button("resetButton")
      {
        private static final long serialVersionUID = 1000000;

        @Override
        public void onSubmit()
        {
          dataProvider.setFilter("");
        }
      };
      filterForm.add(resetButton);

      tableContainer.add(filterForm);

      // The configuration value data view
      DataView<ConfigurationValue> dataView = new DataView<ConfigurationValue>(
          "configurationValue", dataProvider)
      {
        private static final long serialVersionUID = 1000000;

        @Override
        protected void populateItem(Item<ConfigurationValue> item)
        {
          item.add(new Label("key", new PropertyModel<String>(item.getModel(), "key")));
          item.add(new Label("value", new PropertyModel<String>(item.getModel(), "value")));

          // The "updateLink" link
          Link<Void> updateLink = new Link<Void>("updateLink")
          {
            private static final long serialVersionUID = 1000000;

            @Override
            public void onClick()
            {
              UpdateConfigurationValuePage page = new UpdateConfigurationValuePage(
                  getPageReference(), item.getModel());

              setResponsePage(page);
            }
          };
          item.add(updateLink);

          // The "removeLink" link
          AjaxLink<Void> removeLink = new AjaxLink<Void>("removeLink")
          {
            private static final long serialVersionUID = 1000000;

            @Override
            public void onClick(AjaxRequestTarget target)
            {
              ConfigurationValue configuration = item.getModelObject();

              if (configuration != null)
              {
                removeDialog.show(target, configuration);
              }
              else
              {
                target.add(tableContainer);
              }
            }
          };
          item.add(removeLink);
        }
      };
      dataView.setItemsPerPage(10);
      dataView.setItemReuseStrategy(ReuseIfModelsEqualStrategy.getInstance());
      tableContainer.add(dataView);

      tableContainer.add(new PagingNavigator("navigator", dataView));
    }
    catch (Throwable e)
    {
      throw new WebApplicationException("Failed to initialise the ConfigurationAdministrationPage",
          e);
    }
  }

  /**
   * The <code>RemoveDialog</code> class implements a dialog that allows the removal of a
   * configuration value to be confirmed.
   */
  private class RemoveDialog extends Dialog
  {
    private static final long serialVersionUID = 1000000;
    private String key;
    private Label nameLabel;

    /**
     * Constructs a new <code>RemoveDialog</code>.
     *
     * @param tableContainer the table container, which allows the configuration value table and its
     *                       associated navigator to be updated using AJAX
     */
    RemoveDialog(WebMarkupContainer tableContainer)
    {
      super("removeDialog");

      nameLabel = new Label("name", Model.of(""));

      nameLabel.setOutputMarkupId(true);
      add(nameLabel);

      // The "removeLink" link
      AjaxLink<Void> removeLink = new AjaxLink<Void>("removeLink")
      {
        private static final long serialVersionUID = 1000000;

        @Override
        public void onClick(AjaxRequestTarget target)
        {
          try
          {
            configurationService.removeValue(key);

            target.add(tableContainer);

            ConfigurationAdministrationPage.this.info(
                "Successfully removed the configuration value "
                + nameLabel.getDefaultModelObjectAsString());
          }
          catch (Throwable e)
          {
            logger.error(String.format("Failed to remove the configuration value (%s): %s", key,
                e.getMessage()), e);

            ConfigurationAdministrationPage.this.error("Failed to remove the configuration value "
                + nameLabel.getDefaultModelObjectAsString());
          }

          target.add(getAlerts());

          hide(target);
        }
      };
      add(removeLink);
    }

    /**
     * Show the dialog using Ajax.
     *
     * @param target        the AJAX request target
     * @param configuration the configuration value being removed
     */
    public void show(AjaxRequestTarget target, ConfigurationValue configuration)
    {
      key = configuration.getKey();

      nameLabel.setDefaultModelObject(configuration.getKey());

      target.add(nameLabel);

      super.show(target);
    }
  }
}

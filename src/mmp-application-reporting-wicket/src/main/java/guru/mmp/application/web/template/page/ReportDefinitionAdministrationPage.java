/*
 * Copyright 2014 Marcus Portmann
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

import guru.mmp.application.reporting.IReportingService;
import guru.mmp.application.reporting.ReportDefinition;
import guru.mmp.application.web.WebApplicationException;
import guru.mmp.application.web.WebSession;
import guru.mmp.application.web.component.Dialog;
import guru.mmp.application.web.page.WebPageSecurity;
import guru.mmp.application.web.template.TemplateReportingSecurity;
import guru.mmp.application.web.template.TemplateWebApplication;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;

import javax.inject.Inject;

/**
 * The <code>ReportDefinitionAdministrationPage</code> class implements the
 * "Report Definition Administration" page for the Web Application Template.
 *
 * @author Marcus Portmann
 */
@WebPageSecurity(TemplateReportingSecurity.FUNCTION_CODE_REPORT_DEFINITION_ADMINISTRATION)
public class ReportDefinitionAdministrationPage extends TemplateWebPage
{
  /* Logger */
  private static final Logger logger =
    LoggerFactory.getLogger(ReportDefinitionAdministrationPage.class);
  private static final long serialVersionUID = 1000000;

  /* Reporting Service */
  @Inject
  private IReportingService reportingService;

  /**
   * Constructs a new <code>ReportDefinitionAdministrationPage</code>.
   */
  public ReportDefinitionAdministrationPage()
  {
    super("Report Definition Administration", "Report Definition Administration");
    setTitle(((TemplateWebApplication) getApplication()).getDisplayName()
        + " | Report Definition Administration");

    try
    {
      /*
       * The table container, which allows the table and its associated navigator to be updated
       * using AJAX.
       */
      final WebMarkupContainer tableContainer = new WebMarkupContainer("tableContainer");
      tableContainer.setOutputMarkupId(true);
      add(tableContainer);

      // The dialog used to confirm the removal of a group
      final RemoveDialog removeDialog = new RemoveDialog(tableContainer);
      add(removeDialog);

      // The "addLink" link
      Link<Void> addLink = new Link<Void>("addLink")
      {
        private static final long serialVersionUID = 1000000;

        @Override
        public void onClick()
        {
          AddReportDefinitionPage page =
            new AddReportDefinitionPage(getPageReference());

          setResponsePage(page);
        }
      };
      add(addLink);

      LoadableDetachableModel<List<ReportDefinition>> ldm =
        new LoadableDetachableModel<List<ReportDefinition>>()
      {
        private static final long serialVersionUID = 1000000;

        @Override
        protected List<ReportDefinition> load()
        {
          WebSession session = getWebApplicationSession();

          try
          {
            return reportingService.getReportDefinitionsForOrganisation(session.getOrganisation());
          }
          catch (Throwable e)
          {
            throw new WebApplicationException(
                "Failed to retrieve a complete list of report definitions for the organisation ("
                + session.getOrganisation() + ") from the processing service", e);
          }
        }
      };

      ListView<ReportDefinition> listView = new ListView<ReportDefinition>("reportDefinition", ldm)
      {
        private static final long serialVersionUID = 1000000;

        @Override
        protected void populateItem(ListItem<ReportDefinition> item)
        {
          final IModel<ReportDefinition> reportDefinitionModel = item.getModel();

          item.add(new Label("name", new PropertyModel<String>(reportDefinitionModel, "name")));
          item.add(new Label("lastUpdated",
              new PropertyModel<String>(reportDefinitionModel, "updatedAsString")));

          // The "updateLink" link
          Link<Void> updateLink = new Link<Void>("updateLink")
          {
            private static final long serialVersionUID = 1000000;

            @Override
            public void onClick()
            {
              UpdateReportDefinitionPage page =
                new UpdateReportDefinitionPage(getPageReference(),
                  new Model<>(reportDefinitionModel.getObject()));

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
              removeDialog.show(target, reportDefinitionModel);
            }
          };
          item.add(removeLink);
        }
      };

      tableContainer.add(listView);
    }
    catch (Throwable e)
    {
      throw new WebApplicationException(
          "Failed to initialise the ReportDefinitionAdministrationPage", e);
    }
  }

  /**
   * The <code>RemoveDialog</code> class implements a dialog that allows the removal
   * of a report definition to be confirmed.
   */
  private class RemoveDialog extends Dialog
  {
    private static final long serialVersionUID = 1000000;
    private String id;
    private Label nameLabel;

    /**
     * Constructs a new <code>RemoveDialog</code>.
     *
     * @param tableContainer the table container, which allows the code table and its
     *                       associated navigator to be updated using AJAX
     */
    public RemoveDialog(final WebMarkupContainer tableContainer)
    {
      super("removeDialog");

      nameLabel = new Label("name", Model.of(""));
      nameLabel.setOutputMarkupId(true);
      add(nameLabel);

      add(new AjaxLink<Void>("removeLink")
      {
        private static final long serialVersionUID = 1000000;

        @Override
        public void onClick(AjaxRequestTarget target)
        {
          try
          {
            reportingService.deleteReportDefinition(id);

            target.add(tableContainer);

            ReportDefinitionAdministrationPage.this.info(
                "Successfully removed the report definition "
                + nameLabel.getDefaultModelObjectAsString());
          }
          catch (Throwable e)
          {
            logger.error("Failed to remove the report definition (" + id + "): " + e.getMessage(),
                e);

            ReportDefinitionAdministrationPage.this.error("Failed to remove the report definition "
                + nameLabel.getDefaultModelObjectAsString());
          }

          target.add(getAlerts());

          hide(target);
        }
      });
    }

    /**
     * @see Dialog#hide(org.apache.wicket.ajax.AjaxRequestTarget)
     *
     * @param target the AJAX request target
     */
    public void hide(AjaxRequestTarget target)
    {
      super.hide(target);
    }

    /**
     * Show the dialog using Ajax.
     *
     * @param target                the AJAX request target
     * @param reportDefinitionModel the model for the report definition being removed
     */
    public void show(AjaxRequestTarget target, IModel<ReportDefinition> reportDefinitionModel)
    {
      ReportDefinition reportDefinition = reportDefinitionModel.getObject();

      id = reportDefinition.getId();
      nameLabel.setDefaultModelObject(reportDefinition.getName());

      target.add(nameLabel);

      super.show(target);
    }
  }
}

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

import guru.mmp.application.reporting.IReportingService;
import guru.mmp.application.reporting.ReportDefinition;
import guru.mmp.application.reporting.ReportDefinitionSummary;
import guru.mmp.application.web.WebApplicationException;
import guru.mmp.application.web.WebSession;
import guru.mmp.application.web.page.WebPageSecurity;
import guru.mmp.application.web.template.TemplateReportingSecurity;
import guru.mmp.application.web.template.component.Dialog;
import guru.mmp.application.web.template.component.PagingNavigator;
import guru.mmp.application.web.template.data.ReportDefinitionSummaryDataProvider;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.ReuseIfModelsEqualStrategy;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//~--- JDK imports ------------------------------------------------------------

import javax.inject.Inject;
import java.util.UUID;

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
    super("Report Definitions");

    try
    {
      WebSession session = getWebApplicationSession();

      /*
       * The table container, which allows the table and its associated navigator to be updated
       * using AJAX.
       */
      WebMarkupContainer tableContainer = new WebMarkupContainer("tableContainer");
      tableContainer.setOutputMarkupId(true);
      add(tableContainer);

      // The dialog used to confirm the removal of a group
      RemoveDialog removeDialog = new RemoveDialog(tableContainer);
      add(removeDialog);

      // The "addLink" link
      Link<Void> addLink = new Link<Void>("addLink")
      {
        private static final long serialVersionUID = 1000000;

        @Override
        public void onClick()
        {
          AddReportDefinitionPage page = new AddReportDefinitionPage(getPageReference());

          setResponsePage(page);
        }
      };
      tableContainer.add(addLink);

      ReportDefinitionSummaryDataProvider dataProvider =
        new ReportDefinitionSummaryDataProvider();

      // The report definition data view
      DataView<ReportDefinitionSummary> dataView =
        new DataView<ReportDefinitionSummary>("reportDefinition", dataProvider)
      {
        private static final long serialVersionUID = 1000000;

        protected void populateItem(Item<ReportDefinitionSummary> item)
        {
          item.add(new Label("name", new PropertyModel<String>(item.getModel(), "name")));

          // The "updateLink" link
          Link<Void> updateLink = new Link<Void>("updateLink")
          {
            private static final long serialVersionUID = 1000000;

            @Override
            public void onClick()
            {
              ReportDefinitionSummary reportDefinitionSummary = item.getModelObject();

              try
              {
                ReportDefinition reportDefinition =
                  reportingService.getReportDefinition(reportDefinitionSummary.getId());

                UpdateReportDefinitionPage page =
                  new UpdateReportDefinitionPage(getPageReference(), new Model<>(reportDefinition));

                setResponsePage(page);
              }
              catch (Throwable e)
              {
                logger.error("Failed to retrieve the report definition ("
                    + reportDefinitionSummary.getId() + ")", e);

                error("Failed to retrieve the report definition ("
                    + reportDefinitionSummary.getId() + ")");
              }
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
              ReportDefinitionSummary reportDefinitionSummary = item.getModelObject();

              if (reportDefinitionSummary != null)
              {
                removeDialog.show(target, reportDefinitionSummary);
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
    private UUID id;
    private Label nameLabel;

    /**
     * Constructs a new <code>RemoveDialog</code>.
     *
     * @param tableContainer the table container, which allows the code table and its
     *                       associated navigator to be updated using AJAX
     */
    public RemoveDialog(WebMarkupContainer tableContainer)
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
     * @param target                  the AJAX request target
     * @param reportDefinitionSummary the summary for the report definition being removed
     */
    public void show(AjaxRequestTarget target, ReportDefinitionSummary reportDefinitionSummary)
    {
      id = reportDefinitionSummary.getId();
      nameLabel.setDefaultModelObject(reportDefinitionSummary.getName());

      target.add(nameLabel);

      super.show(target);
    }
  }
}

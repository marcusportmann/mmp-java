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
import guru.mmp.application.web.page.WebPageSecurity;
import guru.mmp.application.web.template.TemplateReportingSecurity;
import guru.mmp.application.web.template.TemplateWebApplication;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.LoadableDetachableModel;

//~--- JDK imports ------------------------------------------------------------

import java.text.SimpleDateFormat;

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
      final Link<Void> addLink = new Link<Void>("addLink")
      {
        private static final long serialVersionUID = 1000000;

        @Override
        public void onClick()
        {
          AddReportDefinitionPage page =
            new AddReportDefinitionPage(ReportDefinitionAdministrationPage.this);

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

      final ListView<ReportDefinition> listView =
        new ListView<ReportDefinition>("reportDefinition", ldm)
      {
        private static final long serialVersionUID = 1000000;

        @Override
        protected void populateItem(final ListItem<ReportDefinition> item)
        {
          ReportDefinition reportDefinition = item.getModelObject();

          SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm a");

          item.add(new Label("name", reportDefinition.getName()));

          if (reportDefinition.getUpdated() != null)
          {
            item.add(new Label("lastUpdated", sdf.format(reportDefinition.getUpdated())));
          }
          else
          {
            item.add(new Label("lastUpdated", "N/A"));
          }

          if (reportDefinition.getUpdatedBy() != null)
          {
            item.add(new Label("lastUpdatedBy", reportDefinition.getUpdatedBy()));
          }
          else
          {
            item.add(new Label("lastUpdatedBy", "N/A"));
          }

          final Link<Void> updateLink = new Link<Void>("updateLink")
          {
            private static final long serialVersionUID = 1000000;

            @Override
            public void onClick()
            {
              UpdateReportDefinitionPage page =
                new UpdateReportDefinitionPage(ReportDefinitionAdministrationPage.this,
                  item.getModelObject());

              setResponsePage(page);
            }
          };

          item.add(updateLink);

          final Link<Void> removeLink = new Link<Void>("removeLink")
          {
            private static final long serialVersionUID = 1000000;

            @Override
            public void onClick()
            {
              ConfirmRemoveReportDefinitionPage page =
                new ConfirmRemoveReportDefinitionPage(ReportDefinitionAdministrationPage.this,
                  item.getModelObject());

              setResponsePage(page);
            }
          };

          item.add(removeLink);
        }
      };

      add(listView);
    }
    catch (Throwable e)
    {
      throw new WebApplicationException(
          "Failed to initialise the ReportDefinitionAdministrationPage", e);
    }
  }
}

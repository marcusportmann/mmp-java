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

import guru.mmp.application.messaging.ErrorReportSummary;
import guru.mmp.application.messaging.IMessagingService;
import guru.mmp.application.web.WebApplicationException;
import guru.mmp.application.web.WebSession;
import guru.mmp.application.web.page.WebPageSecurity;
import guru.mmp.application.web.template.TemplateMessagingSecurity;

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
 * The <code>ErrorReportsPage</code> class implements the
 * "Error Reports" page for the Web Application Template.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
@WebPageSecurity(TemplateMessagingSecurity.FUNCTION_CODE_ERROR_REPORTS)
public class ErrorReportsPage extends TemplateWebPage
{
  private static final int MAXIMUM_NUMBER_OF_ENTRIES = 50;
  private static final long serialVersionUID = 1000000;

  /* Messaging Service */
  @Inject
  private IMessagingService messagingService;

  /**
   * Constructs a new <code>ErrorReportsPage</code>.
   */
  public ErrorReportsPage()
  {
    super("Error Reports");

    try
    {
      LoadableDetachableModel<List<ErrorReportSummary>> ldm =
        new LoadableDetachableModel<List<ErrorReportSummary>>()
      {
        private static final long serialVersionUID = 1000000;

        @Override
        protected List<ErrorReportSummary> load()
        {
          WebSession session = getWebApplicationSession();

          try
          {
            return messagingService.getMostRecentErrorReportSummaries(MAXIMUM_NUMBER_OF_ENTRIES);
          }
          catch (Throwable e)
          {
            throw new WebApplicationException(
                "Failed to load the form audit log entries for the organisation ("
                + session.getOrganisation() + ")", e);
          }
        }
      };

      final ListView<ErrorReportSummary> listView =
        new ListView<ErrorReportSummary>("errorReportSummary", ldm)
      {
        private static final long serialVersionUID = 1000000;

        @Override
        protected void populateItem(ListItem<ErrorReportSummary> item)
        {
          ErrorReportSummary errorReportSummary = item.getModelObject();

          final String id = errorReportSummary.getId();

          SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

          item.add(new Label("application", errorReportSummary.getApplicationName()));
          item.add(new Label("created", sdf.format(errorReportSummary.getCreated())));
          item.add(new Label("who", errorReportSummary.getWho()));

          final Link<Void> viewErrorReportLink = new Link<Void>("viewErrorReportLink")
          {
            private static final long serialVersionUID = 1000000;

            @Override
            public void onClick()
            {
              ViewErrorReportPage page = new ViewErrorReportPage(getPageReference(), id);

              setResponsePage(page);
            }
          };

          viewErrorReportLink.add(new Label("id", errorReportSummary.getId()));

          item.add(viewErrorReportLink);
        }
      };

      add(listView);
    }
    catch (Throwable e)
    {
      throw new WebApplicationException("Failed to initialise the ErrorReportsPage", e);
    }
  }
}

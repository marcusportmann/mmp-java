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

package guru.mmp.sample.web.pages;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.reporting.ReportType;
import guru.mmp.application.web.WebSession;
import guru.mmp.application.web.pages.WebPageSecurity;
import guru.mmp.application.web.servlets.ViewReportParameters;
import guru.mmp.application.web.template.TemplateSecurity;
import guru.mmp.application.web.template.pages.TemplateWebPage;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.request.http.handler.RedirectRequestHandler;

import java.util.HashMap;
import java.util.Map;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>DashboardPage</code> class implements the "Dashboard"
 * page for the web application.
 *
 * @author Marcus Portmann
 */
@WebPageSecurity(TemplateSecurity.FUNCTION_CODE_DASHBOARD)
public class DashboardPage extends TemplateWebPage
{
  private static final long serialVersionUID = 1000000;

  /**
   * Constructs a new <code>DashboardPage</code>.
   */
  public DashboardPage()
  {
    super("Dashboard");

    // The "sampleReportLink" link
    Link<Void> sampleReportLink = new Link<Void>("sampleReportLink")
    {
      private static final long serialVersionUID = 1000000;

      @Override
      public void onClick()
      {
        WebSession session = getWebApplicationSession();

        Map<String, Object> reportParameters = new HashMap<>();

        ViewReportParameters viewReportParameters = new ViewReportParameters("Sample Report",
            ReportType.DATABASE, "2a4b74e8-7f03-416f-b058-b35bb06944ef", reportParameters);

        session.addViewReportParameters(viewReportParameters);

        getRequestCycle().scheduleRequestHandlerAfterCurrent(new RedirectRequestHandler(
            "/servlet/ViewReportServlet?viewReportParametersId=" + viewReportParameters.getId()));
      }
    };

    add(sampleReportLink);
  }
}

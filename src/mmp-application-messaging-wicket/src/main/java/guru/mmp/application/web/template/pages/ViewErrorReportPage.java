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

import guru.mmp.application.messaging.ErrorReport;
import guru.mmp.application.messaging.IMessagingService;
import guru.mmp.application.web.WebApplicationException;
import guru.mmp.application.web.pages.WebPageSecurity;
import guru.mmp.application.web.template.TemplateMessagingSecurity;
import org.apache.wicket.PageReference;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.UUID;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>ViewErrorReportPage</code> class implements the "View Error Report"
 * page for the Web Application Template.
 *
 * @author Marcus Portmann
 */
@WebPageSecurity(TemplateMessagingSecurity.FUNCTION_CODE_ERROR_REPORTS)
public class ViewErrorReportPage extends TemplateWebPage
{
  /* Logger */
  @SuppressWarnings("unused")
  private static final Logger logger = LoggerFactory.getLogger(ViewErrorReportPage.class);
  private static final long serialVersionUID = 1000000;

  /* Messaging Service */
  @Inject
  private IMessagingService messagingService;

  /**
   * Constructs a new <code>ViewErrorReportPage</code>.
   * <p/>
   * Hidden default constructor to support CDI.
   */
  @SuppressWarnings("unused")
  protected ViewErrorReportPage() {}

  /**
   * Constructs a new <code>ViewErrorReportPage</code>.
   *
   * @param previousPage the previous page
   * @param id           the Universally Unique Identifier (UUID) used to uniquely identify the
   *                     error report
   */
  public ViewErrorReportPage(PageReference previousPage, UUID id)
  {
    super("View Error Report", "Viewing the error report: " + id);

    try
    {
      // The "backLink" link
      Link<Void> backLink = new Link<Void>("backLink")
      {
        private static final long serialVersionUID = 1000000;

        @Override
        public void onClick()
        {
          setResponsePage(previousPage.getPage());
        }
      };
      add(backLink);

      ErrorReport errorReport = messagingService.getErrorReport(id);

      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

      Form<Void> form = new Form<>("viewErrorReportForm");

      form.add(new Label("id", errorReport.getId()));
      form.add(new Label("applicationId", errorReport.getApplicationId()));
      form.add(new Label("applicationVersion", String.valueOf(
          errorReport.getApplicationVersion())));
      form.add(new Label("deviceId", errorReport.getDeviceId()));
      form.add(new Label("created", sdf.format(errorReport.getCreated())));
      form.add(new Label("who", errorReport.getWho()));
      form.add(new Label("description", errorReport.getDescription()));
      form.add(new Label("feedback", errorReport.getFeedback()));
      form.add(new Label("detail", errorReport.getDetail()));

      byte[] data = errorReport.getData();

      if (data != null)
      {
        form.add(new Label("data", new String(errorReport.getData(), "UTF-8")));
      }
      else
      {
        form.add(new Label("data", ""));
      }

      add(form);
    }
    catch (Throwable e)
    {
      throw new WebApplicationException("Failed to initialise the ViewErrorReportPage", e);
    }
  }
}

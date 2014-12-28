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
import guru.mmp.application.web.component.FeedbackList;
import guru.mmp.application.web.page.WebPageSecurity;
import guru.mmp.application.web.template.TemplateReportingSecurity;
import guru.mmp.application.web.template.TemplateWebApplication;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//~--- JDK imports ------------------------------------------------------------

import javax.inject.Inject;

/**
 * The <code>ConfirmRemoveReportDefinitionPage</code> class implements the
 * "Confirm Remove Report Definition" page for the Web Application Template.
 *
 * @author Marcus Portmann
 */
@WebPageSecurity(TemplateReportingSecurity.FUNCTION_CODE_REMOVE_REPORT_DEFINITION)
public class ConfirmRemoveReportDefinitionPage extends TemplateWebPage
{
  private static final long serialVersionUID = 1000000;

  /* Logger */
  private static final Logger logger =
    LoggerFactory.getLogger(ConfirmRemoveReportDefinitionPage.class);

  /* Reporting Service */
  @Inject
  private IReportingService reportingService;

  /**
   * Constructs a new <code>ConfirmRemoveReportDefinitionPage</code>.
   *
   * @param previousPage     the previous page
   * @param reportDefinition the report definition to remove
   */
  public ConfirmRemoveReportDefinitionPage(final Page previousPage,
      final ReportDefinition reportDefinition)
  {
    super("Confirm Remove Report Definition", "Confirm Remove Report Definition");
    setTitle(((TemplateWebApplication) getApplication()).getDisplayName()
        + " | Confirm Remove Report Definition");

    try
    {
      add(new FeedbackList("feedback"));

      Form<Void> form = new Form<>("confirmRemoveReportDefinitionForm");

      form.setMarkupId("confirmRemoveReportDefinitionForm");
      form.setOutputMarkupId(true);
      add(form);

      // The "name" field
      Label nameLabel = new Label("name", reportDefinition.getName());

      form.add(nameLabel);

      Button yesButton = new Button("yesButton")
      {
        private static final long serialVersionUID = 1000000;

        @Override
        public void onSubmit()
        {
          try
          {
            reportingService.deleteReportDefinition(reportDefinition);

            setResponsePage(previousPage);
          }
          catch (Throwable e)
          {
            logger.error("Failed to remove the report definition (" + reportDefinition.getId()
                + "): " + e.getMessage(), e);
            error("Your request could not be processed at this time.");
            error("Please contact your administrator.");
          }
        }
      };

      form.add(yesButton);

      Button noButton = new Button("noButton")
      {
        private static final long serialVersionUID = 1000000;

        @Override
        public void onSubmit()
        {
          setResponsePage(previousPage);
        }
      };

      form.add(noButton);
    }
    catch (Throwable e)
    {
      throw new WebApplicationException(
          "Failed to initialise the ConfirmRemoveReportDefinitionPage", e);
    }
  }

  /**
   * Constructs a new <code>ConfirmRemoveReportDefinitionPage</code>.
   * <p/>
   * Hidden default constructor to support CDI.
   */
  protected ConfirmRemoveReportDefinitionPage() {}
}

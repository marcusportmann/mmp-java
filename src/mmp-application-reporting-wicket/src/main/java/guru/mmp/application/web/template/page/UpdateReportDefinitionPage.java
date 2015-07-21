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
import guru.mmp.application.web.WebApplicationException;
import guru.mmp.application.web.WebSession;
import guru.mmp.application.web.page.WebPageSecurity;
import guru.mmp.application.web.template.TemplateReportingSecurity;
import guru.mmp.application.web.template.TemplateWebApplication;
import guru.mmp.application.web.template.component.ReportDefinitionInputPanel;
import org.apache.wicket.PageReference;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>UpdateReportDefinitionPage</code> class implements the
 * "Update ReportDefinition" page for the Web Application Template.
 *
 * @author Marcus Portmann
 */
@WebPageSecurity(TemplateReportingSecurity.FUNCTION_CODE_UPDATE_REPORT_DEFINITION)
public class UpdateReportDefinitionPage extends TemplateWebPage
{
  private static final long serialVersionUID = 1000000;

  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(UpdateReportDefinitionPage.class);

  /* Reporting Service */
  @Inject
  private IReportingService reportingService;

  /**
   * Constructs a new <code>UpdateReportDefinitionPage</code>.
   *
   * @param previousPage          the previous page
   * @param reportDefinitionModel the model for the report definition
   */
  public UpdateReportDefinitionPage(final PageReference previousPage,
      final IModel<ReportDefinition> reportDefinitionModel)
  {
    super("Update ReportDefinition", "Update ReportDefinition");
    setTitle(((TemplateWebApplication) getApplication()).getDisplayName()
        + " | Update ReportDefinition");

    try
    {
      Form<ReportDefinition> updateForm = new Form<>("updateForm",
        new CompoundPropertyModel<>(reportDefinitionModel));

      final ReportDefinitionInputPanel reportDefinitionInputPanel =
        new ReportDefinitionInputPanel("reportDefinition", reportDefinitionModel, true);

      updateForm.add(reportDefinitionInputPanel);

      // The "updateButton" button
      Button updateButton = new Button("updateButton")
      {
        private static final long serialVersionUID = 1000000;

        @Override
        public void onSubmit()
        {
          FileUpload fileUpload = null;

          try
          {
            ReportDefinition reportDefinition = reportDefinitionModel.getObject();

            WebSession session = getWebApplicationSession();

            reportDefinition.setOrganisation(session.getOrganisation());

            fileUpload = reportDefinitionInputPanel.getFileUpload();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int numberOfBytesRead;
            InputStream in = new BufferedInputStream(fileUpload.getInputStream());
            byte[] buffer = new byte[512];

            while ((numberOfBytesRead = in.read(buffer)) != -1)
            {
              baos.write(buffer, 0, numberOfBytesRead);
            }

            reportDefinition.setTemplate(baos.toByteArray());

            reportingService.saveReportDefinition(reportDefinition, session.getUsername());

            setResponsePage(previousPage.getPage());
          }
          catch (Throwable e)
          {
            logger.error("Failed to update the report definition: " + e.getMessage(), e);
            error("Your request could not be processed at this time.");
            error("Please contact your administrator.");
          }
          finally
          {
            try
            {
              if (fileUpload != null)
              {
                // Delete the uploaded file
                fileUpload.delete();
              }
            }
            catch (Throwable e)
            {
              logger.error("Failed to delete the uploaded file (" + fileUpload.getClientFileName()
                  + ")", e);
            }
          }
        }
      };

      updateButton.setDefaultFormProcessing(true);
      updateForm.add(updateButton);

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
      throw new WebApplicationException("Failed to initialise the UpdateReportDefinitionPage", e);
    }
  }

  /**
   * Hidden <code>UpdateReportDefinitionPage</code> constructor.
   */
  protected UpdateReportDefinitionPage() {}
}

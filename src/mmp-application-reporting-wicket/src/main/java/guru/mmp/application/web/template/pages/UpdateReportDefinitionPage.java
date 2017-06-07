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

import guru.mmp.application.reporting.IReportingService;
import guru.mmp.application.reporting.ReportDefinition;
import guru.mmp.application.web.WebApplicationException;
import guru.mmp.application.web.WebSession;
import guru.mmp.application.web.pages.WebPageSecurity;
import guru.mmp.application.web.template.TemplateReportingSecurity;
import guru.mmp.application.web.template.components.ReportDefinitionInputPanel;
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
 * "Update Report Definition" page for the Web Application Template.
 *
 * @author Marcus Portmann
 */
@WebPageSecurity(TemplateReportingSecurity.FUNCTION_CODE_REPORT_DEFINITION_ADMINISTRATION)
public class UpdateReportDefinitionPage extends TemplateWebPage
{
  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(UpdateReportDefinitionPage.class);
  private static final long serialVersionUID = 1000000;

  /* Reporting Service */
  @Inject
  private IReportingService reportingService;

  /**
   * Constructs a new <code>UpdateReportDefinitionPage</code>.
   *
   * @param previousPage          the previous page
   * @param reportDefinitionModel the model for the report definition
   */
  public UpdateReportDefinitionPage(PageReference previousPage,
      IModel<ReportDefinition> reportDefinitionModel)
  {
    super("Update Report Definition");

    try
    {
      Form<ReportDefinition> updateForm = new Form<>("updateForm", new CompoundPropertyModel<>(
          reportDefinitionModel));

      ReportDefinitionInputPanel reportDefinitionInputPanel = new ReportDefinitionInputPanel(
          "reportDefinition", true);

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

            reportingService.saveReportDefinition(reportDefinition);

            setResponsePage(previousPage.getPage());
          }
          catch (Throwable e)
          {
            logger.error("Failed to update the report definition: " + e.getMessage(), e);

            UpdateReportDefinitionPage.this.error("Failed to update the report definition");
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
              logger.error(String.format("Failed to delete the uploaded file (%s)",
                  fileUpload.getClientFileName()), e);
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
}

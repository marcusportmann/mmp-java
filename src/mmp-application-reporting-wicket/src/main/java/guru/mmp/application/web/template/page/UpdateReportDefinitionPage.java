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

import guru.mmp.application.codes.CodeCategory;
import guru.mmp.application.reporting.IReportingService;
import guru.mmp.application.reporting.ReportDefinition;
import guru.mmp.application.web.WebApplicationException;
import guru.mmp.application.web.WebSession;
import guru.mmp.application.web.component.FeedbackLabel;
import guru.mmp.application.web.component.FeedbackList;
import guru.mmp.application.web.page.WebPageSecurity;
import guru.mmp.application.web.template.TemplateReportingSecurity;
import guru.mmp.application.web.template.TemplateWebApplication;

import org.apache.wicket.Page;
import org.apache.wicket.PageReference;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//~--- JDK imports ------------------------------------------------------------

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.inject.Inject;

/**
 * The <code>UpdateReportDefinitionPage</code> class implements the "Update Report Definition"
 * page for the Web Application Template.
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
    super("Update Report Definition", "Update Report Definition");
    setTitle(((TemplateWebApplication) getApplication()).getDisplayName()
        + " | Update Report Definition");

    try
    {
      add(new FeedbackList("feedback"));

      Form<Void> form = new Form<>("updateReportDefinitionForm");

      form.setMarkupId("updateReportDefinitionForm");
      form.setOutputMarkupId(true);
      add(form);

      // The "id" field
      final TextField<String> idField = new TextField<>("id",
        new PropertyModel<>(reportDefinition, "id"));

      idField.setRequired(true);
      form.add(idField);
      form.add(new FeedbackLabel("idFeedback", idField));

      // The "name" field
      final TextField<String> nameField = new TextField<>("name",
        new PropertyModel<>(reportDefinition, "name"));

      nameField.setRequired(true);
      form.add(nameField);
      form.add(new FeedbackLabel("nameFeedback", nameField));

      // The "fileUpload" field
      final FileUploadField fileUploadField = new FileUploadField("fileUpload");

      fileUploadField.setRequired(true);
      form.add(fileUploadField);
      form.add(new FeedbackLabel("fileUploadFeedback", fileUploadField));

      Button updateButton = new Button("updateButton")
      {
        private static final long serialVersionUID = 1000000;

        @Override
        public void onSubmit()
        {
          FileUpload fileUpload = null;

          try
          {
            WebSession session = getWebApplicationSession();

            fileUpload = fileUploadField.getFileUpload();

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

            setResponsePage(previousPage);
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
      form.add(updateButton);

      Button cancelButton = new Button("cancelButton")
      {
        private static final long serialVersionUID = 1000000;

        @Override
        public void onSubmit()
        {
          setResponsePage(previousPage);
        }
      };

      cancelButton.setDefaultFormProcessing(false);
      form.add(cancelButton);
    }
    catch (Throwable e)
    {
      throw new WebApplicationException("Failed to initialise the UpdateReportDefinitionPage", e);
    }
  }

  /**
   * Constructs a new <code>UpdateReportDefinitionPage</code>.
   * <p/>
   * Hidden default constructor to support CDI.
   */
  protected UpdateReportDefinitionPage() {}
}

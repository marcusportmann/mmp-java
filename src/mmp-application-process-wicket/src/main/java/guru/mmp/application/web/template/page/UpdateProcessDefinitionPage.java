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

package guru.mmp.application.web.template.page;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.process.IProcessService;
import guru.mmp.application.process.ProcessDefinition;
import guru.mmp.application.web.WebApplicationException;
import guru.mmp.application.web.WebSession;
import guru.mmp.application.web.page.WebPageSecurity;
import guru.mmp.application.web.template.TemplateProcessSecurity;
import guru.mmp.application.web.template.component.ProcessDefinitionInputPanel;

import org.apache.wicket.PageReference;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//~--- JDK imports ------------------------------------------------------------

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.inject.Inject;

/**
 * The <code>UpdateProcessDefinitionPage</code> class implements the
 * "Update Process Definition" page for the Web Application Template.
 *
 * @author Marcus Portmann
 */
@WebPageSecurity(TemplateProcessSecurity.FUNCTION_CODE_UPDATE_PROCESS_DEFINITION)
public class UpdateProcessDefinitionPage extends TemplateWebPage
{
  private static final long serialVersionUID = 1000000;

  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(UpdateProcessDefinitionPage.class);

  /* Process Service */
  @Inject
  private IProcessService processService;

  /**
   * Constructs a new <code>UpdateProcessDefinitionPage</code>.
   *
   * @param previousPage          the previous page
   * @param processDefinitionModel the model for the process definition
   */
  public UpdateProcessDefinitionPage(PageReference previousPage,
      IModel<ProcessDefinition> processDefinitionModel)
  {
    super("Update Process Definition");

    try
    {
      processDefinitionModel.getObject().setVersion(processDefinitionModel.getObject().getVersion()
          + 1);

      Form<ProcessDefinition> updateForm = new Form<>("updateForm",
        new CompoundPropertyModel<>(processDefinitionModel));

      ProcessDefinitionInputPanel processDefinitionInputPanel =
        new ProcessDefinitionInputPanel("processDefinition", true);

      updateForm.add(processDefinitionInputPanel);

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
            ProcessDefinition processDefinition = processDefinitionModel.getObject();

            WebSession session = getWebApplicationSession();

            fileUpload = processDefinitionInputPanel.getFileUpload();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int numberOfBytesRead;
            InputStream in = new BufferedInputStream(fileUpload.getInputStream());
            byte[] buffer = new byte[512];

            while ((numberOfBytesRead = in.read(buffer)) != -1)
            {
              baos.write(buffer, 0, numberOfBytesRead);
            }

            processDefinition.setData(baos.toByteArray());

            processService.createProcessDefinition(processDefinition);

            setResponsePage(previousPage.getPage());
          }
          catch (Throwable e)
          {
            logger.error("Failed to update the process definition: " + e.getMessage(), e);
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
      throw new WebApplicationException("Failed to initialise the UpdateProcessDefinitionPage", e);
    }
  }

  /**
   * Hidden <code>UpdateProcessDefinitionPage</code> constructor.
   */
  @SuppressWarnings("unused")
  protected UpdateProcessDefinitionPage() {}
}

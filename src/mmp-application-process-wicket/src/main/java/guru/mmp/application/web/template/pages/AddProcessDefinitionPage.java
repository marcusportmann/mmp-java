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

import guru.mmp.application.process.IProcessService;
import guru.mmp.application.process.ProcessDefinition;
import guru.mmp.application.web.WebApplicationException;
import guru.mmp.application.web.WebSession;
import guru.mmp.application.web.pages.WebPageSecurity;
import guru.mmp.application.web.template.TemplateProcessSecurity;
import guru.mmp.application.web.template.components.ProcessDefinitionInputPanel;
import org.apache.wicket.PageReference;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.UUID;

/**
 * The <code>AddProcessDefinitionPage</code> class implements the "Add Process Definition"
 * page for the Web Application Template.
 *
 * @author Marcus Portmann
 */
@WebPageSecurity(TemplateProcessSecurity.FUNCTION_CODE_ADD_PROCESS_DEFINITION)
public class AddProcessDefinitionPage
  extends TemplateWebPage
{
  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(AddProcessDefinitionPage.class);

  private static final long serialVersionUID = 1000000;

  /* Process Service */
  @Inject
  private IProcessService processService;

  /**
   * Constructs a new <code>AddProcessDefinitionPage</code>.
   *
   * @param previousPage the previous page
   */
  public AddProcessDefinitionPage(PageReference previousPage)
  {
    super("Add Process Definition");

    try
    {
      Form<ProcessDefinition> addForm = new Form<>("addForm",
        new CompoundPropertyModel<>(new Model<>(new ProcessDefinition())));

      addForm.getModelObject().setId(UUID.randomUUID());
      addForm.getModelObject().setVersion(1);

      ProcessDefinitionInputPanel processDefinitionInputPanel = new ProcessDefinitionInputPanel(
        "processDefinition", false);

      addForm.add(processDefinitionInputPanel);

      // The "addButton" button
      Button addButton = new Button("addButton")
      {
        private static final long serialVersionUID = 1000000;

        @Override
        public void onSubmit()
        {
          FileUpload fileUpload = null;

          try
          {
            ProcessDefinition processDefinition = addForm.getModelObject();

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
            logger.error("Failed to add the process definition: " + e.getMessage(), e);
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
              logger.error(String.format("Failed to delete the uploaded file (%s)",
                fileUpload.getClientFileName()), e);
            }
          }
        }
      };

      addButton.setDefaultFormProcessing(true);
      addForm.add(addButton);

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
      addForm.add(cancelButton);

      add(addForm);
    }
    catch (Throwable e)
    {
      throw new WebApplicationException("Failed to initialise the AddProcessDefinitionPage", e);
    }
  }

  /**
   * Constructs a new <code>AddProcessDefinitionPage</code>.
   * <p/>
   * Hidden default constructor to support CDI.
   */
  @SuppressWarnings("unused")
  protected AddProcessDefinitionPage() {}
}

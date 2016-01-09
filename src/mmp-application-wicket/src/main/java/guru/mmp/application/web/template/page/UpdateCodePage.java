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

import guru.mmp.application.codes.Code;
import guru.mmp.application.codes.ICodesService;
import guru.mmp.application.web.WebApplicationException;
import guru.mmp.application.web.WebSession;
import guru.mmp.application.web.page.WebPageSecurity;
import guru.mmp.application.web.template.TemplateSecurity;
import guru.mmp.application.web.template.component.CodeInputPanel;
import org.apache.wicket.PageReference;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

/**
 * The <code>UpdateCodePage</code> class implements the
 * "Update Code" page for the Web Application Template.
 *
 * @author Marcus Portmann
 */
@WebPageSecurity(TemplateSecurity.FUNCTION_CODE_UPDATE_CODE)
public class UpdateCodePage
  extends TemplateWebPage
{
  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(UpdateCodePage.class);

  private static final long serialVersionUID = 1000000;

  /* Codes Service */
  @Inject
  private ICodesService codesService;

  /**
   * Constructs a new <code>UpdateCodePage</code>.
   *
   * @param previousPage the previous page
   * @param codeModel    the model for the code
   */
  public UpdateCodePage(PageReference previousPage, IModel<Code> codeModel)
  {
    super("Update Code");

    try
    {
      Form<Code> updateForm = new Form<>("updateForm", new CompoundPropertyModel<>(codeModel));

      updateForm.add(new CodeInputPanel("code", true));

      // The "updateButton" button
      Button updateButton = new Button("updateButton")
      {
        private static final long serialVersionUID = 1000000;

        @Override
        public void onSubmit()
        {
          try
          {
            WebSession session = getWebApplicationSession();

            Code code = updateForm.getModelObject();

            codesService.updateCode(code);

            setResponsePage(previousPage.getPage());
          }
          catch (Throwable e)
          {
            logger.error("Failed to update the code: " + e.getMessage(), e);

            UpdateCodePage.this.error("Failed to update the code");
          }
        }
      };
      updateButton.setDefaultFormProcessing(true);
      updateForm.add(updateButton);

      // The "cancelButton" button
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
      throw new WebApplicationException("Failed to initialise the UpdateCodePage", e);
    }
  }

  /**
   * Hidden <code>UpdateCodePage</code> constructor.
   */
  @SuppressWarnings("unused")
  protected UpdateCodePage() {}
}

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

import guru.mmp.application.codes.CodeCategory;
import guru.mmp.application.codes.CodeCategoryType;
import guru.mmp.application.codes.ICodesService;
import guru.mmp.application.web.WebApplicationException;
import guru.mmp.application.web.pages.WebPageSecurity;
import guru.mmp.application.web.template.TemplateSecurity;
import guru.mmp.application.web.template.components.CodeCategoryInputPanel;
import org.apache.wicket.PageReference;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>UpdateCodeCategoryPage</code> class implements the
 * "Update Code Category" page for the Web Application Template.
 *
 * @author Marcus Portmann
 */
@WebPageSecurity(TemplateSecurity.FUNCTION_CODE_CODE_CATEGORY_ADMINISTRATION)
class UpdateCodeCategoryPage extends TemplateWebPage
{
  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(UpdateCodeCategoryPage.class);
  private static final long serialVersionUID = 1000000;

  /* Codes Service */
  @Inject
  private ICodesService codesService;

  /**
   * Constructs a new <code>UpdateCodeCategoryPage</code>.
   *
   * @param previousPage      the previous page
   * @param codeCategoryModel the model for the code category
   */
  UpdateCodeCategoryPage(PageReference previousPage, IModel<CodeCategory> codeCategoryModel)
  {
    super("Update Code Category");

    try
    {
      Form<CodeCategory> updateForm = new Form<>("updateForm", new CompoundPropertyModel<>(
          codeCategoryModel));

      updateForm.add(new CodeCategoryInputPanel("codeCategory", true));

      // The "updateButton" button
      Button updateButton = new Button("updateButton")
      {
        private static final long serialVersionUID = 1000000;

        @Override
        public void onSubmit()
        {
          try
          {
            CodeCategory codeCategory = updateForm.getModelObject();

            if (codeCategory.getCategoryType() != CodeCategoryType.LOCAL_CUSTOM)
            {
              codeCategory.setCodeData(null);
            }

            if ((codeCategory.getCategoryType() != CodeCategoryType.REMOTE_HTTP_SERVICE)
                && (codeCategory.getCategoryType() != CodeCategoryType.REMOTE_WEB_SERVICE))
            {
              codeCategory.setEndPoint(null);
              codeCategory.setIsEndPointSecure(false);
              codeCategory.setIsCacheable(false);
              codeCategory.setCacheExpiry(null);
            }

            if (!codeCategory.getIsCacheable())
            {
              codeCategory.setCacheExpiry(null);
            }

            codesService.updateCodeCategory(codeCategory);

            setResponsePage(previousPage.getPage());
          }
          catch (Throwable e)
          {
            logger.error("Failed to update the code category: " + e.getMessage(), e);

            UpdateCodeCategoryPage.this.error("Failed to update the code category");
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
      throw new WebApplicationException("Failed to initialise the UpdateCodeCategoryPage", e);
    }
  }
}

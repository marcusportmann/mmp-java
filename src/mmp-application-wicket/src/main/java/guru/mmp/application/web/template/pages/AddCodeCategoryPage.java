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

import guru.mmp.application.codes.CodeCategory;
import guru.mmp.application.codes.CodeCategoryType;
import guru.mmp.application.codes.ICodesService;
import guru.mmp.application.web.WebApplicationException;
import guru.mmp.application.web.WebSession;
import guru.mmp.application.web.pages.WebPageSecurity;
import guru.mmp.application.web.template.TemplateSecurity;
import guru.mmp.application.web.template.components.CodeCategoryInputPanel;
import org.apache.wicket.PageReference;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Date;
import java.util.UUID;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>AddCodeCategoryPage</code> class implements the
 * "Add Code Category" page for the Web Application Template.
 *
 * @author Marcus Portmann
 */
@WebPageSecurity(TemplateSecurity.FUNCTION_CODE_ADD_CODE_CATEGORY)
public class AddCodeCategoryPage extends TemplateWebPage
{
  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(AddCodeCategoryPage.class);
  private static final long serialVersionUID = 1000000;

  /* Codes Service */
  @Inject
  private ICodesService codesService;

  /**
   * Hidden <code>AddCodeCategoryPage</code> constructor.
   */
  @SuppressWarnings("unused")
  protected AddCodeCategoryPage() {}

  /**
   * Constructs a new <code>AddCodeCategoryPage</code>.
   *
   * @param previousPage the previous page
   */
  public AddCodeCategoryPage(PageReference previousPage)
  {
    super("Add Code Category");

    try
    {
      Form<CodeCategory> addForm = new Form<>("addForm", new CompoundPropertyModel<>(new Model<>(
          new CodeCategory())));

      addForm.getModelObject().setId(UUID.randomUUID());

      addForm.add(new CodeCategoryInputPanel("codeCategory", false));

      // The "addButton" button
      Button addButton = new Button("addButton")
      {
        private static final long serialVersionUID = 1000000;

        @Override
        public void onSubmit()
        {
          try
          {
            WebSession session = getWebApplicationSession();

            Date created = new Date();

            CodeCategory codeCategory = addForm.getModelObject();

            codeCategory.setUpdated(created);

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

            codesService.createCodeCategory(codeCategory);

            setResponsePage(previousPage.getPage());
          }
          catch (Throwable e)
          {
            logger.error("Failed to add the code category: " + e.getMessage(), e);

            AddCodeCategoryPage.this.error("Failed to add the code category");
          }
        }
      };
      addButton.setDefaultFormProcessing(true);
      addForm.add(addButton);

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
      addForm.add(cancelButton);

      add(addForm);
    }
    catch (Throwable e)
    {
      throw new WebApplicationException("Failed to initialise the AddCodeCategoryPage", e);
    }
  }

  @Override
  protected void onInitialize()
  {
    super.onInitialize();
  }
}

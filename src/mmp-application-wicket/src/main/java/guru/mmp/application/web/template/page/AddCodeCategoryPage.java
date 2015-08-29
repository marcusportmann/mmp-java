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

import guru.mmp.application.codes.CodeCategory;
import guru.mmp.application.codes.CodeCategoryType;
import guru.mmp.application.codes.ICodesService;
import guru.mmp.application.web.WebApplicationException;
import guru.mmp.application.web.WebSession;
import guru.mmp.application.web.page.WebPageSecurity;
import guru.mmp.application.web.template.TemplateSecurity;
import guru.mmp.application.web.template.component.CodeCategoryInputPanel;

import org.apache.wicket.PageReference;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//~--- JDK imports ------------------------------------------------------------

import java.util.Date;
import java.util.UUID;

import javax.inject.Inject;

/**
 * The <code>AddCodeCategoryPage</code> class implements the
 * "Add Code Category" page for the Web Application Template.
 *
 * @author Marcus Portmann
 */
@WebPageSecurity(TemplateSecurity.FUNCTION_CODE_ADD_CODE_CATEGORY)
public class AddCodeCategoryPage extends TemplateWebPage
{
  private static final long serialVersionUID = 1000000;

  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(AddCodeCategoryPage.class);

  /* Codes Service */
  @Inject
  private ICodesService codesService;

  @Override
  protected void onInitialize()
  {
    super.onInitialize();
  }

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
      Form<CodeCategory> addForm = new Form<>("addForm",
        new CompoundPropertyModel<>(new Model<>(new CodeCategory())));

      addForm.getModelObject().setId(UUID.randomUUID().toString());

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

            codeCategory.setOrganisation(session.getOrganisation());
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

  /**
   * Hidden <code>AddCodeCategoryPage</code> constructor.
   */
  protected AddCodeCategoryPage() {}
}

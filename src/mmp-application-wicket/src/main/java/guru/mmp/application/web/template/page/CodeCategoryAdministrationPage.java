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
import guru.mmp.application.codes.CodeCategoryType;
import guru.mmp.application.codes.ICodesService;
import guru.mmp.application.web.WebApplicationException;
import guru.mmp.application.web.WebSession;
import guru.mmp.application.web.component.Dialog;
import guru.mmp.application.web.page.WebPageSecurity;
import guru.mmp.application.web.template.TemplateSecurity;
import guru.mmp.application.web.template.TemplateWebApplication;
import guru.mmp.application.web.template.component.PagingNavigator;
import guru.mmp.application.web.template.data.CodeCategoryDataProvider;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.ReuseIfModelsEqualStrategy;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>CodeCategoryAdministrationPage</code> class implements the
 * "Code Category Administration" page for the Web Application Template.
 *
 * @author Marcus Portmann
 */
@WebPageSecurity(TemplateSecurity.FUNCTION_CODE_CODE_CATEGORY_ADMINISTRATION)
public class CodeCategoryAdministrationPage extends TemplateWebPage
{
  private static final long serialVersionUID = 1000000;

  /* Logger */
  private static final Logger logger =
    LoggerFactory.getLogger(CodeCategoryAdministrationPage.class);

  /* Codes Service */
  @Inject
  private ICodesService codesService;

  /**
   * Constructs a new <code>CodeCategoryAdministrationPage</code>.
   */
  public CodeCategoryAdministrationPage()
  {
    super("Code Category Administration", "Code Category Administration");
    setTitle(((TemplateWebApplication) getApplication()).getDisplayName()
        + " | Code Category Administration");

    try
    {
      WebSession session = getWebApplicationSession();

      /*
       * The table container, which allows the table and its associated navigator to be updated
       * using AJAX.
       */
      final WebMarkupContainer tableContainer = new WebMarkupContainer("tableContainer");
      tableContainer.setOutputMarkupId(true);
      add(tableContainer);

      // The dialog used to confirm the removal of a code category
      final RemoveDialog removeDialog = new RemoveDialog(tableContainer);
      add(removeDialog);

      // The "addLink" used to add a new code category
      Link<Void> addLink = new Link<Void>("addLink")
      {
        private static final long serialVersionUID = 1000000;

        @Override
        public void onClick()
        {
          setResponsePage(new AddCodeCategoryPage(getPageReference()));
        }
      };
      add(addLink);

      // The code category data view
      CodeCategoryDataProvider dataProvider =
        new CodeCategoryDataProvider(session.getOrganisation(), false);

      DataView<CodeCategory> dataView = new DataView<CodeCategory>("codeCategory", dataProvider)
      {
        private static final long serialVersionUID = 1000000;

        @Override
        protected void populateItem(Item<CodeCategory> item)
        {
          final IModel<CodeCategory> codeCategoryModel = item.getModel();

          item.add(new Label("name", new PropertyModel<String>(codeCategoryModel, "name")));
          item.add(new Label("categoryType",
              new PropertyModel<String>(codeCategoryModel, "categoryType.name")));

          // The "updateLink" link
          Link<Void> updateLink = new Link<Void>("updateLink")
          {
            private static final long serialVersionUID = 1000000;

            @Override
            public void onClick()
            {
              UpdateCodeCategoryPage page = new UpdateCodeCategoryPage(getPageReference(),
                codeCategoryModel);

              setResponsePage(page);
            }
          };
          item.add(updateLink);

          // The "removeLink" link
          AjaxLink<Void> removeLink = new AjaxLink<Void>("removeLink")
          {
            private static final long serialVersionUID = 1000000;

            @Override
            public void onClick(AjaxRequestTarget target)
            {
              removeDialog.show(target, codeCategoryModel);
            }
          };
          item.add(removeLink);

          // The "codeAdministrationLink" link
          Link<Void> codeAdministrationLink = new Link<Void>("codeAdministrationLink")
          {
            private static final long serialVersionUID = 1000000;

            @Override
            public void onClick()
            {
              CodeCategory codeCategory = codeCategoryModel.getObject();

              String codeCategoryId = codeCategory.getId();
              String codeCategoryName = codeCategory.getName();

              setResponsePage(new CodeAdministrationPage(getPageReference(), codeCategoryId,
                  codeCategoryName));
            }
          };
          codeAdministrationLink.setVisible(codeCategoryModel.getObject().getCategoryType()
              == CodeCategoryType.LOCAL_STANDARD);
          item.add(codeAdministrationLink);
        }
      };
      dataView.setItemsPerPage(10);
      dataView.setItemReuseStrategy(ReuseIfModelsEqualStrategy.getInstance());
      tableContainer.add(dataView);

      tableContainer.add(new PagingNavigator("navigator", dataView));
    }
    catch (Throwable e)
    {
      throw new WebApplicationException("Failed to initialise the CodeCategoryAdministrationPage",
          e);
    }
  }

  /**
   * The <code>RemoveDialog</code> class implements a dialog that allows the removal
   * of a code category to be confirmed.
   */
  private class RemoveDialog extends Dialog
  {
    private static final long serialVersionUID = 1000000;
    private String id;
    private Label nameLabel;

    /**
     * Constructs a new <code>RemoveDialog</code>.
     *
     * @param tableContainer the table container, which allows the code category table and its
     *                       associated navigator to be updated using AJAX
     */
    public RemoveDialog(final WebMarkupContainer tableContainer)
    {
      super("removeDialog");

      nameLabel = new Label("name", Model.of(""));
      nameLabel.setOutputMarkupId(true);
      add(nameLabel);

      add(new AjaxLink<Void>("removeLink")
      {
        private static final long serialVersionUID = 1000000;

        @Override
        public void onClick(AjaxRequestTarget target)
        {
          try
          {
            codesService.deleteCodeCategory(id);

            target.add(tableContainer);

            CodeCategoryAdministrationPage.this.info("Successfully removed the code category "
                + nameLabel.getDefaultModelObjectAsString());
          }
          catch (Throwable e)
          {
            logger.error("Failed to remove the code category (" + id + "): " + e.getMessage(), e);

            CodeCategoryAdministrationPage.this.error("Failed to remove the code category "
                + nameLabel.getDefaultModelObjectAsString());
          }

          target.add(getAlerts());

          hide(target);
        }
      });
    }

    /**
     * @see Dialog#hide(org.apache.wicket.ajax.AjaxRequestTarget)
     *
     * @param target the AJAX request target
     */
    public void hide(AjaxRequestTarget target)
    {
      super.hide(target);
    }

    /**
     * Show the dialog using Ajax.
     *
     * @param target            the AJAX request target
     * @param codeCategoryModel the model for the code category being removed
     */
    public void show(AjaxRequestTarget target, IModel<CodeCategory> codeCategoryModel)
    {
      CodeCategory codeCategory = codeCategoryModel.getObject();

      id = codeCategory.getId();
      nameLabel.setDefaultModelObject(codeCategory.getName());

      target.add(nameLabel);

      super.show(target);
    }
  }
}

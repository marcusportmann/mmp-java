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
import guru.mmp.application.web.template.components.Dialog;
import guru.mmp.application.web.template.components.PagingNavigator;
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
import java.util.UUID;

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
  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(
      CodeCategoryAdministrationPage.class);
  private static final long serialVersionUID = 1000000;

  /* Codes Service */
  @Inject
  private ICodesService codesService;

  /**
   * Constructs a new <code>CodeCategoryAdministrationPage</code>.
   */
  public CodeCategoryAdministrationPage()
  {
    super("Code Categories");

    try
    {
      /*
       * The table container, which allows the table and its associated navigator to be updated
       * using AJAX.
       */
      WebMarkupContainer tableContainer = new WebMarkupContainer("tableContainer");
      tableContainer.setOutputMarkupId(true);
      add(tableContainer);

      // The dialog used to confirm the removal of a code category
      RemoveDialog removeDialog = new RemoveDialog(tableContainer);
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
      tableContainer.add(addLink);

      // The code category data view
      CodeCategoryDataProvider dataProvider = new CodeCategoryDataProvider(false);

      DataView<CodeCategory> dataView = new DataView<CodeCategory>("codeCategory", dataProvider)
      {
        private static final long serialVersionUID = 1000000;

        @Override
        protected void populateItem(Item<CodeCategory> item)
        {
          item.add(new Label("name", new PropertyModel<String>(item.getModel(), "name")));
          item.add(new Label("categoryType", new PropertyModel<String>(item.getModel(),
              "categoryType.name")));

          // The "updateLink" link
          Link<Void> updateLink = new Link<Void>("updateLink")
          {
            private static final long serialVersionUID = 1000000;

            @Override
            public void onClick()
            {
              UpdateCodeCategoryPage page = new UpdateCodeCategoryPage(getPageReference(),
                  item.getModel());

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
              removeDialog.show(target, item.getModel());
            }
          };
          item.add(removeLink);

          // The "codeAdministrationLink" link
          Link<Void> codeAdministrationLink = new Link<Void>("codeAdministrationLink")
          {
            private static final long serialVersionUID = 1000000;

            @Override
            public boolean isVisible()
            {
              return item.getModelObject().getCategoryType() == CodeCategoryType.LOCAL_STANDARD;
            }

            @Override
            public void onClick()
            {
              CodeCategory codeCategory = item.getModelObject();

              UUID codeCategoryId = codeCategory.getId();
              String codeCategoryName = codeCategory.getName();

              setResponsePage(new CodeAdministrationPage(getPageReference(), codeCategoryId,
                  codeCategoryName));
            }
          };

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
    private UUID id;
    private Label nameLabel;

    /**
     * Constructs a new <code>RemoveDialog</code>.
     *
     * @param tableContainer the table container, which allows the code category table and its
     *                       associated navigator to be updated using AJAX
     */
    public RemoveDialog(WebMarkupContainer tableContainer)
    {
      super("removeDialog");

      nameLabel = new Label("name", Model.of(""));

      nameLabel.setOutputMarkupId(true);
      add(nameLabel);

      // The "removeLink" link
      AjaxLink<Void> removeLink = new AjaxLink<Void>("removeLink")
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
            logger.error(String.format("Failed to remove the code category (%s): %s", id,
                e.getMessage()), e);

            CodeCategoryAdministrationPage.this.error("Failed to remove the code category "
                + nameLabel.getDefaultModelObjectAsString());
          }

          target.add(getAlerts());

          hide(target);
        }
      };
      add(removeLink);
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

      if (codeCategory != null)
      {
        id = codeCategory.getId();

        nameLabel.setDefaultModelObject(codeCategory.getName());

        target.add(nameLabel);

        super.show(target);
      }
    }
  }
}

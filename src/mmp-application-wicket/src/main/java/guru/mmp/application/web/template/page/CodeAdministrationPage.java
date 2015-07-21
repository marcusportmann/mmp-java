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

import guru.mmp.application.codes.Code;
import guru.mmp.application.codes.ICodesService;
import guru.mmp.application.web.WebApplicationException;
import guru.mmp.application.web.component.Dialog;
import guru.mmp.application.web.page.WebPageSecurity;
import guru.mmp.application.web.template.TemplateSecurity;
import guru.mmp.application.web.template.TemplateWebApplication;
import guru.mmp.application.web.template.component.PagingNavigator;
import guru.mmp.application.web.template.data.CodeDataProvider;
import guru.mmp.common.util.StringUtil;
import org.apache.wicket.PageReference;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>CodeAdministrationPage</code> class implements the
 * "Code Administration" page for the Web Application Template.
 *
 * @author Marcus Portmann
 */
@WebPageSecurity(TemplateSecurity.FUNCTION_CODE_CODE_ADMINISTRATION)
public class CodeAdministrationPage extends TemplateWebPage
{
  private static final long serialVersionUID = 1000000;

  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(CodeAdministrationPage.class);

  /* Codes Service */
  @Inject
  private ICodesService codesService;

  /**
   * Constructs a new <code>CodeAdministrationPage</code>.
   *
   * @param previousPage   the previous page
   * @param codeCategoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                       code category the codes are associated with
   * @param codeCategoryName the name of the code category
   */
  public CodeAdministrationPage(final PageReference previousPage, final String codeCategoryId,
      final String codeCategoryName)
  {
    super("Code Administration", "Code Administration", codeCategoryName, previousPage);
    setTitle(((TemplateWebApplication) getApplication()).getDisplayName()
        + " | Code Administration");

    try
    {
      /*
       * The table container, which allows the table and its associated navigator to be updated
       * using AJAX.
       */
      final WebMarkupContainer tableContainer = new WebMarkupContainer("tableContainer");
      tableContainer.setOutputMarkupId(true);
      add(tableContainer);

      // The dialog used to confirm the removal of a code
      final RemoveDialog removeDialog = new RemoveDialog(tableContainer);
      add(removeDialog);

      // The "addLink" link
      Link<Void> addLink = new Link<Void>("addLink")
      {
        private static final long serialVersionUID = 1000000;

        @Override
        public void onClick()
        {
          AddCodePage page = new AddCodePage(getPageReference(), codeCategoryId);

          setResponsePage(page);
        }
      };
      add(addLink);

      // The code data view
      CodeDataProvider dataProvider = new CodeDataProvider(codeCategoryId);

      DataView<Code> dataView = new DataView<Code>("code", dataProvider)
      {
        private static final long serialVersionUID = 1000000;

        @Override
        protected void populateItem(Item<Code> item)
        {
          final IModel<Code> codeModel = item.getModel();

          Code code = codeModel.getObject();

          String name = StringUtil.truncate(code.getName(), 25);
          String value = StringUtil.truncate(code.getValue(), 30);

          item.add(new Label("name", Model.of(name)));
          item.add(new Label("value", Model.of(value)));

          // The "updateLink" link
          Link<Void> updateLink = new Link<Void>("updateLink")
          {
            private static final long serialVersionUID = 1000000;

            @Override
            public void onClick()
            {
              UpdateCodePage page = new UpdateCodePage(getPageReference(), codeModel);

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
              removeDialog.show(target, codeModel);
            }
          };
          item.add(removeLink);
        }
      };
      dataView.setItemsPerPage(10);
      dataView.setItemReuseStrategy(ReuseIfModelsEqualStrategy.getInstance());
      tableContainer.add(dataView);

      tableContainer.add(new PagingNavigator("navigator", dataView));

    }
    catch (Throwable e)
    {
      throw new WebApplicationException("Failed to initialise the CodeAdministrationPage", e);
    }
  }

  /**
   * Constructs a new <code>CodeAdministrationPage</code>.
   */
  protected CodeAdministrationPage() {}

  /**
   * The <code>RemoveDialog</code> class implements a dialog that allows the removal
   * of a code to be confirmed.
   */
  private class RemoveDialog extends Dialog
  {
    private static final long serialVersionUID = 1000000;
    private String id;
    private Label nameLabel;

    /**
     * Constructs a new <code>RemoveDialog</code>.
     *
     * @param tableContainer the table container, which allows the code table and its
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
            codesService.deleteCode(id);

            target.add(tableContainer);

            CodeAdministrationPage.this.info("Successfully removed the code "
                + nameLabel.getDefaultModelObjectAsString());
          }
          catch (Throwable e)
          {
            logger.error("Failed to remove the code (" + id + "): " + e.getMessage(), e);

            CodeAdministrationPage.this.error("Failed to remove the code "
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
     * @param target    the AJAX request target
     * @param codeModel the model for the code being removed
     */
    public void show(AjaxRequestTarget target, IModel<Code> codeModel)
    {
      Code code = codeModel.getObject();

      id = code.getId();
      nameLabel.setDefaultModelObject(code.getName());

      target.add(nameLabel);

      super.show(target);
    }
  }
}

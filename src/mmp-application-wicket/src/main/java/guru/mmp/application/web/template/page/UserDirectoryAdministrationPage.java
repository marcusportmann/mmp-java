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

import guru.mmp.application.security.ISecurityService;
import guru.mmp.application.security.User;
import guru.mmp.application.security.UserDirectory;
import guru.mmp.application.security.UserDirectoryType;
import guru.mmp.application.web.WebApplicationException;
import guru.mmp.application.web.page.WebPageSecurity;
import guru.mmp.application.web.template.TemplateSecurity;
import guru.mmp.application.web.template.component.Dialog;
import guru.mmp.application.web.template.component.DropDownChoiceWithFeedback;
import guru.mmp.application.web.template.component.PagingNavigator;
import guru.mmp.application.web.template.data.UserDirectoryDataProvider;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.ReuseIfModelsEqualStrategy;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 *   The <code>UserDirectoryAdministrationPage</code> class implements the
 * "UserDirectory Administration" page for the Web Application Template.
 *
 * @author Marcus Portmann
 */

//@WebPageSecurity(TemplateSecurity.FUNCTION_CODE_SECURITY_ADMINISTRATION)
public class UserDirectoryAdministrationPage extends TemplateWebPage
{
  private static final long serialVersionUID = 1000000;

  /* Logger */
  private static final Logger logger =
    LoggerFactory.getLogger(UserDirectoryAdministrationPage.class);

  /* Security Service */
  @Inject
  private ISecurityService securityService;

  /**
   * Constructs a new <code>UserDirectoryAdministrationPage</code>.
   */
  public UserDirectoryAdministrationPage()
  {
    super("User Directories");

    try
    {
      /*
       * The table container, which allows the table and its associated navigator to be updated
       * using AJAX.
       */
      WebMarkupContainer tableContainer = new WebMarkupContainer("tableContainer");
      tableContainer.setOutputMarkupId(true);
      add(tableContainer);

      // The dialog used to confirm the removal of a user directory
      RemoveDialog removeDialog = new RemoveDialog(tableContainer);
      add(removeDialog);

      // The dialog used to confirm the removal of a user directory
      AddDialog selectUserDirectoryTypeDialog = new AddDialog();
      add(selectUserDirectoryTypeDialog);

      // The "addLink" used to add a new user directory
      AjaxLink<Void> addLink = new AjaxLink<Void>("addLink")
      {
        private static final long serialVersionUID = 1000000;

        @Override
        public void onClick(AjaxRequestTarget target)
        {
          selectUserDirectoryTypeDialog.show(target);
        }
      };
      tableContainer.add(addLink);

      UserDirectoryDataProvider dataProvider = new UserDirectoryDataProvider();

      // The user directory data view
      DataView<UserDirectory> dataView = new DataView<UserDirectory>("userDirectory", dataProvider)
      {
        private static final long serialVersionUID = 1000000;

        @Override
        protected void populateItem(Item<UserDirectory> item)
        {
          item.add(new Label("name", new PropertyModel<String>(item.getModel(), "name")));
          item.add(new Label("userDirectoryClass",
              new PropertyModel<String>(item.getModel(), "userDirectoryClass")));

          // The "updateLink" link
          Link<Void> updateLink = new Link<Void>("updateLink")
          {
            private static final long serialVersionUID = 1000000;

            @Override
            public void onClick()
            {
//            UpdateUserDirectoryPage page = new UpdateUserDirectoryPage(getPageReference(),
//              item.getModel());
//
//            setResponsePage(page);
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
              UserDirectory userDirectory = item.getModelObject();

              if (userDirectory != null)
              {
                removeDialog.show(target, userDirectory);
              }
              else
              {
                target.add(tableContainer);
              }
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
      throw new WebApplicationException("Failed to initialise the UserDirectoryAdministrationPage",
          e);
    }
  }

  /**
   * The <code>AddDialog</code> class implements a dialog that allows the user directory type to be
   * selected when adding a user directory.
   */
  private class AddDialog extends Dialog
  {
    private static final long serialVersionUID = 1000000;
    @SuppressWarnings("unused")
    private String userDirectoryTypeName;

    /**
     * Constructs a new <code>AddDialog</code>.
     */
    public AddDialog()
    {
      super("addDialog");

      Form<User> addForm = new Form<>("addForm");

      // The "userDirectoryType" field
      DropDownChoice<String> userDirectoryTypeField =
        new DropDownChoiceWithFeedback<>("userDirectoryType",
          new PropertyModel<>(this, "userDirectoryTypeName"), getUserDirectoryTypeOptions());
      userDirectoryTypeField.setRequired(true);
      userDirectoryTypeField.setOutputMarkupId(true);
      addForm.add(userDirectoryTypeField);

      add(addForm);

      add(new AjaxSubmitLink("addLink", addForm)
      {
        @Override
        protected void onSubmit(AjaxRequestTarget target, Form<?> form)
        {
          logger.info("[onSubmit] userDirectoryTypeField.getModelObject() = "
              + userDirectoryTypeField.getModelObject());

          userDirectoryTypeField.setDefaultModelObject(null);

          setResponsePage(new AddUserDirectoryPage(getPageReference()));
        }

        @Override
        protected void onError(AjaxRequestTarget target, Form<?> form)
        {
          super.onError(target, form);

          if (userDirectoryTypeField.hasErrorMessage())
          {
            target.add(userDirectoryTypeField);
          }
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
     * @param target the AJAX request target
     */
    public void show(AjaxRequestTarget target)
    {
      super.show(target);
    }

    private List<String> getUserDirectoryTypeOptions()
    {
      List<String> userDirectoryTypeOptions = new ArrayList<>();

      for (UserDirectoryType userDirectoryType : securityService.getUserDirectoryTypes())
      {
        userDirectoryTypeOptions.add(userDirectoryType.getName());
      }

      return userDirectoryTypeOptions;
    }
  }

  /**
   * The <code>RemoveDialog</code> class implements a dialog that allows the removal of a
   * user directory to be confirmed.
   */
  private class RemoveDialog extends Dialog
  {
    private static final long serialVersionUID = 1000000;
    private long id;
    private Label nameLabel;

    /**
     * Constructs a new <code>RemoveDialog</code>.
     *
     * @param tableContainer the table container, which allows the user directory table and its
     *                       associated navigator to be updated using AJAX
     */
    public RemoveDialog(WebMarkupContainer tableContainer)
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
            // TODO: Confirm that there are no organisations associated with this user directory

            // securityService.deleteUserDirectory(id);

            target.add(tableContainer);

            UserDirectoryAdministrationPage.this.info("Successfully removed the user directory "
                + nameLabel.getDefaultModelObjectAsString());
          }
          catch (Throwable e)
          {
            logger.error("Failed to remove the user directory (" + id + "): " + e.getMessage(), e);

            UserDirectoryAdministrationPage.this.error("Failed to remove the user directory "
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
     * @param target        the AJAX request target
     * @param userDirectory the user directory being removed
     */
    public void show(AjaxRequestTarget target, UserDirectory userDirectory)
    {
      id = userDirectory.getId();
      nameLabel.setDefaultModelObject(userDirectory.getName());

      target.add(nameLabel);

      super.show(target);
    }
  }
}

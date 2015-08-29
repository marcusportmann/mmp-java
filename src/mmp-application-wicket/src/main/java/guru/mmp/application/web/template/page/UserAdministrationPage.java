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
import guru.mmp.application.web.WebApplicationException;
import guru.mmp.application.web.WebSession;
import guru.mmp.application.web.component.Dialog;
import guru.mmp.application.web.page.WebPageSecurity;
import guru.mmp.application.web.template.TemplateSecurity;
import guru.mmp.application.web.template.component.PagingNavigator;
import guru.mmp.application.web.template.data.UserDataProvider;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
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
 * The <code>UserAdministrationPage</code> class implements the
 * "User Administration" page for the Web Application Template.
 *
 * @author Marcus Portmann
 */
@WebPageSecurity(TemplateSecurity.FUNCTION_CODE_USER_ADMINISTRATION)
public class UserAdministrationPage extends TemplateWebPage
{
  private static final long serialVersionUID = 1000000;

  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(UserAdministrationPage.class);

  /* Security Service */
  @Inject
  private ISecurityService securityService;

  /**
   * Constructs a new <code>UserAdministrationPage</code>.
   */
  public UserAdministrationPage()
  {
    super("User Administration");

    try
    {
      WebSession session = getWebApplicationSession();

      /*
       * The table container, which allows the table and its associated navigator to be updated
       * using AJAX.
       */
      WebMarkupContainer tableContainer = new WebMarkupContainer("tableContainer");
      tableContainer.setOutputMarkupId(true);
      add(tableContainer);

      // The dialog used to confirm the removal of a user
      RemoveDialog removeDialog = new RemoveDialog(tableContainer);
      add(removeDialog);

      // The "addLink" used to add a new user
      Link<Void> addLink = new Link<Void>("addLink")
      {
        private static final long serialVersionUID = 1000000;

        @Override
        public void onClick()
        {
          AddUserPage page = new AddUserPage(getPageReference());

          setResponsePage(page);
        }
      };
      tableContainer.add(addLink);

      UserDataProvider dataProvider = new UserDataProvider(session.getOrganisation());

      // The "filterForm" form
      Form<Void> filterForm = new Form<>("filterForm");
      filterForm.setMarkupId("filterForm");
      filterForm.setOutputMarkupId(true);

      // The "filter" field
      final TextField<String> filterField = new TextField<>("filter",
        new PropertyModel<>(dataProvider, "filter"));
      filterForm.add(filterField);

      // The "filterButton" button
      Button filterButton = new Button("filterButton")
      {
        private static final long serialVersionUID = 1000000;

        @Override
        public void onSubmit() {}
      };
      filterButton.setDefaultFormProcessing(true);
      filterForm.add(filterButton);
      tableContainer.add(filterForm);

      // The user data view
      final DataView<User> dataView = new DataView<User>("user", dataProvider)
      {
        private static final long serialVersionUID = 1000000;

        @Override
        protected void populateItem(Item<User> item)
        {
          item.add(new Label("username", new PropertyModel<String>(item.getModel(), "username")));
          item.add(new Label("firstNames",
              new PropertyModel<String>(item.getModel(), "firstNames")));
          item.add(new Label("lastName", new PropertyModel<String>(item.getModel(), "lastName")));

          // The "userGroupsLink" link
          Link<Void> userGroupsLink = new Link<Void>("userGroupsLink")
          {
            private static final long serialVersionUID = 1000000;

            @Override
            public void onClick()
            {
              User user = item.getModelObject();

              if (!user.getUsername().equalsIgnoreCase("Administrator"))
              {
                UserGroupsPage page = new UserGroupsPage(getPageReference(), user.getUsername());

                setResponsePage(page);
              }
            }
          };
          item.add(userGroupsLink);

          // The "updateLink" link
          Link<Void> updateLink = new Link<Void>("updateLink")
          {
            private static final long serialVersionUID = 1000000;

            @Override
            public void onClick()
            {
              User user = item.getModelObject();

              if (!user.getUsername().equalsIgnoreCase("Administrator"))
              {
                UpdateUserPage page = new UpdateUserPage(getPageReference(), item.getModel());

                setResponsePage(page);
              }
            }
          };
          item.add(updateLink);

          // The "resetPassword" link
          Link<Void> resetPasswordLink = new Link<Void>("resetPasswordLink")
          {
            private static final long serialVersionUID = 1000000;

            @Override
            public void onClick()
            {
              ResetUserPasswordPage page = new ResetUserPasswordPage(getPageReference(),
                new Model<>(item.getModelObject()));

              setResponsePage(page);
            }
          };
          item.add(resetPasswordLink);

          // The "removeLink" link
          AjaxLink<Void> removeLink = new AjaxLink<Void>("removeLink")
          {
            private static final long serialVersionUID = 1000000;

            @Override
            public void onClick(AjaxRequestTarget target)
            {
              if (!item.getModelObject().getUsername().equalsIgnoreCase("Administrator"))
              {
                removeDialog.show(target, item.getModel());
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
      throw new WebApplicationException("Failed to initialise the UserAdministrationPage", e);
    }
  }

  /**
   * The <code>RemoveDialog</code> class implements a dialog that allows the removal
   * of a user to be confirmed.
   */
  private class RemoveDialog extends Dialog
  {
    private static final long serialVersionUID = 1000000;
    private Label nameLabel;
    private String username;

    /**
     * Constructs a new <code>RemoveDialog</code>.
     *
     * @param tableContainer the table container, which allows the user table and its
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
            securityService.deleteUser(username, getRemoteAddress());

            target.add(tableContainer);

            UserAdministrationPage.this.info("Successfully removed the user "
                + nameLabel.getDefaultModelObjectAsString());
          }
          catch (Throwable e)
          {
            logger.error("Failed to remove the user (" + username + "): " + e.getMessage(), e);

            UserAdministrationPage.this.error("Failed to remove the user "
                + nameLabel.getDefaultModelObjectAsString());
          }

          target.add(getAlerts());

          hide(target);
        }
      });
    }

    /**
     * @param target the AJAX request target
     * @see Dialog#hide(org.apache.wicket.ajax.AjaxRequestTarget)
     */
    public void hide(AjaxRequestTarget target)
    {
      super.hide(target);
    }

    /**
     * Show the dialog using Ajax.
     *
     * @param target    the AJAX request target
     * @param userModel the model for the user being removed
     */
    public void show(AjaxRequestTarget target, IModel<User> userModel)
    {
      User user = userModel.getObject();

      this.username = user.getUsername();
      this.nameLabel.setDefaultModelObject(user.getFirstNames() + " " + user.getLastName());

      target.add(nameLabel);

      super.show(target);
    }
  }
}

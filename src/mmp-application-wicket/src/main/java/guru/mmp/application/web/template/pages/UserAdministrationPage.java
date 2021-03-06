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

import guru.mmp.application.security.*;
import guru.mmp.application.security.SecurityException;
import guru.mmp.application.web.WebApplicationException;
import guru.mmp.application.web.WebSession;
import guru.mmp.application.web.pages.WebPageSecurity;
import guru.mmp.application.web.template.TemplateSecurity;
import guru.mmp.application.web.template.components.Dialog;
import guru.mmp.application.web.template.components.DropdownMenu;
import guru.mmp.application.web.template.components.PagingNavigator;
import guru.mmp.application.web.template.data.FilteredUserDataProvider;
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
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

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
  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(UserAdministrationPage.class);
  private static final long serialVersionUID = 1000000;

  /* Security Service */
  @Inject
  private ISecurityService securityService;

  /**
   * The user directory the users are associated with.
   */
  private UserDirectory userDirectory;

  /**
   * Constructs a new <code>UserAdministrationPage</code>.
   */
  public UserAdministrationPage()
  {
    super("Users");

    try
    {
      /*
       * Retrieve the list of user directories for the organisation the currently logged on user
       * is associated with and default to the first user directory.
       */
      List<UserDirectory> userDirectories = getUserDirectories();

      userDirectory = userDirectories.get(0);

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
          AddUserPage page = new AddUserPage(getPageReference(), userDirectory.getId());

          setResponsePage(page);
        }
      };
      tableContainer.add(addLink);

      FilteredUserDataProvider dataProvider = new FilteredUserDataProvider(userDirectory.getId());

      // The "userDirectoryDropdownMenu" dropdown button
      DropdownMenu<UserDirectory> userDirectoryDropdownMenu = new DropdownMenu<UserDirectory>(
          "userDirectoryDropdownMenu", new PropertyModel<>(this, "userDirectory"), userDirectories,
          "fa fa-users")
      {
        @Override
        protected String getDisplayValue(UserDirectory menuItem)
        {
          return menuItem.getName();
        }

        @Override
        protected void onMenuItemSelected(AjaxRequestTarget target, UserDirectory menuItem)
        {
          dataProvider.setUserDirectoryId(userDirectory.getId());

          if (target != null)
          {
            target.add(tableContainer);

            target.appendJavaScript(
                "jQuery('[data-toggle=\"tooltip\"]').tooltip({container: 'body', animation: false});");
          }
        }
      };
      userDirectoryDropdownMenu.setVisible(userDirectories.size() > 1);
      tableContainer.add(userDirectoryDropdownMenu);

      // The "filterForm" form
      Form<Void> filterForm = new Form<>("filterForm");
      filterForm.setMarkupId("filterForm");
      filterForm.setOutputMarkupId(true);

      // The "filter" field
      TextField<String> filterField = new TextField<>("filter", new PropertyModel<>(dataProvider,
          "filter"));
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

      // The "resetButton" button
      Button resetButton = new Button("resetButton")
      {
        private static final long serialVersionUID = 1000000;

        @Override
        public void onSubmit()
        {
          dataProvider.setFilter("");
        }
      };
      filterForm.add(resetButton);

      tableContainer.add(filterForm);

      // The user data view
      DataView<User> dataView = new DataView<User>("user", dataProvider)
      {
        private static final long serialVersionUID = 1000000;

        @Override
        protected void populateItem(Item<User> item)
        {
          User user = item.getModelObject();

          item.add(new Label("username", new PropertyModel<String>(item.getModel(), "username")));
          item.add(new Label("firstName", new PropertyModel<String>(item.getModel(), "firstName")));
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
                UserGroupsPage page = new UserGroupsPage(getPageReference(), userDirectory.getId(),
                    user.getUsername());

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
          updateLink.setVisible(!user.isReadOnly());
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
          resetPasswordLink.setVisible(!user.isReadOnly());
          item.add(resetPasswordLink);

          // The "removeLink" link
          AjaxLink<Void> removeLink = new AjaxLink<Void>("removeLink")
          {
            private static final long serialVersionUID = 1000000;

            @Override
            public void onClick(AjaxRequestTarget target)
            {
              User user = item.getModelObject();

              if (user != null)
              {
                if (!user.getUsername().equalsIgnoreCase("Administrator"))
                {
                  removeDialog.show(target, user);
                }
              }
              else
              {
                target.add(tableContainer);
              }
            }
          };
          removeLink.setVisible(!user.isReadOnly());
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

  private List<UserDirectory> getUserDirectories()
    throws OrganisationNotFoundException, UserDirectoryNotFoundException, SecurityException
  {
    WebSession session = getWebApplicationSession();

    List<UserDirectory> allUserDirectories = securityService.getUserDirectoriesForOrganisation(
        session.getOrganisation().getId());

    List<UserDirectory> userDirectories = new ArrayList<>();

    for (UserDirectory userDirectory : allUserDirectories)
    {
      if ((userDirectory.getId().equals(SecurityService.DEFAULT_USER_DIRECTORY_ID))
          && (!session.getUserDirectoryId().equals(SecurityService.DEFAULT_USER_DIRECTORY_ID)))
      {
        // Do nothing
      }
      else if (securityService.supportsUserAdministration(userDirectory.getId()))
      {
        userDirectories.add(userDirectory);
      }
    }

    return userDirectories;
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
    RemoveDialog(WebMarkupContainer tableContainer)
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
                securityService.deleteUser(userDirectory.getId(), username);

                target.add(tableContainer);

                UserAdministrationPage.this.info("Successfully removed the user "
                    + nameLabel.getDefaultModelObjectAsString());
              }
              catch (Throwable e)
              {
                logger.error(String.format("Failed to remove the user (%s): %s", username,
                    e.getMessage()), e);

                UserAdministrationPage.this.error("Failed to remove the user "
                    + nameLabel.getDefaultModelObjectAsString());
              }

              target.add(getAlerts());

              hide(target);
            }
          });
    }

    /**
     * Show the dialog using Ajax.
     *
     * @param target the AJAX request target
     * @param user   the user being removed
     */
    public void show(AjaxRequestTarget target, User user)
    {
      this.username = user.getUsername();

      this.nameLabel.setDefaultModelObject(user.getFirstName() + " " + user.getLastName());

      target.add(nameLabel);

      super.show(target);
    }
  }
}

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
import guru.mmp.application.web.template.data.GroupDataProvider;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
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
 * The <code>GroupAdministrationPage</code> class implements the
 * "Group Administration" page for the Web Application Template.
 *
 * @author Marcus Portmann
 */
@WebPageSecurity(TemplateSecurity.FUNCTION_CODE_GROUP_ADMINISTRATION)
public class GroupAdministrationPage extends TemplateWebPage
{
  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(GroupAdministrationPage.class);
  private static final long serialVersionUID = 1000000;

  /* Security Service */
  @Inject
  private ISecurityService securityService;

  /**
   * The user directory the groups are associated with.
   */
  private UserDirectory userDirectory;

  /**
   * Constructs a new <code>GroupAdministrationPage</code>.
   */
  public GroupAdministrationPage()
  {
    super("Groups");

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

      // The dialog used to confirm the removal of a group
      RemoveDialog removeDialog = new RemoveDialog(tableContainer);
      add(removeDialog);

      // The "addLink" link
      Link<Void> addLink = new Link<Void>("addLink")
      {
        private static final long serialVersionUID = 1000000;

        @Override
        public void onClick()
        {
          AddGroupPage page = new AddGroupPage(getPageReference(), userDirectory.getId());

          setResponsePage(page);
        }
      };
      tableContainer.add(addLink);

      GroupDataProvider dataProvider = new GroupDataProvider(userDirectory.getId());

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
          }
        }
      };
      userDirectoryDropdownMenu.setVisible(userDirectories.size() > 1);
      tableContainer.add(userDirectoryDropdownMenu);

      // The group data view
      DataView<Group> dataView = new DataView<Group>("group", dataProvider)
      {
        private static final long serialVersionUID = 1000000;

        @Override
        protected void populateItem(Item<Group> item)
        {
          item.add(new Label("groupName", new PropertyModel<String>(item.getModel(), "groupName")));
          item.add(new Label("description", new PropertyModel<String>(item.getModel(),
              "description")));

          // The "updateLink" link
          Link<Void> updateLink = new Link<Void>("updateLink")
          {
            private static final long serialVersionUID = 1000000;

            @Override
            public void onClick()
            {
              UpdateGroupPage page = new UpdateGroupPage(getPageReference(), item.getModel());

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
              Group group = item.getModelObject();

              if (group != null)
              {
                removeDialog.show(target, group);
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
      throw new WebApplicationException("Failed to initialise the GroupAdministrationPage", e);
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
      if ((userDirectory.getId().equals(SecurityService.DEFAULT_USER_DIRECTORY_ID)
          && (!session.getUserDirectoryId().equals(SecurityService.DEFAULT_USER_DIRECTORY_ID))))
      {
        // Do nothing
      }
      else if (securityService.supportsGroupAdministration(userDirectory.getId()))
      {
        userDirectories.add(userDirectory);
      }
    }

    return userDirectories;
  }

  /**
   * The <code>RemoveDialog</code> class implements a dialog that allows the removal
   * of a group to be confirmed.
   */
  private class RemoveDialog extends Dialog
  {
    private static final long serialVersionUID = 1000000;
    private String groupName;
    private Label nameLabel;

    /**
     * Constructs a new <code>RemoveDialog</code>.
     *
     * @param tableContainer the table container, which allows the group table and its
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
            securityService.deleteGroup(userDirectory.getId(), groupName);

            target.add(tableContainer);

            GroupAdministrationPage.this.info("Successfully removed the group "
                + nameLabel.getDefaultModelObjectAsString());
          }
          catch (ExistingGroupMembersException e)
          {
            GroupAdministrationPage.this.error("Failed to remove the group "
                + nameLabel.getDefaultModelObjectAsString() + " " + "with existing users");
          }
          catch (Throwable e)
          {
            logger.error(String.format("Failed to remove the group (%s): %s", groupName,
                e.getMessage()), e);

            GroupAdministrationPage.this.error("Failed to remove the group "
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
     * @param target the AJAX request target
     * @param group  the group being removed
     */
    public void show(AjaxRequestTarget target, Group group)
    {
      groupName = group.getGroupName();

      nameLabel.setDefaultModelObject(group.getGroupName());

      target.add(nameLabel);

      super.show(target);
    }
  }
}

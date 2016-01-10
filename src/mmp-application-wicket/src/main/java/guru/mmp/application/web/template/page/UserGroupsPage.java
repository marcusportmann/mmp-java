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

package guru.mmp.application.web.template.page;

import guru.mmp.application.security.*;
import guru.mmp.application.security.SecurityException;
import guru.mmp.application.web.WebApplicationException;
import guru.mmp.application.web.WebSession;
import guru.mmp.application.web.page.WebPageSecurity;
import guru.mmp.application.web.template.TemplateSecurity;
import guru.mmp.application.web.template.component.PagingNavigator;
import guru.mmp.application.web.template.data.GroupsForUserDataProvider;
import guru.mmp.common.util.StringUtil;
import org.apache.wicket.PageReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
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

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * The <code>UserGroupsPage</code> class implements the
 * "User Groups" page for the Web Application Template.
 *
 * @author Marcus Portmann
 */
@WebPageSecurity(TemplateSecurity.FUNCTION_CODE_USER_GROUPS)
public class UserGroupsPage
  extends TemplateWebPage
{
  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(UserGroupsPage.class);

  private static final long serialVersionUID = 1000000;

  @SuppressWarnings("unused")
  private String groupName;

  /* Security Service */
  @Inject
  private ISecurityService securityService;

  /**
   * Constructs a new <code>UserGroupsPage</code>.
   *
   * @param previousPage    the previous page
   * @param userDirectoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        user directory
   * @param username        the username identifying the user
   */
  public UserGroupsPage(PageReference previousPage, UUID userDirectoryId, String username)
  {
    super("User Groups", username);

    try
    {
      /*
       * The table container, which allows the table and its associated navigator to be updated
       * using AJAX.
       */
      WebMarkupContainer tableContainer = new WebMarkupContainer("tableContainer");
      tableContainer.setOutputMarkupId(true);
      add(tableContainer);

      // The "backLink" link
      Link<Void> backLink = new Link<Void>("backLink")
      {
        private static final long serialVersionUID = 1000000;

        @Override
        public void onClick()
        {
          setResponsePage(previousPage.getPage());
        }
      };
      tableContainer.add(backLink);

      // The "addUserToGroupForm" form
      DropDownChoice<String> groupNameField = new DropDownChoice<>("groupName",
        new PropertyModel<>(this, "groupName"), getGroupOptions(userDirectoryId, username));

      groupNameField.setRequired(true);

      Form<Void> addUserToGroupForm = new Form<Void>("addUserToGroupForm")
      {
        private static final long serialVersionUID = 1000000;

        @Override
        protected void onSubmit()
        {
          WebSession session = getWebApplicationSession();

          try
          {
            securityService.addUserToGroup(userDirectoryId, username, groupName);

            logger.info(String.format(
              "User (%s) added the user (%s) to the group (%s) for the user directory (%s)",
              session.getUsername(), username, groupName, userDirectoryId));

            groupNameField.setChoices(getGroupOptions(userDirectoryId, username));
            groupNameField.setModelObject(null);
          }
          catch (Throwable e)
          {
            logger.error(String.format(
              "Failed to add the user (%s) to the group (%s) for the user directory (%s): %s",
              username, groupName, userDirectoryId, e.getMessage()), e);

            UserGroupsPage.this.error(
              String.format("Failed to add the user %s to the group %s", username, groupName));
          }
        }
      };

      addUserToGroupForm.setMarkupId("addUserToGroupForm");
      addUserToGroupForm.setOutputMarkupId(true);
      tableContainer.add(addUserToGroupForm);

      addUserToGroupForm.add(groupNameField);

      // The group data view
      GroupsForUserDataProvider dataProvider = new GroupsForUserDataProvider(userDirectoryId,
        username);

      DataView<Group> dataView = new DataView<Group>("group", dataProvider)
      {
        private static final long serialVersionUID = 1000000;

        @Override
        protected void populateItem(Item<Group> item)
        {
          Group group = item.getModelObject();

          String name = StringUtil.truncate(group.getGroupName(), 25);
          String description = StringUtil.truncate(group.getDescription(), 30);

          item.add(new Label("name", Model.of(name)));

          // The "removeLink" link
          AjaxLink<Void> removeLink = new AjaxLink<Void>("removeLink")
          {
            private static final long serialVersionUID = 1000000;

            @Override
            public void onClick(AjaxRequestTarget target)
            {
              WebSession session = getWebApplicationSession();

              Group group = item.getModelObject();

              try
              {
                securityService.removeUserFromGroup(userDirectoryId, username,
                  group.getGroupName());

                logger.info(String.format(
                  "User (%s) removed the user (%s) from the group (%s) for the user directory (%s)",
                  session.getUsername(), username, group.getGroupName(), userDirectoryId));

                groupNameField.setChoices(getGroupOptions(userDirectoryId, username));
                groupNameField.setModelObject(null);

                if (target != null)
                {
                  target.add(tableContainer);

                  target.appendJavaScript(
                    "jQuery('[data-toggle=\"tooltip\"]').tooltip({container: 'body', animation: " +
                      "false});");
                }
              }
              catch (Throwable e)
              {
                logger.error(String.format(
                  "Failed to remove the user (%s) from the group (%s) for the user directory (%s)" +
                    ": %s", username, group.getGroupName(), userDirectoryId, e.getMessage()), e);

                UserGroupsPage.this.error(
                  String.format("Failed to remove the user %s from the group %s", username,
                    group.getGroupName()));
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
      throw new WebApplicationException("Failed to initialise the UserGroupsPage", e);
    }
  }

  /**
   * Hidden <code>UserGroupsPage</code> constructor.
   */
  @SuppressWarnings("unused")
  protected UserGroupsPage() {}

  private List<String> getGroupOptions(UUID userDirectoryId, String username)
    throws UserDirectoryNotFoundException, UserNotFoundException, SecurityException
  {
    WebSession session = getWebApplicationSession();

    // Retrieve a list of name of the existing groups for the user
    List<String> existingGroupNames = securityService.getGroupNamesForUser(userDirectoryId,
      username);

    // Retrieve a complete list of groups for the organisation
    List<Group> groups = securityService.getGroups(userDirectoryId);

    // Filter the list of available groups for the user
    List<String> groupOptions = new ArrayList<>();

    for (Group group : groups)
    {
      boolean isAvailableUserGroup = true;

      for (String existingGroupName : existingGroupNames)
      {
        if (existingGroupName.equalsIgnoreCase(group.getGroupName()))
        {
          isAvailableUserGroup = false;

          break;
        }
      }

      if (group.getGroupName().equalsIgnoreCase(TemplateSecurity.ADMINISTRATORS_GROUP_NAME))
      {
        if (!session.hasAcccessToFunction(TemplateSecurity.FUNCTION_CODE_ADD_ORGANISATION))
        {
          isAvailableUserGroup = false;
        }
      }

      if (isAvailableUserGroup)
      {
        groupOptions.add(group.getGroupName());
      }
    }

    return groupOptions;
  }
}

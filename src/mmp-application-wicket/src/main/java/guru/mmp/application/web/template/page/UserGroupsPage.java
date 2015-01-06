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

import guru.mmp.application.security.Group;
import guru.mmp.application.security.ISecurityService;
import guru.mmp.application.security.SecurityException;
import guru.mmp.application.web.WebSession;
import guru.mmp.application.web.page.WebPageSecurity;
import guru.mmp.application.web.template.TemplateSecurity;
import guru.mmp.application.web.template.TemplateWebApplication;
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
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.ReuseIfModelsEqualStrategy;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>UserGroupsPage</code> class implements the
 * "User Groups" page for the Web Application Template.
 *
 * @author Marcus Portmann
 */
@WebPageSecurity(TemplateSecurity.FUNCTION_CODE_USER_GROUPS)
public class UserGroupsPage extends TemplateWebPage
{
  private static final long serialVersionUID = 1000000;

  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(UserGroupsPage.class);
  @SuppressWarnings("unused")
  private String groupName;

  /* Security Service */
  @Inject
  private ISecurityService securityService;

  /**
   * Constructs a new <code>UserGroupsPage</code>.
   *
   * @param previousPage the previous page
   * @param username     the username identifying the user
   */
  public UserGroupsPage(final PageReference previousPage, final String username)
  {
    super("User Groups", "User Groups", username, previousPage);
    setTitle(((TemplateWebApplication) getApplication()).getDisplayName() + " | User Groups");

    /*
     * The table container, which allows the table and its associated navigator to be updated
     * using AJAX.
     */
    final WebMarkupContainer tableContainer = new WebMarkupContainer("tableContainer");
    tableContainer.setOutputMarkupId(true);
    add(tableContainer);

    // The "addUserToGroupForm" form
    final DropDownChoice<String> groupNameField = new DropDownChoice<>("groupName",
      new PropertyModel<>(this, "groupName"), getGroupOptions(username));

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
          securityService.addUserToGroup(username, groupName, session.getOrganisation(),
              getRemoteAddress());

          logger.info("User (" + session.getUsername() + ") added the user (" + username
              + ") to the group (" + groupName + ") for the organisation ("
              + session.getOrganisation() + ")");

          groupNameField.setChoices(getGroupOptions(username));
          groupNameField.setModelObject(null);
        }
        catch (Throwable e)
        {
          logger.error("Failed to add the user (" + username + ") to the group (" + groupName
              + ") for the organisation (" + session.getOrganisation() + "): " + e.getMessage(), e);
          UserGroupsPage.this.error("Failed to add the user " + username + " to the group ("
              + groupName + ") for the organisation (" + session.getOrganisation());
        }
      }
    };

    addUserToGroupForm.setMarkupId("addUserToGroupForm");
    addUserToGroupForm.setOutputMarkupId(true);
    add(addUserToGroupForm);

    addUserToGroupForm.add(groupNameField);

    // The group data view
    GroupsForUserDataProvider dataProvider = new GroupsForUserDataProvider(username,
      getWebApplicationSession().getOrganisation());

    DataView<Group> dataView = new DataView<Group>("group", dataProvider)
    {
      private static final long serialVersionUID = 1000000;

      @Override
      protected void populateItem(Item<Group> item)
      {
        final IModel<Group> groupModel = item.getModel();

        Group group = groupModel.getObject();

        String name = StringUtil.truncate(group.getGroupName(), 25);
        String description = StringUtil.truncate(group.getDescription(), 30);

        item.add(new Label("name", Model.of(name)));
        item.add(new Label("description", Model.of(description)));

        // The "removeLink" link
        AjaxLink<Void> removeLink = new AjaxLink<Void>("removeLink")
        {
          private static final long serialVersionUID = 1000000;

          @Override
          public void onClick(AjaxRequestTarget target)
          {
            WebSession session = getWebApplicationSession();

            Group group = groupModel.getObject();

            try
            {
              securityService.removeUserFromGroup(username, group.getGroupName(),
                  session.getOrganisation(), getRemoteAddress());

              logger.info("User (" + session.getUsername() + ") removed the user (" + username
                  + ") from the group (" + group.getGroupName() + ") for the organisation ("
                  + session.getOrganisation() + ")");

              groupNameField.setChoices(getGroupOptions(username));
              groupNameField.setModelObject(null);

              target.add(tableContainer);
            }
            catch (Throwable e)
            {
              logger.error("Failed to remove the user (" + username + ") from the group ("
                  + group.getGroupName() + ") for the organisation (" + session.getOrganisation()
                  + "): " + e.getMessage(), e);

              UserGroupsPage.this.error("Failed to remove the user " + username
                  + " from the group " + group.getGroupName() + " for the organisation "
                  + session.getOrganisation());
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

  /**
   * Hidden <code>UserGroupsPage</code> constructor.
   */
  protected UserGroupsPage() {}

  private List<String> getGroupOptions(final String username)
    throws SecurityException
  {
    WebSession session = getWebApplicationSession();

    // Retrieve a list of name of the existing groups for the user
    List<String> existingGroupNames = securityService.getGroupNamesForUser(username,
      session.getOrganisation(), getRemoteAddress());

    // Retrieve a complete list of groups for the organisation
    List<Group> groups = securityService.getGroups(getRemoteAddress());

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

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

package guru.mmp.application.web.template;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.web.WebSession;
import guru.mmp.application.web.template.navigation.NavigationGroup;
import guru.mmp.application.web.template.navigation.NavigationItem;
import guru.mmp.application.web.template.navigation.NavigationLink;
import guru.mmp.application.web.template.navigation.NavigationState;
import org.apache.wicket.request.Request;

import java.io.Serializable;
import java.util.List;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>TemplateWebSession</code> class stores the session information for a user accessing a
 * Wicket web application that make use of the Web Application Template.
 *
 * @author Marcus Portmann
 */
public class TemplateWebSession extends WebSession
  implements Serializable
{
  private static final long serialVersionUID = 1000000;
  private NavigationGroup navigation;
  private NavigationState navigationState;

  /**
   * Constructs a new <code>TemplateWebSession</code>.
   *
   * @param request the Wicket request
   */
  TemplateWebSession(Request request)
  {
    super(request);
  }

  /**
   * Returns the user-specific navigation hierarchy for the application.
   * <p/>
   * This hierarchy is derived from the application's navigation hierarchy and filtered according
   * to the security permissions of the user associated with the session.
   * <p/>
   * NOTE: If the session is associated with an "anonymous" user then only the unsecured navigation
   * items will be included in the navigation hierarchy.
   *
   * @return the user-specific navigation hierarchy for the application
   */
  public NavigationGroup getNavigation()
  {
    // If the user-specific navigation hierarchy has not been constructed then do so now
    if (navigation == null)
    {
      TemplateWebApplication application = (TemplateWebApplication) getApplication();
      NavigationGroup applicationNavigation = application.getNavigation();

      navigation = new NavigationGroup(applicationNavigation.getName());
      filterNavigationGroup(navigation, applicationNavigation);
    }

    return navigation;
  }

  /**
   * Returns the navigation state for the user.
   *
   * @return the navigation state for the user
   */
  public NavigationState getNavigationState()
  {
    if (navigationState == null)
    {
      navigationState = new NavigationState();
    }

    return navigationState;
  }

  /**
   * Invalidate the user's session.
   */
  @Override
  public void invalidate()
  {
    super.invalidate();

    navigation = null;
    navigationState = null;
  }

  /**
   * Invalidate the user's session immediately.
   * <p/>
   * Calling this method will remove all Wicket components from this session, which means that you
   * will no longer be able to work with them.
   */
  @Override
  public void invalidateNow()
  {
    super.invalidateNow();

    navigation = null;
    navigationState = null;
  }

  /**
   * Logout the logged in user.
   */
  @Override
  public void logoutUser()
  {
    super.logoutUser();

    navigation = null;
    navigationState = null;
  }

  /**
   * Set the function codes identifying the functionality assigned to the logged in user.
   *
   * @param functionCodes the function codes identifying the functionality assigned to the logged
   *                      in user
   */
  @Override
  public void setFunctionCodes(List<String> functionCodes)
  {
    super.setFunctionCodes(functionCodes);

    // Reset the user navigation
    navigation = null;
    navigationState = null;
  }

  private void filterNavigationGroup(NavigationGroup filteredGroup, NavigationGroup originalGroup)
  {
    for (NavigationItem item : originalGroup.getItems())
    {
      if (item instanceof NavigationLink)
      {
        NavigationLink link = (NavigationLink) item;

        // Filter navigation based on the user's security privileges
        if (link.isSecure())
        {
          if (hasAccessToFunctions(link.getFunctionCodes()))
          {
            filteredGroup.addItem(link);
          }
        }
        else
        {
          if (link.isAnonymousOnly() && isUserLoggedIn()) {}
          else
          {
            filteredGroup.addItem(link);
          }
        }
      }
      else if (item instanceof NavigationGroup)
      {
        NavigationGroup originalSubGroup = (NavigationGroup) item;
        NavigationGroup filteredSubGroup = new NavigationGroup(originalSubGroup.getName(),
            originalSubGroup.getIconClass());

        filterNavigationGroup(filteredSubGroup, originalSubGroup);

        if (filteredSubGroup.getItems().size() > 0)
        {
          filteredGroup.addItem(filteredSubGroup);
        }
      }
    }
  }
}

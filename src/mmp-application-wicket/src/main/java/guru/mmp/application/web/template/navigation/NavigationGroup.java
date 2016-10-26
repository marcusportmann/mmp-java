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

package guru.mmp.application.web.template.navigation;

//~--- non-JDK imports --------------------------------------------------------

import org.apache.wicket.Page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>NavigationGroup</code> class stores the information for a navigation group which
 * groups a number of navigation items under a web application's navigation hierarchy.
 *
 * @author Marcus Portmann
 */
public class NavigationGroup extends NavigationItem
  implements Serializable
{
  private static final long serialVersionUID = 1000000;
  private List<NavigationItem> items;

  /**
   * Constructs a new <code>NavigationGroup</code>.
   *
   * NOTE: This constructor is required to support Java serialization.
   */
  @SuppressWarnings("unused")
  NavigationGroup()
  {}

  /**
   * Constructs a new <code>NavigationGroup</code>.
   *
   * @param name the name of the navigation group
   */
  public NavigationGroup(String name)
  {
    this(name, null);
  }

  /**
   * Constructs a new <code>NavigationGroup</code>.
   *
   * @param name      the name of the navigation group
   * @param iconClass the CSS class for the icon for the navigation item
   */
  public NavigationGroup(String name, String iconClass)
  {
    super(name, iconClass);
    items = new ArrayList<>();
  }

  /**
   * Add the navigation item to the navigation group. The item added may be a sub-group.
   *
   * @param navigationItem the navigation item to add to the navigation group
   */
  public void addItem(NavigationItem navigationItem)
  {
    items.add(navigationItem);
  }

  /**
   * Returns the navigation items for the navigation group.
   *
   * @return the navigation items for the navigation group
   */
  public List<NavigationItem> getItems()
  {
    return items;
  }

  /**
   * Retrieve the navigation group with the specified name.
   * <p/>
   * The case will be ignored when matching the name.
   *
   * @param name the name of the group
   *
   * @return the navigation group with the specified name or <code>null</code> if the navigation
   * group cannot be found
   */
  public NavigationGroup getNavigationGroup(String name)
  {
    for (NavigationItem item : items)
    {
      if (item instanceof NavigationGroup)
      {
        NavigationGroup navigationGroup = (NavigationGroup) item;

        if (navigationGroup.getName().equalsIgnoreCase(name))
        {
          return navigationGroup;
        }
      }
    }

    return null;
  }

  /**
   * Returns <code>true</code> if the page is in the navigation item's hierarchy or
   * <code>false</code> otherwise.
   *
   * @param page the page
   *
   * @return <code>true</code> if the page is in the navigation item's hierarchy or
   *         <code>false</code> otherwise
   */
  public boolean isPageInNavigationHierarchy(Page page)
  {
    for (NavigationItem navigationItem : items)
    {
      if (navigationItem.isPageInNavigationHierarchy(page))
      {
        return true;
      }
    }

    return false;
  }

  /**
   * Sort the items in the navigation group.
   * <p/>
   * This will place all the navigation links first followed by the navigation sub-groups. Both the
   * navigation links and the navigation sub-groups will be sorted alphabetically.
   */
  public void sortItems()
  {
    Collections.sort(items,
        (navigationItem1, navigationItem2) ->
        {
          if ((navigationItem1 instanceof NavigationLink)
              && ((navigationItem2 instanceof NavigationLink)))
          {
            return navigationItem1.getName().compareTo(navigationItem2.getName());
          }
          else if ((navigationItem1 instanceof NavigationGroup)
              && ((navigationItem2 instanceof NavigationGroup)))
          {
            return navigationItem1.getName().compareTo(navigationItem2.getName());
          }
          else if ((navigationItem1 instanceof NavigationLink)
              && ((navigationItem2 instanceof NavigationGroup)))
          {
            return -1;
          }
          else if ((navigationItem1 instanceof NavigationGroup)
              && ((navigationItem2 instanceof NavigationLink)))
          {
            return 1;
          }
          else
          {
            return navigationItem1.getName().compareTo(navigationItem2.getName());
          }
        }
        );
  }
}

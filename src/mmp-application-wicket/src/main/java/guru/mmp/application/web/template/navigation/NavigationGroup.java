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

package guru.mmp.application.web.template.navigation;

//~--- non-JDK imports --------------------------------------------------------

import org.apache.wicket.Page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>NavigationGroup</code> class stores the information for a navigation group which
 * groups a number of navigation items under an application's navigation hierarchy.
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
}

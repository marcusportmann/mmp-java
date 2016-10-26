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

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>NavigationItem</code> class implements the base functionality common to
 * all navigation items that form part of a web application's navigation hierarchy.
 *
 * @author Marcus Portmann
 */
public abstract class NavigationItem
  implements Serializable
{
  private static final long serialVersionUID = 1000000;
  private String iconClass;
  private String name;

  /**
   * Constructs a new <code>NavigationItem</code>.
   *
   * NOTE: This constructor is required to support Java serialization.
   */
  @SuppressWarnings("unused")
  NavigationItem() {}

  /**
   * Constructs a new <code>NavigationItem</code>.
   *
   * @param name      the name of the navigation item
   * @param iconClass the CSS class for the icon for the navigation item
   */
  NavigationItem(String name, String iconClass)
  {
    this.name = name;
    this.iconClass = iconClass;
  }

  /**
   * The CSS class for the icon for the navigation item.
   *
   * @return the CSS class for the icon for the navigation item
   */
  public String getIconClass()
  {
    return iconClass;
  }

  /**
   * Returns the name of the navigation item.
   *
   * @return the name of the navigation item
   */
  public String getName()
  {
    return name;
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
  public abstract boolean isPageInNavigationHierarchy(Page page);
}

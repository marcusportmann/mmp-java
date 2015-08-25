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
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;

//~--- JDK imports ------------------------------------------------------------

import java.io.Serializable;

import java.util.List;

/**
 * The <code>NavigationState</code> class stores the current navigation state.
 *
 * @author Marcus Portmann
 */
public class NavigationState
  implements Serializable
{
  private static final long serialVersionUID = 1000000;

  /**
   * The cached user menu HTML.
   */
  private String cachedUserMenuHTML;

  /**
   * The cached breadcrumbs HTML for the last page that was accessed that was part of the
   * navigation hierarchy.
   */
  private String lastPageAccessedInNavigationHierarchyBreadcrumbsHTML;

  /**
   * The class for the last page that was accessed that was part of the navigation hierarchy.
   */
  private Class<? extends Page> lastPageAccessedInNavigationHierarchyClass;

  /**
   * The cached main navigation HTML for the last page that was accessed that was part of the
   * navigation hierarchy.
   */
  private String lastPageAccessedInNavigationHierarchyMainNavigationHTML;

  /**
   * The parameters for the last page that was accessed that was part of the navigation hierarchy.
   */
  private PageParameters lastPageAccessedInNavigationHierarchyParameters;

  /**
   * Returns the cached user menu HTML.
   *
   * @return the cached user menu HTML
   */
  public String getCachedUserMenuHTML()
  {
    return cachedUserMenuHTML;
  }

  /**
   * Returns the cached breadcrumbs HTML for the last page that was accessed that was part of the
   * navigation hierarchy.
   *
   * @return the cached breadcrumbs HTML for the last page that was accessed that was part of the
   *         navigation hierarchy
   */
  public String getLastPageAccessedInNavigationHierarchyBreadcrumbsHTML()
  {
    return lastPageAccessedInNavigationHierarchyBreadcrumbsHTML;
  }

  /**
   * Returns the cached main navigation HTML for the last page that was accessed that was part of
   * the navigation hierarchy.
   *
   * @return the cached main navigation HTML for the last page that was accessed that was part of
   *         the navigation hierarchy
   */
  public String getLastPageAccessedInNavigationHierarchyMainNavigationHTML()
  {
    return lastPageAccessedInNavigationHierarchyMainNavigationHTML;
  }

  /**
   * Invalidate the navigation state.
   */
  public void invalidate()
  {
    cachedUserMenuHTML = null;
    lastPageAccessedInNavigationHierarchyBreadcrumbsHTML = null;
    lastPageAccessedInNavigationHierarchyClass = null;
    lastPageAccessedInNavigationHierarchyMainNavigationHTML = null;
    lastPageAccessedInNavigationHierarchyParameters = null;
  }

  /**
   * Returns <code>true</code> if the specified page is the last page that was accessed in the
   * navigation hierarchy or <code>false</code> otherwise.
   *
   * @param page the page
   *
   * @return <code>true</code> if the specified page is the last page that was accessed in the
   *         navigation hierarchy or <code>false</code> otherwise
   */
  public boolean isLastPageAccessedInNavigationHierarchy(Page page)
  {
    // If we do not have a last page that was accessed in the navigation hierarchy
    if (lastPageAccessedInNavigationHierarchyClass == null)
    {
      return false;
    }

    /*
     * If the page classes don't match then this can't be the last page that was accessed in
     * the navigation hierarchy.
     */
    if (lastPageAccessedInNavigationHierarchyClass != page.getPageClass())
    {
      return false;
    }

    /*
     * If the page does not have parameters then this must be the last page that was accessed in
     * the navigation hierarchy.
     */
    if (page.getPageParameters().isEmpty())
    {
      return true;
    }

    /*
     * If we get here we check the page parameters since we might be dealing with the same
     * type of page but it might have different parameters.
     */
    PageParameters pageParameters = page.getPageParameters();

    for (String namedKey : pageParameters.getNamedKeys())
    {
      List<StringValue> pageParameterValues = pageParameters.getValues(namedKey);

      List<StringValue> lastPageAccessedInNavigationHierarchyParameterValues =
        lastPageAccessedInNavigationHierarchyParameters.getValues(namedKey);

      if (pageParameterValues.size() != lastPageAccessedInNavigationHierarchyParameterValues.size())
      {
        return false;
      }

      for (int i = 0; i < pageParameterValues.size(); i++)
      {
        if (!pageParameterValues.get(i).toString().equals(
            lastPageAccessedInNavigationHierarchyParameterValues.get(i).toString()))
        {
          return false;
        }
      }
    }

    return true;
  }

  /**
   * Set the cached user menu HTML.
   *
   * @param cachedUserMenuHTML the cached user menu HTML
   */
  public void setCachedUserMenuHTML(String cachedUserMenuHTML)
  {
    this.cachedUserMenuHTML = cachedUserMenuHTML;
  }

  /**
   * Set the last page that was accessed that was part of the navigation hierarchy.
   *
   * @param page the last page that was accessed that was part of the navigation hierarchy
   */
  public void setLastPageAccessedInNavigationHierarchy(Page page)
  {
    lastPageAccessedInNavigationHierarchyClass = page.getPageClass();

    lastPageAccessedInNavigationHierarchyParameters = page.getPageParameters();
  }

  /**
   * Set the cached breadcrumbs HTML for the last page that was accessed that was part of the
   * navigation hierarchy.
   *
   * @param breadcrumbsHTML the cached breadcrumbs HTML for the last page that was accessed that
   *                        was part of the navigation hierarchy
   */
  public void setLastPageAccessedInNavigationHierarchyBreadcrumbsHTML(String breadcrumbsHTML)
  {
    this.lastPageAccessedInNavigationHierarchyBreadcrumbsHTML = breadcrumbsHTML;
  }

  /**
   * Set the cached main navigation HTML for the last page that was accessed that was part of the
   * navigation hierarchy.
   *
   * @param mainNavigationHTML the cached main navigation HTML for the last page that was accessed
   *                           that was part of the navigation hierarchy
   */
  public void setLastPageAccessedInNavigationHierarchyMainNavigationHTML(String mainNavigationHTML)
  {
    this.lastPageAccessedInNavigationHierarchyMainNavigationHTML = mainNavigationHTML;
  }
}

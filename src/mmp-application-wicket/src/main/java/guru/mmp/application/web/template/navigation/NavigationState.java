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

import org.apache.wicket.Page;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;

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
   * The cached backend header HTML.
   */
  private String cachedBackendHeaderHTML;

  /**
   * The cached backend main navigation HTML for the last page that was accessed that was part of
   * the
   * navigation hierarchy.
   */
  private String cachedBackendMainNavigationHTML;

  /**
   * The cached backend user menu side overlay HTML.
   */
  private String cachedBackendUserMenuSideOverlayHTML;

  /**
   * The cached breadcrumbs HTML for the last page that was accessed that was part of the
   * navigation hierarchy.
   */
  private String cachedBreadcrumbsHTML;

  /**
   * The class for the last page that was accessed that was part of the navigation hierarchy.
   */
  private Class<? extends Page> lastPageAccessedInNavigationHierarchyClass;

  /**
   * The parameters for the last page that was accessed that was part of the navigation hierarchy.
   */
  private PageParameters lastPageAccessedInNavigationHierarchyParameters;

  /**
   * Returns the cached backend header HTML.
   *
   * @return the cached backend header HTML
   */
  public String getCachedBackendHeaderHTML()
  {
    return cachedBackendHeaderHTML;
  }

  /**
   * Returns the cached backend main navigation HTML for the last page that was accessed that was
   * part of the navigation hierarchy.
   *
   * @return the cached backend main navigation HTML for the last page that was accessed that was
   * part of
   * the navigation hierarchy
   */
  public String getCachedBackendMainNavigationHTML()
  {
    return cachedBackendMainNavigationHTML;
  }

  /**
   * Returns the cached backend user menu side overlay HTML.
   *
   * @return the cached backend user menu side overlay HTML
   */
  public String getCachedBackendUserMenuSideOverlayHTML()
  {
    return cachedBackendUserMenuSideOverlayHTML;
  }

  /**
   * Returns the cached breadcrumbs HTML for the last page that was accessed that was part of the
   * navigation hierarchy.
   *
   * @return the cached breadcrumbs HTML for the last page that was accessed that was part of the
   * navigation hierarchy
   */
  public String getCachedBreadcrumbsHTML()
  {
    return cachedBreadcrumbsHTML;
  }

  /**
   * Invalidate the navigation state.
   */
  public void invalidate()
  {
    cachedBackendUserMenuSideOverlayHTML = null;
    cachedBreadcrumbsHTML = null;
    lastPageAccessedInNavigationHierarchyClass = null;
    cachedBackendMainNavigationHTML = null;
    lastPageAccessedInNavigationHierarchyParameters = null;
    cachedBackendHeaderHTML = null;
  }

  /**
   * Returns <code>true</code> if the specified page is the last page that was accessed in the
   * navigation hierarchy or <code>false</code> otherwise.
   *
   * @param page the page
   *
   * @return <code>true</code> if the specified page is the last page that was accessed in the
   * navigation hierarchy or <code>false</code> otherwise
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
        lastPageAccessedInNavigationHierarchyParameters.getValues(
        namedKey);

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
   * Set the cached backend header HTML.
   *
   * @param cachedBackendHeaderHTML the cached backend header HTML
   */
  public void setCachedBackendHeaderHTML(String cachedBackendHeaderHTML)
  {
    this.cachedBackendHeaderHTML = cachedBackendHeaderHTML;
  }

  /**
   * Set the cached backend main navigation HTML for the last page that was accessed that was part
   * of the navigation hierarchy.
   *
   * @param mainNavigationHTML the cached backend main navigation HTML for the last page that was
   *                           accessed that was part of the navigation hierarchy
   */
  public void setCachedBackendMainNavigationHTML(String mainNavigationHTML)
  {
    this.cachedBackendMainNavigationHTML = mainNavigationHTML;
  }

  /**
   * Set the cached backend user menu side overlay HTML.
   *
   * @param cachedBackendUserMenuSideOverlayHTML the cached backend user menu side overlay HTML
   */
  public void setCachedBackendUserMenuSideOverlayHTML(String cachedBackendUserMenuSideOverlayHTML)
  {
    this.cachedBackendUserMenuSideOverlayHTML = cachedBackendUserMenuSideOverlayHTML;
  }

  /**
   * Set the cached breadcrumbs HTML for the last page that was accessed that was part of the
   * navigation hierarchy.
   *
   * @param breadcrumbsHTML the cached breadcrumbs HTML for the last page that was accessed that
   *                        was part of the navigation hierarchy
   */
  public void setCachedBreadcrumbsHTML(String breadcrumbsHTML)
  {
    this.cachedBreadcrumbsHTML = breadcrumbsHTML;
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
}

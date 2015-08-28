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

package guru.mmp.application.web.template.component;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.web.template.TemplateWebSession;
import guru.mmp.application.web.template.navigation.NavigationGroup;
import guru.mmp.application.web.template.navigation.NavigationItem;
import guru.mmp.application.web.template.navigation.NavigationLink;
import guru.mmp.application.web.template.navigation.NavigationState;

import org.apache.wicket.Component;
import org.apache.wicket.Session;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupElement;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.protocol.http.RequestUtils;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.cycle.RequestCycle;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 * The <code>MainNavigationMenu</code> class provides a Wicket component that renders the main
 * navigation menu for the Web Application Template.
 *
 * @author Marcus Portmann
 */
public class MainNavigationMenu extends Component
{
  private static final long serialVersionUID = 1000000;

  /**
   * @see org.apache.wicket.Component#Component(String)
   *
   * @param id the non-null id of this component
   */
  public MainNavigationMenu(final String id)
  {
    super(id);
  }

  /**
   * Render the main navigation menu.
   */
  @Override
  protected void onRender()
  {
    MarkupStream markupStream = findMarkupStream();
    MarkupElement element = markupStream.get();
    Response response = getResponse();

    if (element instanceof ComponentTag)
    {
      Session session = getSession();

      if (session instanceof TemplateWebSession)
      {
        TemplateWebSession webSession = ((TemplateWebSession) session);

        NavigationState navigationState = webSession.getNavigationState();

        // If we have cached navigation HTML then check if we can use it
        if (navigationState.getLastPageAccessedInNavigationHierarchyMainNavigationHTML() != null)
        {
          /*
           * If we are rendering the main navigation menu for the last page accessed in the
           * navigation hierarchy then use the cached navigation HTML.
           */
          if (navigationState.isLastPageAccessedInNavigationHierarchy(getPage()))
          {
            response.write(
                navigationState.getLastPageAccessedInNavigationHierarchyMainNavigationHTML());

            return;
          }

          /*
           * If we are rendering the main navigation menu for a page that is not in the navigation
           * hierarchy then used the cached navigation HTML for the last page accessed in the
           * navigation hierarchy.
           */
          if (!webSession.getNavigation().isPageInNavigationHierarchy(getPage()))
          {
            response.write(
                navigationState.getLastPageAccessedInNavigationHierarchyMainNavigationHTML());

            return;
          }
        }

        String requestURI =
          ((HttpServletRequest) (RequestCycle.get().getRequest()).getContainerRequest())
            .getRequestURI();

        StringBuilder buffer = new StringBuilder();

        // Build the main navigation menu
        renderNavigationGroup(requestURI, webSession.getNavigation(), 0, buffer);

        navigationState.setLastPageAccessedInNavigationHierarchyMainNavigationHTML(
            buffer.toString());

        response.write(
            navigationState.getLastPageAccessedInNavigationHierarchyMainNavigationHTML());
      }
    }
  }

  private void renderNavigationGroup(String requestURI, NavigationGroup navigationGroup, int depth,
      StringBuilder buffer)
  {
    if (depth != 0)
    {
      buffer.append("<ul>");
    }

    List<NavigationItem> navigationItems = navigationGroup.getItems();

    // Determine the selected navigation item for the current page in this navigation group
    NavigationItem selectedNavigationItemForCurrentPage = null;

    for (NavigationItem navigationItem : navigationItems)
    {
      if (navigationItem.isPageInNavigationHierarchy(getPage()))
      {
        selectedNavigationItemForCurrentPage = navigationItem;

        break;
      }
    }

    // Draw each of the navigation items
    for (NavigationItem navigationItem : navigationItems)
    {
      // If the navigation item is a navigation group
      if (navigationItem instanceof NavigationGroup)
      {
        NavigationGroup subNavigationGroup = (NavigationGroup) navigationItem;

        if (navigationItem == selectedNavigationItemForCurrentPage)
        {
          if (depth == 0)
          {
            buffer.append("<li class=\"active opened\">");
          }
          else
          {
            buffer.append("<li class=\"active\">");
          }
        }
        else
        {
          buffer.append("<li>");
        }

        buffer.append("<a href=\"javascript:void(0)\">");

        if (subNavigationGroup.getIconClass() != null)
        {
          buffer.append("<i class=\"").append(subNavigationGroup.getIconClass()).append("\"></i>");
        }
        else
        {
          // buffer.append("<i class=\"clip-folder\"></i>");
        }

        if (depth > 0)
        {
          buffer.append("&nbsp;");
        }

        buffer.append("<span class=\"title\">").append(subNavigationGroup.getName()).append(
            "</span>");

        if (((navigationItem == selectedNavigationItemForCurrentPage) && (depth == 0)))
        {
          // buffer.append("<i class=\"icon-arrow\"></i>");
          // buffer.append("<span class=\"selected\"></span>");
        }
        else
        {
          // buffer.append("<i class=\"icon-arrow\"></i>");
          // buffer.append("<span class=\"arrow \"></span>");
        }

        buffer.append("</a>");

        renderNavigationGroup(requestURI, subNavigationGroup, depth + 1, buffer);

        buffer.append("</li>");
      }

      // If the navigation item is a navigation link
      else if (navigationItem instanceof NavigationLink)
      {
        NavigationLink navigationLink = (NavigationLink) navigationItem;

        String url = urlFor(navigationLink.getPageClass(),
          navigationLink.getPageParameters()).toString();

        if (navigationItem == selectedNavigationItemForCurrentPage)
        {
          if (depth == 0)
          {
            buffer.append("<li class=\"active opened\">");
          }
          else
          {
            buffer.append("<li class=\"active\">");
          }
        }
        else
        {
          buffer.append("<li>");
        }

        buffer.append("<a href=\"");

        if (url.startsWith("/"))
        {
          buffer.append(url).append("\">");
        }
        else
        {
          buffer.append(RequestUtils.toAbsolutePath(requestURI, url)).append("\">");
        }

        if (navigationItem.getIconClass() != null)
        {
          buffer.append("<i class=\"").append(navigationItem.getIconClass()).append("\"></i>");
        }
        else
        {
          // buffer.append("<i class=\"clip-file-3\"></i>");
        }

        if (depth > 0)
        {
          buffer.append("&nbsp;");
        }

        buffer.append("<span class=\"title\">").append(navigationItem.getName()).append("</span>");

        if (navigationItem == selectedNavigationItemForCurrentPage)
        {
          buffer.append("<span class=\"selected\"></span>");
        }

        buffer.append("</a>");

        buffer.append("</li>");
      }
    }

    if (depth != 0)
    {
      buffer.append("</ul>");
    }
  }
}

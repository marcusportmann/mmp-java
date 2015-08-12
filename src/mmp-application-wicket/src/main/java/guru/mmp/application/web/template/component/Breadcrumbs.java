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

import guru.mmp.application.web.template.TemplateWebApplication;
import guru.mmp.application.web.template.TemplateWebSession;
import guru.mmp.application.web.template.navigation.NavigationGroup;
import guru.mmp.application.web.template.navigation.NavigationItem;
import guru.mmp.application.web.template.navigation.NavigationState;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.Session;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupElement;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * The <code>Breadcrumbs</code> class provides a Wicket component that renders the breadcrumbs
 * for the Web Application Template.
 *
 * @author Marcus Portmann
 */
public class Breadcrumbs extends Component
{
  private static final long serialVersionUID = 1000000;

  /**
   * @see org.apache.wicket.Component#Component(String)
   *
   * @param id the non-null id of this component
   */
  public Breadcrumbs(final String id)
  {
    super(id);
  }

  /**
   * Render the breadcrumbs.
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

        // If we have cached breadcrumbs HTML then check if we can use it
        if (navigationState.getLastPageAccessedInNavigationHierarchyBreadcrumbsHTML() != null)
        {
          /*
           * If we are rendering the breadcrumbs for the last page accessed in the navigation
           * hierarchy then use the cached navigation HTML.
           */
          if (navigationState.isLastPageAccessedInNavigationHierarchy(getPage()))
          {
            response.write(
                navigationState.getLastPageAccessedInNavigationHierarchyBreadcrumbsHTML());

            return;
          }

          /*
           * If we are rendering the breadcrumbs for a page that is not in the navigation
           * hierarchy then used the cached breadcrumbs HTML for the last page accessed in the
           * navigation hierarchy.
           */
          if (!webSession.getNavigation().isPageInNavigationHierarchy(getPage()))
          {
            response.write(
                navigationState.getLastPageAccessedInNavigationHierarchyBreadcrumbsHTML());

            return;
          }
        }

        StringBuilder buffer = new StringBuilder();

        buffer.append("<ol class=\"breadcrumb bc-1\">");

        Class clazz = null;

        if (webSession.isUserLoggedIn())
        {
          clazz = TemplateWebApplication.getTemplateWebApplication().getSecureHomePage();
        }
        else
        {
          clazz = TemplateWebApplication.getTemplateWebApplication().getHomePage();
        }

        if (clazz.isAssignableFrom(getPage().getPageClass()))
        {
          buffer.append(
              "<li class=\"active\"><a href=\"#\"><i class=\"fa-home\"></i>Home</a></li>");
        }
        else
        {
          buffer.append("<li><a href=\"");

          buffer.append(RequestCycle.get().urlFor(clazz, new PageParameters()));

          buffer.append("\"><i class=\"fa-home\"></i>Home</a></li>");

          renderBreadcrumbs(webSession.getNavigation(), buffer);
        }

        buffer.append("</ol>");

        navigationState.setLastPageAccessedInNavigationHierarchy(getPage());

        navigationState.setLastPageAccessedInNavigationHierarchyBreadcrumbsHTML(buffer.toString());

        response.write(navigationState.getLastPageAccessedInNavigationHierarchyBreadcrumbsHTML());
      }
    }
  }

  private void renderBreadcrumbs(NavigationItem navigationItem, StringBuilder buffer)
  {
    if (navigationItem instanceof NavigationGroup)
    {
      NavigationGroup navigationGroup = (NavigationGroup) navigationItem;

      for (NavigationItem childNavigationItem : navigationGroup.getItems())
      {
        if (childNavigationItem.isPageInNavigationHierarchy(getPage()))
        {
          if (childNavigationItem instanceof NavigationGroup)
          {
            buffer.append("<li>").append(childNavigationItem.getName()).append("</li>");

            renderBreadcrumbs(childNavigationItem, buffer);

            return;
          }
          else
          {
            buffer.append("<li class=\"active\">");
            buffer.append(childNavigationItem.getName());
            buffer.append("</li>");
          }
        }
      }
    }
  }
}

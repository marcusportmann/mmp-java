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

import guru.mmp.application.web.WebApplication;
import guru.mmp.application.web.template.TemplateWebApplication;
import guru.mmp.application.web.template.TemplateWebSession;
import guru.mmp.application.web.template.navigation.NavigationState;

import org.apache.wicket.Application;
import org.apache.wicket.Component;
import org.apache.wicket.Session;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupElement;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.protocol.http.RequestUtils;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;

//~--- JDK imports ------------------------------------------------------------

import javax.servlet.http.HttpServletRequest;

/**
 * The <code>UserMenu</code> class provides a Wicket component that renders the user
 * menu for the Web Application Template.
 *
 * @author Marcus Portmann
 */
public class UserMenu extends Component
{
  private static final long serialVersionUID = 1000000;
  private boolean isMultipleOrganisationSupportEnabled;

  /**
   * @see org.apache.wicket.Component#Component(String)
   *
   * @param id the non-null id of this component
   */
  public UserMenu(final String id)
  {
    super(id);

    Application application = getApplication();

    if (application instanceof TemplateWebApplication)
    {
      isMultipleOrganisationSupportEnabled =
        ((TemplateWebApplication) application).isMultipleOrganisationSupportEnabled();
    }
  }

  /**
   * Render the top navigation menu.
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

        if (!webSession.isUserLoggedIn())
        {
          response.write("");

          return;
        }

        NavigationState navigationState = webSession.getNavigationState();

        if (navigationState.getLastPageAccessedInNavigationHierarchyTopNavigationHTML() != null)
        {
          response.write(
              navigationState.getLastPageAccessedInNavigationHierarchyTopNavigationHTML());

          return;
        }

        String requestURI =
          ((HttpServletRequest) (RequestCycle.get().getRequest()).getContainerRequest())
            .getRequestURI();

        StringBuilder buffer = new StringBuilder();

        /*
        buffer.append("<ul class=\"nav navbar-right\">");

        // User dropdown
        buffer.append("<li class=\"dropdown current-user\">");
        buffer.append("<a data-toggle=\"dropdown\" data-hover=\"dropdown\"");
        buffer.append(" class=\"dropdown-toggle\" data-close-others=\"true\" href=\"#\">");
        buffer.append("<i class=\"clip-user-3\"></i>&nbsp;");

        buffer.append("<span class=\"username\">");
        buffer.append(webSession.getUsername());

        if (isMultipleOrganisationSupportEnabled)
        {
          buffer.append(" | ");
          buffer.append(webSession.getOrganisation());
        }

        buffer.append("</span>");

        buffer.append("<i class=\"clip-chevron-down\"></i>");
        buffer.append("</a>");

        buffer.append("<ul class=\"dropdown-menu\">");

        WebApplication webApplication = (WebApplication) getApplication();

        String url = urlFor(webApplication.getLogoutPage(), new PageParameters()).toString();

        buffer.append("<li><a href=\"#\"><i class=\"clip-user-2\"></i>&nbsp;My Profile</a></li>");
        buffer.append("<li class=\"divider\"></li>");

        buffer.append("<li><a href=\"");

        if (url.startsWith("/"))
        {
          buffer.append(url);
        }
        else
        {
          buffer.append(RequestUtils.toAbsolutePath(requestURI, url));
        }

        buffer.append("\"><i class=\"clip-exit\"></i>&nbsp;Log Out</a></li>");

        buffer.append("</ul>");

        buffer.append("</li>");

        buffer.append("</ul>");
        */

        navigationState.setLastPageAccessedInNavigationHierarchyTopNavigationHTML(
            buffer.toString());
        response.write(navigationState.getLastPageAccessedInNavigationHierarchyTopNavigationHTML());
      }
    }
  }
}

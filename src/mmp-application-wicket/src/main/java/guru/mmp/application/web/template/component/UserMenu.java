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

import guru.mmp.application.security.ISecurityService;
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

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>UserMenu</code> class provides a Wicket component that renders the user
 * menu for the Web Application Template.
 *
 * @author Marcus Portmann
 */
public class UserMenu extends Component
{
  private static final long serialVersionUID = 1000000;
  private boolean hasMultipleOrganisations;
  private boolean isMultipleOrganisationSupportEnabled;

  /* Security Service */
  @Inject
  private ISecurityService securityService;

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

        NavigationState navigationState = webSession.getNavigationState();

        if (navigationState.getCachedUserMenuHTML() != null)
        {
          response.write(navigationState.getCachedUserMenuHTML());

          return;
        }

        WebApplication webApplication = (WebApplication) getApplication();

        String requestURI =
          ((HttpServletRequest) (RequestCycle.get().getRequest()).getContainerRequest())
            .getRequestURI();

        if (!webSession.isUserLoggedIn())
        {
          StringBuilder buffer = new StringBuilder();

          String loginUrl = urlFor(webApplication.getLoginPage(), new PageParameters()).toString();

          buffer.append("<li class=\"login\">");
          buffer.append("<a href=\"");

          if (loginUrl.startsWith("/"))
          {
            buffer.append(loginUrl);
          }
          else
          {
            buffer.append(RequestUtils.toAbsolutePath(requestURI, loginUrl));
          }

          buffer.append("\"><i class=\"fa-sign-in\"></i><span class=\"\"> Login</span></a></li>");

          navigationState.setCachedUserMenuHTML(buffer.toString());
          response.write(navigationState.getCachedUserMenuHTML());
        }
        else
        {
          StringBuilder buffer = new StringBuilder();

          buffer.append("<li class=\"dropdown user-profile\">");
          buffer.append("<a href=\"#\" class=\"dropdown-toggle\" data-toggle=\"dropdown\">");
          buffer.append("<div class=\"img-circle userpic-32\"></div><span>");
          buffer.append(webSession.getUsername());

          if (isMultipleOrganisationSupportEnabled)
          {
            buffer.append(" | ");
            buffer.append(webSession.getOrganisation());
          }

          buffer.append(" <i class=\"fa-angle-down\"></i></span></a>");
          buffer.append("<ul class=\"dropdown-menu user-profile-menu list-unstyled\">");

          // buffer.append("<li><a href=\"#\"><i class=\"fa-wrench\"></i> Settings</a></li>");
          // buffer.append("<li><a href=\"#\"><i class=\"fa-user\"></i> Profile</a></li>");
          // buffer.append("<li><a href=\"#\"><i class=\"fa-info\"></i> Help</a></li>");

          buffer.append("<li class=\"last\"><a href=\"");

          String logoutUrl = urlFor(webApplication.getLogoutPage(),
            new PageParameters()).toString();

          if (logoutUrl.startsWith("/"))
          {
            buffer.append(logoutUrl);
          }
          else
          {
            buffer.append(RequestUtils.toAbsolutePath(requestURI, logoutUrl));
          }

          buffer.append("\"><i class=\"fa-sign-out\"></i> Logout</a></li></ul></li>");

          navigationState.setCachedUserMenuHTML(buffer.toString());
          response.write(navigationState.getCachedUserMenuHTML());
        }
      }
    }
  }
}

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

package guru.mmp.application.web.template.components;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.security.ISecurityService;
import guru.mmp.application.web.WebApplication;
import guru.mmp.application.web.template.TemplateWebApplication;
import guru.mmp.application.web.template.TemplateWebSession;
import guru.mmp.application.web.template.navigation.NavigationState;
import guru.mmp.common.util.StringUtil;
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
 * The <code>BackendUserMenuSideOverlay</code> class provides a Wicket component that renders the
 * user menu side overlay for the backend user interface for the Web Application Template.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("CdiManagedBeanInconsistencyInspection")
public class BackendUserMenuSideOverlay extends Component
{
  private static final long serialVersionUID = 1000000;
  private boolean isMultipleOrganisationSupportEnabled;

  /* Security Service */
  @Inject
  private ISecurityService securityService;

  /**
   * @param id the non-null id of this component
   *
   * @see org.apache.wicket.Component#Component(String)
   */
  public BackendUserMenuSideOverlay(String id)
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
   * Render the backend user menu side overlay.
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

        if (navigationState.getCachedBackendUserMenuSideOverlayHTML() != null)
        {
          response.write(navigationState.getCachedBackendUserMenuSideOverlayHTML());

          return;
        }

        WebApplication webApplication = (WebApplication) getApplication();

        String requestURI = ((HttpServletRequest) (RequestCycle.get()
            .getRequest()).getContainerRequest()).getRequestURI();

        if (!webSession.isUserLoggedIn())
        {
          navigationState.setCachedBackendUserMenuSideOverlayHTML("");
          response.write(navigationState.getCachedBackendUserMenuSideOverlayHTML());
        }
        else
        {
          StringBuilder buffer = new StringBuilder();

          if (isMultipleOrganisationSupportEnabled)
          {
            String fullName = webSession.getUserFullName();

            if (StringUtil.isNullOrEmpty(fullName))
            {
              fullName = webSession.getUsername();
            }

            buffer.append(getBackendUserMenuHeader(fullName, webSession.getOrganisation()
                .getName()));
          }
          else
          {
            buffer.append(getBackendUserMenuHeader(StringUtil.notNull(
                webSession.getUserFullName()), ""));
          }

//        buffer.append("<li class=\"action\"><a>");
//        buffer.append("<div class=\"row\">");
//        buffer.append("<div class=\"pull-left\"><i class=\"fa fa-wrench\"></i></div>");
//        buffer.append("<div class=\"pull-left text-gray-darker\">Settings</div>");
//        buffer.append("</div></a></li>");
//
//        buffer.append("<li class=\"action\"><a>");
//        buffer.append("<div class=\"row\">");
//        buffer.append("<div class=\"pull-left\"><i class=\"fa fa-user\"></i></div>");
//        buffer.append("<div class=\"pull-left text-gray-darker\">Profiles</div>");
//        buffer.append("</div></a></li>");

          buffer.append("<li class=\"action bg-gray-lighter\"><a href=\"");

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

          buffer.append("\"><div class=\"row\">");
          buffer.append("<div class=\"pull-left\"><i class=\"fa fa-sign-out\"></i></div>");
          buffer.append("<div class=\"pull-left text-gray-darker\">Logout</div>");
          buffer.append("</div></a></li>");
          buffer.append("</ul></div></div></aside>");

          navigationState.setCachedBackendUserMenuSideOverlayHTML(buffer.toString());
          response.write(navigationState.getCachedBackendUserMenuSideOverlayHTML());
        }
      }
    }
  }

  private String getBackendUserMenuHeader(String userFullName, String organisationName)
  {
    return "<aside id=\"side-overlay\"><div id=\"side-overlay-scroll\">"
        + "<div class=\"side-header side-content\"><button class=\"btn btn-default pull-right\" "
        + "type=\"button\" data-toggle=\"layout\" data-action=\"side_overlay_close\"><i class=\"fa "
        + "fa-times\"></i></button><div class=\"current-user\"><div class=\"font-w600 text-default\">" + userFullName
        + "</div><div class=\"font-s12 text-gray-darker push-5-l\">" + organisationName
        + "</div></div></div><div class=\"side-content remove-padding-t\"><ul class=\"list "
        + "list-activity pull-r-l border-t\">";
  }
}

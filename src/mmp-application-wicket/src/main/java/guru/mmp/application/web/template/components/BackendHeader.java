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

import guru.mmp.application.web.WebApplication;
import guru.mmp.application.web.template.TemplateWebSession;
import guru.mmp.application.web.template.navigation.NavigationState;
import org.apache.wicket.Component;
import org.apache.wicket.Session;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupElement;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.protocol.http.RequestUtils;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import javax.servlet.http.HttpServletRequest;

/**
 * The <code>BackendHeader</code> class provides a Wicket component that renders the header for the
 * backend user interface for the Web Application Template.
 *
 * @author Marcus Portmann
 */
public class BackendHeader
  extends Component
{
  private static final long serialVersionUID = 1000000;

  /**
   * @param id the non-null id of this component
   *
   * @see org.apache.wicket.Component#Component(String)
   */
  public BackendHeader(String id)
  {
    super(id);
  }

  /**
   * Render the backend header.
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

        // If we have cached the header HTML then use it
        if (navigationState.getCachedBackendHeaderHTML() != null)
        {
          response.write(navigationState.getCachedBackendHeaderHTML());

          return;
        }

        StringBuilder buffer = new StringBuilder();

        buffer.append("<header id=\"header-navbar\" class=\"content-mini content-mini-full\">");
        buffer.append("<ul class=\"nav-header pull-right\">");

        if (!webSession.isUserLoggedIn())
        {
          buffer.append("<li><a class=\"btn btn-default\" href=\"");

          WebApplication webApplication = (WebApplication) getApplication();

          String loginUrl = urlFor(webApplication.getLoginPage(), new PageParameters()).toString();

          String requestURI = ((HttpServletRequest) (RequestCycle.get().getRequest())
            .getContainerRequest()).getRequestURI();

          if (loginUrl.startsWith("/"))
          {
            buffer.append(loginUrl);
          }
          else
          {
            buffer.append(RequestUtils.toAbsolutePath(requestURI, loginUrl));
          }

          buffer.append("\"><i class=\"fa fa-sign-in\"></i></a></li>");
        }
        else
        {
          buffer.append("<li><button class=\"btn btn-default\" data-toggle=\"layout\" " +
            "data-action=\"side_overlay_toggle\" type=\"button\"><i class=\"fa " +
            "fa-user\"></i></button></li>");
        }

        buffer.append(
          "</ul><ul class=\"nav-header pull-left\"><li class=\"hidden-md hidden-lg\"><button " +
            "class=\"btn btn-default\" data-toggle=\"layout\" data-action=\"sidebar_toggle\" " +
            "type=\"button\"><i class=\"fa fa-navicon\"></i></button></li><li class=\"hidden-xs " +
            "hidden-sm\"><button class=\"btn btn-default\" data-toggle=\"layout\" " +
            "data-action=\"sidebar_mini_toggle\" type=\"button\"><i class=\"fa " +
            "fa-ellipsis-v\"></i></button></li></ul></header>");

        navigationState.setCachedBackendHeaderHTML(buffer.toString());

        response.write(navigationState.getCachedBackendHeaderHTML());
      }
    }
  }
}

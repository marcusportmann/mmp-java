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

import guru.mmp.application.web.template.TemplateWebSession;
import guru.mmp.application.web.template.navigation.NavigationState;
import guru.mmp.application.web.template.pages.LoginPage;
import guru.mmp.application.web.template.pages.LogoutPage;
import org.apache.wicket.Component;
import org.apache.wicket.Session;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupElement;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * The <code>BackendUserMenu</code> class provides a Wicket component that renders the user menu
 * for the backend user interface for the Web Application Template.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("CdiManagedBeanInconsistencyInspection")
public class BackendUserMenu extends Component
{
  private static final long serialVersionUID = 1000000;

  /**
   * @param id the non-null id of this component
   *
   * @see org.apache.wicket.Component#Component(String)
   */
  public BackendUserMenu(String id)
  {
    super(id);
  }

  /**
   * Render the backend user menu.
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

        if (navigationState.getCachedBackendUserMenuHTML() == null)
        {
          if (webSession.isUserLoggedIn())
          {
            String buffer = "<li class=\"dropdown user user-menu\">"
                + "<a href=\"#\" class=\"dropdown-toggle\" data-toggle=\"dropdown\"><img src=\""
                + getRequest().getContextPath() + "/images/user-grey.png"
                + "\" class=\"user-image\" alt=\"User Image\"><span class=\"hidden-xs\">"
                + webSession.getUserFullName() + "</span></a><ul class=\"dropdown-menu\">"
                + "<li class=\"user-header\"><img src=\"" + getRequest().getContextPath()
                + "/images/user-white.png\" class=\"img-circle\" alt=\"User Image\"><p>"
                + webSession.getUserFullName() + "</p></li><li class=\"user-footer\">"
                + "<div class=\"pull-right\"><a href=\"" + getRequest().getContextPath()
                + "/logout\" class=\"btn btn-default btn-flat\">Logout</a></div></li></ul></li>";

            navigationState.setCachedBackendUserMenuHTML(buffer);
          }
          else
          {
            String buffer = "<li class=\"user user-menu\"><a href=\"" + urlFor(LoginPage.class,
                new PageParameters())
                + "\" class=\"dropdown-toggle\"><i class=\"fa fa-sign-in\"></i>"
                + "<span class=\"hidden-xs\">Login</span></a></li>";

            navigationState.setCachedBackendUserMenuHTML(buffer);
          }
        }

        response.write(navigationState.getCachedBackendUserMenuHTML());
      }
    }
  }
}

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
import org.apache.wicket.Component;
import org.apache.wicket.Session;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupElement;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.request.Response;

/**
 * The <code>BackendUserPanel</code> class provides a Wicket component that renders the user panel
 * for the backend user interface for the Web Application Template.
 *
 * @author Marcus Portmann
 */
public class BackendUserPanel extends Component
{
  private static final long serialVersionUID = 1000000;

  /**
   * @param id the non-null id of this component
   *
   * @see org.apache.wicket.Component#Component(String)
   */
  public BackendUserPanel(String id)
  {
    super(id);
  }

  /**
   * Render the backend user panel.
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

        if (navigationState.getCachedBackendUserPanelHTML() == null)
        {
          if (webSession.isUserLoggedIn())
          {
            String buffer = "<div class=\"user-panel\"><div class=\"pull-left image\"><img src=\""
                + getRequest().getContextPath() + "/images/user-white.png"
                + "\" class=\"img-circle\" alt=\"User Image\"/></div>"
                + "<div class=\"pull-left info\"><p>" + webSession.getUserFullName()
                + "</p></div></div>";

            navigationState.setCachedBackendUserPanelHTML(buffer);
          }
          else
          {
            navigationState.setCachedBackendUserPanelHTML("");
          }
        }

        response.write(navigationState.getCachedBackendUserPanelHTML());
      }
    }
  }
}

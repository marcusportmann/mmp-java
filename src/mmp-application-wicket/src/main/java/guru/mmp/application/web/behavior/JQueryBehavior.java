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

package guru.mmp.application.web.behavior;

import guru.mmp.application.web.WebApplicationException;
import guru.mmp.application.web.resource.thirdparty.jqueryui.JQueryUIJavaScriptResourceReference;
import org.apache.wicket.Application;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.StringHeaderItem;

/**
 * The <code>JQueryBehavior</code> class implements the Wicket behavior that enables jQuery and
 * jQuery User Interface for a control that is assigned the behavior on page load.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public class JQueryBehavior
  extends AbstractDefaultAjaxBehavior
{
  private static final long serialVersionUID = 1000000;

  /**
   * Render to the web response whatever the component wants to contribute to the head section.
   *
   * @param component the component
   * @param response  the header response
   */
  @Override
  public void renderHead(Component component, IHeaderResponse response)
  {
    try
    {
      super.renderHead(component, response);

      response.render(JavaScriptHeaderItem.forReference(
        Application.get().getJavaScriptLibrarySettings().getJQueryReference()));
      response.render(JavaScriptHeaderItem.forReference(JQueryUIJavaScriptResourceReference.get()));

      CharSequence script = getOnReadyScript();

      if ((script != null) && (script.length() > 0))
      {
        StringBuilder buffer = new StringBuilder();

        buffer.append("<script type=\"text/javascript\">\n$(document).ready(function(){\n");
        buffer.append(script);
        buffer.append("\n});</script>");
        response.render(StringHeaderItem.forString(buffer));
      }
    }
    catch (Throwable e)
    {
      throw new WebApplicationException(
        "Failed the add the jQuery and jQuery UI JavaScript header items to the response", e);
    }
  }

  /**
   * This method should be overridden by a subclass if it needs to run script when the DOM is ready.
   * <p/>
   * The returned script is wrapped by caller into a &lt;script&gt; tag and the
   * "$(document).ready(function(){...}".
   *
   * @return the script to execute when the DOM is ready, or <code>null</code> (default)
   */
  protected CharSequence getOnReadyScript()
  {
    return null;
  }

  @Override
  protected void respond(AjaxRequestTarget target)
  {
    throw new UnsupportedOperationException("Not Implemented");
  }
}

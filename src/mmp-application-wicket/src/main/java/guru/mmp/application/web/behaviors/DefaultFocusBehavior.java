/*
 * Copyright 2017 Marcus Portmann
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

package guru.mmp.application.web.behaviors;

//~--- non-JDK imports --------------------------------------------------------

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnLoadHeaderItem;

/**
 * The <code>DefaultFocusBehavior</code> class implements the Wicket behavior that sets the focus
 * to the control that is assigned the behavior on page load.
 *
 * @author Marcus Portmann
 */
public class DefaultFocusBehavior extends Behavior
{
  private static final long serialVersionUID = 1000000;

  /**
   * Bind this handler to the given component.
   * <p/>
   * This method is called by the host component immediately after this behavior is added to it.
   * This method is useful if you need to do initialisation based on the component it is attached
   * and you can't wait to do it at render time. Keep in mind that if you decide to keep a
   * reference to the host component, it is not thread safe anymore, and should thus only be
   * used in situations where you do not reuse the behavior for multiple components.
   *
   * @param component the component to bind the behavior to
   */
  @Override
  public void bind(Component component)
  {
    component.setOutputMarkupId(true);
  }

  /**
   * Render to the web response whatever the component wants to contribute to the head section.
   *
   * @param component the component
   * @param response  the header response
   */
  @Override
  public void renderHead(Component component, IHeaderResponse response)
  {
    super.renderHead(component, response);

    response.render(OnLoadHeaderItem.forScript(String.format(
        "document.getElementById('%s').focus();", component.getMarkupId())));
  }
}

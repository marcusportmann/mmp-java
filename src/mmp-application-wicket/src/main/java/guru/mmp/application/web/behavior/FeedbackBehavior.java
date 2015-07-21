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

package guru.mmp.application.web.behavior;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.web.util.FeedbackUtil;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;

/**
 * The <code>HasErrorBehavior</code> class implements the Wicket behavior that adds a
 * feedback CSS style to a component that has one or more feedback messages associated with it.
 *
 * @author Marcus Portmann
 */
public class FeedbackBehavior extends Behavior
{
  private static final long serialVersionUID = 1000000;

  /**
   * The re-usable thread-safe feedback behavior.
   */
  public static final FeedbackBehavior FEEDBACK_BEHAVIOR = new FeedbackBehavior();

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
  public void bind(Component component) {}

  /**
   * Configure the component this behavior is associated with.
   *
   * @param component the component to configure
   */
  @Override
  public void onConfigure(Component component)
  {
    FeedbackUtil.applyFeedbackCssClassModifier(component);
  }
}

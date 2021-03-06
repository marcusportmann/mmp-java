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

package guru.mmp.application.web.template.util;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.common.util.StringUtil;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.core.util.string.JavaScriptUtils;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.feedback.FeedbackMessages;

import java.util.List;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>FeedbackUtil</code> class is a utility class that provides methods for working with
 * Wicket feedback messages.
 *
 * @author Marcus Portmann
 */
public class FeedbackUtil
{
  /**
   * The <code>AttributeModifier</code> used to apply the has-error CSS class to a form component
   * that has an error message.
   */
  public static final AttributeModifier HAS_ERROR_CSS_CLASS_MODIFIER = AttributeModifier.append(
      "class", " has-error");

  /**
   * The JavaScript used to clear the feedback for a form component.
   */
  private static final String CLEAR_FORM_COMPONENT_FEEDBACK_JAVA_SCRIPT =
      "clear_form_component_feedback('%1$s');";

  /**
   * The JavaScript used to display the feedback for a form component using the 'domready' event.
   */
  private static final String DOM_READY_SHOW_FORM_COMPONENT_FEEDBACK_JAVA_SCRIPT = "$(function()"
      + "{show_form_component_feedback('%1$s', '%2$s', '%3$s', '%4$s');});";

  /**
   * The JavaScript used to display the feedback for a form component.
   */
  private static final String SHOW_FORM_COMPONENT_FEEDBACK_JAVA_SCRIPT =
      "show_form_component_feedback('%1$s', '%2$s', '%3$s', '%4$s');";

  /**
   * Applies the appropriate CSS class to a component based on the type of feedback message
   * associated with the component using a Wicket <code>AttributeModifier</code>.
   *
   * @param component the component to apply the CSS class to based on the type of feedback message
   *                  associated with the component
   */
  public static void applyFeedbackCssClassModifier(Component component)
  {
    List<? extends Behavior> behaviors = component.getBehaviors();

    if (component.hasErrorMessage())
    {
      if (!behaviors.contains(HAS_ERROR_CSS_CLASS_MODIFIER))
      {
        component.add(HAS_ERROR_CSS_CLASS_MODIFIER);
      }
    }
    else
    {
      if (behaviors.contains(HAS_ERROR_CSS_CLASS_MODIFIER))
      {
        component.remove(HAS_ERROR_CSS_CLASS_MODIFIER);
      }
    }
  }

  /**
   * Generate the Javascript to display the feedback for the specified component.
   *
   * @param id                     the id of the component to provide the feedback for
   * @param component              the component to generate the feedback JavaScript for
   * @param isAjaxRequest          is feedback being generated as part of an Ajax request
   * @param feedbackMessageClasses the additional CSS classes to apply to the feedback message
   *
   * @return the JavaScript to display the feedback message or <code>null</code>
   *         if there is no feedback for the specified component
   */
  public static String generateFeedbackJavaScript(String id, Component component,
      boolean isAjaxRequest, String feedbackMessageClasses)
  {
    if (component.hasFeedbackMessage())
    {
      FeedbackMessages feedbackMessages = component.getFeedbackMessages();

      FeedbackMessage feedbackMessage = feedbackMessages.first();

      String feedbackClass = null;

      if (feedbackMessage.isError())
      {
        feedbackClass = "has-error";
      }
      else if (feedbackMessage.isFatal())
      {
        feedbackClass = "has-error";
      }
      else if (feedbackMessage.isWarning())
      {
        feedbackClass = "has-warning";
      }
      else if (feedbackMessage.isInfo())
      {
        feedbackClass = "has-info";
      }
      else if (feedbackMessage.isDebug())
      {
        feedbackClass = "has-success";
      }

      String javaScript = String.format(isAjaxRequest
          ? SHOW_FORM_COMPONENT_FEEDBACK_JAVA_SCRIPT
          : DOM_READY_SHOW_FORM_COMPONENT_FEEDBACK_JAVA_SCRIPT, id, feedbackClass,
              StringUtil.notNull(feedbackMessageClasses), JavaScriptUtils.escapeQuotes(
              feedbackMessage.getMessage().toString()));

      // Clear the feedback messages for the component
      for (FeedbackMessage componentFeedbackMessage : feedbackMessages)
      {
        componentFeedbackMessage.markRendered();
      }

      return javaScript;
    }
    else
    {
      if (isAjaxRequest)
      {
        return String.format(CLEAR_FORM_COMPONENT_FEEDBACK_JAVA_SCRIPT, id);
      }

      return null;
    }
  }
}

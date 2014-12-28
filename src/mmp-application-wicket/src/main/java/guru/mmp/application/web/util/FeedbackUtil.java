/*
 * Copyright 2014 Marcus Portmann
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

package guru.mmp.application.web.util;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.common.util.StringUtil;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.feedback.FeedbackMessages;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;

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
  public static final AttributeModifier HAS_ERROR_CSS_CLASS_MODIFIER =
    AttributeModifier.append("class", " has-error");

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
   * Generate the HTML for the feedback message associated with the specified component.
   *
   * @param id        the id for the HTML div that will be used to display the feedback message or
   *                  <code>null</code> if no id
   * @param component the component to generate the feedback message HTML for
   *
   * @return the HTML for the feedback message
   */
  public static String generateFeedbackHtml(String id, Component component)
  {
    StringBuilder buffer = new StringBuilder();

    if (StringUtil.isNullOrEmpty(id))
    {
      buffer.append("<div");
    }
    else
    {
      buffer.append("<div id=\"").append(id).append("\"");
    }

    if (component.hasFeedbackMessage())
    {
      FeedbackMessages feedbackMessages = component.getFeedbackMessages();

      buffer.append(" class=\"");

      FeedbackMessage feedbackMessage = feedbackMessages.first();

      if (feedbackMessage.isError())
      {
        buffer.append("feedback-error");
      }
      else if (feedbackMessage.isFatal())
      {
        buffer.append("feedback-error");
      }
      else if (feedbackMessage.isWarning())
      {
        buffer.append("feedback-warning");
      }
      else if (feedbackMessage.isInfo())
      {
        buffer.append("feedback-info");
      }
      else if (feedbackMessage.isDebug())
      {
        buffer.append("feedback-debug");
      }

      buffer.append("\">");
      buffer.append(feedbackMessage.getMessage());
      buffer.append("</div>");

      // Clear the feedback messages for the component
      for (FeedbackMessage componentFeedbackMessage : feedbackMessages)
      {
        componentFeedbackMessage.markRendered();
      }
    }
    else
    {
      buffer.append(" class=\"hidden\"></div>");
    }

    return buffer.toString();
  }
}

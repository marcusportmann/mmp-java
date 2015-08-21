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

package guru.mmp.application.web.util;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.common.util.StringUtil;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.core.util.string.JavaScriptUtils;
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
    return "";

    /*
    if (component.hasFeedbackMessage())
    {
      FeedbackMessages feedbackMessages = component.getFeedbackMessages();

      FeedbackMessage feedbackMessage = feedbackMessages.first();

      String feedbackHtml = generateFeedbackHtml(id, feedbackMessage);

      // Clear the feedback messages for the component
      for (FeedbackMessage componentFeedbackMessage : feedbackMessages)
      {
        componentFeedbackMessage.markRendered();
      }
    }

    return buffer.toString();
    */
  }

  /**
   * Generate the Javascript to display the feedback for the specified component.
   *
   * @param id          the id of the component to provide the feedback for
   * @param component   the component to generate the feedback JavaScript for
   * @param useDOMReady use the DOM ready event to execute the JavaScript
   *
   * @return the HTML for the feedback message
   */
  public static String generateFeedbackJavaScript(String id, Component component,
      boolean useDOMReady)
  {
    if (component.hasFeedbackMessage())
    {
      FeedbackMessages feedbackMessages = component.getFeedbackMessages();

      FeedbackMessage feedbackMessage = feedbackMessages.first();

      String feedbackId = id + "Feedback";

      StringBuilder buffer = new StringBuilder();

      if (useDOMReady)
      {
        buffer.append("$(function() {");
      }

      buffer.append("if ($('#").append(id).append("')");
      buffer.append(" && $('#").append(id).append("').parent()");
      buffer.append(" && $('#").append(id).append("').parent().parent()");
      buffer.append(" && $('#").append(id).append("').parent().parent().hasClass('form-group')){");
      buffer.append("$('#").append(id).append("').parent().parent().addClass('");

      if (feedbackMessage.isError())
      {
        buffer.append("has-error");
      }
      else if (feedbackMessage.isFatal())
      {
        buffer.append("has-error");
      }
      else if (feedbackMessage.isWarning())
      {
        buffer.append("has-warning");
      }
      else if (feedbackMessage.isInfo())
      {
        buffer.append("has-info");
      }
      else if (feedbackMessage.isDebug())
      {
        buffer.append("has-success");
      }

      buffer.append("');");
      buffer.append("$('#").append(feedbackId).append("').replaceWith('");

      buffer.append(JavaScriptUtils.escapeQuotes(FeedbackUtil.generateFeedbackHtml(feedbackId,
          feedbackMessage)));

      buffer.append("');");
      buffer.append("}");

      if (useDOMReady)
      {
        buffer.append("});");
      }

      // Clear the feedback messages for the component
      for (FeedbackMessage componentFeedbackMessage : feedbackMessages)
      {
        componentFeedbackMessage.markRendered();
      }

      return buffer.toString();
    }
    else
    {
      return "";
    }
  }

  private static String generateFeedbackHtml(String id, FeedbackMessage feedbackMessage)
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

    buffer.append(" class=\"feedback\">");
    buffer.append(feedbackMessage.getMessage());
    buffer.append("</div>");

    return buffer.toString();
  }
}

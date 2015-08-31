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
   * The JavaScript used to clear the feedback for a form component.
   */
  private static final String CLEAR_FEEDBACK_JAVA_SCRIPT = "if($('#%1$sFeedback')){"
    + "if($('#%1$sFeedback').parent().hasClass('form-group')){"
    + "$('#%1$sFeedback').parent().removeClass('" + "has-error has-warning has-info has-success"
    + "');} if($('#%1$sFeedback').parent().parent().hasClass('form-group')){"
    + "$('#%1$sFeedback').parent().parent().removeClass('"
    + "has-error has-warning has-info has-success"
    + "');}$('#%1$sFeedback').replaceWith('<div id=\"#%1$sFeedback\"></div>');}";

  /**
   * The JavaScript used to display the feedback for a form component using the 'domready' event.
   */
  private static final String DOM_READY_FEEDBACK_JAVA_SCRIPT =
    "$(function(){if($('#%1$sFeedback')){"
    + "if($('#%1$sFeedback').parent().hasClass('form-group')){"
    + "$('#%1$sFeedback').parent().addClass('%2$s');}"
    + "if($('#%1$sFeedback').parent().parent().hasClass('form-group')){"
    + "$('#%1$sFeedback').parent().parent().addClass('%2$s');}"
    + "$('#%1$sFeedback').replaceWith('%3$s');}});";

  /**
   * The JavaScript used to display the feedback for a form component.
   */
  private static final String FEEDBACK_JAVA_SCRIPT = "if($('#%1$sFeedback')){"
    + "if($('#%1$sFeedback').parent().hasClass('form-group')){"
    + "$('#%1$sFeedback').parent().addClass('%2$s');}"
    + "if($('#%1$sFeedback').parent().parent().hasClass('form-group')){"
    + "$('#%1$sFeedback').parent().parent().addClass('%2$s');}"
    + "$('#%1$sFeedback').replaceWith('%3$s');}";

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
   * @param id            the id of the component to provide the feedback for
   * @param component     the component to generate the feedback JavaScript for
   * @param isAjaxRequest is feedback being generated as part of an Ajax request
   *
   * @return the JavaScript to display the feedback message or <code>null</code>
   *         if there is no feedback for the specified component
   */
  public static String generateFeedbackJavaScript(String id, Component component,
      boolean isAjaxRequest)
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

      String feedbackId = id + "Feedback";

      String javaScript = String.format(isAjaxRequest
          ? FEEDBACK_JAVA_SCRIPT
          : DOM_READY_FEEDBACK_JAVA_SCRIPT, id, feedbackClass,
            JavaScriptUtils.escapeQuotes(FeedbackUtil.generateFeedbackHtml(feedbackId,
              feedbackMessage)));

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
        return String.format(CLEAR_FEEDBACK_JAVA_SCRIPT, id);
      }

      return null;
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

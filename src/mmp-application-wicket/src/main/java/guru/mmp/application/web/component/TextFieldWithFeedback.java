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

package guru.mmp.application.web.component;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.web.util.FeedbackUtil;
import org.apache.wicket.ajax.AjaxRequestHandler;
import org.apache.wicket.core.util.string.JavaScriptUtils;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.IRequestHandler;

/**
 * The <code>TextFieldWithFeedback</code> class extends the Wicket <code>TextField</code>
 * component to provide support for displaying the feedback message for the component.
 *
 * This class also applies CSS styling to the Wicket <code>TextField</code>
 * component to indicate the type of feedback message associated with the component.
 *
 * @param <T>
 *
 * @author Marcus Portmann
 */
public class TextFieldWithFeedback<T> extends TextField<T>
{
  private static final long serialVersionUID = 1000000;
  private String feedbackMarkupId;

  /**
   * Constructs a new <code>TextFieldWithFeedback</code>.
   *
   * @param id the non-null id of this component
   */
  public TextFieldWithFeedback(String id)
  {
    super(id);

    feedbackMarkupId = id + "Feedback";
  }

  /**
   * Constructs a new <code>TextFieldWithFeedback</code>.
   *
   * @param id    the non-null id of this component
   * @param model the model for this component
   */
  public TextFieldWithFeedback(String id, IModel<T> model)
  {
    super(id, model);

    feedbackMarkupId = id + "Feedback";
  }

  /**
   * Returns the markup ID of the feedback for the form component.
   *
   * @return the markup ID of the feedback for the form component
   */
  public String getFeedbackMarkupId()
  {
    return feedbackMarkupId;
  }

  /**
   * @see org.apache.wicket.markup.html.form.TextField#onConfigure()
   */
  @Override
  protected void onConfigure()
  {
    super.onConfigure();

    FeedbackUtil.applyFeedbackCssClassModifier(this);
  }

  /**
   * @see org.apache.wicket.markup.html.form.TextField#onRender()
   */
  @Override
  protected void onRender()
  {
    super.onRender();

    IRequestHandler requestHandler = getRequestCycle().getActiveRequestHandler();

    if (requestHandler instanceof AjaxRequestHandler)
    {
      AjaxRequestHandler ajaxRequestHandler = (AjaxRequestHandler) requestHandler;

      ajaxRequestHandler.appendJavaScript(
          String.format(
            "if (Wicket.$('%s')) { Wicket.DOM.replace(Wicket.$('%s'), '%s'); };",
              getFeedbackMarkupId(), getFeedbackMarkupId(),
                JavaScriptUtils.escapeQuotes(
                  FeedbackUtil.generateFeedbackHtml(getFeedbackMarkupId(), this))));
    }
    else
    {
      getResponse().write(FeedbackUtil.generateFeedbackHtml(getFeedbackMarkupId(), this));
    }
  }
}

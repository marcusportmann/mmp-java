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

import guru.mmp.application.web.template.resources.TemplateJavaScriptResourceReference;
import guru.mmp.application.web.template.util.FeedbackUtil;

import org.apache.wicket.ajax.AjaxRequestHandler;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.IRequestHandler;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;

/**
 * The <code>DropDownChoiceWithFeedback</code> class extends the Wicket <code>DropDownChoice</code>
 * component to provide support for displaying the feedback message for the component.
 *
 * @param <T>
 *
 * @author Marcus Portmann
 */
public class DropDownChoiceWithFeedback<T> extends DropDownChoice<T>
{
  private static final long serialVersionUID = 1000000;
  private String feedbackMessageClasses;

  /**
   * Constructs a new <code>DropDownChoiceWithFeedback</code>.
   *
   * @param id the non-null id of this component
   */
  @SuppressWarnings("unused")
  public DropDownChoiceWithFeedback(String id)
  {
    super(id);
  }

  /**
   * Constructs a new <code>DropDownChoiceWithFeedback</code>.
   *
   * @param id      the non-null id of this component
   * @param choices the choices
   */
  public DropDownChoiceWithFeedback(String id, List<? extends T> choices)
  {
    super(id, choices);
  }

  /**
   * Constructs a new <code>DropDownChoiceWithFeedback</code>.
   *
   * @param id      the non-null id of this component
   * @param model   the model for this component
   * @param choices the choices
   */
  public DropDownChoiceWithFeedback(String id, IModel<T> model, List<? extends T> choices)
  {
    super(id, model, choices);
  }

  /**
   * Constructs a new <code>DropDownChoiceWithFeedback</code>.
   *
   * @param id       the non-null id of this component
   * @param choices  the choices
   * @param renderer the custom renderer for the choices
   */
  public DropDownChoiceWithFeedback(String id, List<? extends T> choices,
      IChoiceRenderer<? super T> renderer)
  {
    super(id, choices, renderer);
  }

  /**
   * Constructs a new <code>DropDownChoiceWithFeedback</code>.
   *
   * @param id       the non-null id of this component
   * @param model    the model for this component
   * @param choices  the choices
   * @param renderer the custom renderer for the choices
   */
  public DropDownChoiceWithFeedback(String id, IModel<T> model, List<? extends T> choices,
      IChoiceRenderer<? super T> renderer)
  {
    super(id, model, choices, renderer);
  }

  /**
   * Returns the additional CSS classes to apply to the feedback message.
   *
   * @return the additional CSS classes to apply to the feedback message
   */
  public String getFeedbackMessageClasses()
  {
    return feedbackMessageClasses;
  }

  /**
   * @param response the Wicket header response
   *
   * @see org.apache.wicket.markup.html.form.TextField#renderHead(IHeaderResponse)
   */
  @Override
  public void renderHead(IHeaderResponse response)
  {
    super.renderHead(response);

    response.render(TemplateJavaScriptResourceReference.getJavaScriptHeaderItem());

    String feedbackJavaScript = FeedbackUtil.generateFeedbackJavaScript(getId(), this, false,
        feedbackMessageClasses);

    if (feedbackJavaScript != null)
    {
      response.render(JavaScriptHeaderItem.forScript(feedbackJavaScript, null));
    }
  }

  /**
   * Set the additional CSS classes to apply to the feedback message.
   *
   * @param feedbackMessageClasses the additional CSS classes to apply to the feedback message
   */
  public void setFeedbackMessageClasses(String feedbackMessageClasses)
  {
    this.feedbackMessageClasses = feedbackMessageClasses;
  }

  /**
   * @see org.apache.wicket.markup.html.form.DropDownChoice#onConfigure()
   */
  @Override
  protected void onConfigure()
  {
    super.onConfigure();

    FeedbackUtil.applyFeedbackCssClassModifier(this);
  }

  /**
   * @see org.apache.wicket.markup.html.form.DropDownChoice#onRender()
   */
  @Override
  protected void onRender()
  {
    super.onRender();

    IRequestHandler requestHandler = getRequestCycle().getActiveRequestHandler();

    if (requestHandler instanceof AjaxRequestHandler)
    {
      AjaxRequestHandler ajaxRequestHandler = (AjaxRequestHandler) requestHandler;

      String feedbackJavaScript = FeedbackUtil.generateFeedbackJavaScript(getId(), this, true,
          feedbackMessageClasses);

      if (feedbackJavaScript != null)
      {
        ajaxRequestHandler.appendJavaScript(feedbackJavaScript);
      }
    }
  }
}

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

package guru.mmp.application.web.template.component;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.web.template.resource.TemplateJavaScriptResourceReference;
import guru.mmp.application.web.template.util.FeedbackUtil;
import org.apache.wicket.ajax.AjaxRequestHandler;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.IRequestHandler;

/**
 * The <code>PasswordTextFieldWithFeedback</code> class extends the Wicket
 * <code>PasswordTextField</code> component to provide support for displaying the feedback message
 * for the component.
 *
 * @author Marcus Portmann
 */
public class PasswordTextFieldWithFeedback extends PasswordTextField
{
  private static final long serialVersionUID = 1000000;

  /**
   * Constructs a new <code>PasswordTextFieldWithFeedback</code>.
   *
   * @param id the non-null id of this component
   */
  public PasswordTextFieldWithFeedback(String id)
  {
    super(id);
  }

  /**
   * Constructs a new <code>PasswordTextFieldWithFeedback</code>.
   *
   * @param id    the non-null id of this component
   * @param model the model for the component
   */
  public PasswordTextFieldWithFeedback(String id, IModel<String> model)
  {
    super(id, model);
  }

  /**
   * @see org.apache.wicket.markup.html.form.TextField#renderHead(IHeaderResponse)
   *
   * @param response the Wicket header response
   */
  @Override
  public void renderHead(IHeaderResponse response)
  {
    super.renderHead(response);

    response.render(TemplateJavaScriptResourceReference.getJavaScriptHeaderItem());

    String feedbackJavaScript = FeedbackUtil.generateFeedbackJavaScript(getId(), this, false);

    if (feedbackJavaScript != null)
    {
      response.render(JavaScriptHeaderItem.forScript(feedbackJavaScript, null));
    }
  }

  /**
   * @see org.apache.wicket.markup.html.form.PasswordTextField#onConfigure()
   */
  @Override
  protected void onConfigure()
  {
    super.onConfigure();

    FeedbackUtil.applyFeedbackCssClassModifier(this);
  }

  /**
   * @see org.apache.wicket.markup.html.form.PasswordTextField#onRender()
   */
  @Override
  protected void onRender()
  {
    super.onRender();

    IRequestHandler requestHandler = getRequestCycle().getActiveRequestHandler();

    if (requestHandler instanceof AjaxRequestHandler)
    {
      AjaxRequestHandler ajaxRequestHandler = (AjaxRequestHandler) requestHandler;

      if (ajaxRequestHandler.getComponents().contains(this.getForm()))
      {
        getResponse().write("<div id=\"" + getId() + "Feedback\" class=\"hidden\"></div>");
      }

      String feedbackJavaScript = FeedbackUtil.generateFeedbackJavaScript(getId(), this, true);

      if (feedbackJavaScript != null)
      {
        ajaxRequestHandler.appendJavaScript(feedbackJavaScript);
      }
    }
    else
    {
      getResponse().write("<div id=\"" + getId() + "Feedback\" class=\"hidden\"></div>");
    }
  }
}

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

package guru.mmp.application.web.template.component;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.security.UserDirectory;

import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.MarkupElement;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.link.ILinkListener;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.request.IRequestParameters;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;

/**
 * The <code>DropdownButton</code> class provides a Wicket component that renders a dropdown button.
 *
 * @author Marcus Portmann
 *
 * @param <T>
 */
@SuppressWarnings("unused")
public class DropdownButton<T> extends Panel
  implements ILinkListener
{
  private static final long serialVersionUID = 1000000;
  private IModel<? extends List<? extends T>> choices;
  private IChoiceRenderer<T> renderer;

  /**
   * Constructs a new <code>DropdownButton</code>.
   *
   * @param id       the non-null id of this component
   * @param model    the model for the component
   * @param choices  the model providing the list of all rendered choices
   * @param renderer the custom renderer for the choices
   */
  public DropdownButton(String id, IModel<T> model, IModel<? extends List<? extends T>> choices,
      IChoiceRenderer<T> renderer)
  {
    super(id, model);

    this.choices = choices;
    this.renderer = renderer;
  }

  /**
   * Constructs a new <code>DropdownButton</code>.
   *
   * @param id       the non-null id of this component
   * @param model    the model for the component
   * @param choices  the choices
   * @param renderer the custom renderer for the choices
   */
  public DropdownButton(String id, IModel<T> model, List<? extends T> choices,
      IChoiceRenderer<T> renderer)
  {
    this(id, model, new ListModel<>(choices), renderer);
  }

  /**
   * Method description
   */
  @Override
  public void onLinkClicked()
  {
    Request request = RequestCycle.get().getRequest();
    IRequestParameters requestParameters = request.getRequestParameters();
    StringValue choiceId = requestParameters.getParameterValue("choiceId");

    if (!choiceId.isEmpty())
    {
      setDefaultModelObject(renderer.getObject(choiceId.toString(), choices));
    }

  }

  /**
   * Render the breadcrumbs.
   */
  @Override
  protected void onRender()
  {
    MarkupStream markupStream = findMarkupStream();
    MarkupElement element = markupStream.get();
    Response response = getResponse();

    List<? extends T> choices = this.choices.getObject();

    for (int i = 0; i < choices.size(); i++)
    {
      T choice = choices.get(i);

      PageParameters pageParameters = new PageParameters();
      pageParameters.add("choiceId", renderer.getIdValue(choice, i));

      response.write("<a href=\"" + urlFor(ILinkListener.INTERFACE, pageParameters) + "\">"
          + renderer.getDisplayValue(choice) + "</a>");

    }
  }
}

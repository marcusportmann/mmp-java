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

package guru.mmp.application.web.component;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.web.util.FeedbackUtil;
import org.apache.wicket.Component;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupElement;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.request.Response;

/**
 * The <code>FeedbackLabel<code> class provides a label displaying feedback messages for a
 * form component.
 *
 * @author Marcus Portmann
 */
public class FeedbackLabel extends Component
{
  private static final long serialVersionUID = 1000000;

  /**
   * The form component the feedback label is associated with.
   */
  private FormComponent<?> component;

  /**
   * Constructs a new <code>FeedbackLabel</code>.
   *
   * @param id        the non-null id of this component
   * @param component the form component the feedback label is associated with
   */
  public FeedbackLabel(String id, FormComponent<?> component)
  {
    super(id);
    setOutputMarkupId(true);

    this.component = component;
    this.component.setOutputMarkupId(true);
  }

  /**
   * Render the feedback label.
   */
  @Override
  protected void onRender()
  {
    MarkupStream markupStream = findMarkupStream();
    MarkupElement element = markupStream.get();
    Response response = getResponse();

    if (element instanceof ComponentTag)
    {
      if (component.hasFeedbackMessage())
      {
        response.write(FeedbackUtil.generateFeedbackHtml(getMarkupId(), component));
      }
    }
  }
}

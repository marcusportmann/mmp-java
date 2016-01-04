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

package guru.mmp.application.web.component;

//~--- non-JDK imports --------------------------------------------------------

import org.apache.wicket.Component;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupElement;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.Response;

/**
 * The <code>XMLPanel</code> class provides a Wicket component that can be used to display XML.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public class XMLPanel extends Component
{
  private static final long serialVersionUID = 1000000;

  /**
   * Constructs a new <code>XMLPanel</code>.
   *
   * @param id    the non-null id of this component
   * @param model the model containing the XML to display
   */
  public XMLPanel(String id, IModel<?> model)
  {
    super(id, model);
  }

  /**
   * Render the XML panel.
   */
  @Override
  protected void onRender()
  {
    String xml = getDefaultModelObjectAsString();

    MarkupStream markupStream = findMarkupStream();
    MarkupElement element = markupStream.get();
    Response response = getResponse();

    if (element instanceof ComponentTag)
    {
      xml = xml.replace("<", "&lt;");
      xml = xml.replace(">", "&gt;");

      response.write(xml);
    }
  }
}

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

import guru.mmp.application.web.WebSession;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.protocol.http.servlet.ServletWebRequest;
import org.apache.wicket.request.cycle.RequestCycle;

/**
 * The <code>InputPanel</code> class provides the base class from which all input panels should
 * be derived.
 *
 * @author Marcus Portmann
 */
public abstract class InputPanel
  extends Panel
{
  private static final long serialVersionUID = 1000000;

  /**
   * Constructs a new <code>InputPanel</code>.
   *
   * @param id the non-null id of this component
   */
  public InputPanel(String id)
  {
    super(id);
  }

  /**
   * Constructs a new <code>InputPanel</code>.
   *
   * @param id    the non-null id of this component
   * @param model the model for the panel
   */
  @SuppressWarnings("unused")
  public InputPanel(String id, IModel<?> model)
  {
    super(id, model);
  }

  /**
   * Returns the form this input panel is associated with.
   *
   * @return the form this input panel is associated with
   */
  public Form<?> getForm()
  {
    MarkupContainer parent = getParent();

    while (parent != null)
    {
      if (parent instanceof Form<?>)
      {
        return (Form<?>) parent;
      }

      parent = parent.getParent();
    }

    return null;
  }

  /**
   * Returns the IP address of the remote client associated with the request.
   *
   * @return the IP address of the remote client associated with the request
   */
  public String getRemoteAddress()
  {
    ServletWebRequest servletWebRequest = (ServletWebRequest) RequestCycle.get().getRequest();

    return servletWebRequest.getContainerRequest().getRemoteAddr();
  }

  /**
   * Returns the <code>WebSession</code> for the user associated with the current request.
   *
   * @return the <code>WebSession</code> for the user associated with the current request
   */
  protected WebSession getWebApplicationSession()
  {
    return WebSession.class.cast(getSession());
  }
}

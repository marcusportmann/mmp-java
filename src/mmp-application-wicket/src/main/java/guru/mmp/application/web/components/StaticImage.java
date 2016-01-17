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

package guru.mmp.application.web.components;

//~--- non-JDK imports --------------------------------------------------------

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.model.IModel;

/**
 * The <code>StaticImage</code> class provides a Wicket component that can be used to include
 * external image in a page.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public class StaticImage extends WebComponent
{
  private static final long serialVersionUID = 1000000;

  /**
   * Constructs a new <code>StaticImage</code>.
   *
   * @param id    the non-null id of this component
   * @param model the model containing the URL for the image
   */
  public StaticImage(String id, IModel<?> model)
  {
    super(id, model);
  }

  @Override
  protected void onComponentTag(ComponentTag tag)
  {
    super.onComponentTag(tag);
    checkComponentTag(tag, "img");

    tag.put("src", getDefaultModelObjectAsString());
  }
}

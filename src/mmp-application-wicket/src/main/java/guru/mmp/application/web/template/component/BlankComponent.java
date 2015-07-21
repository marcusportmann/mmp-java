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

import org.apache.wicket.Component;
import org.apache.wicket.markup.MarkupStream;

/**
 * The <code>BlankComponent</code> class provides a Wicket component that renders nothing.
 *
 * @author Marcus Portmann
 */
public class BlankComponent extends Component
{
  private static final long serialVersionUID = 1000000;

  /**
   * @see org.apache.wicket.Component#Component(String)
   *
   * @param id the non-null id of this component
   */
  public BlankComponent(final String id)
  {
    super(id);
  }

  /**
   * Render the blank component.
   */
  @Override
  protected void onRender()
  {
    MarkupStream markupStream = findMarkupStream();

    markupStream.skipComponent();
  }
}

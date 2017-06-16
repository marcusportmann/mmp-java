/*
 * Copyright 2017 Marcus Portmann
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

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;

import java.util.List;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>ExtensibleDialogImplementation</code> class provides the base class that all
 * implementations for the <code>ExtensibleDialog</code> class should be derived from.
 *
 * @author Marcus Portmann
 */
public abstract class ExtensibleDialogImplementation extends Panel
{
  private static final long serialVersionUID = 1000000;
  private String title;

  /**
   * Constructs a new <code>ExtensibleDialogImplementation</code>.
   *
   * @param title the title for the extensible dialog
   */
  public ExtensibleDialogImplementation(String title)
  {
    super("implementation");

    this.title = title;
  }

  /**
   * Returns the buttons associated with the extensible dialog.
   *
   * @return the buttons associated with the extensible dialog
   */
  protected abstract List<ExtensibleDialogButton> getButtons();

  /**
   * Returns the extensible and reusable modal dialog this implementation is associated with.
   *
   * @return the extensible and reusable modal dialog this implementation is associated with
   */
  protected final ExtensibleDialog getDialog()
  {
    MarkupContainer parent = getParent();

    while (parent != null)
    {
      if (parent instanceof ExtensibleDialog)
      {
        return (ExtensibleDialog) parent;
      }

      parent = parent.getParent();
    }

    return null;
  }

  /**
   * Returns the title for the extensible dialog.
   *
   * @return the title for the extensible dialog
   */
  protected final String getTitle()
  {
    return title;
  }
}

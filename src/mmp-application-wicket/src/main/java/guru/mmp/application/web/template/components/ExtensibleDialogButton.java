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

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * The <code>ExtensibleDialogButton</code> class.
 *
 * @author Marcus Portmann
 */
public class ExtensibleDialogButton extends Button
{
  /**
   * Constructs a new <code>ExtensibleDialogButton</code>.
   *
   * @param labelModel the model for the label for the button
   */
  public ExtensibleDialogButton(IModel<String> labelModel)
  {
    this(labelModel, false);
  }

  /**
   * Constructs a new <code>ExtensibleDialogButton</code>.
   *
   * @param label the label for the button
   */
  public ExtensibleDialogButton(String label)
  {
    this(new Model<>(label));
  }

  /**
   * Constructs a new <code>ExtensibleDialogButton</code>.
   *
   * @param labelModel the model for the label for the button
   * @param isPrimary  is this the primary button for the extensible dialog
   */
  public ExtensibleDialogButton(IModel<String> labelModel, boolean isPrimary)
  {
    super("button");

    Label label = new Label("label", labelModel);
    label.setRenderBodyOnly(true);

    if (isPrimary)
    {
      add(new AttributeAppender("class", "btn btn-primary"));
    }
    else
    {
      add(new AttributeAppender("class", "btn btn-default"));
    }

    add(label);
  }

  /**
   * Constructs a new <code>ExtensibleDialogButton</code>.
   *
   * @param label     the label for the button
   * @param isPrimary is this the primary button for the extensible dialog
   */
  public ExtensibleDialogButton(String label, boolean isPrimary)
  {
    this(new Model<>(label), isPrimary);
  }
}

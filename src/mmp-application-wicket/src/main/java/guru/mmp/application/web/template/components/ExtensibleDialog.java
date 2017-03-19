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

import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;

import java.util.ArrayList;
import java.util.List;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>ExtensibleDialog</code> class provides an extensible and reusable modal dialog box
 * which appears over other content. The content and behaviour of the dialog box can be changed by
 * specifying a new <code>ExtensibleDialogImplementation</code>.
 * <p/>
 * The dialog can be opened or closed by straight JavaScript or by a Wicket AjaxRequestTarget.
 * <p/>
 * It can optionally be closed by clicking outside the dialog.
 *
 *
 * @author Marcus Portmann
 */
public class ExtensibleDialog extends Panel
{
  private static final long serialVersionUID = 1000000;
  private ExtensibleDialogImplementation implementation;
  private ListView<ExtensibleDialogButton> buttonsListView;

  /**
   * Constructs a new <code>ExtensibleDialog</code>.
   *
   * @param id the non-null id of this component
   */
  public ExtensibleDialog(String id)
  {
    super(id);

    setOutputMarkupId(true);

    // The "title" label
    Label titleLabel = new Label("title", new PropertyModel<String>(this, "title"));
    titleLabel.setRenderBodyOnly(true);
    add(titleLabel);

    // Initialise the default extensible dialog implementation
    this.implementation = new DefaultImplementation();

    add(implementation);

    // Initialise the buttons
    buttonsListView = new ListView<ExtensibleDialogButton>("buttons", implementation.getButtons())
    {
      protected void populateItem(ListItem<ExtensibleDialogButton> item)
      {
        item.add(item.getModelObject());
      }
    };
    add(buttonsListView);
  }

  /**
   * Returns the title for the extensible dialog.
   *
   * @return the title for the extensible dialog
   */
  public String getTitle()
  {
    return implementation.getTitle();
  }

  /**
   * Hide the extensible dialog using Ajax.
   *
   * @param target the AJAX request target
   */
  public void hide(AjaxRequestTarget target)
  {
    target.appendJavaScript(getHideJavaScript());
  }

  /**
   * Show the extensible dialog using Ajax.
   *
   * @param target         the AJAX request target
   * @param implementation the implementation for the extensible dialog
   */
  public void show(AjaxRequestTarget target, ExtensibleDialogImplementation implementation)
  {
    // Replace the implementation
    this.implementation.replaceWith(implementation);
    this.implementation = implementation;

    // Add the buttons for the implementation
    buttonsListView.setList(implementation.getButtons());

    // Flag the dialog to be redrawn
    if (target != null)
    {
      target.add(this);
    }

    // Show the dialog
    target.appendJavaScript(getShowJavaScript());
  }

  /**
   * Returns the JavaScript required to hide the dialog in the client browser.
   *
   * @return the JavaScript required to hide the dialog in the client browser
   */
  private String getHideJavaScript()
  {
    return "$('#" + getMarkupId() + "').modal('hide')";
  }

  /**
   * Returns the JavaScript required to show the dialog in the client browser.
   *
   * @return the JavaScript required to show the dialog in the client browser
   */
  private String getShowJavaScript()
  {
    if (!isEnabled())
    {
      return "";
    }

    return "$('#" + getMarkupId() + "').modal('show')";
  }

  /**
   * The <code>DefaultImplementation</code> class provides the empty default implementation for
   * the extensible dialog.
   */
  private class DefaultImplementation extends ExtensibleDialogImplementation
  {
    /**
     * Constructs a new <code>DefaultImplementation</code>.
     */
    DefaultImplementation()
    {
      super("");
    }

    @Override
    protected List<ExtensibleDialogButton> getButtons()
    {
      ExtensibleDialogButton okButton = new ExtensibleDialogButton("OK", true);

      okButton.add(new AjaxEventBehavior("click")
      {
        @Override
        protected void onEvent(AjaxRequestTarget target)
        {
          getDialog().hide(target);
        }
      });

      List<ExtensibleDialogButton> buttons = new ArrayList<>();
      buttons.add(okButton);

      return buttons;
    }
  }
}

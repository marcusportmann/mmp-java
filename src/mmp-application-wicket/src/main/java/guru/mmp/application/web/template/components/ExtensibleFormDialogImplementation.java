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

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;

import java.io.Serializable;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>ExtensibleFormDialogImplementation</code> class provides the base class that all
 * implementations for the <code>ExtensibleFormDialog</code> should be derived from.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public abstract class ExtensibleFormDialogImplementation extends Panel
{
  private static final long serialVersionUID = 1000000;
  private String title;
  private String submitText;
  private String cancelText;

  /**
   * Constructs a new <code>ExtensibleFormDialogImplementation</code>.
   *
   * @param title      the title for the form dialog
   * @param submitText the text to display on the "submit" button
   * @param cancelText the text to display on the "cancel" button
   */
  public ExtensibleFormDialogImplementation(String title, String submitText, String cancelText)
  {
    super("implementation");

    setOutputMarkupId(true);

    this.title = title;
    this.submitText = submitText;
    this.cancelText = cancelText;
  }

  /**
   * Returns the text to display on the "cancel" button.
   *
   * @return the text to display on the "cancel" button
   */
  public String getCancelText()
  {
    return cancelText;
  }

  /**
   * Returns the extensible and reusable modal dialog this implementation is associated with.
   *
   * @return the extensible and reusable modal dialog this implementation is associated with
   */
  public ExtensibleFormDialog getDialog()
  {
    MarkupContainer parent = getParent();

    while (parent != null)
    {
      if (parent instanceof ExtensibleFormDialog)
      {
        return (ExtensibleFormDialog) parent;
      }

      parent = parent.getParent();
    }

    return null;
  }

  /**
   * Returns the text to display on the "submit" button.
   *
   * @return the text to display on the "submit" button
   */
  public String getSubmitText()
  {
    return submitText;
  }

  /**
   * Returns the title for the form dialog.
   *
   * @return the title for the form dialog
   */
  public String getTitle()
  {
    return title;
  }

  /**
   * Process the cancellation of the form associated with the dialog.
   *
   * @param target the AJAX request target
   * @param form   the form
   */
  public abstract void onCancel(AjaxRequestTarget target, Form form);

  /**
   * Process the errors for the form associated with the dialog.
   *
   * @param target the AJAX request target
   * @param form   the form
   */
  public abstract void onError(AjaxRequestTarget target, Form form);

  /**
   * Process the submission of the form associated with the dialog.
   *
   * @param target the AJAX request target
   * @param form   the form
   *
   * @return <code>true</code> if the form was submitted successfully without errors or
   *         <code>false</code> otherwise
   */
  public abstract boolean onSubmit(AjaxRequestTarget target, Form form);

  /**
   * Reset the model for the dialog.
   */
  public abstract void resetModel();

  /**
   * Registers a debug feedback message for this component.
   *
   * @param target  the AJAX request target
   * @param message the feedback message
   */
  protected final void debug(AjaxRequestTarget target, Serializable message)
  {
    getDialog().debug(target, message);
  }

  /**
   * Registers an error feedback message for this component.
   *
   * @param target  the AJAX request target
   * @param message the feedback message
   */
  protected final void error(AjaxRequestTarget target, Serializable message)
  {
    getDialog().error(target, message);
  }

  /**
   * Registers a fatal feedback message for this component.
   *
   * @param target  the AJAX request target
   * @param message the feedback message
   */
  protected final void fatal(AjaxRequestTarget target, Serializable message)
  {
    getDialog().fatal(target, message);
  }

  /**
   * Registers a warning feedback message for this component.
   *
   * @param target  the AJAX request target
   * @param message the feedback message
   */
  protected final void warn(AjaxRequestTarget target, Serializable message)
  {
    getDialog().warn(target, message);
  }
}

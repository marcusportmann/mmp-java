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

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;

/**
 * The <code>ExtensibleFormDialog</code> class provides an extensible and reusable modal dialog box
 * with a form which appears over other content. The content and behaviour of the dialog box can
 * be changed by specifying a new <code>ExtensibleFormDialogImplementation</code>.
 * <p/>
 * The dialog can be opened or closed by straight JavaScript or by a Wicket AjaxRequestTarget.
 * <p/>
 * It can optionally be closed by clicking outside the dialog.
 *
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public class ExtensibleFormDialog extends FormDialog
{
  private static final long serialVersionUID = 1000000;
  private ExtensibleFormDialogImplementation implementation;

  /**
   * Constructs a new <code>ExtensibleFormDialog</code>.
   *
   * @param id the non-null id of this component
   */
  public ExtensibleFormDialog(String id)
  {
    super(id);

    implementation = new DefaultImplementation();

    getForm().add(implementation);
  }

  /**
   * Show the dialog using Ajax.
   *
   * @param target         the AJAX request target
   * @param implementation the implementation for the extensible dialog
   */
  public void show(AjaxRequestTarget target, ExtensibleFormDialogImplementation implementation)
  {
    this.implementation.replaceWith(implementation);

    this.implementation = implementation;
    this.title = implementation.getTitle();
    this.submitText = implementation.getSubmitText();
    this.cancelText = implementation.getCancelText();

    if (target != null)
    {
      target.add(this);
    }

    super.show(target);
  }

  /**
   * Process the cancellation of the form associated with the dialog.
   *
   * @param target the AJAX request target
   * @param form   the form
   */
  @Override
  protected void onCancel(AjaxRequestTarget target, Form form)
  {
    implementation.onCancel(target, form);
  }

  /**
   * Process the errors for the form associated with the dialog.
   *
   * @param target the AJAX request target
   * @param form   the form
   */
  @Override
  protected void onError(AjaxRequestTarget target, Form form)
  {
    implementation.onError(target, form);
  }

  /**
   * Process the submission of the form associated with the dialog.
   *
   * @param target the AJAX request target
   * @param form   the form
   *
   * @return <code>true</code> if the form was submitted successfully without errors or
   *         <code>false</code> otherwise
   */
  @Override
  protected boolean onSubmit(AjaxRequestTarget target, Form form)
  {
    return implementation.onSubmit(target, form);
  }

  /**
   * Reset the model for the dialog.
   */
  @Override
  protected void resetModel()
  {
    implementation.resetModel();
  }

  class DefaultImplementation extends ExtensibleFormDialogImplementation
  {
    /**
     * Constructs a new <code>DefaultImplementation</code>.
     */
    DefaultImplementation()
    {
      super("", "OK", "Cancel");
    }

    /**
     * Process the cancellation of the form associated with the dialog.
     *
     * @param target the AJAX request target
     * @param form   the form
     */
    @Override
    public void onCancel(AjaxRequestTarget target, Form form) {}

    /**
     * Process the errors for the form associated with the dialog.
     *
     * @param target the AJAX request target
     * @param form   the form
     */
    @Override
    public void onError(AjaxRequestTarget target, Form form) {}

    /**
     * Process the submission of the form associated with the dialog.
     *
     * @param target the AJAX request target
     * @param form   the form
     *
     * @return <code>true</code> if the form was submitted successfully without errors or
     *         <code>false</code> otherwise
     */
    @Override
    public boolean onSubmit(AjaxRequestTarget target, Form form)
    {
      return true;
    }

    /**
     * Reset the model for the dialog.
     */
    @Override
    public void resetModel() {}
  }
}

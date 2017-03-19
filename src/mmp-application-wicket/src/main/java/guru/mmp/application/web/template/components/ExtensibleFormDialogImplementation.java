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

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.markup.html.form.Form;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>ExtensibleFormDialogImplementation</code> class provides the base class that all
 * form-based implementations for the <code>ExtensibleDialog</code> class should be derived from.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public abstract class ExtensibleFormDialogImplementation<T> extends ExtensibleDialogImplementation
{
  private static final long serialVersionUID = 1000000;
  private Alerts alerts;
  private Form<T> form;
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
    super(title);

    this.submitText = submitText;
    this.cancelText = cancelText;

    alerts = new Alerts("alerts", getId());
    add(alerts);

    form = new Form<>("form");
    add(form);
  }

  /**
   * Returns the form associated with the extensible dialog.
   *
   * @return the form associated with the extensible dialog
   */
  public Form<T> getForm()
  {
    return form;
  }

  /**
   * Registers a debug feedback message for this component.
   *
   * @param target  the AJAX request target
   * @param message the feedback message
   */
  protected final void debug(AjaxRequestTarget target, Serializable message)
  {
    debug(message);

    target.add(alerts);
  }

  /**
   * Registers an error feedback message for this component.
   *
   * @param target  the AJAX request target
   * @param message the feedback message
   */
  protected final void error(AjaxRequestTarget target, Serializable message)
  {
    error(message);

    target.add(alerts);
  }

  /**
   * Registers a fatal feedback message for this component.
   *
   * @param target  the AJAX request target
   * @param message the feedback message
   */
  protected final void fatal(AjaxRequestTarget target, Serializable message)
  {
    fatal(message);

    target.add(alerts);
  }

  /**
   * Returns the buttons associated with the extensible dialog.
   *
   * @return the buttons associated with the extensible dialog
   */
  @Override
  protected List<ExtensibleDialogButton> getButtons()
  {
    ExtensibleDialogButton cancelButton = new ExtensibleDialogButton(cancelText);

    cancelButton.add(new AjaxEventBehavior("click")
        {
          @Override
          protected void onEvent(AjaxRequestTarget target)
          {
            System.out.println("[DEBUG][cancelButton][onSubmit] HERE!!!!");

            ExtensibleFormDialogImplementation.this.onCancel(target, form);

            //resetExtensibleFormDialogImplementation(target);

            getDialog().hide(target);
          }
        });

    ExtensibleDialogButton submitButton = new ExtensibleDialogButton(submitText, true);

    submitButton.add(new AjaxFormSubmitBehavior(form, "click")
        {
          @Override
          protected void onSubmit(AjaxRequestTarget target)
          {
            if (target != null)
            {
              resetFeedbackMessages(target);
            }

            if (ExtensibleFormDialogImplementation.this.onSubmit(target,
                ExtensibleFormDialogImplementation.this.form))
            {
              getDialog().hide(target);
            }
            else
            {
              target.add(ExtensibleFormDialogImplementation.this.alerts);
            }
          }

          @Override
          protected void onAfterSubmit(AjaxRequestTarget target) {}

          @Override
          protected void onError(AjaxRequestTarget target)
          {
            if (target != null)
            {
              // Visit each form component and if it is visible re-render it.
              // NOTE: We have to re-render every component to remove stale validation error messages.
              form.visitFormComponents(
                  (formComponent, iVisit) ->
              {
                if ((formComponent.getParent() != null)
                    && formComponent.getParent().isVisible()
                    && formComponent.isVisible())
                {
                  target.add(formComponent);
                }
              }
              );
            }

            ExtensibleFormDialogImplementation.this.onError(target, form);
          }

          @Override
          protected void updateAjaxAttributes(AjaxRequestAttributes attributes)
          {
            super.updateAjaxAttributes(attributes);

            // Do not allow normal form submit to happen
            attributes.setPreventDefault(true);
          }

          @Override
          public boolean getDefaultProcessing()
          {
            return submitButton.getDefaultFormProcessing();
          }

          @Override
          public boolean getStatelessHint(Component component)
          {
            return false;
          }
        });
    submitButton.setDefaultFormProcessing(true);

    List<ExtensibleDialogButton> buttons = new ArrayList<>();
    buttons.add(submitButton);
    buttons.add(cancelButton);

    return buttons;
  }

  /**
   * Process the cancellation of the form associated with the extensible form dialog implementation.
   *
   * @param target the AJAX request target
   * @param form   the form
   */
  protected abstract void onCancel(AjaxRequestTarget target, Form form);

  /**
   * Process the errors for the form associated with the extensible form dialog implementation.
   *
   * @param target the AJAX request target
   * @param form   the form
   */
  protected abstract void onError(AjaxRequestTarget target, Form form);

  /**
   * Process the submission of the form associated with the extensible form dialog implementation.
   *
   * @param target the AJAX request target
   * @param form   the form
   *
   * @return <code>true</code> if the form was submitted successfully without errors or
   *         <code>false</code> otherwise
   */
  protected abstract boolean onSubmit(AjaxRequestTarget target, Form form);

  /**
   * Registers a warning feedback message for this component.
   *
   * @param target  the AJAX request target
   * @param message the feedback message
   */
  protected final void warn(AjaxRequestTarget target, Serializable message)
  {
    warn(message);

    target.add(alerts);
  }

  /**
   * Reset the extensible form dialog implementation including all forms associated with the
   * implementation, and their associated form components.
   *
   * @param target the AJAX request target
   */
  private void resetExtensibleFormDialogImplementation(AjaxRequestTarget target)
  {
    // Reset the alerts
    alerts.getFeedbackMessages().clear();

    if (target != null)
    {
      target.add(alerts);
    }

    // Reset the forms and form components
    visitChildren(
        (component, componentVisitor) ->
        {
          if (Form.class.isAssignableFrom(component.getClass()))
          {
            if (target != null)
            {
              target.add(component);
            }

            // Visit each form component and clear its input and feedback messages
            ((Form<?>) component).visitFormComponents(
                (formComponent, formComponentVisitor) ->
            {
              formComponent.getFeedbackMessages().clear();
              formComponent.clearInput();
            }
            );
          }
        }
        );
  }

  /**
   * Reset the feedback messages for all the form components for all the forms associated with the
   * extensible form dialog implementation.
   *
   * @param target the AJAX request target
   */
  private void resetFeedbackMessages(AjaxRequestTarget target)
  {
    visitChildren(
        (component, componentVisitor) ->
        {
          if (Form.class.isAssignableFrom(component.getClass()))
          {
            if (target != null)
            {
              target.add(component);
            }

            // Visit each form component and clear its input and feedback messages
            ((Form<?>) component).visitFormComponents((formComponent,
                formComponentVisitor) -> formComponent.getFeedbackMessages().clear());
          }
        }
        );
  }
}

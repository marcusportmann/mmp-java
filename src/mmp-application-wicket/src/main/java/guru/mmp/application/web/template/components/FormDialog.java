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

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.PropertyModel;

import java.io.Serializable;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>FormDialog</code> class provides a modal dialog box with a form which appears over
 * other content.
 * <p/>
 * The dialog can be opened or closed by straight JavaScript or by a Wicket AjaxRequestTarget.
 * <p/>
 * It can optionally be closed by clicking outside the dialog.
 *
 * @param <T> the model type
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public abstract class FormDialog<T> extends Dialog
{
  private static final long serialVersionUID = 1000000;
  private Alerts alerts;
  private Form<T> form;
  protected String title;
  protected String submitText;
  protected String cancelText;

  /**
   * Constructs a new <code>FormDialog</code>.
   *
   * @param id the non-null id of this component
   */
  public FormDialog(String id)
  {
    this(id, null, null, null);
  }

  /**
   * Constructs a new <code>FormDialog</code>.
   *
   * @param id         the non-null id of this component
   * @param title      the title for the form dialog
   * @param submitText the text to display on the "submit" button
   * @param cancelText the text to display on the "cancel" button
   */
  public FormDialog(String id, String title, String submitText, String cancelText)
  {
    super(id);

    this.title = title;
    this.submitText = submitText;
    this.cancelText = cancelText;

    Label titleLabel = new Label("title", new PropertyModel<String>(this, "title"));
    titleLabel.setRenderBodyOnly(true);
    add(titleLabel);

    alerts = new Alerts("alerts", id);
    add(alerts);

    form = new Form<>("form");
    add(form);

    AjaxButton submitButton = new AjaxButton("submitButton", form)
    {
      @Override
      protected void onError(AjaxRequestTarget target, Form<?> form)
      {
        super.onError(target, form);

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

        FormDialog.this.onError(target, FormDialog.this.getForm());
      }

      @Override
      protected void onSubmit(AjaxRequestTarget target, Form<?> form)
      {
        if (target != null)
        {
          resetFeedbackMessages(target);
        }

        if (FormDialog.this.onSubmit(target, FormDialog.this.getForm()))
        {
          hide(target);
        }
        else
        {
          target.add(getAlerts());
        }
      }
    };
    submitButton.setDefaultFormProcessing(true);
    add(submitButton);

    Label submitTextLabel = new Label("submitText", new PropertyModel<String>(this, "submitText"));
    submitTextLabel.setRenderBodyOnly(true);
    submitButton.add(submitTextLabel);

    AjaxLink cancelLink = new AjaxLink("cancelLink")
    {
      @Override
      public void onClick(AjaxRequestTarget target)
      {
        FormDialog.this.onCancel(target, getForm());

        hide(target);
      }
    };
    add(cancelLink);

    Label cancelTextLabel = new Label("cancelText", new PropertyModel<String>(this, "cancelText"));
    cancelTextLabel.setRenderBodyOnly(true);
    cancelLink.add(cancelTextLabel);
  }

  /**
   * Show the dialog using Ajax.
   *
   * @param target the AJAX request target
   */
  @Override
  public void show(AjaxRequestTarget target)
  {
    reset(target);

    super.show(target);
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
   * Returns the alerts.
   *
   * @return the alerts
   */
  protected Alerts getAlerts()
  {
    return alerts;
  }

  /**
   * Returns the form for the dialog.
   *
   * @return the form for the dialog
   */
  protected Form<T> getForm()
  {
    return form;
  }

  /**
   * Returns the JavaScript required to hide the dialog in the client browser.
   *
   * @return the JavaScript required to hide the dialog in the client browser
   */
  protected String getHideJavaScript()
  {
    return "$('#" + getMarkupId() + "').modal('hide')";
  }

  /**
   * Returns the JavaScript required to show the dialog in the client browser.
   *
   * @return the JavaScript required to show the dialog in the client browser
   */
  protected String getShowJavaScript()
  {
    if (!isEnabled())
    {
      return "";
    }

    return "$('#" + getMarkupId() + "').modal('show')";
  }

  /**
   * Registers an info feedback message for this component.
   *
   * @param target  the AJAX request target
   * @param message the feedback message
   */
  protected final void info(AjaxRequestTarget target, Serializable message)
  {
    info(message);

    target.add(alerts);
  }

  /**
   * Process the cancellation of the form associated with the dialog.
   *
   * @param target the AJAX request target
   * @param form   the form
   */
  protected abstract void onCancel(AjaxRequestTarget target, Form<T> form);

  /**
   * Process the errors for the form associated with the dialog.
   *
   * @param target the AJAX request target
   * @param form   the form
   */
  protected abstract void onError(AjaxRequestTarget target, Form<T> form);

  /**
   * Process the submission of the form associated with the dialog.
   *
   * @param target the AJAX request target
   * @param form   the form
   *
   * @return <code>true</code> if the form was submitted successfully without errors or
   *         <code>false</code> otherwise
   */
  protected abstract boolean onSubmit(AjaxRequestTarget target, Form<T> form);

  /**
   * Reset the dialog including all forms associated with the dialog, and their associated form
   * components.
   *
   * @param target the AJAX request target
   */
  protected void reset(AjaxRequestTarget target)
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
   * Reset the feedback messages for all the form components for all the forms associated with the
   * dialog.
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

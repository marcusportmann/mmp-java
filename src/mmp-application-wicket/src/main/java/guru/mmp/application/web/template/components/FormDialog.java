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

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;

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

    Label titleLabel = new Label("title", title);
    titleLabel.setRenderBodyOnly(true);
    add(titleLabel);

    alerts = new Alerts("alerts", id);
    add(alerts);

    form = new Form<>("form");
    add(form);

    AjaxButton submitButton = new AjaxButton("submitButton", getForm())
    {
      @Override
      protected void onError(AjaxRequestTarget target, Form<?> form)
      {
        super.onError(target, form);

        if (target != null)
        {
          // Visit each form component and if it is visible re-render it.
          // NOTE: We have to re-render every component to remove stale validation error messages.
          form.visitFormComponents(new IVisitor<FormComponent<?>, Object>()
              {
                @Override
                public void component(FormComponent<?> formComponent, IVisit<Object> iVisit)
                {
                  if ((formComponent.getParent() != null)
                      && formComponent.getParent().isVisible()
                      && formComponent.isVisible())
                  {
                    target.add(formComponent);
                  }
                }
              });
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

        FormDialog.this.onSubmit(target, FormDialog.this.getForm());
      }
    };
    submitButton.setDefaultFormProcessing(true);
    add(submitButton);

    Label submitTextLabel = new Label("submitText", submitText);
    submitTextLabel.setRenderBodyOnly(true);
    submitButton.add(submitTextLabel);

    AjaxLink cancelLink = new AjaxLink("cancelLink")
    {
      @Override
      public void onClick(AjaxRequestTarget target)
      {
        FormDialog.this.onCancel(target, getForm());

        resetDialog(target);

        hide(target);
      }
    };
    add(cancelLink);

    Label cancelTextLabel = new Label("cancelText", cancelText);
    cancelTextLabel.setRenderBodyOnly(true);
    cancelLink.add(cancelTextLabel);
  }

  /**
   * Registers a debug feedback message for this component.
   *
   * @param target  the AJAX request target
   * @param message the feedback message
   */
  public final void debug(AjaxRequestTarget target, Serializable message)
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
  public final void error(AjaxRequestTarget target, Serializable message)
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
  public final void fatal(AjaxRequestTarget target, Serializable message)
  {
    fatal(message);

    target.add(alerts);
  }

  /**
   * Returns the alerts.
   *
   * @return the alerts
   */
  public Alerts getAlerts()
  {
    return alerts;
  }

  /**
   * Returns the form for the dialog.
   *
   * @return the form for the dialog
   */
  public Form<T> getForm()
  {
    return form;
  }

  /**
   * Registers an info feedback message for this component.
   *
   * @param target  the AJAX request target
   * @param message the feedback message
   */
  public final void info(AjaxRequestTarget target, Serializable message)
  {
    info(message);

    target.add(alerts);
  }

  /**
   * Registers a warning feedback message for this component.
   *
   * @param target  the AJAX request target
   * @param message the feedback message
   */
  public final void warn(AjaxRequestTarget target, Serializable message)
  {
    warn(message);

    target.add(alerts);
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
  protected void onError(AjaxRequestTarget target, Form<T> form) {}

  /**
   * Process the submission of the form associated with the dialog.
   *
   * @param target the AJAX request target
   * @param form   the form
   */
  protected abstract void onSubmit(AjaxRequestTarget target, Form<T> form);

  /**
   * Reset the dialog including all forms associated with the dialog, and their associated form
   * components.
   *
   * @param target the AJAX request target
   */
  protected void resetDialog(AjaxRequestTarget target)
  {
    resetDialogModel();

    alerts.getFeedbackMessages().clear();

    visitChildren(new IVisitor<Component, Object>()
        {
          // Visit each component
          @Override
          public void component(Component component, IVisit<Object> iVisit)
          {
            // Is this a form?
            if (Form.class.isAssignableFrom(component.getClass()))
            {
              // Visit each form component and clear its input and feedback messages
              ((Form<?>) component).visitFormComponents(new IVisitor<FormComponent<?>, Object>()
              {
                @Override
                public void component(FormComponent<?> formComponent, IVisit<Object> iVisit)
                {
                  formComponent.clearInput();
                  formComponent.getFeedbackMessages().clear();
                }
              });
            }
          }
        });

    if (target != null)
    {
      target.add(alerts);
      target.add(form);
    }
  }

  /**
   * Reset the model for the dialog.
   */
  protected abstract void resetDialogModel();

  /**
   * Reset the feedback messages for the dialog including the feedback messages for all the forms
   * associated with the dialog, and their associated form components.
   *
   * @param target the AJAX request target
   */
  protected void resetFeedbackMessages(AjaxRequestTarget target)
  {
    visitChildren(new IVisitor<Component, Object>()
        {
          // Visit each component
          @Override
          public void component(Component component, IVisit<Object> iVisit)
          {
            // Is this a form?
            if (Form.class.isAssignableFrom(component.getClass()))
            {
              if (target != null)
              {
                target.add(component);
              }

              // Visit each form component and clear its input and feedback messages
              ((Form<?>) component).visitFormComponents(new IVisitor<FormComponent<?>, Object>()
              {
                @Override
                public void component(FormComponent<?> formComponent, IVisit<Object> iVisit)
                {
                  formComponent.getFeedbackMessages().clear();
                }
              });
            }
          }
        });
  }
}

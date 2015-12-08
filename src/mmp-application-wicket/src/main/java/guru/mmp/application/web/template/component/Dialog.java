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
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;

/**
 * The <code>Dialog</code> class provides a modal dialog box which appears over other content.
 *
 * The dialog can be opened or closed by straight JavaScript or by a Wicket AjaxRequestTarget.
 *
 * It can optionally be closed by clicking outside the dialog.
 *
 * @author Marcus Portmann
 */
public class Dialog extends WebMarkupContainer
{
  private static final long serialVersionUID = 1000000;

  /**
   * Constructs a new <code>Dialog</code>.
   *
   * @param id the non-null id of this component
   */
  public Dialog(String id)
  {
    super(id);
    setOutputMarkupId(true);
  }

  /**
   * Hide the dialog using Ajax.
   *
   * @param target the AJAX request target
   */
  public void hide(AjaxRequestTarget target)
  {
    target.appendJavaScript(getHideJavaScript());
  }

  /**
   * Show the dialog using Ajax.
   *
   * @param target the AJAX request target
   */
  public void show(AjaxRequestTarget target)
  {
    target.appendJavaScript(getShowJavaScript());
  }

  /**
   * Add a cancel link to the dialog that will reset all the forms associated with the dialog, and
   * their associated form components, and then hide the dialog.
   */
  protected void addCancelLink()
  {
    AjaxLink cancelLink = new AjaxLink("cancelLink")
    {
      @Override
      public void onClick(AjaxRequestTarget target)
      {
        // Visit each component
        Dialog.this.visitChildren(new IVisitor<Component, Object>()
        {
          @Override
          public void component(Component component, IVisit<Object> iVisit)
          {
            // Is this a form?
            if (Form.class.isAssignableFrom(component.getClass()))
            {
              // Visit each form component and clear its input and feedback messages
              ((Form) component).visitFormComponents(new IVisitor<FormComponent<?>, Object>()
              {
                @Override
                public void component(FormComponent<?> formComponent, IVisit<Object> iVisit)
                {
                  formComponent.clearInput();
                  formComponent.getFeedbackMessages().clear();
                  target.add(formComponent);
                }
              });
            }
          }
        });

        hide(target);
      }
    };
    add(cancelLink);
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
   * Reset the dialog including all forms associated with the dialog, and their associated form
   * components.
   *
   * @param target the AJAX request target
   */
  protected void resetDialog(AjaxRequestTarget target)
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
          // Visit each form component and clear its input and feedback messages
          ((Form) component).visitFormComponents(new IVisitor<FormComponent<?>, Object>()
          {
            @Override
            public void component(FormComponent<?> formComponent, IVisit<Object> iVisit)
            {
              System.out.println("Resetting form component (" + formComponent.getId()
                  + ") for form (" + component.getId() + ")");

              formComponent.clearInput();
              formComponent.getFeedbackMessages().clear();
              target.add(formComponent);
            }
          });
        }
      }
    });
  }
}

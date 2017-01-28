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

package guru.mmp.sample.web.pages.dialogs;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.web.WebApplicationException;
import guru.mmp.application.web.template.components.ExtensibleFormDialogImplementation;
import guru.mmp.application.web.template.components.TextFieldWithFeedback;
import guru.mmp.application.web.template.pages.TemplateExtensibleDialogWebPage;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;

/**
 * The <code>TestExtensibleFormDialogImplementationPage</code> class implements the
 * "Test Extensible Form Dialog Implementation" page for the web application.
 *
 * @author Marcus Portmann
 */
public class TestExtensibleFormDialogImplementationPage extends TemplateExtensibleDialogWebPage
{
  private static final long serialVersionUID = 1000000;

  /**
   * Constructs a new <code>TestExtensibleFormDialogImplementationPage</code>.
   */
  public TestExtensibleFormDialogImplementationPage()
  {
    super("Test Extensible Form Dialog Implementation",
        "The test extensible form dialog implementation");

    try
    {
      AjaxLink showDialogLink = new AjaxLink("showDialogLink")
      {
        @Override
        public void onClick(AjaxRequestTarget target)
        {
          getDialog().show(target, new TestExtensibleFormDialogImplementation());

        }
      };

      add(showDialogLink);
    }
    catch (Throwable e)
    {
      throw new WebApplicationException(
          "Failed to initialise the TestExtensibleFormDialogImplementationPage", e);
    }
  }

  private class TestExtensibleFormDialogImplementation extends ExtensibleFormDialogImplementation
  {
    private String firstName;
    private String lastName;

    /**
     * Constructs a new <code>TestExtensibleFormDialogImplementation</code>.
     */
    public TestExtensibleFormDialogImplementation()
    {
      super("Test Extensible Form Dialog", "OK", "Cancel");

      // The "firstName" field
      TextField<String> firstNameField = new TextFieldWithFeedback<>("firstName",
          new PropertyModel<>(this, "firstName"));
      firstNameField.setRequired(true);
      getForm().add(firstNameField);

      // The "lastName" field
      TextField<String> lastNameField = new TextFieldWithFeedback<>("lastName", new PropertyModel<>(
          this, "lastName"));
      lastNameField.setRequired(true);
      getForm().add(lastNameField);
    }

    @Override
    protected void onCancel(AjaxRequestTarget target, Form form)
    {
      System.out.println("[DEBUG][TestExtensibleFormDialogImplementation][onCancel] "
          + "The form submission was cancelled");
    }

    @Override
    protected void onError(AjaxRequestTarget target, Form form)
    {
      System.out.println("[DEBUG][TestExtensibleFormDialogImplementation][onError] "
          + "An error occurred while submitting the form");
    }

    @Override
    protected boolean onSubmit(AjaxRequestTarget target, Form form)
    {
      System.out.println("[DEBUG][TestExtensibleFormDialogImplementation][onSubmit] "
          + "firstName = " + firstName);
      System.out.println("[DEBUG][TestExtensibleFormDialogImplementation][onSubmit] "
          + "lastName = " + lastName);

      return true;
    }

    @Override
    protected void resetModel() {}
  }
}

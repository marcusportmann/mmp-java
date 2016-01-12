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

package guru.mmp.sample.web.page.forms;

import guru.mmp.application.web.WebApplicationException;
import guru.mmp.application.web.template.components.*;
import guru.mmp.application.web.template.pages.TemplateWebPage;
import guru.mmp.sample.model.TestData;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * The <code>TestFormPage</code> class implements the "Test Form"
 * page for the web application.
 *
 * @author Marcus Portmann
 */
public class TestFormPage
  extends TemplateWebPage
{
  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(TestFormPage.class);

  private static final long serialVersionUID = 1000000;

  /**
   * Returns the favourite pet options e.g. Dog, Cat, etc.
   *
   * @return the the favourite pet options e.g. Dog, Cat, etc
   */
  public static List<String> getFavouritePetOptions()
  {
    List<String> favouritePetOptions = new ArrayList<>();

    favouritePetOptions.add("Dog");
    favouritePetOptions.add("Cat");
    favouritePetOptions.add("Hamster");

    return favouritePetOptions;
  }

  /**
   * Returns the user title options e.g. Mr, Mrs, Ms, etc.
   *
   * @return the user title options e.g. Mr, Mrs, Ms, etc
   */
  public static List<String> getTitleOptions()
  {
    List<String> titleOptions = new ArrayList<>();

    titleOptions.add("Mr");
    titleOptions.add("Mrs");
    titleOptions.add("Ms");
    titleOptions.add("Dr");

    return titleOptions;
  }

  /**
   * Constructs a new <code>TestFormPage</code>.
   */
  public TestFormPage()
  {
    super("Test Form", "The test form");

    try
    {
      Form<TestData> testForm = new Form<>("testForm",
        new CompoundPropertyModel<>(new Model<>(new TestData())));

      // The "firstNames" field
      TextField<String> firstNamesField = new TextFieldWithFeedback<>("firstNames");
      firstNamesField.setRequired(true);
      testForm.add(firstNamesField);

      // The "lastName" field
      TextField<String> lastNameField = new TextFieldWithFeedback<>("lastName");
      lastNameField.setRequired(true);
      testForm.add(lastNameField);

      // The "title" field
      DropDownChoice<String> titleField = new DropDownChoiceWithFeedback<>("title",
        getTitleOptions());
      titleField.setRequired(true);
      testForm.add(titleField);

      // The "favouritePet" field
      DropDownChoice<String> favouritePetField = new DropDownChoiceWithFeedback<>("favouritePet",
        getFavouritePetOptions());
      favouritePetField.setRequired(true);
      testForm.add(favouritePetField);

      // The "password" field
      TextField<String> passwordField = new PasswordTextFieldWithFeedback("password");
      passwordField.setRequired(true);
      testForm.add(passwordField);

      // The "confirmPassword" field
      TextField<String> confirmPasswordField = new PasswordTextFieldWithFeedback("confirmPassword");
      confirmPasswordField.setRequired(true);
      testForm.add(confirmPasswordField);

      // The "notes" field
      TextArea<String> notesField = new TextAreaWithFeedback<>("notes");
      notesField.setRequired(true);
      testForm.add(notesField);

      // The "role" field
      RadioGroup<String> roleField = new RadioGroupWithFeedback<>("role");
      roleField.setRequired(true);
      testForm.add(roleField);

      roleField.add(new Radio<>("roleManager", new Model<>("MANAGER")));
      roleField.add(new Radio<>("roleSupervisor", new Model<>("SUPERVISOR")));
      roleField.add(new Radio<>("roleWorker", new Model<>("WORKER")));

      // The "isActive" field
      CheckBox isActiveField = new CheckBox("isActive");
      testForm.add(isActiveField);

      // The "okButton" button
      Button okButton = new Button("okButton")
      {
        private static final long serialVersionUID = 1000000;

        @Override
        public void onSubmit()
        {
          try
          {
            // setResponsePage(previousPage.getPage());
          }
          catch (Throwable e)
          {
            logger.error("Failed to process the test form submission: " + e.getMessage(), e);
            error("Your request could not be processed at this time.");
            error("Please contact your administrator.");
          }
        }
      };

      okButton.setDefaultFormProcessing(true);
      testForm.add(okButton);

      Button cancelButton = new Button("cancelButton")
      {
        private static final long serialVersionUID = 1000000;

        @Override
        public void onSubmit()
        {
          // setResponsePage(previousPage.getPage());
        }
      };

      cancelButton.setDefaultFormProcessing(false);
      testForm.add(cancelButton);

      add(testForm);
    }
    catch (Throwable e)
    {
      throw new WebApplicationException("Failed to initialise the TestFormPage", e);
    }
  }

  /**
   * Render to the web response whatever the component wants to contribute to the head section.
   *
   * @param response the header response
   */
  @Override
  public void renderHead(IHeaderResponse response)
  {
    super.renderHead(response);
  }
}

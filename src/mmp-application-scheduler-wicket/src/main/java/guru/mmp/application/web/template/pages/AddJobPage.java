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

package guru.mmp.application.web.template.pages;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.scheduler.ISchedulerService;
import guru.mmp.application.scheduler.Job;
import guru.mmp.application.web.WebApplicationException;
import guru.mmp.application.web.WebSession;
import guru.mmp.application.web.pages.WebPageSecurity;
import guru.mmp.application.web.template.TemplateSchedulerSecurity;
import guru.mmp.application.web.template.components.DropDownChoiceWithFeedback;
import guru.mmp.application.web.template.components.JobStatusChoiceRenderer;
import guru.mmp.application.web.template.components.TextFieldWithFeedback;

import org.apache.wicket.PageReference;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//~--- JDK imports ------------------------------------------------------------

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

/**
 * The <code>AddJobPage</code> class implements the "Add Job"
 * page for the Web Application Template.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("CdiManagedBeanInconsistencyInspection")
@WebPageSecurity(TemplateSchedulerSecurity.FUNCTION_CODE_SCHEDULER_ADMINISTRATION)
public class AddJobPage extends TemplateWebPage
{
  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(AddJobPage.class);
  private static final long serialVersionUID = 1000000;

  /* Scheduler Service */
  @Inject
  private ISchedulerService schedulerService;

  /**
   * Constructs a new <code>AddJobPage</code>.
   *
   * @param previousPage the previous page
   */
  public AddJobPage(PageReference previousPage)
  {
    super("Add Process Definition");

    try
    {
      Form<Job> addForm = new Form<>("addForm", new CompoundPropertyModel<>(new Model<>(
          new Job())));

      addForm.getModelObject().setId(UUID.randomUUID());

      // The "id" field
      TextField<String> idField = new TextFieldWithFeedback<>("id");
      idField.setRequired(true);
      addForm.add(idField);

      // The "name" field
      TextField<String> nameField = new TextFieldWithFeedback<>("name");
      nameField.setRequired(true);
      addForm.add(nameField);

      // The "schedulingPattern" field
      TextField<String> schedulingPatternField = new TextFieldWithFeedback<>("schedulingPattern");
      schedulingPatternField.setRequired(true);
      addForm.add(schedulingPatternField);

      // The "jobClass" field
      TextField<String> jobClassField = new TextFieldWithFeedback<>("jobClass");
      jobClassField.setRequired(true);
      addForm.add(jobClassField);

      // The "status" field
      DropDownChoice<Job.Status> statusField = new DropDownChoiceWithFeedback<>("status",
          getJobStatusOptions(), new JobStatusChoiceRenderer());
      statusField.setRequired(true);
      addForm.add(statusField);

      // The "addButton" button
      Button addButton = new Button("addButton")
      {
        private static final long serialVersionUID = 1000000;

        @Override
        public void onSubmit()
        {
          FileUpload fileUpload = null;

          try
          {
            Job job = addForm.getModelObject();

            schedulerService.createJob(job);

            setResponsePage(previousPage.getPage());
          }
          catch (Throwable e)
          {
            logger.error("Failed to add the job: " + e.getMessage(), e);
            error("Your request could not be processed at this time.");
            error("Please contact your administrator.");
          }
        }
      };

      addButton.setDefaultFormProcessing(true);
      addForm.add(addButton);

      Button cancelButton = new Button("cancelButton")
      {
        private static final long serialVersionUID = 1000000;

        @Override
        public void onSubmit()
        {
          setResponsePage(previousPage.getPage());
        }
      };

      cancelButton.setDefaultFormProcessing(false);
      addForm.add(cancelButton);

      add(addForm);
    }
    catch (Throwable e)
    {
      throw new WebApplicationException("Failed to initialise the AddJobPage", e);
    }
  }

  private List<Job.Status> getJobStatusOptions()
  {
    List<Job.Status> list = new ArrayList<>();

    list.add(Job.Status.UNSCHEDULED);
    list.add(Job.Status.ONCE_OFF);

    return list;
  }
}

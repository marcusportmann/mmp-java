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

package guru.mmp.application.web.template.pages;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.scheduler.ISchedulerService;
import guru.mmp.application.scheduler.Job;
import guru.mmp.application.web.WebApplicationException;
import guru.mmp.application.web.pages.WebPageSecurity;
import guru.mmp.application.web.template.TemplateSchedulerSecurity;
import guru.mmp.application.web.template.components.DropDownChoiceWithFeedback;
import guru.mmp.application.web.template.components.JobStatusChoiceRenderer;
import guru.mmp.application.web.template.components.TextFieldWithFeedback;

import org.apache.wicket.PageReference;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

/**
 * The <code>UpdateJobPage</code> class implements the "Update Job"
 * page for the Web Application Template.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("CdiManagedBeanInconsistencyInspection")
@WebPageSecurity(TemplateSchedulerSecurity.FUNCTION_CODE_SCHEDULER_ADMINISTRATION)
public class UpdateJobPage extends TemplateWebPage
{
  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(UpdateJobPage.class);
  private static final long serialVersionUID = 1000000;

  /* Scheduler Service */
  @Inject
  private ISchedulerService schedulerService;

  /**
   * Constructs a new <code>UpdateJobPage</code>.
   *
   * @param previousPage the previous page
   * @param jobModel     the model for the job
   */
  public UpdateJobPage(PageReference previousPage, IModel<Job> jobModel)
  {
    super("Update Job");

    try
    {
      Form<Job> updateForm = new Form<>("updateForm", new CompoundPropertyModel<>(jobModel));

      // The "id" field
      TextField<String> idField = new TextFieldWithFeedback<>("id");
      idField.setRequired(true);
      idField.setEnabled(false);
      updateForm.add(idField);

      // The "name" field
      TextField<String> nameField = new TextFieldWithFeedback<>("name");
      nameField.setRequired(true);
      updateForm.add(nameField);

      // The "schedulingPattern" field
      TextField<String> schedulingPatternField = new TextFieldWithFeedback<>("schedulingPattern");
      schedulingPatternField.setRequired(true);
      updateForm.add(schedulingPatternField);

      // The "jobClass" field
      TextField<String> jobClassField = new TextFieldWithFeedback<>("jobClass");
      jobClassField.setRequired(true);
      updateForm.add(jobClassField);

      // The "status" field
      List<Job.Status> statusOptions = getStatusOptions(updateForm.getModelObject().getStatus());

      DropDownChoice<Job.Status> statusField = new DropDownChoiceWithFeedback<>("status",
          statusOptions, new JobStatusChoiceRenderer());
      statusField.setRequired(true);
      statusField.setEnabled(statusOptions.size() > 1);
      updateForm.add(statusField);

      // The "isEnabled" field
      CheckBox isEnabledField = new CheckBox("isEnabled");
      updateForm.add(isEnabledField);

      // The "updateButton" button
      Button updateButton = new Button("updateButton")
      {
        private static final long serialVersionUID = 1000000;

        @Override
        public void onSubmit()
        {
          try
          {
            Job job = updateForm.getModelObject();

            // schedulerService.updateJob(job);

            setResponsePage(previousPage.getPage());
          }
          catch (Throwable e)
          {
            logger.error("Failed to update the job: " + e.getMessage(), e);
            error("Your request could not be processed at this time.");
            error("Please contact your administrator.");
          }
        }
      };

      updateButton.setDefaultFormProcessing(true);
      updateForm.add(updateButton);

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
      updateForm.add(cancelButton);

      add(updateForm);
    }
    catch (Throwable e)
    {
      throw new WebApplicationException("Failed to initialise the UpdateJobPage", e);
    }
  }

  private List<Job.Status> getStatusOptions(Job.Status status)
  {
    List<Job.Status> list = new ArrayList<>();

    if (status == Job.Status.UNSCHEDULED)
    {
      list.add(Job.Status.UNSCHEDULED);
      list.add(Job.Status.ONCE_OFF);
    }
    else if (status == Job.Status.SCHEDULED)
    {
      list.add(Job.Status.UNSCHEDULED);
      list.add(Job.Status.SCHEDULED);
      list.add(Job.Status.ONCE_OFF);
    }
    else if (status == Job.Status.EXECUTING)
    {
      list.add(Job.Status.EXECUTING);
    }
    else if (status == Job.Status.EXECUTED)
    {
      list.add(Job.Status.UNSCHEDULED);
      list.add(Job.Status.EXECUTED);
      list.add(Job.Status.ONCE_OFF);
    }
    else if (status == Job.Status.ABORTED)
    {
      list.add(Job.Status.UNSCHEDULED);
      list.add(Job.Status.ABORTED);
      list.add(Job.Status.ONCE_OFF);
    }
    else if (status == Job.Status.FAILED)
    {
      list.add(Job.Status.UNSCHEDULED);
      list.add(Job.Status.FAILED);
      list.add(Job.Status.ONCE_OFF);
    }
    else if (status == Job.Status.ONCE_OFF)
    {
      list.add(Job.Status.UNSCHEDULED);
      list.add(Job.Status.ONCE_OFF);
    }
    else if (status == Job.Status.UNKNOWN)
    {
      list.add(Job.Status.UNSCHEDULED);
      list.add(Job.Status.ONCE_OFF);
      list.add(Job.Status.UNKNOWN);
    }

    return list;
  }
}

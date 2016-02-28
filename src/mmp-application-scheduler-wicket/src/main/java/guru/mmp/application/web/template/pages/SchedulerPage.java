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
import guru.mmp.application.web.converters.ISO8601Converter;
import guru.mmp.application.web.pages.WebPageSecurity;
import guru.mmp.application.web.template.TemplateSchedulerSecurity;
import guru.mmp.application.web.template.components.Dialog;
import guru.mmp.application.web.template.components.PagingNavigator;
import guru.mmp.application.web.template.data.FilteredJobDataProvider;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.datetime.markup.html.basic.DateLabel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.ReuseIfModelsEqualStrategy;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import org.apache.wicket.util.convert.IConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

/**
 * The <code>SchedulerPage</code> class implements the
 * "Scheduler" page for the Web Application Template.
 *
 * @author Marcus Portmann
 */
@WebPageSecurity(TemplateSchedulerSecurity.FUNCTION_CODE_SCHEDULER_ADMINISTRATION)
public class SchedulerPage extends TemplateWebPage
{
  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(SchedulerPage.class);
  private static final long serialVersionUID = 1000000;

  /* Scheduler Service */
  @Inject
  private ISchedulerService schedulerService;

  /**
   * Constructs a new <code>SchedulerPage</code>.
   */
  public SchedulerPage()
  {
    super("Scheduler");

    try
    {
      /*
       * The table container, which allows the table and its associated navigator to be updated
       * using AJAX.
       */
      WebMarkupContainer tableContainer = new WebMarkupContainer("tableContainer");
      tableContainer.setOutputMarkupId(true);
      add(tableContainer);

      // The dialog used to confirm the removal of a job
      RemoveDialog removeDialog = new RemoveDialog(tableContainer);
      add(removeDialog);

      // The "addLink" used to add a new job
      Link<Void> addLink = new Link<Void>("addLink")
      {
        private static final long serialVersionUID = 1000000;

        @Override
        public void onClick()
        {
          AddJobPage page = new AddJobPage(getPageReference());

          setResponsePage(page);
        }
      };
      tableContainer.add(addLink);

      FilteredJobDataProvider dataProvider = new FilteredJobDataProvider();

      // The "filterForm" form
      Form<Void> filterForm = new Form<>("filterForm");
      filterForm.setMarkupId("filterForm");
      filterForm.setOutputMarkupId(true);

      // The "filter" field
      TextField<String> filterField = new TextField<>("filter", new PropertyModel<>(dataProvider,
          "filter"));
      filterForm.add(filterField);

      // The "filterButton" button
      Button filterButton = new Button("filterButton")
      {
        private static final long serialVersionUID = 1000000;

        @Override
        public void onSubmit() {}
      };
      filterButton.setDefaultFormProcessing(true);
      filterForm.add(filterButton);

      // The "resetButton" button
      Button resetButton = new Button("resetButton")
      {
        private static final long serialVersionUID = 1000000;

        @Override
        public void onSubmit()
        {
          dataProvider.setFilter("");
        }
      };
      filterForm.add(resetButton);

      tableContainer.add(filterForm);

      // The job data view
      DataView<Job> dataView = new DataView<Job>("job", dataProvider)
      {
        private static final long serialVersionUID = 1000000;

        @Override
        protected void populateItem(Item<Job> item)
        {
          item.add(new Label("name", new PropertyModel<String>(item.getModel(), "name")));
          item.add(new Label("jobClass", new PropertyModel<String>(item.getModel(), "jobClass")));
          item.add(DateLabel.forDatePattern("nextExecution", new PropertyModel<Date>(item.getModel(), "nextExecution"), "YYYY-MM-dd hh:mm a"));

          // The "parametersLink" link
          Link<Void> parametersLink = new Link<Void>("parametersLink")
          {
            private static final long serialVersionUID = 1000000;

            @Override
            public void onClick()
            {
              Job job = item.getModelObject();

//            JobParametersPage page = new JobParametersPage(getPageReference(), job.getId());
//
//            setResponsePage(page);
            }
          };
          item.add(parametersLink);

          // The "updateLink" link
          Link<Void> updateLink = new Link<Void>("updateLink")
          {
            private static final long serialVersionUID = 1000000;

            @Override
            public void onClick()
            {
              Job job = item.getModelObject();

//            UpdateJobPage page = new UpdateJobPage(getPageReference(), item.getModel());
//
//            setResponsePage(page);
            }
          };
          item.add(updateLink);

          // The "removeLink" link
          AjaxLink<Void> removeLink = new AjaxLink<Void>("removeLink")
          {
            private static final long serialVersionUID = 1000000;

            @Override
            public void onClick(AjaxRequestTarget target)
            {
              Job job = item.getModelObject();

              if (job != null)
              {
                removeDialog.show(target, job);
              }
              else
              {
                target.add(tableContainer);
              }
            }
          };
          item.add(removeLink);
        }
      };
      dataView.setItemsPerPage(10);
      dataView.setItemReuseStrategy(ReuseIfModelsEqualStrategy.getInstance());
      tableContainer.add(dataView);

      tableContainer.add(new PagingNavigator("navigator", dataView));
    }
    catch (Throwable e)
    {
      throw new WebApplicationException("Failed to initialise the SchedulerPage", e);
    }
  }

  /**
   * The <code>RemoveDialog</code> class implements a dialog that allows the removal
   * of a job to be confirmed.
   */
  private class RemoveDialog extends Dialog
  {
    private static final long serialVersionUID = 1000000;
    private Label nameLabel;
    private UUID id;

    /**
     * Constructs a new <code>RemoveDialog</code>.
     *
     * @param tableContainer the table container, which allows the job table and its
     *                       associated navigator to be updated using AJAX
     */
    public RemoveDialog(WebMarkupContainer tableContainer)
    {
      super("removeDialog");

      nameLabel = new Label("name", Model.of(""));

      nameLabel.setOutputMarkupId(true);
      add(nameLabel);

      add(new AjaxLink<Void>("removeLink")
          {
            private static final long serialVersionUID = 1000000;

            @Override
            public void onClick(AjaxRequestTarget target)
            {
              try
              {
                // schedulerService.deleteJob(id);

                target.add(tableContainer);

                SchedulerPage.this.info("Successfully removed the job "
                    + nameLabel.getDefaultModelObjectAsString());
              }
              catch (Throwable e)
              {
                logger.error(String.format("Failed to remove the job (%s): %s", id,
                    e.getMessage()), e);

                SchedulerPage.this.error("Failed to remove the job "
                    + nameLabel.getDefaultModelObjectAsString());
              }

              target.add(getAlerts());

              hide(target);
            }
          });
    }

    /**
     * Show the dialog using Ajax.
     *
     * @param target the AJAX request target
     * @param job    the job being removed
     */
    public void show(AjaxRequestTarget target, Job job)
    {
      this.id = job.getId();

      this.nameLabel.setDefaultModelObject(job.getName());

      target.add(nameLabel);

      super.show(target);
    }
  }
}

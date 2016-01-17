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

package guru.mmp.application.batch;

//~--- non-JDK imports --------------------------------------------------------

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//~--- JDK imports ------------------------------------------------------------

import java.util.concurrent.Future;

import javax.annotation.PostConstruct;

import javax.ejb.*;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.inject.Inject;

/**
 * The <code>BackgroundJobExecutor</code> class implements the Background Job Executor.
 *
 * @author Marcus Portmann
 */
@ApplicationScoped
@Default
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
@TransactionManagement(TransactionManagementType.BEAN)
public class BackgroundJobExecutor
{
  /* Logger */
  private static Logger logger = LoggerFactory.getLogger(BackgroundJobExecutor.class);

  /* Batch Service */
  @Inject
  private IBatchService batchService;

  /**
   * Execute all the jobs scheduled for execution.
   *
   * @return <code>true</code> if the jobs were executed successfully or <code>false</code>
   * otherwise
   */
  @Asynchronous
  public Future<Boolean> execute()
  {
    // If CDI injection was not completed successfully for the bean then stop here
    if (batchService == null)
    {
      logger.error("Failed to execute the jobs: The Batch Service was NOT injected");

      return new AsyncResult<>(false);
    }

    try
    {
      executeJobs();

      return new AsyncResult<>(true);
    }
    catch (Throwable e)
    {
      logger.error("Failed to execute the jobs", e);

      return new AsyncResult<>(false);
    }
  }

  /**
   * Initialise the Background Job Executor.
   */
  @PostConstruct
  public void init()
  {
    logger.info("Initialising the Background Job Executor");

    if (batchService != null)
    {
      /*
       * Reset any locks for jobs that were previously being executed.
       */
      try
      {
        logger.info("Resetting the locks for the jobs being executed");

        batchService.resetJobLocks(Job.Status.EXECUTING, Job.Status.SCHEDULED);
      }
      catch (Throwable e)
      {
        logger.error("Failed to reset the locks for the jobs being executed", e);
      }
    }
    else
    {
      logger.error("Failed to initialise the Background Job Executor: "
          + "The Batch Service was NOT injected");
    }
  }

  private void executeJobs()
  {
    Job job;

    while (true)
    {
      // Retrieve the next job scheduled for execution
      try
      {
        job = batchService.getNextJobScheduledForExecution();

        if (job == null)
        {
          if (logger.isDebugEnabled())
          {
            logger.debug("No jobs scheduled for execution");
          }

          // Schedule any unscheduled jobs
          while (batchService.scheduleNextUnscheduledJobForExecution()) {}

          return;
        }
      }
      catch (Throwable e)
      {
        logger.error("Failed to retrieve the next job scheduled for execution", e);

        return;
      }

      // Execute the job
      try
      {
        if (logger.isDebugEnabled())
        {
          logger.debug(String.format("Executing the job (%s)", job.getId()));
        }

        batchService.executeJob(job);

        // Reschedule the job
        try
        {
          batchService.rescheduleJob(job.getId(), job.getSchedulingPattern());

          try
          {
            batchService.unlockJob(job.getId(), Job.Status.SCHEDULED);
          }
          catch (Throwable f)
          {
            logger.error(String.format(
                "Failed to unlock and set the status for the job (%s) to \"Scheduled\"",
                job.getId()), f);
          }
        }
        catch (Throwable e)
        {
          logger.warn(String.format(
              "The job (%s) could not be rescheduled and will be marked as \"Failed\"",
              job.getId()));

          try
          {
            batchService.unlockJob(job.getId(), Job.Status.FAILED);
          }
          catch (Throwable f)
          {
            logger.error(String.format(
                "Failed to unlock and set the status for the job (%s) to \"Failed\"", job.getId()),
                f);
          }
        }
      }
      catch (Throwable e)
      {
        logger.error(String.format("Failed to execute the job (%s)", job.getId()), e);

        // Increment the execution attempts for the job
        try
        {
          batchService.incrementJobExecutionAttempts(job.getId());

          job.setExecutionAttempts(job.getExecutionAttempts() + 1);
        }
        catch (Throwable f)
        {
          logger.error(String.format("Failed to increment the execution attempts for the job (%s)",
              job.getId()), f);
        }

        try
        {
          /*
           * If the job has exceeded the maximum number of execution attempts then
           * unlock it and set its status to "Failed" otherwise unlock it and set its status to
           * "Scheduled".
           */
          if (job.getExecutionAttempts() >= batchService.getMaximumJobExecutionAttempts())
          {
            logger.warn(String.format(
                "The job (%s) has exceeded the maximum  number of execution attempts and will be "
                + "marked as \"Failed\"", job.getId()));

            batchService.unlockJob(job.getId(), Job.Status.FAILED);
          }
          else
          {
            batchService.unlockJob(job.getId(), Job.Status.SCHEDULED);
          }
        }
        catch (Throwable f)
        {
          logger.error(String.format("Failed to unlock and set the status for the job (%s)",
              job.getId()), f);
        }
      }
    }
  }
}

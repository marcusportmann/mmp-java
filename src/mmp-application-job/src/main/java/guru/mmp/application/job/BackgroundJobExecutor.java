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

package guru.mmp.application.job;

//~--- non-JDK imports --------------------------------------------------------

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//~--- JDK imports ------------------------------------------------------------

import java.util.concurrent.Future;

import javax.annotation.PostConstruct;

import javax.ejb.*;

import javax.inject.Inject;

/**
 * The <code>BackgroundJobExecutor</code> class implements the Background Job Executor.
 *
 * @author Marcus Portmann
 */
@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
@TransactionManagement(TransactionManagementType.BEAN)
public class BackgroundJobExecutor
{
  /* Logger */
  private static Logger logger = LoggerFactory.getLogger(BackgroundJobExecutor.class);

  /* Job Service */
  @Inject
  private IJobService jobService;

  /**
   * Execute all the jobs scheduled for execution.
   *
   * @return <code>true</code> if the jobs were executed successfully or <code>false</code>
   *         otherwise
   */
  @Asynchronous
  public Future<Boolean> execute()
  {
    // If CDI injection was not completed successfully for the bean then stop here
    if (jobService == null)
    {
      logger.error("Failed to execute the jobs:" + " The Job Service was NOT injected");

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

    if (jobService != null)
    {
      /*
       * Reset any locks for jobs that were previously being executed.
       */
      try
      {
        logger.info("Resetting the locks for the jobs being executed");

        jobService.resetJobLocks(Job.Status.EXECUTING, Job.Status.SCHEDULED);
      }
      catch (Throwable e)
      {
        logger.error("Failed to reset the locks for the jobs being executed", e);
      }

    }
    else
    {
      logger.error("Failed to initialise the Background Job Executor:"
          + " The Job Service was NOT injected");
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
        job = jobService.getNextJobScheduledForExecution();

        if (job == null)
        {
          if (logger.isDebugEnabled())
          {
            logger.debug("No jobs scheduled for execution");
          }

          // Schedule any unscheduled jobs
          while (jobService.scheduleNextUnscheduledJobForExecution()) {}

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
          logger.debug("Executing the job (" + job.getId() + ")");
        }

        jobService.executeJob(job);

        // Reschedule the job
        try
        {
          jobService.rescheduleJob(job.getId(), job.getSchedulingPattern());

          try
          {
            jobService.unlockJob(job.getId(), Job.Status.SCHEDULED);
          }
          catch (Throwable f)
          {
            logger.error("Failed to unlock and set the status for the job (" + job.getId()
                + ") to \"Scheduled\"", f);
          }
        }
        catch (Throwable e)
        {
          logger.warn("The job (" + job.getId()
              + ") could not be rescheduled and will be marked as \"Failed\"");

          try
          {
            jobService.unlockJob(job.getId(), Job.Status.FAILED);
          }
          catch (Throwable f)
          {
            logger.error("Failed to unlock and set the status for the job (" + job.getId()
                + ") to \"Failed\"", f);
          }
        }
      }
      catch (Throwable e)
      {
        logger.error("Failed to execute the job (" + job.getId() + ")", e);

        // Increment the execution attempts for the job
        try
        {
          jobService.incrementJobExecutionAttempts(job.getId());

          job.setExecutionAttempts(job.getExecutionAttempts() + 1);
        }
        catch (Throwable f)
        {
          logger.error("Failed to increment the execution attempts for the job (" + job.getId()
              + ")", f);
        }

        try
        {
          /*
           * If the job has exceeded the maximum number of execution attempts then
           * unlock it and set its status to "Failed" otherwise unlock it and set its status to
           * "Scheduled".
           */
          if (job.getExecutionAttempts() >= jobService.getMaximumJobExecutionAttempts())
          {
            logger.warn("The job (" + job.getId() + ") has exceeded the maximum "
                + " number of execution attempts and will be marked as \"Failed\"");

            jobService.unlockJob(job.getId(), Job.Status.FAILED);
          }
          else
          {
            jobService.unlockJob(job.getId(), Job.Status.SCHEDULED);
          }
        }
        catch (Throwable f)
        {
          logger.error("Failed to unlock and set the status for the job (" + job.getId() + ")", f);
        }
      }
    }
  }
}

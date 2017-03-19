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

package guru.mmp.application.scheduler;

//~--- non-JDK imports --------------------------------------------------------

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The <code>JobExecutorThread</code> class.
 *
 * @author Marcus Portmann
 */
public class JobExecutor
  implements Runnable
{
  /* Logger */
  private static Logger logger = LoggerFactory.getLogger(JobExecutor.class);
  private ISchedulerService schedulerService;
  private Job job;

  /**
   * Constructs a new <code>JobExecutorThread</code>.
   *
   * @param schedulerService the Scheduler Service
   * @param job              the job
   */
  public JobExecutor(ISchedulerService schedulerService, Job job)
  {
    this.schedulerService = schedulerService;
    this.job = job;
  }

  @Override
  public void run()
  {
    try
    {
      if (logger.isDebugEnabled())
      {
        logger.debug(String.format("Executing the job (%s)", job.getId()));
      }

      schedulerService.executeJob(job);

      // Reschedule the job
      try
      {
        schedulerService.rescheduleJob(job.getId(), job.getSchedulingPattern());

        try
        {
          schedulerService.unlockJob(job.getId(), Job.Status.SCHEDULED);
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
            "The job (%s) could not be rescheduled and will be marked as \"Failed\"", job.getId()));

        try
        {
          schedulerService.unlockJob(job.getId(), Job.Status.FAILED);
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
        schedulerService.incrementJobExecutionAttempts(job.getId());

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
        if (job.getExecutionAttempts() >= schedulerService.getMaximumJobExecutionAttempts())
        {
          logger.warn(String.format(
              "The job (%s) has exceeded the maximum  number of execution attempts and will be "
              + "marked as \"Failed\"", job.getId()));

          schedulerService.unlockJob(job.getId(), Job.Status.FAILED);
        }
        else
        {
          schedulerService.unlockJob(job.getId(), Job.Status.SCHEDULED);
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

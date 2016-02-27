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

package guru.mmp.application.scheduler;

//~--- non-JDK imports --------------------------------------------------------

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//~--- JDK imports ------------------------------------------------------------

import java.util.concurrent.Future;

import javax.annotation.PostConstruct;

import javax.ejb.*;

import javax.enterprise.concurrent.ManagedThreadFactory;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;

import javax.inject.Inject;

import javax.naming.InitialContext;

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

  /* Scheduler Service */
  @Inject
  private ISchedulerService schedulerService;

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
    if (schedulerService == null)
    {
      logger.error("Failed to execute the jobs: The Scheduler Service was NOT injected");

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

    if (schedulerService != null)
    {
      /*
       * Reset any locks for jobs that were previously being executed.
       */
      try
      {
        logger.info("Resetting the locks for the jobs being executed");

        schedulerService.resetJobLocks(Job.Status.EXECUTING, Job.Status.SCHEDULED);
      }
      catch (Throwable e)
      {
        logger.error("Failed to reset the locks for the jobs being executed", e);
      }
    }
    else
    {
      logger.error("Failed to initialise the Background Job Executor: "
          + "The Scheduler Service was NOT injected");
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
        job = schedulerService.getNextJobScheduledForExecution();

        if (job == null)
        {
          if (logger.isDebugEnabled())
          {
            logger.debug("No jobs scheduled for execution");
          }

          // Schedule any unscheduled jobs
          while (schedulerService.scheduleNextUnscheduledJobForExecution()) {}

          return;
        }
      }
      catch (Throwable e)
      {
        logger.error("Failed to retrieve the next job scheduled for execution", e);

        return;
      }

      ManagedThreadFactory managedThreadFactory = getManagedThreadFactory();

      // Execute the job in a new thread
      if (managedThreadFactory != null)
      {
        managedThreadFactory.newThread(new JobExecutor(schedulerService, job)).start();
      }

      // Execute the job in the current thread
      else
      {
        new JobExecutor(schedulerService, job).run();
      }
    }
  }

  private ManagedThreadFactory getManagedThreadFactory()
  {
    try
    {
      return InitialContext.doLookup("java:comp/DefaultManagedThreadFactory");
    }
    catch (Throwable e)
    {
      return null;
    }
  }
}

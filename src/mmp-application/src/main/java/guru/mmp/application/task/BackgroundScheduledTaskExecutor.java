/*
 * Copyright 2014 Marcus Portmann
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

package guru.mmp.application.task;

//~--- non-JDK imports --------------------------------------------------------

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.ejb.*;
import javax.inject.Inject;
import java.util.concurrent.Future;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>BackgroundScheduledTaskExecutor</code> class implements the background scheduled
 * task executor.
 *
 * @author Marcus Portmann
 */
@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
@TransactionManagement(TransactionManagementType.BEAN)
public class BackgroundScheduledTaskExecutor
{
  /* Logger */
  private static Logger logger = LoggerFactory.getLogger(BackgroundScheduledTaskExecutor.class);

  /* Task Service */
  @Inject
  private ITaskService taskService;

  /**
   * Execute all the tasks scheduled for execution.
   *
   * @return <code>true</code> if the scheduled tasks were executed successfully or
   *         <code>false</code> otherwise
   */
  @Asynchronous
  public Future<Boolean> execute()
  {
    // If CDI injection was not completed successfully for the bean then stop here
    if (taskService == null)
    {
      logger.error("Failed to execute the scheduled tasks:"
          + " The TaskService instance was NOT injected");

      return new AsyncResult<>(false);
    }

    try
    {
      executeScheduledTasks();

      return new AsyncResult<>(true);
    }
    catch (Throwable e)
    {
      logger.error("Failed to execute the scheduled tasks", e);

      return new AsyncResult<>(false);
    }
  }

  /**
   * Initialise the background scheduled task executor.
   */
  @PostConstruct
  public void init()
  {
    logger.info("Initialising the background scheduled task executor");

    if (taskService != null)
    {
      /*
       * Reset any locks for scheduled tasks that were previously being executed.
       */
      try
      {
        logger.info("Resetting the locks for the scheduled tasks being executed");

        taskService.resetScheduledTaskLocks(ScheduledTaskStatus.EXECUTING,
            ScheduledTaskStatus.SCHEDULED);
      }
      catch (Throwable e)
      {
        logger.error("Failed to reset the locks for the scheduled tasks being executed", e);
      }

    }
    else
    {
      logger.error("Failed to initialise the background scheduled task executor:"
          + " The TaskService instance was NOT injected");
    }
  }

  private void executeScheduledTasks()
  {
    ScheduledTask scheduledTask;

    while (true)
    {
      // Retrieve the next task scheduled for execution
      try
      {
        scheduledTask = taskService.getNextTaskScheduledForExecution();

        if (scheduledTask == null)
        {
          if (logger.isDebugEnabled())
          {
            logger.debug("No tasks scheduled for execution");
          }

          // Schedule any unscheduled tasks
          while (taskService.scheduleNextUnscheduledTaskForExecution()) {}

          return;
        }
      }
      catch (Throwable e)
      {
        logger.error("Failed to retrieve the next task scheduled for execution", e);

        return;
      }

      // Execute the scheduled task
      try
      {
        if (logger.isDebugEnabled())
        {
          logger.debug("Executing the schedule task (" + scheduledTask.getId() + ")");
        }

        taskService.executeScheduledTask(scheduledTask);

        // Reschedule the task
        try
        {
          taskService.rescheduleTask(scheduledTask.getId(), scheduledTask.getSchedulingPattern());

          try
          {
            taskService.unlockScheduledTask(scheduledTask.getId(), ScheduledTaskStatus.SCHEDULED);
          }
          catch (Throwable f)
          {
            logger.error("Failed to unlock and set the status for the scheduled task ("
                + scheduledTask.getId() + ") to \"Scheduled\"", f);
          }
        }
        catch (Throwable e)
        {
          logger.warn("The scheduled task (" + scheduledTask.getId()
              + ") could not be rescheduled and will be marked as \"Failed\"");

          try
          {
            taskService.unlockScheduledTask(scheduledTask.getId(), ScheduledTaskStatus.FAILED);
          }
          catch (Throwable f)
          {
            logger.error("Failed to unlock and set the status for the scheduled task ("
                + scheduledTask.getId() + ") to \"Failed\"", f);
          }
        }
      }
      catch (Throwable e)
      {
        logger.error("Failed to execute the scheduled task (" + scheduledTask.getId() + ")", e);

        // Increment the execution attempts for the scheduled task
        try
        {
          taskService.incrementScheduledTaskExecutionAttempts(scheduledTask.getId());

          scheduledTask.setExecutionAttempts(scheduledTask.getExecutionAttempts() + 1);
        }
        catch (Throwable f)
        {
          logger.error("Failed to increment the execution attempts for the scheduled task ("
              + scheduledTask.getId() + ")", f);
        }

        try
        {
          /*
           * If the scheduled task has exceeded the maximum number of execution attempts then
           * unlock it and set its status to "Failed" otherwise unlock it and set its status to
           * "Scheduled".
           */
          if (scheduledTask.getExecutionAttempts()
              >= taskService.getMaximumScheduledTaskExecutionAttempts())
          {
            logger.warn("The scheduled task (" + scheduledTask.getId()
                + ") has exceeded the maximum "
                + " number of execution attempts and will be marked as \"Failed\"");

            taskService.unlockScheduledTask(scheduledTask.getId(), ScheduledTaskStatus.FAILED);
          }
          else
          {
            taskService.unlockScheduledTask(scheduledTask.getId(), ScheduledTaskStatus.SCHEDULED);
          }
        }
        catch (Throwable f)
        {
          logger.error("Failed to unlock and set the status for the scheduled task ("
              + scheduledTask.getId() + ")", f);
        }
      }
    }
  }
}

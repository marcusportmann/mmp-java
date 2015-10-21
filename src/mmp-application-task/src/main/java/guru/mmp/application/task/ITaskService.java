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

package guru.mmp.application.task;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;

/**
 * The <code>ITaskService</code> interface defines the functionality that must be provided
 * by a Task Service implementation.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public interface ITaskService
{
  /**
   * Execute the scheduled task.
   *
   * @param scheduledTask the scheduled task
   *
   * @throws TaskServiceException
   */
  void executeScheduledTask(ScheduledTask scheduledTask)
    throws TaskServiceException;

  /**
   * Returns the maximum number of times execution will be attempted for a scheduled task.
   *
   * @return the maximum number of times execution will be attempted for a scheduled task
   */
  int getMaximumScheduledTaskExecutionAttempts();

  /**
   * Retrieve the next task that is scheduled for execution.
   * <p/>
   * The scheduled task will be locked to prevent duplicate processing.
   *
   * @return the next task that is scheduled for execution or <code>null</code> if no tasks are
   *         currently scheduled for execution
   *
   * @throws TaskServiceException
   */
  ScheduledTask getNextTaskScheduledForExecution()
    throws TaskServiceException;

  /**
   * Retrieve the parameters for the scheduled task with the specified ID.
   *
   * @param id the ID uniquely identifying the scheduled task
   *
   * @return the parameters for the scheduled task
   *
   * @throws TaskServiceException
   */
  List<ScheduledTaskParameter> getScheduledTaskParameters(String id)
    throws TaskServiceException;

  /**
   * Increment the execution attempts for the scheduled task with the specified ID.
   *
   * @param id the ID uniquely identifying the scheduled task
   *
   * @throws TaskServiceException
   */
  void incrementScheduledTaskExecutionAttempts(String id)
    throws TaskServiceException;

  /**
   * Reschedule the task for execution.
   *
   * @param id                the ID uniquely identifying the scheduled task
   * @param schedulingPattern the cron-style scheduling pattern for the scheduled task used to
   *                          determine the next execution time
   *
   * @throws TaskServiceException
   */
  void rescheduleTask(String id, String schedulingPattern)
    throws TaskServiceException;

  /**
   * Reset the scheduled task locks.
   *
   * @param status    the current status of the scheduled tasks that have been locked
   * @param newStatus the new status for the scheduled tasks that have been unlocked
   *
   * @return the number of scheduled task locks reset
   *
   * @throws TaskServiceException
   */
  int resetScheduledTaskLocks(ScheduledTask.Status status, ScheduledTask.Status newStatus)
    throws TaskServiceException;

  /**
   * Schedule the next unscheduled task for execution.
   *
   * @return <code>true</code> if there are more unscheduled tasks to schedule or
   *         <code>false</code> if there are no more unscheduled tasks to schedule
   *
   * @throws TaskServiceException
   */
  boolean scheduleNextUnscheduledTaskForExecution()
    throws TaskServiceException;

  /**
   * Unlock a locked scheduled task.
   *
   * @param id     the ID uniquely identifying the scheduled task
   * @param status the new status for the unlocked scheduled task
   *
   * @throws TaskServiceException
   */
  void unlockScheduledTask(String id, ScheduledTask.Status status)
    throws TaskServiceException;
}

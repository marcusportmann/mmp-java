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

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.persistence.DAOException;

import java.util.List;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>ITaskDAO</code> interface defines the task-related persistence operations.
 *
 * @author Marcus Portmann
 */
public interface ITaskDAO
{
  /**
   * Retrieve the next task that is scheduled for execution.
   * <p/>
   * The scheduled task will be locked to prevent duplicate processing.
   *
   * @param executionRetryDelay the delay in milliseconds between successive attempts to execute
   *                            a scheduled task
   * @param lockName            the name of the lock that should be applied to the task scheduled
   *                            for execution when it is retrieved
   *
   * @return the next task that is scheduled for execution or <code>null</code> if no tasks are
   *         currently scheduled for execution
   *
   * @throws DAOException
   */
  public ScheduledTask getNextTaskScheduledForExecution(int executionRetryDelay, String lockName)
    throws DAOException;

  /**
   * Retrieve the parameters for the scheduled task with the specified ID.
   *
   * @param id the ID uniquely identifying the scheduled task
   *
   * @return the parameters for the scheduled task
   *
   * @throws DAOException
   */
  public List<ScheduledTaskParameter> getScheduledTaskParameters(String id)
    throws DAOException;

  /**
   * Retrieve the unscheduled tasks.
   *
   * @return the unscheduled tasks
   *
   * @throws DAOException
   */
  public List<ScheduledTask> getUnscheduledTasks()
    throws DAOException;

  /**
   * Increment the execution attempts for the scheduled task with the specified ID.
   *
   * @param id the ID uniquely identifying the scheduled task
   *
   * @throws DAOException
   */
  public void incrementScheduledTaskExecutionAttempts(String id)
    throws DAOException;

  /**
   * Lock a scheduled task.
   *
   * @param id       the ID uniquely identifying the scheduled task
   * @param status   the new status for the locked scheduled task
   * @param lockName the name of the lock that should be applied to the scheduled task
   *
   * @throws DAOException
   */
  public void lockScheduledTask(String id, ScheduledTaskStatus status, String lockName)
    throws DAOException;

  /**
   * Reschedule the task for execution.
   *
   * @param id                the ID uniquely identifying the scheduled task
   * @param schedulingPattern the cron-style scheduling pattern for the scheduled task used to
   *                          determine the next execution time
   *
   * @throws DAOException
   */
  public void rescheduleTask(String id, String schedulingPattern)
    throws DAOException;

  /**
   * Reset the scheduled task locks.
   *
   * @param lockName  the name of the lock applied by the entity that has locked the scheduled tasks
   * @param status    the current status of the scheduled tasks that have been locked
   * @param newStatus the new status for the scheduled tasks that have been unlocked
   *
   * @return the number of scheduled task locks reset
   *
   * @throws DAOException
   */
  public int resetScheduledTaskLocks(String lockName, ScheduledTaskStatus status,
      ScheduledTaskStatus newStatus)
    throws DAOException;

  /**
   * Schedule the next unscheduled task for execution.
   *
   * @return <code>true</code> if there are more unscheduled tasks to schedule or
   *         <code>false</code> if there are no more unscheduled tasks to schedule
   *
   * @throws DAOException
   */
  public boolean scheduleNextUnscheduledTaskForExecution()
    throws DAOException;

  /**
   * Set the status for the scheduled task with the specified ID.
   *
   * @param id     the ID uniquely identifying the scheduled task
   * @param status the new status for the scheduled task
   *
   * @throws DAOException
   */
  public void setScheduledTaskStatus(String id, ScheduledTaskStatus status)
    throws DAOException;

  /**
   * Unlock a locked scheduled task.
   *
   * @param id     the ID uniquely identifying the scheduled task
   * @param status the new status for the unlocked scheduled task
   *
   * @throws DAOException
   */
  public void unlockScheduledTask(String id, ScheduledTaskStatus status)
    throws DAOException;
}

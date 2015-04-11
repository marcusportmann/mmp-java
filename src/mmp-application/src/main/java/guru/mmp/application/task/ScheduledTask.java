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

//~--- JDK imports ------------------------------------------------------------

import java.io.Serializable;
import java.util.Date;

/**
 * The <code>ScheduledTask</code> class holds the information for a scheduled task.
 *
 * @author Marcus Portmann
 */
public class ScheduledTask
  implements Serializable
{
  private static final long serialVersionUID = 1000000;

  /**
   * The number of times the current execution of the scheduled task has been attempted.
   */
  private int executionAttempts;

  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the scheduled task.
   */
  private String id;

  /**
   * The date and time the scheduled task was last executed.
   */
  private Date lastExecuted;

  /**
   * The name of the entity that has locked the scheduled task for execution.
   */
  private String lockName;

  /**
   * The name of the scheduled task.
   */
  private String name;

  /**
   * The date and time when the scheduled task will next be executed.
   */
  private Date nextExecution;

  /**
   * The cron-style scheduling pattern for the scheduled task.
   */
  private String schedulingPattern;

  /**
   * The status of the scheduled task.
   */
  private ScheduledTaskStatus status;

  /**
   * The fully qualified name of the Java class that implements the scheduled task.
   */
  private String taskClass;

  /**
   * The date and time the scheduled task was updated.
   */
  private Date updated;

  /**
   * Constructs a new <code>ScheduledTask</code>.
   */
  public ScheduledTask() {}

  /**
   * Constructs a new <code>ScheduledTask</code>.
   *
   * @param id                the Universally Unique Identifier (UUID) used to uniquely identify
   *                          the scheduled task
   * @param name              the name of the scheduled task
   * @param schedulingPattern the cron-style scheduling pattern for the scheduled task
   * @param taskClass         the fully qualified name of the Java class that implements the
   *                          scheduled task
   * @param status            the status of the scheduled task
   * @param executionAttempts the number of times the current execution of the scheduled task has
   *                          been attempted
   * @param lockName          the name of the entity that has locked the scheduled task for
   *                          execution
   * @param lastExecuted      the date and time the scheduled task was last executed
   * @param nextExecution     the date and time when the scheduled task will next be executed
   * @param updated           the date and time the scheduled task was updated
   */
  public ScheduledTask(String id, String name, String schedulingPattern, String taskClass,
      ScheduledTaskStatus status, int executionAttempts, String lockName, Date lastExecuted,
      Date nextExecution, Date updated)
  {
    this.id = id;
    this.name = name;
    this.schedulingPattern = schedulingPattern;
    this.taskClass = taskClass;
    this.status = status;
    this.executionAttempts = executionAttempts;
    this.lockName = lockName;
    this.lastExecuted = lastExecuted;
    this.nextExecution = nextExecution;
    this.updated = updated;
  }

  /**
   * Returns the number of times the current execution of the scheduled task has been attempted.
   *
   * @return the number of times the current execution of the scheduled task has been attempted
   */
  public int getExecutionAttempts()
  {
    return executionAttempts;
  }

  /**
   * Returns the Universally Unique Identifier (UUID) used to uniquely identify the scheduled task.
   *
   * @return the Universally Unique Identifier (UUID) used to uniquely identify the scheduled task
   */
  public String getId()
  {
    return id;
  }

  /**
   * Returns date and time the scheduled task was last executed.
   *
   * @return the date and time the scheduled task was last executed
   */
  public Date getLastExecuted()
  {
    return lastExecuted;
  }

  /**
   * Returns the name of the entity that has locked the scheduled task for execution.
   *
   * @return the name of the entity that has locked the scheduled task for execution
   */
  public String getLockName()
  {
    return lockName;
  }

  /**
   * Returns the name of the scheduled task.
   *
   * @return the name of the scheduled task
   */
  public String getName()
  {
    return name;
  }

  /**
   * Returns the date and time when the scheduled task will next be executed.
   *
   * @return the date and time when the scheduled task will next be executed
   */
  public Date getNextExecution()
  {
    return nextExecution;
  }

  /**
   * Returns the cron-style scheduling pattern for the scheduled task.
   *
   * @return the cron-style scheduling pattern for the scheduled task
   */
  public String getSchedulingPattern()
  {
    return schedulingPattern;
  }

  /**
   * Returns the status of the scheduled task.
   *
   * @return the status of the scheduled task
   */
  public ScheduledTaskStatus getStatus()
  {
    return status;
  }

  /**
   * Returns the fully qualified name of the Java class that implements the scheduled task.
   *
   * @return the fully qualified name of the Java class that implements the scheduled task
   */
  public String getTaskClass()
  {
    return taskClass;
  }

  /**
   * Returns the date and time the scheduled task was updated.
   *
   * @return the date and time the scheduled task was updated
   */
  public Date getUpdated()
  {
    return updated;
  }

  /**
   * Set the number of times the current execution of the scheduled task has been attempted.
   *
   * @param executionAttempts the number of times the current execution of the scheduled task has
   *                          been attempted
   */
  public void setExecutionAttempts(int executionAttempts)
  {
    this.executionAttempts = executionAttempts;
  }

  /**
   * Set the Universally Unique Identifier (UUID) used to uniquely identify the scheduled task.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the scheduled
   *           task
   */
  public void setId(String id)
  {
    this.id = id;
  }

  /**
   * Set the date and time the scheduled task was last executed.
   *
   * @param lastExecuted the date and time the scheduled task was last executed
   */
  public void setLastExecuted(Date lastExecuted)
  {
    this.lastExecuted = lastExecuted;
  }

  /**
   * Set the name of the entity that has locked the scheduled task for execution.
   *
   * @param lockName the name of the entity that has locked the scheduled task for execution
   */
  public void setLockName(String lockName)
  {
    this.lockName = lockName;
  }

  /**
   * Set the name of the scheduled task.
   *
   * @param name the name of the scheduled task
   */
  public void setName(String name)
  {
    this.name = name;
  }

  /**
   * Set the date and time when the scheduled task will next be executed.
   *
   * @param nextExecution the date and time when the scheduled task will next be executed
   */
  public void setNextExecution(Date nextExecution)
  {
    this.nextExecution = nextExecution;
  }

  /**
   * Set the cron-style scheduling pattern for the scheduled task.
   *
   * @param schedulingPattern the cron-style scheduling pattern for the scheduled task
   */
  public void setSchedulingPattern(String schedulingPattern)
  {
    this.schedulingPattern = schedulingPattern;
  }

  /**
   * Set the status of the scheduled task.
   *
   * @param status the status of the scheduled task
   */
  public void setStatus(ScheduledTaskStatus status)
  {
    this.status = status;
  }

  /**
   * Set the fully qualified name of the Java class that implements the scheduled task.
   *
   * @param taskClass the fully qualified name of the Java class that implements the scheduled task
   */
  public void setTaskClass(String taskClass)
  {
    this.taskClass = taskClass;
  }

  /**
   * Set the date and time the scheduled task was updated.
   *
   * @param updated the date and time the scheduled task was updated
   */
  public void setUpdated(Date updated)
  {
    this.updated = updated;
  }
}

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

//~--- JDK imports ------------------------------------------------------------

import java.io.Serializable;

import java.util.Date;

/**
 * The <code>Job</code> class holds the information for a job.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public class Job
  implements Serializable
{
  private static final long serialVersionUID = 1000000;

  /**
   * The number of times the current execution of the job has been attempted.
   */
  private int executionAttempts;

  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the job.
   */
  private String id;

  /**
   * The date and time the job was last executed.
   */
  private Date lastExecuted;

  /**
   * The name of the entity that has locked the job for execution.
   */
  private String lockName;

  /**
   * The name of the job.
   */
  private String name;

  /**
   * The date and time when the job will next be executed.
   */
  private Date nextExecution;

  /**
   * The cron-style scheduling pattern for the job.
   */
  private String schedulingPattern;

  /**
   * The status of the job.
   */
  private Status status;

  /**
   * The fully qualified name of the Java class that implements the job.
   */
  private String jobClass;

  /**
   * The date and time the job was updated.
   */
  private Date updated;

  /**
   * Constructs a new <code>Job</code>.
   */
  public Job() {}

  /**
   * Constructs a new <code>Job</code>.
   *
   * @param id                the Universally Unique Identifier (UUID) used to uniquely identify
   *                          the job
   * @param name              the name of the job
   * @param schedulingPattern the cron-style scheduling pattern for the job
   * @param jobClass          the fully qualified name of the Java class that implements the job
   * @param status            the status of the job
   * @param executionAttempts the number of times the current execution of the job has
   *                          been attempted
   * @param lockName          the name of the entity that has locked the job for execution
   * @param lastExecuted      the date and time the job was last executed
   * @param nextExecution     the date and time when the job will next be executed
   * @param updated           the date and time the job was updated
   */
  public Job(String id, String name, String schedulingPattern, String jobClass,
      Status status, int executionAttempts, String lockName, Date lastExecuted, Date nextExecution,
      Date updated)
  {
    this.id = id;
    this.name = name;
    this.schedulingPattern = schedulingPattern;
    this.jobClass = jobClass;
    this.status = status;
    this.executionAttempts = executionAttempts;
    this.lockName = lockName;
    this.lastExecuted = lastExecuted;
    this.nextExecution = nextExecution;
    this.updated = updated;
  }

  /**
   * The enumeration giving the possible statuses for a job.
   *
   * @author Marcus Portmann
   */
  public enum Status
  {
    UNKNOWN(0, "Unknown"), SCHEDULED(1, "Scheduled"), EXECUTING(2, "Executing"),
    EXECUTED(3, "Executed"), ABORTED(4, "Aborted"), FAILED(5, "Failed");

    private int code;
    private String name;

    Status(int code, String name)
    {
      this.code = code;
      this.name = name;
    }

    /**
     * Returns the status given by the specified numeric code value.
     *
     * @param code the numeric code value identifying the status
     *
     * @return the status given by the specified numeric code value
     */
    public static Status fromCode(int code)
    {
      switch (code)
      {
        case 1:
          return Status.SCHEDULED;

        case 2:
          return Status.EXECUTING;

        case 3:
          return Status.EXECUTED;

        case 4:
          return Status.ABORTED;

        case 5:
          return Status.FAILED;

        default:
          return Status.UNKNOWN;
      }
    }

    /**
     * Returns the numeric code value identifying the status.
     *
     * @return the numeric code value identifying the status
     */
    public int getCode()
    {
      return code;
    }

    /**
     * Returns the <code>String</code> value of the numeric code value identifying the status.
     *
     * @return the <code>String</code> value of the numeric code value identifying the status
     */
    @SuppressWarnings("unused")
    public String getCodeAsString()
    {
      return String.valueOf(code);
    }

    /**
     * Returns the name of the status.
     *
     * @return the name of the status
     */
    public String getName()
    {
      return name;
    }

    /**
     * Return the string representation of the status enumeration value.
     *
     * @return the string representation of the status enumeration value
     */
    public String toString()
    {
      return name;
    }
  }

  /**
   * Returns the number of times the current execution of the job has been attempted.
   *
   * @return the number of times the current execution of the job has been attempted
   */
  public int getExecutionAttempts()
  {
    return executionAttempts;
  }

  /**
   * Returns the Universally Unique Identifier (UUID) used to uniquely identify the job.
   *
   * @return the Universally Unique Identifier (UUID) used to uniquely identify the job
   */
  public String getId()
  {
    return id;
  }

  /**
   * Returns date and time the job was last executed.
   *
   * @return the date and time the job was last executed
   */
  public Date getLastExecuted()
  {
    return lastExecuted;
  }

  /**
   * Returns the name of the entity that has locked the job for execution.
   *
   * @return the name of the entity that has locked the job for execution
   */
  public String getLockName()
  {
    return lockName;
  }

  /**
   * Returns the name of the job.
   *
   * @return the name of the job
   */
  public String getName()
  {
    return name;
  }

  /**
   * Returns the date and time when the job will next be executed.
   *
   * @return the date and time when the job will next be executed
   */
  public Date getNextExecution()
  {
    return nextExecution;
  }

  /**
   * Returns the cron-style scheduling pattern for the job.
   *
   * @return the cron-style scheduling pattern for the job
   */
  public String getSchedulingPattern()
  {
    return schedulingPattern;
  }

  /**
   * Returns the status of the job.
   *
   * @return the status of the job
   */
  public Status getStatus()
  {
    return status;
  }

  /**
   * Returns the fully qualified name of the Java class that implements the job.
   *
   * @return the fully qualified name of the Java class that implements the job
   */
  public String getJobClass()
  {
    return jobClass;
  }

  /**
   * Returns the date and time the job was updated.
   *
   * @return the date and time the job was updated
   */
  public Date getUpdated()
  {
    return updated;
  }

  /**
   * Set the number of times the current execution of the job has been attempted.
   *
   * @param executionAttempts the number of times the current execution of the job has
   *                          been attempted
   */
  public void setExecutionAttempts(int executionAttempts)
  {
    this.executionAttempts = executionAttempts;
  }

  /**
   * Set the Universally Unique Identifier (UUID) used to uniquely identify the job.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the scheduled
   *           job
   */
  public void setId(String id)
  {
    this.id = id;
  }

  /**
   * Set the date and time the job was last executed.
   *
   * @param lastExecuted the date and time the job was last executed
   */
  public void setLastExecuted(Date lastExecuted)
  {
    this.lastExecuted = lastExecuted;
  }

  /**
   * Set the name of the entity that has locked the job for execution.
   *
   * @param lockName the name of the entity that has locked the job for execution
   */
  public void setLockName(String lockName)
  {
    this.lockName = lockName;
  }

  /**
   * Set the name of the job.
   *
   * @param name the name of the job
   */
  public void setName(String name)
  {
    this.name = name;
  }

  /**
   * Set the date and time when the job will next be executed.
   *
   * @param nextExecution the date and time when the job will next be executed
   */
  public void setNextExecution(Date nextExecution)
  {
    this.nextExecution = nextExecution;
  }

  /**
   * Set the cron-style scheduling pattern for the job.
   *
   * @param schedulingPattern the cron-style scheduling pattern for the job
   */
  public void setSchedulingPattern(String schedulingPattern)
  {
    this.schedulingPattern = schedulingPattern;
  }

  /**
   * Set the status of the job.
   *
   * @param status the status of the job
   */
  public void setStatus(Status status)
  {
    this.status = status;
  }

  /**
   * Set the fully qualified name of the Java class that implements the job.
   *
   * @param jobClass the fully qualified name of the Java class that implements the job
   */
  public void setJobClass(String jobClass)
  {
    this.jobClass = jobClass;
  }

  /**
   * Set the date and time the job was updated.
   *
   * @param updated the date and time the job was updated
   */
  public void setUpdated(Date updated)
  {
    this.updated = updated;
  }
}

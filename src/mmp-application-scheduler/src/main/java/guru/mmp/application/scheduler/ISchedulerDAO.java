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

import guru.mmp.common.persistence.DAOException;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;
import java.util.UUID;

/**
 * The <code>ISchedulerDAO</code> interface defines the scheduler-related persistence operations.
 *
 * @author Marcus Portmann
 */
public interface ISchedulerDAO
{
  /**
   * Create the entry for the job in the database.
   *
   * @param job the <code>Job</code> instance containing the information for the job
   */
  void createJob(Job job)
    throws DAOException;

  /**
   * Delete the job.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the job
   */
  void deleteJob(UUID id)
    throws DAOException;

  /**
   * Retrieve the filtered jobs.
   *
   * @param filter the filter to apply to the jobs
   *
   * @return the jobs
   */
  List<Job> getFilteredJobs(String filter)
    throws DAOException;

  /**
   * Retrieve the job.
   *
   * @param id the ID uniquely identifying the job
   *
   * @return the job or <code>null</code> if the job could not be found
   */
  Job getJob(UUID id)
    throws DAOException;

  /**
   * Retrieve the parameters for the job.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the job
   *
   * @return the parameters for the job
   */
  List<JobParameter> getJobParameters(UUID id)
    throws DAOException;

  /**
   * Retrieve the jobs.
   *
   * @return the jobs
   */
  List<Job> getJobs()
    throws DAOException;

  /**
   * Retrieve the next job that is scheduled for execution.
   * <p/>
   * The job will be locked to prevent duplicate processing.
   *
   * @param executionRetryDelay the delay in milliseconds between successive attempts to execute
   *                            a job
   * @param lockName            the name of the lock that should be applied to the job scheduled
   *                            for execution when it is retrieved
   *
   * @return the next job that is scheduled for execution or <code>null</code> if no jobs are
   *         currently scheduled for execution
   */
  Job getNextJobScheduledForExecution(int executionRetryDelay, String lockName)
    throws DAOException;

  /**
   * Retrieve the number of filtered jobs.
   *
   * @param filter the filter to apply to the jobs
   *
   * @return the number of filtered jobs
   */
  int getNumberOfFilteredJobs(String filter)
    throws DAOException;

  /**
   * Retrieve the number of jobs.
   *
   * @return the number of jobs
   */
  int getNumberOfJobs()
    throws DAOException;

  /**
   * Retrieve the unscheduled jobs.
   *
   * @return the unscheduled jobs
   */
  List<Job> getUnscheduledJobs()
    throws DAOException;

  /**
   * Increment the execution attempts for the job.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the job
   */
  void incrementJobExecutionAttempts(UUID id)
    throws DAOException;

  /**
   * Lock a job.
   *
   * @param id       the Universally Unique Identifier (UUID) used to uniquely identify the job
   * @param status   the new status for the locked job
   * @param lockName the name of the lock that should be applied to the job
   */
  void lockJob(UUID id, Job.Status status, String lockName)
    throws DAOException;

  /**
   * Reschedule the job for execution.
   *
   * @param id                the Universally Unique Identifier (UUID) used to uniquely identify
   *                          the job
   * @param schedulingPattern the cron-style scheduling pattern for the job used to determine the
   *                          next execution time
   */
  void rescheduleJob(UUID id, String schedulingPattern)
    throws DAOException;

  /**
   * Reset the job locks.
   *
   * @param lockName  the name of the lock applied by the entity that has locked the jobs
   * @param status    the current status of the jobs that have been locked
   * @param newStatus the new status for the jobs that have been unlocked
   *
   * @return the number of job locks reset
   */
  int resetJobLocks(String lockName, Job.Status status, Job.Status newStatus)
    throws DAOException;

  /**
   * Schedule the next unscheduled job for execution.
   *
   * @return <code>true</code> if there are more unscheduled jobs to schedule or <code>false</code>
   *         if there are no more unscheduled jobs to schedule
   */
  boolean scheduleNextUnscheduledJobForExecution()
    throws DAOException;

  /**
   * Set the status for the job.
   *
   * @param id     the Universally Unique Identifier (UUID) used to uniquely identify the job
   * @param status the new status for the job
   */
  void setJobStatus(UUID id, Job.Status status)
    throws DAOException;

  /**
   * Unlock a locked job.
   *
   * @param id     the Universally Unique Identifier (UUID) used to uniquely identify the job
   * @param status the new status for the unlocked job
   */
  void unlockJob(UUID id, Job.Status status)
    throws DAOException;

  /**
   * Update the entry for the job in the database.
   *
   * @param job the <code>Job</code> instance containing the updated information for the job
   */
  void updateJob(Job job)
    throws DAOException;
}

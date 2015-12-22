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

import guru.mmp.common.persistence.DAOException;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;

/**
 * The <code>IJobDAO</code> interface defines the job-related persistence operations.
 *
 * @author Marcus Portmann
 */
public interface IJobDAO
{
  /**
   * Create the entry for the job in the database.
   *
   * @param job the <code>Job</code> instance containing the information for the job
   *
   * @throws DAOException
   */
  void createJob(Job job)
    throws DAOException;

  /**
   * Retrieve the job.
   *
   * @param id the ID uniquely identifying the job
   *
   * @return the job or <code>null</code> if the job could not be found
   *
   * @throws DAOException
   */
  Job getJob(String id)
    throws DAOException;

  /**
   * Retrieve the parameters for the job.
   *
   * @param id the ID uniquely identifying the job
   *
   * @return the parameters for the job
   *
   * @throws DAOException
   */
  List<JobParameter> getJobParameters(String id)
    throws DAOException;

  /**
   * Retrieve the jobs.
   *
   * @return the jobs
   *
   * @throws DAOException
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
   *
   * @throws DAOException
   */
  Job getNextJobScheduledForExecution(int executionRetryDelay, String lockName)
    throws DAOException;

  /**
   * Retrieve the number of jobs.
   *
   * @return the number of jobs
   *
   * @throws DAOException
   */
  int getNumberOfJobs()
    throws DAOException;

  /**
   * Retrieve the unscheduled jobs.
   *
   * @return the unscheduled jobs
   *
   * @throws DAOException
   */
  List<Job> getUnscheduledJobs()
    throws DAOException;

  /**
   * Increment the execution attempts for the job.
   *
   * @param id the ID uniquely identifying the job
   *
   * @throws DAOException
   */
  void incrementJobExecutionAttempts(String id)
    throws DAOException;

  /**
   * Lock a job.
   *
   * @param id       the ID uniquely identifying the job
   * @param status   the new status for the locked job
   * @param lockName the name of the lock that should be applied to the job
   *
   * @throws DAOException
   */
  void lockJob(String id, Job.Status status, String lockName)
    throws DAOException;

  /**
   * Reschedule the job for execution.
   *
   * @param id                the ID uniquely identifying the job
   * @param schedulingPattern the cron-style scheduling pattern for the job used to determine the
   *                          next execution time
   *
   * @throws DAOException
   */
  void rescheduleJob(String id, String schedulingPattern)
    throws DAOException;

  /**
   * Reset the job locks.
   *
   * @param lockName  the name of the lock applied by the entity that has locked the jobs
   * @param status    the current status of the jobs that have been locked
   * @param newStatus the new status for the jobs that have been unlocked
   *
   * @return the number of job locks reset
   *
   * @throws DAOException
   */
  int resetJobLocks(String lockName, Job.Status status, Job.Status newStatus)
    throws DAOException;

  /**
   * Schedule the next unscheduled job for execution.
   *
   * @return <code>true</code> if there are more unscheduled jobs to schedule or <code>false</code>
   *         if there are no more unscheduled jobs to schedule
   *
   * @throws DAOException
   */
  boolean scheduleNextUnscheduledJobForExecution()
    throws DAOException;

  /**
   * Set the status for the job.
   *
   * @param id     the ID uniquely identifying the job
   * @param status the new status for the job
   *
   * @throws DAOException
   */
  void setJobStatus(String id, Job.Status status)
    throws DAOException;

  /**
   * Unlock a locked job.
   *
   * @param id     the ID uniquely identifying the job
   * @param status the new status for the unlocked job
   *
   * @throws DAOException
   */
  void unlockJob(String id, Job.Status status)
    throws DAOException;
}

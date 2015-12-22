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

import java.util.List;

/**
 * The <code>IJobService</code> interface defines the functionality that must be provided
 * by a Job Service implementation.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public interface IJobService
{
  /**
   * Create the job.
   *
   * @param job the <code>Job</code> instance containing the information for the job
   *
   * @throws JobServiceException
   */
  void createJob(Job job)
    throws JobServiceException;

  /**
   * Execute the job.
   *
   * @param job the job
   *
   * @throws JobServiceException
   */
  void executeJob(Job job)
    throws JobServiceException;

  /**
   * Retrieve the job.
   *
   * @param id the ID uniquely identifying the job
   *
   * @return the job or <code>null</code> if the job could not be found
   *
   * @throws JobServiceException
   */
  Job getJob(String id)
    throws JobServiceException;

  /**
   * Retrieve the parameters for the job.
   *
   * @param id the ID uniquely identifying the job
   *
   * @return the parameters for the job
   *
   * @throws JobServiceException
   */
  List<JobParameter> getJobParameters(String id)
    throws JobServiceException;

  /**
   * Retrieve the jobs.
   *
   * @return the jobs
   *
   * @throws JobServiceException
   */
  List<Job> getJobs()
    throws JobServiceException;

  /**
   * Returns the maximum number of times execution will be attempted for a job.
   *
   * @return the maximum number of times execution will be attempted for a job
   */
  int getMaximumJobExecutionAttempts();

  /**
   * Retrieve the next job that is scheduled for execution.
   * <p/>
   * The job will be locked to prevent duplicate processing.
   *
   * @return the next job that is scheduled for execution or <code>null</code> if no jobs are
   *         currently scheduled for execution
   *
   * @throws JobServiceException
   */
  Job getNextJobScheduledForExecution()
    throws JobServiceException;

  /**
   * Retrieve the number of jobs.
   *
   * @return the number of jobs
   *
   * @throws JobServiceException
   */
  int getNumberOfJobs()
    throws JobServiceException;

  /**
   * Increment the execution attempts for the job.
   *
   * @param id the ID uniquely identifying the job
   *
   * @throws JobServiceException
   */
  void incrementJobExecutionAttempts(String id)
    throws JobServiceException;

  /**
   * Reschedule the job for execution.
   *
   * @param id                the ID uniquely identifying the job
   * @param schedulingPattern the cron-style scheduling pattern for the job used to determine the
   *                          next execution time
   *
   * @throws JobServiceException
   */
  void rescheduleJob(String id, String schedulingPattern)
    throws JobServiceException;

  /**
   * Reset the job locks.
   *
   * @param status    the current status of the jobs that have been locked
   * @param newStatus the new status for the jobs that have been unlocked
   *
   * @return the number of job locks reset
   *
   * @throws JobServiceException
   */
  int resetJobLocks(Job.Status status, Job.Status newStatus)
    throws JobServiceException;

  /**
   * Schedule the next unscheduled job for execution.
   *
   * @return <code>true</code> if there are more unscheduled jobs to schedule or <code>false</code>
   *         if there are no more unscheduled jobs to schedule
   *
   * @throws JobServiceException
   */
  boolean scheduleNextUnscheduledJobForExecution()
    throws JobServiceException;

  /**
   * Unlock a locked job.
   *
   * @param id     the ID uniquely identifying the job
   * @param status the new status for the unlocked job
   *
   * @throws JobServiceException
   */
  void unlockJob(String id, Job.Status status)
    throws JobServiceException;
}

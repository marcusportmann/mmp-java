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

package guru.mmp.application.batch;

import java.util.List;
import java.util.UUID;

/**
 * The <code>IBatchService</code> interface defines the functionality provided by a Batch Service
 * implementation.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public interface IBatchService
{
  /**
   * Create the job.
   *
   * @param job the <code>Job</code> instance containing the information for the job
   *
   * @throws BatchServiceException
   */
  void createJob(Job job)
    throws BatchServiceException;

  /**
   * Execute the job.
   *
   * @param job the job
   *
   * @throws BatchServiceException
   */
  void executeJob(Job job)
    throws BatchServiceException;

  /**
   * Retrieve the job.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the job
   *
   * @return the job or <code>null</code> if the job could not be found
   *
   * @throws BatchServiceException
   */
  Job getJob(UUID id)
    throws BatchServiceException;

  /**
   * Retrieve the parameters for the job.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the job
   *
   * @return the parameters for the job
   *
   * @throws BatchServiceException
   */
  List<JobParameter> getJobParameters(UUID id)
    throws BatchServiceException;

  /**
   * Retrieve the jobs.
   *
   * @return the jobs
   *
   * @throws BatchServiceException
   */
  List<Job> getJobs()
    throws BatchServiceException;

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
   * currently scheduled for execution
   *
   * @throws BatchServiceException
   */
  Job getNextJobScheduledForExecution()
    throws BatchServiceException;

  /**
   * Retrieve the number of jobs.
   *
   * @return the number of jobs
   *
   * @throws BatchServiceException
   */
  int getNumberOfJobs()
    throws BatchServiceException;

  /**
   * Increment the execution attempts for the job.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the job
   *
   * @throws BatchServiceException
   */
  void incrementJobExecutionAttempts(UUID id)
    throws BatchServiceException;

  /**
   * Reschedule the job for execution.
   *
   * @param id                the Universally Unique Identifier (UUID) used to uniquely identify
   *                          the job
   * @param schedulingPattern the cron-style scheduling pattern for the job used to determine the
   *                          next execution time
   *
   * @throws BatchServiceException
   */
  void rescheduleJob(UUID id, String schedulingPattern)
    throws BatchServiceException;

  /**
   * Reset the job locks.
   *
   * @param status    the current status of the jobs that have been locked
   * @param newStatus the new status for the jobs that have been unlocked
   *
   * @return the number of job locks reset
   *
   * @throws BatchServiceException
   */
  int resetJobLocks(Job.Status status, Job.Status newStatus)
    throws BatchServiceException;

  /**
   * Schedule the next unscheduled job for execution.
   *
   * @return <code>true</code> if there are more unscheduled jobs to schedule or <code>false</code>
   * if there are no more unscheduled jobs to schedule
   *
   * @throws BatchServiceException
   */
  boolean scheduleNextUnscheduledJobForExecution()
    throws BatchServiceException;

  /**
   * Unlock a locked job.
   *
   * @param id     the Universally Unique Identifier (UUID) used to uniquely identify the job
   * @param status the new status for the unlocked job
   *
   * @throws BatchServiceException
   */
  void unlockJob(UUID id, Job.Status status)
    throws BatchServiceException;
}

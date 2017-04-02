/*
 * Copyright 2017 Marcus Portmann
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

import guru.mmp.application.configuration.IConfigurationService;
import guru.mmp.application.util.ServiceUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

//~--- JDK imports ------------------------------------------------------------

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;

/**
 * The <code>SchedulerService</code> class provides the Scheduler Service implementation.
 *
 * @author Marcus Portmann
 */
@Service
public class SchedulerService
  implements ISchedulerService
{
  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(SchedulerService.class);

  /* The name of the Scheduler Service instance. */
  private String instanceName = ServiceUtil.getServiceInstanceName("Scheduler Service");

  /* Job DAO */
  @Autowired
  private ISchedulerDAO jobDAO;

  /**
   * The Spring application context.
   */
  @Autowired
  private ApplicationContext applicationContext;

  /*
   * The delay in milliseconds between successive attempts to execute a job.
   */
  private int jobExecutionRetryDelay;

  /*
   * The maximum number of times execution will be attempted for a job.
   */
  private int maximumJobExecutionAttempts;

  /* Configuration Service */
  @Autowired
  private IConfigurationService configurationService;

  /**
   * Create the job.
   *
   * @param job the <code>Job</code> instance containing the information for the job
   */
  public void createJob(Job job)
    throws SchedulerServiceException
  {
    try
    {
      jobDAO.createJob(job);
    }
    catch (Throwable e)
    {
      throw new SchedulerServiceException("Failed to create the job", e);
    }
  }

  /**
   * Delete the job
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the job
   */
  public void deleteJob(UUID id)
    throws SchedulerServiceException
  {
    try
    {
      jobDAO.deleteJob(id);
    }
    catch (Throwable e)
    {
      throw new SchedulerServiceException(String.format("Failed to delete the job (%s)", id), e);
    }

  }

  /**
   * Execute the job.
   *
   * @param job the job
   */
  public void executeJob(Job job)
    throws SchedulerServiceException
  {
    Class<?> jobClass;

    // Load the job class.
    try
    {
      jobClass = Thread.currentThread().getContextClassLoader().loadClass(job.getJobClass());
    }
    catch (Throwable e)
    {
      throw new SchedulerServiceException(String.format(
          "Failed to execute the job (%s) with ID (%s): Failed to load the job class (%s)",
          job.getName(), job.getId(), job.getJobClass()), e);
    }

    // Initialise the job
    IJob jobImplementation;

    try
    {
      // Create a new instance of the job
      Object jobObject = jobClass.newInstance();

      // Check if the job is a valid job
      if (!(jobObject instanceof IJob))
      {
        throw new SchedulerServiceException(String.format(
            "The job class (%s) does not implement the guru.mmp.application.scheduler.IJob interface",
            job.getJobClass()));
      }

      jobImplementation = (IJob) jobObject;

      // Inject the job implementation
      injectJob(job, jobImplementation);
    }
    catch (Throwable e)
    {
      throw new SchedulerServiceException(String.format(
          "Failed to initialise the job (%s) with ID (%s)", job.getName(), job.getId()), e);
    }

    // Execute the job
    try
    {
      // Retrieve the parameters for the job
      List<JobParameter> jobParameters = jobDAO.getJobParameters(job.getId());

      Map<String, String> parameters = new HashMap<>();

      for (JobParameter jobParameter : jobParameters)
      {
        parameters.put(jobParameter.getName(), jobParameter.getValue());
      }

      // Initialise the job execution context
      JobExecutionContext context = new JobExecutionContext(job.getNextExecution(), parameters);

      // Execute the job
      jobImplementation.execute(context);
    }
    catch (Throwable e)
    {
      throw new SchedulerServiceException(String.format(
          "Failed to execute the job (%s) with ID (%s)", job.getName(), job.getId()), e);
    }
  }

  /**
   * Retrieve the filtered jobs.
   *
   * @param filter the filter to apply to the jobs
   *
   * @return the jobs
   */
  public List<Job> getFilteredJobs(String filter)
    throws SchedulerServiceException
  {
    try
    {
      return jobDAO.getFilteredJobs(filter);
    }
    catch (Throwable e)
    {
      throw new SchedulerServiceException(String.format(
          "Failed to retrieve the jobs matching the filter (%s)", filter), e);
    }
  }

  /**
   * Retrieve the job.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the job
   *
   * @return the job or <code>null</code> if the job could not be found
   */
  public Job getJob(UUID id)
    throws SchedulerServiceException
  {
    try
    {
      return jobDAO.getJob(id);
    }
    catch (Throwable e)
    {
      throw new SchedulerServiceException(String.format("Failed to retrieve the job (%s)", id), e);
    }
  }

  /**
   * Retrieve the parameters for the job.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the job
   *
   * @return the parameters for the job
   */
  public List<JobParameter> getJobParameters(UUID id)
    throws SchedulerServiceException
  {
    try
    {
      return jobDAO.getJobParameters(id);
    }
    catch (Throwable e)
    {
      throw new SchedulerServiceException(String.format(
          "Failed to retrieve the parameters for the job (%s)", id), e);
    }
  }

  /**
   * Retrieve the jobs.
   *
   * @return the jobs
   */
  public List<Job> getJobs()
    throws SchedulerServiceException
  {
    try
    {
      return jobDAO.getJobs();
    }
    catch (Throwable e)
    {
      throw new SchedulerServiceException("Failed to retrieve the jobs", e);
    }
  }

  /**
   * Returns the maximum number of times execution will be attempted for a job.
   *
   * @return the maximum number of times execution will be attempted for a job
   */
  public int getMaximumJobExecutionAttempts()
  {
    return maximumJobExecutionAttempts;
  }

  /**
   * Retrieve the next job that is scheduled for execution.
   * <p/>
   * The job will be locked to prevent duplicate processing.
   *
   * @return the next job that is scheduled for execution or <code>null</code> if no jobs are
   *         currently scheduled for execution
   */
  public Job getNextJobScheduledForExecution()
    throws SchedulerServiceException
  {
    try
    {
      return jobDAO.getNextJobScheduledForExecution(jobExecutionRetryDelay, instanceName);
    }
    catch (Throwable e)
    {
      throw new SchedulerServiceException(
          "Failed to retrieve the next job that has been scheduled for execution", e);
    }
  }

  /**
   * Retrieve the number of filtered jobs.
   *
   * @param filter the filter to apply to the jobs
   *
   * @return the number of filtered jobs
   */
  public int getNumberOfFilteredJobs(String filter)
    throws SchedulerServiceException
  {
    try
    {
      return jobDAO.getNumberOfFilteredJobs(filter);
    }
    catch (Throwable e)
    {
      throw new SchedulerServiceException(String.format(
          "Failed to retrieve the number of jobs matching the filter (%s)", filter), e);
    }
  }

  /**
   * Retrieve the number of jobs.
   *
   * @return the number of jobs
   */
  public int getNumberOfJobs()
    throws SchedulerServiceException
  {
    try
    {
      return jobDAO.getNumberOfJobs();
    }
    catch (Throwable e)
    {
      throw new SchedulerServiceException("Failed to retrieve the number of jobs", e);
    }
  }

  /**
   * Retrieve the unscheduled jobs.
   *
   * @return the unscheduled jobs
   */
  public List<Job> getUnscheduledJobs()
    throws SchedulerServiceException
  {
    try
    {
      return jobDAO.getUnscheduledJobs();
    }
    catch (Throwable e)
    {
      throw new SchedulerServiceException("Failed to retrieve the unscheduled jobs", e);
    }
  }

  /**
   * Increment the execution attempts for the job.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the job
   */
  public void incrementJobExecutionAttempts(UUID id)
    throws SchedulerServiceException
  {
    try
    {
      jobDAO.incrementJobExecutionAttempts(id);
    }
    catch (Throwable e)
    {
      throw new SchedulerServiceException(String.format(
          "Failed to increment the execution attempts for the job (%s)", id), e);
    }
  }

  /**
   * Initialise the Scheduler Service.
   */
  @PostConstruct
  public void init()
  {
    logger.info(String.format("Initialising the Scheduler Service (%s)", instanceName));

    try
    {
      // Initialise the configuration for the Scheduler Service
      initConfiguration();
    }
    catch (Throwable e)
    {
      throw new RuntimeException("Failed to initialise the Scheduler Service", e);
    }
  }

  /**
   * Reschedule the job for execution.
   *
   * @param id                the Universally Unique Identifier (UUID) used to uniquely identify
   *                          the job
   * @param schedulingPattern the cron-style scheduling pattern for the job used to determine the
   *                          next execution time
   */
  public void rescheduleJob(UUID id, String schedulingPattern)
    throws SchedulerServiceException
  {
    try
    {
      jobDAO.rescheduleJob(id, schedulingPattern);
    }
    catch (Throwable e)
    {
      throw new SchedulerServiceException(String.format(
          "Failed to reschedule the job (%s) for execution", id), e);
    }
  }

  /**
   * Reset the job locks.
   *
   * @param status    the current status of the jobs that have been locked
   * @param newStatus the new status for the jobs that have been unlocked
   *
   * @return the number of job locks reset
   */
  public int resetJobLocks(Job.Status status, Job.Status newStatus)
    throws SchedulerServiceException
  {
    try
    {
      return jobDAO.resetJobLocks(instanceName, status, newStatus);
    }
    catch (Throwable e)
    {
      throw new SchedulerServiceException(String.format(
          "Failed to reset the locks for the jobs with the status (%s) that have been locked using "
          + "the lock name (%s)", status, instanceName), e);
    }
  }

  /**
   * Schedule the next unscheduled job for execution.
   *
   * @return <code>true</code> if there are more unscheduled jobs to schedule or <code>false</code>
   *         if there are no more unscheduled jobs to schedule
   */
  public boolean scheduleNextUnscheduledJobForExecution()
    throws SchedulerServiceException
  {
    try
    {
      return jobDAO.scheduleNextUnscheduledJobForExecution();
    }
    catch (Throwable e)
    {
      throw new SchedulerServiceException(
          "Failed to schedule the next unscheduled job for execution", e);
    }
  }

  /**
   * Unlock a locked job.
   *
   * @param id     the Universally Unique Identifier (UUID) used to uniquely identify the job
   * @param status the new status for the unlocked job
   */
  public void unlockJob(UUID id, Job.Status status)
    throws SchedulerServiceException
  {
    try
    {
      jobDAO.unlockJob(id, status);
    }
    catch (Throwable e)
    {
      throw new SchedulerServiceException(String.format(
          "Failed to unlock and set the status for the job (%s) to (%s)", id, status), e);
    }
  }

  /**
   * Update the job.
   *
   * @param job the <code>Job</code> instance containing the updated information for the job
   */
  public void updateJob(Job job)
    throws SchedulerServiceException
  {
    try
    {
      jobDAO.updateJob(job);
    }
    catch (Throwable e)
    {
      throw new SchedulerServiceException("Failed to update the job", e);
    }
  }

  /**
   * Initialise the configuration for the <code>SchedulerService</code> instance.
   */
  private void initConfiguration()
    throws SchedulerServiceException
  {
    try
    {
      if (!configurationService.keyExists("SchedulerService.JobExecutionRetryDelay"))
      {
        configurationService.setValue("SchedulerService.JobExecutionRetryDelay", 60000,
            "The delay in milliseconds between attempts to retry the execution of a job");
      }

      if (!configurationService.keyExists("SchedulerService.MaximumJobExecutionAttempts"))
      {
        configurationService.setValue("SchedulerService.MaximumJobExecutionAttempts", 6 * 24,
            "The maximum number of attempts to execute a job");
      }

      jobExecutionRetryDelay = configurationService.getInteger(
          "SchedulerService.JobExecutionRetryDelay");

      maximumJobExecutionAttempts = configurationService.getInteger(
          "SchedulerService.MaximumJobExecutionAttempts");
    }
    catch (Throwable e)
    {
      throw new SchedulerServiceException(
          "Failed to initialise the configuration for the Scheduler Service", e);
    }
  }

  private void injectJob(Job job, IJob jobImplementation)
    throws SchedulerServiceException
  {
    try
    {
      applicationContext.getAutowireCapableBeanFactory().autowireBean(jobImplementation);
    }
    catch (Throwable e)
    {
      throw new SchedulerServiceException(String.format(
          "Failed in inject the job class (%s) for the job (%s) with ID (%s)", job.getJobClass(),
          job.getName(), job.getId()), e);
    }
  }
}

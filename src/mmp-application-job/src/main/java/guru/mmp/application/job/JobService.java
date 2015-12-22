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

import guru.mmp.application.registry.IRegistry;
import guru.mmp.common.cdi.CDIUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//~--- JDK imports ------------------------------------------------------------

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;

import javax.inject.Inject;

import javax.naming.InitialContext;

/**
 * The <code>JobService</code> class provides the Job Service implementation.
 *
 * @author Marcus Portmann
 */
@ApplicationScoped
@Default
public class JobService
  implements IJobService
{
  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(JobService.class);

  /* The name of the Job Service instance. */
  private String instanceName;

  /* Job DAO */
  @Inject
  private IJobDAO jobDAO;

  /*
   * The delay in milliseconds between successive attempts to execute a job.
   */
  private int jobExecutionRetryDelay;

  /*
   * The maximum number of times execution will be attempted for a job.
   */
  private int maximumJobExecutionAttempts;

  /* Registry */
  @Inject
  private IRegistry registry;

  /**
   * Constructs a new <code>JobService</code>.
   */
  public JobService() {}

  /**
   * Create the job.
   *
   * @param job the <code>Job</code> instance containing the information for the job
   *
   * @throws JobServiceException
   */
  public void createJob(Job job)
    throws JobServiceException
  {
    try
    {
      jobDAO.createJob(job);
    }
    catch (Throwable e)
    {
      throw new JobServiceException("Failed to create the job", e);
    }
  }

  /**
   * Execute the job.
   *
   * @param job the job
   *
   * @throws JobServiceException
   */
  public void executeJob(Job job)
    throws JobServiceException
  {
    Class<?> jobClass;

    // Load the job class.
    try
    {
      jobClass = Thread.currentThread().getContextClassLoader().loadClass(job.getJobClass());
    }
    catch (Throwable e)
    {
      throw new JobServiceException("Failed to execute the job (" + job.getName() + ") with ID ("
          + job.getId() + "): Failed to load the job class (" + job.getJobClass() + ")", e);
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
        throw new JobServiceException("The job class (" + job.getJobClass()
            + ") does not implement the guru.mmp.application.job.IJob interface");
      }

      jobImplementation = (IJob) jobObject;

      // Inject the job implementation
      injectJob(job, jobImplementation);
    }
    catch (Throwable e)
    {
      throw new JobServiceException("Failed to initialise the job (" + job.getName()
          + ") with ID (" + job.getId() + ")", e);
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
      throw new JobServiceException("Failed to execute the job (" + job.getName() + ") with ID ("
          + job.getId() + ")", e);
    }
  }

  /**
   * Retrieve the job.
   *
   * @param id the ID uniquely identifying the job
   *
   * @return the job or <code>null</code> if the job could not be found
   *
   * @throws JobServiceException
   */
  public Job getJob(String id)
    throws JobServiceException
  {
    try
    {
      return jobDAO.getJob(id);
    }
    catch (Throwable e)
    {
      throw new JobServiceException("Failed to retrieve the job (" + id + ")", e);
    }
  }

  /**
   * Retrieve the parameters for the job.
   *
   * @param id the ID uniquely identifying the job
   *
   * @return the parameters for the job
   *
   * @throws JobServiceException
   */
  public List<JobParameter> getJobParameters(String id)
    throws JobServiceException
  {
    try
    {
      return jobDAO.getJobParameters(id);
    }
    catch (Throwable e)
    {
      throw new JobServiceException("Failed to retrieve the parameters for the job (" + id + ")",
          e);
    }
  }

  /**
   * Retrieve the jobs.
   *
   * @return the jobs
   *
   * @throws JobServiceException
   */
  public List<Job> getJobs()
    throws JobServiceException
  {
    try
    {
      return jobDAO.getJobs();
    }
    catch (Throwable e)
    {
      throw new JobServiceException("Failed to retrieve jobs", e);
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
   *
   * @throws JobServiceException
   */
  public Job getNextJobScheduledForExecution()
    throws JobServiceException
  {
    try
    {
      return jobDAO.getNextJobScheduledForExecution(jobExecutionRetryDelay, getInstanceName());
    }
    catch (Throwable e)
    {
      throw new JobServiceException(
          "Failed to retrieve the next job that has been scheduled for execution", e);
    }
  }

  /**
   * Retrieve the number of jobs.
   *
   * @return the number of jobs
   *
   * @throws JobServiceException
   */
  public int getNumberOfJobs()
    throws JobServiceException
  {
    try
    {
      return jobDAO.getNumberOfJobs();
    }
    catch (Throwable e)
    {
      throw new JobServiceException("Failed to retrieve the number of jobs", e);
    }
  }

  /**
   * Increment the execution attempts for the job.
   *
   * @param id the ID uniquely identifying the job
   *
   * @throws JobServiceException
   */
  public void incrementJobExecutionAttempts(String id)
    throws JobServiceException
  {
    try
    {
      jobDAO.incrementJobExecutionAttempts(id);
    }
    catch (Throwable e)
    {
      throw new JobServiceException("Failed to increment the execution attempts for the job (" + id
          + ")", e);
    }
  }

  /**
   * Initialise the Job Service instance.
   */
  @PostConstruct
  public void init()
  {
    logger.info("Initialising the Job Service instance (" + getInstanceName() + ")");

    try
    {
      // Initialise the configuration for the Job Service instance
      initConfiguration();
    }
    catch (Throwable e)
    {
      throw new RuntimeException("Failed to initialise the Job Service", e);
    }
  }

  /**
   * Reschedule the job for execution.
   *
   * @param id                the ID uniquely identifying the job
   * @param schedulingPattern the cron-style scheduling pattern for the job used to determine the
   *                          next execution time
   *
   * @throws JobServiceException
   */
  public void rescheduleJob(String id, String schedulingPattern)
    throws JobServiceException
  {
    try
    {
      jobDAO.rescheduleJob(id, schedulingPattern);
    }
    catch (Throwable e)
    {
      throw new JobServiceException("Failed to reschedule the job (" + id + ") for execution", e);
    }
  }

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
  public int resetJobLocks(Job.Status status, Job.Status newStatus)
    throws JobServiceException
  {
    try
    {
      return jobDAO.resetJobLocks(getInstanceName(), status, newStatus);
    }
    catch (Throwable e)
    {
      throw new JobServiceException("Failed to reset the locks for the jobs with the " + "status ("
          + status + ") that have been locked using the lock name (" + getInstanceName() + ")", e);
    }
  }

  /**
   * Schedule the next unscheduled job for execution.
   *
   * @return <code>true</code> if there are more unscheduled jobs to schedule or <code>false</code>
   *         if there are no more unscheduled jobs to schedule
   *
   * @throws JobServiceException
   */
  public boolean scheduleNextUnscheduledJobForExecution()
    throws JobServiceException
  {
    try
    {
      return jobDAO.scheduleNextUnscheduledJobForExecution();
    }
    catch (Throwable e)
    {
      throw new JobServiceException("Failed to schedule the next unscheduled job for execution", e);
    }
  }

  /**
   * Unlock a locked job.
   *
   * @param id     the ID uniquely identifying the job
   * @param status the new status for the unlocked job
   *
   * @throws JobServiceException
   */
  public void unlockJob(String id, Job.Status status)
    throws JobServiceException
  {
    try
    {
      jobDAO.unlockJob(id, status);
    }
    catch (Throwable e)
    {
      throw new JobServiceException("Failed to unlock and set the status for the job (" + id
          + ") to (" + status + ")", e);
    }
  }

  /**
   * Retrieves the name of the Job Service instance.
   */
  private String getInstanceName()
  {
    if (instanceName == null)
    {
      String applicationName = null;

      try
      {
        applicationName = InitialContext.doLookup("java:app/AppName");
      }
      catch (Throwable ignored) {}

      if (applicationName == null)
      {
        try
        {
          applicationName = InitialContext.doLookup("java:comp/env/ApplicationName");
        }
        catch (Throwable ignored) {}
      }

      if (applicationName == null)
      {
        logger.error("Failed to retrieve the application name from JNDI using the names ("
            + "java:app/AppName) and (java:comp/env/ApplicationName) while constructing"
            + " the Job Service instance name");

        applicationName = "Unknown";
      }

      instanceName = applicationName + "::";

      try
      {
        java.net.InetAddress localMachine = java.net.InetAddress.getLocalHost();

        instanceName += localMachine.getHostName().toLowerCase();
      }
      catch (Throwable e)
      {
        logger.error("Failed to retrieve the server hostname while constructing the Job "
            + "Service instance name", e);
        instanceName = "Unknown";
      }

      // Check if we are running under JBoss and if so retrieve the server name
      if (System.getProperty("jboss.server.name") != null)
      {
        instanceName = instanceName + "::" + System.getProperty("jboss.server.name");
      }

      // Check if we are running under Glassfish and if so retrieve the server name
      else if (System.getProperty("glassfish.version") != null)
      {
        instanceName = instanceName + "::" + System.getProperty("com.sun.aas.instanceName");
      }

      // Check if we are running under WebSphere Application Server Community Edition (Geronimo)
      else if (System.getProperty("org.apache.geronimo.server.dir") != null)
      {
        instanceName = instanceName + "::Geronimo";
      }

      // Check if we are running under WebSphere Application Server Liberty Profile
      else if (System.getProperty("wlp.user.dir") != null)
      {
        instanceName = instanceName + "::WLP";
      }

      /*
       * Check if we are running under WebSphere and if so execute the code below to retrieve the
       *  server name.
       */
      else
      {
        try
        {
          instanceName = instanceName + "::" + InitialContext.doLookup("servername").toString();
        }
        catch (Throwable e)
        {
          logger.error("Failed to retrieve the name of the WebSphere server instance from JNDI"
              + " while constructing the Job Service instance name", e);
          instanceName = instanceName + "::Unknown";
        }
      }
    }

    return instanceName;
  }

  /**
   * Initialise the configuration for the <code>JobService</code> instance.
   *
   * @throws JobServiceException
   */
  private void initConfiguration()
    throws JobServiceException
  {
    try
    {
      // Initialise the configuration
      if (!registry.integerValueExists("/Services/JobService", "JobExecutionRetryDelay"))
      {
        registry.setIntegerValue("/Services/JobService", "JobExecutionRetryDelay", 600000);
      }

      if (!registry.integerValueExists("/Services/JobService", "MaximumJobExecutionAttempts"))
      {
        registry.setIntegerValue("/Services/JobService", "MaximumJobExecutionAttempts", 6 * 24);
      }

      jobExecutionRetryDelay = registry.getIntegerValue("/Services/JobService",
          "JobExecutionRetryDelay", 600000);

      maximumJobExecutionAttempts = registry.getIntegerValue("/Services/JobService",
          "MaximumJobExecutionAttempts", 6 * 24);
    }
    catch (Throwable e)
    {
      throw new JobServiceException("Failed to initialise the configuration for the Job Service",
          e);
    }
  }

  private void injectJob(Job job, IJob jobImplementation)
    throws JobServiceException
  {
    try
    {
      CDIUtil.inject(jobImplementation);
    }
    catch (Throwable e)
    {
      throw new JobServiceException("Failed in inject the job class (" + job.getJobClass()
          + ") for the job (" + job.getName() + ") with ID (" + job.getId() + ")", e);
    }
  }
}

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
 * The <code>TaskService</code> class provides the Task Service implementation.
 *
 * @author Marcus Portmann
 */
@ApplicationScoped
@Default
public class TaskService
  implements ITaskService
{
  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(TaskService.class);

  /* The name of the Task Service instance. */
  private String instanceName;

  /*
   * The maximum number of times execution will be attempted for a scheduled task.
   */
  private int maximumScheduledTaskExecutionAttempts;

  /* Registry */
  @Inject
  private IRegistry registry;

  /*
   * The delay in milliseconds between successive attempts to execute a scheduled task.
   */
  private int scheduledTaskExecutionRetryDelay;

  /* Task DAO */
  @Inject
  private ITaskDAO taskDAO;

  /**
   * Constructs a new <code>TaskService</code>.
   */
  public TaskService() {}

  /**
   * Execute the scheduled task.
   *
   * @param scheduledTask the scheduled task
   *
   * @throws TaskServiceException
   */
  public void executeScheduledTask(ScheduledTask scheduledTask)
    throws TaskServiceException
  {
    Class<?> taskClass;

    // Load the task class.
    try
    {
      taskClass =
        Thread.currentThread().getContextClassLoader().loadClass(scheduledTask.getTaskClass());
    }
    catch (Throwable e)
    {
      throw new TaskServiceException("Failed to execute the scheduled task ("
          + scheduledTask.getName() + ") with ID (" + scheduledTask.getId()
          + "): Failed to load the task class (" + scheduledTask.getTaskClass() + ")", e);
    }

    // Initialise the task
    ITask task;

    try
    {
      // Create a new instance of the task
      Object taskObject = taskClass.newInstance();

      // Check if the task is a valid task
      if (!(taskObject instanceof ITask))
      {
        throw new TaskServiceException("The task class (" + scheduledTask.getTaskClass()
            + ") does not implement the guru.mmp.application.task.ITask interface");
      }

      task = (ITask) taskObject;

      // Inject the task
      injectTask(scheduledTask, task);
    }
    catch (Throwable e)
    {
      throw new TaskServiceException("Failed to initialise the scheduled task ("
          + scheduledTask.getName() + ") with ID (" + scheduledTask.getId() + ")", e);
    }

    // Execute the task
    try
    {
      // Retrieve the parameters for the scheduled task
      List<ScheduledTaskParameter> scheduledTaskParameters =
        taskDAO.getScheduledTaskParameters(scheduledTask.getId());

      Map<String, String> parameters = new HashMap<>();

      for (ScheduledTaskParameter scheduledTaskParameter : scheduledTaskParameters)
      {
        parameters.put(scheduledTaskParameter.getName(), scheduledTaskParameter.getValue());
      }

      // Initialise the task execution context
      TaskExecutionContext context = new TaskExecutionContext(scheduledTask.getNextExecution(),
        parameters);

      // Execute the task
      task.execute(context);
    }
    catch (Throwable e)
    {
      throw new TaskServiceException("Failed to execute the scheduled task ("
          + scheduledTask.getName() + ") with ID (" + scheduledTask.getId() + ")", e);
    }
  }

  /**
   * Returns the maximum number of times execution will be attempted for a scheduled task.
   *
   * @return the maximum number of times execution will be attempted for a scheduled task
   */
  public int getMaximumScheduledTaskExecutionAttempts()
  {
    return maximumScheduledTaskExecutionAttempts;
  }

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
  public ScheduledTask getNextTaskScheduledForExecution()
    throws TaskServiceException
  {
    try
    {
      return taskDAO.getNextTaskScheduledForExecution(scheduledTaskExecutionRetryDelay,
          getInstanceName());
    }
    catch (Throwable e)
    {
      throw new TaskServiceException(
          "Failed to retrieve the next task that has been scheduled for execution", e);
    }
  }

  /**
   * Retrieve the parameters for the scheduled task with the specified ID.
   *
   * @param id the ID uniquely identifying the scheduled task
   *
   * @return the parameters for the scheduled task
   *
   * @throws TaskServiceException
   */
  public List<ScheduledTaskParameter> getScheduledTaskParameters(String id)
    throws TaskServiceException
  {
    try
    {
      return taskDAO.getScheduledTaskParameters(id);
    }
    catch (Throwable e)
    {
      throw new TaskServiceException("Failed to retrieve the parameters for the scheduled task ("
          + id + ")", e);
    }
  }

  /**
   * Increment the execution attempts for the scheduled task with the specified ID.
   *
   * @param id the ID uniquely identifying the scheduled task
   *
   * @throws TaskServiceException
   */
  public void incrementScheduledTaskExecutionAttempts(String id)
    throws TaskServiceException
  {
    try
    {
      taskDAO.incrementScheduledTaskExecutionAttempts(id);
    }
    catch (Throwable e)
    {
      throw new TaskServiceException(
          "Failed to increment the execution attempts for the scheduled task (" + id + ")", e);
    }
  }

  /**
   * Initialise the Task Service instance.
   *
   * @throws TaskServiceException
   */
  @PostConstruct
  public void init()
    throws TaskServiceException
  {
    logger.info("Initialising the Task Service instance (" + getInstanceName() + ")");

    try
    {
      // Initialise the configuration for the TaskService instance
      initConfiguration();
    }
    catch (Throwable e)
    {
      throw new TaskServiceException("Failed to initialise the Task Service", e);
    }
  }

  /**
   * Reschedule the task for execution.
   *
   * @param id                the ID uniquely identifying the scheduled task
   * @param schedulingPattern the cron-style scheduling pattern for the scheduled task used to
   *                          determine the next execution time
   *
   * @throws TaskServiceException
   */
  public void rescheduleTask(String id, String schedulingPattern)
    throws TaskServiceException
  {
    try
    {
      taskDAO.rescheduleTask(id, schedulingPattern);
    }
    catch (Throwable e)
    {
      throw new TaskServiceException("Failed to reschedule the task (" + id + ") for execution", e);
    }
  }

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
  public int resetScheduledTaskLocks(ScheduledTask.Status status, ScheduledTask.Status newStatus)
    throws TaskServiceException
  {
    try
    {
      return taskDAO.resetScheduledTaskLocks(getInstanceName(), status, newStatus);
    }
    catch (Throwable e)
    {
      throw new TaskServiceException("Failed to reset the locks for the scheduled tasks with the "
          + "status (" + status + ") that have been locked using the lock name ("
          + getInstanceName() + ")", e);
    }
  }

  /**
   * Schedule the next unscheduled task for execution.
   *
   * @return <code>true</code> if there are more unscheduled tasks to schedule or
   *         <code>false</code> if there are no more unscheduled tasks to schedule
   *
   * @throws TaskServiceException
   */
  public boolean scheduleNextUnscheduledTaskForExecution()
    throws TaskServiceException
  {
    try
    {
      return taskDAO.scheduleNextUnscheduledTaskForExecution();
    }
    catch (Throwable e)
    {
      throw new TaskServiceException("Failed to schedule the next unscheduled task for execution",
          e);
    }
  }

  /**
   * Unlock a locked scheduled task.
   *
   * @param id     the ID uniquely identifying the scheduled task
   * @param status the new status for the unlocked scheduled task
   *
   * @throws TaskServiceException
   */
  public void unlockScheduledTask(String id, ScheduledTask.Status status)
    throws TaskServiceException
  {
    try
    {
      taskDAO.unlockScheduledTask(id, status);
    }
    catch (Throwable e)
    {
      throw new TaskServiceException("Failed to unlock and set the status for the scheduled task ("
          + id + ") to (" + status + ")", e);
    }
  }

  /**
   * Retrieves the name of the Task Service instance.
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
            + " the Task Service instance name");

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
        logger.error("Failed to retrieve the server hostname while constructing the Task "
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
              + " while constructing the Task Service instance name", e);
          instanceName = instanceName + "::Unknown";
        }
      }
    }

    return instanceName;
  }

  /**
   * Initialise the configuration for the <code>TaskService</code> instance.
   *
   * @throws TaskServiceException
   */
  private void initConfiguration()
    throws TaskServiceException
  {
    try
    {
      // Initialise the configuration
      if (!registry.integerValueExists("/Services/TaskService", "ScheduledTaskExecutionRetryDelay"))
      {
        registry.setIntegerValue("/Services/TaskService", "ScheduledTaskExecutionRetryDelay",
            600000);
      }

      if (!registry.integerValueExists("/Services/TaskService",
          "MaximumScheduledTaskExecutionAttempts"))
      {
        registry.setIntegerValue("/Services/TaskService", "MaximumScheduledTaskExecutionAttempts",
            6 * 24);
      }

      scheduledTaskExecutionRetryDelay = registry.getIntegerValue("/Services/TaskService",
          "ScheduledTaskExecutionRetryDelay", 600000);

      maximumScheduledTaskExecutionAttempts = registry.getIntegerValue("/Services/TaskService",
          "MaximumScheduledTaskExecutionAttempts", 6 * 24);
    }
    catch (Throwable e)
    {
      throw new TaskServiceException("Failed to initialise the configuration for the Task Service",
          e);
    }
  }

  private void injectTask(ScheduledTask scheduledTask, ITask task)
    throws TaskServiceException
  {
    try
    {
      CDIUtil.inject(task);
    }
    catch (Throwable e)
    {
      throw new TaskServiceException("Failed in inject the task class ("
          + scheduledTask.getTaskClass() + ") for the scheduled task (" + scheduledTask.getName()
          + ") with ID (" + scheduledTask.getId() + ")", e);
    }
  }
}

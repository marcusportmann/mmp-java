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

package guru.mmp.application.process;

//~--- non-JDK imports --------------------------------------------------------

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;

import javax.annotation.PostConstruct;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;

import javax.inject.Inject;

import javax.naming.InitialContext;

/**
 * The <code>ProcessService</code> class provides the Process Service implementation.
 *
 * @author Marcus Portmann
 */
@ApplicationScoped
@Default
public class ProcessService
  implements IProcessService
{
  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(ProcessService.class);

  /* The name of the Process Service instance. */
  private String instanceName;

  /* The DAO providing persistence capabilities for the process infrastructure. */
  @Inject
  private IProcessDAO processDAO;

  /**
   * Constructs a new <code>ProcessService</code>.
   */
  public ProcessService() {}

  /**
   * Create the new process definition.
   *
   * @param processDefinition the <code>ProcessDefinition</code> instance containing the information
   *                         for the new process definition
   * @param createdBy        the username identifying the user that created the process definition
   *
   * @throws ProcessServiceException
   */
  public void createProcessDefinition(ProcessDefinition processDefinition, String createdBy)
    throws ProcessServiceException
  {
    if (processDefinitionExists(processDefinition.getId(), processDefinition.getVersion()))
    {
      throw new ProcessServiceException("A process definition with ID ("
          + processDefinition.getId() + ") and version (" + processDefinition.getVersion()
          + ") already exists");
    }

    try
    {
      processDAO.createProcessDefinition(processDefinition);
    }
    catch (Throwable e)
    {
      throw new ProcessServiceException("Failed to create the process definition with ID ("
          + processDefinition.getId() + ") and version (" + processDefinition.getVersion()
          + ")", e);
    }
  }

  /**
   * Delete all versions of the existing process definition.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the
   *           process definition
   *
   * @throws ProcessServiceException
   */
  public void deleteProcessDefinition(String id)
    throws ProcessServiceException
  {
    try
    {
      processDAO.deleteProcessDefinition(id);
    }
    catch (Throwable e)
    {
      throw new ProcessServiceException(
          "Failed to delete all versions of the process definition with ID (" + id + ")", e);
    }
  }

  /**
   * Execute the process instance.
   *
   * @param processInstance the process instance to execute
   *
   * @throws ProcessServiceException
   */
  public void executeProcessInstance(ProcessInstance processInstance)
    throws ProcessServiceException
  {
    try
    {
      // Retrieve the process definition for the process instance

    }
    catch (Throwable e)
    {
      throw new ProcessServiceException("Failed to execute the process instance ("
          + processInstance.getId() + ")", e);
    }

  }

  /**
   * Returns the summaries for the current versions of all the process definitions associated with
   * the organisation identified by the specified organisation code.
   *
   * @param organisation the organisation code identifying the organisation
   *
   * @return the summaries for the current versions of all the process definitions associated with
   *         the organisation identified by the specified organisation code
   *
   * @throws ProcessServiceException
   */
  public List<ProcessDefinitionSummary> getCurrentProcessDefinitionSummariesForOrganisation(
      String organisation)
    throws ProcessServiceException
  {
    try
    {
      return processDAO.getCurrentProcessDefinitionSummariesForOrganisation(organisation);
    }
    catch (Throwable e)
    {
      throw new ProcessServiceException(
          "Failed to retrieve the summaries for the current versions of the"
          + " process definitions for the organisation (" + organisation + ")", e);
    }
  }

  /**
   * Returns the current versions of all the process definitions associated with the organisation
   * identified by the specified organisation code.
   *
   * @param organisation the organisation code identifying the organisation
   *
   * @return the current versions of all the process definitions associated with the organisation
   *         identified by the specified organisation code
   *
   * @throws ProcessServiceException
   */
  public List<ProcessDefinition> getCurrentProcessDefinitionsForOrganisation(String organisation)
    throws ProcessServiceException
  {
    try
    {
      return processDAO.getCurrentProcessDefinitionsForOrganisation(organisation);
    }
    catch (Throwable e)
    {
      throw new ProcessServiceException(
          "Failed to retrieve the current versions of the process definitions"
          + " for the organisation (" + organisation + ")", e);
    }
  }

  /**
   * Retrieve the next process instance that is scheduled for execution.
   * <p/>
   * The process instance will be locked to prevent duplicate processing.
   *
   * @return the next process instance that is scheduled for execution or <code>null</code> if no
   *         process instances are currently scheduled for execution
   *
   * @throws ProcessServiceException
   */
  public ProcessInstance getNextProcessInstanceScheduledForExecution()
    throws ProcessServiceException
  {
    try
    {
      return processDAO.getNextProcessInstanceScheduledForExecution(getInstanceName());
    }
    catch (Throwable e)
    {
      throw new ProcessServiceException(
          "Failed to retrieve the next process instance that has been scheduled for execution", e);
    }
  }

  /**
   * Returns the number of process definitions associated with the organisation identified by the
   * specified organisation code.
   *
   * @param organisation the organisation code identifying the organisation
   *
   * @return the number of process definitions associated with the organisation identified by the
   *         specified organisation code
   *
   * @throws ProcessServiceException
   */
  public int getNumberOfProcessDefinitionsForOrganisation(String organisation)
    throws ProcessServiceException
  {
    try
    {
      return processDAO.getNumberOfProcessDefinitionsForOrganisation(organisation);
    }
    catch (Throwable e)
    {
      throw new ProcessServiceException(
          "Failed to retrieve the number of process definitions for the organisation ("
          + organisation + ")", e);
    }
  }

  /**
   * Retrieve the process definition with the specified ID and version.
   *
   * @param id      the Universally Unique Identifier (UUID) used to uniquely identify the
   *                process definition
   * @param version the version of the process definition
   *
   * @return the process definition with the specified ID and version or <code>null</code>
   *         if the process definition could not be found
   *
   * @throws ProcessServiceException
   */
  public ProcessDefinition getProcessDefinition(String id, int version)
    throws ProcessServiceException
  {
    try
    {
      return processDAO.getProcessDefinition(id, version);
    }
    catch (Throwable e)
    {
      throw new ProcessServiceException("Failed to retrieve the process definition with ID (" + id
          + ") and version (" + version + ")", e);
    }
  }

  /**
   * Retrieve the summary for the process definition with the specified ID and version.
   *
   * @param id      the Universally Unique Identifier (UUID) used to uniquely identify the
   *                process definition
   * @param version the version of the process definition
   *
   * @return the summary for the process definition with the specified ID and version or
   *         <code>null</code> if the process definition could not be found
   *
   * @throws ProcessServiceException
   */
  public ProcessDefinitionSummary getProcessDefinitionSummary(String id, int version)
    throws ProcessServiceException
  {
    try
    {
      return processDAO.getProcessDefinitionSummary(id, version);
    }
    catch (Throwable e)
    {
      throw new ProcessServiceException(
          "Failed to retrieve the summary for the process definition with ID (" + id
          + ") and version (" + version + ")", e);
    }
  }

  /**
   * Initialise the Process Service instance.
   *
   * @throws ProcessServiceException
   */
  @PostConstruct
  public void init()
    throws ProcessServiceException
  {
    logger.info("Initialising the Process Service instance (" + getInstanceName() + ")");

    try
    {
      // Initialise the configuration for the Process Service instance
      initConfiguration();
    }
    catch (Throwable e)
    {
      throw new ProcessServiceException("Failed to initialise the Process Service: "
          + e.getMessage());
    }
  }

  /**
   * Check whether the process definition with the specified ID and version exists in the database.
   *
   * @param id      the Universally Unique Identifier (UUID) used to uniquely identify the process
   *                definition
   * @param version the version of the process definition
   *
   * @return <code>true</code> if the process definition exists or <code>false</code> otherwise
   *
   * @throws ProcessServiceException
   */
  public boolean processDefinitionExists(String id, int version)
    throws ProcessServiceException
  {
    try
    {
      return processDAO.processDefinitionExists(id, version);
    }
    catch (Throwable e)
    {
      throw new ProcessServiceException("Failed to check whether the process definition with ID ("
          + id + ") and version (" + version + ") exists", e);
    }
  }

  /**
   * Reset the process instance locks.
   *
   * @param status    the current status of the process instances that have been locked
   * @param newStatus the new status for the process instances that have been unlocked
   *
   * @return the number of process instance locks reset
   *
   * @throws ProcessServiceException
   */
  public int resetProcessInstanceLocks(ProcessInstance.Status status,
      ProcessInstance.Status newStatus)
    throws ProcessServiceException
  {
    try
    {
      return processDAO.resetProcessInstanceLocks(getInstanceName(), status, newStatus);
    }
    catch (Throwable e)
    {
      throw new ProcessServiceException("Failed to reset the locks for the process instances with "
          + "the status (" + status + ") that have been locked using the lock name ("
          + getInstanceName() + ")", e);
    }
  }

  /**
   * Unlock a locked process instance.
   *
   * @param id     the Universally Unique Identifier (UUID) used to uniquely identify the process
   *               instance
   * @param status the new status for the unlocked process instance
   *
   * @throws ProcessServiceException
   */
  public void unlockProcessInstance(String id, ProcessInstance.Status status)
    throws ProcessServiceException
  {
    try
    {
      processDAO.unlockProcessInstance(id, status);
    }
    catch (Throwable e)
    {
      throw new ProcessServiceException(
          "Failed to unlock and set the status for the process instance (" + id + ") to (" + status
          + ")", e);
    }
  }

  /**
   * Update the state for process instance with the specified ID.
   *
   * @param id   the Universally Unique Identifier (UUID) used to uniquely identify the process
   *             instance
   * @param data the data giving the current execution state for the process instance
   *
   * @throws ProcessServiceException
   */
  public void updateProcessInstanceData(String id, byte[] data)
    throws ProcessServiceException
  {
    try
    {
      processDAO.updateProcessInstanceData(id, data);
    }
    catch (Throwable e)
    {
      throw new ProcessServiceException("Failed to update the data for the process instance (" + id
          + ")", e);
    }
  }

  /**
   * Retrieves the name of the Process Service instance.
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
            + " the Process Service instance name");

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
        logger.error("Failed to retrieve the server hostname while constructing the Process"
            + " Service instance name", e);
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
       * server name.
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
              + " while constructing the Process Service instance name", e);
          instanceName = instanceName + "::Unknown";
        }
      }
    }

    return instanceName;
  }

  /**
   * Initialise the configuration for the Process Service instance.
   *
   * @throws ProcessServiceException
   */
  private void initConfiguration()
    throws ProcessServiceException
  {
//  try
//  {
//  }
//  catch (Throwable e)
//  {
//    throw new ProcessServiceException(
//        "Failed to initialise the configuration for the Process Service", e);
//  }
  }
}

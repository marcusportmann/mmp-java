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

package guru.mmp.application.process;

import guru.mmp.application.util.ServiceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import java.util.List;
import java.util.UUID;

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
  private String instanceName = ServiceUtil.getServiceInstanceName("Process Service");

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
   *                          for the new process definition
   *
   * @throws ProcessServiceException
   */
  public void createProcessDefinition(ProcessDefinition processDefinition)
    throws ProcessServiceException
  {
    if (processDefinitionExists(processDefinition.getId(), processDefinition.getVersion()))
    {
      throw new ProcessServiceException(
        String.format("A process definition with ID (%s) and version (%d) already exists",
          processDefinition.getId(), processDefinition.getVersion()));
    }

    try
    {
      processDAO.createProcessDefinition(processDefinition);
    }
    catch (Throwable e)
    {
      throw new ProcessServiceException(
        String.format("Failed to create the process definition with ID (%s) and version (%d)",
          processDefinition.getId(), processDefinition.getVersion()), e);
    }
  }

  /**
   * Delete all versions of the existing process definition.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the process
   *           definition
   *
   * @throws ProcessServiceException
   */
  public void deleteProcessDefinition(UUID id)
    throws ProcessServiceException
  {
    try
    {
      processDAO.deleteProcessDefinition(id);
    }
    catch (Throwable e)
    {
      throw new ProcessServiceException(
        String.format("Failed to delete all versions of the process definition with ID (%s)", id),
        e);
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
      throw new ProcessServiceException(
        "Failed to execute the process instance (" + processInstance.getId() + ")", e);
    }
  }

  /**
   * Returns the summaries for the current versions of all the process definitions.
   *
   * @return the summaries for the current versions of all the process definitions
   *
   * @throws ProcessServiceException
   */
  public List<ProcessDefinitionSummary> getCurrentProcessDefinitionSummaries()
    throws ProcessServiceException
  {
    try
    {
      return processDAO.getCurrentProcessDefinitionSummaries();
    }
    catch (Throwable e)
    {
      throw new ProcessServiceException(
        "Failed to retrieve the summaries for the current versions of the process definitions", e);
    }
  }

  /**
   * Returns the current versions of all the process definitions.
   *
   * @return the current versions of all the process definitions
   *
   * @throws ProcessServiceException
   */
  public List<ProcessDefinition> getCurrentProcessDefinitions()
    throws ProcessServiceException
  {
    try
    {
      return processDAO.getCurrentProcessDefinitions();
    }
    catch (Throwable e)
    {
      throw new ProcessServiceException(
        "Failed to retrieve the current versions of the process definitions", e);
    }
  }

  /**
   * Retrieve the next process instance that is scheduled for execution.
   * <p/>
   * The process instance will be locked to prevent duplicate processing.
   *
   * @return the next process instance that is scheduled for execution or <code>null</code> if no
   * process instances are currently scheduled for execution
   *
   * @throws ProcessServiceException
   */
  public ProcessInstance getNextProcessInstanceScheduledForExecution()
    throws ProcessServiceException
  {
    try
    {
      return processDAO.getNextProcessInstanceScheduledForExecution(instanceName);
    }
    catch (Throwable e)
    {
      throw new ProcessServiceException(
        "Failed to retrieve the next process instance that has been scheduled for execution", e);
    }
  }

  /**
   * Returns the number of process definitions.
   *
   * @return the number of process definitions
   *
   * @throws ProcessServiceException
   */
  public int getNumberOfProcessDefinitions()
    throws ProcessServiceException
  {
    try
    {
      return processDAO.getNumberOfProcessDefinitions();
    }
    catch (Throwable e)
    {
      throw new ProcessServiceException("Failed to retrieve the number of process definitions", e);
    }
  }

  /**
   * Retrieve the process definition and version.
   *
   * @param id      the Universally Unique Identifier (UUID) used to uniquely identify the
   *                process definition
   * @param version the version of the process definition
   *
   * @return the process definition and version or <code>null</code> if the process definition
   * could not be found
   *
   * @throws ProcessServiceException
   */
  public ProcessDefinition getProcessDefinition(UUID id, int version)
    throws ProcessServiceException
  {
    try
    {
      return processDAO.getProcessDefinition(id, version);
    }
    catch (Throwable e)
    {
      throw new ProcessServiceException(
        String.format("Failed to retrieve the process definition with ID (%s) and version (%d)", id,
          version), e);
    }
  }

  /**
   * Retrieve the summary for the process definition and version.
   *
   * @param id      the Universally Unique Identifier (UUID) used to uniquely identify the
   *                process definition
   * @param version the version of the process definition
   *
   * @return the summary for the process definition and version or <code>null</code> if the process
   * definition could not be found
   *
   * @throws ProcessServiceException
   */
  public ProcessDefinitionSummary getProcessDefinitionSummary(UUID id, int version)
    throws ProcessServiceException
  {
    try
    {
      return processDAO.getProcessDefinitionSummary(id, version);
    }
    catch (Throwable e)
    {
      throw new ProcessServiceException(String.format(
        "Failed to retrieve the summary for the process definition with ID (%s) and version (%d)",
        id, version), e);
    }
  }

  /**
   * Initialise the Process Service instance.
   */
  @PostConstruct
  public void init()
  {
    logger.info(String.format("Initialising the Process Service instance (%s)", instanceName));

    try
    {
      // Initialise the configuration for the Process Service instance
      initConfiguration();
    }
    catch (Throwable e)
    {
      throw new RuntimeException("Failed to initialise the Process Service", e);
    }
  }

  /**
   * Check whether the process definition and version exists in the database.
   *
   * @param id      the Universally Unique Identifier (UUID) used to uniquely identify the process
   *                definition
   * @param version the version of the process definition
   *
   * @return <code>true</code> if the process definition exists or <code>false</code> otherwise
   *
   * @throws ProcessServiceException
   */
  public boolean processDefinitionExists(UUID id, int version)
    throws ProcessServiceException
  {
    try
    {
      return processDAO.processDefinitionExists(id, version);
    }
    catch (Throwable e)
    {
      throw new ProcessServiceException(String.format(
        "Failed to check whether the process definition with ID (%s) and version (%d) exists", id,
        version), e);
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
  public int resetProcessInstanceLocks(
    ProcessInstance.Status status, ProcessInstance.Status newStatus)
    throws ProcessServiceException
  {
    try
    {
      return processDAO.resetProcessInstanceLocks(instanceName, status, newStatus);
    }
    catch (Throwable e)
    {
      throw new ProcessServiceException(String.format(
        "Failed to reset the locks for the process instances with the status (%s) that have been " +
          "locked using the lock name (%s)", status, instanceName), e);
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
  public void unlockProcessInstance(UUID id, ProcessInstance.Status status)
    throws ProcessServiceException
  {
    try
    {
      processDAO.unlockProcessInstance(id, status);
    }
    catch (Throwable e)
    {
      throw new ProcessServiceException(
        String.format("Failed to unlock and set the status for the process instance (%s) to (%s)",
          id, status), e);
    }
  }

  /**
   * Update the state for process instance.
   *
   * @param id   the Universally Unique Identifier (UUID) used to uniquely identify the process
   *             instance
   * @param data the data giving the current execution state for the process instance
   *
   * @throws ProcessServiceException
   */
  public void updateProcessInstanceData(UUID id, byte[] data)
    throws ProcessServiceException
  {
    try
    {
      processDAO.updateProcessInstanceData(id, data);
    }
    catch (Throwable e)
    {
      throw new ProcessServiceException(
        String.format("Failed to update the data for the process instance (%s)", id), e);
    }
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

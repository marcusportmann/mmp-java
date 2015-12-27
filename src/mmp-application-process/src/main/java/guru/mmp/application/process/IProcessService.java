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

//~--- JDK imports ------------------------------------------------------------

import java.util.List;
import java.util.UUID;

/**
 * The <code>IProcessService</code> interface defines the functionality that must be provided
 * by a Process Service implementation.
 *
 * @author Marcus Portmann
 */
public interface IProcessService
{
  /**
   * The username used to identify operations performed by the system.
   */
  String SYSTEM_USERNAME = "SYSTEM";

  /**
   * Create the new process definition.
   *
   * @param processDefinition the <code>ProcessDefinition</code> instance containing the information
   *                          for the new process definition
   *
   * @throws ProcessServiceException
   */
  void createProcessDefinition(ProcessDefinition processDefinition)
    throws ProcessServiceException;

  /**
   * Delete all versions of the existing process definition.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the process
   *           definition
   *
   * @throws ProcessServiceException
   */
  void deleteProcessDefinition(UUID id)
    throws ProcessServiceException;

  /**
   * Execute the process instance.
   *
   * @param processInstance the process instance to execute
   *
   * @throws ProcessServiceException
   */
  void executeProcessInstance(ProcessInstance processInstance)
    throws ProcessServiceException;

  /**
   * Returns the summaries for the current versions of all the process definitions.
   *
   * @return the summaries for the current versions of all the process definitions
   *
   * @throws ProcessServiceException
   */
  List<ProcessDefinitionSummary> getCurrentProcessDefinitionSummaries()
    throws ProcessServiceException;

  /**
   * Returns the current versions of all the process definitions.
   *
   * @return the current versions of all the process definitions
   *
   * @throws ProcessServiceException
   */
  List<ProcessDefinition> getCurrentProcessDefinitions()
    throws ProcessServiceException;

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
  ProcessInstance getNextProcessInstanceScheduledForExecution()
    throws ProcessServiceException;

  /**
   * Returns the number of process definitions.
   *
   * @return the number of process definitions
   *
   * @throws ProcessServiceException
   */
  int getNumberOfProcessDefinitions()
    throws ProcessServiceException;

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
  ProcessDefinition getProcessDefinition(UUID id, int version)
    throws ProcessServiceException;

  /**
   * Retrieve the summary for the process definition and version.
   *
   * @param id      the Universally Unique Identifier (UUID) used to uniquely identify the
   *                process definition
   * @param version the version of the process definition
   *
   * @return the summary for the process definition and version or <code>null</code> if the process
   *         definition could not be found
   *
   * @throws ProcessServiceException
   */
  ProcessDefinitionSummary getProcessDefinitionSummary(UUID id, int version)
    throws ProcessServiceException;

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
  boolean processDefinitionExists(UUID id, int version)
    throws ProcessServiceException;

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
  int resetProcessInstanceLocks(ProcessInstance.Status status, ProcessInstance.Status newStatus)
    throws ProcessServiceException;

  /**
   * Unlock a locked process instance.
   *
   * @param id     the Universally Unique Identifier (UUID) used to uniquely identify the process
   *               instance
   * @param status the new status for the unlocked process instance
   *
   * @throws ProcessServiceException
   */
  void unlockProcessInstance(UUID id, ProcessInstance.Status status)
    throws ProcessServiceException;

  /**
   * Update the state for process instance.
   *
   * @param id   the Universally Unique Identifier (UUID) used to uniquely identify the process
   *             instance
   * @param data the data giving the current execution state for the process instance
   *
   * @throws ProcessServiceException
   */
  void updateProcessInstanceData(UUID id, byte[] data)
    throws ProcessServiceException;
}

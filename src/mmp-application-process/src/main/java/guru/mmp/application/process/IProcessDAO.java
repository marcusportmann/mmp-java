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

import guru.mmp.common.persistence.DAOException;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;

/**
 * The <code>IProcessDAO</code> interface defines the process-related persistence operations.
 *
 * @author Marcus Portmann
 */
public interface IProcessDAO
{
  /**
   * Create the new process definition.
   *
   * @param processDefinition the <code>ProcessDefinition</code> instance containing the information
   *                         for the new process definition
   *
   * @throws DAOException
   */
  void createProcessDefinition(ProcessDefinition processDefinition)
    throws DAOException;

  /**
   * Create the new process instance.
   *
   * @param processInstance the <code>ProcessInstance</code> instance containing the information
   *                        for the new process instance
   *
   * @throws DAOException
   */
  void createProcessInstance(ProcessInstance processInstance)
    throws DAOException;

  /**
   * Delete all versions of the existing process definition.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the process
   *           definition
   *
   * @throws DAOException
   */
  void deleteProcessDefinition(String id)
    throws DAOException;

  /**
   * Delete the process instance.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the process
   *           instance
   *
   * @throws DAOException
   */
  void deleteProcessInstance(String id)
    throws DAOException;

  /**
   * Returns the summaries for the current versions of all the process definitions associated with
   * the organisation identified by the specified organisation code.
   *
   * @param organisation the organisation code identifying the organisation
   *
   * @return the summaries for the current versions of all the process definitions associated with
   *         the organisation identified by the specified organisation code
   *
   * @throws DAOException
   */
  List<ProcessDefinitionSummary> getCurrentProcessDefinitionSummariesForOrganisation(
      String organisation)
    throws DAOException;

  /**
   * Returns the current versions of all the process definitions associated with the organisation
   * identified by the specified organisation code.
   *
   * @param organisation the organisation code identifying the organisation
   *
   * @return the current versions of all the process definitions associated with the organisation
   *         identified by the specified organisation code
   *
   * @throws DAOException
   */
  List<ProcessDefinition> getCurrentProcessDefinitionsForOrganisation(String organisation)
    throws DAOException;

  /**
   * Retrieve the next process instance that is scheduled for execution.
   * <p/>
   * The process instance will be locked to prevent duplicate processing.
   *
   * @param lockName the name of the lock that should be applied to the process instance scheduled
   *                 for execution when it is retrieved
   *
   * @return the next process instance that is scheduled for execution or <code>null</code> if no
   *         process instances are currently scheduled for execution
   *
   * @throws DAOException
   */
  ProcessInstance getNextProcessInstanceScheduledForExecution(String lockName)
    throws DAOException;

  /**
   * Returns the number of process definitions associated with the organisation identified by the
   * specified organisation code.
   *
   * @param organisation the organisation code identifying the organisation
   *
   * @return the number of process definitions associated with the organisation identified by the
   *         specified organisation code
   *
   * @throws DAOException
   */
  int getNumberOfProcessDefinitionsForOrganisation(String organisation)
    throws DAOException;

  /**
   * Returns the number of process instances associated with the organisation identified by the
   * specified organisation code.
   *
   * @param organisation the organisation code identifying the organisation
   *
   * @return the number of process instances associated with the organisation identified by the
   *         specified organisation code
   *
   * @throws DAOException
   */
  int getNumberOfProcessInstancesForOrganisation(String organisation)
    throws DAOException;

  /**
   * Retrieve the process definition and version.
   *
   * @param id      the Universally Unique Identifier (UUID) used to uniquely identify the
   *                process definition
   * @param version the version of the process definition
   *
   * @return the process definition and version or <code>null</code> if the
   *         process definition could not be found
   *
   * @throws DAOException
   */
  ProcessDefinition getProcessDefinition(String id, int version)
    throws DAOException;

  /**
   * Retrieve the summary for the process definition and version.
   *
   * @param id      the Universally Unique Identifier (UUID) used to uniquely identify the
   *                process definition
   * @param version the version of the process definition
   *
   * @return the summary for the process definition and version or
   *         <code>null</code> if the process definition could not be found
   *
   * @throws DAOException
   */
  ProcessDefinitionSummary getProcessDefinitionSummary(String id, int version)
    throws DAOException;

  /**
   * Retrieve the process instance.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the process
   *           instance
   *
   * @return the process instance or <code>null</code> if the process
   *         instance could not be found
   *
   * @throws DAOException
   */
  ProcessInstance getProcessInstance(String id)
    throws DAOException;

  /**
   * Returns the summaries for the all the process instances associated with the organisation
   * identified by the specified organisation code.
   *
   * @param organisation the organisation code identifying the organisation
   *
   * @return the summaries for the all the process instances associated with the organisation
   *         identified by the specified organisation code
   *
   * @throws DAOException
   */
  List<ProcessInstanceSummary> getProcessInstanceSummariesForOrganisation(String organisation)
    throws DAOException;

  /**
   * Retrieve the summary for the process instance.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the process
   *           instance
   *
   * @return the summary for the process instance or <code>null</code> if the
   *         process definition could not be found
   *
   * @throws DAOException
   */
  ProcessInstanceSummary getProcessInstanceSummary(String id)
    throws DAOException;

  /**
   * Check whether the process definition and version exists in the database.
   *
   * @param id      the Universally Unique Identifier (UUID) used to uniquely identify the process
   *                definition
   * @param version the version of the process definition
   *
   * @return <code>true</code> if the process definition exists or <code>false</code> otherwise
   *
   * @throws DAOException
   */
  boolean processDefinitionExists(String id, int version)
    throws DAOException;

  /**
   * Check whether the process instance exists in the database.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the process
   *           instance
   *
   * @return <code>true</code> if the process instance exists or <code>false</code> otherwise
   *
   * @throws DAOException
   */
  boolean processInstanceExists(String id)
    throws DAOException;

  /**
   * Reset the process instance locks.
   *
   * @param lockName  the name of the lock applied by the entity that has locked the
   *                  process instances
   * @param status    the current status of the process instances that have been locked
   * @param newStatus the new status for the process instances that have been unlocked
   *
   * @return the number of process instance locks reset
   *
   * @throws DAOException
   */
  int resetProcessInstanceLocks(String lockName, ProcessInstance.Status status,
      ProcessInstance.Status newStatus)
    throws DAOException;

  /**
   * Unlock a locked process instance.
   *
   * @param id     the Universally Unique Identifier (UUID) used to uniquely identify the process
   *               instance
   * @param status the new status for the unlocked process instance
   *
   * @throws DAOException
   */
  void unlockProcessInstance(String id, ProcessInstance.Status status)
    throws DAOException;

  /**
   * Update the state for process instance.
   *
   * @param id   the Universally Unique Identifier (UUID) used to uniquely identify the process
   *             instance
   * @param data the data giving the current execution state for the process instance
   *
   * @throws DAOException
   */
  void updateProcessInstanceData(String id, byte[] data)
    throws DAOException;
}

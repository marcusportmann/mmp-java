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

import guru.mmp.application.persistence.DAOException;

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
   * Delete the existing process definition.
   *
   * @param id the ID uniquely identifying the process definition
   *
   * @throws DAOException
   */
  void deleteProcessDefinition(String id)
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
   * Retrieve the process definition with the specified ID.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the
   *           process definition
   *
   * @return the process definition with the specified ID or <code>null</code> if the process
   *         definition could not be found
   *
   * @throws DAOException
   */
  ProcessDefinition getProcessDefinition(String id)
    throws DAOException;

  /**
   * Returns all the process definitions associated with the organisation identified by the
   * specified organisation code.
   *
   * @param organisation the organisation code identifying the organisation
   *
   * @return all the process definitions associated with the organisation identified by the
   *         specified organisation code
   *
   * @throws DAOException
   */
  List<ProcessDefinition> getProcessDefinitionsForOrganisation(String organisation)
    throws DAOException;

  /**
   * Check whether the process definition with the specified ID exists in the database.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the process
   *           definition
   *
   * @return <code>true</code> if the process definition exists or <code>false</code> otherwise
   *
   * @throws DAOException
   */
  boolean processDefinitionExists(String id)
    throws DAOException;

  /**
   * Update the process definition.
   *
   * @param processDefinition the <code>ProcessDefinition</code> instance containing the updated
   *                         information for the process definition
   * @param updatedBy        the username identifying the user that updated the process definition
   *
   * @return the updated process definition
   *
   * @throws DAOException
   */
  ProcessDefinition updateProcessDefinition(ProcessDefinition processDefinition, String updatedBy)
    throws DAOException;  
}

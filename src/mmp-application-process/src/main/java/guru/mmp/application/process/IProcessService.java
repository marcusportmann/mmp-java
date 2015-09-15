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

import java.util.List;

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
   * Delete the existing process definition.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the
   *           process definition
   *
   * @throws ProcessServiceException
   */
  void deleteProcessDefinition(String id)
    throws ProcessServiceException;

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
  int getNumberOfProcessDefinitionsForOrganisation(String organisation)
    throws ProcessServiceException;

  /**
   * Retrieve the process definition with the specified ID.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the
   *           process definition
   *
   * @return the process definition with the specified ID or <code>null</code> if the process
   *         definition could not be found
   *
   * @throws ProcessServiceException
   */
  ProcessDefinition getProcessDefinition(String id)
    throws ProcessServiceException;

  /**
   * Retrieve the summary for the process definition with the specified ID.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the
   *           process definition
   *
   * @return the summary for the process definition with the specified ID or <code>null</code> if
   *         the process definition could not be found
   *
   * @throws ProcessServiceException
   */
  ProcessDefinitionSummary getProcessDefinitionSummary(String id)
    throws ProcessServiceException;

  /**
   * Returns all the process definitions associated with the organisation identified by the specified
   * organisation code.
   *
   * @param organisation the organisation code identifying the organisation
   *
   * @return all the process definitions associated with the organisation identified by the specified
   *         organisation code
   *
   * @throws ProcessServiceException
   */
  List<ProcessDefinition> getProcessDefinitionsForOrganisation(String organisation)
    throws ProcessServiceException;

  /**
   * Returns the summaries for all the process definitions associated with the organisation
   * identified by the specified organisation code.
   *
   * @param organisation the organisation code identifying the organisation
   *
   * @return the summaries for all the process definitions associated with the organisation
   *         identified by the specified organisation code
   *
   * @throws ProcessServiceException
   */
  List<ProcessDefinitionSummary> getProcessDefinitionSummariesForOrganisation(String organisation)
    throws ProcessServiceException;

  /**
   * Check whether the process definition with the specified ID exists in the database.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the process
   *           definition
   *
   * @return <code>true</code> if the process definition exists or <code>false</code> otherwise
   *
   * @throws ProcessServiceException
   */
  boolean processDefinitionExists(String id)
    throws ProcessServiceException;

  /**
   * Save the process definition.
   * <p/>
   * This will create a new entry for the process definition in the database or update the
   * existing entry.
   *
   * @param processDefinition the <code>ProcessDefinition</code> instance containing the information
   *                         for the process definition
   * @param savedBy          the username identifying the user that saved the process definition
   *
   * @return the saved process definition
   *
   * @throws ProcessServiceException
   */
  ProcessDefinition saveProcessDefinition(ProcessDefinition processDefinition, String savedBy)
    throws ProcessServiceException;
}

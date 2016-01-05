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

import java.io.Serializable;
import java.util.Date;

/**
 * The <code>ProcessInstanceSummary</code> class holds the summary information for a process
 * instance.
 *
 * @author Marcus Portmann
 */
public class ProcessInstanceSummary
  implements Serializable
{
  private static final long serialVersionUID = 1000000;

  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the process definition for
   * the process instance.
   */
  private String definitionId;

  /**
   * The name of the process definition for the process instance.
   */
  private String definitionName;

  /**
   * The version of the process definition for the process instance.
   */
  private int definitionVersion;

  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the process instance.
   */
  private String id;

  /**
   * The name of the entity that has locked the process instance for execution.
   */
  private String lockName;

  /**
   * The date and time when the process instance will next be executed.
   */
  private Date nextExecution;

  /**
   * The status of the process instance.
   */
  private ProcessInstance.Status status;

  /**
   * Constructs a new <code>ProcessInstanceSummary</code>.
   */
  public ProcessInstanceSummary() {}

  /**
   * Constructs a new <code>ProcessInstanceSummary</code>.
   *
   * @param id                the Universally Unique Identifier (UUID) used to uniquely identify
   *                          the process instance
   * @param definitionId      the Universally Unique Identifier (UUID) used to uniquely identify
   *                          the process definition for the process instance
   * @param definitionVersion the version of the process definition for the process instance
   * @param definitionName    the name of the process definition for the process instance
   * @param status            the status of the process instance
   * @param nextExecution     the date and time when the process instance will next be executed
   * @param lockName          the name of the entity that has locked the process instance for
   *                          execution
   */
  public ProcessInstanceSummary(
    String id, String definitionId, int definitionVersion, String definitionName,
    ProcessInstance.Status status, Date nextExecution, String lockName)
  {
    this.id = id;
    this.definitionId = definitionId;
    this.definitionVersion = definitionVersion;
    this.definitionName = definitionName;
    this.status = status;
    this.nextExecution = nextExecution;
    this.lockName = lockName;
  }

  /**
   * Returns the Universally Unique Identifier (UUID) used to uniquely identify the process
   * definition for the process instance.
   *
   * @return the Universally Unique Identifier (UUID) used to uniquely identify the process
   * definition for the process instance
   */
  public String getDefinitionId()
  {
    return definitionId;
  }

  /**
   * Returns the name of the process definition for the process instance.
   *
   * @return the name of the process definition for the process instance
   */
  public String getDefinitionName()
  {
    return definitionName;
  }

  /**
   * Returns the version of the process definition for the process instance.
   *
   * @return the version of the process definition for the process instance
   */
  public int getDefinitionVersion()
  {
    return definitionVersion;
  }

  /**
   * Returns the Universally Unique Identifier (UUID) used to uniquely identify the process
   * instance.
   *
   * @return the Universally Unique Identifier (UUID) used to uniquely identify the process
   * instance
   */
  public String getId()
  {
    return id;
  }

  /**
   * Returns the name of the entity that has locked the process instance for execution.
   *
   * @return the name of the entity that has locked the process instance for execution
   */
  public String getLockName()
  {
    return lockName;
  }

  /**
   * Returns the date and time when the process instance will next be executed.
   *
   * @return the date and time when the process instance will next be executed
   */
  public Date getNextExecution()
  {
    return nextExecution;
  }

  /**
   * Returns the status of the process instance.
   *
   * @return the status of the process instance
   */
  public ProcessInstance.Status getStatus()
  {
    return status;
  }

  /**
   * Returns a string representation of the object.
   *
   * @return a string representation of the object
   */
  @Override
  public String toString()
  {
    return String.format(
      "ProcessInstanceSummary {id=\"%s\", definitionId=\"%s\", definitionVersion=\"%d\"}", getId(),
      getDefinitionId(), getDefinitionVersion());
  }
}

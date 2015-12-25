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

import java.io.Serializable;

import java.util.Date;
import java.util.UUID;

/**
 * The <code>ProcessInstance</code> class holds the information for a process instance.
 *
 * @author Marcus Portmann
 */
public class ProcessInstance
  implements Serializable
{
  private static final long serialVersionUID = 1000000;

  /**
   * The data giving the current execution state for the process instance.
   */
  private byte[] data;

  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the process definition for
   * the process instance.
   */
  private UUID definitionId;

  /**
   * The version of the process definition for the process instance.
   */
  private int definitionVersion;

  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the process instance.
   */
  private UUID id;

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
  private Status status;

  /**
   * Constructs a new <code>ProcessInstance</code>.
   */
  public ProcessInstance() {}

  /**
   * Constructs a new <code>ProcessInstance</code>.
   *
   * @param id                the Universally Unique Identifier (UUID) used to uniquely identify
   *                          the process instance
   * @param definitionId      the Universally Unique Identifier (UUID) used to uniquely identify
   *                          the process definition for the process instance
   * @param definitionVersion the version of the process definition for the process instance
   * @param data              the data giving the current execution state for the process instance
   * @param status            the status of the process instance
   * @param nextExecution     the date and time when the process instance will next be executed
   * @param lockName          the name of the entity that has locked the process instance for
   *                          execution
   */
  public ProcessInstance(UUID id, UUID definitionId, int definitionVersion, byte[] data,
      Status status, Date nextExecution, String lockName)
  {
    this.id = id;
    this.definitionId = definitionId;
    this.definitionVersion = definitionVersion;
    this.data = data;
    this.status = status;
    this.nextExecution = nextExecution;
    this.lockName = lockName;
  }

  /**
   * The enumeration giving the possible statuses for a process instance.
   */
  public enum Status
  {
    UNKNOWN(0, "Unknown"), SCHEDULED(1, "Scheduled"), EXECUTING(2, "Executing"),
    COMPLETED(3, "Completed"), FAILED(4, "Failed"), ANY(-1, "Any");

    private int code;
    private String name;

    Status(int code, String name)
    {
      this.code = code;
      this.name = name;
    }

    /**
     * Returns the status given by the specified numeric code value.
     *
     * @param code the numeric code value identifying the status
     *
     * @return the status given by the specified numeric code value
     */
    public static Status fromCode(int code)
    {
      switch (code)
      {
        case 1:
          return Status.SCHEDULED;

        case 2:
          return Status.EXECUTING;

        case 3:
          return Status.COMPLETED;

        case 4:
          return Status.FAILED;

        case -1:
          return Status.ANY;

        default:
          return Status.UNKNOWN;
      }
    }

    /**
     * Returns the numeric code value identifying the status.
     *
     * @return the numeric code value identifying the status
     */
    public int getCode()
    {
      return code;
    }

    /**
     * Returns the <code>String</code> value of the numeric code value identifying the status.
     *
     * @return the <code>String</code> value of the numeric code value identifying the status
     */
    public String getCodeAsString()
    {
      return String.valueOf(code);
    }

    /**
     * Returns the name of the status.
     *
     * @return the name of the status
     */
    public String getName()
    {
      return name;
    }

    /**
     * Return the string representation of the status enumeration value.
     *
     * @return the string representation of the status enumeration value
     */
    public String toString()
    {
      return name;
    }
  }

  /**
   * Returns the data giving the current execution state for the process instance.
   *
   * @return the data giving the current execution state for the process instance
   */
  public byte[] getData()
  {
    return data;
  }

  /**
   * Returns the Universally Unique Identifier (UUID) used to uniquely identify the process
   * definition for the process instance.
   *
   * @return the Universally Unique Identifier (UUID) used to uniquely identify the process
   *         definition for the process instance
   */
  public UUID getDefinitionId()
  {
    return definitionId;
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
   *         instance
   */
  public UUID getId()
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
  public Status getStatus()
  {
    return status;
  }

  /**
   * Set the data giving the current execution state for the process instance.
   *
   * @param data the data giving the current execution state for the process instance
   */
  public void setData(byte[] data)
  {
    this.data = data;
  }

  /**
   * Set the Universally Unique Identifier (UUID) used to uniquely identify the process definition
   * for the process instance.
   *
   * @param definitionId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                     process definition for the process instance
   */
  public void setDefinitionId(UUID definitionId)
  {
    this.definitionId = definitionId;
  }

  /**
   * Set the version of the process definition for the process instance.
   *
   * @param definitionVersion the version of the process definition for the process instance
   */
  public void setDefinitionVersion(int definitionVersion)
  {
    this.definitionVersion = definitionVersion;
  }

  /**
   * Set the Universally Unique Identifier (UUID) used to uniquely identify the process instance.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the process
   *           instance
   */
  public void setId(UUID id)
  {
    this.id = id;
  }

  /**
   * Set the name of the entity that has locked the process instance for execution.
   *
   * @param lockName the name of the entity that has locked the process instance for execution
   */
  public void setLockName(String lockName)
  {
    this.lockName = lockName;
  }

  /**
   * Set the date and time when the process instance will next be executed.
   *
   * @param nextExecution the date and time when the process instance will next be executed
   */
  public void setNextExecution(Date nextExecution)
  {
    this.nextExecution = nextExecution;
  }

  /**
   * Set the status of the process instance.
   *
   * @param status the status of the process instance
   */
  public void setStatus(Status status)
  {
    this.status = status;
  }

  /**
   * Returns a string representation of the object.
   *
   * @return a string representation of the object
   */
  @Override
  public String toString()
  {
    return "ProcessInstance {" + "id=\"" + getId() + "\", " + "definitionId=\"" + getDefinitionId()
        + "\", " + "definitionVersion=\"" + getDefinitionVersion() + "\"}";
  }
}

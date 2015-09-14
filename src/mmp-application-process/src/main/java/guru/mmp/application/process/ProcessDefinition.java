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

import java.util.UUID;

/**
 * The <code>ProcessDefinition</code> class holds the information for a process definition.
 *
 * @author Marcus Portmann
 */
public class ProcessDefinition
  implements Serializable
{
  private static final long serialVersionUID = 1000000;

  /**
   * The data for the process definition.
   */
  private byte[] data;

  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the process definition.
   */
  private String id;

  /**
   * The name of the process definition.
   */
  private String name;

  /**
   * The organisation code identifying the organisation the process definition is associated with.
   */
  private String organisation;

  /**
   * Constructs a new <code>ProcessDefinition</code>.
   */
  public ProcessDefinition() {}

  /**
   * Constructs a new <code>ProcessDefinition</code>.
   *
   * @param organisation the organisation code identifying the organisation the process definition
   *                     is associated with
   * @param name         the name of the process definition
   * @param data         the data for the process definition
   */
  public ProcessDefinition(String organisation, String name, byte[] data)
  {
    this.id = UUID.randomUUID().toString();
    this.organisation = organisation;
    this.name = name;
    this.data = data;
  }

  /**
   * Constructs a new <code>ProcessDefinition</code>.
   *
   * @param id           the Universally Unique Identifier (UUID) used to uniquely identify the
   *                     process definition
   * @param organisation the organisation code identifying the organisation the process definition
   *                     is associated with
   * @param name         the name of the process definition
   * @param data         the data for the process definition
   */
  public ProcessDefinition(String id, String organisation, String name, byte[] data)
  {
    this.id = id;
    this.organisation = organisation;
    this.name = name;
    this.data = data;
  }

  /**
   * Returns the Universally Unique Identifier (UUID) used to uniquely identify the process
   * definition.
   *
   * @return the Universally Unique Identifier (UUID) used to uniquely identify the process
   *         definition
   */
  public String getId()
  {
    return id;
  }

  /**
   * Returns the name of the process definition.
   *
   * @return the name of the process definition
   */
  public String getName()
  {
    return name;
  }

  /**
   * Returns the organisation code identifying the organisation the process definition is associated
   * with.
   *
   * @return the organisation code identifying the organisation the process definition is associated
   *         with
   */
  public String getOrganisation()
  {
    return organisation;
  }

  /**
   * Returns the data for the process definition.
   *
   * @return the data for the process definition
   */
  public byte[] getData()
  {
    return data;
  }

  /**
   * Set the Universally Unique Identifier (UUID) used to uniquely identify the process definition.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the process
   *           definition
   */
  public void setId(String id)
  {
    this.id = id;
  }

  /**
   * Set the name of the process definition.
   *
   * @param name the name of the process definition
   */
  public void setName(String name)
  {
    this.name = name;
  }

  /**
   * Set the organisation code identifying the organisation the process definition is associated
   * with.
   *
   * @param organisation the organisation code identifying the organisation the process definition
   *                     is associated with
   */
  public void setOrganisation(String organisation)
  {
    this.organisation = organisation;
  }

  /**
   * Set the data for the process definition.
   *
   * @param data the data for the process definition
   */
  public void setData(byte[] data)
  {
    this.data = data;
  }

  /**
   * Returns a string representation of the object.
   *
   * @return a string representation of the object
   */
  @Override
  public String toString()
  {
    return "ProcessDefinition {" + "id=\"" + getId() + "\", " + "organisation=\""
        + getOrganisation() + "\", " + "name=\"" + getName() + "\"}";
  }
}

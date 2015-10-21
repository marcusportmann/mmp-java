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

/**
 * The <code>ProcessDefinitionSummary</code> class holds the summary information for a process
 * definition.
 *
 * @author Marcus Portmann
 */
public class ProcessDefinitionSummary
  implements Serializable
{
  private static final long serialVersionUID = 1000000;

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
   * The version of the process definition.
   */
  private int version;

  /**
   * Constructs a new <code>ProcessDefinitionSummary</code>.
   *
   * @param id           the Universally Unique Identifier (UUID) used to uniquely identify the
   *                     process definition
   * @param version      the version of the process definition
   * @param organisation the organisation code identifying the organisation the process definition
   *                     is associated with
   * @param name         the name of the process definition
   */
  public ProcessDefinitionSummary(String id, int version, String organisation, String name)
  {
    this.id = id;
    this.version = version;
    this.organisation = organisation;
    this.name = name;
  }

  /**
   * Constructs a new <code>ProcessDefinitionSummary</code>.
   */
  @SuppressWarnings("unused")
  private ProcessDefinitionSummary() {}

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
   * Returns the version of the process definition.
   *
   * @return the version of the process definition
   */
  public int getVersion()
  {
    return version;
  }

  /**
   * Set the version of the process definition.
   *
   * @param version the version of the process definition
   */
  public void setVersion(int version)
  {
    this.version = version;
  }

  /**
   * Returns a string representation of the object.
   *
   * @return a string representation of the object
   */
  @Override
  public String toString()
  {
    return "ProcessDefinitionSummary {" + "id=\"" + getId() + "\", " + "organisation=\""
        + getOrganisation() + "\", " + "name=\"" + getName() + "\"}";
  }
}

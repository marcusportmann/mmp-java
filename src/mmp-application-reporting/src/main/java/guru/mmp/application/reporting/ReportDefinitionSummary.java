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

package guru.mmp.application.reporting;

//~--- JDK imports ------------------------------------------------------------

import java.io.Serializable;
import java.util.UUID;

/**
 * The <code>ReportDefinitionSummary</code> class holds the summary information for a report
 * definition.
 *
 * @author Marcus Portmann
 */
public class ReportDefinitionSummary
  implements Serializable
{
  private static final long serialVersionUID = 1000000;

  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the report definition.
   */
  private String id;

  /**
   * The name of the report definition.
   */
  private String name;

  /**
   * The organisation code identifying the organisation the report definition is associated with.
   */
  private String organisation;

  /**
   * Constructs a new <code>ReportDefinitionSummary</code>.
   */
  public ReportDefinitionSummary() {}

  /**
   * Constructs a new <code>ReportDefinitionSummary</code>.
   *
   * @param organisation the organisation code identifying the organisation the report definition
   *                     is associated with
   * @param name         the name of the report definition
   */
  public ReportDefinitionSummary(String organisation, String name)
  {
    this.id = UUID.randomUUID().toString();
    this.organisation = organisation;
    this.name = name;
  }

  /**
   * Constructs a new <code>ReportDefinitionSummary</code>.
   *
   * @param id           the Universally Unique Identifier (UUID) used to uniquely identify the
   *                     report definition
   * @param organisation the organisation code identifying the organisation the report definition
   *                     is associated with
   * @param name         the name of the report definition
   */
  public ReportDefinitionSummary(String id, String organisation, String name)
  {
    this.id = id;
    this.organisation = organisation;
    this.name = name;
  }

  /**
   * Returns the Universally Unique Identifier (UUID) used to uniquely identify the report
   * definition.
   *
   * @return the Universally Unique Identifier (UUID) used to uniquely identify the report
   *         definition
   */
  public String getId()
  {
    return id;
  }

  /**
   * Returns the name of the report definition.
   *
   * @return the name of the report definition
   */
  public String getName()
  {
    return name;
  }

  /**
   * Returns the organisation code identifying the organisation the report definition is associated
   * with.
   *
   * @return the organisation code identifying the organisation the report definition is associated
   *         with
   */
  public String getOrganisation()
  {
    return organisation;
  }

  /**
   * Set the Universally Unique Identifier (UUID) used to uniquely identify the report definition.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the report
   *           definition
   */
  public void setId(String id)
  {
    this.id = id;
  }

  /**
   * Set the name of the report definition.
   *
   * @param name the name of the report definition
   */
  public void setName(String name)
  {
    this.name = name;
  }

  /**
   * Set the organisation code identifying the organisation the report definition is associated
   * with.
   *
   * @param organisation the organisation code identifying the organisation the report definition
   *                     is associated with
   */
  public void setOrganisation(String organisation)
  {
    this.organisation = organisation;
  }

  /**
   * Returns a string representation of the object.
   *
   * @return a string representation of the object
   */
  @Override
  public String toString()
  {
    return "ReportDefinitionSummary {" + "id=\"" + getId() + "\", " + "organisation=\""
      + getOrganisation() + "\", " + "name=\"" + getName() + "\"}";
  }
}

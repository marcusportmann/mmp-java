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
 * The <code>ReportDefinition</code> class holds the information for a report definition.
 *
 * @author Marcus Portmann
 */
public class ReportDefinition
  implements Serializable
{
  private static final long serialVersionUID = 1000000;

  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the report definition.
   */
  private UUID id;

  /**
   * The name of the report definition.
   */
  private String name;

  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the organisation the report
   * definition is associated with.
   */
  private UUID organisationId;

  /**
   * The JasperReports template for the report definition.
   */
  private byte[] template;

  /**
   * Constructs a new <code>ReportDefinition</code>.
   */
  public ReportDefinition() {}

  /**
   * Constructs a new <code>ReportDefinition</code>.
   *
   * @param organisationId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                       organisation the report definition is associated with
   * @param name           the name of the report definition
   * @param template       the JasperReports template for the report definition
   */
  public ReportDefinition(UUID organisationId, String name, byte[] template)
  {
    this.id = UUID.randomUUID();
    this.organisationId = organisationId;
    this.name = name;
    this.template = template;
  }

  /**
   * Constructs a new <code>ReportDefinition</code>.
   *
   * @param id             the Universally Unique Identifier (UUID) used to uniquely identify the
   *                       report definition
   * @param organisationId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                       organisation the report definition is associated with
   * @param name           the name of the report definition
   * @param template       the JasperReports template for the report definition
   */
  public ReportDefinition(UUID id, UUID organisationId, String name, byte[] template)
  {
    this.id = id;
    this.organisationId = organisationId;
    this.name = name;
    this.template = template;
  }

  /**
   * Returns the Universally Unique Identifier (UUID) used to uniquely identify the report
   * definition.
   *
   * @return the Universally Unique Identifier (UUID) used to uniquely identify the report
   *         definition
   */
  public UUID getId()
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
   * Returns the Universally Unique Identifier (UUID) used to uniquely identify the organisation
   * the report definition is associated with.
   *
   * @return the Universally Unique Identifier (UUID) used to uniquely identify the organisation
   *         the report definition is associated with
   */
  public UUID getOrganisationId()
  {
    return organisationId;
  }

  /**
   * Returns the JasperReports template for the report definition.
   *
   * @return the JasperReports template for the report definition
   */
  public byte[] getTemplate()
  {
    return template;
  }

  /**
   * Set the Universally Unique Identifier (UUID) used to uniquely identify the report definition.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the report
   *           definition
   */
  public void setId(UUID id)
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
   * Set the Universally Unique Identifier (UUID) used to uniquely identify the organisation the
   * report definition is associated with.
   *
   * @param organisationId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                       organisation the report definition is associated with
   */
  public void setOrganisationId(UUID organisationId)
  {
    this.organisationId = organisationId;
  }

  /**
   * Set the JasperReports template for the report definition.
   *
   * @param template the JasperReports template for the report definition
   */
  public void setTemplate(byte[] template)
  {
    this.template = template;
  }

  /**
   * Returns a string representation of the object.
   *
   * @return a string representation of the object
   */
  @Override
  public String toString()
  {
    return "ReportDefinition {" + "id=\"" + getId() + "\", " + "organisationId=\""
        + getOrganisationId() + "\", " + "name=\"" + getName() + "\"}";
  }
}

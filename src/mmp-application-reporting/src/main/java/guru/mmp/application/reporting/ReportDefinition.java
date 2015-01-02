/*
 * Copyright 2014 Marcus Portmann
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

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.common.util.StringUtil;

//~--- JDK imports ------------------------------------------------------------

import java.io.Serializable;

import java.util.Date;
import java.util.UUID;

/**
 * The <class>ReportDefinition</class> class holds the information for a report definition.
 *
 * @author Marcus Portmann
 */
public class ReportDefinition
  implements Serializable
{
  private static final long serialVersionUID = 1000000;

  /**
   * The username identifying the user that created the report definition.
   */
  public String createdBy;

  /**
   * The date and time the report definition was created.
   */
  private Date created;

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
   * The JasperReports template for the report definition.
   */
  private byte[] template;

  /**
   * The date and time the report definition was updated.
   */
  private Date updated;

  /**
   * The username identifying the user that updated the report definition.
   */
  private String updatedBy;

  /**
   * Constructs a new <code>ReportDefinition</code>.
   */
  public ReportDefinition() {}

  /**
   * Constructs a new <code>ReportDefinition</code>.
   *
   * The UUID used to uniquely identify the report definition will be generated using the
   * <code>UUIDGenerator</code>. The <code>UUIDGenerator</code> singleton must have been
   * successfully initialised prior to invoking this constructor.
   *
   * @param organisation the organisation code identifying the organisation the report definition
   *                     is associated with
   * @param name         the name of the report definition
   * @param template     the JasperReports template for the report definition
   * @param createdBy    the user ID identifying the user that created the report definition
   */
  public ReportDefinition(String organisation, String name, byte[] template, String createdBy)
  {
    this.id = UUID.randomUUID().toString();
    this.organisation = organisation;
    this.name = name;
    this.template = template;
    this.created = new Date();
    this.createdBy = createdBy;
  }

  /**
   * Constructs a new <code>ReportDefinition</code>.
   *
   * The UUID used to uniquely identify the report definition will be generated using the
   * <code>UUIDGenerator</code>. The <code>UUIDGenerator</code> singleton must have been
   * successfully initialised prior to invoking this constructor.
   *
   * @param id           the Universally Unique Identifier (UUID) used to uniquely identify the
   *                     report definition
   * @param organisation the organisation code identifying the organisation the report definition
   *                     is associated with
   * @param name         the name of the report definition
   * @param template     the JasperReports template for the report definition
   * @param created      the date and time the report definition was created
   * @param createdBy    the user ID identifying the user that created the report definition
   * @param updated      the date and time the report definition was updated
   * @param updatedBy    the username identifying the user that updated the report definition
   */
  public ReportDefinition(String id, String organisation, String name, byte[] template,
      Date created, String createdBy, Date updated, String updatedBy)
  {
    this.id = id;
    this.organisation = organisation;
    this.name = name;
    this.template = template;
    this.created = created;
    this.createdBy = createdBy;
    this.updated = updated;
    this.updatedBy = updatedBy;
  }

  /**
   * Returns the date and time the report definition was created.
   *
   * @return the date and time the report definition was created
   */
  public Date getCreated()
  {
    return created;
  }

  /**
   * Returns the username identifying the user that created the report definition.
   *
   * @return the username identifying the user that created the report definition
   */
  public String getCreatedBy()
  {
    return createdBy;
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
   * Returns the JasperReports template for the report definition.
   *
   * @return the JasperReports template for the report definition
   */
  public byte[] getTemplate()
  {
    return template;
  }

  /**
   * Returns the date and time the report definition was updated.
   *
   * @return the date and time the report definition was updated
   */
  public Date getUpdated()
  {
    return updated;
  }

  /**
   * Returns the date and time the report definition was updated as a <code>String</code>.
   *
   * @return the date and time the report definition was updated as a <code>String</code>
   */
  public String getUpdatedAsString()
  {
    return (updated == null)
        ? "N/A"
        : StringUtil.convertDateToString(updated);
  }

  /**
   * Returns the username identifying the user that updated the report definition.
   *
   * @return the username identifying the user that updated the report definition
   */
  public String getUpdatedBy()
  {
    return updatedBy;
  }

  /**
   * Set the date and time the report definition was created.
   *
   * @param created the date and time the report definition was created
   */
  public void setCreated(Date created)
  {
    this.created = created;
  }

  /**
   * Set the username identifying the user that created the report definition.
   *
   * @param createdBy the username identifying the user that created the report definition
   */
  public void setCreatedBy(String createdBy)
  {
    this.createdBy = createdBy;
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
   * Set the JasperReports template for the report definition.
   *
   * @param template the JasperReports template for the report definition
   */
  public void setTemplate(byte[] template)
  {
    this.template = template;
  }

  /**
   * Set the date and time the report definition was updated.
   *
   * @param updated the date and time the report definition was updated
   */
  public void setUpdated(Date updated)
  {
    this.updated = updated;
  }

  /**
   * Set the username identifying the user that updated the report definition.
   *
   * @param updatedBy the username identifying the user that updated the report definition
   */
  public void setUpdatedBy(String updatedBy)
  {
    this.updatedBy = updatedBy;
  }

  /**
   * Returns a string representation of the object.
   *
   * @return a string representation of the object
   */
  @Override
  public String toString()
  {
    return "ReportDefinition {" + "id=\"" + getId() + "\", " + "organisation=\""
        + getOrganisation() + "\", " + "name=\"" + getName() + "\", " + "created=\"" + getCreated()
        + "\", " + "createdBy=\"" + getCreatedBy() + "\", " + "updated=\"" + ((getUpdated() == null)
        ? ""
        : getUpdated()) + "\"" + "updatedBy=\"" + ((getUpdatedBy() == null)
        ? ""
        : getUpdatedBy()) + "\"" + "}";
  }
}

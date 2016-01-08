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

package guru.mmp.application.messaging.message;

import guru.mmp.application.security.Organisation;
import guru.mmp.common.util.StringUtil;
import guru.mmp.common.wbxml.Element;

import java.io.Serializable;
import java.util.UUID;

/**
 * The <code>OrganisationData</code> class stores the information for an organisation.
 *
 * @author Marcus Portmann
 */
public class OrganisationData
  implements Serializable
{
  private static final long serialVersionUID = 1000000;

  /**
   * The description for the organisation.
   */
  private String description;

  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the organisation.
   */
  private UUID id;

  /**
   * The name of the organisation.
   */
  private String name;

  /**
   * Constructs a new <code>OrganisationData</code>.
   */
  public OrganisationData() {}

  /**
   * Constructs a new <code>OrganisationData</code>.
   *
   * @param element the WBXML element containing the organisation data
   */
  public OrganisationData(Element element)
  {
    try
    {
      this.id = UUID.fromString(element.getChildText("Id"));
      this.name = StringUtil.notNull(element.getChildText("Name"));
      this.description = StringUtil.notNull(element.getChildText("Description"));
    }
    catch (Throwable e)
    {
      throw new RuntimeException("Failed to extract the organisation data from the WBXML", e);
    }
  }

  /**
   * Constructs a new <code>OrganisationData</code>.
   *
   * @param organisation the <code>Organisation</code> instance containing the organisation data
   */
  public OrganisationData(Organisation organisation)
  {
    this.id = organisation.getId();
    this.name = organisation.getName();
    this.description = organisation.getDescription();
  }

  /**
   * Returns the description for the organisation.
   *
   * @return the description for the organisation
   */
  public String getDescription()
  {
    return description;
  }

  /**
   * Returns the Universally Unique Identifier (UUID) used to uniquely identify the organisation.
   *
   * @return the Universally Unique Identifier (UUID) used to uniquely identify the organisation
   */
  public UUID getId()
  {
    return id;
  }

  /**
   * Returns the name of the organisation.
   *
   * @return the name of the organisation
   */
  public String getName()
  {
    return name;
  }

  /**
   * Set the description for the organisation.
   *
   * @param description the description for the organisation
   */
  public void setDescription(String description)
  {
    this.description = description;
  }

  /**
   * Set the Universally Unique Identifier (UUID) used to uniquely identify the organisation.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the organisation
   */
  public void setId(UUID id)
  {
    this.id = id;
  }

  /**
   * Set the name of the organisation.
   *
   * @param name the name of the organisation
   */
  public void setName(String name)
  {
    this.name = name;
  }

  /**
   * Returns the WBXML element containing the organisation data.
   *
   * @return the WBXML element containing the organisation data
   */
  public Element toElement()
  {
    Element organisationElement = new Element("Organisation");

    organisationElement.addContent(new Element("Id", id.toString()));
    organisationElement.addContent(new Element("Name", StringUtil.notNull(name)));
    organisationElement.addContent(new Element("Description", StringUtil.notNull(description)));

    return organisationElement;
  }
}

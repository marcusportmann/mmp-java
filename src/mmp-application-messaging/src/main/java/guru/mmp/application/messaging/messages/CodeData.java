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

package guru.mmp.application.messaging.messages;

import guru.mmp.application.codes.Code;
import guru.mmp.common.util.StringUtil;
import guru.mmp.common.wbxml.Element;

import java.io.Serializable;
import java.util.UUID;

/**
 * The <code>CodeData</code> class stores the information for a code.
 *
 * @author Marcus Portmann
 */
public class CodeData
  implements Serializable
{
  private static final long serialVersionUID = 1000000;

  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the category the code is
   * associated with.
   */
  private UUID categoryId;

  /**
   * The description for the code.
   */
  private String description;

  /**
   * The ID used to uniquely identify the code.
   */
  private String id;

  /**
   * The name of the code.
   */
  private String name;

  /**
   * The value for the code.
   */
  private String value;

  /**
   * Constructs a new <code>CodeData</code>.
   */
  public CodeData() {}

  /**
   * Constructs a new <code>CodeData</code>.
   *
   * @param code the <code>Code</code> instance containing the code data
   */
  public CodeData(Code code)
  {
    this.id = String.valueOf(code.getId());
    this.categoryId = code.getCategoryId();
    this.name = code.getName();
    this.description = code.getDescription();
    this.value = code.getValue();
  }

  /**
   * Constructs a new <code>CodeData</code>.
   *
   * @param element the WBXML element containing the code data
   */
  public CodeData(Element element)
  {
    try
    {
      this.id = element.getChildText("Id");
      this.categoryId = UUID.fromString(element.getChildText("CategoryId"));
      this.name = StringUtil.notNull(element.getChildText("Name"));
      this.description = StringUtil.notNull(element.getChildText("Description"));
      this.value = StringUtil.notNull(element.getChildText("Value"));
    }
    catch (Throwable e)
    {
      throw new RuntimeException("Failed to extract the code data from the WBXML", e);
    }
  }

  /**
   * Constructs a new <code>CodeData</code>.
   *
   * @param categoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                   category the code is associated with
   * @param code       the <code>guru.mmp.services.codes.ws.Code</code> instance containing the
   *                   code data
   */
  public CodeData(UUID categoryId, guru.mmp.service.codes.ws.Code code)
  {
    this.id = code.getId();
    this.categoryId = categoryId;
    this.name = code.getName();
    this.description = code.getDescription();
    this.value = code.getValue();
  }

  /**
   * Returns the Universally Unique Identifier (UUID) used to uniquely identify the category the
   * code is associated with.
   *
   * @return the Universally Unique Identifier (UUID) used to uniquely identify the category the
   * code is associated with
   */
  public UUID getCategoryId()
  {
    return categoryId;
  }

  /**
   * Returns the description for the code.
   *
   * @return the description for the code
   */
  public String getDescription()
  {
    return description;
  }

  /**
   * Returns the ID used to uniquely identify the code.
   *
   * @return the ID used to uniquely identify the code
   */
  public String getId()
  {
    return id;
  }

  /**
   * Returns the name of the code.
   *
   * @return the name of the code
   */
  public String getName()
  {
    return name;
  }

  /**
   * Returns the value for the code.
   *
   * @return the value for the code
   */
  public String getValue()
  {
    return value;
  }

  /**
   * Set the Universally Unique Identifier (UUID) used to uniquely identify the category the code
   * is associated with.
   *
   * @param categoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                   category the code is associated with
   */
  public void setCategoryId(UUID categoryId)
  {
    this.categoryId = categoryId;
  }

  /**
   * Set the description for the code.
   *
   * @param description the description for the code
   */
  public void setDescription(String description)
  {
    this.description = description;
  }

  /**
   * Set the ID used to uniquely identify the code.
   *
   * @param id the ID used to uniquely identify the code
   */
  public void setId(String id)
  {
    this.id = id;
  }

  /**
   * Set the name of the code.
   *
   * @param name the name of the code
   */
  public void setName(String name)
  {
    this.name = name;
  }

  /**
   * Set the value for the code.
   *
   * @param value the value for the code
   */
  public void setValue(String value)
  {
    this.value = value;
  }

  /**
   * Returns the WBXML element containing the code data.
   *
   * @return the WBXML element containing the code data
   */
  public Element toElement()
  {
    Element codeElement = new Element("Code");

    codeElement.addContent(new Element("Id", StringUtil.notNull(id)));
    codeElement.addContent(new Element("CategoryId", categoryId.toString()));
    codeElement.addContent(new Element("Name", StringUtil.notNull(name)));
    codeElement.addContent(new Element("Description", StringUtil.notNull(description)));
    codeElement.addContent(new Element("Value", StringUtil.notNull(value)));

    return codeElement;
  }
}

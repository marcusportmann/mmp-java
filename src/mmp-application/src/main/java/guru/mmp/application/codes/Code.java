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

package guru.mmp.application.codes;

//~--- JDK imports ------------------------------------------------------------

import java.io.Serializable;
import java.util.UUID;

/**
 * The <code>Code</code> class holds the information for a code.
 *
 * @author Marcus Portmann
 */
public class Code
  implements Serializable
{
  private static final long serialVersionUID = 1000000;

  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the code category the code
   * is associated with.
   */
  private UUID categoryId;

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
   * Constructs a new <code>Code</code>.
   */
  public Code() {}

  /**
   * Constructs a new <code>Code</code>.
   *
   * @param id         the ID used to uniquely identify the code
   * @param categoryId the Universally Unique Identifier (UUID) used to uniquely identify the code
   *                   category the code is associated with
   * @param name       the name of the code
   * @param value      the value for the code
   */
  public Code(String id, UUID categoryId, String name, String value)
  {
    this.id = id;
    this.categoryId = categoryId;
    this.name = name;
    this.value = value;
  }

  /**
   * Returns the Universally Unique Identifier (UUID) used to uniquely identify the code category
   * the code is associated with.
   *
   * @return the Universally Unique Identifier (UUID) used to uniquely identify the code category
   * the code is associated with
   */
  public UUID getCategoryId()
  {
    return categoryId;
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
    return this.value;
  }

  /**
   * Set the Universally Unique Identifier (UUID) used to uniquely identify the code category the
   * code is associated with.
   *
   * @param categoryId the Universally Unique Identifier (UUID) used to uniquely identify the code
   *                   category the code is associated with
   */
  public void setCategoryId(UUID categoryId)
  {
    this.categoryId = categoryId;
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
}

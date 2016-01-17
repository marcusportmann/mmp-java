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

package guru.mmp.application.security;

import java.util.UUID;

/**
 * The <code>Organisation</code> class stores the information for an organisation.
 *
 * @author Marcus Portmann
 */
public class Organisation
  implements java.io.Serializable
{
  private static final long serialVersionUID = 1000000;

  private UUID id;

  private String name;

  /**
   * Constructs a new <code>Organisation</code>.
   */
  public Organisation() {}

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
}

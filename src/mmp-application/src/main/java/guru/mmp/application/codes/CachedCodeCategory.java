/*
 * Copyright 2017 Marcus Portmann
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * The <code>CachedCodeCategory</code> class holds the information for a cached code category.
 *
 * @author Marcus Portmann
 */
class CachedCodeCategory
  implements Serializable
{
  private static final long serialVersionUID = 1000000;

  /**
   * The date and time the code category was cached.
   */
  private LocalDateTime cached;

  /**
   * The custom code data for the cached code category.
   */
  private String codeData;

  /**
   * The codes for the cached code category.
   */
  private List<Code> codes;

  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the cached code category.
   */
  private UUID id;

  /**
   * The date and time the cached code category was last updated.
   */
  private LocalDateTime lastUpdated;

  /**
   * Constructs a new <code>CachedCodeCategory</code>.
   */
  @SuppressWarnings("unused")
  public CachedCodeCategory() {}

  /**
   * Constructs a new <code>CachedCodeCategory</code>.
   *
   * @param id          the Universally Unique Identifier (UUID) used to uniquely identify the
   *                    cached code category
   * @param codeData    the custom code data for the cached code category
   * @param lastUpdated the date and time the cached code category was last updated
   * @param cached      the date and time the code category was cached
   */
  CachedCodeCategory(UUID id, String codeData, LocalDateTime lastUpdated, LocalDateTime cached)
  {
    this.id = id;
    this.codeData = codeData;
    this.lastUpdated = lastUpdated;
    this.cached = cached;
  }

  /**
   * Returns the codes for the cached code category.
   *
   * @return the codes for the cached code category
   */
  public List<Code> getCodes()
  {
    return codes;
  }

  /**
   * Returns the Universally Unique Identifier (UUID) used to uniquely identify the cached code
   * category.
   *
   * @return the Universally Unique Identifier (UUID) used to uniquely identify the cached code
   * category
   */
  public UUID getId()
  {
    return id;
  }

  /**
   * Set the custom code data for the cached code category.
   *
   * @param codeData the custom code data for the cached code category
   */
  @SuppressWarnings("unused")
  public void setCodeData(String codeData)
  {
    this.codeData = codeData;
  }

  /**
   * Set the codes for the cached code category.
   *
   * @param codes the codes for the cached code category
   */
  public void setCodes(List<Code> codes)
  {
    this.codes = codes;
  }

  /**
   * Set the Universally Unique Identifier (UUID) used to uniquely identify the cached code
   * category.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the cached code
   *           category
   */
  public void setId(UUID id)
  {
    this.id = id;
  }

  /**
   * Set the date and time the cached code category was last updated.
   *
   * @param lastUpdated the date and time the cached code category was last updated
   */
  @SuppressWarnings("unused")
  public void setLastUpdated(LocalDateTime lastUpdated)
  {
    this.lastUpdated = lastUpdated;
  }

  /**
   * Returns the date and time the code category was cached.
   *
   * @return the date and time the code category was cached
   */
  LocalDateTime getCached()
  {
    return cached;
  }

  /**
   * Returns the custom code data for the cached code category.
   *
   * @return the custom code data for the cached code category
   */
  String getCodeData()
  {
    return codeData;
  }

  /**
   * Returns the date and time the cached code category was last updated.
   *
   * @return the date and time the cached code category was last updated
   */
  LocalDateTime getLastUpdated()
  {
    return lastUpdated;
  }
}

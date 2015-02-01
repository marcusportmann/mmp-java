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

package guru.mmp.application.codes;

//~--- JDK imports ------------------------------------------------------------

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * The <class>CachedCodeCategory</class> class holds the information for a cached code category.
 *
 * @author Marcus Portmann
 */
public class CachedCodeCategory
  implements Serializable
{
  private static final long serialVersionUID = 1000000;

  /**
   * The date and time the code category was cached.
   */
  private Date cached;

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
  private String id;

  /**
   * The date and time the cached code category was last updated.
   */
  private Date lastUpdated;

  /**
   * Constructs a new <code>CachedCodeCategory</code>.
   */
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
  public CachedCodeCategory(String id, String codeData, Date lastUpdated, Date cached)
  {
    this.id = id;
    this.codeData = codeData;
    this.lastUpdated = lastUpdated;
    this.cached = cached;
  }

  /**
   * Returns the date and time the code category was cached.
   *
   * @return the date and time the code category was cached
   */
  public Date getCached()
  {
    return cached;
  }

  /**
   * Returns the custom code data for the cached code category.
   *
   * @return the custom code data for the cached code category
   */
  public String getCodeData()
  {
    return codeData;
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
   *         category
   */
  public String getId()
  {
    return id;
  }

  /**
   * Returns the date and time the cached code category was last updated.
   *
   * @return the date and time the cached code category was last updated
   */
  public Date getLastUpdated()
  {
    return lastUpdated;
  }

  /**
   * Set the date and time the code category was cached.
   *
   * @param cached the date and time the code category was cached
   */
  public void setCached(Date cached)
  {
    this.cached = cached;
  }

  /**
   * Set the custom code data for the cached code category.
   *
   * @param codeData the custom code data for the cached code category
   */
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
  public void setId(String id)
  {
    this.id = id;
  }

  /**
   * Set the date and time the cached code category was last updated.
   *
   * @param lastUpdated the date and time the cached code category was last updated
   */
  public void setLastUpdated(Date lastUpdated)
  {
    this.lastUpdated = lastUpdated;

  }
}

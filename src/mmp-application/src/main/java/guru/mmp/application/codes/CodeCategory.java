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
 * The <code>CodeCategory</code> class holds the information for a code category.
 *
 * @author Marcus Portmann
 */
public class CodeCategory
  implements Serializable
{
  /**
   * The default time in seconds after which the cached code data for the remote code category will
   * expire, which is 1 day.
   */
  public static final int DEFAULT_CACHE_EXPIRY = 60 * 60 * 24;
  private static final long serialVersionUID = 1000000;

  /**
   * The time in seconds after which the cached code data for the remote code category will expire.
   */
  private Integer cacheExpiry;

  /**
   * The type of code category e.g. Local, RemoteHTTPService, RemoteWebService, etc.
   */
  private CodeCategoryType categoryType;

  /**
   * The custom code data for the code category.
   */
  private String codeData;

  /**
   * The codes for the code category.
   */
  private List<Code> codes;

  /**
   * The date and time the code category was created.
   */
  private Date created;

  /**
   * The username identifying the user that created the code category.
   */
  private String createdBy;

  /**
   * The description for the code category.
   */
  private String description;

  /**
   * The endpoint if this is a remote code category.
   */
  private String endPoint;

  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the code category.
   */
  private String id;

  /**
   * Is the code data retrieved for the remote code category cacheable?
   */
  private boolean isCacheable;

  /**
   * Is the endpoint for the remote code category secure?
   */
  private boolean isEndPointSecure;

  /**
   * The name of the code category.
   */
  private String name;

  /**
   * The organisation code identifying the organisation the code category is associated with.
   */
  private String organisation;

  /**
   * The date and time the code category was updated.
   */
  private Date updated;

  /**
   * The username identifying the user that updated the code category.
   */
  private String updatedBy;

  /**
   * Constructs a new <code>CodeCategory</code>.
   */
  public CodeCategory() {}

  /**
   * Constructs a new <code>CodeCategory</code>.
   *
   * @param id               the Universally Unique Identifier (UUID) used to uniquely identify the
   *                         code category
   * @param organisation     the organisation code identifying the organisation the code category
   *                         is associated with
   * @param categoryType     the type of code category e.g. Local, RemoteHTTPService,
   *                         RemoteWebService, etc
   * @param name             the name of the code category
   * @param description      the description for the code category
   * @param endPoint         the endpoint if this is a remote code category
   * @param isEndPointSecure is the endpoint for the remote code category secure
   * @param isCacheable      is the code data retrieved for the remote code category cacheable
   * @param cacheExpiry      the time in seconds after which the cached code data for the remote
   *                         code category will expire
   * @param created          the date and time the code category was created
   * @param createdBy        the username identifying the user that created the code category
   * @param updated          the date and time the code category was updated
   * @param updatedBy        the username identifying the user that updated the code category
   */
  public CodeCategory(String id, String organisation, CodeCategoryType categoryType, String name,
      String description, String endPoint, boolean isEndPointSecure, boolean isCacheable,
      Integer cacheExpiry, Date created, String createdBy, Date updated, String updatedBy)
  {
    this.id = id;
    this.organisation = organisation;
    this.categoryType = categoryType;
    this.name = name;
    this.description = description;
    this.codeData = null;
    this.endPoint = endPoint;
    this.isEndPointSecure = isEndPointSecure;
    this.isCacheable = isCacheable;
    this.cacheExpiry = cacheExpiry;
    this.created = created;
    this.createdBy = createdBy;
    this.updated = updated;
    this.updatedBy = updatedBy;
  }

  /**
   * Constructs a new <code>CodeCategory</code>.
   *
   * @param id               the Universally Unique Identifier (UUID) used to uniquely identify the
   *                         code category
   * @param organisation     the organisation code identifying the organisation the code category
   *                         is associated with
   * @param categoryType     the type of code category e.g. Local, RemoteHTTPService,
   *                         RemoteWebService, etc
   * @param name             the name of the code category
   * @param description      the description for the code category
   * @param codeData         the custom code data for the code category
   * @param endPoint         the endpoint if this is a remote code category
   * @param isEndPointSecure is the endpoint for the remote code category secure
   * @param isCacheable      is the code data retrieved for the remote code category cacheable
   * @param cacheExpiry      the time in seconds after which the cached code data for the remote
   *                         code category will expire
   * @param created          the date and time the code category was created
   * @param createdBy        the username identifying the user that created the code category
   * @param updated          the date and time the code category was updated
   * @param updatedBy        the username identifying the user that updated the code category
   */
  public CodeCategory(String id, String organisation, CodeCategoryType categoryType, String name,
      String description, String codeData, String endPoint, boolean isEndPointSecure,
      boolean isCacheable, Integer cacheExpiry, Date created, String createdBy, Date updated,
      String updatedBy)
  {
    this.id = id;
    this.organisation = organisation;
    this.categoryType = categoryType;
    this.name = name;
    this.description = description;
    this.codeData = codeData;
    this.endPoint = endPoint;
    this.isEndPointSecure = isEndPointSecure;
    this.isCacheable = isCacheable;
    this.cacheExpiry = cacheExpiry;
    this.created = created;
    this.createdBy = createdBy;
    this.updated = updated;
    this.updatedBy = updatedBy;
  }

  /**
   * Returns the time in seconds after which the cached code data for the remote code category will
   * expire.
   *
   * @return the the time in seconds after which the cached code data for the remote code category
   *         will expire
   */
  public Integer getCacheExpiry()
  {
    return cacheExpiry;
  }

  /**
   * Returns the type of code category e.g. Local, RemoteHTTPService, RemoteWebService, etc.
   *
   * @return the type of code category e.g. Local, RemoteHTTPService, RemoteWebService, etc
   */
  public CodeCategoryType getCategoryType()
  {
    return categoryType;
  }

  /**
   * Returns the custom code data for the code category.
   *
   * @return the custom code data for the code category
   */
  public String getCodeData()
  {
    return codeData;
  }

  /**
   * Returns the codes for the code category.
   *
   * @return the codes for the code category
   */
  public List<Code> getCodes()
  {
    return codes;
  }

  /**
   * Returns the date and time the code category was created.
   *
   * @return the date and time the code category was created
   */
  public Date getCreated()
  {
    return created;
  }

  /**
   * Returns the username identifying the user that created the code category.
   *
   * @return the username identifying the user that created the code category
   */
  public String getCreatedBy()
  {
    return createdBy;
  }

  /**
   * Returns the description for the code category.
   *
   * @return the description for the code category
   */
  public String getDescription()
  {
    return description;
  }

  /**
   * Returns the endpoint if this is a remote code category.
   *
   * @return the endpoint if this is a remote code category
   */
  public String getEndPoint()
  {
    return endPoint;
  }

  /**
   * Returns the Universally Unique Identifier (UUID) used to uniquely identify the code category.
   *
   * @return the Universally Unique Identifier (UUID) used to uniquely identify the code category
   */
  public String getId()
  {
    return id;
  }

  /**
   * Returns whether the code data retrieved for the remote code category is cacheable.
   *
   * @return <code>true</code> if the code data retrieved for the remote code category is cacheable
   *         or <code>false</code> otherwise
   */
  public boolean getIsCacheable()
  {
    return isCacheable;
  }

  /**
   * Returns <code>true</code> if the endpoint for the remote code category is secure or
   * <code>false</code> otherwise.
   *
   * @return <code>true</code> if the endpoint for the remote code category is secure or
   *         <code>false</code> otherwise
   */
  public boolean getIsEndPointSecure()
  {
    return isEndPointSecure;
  }

  /**
   * Returns the name of the code category.
   *
   * @return the name of the code category
   */
  public String getName()
  {
    return name;
  }

  /**
   * Returns the organisation code identifying the organisation the code category is associated
   * with.
   *
   * @return the organisation code identifying the organisation the code category is associated
   *         with
   */
  public String getOrganisation()
  {
    return organisation;
  }

  /**
   * Returns the date and time the code category was updated.
   *
   * @return the date and time the code category was updated
   */
  public Date getUpdated()
  {
    return updated;
  }

  /**
   * Returns the username identifying the user that updated the code category.
   *
   * @return the username identifying the user that updated the code category
   */
  public String getUpdatedBy()
  {
    return updatedBy;
  }

  /**
   * Set the time in seconds after which the cached code data for the remote code category will
   * expire.
   *
   * @param cacheExpiry the time in seconds after which the cached code data for the remote code
   *                    category will expire
   */
  public void setCacheExpiry(Integer cacheExpiry)
  {
    this.cacheExpiry = cacheExpiry;
  }

  /**
   * Set the type of code category e.g. Local, RemoteHTTPService, RemoteWebService, etc.
   *
   * @param categoryType the type of code category e.g. Local, RemoteHTTPService, RemoteWebService,
   *                     etc
   */
  public void setCategoryType(CodeCategoryType categoryType)
  {
    this.categoryType = categoryType;
  }

  /**
   * Set the custom code data for the code category.
   *
   * @param codeData the custom code data for the code category
   */
  public void setCodeData(String codeData)
  {
    this.codeData = codeData;
  }

  /**
   * Set the codes for the code category.
   *
   * @param codes the codes for the code category
   */
  public void setCodes(List<Code> codes)
  {
    this.codes = codes;
  }

  /**
   * Set the date and time the code category was created.
   *
   * @param created the date and time the code category was created
   */
  public void setCreated(Date created)
  {
    this.created = created;
  }

  /**
   * Set the username identifying the user that created the code category.
   *
   * @param createdBy the username identifying the user that created the code category
   */
  public void setCreatedBy(String createdBy)
  {
    this.createdBy = createdBy;
  }

  /**
   * Set the description for the code category.
   *
   * @param description the description for the code category
   */
  public void setDescription(String description)
  {
    this.description = description;
  }

  /**
   * Set the endpoint if this is a remote code category.
   *
   * @param endPoint the endpoint if this is a remote code category
   */
  public void setEndPoint(String endPoint)
  {
    this.endPoint = endPoint;
  }

  /**
   * Set the Universally Unique Identifier (UUID) used to uniquely identify the code category.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the code category
   */
  public void setId(String id)
  {
    this.id = id;
  }

  /**
   * Set whether the code data retrieved for the remote code category is cacheable.
   *
   * @param isCacheable is the code data retrieved for the remote code category cacheable
   */
  public void setIsCacheable(boolean isCacheable)
  {
    this.isCacheable = isCacheable;
  }

  /**
   * Set whether the endpoint for the remote code category is secure.
   *
   * @param isEndPointSecure <code>true</code> if the endpoint for the remote code category is
   *                         secure or <code>false</code> otherwise
   */
  public void setIsEndPointSecure(boolean isEndPointSecure)
  {
    this.isEndPointSecure = isEndPointSecure;
  }

  /**
   * Set the name of the code category.
   *
   * @param name the name of the code category
   */
  public void setName(String name)
  {
    this.name = name;
  }

  /**
   * Set the organisation code identifying the organisation the code category is associated with.
   *
   * @param organisation the organisation code identifying the organisation the code category is
   *                     associated with
   */
  public void setOrganisation(String organisation)
  {
    this.organisation = organisation;
  }

  /**
   * Set the date and time the code category was updated.
   *
   * @param updated the date and time the code category was updated
   */
  public void setUpdated(Date updated)
  {
    this.updated = updated;
  }

  /**
   * Set the username identifying the user that updated the code category.
   *
   * @param updatedBy the username identifying the user that updated the code category
   */
  public void setUpdatedBy(String updatedBy)
  {
    this.updatedBy = updatedBy;
  }
}

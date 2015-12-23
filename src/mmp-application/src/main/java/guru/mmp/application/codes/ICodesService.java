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

package guru.mmp.application.codes;

//~--- JDK imports ------------------------------------------------------------

import guru.mmp.common.persistence.DAOException;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * The <code>ICodesService</code> interface defines the functionality that must be provided by a
 * Codes Service implementation.
 *
 * @author Marcus Portmann
 */
public interface ICodesService
{
  /**
   * Check whether the cached code category exists.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the cached code
   *           category
   *
   * @return <code>true</code> if the cached code category exists or <code>false</code> otherwise
   *
   * @throws CodesServiceException
   */
  boolean cachedCodeCategoryExists(UUID id)
    throws CodesServiceException;

  /**
   * Check whether the code category exists.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the code category
   *
   * @return <code>true</code> if the code category exists or <code>false</code> otherwise
   *
   * @throws CodesServiceException
   */
  boolean codeCategoryExists(UUID id)
    throws CodesServiceException;

  /**
   * Create the new cached code.
   *
   * @param code the <code>Code</code> instance containing the information for the new cached code
   *
   * @throws CodesServiceException
   */
  void createCachedCode(Code code)
    throws CodesServiceException;

  /**
   * Create the new cached code category.
   *
   * @param cachedCodeCategory the <code>CachedCodeCategory</code> instance containing the
   *                           information for the new cached code category
   *
   * @throws CodesServiceException
   */
  void createCachedCodeCategory(CachedCodeCategory cachedCodeCategory)
    throws CodesServiceException;

  /**
   * Create the new code.
   *
   * @param code the <code>Code</code> instance containing the information for the new code
   *
   * @throws CodesServiceException
   */
  void createCode(Code code)
    throws CodesServiceException;

  /**
   * Create the new code category.
   *
   * @param codeCategory the <code>CodeCategory</code> instance containing the information for the
   *                     new code category
   *
   * @throws CodesServiceException
   */
  void createCodeCategory(CodeCategory codeCategory)
    throws CodesServiceException;

  /**
   * Delete the cached code category.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the cached code
   *           category
   *
   * @throws CodesServiceException
   */
  void deleteCachedCodeCategory(UUID id)
    throws CodesServiceException;

  /**
   * Delete the code.
   *
   * @param id the ID uniquely identifying the code
   *
   * @throws CodesServiceException
   */
  void deleteCode(String id)
    throws CodesServiceException;

  /**
   * Delete the code category.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the code category
   *
   * @throws CodesServiceException
   */
  void deleteCodeCategory(UUID id)
    throws CodesServiceException;

  /**
   * Retrieve the cached code category.
   *
   * @param id            the Universally Unique Identifier (UUID) used to uniquely identify the
   *                      cached code category
   * @param retrieveCodes retrieve the codes and/or code data for the cached code category
   *
   * @return the cached code category or <code>null</code> if the cached code category could not be
   *         found
   *
   * @throws CodesServiceException
   */
  CachedCodeCategory getCachedCodeCategory(UUID id, boolean retrieveCodes)
    throws CodesServiceException;

  /**
   * Retrieve the code.
   *
   * @param id the ID uniquely identifying the code
   *
   * @return the code or <code>null</code> if the code could not be found
   *
   * @throws CodesServiceException
   */
  Code getCode(String id)
    throws CodesServiceException;

  /**
   * Returns all the code categories for the organisation.
   *
   * @param organisationId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                       organisation
   * @param retrieveCodes  retrieve the codes and/or code data for the code categories
   *
   * @return all the code categories for the organisation
   *
   * @throws CodesServiceException
   */
  List<CodeCategory> getCodeCategoriesForOrganisation(UUID organisationId, boolean retrieveCodes)
    throws CodesServiceException;

  /**
   * Retrieve the code category.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the code category
   *
   * @return the code category or <code>null</code> if the code category could not be found
   *
   * @throws CodesServiceException
   */
  CodeCategory getCodeCategory(UUID id, boolean retrieveCodes)
    throws CodesServiceException;

  /**
   * Retrieve the code category using the specified parameters.
   *
   * @param id            the Universally Unique Identifier (UUID) used to uniquely identify the
   *                      code category
   * @param parameters    the parameters
   * @param retrieveCodes retrieve the codes and/or code data for the code category
   *
   * @return the code category or <code>null</code> if the code category
   *         could not be found
   *
   * @throws CodesServiceException
   */
  CodeCategory getCodeCategoryWithParameters(UUID id, Map<String, String> parameters,
      boolean retrieveCodes)
    throws CodesServiceException;

  /**
   * Retrieve the code category from the appropriate code provider that has been registered with
   * the Codes Service in the <code>META-INF/CodesConfig.xml</code> configuration file.
   *
   * @param codeCategory         the code provider code category
   * @param lastRetrieved        the date and time the code category was last retrieved
   * @param returnCodesIfCurrent should the the <b>Standard</b> codes and/or <b>Custom</b> code
   *                             data be retrieved even if the code category has not been
   *                             updated after the date and time specified by the
   *                             <code>lastRetrieved</code> parameter
   *
   * @return the code provider code category including the <b>Standard</b> codes and/or
   *         <b>Custom</b> code data or <code>null</code> if the code category could not be found
   *
   * @throws CodesServiceException
   */
  CodeCategory getCodeProviderCodeCategory(CodeCategory codeCategory, Date lastRetrieved,
      boolean returnCodesIfCurrent)
    throws CodesServiceException;

  /**
   * Retrieve the code category from the appropriate code provider that has been registered with
   * the Codes Service in the <code>META-INF/CodesConfig.xml</code> configuration file using the
   * specified parameters.
   *
   * @param codeCategory         the code provider code category
   * @param parameters           the parameters
   * @param lastRetrieved        the date and time the code category was last retrieved
   * @param returnCodesIfCurrent should the the <b>Standard</b> codes and/or <b>Custom</b> code
   *                             data be retrieved even if the code category has not been
   *                             updated after the date and time specified by the
   *                             <code>lastRetrieved</code> parameter
   *
   * @return the code provider code category including the <b>Standard</b> codes and/or
   *         <b>Custom</b> code data or <code>null</code> if the code category could not be found
   *
   * @throws CodesServiceException
   */
  CodeCategory getCodeProviderCodeCategoryWithParameters(CodeCategory codeCategory,
      Map<String, String> parameters, Date lastRetrieved, boolean returnCodesIfCurrent)
    throws CodesServiceException;

  /**
   * Returns all the codes for the code category.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the code category
   *
   * @return all the codes for the code category
   *
   * @throws CodesServiceException
   */
  List<Code> getCodesForCodeCategory(UUID id)
    throws CodesServiceException;

  /**
   * Returns the number of code categories for the organisation.
   *
   * @param organisationId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                       organisation
   *
   * @return the number of code categories for the organisation
   *
   * @throws CodesServiceException
   */
  int getNumberOfCodeCategoriesForOrganisation(UUID organisationId)
    throws CodesServiceException;

  /**
   * Returns the number of codes for the code category.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the code category
   *
   * @return the number of codes for the code category
   *
   * @throws CodesServiceException
   */
  int getNumberOfCodesForCodeCategory(UUID id)
    throws CodesServiceException;

  /**
   * Retrieve the remote code category including the <b>Standard</b> codes and/or <b>Custom</b>
   * code data.
   *
   * @param codeCategory         the remote code category
   * @param lastRetrieved        the date and time the remote code category was last retrieved
   * @param returnCodesIfCurrent should the the <b>Standard</b> codes and/or <b>Custom</b> code
   *                             data be retrieved even if the remote code category has not been
   *                             updated after the date and time specified by the
   *                             <code>lastRetrieved</code> parameter
   *
   * @return the remote code category including the <b>Standard</b> codes and/or <b>Custom</b>
   *         code data
   *
   * @throws CodesServiceException
   */
  CodeCategory getRemoteCodeCategory(CodeCategory codeCategory, Date lastRetrieved,
      boolean returnCodesIfCurrent)
    throws CodesServiceException;

  /**
   * Retrieve the remote code category including the <b>Standard</b> codes and/or <b>Custom</b>
   * code data using the specified parameters.
   *
   * @param codeCategory         the remote code category
   * @param parameters           the parameters
   * @param lastRetrieved        the date and time the remote code category was last retrieved
   * @param returnCodesIfCurrent should the the <b>Standard</b> codes and/or <b>Custom</b> code
   *                             data be retrieved even if the remote code category has not been
   *                             updated after the date and time specified by the
   *                             <code>lastRetrieved</code> parameter
   *
   * @return the remote code category including the <b>Standard</b> codes and/or <b>Custom</b>
   *         code data
   *
   * @throws CodesServiceException
   */
  CodeCategory getRemoteCodeCategoryWithParameters(CodeCategory codeCategory,
      Map<String, String> parameters, Date lastRetrieved, boolean returnCodesIfCurrent)
    throws CodesServiceException;

  /**
   * Is the cached code category current?
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the cached code
   *           category
   *
   * @return <code>true</code> if the cached code category is current or <code>false</code>
   *         otherwise
   *
   * @throws CodesServiceException
   */
  boolean isCachedCodeCategoryCurrent(UUID id)
    throws CodesServiceException;

  /**
   * Update the existing cached code category.
   *
   * @param cachedCodeCategory the <code>CachedCodeCategory</code> instance containing the updated
   *                           information for the cached code category
   * @param updatedBy          the username identifying the user that updated the cached code
   *                           category
   *
   * @return the updated cached code category
   *
   * @throws CodesServiceException
   */
  @SuppressWarnings("unused")
  CachedCodeCategory updateCachedCodeCategory(CachedCodeCategory cachedCodeCategory,
      String updatedBy)
    throws CodesServiceException;

  /**
   * Update the existing code.
   *
   * @param code      the <code>Code</code> instance containing the updated information for the code
   * @param updatedBy the username identifying the user that updated the code
   *
   * @return the updated code
   *
   * @throws CodesServiceException
   */
  Code updateCode(Code code, String updatedBy)
    throws CodesServiceException;

  /**
   * Update the existing code category.
   *
   * @param codeCategory the <code>CodeCategory</code> instance containing the updated information
   *                     for the code category
   * @param updatedBy    the username identifying the user that updated the code category
   *
   * @return the updated code category
   *
   * @throws CodesServiceException
   */
  CodeCategory updateCodeCategory(CodeCategory codeCategory, String updatedBy)
    throws CodesServiceException;
}

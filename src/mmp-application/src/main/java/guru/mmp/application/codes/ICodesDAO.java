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

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.common.persistence.DAOException;

import java.util.List;
import java.util.UUID;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>ICodesDAO</code> interface defines the codes-related persistence operations.
 *
 * @author Marcus Portmann
 */
public interface ICodesDAO
{
  /**
   * Check whether the cached code category exists.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the cached code
   *           category
   *
   * @return <code>true</code> if the cached code category exists or <code>false</code> otherwise
   *
   * @throws DAOException
   */
  boolean cachedCodeCategoryExists(UUID id)
    throws DAOException;

  /**
   * Check whether the code category exists.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the code category
   *
   * @return <code>true</code> if the code category exists or <code>false</code> otherwise
   *
   * @throws DAOException
   */
  boolean codeCategoryExists(UUID id)
    throws DAOException;

  /**
   * Create the new cached code.
   *
   * @param code the <code>Code</code> instance containing the information for the new cached code
   *
   * @throws DAOException
   */
  void createCachedCode(Code code)
    throws DAOException;

  /**
   * Create the new cached code category.
   *
   * @param cachedCodeCategory the <code>CachedCodeCategory</code> instance containing the
   *                           information for the new code category
   *
   * @throws DAOException
   */
  void createCachedCodeCategory(CachedCodeCategory cachedCodeCategory)
    throws DAOException;

  /**
   * Create the new code.
   *
   * @param code the <code>Code</code> instance containing the information for the new code
   *
   * @throws DAOException
   */
  void createCode(Code code)
    throws DAOException;

  /**
   * Create the new code category.
   *
   * @param codeCategory the <code>CodeCategory</code> instance containing the information for the
   *                     new code category
   *
   * @throws DAOException
   */
  void createCodeCategory(CodeCategory codeCategory)
    throws DAOException;

  /**
   * Delete the cached code category.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the cached code
   *           category
   *
   * @throws DAOException
   */
  void deleteCachedCodeCategory(UUID id)
    throws DAOException;

  /**
   * Delete the code.
   *
   * @param codeCategoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                       code category
   * @param id             the ID uniquely identifying the code
   *
   * @throws DAOException
   */
  void deleteCode(UUID codeCategoryId, String id)
    throws DAOException;

  /**
   * Delete the code category.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the code category
   *
   * @throws DAOException
   */
  void deleteCodeCategory(UUID id)
    throws DAOException;

  /**
   * Retrieve the cached code category.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the cached code
   *           category
   *
   * @return the cached code category or <code>null</code> if the cached code category could not be
   * found
   *
   * @throws DAOException
   */
  CachedCodeCategory getCachedCodeCategory(UUID id)
    throws DAOException;

  /**
   * Returns all the cached codes for the cached code category.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the cached code
   *           category
   *
   * @return all the cached codes for the cached code category
   *
   * @throws DAOException
   */
  List<Code> getCachedCodesForCachedCodeCategory(UUID id)
    throws DAOException;

  /**
   * Retrieve the code.
   *
   * @param codeCategoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                       code category
   * @param id             the ID uniquely identifying the code
   *
   * @return the code or <code>null</code> if the code could not be found
   *
   * @throws DAOException
   */
  Code getCode(UUID codeCategoryId, String id)
    throws DAOException;

  /**
   * Returns all the code categories.
   *
   * @param retrieveCodes retrieve the codes and/or code data for the code categories
   *
   * @return all the code categories
   *
   * @throws DAOException
   */
  List<CodeCategory> getCodeCategories(boolean retrieveCodes)
    throws DAOException;

  /**
   * Retrieve the code category.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the code category
   *
   * @return the code category or <code>null</code> if the code category could not be found
   *
   * @throws DAOException
   */
  CodeCategory getCodeCategory(UUID id)
    throws DAOException;

  /**
   * Returns all the codes for the code category.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the code category
   *
   * @return all the codes for the code category
   *
   * @throws DAOException
   */
  List<Code> getCodesForCodeCategory(UUID id)
    throws DAOException;

  /**
   * Returns the number of code categories.
   *
   * @return the number of code categories
   *
   * @throws DAOException
   */
  int getNumberOfCodeCategories()
    throws DAOException;

  /**
   * Returns the number of codes for the code category.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the code category
   *
   * @return the number of codes for the code category
   *
   * @throws DAOException
   */
  int getNumberOfCodesForCodeCategory(UUID id)
    throws DAOException;

  /**
   * Is the cached code category current?
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the cached code
   *           category
   *
   * @return <code>true</code> if the cached code category is current or <code>false</code>
   * otherwise
   *
   * @throws DAOException
   */
  boolean isCachedCodeCategoryCurrent(UUID id)
    throws DAOException;

  /**
   * Update the existing cached code category.
   *
   * @param cachedCodeCategory the <code>CachedCodeCategory</code> instance containing the updated
   *                           information for the cached code category
   *
   * @return the updated cached code category
   *
   * @throws DAOException
   */
  CachedCodeCategory updateCachedCodeCategory(CachedCodeCategory cachedCodeCategory)
    throws DAOException;

  /**
   * Update the existing code.
   *
   * @param code the <code>Code</code> instance containing the updated information for the code
   *
   * @return the updated code
   *
   * @throws DAOException
   */
  Code updateCode(Code code)
    throws DAOException;

  /**
   * Update the existing code category.
   *
   * @param codeCategory the <code>CodeCategory</code> instance containing the updated information
   *                     for the code category
   *
   * @return the updated code category
   *
   * @throws DAOException
   */
  CodeCategory updateCodeCategory(CodeCategory codeCategory)
    throws DAOException;
}

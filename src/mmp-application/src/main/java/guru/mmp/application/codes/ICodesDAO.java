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

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.persistence.DAOException;

import java.util.List;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>ICodesDAO</code> interface defines the codes-related persistence operations.
 *
 * @author Marcus Portmann
 */
public interface ICodesDAO
{
  /**
   * Check whether the cached code category with the specified ID exists.
   *
   * @param id the ID uniquely identifying the cached code category
   * @return <code>true</code> if the cached code category exists or <code>false</code> otherwise
   * @throws DAOException
   */
  public boolean cachedCodeCategoryExists(String id)
    throws DAOException;

  /**
   * Check whether the code category with the specified ID exists.
   *
   * @param id the ID uniquely identifying the code category
   * @return <code>true</code> if the code category exists or <code>false</code> otherwise
   * @throws DAOException
   */
  public boolean codeCategoryExists(String id)
    throws DAOException;

  /**
   * Create the new cached code.
   *
   * @param code the <code>Code</code> instance containing the information for the new cached code
   * @throws DAOException
   */
  public void createCachedCode(Code code)
    throws DAOException;

  /**
   * Create the new cached code category.
   *
   * @param cachedCodeCategory the <code>CachedCodeCategory</code> instance containing the
   *                           information for the new code category
   * @throws DAOException
   */
  public void createCachedCodeCategory(CachedCodeCategory cachedCodeCategory)
    throws DAOException;

  /**
   * Create the new code.
   *
   * @param code the <code>Code</code> instance containing the information for the new code
   * @throws DAOException
   */
  public void createCode(Code code)
    throws DAOException;

  /**
   * Create the new code category.
   *
   * @param codeCategory the <code>CodeCategory</code> instance containing the information for the
   *                     new code category
   * @throws DAOException
   */
  public void createCodeCategory(CodeCategory codeCategory)
    throws DAOException;

  /**
   * Delete the cached code category.
   *
   * @param id the ID uniquely identifying the cached code category
   * @throws DAOException
   */
  public void deleteCachedCodeCategory(String id)
    throws DAOException;

  /**
   * Delete the code.
   *
   * @param id the ID uniquely identifying the code
   * @throws DAOException
   */
  public void deleteCode(String id)
    throws DAOException;

  /**
   * Delete the code category.
   *
   * @param id the ID uniquely identifying the code category
   * @throws DAOException
   */
  public void deleteCodeCategory(String id)
    throws DAOException;

  /**
   * Retrieve the cached code category with the specified ID.
   *
   * @param id the ID uniquely identifying the cached code category
   * @return the cached code category with the specified ID or <code>null</code> if the cached code
   * category could not be found
   * @throws DAOException
   */
  public CachedCodeCategory getCachedCodeCategory(String id)
    throws DAOException;

  /**
   * Returns all the cached codes for the cached code category with the specified ID.
   *
   * @param cachedCodeCategoryId the ID uniquely identifying the cached code category
   * @return all the cached codes for the cached code category with the specified ID
   * @throws DAOException
   */
  public List<Code> getCachedCodesForCachedCodeCategory(String cachedCodeCategoryId)
    throws DAOException;

  /**
   * Retrieve the code with the specified ID.
   *
   * @param id the ID uniquely identifying the code
   * @return the code with the specified ID or <code>null</code> if the code could not be found
   * @throws DAOException
   */
  public Code getCode(String id)
    throws DAOException;

  /**
   * Returns all the code categories associated with the organisation identified by the
   * specified organisation code.
   *
   * @param organisation  the organisation code identifying the organisation
   * @param retrieveCodes retrieve the codes and/or code data for the code categories
   * @return all the code categories associated with the organisation identified by the
   * specified organisation code
   * @throws DAOException
   */
  public List<CodeCategory> getCodeCategoriesForOrganisation(String organisation,
      boolean retrieveCodes)
    throws DAOException;

  /**
   * Retrieve the code category with the specified ID.
   *
   * @param id the ID uniquely identifying the code category
   * @return the code category with the specified ID or <code>null</code> if the code category
   * could not be found
   * @throws DAOException
   */
  public CodeCategory getCodeCategory(String id)
    throws DAOException;

  /**
   * Returns all the codes for the code category with the specified ID.
   *
   * @param codeCategoryId the ID uniquely identifying the code category
   * @return all the codes for the code category with the specified ID
   * @throws DAOException
   */
  public List<Code> getCodesForCodeCategory(String codeCategoryId)
    throws DAOException;

  /**
   * Returns the number of code categories associated with the organisation identified by the
   * specified organisation code.
   *
   * @param organisation the organisation code identifying the organisation
   * @return the number of code categories associated with the organisation identified by the
   * specified organisation code
   * @throws DAOException
   */
  public int getNumberOfCodeCategoriesForOrganisation(String organisation)
    throws DAOException;

  /**
   * Returns the number of codes for the code category with the specified ID.
   *
   * @param codeCategoryId the ID uniquely identifying the code category
   * @return the number of codes for the code category with the specified ID
   * @throws DAOException
   */
  public int getNumberOfCodesForCodeCategory(String codeCategoryId)
    throws DAOException;

  /**
   * Is the cached code category current?
   *
   * @param id the ID uniquely identifying the cached code category
   * @return <code>true</code> if the cached code category is current or <code>false</code>
   * otherwise
   * @throws DAOException
   */
  public boolean isCachedCodeCategoryCurrent(String id)
    throws DAOException;

  /**
   * Update the existing cached code category.
   *
   * @param cachedCodeCategory the <code>CachedCodeCategory</code> instance containing the updated
   *                           information for the cached code category
   * @param updatedBy          the username identifying the user that updated the cached code
   *                           category
   * @return the updated cached code category
   * @throws DAOException
   */
  public CachedCodeCategory updateCachedCodeCategory(CachedCodeCategory cachedCodeCategory,
      String updatedBy)
    throws DAOException;

  /**
   * Update the existing code.
   *
   * @param code      the <code>Code</code> instance containing the updated information for the code
   * @param updatedBy the username identifying the user that updated the code
   * @return the updated code
   * @throws DAOException
   */
  public Code updateCode(Code code, String updatedBy)
    throws DAOException;

  /**
   * Update the existing code category.
   *
   * @param codeCategory the <code>CodeCategory</code> instance containing the updated information
   *                     for the code category
   * @param updatedBy    the username identifying the user that updated the code category
   * @return the updated code category
   * @throws DAOException
   */
  public CodeCategory updateCodeCategory(CodeCategory codeCategory, String updatedBy)
    throws DAOException;
}

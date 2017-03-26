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

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.persistence.IDGenerator;
import guru.mmp.common.persistence.DAOException;
import guru.mmp.common.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.io.UnsupportedEncodingException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>CodesDAO</code> class implements the codes-related persistence operations.
 *
 * @author Marcus Portmann
 */
@Repository
public class CodesDAO
  implements ICodesDAO
{
  /**
   * The data source used to provide connections to the application database.
   */
  @Autowired
  @Qualifier("applicationDataSource")
  private DataSource dataSource;

  /**
   * The ID Generator.
   */
  @Autowired
  private IDGenerator idGenerator;

  /**
   * Constructs a new <code>CodesDAO</code>.
   */
  public CodesDAO() {}

  /**
   * Check whether the cached code category exists.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the cached code
   *           category
   *
   * @return <code>true</code> if the cached code category exists or <code>false</code> otherwise
   */
  @Transactional
  public boolean cachedCodeCategoryExists(UUID id)
    throws DAOException
  {
    String cachedCodeCategoryExistsSQL = "SELECT CCC.ID FROM CODES.CACHED_CODE_CATEGORIES CCC "
        + "WHERE CCC.ID=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(cachedCodeCategoryExistsSQL))
    {
      statement.setObject(1, id);

      try (ResultSet rs = statement.executeQuery())
      {
        return rs.next();
      }
    }
    catch (Throwable e)
    {
      throw new DAOException(String.format(
          "Failed to check whether the cached code category (%s) exists in the database", id), e);
    }
  }

  /**
   * Check whether the code category exists.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the code
   *           category
   *
   * @return <code>true</code> if the code category exists or <code>false</code> otherwise
   */
  @Transactional
  public boolean codeCategoryExists(UUID id)
    throws DAOException
  {
    String codeCategoryExistsSQL = "SELECT CC.ID FROM CODES.CODE_CATEGORIES CC WHERE CC.ID=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(codeCategoryExistsSQL))
    {
      statement.setObject(1, id);

      try (ResultSet rs = statement.executeQuery())
      {
        return rs.next();
      }
    }
    catch (Throwable e)
    {
      throw new DAOException(String.format(
          "Failed to check whether the code category (%s) exists in the database", id), e);
    }
  }

  /**
   * Create the new cached code.
   *
   * @param code the <code>Code</code> instance containing the information for the new cached
   *             code
   */
  @Transactional
  public void createCachedCode(Code code)
    throws DAOException
  {
    String createCachedCodeSQL = "INSERT INTO CODES.CACHED_CODES (ID, CATEGORY_ID, NAME, VALUE) "
        + "VALUES (?, ?, ?, ?)";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(createCachedCodeSQL))
    {
      if (StringUtil.isNullOrEmpty(code.getId()))
      {
        statement.setString(1, String.valueOf(idGenerator.nextUUID()));
      }
      else
      {
        statement.setString(1, code.getId());
      }

      statement.setObject(2, code.getCategoryId());
      statement.setString(3, code.getName());
      statement.setString(4, code.getValue());

      if (statement.executeUpdate() != 1)
      {
        throw new DAOException(String.format(
            "No rows were affected as a result of executing the SQL statement (%s)",
            createCachedCodeSQL));
      }
    }
    catch (Throwable e)
    {
      throw new DAOException(String.format(
          "Failed to add the cached code (%s) for the cached code category (%s) to the database",
          code.getName(), code.getCategoryId()), e);
    }
  }

  /**
   * Create the new cached code category.
   *
   * @param cachedCodeCategory the <code>CachedCodeCategory</code> instance containing the
   *                           information for the new code category
   */
  @Transactional
  public void createCachedCodeCategory(CachedCodeCategory cachedCodeCategory)
    throws DAOException
  {
    String createCachedCodeCategorySQL =
        "INSERT INTO CODES.CACHED_CODE_CATEGORIES (ID, CODE_DATA, "
        + "LAST_UPDATED, CACHED) VALUES (?, ?, ?, ?)";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(createCachedCodeCategorySQL))
    {
      statement.setObject(1, cachedCodeCategory.getId());
      statement.setBytes(2,
          (cachedCodeCategory.getCodeData() != null)
          ? cachedCodeCategory.getCodeData().getBytes("UTF-8")
          : null);
      statement.setTimestamp(3, new Timestamp(cachedCodeCategory.getLastUpdated().getTime()));
      statement.setTimestamp(4, new Timestamp(cachedCodeCategory.getCached().getTime()));

      if (statement.executeUpdate() != 1)
      {
        throw new DAOException(String.format(
            "No rows were affected as a result of executing the SQL statement (%s)",
            createCachedCodeCategorySQL));
      }
    }
    catch (Throwable e)
    {
      throw new DAOException(String.format(
          "Failed to add the cached code category (%s) to the database",
          cachedCodeCategory.getId()), e);
    }
  }

  /**
   * Create the new code.
   *
   * @param code the <code>Code</code> instance containing the information for the new code
   */
  @Transactional
  public void createCode(Code code)
    throws DAOException
  {
    String createCodeSQL =
        "INSERT INTO CODES.CODES (ID, CATEGORY_ID, NAME, VALUE) VALUES (?, ?, ?, ?)";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(createCodeSQL))
    {
      if (StringUtil.isNullOrEmpty(code.getId()))
      {
        statement.setString(1, String.valueOf(idGenerator.nextUUID()));
      }
      else
      {
        statement.setString(1, code.getId());
      }

      statement.setObject(2, code.getCategoryId());
      statement.setString(3, code.getName());
      statement.setString(4, code.getValue());

      if (statement.executeUpdate() != 1)
      {
        throw new DAOException(String.format(
            "No rows were affected as a result of executing the SQL statement (%s)",
            createCodeSQL));
      }
    }
    catch (Throwable e)
    {
      throw new DAOException(String.format(
          "Failed to add the code (%s) for the code category (%s) to the database", code.getName(),
          code.getCategoryId()), e);
    }
  }

  /**
   * Create the new code category.
   *
   * @param codeCategory the <code>CodeCategory</code> instance containing the information for the
   *                     new code category
   */
  @Transactional
  public void createCodeCategory(CodeCategory codeCategory)
    throws DAOException
  {
    String createCodeCategorySQL = "INSERT INTO CODES.CODE_CATEGORIES (ID, CATEGORY_TYPE, NAME, "
        + "CODE_DATA, ENDPOINT, IS_ENDPOINT_SECURE, IS_CACHEABLE, CACHE_EXPIRY, UPDATED) "
        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(createCodeCategorySQL))
    {
      statement.setObject(1, codeCategory.getId());
      statement.setInt(2, codeCategory.getCategoryType().getCode());
      statement.setString(3, codeCategory.getName());
      statement.setBytes(4,
          (codeCategory.getCodeData() != null)
          ? codeCategory.getCodeData().getBytes("UTF-8")
          : null);
      statement.setString(5, codeCategory.getEndPoint());
      statement.setBoolean(6, codeCategory.getIsEndPointSecure());
      statement.setBoolean(7, codeCategory.getIsCacheable());

      if (codeCategory.getCacheExpiry() == null)
      {
        statement.setNull(8, java.sql.Types.INTEGER);
      }
      else
      {
        statement.setInt(8, codeCategory.getCacheExpiry());
      }

      if (codeCategory.getUpdated() == null)
      {
        Timestamp updated = new Timestamp(System.currentTimeMillis());

        statement.setTimestamp(9, updated);

        codeCategory.setUpdated(updated);
      }
      else
      {
        statement.setTimestamp(9, new Timestamp(codeCategory.getUpdated().getTime()));
      }

      if (statement.executeUpdate() != 1)
      {
        throw new DAOException(String.format(
            "No rows were affected as a result of executing the SQL statement (%s)",
            createCodeCategorySQL));
      }

    }
    catch (Throwable e)
    {
      throw new DAOException(String.format("Failed to add the code category (%s) to the database",
          codeCategory.getId()), e);
    }
  }

  /**
   * Delete the cached code category.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the cached code
   *           category
   */
  @Transactional
  public void deleteCachedCodeCategory(UUID id)
    throws DAOException
  {
    String deleteCachedCodeCategorySQL =
        "DELETE FROM CODES.CACHED_CODE_CATEGORIES CCC WHERE CCC.ID=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(deleteCachedCodeCategorySQL))
    {
      statement.setObject(1, id);

      if (statement.executeUpdate() != 1)
      {
        throw new DAOException(String.format(
            "No rows were affected as a result of executing the SQL statement (%s)",
            deleteCachedCodeCategorySQL));
      }
    }
    catch (Throwable e)
    {
      throw new DAOException(String.format(
          "Failed to delete the cached code category (%s) in the database", id), e);
    }
  }

  /**
   * Delete the code.
   *
   * @param codeCategoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                       code category
   * @param id             the ID uniquely identifying the code
   */
  @Transactional
  public void deleteCode(UUID codeCategoryId, String id)
    throws DAOException
  {
    String deleteCodeSQL = "DELETE FROM CODES.CODES C WHERE C.CATEGORY_ID=? AND C.ID=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(deleteCodeSQL))
    {
      statement.setObject(1, codeCategoryId);
      statement.setString(2, id);

      if (statement.executeUpdate() != 1)
      {
        throw new DAOException(String.format(
            "No rows were affected as a result of executing the SQL statement (%s)",
            deleteCodeSQL));
      }
    }
    catch (Throwable e)
    {
      throw new DAOException(String.format(
          "Failed to delete the code (%s) for the code category (%s) in the database", id,
          codeCategoryId), e);
    }
  }

  /**
   * Delete the code category.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the code
   *           category
   */
  @Transactional
  public void deleteCodeCategory(UUID id)
    throws DAOException
  {
    String deleteCodeCategorySQL = "DELETE FROM CODES.CODE_CATEGORIES CC WHERE CC.ID=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(deleteCodeCategorySQL))
    {
      statement.setObject(1, id);

      if (statement.executeUpdate() != 1)
      {
        throw new DAOException(String.format(
            "No rows were affected as a result of executing the SQL statement (%s)",
            deleteCodeCategorySQL));
      }
    }
    catch (Throwable e)
    {
      throw new DAOException(String.format(
          "Failed to delete the code category (%s) in the database", id), e);
    }
  }

  /**
   * Retrieve the cached code category.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the cached code
   *           category
   *
   * @return the cached code category or <code>null</code> if the cached code category could not
   * be found
   */
  @Transactional
  public CachedCodeCategory getCachedCodeCategory(UUID id)
    throws DAOException
  {
    String getCachedCodeCategorySQL = "SELECT CCC.ID, CCC.CODE_DATA, CCC.LAST_UPDATED, CCC.CACHED "
        + "FROM CODES.CACHED_CODE_CATEGORIES CCC WHERE CCC.ID=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(getCachedCodeCategorySQL))
    {
      statement.setObject(1, id);

      try (ResultSet rs = statement.executeQuery())
      {
        if (rs.next())
        {
          return getCachedCodeCategory(rs);
        }
        else
        {
          return null;
        }
      }
    }
    catch (Throwable e)
    {
      throw new DAOException(String.format(
          "Failed to retrieve the cached code category (%s) from the database", id), e);
    }
  }

  /**
   * Returns all the cached codes for the cached code category.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the cached code
   *           category
   *
   * @return all the cached codes for the cached code category
   */
  @Transactional
  public List<Code> getCachedCodesForCachedCodeCategory(UUID id)
    throws DAOException
  {
    String getCachedCodesForCachedCodeCategorySQL =
        "SELECT CC.ID, CC.CATEGORY_ID, CC.NAME, CC.VALUE FROM CODES.CACHED_CODES CC "
        + "WHERE CC.CATEGORY_ID=? ORDER BY CC.NAME";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(
          getCachedCodesForCachedCodeCategorySQL))
    {
      statement.setObject(1, id);

      try (ResultSet rs = statement.executeQuery())
      {
        List<Code> codes = new ArrayList<>();

        while (rs.next())
        {
          codes.add(getCode(rs));
        }

        return codes;
      }
    }
    catch (Throwable e)
    {
      throw new DAOException(String.format(
          "Failed to retrieve the cached codes for the cached code category (%s) from the database",
          id), e);
    }
  }

  /**
   * Retrieve the code.
   *
   * @param codeCategoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                       code category
   * @param id             the ID uniquely identifying the code
   *
   * @return the code or <code>null</code> if the code could not be found
   */
  @Transactional
  public Code getCode(UUID codeCategoryId, String id)
    throws DAOException
  {
    String getCodeSQL = "SELECT C.ID, C.CATEGORY_ID, C.NAME, C.VALUE FROM CODES.CODES C "
        + "WHERE C.CATEGORY_ID=? AND C.ID=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(getCodeSQL))
    {
      statement.setObject(1, codeCategoryId);
      statement.setString(2, id);

      try (ResultSet rs = statement.executeQuery())
      {
        if (rs.next())
        {
          return getCode(rs);
        }
        else
        {
          return null;
        }
      }
    }
    catch (Throwable e)
    {
      throw new DAOException(String.format("Failed to retrieve the code (%s) from the database",
          id), e);
    }
  }

  /**
   * Returns all the code categories.
   *
   * @param retrieveCodes retrieve the codes and/or code data for the code categories
   *
   * @return all the code categories
   */
  @Transactional
  public List<CodeCategory> getCodeCategories(boolean retrieveCodes)
    throws DAOException
  {
    String getCodeCategoriesSQL = "SELECT CC.ID, CC.CATEGORY_TYPE, CC.NAME, CC.CODE_DATA, "
        + "CC.ENDPOINT, CC.IS_ENDPOINT_SECURE, CC.IS_CACHEABLE, CC.CACHE_EXPIRY, CC.UPDATED FROM "
        + "CODES.CODE_CATEGORIES CC ORDER BY CC.NAME";

    String getCodeCategoriesNoDataSQL = "SELECT CC.ID, CC.CATEGORY_TYPE, CC.NAME, CC.ENDPOINT, "
        + "CC.IS_ENDPOINT_SECURE, CC.IS_CACHEABLE, CC.CACHE_EXPIRY, CC.UPDATED FROM "
        + "CODES.CODE_CATEGORIES CC ORDER BY CC.NAME";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(retrieveCodes
          ? getCodeCategoriesSQL
          : getCodeCategoriesNoDataSQL))
    {
      try (ResultSet rs = statement.executeQuery())
      {
        List<CodeCategory> codeCategories = new ArrayList<>();

        while (rs.next())
        {
          if (retrieveCodes)
          {
            CodeCategory codeCategory = getCodeCategory(rs);

            if (codeCategory.getCategoryType().equals(CodeCategoryType.LOCAL_STANDARD))
            {
              codeCategory.setCodes(getCodesForCodeCategory(connection, codeCategory.getId()));
            }

            codeCategories.add(codeCategory);
          }
          else
          {
            codeCategories.add(getCodeCategoryNoData(rs));
          }
        }

        return codeCategories;
      }
    }
    catch (Throwable e)
    {
      throw new DAOException("Failed to retrieve the code categories from the database", e);
    }
  }

  /**
   * Retrieve the code category.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the code
   *           category
   *
   * @return the code category or <code>null</code> if the code category could not be found
   */
  @Transactional
  public CodeCategory getCodeCategory(UUID id)
    throws DAOException
  {
    String getCodeCategorySQL = "SELECT CC.ID, CC.CATEGORY_TYPE, CC.NAME, CC.CODE_DATA, "
        + "CC.ENDPOINT, CC.IS_ENDPOINT_SECURE, CC.IS_CACHEABLE, CC.CACHE_EXPIRY, CC.UPDATED FROM "
        + "CODES.CODE_CATEGORIES CC WHERE CC.ID=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(getCodeCategorySQL))
    {
      statement.setObject(1, id);

      try (ResultSet rs = statement.executeQuery())
      {
        if (rs.next())
        {
          return getCodeCategory(rs);
        }
        else
        {
          return null;
        }
      }
    }
    catch (Throwable e)
    {
      throw new DAOException(String.format(
          "Failed to retrieve the code category (%s) from the database", id), e);
    }
  }

  /**
   * Returns all the codes for the code category.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the code
   *           category
   *
   * @return all the codes for the code category
   */
  @Transactional
  public List<Code> getCodesForCodeCategory(UUID id)
    throws DAOException
  {
    try (Connection connection = dataSource.getConnection())
    {
      return getCodesForCodeCategory(connection, id);
    }
    catch (Throwable e)
    {
      throw new DAOException(String.format(
          "Failed to retrieve the codes for the code category (%s) from the database", id), e);
    }
  }

  /**
   * Returns the number of code categories.
   *
   * @return the number of code categories
   */
  @Transactional
  public int getNumberOfCodeCategories()
    throws DAOException
  {
    String getNumberOfCodeCategoriesSQL = "SELECT COUNT(CC.ID) FROM CODES.CODE_CATEGORIES CC";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(getNumberOfCodeCategoriesSQL))
    {
      try (ResultSet rs = statement.executeQuery())
      {
        if (rs.next())
        {
          return rs.getInt(1);
        }
        else
        {
          throw new DAOException(String.format(
              "No results were returned as a result of executing the SQL statement (%s)",
              getNumberOfCodeCategoriesSQL));
        }
      }
    }
    catch (Throwable e)
    {
      throw new DAOException("Failed to retrieve the number of code categories in the database", e);
    }
  }

  /**
   * Returns the number of codes for the code category.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the code
   *           category
   *
   * @return the number of codes for the code category
   */
  @Transactional
  public int getNumberOfCodesForCodeCategory(UUID id)
    throws DAOException
  {
    String getNumberOfCodesForCodeCategorySQL =
        "SELECT COUNT(C.ID) FROM CODES.CODES C WHERE C.CATEGORY_ID=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(getNumberOfCodesForCodeCategorySQL))
    {
      statement.setObject(1, id);

      try (ResultSet rs = statement.executeQuery())
      {
        if (rs.next())
        {
          return rs.getInt(1);
        }
        else
        {
          throw new DAOException(String.format(
              "No results were returned as a result of executing the SQL statement (%s)",
              getNumberOfCodesForCodeCategorySQL));
        }
      }
    }
    catch (Throwable e)
    {
      throw new DAOException(String.format(
          "Failed to retrieve the number of codes for the code category (%s) in the database", id),
          e);
    }
  }

  /**
   * Is the cached code category current?
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the cached code
   *           category
   *
   * @return <code>true</code> if the cached code category is current or <code>false</code>
   * otherwise
   */
  @Transactional
  public boolean isCachedCodeCategoryCurrent(UUID id)
    throws DAOException
  {
    try (Connection connection = dataSource.getConnection())
    {
      Integer cacheExpiry = getCodeCategoryCacheExpiry(connection, id);

      if (cacheExpiry == null)
      {
        return false;
      }

      Date cached = getCachedCodeCategoryCached(connection, id);

      return (cached != null)
          && (System.currentTimeMillis() <= (cached.getTime() + (cacheExpiry.longValue() * 1000L)));
    }
    catch (Throwable e)
    {
      throw new DAOException(String.format(
          "Failed to check whether the cached code category (%s) in the database is current", id),
          e);
    }
  }

  /**
   * Update the existing code.
   *
   * @param code the <code>Code</code> instance containing the updated information for the code
   *
   * @return the updated code
   */
  @Transactional
  public Code updateCode(Code code)
    throws DAOException
  {
    String updateCodeSQL = "UPDATE CODES.CODES C SET NAME=?, VALUE=? WHERE C.ID=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(updateCodeSQL))
    {
      statement.setString(1, code.getName());
      statement.setString(2, code.getValue());
      statement.setString(3, code.getId());

      if (statement.executeUpdate() != 1)
      {
        throw new DAOException(String.format(
            "No rows were affected as a result of executing the SQL statement (%s)",
            updateCodeSQL));
      }

      return code;
    }
    catch (Throwable e)
    {
      throw new DAOException(String.format("Failed to update the code (%s) in the database",
          code.getId()), e);
    }
  }

  /**
   * Update the existing code category.
   *
   * @param codeCategory the <code>CodeCategory</code> instance containing the updated information
   *                     for the code category
   *
   * @return the updated code category
   */
  @Transactional
  public CodeCategory updateCodeCategory(CodeCategory codeCategory)
    throws DAOException
  {
    String updateCodeCategorySQL = "UPDATE CODES.CODE_CATEGORIES CC SET CATEGORY_TYPE=?, NAME=?, "
        + "CODE_DATA=?, ENDPOINT=?, IS_ENDPOINT_SECURE=?, IS_CACHEABLE=?, CACHE_EXPIRY=?, UPDATED=? "
        + "WHERE CC.ID=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(updateCodeCategorySQL))
    {
      Date updated = new Date();

      statement.setInt(1, codeCategory.getCategoryType().getCode());
      statement.setString(2, codeCategory.getName());
      statement.setBytes(3,
          (codeCategory.getCodeData() != null)
          ? codeCategory.getCodeData().getBytes("UTF-8")
          : null);
      statement.setString(4, codeCategory.getEndPoint());
      statement.setBoolean(5, codeCategory.getIsEndPointSecure());
      statement.setBoolean(6, codeCategory.getIsCacheable());

      if (codeCategory.getCacheExpiry() == null)
      {
        statement.setNull(7, java.sql.Types.INTEGER);
      }
      else
      {
        statement.setInt(7, codeCategory.getCacheExpiry());
      }

      statement.setTimestamp(8, new Timestamp(updated.getTime()));
      statement.setObject(9, codeCategory.getId());

      if (statement.executeUpdate() != 1)
      {
        throw new DAOException(String.format(
            "No rows were affected as a result of executing the SQL statement (%s)",
            updateCodeCategorySQL));
      }

      codeCategory.setUpdated(updated);

      return codeCategory;
    }
    catch (Throwable e)
    {
      throw new DAOException(String.format(
          "Failed to update the code category (%s) in the database", codeCategory.getId()), e);
    }
  }

  private CachedCodeCategory getCachedCodeCategory(ResultSet rs)
    throws SQLException, UnsupportedEncodingException
  {
    return new CachedCodeCategory((UUID) rs.getObject(1),
        (rs.getBytes(2) != null)
        ? new String(rs.getBytes(2), "UTF-8")
        : null, rs.getTimestamp(3), rs.getTimestamp(4));
  }

  private Date getCachedCodeCategoryCached(Connection connection, UUID id)
    throws SQLException
  {
    String getCachedCodeCategoryCachedSQL =
        "SELECT CCC.CACHED FROM CODES.CACHED_CODE_CATEGORIES CCC WHERE CCC.ID=?";

    try (PreparedStatement statement = connection.prepareStatement(getCachedCodeCategoryCachedSQL))
    {
      statement.setObject(1, id);

      try (ResultSet rs = statement.executeQuery())
      {
        if (rs.next())
        {
          return rs.getTimestamp(1);
        }
        else
        {
          return null;
        }
      }
    }
    catch (Throwable e)
    {
      throw new DAOException(String.format(
          "Failed to retrieve the cached date and time for the cached code category (%s) from the "
          + "database", id), e);
    }
  }

  private Code getCode(ResultSet rs)
    throws SQLException
  {
    return new Code(rs.getString(1), (UUID) rs.getObject(2), rs.getString(3), rs.getString(4));
  }

  private CodeCategory getCodeCategory(ResultSet rs)
    throws SQLException, UnsupportedEncodingException
  {
    int cacheExpiry = rs.getInt(8);

    boolean cacheExpiryIsNull = rs.wasNull();

    return new CodeCategory((UUID) rs.getObject(1), CodeCategoryType.fromCode(rs.getInt(2)),
        rs.getString(3),
        (rs.getBytes(4) != null)
        ? new String(rs.getBytes(4), "UTF-8")
        : null, rs.getString(5), rs.getBoolean(6), rs.getBoolean(7),
        cacheExpiryIsNull
        ? null
        : cacheExpiry, rs.getTimestamp(9));
  }

  private Integer getCodeCategoryCacheExpiry(Connection connection, UUID id)
    throws SQLException
  {
    String getCodeCategoryCacheExpirySQL =
        "SELECT CC.CACHE_EXPIRY FROM CODES.CODE_CATEGORIES CC WHERE CC.ID=?";

    try (PreparedStatement statement = connection.prepareStatement(getCodeCategoryCacheExpirySQL))
    {
      statement.setObject(1, id);

      try (ResultSet rs = statement.executeQuery())
      {
        if (rs.next())
        {
          int cacheExpiry = rs.getInt(1);

          boolean cacheExpiryIsNull = rs.wasNull();

          if (cacheExpiryIsNull)
          {
            return null;
          }
          else
          {
            return cacheExpiry;
          }
        }
        else
        {
          return null;
        }
      }
    }
    catch (Throwable e)
    {
      throw new DAOException(String.format(
          "Failed to retrieve the cache expiry for the code category (%s) from the database", id),
          e);
    }
  }

  private CodeCategory getCodeCategoryNoData(ResultSet rs)
    throws SQLException
  {
    int cacheExpiry = rs.getInt(7);

    boolean cacheExpiryIsNull = rs.wasNull();

    return new CodeCategory((UUID) rs.getObject(1), CodeCategoryType.fromCode(rs.getInt(2)),
        rs.getString(3), rs.getString(4), rs.getBoolean(5), rs.getBoolean(6),
        cacheExpiryIsNull
        ? null
        : cacheExpiry, rs.getTimestamp(8));
  }

  private List<Code> getCodesForCodeCategory(Connection connection, UUID id)
    throws SQLException
  {
    String getCodesForCodeCategorySQL = "SELECT C.ID, C.CATEGORY_ID, C.NAME, C.VALUE FROM "
        + "CODES.CODES C WHERE C.CATEGORY_ID=? ORDER BY C.NAME";

    try (PreparedStatement statement = connection.prepareStatement(getCodesForCodeCategorySQL))
    {
      statement.setObject(1, id);

      try (ResultSet rs = statement.executeQuery())
      {
        List<Code> codes = new ArrayList<>();

        while (rs.next())
        {
          codes.add(getCode(rs));
        }

        return codes;
      }
    }
  }
}

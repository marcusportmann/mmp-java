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

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.common.persistence.DAOException;
import guru.mmp.common.persistence.DataAccessObject;
import guru.mmp.common.persistence.IDGenerator;
import guru.mmp.common.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.naming.InitialContext;
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
@ApplicationScoped
@Default
public class CodesDAO
  implements ICodesDAO
{
  /* Logger */
  @SuppressWarnings("unused")
  private static final Logger logger = LoggerFactory.getLogger(CodesDAO.class);
  private String cachedCodeCategoryExistsSQL;
  private String codeCategoryExistsSQL;
  private String createCachedCodeCategorySQL;
  private String createCachedCodeSQL;
  private String createCodeCategorySQL;
  private String createCodeSQL;

  /**
   * The data source used to provide connections to the database.
   */
  private DataSource dataSource;
  private String deleteCachedCodeCategorySQL;
  private String deleteCodeCategorySQL;
  private String deleteCodeSQL;
  private String getCachedCodeCategoryCachedSQL;
  private String getCachedCodeCategorySQL;
  private String getCachedCodesForCachedCodeCategorySQL;
  private String getCodeCategoriesNoDataSQL;
  private String getCodeCategoriesSQL;
  private String getCodeCategoryCacheExpirySQL;
  private String getCodeCategorySQL;
  private String getCodeSQL;
  private String getCodesForCodeCategorySQL;
  private String getNumberOfCodeCategoriesSQL;
  private String getNumberOfCodesForCodeCategorySQL;

  /**
   * The ID generator used to generate unique numeric IDs for the DAO.
   */
  private IDGenerator idGenerator;
  private String updateCachedCodeCategorySQL;
  private String updateCodeCategorySQL;
  private String updateCodeSQL;

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
   *
   * @throws DAOException
   */
  public boolean cachedCodeCategoryExists(UUID id)
    throws DAOException
  {
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
      throw new DAOException("Failed to check whether the cached code category (" + id
          + ") exists in the database", e);
    }
  }

  /**
   * Check whether the code category exists.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the code category
   *
   * @return <code>true</code> if the code category exists or <code>false</code> otherwise
   *
   * @throws DAOException
   */
  public boolean codeCategoryExists(UUID id)
    throws DAOException
  {
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
      throw new DAOException("Failed to check whether the code category (" + id
          + ") exists in the database", e);
    }
  }

  /**
   * Create the new cached code.
   *
   * @param code the <code>Code</code> instance containing the information for the new cached code
   *
   * @throws DAOException
   */
  public void createCachedCode(Code code)
    throws DAOException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(createCachedCodeSQL))
    {
      if (StringUtil.isNullOrEmpty(code.getId()))
      {
        statement.setString(1, String.valueOf(IDGenerator.nextUUID(dataSource)));
      }
      else
      {
        statement.setString(1, code.getId());
      }

      statement.setObject(2, code.getCategoryId());
      statement.setString(3, code.getName());
      statement.setString(4, code.getDescription());
      statement.setString(5, code.getValue());

      if (statement.executeUpdate() != 1)
      {
        throw new DAOException("No rows were affected as a result of executing the SQL statement ("
            + createCachedCodeSQL + ")");
      }
    }
    catch (Throwable e)
    {
      throw new DAOException("Failed to add the cached code (" + code.getName()
          + ") for the cached code category (" + code.getCategoryId() + ") to the database", e);
    }
  }

  /**
   * Create the new cached code category.
   *
   * @param cachedCodeCategory the <code>CachedCodeCategory</code> instance containing the
   *                           information for the new code category
   * @throws DAOException
   */
  public void createCachedCodeCategory(CachedCodeCategory cachedCodeCategory)
    throws DAOException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(createCachedCodeCategorySQL))
    {
      statement.setObject(1, cachedCodeCategory.getId());
      statement.setBytes(2, (cachedCodeCategory.getCodeData() != null)
          ? cachedCodeCategory.getCodeData().getBytes("UTF-8")
          : null);
      statement.setTimestamp(3, new Timestamp(cachedCodeCategory.getLastUpdated().getTime()));
      statement.setTimestamp(4, new Timestamp(cachedCodeCategory.getCached().getTime()));

      if (statement.executeUpdate() != 1)
      {
        throw new DAOException("No rows were affected as a result of executing the SQL statement ("
            + createCachedCodeCategorySQL + ")");
      }
    }
    catch (Throwable e)
    {
      throw new DAOException("Failed to add the cached code category ("
          + cachedCodeCategory.getId() + ") to the database", e);
    }
  }

  /**
   * Create the new code.
   *
   * @param code the <code>Code</code> instance containing the information for the new code
   *
   * @throws DAOException
   */
  public void createCode(Code code)
    throws DAOException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(createCodeSQL))
    {
      if (StringUtil.isNullOrEmpty(code.getId()))
      {
        statement.setString(1, String.valueOf(IDGenerator.nextUUID(dataSource)));
      }
      else
      {
        statement.setString(1, code.getId());
      }

      statement.setObject(2, code.getCategoryId());
      statement.setString(3, code.getName());
      statement.setString(4, code.getDescription());
      statement.setString(5, code.getValue());

      if (statement.executeUpdate() != 1)
      {
        throw new DAOException("No rows were affected as a result of executing the SQL statement ("
            + createCodeSQL + ")");
      }
    }
    catch (Throwable e)
    {
      throw new DAOException("Failed to add the code (" + code.getName()
          + ") for the code category (" + code.getCategoryId() + ") to the database", e);
    }
  }

  /**
   * Create the new code category.
   *
   * @param codeCategory the <code>CodeCategory</code> instance containing the information for the
   *                     new code category
   *
   * @throws DAOException
   */
  public void createCodeCategory(CodeCategory codeCategory)
    throws DAOException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(createCodeCategorySQL))
    {
      statement.setObject(1, codeCategory.getId());
      statement.setInt(2, codeCategory.getCategoryType().getCode());
      statement.setString(3, codeCategory.getName());
      statement.setString(4, codeCategory.getDescription());
      statement.setBytes(5, (codeCategory.getCodeData() != null)
          ? codeCategory.getCodeData().getBytes("UTF-8")
          : null);
      statement.setString(6, codeCategory.getEndPoint());
      statement.setBoolean(7, codeCategory.getIsEndPointSecure());
      statement.setBoolean(8, codeCategory.getIsCacheable());

      if (codeCategory.getCacheExpiry() == null)
      {
        statement.setNull(9, java.sql.Types.INTEGER);
      }
      else
      {
        statement.setInt(9, codeCategory.getCacheExpiry());
      }

      Timestamp updated = new Timestamp(System.currentTimeMillis());

      statement.setTimestamp(10, updated);

      if (statement.executeUpdate() != 1)
      {
        throw new DAOException("No rows were affected as a result of executing the SQL statement ("
            + createCodeCategorySQL + ")");
      }

      codeCategory.setUpdated(updated);
    }
    catch (Throwable e)
    {
      throw new DAOException("Failed to add the code category (" + codeCategory.getId()
          + ") to the database", e);
    }
  }

  /**
   * Delete the cached code category.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the cached code
   *           category
   *
   * @throws DAOException
   */
  public void deleteCachedCodeCategory(UUID id)
    throws DAOException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(deleteCachedCodeCategorySQL))
    {
      statement.setObject(1, id);

      if (statement.executeUpdate() != 1)
      {
        throw new DAOException("No rows were affected as a result of executing the SQL statement ("
            + deleteCachedCodeCategorySQL + ")");
      }
    }
    catch (Throwable e)
    {
      throw new DAOException("Failed to delete the cached code category (" + id
          + ") in the database", e);
    }
  }

  /**
   * Delete the code.
   *
   * @param codeCategoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                       code category
   * @param id             the ID uniquely identifying the code
   *
   * @throws DAOException
   */
  public void deleteCode(UUID codeCategoryId, String id)
    throws DAOException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(deleteCodeSQL))
    {
      statement.setObject(1, codeCategoryId);
      statement.setString(2, id);

      if (statement.executeUpdate() != 1)
      {
        throw new DAOException("No rows were affected as a result of executing the SQL statement ("
            + deleteCodeSQL + ")");
      }
    }
    catch (Throwable e)
    {
      throw new DAOException("Failed to delete the code (" + id + ") for the code category ("
          + codeCategoryId + ") in the database", e);
    }
  }

  /**
   * Delete the code category.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the code category
   *
   * @throws DAOException
   */
  public void deleteCodeCategory(UUID id)
    throws DAOException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(deleteCodeCategorySQL))
    {
      statement.setObject(1, id);

      if (statement.executeUpdate() != 1)
      {
        throw new DAOException("No rows were affected as a result of executing the SQL statement ("
            + deleteCodeCategorySQL + ")");
      }
    }
    catch (Throwable e)
    {
      throw new DAOException("Failed to delete the code category (" + id + ") in the database", e);
    }
  }

  /**
   * Retrieve the cached code category.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the cached code
   *           category
   *
   * @return the cached code category or <code>null</code> if the cached code category could not be
   *         found
   *
   * @throws DAOException
   */
  public CachedCodeCategory getCachedCodeCategory(UUID id)
    throws DAOException
  {
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
      throw new DAOException("Failed to retrieve the cached code category (" + id
          + ") from the database", e);
    }
  }

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
  public List<Code> getCachedCodesForCachedCodeCategory(UUID id)
    throws DAOException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement =
          connection.prepareStatement(getCachedCodesForCachedCodeCategorySQL))
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
      throw new DAOException("Failed to retrieve the cached codes for the cached code category ("
          + id + ") from the database", e);
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
   *
   * @throws DAOException
   */
  public Code getCode(UUID codeCategoryId, String id)
    throws DAOException
  {
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
      throw new DAOException("Failed to retrieve the code (" + id + ") from the database", e);
    }
  }

  /**
   * Returns all the code categories.
   *
   * @param retrieveCodes  retrieve the codes and/or code data for the code categories
   *
   * @return all the code categories
   *
   * @throws DAOException
   */
  public List<CodeCategory> getCodeCategories(boolean retrieveCodes)
    throws DAOException
  {
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
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the code category
   *
   * @return the code category or <code>null</code> if the code category could not be found
   *
   * @throws DAOException
   */
  public CodeCategory getCodeCategory(UUID id)
    throws DAOException
  {
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
      throw new DAOException("Failed to retrieve the code category (" + id + ") from the database",
          e);
    }
  }

  /**
   * Returns all the codes for the code category.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the code category
   *
   * @return all the codes for the code category
   *
   * @throws DAOException
   */
  public List<Code> getCodesForCodeCategory(UUID id)
    throws DAOException
  {
    try (Connection connection = dataSource.getConnection())
    {
      return getCodesForCodeCategory(connection, id);
    }
    catch (Throwable e)
    {
      throw new DAOException("Failed to retrieve the codes for the code category (" + id
          + ") from the database", e);
    }
  }

  /**
   * Returns the number of code categories.
   *
   * @return the number of code categories
   *
   * @throws DAOException
   */
  public int getNumberOfCodeCategories()
    throws DAOException
  {
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
          throw new DAOException(
              "No results were returned as a result of executing the SQL statement ("
              + getNumberOfCodeCategoriesSQL + ")");
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
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the code category
   *
   * @return the number of codes for the code category
   *
   * @throws DAOException
   */
  public int getNumberOfCodesForCodeCategory(UUID id)
    throws DAOException
  {
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
          throw new DAOException(
              "No results were returned as a result of executing the SQL statement ("
              + getNumberOfCodesForCodeCategorySQL + ")");
        }
      }
    }
    catch (Throwable e)
    {
      throw new DAOException("Failed to retrieve the number of codes for the code category (" + id
          + ") in the database", e);
    }
  }

  /**
   * Initialise the <code>CodesDAO</code> instance.
   */
  @PostConstruct
  public void init()
  {
    try
    {
      dataSource = InitialContext.doLookup("java:app/jdbc/ApplicationDataSource");
    }
    catch (Throwable ignored) {}

    if (dataSource == null)
    {
      try
      {
        dataSource = InitialContext.doLookup("java:comp/env/jdbc/ApplicationDataSource");
      }
      catch (Throwable ignored) {}
    }

    if (dataSource == null)
    {
      throw new DAOException("Failed to retrieve the application data source"
          + " using the JNDI names (java:app/jdbc/ApplicationDataSource) and"
          + " (java:comp/env/jdbc/ApplicationDataSource)");
    }

    try
    {
      // Retrieve the database meta data
      String schemaSeparator;
      String idQuote;

      try (Connection connection = dataSource.getConnection())
      {
        DatabaseMetaData metaData = connection.getMetaData();

        // Retrieve the schema separator for the database
        schemaSeparator = metaData.getCatalogSeparator();

        if ((schemaSeparator == null) || (schemaSeparator.length() == 0))
        {
          schemaSeparator = ".";
        }

        // Retrieve the identifier enquoting string for the database
        idQuote = metaData.getIdentifierQuoteString();

        if ((idQuote == null) || (idQuote.length() == 0))
        {
          idQuote = "\"";
        }
      }

      // Determine the schema prefix
      String schemaPrefix = idQuote + DataAccessObject.DEFAULT_DATABASE_SCHEMA + idQuote
        + schemaSeparator;

      // Build the SQL statements for the DAO
      buildStatements(schemaPrefix);
    }
    catch (Throwable e)
    {
      throw new DAOException("Failed to initialise the " + getClass().getName()
          + " data access object: " + e.getMessage(), e);
    }

    idGenerator = new IDGenerator(dataSource, DataAccessObject.DEFAULT_DATABASE_SCHEMA);
  }

  /**
   * Is the cached code category current?
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the cached code
   *           category
   *
   * @return <code>true</code> if the cached code category is current or <code>false</code>
   *         otherwise
   *
   * @throws DAOException
   */
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
      throw new DAOException("Failed to check whether the cached code category (" + id
          + ") in the database is current", e);
    }
  }

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
  public CachedCodeCategory updateCachedCodeCategory(CachedCodeCategory cachedCodeCategory)
    throws DAOException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(updateCachedCodeCategorySQL))
    {
      Date cached = new Date();

      statement.setBytes(1, (cachedCodeCategory.getCodeData() != null)
          ? cachedCodeCategory.getCodeData().getBytes("UTF-8")
          : null);
      statement.setTimestamp(2, new Timestamp(cached.getTime()));
      statement.setTimestamp(3, new Timestamp(cachedCodeCategory.getLastUpdated().getTime()));
      statement.setObject(4, cachedCodeCategory.getId());

      if (statement.executeUpdate() != 1)
      {
        throw new DAOException("No rows were affected as a result of executing the SQL statement ("
            + updateCachedCodeCategorySQL + ")");
      }

      cachedCodeCategory.setCached(cached);

      return cachedCodeCategory;
    }
    catch (Throwable e)
    {
      throw new DAOException("Failed to update the cached code category ("
          + cachedCodeCategory.getId() + ") in the database", e);
    }
  }

  /**
   * Update the existing code.
   *
   * @param code the <code>Code</code> instance containing the updated information for the code
   *
   * @return the updated code
   *
   * @throws DAOException
   */
  public Code updateCode(Code code)
    throws DAOException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(updateCodeSQL))
    {
      statement.setString(1, code.getName());
      statement.setString(2, code.getDescription());
      statement.setString(3, code.getValue());
      statement.setString(4, code.getId());

      if (statement.executeUpdate() != 1)
      {
        throw new DAOException("No rows were affected as a result of executing the SQL statement ("
            + updateCodeSQL + ")");
      }

      return code;
    }
    catch (Throwable e)
    {
      throw new DAOException("Failed to update the code (" + code.getId() + ") in the database", e);
    }
  }

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
  public CodeCategory updateCodeCategory(CodeCategory codeCategory)
    throws DAOException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(updateCodeCategorySQL))
    {
      Date updated = new Date();

      statement.setInt(1, codeCategory.getCategoryType().getCode());
      statement.setString(2, codeCategory.getName());
      statement.setString(3, codeCategory.getDescription());
      statement.setBytes(4, (codeCategory.getCodeData() != null)
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

      statement.setTimestamp(9, new Timestamp(updated.getTime()));
      statement.setObject(10, codeCategory.getId());

      if (statement.executeUpdate() != 1)
      {
        throw new DAOException("No rows were affected as a result of executing the SQL statement ("
            + updateCodeCategorySQL + ")");
      }

      codeCategory.setUpdated(updated);

      return codeCategory;
    }
    catch (Throwable e)
    {
      throw new DAOException("Failed to update the code category (" + codeCategory.getId()
          + ") in the database", e);
    }
  }

  /**
   * Build the SQL statements for the DAO.
   *
   * @param schemaPrefix the schema prefix to prepend to database objects referenced by the DAO
   */
  protected void buildStatements(String schemaPrefix)
  {
    // cachedCodeCategoryExistsSQL
    cachedCodeCategoryExistsSQL = "SELECT CCC.ID FROM " + schemaPrefix
        + "CACHED_CODE_CATEGORIES CCC WHERE CCC.ID=?";

    // codeCategoryExistsSQL
    codeCategoryExistsSQL = "SELECT CC.ID FROM " + schemaPrefix + "CODE_CATEGORIES CC"
        + " WHERE CC.ID=?";

    // createCachedCodeCategorySQL
    createCachedCodeCategorySQL = "INSERT INTO " + schemaPrefix + "CACHED_CODE_CATEGORIES"
        + " (ID, CODE_DATA, LAST_UPDATED, CACHED) VALUES (?, ?, ?, ?)";

    // createCachedCodeSQL
    createCachedCodeSQL = "INSERT INTO " + schemaPrefix + "CACHED_CODES"
        + " (ID, CATEGORY_ID, NAME, DESCRIPTION, VALUE) VALUES (?, ?, ?, ?, ?)";

    // createCodeCategorySQL
    createCodeCategorySQL = "INSERT INTO " + schemaPrefix + "CODE_CATEGORIES"
        + " (ID, CATEGORY_TYPE, NAME, DESCRIPTION, CODE_DATA, ENDPOINT, IS_ENDPOINT_SECURE,"
        + " IS_CACHEABLE, CACHE_EXPIRY, UPDATED) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    // createCodeSQL
    createCodeSQL = "INSERT INTO " + schemaPrefix + "CODES"
        + " (ID, CATEGORY_ID, NAME, DESCRIPTION, VALUE) VALUES (?, ?, ?, ?, ?)";

    // deleteCachedCodeCategorySQL
    deleteCachedCodeCategorySQL = "DELETE FROM " + schemaPrefix
        + "CACHED_CODE_CATEGORIES CCC WHERE CCC.ID=?";

    // deleteCodeCategorySQL
    deleteCodeCategorySQL = "DELETE FROM " + schemaPrefix + "CODE_CATEGORIES CC WHERE CC.ID=?";

    // deleteCodeSQL
    deleteCodeSQL = "DELETE FROM " + schemaPrefix + "CODES C WHERE C.CATEGORY_ID=? AND C.ID=?";

    // getCachedCodeCategoryCachedSQL
    getCachedCodeCategoryCachedSQL = "SELECT CCC.CACHED FROM " + schemaPrefix
        + "CACHED_CODE_CATEGORIES CCC WHERE CCC.ID=?";

    // getCachedCodeCategorySQL
    getCachedCodeCategorySQL = "SELECT CCC.ID, CCC.CODE_DATA, CCC.LAST_UPDATED, CCC.CACHED FROM "
        + schemaPrefix + "CACHED_CODE_CATEGORIES CCC WHERE CCC.ID=?";

    // getCodeCategoriesSQL
    getCodeCategoriesSQL = "SELECT CC.ID, CC.CATEGORY_TYPE, CC.NAME, CC.DESCRIPTION, CC.CODE_DATA,"
        + " CC.ENDPOINT, CC.IS_ENDPOINT_SECURE, CC.IS_CACHEABLE, CC.CACHE_EXPIRY, CC.UPDATED FROM "
        + schemaPrefix + "CODE_CATEGORIES ORDER BY CC.NAME";

    // getCodeCategoriesNoDataSQL
    getCodeCategoriesNoDataSQL = "SELECT CC.ID, CC.CATEGORY_TYPE, CC.NAME, CC.DESCRIPTION,"
        + " CC.ENDPOINT, CC.IS_ENDPOINT_SECURE, CC.IS_CACHEABLE, CC.CACHE_EXPIRY, CC.UPDATED FROM "
        + schemaPrefix + "CODE_CATEGORIES CC ORDER BY CC.NAME";

    // getCodeCategoryCacheExpirySQL
    getCodeCategoryCacheExpirySQL = "SELECT CC.CACHE_EXPIRY FROM " + schemaPrefix
        + "CODE_CATEGORIES CC WHERE CC.ID=?";

    // getCodeCategorySQL
    getCodeCategorySQL = "SELECT CC.ID, CC.CATEGORY_TYPE, CC.NAME, CC.DESCRIPTION, CC.CODE_DATA,"
        + " CC.ENDPOINT, CC.IS_ENDPOINT_SECURE, CC.IS_CACHEABLE, CC.CACHE_EXPIRY, CC.UPDATED FROM "
        + schemaPrefix + "CODE_CATEGORIES CC WHERE CC.ID=?";

    // getCodesForCodeCategorySQL
    getCodesForCodeCategorySQL = "SELECT C.ID, C.CATEGORY_ID, C.NAME, C.DESCRIPTION, C.VALUE"
        + " FROM " + schemaPrefix + "CODES C WHERE C.CATEGORY_ID=? ORDER BY C.NAME";

    // getCachedCodesForCachedCodeCategorySQL
    getCachedCodesForCachedCodeCategorySQL = "SELECT CC.ID, CC.CATEGORY_ID, CC.NAME,"
        + " CC.DESCRIPTION, CC.VALUE FROM " + schemaPrefix + "CACHED_CODES CC"
        + " WHERE CC.CATEGORY_ID=? ORDER BY CC.NAME";

    // getCodeSQL
    getCodeSQL = "SELECT C.ID, C.CATEGORY_ID, C.NAME, C.DESCRIPTION, C.VALUE FROM " + schemaPrefix
        + "CODES C WHERE C.CATEGORY_ID=? AND C.ID=?";

    // getNumberOfCodesForCodeCategorySQL
    getNumberOfCodesForCodeCategorySQL = "SELECT COUNT(C.ID) FROM " + schemaPrefix + "CODES C"
        + " WHERE C.CATEGORY_ID=?";

    // getNumberOfCodeCategoriesSQL
    getNumberOfCodeCategoriesSQL = "SELECT COUNT(CC.ID) FROM " + schemaPrefix
        + "CODE_CATEGORIES CC";

    // updateCachedCodeCategorySQL
    updateCachedCodeCategorySQL = "UPDATE " + schemaPrefix + "CACHED_CODE_CATEGORIES CCC"
        + " SET CCC.CODE_DATA=?, CCC.LAST_UPDATED, CCC.CACHED=? WHERE CCC.ID=?";

    // updateCodeCategorySQL
    updateCodeCategorySQL = "UPDATE " + schemaPrefix + "CODE_CATEGORIES CC"
        + " SET CC.CATEGORY_TYPE=?, CC.NAME=?, CC.DESCRIPTION=?, CC.CODE_DATA=?, CC.ENDPOINT=?,"
        + " CC.IS_ENDPOINT_SECURE=?, CC.IS_CACHEABLE=?, CC.CACHE_EXPIRY=?, CC.UPDATED=?"
        + " WHERE CC.ID=?";

    // updateCodeSQL
    updateCodeSQL = "UPDATE " + schemaPrefix + "CODES C"
        + " SET C.NAME=?, C.DESCRIPTION=?, C.VALUE=? WHERE C.ID=?";
  }

  private CachedCodeCategory getCachedCodeCategory(ResultSet rs)
    throws SQLException, UnsupportedEncodingException
  {
    return new CachedCodeCategory((UUID) rs.getObject(1), (rs.getBytes(2) != null)
        ? new String(rs.getBytes(2), "UTF-8")
        : null, rs.getTimestamp(3), rs.getTimestamp(4));

  }

  private Date getCachedCodeCategoryCached(Connection connection, UUID id)
    throws SQLException
  {
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
      throw new DAOException("Failed to retrieve the cached date and time for the cached code"
          + " category (" + id + ") from the database", e);
    }
  }

  private Code getCode(ResultSet rs)
    throws SQLException
  {
    return new Code(rs.getString(1), (UUID) rs.getObject(2), rs.getString(3), rs.getString(4),
        rs.getString(5));
  }

  private CodeCategory getCodeCategory(ResultSet rs)
    throws SQLException, UnsupportedEncodingException
  {
    int cacheExpiry = rs.getInt(9);

    boolean cacheExpiryIsNull = rs.wasNull();

    return new CodeCategory((UUID) rs.getObject(1), CodeCategoryType.fromCode(rs.getInt(2)),
        rs.getString(3), rs.getString(4), (rs.getBytes(5) != null)
        ? new String(rs.getBytes(5), "UTF-8")
        : null, rs.getString(6), rs.getBoolean(7), rs.getBoolean(8), cacheExpiryIsNull
        ? null
        : cacheExpiry, rs.getTimestamp(10));
  }

  private Integer getCodeCategoryCacheExpiry(Connection connection, UUID id)
    throws SQLException
  {
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
      throw new DAOException("Failed to retrieve the cache expiry for the code category (" + id
          + ") from the database", e);
    }
  }

  private CodeCategory getCodeCategoryNoData(ResultSet rs)
    throws SQLException
  {
    int cacheExpiry = rs.getInt(8);

    boolean cacheExpiryIsNull = rs.wasNull();

    return new CodeCategory((UUID) rs.getObject(1), CodeCategoryType.fromCode(rs.getInt(2)),
        rs.getString(3), rs.getString(4), rs.getString(5), rs.getBoolean(6), rs.getBoolean(7),
        cacheExpiryIsNull
        ? null
        : cacheExpiry, rs.getTimestamp(9));
  }

  private List<Code> getCodesForCodeCategory(Connection connection, UUID id)
    throws SQLException
  {
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

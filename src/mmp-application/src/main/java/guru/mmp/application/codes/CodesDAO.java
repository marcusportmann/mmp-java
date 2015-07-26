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

import guru.mmp.application.persistence.DAOException;
import guru.mmp.application.persistence.DataAccessObject;
import guru.mmp.common.persistence.IDGenerator;
import guru.mmp.common.util.StringUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//~--- JDK imports ------------------------------------------------------------

import java.io.UnsupportedEncodingException;

import java.sql.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;

import javax.naming.InitialContext;

import javax.sql.DataSource;

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
  private String getCodeCategoriesForOrganisationSQL;
  private String getCodeCategoriesNoDataForOrganisationSQL;
  private String getCodeCategoryCacheExpirySQL;
  private String getCodeCategorySQL;
  private String getCodeSQL;
  private String getCodesForCodeCategorySQL;
  private String getNumberOfCodeCategoriesForOrganisationSQL;
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
   * Check whether the cached code category with the specified ID exists.
   *
   * @param id the ID uniquely identifying the cached code category
   *
   * @return <code>true</code> if the cached code category exists or <code>false</code> otherwise
   *
   * @throws DAOException
   */
  public boolean cachedCodeCategoryExists(String id)
    throws DAOException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(cachedCodeCategoryExistsSQL))
    {
      statement.setString(1, id);

      try (ResultSet rs = statement.executeQuery())
      {
        return rs.next();
      }
    }
    catch (DAOException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new DAOException("Failed to check whether the cached code category (" + id
          + ") exists in the database", e);
    }
  }

  /**
   * Check whether the code category with the specified ID exists.
   *
   * @param id the ID uniquely identifying the code category
   *
   * @return <code>true</code> if the code category exists or <code>false</code> otherwise
   *
   * @throws DAOException
   */
  public boolean codeCategoryExists(String id)
    throws DAOException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(codeCategoryExistsSQL))
    {
      statement.setString(1, id);

      try (ResultSet rs = statement.executeQuery())
      {
        return rs.next();
      }
    }
    catch (DAOException e)
    {
      throw e;
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
        statement.setString(1, String.valueOf(idGenerator.next("Application.CodeId")));
      }
      else
      {
        statement.setString(1, code.getId());
      }

      statement.setString(2, code.getCategoryId());
      statement.setString(3, code.getName());
      statement.setString(4, code.getDescription());
      statement.setString(5, code.getValue());

      if (statement.executeUpdate() != 1)
      {
        throw new DAOException("Failed to add the cached code (" + code.getName()
            + ") for the cached code category (" + code.getCategoryId() + ") to the"
            + " database: No rows were affected as a result of executing the SQL statement ("
            + createCachedCodeSQL + ")");
      }
    }
    catch (DAOException e)
    {
      throw e;
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
      statement.setString(1, cachedCodeCategory.getId());
      statement.setBytes(2, (cachedCodeCategory.getCodeData() != null)
          ? cachedCodeCategory.getCodeData().getBytes("UTF-8")
          : null);
      statement.setTimestamp(3, new Timestamp(cachedCodeCategory.getLastUpdated().getTime()));
      statement.setTimestamp(4, new Timestamp(cachedCodeCategory.getCached().getTime()));

      if (statement.executeUpdate() != 1)
      {
        throw new DAOException("Failed to add the cached code category ("
            + cachedCodeCategory.getId() + ") to the"
            + " database: No rows were affected as a result of executing the SQL statement ("
            + createCachedCodeCategorySQL + ")");
      }
    }
    catch (DAOException e)
    {
      throw e;
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
        statement.setString(1, String.valueOf(idGenerator.next("Application.CodeId")));
      }
      else
      {
        statement.setString(1, code.getId());
      }

      statement.setString(2, code.getCategoryId());
      statement.setString(3, code.getName());
      statement.setString(4, code.getDescription());
      statement.setString(5, code.getValue());

      if (statement.executeUpdate() != 1)
      {
        throw new DAOException("Failed to add the code (" + code.getName()
            + ") for the code category (" + code.getCategoryId() + ") to the"
            + " database: No rows were affected as a result of executing the SQL statement ("
            + createCodeSQL + ")");
      }
    }
    catch (DAOException e)
    {
      throw e;
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
   * @throws DAOException
   */
  public void createCodeCategory(CodeCategory codeCategory)
    throws DAOException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(createCodeCategorySQL))
    {
      statement.setString(1, codeCategory.getId());
      statement.setString(2, codeCategory.getOrganisation());
      statement.setInt(3, codeCategory.getCategoryType().getCode());
      statement.setString(4, codeCategory.getName());
      statement.setString(5, codeCategory.getDescription());
      statement.setBytes(6, (codeCategory.getCodeData() != null)
          ? codeCategory.getCodeData().getBytes("UTF-8")
          : null);
      statement.setString(7, codeCategory.getEndPoint());
      statement.setBoolean(8, codeCategory.getIsEndPointSecure());
      statement.setBoolean(9, codeCategory.getIsCacheable());

      if (codeCategory.getCacheExpiry() == null)
      {
        statement.setNull(10, java.sql.Types.INTEGER);
      }
      else
      {
        statement.setInt(10, codeCategory.getCacheExpiry());
      }

      statement.setTimestamp(11, new Timestamp(System.currentTimeMillis()));

      if (statement.executeUpdate() != 1)
      {
        throw new DAOException("Failed to add the code category (" + codeCategory.getId()
            + ") to the"
            + " database: No rows were affected as a result of executing the SQL statement ("
            + createCodeCategorySQL + ")");
      }
    }
    catch (DAOException e)
    {
      throw e;
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
   * @param id the ID uniquely identifying the cached code category
   *
   * @throws DAOException
   */
  public void deleteCachedCodeCategory(String id)
    throws DAOException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(deleteCachedCodeCategorySQL))
    {
      statement.setString(1, id);

      if (statement.executeUpdate() != 1)
      {
        throw new DAOException("Failed to delete the cached code category (" + id
            + ") in the database:"
            + " No rows were affected as a result of executing the SQL statement ("
            + deleteCachedCodeCategorySQL + ")");
      }
    }
    catch (DAOException e)
    {
      throw e;
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
   * @param id the ID uniquely identifying the code
   *
   * @throws DAOException
   */
  public void deleteCode(String id)
    throws DAOException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(deleteCodeSQL))
    {
      statement.setString(1, id);

      if (statement.executeUpdate() != 1)
      {
        throw new DAOException("Failed to delete the code (" + id + ") in the database:"
            + " No rows were affected as a result of executing the SQL statement (" + deleteCodeSQL
            + ")");
      }
    }
    catch (DAOException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new DAOException("Failed to delete the code (" + id + ") in the database", e);
    }
  }

  /**
   * Delete the code category.
   *
   * @param id the ID uniquely identifying the code category
   *
   * @throws DAOException
   */
  public void deleteCodeCategory(String id)
    throws DAOException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(deleteCodeCategorySQL))
    {
      statement.setString(1, id);

      if (statement.executeUpdate() != 1)
      {
        throw new DAOException("Failed to delete the code category (" + id + ") in the database:"
            + " No rows were affected as a result of executing the SQL statement ("
            + deleteCodeCategorySQL + ")");
      }
    }
    catch (DAOException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new DAOException("Failed to delete the code category (" + id + ") in the database", e);
    }
  }

  /**
   * Retrieve the cached code category with the specified ID.
   *
   * @param id the ID uniquely identifying the cached code category
   *
   * @return the cached code category with the specified ID or <code>null</code> if the cached code
   *         category could not be found
   *
   * @throws DAOException
   */
  public CachedCodeCategory getCachedCodeCategory(String id)
    throws DAOException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(getCachedCodeCategorySQL))
    {
      statement.setString(1, id);

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
    catch (DAOException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new DAOException("Failed to retrieve the cached code category (" + id
          + ") from the database", e);
    }
  }

  /**
   * Returns all the cached codes for the cached code category with the specified ID.
   *
   * @param cachedCodeCategoryId the ID uniquely identifying the cached code category
   *
   * @return all the cached codes for the cached code category with the specified ID
   *
   * @throws DAOException
   */
  public List<Code> getCachedCodesForCachedCodeCategory(String cachedCodeCategoryId)
    throws DAOException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement =
          connection.prepareStatement(getCachedCodesForCachedCodeCategorySQL))
    {
      statement.setString(1, cachedCodeCategoryId);

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
    catch (DAOException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new DAOException("Failed to retrieve the cached codes for the cached code category ("
          + cachedCodeCategoryId + ") from the database", e);
    }
  }

  /**
   * Retrieve the code with the specified ID.
   *
   * @param id the ID uniquely identifying the code
   *
   * @return the code with the specified ID or <code>null</code> if the code could not be found
   *
   * @throws DAOException
   */
  public Code getCode(String id)
    throws DAOException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(getCodeSQL))
    {
      statement.setString(1, id);

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
    catch (DAOException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new DAOException("Failed to retrieve the code (" + id + ") from the database", e);
    }
  }

  /**
   * Returns all the code categories associated with the organisation identified by the
   * specified organisation code.
   *
   * @param organisation  the organisation code identifying the organisation
   * @param retrieveCodes retrieve the codes and/or code data for the code categories
   *
   * @return all the code categories associated with the organisation identified by the
   *         specified organisation code
   *
   * @throws DAOException
   */
  public List<CodeCategory> getCodeCategoriesForOrganisation(String organisation,
      boolean retrieveCodes)
    throws DAOException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(retrieveCodes
          ? getCodeCategoriesForOrganisationSQL
          : getCodeCategoriesNoDataForOrganisationSQL))
    {
      statement.setString(1, organisation);

      try (ResultSet rs = statement.executeQuery())
      {
        List<CodeCategory> codeCategories = new ArrayList<>();

        while (rs.next())
        {
          if (retrieveCodes)
          {
            codeCategories.add(getCodeCategory(rs));
          }
          else
          {
            codeCategories.add(getCodeCategoryNoData(rs));
          }
        }

        return codeCategories;
      }
    }
    catch (DAOException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new DAOException("Failed to retrieve the code categories for the organisation ("
          + organisation + ") from the database", e);
    }
  }

  /**
   * Retrieve the code category with the specified ID.
   *
   * @param id the ID uniquely identifying the code category
   *
   * @return the code category with the specified ID or <code>null</code> if the code category
   *         could not be found
   *
   * @throws DAOException
   */
  public CodeCategory getCodeCategory(String id)
    throws DAOException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(getCodeCategorySQL))
    {
      statement.setString(1, id);

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
    catch (DAOException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new DAOException("Failed to retrieve the code category (" + id + ") from the database",
          e);
    }
  }

  /**
   * Returns all the codes for the code category with the specified ID.
   *
   * @param codeCategoryId the ID uniquely identifying the code category
   *
   * @return all the codes for the code category with the specified ID
   *
   * @throws DAOException
   */
  public List<Code> getCodesForCodeCategory(String codeCategoryId)
    throws DAOException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(getCodesForCodeCategorySQL))
    {
      statement.setString(1, codeCategoryId);

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
    catch (DAOException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new DAOException("Failed to retrieve the codes for the code category ("
          + codeCategoryId + ") from the database", e);
    }
  }

  /**
   * Returns the number of code categories associated with the organisation identified by the
   * specified organisation code.
   *
   * @param organisation the organisation code identifying the organisation
   *
   * @return the number of code categories associated with the organisation identified by the
   *         specified organisation code
   *
   * @throws DAOException
   */
  public int getNumberOfCodeCategoriesForOrganisation(String organisation)
    throws DAOException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement =
          connection.prepareStatement(getNumberOfCodeCategoriesForOrganisationSQL))
    {
      statement.setString(1, organisation);

      try (ResultSet rs = statement.executeQuery())
      {
        if (rs.next())
        {
          return rs.getInt(1);
        }
        else
        {
          throw new DAOException("Failed to retrieve the number of code categories for the"
              + " organisation (" + organisation + ") in the database:"
              + " No results were returned as a result of executing the SQL statement ("
              + getNumberOfCodeCategoriesForOrganisationSQL + ")");
        }
      }
    }
    catch (DAOException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new DAOException("Failed to retrieve the number of code categories for the"
          + " organisation (" + organisation + ") in the database", e);
    }
  }

  /**
   * Returns the number of codes for the code category with the specified ID.
   *
   * @param codeCategoryId the ID uniquely identifying the code category
   *
   * @return the number of codes for the code category with the specified ID
   *
   * @throws DAOException
   */
  public int getNumberOfCodesForCodeCategory(String codeCategoryId)
    throws DAOException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(getNumberOfCodesForCodeCategorySQL))
    {
      statement.setString(1, codeCategoryId);

      try (ResultSet rs = statement.executeQuery())
      {
        if (rs.next())
        {
          return rs.getInt(1);
        }
        else
        {
          throw new DAOException("Failed to retrieve the number of codes for the code category ("
              + codeCategoryId + ") in the database:"
              + " No results were returned as a result of executing the SQL statement ("
              + getNumberOfCodesForCodeCategorySQL + ")");
        }
      }
    }
    catch (DAOException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new DAOException("Failed to retrieve the number of codes for the code category ("
          + codeCategoryId + ") in the database", e);
    }
  }

  /**
   * Initialise the <code>DataAccessObject</code>.
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
      String schemaPrefix = idQuote + DataAccessObject.DEFAULT_APPLICATION_DATABASE_SCHEMA
        + idQuote + schemaSeparator;

      // Build the SQL statements for the DAO
      buildStatements(schemaPrefix);
    }
    catch (Throwable e)
    {
      throw new DAOException("Failed to initialise the " + getClass().getName()
          + " data access object: " + e.getMessage(), e);
    }

    idGenerator = new IDGenerator(dataSource, DataAccessObject.DEFAULT_APPLICATION_DATABASE_SCHEMA);
  }

  /**
   * Is the cached code category current?
   *
   * @param id the ID uniquely identifying the cached code category
   *
   * @return <code>true</code> if the cached code category is current or <code>false</code>
   *         otherwise
   *
   * @throws DAOException
   */
  public boolean isCachedCodeCategoryCurrent(String id)
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
    catch (DAOException e)
    {
      throw e;
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
      statement.setString(4, cachedCodeCategory.getId());

      if (statement.executeUpdate() != 1)
      {
        throw new DAOException("Failed to update the cached code category ("
            + cachedCodeCategory.getId() + ") in the"
            + " database: No rows were affected as a result of executing the SQL statement ("
            + updateCachedCodeCategorySQL + ")");
      }

      cachedCodeCategory.setCached(cached);

      return cachedCodeCategory;
    }
    catch (DAOException e)
    {
      throw e;
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
      statement.setString(1, code.getCategoryId());
      statement.setString(2, code.getName());
      statement.setString(3, code.getDescription());
      statement.setString(4, code.getValue());
      statement.setString(5, code.getId());

      if (statement.executeUpdate() != 1)
      {
        throw new DAOException("Failed to update the code (" + code.getId() + ") in the database:"
            + " No rows were affected as a result of executing the SQL statement (" + updateCodeSQL
            + ")");
      }

      return code;
    }
    catch (DAOException e)
    {
      throw e;
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

      statement.setString(1, codeCategory.getOrganisation());
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

      statement.setTimestamp(10, new Timestamp(updated.getTime()));
      statement.setString(11, codeCategory.getId());

      if (statement.executeUpdate() != 1)
      {
        throw new DAOException("Failed to update the code category (" + codeCategory.getId()
            + ") in the"
            + " database: No rows were affected as a result of executing the SQL statement ("
            + updateCodeCategorySQL + ")");
      }

      codeCategory.setUpdated(updated);

      return codeCategory;
    }
    catch (DAOException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new DAOException("Failed to update the code category (" + codeCategory.getId()
          + ") in the database", e);
    }
  }

  /**
   * This method must be implemented by all classes derived from <code>DataAccessObject</code> and
   * should contain the code to generate the SQL statements for the DAO.
   *
   * @param schemaPrefix the schema prefix to append to database objects reference by the DAO
   *
   * @throws SQLException if a database error occurs
   */
  protected void buildStatements(String schemaPrefix)
    throws SQLException
  {
    // cachedCodeCategoryExistsSQL
    cachedCodeCategoryExistsSQL = "SELECT ID" + " FROM " + schemaPrefix + "CACHED_CODE_CATEGORIES"
        + " WHERE ID=?";

    // codeCategoryExistsSQL
    codeCategoryExistsSQL = "SELECT ID" + " FROM " + schemaPrefix + "CODE_CATEGORIES"
        + " WHERE ID=?";

    // createCachedCodeCategorySQL
    createCachedCodeCategorySQL = "INSERT INTO " + schemaPrefix + "CACHED_CODE_CATEGORIES"
        + " (ID, CODE_DATA, LAST_UPDATED, CACHED)" + " VALUES (?, ?, ?, ?)";

    // createCachedCodeSQL
    createCachedCodeSQL = "INSERT INTO " + schemaPrefix + "CACHED_CODES"
        + " (ID, CATEGORY_ID, NAME, DESCRIPTION, VALUE)" + " VALUES (?, ?, ?, ?, ?)";

    // createCodeCategorySQL
    createCodeCategorySQL = "INSERT INTO " + schemaPrefix + "CODE_CATEGORIES"
        + " (ID, ORGANISATION, CATEGORY_TYPE, NAME, DESCRIPTION, CODE_DATA, ENDPOINT,"
        + " IS_ENDPOINT_SECURE, IS_CACHEABLE, CACHE_EXPIRY, UPDATED)"
        + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    // createCodeSQL
    createCodeSQL = "INSERT INTO " + schemaPrefix + "CODES"
        + " (ID, CATEGORY_ID, NAME, DESCRIPTION, VALUE)" + " VALUES (?, ?, ?, ?, ?)";

    // deleteCachedCodeCategorySQL
    deleteCachedCodeCategorySQL = "DELETE FROM " + schemaPrefix
        + "CACHED_CODE_CATEGORIES WHERE ID=?";

    // deleteCodeCategorySQL
    deleteCodeCategorySQL = "DELETE FROM " + schemaPrefix + "CODE_CATEGORIES WHERE ID=?";

    // deleteCodeSQL
    deleteCodeSQL = "DELETE FROM " + schemaPrefix + "CODES WHERE ID=?";

    // getCachedCodeCategoryCachedSQL
    getCachedCodeCategoryCachedSQL = "SELECT CACHED" + " FROM " + schemaPrefix
        + "CACHED_CODE_CATEGORIES" + " WHERE ID=?";

    // getCachedCodeCategorySQL
    getCachedCodeCategorySQL = "SELECT ID, CODE_DATA, LAST_UPDATED, CACHED" + " FROM "
        + schemaPrefix + "CACHED_CODE_CATEGORIES" + " WHERE ID=?";

    // getCodeCategoriesForOrganisationSQL
    getCodeCategoriesForOrganisationSQL =
      "SELECT ID, ORGANISATION, CATEGORY_TYPE, NAME,"
      + " DESCRIPTION, CODE_DATA, ENDPOINT, IS_ENDPOINT_SECURE, IS_CACHEABLE, CACHE_EXPIRY, UPDATED"
      + " FROM " + schemaPrefix + "CODE_CATEGORIES" + " WHERE ORGANISATION=?" + " ORDER BY NAME";

    // getCodeCategoriesNoDataForOrganisationSQL
    getCodeCategoriesNoDataForOrganisationSQL = "SELECT ID, ORGANISATION, CATEGORY_TYPE, NAME,"
        + " DESCRIPTION, ENDPOINT, IS_ENDPOINT_SECURE, IS_CACHEABLE, CACHE_EXPIRY, UPDATED"
        + " FROM " + schemaPrefix + "CODE_CATEGORIES" + " WHERE ORGANISATION=?" + " ORDER BY NAME";

    // getCodeCategoryCacheExpirySQL
    getCodeCategoryCacheExpirySQL = "SELECT CACHE_EXPIRY" + " FROM " + schemaPrefix
        + "CODE_CATEGORIES" + " WHERE ID=?";

    // getCodeCategorySQL
    getCodeCategorySQL = "SELECT ID, ORGANISATION, CATEGORY_TYPE, NAME, DESCRIPTION, CODE_DATA,"
        + " ENDPOINT, IS_ENDPOINT_SECURE, IS_CACHEABLE, CACHE_EXPIRY, UPDATED" + " FROM "
        + schemaPrefix + "CODE_CATEGORIES" + " WHERE ID=?";

    // getCodesForCodeCategorySQL
    getCodesForCodeCategorySQL = "SELECT ID, CATEGORY_ID, NAME, DESCRIPTION, VALUE" + " FROM "
        + schemaPrefix + "CODES" + " WHERE CATEGORY_ID=? ORDER BY NAME";

    // getCachedCodesForCachedCodeCategorySQL
    getCachedCodesForCachedCodeCategorySQL = "SELECT ID, CATEGORY_ID, NAME, DESCRIPTION, VALUE"
        + " FROM " + schemaPrefix + "CACHED_CODES" + " WHERE CATEGORY_ID=? ORDER BY NAME";

    // getCodeSQL
    getCodeSQL = "SELECT ID, CATEGORY_ID, NAME, DESCRIPTION, VALUE" + " FROM " + schemaPrefix
        + "CODES" + " WHERE ID=?";

    // getNumberOfCodesForCodeCategorySQL
    getNumberOfCodesForCodeCategorySQL = "SELECT COUNT(ID) FROM " + schemaPrefix + "CODES"
        + " WHERE CATEGORY_ID=?";

    // getNumberOfCodeCategoriesForOrganisationSQL
    getNumberOfCodeCategoriesForOrganisationSQL = "SELECT COUNT(ID) FROM " + schemaPrefix
        + "CODE_CATEGORIES" + " WHERE ORGANISATION=?";

    // updateCachedCodeCategorySQL
    updateCachedCodeCategorySQL = "UPDATE " + schemaPrefix + "CACHED_CODE_CATEGORIES"
        + " SET CODE_DATA=?, LAST_UPDATED, CACHED=? WHERE ID=?";

    // updateCodeCategorySQL
    updateCodeCategorySQL = "UPDATE " + schemaPrefix + "CODE_CATEGORIES"
        + " SET ORGANISATION=?, CATEGORY_TYPE=?, NAME=?, DESCRIPTION=?, CODE_DATA=?, ENDPOINT=?,"
        + " IS_ENDPOINT_SECURE=?, IS_CACHEABLE=?, CACHE_EXPIRY=?, UPDATED=? WHERE ID=?";

    // updateCodeSQL
    updateCodeSQL = "UPDATE " + schemaPrefix + "CODES"
        + " SET CATEGORY_ID=?, NAME=?, DESCRIPTION=?, VALUE=? WHERE ID=?";
  }

  private CachedCodeCategory getCachedCodeCategory(ResultSet rs)
    throws SQLException, UnsupportedEncodingException
  {
    return new CachedCodeCategory(rs.getString(1), (rs.getBytes(2) != null)
        ? new String(rs.getBytes(2), "UTF-8")
        : null, rs.getTimestamp(3), rs.getTimestamp(4));

  }

  private Date getCachedCodeCategoryCached(Connection connection, String id)
    throws SQLException
  {
    try (PreparedStatement statement = connection.prepareStatement(getCachedCodeCategoryCachedSQL))
    {
      statement.setString(1, id);

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
    catch (DAOException e)
    {
      throw e;
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
    return new Code(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
        rs.getString(5));
  }

  private CodeCategory getCodeCategory(ResultSet rs)
    throws SQLException, UnsupportedEncodingException
  {
    int cacheExpiry = rs.getInt(10);

    boolean cacheExpiryIsNull = rs.wasNull();

    return new CodeCategory(rs.getString(1), rs.getString(2),
        CodeCategoryType.fromCode(rs.getInt(3)), rs.getString(4), rs.getString(5),
        (rs.getBytes(6) != null)
        ? new String(rs.getBytes(6), "UTF-8")
        : null, rs.getString(7), rs.getBoolean(8), rs.getBoolean(9), cacheExpiryIsNull
        ? null
        : cacheExpiry, rs.getTimestamp(11));
  }

  private Integer getCodeCategoryCacheExpiry(Connection connection, String id)
    throws SQLException
  {
    try (PreparedStatement statement = connection.prepareStatement(getCodeCategoryCacheExpirySQL))
    {
      statement.setString(1, id);

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
    catch (DAOException e)
    {
      throw e;
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
    int cacheExpiry = rs.getInt(9);

    boolean cacheExpiryIsNull = rs.wasNull();

    return new CodeCategory(rs.getString(1), rs.getString(2),
        CodeCategoryType.fromCode(rs.getInt(3)), rs.getString(4), rs.getString(5), rs.getString(6),
        rs.getBoolean(7), rs.getBoolean(8), cacheExpiryIsNull
        ? null
        : cacheExpiry, rs.getTimestamp(10));
  }
}

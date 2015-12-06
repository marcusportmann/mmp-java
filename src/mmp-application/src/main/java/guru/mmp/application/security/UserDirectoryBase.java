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

package guru.mmp.application.security;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.persistence.DataAccessObject;
import guru.mmp.common.persistence.TransactionManager;
import guru.mmp.common.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//~--- JDK imports ------------------------------------------------------------

import java.security.MessageDigest;

import java.sql.*;

import java.util.Map;

import javax.naming.InitialContext;

import javax.sql.DataSource;

/**
 * The <code>UserDirectoryBase</code> class provides the base class from which all user directory
 * classes should be derived.
 */
public abstract class UserDirectoryBase
  implements IUserDirectory
{
  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(UserDirectoryBase.class);
  private String createGroupSQL;
  private DataSource dataSource;
  private String databaseCatalogSeparator;
  private String deleteGroupSQL;
  private String getGroupIdSQL;
  private String insertIDGeneratorSQL;

  /**
   * The key-value configuration parameters for the user directory.
   */
  private Map<String, String> parameters;
  private String selectIDGeneratorSQL;
  private String updateIDGeneratorSQL;

  /**
   * The unique ID for the user directory.
   */
  private long userDirectoryId;

  /**
   * Constructs a new <code>UserDirectoryBase</code>.
   *
   * @param userDirectoryId the unique ID for the user directory
   * @param parameters      the key-value configuration parameters for the user directory
   *
   * @throws SecurityException
   */
  public UserDirectoryBase(long userDirectoryId, Map<String, String> parameters)
    throws SecurityException
  {
    this.userDirectoryId = userDirectoryId;
    this.parameters = parameters;

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
      throw new SecurityException("Failed to initialise the user directory (" + userDirectoryId
          + "): Failed to retrieve the application data source"
          + " using the JNDI names (java:app/jdbc/ApplicationDataSource) and"
          + " (java:comp/env/jdbc/ApplicationDataSource)");
    }

    try
    {
      // Retrieve the database meta data
      try (Connection connection = dataSource.getConnection())
      {
        DatabaseMetaData metaData = connection.getMetaData();

        // Retrieve the schema separator for the database
        databaseCatalogSeparator = metaData.getCatalogSeparator();

        if ((databaseCatalogSeparator == null) || (databaseCatalogSeparator.length() == 0))
        {
          databaseCatalogSeparator = ".";
        }
      }

      // Determine the schema prefix
      String schemaPrefix = DataAccessObject.DEFAULT_APPLICATION_DATABASE_SCHEMA
        + databaseCatalogSeparator;

      // Build the SQL statements
      buildStatements(schemaPrefix);
    }
    catch (Throwable e)
    {
      throw new SecurityException("Failed to initialise the the user directory (" + userDirectoryId
          + "): " + e.getMessage(), e);
    }
  }

  /**
   * Returns the key-value configuration parameters for the user directory.
   *
   * @return the key-value configuration parameters for the user directory
   */
  public Map<String, String> getParameters()
  {
    return parameters;
  }

  /**
   * Returns the unique ID for the user directory.
   *
   * @return the unique ID for the user directory
   */
  public long getUserDirectoryId()
  {
    return userDirectoryId;
  }

  /**
   * Get the next unique <code>long</code> ID for the entity with the specified type.
   *
   * @param type the type of entity to retrieve the next ID for
   *
   * @return the next unique <code>long</code> ID for the entity with the specified type
   *
   * @throws SQLException
   */
  public long nextId(String type)
    throws SQLException
  {
    // Local variables
    long result;

    // Retrieve the Transaction Manager
    TransactionManager transactionManager = TransactionManager.getTransactionManager();
    javax.transaction.Transaction existingTransaction = null;

    try
    {
      if (transactionManager.isTransactionActive())
      {
        existingTransaction = transactionManager.beginNew();
      }
      else
      {
        transactionManager.begin();
      }

      try (Connection connection = dataSource.getConnection())
      {
        try (PreparedStatement updateStatement = connection.prepareStatement(updateIDGeneratorSQL))
        {
          updateStatement.setString(1, type);

          // The following statment will block if another connection is currently
          // executing a transaction that is updating the IDGENERATOR table.
          if (updateStatement.executeUpdate() == 0)
          {
            // The row could not be found so INSERT one starting at id = 1
            try (PreparedStatement insertStatement =
                connection.prepareStatement(insertIDGeneratorSQL))
            {
              insertStatement.setLong(1, 1);
              insertStatement.setString(2, type);
              insertStatement.executeUpdate();
            }
          }
        }

        try (PreparedStatement selectStatement = connection.prepareStatement(selectIDGeneratorSQL))
        {
          selectStatement.setString(1, type);

          try (ResultSet rs = selectStatement.executeQuery())
          {
            if (rs.next())
            {
              result = rs.getLong(1);
            }
            else
            {
              throw new SQLException("No IDGenerator row found for type (" + type + ")");
            }
          }
        }
      }

      transactionManager.commit();

      return result;
    }
    catch (Throwable e)
    {
      try
      {
        transactionManager.rollback();
      }
      catch (Throwable f)
      {
        logger.error("Failed to rollback the transaction while retrieving the new"
            + " ID for the entity of type (" + type + ") from the IDGENERATOR table", f);
      }

      throw new SQLException("Failed to retrieve the new ID for the entity of type (" + type
          + ") from the IDGENERATOR table", e);
    }
    finally
    {
      try
      {
        transactionManager.resume(existingTransaction);
      }
      catch (Throwable e)
      {
        logger.error("Failed to resume the original transaction while retrieving the new"
            + " ID for the entity of type (" + type + ") from the IDGENERATOR table", e);
      }
    }
  }

  /**
   * Build the SQL statements for the user directory.
   *
   * @param schemaPrefix the schema prefix to prepend to database objects for the user directory
   */
  protected void buildStatements(String schemaPrefix)
  {
    // createGroupSQL
    createGroupSQL = "INSERT INTO " + schemaPrefix + "GROUPS"
        + " (ID, USER_DIRECTORY_ID, GROUPNAME) VALUES (?, ?, ?)";

    // deleteGroupSQL
    deleteGroupSQL = "DELETE FROM " + schemaPrefix + "GROUPS G"
        + " WHERE G.USER_DIRECTORY_ID=? AND UPPER(G.GROUPNAME)=UPPER(CAST(? AS VARCHAR(100)))";

    // getGroupIdSQL
    getGroupIdSQL = "SELECT G.ID FROM " + schemaPrefix + "GROUPS G"
        + " WHERE G.USER_DIRECTORY_ID=? AND UPPER(G.GROUPNAME)=UPPER(CAST(? AS VARCHAR(100)))";

    // insertIDGeneratorSQL
    insertIDGeneratorSQL = "INSERT INTO " + schemaPrefix + "IDGENERATOR"
        + " (CURRENT, NAME) VALUES (?, ?)";

    // selectIDGeneratorSQL
    selectIDGeneratorSQL = "SELECT CURRENT FROM " + schemaPrefix + "IDGENERATOR" + " WHERE NAME=?";

    // updateIDGeneratorSQL
    updateIDGeneratorSQL = "UPDATE " + schemaPrefix + "IDGENERATOR"
        + " SET CURRENT = CURRENT + 1 WHERE NAME=?";
  }

  /**
   * Create a new group.
   * <p>
   * If a group with the specified group name already exists the ID for this existing group will be
   * returned.
   *
   * @param connection the existing database connection
   * @param groupName  the group name uniquely identifying the group
   *
   * @return the numeric ID for the group
   *
   * @throws SecurityException
   */
  protected long createGroup(Connection connection, String groupName)
    throws SecurityException
  {
    try (PreparedStatement statement = connection.prepareStatement(createGroupSQL))
    {
      long groupId = getGroupId(connection, groupName);

      if (groupId != -1)
      {
        return groupId;
      }

      groupId = nextId("Application.GroupId");

      statement.setLong(1, groupId);
      statement.setLong(2, getUserDirectoryId());
      statement.setString(3, groupName);

      if (statement.executeUpdate() != 1)
      {
        throw new SecurityException(
            "No rows were affected as a result of executing the SQL statement (" + createGroupSQL
            + ")");
      }

      return groupId;
    }
    catch (Throwable e)
    {
      throw new SecurityException("Failed to create the group (" + groupName
          + ") for the user directory (" + getUserDirectoryId() + "): " + e.getMessage(), e);
    }
  }

  /**
   * Create a SHA-256 has of the specified password.
   *
   * @param password the password to hash
   *
   * @return the SHA-256 hash of the password
   *
   * @throws SecurityException
   */
  protected String createPasswordHash(String password)
    throws SecurityException
  {
    try
    {
      MessageDigest md = MessageDigest.getInstance("SHA-256");

      md.update(password.getBytes("iso-8859-1"), 0, password.length());

      return Base64.encodeBytes(md.digest());
    }
    catch (Throwable e)
    {
      throw new SecurityException("Failed to generate a SHA-256 hash of the password (" + password
          + "): " + e.getMessage(), e);
    }
  }

  /**
   * Delete the group.
   *
   * @param connection the existing database connection to use
   * @param groupName  the group name uniquely identifying the group
   *
   * @return the numeric ID for the group or -1 if a group with the specified group name could not
   *         be found
   *
   * @throws SecurityException
   */
  protected long deleteGroup(Connection connection, String groupName)
    throws SecurityException
  {
    try (PreparedStatement statement = connection.prepareStatement(deleteGroupSQL))
    {
      long groupId = getGroupId(connection, groupName);

      if (groupId == -1)
      {
        return groupId;
      }

      statement.setLong(1, getUserDirectoryId());
      statement.setString(2, groupName);

      if (statement.executeUpdate() <= 0)
      {
        throw new SecurityException(
            "No rows were affected as a result of executing the SQL statement (" + deleteGroupSQL
            + ")");
      }

      return groupId;
    }
    catch (Throwable e)
    {
      throw new SecurityException("Failed to delete the group (" + groupName
          + ") for the user directory (" + getUserDirectoryId() + "): " + e.getMessage(), e);
    }
  }

  /**
   * Returns the data source for the user directory.
   *
   * @return the data source for the user directory
   */
  protected DataSource getDataSource()
  {
    return dataSource;
  }

  /**
   * Returns the database catalog separator.
   *
   * @return the database catalog separator
   */
  protected String getDatabaseCatalogSeparator()
  {
    return databaseCatalogSeparator;
  }

  /**
   * Returns the numeric ID for the group with the specified group name.
   *
   * @param connection the existing database connection to use
   * @param groupName  the group name uniquely identifying the group
   *
   * @return the numeric ID for the group or -1 if a group with the specified group name could not
   *         be found
   *
   * @throws SecurityException
   */
  protected long getGroupId(Connection connection, String groupName)
    throws SecurityException
  {
    try (PreparedStatement statement = connection.prepareStatement(getGroupIdSQL))
    {
      statement.setLong(1, getUserDirectoryId());
      statement.setString(2, groupName);

      try (ResultSet rs = statement.executeQuery())
      {
        if (rs.next())
        {
          return rs.getLong(1);
        }
        else
        {
          return -1;
        }
      }
    }
    catch (Throwable e)
    {
      throw new SecurityException("Failed to retrieve the numeric ID for the group (" + groupName
          + ") for the user directory (" + getUserDirectoryId() + ")", e);
    }
  }

  /**
   * Checks whether the specified value is <code>null</code> or blank.
   *
   * @param value the value to check
   *
   * @return true if the value is <code>null</code> or blank
   */
  protected boolean isNullOrEmpty(Object value)
  {
    if (value == null)
    {
      return true;
    }

    if (value instanceof String)
    {
      if (String.class.cast(value).length() == 0)
      {
        return true;
      }
    }

    return false;
  }
}

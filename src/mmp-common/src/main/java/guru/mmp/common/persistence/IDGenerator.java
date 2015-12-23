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

package guru.mmp.common.persistence;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.common.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.util.UUID;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>IDGenerator</code> class provides unique IDs for the entity types in the database.
 * It requires the IDGENERATOR table which must be created under at least one schema
 * within the database. The unique ID will be retrieved using a new transaction while
 * suspending the existing database transaction. This is done to reduce deadlocks and
 * improve performance.
 * <p/>
 * <code>
 * CREATE TABLE "IDGENERATOR"
 * (
 * "TYPE" VARCHAR(100) NOT NULL,
 * "CURRENT" BIGINT DEFAULT 0,
 * PRIMARY KEY ("TYPE")
 * )
 * </code>
 *
 * @author Marcus Portmann
 */
public class IDGenerator
{
  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(IDGenerator.class);
  private DataSource dataSource;
  private String getCurrentIdSQL;
  private String insertIdSQL;
  private String schema;
  private String updateIdSQL;

  /**
   * Constructs a new <code>IDGenerator</code>.
   *
   * @param dataSource the data source to use
   */
  @SuppressWarnings("unused")
  public IDGenerator(DataSource dataSource)
  {
    this.dataSource = dataSource;
    init();
  }

  /**
   * Constructs a new <code>IDGenerator</code>.
   *
   * @param dataSource the data source to use
   * @param schema     the name of the database schema containing the IDGENERATOR table
   */
  public IDGenerator(DataSource dataSource, String schema)
  {
    this.dataSource = dataSource;

    if (schema != null)
    {
      this.schema = schema;
    }

    init();
  }

  /**
   * Returns the next <code>UUID</code>.
   *
   * @param dataSource the data source used to generate the next UUID
   *
   * @return the next <code>UUID</code>
   */
  public static UUID nextUUID(DataSource dataSource)
  {
    // TODO: Save the results of checking if we are using a PostgreSQL database

    /*
     * First check whether this is a PostgreSQL database and we should be using a stored procedure
     * to retrieve the next UUID.
     */
    try (Connection connection = dataSource.getConnection())
    {
      DatabaseMetaData metaData = connection.getMetaData();

      if (metaData.getDatabaseProductName().equals("PostgreSQL"))
      {


      }
    }
    catch (Throwable e)
    {
      logger.error("Failed to retrieve the next UUID", e);
    }

    return UUID.randomUUID();
  }

  /**
   * Get the next unique <code>long</code> ID for the entity with the specified type.
   * <p/>
   *
   * @param type the type of entity to retrieve the next ID for
   *
   * @return the next unique <code>long</code> ID for the entity with the specified type
   */
  public long nextx(String type)
  {
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

      long id;

      try (Connection connection = dataSource.getConnection())
      {
        id = getCurrentId(connection, type);

        if (id == -1)
        {
          id = 1;
          insertId(connection, type, id);

          // TODO: Handle a duplicate row exception caused by the INSERT/UPDATE race condition.
          // This race condition occurs when there is no row for a particular type of entity
          // in the IDGENERATOR table. Assuming we have two different threads that are both
          // attempting to retrieve the next ID for this entity type. When the first thread
          // executes the SELECT FOR UPDATE call, it will not able to lock a row and will then
          // attempt to execute the INSERT. If another thread manages to execute the SELECT FOR
          // UPDATE call before the first thread completes the INSERT then one of the threads
          // will experience a duplicate row exception as they will both attempt to INSERT.
          // The easiest way to prevent this from happening is to pre-populate the IDGENERATOR
          // table with initial IDs.
        }
        else
        {
          id = id + 1;
          updateId(connection, type, id);
        }
      }

      transactionManager.commit();

      return id;
    }
    catch (Exception e)
    {
      try
      {
        transactionManager.rollback();
      }
      catch (Throwable f)
      {
        logger.error("Failed to rollback the transaction while retrieving"
            + " the new ID for the entity of type (" + type + ") from the IDGENERATOR table", f);
      }

      throw new IDGeneratorException("Failed to retrieve the new ID for the entity of type ("
          + type + ") from the IDGENERATOR table: " + e.getMessage(), e);
    }
    finally
    {
      try
      {
        if (existingTransaction != null)
        {
          transactionManager.resume(existingTransaction);
        }
      }
      catch (Throwable e)
      {
        logger.error("Failed to resume the original transaction while retrieving"
            + " the new ID for the entity of type (" + type + ") from the IDGENERATOR table", e);
      }
    }
  }

  /**
   * Build the SQL statements for the <code>IDGenerator</code>.
   *
   * @param schemaPrefix the schema prefix to prepend to database objects referenced by the
   *                     <code>IDGenerator</code>
   */
  protected void buildStatements(String schemaPrefix)
  {
    // getCurrentIdSQL
    getCurrentIdSQL = "SELECT CURRENT FROM " + schemaPrefix + "IDGENERATOR"
        + " WHERE NAME=? FOR UPDATE";

    // insertIdSQL
    insertIdSQL = "INSERT INTO " + schemaPrefix + "IDGENERATOR" + " (CURRENT, NAME) VALUES (?, ?)";

    // updateIdSQL
    updateIdSQL = "UPDATE " + schemaPrefix + "IDGENERATOR" + " SET CURRENT=? WHERE NAME=?";
  }

  private long getCurrentId(Connection connection, String type)
    throws SQLException
  {
    try (PreparedStatement statement = connection.prepareStatement(getCurrentIdSQL))
    {
      statement.setString(1, type);

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
  }

  /**
   * Initialise the <code>IDGenerator</code>.
   */
  private void init()
  {
    Connection connection = null;

    try
    {
      // Retrieve the database meta data
      String schemaSeparator = ".";
      String idQuote = "\"";

      try
      {
        connection = dataSource.getConnection();

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
      finally
      {
        DAOUtil.close(connection);
      }

      // Determine the schema prefix
      String schemaPrefix = "";

      if (!StringUtil.isNullOrEmpty(schema))
      {
        schemaPrefix = idQuote + schema + idQuote + schemaSeparator;
      }

      // Build the SQL statements for the DAO
      buildStatements(schemaPrefix);
    }
    catch (Throwable e)
    {
      throw new IDGeneratorException("Failed to initialise the IDGenerator: " + e.getMessage(), e);
    }
  }

  private void insertId(Connection connection, String type, long id)
    throws SQLException
  {
    try (PreparedStatement statement = connection.prepareStatement(insertIdSQL))
    {
      statement.setLong(1, id);
      statement.setString(2, type);

      if (statement.executeUpdate() == 0)
      {
        throw new SQLException("No rows were affected while inserting the IDGENERATOR table"
            + " row for the type (" + type + ")");
      }
    }
  }

  private void updateId(Connection connection, String type, long id)
    throws SQLException
  {
    try (PreparedStatement statement = connection.prepareStatement(updateIdSQL))
    {
      statement.setLong(1, id);
      statement.setString(2, type);

      if (statement.executeUpdate() == 0)
      {
        throw new SQLException("No rows were affected while updating the IDGENERATOR table"
            + " row for the type (" + type + ")");
      }
    }
  }
}

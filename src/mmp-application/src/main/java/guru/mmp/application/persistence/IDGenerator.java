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

package guru.mmp.application.persistence;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.common.persistence.DAOUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.*;
import java.util.UUID;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>IDGenerator</code> class provides unique IDs for the entity types in the database.
 * It requires the IDGENERATOR table which must be created under the IDGENERATOR schema
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
@Repository
public class IDGenerator
{
  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(IDGenerator.class);

  /**
   * The data source used to provide connections to the application database.
   */
  @Autowired
  @Qualifier("applicationDataSource")
  private DataSource dataSource;

  /**
   * Get the next unique <code>long</code> ID for the entity with the specified type.
   *
   * @param type the type of entity to retrieve the next ID for
   *
   * @return the next unique <code>long</code> ID for the entity with the specified type
   */
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public long next(String schema, String type)
  {
    Connection connection = null;

    try
    {
      long id;

      connection = DataSourceUtils.doGetConnection(dataSource);

      id = getCurrentId(connection, schema, type);

      if (id == -1)
      {
        id = 1;
        insertId(connection, schema, type, id);

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
        updateId(connection, schema, type, id);
      }

      return id;
    }
    catch (Exception e)
    {
      throw new IDGeneratorException(String.format(
          "Failed to retrieve the new ID for the entity of type (%s) from the IDGENERATOR table: %s",
          type, e.getMessage()), e);
    }
    finally
    {
      try
      {
        DataSourceUtils.doReleaseConnection(connection, dataSource);
      }
      catch (SQLException e)
      {
        logger.error(String.format("Failed to release the database connection when retrieving the"
            + " new ID for the entity of type (%s) from the IDGENERATOR table: %s", type,
            e.getMessage()), e);

      }
    }
  }

  /**
   * Returns the next <code>UUID</code>.
   *
   * @return the next <code>UUID</code>
   */
  public UUID nextUUID()
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
        // TODO: Retrieve the next UUID using a PostgreSQL stored procedure
      }
    }
    catch (Throwable e)
    {
      throw new IDGeneratorException("Failed to retrieve the next UUID", e);
    }

    return UUID.randomUUID();
  }

  private long getCurrentId(Connection connection, String schema, String type)
    throws SQLException
  {
    try (PreparedStatement statement = connection.prepareStatement("SELECT CURRENT FROM " + schema
        + ".IDGENERATOR WHERE NAME=? FOR UPDATE"))
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

  private void insertId(Connection connection, String schema, String type, long id)
    throws SQLException
  {
    try (PreparedStatement statement = connection.prepareStatement("INSERT INTO " + schema
        + ".IDGENERATOR (CURRENT, NAME) VALUES (?, ?)"))
    {
      statement.setLong(1, id);
      statement.setString(2, type);

      if (statement.executeUpdate() == 0)
      {
        throw new SQLException("No rows were affected while inserting the " + DAOUtil
            .MMP_DATABASE_SCHEMA + ".IDGENERATOR table row for the type (" + type + ")");
      }
    }
  }

  private void updateId(Connection connection, String schema, String type, long id)
    throws SQLException
  {
    try (PreparedStatement statement = connection.prepareStatement("UPDATE " + schema
        + ".IDGENERATOR SET CURRENT=? WHERE NAME=?"))
    {
      statement.setLong(1, id);
      statement.setString(2, type);

      if (statement.executeUpdate() == 0)
      {
        throw new SQLException("No rows were affected while updating the " + DAOUtil
            .MMP_DATABASE_SCHEMA + ".IDGENERATOR table row for the type (" + type + ")");
      }
    }
  }
}

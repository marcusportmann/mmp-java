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

package guru.mmp.application.test;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.persistence.HsqldbDataSource;
import guru.mmp.common.test.DatabaseTest;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

/**
 * The <code>HsqldbDatabaseTests</code> class provides a base class for JUnit tests that wish to
 * use an in-memory HSQLDB database.
 *
 * @author Marcus Portmann
 */
public abstract class HsqldbDatabaseTest extends DatabaseTest
{
  /**
   * Initialise an in-memory HSQLDB database and return a data source that can be used to interact
   * with the database.
   * <p/>
   * The database will be initialised using the SQL statements in the files given by the specified
   * resource path. Each file will be loaded as a resource using the Java classloader associated
   * with the current thread.
   * <p/>
   * NOTE: This data source returned by this method must be closed after use with the
   * <code>close()</code> method.
   *
   * @param name          the name of the in-memory HSQLDB database
   * @param resourcePaths the paths to the files containing the SQL statements that will be used to
   *                      initialise the in-memory HSQLDB database
   * @param logSQL        log the statements that are executed when initialising the database
   *
   * @return the data source that can be used to interact with the in-memory HSQLDB database
   *
   * @throws IOException
   * @throws SQLException
   */
  public DataSource initDatabase(String name, List<String> resourcePaths, boolean logSQL)
    throws IOException, SQLException
  {
    // Setup the data source
    HsqldbDataSource dataSource = new HsqldbDataSource();

    dataSource.setDriverClassName("org.hsqldb.jdbcDriver");
    dataSource.setUsername("sa");
    dataSource.setPassword("");
    dataSource.setUrl("jdbc:hsqldb:mem:" + name);

    /*
     * Initialise the in-memory database using the SQL statements contained in the file with the
     * specified resource path.
     */
    for (String resourcePath : resourcePaths)
    {
      try
      {
        // Load the SQL statements used to initialise the database tables
        List<String> sqlStatements = loadSQL(resourcePath);

        // Get a connection to the in-memory database
        try (Connection connection = dataSource.getConnection())
        {
          for (String sqlStatement : sqlStatements)
          {
            if (logSQL)
            {
              getLogger().info("Executing SQL statement: " + sqlStatement);
            }

            try (Statement statement = connection.createStatement())
            {
              statement.execute(sqlStatement);
            }
          }
        }
      }
      catch (SQLException e)
      {
        try (Connection connection = dataSource.getConnection();
          Statement shutdownStatement = connection.createStatement())
        {
          shutdownStatement.executeUpdate("SHUTDOWN");
        }
        catch (Throwable f)
        {
          System.err.println("[ERROR] " + f.getMessage());
          f.printStackTrace(System.err);
        }

        throw e;
      }
    }

    return dataSource;
  }

  /**
   * Initialise an in-memory HSQLDB database and return a data source that can be used to interact
   * with the database.
   * <p/>
   * The database will be initialised using the SQL statements in the file given by the specified
   * resource path. This file will be loaded as a resource using the Java classloader associated
   * with the current thread.
   * <p/>
   * NOTE: This data source returned by this method must be closed after use with the
   * <code>close()</code> method.
   *
   * @param name         the name of the in-memory HSQLDB database
   * @param resourcePath the path to the file containing the SQL statements that will be used to
   *                     initialise the in-memory HSQLDB database
   * @param logSQL       log the statements that are executed when initialising the database
   *
   * @return the data source that can be used to interact with the in-memory HSQLDB database
   *
   * @throws IOException
   * @throws SQLException
   */
  public DataSource initDatabase(String name, String resourcePath, boolean logSQL)
    throws IOException, SQLException
  {
    List<String> resourcePaths = new ArrayList<>();
    resourcePaths.add(resourcePath);

    return initDatabase(name, resourcePaths, logSQL);
  }
}

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

package guru.mmp.common.test;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.common.persistence.DAOUtil;

import org.apache.commons.dbcp2.BasicDataSource;

//~--- JDK imports ------------------------------------------------------------

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.naming.InitialContext;

/**
 * The <code>JNDITest</code> class provides the base class for all JUnit test classes that make use
 * of an in-memory database.
 * <p>
 * The in-memory database functionality is provided by the H2 database.
 * <p>
 * Please ensure you add the following dependency to your Maven pom.xml file:
 * <pre>
 * &lt;dependency&gt;
 *   &lt;groupId&gt;com.h2database&lt;/groupId&gt;
 *   &lt;artifactId&gt;h2&lt;/artifactId&gt;
 *   &lt;version&gt;1.4.190&lt;/version&gt;
 *   &lt;scope&gt;test&lt;/scope&gt;
 * &lt;/dependency&gt;
 * <pre>
 *
 * @author Marcus Portmann
 */
public abstract class DatabaseTest extends JNDITest
{
  /**
   * Initialise the in-memory database and return a data source that can be used to interact with
   * the database.
   * <p/>
   * The database will be initialised using the SQL statements in the files given by the specified
   * resource paths. Each file will be loaded as a resource using the Java class loader associated
   * with the current thread.
   * <p/>
   * NOTE: This data source returned by this method must be closed after use with the
   * <code>close()</code> method.
   *
   * @param name          the name of the in-memory database
   * @param resourcePaths the paths to the files containing the SQL statements that will be used to
   *                      initialise the in-memory database
   * @param logSQL        log the statements that are executed when initialising the database
   *
   * @return the data source that can be used to interact with the in-memory database
   */
  public static BasicDataSource initDatabase(String name, List<String> resourcePaths,
      boolean logSQL)
  {
    try
    {
      // Setup the data source
      BasicDataSource dataSource = new BasicDataSource()
      {
        @Override
        public synchronized void close()
          throws SQLException
        {
          try (Connection connection = getConnection();
            Statement statement = connection.createStatement())
          {
            statement.executeUpdate("SHUTDOWN");
          }

          super.close();
        }
      };

      dataSource.setDriverClassName("org.h2.Driver");
      dataSource.setUsername("");
      dataSource.setPassword("");
      dataSource.setUrl("jdbc:h2:mem:" + name
          + ";MODE=DB2;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE");

      /*
       * Initialise the in-memory database using the SQL statements contained in the file with the
       * specified resource path.
       */
      for (String resourcePath : resourcePaths)
      {
        try
        {
          // Load the SQL statements used to initialise the database tables
          List<String> sqlStatements = DAOUtil.loadSQL(resourcePath);

          // Get a connection to the in-memory database
          try (Connection connection = dataSource.getConnection())
          {
            for (String sqlStatement : sqlStatements)
            {
              if (logSQL)
              {
                Logger.getAnonymousLogger().info("Executing SQL statement: " + sqlStatement);
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

      InitialContext ic = null;

      try
      {
        ic = new InitialContext();

        ic.bind("java:comp/env/jdbc/" + name, dataSource);
      }
      catch (Throwable e)
      {
        throw new RuntimeException("Failed to bind the data source (" + name
            + ") under the java:/comp/env/jdbc JNDI context", e);
      }
      finally
      {
        if (ic != null)
        {
          try
          {
            ic.close();
          }
          catch (Throwable ignored) {}
        }
      }

      return dataSource;
    }
    catch (Throwable e)
    {
      throw new RuntimeException("Failed to initialise the in-memory database (" + name + ")", e);
    }
  }

  /**
   * Initialise the in-memory database and return a data source that can be used to interact with
   * the database.
   * <p/>
   * The database will be initialised using the SQL statements in the files given by the specified
   * resource paths. Each file will be loaded as a resource using the Java class loader associated
   * with the current thread.
   * <p/>
   * NOTE: This data source returned by this method must be closed after use with the
   * <code>close()</code> method.
   *
   * @param name         the name of the in-memory database
   * @param resourcePath the paths to the files containing the SQL statements that will be used to
   *                     initialise the in-memory database
   * @param logSQL       log the statements that are executed when initialising the database
   *
   * @return the data source that can be used to interact with the in-memory database
   */
  public static BasicDataSource initDatabase(String name, String resourcePath, boolean logSQL)
  {
    List<String> resourcePaths = new ArrayList<>();
    resourcePaths.add(resourcePath);

    return initDatabase(name, resourcePaths, logSQL);
  }
}

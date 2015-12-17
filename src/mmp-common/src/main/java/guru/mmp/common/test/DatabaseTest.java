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

import org.apache.commons.dbcp2.BasicDataSource;

import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>DatabaseTest</code> class provides a base class that contains functionality common to
 * all JUnit test classes that make use of an in-memory H2 database.
 *
 * @author Marcus Portmann
 */
public abstract class DatabaseTest extends JNDITest
{
  /**
   * Initialise an in-memory H2 database and return a data source that can be used to interact
   * with the database.
   * <p/>
   * The database will be initialised using the SQL statements in the files given by the specified
   * resource path. Each file will be loaded as a resource using the Java classloader associated
   * with the current thread.
   * <p/>
   * NOTE: This data source returned by this method must be closed after use with the
   * <code>close()</code> method.
   *
   * @param name                      the name of the in-memory H2 database
   * @param resourcePaths             the paths to the files containing the SQL statements that
   *                                  will be used to initialise the in-memory H2 database
   * @param bindApplicationDataSource should the data source for the database be bound under the
   *                                  java:app/jdbc/ApplicationDataSource JNDI name otherwise it
   *                                  will just be bound under the java:comp/env/jdbc/DBNAME name
   * @param logSQL                    log the statements that are executed when initialising the
   *                                  database
   *
   * @return the data source that can be used to interact with the in-memory H2 database
   *
   * @throws IOException
   * @throws SQLException
   */
  public DataSource initDatabase(String name, List<String> resourcePaths, boolean bindApplicationDataSource, boolean logSQL)
    throws IOException, SQLException
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
    dataSource.setUrl("jdbc:h2:mem:" + name + ";MODE=DB2;DB_CLOSE_DELAY=-1");

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

    try
    {
      InitialContext ic = new InitialContext();

      if (bindApplicationDataSource)
      {
        ic.bind("java:app/jdbc/ApplicationDataSource", dataSource);
      }

      ic.bind("java:comp/env/jdbc/" + name, dataSource);
    }
    catch (Throwable e)
    {
      throw new RuntimeException("Failed to bind the data source (" + name
          + ") under the java:/comp/env/jdbc JNDI context", e);
    }

    return dataSource;
  }

  /**
   * Initialise an in-memory H2 database and return a data source that can be used to interact
   * with the database.
   * <p/>
   * The database will be initialised using the SQL statements in the file given by the specified
   * resource path. This file will be loaded as a resource using the Java classloader associated
   * with the current thread.
   * <p/>
   * NOTE: This data source returned by this method must be closed after use with the
   * <code>close()</code> method.
   *
   * @param name         the name of the in-memory H2 database
   * @param resourcePath the path to the file containing the SQL statements that will be used to
   *                     initialise the in-memory H2 database
   * @param logSQL       log the statements that are executed when initialising the database
   *
   * @return the data source that can be used to interact with the in-memory H2 database
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

  /**
   * Cleanup the SQL.
   *
   * @param text the SQL to cleanup
   *
   * @return the SQL that has been cleaned up
   */
  protected String cleanSQL(String text)
  {
    if (text == null)
    {
      throw new NullPointerException("Failed to clean the null SQL string");
    }

    // Strip whitespace from the beginning and end of the text
    text = text.trim();

    // If this is an empty string then stop here
    if (text.length() == 0)
    {
      return text;
    }

    // First remove the new line characters
    int index = text.length() - 1;

    while ((index >= 0) && ((text.charAt(index) == '\r') || (text.charAt(index) == '\n')))
    {
      index--;
    }

    if (index < 0)
    {
      return "";
    }

    text = text.substring(0, index + 1);

    // Replace multiple spaces with a single space
    while (text.contains("  "))
    {
      text = text.replaceAll("  ", " ");
    }

    // Replace tabs with a single space
    text = text.replaceAll("\t", " ");

    return text;
  }

  /**
   * Load the SQL statements from the resource on the classpath.
   *
   * @param resourcePath the path to the resource on the classpath containing the SQL statements
   *
   * @return the SQL statements loaded from the resource on the classpath
   *
   * @throws IOException
   */
  protected List<String> loadSQL(String resourcePath)
    throws IOException
  {
    List<String> sqlStatements = new ArrayList<>();
    BufferedReader reader = null;

    try
    {
      reader = new BufferedReader(
          new InputStreamReader(
            Thread.currentThread().getContextClassLoader().getResourceAsStream(resourcePath)));

      StringBuilder multiLineBuffer = null;
      String line;

      while ((line = reader.readLine()) != null)
      {
        /*
         * Remove any whitespace at the beginning or end of the line, any newline characters and
         * any multiple spaces.
         */
        line = cleanSQL(line);

        if (line.length() == 0)
        {
          continue;
        }

        // Only process the line if it is not a SQL comment
        if (!line.startsWith("--"))
        {
          /*
           * If we have already built up part of the multi-line SQL statement then add spacing
           * between this and the next part of the SQL statement on the current line.
           */
          if (multiLineBuffer != null)
          {
            multiLineBuffer.append(" ");
          }

          /*
           * If the line contains a ';' then one of the following is true:
           * - The line contains one or more single-line SQL statements
           * - The line contains the end of a multi-line SQL statement
           * - The line contains both of the above
           */
          if (line.contains(";"))
          {
            StringTokenizer tokens = new StringTokenizer(line, ";");

            while (tokens.hasMoreTokens())
            {
              String token = tokens.nextToken().trim();

              // If we are currently processing a multi-line buffer
              if (multiLineBuffer != null)
              {
                multiLineBuffer.append(token);
                sqlStatements.add(multiLineBuffer.toString());
                multiLineBuffer = null;
              }
              else
              {
                if (tokens.hasMoreTokens())
                {
                  sqlStatements.add(token);
                }
                else
                {
                  if (line.endsWith(";"))
                  {
                    sqlStatements.add(token);
                  }
                  else
                  {
                    multiLineBuffer = new StringBuilder();
                    multiLineBuffer.append(token);
                  }
                }
              }
            }
          }

          /*
           * The line does not contain the end of a SQL statement which means it is either
           * the start of a new multi-line SQL statement or the continuation of an existing
           * multi-line SQL statement.
           */
          else
          {
            /*
             * If this is a new multi-line SQL statement then initialise the buffer
             * that will be used to concatenate the individual lines of the statement into
             * a single-line SQL statement.
             */
            if (multiLineBuffer == null)
            {
              multiLineBuffer = new StringBuilder();
            }

            multiLineBuffer.append(line);
          }
        }
      }

      if (multiLineBuffer != null)
      {
        getLogger().warn("Failed to process the last SQL statement from the file (" + resourcePath
            + ") since it was not terminated by a ';'");
      }

      return sqlStatements;
    }
    finally
    {
      try
      {
        if (reader != null)
        {
          reader.close();
        }
      }
      catch (Throwable ignored) {}
    }
  }
}

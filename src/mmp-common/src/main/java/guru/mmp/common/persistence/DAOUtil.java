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

package guru.mmp.common.persistence;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.common.util.StringUtil;

//~--- JDK imports ------------------------------------------------------------

import java.io.*;

import java.sql.*;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

//import javax.persistence.EntityManager;
//import javax.persistence.EntityTransaction;

import javax.sql.DataSource;

/**
 * The <code>DAOUtil</code> class provides utility functions used by the Data Access Objects (DAOs).
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public class DAOUtil
{
  /**
   * The mmp-java Java and JEE development framework database schema.
   */
  public static final String MMP_DATABASE_SCHEMA = "MMP";

  /**
   * Private default constructor to enforce utility pattern.
   */
  private DAOUtil() {}

  /**
   * Close the connection.
   *
   * @param connection the connection to close
   */
  public static void close(Connection connection)
  {
    if (connection != null)
    {
      try
      {
        connection.close();
      }
      catch (SQLException e)
      {
        // Do nothing
      }
    }
  }

// TODO: DELETE THIS METHOD -- MARCUS
//  /**
//   * Close the entity manager.
//   *
//   * @param entityManager the entity manager to close
//   */
//  public static void close(EntityManager entityManager)
//  {
//    if (entityManager != null)
//    {
//      try
//      {
//        entityManager.close();
//      }
//      catch (Throwable e)
//      {
//        throw new RuntimeException("Failed to close the entity manager", e);
//      }
//    }
//  }

  /**
   * Close the result set.
   *
   * @param rs the result set to close
   */
  public static void close(ResultSet rs)
  {
    if (rs != null)
    {
      try
      {
        rs.close();
      }
      catch (SQLException e)
      {
        // Do nothing
      }
    }
  }

  /**
   * Close the statement.
   *
   * @param statement the statement to close
   */
  public static void close(Statement statement)
  {
    if (statement != null)
    {
      try
      {
        statement.close();
      }
      catch (SQLException e)
      {
        // Do nothing
      }
    }
  }

// TODO: DELETE THIS METHOD -- MARCUS
//  /**
//   * Close the entity manager and commit or rollback the associated transaction if one is active.
//   *
//   * @param entityManager the entity manager to close
//   */
//  public static void closeAndCommitOrRollback(EntityManager entityManager)
//  {
//    EntityTransaction entityTransaction = entityManager.getTransaction();
//
//    if (entityTransaction.isActive())
//    {
//      if (entityTransaction.getRollbackOnly())
//      {
//        try
//        {
//          entityTransaction.rollback();
//        }
//        catch (Throwable e)
//        {
//          throw new RuntimeException(
//              "Failed to rollback the entity manager transaction and close the entity manager", e);
//        }
//      }
//      else
//      {
//        try
//        {
//          entityTransaction.commit();
//        }
//        catch (Throwable e)
//        {
//          throw new RuntimeException(
//              "Failed to commit the entity manager transaction and close the entity manager", e);
//        }
//      }
//    }
//
//    try
//    {
//      entityManager.close();
//    }
//    catch (Throwable e)
//    {
//      throw new RuntimeException("Failed to close the entity manager", e);
//    }
//  }

  /**
   * Execute the SQL statement using the database connection.
   *
   * @param connection the database connection to use
   * @param sql        the SQL statement to execute
   *
   * @return the row count
   *
   * @throws SQLException
   */
  public static int executeStatement(Connection connection, String sql)
    throws SQLException
  {
    Statement statement = null;

    try
    {
      statement = connection.createStatement();

      return statement.executeUpdate(sql);
    }
    catch (Throwable e)
    {
      throw new SQLException("Failed to execute the SQL statement: " + sql, e);
    }
    finally
    {
      DAOUtil.close(statement);
    }
  }

  /**
   * Execute the SQL statements in the file with the specified resource path using the database
   * connection.
   *
   * @param connection   the database connection to use
   * @param resourcePath the resource path to the file containing the SQL statements
   *
   * @return the number of SQL statements successfully executed
   *
   * @throws SQLException
   */
  public static int executeStatements(Connection connection, String resourcePath)
    throws SQLException
  {
    int numberOfStatementsExecuted = 0;

    try
    {
      List<String> sqlStatements = loadSQL(resourcePath);

      for (String sqlStatement : sqlStatements)
      {
        System.out.println("EXECUTING: " + sqlStatement);

        executeStatement(connection, sqlStatement);
        numberOfStatementsExecuted++;
      }

      return numberOfStatementsExecuted;
    }
    catch (Throwable e)
    {
      throw new SQLException("Failed to execute the SQL statements in the resource file ("
          + resourcePath + ")", e);
    }
  }

  /**
   * Retrieve the schema separator for the database associated with the specified data source.
   *
   * @param dataSource
   *
   * @return the schema separator for the database associated with the specified data source
   *
   * @throws SQLException
   */
  public static String getSchemaSeparator(DataSource dataSource)
    throws SQLException
  {
    try (Connection connection = dataSource.getConnection())
    {
      DatabaseMetaData metaData = connection.getMetaData();

      // Retrieve the schema separator for the database
      String schemaSeparator = metaData.getCatalogSeparator();

      if ((schemaSeparator == null) || (schemaSeparator.length() == 0))
      {
        schemaSeparator = ".";
      }

      return schemaSeparator;
    }
  }

  /**
   * Load the SQL statements from the file with the specified resource path.
   *
   * @param resourcePath the resource path
   *
   * @return the SQL statements loaded from the file
   *
   * @throws IOException
   */
  public static List<String> loadSQL(String resourcePath)
    throws IOException
  {
    List<String> sqlStatements = new ArrayList<>();
    BufferedReader reader = null;

    try
    {
      InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(
          resourcePath);

      if (inputStream == null)
      {
        throw new IOException("Failed to load the SQL statements from the file (" + resourcePath
            + "): The file could not be found");
      }

      reader = new BufferedReader(new InputStreamReader(inputStream));

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
          // If the line contains a SQL comment then only process the portion before the comment
          if (line.contains("--"))
          {
            line = line.substring(0, line.indexOf("--"));
          }

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
        throw new IOException("Failed to process the last SQL statement from the file ("
            + resourcePath + ") since " + "it was not terminated by a ';'");
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

  /**
   * Read the blob associated with the column with the specified index from the
   * specified result set.
   *
   * @param rs    the result set
   * @param index the index of the column containing the blob
   *
   * @return the binary data for the BLOB
   *
   * @throws SQLException if a database error occurs
   */
  public static byte[] readBlob(ResultSet rs, int index)
    throws SQLException
  {
    ByteArrayOutputStream bos = null;
    BufferedInputStream in = null;

    try
    {
      bos = new ByteArrayOutputStream();
      in = new BufferedInputStream(rs.getBinaryStream(index));

      int noBytes;
      byte[] tmpBuffer = new byte[1024];

      while ((noBytes = in.read(tmpBuffer)) != -1)
      {
        bos.write(tmpBuffer, 0, noBytes);
      }

      return bos.toByteArray();
    }
    catch (IOException e)
    {
      throw new SQLException("An IO error occurred while reading the BLOB from the database: "
          + e.getMessage());
    }
    finally
    {
      if (bos != null)
      {
        try
        {
          bos.close();
        }
        catch (IOException e)
        {
          // Do nothing
        }
      }

      if (in != null)
      {
        try
        {
          in.close();
        }
        catch (IOException e)
        {
          // Do nothing
        }
      }
    }
  }

  /**
   * Rollback the current transaction on the specified connection.
   *
   * @param connection the connection to rollback the transaction on
   */
  public static void rollback(Connection connection)
  {
    if (connection != null)
    {
      try
      {
        connection.rollback();
      }
      catch (SQLException e)
      {
        // Do nothing
      }
    }
  }

  /**
   * Checks whether the schema with the specified name exists for the database referenced
   * by the connection.
   *
   * @param connection the database connection to use
   * @param catalog    the catalog name or <code>null</code> if a catalog should not be used
   * @param schema     the schema name
   *
   * @return true if the schema exists or false otherwise
   *
   * @throws SQLException
   */
  @SuppressWarnings("resource")
  public static boolean schemaExists(Connection connection, String catalog, String schema)
    throws SQLException
  {
    ResultSet rs = null;

    if (schema == null)
    {
      throw new SQLException("Failed to check whether the schema (null) exists");
    }

    try
    {
      DatabaseMetaData metaData = connection.getMetaData();

      rs = metaData.getSchemas();

      while (rs.next())
      {
        String tmpCatalog = StringUtil.notNull(rs.getString("TABLE_CATALOG"));
        String tmpSchema = StringUtil.notNull(rs.getString("TABLE_SCHEM"));

        if ((catalog == null) || ((catalog != null) && catalog.equalsIgnoreCase(tmpCatalog)))
        {
          if (tmpSchema.equalsIgnoreCase(schema))
          {
            return true;
          }
        }
      }

      return false;
    }
    finally
    {
      DAOUtil.close(rs);
    }
  }

  /**
   * Checks whether the schema with the specified name exists for the database referenced
   * by the data source.
   *
   * @param dataSource the data source to use
   * @param catalog    the catalog name or <code>null</code> if a catalog should not be used
   * @param schema     the schema name
   *
   * @return true if the schema exists or false otherwise
   *
   * @throws SQLException
   */
  @SuppressWarnings("resource")
  public static boolean schemaExists(DataSource dataSource, String catalog, String schema)
    throws SQLException
  {
    Connection connection = null;
    ResultSet rs = null;

    if (schema == null)
    {
      throw new SQLException("Failed to check whether the schema (null) exists");
    }

    try
    {
      connection = dataSource.getConnection();

      DatabaseMetaData metaData = connection.getMetaData();

      rs = metaData.getSchemas();

      while (rs.next())
      {
        String tmpCatalog = StringUtil.notNull(rs.getString("TABLE_CATALOG"));
        String tmpSchema = StringUtil.notNull(rs.getString("TABLE_SCHEM"));

        if ((catalog == null) || ((catalog != null) && catalog.equalsIgnoreCase(tmpCatalog)))
        {
          if (tmpSchema.equalsIgnoreCase(schema))
          {
            return true;
          }
        }
      }

      return false;
    }
    finally
    {
      DAOUtil.close(rs);
      DAOUtil.close(connection);
    }
  }

  /**
   * Close and release all connections that are currently stored in the connection pool associated
   * with the data source.
   * <p/>
   * The HSQLDB database associated with the data source will also be shutdown.
   *
   * @param connection the HSQLDB database connection
   *
   * @throws SQLException
   */
  public static void shutdownHsqlDatabase(Connection connection)
    throws SQLException
  {
    Statement statement = null;

    try
    {
      statement = connection.createStatement();

      statement.executeUpdate("SHUTDOWN");
    }
    finally
    {
      DAOUtil.close(statement);
    }
  }

  /**
   * Checks whether the table with the specified name exists under the catalog and schema for the
   * database referenced by the data source.
   *
   * @param connection the database connection to use
   * @param catalog    the catalog name or <code>null</code> if a catalog should not be used
   * @param schema     the schema name or <code>null</code> if a schema should not be used
   * @param table      the name of the table
   *
   * @return true if the table exists or false otherwise
   *
   * @throws SQLException
   */
  @SuppressWarnings("resource")
  public static boolean tableExists(Connection connection, String catalog, String schema,
      String table)
    throws SQLException
  {
    ResultSet rs = null;

    if (table == null)
    {
      throw new SQLException("Failed to check whether the table (null) exists");
    }

    try
    {
      // First check if the schema exists
      if ((schema != null) && (!schemaExists(connection, catalog, schema)))
      {
        return false;
      }

      DatabaseMetaData metaData = connection.getMetaData();

      rs = metaData.getTables(catalog, schema, table, new String[] { "TABLE" });

      while (rs.next())
      {
        String tmpTable = StringUtil.notNull(rs.getString("TABLE_NAME"));

        if (table.equals(tmpTable))
        {
          return true;
        }
      }

      return false;
    }
    finally
    {
      DAOUtil.close(rs);
    }
  }

  /**
   * Checks whether the table with the specified name exists under the catalog and schema for the
   * database referenced by the data source.
   *
   * @param dataSource the data source to use
   * @param catalog    the catalog name or <code>null</code> if a catalog should not be used
   * @param schema     the schema name or <code>null</code> if a schema should not be used
   * @param table      the name of the table
   *
   * @return true if the table exists or false otherwise
   *
   * @throws SQLException
   */
  @SuppressWarnings("resource")
  public static boolean tableExists(DataSource dataSource, String catalog, String schema,
      String table)
    throws SQLException
  {
    Connection connection = null;
    ResultSet rs = null;

    if (table == null)
    {
      throw new SQLException("Failed to check whether the table (null) exists");
    }

    try
    {
      connection = dataSource.getConnection();

      // First check if the schema exists
      if ((schema != null) && (!schemaExists(connection, catalog, schema)))
      {
        return false;
      }

      DatabaseMetaData metaData = connection.getMetaData();

      rs = metaData.getTables(catalog, schema, table, new String[] { "TABLE" });

      while (rs.next())
      {
        String tmpTable = StringUtil.notNull(rs.getString("TABLE_NAME"));

        if (table.equals(tmpTable))
        {
          return true;
        }
      }

      return false;
    }
    finally
    {
      DAOUtil.close(rs);
      DAOUtil.close(connection);
    }
  }

  private static String cleanSQL(String text)
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
}

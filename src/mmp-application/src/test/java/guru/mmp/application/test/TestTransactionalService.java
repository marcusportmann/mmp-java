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

import guru.mmp.application.persistence.DAOException;
import guru.mmp.application.persistence.DataAccessObject;
import guru.mmp.application.persistence.NewTransaction;
import guru.mmp.application.persistence.Transactional;
import guru.mmp.application.security.SecurityException;

//~--- JDK imports ------------------------------------------------------------

import java.sql.*;

import javax.annotation.PostConstruct;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;

import javax.naming.InitialContext;

import javax.sql.DataSource;
import javax.sql.XAConnection;
import javax.sql.XADataSource;

/**
 * The <code>TestTransactionalService</code> class provides the Test Transactional Service
 * implementation.
 *
 * @author Marcus Portmann
 */
@ApplicationScoped
@Default
public class TestTransactionalService
  implements ITestTransactionalService
{
  private String createTestDataSQL;
  private DataSource dataSource;
  private String getTestDataSQL;

  /**
   * Create the test data.
   *
   * @param testData the test data
   *
   * @throws TestTransactionalServiceException
   */
  @Transactional
  public void createTestData(TestData testData)
    throws TestTransactionalServiceException
  {
    try (Connection connection = dataSource.getConnection();
         PreparedStatement statement = connection.prepareStatement(createTestDataSQL))
    {
      statement.setString(1, testData.getId());
      statement.setString(2, testData.getName());
      statement.setString(3, testData.getValue());

      if (statement.executeUpdate() != 1)
      {
        throw new RuntimeException(
            "No rows were affected as a result of executing the SQL statement ("
            + createTestDataSQL + ")");
      }
    }
    catch (Throwable e)
    {
      throw new TestTransactionalServiceException("Failed to create the test data", e);
    }
  }

  /**
   * Create the test data in a new transaction.
   *
   * @param testData the test data
   *
   * @throws TestTransactionalServiceException
   */
  @Transactional
  @NewTransaction
  public void createTestDataInNewTransaction(TestData testData)
    throws TestTransactionalServiceException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(createTestDataSQL))
    {
      statement.setString(1, testData.getId());
      statement.setString(2, testData.getName());
      statement.setString(3, testData.getValue());

      if (statement.executeUpdate() != 1)
      {
        throw new RuntimeException(
            "No rows were affected as a result of executing the SQL statement ("
            + createTestDataSQL + ")");
      }
    }
    catch (Throwable e)
    {
      throw new TestTransactionalServiceException(
          "Failed to create the test data in a new transaction", e);
    }
  }

  /**
   * Retrieve the test data with the specified ID.
   *
   * @param id the ID
   *
   * @return the test data or <code>null</code> if the test data cannot be found
   *
   * @throws TestTransactionalServiceException
   */
  public TestData getTestData(String id)
    throws TestTransactionalServiceException
  {
    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(getTestDataSQL))
    {
      statement.setString(1, id);

      try (ResultSet rs = statement.executeQuery())
      {
        if (rs.next())
        {
          return buildTestDataFromResultSet(rs);
        }
        else
        {
          return null;
        }
      }
    }
    catch (Throwable e)
    {
      throw new TestTransactionalServiceException(
          "Failed to create the test data in a new transaction", e);
    }
  }

  /**
   * Initialise the Test Transactional Service instance.
   */
  @PostConstruct
  public void init()
  {
    try
    {
      Thread currentThread = Thread.currentThread();

      InitialContext ic = new InitialContext();

      dataSource = InitialContext.doLookup("java:app/jdbc/ApplicationDataSource");
    }
    catch (Throwable ignored)
    {
      int xxx = 0;
      xxx++;
    }

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

      try (Connection connection = dataSource.getConnection())
      {
        DatabaseMetaData metaData = connection.getMetaData();

        // Retrieve the schema separator for the database
        schemaSeparator = metaData.getCatalogSeparator();

        if ((schemaSeparator == null) || (schemaSeparator.length() == 0))
        {
          schemaSeparator = ".";
        }
      }

      // Determine the schema prefix
      String schemaPrefix = DataAccessObject.DEFAULT_APPLICATION_DATABASE_SCHEMA + schemaSeparator;

      // Build the SQL statements for the DAO
      buildStatements(schemaPrefix);
    }
    catch (Throwable e)
    {
      throw new SecurityException("Failed to initialise the Security Service: " + e.getMessage(),
          e);
    }
  }

  /**
   * Generate the SQL statements.
   *
   * @param schemaPrefix the schema prefix to prepend to database objects
   *
   * @throws SQLException
   */
  private void buildStatements(String schemaPrefix)
    throws SQLException
  {
    // createTestDataSQL
    createTestDataSQL = "INSERT INTO " + schemaPrefix + "TEST_DATA"
        + " (ID, NAME, VALUE) VALUES (?, ?, ?)";

    // getTestDataSQL
    getTestDataSQL = "SELECT ID, NAME, VALUE FROM " + schemaPrefix + "TEST_DATA WHERE ID=?";
  }

  private TestData buildTestDataFromResultSet(ResultSet rs)
    throws SQLException
  {
    return new TestData(rs.getString(1), rs.getString(2), rs.getString(3));
  }
}

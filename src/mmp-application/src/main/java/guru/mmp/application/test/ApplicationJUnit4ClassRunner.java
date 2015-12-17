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

import guru.mmp.application.cdi.CDIUtil;
import guru.mmp.common.persistence.DAOUtil;

import org.apache.commons.dbcp2.BasicDataSource;

import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.TestClass;

//~--- JDK imports ------------------------------------------------------------

import java.lang.reflect.Method;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.List;

import javax.enterprise.inject.Instance;

import javax.enterprise.inject.spi.BeanManager;
import javax.naming.Context;
import javax.naming.InitialContext;

/**
 * The <code>ApplicationJUnit4ClassRunner</code> class implements the JUnit runner that provides
 * support for JUnit test classes that test the capabilities provided by the the <b>mmp-java (Open
 * Source Java and JEE Development Framework)</b>.
 * <p>
 * This includes support for JEE 6 Contexts and Dependency Injection (CDI) using Weld.
 *
 * @author Marcus Portmann
 */
public class ApplicationJUnit4ClassRunner extends BlockJUnit4ClassRunner
{
  /**
   * The paths to the resources on the classpath that contain the SQL statements used to initialise
   * the in-memory application database.
   */
  public static final String[] APPLICATION_SQL_RESOURCES = {
    "guru/mmp/application/persistence/ApplicationH2.sql" };
  private BasicDataSource dataSource;
  private final Class<?> testClass;
  private Object weldContainerObject;
  private Object weldObject;
  private BeanManager beanManager;

  /**
   * Constructs a new <code>ApplicationJUnit4ClassRunner</code>.
   *
   * @param testClass the JUnit test class to run
   *
   * @throws InitializationError
   */
  public ApplicationJUnit4ClassRunner(Class<?> testClass)
    throws InitializationError
  {
    super(testClass);

    this.testClass = testClass;

    InitialContext ic = null;

    try
    {
      // Initialise Weld
      try
      {
        Class<?> weldClass = Thread.currentThread().getContextClassLoader().loadClass(
            "org.jboss.weld.environment.se.Weld");

        weldObject = weldClass.newInstance();

        Method initializeMethod = weldObject.getClass().getMethod("initialize");

        weldContainerObject = initializeMethod.invoke(weldObject);

        Method getBeanManagerMethod = weldContainerObject.getClass().getMethod("getBeanManager");

        beanManager = (BeanManager)getBeanManagerMethod.invoke(weldContainerObject);
      }
      catch (Throwable e)
      {
        throw new RuntimeException("Failed to initialise Weld", e);
      }

      // Initialise the JNDI initial context
      try
      {
        System.setProperty(Context.INITIAL_CONTEXT_FACTORY,
            "org.apache.naming.java.javaURLContextFactory");
        System.setProperty(Context.URL_PKG_PREFIXES, "org.apache.naming");

        ic = new InitialContext();

        ic.createSubcontext("java:");
        ic.createSubcontext("java:app");
        ic.createSubcontext("java:app/env");
        ic.createSubcontext("java:app/jdbc");

        ic.createSubcontext("java:comp");
        ic.createSubcontext("java:comp/env");
        ic.createSubcontext("java:comp/env/jdbc");
      }
      catch (Throwable e)
      {
        throw new RuntimeException("Failed to initialise the JNDI initial context", e);
      }

      // Initialise the in-memory database that will be used when executing a test
      try
      {
        dataSource = initApplicationDatabase(false);

        // Initialise the JNDI
        ic = new InitialContext();

        ic.bind("java:app/env/RegistryPathPrefix", "/ApplicationTest");
        ic.bind("java:app/jdbc/ApplicationDataSource", dataSource);
      }
      catch (Throwable e)
      {
        throw new RuntimeException("Failed to initialise the in-memory application database", e);
      }
    }
    catch (Throwable e)
    {
      throw new RuntimeException("Failed to initialise the ApplicationJUnit4ClassRunner", e);
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
  }

  /**
   * Run the tests for this runner.
   *
   * @param notifier the run notifier that will be notified of events while tests are being run
   */
  @Override
  public void run(RunNotifier notifier)
  {
    super.run(notifier);

    try
    {
      dataSource.close();
    }
    catch (Throwable e)
    {
      throw new RuntimeException("Failed to shutdown the in-memory application database", e);
    }
  }

  @Override
  protected Object createTest()
    throws Exception
  {
    Object testObject = super.createTest();

    CDIUtil

    beanManager

    return testObject;
  }

//  /**
//   * Create the test class.
//   *
//   * @param testClass the test class to create
//   *
//   * @return the test class
//   */
//  @Override
//  protected TestClass createTestClass(Class<?> testClass)
//  {
//    try
//    {
//      Instance<Object> instance = (Instance<Object>) instanceMethod.invoke(weldContainerObject);
//
//      return instance.select(testClass).get();
//
//    }
//    catch (Throwable e)
//    {
//      throw new RuntimeException("Failed to create the test class (" + testClass.getName() + ")");
//    }
//
//    return super.createTestClass(testClass);
//  }

  /**
   * Initialise the in-memory application database and return a data source that can be used to
   * interact with the database.
   * <p/>
   * NOTE: This data source returned by this method must be closed after use with the
   * <code>close()</code> method.
   *
   * @param logSQL log the statements that are executed when initialising the database
   *
   * @return the data source that can be used to interact with the in-memory database
   */
  protected BasicDataSource initApplicationDatabase(boolean logSQL)
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
      dataSource.setUrl("jdbc:h2:mem:Application;MODE=DB2;DB_CLOSE_DELAY=-1");

      /*
       * Initialise the in-memory database using the SQL statements contained in the file with the
       * specified resource path.
       */
      for (String resourcePath : APPLICATION_SQL_RESOURCES)
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
                java.util.logging.Logger.getAnonymousLogger().info("Executing SQL statement: "
                    + sqlStatement);
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
            java.util.logging.Logger.getAnonymousLogger().severe(
                "Failed to shutdown the in-memory application database: " + e.getMessage());
          }

          throw e;
        }
      }

      return dataSource;
    }
    catch (Throwable e)
    {
      throw new RuntimeException("Failed to initialise the in-memory application database", e);
    }
  }
}

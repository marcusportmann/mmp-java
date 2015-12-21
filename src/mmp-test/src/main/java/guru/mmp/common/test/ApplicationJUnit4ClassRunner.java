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

import guru.mmp.common.cdi.CDIUtil;
import guru.mmp.common.persistence.DAOUtil;
import net.sf.cglib.proxy.Enhancer;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

import javax.enterprise.inject.spi.BeanManager;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import javax.sql.XADataSource;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>ApplicationJUnit4ClassRunner</code> class implements the JUnit runner that provides
 * support for JUnit test classes that test the capabilities provided by the the <b>mmp-java (Open
 * Source Java and JEE Development Framework)</b>.
 * <p>
 * This includes support for JEE 6 Contexts and Dependency Injection (CDI) using Weld.
 *
 * @author Marcus Portmann
 */
public class ApplicationJUnit4ClassRunner
  extends BlockJUnit4ClassRunner
{
  /**
   * The paths to the resources on the classpath that contain the SQL statements used to initialise
   * the in-memory application database.
   */
  public static final String[] APPLICATION_SQL_RESOURCES = {
    "guru/mmp/application/persistence/ApplicationH2.sql" };

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

    try
    {
      System.setProperty(Context.INITIAL_CONTEXT_FACTORY,
          "org.apache.naming.java.javaURLContextFactory");
      System.setProperty(Context.URL_PKG_PREFIXES, "org.apache.naming");

      Class<?> contextBindingsClass = Thread.currentThread().getContextClassLoader().loadClass(
          "org.apache.naming.ContextBindings");

      Method isThreadBoundMethod = contextBindingsClass.getMethod("isThreadBound");

      Boolean isThreadBound = (Boolean) isThreadBoundMethod.invoke(null);

      if (!isThreadBound)
      {
        // Initialise the initial context
        InitialContext ic = new InitialContext();

        // ic.createSubcontext("");
        ic.createSubcontext("app");
        ic.createSubcontext("app/env");
        ic.createSubcontext("app/jdbc");

        ic.createSubcontext("comp");
        ic.createSubcontext("comp/env");
        ic.createSubcontext("comp/env/jdbc");

        ic.createSubcontext("jboss");

        ic.bind("app/env/RegistryPathPrefix", "/ApplicationTest");

        // Initialise the JTA user transaction and transaction manager
        Class<?> transactionManagerClass = Thread.currentThread().getContextClassLoader().loadClass(
            "com.atomikos.icatch.jta.UserTransactionManager");

        Enhancer transactionManagerEnhancer = new Enhancer();
        transactionManagerEnhancer.setSuperclass(transactionManagerClass);
        transactionManagerEnhancer.setCallback(new TransactionManagerTransactionTracker());

        TransactionManager transactionManager =
          (TransactionManager) transactionManagerEnhancer.create();

        ic.bind("comp/TransactionManager", transactionManager);
        ic.bind("jboss/TransactionManager", transactionManager);

        Class<?> userTransactionClass = Thread.currentThread().getContextClassLoader().loadClass(
            "com.atomikos.icatch.jta.UserTransactionImp");

        Enhancer userTransactionEnhancer = new Enhancer();
        userTransactionEnhancer.setSuperclass(userTransactionClass);
        userTransactionEnhancer.setCallback(new UserTransactionTracker());

        UserTransaction userTransaction = (UserTransaction) userTransactionEnhancer.create();

        Method setTransactionTimeoutMethod =
          userTransactionClass.getMethod("setTransactionTimeout", Integer.TYPE);

        setTransactionTimeoutMethod.invoke(userTransaction, 300);

        ic.bind("comp/UserTransaction", userTransaction);
        ic.bind("jboss/UserTransaction", userTransaction);

        // Initialise the Weld bean manager
        Class<?> weldClass = Thread.currentThread().getContextClassLoader().loadClass(
            "org.jboss.weld.environment.se.Weld");

        Method initializeMethod = weldClass.getMethod("initialize");

        Object weldObject = weldClass.newInstance();

        Object weldContainerObject = initializeMethod.invoke(weldObject);

        Method getBeanManagerMethod = weldContainerObject.getClass().getMethod("getBeanManager");

        BeanManager beanManager = (BeanManager) getBeanManagerMethod.invoke(weldContainerObject);

        ic.bind("comp/BeanManager", beanManager);

        // Initialise the application data source
        DataSource dataSource = initApplicationDatabase(false);

        ic.bind("app/jdbc/ApplicationDataSource", dataSource);

        // Bind the initial context on the current thread
        Method bindContextMethod = contextBindingsClass.getMethod("bindContext", Object.class,
          Context.class);

        bindContextMethod.invoke(null, Thread.currentThread().getName(), ic);

        Method bindThreadMethod = contextBindingsClass.getMethod("bindThread", Object.class,
          Object.class);

        bindThreadMethod.invoke(null, Thread.currentThread().getName(), null);
      }
    }
    catch (Throwable e)
    {
      throw new RuntimeException("Failed to initialise the ApplicationJUnit4ClassRunner", e);
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
  }

  @Override
  protected Object createTest()
    throws Exception
  {
    Object testObject = super.createTest();

    try
    {
      CDIUtil.inject(testObject);
    }
    catch (Throwable e)
    {
      throw new RuntimeException("Failed to inject the test object of type ("
          + testObject.getClass().getName() + ")", e);
    }

    return testObject;
  }

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
  protected DataSource initApplicationDatabase(boolean logSQL)
  {
    try
    {
      Thread.currentThread().getContextClassLoader().loadClass("org.h2.Driver");

      Class<?> jdbcDataSourceClass =
        Thread.currentThread().getContextClassLoader().loadClass("org.h2.jdbcx.JdbcDataSource");

      Method setURLMethod = jdbcDataSourceClass.getMethod("setURL", String.class);

      final DataSource jdbcDataSource = (DataSource) jdbcDataSourceClass.newInstance();

      setURLMethod.invoke(jdbcDataSource,
          "jdbc:h2:mem:" + Thread.currentThread().getName()
          + ";MODE=DB2;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE");

      Runtime.getRuntime().addShutdownHook(new Thread()
      {
        @Override
        public void run()
        {
          try
          {
            try (Connection connection = jdbcDataSource.getConnection();
              Statement statement = connection.createStatement())
            {
              statement.executeUpdate("SHUTDOWN");
            }
          }
          catch (Throwable e)
          {
            throw new RuntimeException("Failed to shutdown the in-memory application database", e);
          }
        }
      });

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
          try (Connection connection = jdbcDataSource.getConnection())
          {
            for (String sqlStatement : sqlStatements)
            {
              if (logSQL)
              {
                Logger.getAnonymousLogger().info("Executing SQL statement: "
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
          try (Connection connection = jdbcDataSource.getConnection();
            Statement shutdownStatement = connection.createStatement())
          {
            shutdownStatement.executeUpdate("SHUTDOWN");
          }
          catch (Throwable f)
          {
            Logger.getAnonymousLogger().severe(
                "Failed to shutdown the in-memory application database: " + e.getMessage());
          }

          throw e;
        }
      }

      Class<?> atomikosDataSourceBeanClass =
        Thread.currentThread().getContextClassLoader().loadClass(
          "com.atomikos.jdbc.AtomikosDataSourceBean");

      Object atomikosDataSourceBean = atomikosDataSourceBeanClass.newInstance();

      Method setUniqueResourceNameMethod =
        atomikosDataSourceBeanClass.getMethod("setUniqueResourceName", String.class);

      setUniqueResourceNameMethod.invoke(atomikosDataSourceBean,
          Thread.currentThread().getName() + "-ApplicationDataSource");

      Method setXaDataSourceMethod = atomikosDataSourceBeanClass.getMethod("setXaDataSource",
        XADataSource.class);

      setXaDataSourceMethod.invoke(atomikosDataSourceBean, (XADataSource) jdbcDataSource);

      Method setMinPoolSizeMethod = atomikosDataSourceBeanClass.getMethod("setMinPoolSize",
        Integer.TYPE);
      setMinPoolSizeMethod.invoke(atomikosDataSourceBean, 5);

      Method setMaxPoolSizeMethod = atomikosDataSourceBeanClass.getMethod("setMaxPoolSize",
        Integer.TYPE);
      setMaxPoolSizeMethod.invoke(atomikosDataSourceBean, 10);

      return ((DataSource) atomikosDataSourceBean);
    }
    catch (Throwable e)
    {
      throw new RuntimeException("Failed to initialise the in-memory application database", e);
    }
  }

  /**
   * Run the child test for this runner.
   *
   * @param method   the test method being run
   * @param notifier the run notifier that will be notified of events while tests are being run
   */
  @Override
  protected void runChild(FrameworkMethod method, RunNotifier notifier)
  {
    super.runChild(method, notifier);

    Map<Transaction, StackTraceElement[]> activeTransactionStackTraces =
      TransactionManagerTransactionTracker.getActiveTransactionStackTraces();

    // Check for unexpected active transactions managed by the Transaction Manager implementation
    for (Transaction transaction : activeTransactionStackTraces.keySet())
    {
      StackTraceElement[] stackTrace = activeTransactionStackTraces.get(transaction);

      for (int i = 0; i < stackTrace.length; i++)
      {
        if (stackTrace[i].getMethodName().equals("begin") && (stackTrace[i].getLineNumber() != -1))
        {
          Logger.getAnonymousLogger().log(Level.WARNING,
              "Failed to successfully execute the test (" + method.getName()
              + "): Found an unexpected active transaction (" + transaction.toString()
              + ") that was started by the method (" + stackTrace[i + 1].getMethodName()
              + ") on the class (" + stackTrace[i + 1].getClassName() + ") on line ("
              + stackTrace[i + 1].getLineNumber() + ")");

          throw new RuntimeException("Failed to successfully execute the test (" + method.getName()
              + "): Found an unexpected active transaction (" + transaction.toString()
              + ") that was started by the method (" + stackTrace[i + 1].getMethodName()
              + ") on the class (" + stackTrace[i + 1].getClassName() + ") on line ("
              + stackTrace[i + 1].getLineNumber() + ")");
        }
      }
    }

    activeTransactionStackTraces = UserTransactionTracker.getActiveTransactionStackTraces();

    // Check for unexpected active transactions managed by the User Transaction implementation
    for (Transaction transaction : activeTransactionStackTraces.keySet())
    {
      StackTraceElement[] stackTrace = activeTransactionStackTraces.get(transaction);

      for (int i = 0; i < stackTrace.length; i++)
      {
        if (stackTrace[i].getMethodName().equals("begin") && (stackTrace[i].getLineNumber() != -1))
        {
          Logger.getAnonymousLogger().log(Level.WARNING,
              "Failed to successfully execute the test (" + method.getName()
              + "): Found an unexpected active transaction (" + transaction.toString()
              + ") that was started by the method (" + stackTrace[i + 1].getMethodName()
              + ") on the class (" + stackTrace[i + 1].getClassName() + ") on line ("
              + stackTrace[i + 1].getLineNumber() + ")");

          throw new RuntimeException("Failed to successfully execute the test (" + method.getName()
              + "): Found an unexpected active transaction (" + transaction.toString()
              + ") that was started by the method (" + stackTrace[i + 1].getMethodName()
              + ") on the class (" + stackTrace[i + 1].getClassName() + ") on line ("
              + stackTrace[i + 1].getLineNumber() + ")");
        }
      }
    }
  }
}

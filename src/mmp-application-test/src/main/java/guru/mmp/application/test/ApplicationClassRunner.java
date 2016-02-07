/*
 * Copyright 2016 Marcus Portmann
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

import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;

import guru.mmp.common.cdi.CDIUtil;
import guru.mmp.common.persistence.DAOUtil;

import net.sf.cglib.proxy.Enhancer;

import org.apache.naming.ContextBindings;

import org.jboss.weld.bootstrap.api.CDI11Bootstrap;
import org.jboss.weld.bootstrap.spi.Deployment;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.jboss.weld.resources.spi.ResourceLoader;
import org.jboss.weld.transaction.spi.TransactionServices;

import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

//~--- JDK imports ------------------------------------------------------------

import java.lang.reflect.Method;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import javax.sql.XADataSource;

import javax.transaction.*;

/**
 * The <code>ApplicationClassRunner</code> class implements the JUnit runner that provides
 * support for JUnit test classes that test the capabilities provided by the the <b>mmp-java (Open
 * Source Java and JEE Development Framework)</b>.
 * <p/>
 * This runner provides support for:
 * <ul>
 *   <li>JNDI using Apache Tomcat</li>
 *   <li>An in-memory application database using H2</li>
 *   <li>Contexts and Dependency Injection (CDI) using Weld</li>
 *   <li>JTA transaction management using the Atomikos transaction manager</li>
 *   <li>JPA using Hibernate</li>
 * </ul>
 *
 * @author Marcus Portmann
 */
public class ApplicationClassRunner extends BlockJUnit4ClassRunner
{
  /**
   * Constructs a new <code>ApplicationClassRunner</code>.
   *
   * @param testClass the JUnit test class to run
   *
   * @throws InitializationError
   */
  public ApplicationClassRunner(Class<?> testClass)
    throws InitializationError
  {
    super(testClass);

    try
    {
      LogManager.getLogManager().readConfiguration(Thread.currentThread().getContextClassLoader()
          .getResourceAsStream("logging.properties"));

      System.setProperty(Context.INITIAL_CONTEXT_FACTORY,
          "org.apache.naming.java.javaURLContextFactory");
      System.setProperty(Context.URL_PKG_PREFIXES, "org.apache.naming");

      if (!ContextBindings.isThreadBound())
      {
        // Initialise the initial context
        InitialContext ic = new InitialContext();

        ic.createSubcontext("app");
        ic.createSubcontext("app/env");
        ic.createSubcontext("app/jdbc");

        ic.createSubcontext("comp");
        ic.createSubcontext("comp/env");
        ic.createSubcontext("comp/env/jdbc");

        ic.createSubcontext("jboss");
        ic.createSubcontext("jboss/datasources");

        ic.bind("app/AppName", "Test");

        // Initialise the JTA user transaction and transaction manager
        Enhancer transactionManagerEnhancer = new Enhancer();
        transactionManagerEnhancer.setSuperclass(UserTransactionManager.class);
        transactionManagerEnhancer.setCallback(new TransactionManagerTransactionTracker());

        TransactionManager transactionManager =
            (TransactionManager) transactionManagerEnhancer.create();

        ic.bind("comp/TransactionManager", transactionManager);
        ic.bind("jboss/TransactionManager", transactionManager);

        Enhancer userTransactionEnhancer = new Enhancer();
        userTransactionEnhancer.setSuperclass(UserTransactionImp.class);
        userTransactionEnhancer.setCallback(new UserTransactionTracker());

        UserTransactionImp userTransaction = (UserTransactionImp) userTransactionEnhancer.create();

        userTransaction.setTransactionTimeout(300);

        ic.bind("comp/UserTransaction", userTransaction);
        ic.bind("jboss/UserTransaction", userTransaction);

        // Initialise the Weld bean manager with JTA transaction support
        Weld weld = new Weld()
        {
          @Override
          protected Deployment createDeployment(ResourceLoader resourceLoader,
              CDI11Bootstrap bootstrap)
          {
            Deployment deployment = super.createDeployment(resourceLoader, bootstrap);
            deployment.getServices().add(TransactionServices.class,
                new TransactionServices()
                {
                  @Override
                  public void cleanup() {}

                  @Override
                  public void registerSynchronization(Synchronization synchronization)
                  {
                    try
                    {
                      Transaction transaction = transactionManager.getTransaction();

                      if (transaction != null)
                      {
                        transaction.registerSynchronization(synchronization);
                      }
                    }
                    catch (Throwable e)
                    {
                      throw new RuntimeException(
                          "Failed to register the synchronisation with the Transaction", e);
                    }
                  }

                  @Override
                  public boolean isTransactionActive()
                  {
                    try
                    {
                      return userTransaction.getStatus() == Status.STATUS_ACTIVE;
                    }
                    catch (Throwable e)
                    {
                      throw new RuntimeException(
                          "Failed to check whether there is an active Transaction", e);
                    }
                  }

                  @Override
                  public UserTransaction getUserTransaction()
                  {
                    return userTransaction;
                  }
                });

            return deployment;
          }
        };

        WeldContainer weldContainer = weld.initialize();

        ic.bind("comp/BeanManager", weldContainer.getBeanManager());

        // Initialise the application data source
        DataSource dataSource = initApplicationDatabase(false);

        ic.bind("app/jdbc/ApplicationDataSource", dataSource);

        // Bind the initial context on the current thread
        ContextBindings.bindContext(Thread.currentThread().getName(), ic);

        ContextBindings.bindThread(Thread.currentThread().getName(), null);

        // Bind the application data source resource references
        ApplicationDataSourceResourceReference[] applicationDataSourceResourceReferences =
            testClass.getAnnotationsByType(ApplicationDataSourceResourceReference.class);

        for (ApplicationDataSourceResourceReference applicationDataSourceResourceReference :
            applicationDataSourceResourceReferences)
        {
          String name = applicationDataSourceResourceReference.name();

          if (name.startsWith("java:"))
          {
            name = name.substring(5);
          }

          try
          {
            ic.bind(name, dataSource);
          }
          catch (Throwable e)
          {
            throw new RuntimeException(
                "Failed to bind the application data source resource reference ("
                + applicationDataSourceResourceReference.name() + ")", e);
          }
        }
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

      Class<?> jdbcDataSourceClass = Thread.currentThread().getContextClassLoader().loadClass(
          "org.h2.jdbcx.JdbcDataSource");

      Method setURLMethod = jdbcDataSourceClass.getMethod("setURL", String.class);

      final DataSource jdbcDataSource = (DataSource) jdbcDataSourceClass.newInstance();

      setURLMethod.invoke(jdbcDataSource, "jdbc:h2:mem:" + Thread.currentThread().getName()
          + ";MODE=DB2;DB_CLOSE_DELAY=-1;" + "DB_CLOSE_ON_EXIT=FALSE");

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
                throw new RuntimeException("Failed to shutdown the in-memory application database",
                    e);
              }
            }
          });

      /*
       * Initialise the in-memory database using the SQL statements contained in the file with the
       * specified resource path.
       */
      for (String resourcePath : getDatabaseInitResources())
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

      Class<?> atomikosDataSourceBeanClass = Thread.currentThread().getContextClassLoader()
          .loadClass("com.atomikos.jdbc.AtomikosDataSourceBean");

      Object atomikosDataSourceBean = atomikosDataSourceBeanClass.newInstance();

      Method setUniqueResourceNameMethod = atomikosDataSourceBeanClass.getMethod(
          "setUniqueResourceName", String.class);

      setUniqueResourceNameMethod.invoke(atomikosDataSourceBean, Thread.currentThread().getName()
          + "-ApplicationDataSource");

      Method setXaDataSourceMethod = atomikosDataSourceBeanClass.getMethod("setXaDataSource",
          XADataSource.class);

      setXaDataSourceMethod.invoke(atomikosDataSourceBean, (XADataSource) jdbcDataSource);

      Method setMinPoolSizeMethod = atomikosDataSourceBeanClass.getMethod("setMinPoolSize", Integer
          .TYPE);
      setMinPoolSizeMethod.invoke(atomikosDataSourceBean, 5);

      Method setMaxPoolSizeMethod = atomikosDataSourceBeanClass.getMethod("setMaxPoolSize", Integer
          .TYPE);
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
              "Failed to successfully execute the test (" + method.getName() + "): Found an "
              + "unexpected active transaction (" + transaction.toString() + ") that was "
              + "started by the method (" + stackTrace[i + 1].getMethodName() + ") on the class"
              + " (" + stackTrace[i + 1].getClassName() + ") on line ("
              + stackTrace[i + 1].getLineNumber() + ")");

          throw new RuntimeException("Failed to successfully execute the test (" + method.getName()
              + "): Found an " + "unexpected active transaction (" + transaction.toString()
              + ") that was " + "started by the method (" + stackTrace[i + 1].getMethodName()
              + ") on the class" + " (" + stackTrace[i + 1].getClassName() + ") on line ("
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
              "Failed to successfully execute the test (" + method.getName() + "): Found an "
              + "unexpected active transaction (" + transaction.toString() + ") that was "
              + "started by the method (" + stackTrace[i + 1].getMethodName() + ") on the class"
              + " (" + stackTrace[i + 1].getClassName() + ") on line ("
              + stackTrace[i + 1].getLineNumber() + ")");

          throw new RuntimeException("Failed to successfully execute the test (" + method.getName()
              + "): Found an " + "unexpected active transaction (" + transaction.toString()
              + ") that was " + "started by the method (" + stackTrace[i + 1].getMethodName()
              + ") on the class" + " (" + stackTrace[i + 1].getClassName() + ") on line ("
              + stackTrace[i + 1].getLineNumber() + ")");
        }
      }
    }
  }

  /**
   * Returns the paths to the resources on the classpath that contain the SQL statements used to
   * initialise the in-memory application database.
   */
  private List<String> getDatabaseInitResources()
  {
    List<String> resources = new ArrayList<>();

    resources.add("guru/mmp/application/persistence/ApplicationH2.sql");

    ApplicationDataSourceSQLResource[] applicationDataSourceSQLResources =
        getTestClass().getJavaClass().getAnnotationsByType(ApplicationDataSourceSQLResource.class);

    for (ApplicationDataSourceSQLResource applicationDataSourceSQLResource :
        applicationDataSourceSQLResources)
    {
      resources.add(applicationDataSourceSQLResource.path());
    }

    return resources;
  }
}

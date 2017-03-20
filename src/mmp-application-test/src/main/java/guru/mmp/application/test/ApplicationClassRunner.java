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

package guru.mmp.application.test;

//~--- non-JDK imports --------------------------------------------------------

import org.junit.runners.model.InitializationError;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.logging.LogManager;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>ApplicationClassRunner</code> class implements the JUnit runner that provides
 * support for JUnit test classes that test the capabilities provided by the the <b>mmp-java (Open
 * Source Java and JEE Development Framework)</b>.
 * <p/>
 * This runner provides support for:
 * <ul>
 *   <li>Initialising the Java utility logging</li>
 *   <li>JNDI using Apache Tomcat</li>
 *   <li>An in-memory application database using H2</li>
 *   <li>Contexts and Dependency Injection (CDI) using Weld</li>
 *   <li>JTA transaction management using the Atomikos transaction manager</li>
 *   <li>JPA using Hibernate</li>
 * </ul>
 *
 * @author Marcus Portmann
 */
public class ApplicationClassRunner extends SpringJUnit4ClassRunner
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
    }
    catch (Throwable e)
    {
      throw new InitializationError("Failed to initialize the JDK logging: " + e.getMessage());
    }
  }
}

///*
// * Copyright 2017 Marcus Portmann
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *   http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package guru.mmp.application.test;
//
////~--- non-JDK imports --------------------------------------------------------
//
//import com.atomikos.icatch.jta.UserTransactionImp;
//import com.atomikos.icatch.jta.UserTransactionManager;
//import guru.mmp.common.cdi.CDIUtil;
//import guru.mmp.common.persistence.DAOUtil;
//import net.sf.cglib.proxy.Enhancer;
//import org.apache.naming.ContextBindings;
//import org.jboss.weld.bootstrap.api.CDI11Bootstrap;
//import org.jboss.weld.bootstrap.spi.Deployment;
//import org.jboss.weld.environment.se.Weld;
//import org.jboss.weld.environment.se.WeldContainer;
//import org.jboss.weld.resources.spi.ResourceLoader;
//import org.jboss.weld.transaction.spi.TransactionServices;
//import org.junit.runner.notification.RunNotifier;
//import org.junit.runners.BlockJUnit4ClassRunner;
//import org.junit.runners.model.FrameworkMethod;
//import org.junit.runners.model.InitializationError;
//
//import javax.enterprise.inject.spi.Extension;
//import javax.naming.Context;
//import javax.naming.InitialContext;
//import javax.sql.DataSource;
//import javax.sql.XADataSource;
//import javax.transaction.*;
//import java.lang.reflect.Field;
//import java.lang.reflect.Method;
//import java.sql.Connection;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.logging.Level;
//import java.util.logging.LogManager;
//import java.util.logging.Logger;
//
////~--- JDK imports ------------------------------------------------------------
//
///**
// * The <code>ApplicationClassRunner</code> class implements the JUnit runner that provides
// * support for JUnit test classes that test the capabilities provided by the the <b>mmp-java (Open
// * Source Java and JEE Development Framework)</b>.
// * <p/>
// * This runner provides support for:
// * <ul>
// *   <li>Initialising the Java utility logging</li>
// *   <li>JNDI using Apache Tomcat</li>
// *   <li>An in-memory application database using H2</li>
// *   <li>Contexts and Dependency Injection (CDI) using Weld</li>
// *   <li>JTA transaction management using the Atomikos transaction manager</li>
// *   <li>JPA using Hibernate</li>
// * </ul>
// *
// * @author Marcus Portmann
// */
//public class ApplicationClassRunner extends BlockJUnit4ClassRunner
//{
//  /**
//   * Constructs a new <code>ApplicationClassRunner</code>.
//   *
//   * @param testClass the JUnit test class to run
//   *
//   * @throws InitializationError
//   */
//  public ApplicationClassRunner(Class<?> testClass)
//    throws InitializationError
//  {
//    super(testClass);
//
//    try
//    {
//      LogManager.getLogManager().readConfiguration(Thread.currentThread().getContextClassLoader()
//          .getResourceAsStream("logging.properties"));
//
//      System.setProperty(Context.INITIAL_CONTEXT_FACTORY,
//          "org.apache.naming.java.javaURLContextFactory");
//      System.setProperty(Context.URL_PKG_PREFIXES, "org.apache.naming");
//
//      if (!ContextBindings.isThreadBound())
//      {
//        // Initialise the initial context
//        InitialContext ic = new InitialContext();
//
//        ic.createSubcontext("app");
//        ic.createSubcontext("app/env");
//        ic.createSubcontext("app/jdbc");
//
//        ic.createSubcontext("comp");
//        ic.createSubcontext("comp/env");
//        ic.createSubcontext("comp/env/jdbc");
//
//        ic.createSubcontext("jboss");
//        ic.createSubcontext("jboss/datasources");
//
//        ic.bind("app/AppName", "Test");
//
//        // Initialise the JTA user transaction and transaction manager
//        Enhancer transactionManagerEnhancer = new Enhancer();
//        transactionManagerEnhancer.setSuperclass(UserTransactionManager.class);
//        transactionManagerEnhancer.setCallback(new TransactionManagerTransactionTracker());
//
//        TransactionManager transactionManager = (TransactionManager) transactionManagerEnhancer.create();
//
//        ic.bind("comp/TransactionManager", transactionManager);
//        ic.bind("jboss/TransactionManager", transactionManager);
//
//        Enhancer userTransactionEnhancer = new Enhancer();
//        userTransactionEnhancer.setSuperclass(UserTransactionImp.class);
//        userTransactionEnhancer.setCallback(new UserTransactionTracker());
//
//        UserTransactionImp userTransaction = (UserTransactionImp) userTransactionEnhancer.create();
//
//        userTransaction.setTransactionTimeout(300);
//
//        ic.bind("comp/UserTransaction", userTransaction);
//        ic.bind("jboss/UserTransaction", userTransaction);
//
//        // Initialise the Weld bean manager with JTA transaction support
//        Weld weld = new Weld()
//        {
//          @Override
//          public Weld extensions(Extension... extensions)
//          {
//            return super.extensions(extensions);
//          }
//
//          @Override
//          protected Deployment createDeployment(ResourceLoader resourceLoader,
//              CDI11Bootstrap bootstrap)
//          {
//            Deployment deployment = super.createDeployment(resourceLoader, bootstrap);
//
//            deployment.getServices().add(TransactionServices.class,
//                new TransactionServices()
//                {
//                  @Override
//                  public void cleanup() {}
//
//                  @Override
//                  public void registerSynchronization(Synchronization synchronization)
//                  {
//                    try
//                    {
//                      Transaction transaction = transactionManager.getTransaction();
//
//                      if (transaction != null)
//                      {
//                        transaction.registerSynchronization(synchronization);
//                      }
//                    }
//                    catch (Throwable e)
//                    {
//                      throw new RuntimeException(
//                          "Failed to register the synchronisation with the Transaction", e);
//                    }
//                  }
//
//                  @Override
//                  public boolean isTransactionActive()
//                  {
//                    try
//                    {
//                      return userTransaction.getStatus() == Status.STATUS_ACTIVE;
//                    }
//                    catch (Throwable e)
//                    {
//                      throw new RuntimeException(
//                          "Failed to check whether there is an active Transaction", e);
//                    }
//                  }
//
//                  @Override
//                  public UserTransaction getUserTransaction()
//                  {
//                    return userTransaction;
//                  }
//                });
//
//            return deployment;
//          }
//        };
//
//        WeldContainer weldContainer = weld.initialize();
//
//        ic.bind("comp/BeanManager", weldContainer.getBeanManager());
//
//        // Initialise the application data source
//        DataSource dataSource = initApplicationDatabase(false);
//
//        ic.bind("app/jdbc/ApplicationDataSource", dataSource);
//
//        // Bind the initial context on the current thread
//        ContextBindings.bindContext(Thread.currentThread().getName(), ic);
//
//        ContextBindings.bindThread(Thread.currentThread().getName(), null);
//
//        // Bind the application data source resource references
//        ApplicationDataSourceResourceReference[] applicationDataSourceResourceReferences =
//            testClass.getAnnotationsByType(ApplicationDataSourceResourceReference.class);
//
//        for (ApplicationDataSourceResourceReference applicationDataSourceResourceReference :
//            applicationDataSourceResourceReferences)
//        {
//          String name = applicationDataSourceResourceReference.name();
//
//          if (name.startsWith("java:"))
//          {
//            name = name.substring(5);
//          }
//
//          try
//          {
//            ic.bind(name, dataSource);
//          }
//          catch (Throwable e)
//          {
//            throw new RuntimeException(
//                "Failed to bind the application data source resource reference ("
//                + applicationDataSourceResourceReference.name() + ")", e);
//          }
//        }
//      }
//    }
//    catch (Throwable e)
//    {
//      throw new RuntimeException("Failed to initialise the ApplicationClassRunner", e);
//    }
//  }
//
//  /**
//   * Run the tests for this runner.
//   *
//   * @param notifier the run notifier that will be notified of events while tests are being run
//   */
//  @Override
//  public void run(RunNotifier notifier)
//  {
//    super.run(notifier);
//  }
//
//  @Override
//  protected Object createTest()
//    throws Exception
//  {
//    Object testObject = super.createTest();
//
//    try
//    {
//      CDIUtil.inject(testObject);
//    }
//    catch (Throwable e)
//    {
//      throw new RuntimeException("Failed to inject the test object of type ("
//          + testObject.getClass().getName() + ")", e);
//    }
//
//    return testObject;
//  }
//



//
//  /**
//   * Run the child test for this runner.
//   *
//   * @param method   the test method being run
//   * @param notifier the run notifier that will be notified of events while tests are being run
//   */
//  @Override
//  protected void runChild(FrameworkMethod method, RunNotifier notifier)
//  {
//    super.runChild(method, notifier);
//
//    Map<Transaction, StackTraceElement[]> activeTransactionStackTraces =
//        TransactionManagerTransactionTracker.getActiveTransactionStackTraces();
//
//    // Check for unexpected active transactions managed by the Transaction Manager implementation
//    for (Transaction transaction : activeTransactionStackTraces.keySet())
//    {
//      StackTraceElement[] stackTrace = activeTransactionStackTraces.get(transaction);
//
//      for (int i = 0; i < stackTrace.length; i++)
//      {
//        if (stackTrace[i].getMethodName().equals("begin") && (stackTrace[i].getLineNumber() != -1))
//        {
//          Logger.getAnonymousLogger().log(Level.WARNING,
//              "Failed to successfully execute the test (" + method.getName() + "): Found an "
//              + "unexpected active transaction (" + transaction.toString() + ") that was "
//              + "started by the method (" + stackTrace[i + 1].getMethodName() + ") on the class"
//              + " (" + stackTrace[i + 1].getClassName() + ") on line ("
//              + stackTrace[i + 1].getLineNumber() + ")");
//
//          throw new RuntimeException("Failed to successfully execute the test (" + method.getName()
//              + "): Found an " + "unexpected active transaction (" + transaction.toString()
//              + ") that was " + "started by the method (" + stackTrace[i + 1].getMethodName()
//              + ") on the class" + " (" + stackTrace[i + 1].getClassName() + ") on line ("
//              + stackTrace[i + 1].getLineNumber() + ")");
//        }
//      }
//    }
//
//    activeTransactionStackTraces = UserTransactionTracker.getActiveTransactionStackTraces();
//
//    // Check for unexpected active transactions managed by the User Transaction implementation
//    for (Transaction transaction : activeTransactionStackTraces.keySet())
//    {
//      StackTraceElement[] stackTrace = activeTransactionStackTraces.get(transaction);
//
//      for (int i = 0; i < stackTrace.length; i++)
//      {
//        if (stackTrace[i].getMethodName().equals("begin") && (stackTrace[i].getLineNumber() != -1))
//        {
//          Logger.getAnonymousLogger().log(Level.WARNING,
//              "Failed to successfully execute the test (" + method.getName() + "): Found an "
//              + "unexpected active transaction (" + transaction.toString() + ") that was "
//              + "started by the method (" + stackTrace[i + 1].getMethodName() + ") on the class"
//              + " (" + stackTrace[i + 1].getClassName() + ") on line ("
//              + stackTrace[i + 1].getLineNumber() + ")");
//
//          throw new RuntimeException("Failed to successfully execute the test (" + method.getName()
//              + "): Found an " + "unexpected active transaction (" + transaction.toString()
//              + ") that was " + "started by the method (" + stackTrace[i + 1].getMethodName()
//              + ") on the class" + " (" + stackTrace[i + 1].getClassName() + ") on line ("
//              + stackTrace[i + 1].getLineNumber() + ")");
//        }
//      }
//    }
//  }
//
//}

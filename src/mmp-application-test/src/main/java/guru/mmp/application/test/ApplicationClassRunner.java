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

import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transaction;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

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
              + "): Found an unexpected active transaction (" + transaction.toString()
              + ") that was started by the method (" + stackTrace[i + 1].getMethodName()
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
              + "): Found an unexpected active transaction (" + transaction.toString()
              + ") that was started by the method (" + stackTrace[i + 1].getMethodName()
              + ") on the class" + " (" + stackTrace[i + 1].getClassName() + ") on line ("
              + stackTrace[i + 1].getLineNumber() + ")");
        }
      }
    }
  }
}

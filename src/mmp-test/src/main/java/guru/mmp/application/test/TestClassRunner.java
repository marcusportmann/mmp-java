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
import org.slf4j.LoggerFactory;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transaction;
import java.sql.Connection;
import java.util.Map;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>TestClassRunner</code> class implements the JUnit runner that provides support for
 * JUnit test classes that test the capabilities provided by the <b>mmp-java (Open Source Java
 * and JEE Development Framework)</b>.
 *
 * @author Marcus Portmann
 */
public class TestClassRunner extends SpringJUnit4ClassRunner
{
  /**
   * Constructs a new <code>TestClassRunner</code>.
   *
   * @param testClass the JUnit test class to run
   */
  public TestClassRunner(Class<?> testClass)
    throws InitializationError
  {
    super(testClass);
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

    checkForActiveTransactions(method,
        TransactionManagerTransactionTracker.getActiveTransactionStackTraces());

    checkForActiveTransactions(method, UserTransactionTracker.getActiveTransactionStackTraces());

    checkForOpenDatabaseConnections(method, DataSourceTracker.getActiveDatabaseConnections());
  }

  private void checkForActiveTransactions(FrameworkMethod method, Map<Transaction,
      StackTraceElement[]> activeTransactionStackTraces)
  {
    for (Transaction transaction : activeTransactionStackTraces.keySet())
    {
      StackTraceElement[] stackTrace = activeTransactionStackTraces.get(transaction);

      for (int i = 0; i < stackTrace.length; i++)
      {
        if (stackTrace[i].getMethodName().equals("begin") && (stackTrace[i].getLineNumber() != -1))
        {
          LoggerFactory.getLogger(TestClassRunner.class).warn(
              "Failed to successfully execute the test (" + method.getName() + "): Found an "
              + "unexpected active transaction (" + transaction.toString() + ") that was "
              + "started by the method (" + stackTrace[i + 1].getMethodName() + ") on the class ("
              + stackTrace[i + 1].getClassName() + ") on line ("
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

  private void checkForOpenDatabaseConnections(FrameworkMethod method, Map<Connection,
      StackTraceElement[]> activeDatabaseConnections)
  {
    for (Connection connection : activeDatabaseConnections.keySet())
    {
      StackTraceElement[] stackTrace = DataSourceTracker.getActiveDatabaseConnections().get(
          connection);

      for (int i = 0; i < stackTrace.length; i++)
      {
        if (stackTrace[i].getMethodName().equals("getConnection"))
        {
          LoggerFactory.getLogger(TestClassRunner.class).warn(
              "Failed to successfully execute the test (" + method.getName() + "): Found an "
              + "unexpected open database connection (" + connection.toString() + ") that was "
              + "retrieved by the method (" + stackTrace[i + 1].getMethodName() + ") on the class ("
              + stackTrace[i + 1].getClassName() + ") on line ("
              + stackTrace[i + 1].getLineNumber() + ")");

          throw new RuntimeException("Failed to successfully execute the test (" + method.getName()
              + "): Found an unexpected open database connection (" + connection.toString()
              + ") that was retrieved by the method (" + stackTrace[i + 1].getMethodName()
              + ") on the class" + " (" + stackTrace[i + 1].getClassName() + ") on line ("
              + stackTrace[i + 1].getLineNumber() + ")");
        }
      }
    }

  }
}

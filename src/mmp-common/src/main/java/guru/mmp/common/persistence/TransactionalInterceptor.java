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

package guru.mmp.common.persistence;

//~--- non-JDK imports --------------------------------------------------------

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Priority;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.naming.InitialContext;
import javax.transaction.Status;
import javax.transaction.UserTransaction;
import java.io.Serializable;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>TransactionalInterceptor</code> CDI interceptor is used together with the
 * <code>Transactional</code> annotation to apply a JTA transaction to a managed  bean or managed
 * bean method.
 *
 * @author Marcus Portmann
 */
@Interceptor
@Transactional
@Priority(Interceptor.Priority.APPLICATION)
public class TransactionalInterceptor
  implements Serializable
{
  private static final long serialVersionUID = 1000000;

  /**
   * Invokes the intercepted method under a JTA transaction.
   * <p/>
   * If the <code>newTransaction</code> annotation parameter is <code>false</code> then the
   * intercepted method will be invoked under the existing JTA user transaction if one exists. If
   * an existing JTA user transaction is not present then a new JTA user transaction will be
   * started.
   * <p/>
   * If the <code>newTransaction</code> annotation parameter is <code>true</code> then any existing
   * JTA user transaction will be suspended using the JTA <code>TransactionManager</code>. If
   * an existing JTA user transaction is not present then a new JTA user transaction will be
   * started.
   * <p/>
   * NOTED: If a runtime exception is thrown then the transaction will be rolled back if it is a
   * new transaction or marked for rollback if it is an existing transaction.
   *
   * @param context the current invocation-context
   *
   * @return the result of the intercepted method
   *
   * @throws Exception
   */
  @AroundInvoke
  public Object executeInTransaction(InvocationContext context)
    throws Exception
  {
    NewTransaction newTransactionAnnotation = getNewTransactionAnnotation(context);

    if (newTransactionAnnotation != null)
    {
      return executeInNewTransaction(context);
    }
    else
    {
      return executeInExistingTransaction(context);
    }
  }

  private Object executeInExistingTransaction(InvocationContext context)
    throws Exception
  {
    UserTransaction userTransaction = null;

    try
    {
      userTransaction = InitialContext.doLookup("java:comp/UserTransaction");

      getLogger().info("Successfully retrieved the bean managed transaction using the JNDI lookup"
          + " (java:comp/UserTransaction)");
    }
    catch (Throwable ignored) {}

    if ((userTransaction == null) && (System.getProperty("jboss.home.dir") != null))
    {
      try
      {
        userTransaction = InitialContext.doLookup("java:jboss/UserTransaction");

        getLogger().info(
            "Successfully retrieved the bean managed transaction using the JNDI lookup"
            + " (java:jboss/UserTransaction)");
      }
      catch (Throwable ignored)
      {
        getLogger().warn("Failed to lookup the bean managed transaction using the JNDI lookups"
            + " (java:comp/UserTransaction) and (java:jboss/UserTransaction)");
      }
    }

    if (userTransaction != null)
    {
      // Check for an existing transaction
      boolean isExistingTransaction;

      try
      {
        isExistingTransaction = (userTransaction.getStatus() != Status.STATUS_NO_TRANSACTION);
      }
      catch (Throwable e)
      {
        throw new TransactionalInterceptorException("Failed to determine if an existing JTA"
            + " transaction is active while invoking the intercepted method (" + getTarget(context)
            + ")");
      }

      try
      {
        if (!isExistingTransaction)
        {
          userTransaction.begin();
        }

        Object object = context.proceed();

        if (!isExistingTransaction)
        {
          userTransaction.commit();
        }

        return object;
      }
      catch (Exception e)
      {
        if (e instanceof RuntimeException)
        {
          if (isExistingTransaction)
          {
            getLogger().warn(
                "A runtime exception was encountered while invoking the intercepted method ("
                + getTarget(context) + "): The current transaction will be rolled back: "
                + e.getMessage());

            try
            {
              userTransaction.setRollbackOnly();
            }
            catch (Throwable f)
            {
              getLogger().error("An error occurred while invoking the intercepted method ("
                  + getTarget(context)
                  + "): The existing transaction could not be marked for rollback", f);
            }
          }
          else
          {
            if ((userTransaction.getStatus() != Status.STATUS_ROLLING_BACK)
                && (userTransaction.getStatus() != Status.STATUS_ROLLEDBACK)
                && (userTransaction.getStatus() != Status.STATUS_NO_TRANSACTION))
            {
              getLogger().warn(
                  "A runtime exception was encountered while invoking the intercepted method ("
                  + getTarget(context) + "): The new transaction will be rolled back: "
                  + e.getMessage());

              try
              {
                userTransaction.rollback();
              }
              catch (Throwable f)
              {
                getLogger().error("An error occurred while invoking the intercepted method ("
                    + getTarget(context) + "): The new transaction could not be rolled back", f);
              }
            }
          }

          throw e;
        }
        else
        {
          if (!isExistingTransaction)
          {
            if (userTransaction.getStatus() == Status.STATUS_MARKED_ROLLBACK)
            {
              try
              {
                userTransaction.rollback();
              }
              catch (Throwable f)
              {
                getLogger().error("An error occurred while invoking the intercepted method ("
                    + getTarget(context) + "): The new transaction could not be rolled back", f);
              }
            }
            else if ((userTransaction.getStatus() != Status.STATUS_ROLLING_BACK)
                && (userTransaction.getStatus() != Status.STATUS_ROLLEDBACK)
                && (userTransaction.getStatus() != Status.STATUS_NO_TRANSACTION))
            {
              getLogger().warn(
                  "A checked exception was encountered while invoking the intercepted method ("
                  + getTarget(context) + "): The current transaction will be committed: "
                  + e.getMessage());

              try
              {
                userTransaction.commit();
              }
              catch (Throwable f)
              {
                getLogger().error("An error occurred while invoking the intercepted method ("
                    + getTarget(context) + "): The new transaction could not be committed", f);
              }
            }
          }
        }

        throw e;
      }
      catch (Throwable e)
      {
        if (isExistingTransaction)
        {
          getLogger().warn(
              "A runtime exception was encountered while invoking the intercepted method ("
              + getTarget(context) + "): The existing transaction will be rolled back: "
              + e.getMessage());

          try
          {
            userTransaction.setRollbackOnly();
          }
          catch (Throwable f)
          {
            getLogger().error("An error occurred while invoking the intercepted method ("
                + getTarget(context)
                + "): The existing transaction could not be marked for rollback", f);
          }
        }
        else
        {
          if ((userTransaction.getStatus() != Status.STATUS_ROLLING_BACK)
              && (userTransaction.getStatus() != Status.STATUS_ROLLEDBACK)
              && (userTransaction.getStatus() != Status.STATUS_NO_TRANSACTION))
          {
            getLogger().warn(
                "A runtime exception was encountered while invoking the intercepted method ("
                + getTarget(context) + "): The new transaction will be rolled back: "
                + e.getMessage());

            try
            {
              userTransaction.rollback();
            }
            catch (Throwable f)
            {
              getLogger().error("An error occurred while invoking the intercepted method ("
                  + getTarget(context) + "): The new transaction could not be rolled back", f);
            }
          }
        }

        throw new RuntimeException(
            "A runtime exception was encountered while invoking the intercepted method ("
            + getTarget(context) + "): The current transaction will be rolled back", e);
      }
    }

    /*
     * If the injected UserTransaction is null we assume we are executing as part of a CMT for
     * an EJB. We do not do any transaction management so the default transaction management
     * for an EJB will apply i.e. no rollback for checked exception and rollback for runtime
     * exception.
     */
    else
    {
      return context.proceed();
    }
  }

  private Object executeInNewTransaction(InvocationContext context)
    throws Exception
  {
    UserTransaction userTransaction = null;

    try
    {
      userTransaction = InitialContext.doLookup("java:comp/UserTransaction");

      getLogger().info("Successfully retrieved the bean managed transaction using the JNDI lookup"
          + " (java:comp/UserTransaction)");
    }
    catch (Throwable ignored) {}

    if ((userTransaction == null) && (System.getProperty("jboss.home.dir") != null))
    {
      try
      {
        userTransaction = InitialContext.doLookup("java:jboss/UserTransaction");

        getLogger().info(
            "Successfully retrieved the bean managed transaction using the JNDI lookup"
            + " (java:jboss/UserTransaction)");
      }
      catch (Throwable ignored)
      {
        getLogger().warn("Failed to lookup the bean managed transaction using the JNDI lookups"
            + " (java:comp/UserTransaction) and (java:jboss/UserTransaction)");
      }
    }

    if (userTransaction != null)
    {
      // Check for an existing transaction
      boolean isExistingTransaction;

      try
      {
        isExistingTransaction = (userTransaction.getStatus() != Status.STATUS_NO_TRANSACTION);
      }
      catch (Throwable e)
      {
        throw new TransactionalInterceptorException("Failed to determine if an existing JTA"
            + " transaction is active while invoking the intercepted method (" + getTarget(context)
            + ")");
      }

      TransactionManager transactionManager = TransactionManager.getTransactionManager();

      javax.transaction.Transaction existingTransaction = null;

      try
      {
        if (isExistingTransaction)
        {
          existingTransaction = transactionManager.beginNew();
        }
        else
        {
          userTransaction.begin();
        }

        Object object = context.proceed();

        if (isExistingTransaction)
        {
          transactionManager.commit();
        }
        else
        {
          userTransaction.commit();
        }

        return object;
      }
      catch (Throwable e)
      {
        if (e instanceof RuntimeException)
        {
          if (isExistingTransaction)
          {
            getLogger().warn(
                "A runtime exception was encountered while invoking the intercepted method ("
                + getTarget(context) + "): The new transaction will be rolled back: "
                + e.getMessage());

            try
            {
              transactionManager.rollback();
            }
            catch (Throwable f)
            {
              getLogger().error("An error occurred while invoking the intercepted method ("
                  + getTarget(context) + "): The new transaction could not be rolled back", f);
            }
          }
          else
          {
            if ((userTransaction.getStatus() != Status.STATUS_ROLLING_BACK)
                && (userTransaction.getStatus() != Status.STATUS_ROLLEDBACK)
                && (userTransaction.getStatus() != Status.STATUS_NO_TRANSACTION))
            {
              getLogger().warn(
                  "A runtime exception was encountered while invoking the intercepted method ("
                  + getTarget(context) + "): The new transaction will be rolled back: "
                  + e.getMessage());

              try
              {
                userTransaction.rollback();
              }
              catch (Throwable f)
              {
                getLogger().error("An error occurred while invoking the intercepted method ("
                    + getTarget(context) + "): The new transaction could not be rolled back", f);
              }
            }
          }

          throw e;
        }
        else
        {
          if (isExistingTransaction)
          {
            getLogger().warn(
                "A checked exception was encountered while invoking the intercepted method ("
                + getTarget(context) + "): The new transaction will be committed: "
                + e.getMessage());

            try
            {
              transactionManager.commit();
            }
            catch (Throwable f)
            {
              getLogger().error("An error occurred while invoking the intercepted method ("
                  + getTarget(context) + "): The new transaction could not be committed", f);
            }
          }
          else
          {
            if (userTransaction.getStatus() == Status.STATUS_MARKED_ROLLBACK)
            {
              try
              {
                userTransaction.rollback();
              }
              catch (Throwable f)
              {
                getLogger().error("An error occurred while invoking the intercepted method ("
                    + getTarget(context) + "): The new transaction could not be rolled back", f);
              }
            }
            else if ((userTransaction.getStatus() != Status.STATUS_ROLLING_BACK)
                && (userTransaction.getStatus() != Status.STATUS_ROLLEDBACK)
                && (userTransaction.getStatus() != Status.STATUS_NO_TRANSACTION))
            {
              getLogger().warn(
                  "A checked exception was encountered while invoking the intercepted method ("
                  + getTarget(context) + "): The new transaction will be committed: "
                  + e.getMessage());

              try
              {
                userTransaction.commit();
              }
              catch (Throwable f)
              {
                getLogger().error("An error occurred while invoking the intercepted method ("
                    + getTarget(context) + "): The new transaction could not be committed", f);
              }
            }
          }
        }

        throw e;
      }
      finally
      {
        if (isExistingTransaction)
        {
          try
          {
            if (existingTransaction != null)
            {
              transactionManager.resume(existingTransaction);
            }
          }
          catch (Throwable e)
          {
            getLogger().error("An error occurred while invoking the intercepted method ("
                + getTarget(context) + "): Failed to resume the original transaction", e);
          }
        }
      }
    }

    /*
     * If the injected UserTransaction is null we assume we are executing as part of a CMT for
     * an EJB.
     */
    else
    {
      TransactionManager transactionManager = TransactionManager.getTransactionManager();

      javax.transaction.Transaction existingTransaction = null;

      try
      {
        if (transactionManager.isTransactionActive())
        {
          existingTransaction = transactionManager.beginNew();
        }
        else
        {
          transactionManager.begin();
        }

        Object object = context.proceed();

        transactionManager.commit();

        return object;
      }
      catch (Throwable e)
      {
        if (e instanceof RuntimeException)
        {
          getLogger().warn(
              "A runtime exception was encountered while invoking the intercepted method ("
              + getTarget(context) + "): The new transaction will be rolled back", e);

          try
          {
            transactionManager.rollback();
          }
          catch (Throwable f)
          {
            getLogger().error("An error occurred while invoking the intercepted method ("
                + getTarget(context) + "): The new transaction could not be rolled back", f);
          }

          throw e;
        }
        else
        {
          getLogger().warn(
              "A checked exception was encountered while invoking the intercepted method ("
              + getTarget(context) + "): The new transaction will be comitted");

          try
          {
            transactionManager.commit();
          }
          catch (Throwable f)
          {
            getLogger().error("An error occurred while invoking the intercepted method ("
                + getTarget(context) + "): The new transaction could not be committed", f);
          }
        }

        throw e;
      }
      finally
      {
        try
        {
          if (existingTransaction != null)
          {
            transactionManager.resume(existingTransaction);
          }
        }
        catch (Throwable e)
        {
          getLogger().error("An error occurred while invoking the intercepted method ("
              + getTarget(context) + "): Failed to resume the original transaction", e);
        }
      }
    }
  }

  private Logger getLogger()
  {
    // Retrieve the logger at runtime to prevent WebSphere "issues"
    return LoggerFactory.getLogger(TransactionalInterceptor.class);
  }

  private NewTransaction getNewTransactionAnnotation(InvocationContext context)
  {
    NewTransaction newTransactionAnnotation =
      context.getMethod().getAnnotation(NewTransaction.class);

    if (newTransactionAnnotation == null)
    {
      newTransactionAnnotation = context.getTarget().getClass().getAnnotation(NewTransaction.class);
    }

    return newTransactionAnnotation;
  }

  private String getTarget(InvocationContext context)
  {
    if ((context != null) && (context.getTarget() != null))
    {
      return context.getTarget().getClass().getName() + "." + context.getMethod().getName();
    }
    else
    {
      return "Unknown Target";
    }
  }
}

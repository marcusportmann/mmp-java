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

import guru.mmp.common.persistence.TransactionManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//~--- JDK imports ------------------------------------------------------------

import java.io.Serializable;

import javax.annotation.Priority;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import javax.naming.InitialContext;

import javax.transaction.*;

/**
 * The <code>ContainerManagedTransactionsInterceptor</code> CDI interceptor is used to apply the
 * container managed transaction behaviour to managed bean methods that are annotated with the
 * <code>ContainerManagedTransactions</code> annotation.
 *
 * @author Marcus Portmann
 */
@Interceptor
@ContainerManagedTransactions
@Priority(Interceptor.Priority.APPLICATION)
public class ContainerManagedTransactionsInterceptor
  implements Serializable
{
  private static final long serialVersionUID = 1000000;

  /**
   * Invokes the intercepted method.
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
    Transactional transactionalAnnotation = getTransactionalAnnotation(context);

    if (transactionalAnnotation != null)
    {
      TransactionManager transactionManager = TransactionManager.getTransactionManager();

      if (!transactionManager.isEnabled())
      {
        throw new ContainerManagedTransactionsInterceptorException(
            "Failed to initialise the JTA transaction manager");
      }

      if (transactionalAnnotation.value() == Transactional.TxType.REQUIRED)
      {
        return executeInTransaction(context, transactionManager, false);
      }
      else if (transactionalAnnotation.value() == Transactional.TxType.REQUIRES_NEW)
      {
        return executeInTransaction(context, transactionManager, true);
      }
      else if (transactionalAnnotation.value() == Transactional.TxType.MANDATORY)
      {
        if (!transactionManager.isTransactionActive())
        {
          throw new TransactionRequiredException();
        }
        else
        {
          return context.proceed();
        }
      }
      else if (transactionalAnnotation.value() == Transactional.TxType.SUPPORTS)
      {
        return context.proceed();
      }
      else if (transactionalAnnotation.value() == Transactional.TxType.NOT_SUPPORTED)
      {
        Transaction existingTransaction = null;

        try
        {
          existingTransaction = transactionManager.suspend();

          return context.proceed();
        }
        finally
        {
          if (existingTransaction != null)
          {
            transactionManager.resume(existingTransaction);
          }
        }
      }
      else if (transactionalAnnotation.value() == Transactional.TxType.NEVER)
      {
        if (transactionManager.getStatus() != Status.STATUS_NO_TRANSACTION)
        {
          throw new InvalidTransactionException();
        }
        else
        {
          return context.proceed();
        }
      }
      else
      {
        return new ContainerManagedTransactionsInterceptorException("Unknown TxType value ("
            + transactionalAnnotation.value() + ") found for Transactional annotation");
      }
    }
    else
    {
      return context.proceed();
    }
  }

  private Object executeInTransaction(InvocationContext context,
      TransactionManager transactionManager, boolean requiresNew)
    throws Exception
  {
    Transaction existingTransaction = null;
    boolean isNewTransaction = false;

    try
    {
      if (requiresNew)
      {
        existingTransaction = transactionManager.beginNew();
        isNewTransaction = true;
      }
      else if (transactionManager.getStatus() == Status.STATUS_ACTIVE)
      {
        // Do nothing we will re-use the existing transaction
      }
      else if (transactionManager.getStatus() == Status.STATUS_NO_TRANSACTION)
      {
        transactionManager.begin();
        isNewTransaction = true;
      }
      else if ((transactionManager.getStatus() == Status.STATUS_MARKED_ROLLBACK)
          || (transactionManager.getStatus() == Status.STATUS_ROLLING_BACK)
          || (transactionManager.getStatus() == Status.STATUS_ROLLEDBACK)
          || (transactionManager.getStatus() == Status.STATUS_PREPARING)
          || (transactionManager.getStatus() == Status.STATUS_PREPARED)
          || (transactionManager.getStatus() == Status.STATUS_COMMITTING)
          || (transactionManager.getStatus() == Status.STATUS_COMMITTED))
      {
        existingTransaction = transactionManager.beginNew();
        isNewTransaction = true;
      }
      else
      {
        throw new ContainerManagedTransactionsInterceptorException("Invalid transaction status ("
            + transactionManager.getStatus() + ")");
      }

      Object object = context.proceed();

      if (isNewTransaction)
      {
        if (transactionManager.getStatus() == Status.STATUS_ACTIVE)
        {
          transactionManager.commit();
        }
        else if (transactionManager.getStatus() == Status.STATUS_MARKED_ROLLBACK)
        {
          transactionManager.rollback();
        }
        else
        {
          throw new ContainerManagedTransactionsInterceptorException("Invalid status ("
              + transactionManager.getStatus() + ") for the new transaction");
        }
      }

      return object;
    }
    catch (Throwable e)
    {
      if (e instanceof RuntimeException)
      {
        if (isNewTransaction)
        {
          getLogger().warn(
              "A runtime exception was encountered while invoking the intercepted method ("
              + getTarget(context) + "): The new transaction will be rolled back", e);

          if (transactionManager.getStatus() == Status.STATUS_ACTIVE)
          {
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
          else if ((transactionManager.getStatus() == Status.STATUS_ROLLING_BACK)
              || (transactionManager.getStatus() == Status.STATUS_ROLLEDBACK))
          {
            // Do nothing the transaction is rolling back or has already rolled back
          }
          else
          {
            getLogger().error("An error occurred while invoking the intercepted method ("
                + getTarget(context) + "): The new transaction has an invalid status ("
                + transactionManager.getStatus() + ") and could not be rolled back");
          }
        }
        else
        {
          getLogger().warn(
              "A runtime exception was encountered while invoking the intercepted method ("
              + getTarget(context) + "): The existing transaction will be marked for rollback", e);

          if (transactionManager.getStatus() == Status.STATUS_ACTIVE)
          {
            try
            {
              transactionManager.setRollbackOnly();
            }
            catch (Throwable f)
            {
              getLogger().error("An error occurred while invoking the intercepted method ("
                  + getTarget(context)
                  + "): The existing transaction could not be marked for rollback", f);
            }
          }
          else if ((transactionManager.getStatus() == Status.STATUS_ROLLING_BACK)
              || (transactionManager.getStatus() == Status.STATUS_ROLLEDBACK))
          {
            // Do nothing the transaction is rolling back or has already rolled back
          }
          else
          {
            getLogger().error("An error occurred while invoking the intercepted method ("
                + getTarget(context) + "): The existing transaction has an invalid status ("
                + transactionManager.getStatus() + ") and could not be marked for rollback");
          }
        }

        throw e;
      }
      else
      {
        if (isNewTransaction)
        {
          getLogger().warn(
              "A checked exception was encountered while invoking the intercepted method ("
              + getTarget(context) + "): The new transaction will be committed");

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
      }

      throw e;
    }
    finally
    {
      if (existingTransaction != null)
      {
        try
        {
          transactionManager.resume(existingTransaction);
        }
        catch (Throwable e)
        {
          getLogger().error("An error occurred while invoking the intercepted method (" + getTarget(
              context) + "): Failed to resume the original transaction", e);
        }
      }
    }
  }

  private Logger getLogger()
  {
    // Retrieve the logger at runtime to prevent WebSphere "issues"
    return LoggerFactory.getLogger(ContainerManagedTransactionsInterceptor.class);
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

  private Transactional getTransactionalAnnotation(InvocationContext context)
  {
    Transactional transactionalAnnotation = context.getMethod().getAnnotation(Transactional.class);

    if (transactionalAnnotation == null)
    {
      transactionalAnnotation = context.getTarget().getClass().getAnnotation(Transactional.class);
    }

    return transactionalAnnotation;
  }
}

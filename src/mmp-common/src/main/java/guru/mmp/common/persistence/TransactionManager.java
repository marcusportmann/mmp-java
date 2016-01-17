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

package guru.mmp.common.persistence;

//~--- non-JDK imports --------------------------------------------------------

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//~--- JDK imports ------------------------------------------------------------

import javax.transaction.*;

/**
 * The TransactionManager class provides utility methods for managing transactions in a J2EE
 * application server neutral fashion.
 *
 * @author Marcus Portmann
 */
public final class TransactionManager
  implements javax.transaction.TransactionManager
{
  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(TransactionManager.class);
  private static TransactionManager singleton;
  private javax.transaction.TransactionManager transactionManager;

  /**
   * Private default constructor to enforce singleton pattern.
   */
  private TransactionManager()
  {
    try
    {
      transactionManager = TransactionManagerFactory.getTransactionManager();
    }
    catch (Throwable e)
    {
      logger.error("Failed to retrieve the JTA TransactionManager: " + e.getMessage(), e);
    }

    if (transactionManager == null)
    {
      logger.warn("Failed to retrieve the JTA TransactionManager:"
          + " Transactional database access is " + "not available");
    }
  }

  /**
   * Returns the <code>TransactionManager</code> singleton.
   *
   * @return the <code>TransactionManager</code> singleton
   */
  public static TransactionManager getTransactionManager()
  {
    if (singleton == null)
    {
      singleton = new TransactionManager();
    }

    return singleton;
  }

  /**
   * @throws NotSupportedException
   * @throws SystemException
   * @see javax.transaction.TransactionManager#begin()
   */
  public void begin()
    throws NotSupportedException, SystemException
  {
    if (isEnabled())
    {
      transactionManager.begin();
    }
  }

  /**
   * Calling this method will suspend the current transaction (if there is one) and begin a brand
   * new transaction. This method <b>MUST </b> be used in conjunction with a matching call to the
   * <code>resume</code> method.
   *
   * @return The transaction that was active before invocation of this method or <code>null</code>
   * if no transaction was active. The return value from this method <b>MUST </b> be passed
   * to the <code>resume</code> method or the suspended transaction will be lost.
   *
   * @throws SystemException
   * @throws NotSupportedException
   */
  public Transaction beginNew()
    throws SystemException, NotSupportedException
  {
    Transaction result = null;

    if (isEnabled())
    {
      result = transactionManager.suspend();

      begin();
    }

    return result;
  }

  /**
   * @throws HeuristicMixedException
   * @throws HeuristicRollbackException
   * @throws IllegalStateException
   * @throws RollbackException
   * @throws SecurityException
   * @throws SystemException
   * @see javax.transaction.TransactionManager#commit()
   */
  public void commit()
    throws RollbackException, HeuristicMixedException, HeuristicRollbackException,
        SecurityException, IllegalStateException, SystemException
  {
    if (isEnabled())
    {
      transactionManager.commit();
    }
  }

  /**
   * @return the transaction status
   *
   * @throws SystemException
   * @see javax.transaction.TransactionManager#getStatus()
   */
  public int getStatus()
    throws SystemException
  {
    if (isEnabled())
    {
      return transactionManager.getStatus();
    }
    else
    {
      return Status.STATUS_NO_TRANSACTION;
    }
  }

  /**
   * @return the current transaction
   *
   * @throws SystemException
   * @see javax.transaction.TransactionManager#getTransaction()
   */
  public Transaction getTransaction()
    throws SystemException
  {
    if (isEnabled())
    {
      return transactionManager.getTransaction();
    }
    else
    {
      return null;
    }
  }

  /**
   * Is the TransactionManager supported?
   *
   * @return true if TransactionManager is supported or false otherwise
   */
  public boolean isEnabled()
  {
    return (transactionManager != null);
  }

  /**
   * Is there a transaction associated with the current thread?
   *
   * @return true if there is a transaction associated with the current thread or false otherwise
   */
  public boolean isTransactionActive()
  {
    boolean result = false;

    if (isEnabled())
    {
      try
      {
        result = (transactionManager.getStatus() != Status.STATUS_NO_TRANSACTION);
      }
      catch (SystemException e)
      {
        // IGNORE the exceptions because all we want to know is if a transaction is in progress.
      }
    }

    return result;
  }

  /**
   * Resume the previously suspended transaction.
   * </p>
   * This method will resume a previously suspended transaction. This method <b>MUST</b> be called
   * subsequent to a call to <code>beginNew</code>.
   *
   * @param transaction The value of this parameter <b>MUST</b> be an instance that is returned by
   *                    a call to <code>beginNew</code>. Any other value is ILLEGAL. This method
   *                    gracefully handles passing <code>null</code>.
   *
   * @throws InvalidTransactionException
   * @throws SystemException
   * @see javax.transaction.TransactionManager#resume(Transaction)
   */
  public void resume(Transaction transaction)
    throws InvalidTransactionException, SystemException
  {
    if (isEnabled())
    {
      if (transaction != null)
      {
        transactionManager.resume(transaction);
      }
    }
  }

  /**
   * @throws SystemException
   * @see javax.transaction.TransactionManager#rollback()
   */
  public void rollback()
    throws SystemException
  {
    if (isEnabled())
    {
      transactionManager.rollback();
    }
  }

  /**
   * @throws IllegalStateException
   * @throws SystemException
   * @see javax.transaction.TransactionManager#setRollbackOnly()
   */
  public void setRollbackOnly()
    throws IllegalStateException, SystemException
  {
    if (isEnabled())
    {
      transactionManager.setRollbackOnly();
    }
  }

  /**
   * @param seconds the transaction timeout in seconds
   *
   * @throws SystemException
   * @see javax.transaction.TransactionManager#setTransactionTimeout(int)
   */
  public void setTransactionTimeout(int seconds)
    throws SystemException
  {
    if (isEnabled())
    {
      transactionManager.setTransactionTimeout(seconds);
    }
  }

  /**
   * @return the suspended transaction or <code>null</code> if transactions are not supported
   *
   * @throws SystemException
   * @see javax.transaction.TransactionManager#suspend()
   */
  public Transaction suspend()
    throws SystemException
  {
    if (isEnabled())
    {
      return transactionManager.suspend();
    }
    else
    {
      return null;
    }
  }
}

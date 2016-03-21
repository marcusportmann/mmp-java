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

import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * The TransactionManagerFactory retrieves the implementation of the TransactionManager for the
 * J2EE application server being used.
 *
 * @author Marcus Portmann
 */
public class TransactionManagerFactory
{
  /**
   * Private default constructor to enforce utility pattern.
   */
  private TransactionManagerFactory() {}

  /**
   * This class retrieves the TransactionManager implementation from JNDI.
   *
   * @return the TransactionManger
   *
   * @throws TransactionManagerException
   */
  public static javax.transaction.TransactionManager getTransactionManager()
    throws TransactionManagerException
  {
    InitialContext ic = null;

    Logger logger = LoggerFactory.getLogger(TransactionManagerFactory.class);

    // JBoss 7
    try
    {
      ic = new InitialContext();

      // Attempt to retrieve using a local JNDI reference
      try
      {
        if (logger.isDebugEnabled())
        {
          logger.debug("Attempting to retrieve the TransactionManager using the JNDI" + " entry "
              + "(java:jboss/TransactionManager)");
        }

        javax.transaction.TransactionManager transactionManager = (javax.transaction
            .TransactionManager) ic.lookup("java:jboss/TransactionManager");

        if (transactionManager != null)
        {
          if (logger.isDebugEnabled())
          {
            logger.debug("Successfully retrieved the TransactionManager using the JNDI" + " entry "
                + "(java:jboss/TransactionManager)");
          }

          return transactionManager;
        }
      }
      catch (NamingException e)
      {
        // Do nothing
      }
    }
    catch (Throwable e)
    {
      throw new TransactionManagerException("Failed to retrieve the TransactionManager from JNDI: "
          + e.getMessage(), e);
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

    // Glassfish
    try
    {
      ic = new InitialContext();

      // Attempt to retrieve using a local JNDI reference
      try
      {
        if (logger.isDebugEnabled())
        {
          logger.debug("Attempting to retrieve the TransactionManager using the JNDI" + " entry "
              + "(java:appserver/TransactionManager)");
        }

        javax.transaction.TransactionManager transactionManager = (javax.transaction
            .TransactionManager) ic.lookup("java:appserver/TransactionManager");

        if (transactionManager != null)
        {
          if (logger.isDebugEnabled())
          {
            logger.debug("Successfully retrieved the TransactionManager using the JNDI" + " entry "
                + "(java:appserver/TransactionManager)");
          }

          return transactionManager;
        }
      }
      catch (NamingException e)
      {
        // Do nothing
      }
    }
    catch (Throwable e)
    {
      throw new TransactionManagerException("Failed to retrieve the TransactionManager from JNDI: "
          + e.getMessage(), e);
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

    // WebSphere 5.1, 6
    try
    {
      if (logger.isDebugEnabled())
      {
        logger.debug("Attempting to retrieve the TransactionManager from the" + " "
            + "TransactionManagerFactory (com.ibm.ws.Transaction.TransactionManagerFactory)");
      }

      // The implementation class in WebSphere 5
      Class<?> clazz = Class.forName("com.ibm.ws.Transaction.TransactionManagerFactory");
      javax.transaction.TransactionManager transactionManager = (javax.transaction
          .TransactionManager) clazz.getMethod("getTransactionManager", (Class[]) null).invoke(
          null, (Object[]) null);

      if (transactionManager != null)
      {
        if (logger.isDebugEnabled())
        {
          logger.debug(
              "Successfully retrieved the TransactionManager from the TransactionManagerFactory"
              + " (com.ibm.ws.Transaction.TransactionManagerFactory)");
        }

        return transactionManager;
      }
    }
    catch (Throwable e)
    {
      // Do nothing
    }

    // WebSphere 5
    try
    {
      if (logger.isDebugEnabled())
      {
        logger.debug("Attempting to retrieve the TransactionManager from the" + " "
            + "TransactionManagerFactory (com.ibm.ejs.jts.jta.TransactionManagerFactory)");
      }

      // The implementation class in WebSphere 5
      Class<?> clazz = Class.forName("com.ibm.ejs.jts.jta.TransactionManagerFactory");
      javax.transaction.TransactionManager transactionManager = (javax.transaction
          .TransactionManager) clazz.getMethod("getTransactionManager", (Class[]) null).invoke(
          null, (Object[]) null);

      if (transactionManager != null)
      {
        if (logger.isDebugEnabled())
        {
          logger.debug(
              "Successfully retrieved the TransactionManager from the TransactionManagerFactory"
              + " (com.ibm.ejs.jts.jta.TransactionManagerFactory)");
        }

        return transactionManager;
      }
    }
    catch (Throwable e)
    {
      // Do nothing
    }

    // JBoss 3
    try
    {
      ic = new InitialContext();

      // Attempt to retrieve using a local JNDI reference
      try
      {
        if (logger.isDebugEnabled())
        {
          logger.debug("Attempting to retrieve the TransactionManager using the JNDI" + " entry "
              + "(java:/TransactionManager)");
        }

        javax.transaction.TransactionManager transactionManager = (javax.transaction
            .TransactionManager) ic.lookup("java:/TransactionManager");

        if (transactionManager != null)
        {
          if (logger.isDebugEnabled())
          {
            logger.debug("Successfully retrieved the TransactionManager using the JNDI" + " entry "
                + "(java:/TransactionManager)");
          }

          return transactionManager;
        }
      }
      catch (NamingException e)
      {
        // Do nothing
      }
    }
    catch (Throwable e)
    {
      throw new TransactionManagerException("Failed to retrieve the TransactionManager from JNDI: "
          + e.getMessage(), e);
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

    // WebLogic 8
    try
    {
      ic = new InitialContext();

      // Attempt to retrieve using a local JNDI reference
      try
      {
        if (logger.isDebugEnabled())
        {
          logger.debug("Attempting to retrieve the TransactionManager using the JNDI"
              + " entry (javax" + ".transaction.TransactionManager)");
        }

        javax.transaction.TransactionManager transactionManager = (javax.transaction
            .TransactionManager) ic.lookup("javax.transaction.TransactionManager");

        if (transactionManager != null)
        {
          if (logger.isDebugEnabled())
          {
            logger.debug("Successfully retrieved the TransactionManager using the JNDI"
                + " entry (javax" + ".transaction.TransactionManager)");
          }

          return transactionManager;
        }
      }
      catch (NamingException e)
      {
        // Do nothing
      }
    }
    catch (Throwable e)
    {
      throw new TransactionManagerException("Failed to retrieve the TransactionManager from JNDI: "
          + e.getMessage(), e);
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

    try
    {
      ic = new InitialContext();

      // Attempt to retrieve using a local JNDI reference
      try
      {
        if (logger.isDebugEnabled())
        {
          logger.debug("Attempting to retrieve the TransactionManager using the local JNDI"
              + " reference " + "(java:comp/env/TransactionManager)");
        }

        javax.transaction.TransactionManager transactionManager = (javax.transaction
            .TransactionManager) ic.lookup("java:comp/env/TransactionManager");

        if (transactionManager != null)
        {
          if (logger.isDebugEnabled())
          {
            logger.debug("Successfully retrieved the TransactionManager using the local JNDI" + " "
                + "reference (java:comp/env/TransactionManager)");
          }

          return transactionManager;
        }
      }
      catch (NamingException e)
      {
        // Do nothing
      }
    }
    catch (Throwable e)
    {
      throw new TransactionManagerException("Failed to retrieve the TransactionManager from JNDI: "
          + e.getMessage(), e);
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

    return null;
  }
}

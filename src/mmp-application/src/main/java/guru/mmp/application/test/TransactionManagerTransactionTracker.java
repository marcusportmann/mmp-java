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

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

//~--- JDK imports ------------------------------------------------------------

import java.io.Serializable;

import java.lang.reflect.Method;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.transaction.*;

/**
 * The <code>TransactionManagerTransactionTracker</code> class.
 *
 * @author Marcus Portmann
 */
public class TransactionManagerTransactionTracker
  implements MethodInterceptor, Serializable
{
  private static final long serialVersionUID = 1000000;

  private transient static Map<Transaction, StackTraceElement[]> activeTransactionStackTraces = new ConcurrentHashMap<>();

  /**
   * Returns the active transaction stack traces.
   *
   * @return the active transaction stack traces
   */
  public static Map<Transaction, StackTraceElement[]> getActiveTransactionStackTraces()
  {
    return activeTransactionStackTraces;
  }

  /**
   * Constructs a new <code>TransactionManagerMethodInterceptor</code>.
   */
  public TransactionManagerTransactionTracker()
  {
    try
    {
    }
    catch (Throwable e)
    {
      throw new RuntimeException(
          "Failed to retrieve the getTransaction method for the TransactionManager class");
    }
  }

  /**
   * Intercept the method invocation.
   *
   * @param obj    the object the method was invoked on
   * @param method the method that was invoked
   * @param args   the method arguments
   * @param proxy  the proxy
   *
   * @return the results of invoking the method
   *
   * @throws Throwable
   */
  @Override
  public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy)
    throws Throwable
  {
    Method getTransactionMethod = TransactionManager.class.getMethod("getTransaction");

    try
    {
      if (method.getName().equals("begin"))
      {
        Transaction beforeTransaction = (Transaction)getTransactionMethod.invoke(obj);

        // TODO: Should we throw an exception if there is an existing transaction when calling being -- MARCUS

        try
        {
          return proxy.invokeSuper(obj, args);
        }
        catch (Throwable e)
        {
          throw e;
        }
        finally
        {
          Transaction afterTransaction = (Transaction) getTransactionMethod.invoke(obj);

          if (afterTransaction != null)
          {
            activeTransactionStackTraces
              .put(afterTransaction, Thread.currentThread().getStackTrace());
          }
        }
      }
      else if (method.getName().equals("commit"))
      {
        Transaction beforeTransaction = (Transaction)getTransactionMethod.invoke(obj);

        try
        {
          return proxy.invokeSuper(obj, args);
        }
        catch (Throwable e)
        {
          throw e;
        }
        finally
        {
          Transaction afterTransaction = (Transaction) getTransactionMethod.invoke(obj);

          if ((beforeTransaction != null) && (afterTransaction == null))
          {
            if (activeTransactionStackTraces.containsKey(beforeTransaction))
            {
              activeTransactionStackTraces.remove(beforeTransaction);
            }
          }
        }
      }
      else if (method.getName().equals("resume"))
      {
        Transaction beforeTransaction = (Transaction)getTransactionMethod.invoke(obj);

        Object result = proxy.invokeSuper(obj, args);

        Transaction afterTransaction = (Transaction)getTransactionMethod.invoke(obj);

        return result;
      }
      else if (method.getName().equals("rollback"))
      {
        Transaction beforeTransaction = (Transaction)getTransactionMethod.invoke(obj);

        try
        {
          return proxy.invokeSuper(obj, args);
        }
        catch (Throwable e)
        {
          throw e;
        }
        finally
        {
          Transaction afterTransaction = (Transaction) getTransactionMethod.invoke(obj);

          if ((beforeTransaction != null) && (afterTransaction == null))
          {
            if (activeTransactionStackTraces.containsKey(beforeTransaction))
            {
              activeTransactionStackTraces.remove(beforeTransaction);
            }
          }
        }
      }
      else if (method.getName().equals("setRollbackOnly"))
      {
        Object result = proxy.invokeSuper(obj, args);

        return result;
      }
      else if (method.getName().equals("suspend"))
      {
        Transaction beforeTransaction = (Transaction)getTransactionMethod.invoke(obj);

        Object result = proxy.invokeSuper(obj, args);

        Transaction afterTransaction = (Transaction)getTransactionMethod.invoke(obj);

        return result;
      }
      else
      {
        return proxy.invokeSuper(obj, args);
      }
    }
    catch (Throwable e)
    {
      Logger.getAnonymousLogger().log(Level.SEVERE,
          "Failed to invoke the Transaction Manager method", e);

      throw e;
    }
  }
}

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

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The <code>UserTransactionMethodInterceptor</code> class.
 *
 * @author Marcus Portmann
 */
public class UserTransactionMethodInterceptor
  implements MethodInterceptor, Serializable
{
  private static final long serialVersionUID = 1000000;

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
    try
    {
      if (method.getName().equals("begin"))
      {
        return proxy.invokeSuper(obj, args)
      }
      else if (method.getName().equals("commit"))
      {
        return proxy.invokeSuper(obj, args);
      }
      else if (method.getName().equals("commit"))
      {
        return proxy.invokeSuper(obj, args);
      }
      else
      {
        return proxy.invokeSuper(obj, args);
      }
    }
    catch (Throwable e)
    {
      Logger
        .getAnonymousLogger().log(Level.SEVERE, "Failed to invoke the UserTransaction method", e);
      throw e;
    }
  }
}
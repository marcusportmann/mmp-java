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

//~--- JDK imports ------------------------------------------------------------

import javax.transaction.Transaction;
import java.lang.reflect.Method;

import java.sql.Connection;

/**
 * The <code>AutoEnlistConnection</code> class.
 *
 *  @author Marcus Portmann
 */
public class AutoEnlistConnection
  implements java.lang.reflect.InvocationHandler
{
  private Connection connection;
  private AutoEnlistJdbcDataSource autoEnlistJdbcDataSource;

  private AutoEnlistConnection(Connection connection)
  {
    this.connection = connection;
  }

  /**
   * Method description
   *
   * @param connection
   *
   * @return
   */
  public static Object newInstance(AutoEnlistJdbcDataSource autoEnlistJdbcDataSource,  Connection connection)
  {
    return java.lang.reflect.Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
        new Class<?>[] { Connection.class }, new AutoEnlistConnection(connection));
  }

  /**
   * Method description
   *
   * @param proxy
   * @param method
   * @param args
   *
   * @return
   *
   * @throws Throwable
   */
  public Object invoke(Object proxy, Method method, Object[] args)
    throws Throwable
  {
    try
    {
      if (method.getName().equals("close"))
      {

        return method.invoke(connection, args);
      }
      else
      {
        return method.invoke(connection, args);
      }
    }
    catch (Throwable e)
    {
      throw new RuntimeException(
          "The AutoEnlistConnection dynamic proxy failed to invoke the method (" + method.getName()
          + ") on the connection (" + connection.toString() + ")", e);
    }

  }
}

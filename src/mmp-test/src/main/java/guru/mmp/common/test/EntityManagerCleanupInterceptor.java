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

package guru.mmp.common.test;

//~--- JDK imports ------------------------------------------------------------

import java.io.Serializable;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

/**
 * The <code>EntityManagerCleanupInterceptor</code> interceptor is responsible for cleaning up the
 * <code>EntityManager</code> instances, that are not associated with a transaction, that are
 * injected into CDI beans.
 *
 * @author Marcus Portmann
 */
@EntityManagerCleanup
@Interceptor
public class EntityManagerCleanupInterceptor
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
  public Object enitityManagerCleanup(InvocationContext context)
    throws Exception
  {
    try
    {
      return context.proceed();
    }
    finally
    {
      // Cleanup the <code>EntityManager</code> instances that are not associated with a transaction

    }
  }
}

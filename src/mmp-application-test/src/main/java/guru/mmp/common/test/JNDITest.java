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

//~--- non-JDK imports --------------------------------------------------------

import org.apache.naming.ContextBindings;
import org.junit.BeforeClass;

import javax.naming.Context;
import javax.naming.InitialContext;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>JNDITest</code> class provides the base class for all JUnit test classes that make use
 * of JNDI outside of a traditional JEE application server.
 * <p/>
 * The JNDI functionality is provided by the Apache Tomcat <b>naming-factory</b> and
 * <b>naming-resources</b> libraries.
 *
 * @author Marcus Portmann
 */
public abstract class JNDITest extends Test
{
  /**
   * This method is executed before any of the test methods are executed for the test class.
   */
  @BeforeClass
  public static void initJNDITestResources()
  {
    try
    {
      System.setProperty(Context.INITIAL_CONTEXT_FACTORY,
          "org.apache.naming.java.javaURLContextFactory");
      System.setProperty(Context.URL_PKG_PREFIXES, "org.apache.naming");

      InitialContext ic = new InitialContext();

      ic.createSubcontext("app");
      ic.createSubcontext("app/env");
      ic.createSubcontext("app/jdbc");

      ic.createSubcontext("comp");
      ic.createSubcontext("comp/env");
      ic.createSubcontext("comp/env/jdbc");

      // Bind the initial context on the current thread
      ContextBindings.bindContext(Thread.currentThread().getName(), ic);

      ContextBindings.bindThread(Thread.currentThread().getName(), null);
    }
    catch (Throwable e)
    {
      throw new RuntimeException("Failed to initialise the JNDITest resources", e);
    }
  }
}

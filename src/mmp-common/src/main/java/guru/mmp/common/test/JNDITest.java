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

package guru.mmp.common.test;

//~--- non-JDK imports --------------------------------------------------------

import org.junit.Before;
import org.junit.BeforeClass;

//~--- JDK imports ------------------------------------------------------------

import javax.naming.Context;
import javax.naming.InitialContext;

/**
 * The <code>JNDITest</code> class provides a base class that contains functionality common to all
 * JUnit test classes that leverage JNDI outside of a traditional JEE application server.
 *
 * @author Marcus Portmann
 */
public abstract class JNDITest extends Test
{
  /**
   * Initialize the JNDI environment.
   */
  @Before
  public void initJNDI()
  {
    try
    {
      System.setProperty(Context.INITIAL_CONTEXT_FACTORY,
          "org.apache.naming.java.javaURLContextFactory");
      System.setProperty(Context.URL_PKG_PREFIXES, "org.apache.naming");

      InitialContext ic = new InitialContext();

      ic.createSubcontext("java:");
      ic.createSubcontext("java:app");
      ic.createSubcontext("java:app/env");
      ic.createSubcontext("java:app/jdbc");

      ic.createSubcontext("java:comp");
      ic.createSubcontext("java:comp/env");
      ic.createSubcontext("java:comp/env/jdbc");
    }
    catch (Throwable e)
    {
      throw new RuntimeException("Failed to initialise the JNDI environment for the JUnit tests",
          e);
    }
  }
}

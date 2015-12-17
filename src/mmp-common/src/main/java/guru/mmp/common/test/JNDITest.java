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

import org.junit.BeforeClass;

//~--- JDK imports ------------------------------------------------------------

import javax.naming.Context;
import javax.naming.InitialContext;

/**
 * The <code>JNDITest</code> class provides the base class for all JUnit test classes that make use
 * of JNDI outside of a traditional JEE application server.
 * <p>
 * The JNDI functionality is provided by the Apache Tomcat <b>naming-factory</b> and
 * <b>naming-resources</b> libraries.
 * <p>
 * Please ensure you add the following dependencies to your Maven pom.xml file:
 * <pre>
 * &lt;dependency&gt;
 *   &lt;groupId&gt;tomcat&lt;/groupId&gt;
 *   &lt;artifactId&gt;naming-factory&lt;/artifactId&gt;
 *   &lt;version&gt;5.5.23&lt;/version&gt;
 *   &lt;scope&gt;test&lt;/scope&gt;
 * &lt;/dependency&gt;
 * &lt;dependency&gt;
 *   &lt;groupId&gt;tomcat&lt;/groupId&gt;
 *   &lt;artifactId&gt;naming-resources&lt;/artifactId&gt;
 *   &lt;version&gt;5.5.23&lt;/version&gt;
 *   &lt;scope&gt;test&lt;/scope&gt;
 * &lt;/dependency&gt;
 * <pre>
 *
 * @author Marcus Portmann
 */
public abstract class JNDITest extends Test
{
  /**
   * This method is executed by the JUnit test infrastructure before any of the JUnit test methods
   * are executed for the JUnit test class.
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
      throw new RuntimeException("Failed to initialise the test resources", e);
    }
  }
}

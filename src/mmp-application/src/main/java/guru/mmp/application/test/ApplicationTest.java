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

import guru.mmp.common.test.DatabaseTest;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

//~--- JDK imports ------------------------------------------------------------

import java.lang.reflect.Method;

import javax.naming.InitialContext;

import javax.sql.DataSource;

/**
 * The <code>ApplicationTest</code> class provides the base class for all JUnit test classes that
 * test the capabilities provided by the the <b>mmp-java (Open Source Java and JEE Development
 * Framework)</b>.
 *
 * @author Marcus Portmann
 */
@RunWith(InjectionJUnit4ClassRunner.class)
public abstract class ApplicationTest extends DatabaseTest
{
  private static BasicDataSource dataSource;

  /**
   * This method is executed by the JUnit test infrastructure after all the JUnit test methods for
   * the JUnit test class have been executed. It is responsible for cleaning up the resources used
   * by the tests.
   */
  @AfterClass
  public static void cleanupApplicationTestResources()
  {
    try
    {
      dataSource.close();
    }
    catch (Throwable e)
    {
      throw new RuntimeException("Failed to cleanup the test resources", e);
    }
  }

  /**
   * This method is executed by the JUnit test infrastructure before any of the JUnit test methods
   * are executed for the JUnit test class.
   */
  @BeforeClass
  public static void initApplicationTestResources()
  {
    InitialContext ic = null;

    try
    {
      // Initialise the in-memory database that will be used when executing a test
      dataSource = initDatabase("ApplicationTest",
          "guru/mmp/application/persistence/ApplicationH2.sql", false);

      // Initialise the JNDI
      ic = new InitialContext();

      ic.bind("java:app/env/RegistryPathPrefix", "/ApplicationTest");
      ic.bind("java:app/jdbc/ApplicationDataSource", dataSource);
    }
    catch (Throwable e)
    {
      throw new RuntimeException("Failed to initialise the test resources", e);
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
  }
}

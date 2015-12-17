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

import guru.mmp.application.cdi.CDIUtil;
import guru.mmp.common.test.Test;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.TestClass;
import org.slf4j.Logger;

//~--- JDK imports ------------------------------------------------------------


/**
 * The <code>InjectableTest</code> class implements the JUnit runner that provides support for
 * JEE 6 Contexts and Dependency Injection (CDI) using Weld.
 *
 * @author Marcus Portmann
 */
public class InjectionJUnit4ClassRunner
  extends BlockJUnit4ClassRunner
{
  /**
   * Constructs a new <code>InjectableTest</code>.
   *
   * @param testClass the JUnit test class to run
   *
   * @throws InitializationError
   */
  public InjectionJUnit4ClassRunner(Class<?> testClass) throws InitializationError
  {
    super(testClass);
  }

  /**
   *
   * @param testClass
   * @return
   */
  @Override
  protected TestClass createTestClass(Class<?> testClass)
  {
    return super.createTestClass(testClass);
  }
}

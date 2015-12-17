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

import org.slf4j.Logger;

//~--- JDK imports ------------------------------------------------------------


/**
 * The <code>InjectableTests</code> class provides a base class for JUnit tests that wish to use
 * JEE 6 Contexts and Dependency Injection (CDI).
 *
 * @author Marcus Portmann
 */
public abstract class InjectableTests extends Test
{
  /**
   * Constructs a new <code>InjectableTests</code>.
   */
  public InjectableTests()
  {
    CDIUtil.inject(this);
  }

  /**
   * Returns the logger for the derived JUnit tests class.
   *
   * @return the logger for the derived JUnit tests class
   */
  protected abstract Logger getLogger();
}

/*
 * Copyright 2017 Marcus Portmann
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

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The <code>LoggingTest</code> class.
 *
 * @author Marcus Portmann
 */
public class LoggingTest
{
  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(LoggingTest.class);

  /**
   * The logging test.
   */
  @Test
  public void loggingTest()
  {
    logger.info("Testing 1.. 2.. 3..");
  }
}

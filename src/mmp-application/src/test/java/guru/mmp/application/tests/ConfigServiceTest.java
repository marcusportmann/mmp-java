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

package guru.mmp.application.tests;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.config.IConfigService;
import guru.mmp.common.test.ApplicationJUnit4ClassRunner;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

//~--- JDK imports ------------------------------------------------------------

import javax.inject.Inject;

/**
 * The <code>ConfigServiceTest</code> class contains the implementation of the JUnit
 * tests for the <code>ConfigService</code> class.
 *
 * @author Marcus Portmann
 */
@RunWith(ApplicationJUnit4ClassRunner.class)
public class ConfigServiceTest
{
  private static final String TEST_KEY = ConfigServiceTest.class.getName() + ".TestKey";
  private static final String TEST_VALUE = "TestValue";
  @Inject
  private IConfigService configService;

  /**
   * Test the Config Service.
   *
   * @throws Exception
   */
  @Test
  public void configServiceTest()
    throws Exception
  {
    if (configService.keyExists(TEST_KEY))
    {
      fail("Found the configuration key (" + TEST_KEY + ") that should not exist");
    }

    configService.setValue(TEST_KEY, TEST_VALUE);

    if (!configService.keyExists(TEST_KEY))
    {
      fail("Failed to confirm that the configuration key (" + TEST_KEY + ") exists");
    }

    String value = configService.getString(TEST_KEY);

    assertEquals("The required value (" + TEST_VALUE
        + ") was not retrieved for the configuration key (" + TEST_KEY + ")", TEST_VALUE, value);

    configService.setValue(TEST_KEY, TEST_VALUE + "Updated");

    if (!configService.keyExists(TEST_KEY))
    {
      fail("Failed to confirm that the configuration key (" + TEST_KEY + ") exists");
    }

    value = configService.getString(TEST_KEY);

    assertEquals("The required value (" + TEST_VALUE + "Updated"
      + ") was not retrieved for the configuration key (" + TEST_KEY + ")", TEST_VALUE + "Updated", value);
  }
}

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

import guru.mmp.application.configuration.ConfigurationEntry;
import guru.mmp.application.configuration.IConfigurationService;
import guru.mmp.common.test.ApplicationJUnit4ClassRunner;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;

import javax.inject.Inject;

/**
 * The <code>ConfigurationServiceTest</code> class contains the implementation of the JUnit
 * tests for the <code>ConfigurationService</code> class.
 *
 * @author Marcus Portmann
 */
@RunWith(ApplicationJUnit4ClassRunner.class)
public class ConfigurationServiceTest
{
  private static final String TEST_FILTERED_KEY = ConfigurationServiceTest.class.getName() + ".TestFilteredKey";
  private static final String TEST_STRING_KEY = ConfigurationServiceTest.class.getName() + ".TestStringKey";

  private static final String TEST_VALUE = "TestValue";
  @Inject
  private IConfigurationService configurationService;

  /**
   * Test the <code>String</code> configuration.
   *
   * @throws Exception
   */
  @Test
  public void stringConfigurationTest()
    throws Exception
  {
    if (configurationService.keyExists(TEST_STRING_KEY))
    {
      fail("Found the configuration key (" + TEST_STRING_KEY + ") that should not exist");
    }

    configurationService.setValue(TEST_STRING_KEY, TEST_VALUE);

    if (!configurationService.keyExists(TEST_STRING_KEY))
    {
      fail("Failed to confirm that the configuration key (" + TEST_STRING_KEY + ") exists");
    }

    String value = configurationService.getString(TEST_STRING_KEY);

    assertEquals("The required value (" + TEST_VALUE
        + ") was not retrieved for the configuration key (" + TEST_STRING_KEY + ")", TEST_VALUE, value);

    configurationService.setValue(TEST_STRING_KEY, TEST_VALUE + "Updated");

    if (!configurationService.keyExists(TEST_STRING_KEY))
    {
      fail("Failed to confirm that the configuration key (" + TEST_STRING_KEY + ") exists");
    }

    value = configurationService.getString(TEST_STRING_KEY);

    assertEquals("The required value (" + TEST_VALUE + "Updated"
        + ") was not retrieved for the configuration key (" + TEST_STRING_KEY + ")", TEST_VALUE
        + "Updated", value);
  }


  /**
   * Test the filtered configuration.
   *
   * @throws Exception
   */
  @Test
  public void filteredConfigurationTest()
    throws Exception
  {
    configurationService.setValue(TEST_FILTERED_KEY, TEST_VALUE);

    if (!configurationService.keyExists(TEST_FILTERED_KEY))
    {
      fail("Failed to confirm that the configuration key (" + TEST_FILTERED_KEY + ") exists");
    }

    List<ConfigurationEntry> filteredConfigurationEntries = configurationService.getFilteredConfigurationEntries("testfiltered");

    assertEquals("The required number of filtered configuration values (1) was not retrieved", 1,
      filteredConfigurationEntries.size());

    assertEquals("The required filtered configuration value (" + TEST_VALUE
      + ") was not retrieved", TEST_VALUE, filteredConfigurationEntries.get(0).getValue());
  }


}

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

import guru.mmp.application.configuration.ConfigurationException;
import guru.mmp.application.configuration.ConfigurationNotFoundException;
import guru.mmp.application.configuration.ConfigurationValue;
import guru.mmp.application.configuration.IConfigurationService;
import guru.mmp.application.test.ApplicationClassRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.util.List;

import static org.junit.Assert.*;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>ConfigurationServiceTest</code> class contains the implementation of the JUnit
 * tests for the <code>ConfigurationService</code> class.
 *
 * @author Marcus Portmann
 */
@RunWith(ApplicationClassRunner.class)
public class ConfigurationServiceTest
{
  private static final String TEST_FILTERED_KEY = "TestFilteredKey";
  private static final String TEST_STRING_KEY = "TestStringKey";
  private static final String TEST_INTEGER_KEY = "TestIntegerKey";
  private static final String TEST_LONG_KEY = "TestLongKey";
  private static final String TEST_DOUBLE_KEY = "TestDoubleKey";
  private static final String TEST_BINARY_KEY = "TestBinaryKey";
  private static final String TEST_STRING_VALUE = "TestStringValue";
  private static final String TEST_DESCRIPTION = "Test Description";
  private static final Integer TEST_INTEGER_VALUE = 1234;
  private static final Long TEST_LONG_VALUE = 4321L;
  private static final Double TEST_DOUBLE_VALUE = 1234.4321;
  private static final byte[] TEST_BINARY_VALUE = "TestBinaryValue".getBytes();
  private static final byte[] TEST_BINARY_UPDATED_VALUE = "TestBinaryUpdatedValue".getBytes();
  private static final String TEST_BOOLEAN_KEY = "TestBooleanKey";
  private static final boolean TEST_BOOLEAN_VALUE = true;
  @Inject
  private IConfigurationService configurationService;

  /**
   * Test the <code>Binary</code> configuration.
   */
  @Test
  public void binaryConfigurationTest()
    throws ConfigurationException, ConfigurationNotFoundException
  {
    if (configurationService.keyExists(TEST_BINARY_KEY))
    {
      fail("Found the Binary configuration key (" + TEST_BINARY_KEY + ") that should not exist");
    }

    configurationService.setValue(TEST_BINARY_KEY, TEST_BINARY_VALUE, TEST_DESCRIPTION);

    if (!configurationService.keyExists(TEST_BINARY_KEY))
    {
      fail("Failed to confirm that the Binary configuration key (" + TEST_BINARY_KEY + ") exists");
    }

    byte[] value = configurationService.getBinary(TEST_BINARY_KEY);

    assertArrayEquals("The required Binary value was not retrieved for the configuration key ("
        + TEST_BINARY_KEY + ")", TEST_BINARY_VALUE, value);

    configurationService.setValue(TEST_BINARY_KEY, TEST_BINARY_UPDATED_VALUE, TEST_DESCRIPTION
        + " Updated");

    if (!configurationService.keyExists(TEST_BINARY_KEY))
    {
      fail("Failed to confirm that the Binary configuration key (" + TEST_BINARY_KEY + ") exists");
    }

    value = configurationService.getBinary(TEST_BINARY_KEY);

    assertArrayEquals(
        "The required updated Binary value was not retrieved for the configuration key ("
        + TEST_BINARY_KEY + ")", TEST_BINARY_UPDATED_VALUE, value);

    value = configurationService.getBinary(TEST_BINARY_KEY, new byte[0]);

    assertArrayEquals(
        "The required updated Binary value was not retrieved for the configuration key ("
        + TEST_BINARY_KEY + ")", TEST_BINARY_UPDATED_VALUE, value);

  }

  /**
   * Test the <code>Boolean</code> configuration.
   */
  @Test
  public void booleanConfigurationTest()
    throws ConfigurationException, ConfigurationNotFoundException
  {
    if (configurationService.keyExists(TEST_BOOLEAN_KEY))
    {
      fail("Found the Boolean configuration key (" + TEST_BOOLEAN_KEY + ") that should not exist");
    }

    configurationService.setValue(TEST_BOOLEAN_KEY, TEST_BOOLEAN_VALUE, TEST_DESCRIPTION);

    if (!configurationService.keyExists(TEST_BOOLEAN_KEY))
    {
      fail("Failed to confirm that the Boolean configuration key (" + TEST_BOOLEAN_KEY
          + ") exists");
    }

    boolean value = configurationService.getBoolean(TEST_BOOLEAN_KEY);

    assertEquals("The required Boolean value was not retrieved for the configuration key ("
        + TEST_BOOLEAN_KEY + ")", TEST_BOOLEAN_VALUE, value);

    boolean booleanValue = configurationService.getBoolean(TEST_BOOLEAN_KEY, false);

    assertEquals("The required double value was not retrieved for the configuration key ("
        + booleanValue + ")", TEST_BOOLEAN_VALUE, booleanValue);

    configurationService.setValue(TEST_BOOLEAN_KEY, false, TEST_DESCRIPTION + " Updated");

    if (!configurationService.keyExists(TEST_BOOLEAN_KEY))
    {
      fail("Failed to confirm that the Boolean configuration key (" + TEST_BOOLEAN_KEY
          + ") exists");
    }

    value = configurationService.getBoolean(TEST_BOOLEAN_KEY);

    assertEquals("The required updated Boolean value was not retrieved for the configuration key ("
        + TEST_BOOLEAN_KEY + ")", false, value);
  }

  /**
   * Test the <code>Double</code> configuration.
   */
  @Test
  public void doubleConfigurationTest()
    throws ConfigurationException, ConfigurationNotFoundException
  {
    if (configurationService.keyExists(TEST_DOUBLE_KEY))
    {
      fail("Found the Double configuration key (" + TEST_DOUBLE_KEY + ") that should not exist");
    }

    configurationService.setValue(TEST_DOUBLE_KEY, TEST_DOUBLE_VALUE, TEST_DESCRIPTION);

    if (!configurationService.keyExists(TEST_DOUBLE_KEY))
    {
      fail("Failed to confirm that the Double configuration key (" + TEST_DOUBLE_KEY + ") exists");
    }

    Double value = configurationService.getDouble(TEST_DOUBLE_KEY);

    assertEquals("The required Double value was not retrieved for the configuration key ("
        + TEST_DOUBLE_KEY + ")", TEST_DOUBLE_VALUE, value, 0.0);

    double doubleValue = configurationService.getDouble(TEST_DOUBLE_KEY, 666.666);

    assertEquals("The required double value was not retrieved for the configuration key ("
        + doubleValue + ")", TEST_DOUBLE_VALUE, doubleValue, 0.0);

    configurationService.setValue(TEST_DOUBLE_KEY, TEST_DOUBLE_VALUE + 1.1, TEST_DESCRIPTION
        + " Updated");

    if (!configurationService.keyExists(TEST_DOUBLE_KEY))
    {
      fail("Failed to confirm that the Double configuration key (" + TEST_DOUBLE_KEY + ") exists");
    }

    value = configurationService.getDouble(TEST_DOUBLE_KEY);

    assertEquals("The required updated Double value was not retrieved for the configuration key ("
        + TEST_DOUBLE_KEY + ")", TEST_DOUBLE_VALUE + 1.1, value, 0.0);
  }

  /**
   * Test the filtered configuration.
   */
  @Test
  public void filteredConfigurationTest()
    throws ConfigurationException, ConfigurationNotFoundException
  {
    configurationService.setValue(TEST_FILTERED_KEY, TEST_STRING_VALUE, TEST_DESCRIPTION);

    if (!configurationService.keyExists(TEST_FILTERED_KEY))
    {
      fail("Failed to confirm that the configuration key (" + TEST_FILTERED_KEY + ") exists");
    }

    List<ConfigurationValue> filteredConfigurationEntries =
        configurationService.getFilteredConfigurationValues("testfiltered");

    assertEquals("The required number of filtered configuration values (1) was not retrieved", 1,
        filteredConfigurationEntries.size());

    assertEquals("The required filtered configuration value (" + TEST_STRING_VALUE
        + ") was not retrieved", TEST_STRING_VALUE, filteredConfigurationEntries.get(0).getValue());
  }

  /**
   * Test the <code>Integer</code> configuration.
   */
  @Test
  public void integerConfigurationTest()
    throws ConfigurationException, ConfigurationNotFoundException
  {
    if (configurationService.keyExists(TEST_INTEGER_KEY))
    {
      fail("Found the Integer configuration key (" + TEST_INTEGER_KEY + ") that should not exist");
    }

    configurationService.setValue(TEST_INTEGER_KEY, TEST_INTEGER_VALUE, TEST_DESCRIPTION);

    if (!configurationService.keyExists(TEST_INTEGER_KEY))
    {
      fail("Failed to confirm that the Integer configuration key (" + TEST_INTEGER_KEY
          + ") exists");
    }

    Integer value = configurationService.getInteger(TEST_INTEGER_KEY);

    assertEquals("The required Integer value was not retrieved for the Integer configuration key ("
        + TEST_INTEGER_KEY + ")", TEST_INTEGER_VALUE, value);

    int integerValue = configurationService.getInteger(TEST_INTEGER_KEY, 666);

    assertEquals("The required integer value was not retrieved for the Integer configuration key ("
        + TEST_INTEGER_KEY + ")", (int) TEST_INTEGER_VALUE, integerValue);

    configurationService.setValue(TEST_INTEGER_KEY, TEST_INTEGER_VALUE + 1, TEST_DESCRIPTION
        + " Updated");

    if (!configurationService.keyExists(TEST_INTEGER_KEY))
    {
      fail("Failed to confirm that the Integer configuration key (" + TEST_INTEGER_KEY
          + ") exists");
    }

    value = configurationService.getInteger(TEST_INTEGER_KEY);

    assertEquals("The required updated Integer value was not retrieved for the configuration key ("
        + TEST_INTEGER_KEY + ")", TEST_INTEGER_VALUE + 1, value.intValue());
  }

  /**
   * Test the <code>Long</code> configuration.
   */
  @Test
  public void longConfigurationTest()
    throws ConfigurationException, ConfigurationNotFoundException
  {
    if (configurationService.keyExists(TEST_LONG_KEY))
    {
      fail("Found the Long configuration key (" + TEST_LONG_KEY + ") that should not exist");
    }

    configurationService.setValue(TEST_LONG_KEY, TEST_LONG_VALUE, TEST_DESCRIPTION);

    if (!configurationService.keyExists(TEST_LONG_KEY))
    {
      fail("Failed to confirm that the Long configuration key (" + TEST_LONG_KEY + ") exists");
    }

    Long value = configurationService.getLong(TEST_LONG_KEY);

    assertEquals("The required Long value was not retrieved for the Long configuration key ("
        + TEST_LONG_KEY + ")", TEST_LONG_VALUE, value);

    Long longValue = configurationService.getLong(TEST_LONG_KEY, 666);

    assertEquals("The required Long value was not retrieved for the Long configuration key ("
        + TEST_LONG_KEY + ")", TEST_LONG_VALUE, longValue);

    configurationService.setValue(TEST_LONG_KEY, TEST_LONG_VALUE + 1, TEST_DESCRIPTION
        + " Updated");

    if (!configurationService.keyExists(TEST_LONG_KEY))
    {
      fail("Failed to confirm that the Long configuration key (" + TEST_LONG_KEY + ") exists");
    }

    value = configurationService.getLong(TEST_LONG_KEY);

    assertEquals("The required updated Long value was not retrieved for the configuration key ("
        + TEST_LONG_KEY + ")", TEST_LONG_VALUE + 1L, value.longValue());
  }

  /**
   * Test the <code>String</code> configuration.
   */
  @Test
  public void stringConfigurationTest()
    throws ConfigurationException, ConfigurationNotFoundException
  {
    if (configurationService.keyExists(TEST_STRING_KEY))
    {
      fail("Found the String configuration key (" + TEST_STRING_KEY + ") that should not exist");
    }

    configurationService.setValue(TEST_STRING_KEY, TEST_STRING_VALUE, TEST_DESCRIPTION);

    if (!configurationService.keyExists(TEST_STRING_KEY))
    {
      fail("Failed to confirm that the String configuration key (" + TEST_STRING_KEY + ") exists");
    }

    String value = configurationService.getString(TEST_STRING_KEY);

    assertEquals("The required String value was not retrieved for the String configuration key ("
        + TEST_STRING_KEY + ")", TEST_STRING_VALUE, value);

    configurationService.setValue(TEST_STRING_KEY, TEST_STRING_VALUE + "Updated", TEST_DESCRIPTION
        + " Updated");

    if (!configurationService.keyExists(TEST_STRING_KEY))
    {
      fail("Failed to confirm that the String configuration key (" + TEST_STRING_KEY + ") exists");
    }

    value = configurationService.getString(TEST_STRING_KEY);

    assertEquals("The required updated String value was not retrieved for the configuration key ("
        + TEST_STRING_KEY + ")", TEST_STRING_VALUE + "Updated", value);
  }
}

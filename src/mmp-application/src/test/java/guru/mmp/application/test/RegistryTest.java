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

import guru.mmp.application.registry.IRegistry;
import guru.mmp.application.registry.Registry;
import guru.mmp.application.registry.RegistryException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;

import java.math.BigDecimal;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

/**
 * The <code>RegistryTests</code> class contains the implementation of the JUnit tests for
 * the <code>Registry</code> class.
 *
 * @author Marcus Portmann
 */
public class RegistryTest extends H2DatabaseTest
{
  private DataSource dataSource;
  private IRegistry registry;

  /**
   * Test the management of binary configuration values by the <code>Registry</code>.
   *
   * @throws RegistryException
   */
  @Test
  public void binaryConfigurationTest()
    throws RegistryException
  {
    byte[] inputBinaryValue =
    {
      1, 2, 3, 4, 5, 6
    };
    byte[] anotherBinaryValue = { 4, 3, 2, 1 };

    // Set the value
    registry.setBinaryValue("/Section1/Section1.1", "BinaryName", inputBinaryValue);

    // Check whether the value was set
    assertEquals("Binary value was not set in the registry", true,
        registry.binaryValueExists("/Section1/Section1.1", "BinaryName"));

    // Retrieve the value
    byte[] value = registry.getBinaryValue("/Section1/Section1.1", "BinaryName",
      anotherBinaryValue);

    // Check whether the retrieved value is correct
    assertEquals("Binary value retrieved from the registry does not match the value set",
        inputBinaryValue.length, value.length);

    // Remove the value
    registry.removeValue("/Section1/Section1.1", "BinaryName");

    // Check whether the value was removed
    assertEquals("Binary value was not removed from the registry", false,
        registry.binaryValueExists("/Section1/Section1.1", "BinaryName"));
  }

  /**
   * Test the management of decimal configuration values by the <code>Registry</code>.
   *
   * @throws RegistryException
   */
  @Test
  public void decimalConfigurationTest()
    throws RegistryException

  {
    // Set the value
    registry.setDecimalValue("/Section1/Section1.1", "DecimalName",
        new BigDecimal("666.666000000000"));

    // Check whether the value was set
    assertEquals("Decimal value was not set in the registry", true,
        registry.decimalValueExists("/Section1/Section1.1", "DecimalName"));

    // Retrieve the value
    BigDecimal value = registry.getDecimalValue("/Section1/Section1.1", "DecimalName",
      new BigDecimal("777.777000000000"));

    // Check whether the retrieved value is correct
    assertEquals("Decimal value retrieved from the registry does not match the value set",
        new BigDecimal("666.666000000000"), value);

    // Update the value
    registry.setDecimalValue("/Section1/Section1.1", "DecimalName",
      new BigDecimal("888.888000000000"));

    // Retrieve the value
    value = registry.getDecimalValue("/Section1/Section1.1", "DecimalName",
      new BigDecimal("777.777000000000"));

    // Check whether the retrieved value is correct
    assertEquals("Decimal value retrieved from the registry does not match the value set",
      new BigDecimal("888.888000000000"), value);

    // Remove the value
    registry.removeValue("/Section1/Section1.1", "DecimalName");

    // Check whether the value was removed
    assertEquals("Decimal value was not removed from the registry", false,
        registry.decimalValueExists("/Section1/Section1.1", "DecimalName"));
  }

  /**
   * Test the management of integer configuration values by the <code>Registry</code>.
   *
   * @throws RegistryException
   */
  @Test
  public void integerConfigurationTest()
    throws RegistryException

  {
    // Set the value
    registry.setIntegerValue("/Section1/Section1.1", "IntegerName", 666);

    // Check whether the value was set
    assertEquals("Integer value was not set in the registry", true,
        registry.integerValueExists("/Section1/Section1.1", "IntegerName"));

    // Retrieve the value
    int value = registry.getIntegerValue("/Section1/Section1.1", "IntegerName", 777);

    // Check whether the retrieved value is correct
    assertEquals("Integer value retrieved from the registry does not match the value set", 666,
        value);

    // Update the value
    registry.setIntegerValue("/Section1/Section1.1", "IntegerName", 888);

    // Retrieve the value
    value = registry.getIntegerValue("/Section1/Section1.1", "IntegerName", 777);

    // Check whether the retrieved value is correct
    assertEquals("Integer value retrieved from the registry does not match the value set", 888,
      value);

    // Remove the value
    registry.removeValue("/Section1/Section1.1", "IntegerName");

    // Check whether the value was removed
    assertEquals("Integer value was not removed from the registry", false,
        registry.integerValueExists("/Section1/Section1.1", "IntegerName"));
  }

  /**
   * This method is executed by the JUnit test infrastructure before a JUnit test is executed.
   * It is responsible for initialising the resources used by the tests e.g. the in-memory
   * database.
   *
   * @throws IOException
   * @throws SQLException
   */
  @Before
  public void setup()
    throws IOException, SQLException
  {
    // Initialise the in-memory database that will be used when executing a test
    dataSource = initDatabase("RegistryTest", "guru/mmp/application/persistence/ApplicationH2.sql",
        false);

    // Create the Registry instance
    registry = new Registry(dataSource, "/RegistryTest");
  }

  /**
   * Test the management of string configuration values by the <code>Registry</code>.
   *
   * @throws RegistryException
   */
  @Test
  public void stringConfigurationTest()
    throws RegistryException
  {
    // Set the value
    registry.setStringValue("/Section1/Section1.1", "StringName", "StringValue");

    // Check whether the value was set
    assertEquals("String value was not set in the registry", true,
        registry.stringValueExists("/Section1/Section1.1", "StringName"));

    // Retrieve the value
    String value = registry.getStringValue("/Section1/Section1.1", "StringName",
      "InvalidStringValue");

    // Check whether the retrieved value is correct
    assertEquals("String value retrieved from the registry does not match the value set",
        "StringValue", value);

    // Update the value
    registry.setStringValue("/Section1/Section1.1", "StringName", "StringValue2");

    value = registry.getStringValue("/Section1/Section1.1", "StringName",
      "InvalidStringValue");

    // Check whether the retrieved value is correct
    assertEquals("String value retrieved from the registry does not match the value set",
      "StringValue2", value);

    // Remove the value
    registry.removeValue("/Section1/Section1.1", "StringName");

    // Check whether the value was removed
    assertEquals("String value was not removed from the registry", false,
        registry.stringValueExists("/Section1/Section1.1", "StringName"));
  }

  /**
   * This method is executed by the JUnit test infrastructure after each JUnit test has been
   * executed. It is responsible for cleaning up the resources used by the tests.
   *
   * @throws SQLException
   */
  @After
  public void tearDown()
    throws SQLException
  {
    if (dataSource != null)
    {
      try (Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement())
      {
        statement.execute("SHUTDOWN");
      }
      catch (Throwable ignored) {}

      dataSource = null;
    }

    registry = null;
  }
}

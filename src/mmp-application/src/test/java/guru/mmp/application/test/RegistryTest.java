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

package guru.mmp.application.test;

import guru.mmp.application.registry.IRegistry;
import guru.mmp.application.registry.RegistryException;
import guru.mmp.common.crypto.CryptoUtils;
import guru.mmp.common.test.ApplicationJUnit4ClassRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

/**
 * The <code>RegistryTests</code> class contains the implementation of the JUnit tests for
 * the <code>Registry</code> class.
 *
 * @author Marcus Portmann
 */
@RunWith(ApplicationJUnit4ClassRunner.class)
public class RegistryTest
{
  private byte[] encryptionIV = CryptoUtils.createRandomEncryptionIV(CryptoUtils.AES_BLOCK_SIZE);

  private byte[] encryptionKey = CryptoUtils.getRandomAESKey();

  @Inject
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
    byte[] inputBinaryValue = {1, 2, 3, 4, 5, 6};
    byte[] anotherBinaryValue = {4, 3, 2, 1};

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

    // Set the encrypted value
    registry.setBinaryValue("/Section1/Section1.1", "EncryptedBinaryName", inputBinaryValue,
      encryptionKey, encryptionIV);

    // Check whether the encrypted value was set
    assertEquals("Encrypted binary value was not set in the registry", true,
      registry.binaryValueExists("/Section1/Section1.1", "EncryptedBinaryName"));

    // Retrieve the encrypted value
    byte[] encryptedValue = registry.getBinaryValue("/Section1/Section1.1", "EncryptedBinaryName",
      anotherBinaryValue, encryptionKey, encryptionIV);

    // Check whether the retrieved encrypted value is correct
    assertEquals("Encrypted binary value retrieved from the registry does not match the value set",
      inputBinaryValue.length, encryptedValue.length);

    // Remove the encrypted value
    registry.removeValue("/Section1/Section1.1", "EncryptedBinaryName");

    // Check whether the encrypted value was removed
    assertEquals("Encrypted binary value was not removed from the registry", false,
      registry.binaryValueExists("/Section1/Section1.1", "EncryptedBinaryName"));
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
    registry.setDecimalValue("/Section2/Section2.1", "DecimalName",
      new BigDecimal("666.666000000000"));

    // Check whether the value was set
    assertEquals("Decimal value was not set in the registry", true,
      registry.decimalValueExists("/Section2/Section2.1", "DecimalName"));

    // Retrieve the value
    BigDecimal value = registry.getDecimalValue("/Section2/Section2.1", "DecimalName",
      new BigDecimal("777.777000000000"));

    // Check whether the retrieved value is correct
    assertEquals("Decimal value retrieved from the registry does not match the value set",
      new BigDecimal("666.666000000000"), value);

    // Update the value
    registry.setDecimalValue("/Section2/Section2.1", "DecimalName",
      new BigDecimal("888.888000000000"));

    // Retrieve the value
    value = registry.getDecimalValue("/Section2/Section2.1", "DecimalName",
      new BigDecimal("777.777000000000"));

    // Check whether the retrieved value is correct
    assertEquals("Decimal value retrieved from the registry does not match the value set",
      new BigDecimal("888.888000000000"), value);

    // Remove the value
    registry.removeValue("/Section2/Section2.1", "DecimalName");

    // Check whether the value was removed
    assertEquals("Decimal value was not removed from the registry", false,
      registry.decimalValueExists("/Section2/Section2.1", "DecimalName"));
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
    registry.setIntegerValue("/Section3/Section3.1", "IntegerName", 666);

    // Check whether the value was set
    assertEquals("Integer value was not set in the registry", true,
      registry.integerValueExists("/Section3/Section3.1", "IntegerName"));

    // Retrieve the value
    int value = registry.getIntegerValue("/Section3/Section3.1", "IntegerName", 777);

    // Check whether the retrieved value is correct
    assertEquals("Integer value retrieved from the registry does not match the value set", 666,
      value);

    // Update the value
    registry.setIntegerValue("/Section3/Section3.1", "IntegerName", 888);

    // Retrieve the value
    value = registry.getIntegerValue("/Section3/Section3.1", "IntegerName", 777);

    // Check whether the retrieved value is correct
    assertEquals("Integer value retrieved from the registry does not match the value set", 888,
      value);

    // Remove the value
    registry.removeValue("/Section3/Section3.1", "IntegerName");

    // Check whether the value was removed
    assertEquals("Integer value was not removed from the registry", false,
      registry.integerValueExists("/Section3/Section3.1", "IntegerName"));
  }

  /**
   * Test the management of string configuration values at different levels of the configuration
   * hierarchy by the <code>Registry</code>.
   *
   * @throws RegistryException
   */
  @Test
  public void multiLevelStringConfigurationTest()
    throws RegistryException
  {
    // Set the value
    registry.setStringValue("Level1", "Level1Name", "Level11Value");

    // Check whether the value was set
    assertEquals("String value retrieved from the registry does not match the value set",
      "Level11Value", registry.getStringValue("Level1", "Level1Name", "InvalidStringValue"));
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
    registry.setStringValue("/Section4/Section4.1", "StringName", "StringValue");

    // Check whether the value was set
    assertEquals("String value was not set in the registry", true,
      registry.stringValueExists("/Section4/Section4.1", "StringName"));

    // Retrieve the value
    String value = registry.getStringValue("/Section4/Section4.1", "StringName",
      "InvalidStringValue");

    // Check whether the retrieved value is correct
    assertEquals("String value retrieved from the registry does not match the value set",
      "StringValue", value);

    // Update the value
    registry.setStringValue("/Section4/Section4.1", "StringName", "StringValue2");

    value = registry.getStringValue("/Section4/Section4.1", "StringName", "InvalidStringValue");

    // Check whether the retrieved value is correct
    assertEquals("String value retrieved from the registry does not match the value set",
      "StringValue2", value);

    // Remove the value
    registry.removeValue("/Section4/Section4.1", "StringName");

    // Check whether the value was removed
    assertEquals("String value was not removed from the registry", false,
      registry.stringValueExists("/Section4/Section4.1", "StringName"));

    // Set the encrypted value
    registry.setStringValue("/Section4/Section4.1", "EncryptedStringName", "StringValue",
      encryptionKey, encryptionIV);

    // Check whether the encrypted value was set
    assertEquals("Encrypted string value was not set in the registry", true,
      registry.stringValueExists("/Section4/Section4.1", "EncryptedStringName"));

    // Retrieve the encrypted value
    String encryptedValue = registry.getStringValue("/Section4/Section4.1", "EncryptedStringName",
      "InvalidStringValue", encryptionKey, encryptionIV);

    // Check whether the retrieved encrypted value is correct
    assertEquals("Encrypted string value retrieved from the registry does not match the value set",
      "StringValue", encryptedValue);

    // Update the encrypted value
    registry.setStringValue("/Section4/Section4.1", "EncryptedStringName", "StringValue2",
      encryptionKey, encryptionIV);

    encryptedValue = registry.getStringValue("/Section4/Section4.1", "EncryptedStringName",
      "InvalidStringValue", encryptionKey, encryptionIV);

    // Check whether the retrieved encrypted value is correct
    assertEquals("Encrypted string value retrieved from the registry does not match the value set",
      "StringValue2", encryptedValue);

    // Remove the encrypted value
    registry.removeValue("/Section4/Section4.1", "EncryptedStringName");

    // Check whether the encrypted value was removed
    assertEquals("Encrypted string value was not removed from the registry", false,
      registry.stringValueExists("/Section4/Section4.1", "EncryptedStringName"));
  }
}

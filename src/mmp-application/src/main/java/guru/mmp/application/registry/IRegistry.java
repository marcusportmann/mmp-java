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

package guru.mmp.application.registry;

import javax.sql.DataSource;
import java.math.BigDecimal;

/**
 * The <code>IRegistry</code> interface defines the functionality provided by a "Registry"
 * implementation, which is a hierarchical store of configuration information used by applications
 * and services.
 * <p/>
 * The configuration information is referenced through "paths" which use the "/" character as a
 * seperator e.g. /ConfigSection1/ConfigSection1.1/ConfigSection1.1/ConfigValueName
 *
 * @author Marcus Portmann
 */
public interface IRegistry
{
  /**
   * Check whether the binary value with the specified path exists.
   *
   * @param path the path for the registry key e.g. /XYZApp/Section/SubSection
   * @param name the name of the registry value
   *
   * @return <code>true</code> if the binary value exists or <code>false</code> otherwise
   *
   * @throws RegistryException
   */
  boolean binaryValueExists(String path, String name)
    throws RegistryException;

  /**
   * Check whether the decimal value with the specified path exists.
   *
   * @param path the path for the registry key e.g. /XYZApp/Section/SubSection
   * @param name the name of the registry value
   *
   * @return <code>true</code> if the decimal value exists or <code>false</code> otherwise
   *
   * @throws RegistryException
   */
  boolean decimalValueExists(String path, String name)
    throws RegistryException;

  /**
   * Retrieve the binary value with the specified name under the key with the specified path.
   *
   * @param path         the path for the registry key e.g. /XYZApp/Section/SubSection
   * @param name         the name of the registry value
   * @param defaultValue the default value to use if the specified binary value cannot be found
   *
   * @return the binary value or the default value if the value could not be found
   *
   * @throws RegistryException
   */
  byte[] getBinaryValue(String path, String name, byte[] defaultValue)
    throws RegistryException;

  /**
   * Retrieve the binary value with the specified name under the key with the specified path.
   *
   * @param path          the path for the Registry key e.g. /XYZApp/Section/SubSection
   * @param name          the name of the Registry value
   * @param defaultValue  the default value to use if the specified binary value cannot be found
   * @param encryptionKey the encryption key to use to decrypt the value
   * @param encryptionIV  the encryption initialisation vector
   *
   * @return the binary value or the default value if the value could not be found
   *
   * @throws RegistryException
   */
  byte[] getBinaryValue(
    String path, String name, byte[] defaultValue, byte[] encryptionKey, byte[] encryptionIV)
    throws RegistryException;

  /**
   * Returns the data source for the registry.
   *
   * @return the data source for the registry;
   */
  DataSource getDataSource();

  /**
   * Retrieve the decimal value with the specified name under the key with the specified path.
   *
   * @param path         the path for the registry key e.g. /XYZApp/Section/SubSection
   * @param name         the name of the registry value
   * @param defaultValue the default value to use if the specified decimal value cannot be found
   *
   * @return the decimal value or the default value if the value could not be found
   *
   * @throws RegistryException
   */
  BigDecimal getDecimalValue(String path, String name, BigDecimal defaultValue)
    throws RegistryException;

  /**
   * Retrieve the integer value with the specified name under the key with the specified path.
   *
   * @param path         the path for the registry key e.g. /XYZApp/Section/SubSection
   * @param name         the name of the registry value
   * @param defaultValue the default value to use if the specified integer value cannot be found
   *
   * @return the integer value or the default value if the value could not be found
   *
   * @throws RegistryException
   */
  int getIntegerValue(String path, String name, int defaultValue)
    throws RegistryException;

  /**
   * Retrieve the string value with the specified name under the key with the specified path.
   *
   * @param path         the path for the registry key e.g. /XYZApp/Section/SubSection
   * @param name         the name of the registry value
   * @param defaultValue the default value to use if the specified string value cannot be found
   *
   * @return the string value or the default value if the value could not be found
   *
   * @throws RegistryException
   */
  String getStringValue(String path, String name, String defaultValue)
    throws RegistryException;

  /**
   * Retrieve the string value with the specified name under the key with the specified path.
   *
   * @param path          the path for the Registry key e.g. /XYZApp/Section/SubSection
   * @param name          the name of the Registry value
   * @param defaultValue  the default value to use if the specified string value cannot be found
   * @param encryptionKey the encryption key to use to decrypt the value
   * @param encryptionIV  the encryption initialisation vector
   *
   * @return the string value or the default value if the value could not be found
   *
   * @throws RegistryException
   */
  String getStringValue(
    String path, String name, String defaultValue, byte[] encryptionKey, byte[] encryptionIV)
    throws RegistryException;

  /**
   * Check whether the integer value with the specified path exists.
   *
   * @param path the path for the registry key e.g. /XYZApp/Section/SubSection
   * @param name the name of the registry value
   *
   * @return <code>true</code> if the integer value exists or <code>false</code> otherwise
   *
   * @throws RegistryException
   */
  boolean integerValueExists(String path, String name)
    throws RegistryException;

  /**
   * Remove the value with the specified name under the key with the specified path.
   *
   * @param path the path for the registry key e.g. /XYZApp/Section/SubSection
   * @param name the name of the value
   *
   * @return <code>true</code> if the value with the specified name was found and removed or
   * <code>false</code> otherwise
   *
   * @throws RegistryException
   */
  boolean removeValue(String path, String name)
    throws RegistryException;

  /**
   * Set the binary value with the specified name under the key with the specified path to the
   * specified value.
   * <p/>
   * This method will overwrite the value if it already exists.
   *
   * @param path  the path for the registry key e.g. /XYZApp/Section/SubSection
   * @param name  the name of the registry value
   * @param value the new binary value
   *
   * @throws RegistryException
   */
  void setBinaryValue(String path, String name, byte[] value)
    throws RegistryException;

  /**
   * Set the binary value with the specified name under the key with the specified path to the
   * specified value.
   * <p/>
   * This method will overwrite the value if it already exists.
   *
   * @param path          the path for the Registry key e.g. /XYZApp/Section/SubSection
   * @param name          the name of the Registry value
   * @param value         the new binary value
   * @param encryptionKey the encryption key to use to encrypt the value
   * @param encryptionIV  the encryption initialisation vector
   *
   * @throws RegistryException
   */
  void setBinaryValue(
    String path, String name, byte[] value, byte[] encryptionKey, byte[] encryptionIV)
    throws RegistryException;

  /**
   * Set the decimal value with the specified name under the key with the specified path to the
   * specified value.
   * <p/>
   * This method will overwrite the value if it already exists.
   *
   * @param path  the path for the registry key e.g. /XYZApp/Section/SubSection
   * @param name  the name of the registry value
   * @param value the new decimal value
   *
   * @throws RegistryException
   */
  void setDecimalValue(String path, String name, BigDecimal value)
    throws RegistryException;

  /**
   * Set the integer value with the specified name under the key with the specified path to the
   * specified value.
   * <p/>
   * This method will overwrite the value if it already exists.
   *
   * @param path  the path for the registry key e.g. /XYZApp/Section/SubSection
   * @param name  the name of the registry value
   * @param value the new integer value
   *
   * @throws RegistryException
   */
  void setIntegerValue(String path, String name, int value)
    throws RegistryException;

  /**
   * Set the string value with the specified name under the key with the specified path to the
   * specified value.
   * <p/>
   * This method will overwrite the value if it already exists.
   *
   * @param path  the path for the registry key e.g. /XYZApp/Section/SubSection
   * @param name  the name of the registry value
   * @param value the new string value
   *
   * @throws RegistryException
   */
  void setStringValue(String path, String name, String value)
    throws RegistryException;

  /**
   * Set the string value with the specified name under the key with the specified path to the
   * specified value.
   * <p/>
   * This method will overwrite the value if it already exists.
   *
   * @param path          the path for the Registry key e.g. /XYZApp/Section/SubSection
   * @param name          the name of the Registry value
   * @param value         the new string value
   * @param encryptionKey the encryption key to use to decrypt the value
   * @param encryptionIV  the encryption initialisation vector
   *
   * @throws RegistryException
   */
  void setStringValue(
    String path, String name, String value, byte[] encryptionKey, byte[] encryptionIV)
    throws RegistryException;

  /**
   * Check whether the string value with the specified path exists.
   *
   * @param path the path for the registry key e.g. /XYZApp/Section/SubSection
   * @param name the name of the registry value
   *
   * @return <code>true</code> if the string value exists or <code>false</code> otherwise
   *
   * @throws RegistryException
   */
  boolean stringValueExists(String path, String name)
    throws RegistryException;
}

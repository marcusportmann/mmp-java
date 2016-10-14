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

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.common.crypto.CryptoUtils;
import guru.mmp.common.persistence.DAOUtil;
import guru.mmp.common.persistence.DataAccessObject;
import guru.mmp.common.persistence.IDGenerator;
import guru.mmp.common.persistence.TransactionManager;
import guru.mmp.common.util.Base64;
import guru.mmp.common.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>Registry</code> class provides a "Registry" implementation which uses a database
 * to store the hierarchical configuration information for applications and services.
 *
 * @author Marcus Portmann
 */
@ApplicationScoped
@Default
public class Registry
  implements IRegistry
{
  /**
   * The path seperator.
   */
  private static final String PATH_SEPERATOR = "/";

  /**
   * Prefix used to identify encrypted and base64 encoded values.
   */
  private static final String ENCRYPTION_PREFIX = "{ENCRYPTED}";

  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(Registry.class);
  private static ThreadLocal<Cipher> threadLocalCipher = new ThreadLocal<Cipher>()
  {
    @Override
    protected Cipher initialValue()
    {
      try
      {
        return Cipher.getInstance(CryptoUtils.AES_TRANSFORMATION_NAME);
      }
      catch (Throwable e)
      {
        throw new RuntimeException(String.format("Failed to initialise the %s cipher", CryptoUtils
            .AES_TRANSFORMATION_NAME), e);
      }
    }
  };
  private String createKeySQL;
  private DataSource dataSource;
  private String getBinaryValueSQL;
  private String getDecimalValueSQL;
  private String getIntegerValueSQL;
  private String getKeyIdNoParentSQL;
  private String getKeyIdWithParentSQL;
  private String getStringValueSQL;
  private String getValueTypeSQL;
  private String removeValueSQL;
  private String setBinaryValueInsertSQL;
  private String setBinaryValueUpdateSQL;
  private String setDecimalValueInsertSQL;
  private String setDecimalValueUpdateSQL;
  private String setIntegerValueInsertSQL;
  private String setIntegerValueUpdateSQL;
  private String setStringValueInsertSQL;
  private String setStringValueUpdateSQL;

  /**
   * Constructs a new <code>Registry</code>.
   */
  public Registry() {}

  /**
   * Check whether the binary value with the specified path exists.
   *
   * @param path the path for the Registry key e.g. /XYZApp/Section/SubSection
   * @param name the name of the Registry value
   *
   * @return <code>true</code> if the binary value exists or <code>false</code> otherwise
   */
  public boolean binaryValueExists(String path, String name)
    throws RegistryException
  {
    // Check the parameters
    if (StringUtil.isNullOrEmpty(path))
    {
      throw new RegistryException(String.format(
          "Failed to check if the binary value (%s) exists under the key (%s): "
          + "The specified key path is invalid", name, path));
    }

    if (StringUtil.isNullOrEmpty(name))
    {
      throw new RegistryException(String.format(
          "Failed to check if the binary value (%s) exists under the key (%s): "
          + "The specified name is invalid", name, path));
    }

    path = getActualPath(path);

    // Store the value in the database
    try (Connection connection = getConnection();
      PreparedStatement statement = connection.prepareStatement(getBinaryValueSQL))
    {
      // Retrieve the ID of the Registry key with the specified path
      String keyId = getKeyId(connection, path, false);

      if (keyId == null)
      {
        return false;
      }

      statement.setString(1, keyId);
      statement.setString(2, name);

      try (ResultSet rs = statement.executeQuery())
      {
        if (rs.next())
        {
          // Retrieve the type of the existing value if one exists
          int existingType = rs.getInt(1);

          if ((existingType != RegistryValueType.NONE.getCode())
              && (existingType != RegistryValueType.BINARY.getCode()))
          {
            throw new RegistryException(String.format(
                "Failed to check if the binary value (%s) exists under the key (%s): "
                + "A value with the specified name exists with the incorrect type (%d)", name,
                path, existingType));
          }

          return true;
        }
        else
        {
          return false;
        }
      }
    }
    catch (Throwable e)
    {
      throw new RegistryException(String.format(
          "Failed to check if the binary value (%s) exists under the key (%s)", name, path), e);
    }
  }

  /**
   * Check whether the decimal value with the specified path exists.
   *
   * @param path the path for the Registry key e.g. /XYZApp/Section/SubSection
   * @param name the name of the Registry value
   *
   * @return <code>true</code> if the decimal value exists or <code>false</code> otherwise
   */
  public boolean decimalValueExists(String path, String name)
    throws RegistryException
  {
    // Check the parameters
    if (StringUtil.isNullOrEmpty(path))
    {
      throw new RegistryException(String.format(
          "Failed to check if the decimal value (%s) exists under the key (%s): "
          + "The specified key path is invalid", name, path));
    }

    if (StringUtil.isNullOrEmpty(name))
    {
      throw new RegistryException(String.format(
          "Failed to check if the decimal value (%s) exists under the key (%s): "
          + "The specified name is invalid", name, path));
    }

    path = getActualPath(path);

    // Store the value in the database
    try (Connection connection = getConnection();
      PreparedStatement statement = connection.prepareStatement(getDecimalValueSQL))
    {
      // Retrieve the ID of the Registry key with the specified path
      String keyId = getKeyId(connection, path, false);

      if (keyId == null)
      {
        return false;
      }

      statement.setString(1, keyId);
      statement.setString(2, name);

      try (ResultSet rs = statement.executeQuery())
      {
        if (rs.next())
        {
          // Retrieve the type of the existing value if one exists
          int existingType = rs.getInt(1);

          if ((existingType != RegistryValueType.NONE.getCode())
              && (existingType != RegistryValueType.DECIMAL.getCode()))
          {
            throw new RegistryException(String.format(
                "Failed to check if the decimal value (%s) exists under the key (%s): "
                + "A value with the specified name exists with the incorrect type (%d)", name,
                path, existingType));
          }

          return true;
        }
        else
        {
          return false;
        }
      }
    }
    catch (Throwable e)
    {
      throw new RegistryException(String.format(
          "Failed to check if the decimal value (%s) exists under the key (%s)", name, path), e);
    }
  }

  /**
   * Retrieve the binary value with the specified name under the key with the specified path.
   *
   * @param path         the path for the Registry key e.g. /XYZApp/Section/SubSection
   * @param name         the name of the Registry value
   * @param defaultValue the default value to use if the specified binary value cannot be found
   *
   * @return the binary value or the default value if the value could not be found
   */
  public byte[] getBinaryValue(String path, String name, byte[] defaultValue)
    throws RegistryException
  {
    return getBinaryValue(path, name, defaultValue, null, null);
  }

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
   */
  public byte[] getBinaryValue(String path, String name, byte[] defaultValue, byte[] encryptionKey,
      byte[] encryptionIV)
    throws RegistryException
  {
    // Check the parameters
    if (StringUtil.isNullOrEmpty(path))
    {
      throw new RegistryException(String.format(
          "Failed to get the binary value (%s) under the key (%s): "
          + "The specified key path is invalid", name, path));
    }

    if (StringUtil.isNullOrEmpty(name))
    {
      throw new RegistryException(String.format(
          "Failed to get the binary value (%s) under the key (%s): "
          + "The specified name is invalid", name, path));
    }

    if ((encryptionKey != null) && (encryptionKey.length != CryptoUtils.AES_KEY_SIZE))
    {
      throw new RegistryException(String.format(
          "Failed to get the binary value (%s) under the key (%s): "
          + "The specified encryption key is invalid", name, path));
    }

    if ((encryptionIV != null) && (encryptionIV.length != CryptoUtils.AES_BLOCK_SIZE))
    {
      throw new RegistryException(String.format(
          "Failed to get the binary value (%s) under the key (%s): "
          + "The specified encryption IV is invalid", name, path));
    }

    path = getActualPath(path);

    // Store the value in the database
    try (Connection connection = getConnection();
      PreparedStatement statement = connection.prepareStatement(getBinaryValueSQL))
    {
      // Retrieve the ID of the Registry key with the specified path
      String keyId = getKeyId(connection, path, false);

      if (keyId == null)
      {
        return defaultValue;
      }

      statement.setString(1, keyId);
      statement.setString(2, name);

      try (ResultSet rs = statement.executeQuery())
      {
        if (rs.next())
        {
          // Retrieve the type of the existing value if one exists
          int existingType = rs.getInt(1);

          if ((existingType != RegistryValueType.NONE.getCode())
              && (existingType != RegistryValueType.BINARY.getCode()))
          {
            throw new RegistryException(String.format(
                "Failed to get the binary value (%s) under the key (%s): "
                + "A value with the specified name exists with the incorrect type (%d)", name,
                path, existingType));
          }

          if ((encryptionKey != null) && (encryptionIV != null))
          {
            return decryptBinaryValue(DAOUtil.readBlob(rs, 2), encryptionKey, encryptionIV);
          }
          else
          {
            return DAOUtil.readBlob(rs, 2);
          }
        }
        else
        {
          return defaultValue;
        }
      }
    }
    catch (Throwable e)
    {
      throw new RegistryException(String.format(
          "Failed to get the binary value (%s) under the key (%s)", name, path), e);
    }
  }

  /**
   * Returns the <code>DataSource</code> for the <code>Registry</code>.
   *
   * @return the <code>DataSource</code> for the <code>Registry</code>
   */
  public DataSource getDataSource()
  {
    return dataSource;
  }

  /**
   * Retrieve the decimal value with the specified name under the key with the specified path.
   *
   * @param path         the path for the Registry key e.g. /XYZApp/Section/SubSection
   * @param name         the name of the Registry value
   * @param defaultValue the default value to use if the specified decimal value cannot be found
   *
   * @return the decimal value or the default value if the value could not be found
   */
  public BigDecimal getDecimalValue(String path, String name, BigDecimal defaultValue)
    throws RegistryException
  {
    // Check the parameters
    if (StringUtil.isNullOrEmpty(path))
    {
      throw new RegistryException(String.format(
          "Failed to get the decimal value (%s) under the key (%s): "
          + "The specified key path is invalid", name, path));
    }

    if (StringUtil.isNullOrEmpty(name))
    {
      throw new RegistryException(String.format(
          "Failed to get the decimal value (%s) under the key (%s): "
          + "The specified name is invalid", name, path));
    }

    path = getActualPath(path);

    // Store the value in the database
    try (Connection connection = getConnection();
      PreparedStatement statement = connection.prepareStatement(getDecimalValueSQL))
    {
      // Retrieve the ID of the Registry key with the specified path
      String keyId = getKeyId(connection, path, false);

      if (keyId == null)
      {
        return defaultValue;
      }

      statement.setString(1, keyId);
      statement.setString(2, name);

      try (ResultSet rs = statement.executeQuery())
      {
        if (rs.next())
        {
          // Retrieve the type of the existing value if one exists
          int existingType = rs.getInt(1);

          if ((existingType != RegistryValueType.NONE.getCode())
              && (existingType != RegistryValueType.DECIMAL.getCode()))
          {
            throw new RegistryException(String.format(
                "Failed to get the decimal value (%s) under the key (%s): "
                + "A value with the specified name exists with the incorrect type (%d)", name,
                path, existingType));
          }

          return rs.getBigDecimal(2);
        }
        else
        {
          return defaultValue;
        }
      }
    }
    catch (Throwable e)
    {
      throw new RegistryException(String.format(
          "Failed to get the decimal value (%s) under the key (%s)", name, path), e);
    }
  }

  /**
   * Retrieve the integer value with the specified name under the key with the specified path.
   *
   * @param path         the path for the Registry key e.g. /XYZApp/Section/SubSection
   * @param name         the name of the Registry value
   * @param defaultValue the default value to use if the specified integer value cannot be found
   *
   * @return the integer value or the default value if the value could not be found
   */
  public int getIntegerValue(String path, String name, int defaultValue)
    throws RegistryException
  {
    // Check the parameters
    if (StringUtil.isNullOrEmpty(path))
    {
      throw new RegistryException(String.format(
          "Failed to get the integer value (%s) under the key (%s): "
          + "The specified key path is invalid", name, path));
    }

    if (StringUtil.isNullOrEmpty(name))
    {
      throw new RegistryException(String.format(
          "Failed to get the integer value (%s) under the key (%s): "
          + "The specified name is invalid", name, path));
    }

    path = getActualPath(path);

    // Store the value in the database
    try (Connection connection = getConnection();
      PreparedStatement statement = connection.prepareStatement(getIntegerValueSQL))
    {
      // Retrieve the ID of the Registry key with the specified path
      String keyId = getKeyId(connection, path, false);

      if (keyId == null)
      {
        return defaultValue;
      }

      statement.setString(1, keyId);
      statement.setString(2, name);

      try (ResultSet rs = statement.executeQuery())
      {
        if (rs.next())
        {
          // Retrieve the type of the existing value if one exists
          int existingType = rs.getInt(1);

          if ((existingType != RegistryValueType.NONE.getCode())
              && (existingType != RegistryValueType.INTEGER.getCode()))
          {
            throw new RegistryException(String.format(
                "Failed to get the integer value (%s) under the key (%s): "
                + "A value with the specified name exists with the incorrect type (%d)", name,
                path, existingType));
          }

          return rs.getInt(2);
        }
        else
        {
          return defaultValue;
        }
      }
    }
    catch (Throwable e)
    {
      throw new RegistryException(String.format(
          "Failed to get the integer value (%s) under the key (%s)", name, path), e);
    }
  }

  /**
   * Retrieve the string value with the specified name under the key with the specified path.
   *
   * @param path         the path for the Registry key e.g. /XYZApp/Section/SubSection
   * @param name         the name of the Registry value
   * @param defaultValue the default value to use if the specified string value cannot be found
   *
   * @return the string value or the default value if the value could not be found
   */
  public String getStringValue(String path, String name, String defaultValue)
    throws RegistryException
  {
    return getStringValue(path, name, defaultValue, null, null);
  }

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
   */
  public String getStringValue(String path, String name, String defaultValue, byte[] encryptionKey,
      byte[] encryptionIV)
    throws RegistryException
  {
    // Check the parameters
    if (StringUtil.isNullOrEmpty(path))
    {
      throw new RegistryException(String.format(
          "Failed to get the string value (%s) under the key (%s): "
          + "The specified key path is invalid", name, path));
    }

    if (StringUtil.isNullOrEmpty(name))
    {
      throw new RegistryException(String.format(
          "Failed to get the string value (%s) under the key (%s): "
          + "The specified name is invalid", name, path));
    }

    if ((encryptionKey != null) && (encryptionKey.length != CryptoUtils.AES_KEY_SIZE))
    {
      throw new RegistryException(String.format(
          "Failed to get the string value (%s) under the key (%s): "
          + "The specified encryption key is invalid", name, path));
    }

    if ((encryptionIV != null) && (encryptionIV.length != CryptoUtils.AES_BLOCK_SIZE))
    {
      throw new RegistryException(String.format(
          "Failed to get the string value (%s) under the key (%s): "
          + "The specified encryption IV is invalid", name, path));
    }

    path = getActualPath(path);

    // Store the value in the database
    try (Connection connection = getConnection();
      PreparedStatement statement = connection.prepareStatement(getStringValueSQL))
    {
      // Retrieve the ID of the Registry key with the specified path
      String keyId = getKeyId(connection, path, false);

      if (keyId == null)
      {
        return defaultValue;
      }

      statement.setString(1, keyId);
      statement.setString(2, name);

      try (ResultSet rs = statement.executeQuery())
      {
        if (rs.next())
        {
          // Retrieve the type of the existing value if one exists
          int existingType = rs.getInt(1);

          if ((existingType != RegistryValueType.NONE.getCode())
              && (existingType != RegistryValueType.STRING.getCode()))
          {
            throw new RegistryException(String.format(
                "Failed to get the string value (%s) under the key (%s): "
                + "A value with the specified name exists with the incorrect type (%d)", name,
                path, existingType));
          }

          if ((encryptionKey != null) && (encryptionIV != null))
          {
            return decryptStringValue(rs.getString(2), encryptionKey, encryptionIV);
          }
          else
          {
            return rs.getString(2);
          }
        }
        else
        {
          return defaultValue;
        }
      }
    }
    catch (Throwable e)
    {
      throw new RegistryException(String.format(
          "Failed to get the string value (%s) under the key (%s)", name, path), e);
    }
  }

  /**
   * Initialise the Registry.
   */
  @PostConstruct
  public void init()
  {
    try
    {
      dataSource = InitialContext.doLookup("java:app/jdbc/ApplicationDataSource");
    }
    catch (Throwable ignored) {}

    if (dataSource == null)
    {
      try
      {
        dataSource = InitialContext.doLookup("java:comp/env/jdbc/ApplicationDataSource");
      }
      catch (Throwable ignored) {}
    }

    if (dataSource == null)
    {
      throw new RuntimeException(
          "Failed to retrieve the application data source using the JNDI names "
          + "(java:app/jdbc/ApplicationDataSource) and (java:comp/env/jdbc/ApplicationDataSource)");
    }

    try
    {
      // Determine the schema prefix
      String schemaPrefix = DataAccessObject.MMP_DATABASE_SCHEMA + DAOUtil.getSchemaSeparator(
          dataSource);

      // Build the SQL statements for the DAO
      buildStatements(schemaPrefix);
    }
    catch (Throwable e)
    {
      throw new RuntimeException("Failed to initialise the Registry", e);
    }
  }

  /**
   * Check whether the integer value with the specified path exists.
   *
   * @param path the path for the Registry key e.g. /XYZApp/Section/SubSection
   * @param name the name of the Registry value
   *
   * @return <code>true</code> if the integer value exists or <code>false</code> otherwise
   */
  public boolean integerValueExists(String path, String name)
    throws RegistryException
  {
    // Check the parameters
    if (StringUtil.isNullOrEmpty(path))
    {
      throw new RegistryException(String.format(
          "Failed to check if the integer value (%s) exists under the key (%s): "
          + "The specified key path is invalid", name, path));
    }

    if (StringUtil.isNullOrEmpty(name))
    {
      throw new RegistryException(String.format(
          "Failed to check if the integer value (%s) exists under the key (%s): "
          + "The specified name is invalid", name, path));
    }

    path = getActualPath(path);

    // Store the value in the database
    try (Connection connection = getConnection();
      PreparedStatement statement = connection.prepareStatement(getIntegerValueSQL))
    {
      // Retrieve the ID of the Registry key with the specified path
      String keyId = getKeyId(connection, path, false);

      if (keyId == null)
      {
        return false;
      }

      statement.setString(1, keyId);
      statement.setString(2, name);

      try (ResultSet rs = statement.executeQuery())
      {
        if (rs.next())
        {
          // Retrieve the type of the existing value if one exists
          int existingType = rs.getInt(1);

          if ((existingType != RegistryValueType.NONE.getCode())
              && (existingType != RegistryValueType.INTEGER.getCode()))
          {
            throw new RegistryException(String.format(
                "Failed to check if the integer value (%s) exists under the key (%s): "
                + "A value with the specified name exists with the incorrect type (%d)", name,
                path, existingType));
          }

          return true;
        }
        else
        {
          return false;
        }
      }
    }
    catch (Throwable e)
    {
      throw new RegistryException(String.format(
          "Failed to check if the integer value (%s) exists under the key (%s)", name, path), e);
    }
  }

  /**
   * Remove the value with the specified name under the key with the specified path.
   *
   * @param path the path for the registry key e.g. /XYZApp/Section/SubSection
   * @param name the name of the value
   *
   * @return <code>true</code> if the value with the specified name was found and removed or
   *         <code>false</code> otherwise
   */
  public boolean removeValue(String path, String name)
    throws RegistryException
  {
    // Check the parameters
    if (StringUtil.isNullOrEmpty(path))
    {
      throw new RegistryException(String.format(
          "Failed to remove the value (%s) under the key (%s): "
          + "The specified key path is invalid", name, path));
    }

    if (StringUtil.isNullOrEmpty(name))
    {
      throw new RegistryException(String.format(
          "Failed to remove the value (%s) under the key (%s): The specified name is invalid",
          name, path));
    }

    path = getActualPath(path);

    // Retrieve the Transaction Manager
    TransactionManager transactionManager = TransactionManager.getTransactionManager();
    javax.transaction.Transaction existingTransaction = null;

    try
    {
      if (transactionManager.isTransactionActive())
      {
        existingTransaction = transactionManager.beginNew();
      }
      else
      {
        transactionManager.begin();
      }

      try (Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(removeValueSQL))
      {
        // Retrieve the ID of the Registry key with the specified path
        String keyId = getKeyId(connection, path, false);

        if (keyId == null)
        {
          return false;
        }

        statement.setString(1, keyId);
        statement.setString(2, name);
        statement.execute();
      }

      transactionManager.commit();

      return true;
    }
    catch (Throwable e)
    {
      try
      {
        transactionManager.rollback();
      }
      catch (Throwable f)
      {
        logger.error(String.format(
            "Failed to rollback the transaction while removing the value (%s) under the key (%s)",
            name, path), f);
      }

      throw new RegistryException(String.format(
          "Failed to remove the value (%s) under the key (%s)", name, path), e);
    }
    finally
    {
      try
      {
        if (existingTransaction != null)
        {
          transactionManager.resume(existingTransaction);
        }
      }
      catch (Throwable e)
      {
        logger.error(String.format(
            "Failed to resume the transaction while removing the value (%s) under the key (%s)",
            name, path), e);
      }
    }
  }

  /**
   * Set the binary value with the specified name under the key with the specified path to the
   * specified value.
   * <p/>
   * This method will overwrite the value if it already exists.
   *
   * @param path  the path for the Registry key e.g. /XYZApp/Section/SubSection
   * @param name  the name of the Registry value
   * @param value the new binary value
   */
  public void setBinaryValue(String path, String name, byte[] value)
    throws RegistryException
  {
    setBinaryValue(path, name, value, null, null);
  }

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
   */
  public void setBinaryValue(String path, String name, byte[] value, byte[] encryptionKey,
      byte[] encryptionIV)
    throws RegistryException
  {
    // Check the parameters
    if (StringUtil.isNullOrEmpty(path))
    {
      throw new RegistryException(String.format(
          "Failed to set the binary value (%s) under the key (%s): "
          + "The specified key path is invalid", name, path));
    }

    if (StringUtil.isNullOrEmpty(name))
    {
      throw new RegistryException(String.format(
          "Failed to set the binary value (%s) under the key (%s): "
          + "The specified name is invalid", name, path));
    }

    if ((encryptionKey != null) && (encryptionKey.length != CryptoUtils.AES_KEY_SIZE))
    {
      throw new RegistryException(String.format(
          "Failed to set the binary value (%s) under the key (%s): "
          + "The specified encryption key is invalid", name, path));
    }

    if ((encryptionIV != null) && (encryptionIV.length != CryptoUtils.AES_BLOCK_SIZE))
    {
      throw new RegistryException(String.format(
          "Failed to set the binary value (%s) under the key (%s): "
          + "The specified encryption IV is invalid", name, path));
    }

    path = getActualPath(path);

    if ((encryptionKey != null) && (encryptionIV != null))
    {
      value = encryptBinaryValue(value, encryptionKey, encryptionIV);
    }

    // Retrieve the Transaction Manager
    TransactionManager transactionManager = TransactionManager.getTransactionManager();
    javax.transaction.Transaction existingTransaction = null;

    try
    {
      if (transactionManager.isTransactionActive())
      {
        existingTransaction = transactionManager.beginNew();
      }
      else
      {
        transactionManager.begin();
      }

      try (Connection connection = getConnection())
      {
        /*
         * Retrieve the ID of the Registry key with the specified path, creating the path
         * if required.
         */
        String keyId = getKeyId(connection, path, true);

        // Retrieve the type of the existing value if one exists
        int existingType = getValueType(connection, keyId, name);

        if ((existingType != RegistryValueType.NONE.getCode())
            && (existingType != RegistryValueType.BINARY.getCode()))
        {
          throw new RegistryException(String.format(
              "Failed to set the binary value (%s) under the key (%s): "
              + "A value with the specified name already exists with the incorrect type (%d)",
              name, path, existingType));
        }

        if (existingType == RegistryValueType.NONE.getCode())
        {
          String id = IDGenerator.nextUUID(dataSource).toString();

          try (PreparedStatement statement = connection.prepareStatement(setBinaryValueInsertSQL))
          {
            statement.setString(1, id);
            statement.setString(2, keyId);
            statement.setInt(3, RegistryValueType.BINARY.getCode());
            statement.setString(4, name);
            statement.setBytes(5, value);

            statement.execute();
          }
        }
        else
        {
          try (PreparedStatement statement = connection.prepareStatement(setBinaryValueUpdateSQL))
          {
            statement.setBytes(1, value);
            statement.setString(2, keyId);
            statement.setString(3, name);

            statement.execute();
          }
        }
      }

      transactionManager.commit();
    }
    catch (Throwable e)
    {
      try
      {
        transactionManager.rollback();
      }
      catch (Throwable f)
      {
        logger.error(String.format(
            "Failed to rollback the transaction while setting the value (%s) under the key (%s)",
            name, path), f);
      }

      throw new RegistryException(String.format(
          "Failed to set the binary value (%s) under the key (%s)", name, path), e);
    }
    finally
    {
      try
      {
        if (existingTransaction != null)
        {
          transactionManager.resume(existingTransaction);
        }
      }
      catch (Throwable e)
      {
        logger.error(String.format(
            "Failed to resume the transaction while setting the value (%s) under the key (%s)",
            name, path), e);
      }
    }
  }

  /**
   * Set the decimal value with the specified name under the key with the specified path to the
   * specified value.
   * <p/>
   * This method will overwrite the value if it already exists.
   *
   * @param path  the path for the Registry key e.g. /XYZApp/Section/SubSection
   * @param name  the name of the Registry value
   * @param value the new decimal value
   */
  public void setDecimalValue(String path, String name, BigDecimal value)
    throws RegistryException
  {
    // Check the parameters
    if (StringUtil.isNullOrEmpty(path))
    {
      throw new RegistryException(String.format(
          "Failed to set the decimal value (%s) under the key (%s): "
          + "The specified key path is invalid", name, path));
    }

    if (StringUtil.isNullOrEmpty(name))
    {
      throw new RegistryException(String.format(
          "Failed to set the decimal value (%s) under the key (%s): "
          + "The specified name is invalid", name, path));
    }

    path = getActualPath(path);

    // Retrieve the Transaction Manager
    TransactionManager transactionManager = TransactionManager.getTransactionManager();
    javax.transaction.Transaction existingTransaction = null;

    try
    {
      if (transactionManager.isTransactionActive())
      {
        existingTransaction = transactionManager.beginNew();
      }
      else
      {
        transactionManager.begin();
      }

      try (Connection connection = getConnection())
      {
        /*
         * Retrieve the ID of the Registry key with the specified path, creating the path
         * if required.
         */
        String keyId = getKeyId(connection, path, true);

        // Retrieve the type of the existing value if one exists
        int existingType = getValueType(connection, keyId, name);

        if ((existingType != RegistryValueType.NONE.getCode())
            && (existingType != RegistryValueType.DECIMAL.getCode()))
        {
          throw new RegistryException(String.format(
              "Failed to set the decimal value (%s) under the key (%s): "
              + "A value with the specified name already exists with the incorrect type (%d)",
              name, path, existingType));
        }

        if (existingType == RegistryValueType.NONE.getCode())
        {
          String id = IDGenerator.nextUUID(dataSource).toString();

          try (PreparedStatement statement = connection.prepareStatement(setDecimalValueInsertSQL))
          {
            statement.setString(1, id);
            statement.setString(2, keyId);
            statement.setInt(3, RegistryValueType.DECIMAL.getCode());
            statement.setString(4, name);
            statement.setBigDecimal(5, value);

            statement.execute();
          }
        }
        else
        {
          try (PreparedStatement statement = connection.prepareStatement(setDecimalValueUpdateSQL))
          {
            statement.setBigDecimal(1, value);
            statement.setString(2, keyId);
            statement.setString(3, name);

            statement.execute();
          }
        }
      }

      transactionManager.commit();
    }
    catch (Throwable e)
    {
      try
      {
        transactionManager.rollback();
      }
      catch (Throwable f)
      {
        logger.error(String.format(
            "Failed to rollback the transaction while setting the value (%s) under the key (%s)",
            name, path), f);
      }

      throw new RegistryException(String.format(
          "Failed to set the decimal value (%s) under the key (%s)", name, path), e);
    }
    finally
    {
      try
      {
        if (existingTransaction != null)
        {
          transactionManager.resume(existingTransaction);
        }
      }
      catch (Throwable e)
      {
        logger.error(String.format(
            "Failed to resume the transaction while setting the value (%s) under the key (%s)",
            name, path), e);
      }
    }
  }

  /**
   * Set the integer value with the specified name under the key with the specified path to the
   * specified value.
   * <p/>
   * This method will overwrite the value if it already exists.
   *
   * @param path  the path for the Registry key e.g. /XYZApp/Section/SubSection
   * @param name  the name of the Registry value
   * @param value the new integer value
   */
  public void setIntegerValue(String path, String name, int value)
    throws RegistryException
  {
    // Check the parameters
    if (StringUtil.isNullOrEmpty(path))
    {
      throw new RegistryException(String.format(
          "Failed to set the integer value (%s) under the key (%s): "
          + "The specified key path is invalid", name, path));
    }

    if (StringUtil.isNullOrEmpty(name))
    {
      throw new RegistryException(String.format(
          "Failed to set the integer value (%s) under the key (%s): "
          + "The specified name is invalid", name, path));
    }

    path = getActualPath(path);

    // Retrieve the Transaction Manager
    TransactionManager transactionManager = TransactionManager.getTransactionManager();
    javax.transaction.Transaction existingTransaction = null;

    try
    {
      if (transactionManager.isTransactionActive())
      {
        existingTransaction = transactionManager.beginNew();
      }
      else
      {
        transactionManager.begin();
      }

      try (Connection connection = getConnection())
      {
        /*
         * Retrieve the ID of the Registry key with the specified path, creating the path
         * if required.
         */
        String keyId = getKeyId(connection, path, true);

        // Retrieve the type of the existing value if one exists
        int existingType = getValueType(connection, keyId, name);

        if ((existingType != RegistryValueType.NONE.getCode())
            && (existingType != RegistryValueType.INTEGER.getCode()))
        {
          throw new RegistryException(String.format(
              "Failed to set the integer value (%s) under the key (%s): "
              + "A value with the specified name already exists with the incorrect type (%d)",
              name, path, existingType));
        }

        if (existingType == RegistryValueType.NONE.getCode())
        {
          String id = IDGenerator.nextUUID(dataSource).toString();

          try (PreparedStatement statement = connection.prepareStatement(setIntegerValueInsertSQL))
          {
            statement.setString(1, id);
            statement.setString(2, keyId);
            statement.setInt(3, RegistryValueType.INTEGER.getCode());
            statement.setString(4, name);
            statement.setInt(5, value);

            statement.execute();
          }
        }
        else
        {
          try (PreparedStatement statement = connection.prepareStatement(setIntegerValueUpdateSQL))
          {
            statement.setInt(1, value);
            statement.setString(2, keyId);
            statement.setString(3, name);

            statement.execute();
          }
        }
      }

      transactionManager.commit();
    }
    catch (Throwable e)
    {
      try
      {
        transactionManager.rollback();
      }
      catch (Throwable f)
      {
        logger.error(String.format(
            "Failed to rollback the transaction while setting the value (%s) under the key (%s)",
            name, path), f);
      }

      throw new RegistryException(String.format(
          "Failed to set the integer value (%s) under the key (%s)", name, path), e);
    }
    finally
    {
      try
      {
        if (existingTransaction != null)
        {
          transactionManager.resume(existingTransaction);
        }
      }
      catch (Throwable e)
      {
        logger.error(String.format(
            "Failed to resume the transaction while setting the value (%s) under the key (%s)",
            name, path), e);
      }
    }
  }

  /**
   * Set the string value with the specified name under the key with the specified path to the
   * specified value.
   * <p/>
   * This method will overwrite the value if it already exists.
   *
   * @param path  the path for the Registry key e.g. /XYZApp/Section/SubSection
   * @param name  the name of the Registry value
   * @param value the new string value
   */
  public void setStringValue(String path, String name, String value)
    throws RegistryException
  {
    setStringValue(path, name, value, null, null);
  }

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
   */
  public void setStringValue(String path, String name, String value, byte[] encryptionKey,
      byte[] encryptionIV)
    throws RegistryException
  {
    // Check the parameters
    if (StringUtil.isNullOrEmpty(path))
    {
      throw new RegistryException(String.format(
          "Failed to set the string value (%s) under the key (%s): "
          + "The specified key path is invalid", name, path));
    }

    if (StringUtil.isNullOrEmpty(name))
    {
      throw new RegistryException(String.format(
          "Failed to set the string value (%s) under the key (%s): "
          + "The specified name is invalid", name, path));
    }

    if ((encryptionKey != null) && (encryptionKey.length != CryptoUtils.AES_KEY_SIZE))
    {
      throw new RegistryException(String.format(
          "Failed to set the string value (%s) under the key (%s): "
          + "The specified encryption key is invalid", name, path));
    }

    if ((encryptionIV != null) && (encryptionIV.length != CryptoUtils.AES_BLOCK_SIZE))
    {
      throw new RegistryException(String.format(
          "Failed to set the string value (%s) under the key (%s): "
          + "The specified encryption IV is invalid", name, path));
    }

    if ((encryptionKey != null) && (encryptionIV != null))
    {
      value = encryptStringValue(value, encryptionKey, encryptionIV);
    }

    path = getActualPath(path);

    // Retrieve the Transaction Manager
    TransactionManager transactionManager = TransactionManager.getTransactionManager();
    javax.transaction.Transaction existingTransaction = null;

    try
    {
      if (transactionManager.isTransactionActive())
      {
        existingTransaction = transactionManager.beginNew();
      }
      else
      {
        transactionManager.begin();
      }

      try (Connection connection = getConnection())
      {
        /*
         * Retrieve the ID of the Registry key with the specified path, creating the path
         * if required.
         */
        String keyId = getKeyId(connection, path, true);

        // Retrieve the type of the existing value if one exists
        int existingType = getValueType(connection, keyId, name);

        if ((existingType != RegistryValueType.NONE.getCode())
            && (existingType != RegistryValueType.STRING.getCode()))
        {
          throw new RegistryException(String.format(
              "Failed to set the string value (%s) under the key (%s): "
              + "A value with the  specified name (%s) already exists with the incorrect type (%d)",
              name, path, name, existingType));
        }

        if (existingType == RegistryValueType.NONE.getCode())
        {
          String id = IDGenerator.nextUUID(dataSource).toString();

          try (PreparedStatement statement = connection.prepareStatement(setStringValueInsertSQL))
          {
            statement.setString(1, id);
            statement.setString(2, keyId);
            statement.setInt(3, RegistryValueType.STRING.getCode());
            statement.setString(4, name);
            statement.setString(5, value);

            statement.execute();
          }
        }
        else
        {
          try (PreparedStatement statement = connection.prepareStatement(setStringValueUpdateSQL))
          {
            statement.setString(1, value);
            statement.setString(2, keyId);
            statement.setString(3, name);

            statement.execute();
          }
        }
      }

      transactionManager.commit();
    }
    catch (Throwable e)
    {
      try
      {
        transactionManager.rollback();
      }
      catch (Throwable f)
      {
        logger.error(String.format(
            "Failed to rollback the transaction while setting the value (%s) under the key (%s)",
            name, path), f);
      }

      throw new RegistryException(String.format(
          "Failed to set the string value (%s) under the key (%s)", name, path), e);
    }
    finally
    {
      try
      {
        if (existingTransaction != null)
        {
          transactionManager.resume(existingTransaction);
        }
      }
      catch (Throwable e)
      {
        logger.error(String.format(
            "Failed to resume the transaction while setting the value (%s) under the key (%s)",
            name, path), e);
      }
    }
  }

  /**
   * Check whether the string value with the specified path exists.
   *
   * @param path the path for the Registry key e.g. /XYZApp/Section/SubSection
   * @param name the name of the Registry value
   *
   * @return true if the string value exists or false otherwise
   */
  public boolean stringValueExists(String path, String name)
    throws RegistryException
  {
    // Check the parameters
    if (StringUtil.isNullOrEmpty(path))
    {
      throw new RegistryException(String.format(
          "Failed to check if the string value (%s) exists under the key (%s): "
          + "The specified key path is invalid", name, path));
    }

    if (StringUtil.isNullOrEmpty(name))
    {
      throw new RegistryException(String.format(
          "Failed to check if the string value (%s) exists under the key (%s): "
          + "The specified name is invalid", name, path));
    }

    path = getActualPath(path);

    try (Connection connection = getConnection();
      PreparedStatement statement = connection.prepareStatement(getStringValueSQL))
    {
      // Retrieve the ID of the Registry key with the specified path
      String keyId = getKeyId(connection, path, false);

      if (keyId == null)
      {
        return false;
      }

      statement.setString(1, keyId);
      statement.setString(2, name);

      try (ResultSet rs = statement.executeQuery())
      {
        if (rs.next())
        {
          // Retrieve the type of the existing value if one exists
          int existingType = rs.getInt(1);

          if ((existingType != RegistryValueType.NONE.getCode())
              && (existingType != RegistryValueType.STRING.getCode()))
          {
            throw new RegistryException(String.format(
                "Failed to check if the string value (%s) exists under the key (%s): "
                + "A value with the specified name exists with the incorrect type (%d)", name,
                path, existingType));
          }

          return true;
        }
        else
        {
          return false;
        }
      }
    }
    catch (Throwable e)
    {
      throw new RegistryException(String.format(
          "Failed to check if the string value (%s) exists under the key (%s)", name, path), e);
    }
  }

  /**
   * Build the SQL statements for the <code>Registry</code>.
   *
   * @param schemaPrefix the schema prefix to prepend to database objects
   */
  protected void buildStatements(String schemaPrefix)
  {
    // setStringValueInsertSQL
    setStringValueInsertSQL = "INSERT INTO " + schemaPrefix + "REGISTRY "
        + "(ID, PARENT_ID, ENTRY_TYPE, NAME, SVALUE) VALUES (?, ?, ?, ?, ?)";

    // setStringValueUpdateSQL
    setStringValueUpdateSQL = "UPDATE " + schemaPrefix + "REGISTRY "
        + "SET SVALUE=? WHERE PARENT_ID=? AND NAME=?";

    // setBinaryValueInsertSQL
    setBinaryValueInsertSQL = "INSERT INTO " + schemaPrefix + "REGISTRY "
        + "(ID, PARENT_ID, ENTRY_TYPE, NAME, BVALUE) VALUES (?, ?, ?, ?, ?)";

    // setBinaryValueUpdateSQL
    setBinaryValueUpdateSQL = "UPDATE " + schemaPrefix + "REGISTRY "
        + "SET BVALUE=? WHERE PARENT_ID=? AND NAME=?";

    // setIntegerValueInsertSQL
    setIntegerValueInsertSQL = "INSERT INTO " + schemaPrefix + "REGISTRY "
        + "(ID, PARENT_ID, ENTRY_TYPE, NAME, IVALUE) VALUES (?, ?, ?, ?, ?)";

    // setIntegerValueUpdateSQL
    setIntegerValueUpdateSQL = "UPDATE " + schemaPrefix + "REGISTRY "
        + "SET IVALUE=? WHERE PARENT_ID=? AND NAME=?";

    // setDecimalValueInsertSQL
    setDecimalValueInsertSQL = "INSERT INTO " + schemaPrefix + "REGISTRY "
        + "(ID, PARENT_ID, ENTRY_TYPE, NAME, DVALUE) VALUES (?, ?, ?, ?, ?)";

    // setDecimalValueUpdateSQL
    setDecimalValueUpdateSQL = "UPDATE " + schemaPrefix + "REGISTRY "
        + "SET DVALUE=? WHERE PARENT_ID=? AND NAME=?";

    // removeValueSQL
    removeValueSQL = "DELETE FROM " + schemaPrefix + "REGISTRY WHERE PARENT_ID=? AND NAME=?";

    // getKeyIdNoParentSQL
    getKeyIdNoParentSQL = "SELECT ID FROM " + schemaPrefix + "REGISTRY "
        + "WHERE NAME=? AND PARENT_ID IS NULL AND ENTRY_TYPE=0";

    // getKeyIdWithParentSQL
    getKeyIdWithParentSQL = "SELECT ID FROM " + schemaPrefix + "REGISTRY "
        + "WHERE NAME=? AND PARENT_ID=? AND ENTRY_TYPE=0";

    // createKeySQL
    createKeySQL = "INSERT INTO " + schemaPrefix + "REGISTRY "
        + "(ID, ENTRY_TYPE, PARENT_ID, NAME) VALUES (?,0,?,?)";

    // getValueTypeSQL
    getValueTypeSQL = "SELECT ENTRY_TYPE FROM " + schemaPrefix + "REGISTRY "
        + "WHERE PARENT_ID=? AND NAME=?";

    // getStringValueSQL
    getStringValueSQL = "SELECT ENTRY_TYPE, SVALUE FROM " + schemaPrefix + "REGISTRY "
        + "WHERE PARENT_ID=? AND NAME=?";

    // getBinaryValueSQL
    getBinaryValueSQL = "SELECT ENTRY_TYPE, BVALUE FROM " + schemaPrefix + "REGISTRY "
        + " WHERE PARENT_ID=? AND NAME=?";

    // getIntegerValueSQL
    getIntegerValueSQL = "SELECT ENTRY_TYPE, IVALUE FROM " + schemaPrefix + "REGISTRY "
        + "WHERE PARENT_ID=? AND NAME=?";

    // getDecimalValueSQL
    getDecimalValueSQL = "SELECT ENTRY_TYPE, DVALUE FROM " + schemaPrefix + "REGISTRY "
        + " WHERE PARENT_ID=? AND NAME=?";
  }

  /**
   * Create the Registry key with the specified name under the existing key with specified ID.
   *
   * @param parentId the ID of the parent key
   * @param name     the name of the new Registry key
   *
   * @return the ID of the new Registry key
   */
  private String createKey(String parentId, String name)
    throws RegistryException

  {
    if (name.length() == 0)
    {
      if (parentId != null)
      {
        throw new RegistryException(String.format(
            "Unable to create the empty key under the existing key with ID (%s)", parentId));
      }
      else
      {
        throw new RegistryException("Unable to create the empty root key");
      }
    }

    try (Connection connection = getConnection();
      PreparedStatement statement = connection.prepareStatement(createKeySQL))
    {
      String id = IDGenerator.nextUUID(dataSource).toString();

      statement.setString(1, id);
      statement.setString(2, parentId);
      statement.setString(3, name);
      statement.execute();

      return id;
    }
    catch (RegistryException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new RegistryException(String.format("Failed to create the new key (%s)", name), e);
    }
  }

  /**
   * Decrypt the specified value with the specified AES symmetric encryption key.
   *
   * @param value         the value to decrypt
   * @param encryptionKey the encryption key to use to decrypt the value
   * @param encryptionIV  the encryption initialisation vector
   *
   * @return the decrypted value
   */
  private byte[] decryptBinaryValue(byte[] value, byte[] encryptionKey, byte[] encryptionIV)
    throws RegistryException
  {
    // Check the value
    if (value == null)
    {
      throw new RegistryException(
          "Failed to decrypt the encrypted binary value: The value is invalid");
    }

    try
    {
      Cipher cipher = threadLocalCipher.get();
      cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(encryptionKey, CryptoUtils.AES_KEY_SPEC),
          new IvParameterSpec(encryptionIV));

      return cipher.doFinal(value);
    }
    catch (Throwable e)
    {
      throw new RegistryException(String.format(
          "Failed to decrypt the encrypted binary value of length (%d)", value.length), e);
    }
  }

  /**
   * Base64 decode and decrypt the specified value with the specified AES symmetric encryption
   * key.
   *
   * @param value         the value to base64 decode and decrypt
   * @param encryptionKey the encryption key to use to decrypt the value
   * @param encryptionIV  the encryption initialisation vector
   *
   * @return the base64 decoded and decrypted value
   */
  private String decryptStringValue(String value, byte[] encryptionKey, byte[] encryptionIV)
    throws RegistryException
  {
    // Check the value
    if (value == null)
    {
      throw new RegistryException(
          "Failed to decode and decrypt the encrypted string value: The value is invalid");
    }

    if (!value.startsWith(ENCRYPTION_PREFIX))
    {
      throw new RegistryException(String.format(
          "Unable to decode and decrypt the string value (%s): "
          + "The value does not appear to be encrypted", value));
    }

    value = value.substring(ENCRYPTION_PREFIX.length());

    try
    {
      byte[] decodedValue = Base64.decode(value);

      if (decodedValue == null)
      {
        throw new RegistryException(String.format(
            "Unable to decode and decrypt the encrypted string value (%s): "
            + "The Base64 decoded value was null", value));
      }

      Cipher cipher = threadLocalCipher.get();
      cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(encryptionKey, CryptoUtils.AES_KEY_SPEC),
          new IvParameterSpec(encryptionIV));

      return new String(cipher.doFinal(decodedValue));
    }
    catch (RegistryException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new RegistryException(String.format(
          "Failed to decode and decrypt the encrypted string value (%s)", value), e);
    }
  }

  /**
   * Encrypt the specified value with the specified AES symmetric encryption key.
   *
   * @param value         the value to encrypt
   * @param encryptionKey the encryption key to use to encrypt the value
   * @param encryptionIV  the encryption initialisation vector
   *
   * @return the encrypted value
   */
  private byte[] encryptBinaryValue(byte[] value, byte[] encryptionKey, byte[] encryptionIV)
    throws RegistryException
  {
    // Check the value
    if (value == null)
    {
      throw new RegistryException("Failed to encrypt the binary value: The value is invalid");
    }

    try
    {
      Cipher cipher = threadLocalCipher.get();
      cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(encryptionKey, CryptoUtils.AES_KEY_SPEC),
          new IvParameterSpec(encryptionIV));

      return cipher.doFinal(value);
    }
    catch (Throwable e)
    {
      throw new RegistryException(String.format(
          "Failed to encrypt the binary value of length (%d)", value.length), e);
    }
  }

  /**
   * Encrypt and base64 encode the specified value with the specified AES symmetric encryption
   * key.
   *
   * @param value         the value to encrypt
   * @param encryptionKey the encryption key to use to encrypt the value
   * @param encryptionIV  the encryption initialisation vector
   *
   * @return the encrypted and base64 encoded value
   */
  private String encryptStringValue(String value, byte[] encryptionKey, byte[] encryptionIV)
    throws RegistryException
  {
    // Check the value
    if (value == null)
    {
      throw new RegistryException(
          "Failed to encrypt and encode the string value: The value is invalid");
    }

    try
    {
      Cipher cipher = threadLocalCipher.get();
      cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(encryptionKey, CryptoUtils.AES_KEY_SPEC),
          new IvParameterSpec(encryptionIV));

      byte[] encryptedValue = cipher.doFinal(value.getBytes());

      return ENCRYPTION_PREFIX + Base64.encodeBytes(encryptedValue);
    }
    catch (Throwable e)
    {
      throw new RegistryException(String.format(
          "Failed to encrypt and encode the string value (%s)", value), e);
    }
  }

  /*
   * Get the actual path based on the Registry path prefix specified for accessing the Registry.
   */
  private String getActualPath(String path)
  {
    if (path.startsWith("/"))
    {
      return path;
    }
    else
    {
      return "/" + path;
    }
  }

  /**
   * Returns a connection from the data source associated with the Registry.
   *
   * @return a connection from the data source associated with the Registry
   */
  private Connection getConnection()
    throws RegistryException
  {
    try
    {
      return dataSource.getConnection();
    }
    catch (Throwable e)
    {
      throw new RegistryException(String.format(
          "Failed to retrieve a database connection from the data source: %s", e.getMessage()), e);
    }
  }

  private String getKeyId(Connection connection, String path, boolean createIfRequired)
    throws RegistryException
  {
    return getKeyId(connection, path, getPathComponents(path), 0, null, createIfRequired);
  }

  private String getKeyId(Connection connection, String path, String[] pathComponents,
      int currentPathIndex, String currentKeyId, boolean createIfRequired)
    throws RegistryException
  {
    String keyId = null;
    String name = pathComponents[currentPathIndex];

    // Find the ID of the path component with the specified name under the path component with
    // the specified ID or the root path component if the key ID is NULL
    try (PreparedStatement statement = (currentKeyId == null)
          ? connection.prepareStatement(getKeyIdNoParentSQL)
          : connection.prepareStatement(getKeyIdWithParentSQL))
    {
      if (currentKeyId == null)
      {
        statement.setString(1, name);
      }
      else
      {
        statement.setString(1, name);
        statement.setString(2, currentKeyId);
      }

      try (ResultSet rs = statement.executeQuery())
      {
        if (rs.next())
        {
          keyId = rs.getString(1);
        }
      }
    }
    catch (Throwable e)
    {
      throw new RegistryException(String.format(
          "Failed to retrieve the ID of the path component (%s) of the key path (%s) at index (%d)",
          name, path, currentPathIndex), e);
    }

    // If we could not find the key with the specified name
    if (keyId == null)
    {
      if (createIfRequired)
      {
        // Key not found so create it and carry on recursing
        keyId = createKey(currentKeyId, name);

        if (currentPathIndex < (pathComponents.length - 1))
        {
          return getKeyId(connection, path, pathComponents, currentPathIndex + 1, keyId, true);
        }
        else
        {
          return keyId;
        }
      }
      else
      {
        // Key not found and not created...
        return null;
      }
    }

    // If we found the key and this is the last key in the path then return the ID otherwise
    // recurse
    else
    {
      if (currentPathIndex < (pathComponents.length - 1))
      {
        return getKeyId(connection, path, pathComponents, currentPathIndex + 1, keyId,
            createIfRequired);
      }
      else
      {
        return keyId;
      }
    }
  }

  private String[] getPathComponents(String configurationPath)
    throws RegistryException
  {
    StringTokenizer tokens = new StringTokenizer(configurationPath, PATH_SEPERATOR);
    List<String> list = new ArrayList<>();

    while (tokens.hasMoreTokens())
    {
      String token = tokens.nextToken();

      if (token.length() == 0)
      {
        throw new RegistryException(String.format(
            "Failed to retrieve the components for the path (%s): "
            + "One or more of the path components are invalid", configurationPath));
      }

      list.add(token);
    }

    return list.toArray(new String[list.size()]);
  }

  /**
   * Retrieve the type of the value with the specified name under the key with the
   * specified ID.
   *
   * @param connection the existing connection to the database to use
   * @param keyId      the unique ID of the Registry key
   * @param name       the name of the value
   *
   * @return the type of the value or -1 if the value does not exist
   */
  private int getValueType(Connection connection, String keyId, String name)
    throws RegistryException
  {
    try (PreparedStatement statement = connection.prepareStatement(getValueTypeSQL))
    {
      statement.setString(1, keyId);
      statement.setString(2, name);

      try (ResultSet rs = statement.executeQuery())
      {
        if (rs.next())
        {
          return rs.getInt(1);
        }
        else
        {
          return -1;
        }
      }
    }
    catch (Throwable e)
    {
      throw new RegistryException(String.format(
          "Failed to check whether the value (%s) exists under the key (%s)", name, keyId), e);
    }
  }
}

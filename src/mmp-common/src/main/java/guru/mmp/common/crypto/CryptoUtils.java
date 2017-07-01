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

package guru.mmp.common.crypto;

//~--- non-JDK imports --------------------------------------------------------

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.InputStream;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.X509Certificate;
import java.util.UUID;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>CryptUtils</code> class provides cryptography related utility functions.
 *
 * @author Marcus Portmann
 */
public class CryptoUtils
{
  /**
   * The AES block size.
   */
  public static final int AES_BLOCK_SIZE = 16;

  /**
   * The AES key size.
   */
  public static final int AES_KEY_SIZE = 32;

  /**
   * The AES key specification.
   */
  public static final String AES_KEY_SPEC = "AES";

  /**
   * The AES tranformation name.
   */
  public static final String AES_TRANSFORMATION_NAME = "AES/CFB8/NoPadding";
  private static SecureRandom secureRandom = new SecureRandom();

  /**
   * Creates a random encryption initialisation vector with the specified length.
   *
   * @param length the length of the random encryption initialisation vector
   *
   * @return the random encryption initialisation vector
   */
  public static byte[] createRandomEncryptionIV(int length)
  {
    byte[] encryptionIV = new byte[length];

    secureRandom.nextBytes(encryptionIV);

    return encryptionIV;
  }

  /**
   * Returns a randomly generated AES key.
   *
   * @return a randomly generated AES key
   */
  public static byte[] getRandomAESKey()
  {
    String randomPassword = new BigInteger(130, secureRandom).toString(32);

    return CryptoUtils.passwordToAESKey(randomPassword, UUID.randomUUID().toString());
  }

  /**
   * Load a key store.
   *
   * @param path     the path to the key store
   * @param password the key store password
   * @param type     the type of key store e.g. JKS, PKCS12, etc
   *
   * @return the key store that was loaded
   *
   * @throws GeneralSecurityException
   */
  public static KeyStore loadKeyStore(String path, String password, String type)
    throws GeneralSecurityException
  {
    InputStream input = null;

    try
    {
      PathMatchingResourcePatternResolver resourceLoader =
          new PathMatchingResourcePatternResolver();

      Resource keyStoreResource = resourceLoader.getResource(path);

      if (!keyStoreResource.exists())
      {
        throw new GeneralSecurityException("The key store (" + path + ") could not be found");
      }

      KeyStore ks = KeyStore.getInstance(type);

      input = keyStoreResource.getInputStream();

      ks.load(input,
          ((password == null) || (password.length() == 0))
          ? new char[0]
          : password.toCharArray());

      return ks;
    }
    catch (Throwable e)
    {
      throw new GeneralSecurityException("Failed to load the key store (" + path + ")", e);
    }
    finally
    {
      try
      {
        if (input != null)
        {
          input.close();
        }
      }
      catch (Throwable ignored) {}
    }
  }

  /**
   * Load a key store and query it to confirm a key pair with the specified alias is present.
   *
   * @param path     the path to the key store
   * @param alias    the alias for the key pair in the key store that should be retrieved
   * @param password the key store password
   * @param type     the type of key store e.g. JKS, PKCS12, etc
   *
   * @return the key store that was loaded
   *
   * @throws GeneralSecurityException
   */
  public static KeyStore loadKeyStore(String path, String alias, String password, String type)
    throws GeneralSecurityException
  {
    InputStream input = null;

    try
    {
      PathMatchingResourcePatternResolver resourceLoader =
          new PathMatchingResourcePatternResolver();

      Resource keyStoreResource = resourceLoader.getResource(path);

      if (!keyStoreResource.exists())
      {
        throw new GeneralSecurityException("The key store (" + path + ") could not be found");
      }

      KeyStore ks = KeyStore.getInstance(type);

      input = keyStoreResource.getInputStream();

      ks.load(input,
          ((password == null) || (password.length() == 0))
          ? new char[0]
          : password.toCharArray());

      // Attempt to retrieve the private key from the key store
      Key privateKey = ks.getKey(alias, password.toCharArray());

      if (privateKey == null)
      {
        throw new GeneralSecurityException("A private key with alias (" + alias
            + ") could not be found in the key store (" + path + ")");
      }

      // Attempt to retrieve the certificate from the key store
      java.security.cert.Certificate certificate = ks.getCertificate(alias);

      if (certificate == null)
      {
        throw new GeneralSecurityException("A certificate with alias (" + alias
            + ") could not be found in the key store (" + path + ")");
      }

      if (!(certificate instanceof X509Certificate))
      {
        throw new GeneralSecurityException("The certificate with alias (" + alias
            + ") is not an X509 certificate");
      }

      return ks;
    }
    catch (Throwable e)
    {
      throw new GeneralSecurityException("Failed to load and query the key store (" + path + ")",
          e);
    }
    finally
    {
      try
      {
        if (input != null)
        {
          input.close();
        }
      }
      catch (Throwable ignored) {}
    }
  }

  /**
   * Load the trust store.
   *
   * @param path     the path to the trust store
   * @param password the trust store password
   * @param type     the type of trust store e.g. JKS, PKCS12, etc
   *
   * @return the trust store that was loaded
   *
   * @throws GeneralSecurityException
   */
  public static KeyStore loadTrustStore(String path, String password, String type)
    throws GeneralSecurityException
  {
    KeyStore ks;

    InputStream input = null;

    try
    {
      PathMatchingResourcePatternResolver resourceLoader =
          new PathMatchingResourcePatternResolver();

      Resource trustStoreResource = resourceLoader.getResource(path);

      if (!trustStoreResource.exists())
      {
        throw new GeneralSecurityException("The trust store (" + path + ") could not be found");
      }

      ks = KeyStore.getInstance(type);

      input = trustStoreResource.getInputStream();

      ks.load(input,
          ((password == null) || (password.length() == 0))
          ? new char[0]
          : password.toCharArray());

      return ks;
    }
    catch (Throwable e)
    {
      throw new GeneralSecurityException("Failed to load the trust store (" + path + ")", e);
    }
    finally
    {
      try
      {
        if (input != null)
        {
          input.close();
        }
      }
      catch (Throwable ignored) {}
    }
  }

  /**
   * Convert the specified password to a 3DES key that can be used with the 3DES cypher encrypt and
   * decrypt functions.
   *
   * @param password the password to convert to a 3DES key
   *
   * @return the 3DES key
   */
  public static byte[] passwordTo3DESKey(String password)
    throws CryptoException
  {
    byte[] salt = "0907df13-2ef5-41a8-90e7-f08a3ca16af4".getBytes();

    return passwordTo3DESKey(password.getBytes(), salt, 4, 24);
  }

  /**
   * Convert the specified password to an AES key that can be used with the AES cypher encrypt and
   * decrypt functions.
   *
   * @param password the password to convert to an AES key
   *
   * @return the AES key
   */
  public static byte[] passwordToAESKey(String password)
    throws CryptoException
  {
    byte[] salt = "9aeabd0f-be94-486e-a693-ed2d553ea202".getBytes();

    return passwordToAESKey(password.getBytes(), salt, 4, AES_KEY_SIZE);
  }

  /**
   * Convert the specified password to an AES key that can be used with the AES cypher encrypt and
   * decrypt functions.
   *
   * @param password the password to convert to an AES key
   * @param salt     the salt to use when generating the AES key
   *
   * @return the AES key
   */
  public static byte[] passwordToAESKey(String password, byte[] salt)
    throws CryptoException
  {
    return passwordToAESKey(password.getBytes(), salt, 4, AES_KEY_SIZE);
  }

  /**
   * Convert the specified password to an AES key that can be used with the AES cypher encrypt and
   * decrypt functions.
   *
   * @param password the password to convert to an AES key
   * @param salt     the salt to use when generating the AES key
   *
   * @return the AES key
   */
  public static byte[] passwordToAESKey(String password, String salt)
    throws CryptoException
  {
    return passwordToAESKey(password.getBytes(), salt.getBytes(), 4, AES_KEY_SIZE);
  }

  private static byte[] hashPasswordAndSalt(byte[] password, byte[] salt, int iteration)
  {
    try
    {
      // Concatenate password and salt.
      byte[] pwAndSalt = new byte[password.length + salt.length];

      System.arraycopy(password, 0, pwAndSalt, 0, password.length);
      System.arraycopy(salt, 0, pwAndSalt, password.length, salt.length);

      // Create the key as sha1(sha1(sha1(sha1(...(pw+salt))...)
      MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");

      for (int i = 0; i < iteration; i++)
      {
        messageDigest.update(pwAndSalt, 0, pwAndSalt.length);
        messageDigest.digest(pwAndSalt, 0, messageDigest.getDigestLength());
      }

      return pwAndSalt;
    }
    catch (Throwable e)
    {
      throw new CryptoException("Failed to hash the password and key", e);
    }
  }

  private static byte[] passwordTo3DESKey(byte[] password, byte[] salt, int iteration, int keysize)
    throws CryptoException
  {
    try
    {
      byte[] key = new byte[keysize];

      System.arraycopy(hashPasswordAndSalt(password, salt, iteration), 0, key, 0, keysize);

      return key;
    }
    catch (Throwable e)
    {
      throw new CryptoException("Failed to convert the password to a 3DES key", e);
    }
  }

  private static byte[] passwordToAESKey(byte[] password, byte[] salt, int iteration, int keysize)
    throws CryptoException
  {
    try
    {
      byte[] key = new byte[keysize];

      System.arraycopy(hashPasswordAndSalt(password, salt, iteration), 0, key, 0, keysize);

      return key;
    }
    catch (Throwable e)
    {
      throw new CryptoException("Failed to convert the password to an AES key", e);
    }
  }
}

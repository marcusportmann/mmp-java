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

package guru.mmp.common.crypto;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.common.util.Base64;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.UUID;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>CryptUtils</code> class provides cryptography related utility functions.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public class CryptoUtils
{
  /**
   * The AES key size.
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
   * Main
   *
   * @param args the command-line arguments
   */
  public static void main(String args[])
  {
    SecureRandom secureRandom = new SecureRandom();

    secureRandom.setSeed(System.currentTimeMillis());

    byte[] randomAESKey = new byte[16];

    secureRandom.nextBytes(randomAESKey);

    System.out.println("Random AES key = [" + Base64.encodeBytes(randomAESKey) + "]");

    System.out.println("3DES Key for password (Password1) = ["
        + Base64.encodeBytes(passwordTo3DESKey("Password1")) + "]");
    System.out.println("AES Key for password (Password1) = ["
        + Base64.encodeBytes(passwordToAESKey("Password1")) + "]");

    // 3DES Key for password (Password1) = [3kmNuSkm8e6gYVuxFBuVEY6jBPlmNS00]
    // AES Key for password (Password1) = [8ckdUe0IGVPImdQ371V9ZA==]

    // [3kmNuSkm8e6gYVuxFBuVEY6jBPlmNS00]
    // [3kmNuSkm8e6gYVuxFBuVEY6jBPlmNS00]
  }

  /**
   * Convert the specified password to a 3DES key that can be used with the 3DES cypher encrypt and
   * decrypt functions.
   *
   * @param password the password to convert to a 3DES key
   *
   * @return the 3DES key
   *
   * @throws CryptoException
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
   *
   * @throws CryptoException
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
   *
   * @throws CryptoException
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
   *
   * @throws CryptoException
   */
  public static byte[] passwordToAESKey(String password, String salt)
    throws CryptoException
  {
    return passwordToAESKey(password.getBytes(), salt.getBytes(), 4, AES_KEY_SIZE);
  }

  private static byte[] passwordTo3DESKey(byte[] password, byte[] salt, int iteration, int keysize)
    throws CryptoException
  {
    try
    {
      // Concatenate password and salt.
      byte[] pwAndSalt = new byte[password.length + salt.length];

      System.arraycopy(password, 0, pwAndSalt, 0, password.length);
      System.arraycopy(salt, 0, pwAndSalt, password.length, salt.length);

      // Create the key as sha1(sha1(sha1(sha1(...(pw+salt))...)
      MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");

      for (int i = 0; i < iteration; i++)
      {
        messageDigest.update(pwAndSalt, 0, pwAndSalt.length);
        messageDigest.digest(pwAndSalt, 0, messageDigest.getDigestLength());
      }

      byte[] key = new byte[keysize];

      System.arraycopy(pwAndSalt, 0, key, 0, keysize);

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
      // Concatenate password and salt.
      byte[] pwAndSalt = new byte[password.length + salt.length];

      System.arraycopy(password, 0, pwAndSalt, 0, password.length);
      System.arraycopy(salt, 0, pwAndSalt, password.length, salt.length);

      // Create the key as sha1(sha1(sha1(sha1(...(pw+salt))...)
      MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");

      for (int i = 0; i < iteration; i++)
      {
        messageDigest.update(pwAndSalt, 0, pwAndSalt.length);
        messageDigest.digest(pwAndSalt, 0, messageDigest.getDigestLength());
      }

      byte[] key = new byte[keysize];

      System.arraycopy(pwAndSalt, 0, key, 0, keysize);

      return key;
    }
    catch (Throwable e)
    {
      throw new CryptoException("Failed to convert the password to an AES key", e);
    }
  }
}

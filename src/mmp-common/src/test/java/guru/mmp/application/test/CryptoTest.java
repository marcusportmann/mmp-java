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

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.common.crypto.CryptoException;
import guru.mmp.common.crypto.CryptoUtils;
import org.apache.ws.security.util.UUIDGenerator;
import org.junit.Test;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>CryptoTest</code> class.
 */
public class CryptoTest
{
  /**
   * The AES crypto test.
   */
  @Test
  public void aesTest()
    throws NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException,
        IllegalBlockSizeException, InvalidAlgorithmParameterException, InvalidKeyException,
        CryptoException
  {
    String dataToEncrypt = "This is the test data!";

    byte[] encryptionKey = CryptoUtils.getRandomAESKey();

    assertEquals("AES key is not " + CryptoUtils.AES_KEY_SIZE + " bytes", CryptoUtils.AES_KEY_SIZE,
        encryptionKey.length);

    byte[] encryptionIV = CryptoUtils.createRandomEncryptionIV(CryptoUtils.AES_BLOCK_SIZE);

    assertEquals("AES encryption IV is not " + CryptoUtils.AES_BLOCK_SIZE + " bytes", CryptoUtils
        .AES_BLOCK_SIZE, encryptionIV.length);

    SecretKey secretKey = new SecretKeySpec(encryptionKey, CryptoUtils.AES_KEY_SPEC);
    IvParameterSpec iv = new IvParameterSpec(encryptionIV);
    Cipher encryptCipher = Cipher.getInstance(CryptoUtils.AES_TRANSFORMATION_NAME);

    encryptCipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);

    byte[] encryptedData = encryptCipher.doFinal(dataToEncrypt.getBytes());

    Cipher decryptCipher = Cipher.getInstance(CryptoUtils.AES_TRANSFORMATION_NAME);

    decryptCipher.init(Cipher.DECRYPT_MODE, secretKey, iv);

    byte[] decryptedData = decryptCipher.doFinal(encryptedData);

    assertEquals("The decrypted data is invalid (" + new String(decryptedData) + ")",
        dataToEncrypt, new String(decryptedData));
  }

  /**
   * The password to 3DES key test.
   */
  @Test
  public void passwordTo3DESKeyTest()
  {
    CryptoUtils.passwordTo3DESKey("Password1");
  }

  /**
   * The password to AES key test.
   */
  @Test
  public void passwordToAESKeyTest()
  {
    Random random = new Random();

    byte[] salt = new byte[CryptoUtils.AES_KEY_SIZE];

    random.nextBytes(salt);

    CryptoUtils.passwordToAESKey("Password1", salt);

    CryptoUtils.passwordToAESKey("Password1");

    CryptoUtils.passwordToAESKey("Password1", UUID.randomUUID().toString());
  }
}

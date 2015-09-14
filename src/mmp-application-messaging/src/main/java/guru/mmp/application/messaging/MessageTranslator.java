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

package guru.mmp.application.messaging;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.common.crypto.CryptoUtils;
import guru.mmp.common.crypto.EncryptionScheme;
import guru.mmp.common.util.Base64;
import guru.mmp.common.util.StringUtil;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>MessageTranslator</code> class provides the facilities to create Messaging
 * Infrastructure messages containing WBXML-based or XML-based message data. It also provides
 * facilities to retrieve the WBXML-based or XML-based message data from a Messaging
 * Infrastructure message.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public class MessageTranslator
{
  private static ThreadLocal<MessageDigest> threadLocalMessageDigest =
    new ThreadLocal<MessageDigest>()
  {
    @Override
    protected MessageDigest initialValue()
    {
      try
      {
        return MessageDigest.getInstance("SHA-1");
      }
      catch (Throwable e)
      {
        throw new RuntimeException("Failed to initialise the SHA-1 message digest", e);
      }

    }

  };

  /**
   * The device ID identifying the device the message originated from.
   */
  private String device;

  /**
   * The encryption key used to encrypt or decrypt the message data.
   */
  private byte[] encryptionKey;

  /**
   * The encryption scheme used to encrypt or decrypt the message data.
   */
  private EncryptionScheme encryptionScheme;

  /**
   * The organisation code identifying the organisation associated with the message.
   */
  private String organisation;

  /**
   * The username uniquely identifying the user responsible for the message.
   */
  private String user;

  /**
   * Constructs a new <code>MessageTranslator</code>.
   *
   * @param user         the username uniquely identifying the user responsible for the message
   * @param organisation the organisation code identifying the organisation associated with the
   *                     message
   * @param device       the device ID identifying the device the message originated from
   */
  public MessageTranslator(String user, String organisation, String device)
  {
    this.user = user;
    this.organisation = organisation;
    this.device = device;
    this.encryptionScheme = EncryptionScheme.NONE;
    this.encryptionKey = null;
  }

  /**
   * Constructs a new <code>MessageTranslator</code>.
   *
   * @param user             the username uniquely identifying the user responsible for the message
   * @param organisation     the organisation code identifying the organisation associated with the
   *                         message
   * @param device           the device ID identifying the device the message originated from
   * @param encryptionScheme the encryption scheme used to encrypt or decrypt the message data
   * @param encryptionKey    the key used to encrypt or decrypt the message data
   */
  public MessageTranslator(String user, String organisation, String device,
      EncryptionScheme encryptionScheme, byte[] encryptionKey)
  {
    this.user = user;
    this.organisation = organisation;
    this.device = device;
    this.encryptionScheme = encryptionScheme;
    this.encryptionKey = encryptionKey;
  }

  /**
   * Decrypt the message data.
   *
   * @param encryptionScheme the encryption scheme used to decrypt the message data
   * @param encryptionKey    the encryption key to use to decrypt the message data
   * @param encryptionIV     the encryption initialisation vector
   * @param data             the message data to decrypt
   *
   * @return the decrypted message data
   *
   * @throws MessagingException
   */
  public static byte[] decryptMessageData(EncryptionScheme encryptionScheme, byte[] encryptionKey,
      byte[] encryptionIV, byte[] data)
    throws MessagingException
  {
    if ((encryptionKey == null) || (encryptionKey.length == 0))
    {
      throw new MessagingException("Failed to decrypt the message data: Invalid encryption key");
    }

    try
    {
      if (encryptionScheme == EncryptionScheme.AES_CFB)
      {
        SecretKey secretKey = new SecretKeySpec(encryptionKey, CryptoUtils.AES_KEY_SPEC);
        IvParameterSpec iv = new IvParameterSpec(encryptionIV);
        Cipher cipher = Cipher.getInstance(CryptoUtils.AES_TRANSFORMATION_NAME);

        cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);

        return cipher.doFinal(data);
      }
      else
      {
        throw new MessagingException("Unsupported encryption scheme (" + encryptionScheme.getName()
            + ")");
      }
    }
    catch (Throwable e)
    {
      throw new MessagingException("Failed to decrypt the message data", e);
    }
  }

  /**
   * Encrypt the message data.
   *
   * @param encryptionScheme the encryption scheme used to encrypt the message data
   * @param encryptionKey    the encryption key to use to encrypt the message data
   * @param encryptionIV     the encryption initialisation vector
   * @param data             the message data to encrypt
   *
   * @return the encrypted message data
   *
   * @throws MessagingException
   */
  public static byte[] encryptMessageData(EncryptionScheme encryptionScheme, byte[] encryptionKey,
      byte[] encryptionIV, byte[] data)
    throws MessagingException

  {
    if (encryptionKey == null)
    {
      throw new MessagingException("Failed to encrypt the message data: Invalid encryption key");
    }

    try
    {
      if (encryptionScheme == EncryptionScheme.AES_CFB)
      {
        SecretKey secretKey = new SecretKeySpec(encryptionKey, CryptoUtils.AES_KEY_SPEC);
        IvParameterSpec iv = new IvParameterSpec(encryptionIV);
        Cipher cipher = Cipher.getInstance(CryptoUtils.AES_TRANSFORMATION_NAME);

        cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);

        return cipher.doFinal(data);
      }
      else
      {
        throw new MessagingException("Unsupported encryption scheme (" + encryptionScheme.getName()
            + ")");
      }
    }
    catch (Throwable e)
    {
      throw new MessagingException("Failed to encrypt the message data", e);
    }
  }

  /**
   * Retrieve the WBXML-based message data from the message.
   *
   * @param message     the message
   * @param messageData the WBXML-based message data object to populate
   * @param <T>         the message data type for the WBXML-based message data
   *
   * @return the WBXML-based message data
   *
   * @throws MessagingException
   */
  public <T extends WbxmlMessageData> T fromMessage(Message message, T messageData)
    throws MessagingException
  {
    byte[] data = message.getData();

    // Decrypt the message if required
    if (message.isEncrypted())
    {
      data = decryptMessageData(encryptionScheme, encryptionKey,
          StringUtil.isNullOrEmpty(message.getEncryptionIV())
          ? new byte[0]
          : Base64.decode(message.getEncryptionIV()), message.getData());

      // Retrieve the SHA-1 hash of the unencrypted message data
      String dataHash = getMessageDataHash(data);

      if (!message.getDataHash().equals(dataHash))
      {
        throw new MessagingException("Failed to decrypt the message data since the data hash for"
            + " the message (" + message.getDataHash()
            + ") does not match the hash for the message data (" + dataHash + ")");
      }
    }

    // Check that the message type for the message data and the specified message match
    if (!messageData.getMessageType().equals(message.getType()))
    {
      throw new MessagingException("The message type for the message (" + message.getType()
          + ") does not match the message type for the message data ("
          + messageData.getMessageType() + ")");
    }

    /*
     * Populate the message data instance with the information contained in the WBXML data for the
     * message.
     */
    if (messageData.fromMessageData(message.getType(), message.getTypeVersion(), data))
    {
      return messageData;
    }
    else
    {
      throw new MessagingException("Failed to populate the instance of the message data class ("
          + messageData.getClass().getName() + ") from the WBXML data for the message");
    }
  }

  /**
   * Returns the encryption scheme used to encrypt or decrypt the message data.
   *
   * @return the encryption scheme used to encrypt or decrypt the message data
   */
  public EncryptionScheme getEncryptionScheme()
  {
    return encryptionScheme;
  }

  /**
   * Set the encryption scheme used to encrypt or decrypt the message data.
   *
   * @param encryptionScheme the encryption scheme used to encrypt or decrypt the message data
   */
  public void setEncryptionScheme(EncryptionScheme encryptionScheme)
  {
    this.encryptionScheme = encryptionScheme;
  }

  /**
   * Returns the message containing the WBXML-based message data.
   *
   * @param messageData the WBXML-based message data
   *
   * @return the message that can be sent via the messaging infrastructure
   *
   * @throws MessagingException
   */
  public Message toMessage(WbxmlMessageData messageData)
    throws MessagingException
  {
    return toMessage(messageData, "");
  }

  /**
   * Returns the message containing the WBXML-based message data.
   *
   * @param messageData   the WBXML-based message data
   * @param correlationId the Universally Unique Identifier (UUID) used to correlate the message
   *
   * @return the message that can be sent via the messaging infrastructure
   *
   * @throws MessagingException
   */
  public Message toMessage(WbxmlMessageData messageData, String correlationId)
    throws MessagingException
  {
    if (StringUtil.isNullOrEmpty(user))
    {
      throw new MessagingException("Failed to create the message with type ("
          + messageData.getMessageType() + ") and version (" + messageData.getMessageTypeVersion()
          + "): A user has not been specified");
    }

    if (StringUtil.isNullOrEmpty(organisation))
    {
      throw new MessagingException("Failed to create the message with type ("
          + messageData.getMessageType() + ") and version (" + messageData.getMessageTypeVersion()
          + "): An organisation has not been specified");
    }

    if (StringUtil.isNullOrEmpty(device))
    {
      throw new MessagingException("Failed to create the message with type ("
          + messageData.getMessageType() + ") and version (" + messageData.getMessageTypeVersion()
          + "): A device ID has not been specified");
    }

    byte[] data = messageData.toMessageData();

    // Encrypt the message data
    if (encryptionKey != null)
    {
      // Retrieve the SHA-1 hash of the unencrypted message data
      String dataHash = getMessageDataHash(data);

      byte[] encryptionIV = new byte[0];

      if (encryptionScheme == EncryptionScheme.AES_CFB)
      {
        encryptionIV = CryptoUtils.createRandomEncryptionIV(CryptoUtils.AES_BLOCK_SIZE);
      }

      data = encryptMessageData(encryptionScheme, encryptionKey, encryptionIV, data);

      return new Message(user, organisation, device, messageData.getMessageType(),
          messageData.getMessageTypeVersion(), correlationId, messageData.getMessageTypePriority(),
          data, dataHash, encryptionScheme, (encryptionIV.length == 0)
          ? ""
          : Base64.encodeBytes(encryptionIV));
    }
    else
    {
      return new Message(user, organisation, device, messageData.getMessageType(),
          messageData.getMessageTypeVersion(), correlationId, messageData.getMessageTypePriority(),
          data);
    }
  }

  /**
   * Generate the SHA-1 hash for the message data.
   *
   * @param data the message data to return the SHA-1 hash for
   *
   * @return the SHA-1 hash for the message data
   *
   * @throws MessagingException
   */
  protected String getMessageDataHash(byte[] data)
    throws MessagingException
  {
    try
    {
      return Base64.encodeBytes(threadLocalMessageDigest.get().digest(data));
    }
    catch (Throwable e)
    {
      throw new MessagingException("Failed to generate the SHA-1 hash for the message data", e);
    }
  }
}

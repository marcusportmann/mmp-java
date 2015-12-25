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

package guru.mmp.application.messaging.message;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.messaging.Message;
import guru.mmp.application.messaging.MessagingException;
import guru.mmp.application.messaging.WbxmlMessageData;
import guru.mmp.common.crypto.EncryptionScheme;
import guru.mmp.common.util.StringUtil;
import guru.mmp.common.wbxml.Document;
import guru.mmp.common.wbxml.Element;
import guru.mmp.common.wbxml.Encoder;

import java.util.UUID;

/**
 * The <code>RegisterResponseData</code> class manages the data for a
 * "Register Response" message.
 * <p/>
 * This is a synchronous message.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public class RegisterResponseData extends WbxmlMessageData
{
  /**
   * The error code returned when authentication fails.
   */
  public static final int ERROR_CODE_AUTHENTICATION_FAILED = -2;

  /**
   * The error code returned when an unknown error occurs during registration.
   */
  public static final int ERROR_CODE_UNKNOWN_ERROR = -1;

  /**
   * The message returned when the registration was successful during registration.
   */
  public static final String ERROR_MESSAGE_SUCCESS = "Success";

  /**
   * The UUID for the "Register Response" message.
   */
  public static final UUID MESSAGE_TYPE_ID = UUID.fromString("aa08aac9-4d15-452f-b3f9-756641b71735");

  /**
   * The error code indicating the result of processing the registration where a code of '0'
   * indicates success and a non-zero code indicates an error condition.
   */
  private int errorCode;

  /**
   * The error message describing the result of processing the registration.
   */
  private String errorMessage;

  /**
   * The encryption key used to encrypt data on the user's device and any data passed as part of a
   * message.
   */
  private byte[] userEncryptionKey;

  /**
   * The encryption scheme for the user's encryption key.
   */
  private EncryptionScheme userEncryptionScheme;

  /**
   * Constructs a new <code>RegisterResponseData</code>.
   */
  public RegisterResponseData()
  {
    super(MESSAGE_TYPE_ID, 1, Message.Priority.HIGH);
  }

  /**
   * Constructs a new <code>RegisterResponseData</code>.
   *
   * @param userEncryptionScheme the encryption scheme for the user's encryption key
   * @param userEncryptionKey    the encryption key used to encrypt data on the user's device and
   *                             any data passed as part of a message
   */
  public RegisterResponseData(EncryptionScheme userEncryptionScheme, byte[] userEncryptionKey)
  {
    super(MESSAGE_TYPE_ID, 1, Message.Priority.HIGH);

    this.errorCode = 0;
    this.errorMessage = ERROR_MESSAGE_SUCCESS;
    this.userEncryptionScheme = userEncryptionScheme;
    this.userEncryptionKey = userEncryptionKey;
  }

  /**
   * Constructs a new <code>RegisterResponseData</code>.
   *
   * @param errorCode    the error code
   * @param errorMessage the error message
   */
  public RegisterResponseData(int errorCode, String errorMessage)
  {
    super(MESSAGE_TYPE_ID, 1, Message.Priority.HIGH);

    this.errorCode = errorCode;
    this.errorMessage = errorMessage;
    this.userEncryptionScheme = EncryptionScheme.NONE;
    this.userEncryptionKey = new byte[0];
  }

  /**
   * Extract the message data from the WBXML data for a message.
   *
   * @param messageType        the UUID identifying the type of message the message data is
   *                           associated with
   * @param messageTypeVersion the version of the message type the message data is associated with
   * @param messageData        the WBXML data for the message
   *
   * @return <code>true</code> if the message data was extracted successfully from the
   *         WBXML data or <code>false</code> otherwise
   *
   * @throws MessagingException
   */
  public boolean fromMessageData(String messageType, int messageTypeVersion, byte[] messageData)
    throws MessagingException
  {
    Document document = parseWBXML(messageData);

    Element rootElement = document.getRootElement();

    if (!rootElement.getName().equals("RegisterResponse"))
    {
      return false;
    }

    if ((!rootElement.hasChild("UserEncryptionScheme"))
        || (!rootElement.hasChild("UserEncryptionKey")) || (!rootElement.hasChild("ErrorCode"))
        || (!rootElement.hasChild("ErrorMessage")))
    {
      return false;
    }

    try
    {
      this.errorCode = Integer.parseInt(rootElement.getChildText("ErrorCode"));
    }
    catch (Throwable e)
    {
      return false;
    }

    this.errorMessage = rootElement.getChildText("ErrorMessage");

    try
    {
      this.userEncryptionScheme = EncryptionScheme.fromCode(
        Integer.parseInt(rootElement.getChildText("UserEncryptionScheme")));
    }
    catch (Throwable e)
    {
      throw new MessagingException("Failed to retrieve the user encryption scheme from the"
          + " message data", e);
    }

    this.userEncryptionKey = rootElement.getChildOpaque("UserEncryptionKey");

    return true;
  }

  /**
   * Returns the error code indicating the result of processing the registration where a code of
   * '0' indicates success and a non-zero code indicates an error condition.
   *
   * @return the error code indicating the result of processing the registration where a code of
   *         '0' indicates success and a non-zero code indicates an error condition
   */
  public int getErrorCode()
  {
    return errorCode;
  }

  /**
   * Returns the error message describing the result of processing the registration.
   *
   * @return the error message describing the result of processing the registration
   */
  public String getErrorMessage()
  {
    return errorMessage;
  }

  /**
   * Returns the encryption key used to encrypt data on the user's device and any data passed
   * as part of a message.
   *
   * @return the encryption key used to encrypt data on the user's device and data passed as
   *         part of a message
   */
  public byte[] getUserEncryptionKey()
  {
    return userEncryptionKey;
  }

  /**
   * Returns the encryption scheme for the user's encryption key.
   *
   * @return the encryption scheme for the user's encryption key
   */
  public EncryptionScheme getUserEncryptionScheme()
  {
    return userEncryptionScheme;
  }

  /**
   * Set the error code indicating the result of processing the registration where a code of
   * '0' indicates success and a non-zero code indicates an error condition.
   *
   * @param errorCode the error code indicating the result of processing the registration where
   *                  a code of '0' indicates success and a non-zero code indicates an error
   *                  condition
   */
  public void setErrorCode(int errorCode)
  {
    this.errorCode = errorCode;
  }

  /**
   * Set the error message describing the result of processing the registration.
   *
   * @param errorMessage the error message describing the result of processing the registration
   */
  public void setErrorMessage(String errorMessage)
  {
    this.errorMessage = errorMessage;
  }

  /**
   * Set the encryption key used to encrypt data on the user's device and any data passed as
   * part of a message.
   *
   * @param userEncryptionKey the encryption key used to encrypt data on the user's device
   *                          and any data passed as part of a message
   */
  public void setUserEncryptionKey(byte[] userEncryptionKey)
  {
    this.userEncryptionKey = userEncryptionKey;
  }

  /**
   * Set the encryption scheme for the user's encryption key.
   *
   * @param userEncryptionScheme the encryption scheme for the user's encryption key
   */
  public void setUserEncryptionScheme(EncryptionScheme userEncryptionScheme)
  {
    this.userEncryptionScheme = userEncryptionScheme;
  }

  /**
   * Returns the WBXML data representation of the message data that will be sent as part of a
   * message.
   *
   * @return the WBXML data representation of the message data that will be sent as part of a
   *         message
   *
   * @throws MessagingException
   */
  public byte[] toMessageData()
    throws MessagingException
  {
    Element rootElement = new Element("RegisterResponse");

    rootElement.addContent(new Element("ErrorCode", String.valueOf(errorCode)));
    rootElement.addContent(new Element("ErrorMessage", StringUtil.notNull(errorMessage)));
    rootElement.addContent(new Element("UserEncryptionScheme",
        String.valueOf(userEncryptionScheme.getCode())));

    rootElement.addContent(new Element("UserEncryptionKey", userEncryptionKey));

    Document document = new Document(rootElement);

    Encoder encoder = new Encoder(document);

    return encoder.getData();
  }
}

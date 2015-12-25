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

//~--- JDK imports ------------------------------------------------------------

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * The <code>AuthenticateResponseData</code> class manages the data for a
 * "Authenticate Response" message.
 * <p/>
 * This is a synchronous message.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public class AuthenticateResponseData extends WbxmlMessageData
{
  /**
   * The error code returned when an unknown error occurs during authentication.
   */
  public static final int ERROR_CODE_UNKNOWN_ERROR = -1;

  /**
   * The message returned when authentication was successful.
   */
  public static final String ERROR_MESSAGE_SUCCESS = "Success";

  /**
   * The UUID for the "Authenticate Response" message.
   */
  public static final UUID MESSAGE_TYPE_ID =
    UUID.fromString("82223035-1726-407f-8703-3977708e792c");

  /**
   * The error code indicating the result of processing the authentication where a code of '0'
   * indicates success and a non-zero code indicates an error condition.
   */
  private int errorCode;

  /**
   * The error message describing the result of processing the authentication.
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
   * The properties returned for the authenticated user.
   */
  private Map<String, Object> userProperties;

  /**
   * Constructs a new <code>AuthenticateResponseData</code>.
   */
  public AuthenticateResponseData()
  {
    super(MESSAGE_TYPE_ID, 1, Message.Priority.HIGH);
  }

  /**
   * Constructs a new <code>AuthenticateResponseData</code>.
   *
   * @param userEncryptionScheme the encryption scheme for the user's encryption key
   * @param userEncryptionKey    the encryption key used to encrypt data on the user's device and
   *                             any data passed as part of a message
   */
  public AuthenticateResponseData(EncryptionScheme userEncryptionScheme, byte[] userEncryptionKey)
  {
    super(MESSAGE_TYPE_ID, 1, Message.Priority.HIGH);

    this.errorCode = 0;
    this.errorMessage = ERROR_MESSAGE_SUCCESS;
    this.userEncryptionScheme = userEncryptionScheme;
    this.userEncryptionKey = userEncryptionKey;
    this.userProperties = new HashMap<>();
  }

  /**
   * Constructs a new <code>AuthenticateResponseData</code>.
   *
   * @param errorCode    the error code
   * @param errorMessage the error message
   */
  public AuthenticateResponseData(int errorCode, String errorMessage)
  {
    super(MESSAGE_TYPE_ID, 1, Message.Priority.HIGH);

    this.errorCode = errorCode;
    this.errorMessage = errorMessage;
    this.userEncryptionScheme = EncryptionScheme.NONE;
    this.userEncryptionKey = new byte[0];
    this.userProperties = new HashMap<>();
  }

  /**
   * Constructs a new <code>AuthenticateResponseData</code>.
   *
   * @param userEncryptionScheme the encryption scheme for the user's encryption key
   * @param userEncryptionKey    the encryption key used to encrypt data on the user's device and
   *                             any data passed as part of a message
   * @param userProperties       the properties returned for the authenticated user
   */
  public AuthenticateResponseData(EncryptionScheme userEncryptionScheme, byte[] userEncryptionKey,
      Map<String, Object> userProperties)
  {
    super(MESSAGE_TYPE_ID, 1, Message.Priority.HIGH);

    this.errorCode = 0;
    this.errorMessage = ERROR_MESSAGE_SUCCESS;
    this.userEncryptionScheme = userEncryptionScheme;
    this.userEncryptionKey = userEncryptionKey;
    this.userProperties = userProperties;
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

    if (!rootElement.getName().equals("AuthenticateResponse"))
    {
      return false;
    }

    if ((!rootElement.hasChild("UserEncryptionKey")) || (!rootElement.hasChild("ErrorCode"))
        || (!rootElement.hasChild("ErrorMessage"))
        || (!rootElement.hasChild("UserEncryptionScheme")))
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

    this.userEncryptionKey = rootElement.getChildOpaque("UserEncryptionKey");

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

    this.userProperties = new HashMap<>();

    if (rootElement.hasChild("UserProperties"))
    {
      Element userPropertiesElement = rootElement.getChild("UserProperties");

      List<Element> userPropertyElements = userPropertiesElement.getChildren("UserProperty");

      for (Element userPropertyElement : userPropertyElements)
      {
        try
        {
          String userPropertyName = userPropertyElement.getAttributeValue("name");
          int userPropertyType = Integer.parseInt(userPropertyElement.getAttributeValue("type"));

          if (userPropertyType == 0)
          {
            this.userProperties.put(userPropertyName, userPropertyElement.getText());
          }
          else
          {
            throw new MessagingException("Failed to read the user property (" + userPropertyName
                + ") with unknown type (" + userPropertyType + ") from the message data");
          }
        }
        catch (Throwable e)
        {
          throw new MessagingException("Failed to read the user property from the message data", e);
        }
      }
    }

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
   * Returns the properties returned for the authenticated user.
   *
   * @return the properties returned for the authenticated user
   */
  public Map<String, Object> getUserProperties()
  {
    return userProperties;
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
   * Set the properties returned for the authenticated user.
   *
   * @param userProperties the properties returned for the authenticated user
   */
  public void setUserProperties(Map<String, Object> userProperties)
  {
    this.userProperties = userProperties;
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
    Element rootElement = new Element("AuthenticateResponse");

    rootElement.addContent(new Element("ErrorCode", String.valueOf(errorCode)));
    rootElement.addContent(new Element("ErrorMessage", StringUtil.notNull(errorMessage)));
    rootElement.addContent(new Element("UserEncryptionKey", userEncryptionKey));
    rootElement.addContent(new Element("UserEncryptionScheme",
        String.valueOf(userEncryptionScheme.getCode())));

    if ((userProperties != null) && (userProperties.size() > 0))
    {
      Element userPropertiesElement = new Element("UserProperties");

      for (String userPropertyName : userProperties.keySet())
      {
        Object userPropertyValue = userProperties.get(userPropertyName);

        if (userPropertyValue instanceof String)
        {
          Element userPropertyElement = new Element("UserProperty");

          userPropertyElement.setAttribute("name", userPropertyName);
          userPropertyElement.setAttribute("type", "0");
          userPropertyElement.setText((String) userPropertyValue);
          userPropertiesElement.addContent(userPropertyElement);
        }
      }

      rootElement.addContent(userPropertiesElement);
    }

    Document document = new Document(rootElement);

    Encoder encoder = new Encoder(document);

    return encoder.getData();
  }
}

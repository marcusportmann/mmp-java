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
import guru.mmp.common.util.StringUtil;
import guru.mmp.common.wbxml.Document;
import guru.mmp.common.wbxml.Element;
import guru.mmp.common.wbxml.Encoder;

//~--- JDK imports ------------------------------------------------------------

import java.util.UUID;

/**
 * The <code>CheckUserExistsResponseData</code> class manages the data for a
 * "Check User Exists Response" message.
 * <p/>
 * This is a synchronous message.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public class CheckUserExistsResponseData extends WbxmlMessageData
{
  /**
   * The error code returned to indicate success.
   */
  public static final int ERROR_CODE_SUCCESS = 0;

  /**
   * The error code returned when an unknown error occurred.
   */
  public static final int ERROR_CODE_UNKNOWN_ERROR = -1;

  /**
   * The message returned to indicate success.
   */
  public static final String ERROR_MESSAGE_SUCCESS = "Success";

  /**
   * The UUID for the "Check User Exists Response" message.
   */
  public static final UUID MESSAGE_TYPE_ID =
    UUID.fromString("a38bd55e-3470-46f1-a96a-a6b08a9adc63");

  /**
   * The error code;
   */
  private int errorCode;

  /**
   * The error message.
   */
  private String errorMessage;

  /**
   * <code>true</code> if the user exists or <code>false</code> otherwise.
   */
  private boolean userExists;

  /**
   * Constructs a new <code>CheckUserExistsResponseData</code>.
   *
   * @param userExists <code>true</code> if the user exists or <code>false</code> otherwise
   */
  public CheckUserExistsResponseData(boolean userExists)
  {
    super(MESSAGE_TYPE_ID, 1, Message.Priority.HIGH);

    this.userExists = userExists;
    this.errorCode = ERROR_CODE_SUCCESS;
    this.errorMessage = ERROR_MESSAGE_SUCCESS;
  }

  /**
   * Constructs a new <code>CheckUserExistsResponseData</code>.
   *
   * @param errorCode    the error code
   * @param errorMessage the error message
   */
  public CheckUserExistsResponseData(int errorCode, String errorMessage)
  {
    super(MESSAGE_TYPE_ID, 1, Message.Priority.HIGH);

    this.errorCode = errorCode;
    this.errorMessage = errorMessage;
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

    if (!rootElement.getName().equals("CheckUserExistsResponse"))
    {
      return false;
    }

    if ((!rootElement.hasChild("ErrorCode")) || (!rootElement.hasChild("ErrorMessage"))
        || (!rootElement.hasChild("UserExists")))
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
      this.userExists = Boolean.parseBoolean(rootElement.getChildText("UserExists"));
    }
    catch (Throwable e)
    {
      return false;
    }

    return true;
  }

  /**
   * Returns the error code.
   *
   * @return the error code
   */
  public int getErrorCode()
  {
    return errorCode;
  }

  /**
   * Returns the error message.
   *
   * @return the error message
   */
  public String getErrorMessage()
  {
    return errorMessage;
  }

  /**
   * Returns <code>true</code> if the user exists or <code>false</code> otherwise.
   *
   * @return <code>true</code> if the user exists or <code>false</code> otherwise
   */
  public boolean getUserExists()
  {
    return userExists;
  }

  /**
   * Set the error code.
   *
   * @param errorCode the error code
   */
  public void setErrorCode(int errorCode)
  {
    this.errorCode = errorCode;
  }

  /**
   * Set the error message.
   *
   * @param errorMessage the error message
   */
  public void setErrorMessage(String errorMessage)
  {
    this.errorMessage = errorMessage;
  }

  /**
   * Set whether the user exists.
   *
   * @param userExists <code>true</code> if the user exists or <code>false</code> otherwise
   */
  public void setUserExists(boolean userExists)
  {
    this.userExists = userExists;
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
    Element rootElement = new Element("CheckUserExistsResponse");

    rootElement.addContent(new Element("ErrorCode", String.valueOf(errorCode)));
    rootElement.addContent(new Element("ErrorMessage", StringUtil.notNull(errorMessage)));
    rootElement.addContent(new Element("UserExists", String.valueOf(userExists)));

    Document document = new Document(rootElement);

    Encoder encoder = new Encoder(document);

    return encoder.getData();
  }
}

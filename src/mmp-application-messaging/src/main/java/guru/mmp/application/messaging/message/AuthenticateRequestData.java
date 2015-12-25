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
 * The <code>AuthenticateRequestData</code> class manages the data for a
 * "Authenticate Request" message.
 * <p/>
 * This is a synchronous message.
 *
 * @author Marcus Portmann
 */
public class AuthenticateRequestData extends WbxmlMessageData
{
  /**
   * The UUID for the "Authenticate Request" message.
   */
  public static final UUID MESSAGE_TYPE_ID =
    UUID.fromString("d21fb54e-5c5b-49e8-881f-ce00c6ced1a3");

  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the device the
   * authentication request originated from.
   */
  private UUID deviceId;

  /**
   * The password used to authenticate the user.
   */
  private String password;

  /**
   * The username identifying the user associated with the message.
   */
  private String user;

  /**
   * Constructs a new <code>AuthenticateRequestData</code>.
   */
  public AuthenticateRequestData()
  {
    super(MESSAGE_TYPE_ID, Message.Priority.HIGH);
  }

  /**
   * Constructs a new <code>AuthenticateRequestData</code>.
   *
   * @param user     the username identifying the user associated with the message
   * @param password the password used to authenticate the user
   * @param deviceId the Universally Unique Identifier (UUID) used to uniquely identify the device
   *                 the authentication request originated from
   */
  public AuthenticateRequestData(String user, String password, UUID deviceId)
  {
    super(MESSAGE_TYPE_ID, Message.Priority.HIGH);

    this.deviceId = deviceId;
    this.password = password;
    this.user = user;
  }

  /**
   * Extract the message data from the WBXML data for a message.
   *
   * @param messageData the WBXML data for the message
   *
   * @return <code>true</code> if the message data was extracted successfully from the WBXML data or
   *         <code>false</code> otherwise
   *
   * @throws MessagingException
   */
  public boolean fromMessageData(byte[] messageData)
    throws MessagingException
  {
    Document document = parseWBXML(messageData);

    Element rootElement = document.getRootElement();

    if (!rootElement.getName().equals("AuthenticateRequest"))
    {
      return false;
    }

    if ((!rootElement.hasChild("User")) || (!rootElement.hasChild("Password"))
        || (!rootElement.hasChild("Device"))
        || (!rootElement.hasChild("PreferredEncryptionScheme")))
    {
      return false;
    }

    this.deviceId = UUID.fromString(rootElement.getChildText("DeviceId"));
    this.password = rootElement.getChildText("Password");
    this.user = rootElement.getChildText("User");

    return true;
  }

  /**
   * Returns the Universally Unique Identifier (UUID) used to uniquely identify the device the
   * authentication request originated from.
   *
   * @return the Universally Unique Identifier (UUID) used to uniquely identify the device the
   *         authentication request originated from
   */
  public UUID getDeviceId()
  {
    return deviceId;
  }

  /**
   * Returns the password used to authenticate the user.
   *
   * @return the password used to authenticate the user
   */
  public String getPassword()
  {
    return password;
  }

  /**
   * Returns the username identifying the user associated with the message.
   *
   * @return the username identifying the user associated with the message
   */
  public String getUser()
  {
    return user;
  }

  /**
   * Set the Universally Unique Identifier (UUID) used to uniquely identify the device the
   * authentication request originated from.
   *
   * @param deviceId the Universally Unique Identifier (UUID) used to uniquely identify the device
   *                 the authentication request originated from
   */
  public void setDevice(UUID deviceId)
  {
    this.deviceId = deviceId;
  }

  /**
   * Set the password used to authenticate the user.
   *
   * @param password the password used to authenticate the user
   */
  public void setPassword(String password)
  {
    this.password = password;
  }

  /**
   * Set the username identifying the user associated with the message.
   *
   * @param user the username identifying the user associated with the message
   */
  public void setUser(String user)
  {
    this.user = user;
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
    Element rootElement = new Element("AuthenticateRequest");

    rootElement.addContent(new Element("DeviceId", deviceId.toString()));
    rootElement.addContent(new Element("Password", StringUtil.notNull(password)));
    rootElement.addContent(new Element("User", StringUtil.notNull(user)));

    Document document = new Document(rootElement);

    Encoder encoder = new Encoder(document);

    return encoder.getData();
  }
}

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

/**
 * The <code>AuthenticateRequestData</code> class manages the data for a
 * "Authenticate Request" message.
 * <p/>
 * This is a synchronous message.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public class AuthenticateRequestData extends WbxmlMessageData
{
  /**
   * The UUID for the "Authenticate Request" message.
   */
  public static final String MESSAGE_TYPE = "d21fb54e-5c5b-49e8-881f-ce00c6ced1a3";

  /**
   * The device ID identifying the device.
   */
  private String device;

  /**
   * The organisation code identifying the organisation associated with the message.
   */
  private String organisation;

  /**
   * The password used to authenticate the user.
   */
  private String password;

  /**
   * The preferred encryption scheme for the user.
   */
  private EncryptionScheme preferredEncryptionScheme;

  /**
   * The username identifying the user associated with the message.
   */
  private String user;

  /**
   * Constructs a new <code>AuthenticateRequestData</code>.
   */
  public AuthenticateRequestData()
  {
    super(MESSAGE_TYPE, 1, Message.Priority.HIGH);
  }

  /**
   * Constructs a new <code>AuthenticateRequestData</code>.
   *
   * @param user                      the username identifying the user associated with the message
   * @param organisation              the organisation code identifying the organisation associated
   *                                  with the message
   * @param password                  the password used to authenticate the user
   * @param device                    the device ID identifying the device
   * @param preferredEncryptionScheme the preferred encryption scheme for the user
   */
  public AuthenticateRequestData(String user, String organisation, String password, String device,
      EncryptionScheme preferredEncryptionScheme)
  {
    super(MESSAGE_TYPE, 1, Message.Priority.HIGH);

    this.device = device;
    this.organisation = organisation;
    this.password = password;
    this.preferredEncryptionScheme = preferredEncryptionScheme;
    this.user = user;
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

    if (!rootElement.getName().equals("AuthenticateRequest"))
    {
      return false;
    }

    // TODO: Make PreferredEncryptionScheme mandatory -- MARCUS
    if ((!rootElement.hasChild("User")) || (!rootElement.hasChild("Password"))
        || (!rootElement.hasChild("Device"))
        || (!rootElement.hasChild("PreferredEncryptionScheme"))
        || (!rootElement.hasChild("Organisation")))
    {
      return false;
    }

    this.device = rootElement.getChildText("Device");
    this.organisation = rootElement.getChildText("Organisation");
    this.password = rootElement.getChildText("Password");

    try
    {
      this.preferredEncryptionScheme = EncryptionScheme.fromCode(
        Integer.parseInt(rootElement.getChildText("PreferredEncryptionScheme")));
    }
    catch (Throwable e)
    {
      throw new MessagingException("Failed to retrieve the preferred encryption scheme from"
          + " the message data", e);
    }

    this.user = rootElement.getChildText("User");

    return true;
  }

  /**
   * Returns the device ID identifying the device.
   *
   * @return the device ID identifying the device
   */
  public String getDevice()
  {
    return device;
  }

  /**
   * Returns the organisation code identifying the organisation associated with the message.
   *
   * @return the organisation code identifying the organisation associated with the message
   */
  public String getOrganisation()
  {
    return organisation;
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
   * Returns the preferred encryption scheme for the user.
   *
   * @return the preferred encryption scheme for the user
   */
  public EncryptionScheme getPreferredEncryptionScheme()
  {
    return preferredEncryptionScheme;
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
   * Set the device ID identifying the device.
   *
   * @param device the device ID identifying the device
   */
  public void setDevice(String device)
  {
    this.device = device;
  }

  /**
   * Set the organisation code identifying the organisation associated with the message.
   *
   * @param organisation the organisation code identifying the organisation associated with the
   *                     message
   */
  public void setOrganisation(String organisation)
  {
    this.organisation = organisation;
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
   * Set the preferred encryption scheme for the user.
   *
   * @param preferredEncryptionScheme the preferred encryption scheme for the user
   */
  public void setPreferredEncryptionScheme(EncryptionScheme preferredEncryptionScheme)
  {
    this.preferredEncryptionScheme = preferredEncryptionScheme;
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

    rootElement.addContent(new Element("Device", StringUtil.notNull(device)));
    rootElement.addContent(new Element("Organisation", StringUtil.notNull(organisation)));
    rootElement.addContent(new Element("Password", StringUtil.notNull(password)));
    rootElement.addContent(new Element("PreferredEncryptionScheme",
        String.valueOf(preferredEncryptionScheme.getCode())));
    rootElement.addContent(new Element("User", StringUtil.notNull(user)));

    Document document = new Document(rootElement);

    Encoder encoder = new Encoder(document);

    return encoder.getData();
  }
}

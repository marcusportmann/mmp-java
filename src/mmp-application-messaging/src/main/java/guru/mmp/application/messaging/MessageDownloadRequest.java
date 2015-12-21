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

import guru.mmp.common.crypto.EncryptionScheme;
import guru.mmp.common.util.StringUtil;
import guru.mmp.common.wbxml.Document;
import guru.mmp.common.wbxml.Element;
import guru.mmp.common.wbxml.Encoder;

/**
 * The <code>MessageDownloadRequest</code> class represents a request sent by the Messaging
 * Infrastructure on a mobile device to download the queued messages for the device from the
 * server-side messaging infrastructure.
 *
 * NOTE: No information in the download request is encrypted and the request itself is not
 * authenticated. This is because the queued messages returned by the server-side Messaging
 * Infrastructure will themselves be encrypted.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public class MessageDownloadRequest
{
  /**
   * The device ID identifying the device the message download request originated from.
   */
  private String device;

  /**
   * The encryption scheme that should be used to secure the downloaded messages.
   */
  private EncryptionScheme messageEncryptionScheme;

  /**
   * The username identifying the user whose messages should be downloaded or blank if the messages
   * for all the users linked to the device should be downloaded.
   */
  private String user;

  /**
   * Constructs a new <code>MessageDownloadRequest</code> and populates it from the information
   * stored in the specified WBXML document.
   *
   * @param document the WBXML document containing the message download information
   *
   * @throws MessagingException
   */
  public MessageDownloadRequest(Document document)
    throws MessagingException
  {
    Element rootElement = document.getRootElement();

    this.device = rootElement.getAttributeValue("device");

    if (rootElement.hasAttribute("user"))
    {
      this.user = rootElement.getAttributeValue("user");
    }
    else
    {
      this.user = "";
    }

    try
    {
      this.messageEncryptionScheme = EncryptionScheme.fromCode(
        Integer.parseInt(rootElement.getAttributeValue("messageEncryptionScheme")));
    }
    catch (Throwable e)
    {
      throw new MessagingException("Failed to retrieve the message encryption scheme from"
          + " the message download request data", e);
    }
  }

  /**
   * Constructs a new <code>MessageDownloadRequest</code>.
   *
   * @param device                  the device ID identifying the device the message download
   *                                request originated from
   * @param user                    the username identifying the user whose messages should be
   *                                downloaded or blank if the messages for all the users linked to
   *                                the device should be downloaded
   * @param messageEncryptionScheme the encryption scheme that should be used to secure the
   *                                downloaded messages
   */
  public MessageDownloadRequest(String device, String user,
      EncryptionScheme messageEncryptionScheme)
  {
    this.device = device;
    this.user = user;
    this.messageEncryptionScheme = messageEncryptionScheme;
  }

  /**
   * Returns <code>true</code> if the WBXML document contains valid message download request
   * information or <code>false</code> otherwise.
   *
   * @param document the WBXML document to validate
   *
   * @return <code>true</code> if the WBXML document contains valid message download request
   *         information or <code>false</code> otherwise
   */
  public static boolean isValidWBXML(Document document)
  {
    Element rootElement = document.getRootElement();

    return rootElement.getName().equals("MessageDownloadRequest")
        && !((rootElement.getAttributes().size() != 1)
          && (rootElement.getAttributes().size() != 3)) && !((!rootElement.hasAttribute("device"))
            || (!rootElement.hasAttribute("messageEncryptionScheme")));

  }

  /**
   * Returns the device ID identifying the device the message download request originated from.
   *
   * @return the device ID identifying the device the message download request originated from
   */
  public String getDevice()
  {
    return device;
  }

  /**
   * Returns the encryption scheme that should be used to secure the downloaded messages.
   *
   * @return the encryption scheme that should be used to secure the downloaded messages
   */
  public EncryptionScheme getMessageEncryptionScheme()
  {
    return messageEncryptionScheme;
  }

  /**
   * Returns the username identifying the user whose messages should be downloaded or blank if the
   * messages for all the users linked to the device should be downloaded.
   *
   * @return the username identifying the user whose messages should be downloaded or blank if the
   *         messages for all the users linked to the device should be downloaded
   */
  public String getUser()
  {
    return user;
  }

  /**
   * Returns <code>true</code> if a user has been specified for the message download request or
   * <code>false</code> otherwise.
   *
   * @return <code>true</code> if a user has been specified for the message download request or
   *         <code>false</code> otherwise
   */
  public boolean hasUserSpecified()
  {
    return (!StringUtil.isNullOrEmpty(user));
  }

  /**
   * Set the device ID identifying the device the message download request originated from.
   *
   * @param device the device ID identifying the device the message download request originated
   *               from
   */
  public void setDevice(String device)
  {
    this.device = device;
  }

  /**
   * Set the encryption scheme that should be used to secure the downloaded messages.
   *
   * @param messageEncryptionScheme the encryption scheme that should be used to secure the
   *                                downloaded messages
   */
  public void setMessageEncryptionScheme(EncryptionScheme messageEncryptionScheme)
  {
    this.messageEncryptionScheme = messageEncryptionScheme;
  }

  /**
   * Set the username identifying the user whose messages should be downloaded or blank if the
   * messages for all the users linked to the device should be downloaded.
   *
   * @param user the username identifying the user whose messages should be downloaded or blank if
   *             the messages for all the users linked to the device should be downloaded
   */
  public void setUser(String user)
  {
    this.user = user;
  }

  /**
   * Returns the String representation of the message download request.
   *
   * @return the String representation of the message download request.
   */
  @Override
  public String toString()
  {
    return "<MessageDownloadRequest" + " device=\"" + device + "\"" + " user=\"" + user + "\""
        + " messageEncryptionScheme=\"" + messageEncryptionScheme + "\"" + "/>";
  }

  /**
   * Returns the WBXML representation of the message download request.
   *
   * @return the WBXML representation of the message download request
   */
  public byte[] toWBXML()
  {
    Element rootElement = new Element("MessageDownloadRequest");

    rootElement.setAttribute("device", device);
    rootElement.setAttribute("user", user);
    rootElement.setAttribute("messageEncryptionScheme",
        String.valueOf(messageEncryptionScheme.getCode()));

    Encoder encoder = new Encoder(new Document(rootElement));

    return encoder.getData();
  }
}

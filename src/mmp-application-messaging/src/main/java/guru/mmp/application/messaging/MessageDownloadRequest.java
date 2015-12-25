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

import guru.mmp.common.wbxml.Document;
import guru.mmp.common.wbxml.Element;
import guru.mmp.common.wbxml.Encoder;

//~--- JDK imports ------------------------------------------------------------

import java.util.UUID;

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
public class MessageDownloadRequest
{
  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the device the message
   * download request originated from.
   */
  private UUID deviceId;

  /**
   * The username identifying the user whose messages should be downloaded.
   */
  private String user;

  /**
   * Constructs a new <code>MessageDownloadRequest</code> and populates it from the information
   * stored in the specified WBXML document.
   *
   * @param document the WBXML document containing the message download information
   */
  public MessageDownloadRequest(Document document)
  {
    Element rootElement = document.getRootElement();

    this.deviceId = UUID.fromString(rootElement.getAttributeValue("deviceId"));
    this.user = rootElement.getAttributeValue("user");
  }

  /**
   * Constructs a new <code>MessageDownloadRequest</code>.
   *
   * @param deviceId the Universally Unique Identifier (UUID) used to uniquely identify the device
   *                 the message download request originated from
   * @param user     the username identifying the user whose messages should be downloaded
   */
  public MessageDownloadRequest(UUID deviceId, String user)
  {
    this.deviceId = deviceId;
    this.user = user;
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
        && (rootElement.getAttributes().size() == 2) && rootElement.hasAttribute("deviceId")
        && rootElement.hasAttribute("user");

  }

  /**
   * Returns the Universally Unique Identifier (UUID) used to uniquely identify the device the
   * message download request originated from.
   *
   * @return the Universally Unique Identifier (UUID) used to uniquely identify the device the
   *         message download request originated from
   */
  public UUID getDeviceId()
  {
    return deviceId;
  }

  /**
   * Returns the username identifying the user whose messages should be downloaded.
   *
   * @return the username identifying the user whose messages should be downloaded
   */
  public String getUser()
  {
    return user;
  }

  /**
   * Set the Universally Unique Identifier (UUID) used to uniquely identify the device the message
   * download request originated from.
   *
   * @param deviceId the Universally Unique Identifier (UUID) used to uniquely identify the device
   *                 the message download request originated from
   */
  public void setDeviceId(UUID deviceId)
  {
    this.deviceId = deviceId;
  }

  /**
   * Set the username identifying the user whose messages should be downloaded.
   *
   * @param user the username identifying the user whose messages should be downloaded
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
    return "<MessageDownloadRequest deviceId=\"" + deviceId + "\" user=\"" + user + "\"/>";
  }

  /**
   * Returns the WBXML representation of the message download request.
   *
   * @return the WBXML representation of the message download request
   */
  public byte[] toWBXML()
  {
    Element rootElement = new Element("MessageDownloadRequest");

    rootElement.setAttribute("deviceId", deviceId.toString());
    rootElement.setAttribute("user", user);

    Encoder encoder = new Encoder(new Document(rootElement));

    return encoder.getData();
  }
}

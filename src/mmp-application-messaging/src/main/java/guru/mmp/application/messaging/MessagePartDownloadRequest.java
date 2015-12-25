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

import java.util.UUID;

/**
 * The <code>MessagePartDownloadRequest</code> class represents a request sent by the Messaging
 * Infrastructure on a mobile device to download the queued message parts for the device from the
 * server-side messaging infrastructure.
 *
 * @author Marcus Portmann
 */
public class MessagePartDownloadRequest
{
  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the device the message part
   * download request originated from.
   */
  private UUID deviceId;

  /**
   * Constructs a new <code>MessagePartDownloadRequest</code> and populates it from the information
   * stored in the specified WBXML document.
   *
   * @param document the WBXML document containing the message part download information
   */
  public MessagePartDownloadRequest(Document document)
  {
    Element rootElement = document.getRootElement();

    this.deviceId = UUID.fromString(rootElement.getAttributeValue("deviceId"));
  }

  /**
   * Constructs a new <code>MessagePartDownloadRequest</code>.
   *
   * @param deviceId the Universally Unique Identifier (UUID) used to uniquely identify the device
   *                 the message part download request originated from
   */
  public MessagePartDownloadRequest(UUID deviceId)
  {
    this.deviceId = deviceId;
  }

  /**
   * Returns <code>true</code> if the WBXML document contains valid message part download request
   * information or <code>false</code> otherwise.
   *
   * @param document the WBXML document to validate
   *
   * @return <code>true</code> if the WBXML document contains valid message part download request
   *         information or <code>false</code> otherwise
   */
  public static boolean isValidWBXML(Document document)
  {
    Element rootElement = document.getRootElement();

    return rootElement.getName().equals("MessagePartDownloadRequest")
        && (rootElement.getAttributes().size() == 1) && rootElement.hasAttribute("deviceId");
  }

  /**
   * Returns the Universally Unique Identifier (UUID) used to uniquely identify the device the
   * message part download request originated from.
   *
   * @return the Universally Unique Identifier (UUID) used to uniquely identify the device the
   *         message part download request originated from
   */
  public UUID getDeviceId()
  {
    return deviceId;
  }

  /**
   * Set the Universally Unique Identifier (UUID) used to uniquely identify the device the message
   * part download request originated from.
   *
   * @param deviceId the Universally Unique Identifier (UUID) used to uniquely identify the device
   *                 the message part download request originated from
   */
  public void setDeviceId(UUID deviceId)
  {
    this.deviceId = deviceId;
  }

  /**
   * Returns the String representation of the message part download request.
   *
   * @return the String representation of the message part download request.
   */
  @Override
  public String toString()
  {
    return "<MessagePartDownloadRequest deviceId=\"" + deviceId + "\"" + "/>";
  }

  /**
   * Returns the WBXML representation of the message part download request.
   *
   * @return the WBXML representation of the message part download request
   */
  public byte[] toWBXML()
  {
    Element rootElement = new Element("MessagePartDownloadRequest");

    rootElement.setAttribute("deviceId", deviceId.toString());

    Encoder encoder = new Encoder(new Document(rootElement));

    return encoder.getData();
  }
}

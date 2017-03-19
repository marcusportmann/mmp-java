/*
 * Copyright 2017 Marcus Portmann
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

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>MessagePartReceivedRequest</code> class represents a request sent by the Messaging
 * Infrastructure on a mobile device to acknowledge the successful download of a message part from
 * the server-side messaging infrastructure.
 *
 * @author Marcus Portmann
 */
public class MessagePartReceivedRequest
{
  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the device the message part
   * received request originated from.
   */
  private UUID deviceId;

  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the message part that was
   * successfully downloaded.
   */
  private UUID messagePartId;

  /**
   * Constructs a new <code>MessagePartReceivedRequest</code> and populates it from the information
   * stored in the specified WBXML document.
   *
   * @param document the WBXML document containing the message part received request information
   */
  public MessagePartReceivedRequest(Document document)
  {
    Element rootElement = document.getRootElement();

    this.deviceId = UUID.fromString(rootElement.getAttributeValue("deviceId"));
    this.messagePartId = UUID.fromString(rootElement.getAttributeValue("messagePartId"));
  }

  /**
   * Constructs a new <code>MessagePartReceivedRequest</code>.
   *
   * @param deviceId      the Universally Unique Identifier (UUID) used to uniquely identify the
   *                      device the message part received request originated from
   * @param messagePartId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                      message part that was successfully downloaded
   */
  public MessagePartReceivedRequest(UUID deviceId, UUID messagePartId)
  {
    this.deviceId = deviceId;
    this.messagePartId = messagePartId;
  }

  /**
   * Returns <code>true</code> if the WBXML document contains valid message part received request
   * information or <code>false</code> otherwise.
   *
   * @param document the WBXML document to validate
   *
   * @return <code>true</code> if the WBXML document contains valid message part received request
   * information or <code>false</code> otherwise
   */
  public static boolean isValidWBXML(Document document)
  {
    Element rootElement = document.getRootElement();

    return rootElement.getName().equals("MessagePartReceivedRequest")
        && (rootElement.getAttributes().size() == 2)
        && !((!rootElement.hasAttribute("deviceId")) || (!rootElement.hasAttribute(
            "messagePartId")));
  }

  /**
   * Returns the Universally Unique Identifier (UUID) used to uniquely identify the device the
   * message part received request originated from.
   *
   * @return the Universally Unique Identifier (UUID) used to uniquely identify the device the
   * message part received request originated from
   */
  public UUID getDeviceId()
  {
    return deviceId;
  }

  /**
   * Returns the Universally Unique Identifier (UUID) used to uniquely identify the message part
   * that was successfully downloaded.
   *
   * @return the Universally Unique Identifier (UUID) used to uniquely identify the message part
   * that was successfully downloaded
   */
  public UUID getMessagePartId()
  {
    return messagePartId;
  }

  /**
   * Returns the String representation of the message part received request.
   *
   * @return the String representation of the message part received request.
   */
  @Override
  public String toString()
  {
    return String.format("<MessagePartReceivedRequest deviceId=\"%s\" messagePartId=\"%s\"/>",
        deviceId, messagePartId);
  }

  /**
   * Returns the WBXML representation of the message part received request.
   *
   * @return the WBXML representation of the message part received request
   */
  public byte[] toWBXML()
  {
    Element rootElement = new Element("MessagePartReceivedRequest");

    rootElement.setAttribute("deviceId", deviceId.toString());
    rootElement.setAttribute("messagePartId", messagePartId.toString());

    Encoder encoder = new Encoder(new Document(rootElement));

    return encoder.getData();
  }
}

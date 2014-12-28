/*
 * Copyright 2014 Marcus Portmann
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
   * The device ID identifying the device the message part received request originated from.
   */
  private String device;

  /**
   * The ID of the message part that was successfully downloaded.
   */
  private String messagePartId;

  /**
   * Constructs a new <code>MessagePartReceivedRequest</code> and populates it from the information
   * stored in the specified WBXML document.
   *
   * @param document the WBXML document containing the message part received request information
   */
  public MessagePartReceivedRequest(Document document)
  {
    Element rootElement = document.getRootElement();

    this.device = rootElement.getAttributeValue("device");
    this.messagePartId = rootElement.getAttributeValue("messagePartId");
  }

  /**
   * Constructs a new <code>MessagePartReceivedRequest</code>.
   *
   * @param device        the device ID identifying the device the message part received request
   *                      originated from
   * @param messagePartId the ID of the message part that was successfully downloaded
   */
  public MessagePartReceivedRequest(String device, String messagePartId)
  {
    this.device = device;
    this.messagePartId = messagePartId;
  }

  /**
   * Returns <code>true</code> if the WBXML document contains valid message part received request
   * information or <code>false</code> otherwise.
   *
   * @param document the WBXML document to validate
   *
   * @return <code>true</code> if the WBXML document contains valid message part received request
   *         information or <code>false</code> otherwise
   */
  public static boolean isValidWBXML(Document document)
  {
    Element rootElement = document.getRootElement();

    return rootElement.getName().equals("MessagePartReceivedRequest")
        && (rootElement.getAttributes().size() == 2)
        && !((!rootElement.hasAttribute("device")) || (!rootElement.hasAttribute("messagePartId")));
  }

  /**
   * Returns the device ID identifying the device the message part received request originated from.
   *
   * @return the device ID identifying the device the message part received request originated from
   */
  public String getDevice()
  {
    return device;
  }

  /**
   * Returns the ID of the message part that was successfully downloaded.
   *
   * @return the ID of the message part that was successfully downloaded
   */
  public String getMessagePartId()
  {
    return messagePartId;
  }

  /**
   * Set the device ID identifying the device the message part received request originated from.
   *
   * @param device the device ID identifying the device the message part received request
   *               originated from
   */
  public void setDevice(String device)
  {
    this.device = device;
  }

  /**
   * Set the ID of the message part that was successfully downloaded.
   *
   * @param messagePartId the ID of the message part that was successfully downloaded
   */
  public void setMessagePartId(String messagePartId)
  {
    this.messagePartId = messagePartId;
  }

  /**
   * Returns the String representation of the message part received request.
   *
   * @return the String representation of the message part received request.
   */
  @Override
  public String toString()
  {
    return "<MessagePartReceivedRequest" + " device=\"" + device + "\"" + " messagePartId=\""
        + messagePartId + "\"" + "/>";
  }

  /**
   * Returns the WBXML representation of the message part received request.
   *
   * @return the WBXML representation of the message part received request
   */
  public byte[] toWBXML()
  {
    Element rootElement = new Element("MessagePartReceivedRequest");

    rootElement.setAttribute("device", device);
    rootElement.setAttribute("messagePartId", messagePartId);

    Encoder encoder = new Encoder(new Document(rootElement));

    return encoder.getData();
  }
}

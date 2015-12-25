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
import guru.mmp.common.wbxml.Parser;

import java.util.UUID;

/**
 * The <code>WbxmlMessageData</code> class provides the abstract base class from which all
 * WBXML-based infrastructural and application-specific message data classes should be derived.
 *
 * @author Marcus Portmann
 */
public abstract class WbxmlMessageData
{
  /**
   * The UUID identifying the type of message the message data is associated with.
   */
  private UUID messageTypeId;

  /**
   * The priority for the message type the message data is associated with.
   */
  private Message.Priority messageTypePriority;

  /**
   * The version of the message type the message data is associated with.
   */
  private int messageTypeVersion;

  /**
   * Constructs a new <code>WbxmlMessageData</code>.
   *
   * @param messageTypeId       the UUID identifying the type of message the message data is
   *                            associated with
   * @param messageTypeVersion  the version of the message type the message data is associated with
   * @param messageTypePriority the priority for the message type the message data is associated
   *                            with
   */
  public WbxmlMessageData(UUID messageTypeId, int messageTypeVersion,
      Message.Priority messageTypePriority)
  {
    this.messageTypeId = messageTypeId;
    this.messageTypeVersion = messageTypeVersion;
    this.messageTypePriority = messageTypePriority;
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
  public abstract boolean fromMessageData(String messageType, int messageTypeVersion,
      byte[] messageData)
    throws MessagingException;

  /**
   * Returns the UUID identifying the type of message the message data is associated with.
   *
   * @return the UUID identifying the type of message the message data is associated with
   */
  public UUID getMessageTypeId()
  {
    return messageTypeId;
  }

  /**
   * Returns the priority for the message type the message data is associated with.
   *
   * @return the priority for the message type the message data is associated with
   */
  public Message.Priority getMessageTypePriority()
  {
    return messageTypePriority;
  }

  /**
   * Returns the version of the message type the message data is associated with.
   *
   * @return the version of the message type the message data is associated with
   */
  public int getMessageTypeVersion()
  {
    return messageTypeVersion;
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
  public abstract byte[] toMessageData()
    throws MessagingException;

  /**
   * Parse the WBXML data representation of the message data.
   *
   * @param data the WBXML data representation of the message data
   *
   * @return the WBXML document representing the message data
   *
   * @throws MessagingException
   */
  protected Document parseWBXML(byte[] data)
    throws MessagingException
  {
    try
    {
      Parser parser = new Parser();

      return parser.parse(data);
    }
    catch (Throwable e)
    {
      throw new MessagingException("Failed to parse the WBXML message data", e);
    }
  }
}

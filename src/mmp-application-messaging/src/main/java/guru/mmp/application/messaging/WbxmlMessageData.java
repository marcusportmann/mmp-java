/*
 * Copyright 2016 Marcus Portmann
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

//~--- JDK imports ------------------------------------------------------------

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
   * Constructs a new <code>WbxmlMessageData</code>.
   *
   * @param messageTypeId       the UUID identifying the type of message the message data is
   *                            associated with
   * @param messageTypePriority the priority for the message type the message data is associated
   *                            with
   */
  public WbxmlMessageData(UUID messageTypeId, Message.Priority messageTypePriority)
  {
    this.messageTypeId = messageTypeId;
    this.messageTypePriority = messageTypePriority;
  }

  /**
   * Extract the message data from the WBXML data for a message.
   *
   * @param messageData the WBXML data for the message
   *
   * @return <code>true</code> if the message data was extracted successfully from the WBXML data or
   * <code>false</code> otherwise
   */
  public abstract boolean fromMessageData(byte[] messageData)
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
   * Returns the WBXML data representation of the message data that will be sent as part of a
   * message.
   *
   * @return the WBXML data representation of the message data that will be sent as part of a
   * message
   */
  public abstract byte[] toMessageData()
    throws MessagingException;

  /**
   * Parse the WBXML data representation of the message data.
   *
   * @param data the WBXML data representation of the message data
   *
   * @return the WBXML document representing the message data
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

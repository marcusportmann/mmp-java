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

/**
 * The <code>XmlMessageData</code> class provides the abstract base class from which all
 * XML-based infrastructural and application-specific message data classes should be derived.
 *
 * @author Marcus Portmann
 */
public abstract class XmlMessageData
{
  /**
   * The UUID identifying the type of message the message data is associated with.
   */
  private String messageType;

  /**
   * The priority for the message type the message data is associated with.
   */
  private Message.Priority messageTypePriority;

  /**
   * The version of the message type the message data is associated with.
   */
  private int messageTypeVersion;

  /**
   * Constructs a new <code>XmlMessageData</code>.
   *
   * @param messageType         the UUID identifying the type of message the message data is
   *                            associated with
   * @param messageTypeVersion  the version of the message type the message data is associated with
   * @param messageTypePriority the priority for the message type the message data is associated
   *                            with
   */
  public XmlMessageData(String messageType, int messageTypeVersion,
      Message.Priority messageTypePriority)
  {
    this.messageType = messageType;
    this.messageTypeVersion = messageTypeVersion;
    this.messageTypePriority = messageTypePriority;
  }

  /**
   * Returns the UUID identifying the type of message the message data is associated with.
   *
   * @return the UUID identifying the type of message the message data is associated with
   */
  public String getMessageType()
  {
    return messageType;
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
}

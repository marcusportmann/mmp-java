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

package guru.mmp.application.messaging.handler;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.messaging.Message;

/**
 * The <code>MessageHandler</code> class provides the base class that all message handlers should
 * be derived from.
 *
 * @author Marcus Portmann
 */
public abstract class MessageHandler
  implements IMessageHandler
{
  /**
   * The configuration information for the message handler.
   */
  private MessageHandlerConfig messageHandlerConfig;

  /**
   * The name of the message handler.
   */
  private String name;

  /**
   * Constructs a new <code>MessageHandler</code>.
   *
   * @param name                 the name of the message handler
   * @param messageHandlerConfig the configuration information for the message handler
   */
  public MessageHandler(String name, MessageHandlerConfig messageHandlerConfig)
  {
    this.name = name;
    this.messageHandlerConfig = messageHandlerConfig;
  }

  /**
   * Constructs a new <code>MessageHandler</code>.
   * <p/>
   * Hidden default constructor to support CDI.
   */
  protected MessageHandler() {}

  /**
   * Returns the configuration information for the message handler.
   *
   * @return the configuration information for the message handler
   */
  public MessageHandlerConfig getMessageHandlerConfig()
  {
    return messageHandlerConfig;
  }

  /**
   * Returns the name of the message handler.
   *
   * @return the name of the message handler
   */
  public String getName()
  {
    return name;
  }

  /**
   * Returns <code>true</code> if the message handler is able to process the specified message
   * asynchronously or <code>false</code> otherwise.
   *
   * @param message the message to check for asynchronous processing
   *
   * @return <code>true</code> if the message handler is able to process the specified message
   *         asynchronously or <code>false</code> otherwise
   */
  public boolean supportsAsynchronousProcessing(Message message)
  {
    return supportsAsynchronousProcessing(message.getType(), message.getTypeVersion());
  }

  /**
   * Returns <code>true</code> if the message handler is able to process a message with the
   * specified type and type version asynchronously or <code>false</code> otherwise.
   *
   * @param messageType        the message type
   * @param messageTypeVersion the version of the message type
   *
   * @return <code>true</code> if the message handler is able to process a message with the
   *         specified type and type version asynchronously or <code>false</code> otherwise
   */
  public boolean supportsAsynchronousProcessing(String messageType, int messageTypeVersion)
  {
    return messageHandlerConfig.supportsAsynchronousProcessing(messageType, messageTypeVersion);
  }

  /**
   * Returns <code>true</code> if the message handler is able to process the specified message
   * synchronously or <code>false</code> otherwise.
   *
   * @param message the message to check for asynchronous processing
   *
   * @return <code>true</code> if the message handler is able to process the specified message
   *         synchronously or <code>false</code> otherwise
   */
  public boolean supportsSynchronousProcessing(Message message)
  {
    return supportsSynchronousProcessing(message.getType(), message.getTypeVersion());
  }

  /**
   * Returns <code>true</code> if the message handler is able to process a message with the
   * specified type and type version synchronously or <code>false</code> otherwise.
   *
   * @param messageType        the message type
   * @param messageTypeVersion the version of the message type
   *
   * @return <code>true</code> if the message handler is able to process a message with the
   *         specified type and type version synchronously or <code>false</code> otherwise
   */
  public boolean supportsSynchronousProcessing(String messageType, int messageTypeVersion)
  {
    return messageHandlerConfig.supportsSynchronousProcessing(messageType, messageTypeVersion);
  }
}

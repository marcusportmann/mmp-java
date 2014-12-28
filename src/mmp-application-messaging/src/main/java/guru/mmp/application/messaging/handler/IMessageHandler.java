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
 * The <code>IMessageHandler</code> interface defines the interface that must be implemented by all
 * message handlers.
 *
 * @author Marcus Portmann
 */
public interface IMessageHandler
{
  /**
   * Returns the configuration information for this message handler.
   *
   * @return the configuration information for this message handler
   */
  public MessageHandlerConfig getMessageHandlerConfig();

  /**
   * Returns the name of the message handler.
   *
   * @return the name of the message handler
   */
  public String getName();

  /**
   * Process the specified message.
   *
   * @param message the message to process
   *
   * @return the response message or <code>null</code> if no response message exists
   *
   * @throws MessageHandlerException
   */
  public Message processMessage(Message message)
    throws MessageHandlerException;

  /**
   * Returns <code>true</code> if the message handler is able to process the specified message
   * asynchronously or <code>false</code> otherwise.
   *
   * @param message the message to check for asynchronous processing
   *
   * @return <code>true</code> if the message handler is able to process the specified message
   *         asynchronously or <code>false</code> otherwise
   */
  public boolean supportsAsynchronousProcessing(Message message);

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
  public boolean supportsAsynchronousProcessing(String messageType, int messageTypeVersion);

  /**
   * Returns <code>true</code> if the message handler is able to process the specified message
   * synchronously or <code>false</code> otherwise.
   *
   * @param message the message to check for asynchronous processing
   *
   * @return <code>true</code> if the message handler is able to process the specified message
   *         synchronously or <code>false</code> otherwise
   */
  public boolean supportsSynchronousProcessing(Message message);

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
  public boolean supportsSynchronousProcessing(String messageType, int messageTypeVersion);
}

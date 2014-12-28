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

import guru.mmp.application.messaging.IMessagingService;
import guru.mmp.application.messaging.Message;

import javax.inject.Inject;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>TestHandler</code> class implements a message handler for test messages.
 *
 * @author Marcus Portmann
 */
public class TestHandler extends MessageHandler
{
  /* Messaging Service */
  @SuppressWarnings("unused")
  @Inject
  private IMessagingService messagingService;

  /**
   * Constructs a new <code>TestHandler</code>.
   *
   * @param messageHandlerConfig the configuration information for this message handler
   */
  public TestHandler(MessageHandlerConfig messageHandlerConfig)
  {
    super("Test Handler", messageHandlerConfig);
  }

  /**
   * Constructs a new <code>TestHandler</code>.
   * <p/>
   * Hidden default constructor to support CDI.
   */
  protected TestHandler() {}

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
    throws MessageHandlerException
  {
    return null;
  }
}

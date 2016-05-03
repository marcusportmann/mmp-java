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

package guru.mmp.sample.messaging.handler;

import guru.mmp.application.messaging.Message;
import guru.mmp.application.messaging.handler.MessageHandler;
import guru.mmp.application.messaging.handler.MessageHandlerConfig;
import guru.mmp.application.messaging.handler.MessageHandlerException;

/**
 * The <code>SampleMessageHandler</code> class.
 *
 * @author Marcus Portmann
 */
public class SampleMessageHandler extends MessageHandler
{
  /**
   * Constructs a new <code>SampleMessageHandler</code>.
   *
   * @param name                 the name of the message handler
   * @param messageHandlerConfig the configuration information for the message handler
   */
  public SampleMessageHandler(String name, MessageHandlerConfig messageHandlerConfig)
  {
    super(name, messageHandlerConfig);
  }

  /**
   * Process the specified message.
   *
   * @param message the message to process
   *
   * @return the response message or <code>null</code> if no response message exists
   *
   * @throws MessageHandlerException
   */
  @Override
  public Message processMessage(Message message)
    throws MessageHandlerException
  {
    if (false)
    {
      throw new MessageHandlerException("Testing 1.. 2.. 3..");
    }

    if (false)
    {
      try
      {
        throw new RuntimeException("Testing 1.. 2.. 3..");
      }
      catch (Throwable e)
      {
        throw new MessageHandlerException("Testing 3.. 2.. 1..", e);
      }
    }


    return null;
  }
}

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.ejb.*;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import java.util.concurrent.Future;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>BackgroundMessageProcessor</code> class implements the Background Message Processor.
 *
 * @author Marcus Portmann
 */
@ApplicationScoped
@Default
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
@TransactionManagement(TransactionManagementType.BEAN)
public class BackgroundMessageProcessor
{
  /* Logger */
  private static Logger logger = LoggerFactory.getLogger(BackgroundMessageProcessor.class);

  /* Messaging Service */
  @Inject
  private IMessagingService messagingService;

  /**
   * Initialise the Background Message Processor.
   */
  @PostConstruct
  public void init()
  {
    logger.info("Initialising the Background Message Processor");

    if (messagingService != null)
    {
      /*
       * Reset any locks for messages that were previously being processed by the Background
       * Message Processor.
       */
      try
      {
        logger.info("Resetting the message locks for the messages being processed");

        messagingService.resetMessageLocks(Message.Status.PROCESSING, Message.Status
            .QUEUED_FOR_PROCESSING);
      }
      catch (Throwable e)
      {
        logger.error("Failed to reset the message locks for the messages being processed", e);
      }
    }
    else
    {
      logger.error("Failed to initialise the Background Message Processor: "
          + "The Messaging Service was NOT injected");
    }
  }

  /**
   * Process.
   *
   * @return <code>true</code> if the processing was successful or <code>false</code> otherwise
   */
  @Asynchronous
  public Future<Boolean> process()
  {
    // If CDI injection was not completed successfully for the bean then stop here
    if (messagingService == null)
    {
      logger.error("Failed to process the messages queued for processing: "
          + " The Messaging Service was NOT injected");

      return new AsyncResult<>(false);
    }

    try
    {
      processMessages();

      return new AsyncResult<>(true);
    }
    catch (Throwable e)
    {
      logger.error("Failed to process the messages queued for processing", e);

      return new AsyncResult<>(false);
    }
  }

  private void processMessages()
  {
    Message message;

    while (true)
    {
      // Retrieve the next message queued for processing
      try
      {
        message = messagingService.getNextMessageQueuedForProcessing();

        if (message == null)
        {
          if (logger.isDebugEnabled())
          {
            logger.debug("No messages queued for processing");
          }

          return;
        }
      }
      catch (Throwable e)
      {
        logger.error("Failed to retrieve the next message queued for processing", e);

        return;
      }

      // Process the asynchronous message
      try
      {
        if (logger.isDebugEnabled())
        {
          logger.debug(String.format("Processing the queued message (%s)%s  %s", message.getId(),
              System.getProperty("line.separator"), message.toString()));
        }

        Message responseMessage = messagingService.processMessage(message);

        if (responseMessage != null)
        {
          messagingService.queueMessageForDownload(responseMessage);
        }

        // Remove the processed message from the queue
        messagingService.deleteMessage(message);
      }
      catch (Throwable e)
      {
        logger.error(String.format("Failed to process the queued message (%s)", message.getId()),
            e);

        // Increment the processing attempts for the message
        try
        {
          messagingService.incrementMessageProcessingAttempts(message);

          message.setProcessAttempts(message.getProcessAttempts() + 1);
        }
        catch (Throwable f)
        {
          logger.error(String.format(
              "Failed to increment the processing attempts for the queued message (%s)",
              message.getId()), f);
        }

        try
        {
          /*
           * If the message has exceeded the maximum number of processing attempts then unlock it
           * and set its status to "Failed" otherwise unlock it and set its status to
           * "QueuedForProcessing".
           */
          if (message.getProcessAttempts() >= messagingService.getMaximumProcessingAttempts())
          {
            logger.warn(String.format(
                "The queued message (%s) has exceeded the maximum number of processing attempts and"
                + " will be marked as \"Failed\"", message.getId()));

            messagingService.unlockMessage(message, Message.Status.FAILED);
          }
          else
          {
            messagingService.unlockMessage(message, Message.Status.QUEUED_FOR_PROCESSING);
          }
        }
        catch (Throwable f)
        {
          logger.error(String.format(
              "Failed to unlock and set the status for the queued message (%s)", message.getId()),
              f);
        }
      }
    }
  }
}

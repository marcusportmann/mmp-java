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

package guru.mmp.application.sms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.ejb.*;
import javax.inject.Inject;
import java.util.concurrent.Future;

/**
 * The <code>BackgroundSMSSender</code> class implements the Background SMS Sender.
 *
 * @author Marcus Portmann
 */
@Singleton
@TransactionManagement(TransactionManagementType.BEAN)
public class BackgroundSMSSender
{
  /* Logger */
  private static Logger logger = LoggerFactory.getLogger(BackgroundSMSSender.class);

  /* SMS Service */
  @Inject
  private ISMSService smsService;

  /**
   * Initialise the Background SMS Sender.
   */
  @PostConstruct
  public void init()
  {
    logger.info("Initialising the Background SMS Sender");

    if (smsService != null)
    {
      /*
       * Reset any locks for SMS that were previously being sent by the background
       * SMS sender.
       */
      try
      {
        logger.info("Resetting the SMS locks for the SMSs being sent");

        smsService.resetSMSLocks(SMS.Status.SENDING, SMS.Status.QUEUED_FOR_SENDING);
      }
      catch (Throwable e)
      {
        logger.error("Failed to reset the SMS locks for the SMSs being sent", e);
      }
    }
    else
    {
      logger.error(
        "Failed to initialise the Background SMS Sender: The SMS Service was NOT injected");
    }
  }

  /**
   * Send all queued SMSs.
   *
   * @return <code>true</code> if the SMSs were sent successfully or <code>false</code> otherwise
   */
  @Asynchronous
  public Future<Boolean> send()
  {
    // If CDI injection was not completed successfully for the bean then stop here
    if (smsService == null)
    {
      logger.error(
        "Failed to send the SMSs queued for sending: The SMSService was NOT injected");

      return new AsyncResult<>(false);
    }

    try
    {
      sendSMSs();

      return new AsyncResult<>(true);
    }
    catch (Throwable e)
    {
      logger.error("Failed to send the SMSs queued for sending", e);

      return new AsyncResult<>(false);
    }
  }

  private void sendSMSs()
  {
    SMS sms;

    while (true)
    {
      // Retrieve the next SMS queued for sending
      try
      {
        sms = smsService.getNextSMSQueuedForSending();

        if (sms == null)
        {
          if (logger.isDebugEnabled())
          {
            logger.debug("No SMSs queued for sending");
          }

          return;
        }
      }
      catch (Throwable e)
      {
        logger.error("Failed to retrieve the next SMS queued for sending", e);

        return;
      }

      // Send the SMS
      try
      {
        if (logger.isDebugEnabled())
        {
          logger.debug(String.format("Sending the queued SMS (%d)", sms.getId()));
        }

        if (smsService.sendSMSSynchronously(sms.getId(), sms.getMobileNumber(), sms.getMessage()))
        {
          // Delete the SMS
          smsService.deleteSMS(sms.getId());
        }
        else
        {
          // Unlock the SMS and mark it as failed
          smsService.unlockSMS(sms.getId(), SMS.Status.FAILED);
        }
      }
      catch (Throwable e)
      {
        logger.error(String.format("Failed to send the queued SMS (%d)", sms.getId()), e);

        // Increment the send attempts for the SMS
        try
        {
          smsService.incrementSMSSendAttempts(sms);
        }
        catch (Throwable f)
        {
          logger.error(
            String.format("Failed to increment the send attempts for the queued SMS (%d)",
              sms.getId()), f);
        }

        try
        {
          /*
           * If the SMS has exceeded the maximum number of processing attempts then unlock it
           * and set its status to "Failed" otherwise unlock it and set its status to
           * "QueuedForSending".
           */
          if (sms.getSendAttempts() >= smsService.getMaximumSendAttempts())
          {
            logger.warn(String.format(
              "The queued SMS (%d) has exceeded the maximum  number of send attempts and will be " +
                "marked as \"Failed\"",
              sms.getId()));

            smsService.unlockSMS(sms.getId(), SMS.Status.FAILED);
          }
          else
          {
            smsService.unlockSMS(sms.getId(), SMS.Status.QUEUED_FOR_SENDING);
          }
        }
        catch (Throwable f)
        {
          logger.error(String.format("Failed to unlock and set the status for the queued SMS (%d)",
            sms.getId()), f);
        }
      }
    }
  }
}

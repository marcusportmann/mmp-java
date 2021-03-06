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

package guru.mmp.application.sms;

//~--- non-JDK imports --------------------------------------------------------

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>BackgroundSMSSender</code> class implements the Background SMS Sender.
 *
 * @author Marcus Portmann
 */
@Service
@SuppressWarnings("unused")
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
   * Send the SMSs.
   */
  @Scheduled(cron = "0 * * * * *")
  @Async
  void sendSMSs()
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
          logger.error(String.format(
              "Failed to increment the send attempts for the queued SMS (%d)", sms.getId()), f);
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
                "The queued SMS (%d) has exceeded the maximum number of send attempts and will be "
                + "marked as \"Failed\"", sms.getId()));

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

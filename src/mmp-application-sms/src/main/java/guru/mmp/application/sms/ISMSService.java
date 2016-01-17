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

/**
 * The <code>ISMSService</code> interface defines the functionality provided by an SMS Service
 * implementation.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public interface ISMSService
{
  /**
   * Delete the existing SMS.
   *
   * @param id the ID uniquely identifying the SMS
   *
   * @return <code>true</code> if the SMS was deleted or <code>false</code> otherwise
   *
   * @throws SMSServiceException
   */
  boolean deleteSMS(long id)
    throws SMSServiceException;

  /**
   * Returns the maximum number of send attempts for a SMS.
   *
   * @return the maximum number of send attempts for a SMS
   */
  int getMaximumSendAttempts();

  /**
   * Retrieve the next SMS that has been queued for sending.
   * <p/>
   * The SMS will be locked to prevent duplicate sending.
   *
   * @return the next SMS that has been queued for sending or <code>null</code> if no SMSs are
   * currently queued for sending
   *
   * @throws SMSServiceException
   */
  SMS getNextSMSQueuedForSending()
    throws SMSServiceException;

  /**
   * Returns the number of SMS credits remaining.
   *
   * @return the number of SMS credits remaining
   *
   * @throws SMSServiceException
   */
  int getNumberOfSMSCreditsRemaining()
    throws SMSServiceException;

  /**
   * Retrieve the SMS.
   *
   * @param id the ID uniquely identifying the SMS
   *
   * @return the SMS or <code>null</code> if the SMS could not be found
   *
   * @throws SMSServiceException
   */
  SMS getSMS(long id)
    throws SMSServiceException;

  /**
   * Increment the send attempts for the SMS.
   *
   * @param sms the SMS whose send attempts should be incremented
   *
   * @throws SMSServiceException
   */
  void incrementSMSSendAttempts(SMS sms)
    throws SMSServiceException;

  /**
   * Reset the SMS locks.
   *
   * @param status    the current status of the SMSs that have been locked
   * @param newStatus the new status for the SMSs that have been unlocked
   *
   * @throws SMSServiceException
   */
  void resetSMSLocks(SMS.Status status, SMS.Status newStatus)
    throws SMSServiceException;

  /**
   * Send the SMS.
   * <p/>
   * NOTE: This will queue the SMS for sending. The SMS will actually be sent asynchronously.
   *
   * @param mobileNumber the mobile number
   * @param message      the message
   *
   * @throws SMSServiceException
   */
  void sendSMS(String mobileNumber, String message)
    throws SMSServiceException;

  /**
   * Send the SMS synchronously.
   * <p/>
   * NOTE: This will NOT queue the SMS for sending. The SMS will actually be sent synchronously.
   *
   * @param smsId        the ID of the SMS
   * @param mobileNumber the mobile number
   * @param message      the message
   *
   * @return <code>true</code> if the SMS was sent successfully or <code>false</code> otherwise
   *
   * @throws SMSServiceException
   */
  boolean sendSMSSynchronously(long smsId, String mobileNumber, String message)
    throws SMSServiceException;

  /**
   * Send all the SMSs queued for sending asynchronously.
   */
  void sendSMSs();

  /**
   * Set the status for the SMS.
   *
   * @param id     the ID uniquely identifying the SMS
   * @param status the new status for the SMS
   *
   * @throws SMSServiceException
   */
  void setSMSStatus(long id, SMS.Status status)
    throws SMSServiceException;

  /**
   * Unlock the SMS.
   *
   * @param id     the ID uniquely identifying the SMS
   * @param status the new status for the unlocked SMS
   *
   * @throws SMSServiceException
   */
  void unlockSMS(long id, SMS.Status status)
    throws SMSServiceException;
}

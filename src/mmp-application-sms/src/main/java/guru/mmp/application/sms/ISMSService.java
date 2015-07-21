/*
 * Copyright 2015 Marcus Portmann
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
 * The <code>ISMSService</code> interface defines the functionality that must be provided by an
 * SMS Service implementation.
 *
 * @author Marcus Portmann
 */
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
  public boolean deleteSMS(long id)
    throws SMSServiceException;

  /**
   * Returns the maximum number of send attempts for a SMS.
   *
   * @return the maximum number of send attempts for a SMS
   */
  public int getMaximumSendAttempts();

  /**
   * Retrieve the next SMS that has been queued for sending.
   * <p/>
   * The SMS will be locked to prevent duplicate sending.
   *
   * @return the next SMS that has been queued for sending or <code>null</code> if no SMSs are
   *         currently queued for sending
   *
   * @throws SMSServiceException
   */
  public SMS getNextSMSQueuedForSending()
    throws SMSServiceException;

  /**
   * Returns the number of SMS credits remaining.
   *
   * @return the number of SMS credits remaining
   *
   * @throws SMSServiceException
   */
  public int getNumberOfSMSCreditsRemaining()
    throws SMSServiceException;

  /**
   * Retrieve the SMS with the specified ID.
   *
   * @param id the ID uniquely identifying the SMS
   *
   * @return the SMS with the specified ID or <code>null</code> if the SMS could not be found
   *
   * @throws SMSServiceException
   */
  public SMS getSMS(long id)
    throws SMSServiceException;

  /**
   * Increment the send attempts for the SMS.
   *
   * @param sms the SMS whose send attempts should be incremented
   *
   * @throws SMSServiceException
   */
  public void incrementSMSSendAttempts(SMS sms)
    throws SMSServiceException;

  /**
   * Reset the SMS locks.
   *
   * @param status    the current status of the SMSs that have been locked
   * @param newStatus the new status for the SMSs that have been unlocked
   *
   * @throws SMSServiceException
   */
  public void resetSMSLocks(SMS.Status status, SMS.Status newStatus)
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
  public void sendSMS(String mobileNumber, String message)
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
  public boolean sendSMSSynchronously(long smsId, String mobileNumber, String message)
    throws SMSServiceException;

  /**
   * Send all the SMSs queued for sending asynchronously.
   */
  public void sendSMSs();

  /**
   * Set the status for the SMS.
   *
   * @param id     the ID uniquely identifying the SMS
   * @param status the new status for the SMS
   *
   * @throws SMSServiceException
   */
  public void setSMSStatus(long id, SMS.Status status)
    throws SMSServiceException;

  /**
   * Unlock the SMS.
   *
   * @param id     the ID uniquely identifying the SMS
   * @param status the new status for the unlocked SMS
   *
   * @throws SMSServiceException
   */
  public void unlockSMS(long id, SMS.Status status)
    throws SMSServiceException;
}

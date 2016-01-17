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

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.common.persistence.DAOException;

/**
 * The <code>ISMSDAO</code> interface defines the persistence operations for the SMS infrastructure.
 *
 * @author Marcus Portmann
 */
public interface ISMSDAO
{
  /**
   * Create the entry for the SMS in the database.
   *
   * @param sms the <code>SMS</code> instance containing the information for the SMS
   *
   * @throws DAOException
   */
  void createSMS(SMS sms)
    throws DAOException;

  /**
   * Delete the existing SMS.
   *
   * @param id the ID uniquely identifying the SMS
   *
   * @return <code>true</code> if the SMS was deleted or <code>false</code> otherwise
   *
   * @throws DAOException
   */
  boolean deleteSMS(long id)
    throws DAOException;

  /**
   * Retrieve the next SMS that has been queued for sending.
   * <p/>
   * The SMS will be locked to prevent duplicate sending.
   *
   * @param sendRetryDelay the delay in milliseconds to wait before re-attempting to send a SMS
   * @param lockName       the name of the lock that should be applied to the message queued for
   *                       processing when it is retrieved
   *
   * @return the next SMS that has been queued for sending or <code>null</code> if no SMSs are
   * currently queued for sending
   *
   * @throws DAOException
   */
  SMS getNextSMSQueuedForSending(int sendRetryDelay, String lockName)
    throws DAOException;

  /**
   * Retrieve the SMS.
   *
   * @param id the ID uniquely identifying the SMS
   *
   * @return the SMS or <code>null</code> if the SMS could not be found
   *
   * @throws DAOException
   */
  SMS getSMS(long id)
    throws DAOException;

  /**
   * Increment the send attempts for the SMS.
   *
   * @param sms the SMS whose send attempts should be incremented
   *
   * @throws DAOException
   */
  void incrementSMSSendAttempts(SMS sms)
    throws DAOException;

  /**
   * Reset the SMS locks.
   *
   * @param lockName  the name of the lock applied by the entity that has locked the SMSs
   * @param status    the current status of the SMSs that have been locked
   * @param newStatus the new status for the SMSs that have been unlocked
   *
   * @throws DAOException
   */
  void resetSMSLocks(String lockName, SMS.Status status, SMS.Status newStatus)
    throws DAOException;

  /**
   * Set the status for the SMS.
   *
   * @param id     the ID uniquely identifying the SMS
   * @param status the new status for the SMS
   *
   * @throws DAOException
   */
  void setSMSStatus(long id, SMS.Status status)
    throws DAOException;

  /**
   * Unlock the SMS.
   *
   * @param id     the ID uniquely identifying the SMS
   * @param status the new status for the unlocked SMS
   *
   * @throws DAOException
   */
  void unlockSMS(long id, SMS.Status status)
    throws DAOException;
}

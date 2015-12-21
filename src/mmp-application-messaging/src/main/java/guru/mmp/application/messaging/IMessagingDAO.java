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

package guru.mmp.application.messaging;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.common.persistence.DAOException;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;

/**
 * The <code>IMessagingDAO</code> interface defines the persistence operations for the
 * messaging infrastructure.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public interface IMessagingDAO
{
  /**
   * Have all the parts been queued for assembly for the message with the specified ID?
   *
   * @param messageId  the ID uniquely identifying the message
   * @param totalParts the total number of parts for the message
   *
   * @return <code>true</code> if all the parts for the message with the specified ID have been
   *          queued for assembly or <code>false</code> otherwise
   *
   * @throws DAOException
   */
  boolean allPartsQueuedForMessage(String messageId, int totalParts)
    throws DAOException;

  /**
   * Archive the message in the database.
   *
   * @param message the <code>Message</code> instance containing the information for the message
   *
   * @throws DAOException
   */
  void archiveMessage(Message message)
    throws DAOException;

  /**
   * Create the entry for the error report in the database.
   *
   * @param errorReport the <code>ErrorReport</code> instance containing the information for the
   *                    error report
   *
   * @throws DAOException
   */
  void createErrorReport(ErrorReport errorReport)
    throws DAOException;

  /**
   * Create the entry for the message in the database.
   *
   * @param message the <code>Message</code> instance containing the information for the message
   *
   * @throws DAOException
   */
  void createMessage(Message message)
    throws DAOException;

  /**
   * Create the entry for the message part in the database.
   *
   * @param messagePart the <code>MessagePart</code> instance containing the information for the
   *                    message part
   *
   * @throws DAOException
   */
  void createMessagePart(MessagePart messagePart)
    throws DAOException;

  /**
   * Delete the message.
   *
   * @param id the ID uniquely identifying the message
   *
   * @throws DAOException
   */
  void deleteMessage(String id)
    throws DAOException;

  /**
   * Delete the message part.
   *
   * @param id the ID uniquely identifying the message part
   *
   * @throws DAOException
   */
  void deleteMessagePart(String id)
    throws DAOException;

  /**
   * Delete the message parts for the message with the specified ID.
   *
   * @param messageId the ID uniquely identifying the message whose message parts should be deleted
   *
   * @throws DAOException
   */
  void deleteMessagePartsForMessage(String messageId)
    throws DAOException;

  /**
   * Retrieve the error report with the specified ID.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the error report
   *
   * @return the error report with the specified ID or <code>null</code> if the error report could
   *         not be found
   *
   * @throws DAOException
   */
  ErrorReport getErrorReport(String id)
    throws DAOException;

  /**
   * Retrieve the summary for the error report with the specified ID.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the error report
   *
   * @return the summary for the error report with the specified ID or <code>null</code> if the
   *         error report could not be found
   *
   * @throws DAOException
   */
  ErrorReportSummary getErrorReportSummary(String id)
    throws DAOException;

  /**
   * Retrieve the message with the specified ID.
   *
   * @param id the ID uniquely identifying the message
   *
   * @return the message with the specified ID or <code>null</code> if the message could not
   *          be found
   *
   * @throws DAOException
   */
  Message getMessage(String id)
    throws DAOException;

  /**
   * Retrieve the message parts queued for assembly for the message with the specified ID.
   *
   * @param messageId the ID uniquely identifying the message
   * @param lockName  the name of the lock that should be applied to the message parts queued for
   *                  assembly when they are retrieved
   *
   * @return the message parts queued for assembly for the message with the specified ID
   *
   * @throws DAOException
   */
  List<MessagePart> getMessagePartsQueuedForAssembly(String messageId, String lockName)
    throws DAOException;

  /**
   * Get the message parts that have been queued for download by a particular remote device.
   *
   * @param device   the device ID identifying the device downloading the message parts
   * @param lockName name of the lock that should be applied to the message parts queued for
   *                 download when they are retrieved
   *
   * @return the message parts that have been queued for download by a particular remote device
   *
   * @throws DAOException
   */
  List<MessagePart> getMessagePartsQueuedForDownload(String device, String lockName)
    throws DAOException;

  /**
   * Get the messages that have been queued for download by a particular remote device.
   *
   * @param device   the device ID identifying the device downloading the messages
   * @param lockName name of the lock that should be applied to the messages queued for download
   *                 when they are retrieved
   *
   * @return the messages that have been queued for download by a particular remote device
   *
   * @throws DAOException
   */
  List<Message> getMessagesQueuedForDownload(String device, String lockName)
    throws DAOException;

  /**
   * Get the messages for a user that have been queued for download by a particular remote device.
   *
   * @param user     the username identifying the user
   * @param device   the device ID identifying the device downloading the messages
   * @param lockName name of the lock that should be applied to the messages queued for download
   *                 when they are retrieved
   *
   * @return the messages for a user that have been queued for download by a particular remote
   *         device
   *
   * @throws DAOException
   */
  List<Message> getMessagesQueuedForDownloadForUser(String user, String device, String lockName)
    throws DAOException;

  /**
   * Retrieve the summaries for the most recent error reports.
   *
   * @param maximumNumberOfEntries the maximum number of summaries for the most recent error
   *                               reports to retrieve
   *
   * @return the summaries for the most recent error reports
   *
   * @throws DAOException
   */
  List<ErrorReportSummary> getMostRecentErrorReportSummaries(int maximumNumberOfEntries)
    throws DAOException;

  /**
   * Retrieve the next message that has been queued for processing.
   * <p/>
   * The message will be locked to prevent duplicate processing.
   *
   * @param processingRetryDelay the delay in milliseconds between successive attempts to process
   *                             a message
   * @param lockName             the name of the lock that should be applied to the message queued
   *                             for processing when it is retrieved
   *
   * @return the next message that has been queued for processing or <code>null</code> if no
   *         messages are currently queued for processing
   *
   * @throws DAOException
   */
  Message getNextMessageQueuedForProcessing(int processingRetryDelay, String lockName)
    throws DAOException;

  /**
   * Returns the total number of error reports in the database.
   *
   * @return the total number of error reports in the database
   *
   * @throws DAOException
   */
  int getNumberOfErrorReports()
    throws DAOException;

  /**
   * Increment the processing attempts for the message with the specified ID.
   *
   * @param message the message whose processing attempts should be incremented
   *
   * @throws DAOException
   */
  void incrementMessageProcessingAttempts(Message message)
    throws DAOException;

  /**
   * Has the message with the specified ID already been archived?
   *
   * @param id the ID uniquely identifying the message
   *
   * @return <code>true</code> if the message has already been archived or <code>false</code>
   *         otherwise
   *
   * @throws DAOException
   */
  boolean isMessageArchived(String id)
    throws DAOException;

  /**
   * Has the message part with the specified ID already been queued for assembly?
   *
   * @param id the ID uniquely identifying the message part
   *
   * @return <code>true</code> if the message part has already been queued for assemble or
   *         <code>false</code> otherwise
   *
   * @throws DAOException
   */
  boolean isMessagePartQueuedForAssembly(String id)
    throws DAOException;

  /**
   * Log the message audit entry.
   *
   * @param type         the type of message
   * @param user         the user responsible for the message audit entry
   * @param organisation the organisation code identifying the organisation associated with the
   *                     message
   * @param device       the ID for the device associated with the message audit entry
   * @param ip           the IP address of the remote device associated with the message audit entry
   * @param successful   was the message associated with the message audit entry successfully
   *                     processed
   *
   * @throws DAOException
   */
  void logMessageAudit(String type, String user, String organisation, String device, String ip,
      boolean successful)
    throws DAOException;

  /**
   * Reset the expired message locks.
   *
   * @param lockTimeout the lock timeout in seconds
   * @param status      the current status of the messages that have been locked
   * @param newStatus   the new status for the messages that have been unlocked
   *
   * @return the number of message locks reset
   *
   * @throws DAOException
   */
  int resetExpiredMessageLocks(int lockTimeout, Message.Status status, Message.Status newStatus)
    throws DAOException;

  /**
   * Reset the expired message part locks.
   *
   * @param lockTimeout the lock timeout in seconds
   * @param status      the current status of the message parts that have been locked
   * @param newStatus   the new status for the message parts that have been unlocked
   *
   * @return the number of message part locks reset
   *
   * @throws DAOException
   */
  int resetExpiredMessagePartLocks(int lockTimeout, MessagePart.Status status,
      MessagePart.Status newStatus)
    throws DAOException;

  /**
   * Reset the message locks.
   *
   * @param lockName  the name of the lock applied by the entity that has locked the messages
   * @param status    the current status of the messages that have been locked
   * @param newStatus the new status for the messages that have been unlocked
   *
   * @return the number of message locks reset
   *
   * @throws DAOException
   */
  int resetMessageLocks(String lockName, Message.Status status, Message.Status newStatus)
    throws DAOException;

  /**
   * Reset the message part locks.
   *
   * @param lockName  the name of the lock applied by the entity that has locked the message parts
   * @param status    the current status of the message parts that have been locked
   * @param newStatus the new status for the message parts that have been unlocked
   *
   * @return the number of message part locks reset
   *
   * @throws DAOException
   */
  int resetMessagePartLocks(String lockName, MessagePart.Status status,
      MessagePart.Status newStatus)
    throws DAOException;

  /**
   * Set the status for a message part.
   *
   * @param id     the ID uniquely identifying the message part
   * @param status the new status
   *
   * @throws DAOException
   */
  void setMessagePartStatus(String id, MessagePart.Status status)
    throws DAOException;

  /**
   * Set the status for a message.
   *
   * @param id     the ID uniquely identifying the message
   * @param status the new status
   *
   * @throws DAOException
   */
  void setMessageStatus(String id, Message.Status status)
    throws DAOException;

  /**
   * Unlock a locked message.
   *
   * @param id     the ID uniquely identifying the message
   * @param status the new status for the unlocked message
   *
   * @throws DAOException
   */
  void unlockMessage(String id, Message.Status status)
    throws DAOException;

  /**
   * Unlock a locked message part.
   *
   * @param id     the ID uniquely identifying the message part
   * @param status the new status for the unlocked message part
   *
   * @throws DAOException
   */
  void unlockMessagePart(String id, MessagePart.Status status)
    throws DAOException;
}

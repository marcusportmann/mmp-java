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

import guru.mmp.common.persistence.DAOException;

import java.util.List;
import java.util.UUID;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>IMessagingDAO</code> interface defines the persistence operations for the
 * messaging infrastructure.
 *
 * @author Marcus Portmann
 */
public interface IMessagingDAO
{
  /**
   * Have all the parts been queued for assembly for the message?
   *
   * @param messageId  the Universally Unique Identifier (UUID) used to uniquely identify the
   *                   message
   * @param totalParts the total number of parts for the message
   *
   * @return <code>true</code> if all the parts for the message have been  queued for assembly or
   *         <code>false</code> otherwise
   */
  boolean allPartsQueuedForMessage(UUID messageId, int totalParts)
    throws DAOException;

  /**
   * Archive the message in the database.
   *
   * @param message the <code>Message</code> instance containing the information for the message
   */
  void archiveMessage(Message message)
    throws DAOException;

  /**
   * Create the entry for the error report in the database.
   *
   * @param errorReport the <code>ErrorReport</code> instance containing the information for the
   *                    error report
   */
  void createErrorReport(ErrorReport errorReport)
    throws DAOException;

  /**
   * Create the entry for the message in the database.
   *
   * @param message the <code>Message</code> instance containing the information for the message
   */
  void createMessage(Message message)
    throws DAOException;

  /**
   * Create the entry for the message part in the database.
   *
   * @param messagePart the <code>MessagePart</code> instance containing the information for the
   *                    message part
   */
  void createMessagePart(MessagePart messagePart)
    throws DAOException;

  /**
   * Delete the message.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the message
   */
  void deleteMessage(UUID id)
    throws DAOException;

  /**
   * Delete the message part.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the message part
   */
  void deleteMessagePart(UUID id)
    throws DAOException;

  /**
   * Delete the message parts for the message.
   *
   * @param messageId the Universally Unique Identifier (UUID) used to uniquely identify the message
   */
  void deleteMessagePartsForMessage(UUID messageId)
    throws DAOException;

  /**
   * Retrieve the error report.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the error report
   *
   * @return the error report or <code>null</code> if the error report could not be found
   */
  ErrorReport getErrorReport(UUID id)
    throws DAOException;

  /**
   * Retrieve the summary for the error report.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the error report
   *
   * @return the summary for the error report or <code>null</code> if the error report could not be
   * found
   */
  ErrorReportSummary getErrorReportSummary(UUID id)
    throws DAOException;

  /**
   * Retrieve the message.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the message
   *
   * @return the message or <code>null</code> if the message could not be found
   */
  Message getMessage(UUID id)
    throws DAOException;

  /**
   * Retrieve the message parts queued for assembly for the message.
   *
   * @param messageId the Universally Unique Identifier (UUID) used to uniquely identify the message
   * @param lockName  the name of the lock that should be applied to the message parts queued for
   *                  assembly when they are retrieved
   *
   * @return the message parts queued for assembly for the message
   */
  List<MessagePart> getMessagePartsQueuedForAssembly(UUID messageId, String lockName)
    throws DAOException;

  /**
   * Get the message parts for a user that have been queued for download by a particular remote
   * device.
   *
   * @param username the username identifying the user
   * @param deviceId the Universally Unique Identifier (UUID) used to uniquely identify the device
   * @param lockName name of the lock that should be applied to the message parts queued for
   *                 download when they are retrieved
   *
   * @return the message parts that have been queued for download by a particular remote device
   */
  List<MessagePart> getMessagePartsQueuedForDownload(String username, UUID deviceId,
      String lockName)
    throws DAOException;

  /**
   * Get the messages for a user that have been queued for download by a particular remote device.
   *
   * @param username the username identifying the user
   * @param deviceId the Universally Unique Identifier (UUID) used to uniquely identify the device
   * @param lockName name of the lock that should be applied to the messages queued for download
   *                 when they are retrieved
   *
   * @return the messages for a user that have been queued for download by a particular remote
   * device
   */
  List<Message> getMessagesQueuedForDownload(String username, UUID deviceId, String lockName)
    throws DAOException;

  /**
   * Retrieve the summaries for the most recent error reports.
   *
   * @param maximumNumberOfEntries the maximum number of summaries for the most recent error
   *                               reports to retrieve
   *
   * @return the summaries for the most recent error reports
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
   */
  Message getNextMessageQueuedForProcessing(int processingRetryDelay, String lockName)
    throws DAOException;

  /**
   * Returns the total number of error reports in the database.
   *
   * @return the total number of error reports in the database
   */
  int getNumberOfErrorReports()
    throws DAOException;

  /**
   * Increment the processing attempts for the message.
   *
   * @param message the message whose processing attempts should be incremented
   */
  void incrementMessageProcessingAttempts(Message message)
    throws DAOException;

  /**
   * Has the message already been archived?
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the message
   *
   * @return <code>true</code> if the message has already been archived or <code>false</code>
   *         otherwise
   */
  boolean isMessageArchived(UUID id)
    throws DAOException;

  /**
   * Has the message part already been queued for assembly?
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the message part
   *
   * @return <code>true</code> if the message part has already been queued for assemble or
   *         <code>false</code> otherwise
   */
  boolean isMessagePartQueuedForAssembly(UUID id)
    throws DAOException;

  /**
   * Reset the message locks.
   *
   * @param lockName  the name of the lock applied by the entity that has locked the messages
   * @param status    the current status of the messages that have been locked
   * @param newStatus the new status for the messages that have been unlocked
   *
   * @return the number of message locks reset
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
   */
  int resetMessagePartLocks(String lockName, MessagePart.Status status, MessagePart
      .Status newStatus)
    throws DAOException;

  /**
   * Unlock a locked message.
   *
   * @param id     the Universally Unique Identifier (UUID) used to uniquely identify the message
   * @param status the new status for the unlocked message
   */
  void unlockMessage(UUID id, Message.Status status)
    throws DAOException;
}

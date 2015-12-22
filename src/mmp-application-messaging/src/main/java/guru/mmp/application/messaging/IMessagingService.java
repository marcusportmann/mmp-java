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

import guru.mmp.common.crypto.EncryptionScheme;

import java.util.List;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>IMessagingService</code> interface defines the interface for the Messaging Service
 * for the messaging infrastructure.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public interface IMessagingService
{
  /**
   * Archive the message.
   *
   * @param message the message to archive
   *
   * @throws MessagingException
   */
  void archiveMessage(Message message)
    throws MessagingException;

  /**
   * Returns <code>true</code> if the message processor is capable of processing the specified
   * message or <code>false</code> otherwise.
   *
   * @param message the message to process
   *
   * @return <code>true</code> if the message processor is capable of processing the specified
   *         message or <code>false</code> otherwise
   *
   * @throws MessagingException
   */
  boolean canProcessMessage(Message message)
    throws MessagingException;

  /**
   * Returns <code>true</code> if the message processor is capable of queueing the specified
   * message part for assembly or <code>false</code> otherwise.
   *
   * @param messagePart the message part to queue for assembly
   *
   * @return <code>true</code> if the message processor is capable of queueing the specified
   *         message part for assembly or <code>false</code> otherwise
   *
   * @throws MessagingException
   */
  boolean canQueueMessagePartForAssembly(MessagePart messagePart)
    throws MessagingException;

  /**
   * Create the entry for the error report in the database.
   *
   * @param errorReport the <code>ErrorReport</code> instance containing the information for the
   *                    error report
   *
   * @throws MessagingException
   */
  void createErrorReport(ErrorReport errorReport)
    throws MessagingException;

  /**
   * Decrypt the message.
   *
   * @param message the message to decrypt
   *
   * @return <code>true</code> if the message data was decrypted successfully or <code>false</code>
   *         otherwise
   *
   * @throws MessagingException
   */
  boolean decryptMessage(Message message)
    throws MessagingException;

  /**
   * Delete the message.
   *
   * @param message the message to delete
   *
   * @throws MessagingException
   */
  void deleteMessage(Message message)
    throws MessagingException;

  /**
   * Delete the message.
   *
   * @param id the ID uniquely identifying the message to delete
   *
   * @throws MessagingException
   */
  void deleteMessage(String id)
    throws MessagingException;

  /**
   * Delete the message part.
   *
   * @param id the ID uniquely identifying the message part to delete
   *
   * @throws MessagingException
   */
  void deleteMessagePart(String id)
    throws MessagingException;

  /**
   * Derive the user-device encryption key.
   *
   * @param encryptionScheme the encryption scheme for the encryption key
   * @param username         the username uniquely identifying the user e.g. test1
   * @param organisation     the organisation code uniquely identifying the organisation
   * @param deviceId         the device ID uniquely identifying the device
   *
   * @return the user-device encryption key
   *
   * @throws MessagingException
   */
  byte[] deriveUserDeviceEncryptionKey(EncryptionScheme encryptionScheme, String username,
      String organisation, String deviceId)
    throws MessagingException;

  /**
   * Encrypt the message.
   *
   * @param encryptionScheme the encryption scheme to use to encrypt the message
   * @param message          the message to encrypt
   *
   * @return <code>true</code> if the message data was encrypted successfully or <code>false</code>
   *         otherwise
   *
   * @throws MessagingException
   */
  boolean encryptMessage(EncryptionScheme encryptionScheme, Message message)
    throws MessagingException;

  /**
   * Retrieve the error report.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the error report
   *
   * @return the error report or <code>null</code> if the error report could
   *         not be found
   *
   * @throws MessagingException
   */
  ErrorReport getErrorReport(String id)
    throws MessagingException;

  /**
   * Retrieve the summary for the error report.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the error report
   *
   * @return the summary for the error report or <code>null</code> if the
   *         error report could not be found
   *
   * @throws MessagingException
   */
  ErrorReportSummary getErrorReportSummary(String id)
    throws MessagingException;

  /**
   * Returns the maximum number of times processing will be attempted for a message.
   *
   * @return the maximum number of times processing will be attempted for a message
   */
  int getMaximumProcessingAttempts();

  /**
   * Retrieve the message.
   *
   * @param id the ID uniquely identifying the message
   *
   * @return the message or <code>null</code> if the message could not
   *         be found
   *
   * @throws MessagingException
   */
  Message getMessage(String id)
    throws MessagingException;

  /**
   * Get the message parts that have been queued for download by a particular remote device.
   *
   * @param device the device ID identifying the device downloading the message parts
   *
   * @return the message parts that have been queued for download by a particular remote device
   *
   * @throws MessagingException
   */
  List<MessagePart> getMessagePartsQueuedForDownload(String device)
    throws MessagingException;

  /**
   * Get the messages that have been queued for download by a particular remote device.
   *
   * @param device the device ID identifying the device downloading the messages
   *
   * @return the messages that have been queued for download by a particular remote device
   *
   * @throws MessagingException
   */
  List<Message> getMessagesQueuedForDownload(String device)
    throws MessagingException;

  /**
   * Get the messages for a user that have been queued for download by a particular remote device.
   *
   * @param user   the username identifying the user
   * @param device the device ID identifying the device downloading the messages
   *
   * @return the messages for a user that have been queued for download by a particular remote
   *         device
   *
   * @throws MessagingException
   */
  List<Message> getMessagesQueuedForDownloadForUser(String user, String device)
    throws MessagingException;

  /**
   * Returns the path to the messaging debug directory if it exists or <code>null</code> otherwise.
   *
   * @return the path to the messaging debug directory if it exists of <code>null</code> otherwise
   */
  String getMessagingDebugDirectory();

  /**
   * Retrieve the summaries for the most recent error reports.
   *
   * @param maximumNumberOfEntries the maximum number of summaries for the most recent error
   *                               reports to retrieve
   *
   * @return the summaries for the most recent error reports
   *
   * @throws MessagingException
   */
  List<ErrorReportSummary> getMostRecentErrorReportSummaries(int maximumNumberOfEntries)
    throws MessagingException;

  /**
   * Retrieve the next message that has been queued for processing.
   * <p/>
   * The message will be locked to prevent duplicate processing.
   *
   * @return the next message that has been queued for processing or <code>null</code> if no
   *         messages are currently queued for processing
   *
   * @throws MessagingException
   */
  Message getNextMessageQueuedForProcessing()
    throws MessagingException;

  /**
   * Returns the total number of error reports.
   *
   * @return the total number of error reports
   *
   * @throws MessagingException
   */
  int getNumberOfErrorReports()
    throws MessagingException;

  /**
   * Increment the processing attempts for the message.
   *
   * @param message the message whose processing attempts should be incremented
   *
   * @throws MessagingException
   */
  void incrementMessageProcessingAttempts(Message message)
    throws MessagingException;

  /**
   * Should the specified message be archived?
   *
   * @param message the message
   *
   * @return <code>true</code> if a message with the specified type information should be archived
   *         or <code>false</code> otherwise
   */
  boolean isArchivableMessage(Message message);

  /**
   * Can the specified message be processed asynchronously?
   *
   * @param message the message
   *
   * @return <code>true</code> if the message can be processed asynchronously or
   *         <code>false</code> otherwise
   */
  boolean isAsynchronousMessage(Message message);

  /**
   * Has the specified message already been archived?
   *
   * @param message the message to check
   *
   * @return <code>true</code> if the message has already been archived or <code>false</code>
   *         otherwise
   *
   * @throws MessagingException
   */
  boolean isMessageArchived(Message message)
    throws MessagingException;

  /**
   * Can the specified message be processed synchronously?
   *
   * @param message the message
   *
   * @return <code>true</code> if the message can be processed synchronously or
   *         <code>false</code> otherwise
   */
  boolean isSynchronousMessage(Message message);

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
   *                     processed?
   *
   * @return <code>true</code> if the message audit entry was logged successfully or
   *         <code>false</code> otherwise
   *
   * @throws MessagingException
   */
  boolean logMessageAudit(String type, String user, String organisation, String device,
      String ip, boolean successful)
    throws MessagingException;

  /**
   * Process the message.
   *
   * @param message the message to process
   *
   * @return the response message or <code>null</code> if no response message exists
   *
   * @throws MessagingException
   */
  Message processMessage(Message message)
    throws MessagingException;

  /**
   * Queue the specified message for download by a remote device.
   *
   * @param message the message to queue
   *
   * @throws MessagingException
   */
  void queueMessageForDownload(Message message)
    throws MessagingException;

  /**
   * Queue the specified message for processing.
   *
   * @param message the message to queue
   *
   * @throws MessagingException
   */
  void queueMessageForProcessing(Message message)
    throws MessagingException;

  /**
   * Queue the specified message part for assembly.
   *
   * @param messagePart the message part to queue
   *
   * @throws MessagingException
   */
  void queueMessagePartForAssembly(MessagePart messagePart)
    throws MessagingException;

  /**
   * Reset the locks for the messages.
   *
   * @param status    the current status of the messages that have been locked
   * @param newStatus the new status for the messages that have been unlocked
   *
   * @throws MessagingException
   */
  void resetMessageLocks(Message.Status status, Message.Status newStatus)
    throws MessagingException;

  /**
   * Reset the locks for the message parts.
   *
   * @param status    the current status of the message parts that have been locked
   * @param newStatus the new status for the message parts that have been unlocked
   *
   * @throws MessagingException
   */
  void resetMessagePartLocks(MessagePart.Status status, MessagePart.Status newStatus)
    throws MessagingException;

  /**
   * Set the status for the message.
   *
   * @param message the message to set the status for
   * @param status  the new status
   *
   * @throws MessagingException
   */
  void setMessageStatus(Message message, Message.Status status)
    throws MessagingException;

  /**
   * Unlock the message.
   *
   * @param message the message to unlock
   * @param status  the new status for the unlocked message
   *
   * @throws MessagingException
   */
  void unlockMessage(Message message, Message.Status status)
    throws MessagingException;
}

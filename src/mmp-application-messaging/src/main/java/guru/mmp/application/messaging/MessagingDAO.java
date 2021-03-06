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

import guru.mmp.application.messaging.Message.Priority;
import guru.mmp.application.messaging.Message.Status;
import guru.mmp.common.persistence.DAOException;
import guru.mmp.common.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>MessagingDAO</code> class implements the persistence operations for the
 * messaging infrastructure.
 *
 * @author Marcus Portmann
 */
@Repository
public class MessagingDAO
  implements IMessagingDAO
{
  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(MessagingDAO.class);

  /**
   * The data source used to provide connections to the database.
   */
  @Inject
  @Qualifier("applicationDataSource")
  private DataSource dataSource;

  /**
   * Constructs a new <code>MessagingDAO</code>.
   */
  public MessagingDAO() {}

  /**
   * Have all the parts been queued for assembly for the message?
   *
   * @param messageId  the Universally Unique Identifier (UUID) used to uniquely identify the
   *                   message
   * @param totalParts the total number of parts for the message
   *
   * @return <code>true</code> if all the parts for the message have been queued for assembly or
   *         <code>false</code> otherwise
   */
  public boolean allPartsQueuedForMessage(UUID messageId, int totalParts)
    throws DAOException
  {
    String allPartsQueuedForMessageSQL = "SELECT COUNT(MP.ID) FROM MESSAGING.MESSAGE_PARTS MP "
        + "WHERE MP.MSG_ID=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(allPartsQueuedForMessageSQL))
    {
      statement.setObject(1, messageId);

      try (ResultSet rs = statement.executeQuery())
      {
        return rs.next() && (totalParts == rs.getInt(1));
      }
    }
    catch (Throwable e)
    {
      throw new DAOException(String.format(
          "Failed to check whether all the message parts for the message (%s) have been queued for "
          + "assembly in the database", messageId), e);
    }
  }

  /**
   * Archive the message in the database.
   *
   * @param message the <code>Message</code> instance containing the information for the message
   */
  public void archiveMessage(Message message)
    throws DAOException
  {
    String archiveMessageSQL = "INSERT INTO MESSAGING.ARCHIVED_MESSAGES "
        + "(ID, USERNAME, DEVICE_ID, TYPE_ID, CORRELATION_ID, CREATED, ARCHIVED, DATA) "
        + "VALUES (?, ?, ?, ?, ?," + " ?, ?, ?)";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(archiveMessageSQL))
    {
      statement.setObject(1, message.getId());
      statement.setString(2, message.getUsername());
      statement.setObject(3, message.getDeviceId());
      statement.setObject(4, message.getTypeId());
      statement.setObject(5, message.getCorrelationId());
      statement.setTimestamp(6, Timestamp.valueOf(message.getCreated()));
      statement.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
      statement.setBytes(8, message.getData());

      if (statement.executeUpdate() != 1)
      {
        throw new DAOException(String.format(
            "No rows were affected as a result of executing the SQL statement (%s)",
            archiveMessageSQL));
      }
    }
    catch (Throwable e)
    {
      throw new DAOException(String.format("Failed to archive the message (%s) in the database",
          message.getId()), e);
    }
  }

  /**
   * Create the entry for the error report in the database.
   *
   * @param errorReport the <code>ErrorReport</code> instance containing the information for the
   *                    error report
   */
  public void createErrorReport(ErrorReport errorReport)
    throws DAOException
  {
    String createErrorReportSQL = "INSERT INTO MESSAGING.ERROR_REPORTS "
        + "(ID, APPLICATION_ID, APPLICATION_VERSION, DESCRIPTION, DETAIL, FEEDBACK, CREATED, WHO, "
        + "DEVICE_ID, DATA) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(createErrorReportSQL))
    {
      String description = errorReport.getDescription();

      if (description.length() > 2048)
      {
        description = description.substring(0, 2048);
      }

      String detail = errorReport.getDetail();

      if (detail.length() > 16384)
      {
        detail = detail.substring(0, 16384);
      }

      String feedback = errorReport.getFeedback();

      if (feedback.length() > 4000)
      {
        feedback = feedback.substring(0, 4000);
      }

      statement.setObject(1, errorReport.getId());
      statement.setObject(2, errorReport.getApplicationId());
      statement.setInt(3, errorReport.getApplicationVersion());
      statement.setString(4, description);
      statement.setString(5, detail);
      statement.setString(6, feedback);
      statement.setTimestamp(7, Timestamp.valueOf(errorReport.getCreated()));
      statement.setString(8, errorReport.getWho());
      statement.setObject(9, errorReport.getDeviceId());
      statement.setBytes(10, errorReport.getData());

      if (statement.executeUpdate() != 1)
      {
        throw new DAOException(String.format(
            "No rows were affected as a result of executing the SQL statement (%s)",
            createErrorReportSQL));
      }
    }
    catch (Throwable e)
    {
      throw new DAOException(String.format("Failed to add the error report (%s) to the database",
          errorReport.getId()), e);
    }
  }

  /**
   * Create the entry for the message in the database.
   *
   * @param message the <code>Message</code> instance containing the information for the message
   */
  public void createMessage(Message message)
    throws DAOException
  {
    String createMessageSQL = "INSERT INTO MESSAGING.MESSAGES "
        + "(ID, USERNAME, DEVICE_ID, TYPE_ID, CORRELATION_ID, PRIORITY, STATUS, CREATED, "
        + "PERSISTED, UPDATED, SEND_ATTEMPTS, PROCESS_ATTEMPTS, DOWNLOAD_ATTEMPTS, DATA) "
        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(createMessageSQL))
    {
      LocalDateTime persisted = LocalDateTime.now();

      statement.setObject(1, message.getId());
      statement.setString(2, message.getUsername());
      statement.setObject(3, message.getDeviceId());
      statement.setObject(4, message.getTypeId());
      statement.setObject(5, message.getCorrelationId());
      statement.setInt(6, message.getPriority().getCode());
      statement.setInt(7, message.getStatus().getCode());
      statement.setTimestamp(8, Timestamp.valueOf(message.getCreated()));
      statement.setTimestamp(9, Timestamp.valueOf(persisted));
      statement.setTimestamp(10, Timestamp.valueOf(message.getCreated()));
      statement.setInt(11, message.getSendAttempts());
      statement.setInt(12, message.getProcessAttempts());
      statement.setInt(13, message.getDownloadAttempts());
      statement.setBytes(14, message.getData());

      if (statement.executeUpdate() != 1)
      {
        throw new DAOException(String.format(
            "No rows were affected as a result of executing the SQL statement (%s)",
            createMessageSQL));
      }

      message.setPersisted(persisted);
      message.setUpdated(message.getCreated());
    }
    catch (Throwable e)
    {
      throw new DAOException(String.format("Failed to add the message (%s) to the database",
          message.getId()), e);
    }
  }

  /**
   * Create the entry for the <code>MessagePart<code> in the database.
   *
   * @param messagePart the <code>MessagePart</code> instance containing the information for the
   *                    message part
   */
  public void createMessagePart(MessagePart messagePart)
    throws DAOException
  {
    String createMessagePartSQL = "INSERT INTO MESSAGING.MESSAGE_PARTS "
        + "(ID, PART_NO, TOTAL_PARTS, SEND_ATTEMPTS, DOWNLOAD_ATTEMPTS, STATUS, PERSISTED, MSG_ID, "
        + "MSG_USERNAME, MSG_DEVICE_ID, MSG_TYPE_ID, MSG_CORRELATION_ID, MSG_PRIORITY, MSG_CREATED, "
        + "MSG_DATA_HASH, MSG_ENCRYPTION_IV, MSG_CHECKSUM, DATA) "
        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(createMessagePartSQL))
    {
      LocalDateTime persisted = LocalDateTime.now();

      statement.setObject(1, messagePart.getId());
      statement.setInt(2, messagePart.getPartNo());
      statement.setInt(3, messagePart.getTotalParts());
      statement.setInt(4, messagePart.getSendAttempts());
      statement.setInt(5, messagePart.getDownloadAttempts());
      statement.setInt(6, messagePart.getStatus().getCode());
      statement.setTimestamp(7, Timestamp.valueOf(persisted));
      statement.setObject(8, messagePart.getMessageId());
      statement.setString(9, messagePart.getMessageUsername());
      statement.setObject(10, messagePart.getMessageDeviceId());
      statement.setObject(11, messagePart.getMessageTypeId());
      statement.setObject(12, messagePart.getMessageCorrelationId());
      statement.setInt(13, messagePart.getMessagePriority().getCode());
      statement.setTimestamp(14, Timestamp.valueOf(messagePart.getMessageCreated()));
      statement.setString(15, messagePart.getMessageDataHash());
      statement.setString(16, messagePart.getMessageEncryptionIV());
      statement.setString(17, messagePart.getMessageChecksum());
      statement.setBytes(18, messagePart.getData());

      if (statement.executeUpdate() != 1)
      {
        throw new DAOException(String.format(
            "No rows were affected as a result of executing the SQL statement (%s)",
            createMessagePartSQL));
      }

      messagePart.setPersisted(persisted);
    }
    catch (Throwable e)
    {
      throw new DAOException(String.format("Failed to add the message part (%s) to the database",
          messagePart.getId()), e);
    }
  }

  /**
   * Delete the message.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the message
   */
  public void deleteMessage(UUID id)
    throws DAOException
  {
    String deleteMessageSQL = "DELETE FROM MESSAGING.MESSAGES WHERE ID=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(deleteMessageSQL))
    {
      statement.setObject(1, id);

      statement.executeUpdate();
    }
    catch (Throwable e)
    {
      throw new DAOException(String.format("Failed to delete the message (%s) in the database",
          id), e);
    }
  }

  /**
   * Delete the message part.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the message part
   */
  public void deleteMessagePart(UUID id)
    throws DAOException
  {
    String deleteMessagePartSQL = "DELETE FROM MESSAGING.MESSAGE_PARTS WHERE ID=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(deleteMessagePartSQL))
    {
      statement.setObject(1, id);

      statement.executeUpdate();
    }
    catch (Throwable e)
    {
      throw new DAOException(String.format(
          "Failed to delete the message part (%s) in the database", id), e);
    }
  }

  /**
   * Delete the message parts for the message.
   *
   * @param messageId the Universally Unique Identifier (UUID) used to uniquely identify the message
   */
  public void deleteMessagePartsForMessage(UUID messageId)
    throws DAOException
  {
    String deleteMessagePartsForMessageSQL = "DELETE FROM MESSAGING.MESSAGE_PARTS WHERE MSG_ID=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(deleteMessagePartsForMessageSQL))
    {
      statement.setObject(1, messageId);

      statement.executeUpdate();
    }
    catch (Throwable e)
    {
      throw new DAOException(String.format(
          "Failed to delete the message parts for the message (%s) in the database", messageId), e);
    }
  }

  /**
   * Retrieve the error report.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the error report
   *
   * @return the error report or <code>null</code> if the error report could not be found
   */
  public ErrorReport getErrorReport(UUID id)
    throws DAOException
  {
    String getErrorReportSQL = "SELECT ER.ID, ER.APPLICATION_ID, ER.APPLICATION_VERSION, "
        + "ER.DESCRIPTION, ER.DETAIL, ER.FEEDBACK, ER.CREATED, ER.WHO, ER.DEVICE_ID, ER.DATA "
        + "FROM MESSAGING.ERROR_REPORTS ER WHERE ER.ID=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(getErrorReportSQL))
    {
      statement.setObject(1, id);

      try (ResultSet rs = statement.executeQuery())
      {
        if (rs.next())
        {
          return buildErrorReportFromResultSet(rs);
        }
        else
        {
          return null;
        }
      }
    }
    catch (Throwable e)
    {
      throw new DAOException(String.format(
          "Failed to retrieve the error report (%s) from the database", id), e);
    }
  }

  /**
   * Retrieve the summary for the error report.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the error report
   *
   * @return the summary for the error report or <code>null</code> if the error report could not be
   * found
   */
  public ErrorReportSummary getErrorReportSummary(UUID id)
    throws DAOException
  {
    String getErrorReportSummarySQL =
        "SELECT ER.ID, ER.APPLICATION_ID, P.NAME, ER.APPLICATION_VERSION, ER.CREATED, ER.WHO, "
        + "ER.DEVICE_ID FROM MESSAGING.ERROR_REPORTS ER "
        + "LEFT OUTER JOIN MESSAGING.PACKAGES P ON ER.APPLICATION_ID = P.ID AND ER.ID=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(getErrorReportSummarySQL))
    {
      statement.setObject(1, id);

      try (ResultSet rs = statement.executeQuery())
      {
        if (rs.next())
        {
          return buildErrorReportSummaryFromResultSet(rs);
        }
        else
        {
          return null;
        }
      }
    }
    catch (Throwable e)
    {
      throw new DAOException(String.format(
          "Failed to retrieve the summary for the error report (%s) from the database", id), e);
    }
  }

  /**
   * Retrieve the message.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the message
   *
   * @return the message or <code>null</code> if the message could not be found
   */
  public Message getMessage(UUID id)
    throws DAOException
  {
    String getMessagSQL = "SELECT M.ID, M.USERNAME, M.DEVICE_ID, M.TYPE_ID, M.CORRELATION_ID, "
        + "M.PRIORITY, M.STATUS, M.CREATED, M.PERSISTED, M.UPDATED, M.SEND_ATTEMPTS, "
        + "M.PROCESS_ATTEMPTS, M.DOWNLOAD_ATTEMPTS, M.LOCK_NAME, M.LAST_PROCESSED, M.DATA "
        + "FROM MESSAGING.MESSAGES M WHERE M.ID=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(getMessagSQL))
    {
      statement.setObject(1, id);

      try (ResultSet rs = statement.executeQuery())
      {
        if (rs.next())
        {
          return buildMessageFromResultSet(rs);
        }
        else
        {
          return null;
        }
      }
    }
    catch (Throwable e)
    {
      throw new DAOException(String.format("Failed to retrieve the message (%s) from the database",
          id), e);
    }
  }

  /**
   * Retrieve the message parts queued for assembly for the message.
   *
   * @param messageId the Universally Unique Identifier (UUID) used to uniquely identify the message
   * @param lockName  the name of the lock that should be applied to the message parts queued for
   *                  assembly when they are retrieved
   *
   * @return the message parts queued for assembly for the message
   */
  @SuppressWarnings("resource")
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public List<MessagePart> getMessagePartsQueuedForAssembly(UUID messageId, String lockName)
    throws DAOException
  {
    String getMessagePartsQueuedForAssemblySQL = "SELECT MP.ID, MP.PART_NO, MP.TOTAL_PARTS, "
        + "MP.SEND_ATTEMPTS, MP.DOWNLOAD_ATTEMPTS, MP.STATUS, MP.PERSISTED, MP.UPDATED, MP.MSG_ID, "
        + "MP.MSG_USERNAME, MP.MSG_DEVICE_ID, MP.MSG_TYPE_ID, MP.MSG_CORRELATION_ID, "
        + "MP.MSG_PRIORITY, MP.MSG_CREATED, MP.MSG_DATA_HASH, MP.MSG_ENCRYPTION_IV, "
        + "MP.MSG_CHECKSUM, MP.LOCK_NAME, MP.DATA FROM MESSAGING.MESSAGE_PARTS MP "
        + "WHERE MP.STATUS=? AND MP.MSG_ID=? ORDER BY MP.PART_NO FOR UPDATE";

    String lockMessagePartSQL =
        "UPDATE MESSAGING.MESSAGE_PARTS SET STATUS=?, LOCK_NAME=?, UPDATED=? WHERE ID=?";

    try
    {
      List<MessagePart> messageParts = new ArrayList<>();

      try (Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(
            getMessagePartsQueuedForAssemblySQL))
      {
        statement.setInt(1, MessagePart.Status.QUEUED_FOR_ASSEMBLY.getCode());
        statement.setObject(2, messageId);

        try (ResultSet rs = statement.executeQuery())
        {
          while (rs.next())
          {
            messageParts.add(buildMessagePartFromResultSet(rs));
          }
        }

        for (MessagePart messagePart : messageParts)
        {
          LocalDateTime updated = LocalDateTime.now();

          messagePart.setStatus(MessagePart.Status.ASSEMBLING);
          messagePart.setLockName(lockName);
          messagePart.setUpdated(updated);

          try (PreparedStatement updateStatement = connection.prepareStatement(lockMessagePartSQL))
          {
            updateStatement.setInt(1, MessagePart.Status.ASSEMBLING.getCode());
            updateStatement.setString(2, lockName);
            updateStatement.setTimestamp(3, Timestamp.valueOf(updated));
            updateStatement.setObject(4, messagePart.getId());

            if (updateStatement.executeUpdate() != 1)
            {
              throw new DAOException(String.format(
                  "No rows were affected as a result of executing the SQL statement (%s)",
                  lockMessagePartSQL));
            }
          }
        }
      }

      return messageParts;
    }
    catch (Throwable e)
    {
      throw new DAOException(String.format(
          "Failed to retrieve the message parts that have been queued for assembly for the message "
          + "(%s) from the database", messageId), e);
    }
  }

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
  @SuppressWarnings("resource")
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public List<MessagePart> getMessagePartsQueuedForDownload(String username, UUID deviceId,
      String lockName)
    throws DAOException
  {
    String getMessagePartsQueuedForDownloadSQL = "SELECT MP.ID, MP.PART_NO, MP.TOTAL_PARTS, "
        + "MP.SEND_ATTEMPTS, MP.DOWNLOAD_ATTEMPTS, MP.STATUS, MP.PERSISTED, MP.UPDATED, "
        + "MP.MSG_ID, MP.MSG_USERNAME, MP.MSG_DEVICE_ID, MP.MSG_TYPE_ID, MP.MSG_CORRELATION_ID, "
        + "MP.MSG_PRIORITY, MP.MSG_CREATED, MP.MSG_DATA_HASH, MP.MSG_ENCRYPTION_IV, "
        + "MP.MSG_CHECKSUM, MP.LOCK_NAME, MP.DATA FROM MESSAGING.MESSAGE_PARTS MP "
        + "WHERE MP.STATUS=? AND MP.MSG_USERNAME=? AND MP.MSG_DEVICE_ID=? ORDER BY MP.PART_NO "
        + "FETCH FIRST 3 ROWS ONLY FOR UPDATE";

    String lockMessagePartForDownloadSQL = "UPDATE MESSAGING.MESSAGE_PARTS "
        + "SET STATUS=?, LOCK_NAME=?, UPDATED=?, DOWNLOAD_ATTEMPTS=DOWNLOAD_ATTEMPTS+1 WHERE ID=?";

    try
    {
      List<MessagePart> messageParts = new ArrayList<>();

      try (Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(
            getMessagePartsQueuedForDownloadSQL))
      {
        /*
         * First check if we already have message parts locked for downloading for this device, if
         * so update the lock and return these message parts. This handles the situation where a
         * device attempted to download message parts previously and failed leaving these message
         * parts locked in a "Downloading" state.
         */
        statement.setInt(1, MessagePart.Status.DOWNLOADING.getCode());
        statement.setString(2, username);
        statement.setObject(3, deviceId);

        try (ResultSet rs = statement.executeQuery())
        {
          while (rs.next())
          {
            messageParts.add(buildMessagePartFromResultSet(rs));
          }
        }

        /*
         * If we did not find message parts already locked for downloading then retrieve the message
         * parts that are "QueuedForDownload" for the device.
         */
        if (messageParts.size() == 0)
        {
          statement.setInt(1, MessagePart.Status.QUEUED_FOR_DOWNLOAD.getCode());
          statement.setString(2, username);
          statement.setObject(3, deviceId);

          try (ResultSet rs = statement.executeQuery())
          {
            while (rs.next())
            {
              messageParts.add(buildMessagePartFromResultSet(rs));
            }
          }
        }

        for (MessagePart messagePart : messageParts)
        {
          LocalDateTime updated = LocalDateTime.now();

          messagePart.setStatus(MessagePart.Status.DOWNLOADING);
          messagePart.setLockName(lockName);
          messagePart.setUpdated(updated);
          messagePart.setDownloadAttempts(messagePart.getDownloadAttempts() + 1);

          try (PreparedStatement updateStatement = connection.prepareStatement(
              lockMessagePartForDownloadSQL))
          {
            updateStatement.setInt(1, MessagePart.Status.DOWNLOADING.getCode());
            updateStatement.setString(2, lockName);
            updateStatement.setTimestamp(3, Timestamp.valueOf(updated));
            updateStatement.setObject(4, messagePart.getId());

            if (updateStatement.executeUpdate() != 1)
            {
              throw new DAOException(String.format(
                  "No rows were affected as a result of executing the SQL statement (%s)",
                  lockMessagePartForDownloadSQL));
            }
          }
        }
      }

      return messageParts;
    }
    catch (Throwable e)
    {
      throw new DAOException(String.format(
          "Failed to retrieve the message parts for the user (%s) that have been queued for "
          + "download by the device (%s) from the database", username, deviceId), e);
    }
  }

  /**
   * Get the messages for a user that have been queued for download by a particular remote device.
   *
   * @param username the username identifying the user
   * @param deviceId the Universally Unique Identifier (UUID) used to uniquely identify the device
   * @param lockName name of the lock that should be applied to the messages queued for download
   *                 when they are retrieved
   *
   * @return the messages for a user that have been queued for download by a particular remote
   *         device
   */
  @SuppressWarnings("resource")
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public List<Message> getMessagesQueuedForDownload(String username, UUID deviceId, String lockName)
    throws DAOException
  {
    String getMessagesQueuedForDownloadSQL = "SELECT M.ID, M.USERNAME, M.DEVICE_ID, M.TYPE_ID, "
        + "M.CORRELATION_ID, M.PRIORITY, M.STATUS, M.CREATED, M.PERSISTED, M.UPDATED, "
        + "M.SEND_ATTEMPTS, M.PROCESS_ATTEMPTS, M.DOWNLOAD_ATTEMPTS, M.LOCK_NAME, "
        + "M.LAST_PROCESSED, M.DATA FROM MESSAGING.MESSAGES M "
        + "WHERE M.STATUS=? AND M.USERNAME=? AND M.DEVICE_ID=? ORDER BY M.CREATED "
        + "FETCH FIRST 3 ROWS ONLY FOR UPDATE";

    String lockMessageForDownloadSQL = "UPDATE MESSAGING.MESSAGES "
        + "SET STATUS=?, LOCK_NAME=?, UPDATED=?, DOWNLOAD_ATTEMPTS=DOWNLOAD_ATTEMPTS+1 WHERE ID=?";

    try
    {
      List<Message> messages = new ArrayList<>();

      try (Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(getMessagesQueuedForDownloadSQL))
      {
        /*
         * First check if we already have messages locked for downloading for the user-device
         * combination, if so update the lock and return these messages. This handles the situation
         * where a device attempted to download messages previously and failed leaving these
         * messages locked in a "Downloading" state.
         */

        statement.setInt(1, Message.Status.DOWNLOADING.getCode());
        statement.setString(2, username);
        statement.setObject(3, deviceId);

        try (ResultSet rs = statement.executeQuery())
        {
          while (rs.next())
          {
            Message message = buildMessageFromResultSet(rs);

            if (!StringUtil.isNullOrEmpty(message.getLockName()))
            {
              if (!message.getLockName().equals(lockName))
              {
                if (logger.isDebugEnabled())
                {
                  logger.debug(String.format(
                      "The message (%s) that was originally locked for download using the lock "
                      + "name (%s) will now be locked for download using the lock name (%s)",
                      message.getId(), message.getLockName(), lockName));
                }
              }
            }

            messages.add(message);
          }
        }

        /*
         * If we did not find messages already locked for downloading then retrieve the messages
         * that are "QueuedForDownload" for the user-device combination.
         */
        if (messages.size() == 0)
        {
          statement.setInt(1, Message.Status.QUEUED_FOR_DOWNLOAD.getCode());
          statement.setString(2, username);
          statement.setObject(3, deviceId);

          try (ResultSet rs = statement.executeQuery())
          {
            while (rs.next())
            {
              messages.add(buildMessageFromResultSet(rs));
            }
          }
        }

        for (Message message : messages)
        {
          LocalDateTime updated = LocalDateTime.now();

          message.setStatus(Message.Status.DOWNLOADING);
          message.setLockName(lockName);
          message.setUpdated(updated);
          message.setDownloadAttempts(message.getDownloadAttempts() + 1);

          try (PreparedStatement updateStatement = connection.prepareStatement(
              lockMessageForDownloadSQL))
          {
            updateStatement.setInt(1, Message.Status.DOWNLOADING.getCode());
            updateStatement.setString(2, lockName);
            updateStatement.setTimestamp(3, Timestamp.valueOf(updated));
            updateStatement.setObject(4, message.getId());

            if (updateStatement.executeUpdate() != 1)
            {
              throw new DAOException(String.format(
                  "No rows were affected as a result of executing the SQL statement (%s)",
                  lockMessageForDownloadSQL));
            }
          }
        }
      }

      return messages;
    }
    catch (Throwable e)
    {
      throw new DAOException(String.format(
          "Failed to retrieve the messages for the user (%s) that have been queued for download by "
          + "the device (%s) from the database", username, deviceId), e);
    }
  }

  /**
   * Retrieve the summaries for the most recent error reports.
   *
   * @param maximumNumberOfEntries the maximum number of summaries for the most recent error
   *                               reports to retrieve
   *
   * @return the summaries for the most recent error reports
   */
  public List<ErrorReportSummary> getMostRecentErrorReportSummaries(int maximumNumberOfEntries)
    throws DAOException
  {
    String getMostRecentErrorReportSummariesSQL = "SELECT ER.ID, ER.APPLICATION_ID, P.NAME, "
        + "ER.APPLICATION_VERSION, ER.CREATED, ER.WHO, ER.DEVICE_ID FROM "
        + "MESSAGING.ERROR_REPORTS ER LEFT OUTER JOIN "
        + "MESSAGING.PACKAGES P ON ER.APPLICATION_ID = P.ID";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(String.format(
          "%s ORDER BY CREATED DESC FETCH FIRST %d ROWS ONLY",
          getMostRecentErrorReportSummariesSQL, maximumNumberOfEntries)))
    {
      try (ResultSet rs = statement.executeQuery())
      {
        List<ErrorReportSummary> errorReportSummaries = new ArrayList<>();

        while (rs.next())
        {
          errorReportSummaries.add(buildErrorReportSummaryFromResultSet(rs));
        }

        return errorReportSummaries;
      }
    }
    catch (Throwable e)
    {
      throw new DAOException(
          "Failed to retrieve the summaries for the most recent error reports from the database",
          e);
    }
  }

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
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public Message getNextMessageQueuedForProcessing(int processingRetryDelay, String lockName)
    throws DAOException
  {
    String getNextMessageForProcessingSQL = "SELECT M.ID, M.USERNAME, M.DEVICE_ID, M.TYPE_ID, "
        + "M.CORRELATION_ID, M.PRIORITY, M.STATUS, M.CREATED, M.PERSISTED, M.UPDATED, "
        + "M.SEND_ATTEMPTS, M.PROCESS_ATTEMPTS," + " M.DOWNLOAD_ATTEMPTS, M.LOCK_NAME, "
        + "M.LAST_PROCESSED, M.DATA FROM MESSAGING.MESSAGES M " + "WHERE M.STATUS=? AND (M"
        + ".LAST_PROCESSED<? OR M.LAST_PROCESSED IS NULL) "
        + "ORDER BY M.UPDATED FETCH FIRST 1 ROWS ONLY FOR UPDATE";

    String lockMessageSQL =
        "UPDATE MESSAGING.MESSAGES SET STATUS=?, LOCK_NAME=?, UPDATED=? WHERE ID=?";

    try
    {
      Message message = null;

      try (Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(getNextMessageForProcessingSQL))
      {
        Timestamp processedBefore = new Timestamp(System.currentTimeMillis()
            - processingRetryDelay);

        statement.setInt(1, Message.Status.QUEUED_FOR_PROCESSING.getCode());
        statement.setTimestamp(2, processedBefore);

        try (ResultSet rs = statement.executeQuery())
        {
          if (rs.next())
          {
            LocalDateTime updated = LocalDateTime.now();

            message = buildMessageFromResultSet(rs);

            message.setStatus(Message.Status.PROCESSING);
            message.setLockName(lockName);
            message.setUpdated(updated);

            try (PreparedStatement updateStatement = connection.prepareStatement(lockMessageSQL))
            {
              updateStatement.setInt(1, Message.Status.PROCESSING.getCode());
              updateStatement.setString(2, lockName);
              updateStatement.setTimestamp(3, Timestamp.valueOf(updated));
              updateStatement.setObject(4, message.getId());

              if (updateStatement.executeUpdate() != 1)
              {
                throw new DAOException(String.format(
                    "No rows were affected as a result of executing the SQL statement (%s)",
                    lockMessageSQL));
              }
            }
          }
        }
      }

      return message;
    }
    catch (Throwable e)
    {
      throw new DAOException(
          "Failed to retrieve the next message that has been queued for processing from the database",
          e);
    }
  }

  /**
   * Returns the total number of error reports in the database.
   *
   * @return the total number of error reports in the database
   */
  public int getNumberOfErrorReports()
    throws DAOException
  {
    String getNumberOfErrorReportsSQL = "SELECT COUNT(ER.ID) FROM MESSAGING.ERROR_REPORTS ER";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(getNumberOfErrorReportsSQL))
    {
      try (ResultSet rs = statement.executeQuery())
      {
        if (rs.next())
        {
          return rs.getInt(1);
        }
        else
        {
          return 0;
        }
      }
    }
    catch (Throwable e)
    {
      throw new DAOException(
          "Failed to retrieve the total number of error reports in the database", e);
    }
  }

  /**
   * Increment the processing attempts for the message.
   *
   * @param message the message whose processing attempts should be incremented
   */
  public void incrementMessageProcessingAttempts(Message message)
    throws DAOException
  {
    String incrementMessageProcessingAttemptsSQL = "UPDATE MESSAGING.MESSAGES "
        + "SET PROCESS_ATTEMPTS=PROCESS_ATTEMPTS + 1, UPDATED=?, LAST_PROCESSED=? WHERE ID=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(
          incrementMessageProcessingAttemptsSQL))
    {
      LocalDateTime currentTime = LocalDateTime.now();

      statement.setTimestamp(1, Timestamp.valueOf(currentTime));
      statement.setTimestamp(2, Timestamp.valueOf(currentTime));
      statement.setObject(3, message.getId());

      if (statement.executeUpdate() != 1)
      {
        throw new DAOException(String.format(
            "No rows were affected as a result of executing the SQL statement (%s)",
            incrementMessageProcessingAttemptsSQL));
      }

      message.setProcessAttempts(message.getProcessAttempts() + 1);
      message.setLastProcessed(currentTime);
    }
    catch (Throwable e)
    {
      throw new DAOException(String.format(
          "Failed to increment the processing attempts for the message (%s) in the database",
          message.getId()), e);
    }
  }

  /**
   * Has the message already been archived?
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the message
   *
   * @return <code>true</code> if the message has already been archived or <code>false</code>
   *         otherwise
   */
  public boolean isMessageArchived(UUID id)
    throws DAOException
  {
    String isMessageArchivedSQL = "SELECT AM.ID FROM MESSAGING.ARCHIVED_MESSAGES AM WHERE AM.ID=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(isMessageArchivedSQL))
    {
      statement.setObject(1, id);

      try (ResultSet rs = statement.executeQuery())
      {
        return rs.next();
      }
    }
    catch (Throwable e)
    {
      throw new DAOException("Failed to check whether the message (" + id
          + ") is archived in the database", e);
    }
  }

  /**
   * Has the message part already been queued for assembly?
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the message part
   *
   * @return <code>true</code> if the message part has already been queued for assemble or
   *         <code>false</code> otherwise
   */
  public boolean isMessagePartQueuedForAssembly(UUID id)
    throws DAOException
  {
    String isMessagePartQueuedForAssemblySQL =
        "SELECT MP.ID FROM MESSAGING.MESSAGE_PARTS MP WHERE MP.ID=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(isMessagePartQueuedForAssemblySQL))
    {
      statement.setObject(1, id);

      try (ResultSet rs = statement.executeQuery())
      {
        return rs.next();
      }
    }
    catch (Throwable e)
    {
      throw new DAOException(String.format(
          "Failed to check whether the message part (%s) is queued for assembly in the database",
          id), e);
    }
  }

///**
// * Reset the expired message locks.
// *
// * @param lockTimeout the lock timeout in seconds
// * @param status      the current status of the messages that have been locked
// * @param newStatus   the new status for the messages that have been unlocked
// *
// * @return the number of message locks reset
// */
//public int resetExpiredMessageLocks(int lockTimeout, Message.Status status, Message
//    .Status newStatus)
//  throws DAOException
//{
// resetExpiredMessageLocksSQL
// resetExpiredMessageLocksSQL = "UPDATE " + schemaPrefix + "MESSAGES M "
// + "SET STATUS=?, LOCK_NAME=NULL, UPDATED=?
// + "WHERE M.LOCK_NAME IS NOT NULL AND M.STATUS=? AND M.UPDATED < ?";
//
//  try (Connection connection = applicationDataSource.getConnection();
//    PreparedStatement statement = connection.prepareStatement(resetExpiredMessageLocksSQL))
//  {
//    statement.setInt(1, newStatus.getCode());
//    statement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
//    statement.setInt(3, status.getCode());
//    statement.setTimestamp(4, new Timestamp(System.currentTimeMillis() - (lockTimeout * 1000L)));
//
//    return statement.executeUpdate();
//  }
//  catch (Throwable e)
//  {
//    throw new DAOException(String.format(
//        "Failed to reset the expired locks for the messages with the status (%s)", status), e);
//  }
//}

///**
// * Reset the expired message part locks.
// *
// * @param lockTimeout the lock timeout in seconds
// * @param status      the current status of the message parts that have been locked
// * @param newStatus   the new status for the message parts that have been unlocked
// *
// * @return the number of message part locks reset
// */
//public int resetExpiredMessagePartLocks(int lockTimeout, MessagePart.Status status, MessagePart
//    .Status newStatus)
//  throws DAOException
//
// resetExpiredMessagePartLocksSQL
// resetExpiredMessagePartLocksSQL = "UPDATE " + schemaPrefix + "MESSAGE_PARTS MP "
// + "SET STATUS=?, LOCK_NAME=NULL, UPDATED=? "
// + "WHERE MP.LOCK_NAME IS NOT NULL AND MP.STATUS=? AND MP.UPDATED < ?";
//
//  try (Connection connection = applicationDataSource.getConnection();
//    PreparedStatement statement = connection.prepareStatement(resetExpiredMessagePartLocksSQL))
//  {
//    statement.setInt(1, newStatus.getCode());
//    statement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
//    statement.setInt(3, status.getCode());
//    statement.setTimestamp(4, new Timestamp(System.currentTimeMillis() - (lockTimeout * 1000L)));
//
//    return statement.executeUpdate();
//  }
//  catch (Throwable e)
//  {
//    throw new DAOException(String.format(
//        "Failed to reset the expired locks for the message parts with the status (%s)", status),
//        e);
//  }
//}

  /**
   * Reset the message locks.
   *
   * @param lockName  the name of the lock applied by the entity that has locked the messages
   * @param status    the current status of the messages that have been locked
   * @param newStatus the new status for the messages that have been unlocked
   *
   * @return the number of message locks reset
   */
  public int resetMessageLocks(String lockName, Message.Status status, Message.Status newStatus)
    throws DAOException
  {
    String resetMessageLocksSQL = "UPDATE MESSAGING.MESSAGES "
        + "SET STATUS=?, LOCK_NAME=NULL, UPDATED=? WHERE LOCK_NAME=? AND STATUS=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(resetMessageLocksSQL))
    {
      statement.setInt(1, newStatus.getCode());
      statement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
      statement.setString(3, lockName);
      statement.setInt(4, status.getCode());

      return statement.executeUpdate();
    }
    catch (Throwable e)
    {
      throw new DAOException(String.format(
          "Failed to reset the locks for the messages with the status (%s) that have been locked "
          + "using the lock name (%s)", status, lockName), e);
    }
  }

  /**
   * Reset the message part locks.
   *
   * @param lockName  the name of the lock applied by the entity that has locked the message parts
   * @param status    the current status of the message parts that have been locked
   * @param newStatus the new status for the message parts that have been unlocked
   *
   * @return the number of message part locks reset
   */
  public int resetMessagePartLocks(String lockName, MessagePart.Status status, MessagePart
      .Status newStatus)
    throws DAOException
  {
    String resetMessagePartLocksSQL = "UPDATE MESSAGING.MESSAGE_PARTS "
        + "SET STATUS=?, LOCK_NAME=NULL WHERE LOCK_NAME=? AND STATUS=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(resetMessagePartLocksSQL))
    {
      statement.setInt(1, newStatus.getCode());
      statement.setString(2, lockName);
      statement.setInt(3, status.getCode());

      return statement.executeUpdate();
    }
    catch (Throwable e)
    {
      throw new DAOException(String.format(
          "Failed to reset the locks for the message parts with the status (%s) that have been "
          + "locked using the lock name (%s)", status, lockName), e);
    }
  }

///**
// * Set the status for a message part.
// *
// * @param id     the Universally Unique Identifier (UUID) used to uniquely identify the message
// *               part
// * @param status the new status
// */
//public void setMessagePartStatus(UUID id, MessagePart.Status status)
//  throws DAOException
//{
// setMessagePartStatusSQL
// setMessagePartStatusSQL = "UPDATE " + schemaPrefix + "MESSAGE_PARTS MP "
// + "SET STATUS=?, UPDATED=? WHERE MP.ID=?";
//
//  try (Connection connection = applicationDataSource.getConnection();
//    PreparedStatement statement = connection.prepareStatement(setMessagePartStatusSQL))
//  {
//    statement.setInt(1, status.getCode());
//    statement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
//    statement.setObject(3, id);
//
//    if (statement.executeUpdate() != 1)
//    {
//      throw new DAOException(String.format(
//          "No rows were affected as a result of executing the SQL statement (%s)",
//          setMessagePartStatusSQL));
//    }
//  }
//  catch (Throwable e)
//  {
//    throw new DAOException(String.format(
//        "Failed to set the status for the message part (%s) to (%s) in the database", id,
//        status.toString()), e);
//  }
//}

///**
// * Set the status for a message.
// *
// * @param id     the Universally Unique Identifier (UUID) used to uniquely identify the message
// * @param status the new status
// */
//public void setMessageStatus(UUID id, Message.Status status)
//  throws DAOException
//{
// setMessageStatusSQL
// setMessageStatusSQL = "UPDATE " + schemaPrefix + "MESSAGES M "
// + "SET STATUS=?, UPDATED=? WHERE M.ID=?";
//
//  try (Connection connection = applicationDataSource.getConnection();
//    PreparedStatement statement = connection.prepareStatement(setMessageStatusSQL))
//  {
//    statement.setInt(1, status.getCode());
//    statement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
//    statement.setObject(3, id);
//
//    if (statement.executeUpdate() != 1)
//    {
//      throw new DAOException(String.format(
//          "No rows were affected as a result of executing the SQL statement (%s)",
//          setMessageStatusSQL));
//    }
//  }
//  catch (Throwable e)
//  {
//    throw new DAOException(String.format(
//        "Failed to set the status for the message (%s) to (%s) in the database", id,
//        status.toString()), e);
//  }
//}

  /**
   * Unlock a locked message.
   *
   * @param id     the Universally Unique Identifier (UUID) used to uniquely identify the message
   * @param status the new status for the unlocked message
   */
  public void unlockMessage(UUID id, Message.Status status)
    throws DAOException
  {
    String unlockMessageSQL =
        "UPDATE MESSAGING.MESSAGES SET STATUS=?, UPDATED=?, LOCK_NAME=NULL WHERE ID=?";

    try (Connection connection = dataSource.getConnection();
      PreparedStatement statement = connection.prepareStatement(unlockMessageSQL))
    {
      statement.setInt(1, status.getCode());
      statement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
      statement.setObject(3, id);

      if (statement.executeUpdate() != 1)
      {
        throw new DAOException(String.format(
            "No rows were affected as a result of executing the SQL statement (%s)",
            unlockMessageSQL));
      }
    }
    catch (Throwable e)
    {
      throw new DAOException(String.format(
          "Failed to unlock and set the status for the message (%s) to (%s) in the database", id,
          status.toString()), e);
    }
  }

  private ErrorReport buildErrorReportFromResultSet(ResultSet rs)
    throws SQLException
  {
    byte[] data = rs.getBytes(10);

    return new ErrorReport(UUID.fromString(rs.getString(1)), UUID.fromString(rs.getString(2)),
        rs.getInt(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getTimestamp(7)
        .toLocalDateTime(), rs.getString(8), UUID.fromString(rs.getString(9)),
        (data == null)
        ? new byte[0]
        : data);
  }

  private ErrorReportSummary buildErrorReportSummaryFromResultSet(ResultSet rs)
    throws SQLException
  {
    String applicationName = rs.getString(3);

    if (applicationName == null)
    {
      applicationName = "Unknown";
    }

    return new ErrorReportSummary(UUID.fromString(rs.getString(1)), UUID.fromString(rs.getString(
        2)), applicationName, rs.getInt(4), rs.getTimestamp(5).toLocalDateTime(), rs.getString(6),
        UUID.fromString(rs.getString(7)));
  }

  private Message buildMessageFromResultSet(ResultSet rs)
    throws SQLException
  {
    return new Message(UUID.fromString(rs.getString(1)), rs.getString(2), UUID.fromString(
        rs.getString(3)), UUID.fromString(rs.getString(4)), UUID.fromString(rs.getString(5)),
        Priority.fromCode(rs.getInt(6)), Status.fromCode(rs.getInt(7)), (rs.getTimestamp(8) == null)
        ? null
        : rs.getTimestamp(8).toLocalDateTime(), (rs.getTimestamp(9) == null)
        ? null
        : rs.getTimestamp(9).toLocalDateTime(), (rs.getTimestamp(10) == null)
        ? null
        : rs.getTimestamp(10).toLocalDateTime(), rs.getInt(11), rs.getInt(12), rs.getInt(13),
            rs.getString(14), (rs.getTimestamp(15) == null)
        ? null
        : rs.getTimestamp(15).toLocalDateTime(), rs.getBytes(16), "", "");
  }

  private MessagePart buildMessagePartFromResultSet(ResultSet rs)
    throws SQLException
  {
    return new MessagePart(UUID.fromString(rs.getString(1)), rs.getInt(2), rs.getInt(3), rs.getInt(
        4), rs.getInt(5), MessagePart.Status.fromCode(rs.getInt(6)), (rs.getTimestamp(7) == null)
        ? null
        : rs.getTimestamp(7).toLocalDateTime(), (rs.getTimestamp(8) == null)
        ? null
        : rs.getTimestamp(8).toLocalDateTime(), UUID.fromString(rs.getString(9)), rs.getString(10),
            UUID.fromString(rs.getString(11)), UUID.fromString(rs.getString(12)), UUID.fromString(
            rs.getString(13)), Priority.fromCode(rs.getInt(14)), (rs.getTimestamp(15) == null)
        ? null
        : rs.getTimestamp(15).toLocalDateTime(), rs.getString(16), rs.getString(17), rs.getString(
            18), rs.getString(19), rs.getBytes(20));
  }

///**
// * Unlock a locked message part.
// *
// * @param id     the Universally Unique Identifier (UUID) used to uniquely identify the message
// *               part
// * @param status the new status for the unlocked message part
// */
//public void unlockMessagePart(UUID id, MessagePart.Status status)
//  throws DAOException
//{
// unlockMessagePartSQL
// unlockMessagePartSQL = "UPDATE " + schemaPrefix + "MESSAGE_PARTS MP "
// + "SET STATUS=?, UPDATED=?, LOCK_NAME=NULL WHERE MP.ID=?";
//
//  try (Connection connection = applicationDataSource.getConnection();
//    PreparedStatement statement = connection.prepareStatement(unlockMessagePartSQL))
//  {
//    statement.setInt(1, status.getCode());
//    statement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
//    statement.setObject(3, id);
//
//    if (statement.executeUpdate() != 1)
//    {
//      throw new DAOException(String.format(
//          "No rows were affected as a result of executing the SQL statement (%s)",
//          unlockMessagePartSQL));
//    }
//  }
//  catch (Throwable e)
//  {
//    throw new DAOException(String.format(
//        "Failed to unlock and set the status for the message part (%s) to (%s) in the database",
//        id, status.toString()), e);
//  }
//}

}

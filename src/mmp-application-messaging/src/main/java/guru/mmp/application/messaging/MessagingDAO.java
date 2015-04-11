/*
 * Copyright 2014 Marcus Portmann
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
import guru.mmp.application.persistence.DAOException;
import guru.mmp.application.persistence.DataAccessObject;
import guru.mmp.common.crypto.EncryptionScheme;
import guru.mmp.common.persistence.DAOUtil;
import guru.mmp.common.persistence.IDGenerator;
import guru.mmp.common.persistence.TransactionManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//~--- JDK imports ------------------------------------------------------------

import java.sql.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;

import javax.naming.InitialContext;

import javax.sql.DataSource;

/**
 * The <code>MessagingDAO</code> class implements the persistence operations for the
 * messaging infrastructure.
 *
 * @author Marcus Portmann
 */
@ApplicationScoped
@Default
public class MessagingDAO
  implements IMessagingDAO
{
  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(MessagingDAO.class);
  private String allPartsQueuedForMessageSQL;
  private String archiveMessageSQL;
  private String createErrorReportSQL;
  private String createMessagePartSQL;
  private String createMessageSQL;

  /** The data source used to provide connections to the database. */
  private DataSource dataSource;
  private String deleteMessagePartSQL;
  private String deleteMessagePartsForMessageSQL;
  private String deleteMessageSQL;
  private String getErrorReportByIdSQL;
  private String getErrorReportSummaryByIdSQL;
  private String getMessageByIdSQL;
  private String getMessagePartsQueuedForAssemblySQL;
  private String getMessagePartsQueuedForDownloadSQL;
  private String getMessagesQueuedForDownloadForUserSQL;
  private String getMessagesQueuedForDownloadSQL;
  private String getMostRecentErrorReportSummariesSQL;
  private String getNextMessageForProcessingSQL;
  private String getNumberOfErrorReportsSQL;

  /** The ID generator used to generate unique numeric IDs for the DAO. */
  private IDGenerator idGenerator;
  private String incrementMessageProcessingAttemptsSQL;
  private String isMessageArchivedSQL;
  private String isMessagePartQueuedForAssemblySQL;
  private String lockMessageForDownloadSQL;
  private String lockMessagePartForDownloadSQL;
  private String lockMessagePartSQL;
  private String lockMessageSQL;
  private String logMessageAuditSQL;
  private String resetExpiredMessageLocksSQL;
  private String resetExpiredMessagePartLocksSQL;
  private String resetMessageLocksSQL;
  private String resetMessagePartLocksSQL;
  private String setMessagePartStatusSQL;
  private String setMessageStatusSQL;
  private String unlockMessagePartSQL;
  private String unlockMessageSQL;

  /**
   * Constructs a new <code>MessagingDAO</code>.
   */
  public MessagingDAO() {}

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
  public boolean allPartsQueuedForMessage(String messageId, int totalParts)
    throws DAOException
  {
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet rs = null;

    try
    {
      connection = dataSource.getConnection();

      statement = connection.prepareStatement(allPartsQueuedForMessageSQL);
      statement.setString(1, messageId);

      rs = statement.executeQuery();

      return rs.next() && (totalParts == rs.getInt(1));
    }
    catch (DAOException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new DAOException("Failed to check whether all the message parts for the message ("
          + messageId + ") have been queued for assembly in the database", e);
    }
    finally
    {
      DAOUtil.close(rs);
      DAOUtil.close(statement);
      DAOUtil.close(connection);
    }
  }

  /**
   * Archive the message in the database.
   *
   * @param message the <code>Message</code> instance containing the information for the message
   *
   * @throws DAOException
   */
  public void archiveMessage(Message message)
    throws DAOException
  {
    Connection connection = null;
    PreparedStatement statement = null;

    try
    {
      connection = dataSource.getConnection();

      statement = connection.prepareStatement(archiveMessageSQL);

      statement.setString(1, message.getId());
      statement.setString(2, message.getUser());
      statement.setString(3, message.getOrganisation());
      statement.setString(4, message.getDevice());
      statement.setString(5, message.getType());
      statement.setInt(6, message.getTypeVersion());
      statement.setString(7, message.getCorrelationId());
      statement.setTimestamp(8, new Timestamp(message.getCreated().getTime()));
      statement.setTimestamp(9, new Timestamp(System.currentTimeMillis()));
      statement.setBytes(10, message.getData());

      if (statement.executeUpdate() != 1)
      {
        throw new DAOException("Failed to archive the message (" + message.getId() + ") in the"
            + " database: No rows were affected as a result of executing the SQL statement ("
            + archiveMessageSQL + ")");
      }
    }
    catch (DAOException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new DAOException("Failed to archive the message (" + message.getId()
          + ") in the database", e);
    }
    finally
    {
      DAOUtil.close(statement);
      DAOUtil.close(connection);
    }
  }

  /**
   * Create the entry for the error report in the database.
   *
   * @param errorReport the <code>ErrorReport</code> instance containing the information for the
   *                    error report
   *
   * @throws DAOException
   */
  public void createErrorReport(ErrorReport errorReport)
    throws DAOException
  {
    Connection connection = null;
    PreparedStatement statement = null;

    try
    {
      connection = dataSource.getConnection();

      statement = connection.prepareStatement(createErrorReportSQL);

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

      statement.setString(1, errorReport.getId());
      statement.setString(2, errorReport.getApplicationId());
      statement.setInt(3, errorReport.getApplicationVersion());
      statement.setString(4, description);
      statement.setString(5, detail);
      statement.setString(6, feedback);
      statement.setTimestamp(7, new Timestamp(errorReport.getCreated().getTime()));
      statement.setString(8, errorReport.getWho());
      statement.setString(9, errorReport.getDevice());
      statement.setBytes(10, errorReport.getData());

      if (statement.executeUpdate() != 1)
      {
        throw new DAOException("Failed to add the error report (" + errorReport.getId()
            + ") to the"
            + " database: No rows were affected as a result of executing the SQL statement ("
            + createErrorReportSQL + ")");
      }
    }
    catch (DAOException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new DAOException("Failed to add the error report (" + errorReport.getId()
          + ") to the database", e);
    }
    finally
    {
      DAOUtil.close(statement);
      DAOUtil.close(connection);
    }
  }

  /**
   * Create the entry for the message in the database.
   *
   * @param message the <code>Message</code> instance containing the information for the message
   *
   * @throws DAOException
   */
  public void createMessage(Message message)
    throws DAOException
  {
    Connection connection = null;
    PreparedStatement statement = null;

    try
    {
      connection = dataSource.getConnection();

      statement = connection.prepareStatement(createMessageSQL);

      Date persisted = new Date();

      statement.setString(1, message.getId());
      statement.setString(2, message.getUser());
      statement.setString(3, message.getOrganisation());
      statement.setString(4, message.getDevice());
      statement.setString(5, message.getType());
      statement.setInt(6, message.getTypeVersion());
      statement.setString(7, message.getCorrelationId());
      statement.setInt(8, message.getPriority().getCode());
      statement.setInt(9, message.getStatus().getCode());
      statement.setTimestamp(10, new Timestamp(message.getCreated().getTime()));
      statement.setTimestamp(11, new Timestamp(persisted.getTime()));
      statement.setTimestamp(12, new Timestamp(message.getCreated().getTime()));
      statement.setInt(13, message.getSendAttempts());
      statement.setInt(14, message.getProcessAttempts());
      statement.setInt(15, message.getDownloadAttempts());
      statement.setBytes(16, message.getData());

      if (statement.executeUpdate() != 1)
      {
        throw new DAOException("Failed to add the message (" + message.getId() + ") to the"
            + " database: No rows were affected as a result of executing the SQL statement ("
            + createMessageSQL + ")");
      }

      message.setPersisted(persisted);
      message.setUpdated(message.getCreated());
    }
    catch (DAOException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new DAOException("Failed to add the message (" + message.getId() + ") to the database",
          e);
    }
    finally
    {
      DAOUtil.close(statement);
      DAOUtil.close(connection);
    }
  }

  /**
   * Create the entry for the <code>MessagePart<code> in the database.
   *
   * @param messagePart the <code>MessagePart</code> instance containing the information for the
   *                    message part
   *
   * @throws DAOException
   */
  public void createMessagePart(MessagePart messagePart)
    throws DAOException
  {
    Connection connection = null;
    PreparedStatement statement = null;

    try
    {
      connection = dataSource.getConnection();

      statement = connection.prepareStatement(createMessagePartSQL);

      Date persisted = new Date();

      statement.setString(1, messagePart.getId());
      statement.setInt(2, messagePart.getPartNo());
      statement.setInt(3, messagePart.getTotalParts());
      statement.setInt(4, messagePart.getSendAttempts());
      statement.setInt(5, messagePart.getDownloadAttempts());
      statement.setInt(6, messagePart.getStatus().getCode());
      statement.setTimestamp(7, new Timestamp(persisted.getTime()));
      statement.setString(8, messagePart.getMessageId());
      statement.setString(9, messagePart.getMessageUser());
      statement.setString(10, messagePart.getMessageOrganisation());
      statement.setString(11, messagePart.getMessageDevice());
      statement.setString(12, messagePart.getMessageType());
      statement.setInt(13, messagePart.getMessageTypeVersion());
      statement.setString(14, messagePart.getMessageCorrelationId());
      statement.setInt(15, messagePart.getMessagePriority().getCode());
      statement.setTimestamp(16, new Timestamp(messagePart.getMessageCreated().getTime()));
      statement.setString(17, messagePart.getMessageDataHash());
      statement.setInt(18, messagePart.getMessageEncryptionScheme().getCode());
      statement.setString(19, messagePart.getMessageEncryptionIV());
      statement.setString(20, messagePart.getMessageChecksum());
      statement.setBytes(21, messagePart.getData());

      if (statement.executeUpdate() != 1)
      {
        throw new DAOException("Failed to add the message part (" + messagePart.getId()
            + ") to the database: No rows were affected as a result of executing the SQL statement"
            + " (" + createMessagePartSQL + ")");
      }

      messagePart.setPersisted(persisted);
    }
    catch (DAOException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new DAOException("Failed to add the message part (" + messagePart.getId()
          + ") to the database", e);
    }
    finally
    {
      DAOUtil.close(statement);
      DAOUtil.close(connection);
    }
  }

  /**
   * Delete the message.
   *
   * @param id the ID uniquely identifying the message
   *
   * @throws DAOException
   */
  public void deleteMessage(String id)
    throws DAOException
  {
    Connection connection = null;
    PreparedStatement statement = null;

    try
    {
      connection = dataSource.getConnection();

      statement = connection.prepareStatement(deleteMessageSQL);
      statement.setString(1, id);

      statement.executeUpdate();
    }
    catch (DAOException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new DAOException("Failed to delete the message (" + id + ") from the database", e);
    }
    finally
    {
      DAOUtil.close(statement);
      DAOUtil.close(connection);
    }
  }

  /**
   * Delete the message part.
   *
   * @param id the ID uniquely identifying the message part
   *
   * @throws DAOException
   */
  public void deleteMessagePart(String id)
    throws DAOException
  {
    Connection connection = null;
    PreparedStatement statement = null;

    try
    {
      connection = dataSource.getConnection();

      statement = connection.prepareStatement(deleteMessagePartSQL);
      statement.setString(1, id);

      statement.executeUpdate();
    }
    catch (DAOException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new DAOException("Failed to delete the message part (" + id + ") from the database", e);
    }
    finally
    {
      DAOUtil.close(statement);
      DAOUtil.close(connection);
    }
  }

  /**
   * Delete the message parts for the message with the specified ID.
   *
   * @param messageId the ID uniquely identifying the message whose message parts should be deleted
   *
   * @throws DAOException
   */
  public void deleteMessagePartsForMessage(String messageId)
    throws DAOException
  {
    Connection connection = null;
    PreparedStatement statement = null;

    try
    {
      connection = dataSource.getConnection();

      statement = connection.prepareStatement(deleteMessagePartsForMessageSQL);
      statement.setString(1, messageId);

      statement.executeUpdate();
    }
    catch (DAOException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new DAOException("Failed to delete the message parts for the message (" + messageId
          + ") from the database", e);
    }
    finally
    {
      DAOUtil.close(statement);
      DAOUtil.close(connection);
    }
  }

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
  public ErrorReport getErrorReport(String id)
    throws DAOException
  {
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet rs = null;

    try
    {
      connection = dataSource.getConnection();

      statement = connection.prepareStatement(getErrorReportByIdSQL);
      statement.setString(1, id);

      rs = statement.executeQuery();

      if (rs.next())
      {
        return getErrorReport(rs);
      }
      else
      {
        return null;
      }
    }
    catch (DAOException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new DAOException("Failed to retrieve the error report (" + id + ") from the database",
          e);
    }
    finally
    {
      DAOUtil.close(rs);
      DAOUtil.close(statement);
      DAOUtil.close(connection);
    }
  }

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
  public ErrorReportSummary getErrorReportSummary(String id)
    throws DAOException
  {
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet rs = null;

    try
    {
      connection = dataSource.getConnection();

      statement = connection.prepareStatement(getErrorReportSummaryByIdSQL);
      statement.setString(1, id);

      rs = statement.executeQuery();

      if (rs.next())
      {
        return getErrorReportSummary(rs);
      }
      else
      {
        return null;
      }
    }
    catch (DAOException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new DAOException("Failed to retrieve the summary for the error report (" + id
          + ") from the database", e);
    }
    finally
    {
      DAOUtil.close(rs);
      DAOUtil.close(statement);
      DAOUtil.close(connection);
    }
  }

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
  public Message getMessage(String id)
    throws DAOException
  {
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet rs = null;

    try
    {
      connection = dataSource.getConnection();

      statement = connection.prepareStatement(getMessageByIdSQL);
      statement.setString(1, id);

      rs = statement.executeQuery();

      if (rs.next())
      {
        return getMessage(rs);
      }
      else
      {
        return null;
      }
    }
    catch (DAOException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new DAOException("Failed to retrieve the message (" + id + ") from the database", e);
    }
    finally
    {
      DAOUtil.close(rs);
      DAOUtil.close(statement);
      DAOUtil.close(connection);
    }
  }

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
  @SuppressWarnings("resource")
  public List<MessagePart> getMessagePartsQueuedForAssembly(String messageId, String lockName)
    throws DAOException
  {
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet rs = null;
    PreparedStatement updateStatement = null;

    // Retrieve the Transaction Manager
    TransactionManager transactionManager = TransactionManager.getTransactionManager();
    javax.transaction.Transaction existingTransaction = null;

    try
    {
      if (transactionManager.isTransactionActive())
      {
        existingTransaction = transactionManager.beginNew();
      }
      else
      {
        transactionManager.begin();
      }

      connection = dataSource.getConnection();

      statement = connection.prepareStatement(getMessagePartsQueuedForAssemblySQL);
      statement.setInt(1, MessagePart.Status.QUEUED_FOR_ASSEMBLY.getCode());
      statement.setString(2, messageId);

      rs = statement.executeQuery();

      List<MessagePart> messageParts = new ArrayList<>();

      while (rs.next())
      {
        messageParts.add(getMessagePart(rs));
      }

      DAOUtil.close(rs);
      rs = null;

      for (MessagePart messagePart : messageParts)
      {
        Timestamp updated = new Timestamp(System.currentTimeMillis());

        messagePart.setStatus(MessagePart.Status.ASSEMBLING);
        messagePart.setLockName(lockName);
        messagePart.setUpdated(updated);

        updateStatement = connection.prepareStatement(lockMessagePartSQL);
        updateStatement.setInt(1, MessagePart.Status.ASSEMBLING.getCode());
        updateStatement.setString(2, lockName);
        updateStatement.setTimestamp(3, updated);
        updateStatement.setString(4, messagePart.getId());

        if (updateStatement.executeUpdate() != 1)
        {
          throw new DAOException("Failed to lock the message part (" + messagePart.getId()
              + ") queued for"
              + " assembly: No rows were affected as a result of executing the SQL"
              + " statement (" + lockMessagePartSQL + ")");
        }

        DAOUtil.close(updateStatement);
        updateStatement = null;
      }

      transactionManager.commit();

      return messageParts;
    }
    catch (DAOException e)
    {
      try
      {
        transactionManager.rollback();
      }
      catch (Throwable f)
      {
        logger.error("Failed to rollback the transaction while retrieving"
            + " the message parts that have been queued for assembly for the message (" + messageId
            + ") from the database", f);
      }

      throw e;
    }
    catch (Throwable e)
    {
      try
      {
        transactionManager.rollback();
      }
      catch (Throwable f)
      {
        logger.error("Failed to rollback the transaction while retrieving"
            + " the message parts that have been queued for assembly for the message (" + messageId
            + ") from the database", f);
      }

      throw new DAOException("Failed to retrieve the message parts that have been queued for"
          + " assembly for the message (" + messageId + ") from the database", e);
    }
    finally
    {
      DAOUtil.close(updateStatement);
      DAOUtil.close(rs);
      DAOUtil.close(statement);
      DAOUtil.close(connection);

      try
      {
        if (existingTransaction != null)
        {
          transactionManager.resume(existingTransaction);
        }
      }
      catch (Throwable e)
      {
        logger.error("Failed to resume the original transaction while retrieving"
            + " the message parts that have been queued for assembly for the message (" + messageId
            + ") from the database", e);
      }
    }
  }

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
  @SuppressWarnings("resource")
  public List<MessagePart> getMessagePartsQueuedForDownload(String device, String lockName)
    throws DAOException
  {
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet rs = null;
    PreparedStatement updateStatement = null;

    // Retrieve the Transaction Manager
    TransactionManager transactionManager = TransactionManager.getTransactionManager();
    javax.transaction.Transaction existingTransaction = null;

    try
    {
      if (transactionManager.isTransactionActive())
      {
        existingTransaction = transactionManager.beginNew();
      }
      else
      {
        transactionManager.begin();
      }

      connection = dataSource.getConnection();

      /*
       * First check if we already have message parts locked for downloading for this device, if
       * so update the lock and return these message parts. This handles the situation where a
       * device attempted to download message parts previously and failed leaving these message
       * parts locked in a "Downloading" state.
       */
      statement = connection.prepareStatement(getMessagePartsQueuedForDownloadSQL);
      statement.setInt(1, MessagePart.Status.DOWNLOADING.getCode());
      statement.setString(2, device);

      rs = statement.executeQuery();

      List<MessagePart> messageParts = new ArrayList<>();

      while (rs.next())
      {
        messageParts.add(getMessagePart(rs));
      }

      DAOUtil.close(rs);
      rs = null;

      /*
       * If we did not find message parts already locked for downloading then retrieve the message
       * parts that are "QueuedForDownload" for the device.
       */
      if (messageParts.size() == 0)
      {
        statement.setInt(1, MessagePart.Status.QUEUED_FOR_DOWNLOAD.getCode());
        statement.setString(2, device);

        rs = statement.executeQuery();

        while (rs.next())
        {
          messageParts.add(getMessagePart(rs));
        }

        DAOUtil.close(rs);
        rs = null;
      }

      for (MessagePart messagePart : messageParts)
      {
        Timestamp updated = new Timestamp(System.currentTimeMillis());

        messagePart.setStatus(MessagePart.Status.DOWNLOADING);
        messagePart.setLockName(lockName);
        messagePart.setUpdated(updated);
        messagePart.setDownloadAttempts(messagePart.getDownloadAttempts() + 1);

        updateStatement = connection.prepareStatement(lockMessagePartForDownloadSQL);
        updateStatement.setInt(1, MessagePart.Status.DOWNLOADING.getCode());
        updateStatement.setString(2, lockName);
        updateStatement.setTimestamp(3, updated);
        updateStatement.setString(4, messagePart.getId());

        if (updateStatement.executeUpdate() != 1)
        {
          throw new DAOException("Failed to lock the message part (" + messagePart.getId()
              + ") queued for download: No rows were affected as a result of executing the SQL"
              + " statement (" + lockMessagePartForDownloadSQL + ")");
        }

        DAOUtil.close(updateStatement);
        updateStatement = null;
      }

      transactionManager.commit();

      return messageParts;
    }
    catch (DAOException e)
    {
      try
      {
        transactionManager.rollback();
      }
      catch (Throwable f)
      {
        logger.error("Failed to rollback the transaction while retrieving"
            + " the message parts that have been queued for download by the device (" + device
            + ") from the database", f);
      }

      throw e;
    }
    catch (Throwable e)
    {
      try
      {
        transactionManager.rollback();
      }
      catch (Throwable f)
      {
        logger.error("Failed to rollback the transaction while retrieving"
            + " the message parts that have been queued for download by the device (" + device
            + ") from the database", f);
      }

      throw new DAOException("Failed to retrieve the message parts that have been queued for"
          + " download by the device (" + device + ") from the database", e);
    }
    finally
    {
      DAOUtil.close(updateStatement);
      DAOUtil.close(rs);
      DAOUtil.close(statement);
      DAOUtil.close(connection);

      try
      {
        if (existingTransaction != null)
        {
          transactionManager.resume(existingTransaction);
        }
      }
      catch (Throwable e)
      {
        logger.error("Failed to resume the original transaction while retrieving"
            + " the message parts that have been queued for download by the device (" + device
            + ") from the database", e);
      }
    }
  }

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
  @SuppressWarnings("resource")
  public List<Message> getMessagesQueuedForDownload(String device, String lockName)
    throws DAOException
  {
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet rs = null;
    PreparedStatement updateStatement = null;

    // Retrieve the Transaction Manager
    TransactionManager transactionManager = TransactionManager.getTransactionManager();
    javax.transaction.Transaction existingTransaction = null;

    try
    {
      if (transactionManager.isTransactionActive())
      {
        existingTransaction = transactionManager.beginNew();
      }
      else
      {
        transactionManager.begin();
      }

      connection = dataSource.getConnection();

      /*
       * First check if we already have messages locked for downloading for this device, if
       * so update the lock and return these messages. This handles the situation where a
       * device attempted to download messages previously and failed leaving these messages
       * locked in a "Downloading" state.
       */
      statement = connection.prepareStatement(getMessagesQueuedForDownloadSQL);
      statement.setInt(1, Message.Status.DOWNLOADING.getCode());
      statement.setString(2, device);

      rs = statement.executeQuery();

      List<Message> messages = new ArrayList<>();

      while (rs.next())
      {
        messages.add(getMessage(rs));
      }

      DAOUtil.close(rs);
      rs = null;

      /*
       * If we did not find messages already locked for downloading then retrieve the messages
       * that are "QueuedForDownload" for the device.
       */
      if (messages.size() == 0)
      {
        statement.setInt(1, Message.Status.QUEUED_FOR_DOWNLOAD.getCode());
        statement.setString(2, device);

        rs = statement.executeQuery();

        while (rs.next())
        {
          messages.add(getMessage(rs));
        }

        DAOUtil.close(rs);
        rs = null;
      }

      for (Message message : messages)
      {
        Timestamp updated = new Timestamp(System.currentTimeMillis());

        message.setStatus(Message.Status.DOWNLOADING);
        message.setLockName(lockName);
        message.setUpdated(updated);
        message.setDownloadAttempts(message.getDownloadAttempts() + 1);

        updateStatement = connection.prepareStatement(lockMessageForDownloadSQL);
        updateStatement.setInt(1, Message.Status.DOWNLOADING.getCode());
        updateStatement.setString(2, lockName);
        updateStatement.setTimestamp(3, updated);
        updateStatement.setString(4, message.getId());

        if (updateStatement.executeUpdate() != 1)
        {
          throw new DAOException("Failed to lock the message (" + message.getId() + ") queued for"
              + " download: No rows were affected as a result of executing the SQL"
              + " statement (" + lockMessageForDownloadSQL + ")");
        }

        DAOUtil.close(updateStatement);
        updateStatement = null;
      }

      transactionManager.commit();

      return messages;
    }
    catch (DAOException e)
    {
      try
      {
        transactionManager.rollback();
      }
      catch (Throwable f)
      {
        logger.error("Failed to rollback the transaction while retrieving"
            + " the messages that have been queued for download by the device (" + device
            + ") from the database", f);
      }

      throw e;
    }
    catch (Throwable e)
    {
      try
      {
        transactionManager.rollback();
      }
      catch (Throwable f)
      {
        logger.error("Failed to rollback the transaction while retrieving"
            + " the messages that have been queued for download by the device (" + device
            + ") from the database", f);
      }

      throw new DAOException("Failed to retrieve the messages that have been queued for download"
          + " by the device (" + device + ") from the database", e);
    }
    finally
    {
      DAOUtil.close(updateStatement);
      DAOUtil.close(rs);
      DAOUtil.close(statement);
      DAOUtil.close(connection);

      try
      {
        if (existingTransaction != null)
        {
          transactionManager.resume(existingTransaction);
        }
      }
      catch (Throwable e)
      {
        logger.error("Failed to resume the original transaction while retrieving"
            + " the messages that have been queued for download by the device (" + device
            + ") from the database", e);
      }
    }
  }

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
  @SuppressWarnings("resource")
  public List<Message> getMessagesQueuedForDownloadForUser(String user, String device,
      String lockName)
    throws DAOException
  {
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet rs = null;
    PreparedStatement updateStatement = null;

    // Retrieve the Transaction Manager
    TransactionManager transactionManager = TransactionManager.getTransactionManager();
    javax.transaction.Transaction existingTransaction = null;

    try
    {
      if (transactionManager.isTransactionActive())
      {
        existingTransaction = transactionManager.beginNew();
      }
      else
      {
        transactionManager.begin();
      }

      connection = dataSource.getConnection();

      /*
       * First check if we already have messages locked for downloading for the user-device
       * combination, if so update the lock and return these messages. This handles the situation
       * where a device attempted to download messages previously and failed leaving these messages
       * locked in a "Downloading" state.
       */
      statement = connection.prepareStatement(getMessagesQueuedForDownloadForUserSQL);
      statement.setInt(1, Message.Status.DOWNLOADING.getCode());
      statement.setString(2, user);
      statement.setString(3, device);

      rs = statement.executeQuery();

      List<Message> messages = new ArrayList<>();

      while (rs.next())
      {
        messages.add(getMessage(rs));
      }

      DAOUtil.close(rs);
      rs = null;

      /*
       * If we did not find messages already locked for downloading then retrieve the messages
       * that are "QueuedForDownload" for the user-device combination.
       */
      if (messages.size() == 0)
      {
        statement.setInt(1, Message.Status.QUEUED_FOR_DOWNLOAD.getCode());
        statement.setString(2, user);
        statement.setString(3, device);

        rs = statement.executeQuery();

        while (rs.next())
        {
          messages.add(getMessage(rs));
        }

        DAOUtil.close(rs);
        rs = null;
      }

      for (Message message : messages)
      {
        Timestamp updated = new Timestamp(System.currentTimeMillis());

        message.setStatus(Message.Status.DOWNLOADING);
        message.setLockName(lockName);
        message.setUpdated(updated);
        message.setDownloadAttempts(message.getDownloadAttempts() + 1);

        updateStatement = connection.prepareStatement(lockMessageForDownloadSQL);
        updateStatement.setInt(1, Message.Status.DOWNLOADING.getCode());
        updateStatement.setString(2, lockName);
        updateStatement.setTimestamp(3, updated);
        updateStatement.setString(4, message.getId());

        if (updateStatement.executeUpdate() != 1)
        {
          throw new DAOException("Failed to lock the message (" + message.getId() + ") queued for"
              + " download: No rows were affected as a result of executing the SQL"
              + " statement (" + lockMessageForDownloadSQL + ")");
        }

        DAOUtil.close(updateStatement);
        updateStatement = null;
      }

      transactionManager.commit();

      return messages;
    }
    catch (DAOException e)
    {
      try
      {
        transactionManager.rollback();
      }
      catch (Throwable f)
      {
        logger.error("Failed to rollback the transaction while retrieving"
            + " the messages for the user (" + user
            + ") that have been queued for download by the device (" + device
            + ") from the database", f);
      }

      throw e;
    }
    catch (Throwable e)
    {
      try
      {
        transactionManager.rollback();
      }
      catch (Throwable f)
      {
        logger.error("Failed to rollback the transaction while retrieving"
            + " the messages for the user (" + user
            + ") that have been queued for download by the device (" + device
            + ") from the database", f);
      }

      throw new DAOException("Failed to retrieve the messages for the user (" + user
          + ") that have been queued for download" + " by the device (" + device
          + ") from the database", e);
    }
    finally
    {
      DAOUtil.close(updateStatement);
      DAOUtil.close(rs);
      DAOUtil.close(statement);
      DAOUtil.close(connection);

      try
      {
        if (existingTransaction != null)
        {
          transactionManager.resume(existingTransaction);
        }
      }
      catch (Throwable e)
      {
        logger.error("Failed to resume the original transaction while retrieving"
            + " the messages for the user (" + user
            + ") that have been queued for download by the device (" + device
            + ") from the database", e);
      }
    }
  }

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
  public List<ErrorReportSummary> getMostRecentErrorReportSummaries(int maximumNumberOfEntries)
    throws DAOException
  {
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet rs = null;

    try
    {
      connection = dataSource.getConnection();

      statement = connection.prepareStatement(getMostRecentErrorReportSummariesSQL
          + " ORDER BY CREATED DESC FETCH FIRST " + maximumNumberOfEntries + " ROWS ONLY");

      rs = statement.executeQuery();

      List<ErrorReportSummary> errorReportSummaries = new ArrayList<>();

      while (rs.next())
      {
        errorReportSummaries.add(getErrorReportSummary(rs));
      }

      return errorReportSummaries;
    }
    catch (DAOException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new DAOException("Failed to retrieve the summaries for the most recent error reports"
          + " from the database", e);
    }
    finally
    {
      DAOUtil.close(rs);
      DAOUtil.close(statement);
      DAOUtil.close(connection);
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
   *
   * @throws DAOException
   */
  public Message getNextMessageQueuedForProcessing(int processingRetryDelay, String lockName)
    throws DAOException
  {
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet rs = null;
    PreparedStatement updateStatement = null;

    // Retrieve the Transaction Manager
    TransactionManager transactionManager = TransactionManager.getTransactionManager();
    javax.transaction.Transaction existingTransaction = null;

    try
    {
      if (transactionManager.isTransactionActive())
      {
        existingTransaction = transactionManager.beginNew();
      }
      else
      {
        transactionManager.begin();
      }

      connection = dataSource.getConnection();

      Timestamp processedBefore = new Timestamp(System.currentTimeMillis() - processingRetryDelay);

      statement = connection.prepareStatement(getNextMessageForProcessingSQL);
      statement.setInt(1, Message.Status.QUEUED_FOR_PROCESSING.getCode());
      statement.setTimestamp(2, processedBefore);

      rs = statement.executeQuery();

      if (rs.next())
      {
        Timestamp updated = new Timestamp(System.currentTimeMillis());

        Message message = getMessage(rs);

        message.setStatus(Message.Status.PROCESSING);
        message.setLockName(lockName);
        message.setUpdated(updated);

        updateStatement = connection.prepareStatement(lockMessageSQL);
        updateStatement.setInt(1, Message.Status.PROCESSING.getCode());
        updateStatement.setString(2, lockName);
        updateStatement.setTimestamp(3, updated);
        updateStatement.setString(4, message.getId());

        if (updateStatement.executeUpdate() != 1)
        {
          throw new DAOException("Failed to lock the message (" + message.getId()
              + ") for processing:"
              + " No rows were affected as a result of executing the SQL statement" + " ("
              + lockMessageSQL + ")");
        }

        transactionManager.commit();

        return message;
      }
      else
      {
        transactionManager.commit();

        return null;
      }
    }
    catch (DAOException e)
    {
      try
      {
        transactionManager.rollback();
      }
      catch (Throwable f)
      {
        logger.error("Failed to rollback the transaction while retrieving"
            + " the next message that has been queued for processing from the database", f);
      }

      throw e;
    }
    catch (Throwable e)
    {
      try
      {
        transactionManager.rollback();
      }
      catch (Throwable f)
      {
        logger.error("Failed to rollback the transaction while retrieving"
            + " the next message that has been queued for processing from the database", f);
      }

      throw new DAOException("Failed to retrieve the next message that has been queued for"
          + " processing from the database", e);
    }
    finally
    {
      DAOUtil.close(updateStatement);
      DAOUtil.close(rs);
      DAOUtil.close(statement);
      DAOUtil.close(connection);

      try
      {
        if (existingTransaction != null)
        {
          transactionManager.resume(existingTransaction);
        }
      }
      catch (Throwable e)
      {
        logger.error("Failed to resume the original transaction while retrieving"
            + " the next message that has been queued for processing from the database", e);
      }
    }
  }

  /**
   * Returns the total number of error reports in the database.
   *
   * @return the total number of error reports in the database
   *
   * @throws DAOException
   */
  public int getNumberOfErrorReports()
    throws DAOException
  {
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet rs = null;

    try
    {
      connection = dataSource.getConnection();

      statement = connection.prepareStatement(getNumberOfErrorReportsSQL);

      rs = statement.executeQuery();

      if (rs.next())
      {
        return rs.getInt(1);
      }
      else
      {
        return 0;
      }
    }
    catch (DAOException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new DAOException(
          "Failed to retrieve the total number of error reports in the database", e);
    }
    finally
    {
      DAOUtil.close(rs);
      DAOUtil.close(statement);
      DAOUtil.close(connection);
    }
  }

  /**
   * Increment the processing attempts for the message with the specified ID.
   *
   * @param message the message whose processing attempts should be incremented
   *
   * @throws DAOException
   */
  public void incrementMessageProcessingAttempts(Message message)
    throws DAOException
  {
    Connection connection = null;
    PreparedStatement statement = null;

    try
    {
      connection = dataSource.getConnection();

      Timestamp currentTime = new Timestamp(System.currentTimeMillis());

      statement = connection.prepareStatement(incrementMessageProcessingAttemptsSQL);
      statement.setTimestamp(1, currentTime);
      statement.setTimestamp(2, currentTime);
      statement.setString(3, message.getId());

      if (statement.executeUpdate() != 1)
      {
        throw new DAOException("Failed to increment the processing attempts for the message ("
            + message.getId()
            + ") in the database: No rows were affected as a result of executing the SQL statement"
            + " (" + incrementMessageProcessingAttemptsSQL + ")");
      }

      message.setProcessAttempts(message.getProcessAttempts() + 1);
      message.setLastProcessed(currentTime);
    }
    catch (DAOException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new DAOException("Failed to increment the processing attempts for the message ("
          + message.getId() + ") in the database", e);
    }
    finally
    {
      DAOUtil.close(statement);
      DAOUtil.close(connection);
    }
  }

  /**
   * Initialise the <code>DataAccessObject</code>.
   */
  @PostConstruct
  public void init()
  {
    try
    {
      dataSource = InitialContext.doLookup("java:app/jdbc/ApplicationDataSource");
    }
    catch (Throwable ignored) {}

    if (dataSource == null)
    {
      try
      {
        dataSource = InitialContext.doLookup("java:comp/env/jdbc/ApplicationDataSource");
      }
      catch (Throwable ignored) {}
    }

    if (dataSource == null)
    {
      throw new DAOException("Failed to retrieve the application data source"
        + " using the JNDI names (java:app/jdbc/ApplicationDataSource) and"
        + " (java:comp/env/jdbc/ApplicationDataSource)");
    }

    Connection connection = null;

    try
    {
      // Retrieve the database meta data
      String schemaSeparator = ".";

      try
      {
        connection = dataSource.getConnection();

        DatabaseMetaData metaData = connection.getMetaData();

        // Retrieve the schema separator for the database
        schemaSeparator = metaData.getCatalogSeparator();

        if ((schemaSeparator == null) || (schemaSeparator.length() == 0))
        {
          schemaSeparator = ".";
        }
      }
      finally
      {
        DAOUtil.close(connection);
      }

      // Determine the schema prefix
      String schemaPrefix = DataAccessObject.DEFAULT_APPLICATION_DATABASE_SCHEMA + schemaSeparator;

      // Build the SQL statements for the DAO
      buildStatements(schemaPrefix);
    }
    catch (Throwable e)
    {
      throw new DAOException("Failed to initialise the " + getClass().getName()
          + " data access object: " + e.getMessage(), e);
    }

    idGenerator = new IDGenerator(dataSource, DataAccessObject.DEFAULT_APPLICATION_DATABASE_SCHEMA);
  }

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
  public boolean isMessageArchived(String id)
    throws DAOException
  {
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet rs = null;

    try
    {
      connection = dataSource.getConnection();

      statement = connection.prepareStatement(isMessageArchivedSQL);
      statement.setString(1, id);

      rs = statement.executeQuery();

      return rs.next();
    }
    catch (DAOException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new DAOException("Failed to check whether the message (" + id
          + ") is archived in the database", e);
    }
    finally
    {
      DAOUtil.close(rs);
      DAOUtil.close(statement);
      DAOUtil.close(connection);
    }
  }

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
  public boolean isMessagePartQueuedForAssembly(String id)
    throws DAOException
  {
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet rs = null;

    try
    {
      connection = dataSource.getConnection();

      statement = connection.prepareStatement(isMessagePartQueuedForAssemblySQL);
      statement.setString(1, id);

      rs = statement.executeQuery();

      return rs.next();
    }
    catch (DAOException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new DAOException("Failed to check whether the message part (" + id
          + ") is queued for assembly in the database", e);
    }
    finally
    {
      DAOUtil.close(rs);
      DAOUtil.close(statement);
      DAOUtil.close(connection);
    }
  }

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
  public void logMessageAudit(String type, String user, String organisation, String device,
      String ip, boolean successful)
    throws DAOException
  {
    Connection connection = null;
    PreparedStatement statement = null;

    try
    {
      connection = dataSource.getConnection();

      statement = connection.prepareStatement(logMessageAuditSQL);

      statement.setLong(1, idGenerator.next("Application.MessageAuditId"));
      statement.setString(2, type);
      statement.setString(3, user);
      statement.setString(4, organisation);
      statement.setString(5, device);
      statement.setString(6, ip);
      statement.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
      statement.setString(8, successful
          ? "Y"
          : "N");

      if (statement.executeUpdate() != 1)
      {
        throw new DAOException("Failed to log the message audit to the database:"
            + " No rows were affected as a result of executing the SQL statement ("
            + logMessageAuditSQL + ")");
      }
    }
    catch (DAOException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new DAOException("Failed to log the message audit to the database", e);
    }
    finally
    {
      DAOUtil.close(statement);
      DAOUtil.close(connection);
    }
  }

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
  public int resetExpiredMessageLocks(int lockTimeout, Message.Status status,
      Message.Status newStatus)
    throws DAOException
  {
    Connection connection = null;
    PreparedStatement statement = null;

    try
    {
      connection = dataSource.getConnection();

      statement = connection.prepareStatement(resetExpiredMessageLocksSQL);
      statement.setInt(1, newStatus.getCode());
      statement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
      statement.setInt(3, status.getCode());
      statement.setTimestamp(4, new Timestamp(System.currentTimeMillis() - (lockTimeout * 1000L)));

      return statement.executeUpdate();
    }
    catch (DAOException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new DAOException("Failed to reset the expired locks for the messages with the status ("
          + status + ")", e);
    }
    finally
    {
      DAOUtil.close(statement);
      DAOUtil.close(connection);
    }
  }

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
  public int resetExpiredMessagePartLocks(int lockTimeout, MessagePart.Status status,
      MessagePart.Status newStatus)
    throws DAOException
  {
    Connection connection = null;
    PreparedStatement statement = null;

    try
    {
      connection = dataSource.getConnection();

      statement = connection.prepareStatement(resetExpiredMessagePartLocksSQL);
      statement.setInt(1, newStatus.getCode());
      statement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
      statement.setInt(3, status.getCode());
      statement.setTimestamp(4, new Timestamp(System.currentTimeMillis() - (lockTimeout * 1000L)));

      return statement.executeUpdate();
    }
    catch (DAOException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new DAOException("Failed to reset the expired locks for the message parts with the"
          + " status (" + status + ")", e);
    }
    finally
    {
      DAOUtil.close(statement);
      DAOUtil.close(connection);
    }
  }

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
  public int resetMessageLocks(String lockName, Message.Status status, Message.Status newStatus)
    throws DAOException
  {
    Connection connection = null;
    PreparedStatement statement = null;

    try
    {
      connection = dataSource.getConnection();

      statement = connection.prepareStatement(resetMessageLocksSQL);
      statement.setInt(1, newStatus.getCode());
      statement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
      statement.setString(3, lockName);
      statement.setInt(4, status.getCode());

      return statement.executeUpdate();
    }
    catch (DAOException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new DAOException("Failed to reset the locks for the messages with the status ("
          + status + ") that have been locked using the lock name (" + lockName + ")", e);
    }
    finally
    {
      DAOUtil.close(statement);
      DAOUtil.close(connection);
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
   *
   * @throws DAOException
   */
  public int resetMessagePartLocks(String lockName, MessagePart.Status status,
      MessagePart.Status newStatus)
    throws DAOException
  {
    Connection connection = null;
    PreparedStatement statement = null;

    try
    {
      connection = dataSource.getConnection();

      statement = connection.prepareStatement(resetMessagePartLocksSQL);
      statement.setInt(1, newStatus.getCode());
      statement.setString(2, lockName);
      statement.setInt(3, status.getCode());

      return statement.executeUpdate();
    }
    catch (DAOException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new DAOException("Failed to reset the locks for the message parts with the status ("
          + status + ") that have been locked using the lock name (" + lockName + ")", e);
    }
    finally
    {
      DAOUtil.close(statement);
      DAOUtil.close(connection);
    }
  }

  /**
   * Set the status for a message part.
   *
   * @param id     the ID uniquely identifying the message part
   * @param status the new status
   *
   * @throws DAOException
   */
  public void setMessagePartStatus(String id, MessagePart.Status status)
    throws DAOException
  {
    Connection connection = null;
    PreparedStatement statement = null;

    try
    {
      connection = dataSource.getConnection();

      statement = connection.prepareStatement(setMessagePartStatusSQL);
      statement.setInt(1, status.getCode());
      statement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
      statement.setString(3, id);

      if (statement.executeUpdate() != 1)
      {
        throw new DAOException("Failed to set the status for the message part (" + id + ") to ("
            + status.toString() + ") in the database: No rows were affected as a result of"
            + " executing the SQL statement (" + setMessagePartStatusSQL + ")");
      }
    }
    catch (DAOException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new DAOException("Failed to set the status for the message part (" + id + ") to ("
          + status.toString() + ") in the database", e);
    }
    finally
    {
      DAOUtil.close(statement);
      DAOUtil.close(connection);
    }
  }

  /**
   * Set the status for a message.
   *
   * @param id     the ID uniquely identifying the message
   * @param status the new status
   *
   * @throws DAOException
   */
  public void setMessageStatus(String id, Message.Status status)
    throws DAOException
  {
    Connection connection = null;
    PreparedStatement statement = null;

    try
    {
      connection = dataSource.getConnection();

      statement = connection.prepareStatement(setMessageStatusSQL);
      statement.setInt(1, status.getCode());
      statement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
      statement.setString(3, id);

      if (statement.executeUpdate() != 1)
      {
        throw new DAOException("Failed to set the status for the message (" + id + ") to ("
            + status.toString() + ") in the database: No rows were affected as a result of"
            + " executing the SQL statement (" + setMessageStatusSQL + ")");
      }
    }
    catch (DAOException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new DAOException("Failed to set the status for the message (" + id + ") to ("
          + status.toString() + ") in the database", e);
    }
    finally
    {
      DAOUtil.close(statement);
      DAOUtil.close(connection);
    }
  }

  /**
   * Unlock a locked message.
   *
   * @param id     the ID uniquely identifying the message
   * @param status the new status for the unlocked message
   *
   * @throws DAOException
   */
  public void unlockMessage(String id, Message.Status status)
    throws DAOException
  {
    Connection connection = null;
    PreparedStatement statement = null;

    try
    {
      connection = dataSource.getConnection();

      statement = connection.prepareStatement(unlockMessageSQL);
      statement.setInt(1, status.getCode());
      statement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
      statement.setString(3, id);

      if (statement.executeUpdate() != 1)
      {
        throw new DAOException("Failed to unlock and set the status for the message (" + id
            + ") to (" + status.toString() + ") in the database: No rows were affected as a result"
            + " of executing the SQL statement (" + unlockMessageSQL + ")");
      }
    }
    catch (DAOException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new DAOException("Failed to unlock and set the status for the message (" + id
          + ") to (" + status.toString() + ") in the database", e);
    }
    finally
    {
      DAOUtil.close(statement);
      DAOUtil.close(connection);
    }
  }

  /**
   * Unlock a locked message part.
   *
   * @param id     the ID uniquely identifying the message part
   * @param status the new status for the unlocked message part
   *
   * @throws DAOException
   */
  public void unlockMessagePart(String id, MessagePart.Status status)
    throws DAOException
  {
    Connection connection = null;
    PreparedStatement statement = null;

    try
    {
      connection = dataSource.getConnection();

      statement = connection.prepareStatement(unlockMessagePartSQL);
      statement.setInt(1, status.getCode());
      statement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
      statement.setString(3, id);

      if (statement.executeUpdate() != 1)
      {
        throw new DAOException("Failed to unlock and set the status for the message part (" + id
            + ") to (" + status.toString() + ") in the database: No rows were affected as a result"
            + " of executing the SQL statement (" + unlockMessagePartSQL + ")");
      }
    }
    catch (DAOException e)
    {
      throw e;
    }
    catch (Throwable e)
    {
      throw new DAOException("Failed to unlock and set the status for the message part (" + id
          + ") to (" + status.toString() + ") in the database", e);
    }
    finally
    {
      DAOUtil.close(statement);
      DAOUtil.close(connection);
    }
  }

  /**
   * This method must be implemented by all classes derived from <code>DataAccessObject</code> and
   * should contain the code to generate the SQL statements for the DAO.
   *
   * @param schemaPrefix the schema prefix to append to database objects reference by the DAO
   *
   * @throws SQLException if a database error occurs
   */
  protected void buildStatements(String schemaPrefix)
    throws SQLException
  {
    // allPartsQueuedForMessageSQL
    allPartsQueuedForMessageSQL = "SELECT COUNT(ID)" + " FROM " + schemaPrefix + "MESSAGE_PARTS"
        + " WHERE MSG_ID=?";

    // archiveMessageSQL
    archiveMessageSQL = "INSERT INTO " + schemaPrefix + "ARCHIVED_MESSAGES"
        + " (ID, USERNAME, ORGANISATION, DEVICE, MSG_TYPE, MSG_TYPE_VER, CORRELATION_ID,"
        + "  CREATED, ARCHIVED, DATA)" + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    // createErrorReportSQL
    createErrorReportSQL = "INSERT INTO " + schemaPrefix + "ERROR_REPORTS"
        + " (ID, APPLICATION_ID, APPLICATION_VERSION, DESCRIPTION, DETAIL, FEEDBACK, CREATED, WHO,"
        + " DEVICE, DATA)" + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    // createMessagePartSQL
    createMessagePartSQL = "INSERT INTO " + schemaPrefix + "MESSAGE_PARTS"
        + " (ID, PART_NO, TOTAL_PARTS, SEND_ATTEMPTS, DOWNLOAD_ATTEMPTS, STATUS, PERSISTED,"
        + " MSG_ID, MSG_USERNAME, MSG_ORGANISATION, MSG_DEVICE, MSG_TYPE,"
        + " MSG_TYPE_VER, MSG_CORRELATION_ID, MSG_PRIORITY, MSG_CREATED, MSG_DATA_HASH,"
        + " MSG_ENC_SCHEME, MSG_ENC_IV, MSG_CHECKSUM, DATA)"
        + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    // createMessageSQL
    createMessageSQL = "INSERT INTO " + schemaPrefix + "MESSAGES"
        + " (ID, USERNAME, ORGANISATION, DEVICE, MSG_TYPE, MSG_TYPE_VER, CORRELATION_ID,"
        + " PRIORITY, STATUS, CREATED, PERSISTED, UPDATED, SEND_ATTEMPTS,"
        + " PROCESS_ATTEMPTS, DOWNLOAD_ATTEMPTS, DATA)"
        + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    // deleteMessagePartsForMessageSQL
    deleteMessagePartsForMessageSQL = "DELETE FROM " + schemaPrefix
        + "MESSAGE_PARTS WHERE MSG_ID=?";

    // deleteMessagePartSQL
    deleteMessagePartSQL = "DELETE FROM " + schemaPrefix + "MESSAGE_PARTS WHERE ID=?";

    // deleteMessageSQL
    deleteMessageSQL = "DELETE FROM " + schemaPrefix + "MESSAGES WHERE ID=?";

    // getErrorReportByIdSQL
    getErrorReportByIdSQL = "SELECT ID, APPLICATION_ID, APPLICATION_VERSION, DESCRIPTION,"
        + " DETAIL, FEEDBACK, CREATED, WHO, DEVICE, DATA" + " FROM " + schemaPrefix
        + "ERROR_REPORTS WHERE ID=?";

    // getErrorReportSummaryByIdSQL
    getErrorReportSummaryByIdSQL =
      "SELECT ER.ID, ER.APPLICATION_ID, P.NAME, ER.APPLICATION_VERSION, ER.CREATED, ER.WHO,"
      + " ER.DEVICE FROM " + schemaPrefix + "ERROR_REPORTS ER LEFT OUTER JOIN " + schemaPrefix
      + "PACKAGES P ON ER.APPLICATION_ID = P.ID AND ER.ID=?";

    // getMessageByIdSQL
    getMessageByIdSQL = "SELECT ID, USERNAME, ORGANISATION, DEVICE, MSG_TYPE,"
        + " MSG_TYPE_VER, CORRELATION_ID, PRIORITY, STATUS, CREATED, PERSISTED, UPDATED,"
        + " SEND_ATTEMPTS, PROCESS_ATTEMPTS, DOWNLOAD_ATTEMPTS, LOCK_NAME,"
        + " LAST_PROCESSED, DATA" + " FROM " + schemaPrefix + "MESSAGES WHERE ID=?";

    // getMessagePartsQueuedForAssemblySQL
    getMessagePartsQueuedForAssemblySQL =
      "SELECT ID, PART_NO, TOTAL_PARTS, SEND_ATTEMPTS, DOWNLOAD_ATTEMPTS, STATUS, PERSISTED,"
      + " UPDATED, MSG_ID, MSG_USERNAME, MSG_ORGANISATION, MSG_DEVICE,"
      + " MSG_TYPE, MSG_TYPE_VER, MSG_CORRELATION_ID, MSG_PRIORITY, MSG_CREATED, MSG_DATA_HASH,"
      + " MSG_ENC_SCHEME, MSG_ENC_IV, MSG_CHECKSUM, LOCK_NAME, DATA" + " FROM " + schemaPrefix
      + "MESSAGE_PARTS" + " WHERE STATUS=? AND MSG_ID=? ORDER BY PART_NO FOR UPDATE";

    // getMessagePartsQueuedForDownloadSQL
    getMessagePartsQueuedForDownloadSQL =
      "SELECT ID, PART_NO, TOTAL_PARTS, SEND_ATTEMPTS, DOWNLOAD_ATTEMPTS, STATUS, PERSISTED,"
      + " UPDATED, MSG_ID, MSG_USERNAME, MSG_ORGANISATION, MSG_DEVICE,"
      + " MSG_TYPE, MSG_TYPE_VER, MSG_CORRELATION_ID, MSG_PRIORITY, MSG_CREATED, MSG_DATA_HASH,"
      + " MSG_ENC_SCHEME, MSG_ENC_IV, MSG_CHECKSUM, LOCK_NAME, DATA" + " FROM " + schemaPrefix
      + "MESSAGE_PARTS"
      + " WHERE STATUS=? AND MSG_DEVICE=? ORDER BY PART_NO FETCH FIRST 3 ROWS ONLY FOR UPDATE";

    // getMessagesQueuedForDownloadForUserSQL
    getMessagesQueuedForDownloadForUserSQL =
      "SELECT ID, USERNAME, ORGANISATION, DEVICE, MSG_TYPE, MSG_TYPE_VER, CORRELATION_ID,"
      + " PRIORITY, STATUS, CREATED, PERSISTED, UPDATED, SEND_ATTEMPTS,"
      + " PROCESS_ATTEMPTS, DOWNLOAD_ATTEMPTS, LOCK_NAME, LAST_PROCESSED," + " DATA" + " FROM "
      + schemaPrefix + "MESSAGES" + " WHERE STATUS=? AND USERNAME=? AND DEVICE=? ORDER BY CREATED"
      + " FETCH FIRST 3 ROWS ONLY FOR UPDATE";

    // getMessagesQueuedForDownloadSQL
    getMessagesQueuedForDownloadSQL =
      "SELECT ID, USERNAME, ORGANISATION, DEVICE, MSG_TYPE, MSG_TYPE_VER, CORRELATION_ID,"
      + " PRIORITY, STATUS, CREATED, PERSISTED, UPDATED, SEND_ATTEMPTS,"
      + " PROCESS_ATTEMPTS, DOWNLOAD_ATTEMPTS, LOCK_NAME, LAST_PROCESSED," + " DATA" + " FROM "
      + schemaPrefix + "MESSAGES" + " WHERE STATUS=? AND DEVICE=? ORDER BY CREATED"
      + " FETCH FIRST 3 ROWS ONLY FOR UPDATE";

    // getMostRecentErrorReportSummariesSQL
    getMostRecentErrorReportSummariesSQL =
      "SELECT ER.ID, ER.APPLICATION_ID, P.NAME, ER.APPLICATION_VERSION, ER.CREATED, ER.WHO,"
      + " ER.DEVICE FROM " + schemaPrefix + "ERROR_REPORTS ER LEFT OUTER JOIN " + schemaPrefix
      + "PACKAGES P ON ER.APPLICATION_ID = P.ID";

    // getNextMessageForProcessingSQL
    getNextMessageForProcessingSQL =
      "SELECT ID, USERNAME, ORGANISATION, DEVICE, MSG_TYPE, MSG_TYPE_VER, CORRELATION_ID,"
      + " PRIORITY, STATUS, CREATED, PERSISTED, UPDATED, SEND_ATTEMPTS,"
      + " PROCESS_ATTEMPTS, DOWNLOAD_ATTEMPTS, LOCK_NAME, LAST_PROCESSED," + " DATA" + " FROM "
      + schemaPrefix + "MESSAGES"
      + " WHERE STATUS=? AND (LAST_PROCESSED<? OR LAST_PROCESSED IS NULL)"
      + " ORDER BY UPDATED FETCH FIRST 1 ROWS ONLY FOR UPDATE";

    // getNumberOfErrorReportsSQL
    getNumberOfErrorReportsSQL = "SELECT COUNT(ID)" + " FROM " + schemaPrefix + "ERROR_REPORTS";

    // incrementMessageProcessingAttemptsSQL
    incrementMessageProcessingAttemptsSQL = "UPDATE " + schemaPrefix + "MESSAGES"
        + " SET PROCESS_ATTEMPTS=PROCESS_ATTEMPTS + 1, UPDATED=?, LAST_PROCESSED=?" + " WHERE ID=?";

    // isMessageArchivedSQL
    isMessageArchivedSQL = "SELECT ID" + " FROM " + schemaPrefix + "ARCHIVED_MESSAGES"
        + " WHERE ID=?";

    // isMessagePartQueuedForAssemblySQL
    isMessagePartQueuedForAssemblySQL = "SELECT ID" + " FROM " + schemaPrefix + "MESSAGE_PARTS"
        + " WHERE ID=?";

    // lockMessageForDownloadSQL
    lockMessageForDownloadSQL = "UPDATE " + schemaPrefix + "MESSAGES"
        + " SET STATUS=?, LOCK_NAME=?, UPDATED=?,"
        + " DOWNLOAD_ATTEMPTS=DOWNLOAD_ATTEMPTS+1 WHERE ID=?";

    // lockMessagePartForDownloadSQL
    lockMessagePartForDownloadSQL = "UPDATE " + schemaPrefix + "MESSAGE_PARTS"
        + " SET STATUS=?, LOCK_NAME=?, UPDATED=?,"
        + " DOWNLOAD_ATTEMPTS=DOWNLOAD_ATTEMPTS+1 WHERE ID=?";

    // lockMessageSQL
    lockMessageSQL = "UPDATE " + schemaPrefix + "MESSAGES"
        + " SET STATUS=?, LOCK_NAME=?, UPDATED=? WHERE ID=?";

    // logMessageAuditSQL
    logMessageAuditSQL = "INSERT INTO " + schemaPrefix + "MESSAGE_AUDIT_LOG"
        + " (ID, MSG_TYPE, USERNAME, ORGANISATION, DEVICE, IP, LOGGED,"
        + "  SUCCESSFUL) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    // lockMessagePartSQL
    lockMessagePartSQL = "UPDATE " + schemaPrefix + "MESSAGE_PARTS"
        + " SET STATUS=?, LOCK_NAME=?, UPDATED=? WHERE ID=?";

    // resetExpiredMessageLocksSQL
    resetExpiredMessageLocksSQL = "UPDATE " + schemaPrefix + "MESSAGES"
        + " SET STATUS=?, LOCK_NAME=NULL, UPDATED=?"
        + " WHERE LOCK_NAME IS NOT NULL AND STATUS=? AND UPDATED < ?";

    // resetExpiredMessagePartLocksSQL
    resetExpiredMessagePartLocksSQL = "UPDATE " + schemaPrefix + "MESSAGE_PARTS"
        + " SET STATUS=?, LOCK_NAME=NULL, UPDATED=?"
        + " WHERE LOCK_NAME IS NOT NULL AND STATUS=? AND UPDATED < ?";

    // resetMessageLocksSQL
    resetMessageLocksSQL = "UPDATE " + schemaPrefix + "MESSAGES"
        + " SET STATUS=?, LOCK_NAME=NULL, UPDATED=?" + " WHERE LOCK_NAME=? AND STATUS=?";

    // resetMessagePartLocksSQL
    resetMessagePartLocksSQL = "UPDATE " + schemaPrefix + "MESSAGE_PARTS"
        + " SET STATUS=?, LOCK_NAME=NULL" + " WHERE LOCK_NAME=? AND STATUS=?";

    // setMessagePartStatusSQL
    setMessagePartStatusSQL = "UPDATE " + schemaPrefix + "MESSAGE_PARTS"
        + " SET STATUS=?, UPDATED=? WHERE ID=?";

    // setMessageStatusSQL
    setMessageStatusSQL = "UPDATE " + schemaPrefix + "MESSAGES"
        + " SET STATUS=?, UPDATED=? WHERE ID=?";

    // unlockMessagePartSQL
    unlockMessagePartSQL = "UPDATE " + schemaPrefix + "MESSAGE_PARTS"
        + " SET STATUS=?, UPDATED=?, LOCK_NAME=NULL WHERE ID=?";

    // unlockMessageSQL
    unlockMessageSQL = "UPDATE " + schemaPrefix + "MESSAGES"
        + " SET STATUS=?, UPDATED=?, LOCK_NAME=NULL WHERE ID=?";
  }

  private ErrorReport getErrorReport(ResultSet rs)
    throws SQLException
  {
    byte[] data = rs.getBytes(10);

    return new ErrorReport(rs.getString(1), rs.getString(2), rs.getInt(3), rs.getString(4),
        rs.getString(5), rs.getString(6), rs.getTimestamp(7), rs.getString(8), rs.getString(9),
        (data == null)
        ? new byte[0]
        : data);
  }

  private ErrorReportSummary getErrorReportSummary(ResultSet rs)
    throws SQLException
  {
    String applicationName = rs.getString(3);

    if (applicationName == null)
    {
      applicationName = "Unknown";
    }

    return new ErrorReportSummary(rs.getString(1), rs.getString(2), applicationName, rs.getInt(4),
        rs.getTimestamp(5), rs.getString(6), rs.getString(7));
  }

  private Message getMessage(ResultSet rs)
    throws SQLException
  {
    return new Message(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
        rs.getString(5), rs.getInt(6), rs.getString(7), Priority.fromCode(rs.getInt(8)),
        Status.fromCode(rs.getInt(9)), rs.getTimestamp(10), rs.getTimestamp(11),
        rs.getTimestamp(12), rs.getInt(13), rs.getInt(14), rs.getInt(15), rs.getString(16),
        rs.getTimestamp(17), rs.getBytes(18), "", EncryptionScheme.NONE, "");
  }

  private MessagePart getMessagePart(ResultSet rs)
    throws SQLException
  {
    return new MessagePart(rs.getString(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getInt(5),
        MessagePart.Status.fromCode(rs.getInt(6)), rs.getTimestamp(7), rs.getTimestamp(8),
        rs.getString(9), rs.getString(10), rs.getString(11), rs.getString(12), rs.getString(13),
        rs.getInt(14), rs.getString(15), Priority.fromCode(rs.getInt(16)), rs.getTimestamp(17),
        rs.getString(18), EncryptionScheme.fromCode(rs.getInt(19)), rs.getString(20),
        rs.getString(21), rs.getString(22), rs.getBytes(23));
  }
}

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

import guru.mmp.application.Debug;
import guru.mmp.application.cdi.CDIUtil;
import guru.mmp.application.messaging.MessagePart.Status;
import guru.mmp.application.messaging.handler.IMessageHandler;
import guru.mmp.application.messaging.handler.MessageHandlerConfig;
import guru.mmp.application.registry.IRegistry;
import guru.mmp.common.crypto.CryptoUtils;
import guru.mmp.common.crypto.EncryptionScheme;
import guru.mmp.common.util.Base64;
import guru.mmp.common.util.StringUtil;
import guru.mmp.common.xml.DtdJarResolver;
import guru.mmp.common.xml.XmlParserErrorHandler;
import guru.mmp.common.xml.XmlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import javax.annotation.PostConstruct;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.naming.InitialContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.security.MessageDigest;
import java.util.*;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>MessagingService</code> class provides the implementation of the Messaging Service
 * for the messaging infrastructure.
 *
 * @author Marcus Portmann
 */
@ApplicationScoped
@Default
public class MessagingService
  implements IMessagingService
{
  /**
   * The path to the messaging configuration files (META-INF/MessagingConfig.xml) on the
   * classpath.
   */
  public static final String MESSAGING_CONFIGURATION_PATH = "META-INF/MessagingConfig.xml";

  /**
   * The AES encryption IV used when generating user-device AES encryption keys.
   */
  private static final byte[] AES_USER_DEVICE_ENCRYPTION_KEY_GENERATION_ENCRYPTION_IV =
    Base64.decode("QSaz5pMnMbar66FsNdI/ZQ==");

  /**
   * The AES encryption key used to encrypt sensitive configuration information stored in
   * the registry.
   */
  private static final byte[] REGISTRY_ENCRYPTION_KEY =
    Base64.decode("Ev5UOwzqSEoSsqbyCVn6q9LZHhhkbXZndDgyOGQyMjY=");

  /**
   * The AES encryption IV.
   */
  private static final byte[] REGISTRY_ENCRYPTION_IV = Base64.decode("xh2wcoShURa4c6w7HbOngQ==");

  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(MessagingService.class);

  /* The AES encryption master key used to derive the device/user specific encryption keys. */
  private byte[] aesEncryptionMasterKey;

  /* Background Message Processor */
  @Inject
  private BackgroundMessageProcessor backgroundMessageProcessor;

  /* The name of the messaging service instance. */
  private String instanceName;

  /* The maximum number of times processing will be attempted for a message. */
  private int maximumProcessingAttempts;

  /* The message handlers */
  private Map<String, IMessageHandler> messageHandlers;

  /**
   * The configuration information for the message handlers read from the messaging configuration
   * files (META-INF/MessagingConfig.xml) on the classpath.
   */
  private List<MessageHandlerConfig> messageHandlersConfig;

  /* The DAO providing persistence capabilities for the messaging infrastructure. */
  @Inject
  private IMessagingDAO messagingDAO;

  /* The delay in milliseconds to wait before re-attempting to process a message. */
  private int processingRetryDelay;

  /* Registry */
  @Inject
  private IRegistry registry;

  /**
   * Constructs a new <code>MessagingService</code>.
   */
  public MessagingService() {}

  /**
   * Archive the message.
   *
   * @param message the message to archive
   *
   * @throws MessagingException
   */
  public void archiveMessage(Message message)
    throws MessagingException
  {
    try
    {
      if (isArchivableMessage(message))
      {
        messagingDAO.archiveMessage(message);
      }
    }
    catch (Throwable e)
    {
      throw new MessagingException("Failed to archive the message (" + message.getId() + ")", e);
    }
  }

  /**
   * Returns <code>true</code> if the messaging service is capable of processing the specified
   * message or <code>false</code> otherwise.
   *
   * @param message the message to process
   *
   * @return <code>true</code> if the messaging service is capable of processing the specified
   *         message or <code>false</code> otherwise
   *
   * @throws MessagingException
   */
  public boolean canProcessMessage(Message message)
    throws MessagingException
  {
    String messageTypeKey = getMessageTypeKey(message.getType(), message.getTypeVersion());

    return messageHandlers.containsKey(messageTypeKey);
  }

  /**
   * Returns <code>true</code> if the messaging service is capable of queueing the specified
   * message part for assembly or <code>false</code> otherwise.
   *
   * @param messagePart the message part to queue for assembly
   *
   * @return <code>true</code> if the messaging service is capable of queueing the specified
   *         message part for assembly or <code>false</code> otherwise
   *
   * @throws MessagingException
   */
  public boolean canQueueMessagePartForAssembly(MessagePart messagePart)
    throws MessagingException
  {
    String messageTypeKey = getMessageTypeKey(messagePart.getMessageType(),
      messagePart.getMessageTypeVersion());

    return messageHandlers.containsKey(messageTypeKey);
  }

  /**
   * Create the entry for the error report in the database.
   *
   * @param errorReport the <code>ErrorReport</code> instance containing the information for the
   *                    error report
   *
   * @throws MessagingException
   */
  public void createErrorReport(ErrorReport errorReport)
    throws MessagingException
  {
    try
    {
      messagingDAO.createErrorReport(errorReport);
    }
    catch (Throwable e)
    {
      throw new MessagingException("Failed to create the error report (" + errorReport.getId()
          + ")", e);
    }
  }

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
  public boolean decryptMessage(Message message)
    throws MessagingException
  {
    // If the message is already decrypted then stop here
    if (!message.isEncrypted())
    {
      return true;
    }

    // TODO: Cache this key...
    byte[] userEncryptionKey = deriveUserDeviceEncryptionKey(message.getEncryptionScheme(),
      message.getUser(), message.getOrganisation(), message.getDevice());

    /*
     * if (logger.isDebugEnabled())
     * {
     * logger.debug("Attempting to decrypt the data for the message (" + message.getId()
     *     + ") using the user's encryption key (" + Base64.encodeBytes(userEncryptionKey) + ")");
     * }
     */

    // Decrypt the message
    try
    {
      // Decrypt the message data
      byte[] decryptedData = MessageTranslator.decryptMessageData(message.getEncryptionScheme(),
        userEncryptionKey, StringUtil.isNullOrEmpty(message.getEncryptionIV())
          ? new byte[0]
          : Base64.decode(message.getEncryptionIV()), message.getData());

      // Verify the data hash for the unencrypted data
      MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");

      messageDigest.update(decryptedData);

      String messageChecksum = Base64.encodeBytes(messageDigest.digest());

      if (!messageChecksum.equals(message.getDataHash()))
      {
        logger.warn("Data hash verification failed for the message (" + message.getId()
            + ") from the user (" + message.getUser() + ") and device (" + message.getDevice()
            + "). " + message.getData().length + " (" + decryptedData.length
            + ") bytes of message data was encrypted using the encryption scheme ("
            + message.getEncryptionScheme() + ") and encryption IV (" + message.getEncryptionIV()
            + "). Expected data hash (" + message.getDataHash() + ") but got (" + messageChecksum
            + ")");

        return false;
      }
      else
      {
        message.setData(decryptedData);
        message.setDataHash("");
        message.setEncryptionScheme(EncryptionScheme.NONE);
        message.setEncryptionIV("");

        return true;
      }
    }
    catch (Throwable e)
    {
      throw new MessagingException("Failed to decrypt the data for the message (" + message.getId()
          + ") from the user (" + message.getUser() + ") and device (" + message.getDevice()
          + ")", e);
    }
  }

  /**
   * Delete the message.
   *
   * @param message the message to delete
   *
   * @throws MessagingException
   */
  public void deleteMessage(Message message)
    throws MessagingException
  {
    try
    {
      messagingDAO.deleteMessage(message.getId());
    }
    catch (Throwable e)
    {
      throw new MessagingException("Failed to delete the message (" + message.getId() + ")", e);
    }
  }

  /**
   * Delete the message.
   *
   * @param id the ID uniquely identifying the message to delete
   *
   * @throws MessagingException
   */
  public void deleteMessage(String id)
    throws MessagingException
  {
    try
    {
      messagingDAO.deleteMessage(id);
    }
    catch (Throwable e)
    {
      throw new MessagingException("Failed to delete the message (" + id + ")", e);
    }
  }

  /**
   * Delete the message part.
   *
   * @param id the ID uniquely identifying the message part to delete
   *
   * @throws MessagingException
   */
  public void deleteMessagePart(String id)
    throws MessagingException
  {
    try
    {
      messagingDAO.deleteMessagePart(id);
    }
    catch (Throwable e)
    {
      throw new MessagingException("Failed to delete the message part (" + id + ")", e);
    }
  }

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
  public byte[] deriveUserDeviceEncryptionKey(EncryptionScheme encryptionScheme, String username,
      String organisation, String deviceId)
    throws MessagingException
  {
    try
    {
      String password = deviceId.toLowerCase() + username.toLowerCase()
        + organisation.toLowerCase();

      if (encryptionScheme == EncryptionScheme.AES_CFB)
      {
        byte[] key = CryptoUtils.passwordToAESKey(password);

        final SecretKey secretKey = new SecretKeySpec(aesEncryptionMasterKey,
          CryptoUtils.AES_KEY_SPEC);
        final IvParameterSpec iv =
          new IvParameterSpec(AES_USER_DEVICE_ENCRYPTION_KEY_GENERATION_ENCRYPTION_IV);
        final Cipher cipher = Cipher.getInstance("AES/CFB/NoPadding");

        cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);

        return cipher.doFinal(key);
      }
      else
      {
        throw new MessagingException("Unsupported encryption scheme (" + encryptionScheme + ")");
      }
    }
    catch (Throwable e)
    {
      throw new MessagingException("Failed to derive the encryption key for the user (" + username
          + "), organisation (" + organisation + ") and device (" + deviceId + ")", e);
    }
  }

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
  public boolean encryptMessage(EncryptionScheme encryptionScheme, Message message)
    throws MessagingException
  {
    // If the message is already encrypted then stop here
    if (message.isEncrypted())
    {
      return true;
    }

    // TODO: Cache this key...
    byte[] userEncryptionKey = deriveUserDeviceEncryptionKey(encryptionScheme, message.getUser(),
      message.getOrganisation(), message.getDevice());

    /*
     * if (logger.isDebugEnabled())
     * {
     * logger.debug("Attempting to encrypt the data for the message (" + message.getId()
     *     + ") using the user's encryption key (" + Base64.encodeBytes(userEncryptionKey) + ")");
     * }
     */

    // Encrypt the message
    try
    {
      byte[] encryptionIV = new byte[0];

      if (encryptionScheme == EncryptionScheme.AES_CFB)
      {
        encryptionIV = CryptoUtils.createRandomEncryptionIV(CryptoUtils.AES_BLOCK_SIZE);
      }

      // Encrypt the message data
      byte[] encryptedData = MessageTranslator.encryptMessageData(encryptionScheme,
        userEncryptionKey, encryptionIV, message.getData());

      // Generate the hash for the unencrypted data
      MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");

      messageDigest.update(message.getData());

      String messageChecksum = Base64.encodeBytes(messageDigest.digest());

      message.setDataHash(messageChecksum);
      message.setData(encryptedData);
      message.setEncryptionScheme(encryptionScheme);
      message.setEncryptionIV((encryptionIV.length == 0)
          ? ""
          : Base64.encodeBytes(encryptionIV));

      return true;
    }
    catch (Throwable e)
    {
      throw new MessagingException("Failed to encrypt the data for the message (" + message.getId()
          + ") from the user (" + message.getUser() + ") and device (" + message.getDevice()
          + ")", e);
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
   * @throws MessagingException
   */
  public ErrorReport getErrorReport(String id)
    throws MessagingException
  {
    try
    {
      return messagingDAO.getErrorReport(id);
    }
    catch (Throwable e)
    {
      throw new MessagingException("Failed to retrieve the error report (" + id + ")", e);
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
   * @throws MessagingException
   */
  public ErrorReportSummary getErrorReportSummary(String id)
    throws MessagingException
  {
    try
    {
      return messagingDAO.getErrorReportSummary(id);
    }
    catch (Throwable e)
    {
      throw new MessagingException("Failed to retrieve the summary for the error report (" + id
          + ")", e);
    }
  }

  /**
   * Returns the maximum number of times processing will be attempted for a message.
   *
   * @return the maximum number of times processing will be attempted for a message
   */
  public int getMaximumProcessingAttempts()
  {
    return maximumProcessingAttempts;
  }

  /**
   * Retrieve the message with the specified ID.
   *
   * @param id the ID uniquely identifying the message
   *
   * @return the message with the specified ID or <code>null</code> if the message could not
   *         be found
   *
   * @throws MessagingException
   */
  public Message getMessage(String id)
    throws MessagingException
  {
    try
    {
      return messagingDAO.getMessage(id);
    }
    catch (Throwable e)
    {
      throw new MessagingException("Failed to retrieve the message (" + id + ")", e);
    }
  }

  /**
   * Get the message parts that have been queued for download by a particular remote device.
   *
   * @param device the device ID identifying the device downloading the message parts
   *
   * @return the message parts that have been queued for download by a particular remote device
   *
   * @throws MessagingException
   */
  public List<MessagePart> getMessagePartsQueuedForDownload(String device)
    throws MessagingException
  {
    try
    {
      return messagingDAO.getMessagePartsQueuedForDownload(device, getInstanceName());
    }
    catch (Throwable e)
    {
      throw new MessagingException("Failed to retrieve the message parts that have been queued for"
          + " download by the device (" + device + ")", e);
    }
  }

  /**
   * Get the messages that have been queued for download by a particular remote device.
   *
   * @param device the device ID identifying the device downloading the messages
   *
   * @return the messages that have been queued for download by a particular remote device
   *
   * @throws MessagingException
   */
  public List<Message> getMessagesQueuedForDownload(String device)
    throws MessagingException
  {
    try
    {
      return messagingDAO.getMessagesQueuedForDownload(device, getInstanceName());
    }
    catch (Throwable e)
    {
      throw new MessagingException("Failed to retrieve the messages that have been queued for"
          + " download by the device (" + device + ")", e);
    }
  }

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
  public List<Message> getMessagesQueuedForDownloadForUser(String user, String device)
    throws MessagingException
  {
    try
    {
      return messagingDAO.getMessagesQueuedForDownloadForUser(user, device, getInstanceName());
    }
    catch (Throwable e)
    {
      throw new MessagingException("Failed to retrieve the messages for the user (" + user
          + ") that have been queued for download by the device (" + device + ")", e);
    }
  }

  /**
   * Returns the path to the messaging debug directory if it exists or <code>null</code> otherwise.
   *
   * @return the path to the messaging debug directory if it exists of <code>null</code> otherwise
   */
  public String getMessagingDebugDirectory()
  {
    String applicationServerRootDirectory = Debug.getApplicationServerRootDirectory();

    if (!StringUtil.isNullOrEmpty(applicationServerRootDirectory))
    {
      String messagingDebugDirectory = applicationServerRootDirectory + File.separator
        + "messagingDebug";

      try
      {
        File tmpFile = new File(messagingDebugDirectory);

        if (tmpFile.exists())
        {
          return messagingDebugDirectory;
        }
      }
      catch (Throwable e)
      {
        logger.warn("Failed to check if the messaging debug directory exists ("
            + messagingDebugDirectory + "): " + e.getMessage());
      }
    }

    return null;
  }

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
  public List<ErrorReportSummary> getMostRecentErrorReportSummaries(int maximumNumberOfEntries)
    throws MessagingException
  {
    try
    {
      return messagingDAO.getMostRecentErrorReportSummaries(maximumNumberOfEntries);
    }
    catch (Throwable e)
    {
      throw new MessagingException(
          "Failed to retrieve the summaries for the most recent error reports", e);
    }
  }

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
  public Message getNextMessageQueuedForProcessing()
    throws MessagingException
  {
    try
    {
      return messagingDAO.getNextMessageQueuedForProcessing(processingRetryDelay,
          getInstanceName());
    }
    catch (Throwable e)
    {
      throw new MessagingException("Failed to retrieve the next message queued for processing", e);
    }
  }

  /**
   * Returns the total number of error reports.
   *
   * @return the total number of error reports
   *
   * @throws MessagingException
   */
  public int getNumberOfErrorReports()
    throws MessagingException
  {
    try
    {
      return messagingDAO.getNumberOfErrorReports();
    }
    catch (Throwable e)
    {
      throw new MessagingException("Failed to retrieve the total number of error reports", e);
    }
  }

  /**
   * Increment the processing attempts for the message.
   *
   * @param message the message whose processing attempts should be incremented
   *
   * @throws MessagingException
   */
  public void incrementMessageProcessingAttempts(Message message)
    throws MessagingException
  {
    try
    {
      messagingDAO.incrementMessageProcessingAttempts(message);
    }
    catch (Throwable e)
    {
      throw new MessagingException("Failed to increment the processing attempts for the message ("
          + message.getId() + ")", e);
    }
  }

  /**
   * Initialise the MessagingService instance.
   */
  @PostConstruct
  public void init()
  {
    logger.info("Initialising the MessagingService instance (" + getInstanceName() + ")");

    messageHandlers = new HashMap<>();

    try
    {
      // Initialise the configuration for the MessagingService instance
      initConfiguration();

      // Read the messaging configuration
      readMessagingConfig();

      // Initialise the message handlers
      initMessageHandlers();
    }
    catch (Throwable e)
    {
      throw new MessagingException("Failed to initialise the MessagingService instance", e);
    }
  }

  /**
   * Should the specified message be archived?
   *
   * @param message the message
   *
   * @return <code>true</code> if a message with the specified type information should be archived
   *         or <code>false</code> otherwise
   */
  public boolean isArchivableMessage(Message message)
  {
    return isArchivableMessage(message.getType(), message.getTypeVersion());
  }

  /**
   * Can the specified message be processed asynchronously?
   *
   * @param message the message
   *
   * @return <code>true</code> if the message can be processed asynchronously or
   *         <code>false</code> otherwise
   */
  public boolean isAsynchronousMessage(Message message)
  {
    return isAsynchronousMessage(message.getType(), message.getTypeVersion());
  }

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
  public boolean isMessageArchived(Message message)
    throws MessagingException
  {
    try
    {
      return messagingDAO.isMessageArchived(message.getId());
    }
    catch (Throwable e)
    {
      throw new MessagingException("Failed to check whether the message (" + message.getId()
          + ") has been archived", e);
    }
  }

  /**
   * Should the specified message be processed synchronously?
   *
   * @param message the message
   *
   * @return <code>true</code> if the message should be processed synchronously or
   *         <code>false</code> otherwise
   */
  public boolean isSynchronousMessage(Message message)
  {
    return isSynchronousMessage(message.getType(), message.getTypeVersion());
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
   *                     processed?
   *
   * @return <code>true</code> if the message audit entry was logged successfully or
   *         <code>false</code> otherwise
   *
   * @throws MessagingException
   */
  public boolean logMessageAudit(String type, String user, String organisation, String device,
      String ip, boolean successful)
    throws MessagingException
  {
    try
    {
      messagingDAO.logMessageAudit(type, user, organisation, device, ip, successful);

      return true;
    }
    catch (Throwable e)
    {
      logger.error("Failed to log the message audit for the message with type (" + type
          + ") from the user (" + user + ") and device (" + device + ")", e);

      return false;
    }
  }

  /**
   * Process the message.
   *
   * @param message the message to process
   *
   * @return the response message or <code>null</code> if no response message exists
   *
   * @throws MessagingException
   */
  public Message processMessage(Message message)
    throws MessagingException
  {
    if (logger.isDebugEnabled())
    {
      logger.debug("Processing message (" + message.getId() + ") with type (" + message.getType()
          + ") and version (" + message.getTypeVersion() + ")");
    }

    String messageTypeKey = getMessageTypeKey(message.getType(), message.getTypeVersion());

    if (!messageHandlers.containsKey(messageTypeKey))
    {
      throw new MessagingException("No message handler registered to process messages with type ("
          + message.getType() + ") and version (" + message.getTypeVersion() + ")");
    }

    IMessageHandler messageHandler = messageHandlers.get(messageTypeKey);

    return messageHandler.processMessage(message);
  }

  /**
   * Queue the specified message for download by a remote device.
   *
   * @param message the message to queue
   *
   * @throws MessagingException
   */
  public void queueMessageForDownload(Message message)
    throws MessagingException
  {
    // Update the status of the message to indicate that it is queued for sending
    message.setStatus(Message.Status.QUEUED_FOR_DOWNLOAD);

    try
    {
      if (message.getData().length <= Message.MAX_ASYNC_MESSAGE_SIZE)
      {
        messagingDAO.createMessage(message);
      }
      else
      {
        /*
         * NOTE: The message parts are not encrypted. Since asynchronous messages should ALWAYS be
         *       encrypted the original message needs to be encrypted BEFORE it is queued for
         *       download and split up into a number of message parts.
         */
        if (!message.isEncrypted())
        {
          throw new MessagingException("The message (" + message.getId() + ") with type ("
              + message.getType() + ") and version (" + message.getTypeVersion()
              + ") exceeds the maximum asynchronous message size ("
              + Message.MAX_ASYNC_MESSAGE_SIZE
              + ") and must be encrypted prior to being queued for download");
        }

        // Calculate the hash for the message data to use as the message checksum
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");

        messageDigest.update(message.getData());

        String messageChecksum = Base64.encodeBytes(messageDigest.digest());

        // Split the message up into a number of message parts and persist each message part
        int numberOfParts = message.getData().length / MessagePart.MAX_MESSAGE_PART_SIZE;

        if ((message.getData().length % MessagePart.MAX_MESSAGE_PART_SIZE) > 0)
        {
          numberOfParts++;
        }

        for (int i = 0; i < numberOfParts; i++)
        {
          byte[] messagePartData;

          // If this is not the last message part
          if (i < (numberOfParts - 1))
          {
            messagePartData = new byte[MessagePart.MAX_MESSAGE_PART_SIZE];

            System.arraycopy(message.getData(), (i * MessagePart.MAX_MESSAGE_PART_SIZE),
                messagePartData, 0, MessagePart.MAX_MESSAGE_PART_SIZE);
          }

          // If this is the last message part
          else
          {
            int sizeOfPart = message.getData().length - (i * MessagePart.MAX_MESSAGE_PART_SIZE);

            messagePartData = new byte[sizeOfPart];

            System.arraycopy(message.getData(), (i * MessagePart.MAX_MESSAGE_PART_SIZE),
                messagePartData, 0, sizeOfPart);
          }

          MessagePart messagePart = new MessagePart(i + 1, numberOfParts, message.getId(),
            message.getUser(), message.getOrganisation(), message.getDevice(), message.getType(),
            message.getTypeVersion(), message.getCorrelationId(), message.getPriority(),
            message.getCreated(), message.getDataHash(), message.getEncryptionScheme(),
            message.getEncryptionIV(), messageChecksum, messagePartData);

          messagePart.setStatus(Status.QUEUED_FOR_DOWNLOAD);

          // Persist the message part in the database
          messagingDAO.createMessagePart(messagePart);
        }

        logger.debug("Queued " + numberOfParts + " message parts for download for the message ("
            + message.getId() + ")");
      }
    }
    catch (Throwable e)
    {
      throw new MessagingException("Failed to queue the message (" + message.getId()
          + ") for download", e);
    }

    // Archive the message
    archiveMessage(message);
  }

  /**
   * Queue the specified message for processing.
   *
   * @param message the message to queue
   *
   * @throws MessagingException
   */
  public void queueMessageForProcessing(Message message)
    throws MessagingException
  {
    // Update the status of the message to indicate that it is queued for processing
    message.setStatus(Message.Status.QUEUED_FOR_PROCESSING);

    try
    {
      messagingDAO.createMessage(message);
    }
    catch (Throwable e)
    {
      throw new MessagingException("Failed to queue the message (" + message.getId()
          + ") for processing", e);
    }

    // Archive the message
    archiveMessage(message);

    if (logger.isDebugEnabled())
    {
      logger.debug("Queued message (" + message.getId() + ") with type (" + message.getType()
          + ") and version (" + message.getTypeVersion() + ") for processing");

      logger.debug(message.toString());
    }

    // Inform the background message processor that a new message has been queued for processing
    try
    {
      backgroundMessageProcessor.process();
    }
    catch (Throwable e)
    {
      logger.error("Failed to invoke the background message processor to process the message ("
          + message.getId() + ") that was queued for processing", e);
    }
  }

  /**
   * Queue the specified message part for assembly.
   *
   * @param messagePart the message part to queue
   *
   * @throws MessagingException
   */
  public void queueMessagePartForAssembly(MessagePart messagePart)
    throws MessagingException
  {
    // Update the status of the message part to indicate that it is queued for assembly
    messagePart.setStatus(MessagePart.Status.QUEUED_FOR_ASSEMBLY);

    try
    {
      // Verify that the message has not already been queued for processing
      if (messagingDAO.isMessageArchived(messagePart.getId()))
      {
        logger.debug("The message (" + messagePart.getMessageId()
            + ") has already been queued for processing so the message part ("
            + messagePart.getId() + ") will be ignored");

        return;
      }

      // Check that we have not already received and queued this message part for assembly
      if (!messagingDAO.isMessagePartQueuedForAssembly(messagePart.getId()))
      {
        messagingDAO.createMessagePart(messagePart);
      }
    }
    catch (Throwable e)
    {
      throw new MessagingException("Failed to queue the message part (" + messagePart.getId()
          + ") for assembly", e);
    }

    // TODO: MOVE TO BACKGROUND THREAD RATHER TO PREVENT MOBILE DEVICE WAITING FOR THIS - MARCUS

    /*
     * Check whether all the parts for the message have been queued for assembly and if so
     * assemble the message.
     */
    try
    {
      // Check whether all the parts for the message have been queued for assembly
      if (messagingDAO.allPartsQueuedForMessage(messagePart.getMessageId(),
          messagePart.getTotalParts()))
      {
        // Retrieve the message parts queued for assembly
        List<MessagePart> messageParts =
          messagingDAO.getMessagePartsQueuedForAssembly(messagePart.getMessageId(),
            getInstanceName());

        // Assemble the message from its constituent parts
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        for (MessagePart tmpMessagePart : messageParts)
        {
          baos.write(tmpMessagePart.getData());
        }

        byte[] reconstructedData = baos.toByteArray();

        // Check that the reconstructed message data is valid
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");

        messageDigest.update(reconstructedData);

        String messageChecksum = Base64.encodeBytes(messageDigest.digest());

        if (!messageChecksum.equals(messagePart.getMessageChecksum()))
        {
          // Delete the message parts
          messagingDAO.deleteMessagePartsForMessage(messagePart.getMessageId());

          logger.error("Failed to verify the checksum for the reconstructed" + " message ("
              + messagePart.getMessageId() + ") with type (" + messagePart.getMessageType()
              + ") and type version (" + messagePart.getMessageTypeVersion() + ") from user ("
              + messagePart.getMessageUser() + ") and device (" + messagePart.getMessageDevice()
              + "). Found " + reconstructedData.length + " bytes of message data with the hash ("
              + messageChecksum + ") that was reconstructed from " + messageParts.size()
              + " message parts. The message will NOT be processed");

          String messagingDebugDirectory = getMessagingDebugDirectory();

          if (!StringUtil.isNullOrEmpty(messagingDebugDirectory))
          {
            FileOutputStream invalidMessageDataOut = null;
            PrintWriter invalidMessageInfoWriter = null;

            try
            {
              String invalidMessageDataFilename = messagingDebugDirectory + File.separator
                + messagePart.getMessageId() + ".data";

              invalidMessageDataOut = new FileOutputStream(new File(invalidMessageDataFilename));

              invalidMessageDataOut.write(reconstructedData);
              invalidMessageDataOut.flush();

              String invalidMessageInfoFilename = messagingDebugDirectory + File.separator
                + messagePart.getMessageId() + ".info";

              invalidMessageInfoWriter =
                new PrintWriter(new FileOutputStream(new File(invalidMessageInfoFilename)));

              invalidMessageInfoWriter.println("ID: " + messagePart.getMessageId());
              invalidMessageInfoWriter.println("Type: " + messagePart.getMessageType());
              invalidMessageInfoWriter.println("Version: " + messagePart.getMessageTypeVersion());
              invalidMessageInfoWriter.println("User: " + messagePart.getMessageUser());
              invalidMessageInfoWriter.println("Device: " + messagePart.getMessageDevice());
              invalidMessageInfoWriter.println("Organisation: "
                  + messagePart.getMessageOrganisation());
              invalidMessageInfoWriter.println("Correlation ID: "
                  + messagePart.getMessageCorrelationId());
              invalidMessageInfoWriter.println("Encryption Scheme: "
                  + messagePart.getMessageEncryptionScheme());
              invalidMessageInfoWriter.println("Data Hash: " + messagePart.getMessageDataHash());
              invalidMessageInfoWriter.println("Checksum: " + messagePart.getMessageChecksum());
              invalidMessageInfoWriter.println("Parts: " + messageParts.size());
              invalidMessageInfoWriter.println("Reconstructucted Checksum: " + messageChecksum);
              invalidMessageInfoWriter.println("Size: " + reconstructedData.length);

              invalidMessageInfoWriter.flush();
            }
            catch (Throwable e)
            {
              logger.error("Failed to write the invalid message to the messaging debug directory ("
                  + messagingDebugDirectory + "): " + e.getMessage(), e);
            }
            finally
            {
              try
              {
                if (invalidMessageDataOut != null)
                {
                  invalidMessageDataOut.close();
                }
              }
              catch (Throwable ignored) {}

              try
              {
                if (invalidMessageInfoWriter != null)
                {
                  invalidMessageInfoWriter.close();
                }
              }
              catch (Throwable ignored) {}
            }
          }

          return;
        }

        Message message = new Message(messagePart.getMessageId(), messagePart.getMessageUser(),
          messagePart.getMessageOrganisation(), messagePart.getMessageDevice(),
          messagePart.getMessageType(), messagePart.getMessageTypeVersion(),
          messagePart.getMessageCorrelationId(), messagePart.getMessagePriority(),
          Message.Status.INITIALISED, messagePart.getMessageCreated(), null, null, 0, 0, 0, null,
          null, reconstructedData, messagePart.getMessageDataHash(),
          messagePart.getMessageEncryptionScheme(), messagePart.getMessageEncryptionIV());

        if (decryptMessage(message))
        {
          queueMessageForProcessing(message);
        }
        else
        {
          // Delete the message parts
          messagingDAO.deleteMessagePartsForMessage(messagePart.getMessageId());

          logger.error("Failed to decrypt the reconstructed message (" + messagePart.getMessageId()
              + ") with type (" + messagePart.getMessageType() + ") and type version ("
              + messagePart.getMessageTypeVersion() + ") from the user ("
              + messagePart.getMessageUser() + ") and device (" + messagePart.getMessageDevice()
              + ") associated with the organisation (" + messagePart.getMessageOrganisation()
              + "). Found " + reconstructedData.length + " bytes of message data with the hash ("
              + messageChecksum + ") that was reconstructed from " + messageParts.size()
              + " message parts. The message will NOT be processed");

          String messagingDebugDirectory = getMessagingDebugDirectory();

          if (!StringUtil.isNullOrEmpty(messagingDebugDirectory))
          {
            FileOutputStream invalidMessageDataOut = null;
            PrintWriter invalidMessageInfoWriter = null;

            try
            {
              String invalidMessageDataFilename = messagingDebugDirectory + File.separator
                + messagePart.getMessageId() + ".data";

              invalidMessageDataOut = new FileOutputStream(new File(invalidMessageDataFilename));

              invalidMessageDataOut.write(reconstructedData);
              invalidMessageDataOut.flush();

              String invalidMessageInfoFilename = messagingDebugDirectory + File.separator
                + messagePart.getMessageId() + ".info";

              invalidMessageInfoWriter =
                new PrintWriter(new FileOutputStream(new File(invalidMessageInfoFilename)));

              invalidMessageInfoWriter.println("ID: " + messagePart.getMessageId());
              invalidMessageInfoWriter.println("Type: " + messagePart.getMessageType());
              invalidMessageInfoWriter.println("Version: " + messagePart.getMessageTypeVersion());
              invalidMessageInfoWriter.println("User: " + messagePart.getMessageUser());
              invalidMessageInfoWriter.println("Device: " + messagePart.getMessageDevice());
              invalidMessageInfoWriter.println("Organisation: "
                  + messagePart.getMessageOrganisation());
              invalidMessageInfoWriter.println("Correlation ID: "
                  + messagePart.getMessageCorrelationId());
              invalidMessageInfoWriter.println("Encryption Scheme: "
                  + messagePart.getMessageEncryptionScheme());
              invalidMessageInfoWriter.println("Checksum: " + messagePart.getMessageChecksum());
              invalidMessageInfoWriter.println("Parts: " + messageParts.size());
              invalidMessageInfoWriter.println("Reconstructucted Checksum: " + messageChecksum);
              invalidMessageInfoWriter.println("Size: " + reconstructedData.length);

              invalidMessageInfoWriter.flush();
            }
            catch (Throwable e)
            {
              logger.error("Failed to write the invalid message to the messaging debug directory ("
                  + messagingDebugDirectory + "): " + e.getMessage(), e);
            }
            finally
            {
              try
              {
                if (invalidMessageDataOut != null)
                {
                  invalidMessageDataOut.close();
                }
              }
              catch (Throwable ignored) {}

              try
              {
                if (invalidMessageInfoWriter != null)
                {
                  invalidMessageInfoWriter.close();
                }
              }
              catch (Throwable ignored) {}
            }
          }

          return;
        }

        // Delete the message parts
        messagingDAO.deleteMessagePartsForMessage(messagePart.getMessageId());
      }
    }
    catch (Exception e)
    {
      throw new MessagingException("Failed to assemble the parts for the message ("
          + messagePart.getMessageId() + ")", e);
    }
  }

  /**
   * Reset the locks for the messages.
   *
   * @param status    the current status of the messages that have been locked
   * @param newStatus the new status for the messages that have been unlocked
   *
   * @throws MessagingException
   */
  public void resetMessageLocks(Message.Status status, Message.Status newStatus)
    throws MessagingException
  {
    try
    {
      messagingDAO.resetMessageLocks(getInstanceName(), status, newStatus);

      if (logger.isDebugEnabled())
      {
        logger.debug("Successfully reset the locks for the messages with the status (" + status
            + ") that have been locked using the lock name (" + getInstanceName() + ")");
      }
    }
    catch (Throwable e)
    {
      throw new MessagingException("Failed to reset the locks for the "
          + " messages with the status (" + status
          + ") that have been locked using the lock name (" + getInstanceName() + ")", e);
    }
  }

  /**
   * Reset the locks for the message parts.
   *
   * @param status    the current status of the message parts that have been locked
   * @param newStatus the new status for the message parts that have been unlocked
   *
   * @throws MessagingException
   */
  public void resetMessagePartLocks(MessagePart.Status status, MessagePart.Status newStatus)
    throws MessagingException
  {
    try
    {
      messagingDAO.resetMessagePartLocks(getInstanceName(), status, newStatus);

      if (logger.isDebugEnabled())
      {
        logger.debug("Successfully reset the locks for the message parts with the status ("
            + status + ") that have been locked using the lock name (" + getInstanceName() + ")");
      }
    }
    catch (Throwable e)
    {
      throw new MessagingException("Failed to reset the locks for the "
          + " message parts with the status (" + status
          + ") that have been locked using the lock name (" + getInstanceName() + ")", e);
    }
  }

  /**
   * Set the status for the message.
   *
   * @param message the message to set the status for
   * @param status  the new status
   *
   * @throws MessagingException
   */
  public void setMessageStatus(Message message, Message.Status status)
    throws MessagingException
  {
    try
    {
      messagingDAO.setMessageStatus(message.getId(), status);

      message.setStatus(status);
    }
    catch (Throwable e)
    {
      throw new MessagingException("Failed to set the status for the message (" + message.getId()
          + ") to (" + status.toString() + ")", e);
    }
  }

  /**
   * Unlock the message.
   *
   * @param message the message to unlock
   * @param status  the new status for the unlocked message
   *
   * @throws MessagingException
   */
  public void unlockMessage(Message message, Message.Status status)
    throws MessagingException
  {
    try
    {
      messagingDAO.unlockMessage(message.getId(), status);

      message.setStatus(status);
    }
    catch (Throwable e)
    {
      throw new MessagingException("Failed to unlock the message (" + message.getId() + ")", e);
    }
  }

  /**
   * Retrieves the name of the messaging service instance.
   */
  private String getInstanceName()
  {
    if (instanceName == null)
    {
      String applicationName = null;

      try
      {
        applicationName = InitialContext.doLookup("java:app/AppName");
      }
      catch (Throwable ignored) {}

      if (applicationName == null)
      {
        try
        {
          applicationName = InitialContext.doLookup("java:comp/env/ApplicationName");
        }
        catch (Throwable ignored) {}
      }

      if (applicationName == null)
      {
        logger.error("Failed to retrieve the application name from JNDI using the names ("
            + "java:app/AppName) and (java:comp/env/ApplicationName) while constructing"
            + " the messaging service instance name");

        applicationName = "Unknown";
      }

      instanceName = applicationName + "::";

      try
      {
        java.net.InetAddress localMachine = java.net.InetAddress.getLocalHost();

        instanceName += localMachine.getHostName().toLowerCase();
      }
      catch (Throwable e)
      {
        logger.error("Failed to retrieve the server hostname while constructing the messaging"
            + " service instance name", e);
        instanceName = "Unknown";
      }

      // Check if we are running under JBoss and if so retrieve the server name
      if (System.getProperty("jboss.server.name") != null)
      {
        instanceName = instanceName + "::" + System.getProperty("jboss.server.name");
      }

      // Check if we are running under Glassfish and if so retrieve the server name
      else if (System.getProperty("glassfish.version") != null)
      {
        instanceName = instanceName + "::" + System.getProperty("com.sun.aas.instanceName");
      }

      // Check if we are running under Tomcat
      else if (System.getProperty("catalina.home") != null)
      {
        instanceName = instanceName + "::Tomcat";
      }

      // Check if we are running under WebSphere Application Server Community Edition (Geronimo)
      else if (System.getProperty("org.apache.geronimo.server.dir") != null)
      {
        instanceName = instanceName + "::Geronimo";
      }

      // Check if we are running under WebSphere Application Server Liberty Profile
      else if (System.getProperty("wlp.user.dir") != null)
      {
        instanceName = instanceName + "::WLP";
      }

      /*
       * Check if we are running under WebSphere and if so execute the code below to retrieve the
       * server name.
       */
      else
      {
        try
        {
          instanceName = instanceName + "::" + InitialContext.doLookup("servername").toString();
        }
        catch (Throwable e)
        {
          logger.error("Failed to retrieve the name of the WebSphere server instance from JNDI"
              + " while constructing the messaging service instance name", e);
          instanceName = instanceName + "::Unknown";
        }
      }
    }

    return instanceName;
  }

  /**
   * Returns the message type "key" used to uniquely identify a combination of message type and
   * message type version.
   *
   * @return the message type "key" used to uniquely identify a combination of message type and
   *         message type version
   */
  private String getMessageTypeKey(String messageType, int messageTypeVersion)
  {
    return messageType + "-" + messageTypeVersion;
  }

  /**
   * Initialise the configuration for the MessagingService instance.
   *
   * @throws MessagingException
   */
  private void initConfiguration()
    throws MessagingException
  {
    try
    {
      // Initialise the configuration
      if (!registry.stringValueExists("/Messaging", "AESEncryptionMasterKey"))
      {
        registry.setStringValue("/Messaging", "AESEncryptionMasterKey",
            "YRe6Xjcs/ClrT8itlgWY+cwWUJ5paW5vYWI0MWYzODk=", REGISTRY_ENCRYPTION_KEY,
            REGISTRY_ENCRYPTION_IV);
      }

      if (!registry.integerValueExists("/Messaging", "ProcessingRetryDelay"))
      {
        registry.setIntegerValue("/Messaging", "ProcessingRetryDelay", 60000);
      }

      if (!registry.integerValueExists("/Messaging", "MaximumProcessingAttempts"))
      {
        registry.setIntegerValue("/Messaging", "MaximumProcessingAttempts", 10000);
      }

      aesEncryptionMasterKey = Base64.decode(registry.getStringValue("/Messaging",
          "AESEncryptionMasterKey", "YRe6Xjcs/ClrT8itlgWY+cwWUJ5paW5vYWI0MWYzODk=",
          REGISTRY_ENCRYPTION_KEY, REGISTRY_ENCRYPTION_IV));

      processingRetryDelay = registry.getIntegerValue("/Messaging", "ProcessingRetryDelay", 60000);

      maximumProcessingAttempts = registry.getIntegerValue("/Messaging",
          "MaximumProcessingAttempts", 10000);
    }
    catch (Throwable e)
    {
      throw new MessagingException(
          "Failed to initialise the configuration for the MessagingService instance", e);
    }
  }

  /**
   * Initialise the message handlers.
   */
  private void initMessageHandlers()
    throws MessagingException
  {
    // Initialise each message handler
    for (MessageHandlerConfig messageHandlerConfig : messageHandlersConfig)
    {
      try
      {
        logger.info("Initialising the message handler (" + messageHandlerConfig.getName()
            + ") with class (" + messageHandlerConfig.getClassName() + ")");

        Class<?> clazz = Thread.currentThread().getContextClassLoader().loadClass(
            messageHandlerConfig.getClassName());

        Constructor<?> constructor = clazz.getConstructor(MessageHandlerConfig.class);

        if (constructor != null)
        {
          // Create an instance of the message handler
          IMessageHandler messageHandler =
            (IMessageHandler) constructor.newInstance(messageHandlerConfig);

          // Perform container-based dependency injection on the message handler
          CDIUtil.inject(messageHandler);

          List<MessageHandlerConfig.MessageConfig> messagesConfig =
            messageHandlerConfig.getMessagesConfig();

          for (MessageHandlerConfig.MessageConfig messageConfig : messagesConfig)
          {
            String messageTypeKey = getMessageTypeKey(messageConfig.getMessageType(),
              messageConfig.getMessageTypeVersion());

            if (messageHandlers.containsKey(messageTypeKey))
            {
              IMessageHandler existingMessageHandler = messageHandlers.get(messageTypeKey);

              logger.warn("Failed to register the message handler ("
                  + messageHandler.getClass().getName() + ") for the message type ("
                  + messageConfig.getMessageType() + ") and version ("
                  + messageConfig.getMessageTypeVersion() + ") since another message handler ("
                  + existingMessageHandler.getClass().getName()
                  + ") has already been registered to process messages of this type");
            }
            else
            {
              messageHandlers.put(messageTypeKey, messageHandler);
            }
          }
        }
        else
        {
          logger.error("Failed to register the message handler ("
              + messageHandlerConfig.getClassName() + ") since the message handler class"
              + " does not provide a constructor with the required signature");
        }
      }
      catch (Throwable e)
      {
        logger.error("Failed to initialise the message handler (" + messageHandlerConfig.getName()
            + ") with class (" + messageHandlerConfig.getClassName() + ")", e);
      }
    }
  }

  /**
   * Should a message with the specified type information be archived?
   *
   * @param type        the UUID identifying the type of message
   * @param typeVersion the version of the message type
   *
   * @return <code>true</code> if a message with the specified type information should be archived
   *         or <code>false</code> otherwise
   */
  private boolean isArchivableMessage(String type, int typeVersion)
  {
    // TODO: Add caching of this check

    // Check if any of the configured handlers supports archiving of the message
    for (MessageHandlerConfig messageHandlerConfig : messageHandlersConfig)
    {
      if (messageHandlerConfig.isArchivable(type, typeVersion))
      {
        return true;
      }
    }

    return false;
  }

  /**
   * Can a message with the specified type information be processed asynchronously?
   *
   * @param type        the UUID identifying the type of message
   * @param typeVersion the version of the message type
   *
   * @return <code>true</code> if a message with the specified type information can be processed
   *         asynchronously or <code>false</code> otherwise
   */
  private boolean isAsynchronousMessage(String type, int typeVersion)
  {
    // TODO: Add caching of this check

    // Check if any of the configured handlers support the synchronous processing of this message
    for (MessageHandlerConfig messageHandlerConfig : messageHandlersConfig)
    {
      if (messageHandlerConfig.supportsAsynchronousProcessing(type, typeVersion))
      {
        return true;
      }
    }

    return false;
  }

  /**
   * Can a message with the specified type information be processed synchronously?
   *
   * @param type        the UUID identifying the type of message
   * @param typeVersion the version of the message type
   *
   * @return <code>true</code> if a message with the specified type information can be processed
   *         synchronously or <code>false</code> otherwise
   */
  private boolean isSynchronousMessage(String type, int typeVersion)
  {
    // TODO: Add caching of this check

    // Check if any of the configured handlers support the synchronous processing of this message
    for (MessageHandlerConfig messageHandlerConfig : messageHandlersConfig)
    {
      if (messageHandlerConfig.supportsSynchronousProcessing(type, typeVersion))
      {
        return true;
      }
    }

    return false;
  }

  /**
   * Read the messaging configuration from all the <i>META-INF/MessagingConfig.xml</i>
   * configuration files that can be found on the classpath.
   *
   * @throws MessagingException
   */
  private void readMessagingConfig()
    throws MessagingException
  {
    try
    {
      messageHandlersConfig = new ArrayList<>();

      ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

      // Load the messaging configuration files from the classpath
      Enumeration<URL> configurationFiles = classLoader.getResources(MESSAGING_CONFIGURATION_PATH);

      while (configurationFiles.hasMoreElements())
      {
        URL configurationFile = configurationFiles.nextElement();

        if (logger.isDebugEnabled())
        {
          logger.debug("Reading the messaging configuration file ("
              + configurationFile.toURI().toString() + ")");
        }

        // Retrieve a document builder instance using the factory
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();

        builderFactory.setValidating(true);

        // builderFactory.setNamespaceAware(true);
        DocumentBuilder builder = builderFactory.newDocumentBuilder();

        builder.setEntityResolver(new DtdJarResolver("MessagingConfig.dtd",
            "META-INF/MessagingConfig.dtd"));
        builder.setErrorHandler(new XmlParserErrorHandler());

        // Parse the XML messaging configuration file using the document builder
        InputSource inputSource = new InputSource(configurationFile.openStream());
        Document document = builder.parse(inputSource);
        Element rootElement = document.getDocumentElement();

        List<Element> messageHandlerElements = XmlUtils.getChildElements(rootElement,
          "messageHandler");

        for (Element messageHandlerElement : messageHandlerElements)
        {
          // Read the handler configuration
          String name = XmlUtils.getChildElementText(messageHandlerElement, "name");
          String className = XmlUtils.getChildElementText(messageHandlerElement, "class");

          MessageHandlerConfig messageHandlerConfig = new MessageHandlerConfig(name, className);

          // Get the "Messages" element
          Element messagesElement = XmlUtils.getChildElement(messageHandlerElement, "messages");

          // Read the message configuration for the handler
          List<Element> messageElements = XmlUtils.getChildElements(messagesElement, "message");

          for (Element messageElement : messageElements)
          {
            String messageType = messageElement.getAttribute("type");
            int messageTypeVersion = Integer.parseInt(messageElement.getAttribute("typeVersion"));
            boolean isSynchronous =
              messageElement.getAttribute("isSynchronous").equalsIgnoreCase("Y");
            boolean isAsynchronous =
              messageElement.getAttribute("isAsynchronous").equalsIgnoreCase("Y");
            boolean isArchivable =
              messageElement.getAttribute("isArchivable").equalsIgnoreCase("Y");

            messageHandlerConfig.addMessageConfig(messageType, messageTypeVersion, isSynchronous,
                isAsynchronous, isArchivable);
          }

          messageHandlersConfig.add(messageHandlerConfig);
        }
      }
    }
    catch (Throwable e)
    {
      throw new MessagingException("Failed to read the messaging configuration", e);
    }
  }
}

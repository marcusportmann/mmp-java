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
import guru.mmp.common.util.ISO8601;
import guru.mmp.common.wbxml.Document;
import guru.mmp.common.wbxml.Element;
import guru.mmp.common.wbxml.Encoder;

import java.util.Date;
import java.util.UUID;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>Message</code> class holds the information for a message that is processed by the
 * messaging infrastructure. It provides facilities to convert to and from the WBXML
 * representation of the message.
 *
 * @author Marcus Portmann
 */
public class Message
{
  /**
   * The maximum size of an asynchronous message in bytes. Messages larger than this size will be
   * split into a number of message parts.
   */
  public static final int MAX_ASYNC_MESSAGE_SIZE = 40960;

  /**
   * The Universally Unique Identifier (UUID) used to correlate the message.
   */
  private String correlationId;

  /**
   * The date and time the message was created.
   */
  private Date created;

  /**
   * The data for the message.
   */
  private byte[] data;

  /**
   * The hash of the unencrypted data for the message.
   *
   * NOTE: If the message data is encrypted then this will be the hash of the unencrypted message
   *       data and will be used to verify the message data has been decrypted successfully.
   */
  private String dataHash;

  /**
   * The device ID identifying the device the message originated from.
   */
  private String device;

  /**
   * The number of times that downloading of the message was attempted.
   */
  private int downloadAttempts;

  /**
   * The base-64 encoded initialisation vector for the encryption scheme for the message.
   */
  private String encryptionIV;

  /**
   * The encryption scheme used to secure the message.
   */
  private EncryptionScheme encryptionScheme;

  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the message.
   */
  private String id;

  /**
   * Is encryption disabled for the message.
   */
  private boolean isEncryptionDisabled;

  /**
   * The date and time the last attempt was made to process the message.
   */
  private Date lastProcessed;

  /**
   * The name of the entity that has locked this message for processing or <code>null</code> if
   * the message is not being processed.
   */
  private String lockName;

  /**
   * The organisation code identifying the organisation associated with the message.
   */
  private String organisation;

  /**
   * The date and time the message was persisted.
   */
  private Date persisted;

  /**
   * The message priority. Messages with a higher priority value are processed before messages
   * with a lower priority value.
   */
  private Priority priority;

  /**
   * The number of times that the processing of the message was attempted.
   */
  private int processAttempts;

  /**
   * The number of times that the sending of the message was attempted.
   */
  private int sendAttempts;

  /**
   * The message status e.g. Initialised, Sending, etc.
   */
  private Status status;

  /**
   * The UUID identifying the type of message.
   */
  private String type;

  /**
   * The version of the message type.
   */
  private int typeVersion;

  /**
   * The date and time the message was updated.
   */
  private Date updated;

  /**
   * The username identifying the user associated with the message.
   */
  private String user;

  /**
   * Constructs a new <code>Message</code> and populates it from the message information stored
   * in the specified WBXML document.
   *
   * @param document the WBXML document containing the message information
   */
  public Message(Document document)
  {
    Element rootElement = document.getRootElement();

    this.id = rootElement.getAttributeValue("id");
    this.user = rootElement.getAttributeValue("user");
    this.organisation = rootElement.getAttributeValue("organisation");
    this.device = rootElement.getAttributeValue("device");
    this.type = rootElement.getAttributeValue("type");
    this.typeVersion = Integer.parseInt(rootElement.getAttributeValue("typeVersion"));
    this.correlationId = rootElement.getAttributeValue("correlationId");
    this.priority = Priority.fromCode(Integer.parseInt(rootElement.getAttributeValue("priority")));
    this.data = rootElement.getOpaque();
    this.dataHash = rootElement.getAttributeValue("dataHash");
    this.encryptionScheme = EncryptionScheme.fromCode(
      Integer.parseInt(rootElement.getAttributeValue("encryptionScheme")));
    this.encryptionIV = rootElement.getAttributeValue("encryptionIV");

    String createdAttributeValue = rootElement.getAttributeValue("created");

    try
    {
      this.created = ISO8601.toDate(createdAttributeValue);
    }
    catch (Throwable e)
    {
      throw new MessagingException("Failed to parse the created ISO8601 timestamp ("
          + createdAttributeValue + ") for the message", e);
    }

    this.sendAttempts = Integer.parseInt(rootElement.getAttributeValue("sendAttempts"));
    this.processAttempts = 0;
    this.status = Status.INITIALISED;
  }

  /**
   * Constructs a new <code>Message</code>.
   *
   * @param user          the username identifying the user associated with the message
   * @param organisation  the organisation code identifying the organisation associated with the
   *                      message
   * @param device        the device ID identifying the device the message originated from
   * @param type          the UUID identifying the type of message
   * @param typeVersion   the version of the message type
   * @param correlationId the Universally Unique Identifier (UUID) used to correlate the message
   * @param priority      the message priority
   * @param data          the data for the message which is NOT encrypted
   */
  public Message(String user, String organisation, String device, String type, int typeVersion,
      String correlationId, Priority priority, byte[] data)
  {
    this.id = UUID.randomUUID().toString();
    this.user = user;
    this.organisation = organisation;
    this.device = device;
    this.type = type;
    this.typeVersion = typeVersion;
    this.correlationId = correlationId;
    this.priority = priority;
    this.data = data;
    this.dataHash = "";
    this.encryptionScheme = EncryptionScheme.NONE;
    this.encryptionIV = "";
    this.created = new Date();
    this.sendAttempts = 0;
    this.processAttempts = 0;
    this.status = Status.INITIALISED;
  }

  /**
   * Constructs a new <code>Message</code>.
   *
   * @param user             the username identifying the user associated with the message
   * @param organisation     the organisation code identifying the organisation associated with the
   *                         message
   * @param device           the device ID identifying the device the message originated from
   * @param type             the UUID identifying the type of message
   * @param typeVersion      the version of the message type
   * @param correlationId    the Universally Unique Identifier (UUID) used to correlate the message
   * @param priority         the message priority
   * @param data             the data for the message which may be encrypted
   * @param dataHash         the hash of the unencrypted data for the message
   * @param encryptionScheme the encryption scheme used to secure the message
   * @param encryptionIV     the base-64 encoded initialisation vector for the encryption scheme
   */
  public Message(String user, String organisation, String device, String type, int typeVersion,
      String correlationId, Priority priority, byte[] data, String dataHash,
      EncryptionScheme encryptionScheme, String encryptionIV)
  {
    this.id = UUID.randomUUID().toString();
    this.user = user;
    this.organisation = organisation;
    this.device = device;
    this.type = type;
    this.typeVersion = typeVersion;
    this.correlationId = correlationId;
    this.priority = priority;
    this.data = data;
    this.dataHash = dataHash;

    if (dataHash.length() == 0)
    {
      throw new MessagingException("Unable to initialise a message with encrypted data using a"
          + " blank data hash");
    }

    this.encryptionScheme = encryptionScheme;
    this.encryptionIV = encryptionIV;
    this.created = new Date();
    this.sendAttempts = 0;
    this.processAttempts = 0;
    this.status = Status.INITIALISED;
  }

  /**
   * Constructs a new <code>Message</code>.
   *
   * @param id               the UUID used to uniquely identify the message
   * @param user             the username identifying the user associated with the message
   * @param organisation     the organisation code identifying the organisation associated with the
   *                         message
   * @param device           the device ID identifying the device the message originated from
   * @param type             the UUID identifying the type of message
   * @param typeVersion      the version of the message type
   * @param correlationId    the Universally Unique Identifier (UUID) used to correlate the message
   * @param priority         the message priority
   * @param status           the message status e.g. Initialised, Sending, etc
   * @param created          the date and time the message was created
   * @param persisted        the date and time the message was persisted
   * @param updated          the date and time the message was updated
   * @param sendAttempts     the number of times that the sending of the message was attempted
   * @param processAttempts  the number of times that the processing of the message was attempted
   * @param downloadAttempts the number of times that downloading of the message was attempted
   * @param lockName         the name of the entity that has locked this message for processing or
   *                         <code>null</code> if the message is not being processed
   * @param lastProcessed    the date and time the last attempt was made to process the message
   * @param data             the data for the message which may be encrypted
   * @param dataHash         the hash of the unencrypted data for the message
   * @param encryptionScheme the encryption scheme used to secure the message
   * @param encryptionIV     the base-64 encoded initialisation vector for the encryption scheme
   */
  public Message(String id, String user, String organisation, String device, String type,
      int typeVersion, String correlationId, Priority priority, Status status, Date created,
      Date persisted, Date updated, int sendAttempts, int processAttempts, int downloadAttempts,
      String lockName, Date lastProcessed, byte[] data, String dataHash,
      EncryptionScheme encryptionScheme, String encryptionIV)
  {
    this.id = id;
    this.user = user;
    this.organisation = organisation;
    this.device = device;
    this.type = type;
    this.typeVersion = typeVersion;
    this.correlationId = correlationId;
    this.priority = priority;
    this.status = status;
    this.created = created;
    this.persisted = persisted;
    this.updated = updated;
    this.sendAttempts = sendAttempts;
    this.processAttempts = processAttempts;
    this.downloadAttempts = downloadAttempts;
    this.lockName = lockName;
    this.lastProcessed = lastProcessed;
    this.data = data;
    this.dataHash = dataHash;
    this.encryptionScheme = encryptionScheme;
    this.encryptionIV = encryptionIV;
  }

  /**
   * The enumeration giving the possible priorities for a message.
   */
  public enum Priority
  {
    LOW(1, "Low"), MEDIUM(5, "Medium"), HIGH(10, "High");

    /**
     * The code identifying the priority.
     */
    private int code;

    /**
     * The name of the priority.
     */
    private String name;

    Priority(int code, String name)
    {
      this.code = code;
      this.name = name;
    }

    /**
     * Returns the priority given by the specified numeric code value.
     *
     * @param code the numeric code value identifying the priority
     *
     * @return the priority given by the specified numeric code value
     */
    public static Priority fromCode(int code)
    {
      switch (code)
      {
        case 1:
          return Priority.LOW;

        case 5:
          return Priority.MEDIUM;

        case 10:
          return Priority.HIGH;

        default:
          return Priority.MEDIUM;
      }
    }

    /**
     * Returns the code identifying the priority.
     *
     * @return the code identifying the priority
     */
    public int getCode()
    {
      return code;
    }

    /**
     * Returns the name of the priority.
     *
     * @return the name of the priority
     */
    public String getName()
    {
      return name;
    }

    /**
     * Return the string representation of the <code>Priority</code> enumeration value.
     *
     * @return the string representation of the <code>Priority</code> enumeration value
     */
    public String toString()
    {
      return name;
    }
  }

  /**
   * The enumeration giving the possible statuses for a message.
   */
  public enum Status
  {
    INITIALISED(0, "Initialised"), QUEUED_FOR_SENDING(1, "QueuedForSending"),
    QUEUED_FOR_PROCESSING(2, "QueuedForProcessing"), ABORTED(3, "Aborted"), FAILED(4, "Failed"),
    PROCESSING(5, "Processing"), SENDING(6, "Sending"),
    QUEUED_FOR_DOWNLOAD(7, "QueuedForDownload"), DOWNLOADING(8, "Downloading"),
    PROCESSED(10, "Processed"), UNKNOWN(-1, "Unknown");

    private int code;
    private String name;

    Status(int code, String name)
    {
      this.code = code;
      this.name = name;
    }

    /**
     * Returns the status given by the specified numeric code value.
     *
     * @param code the numeric code value identifying the status
     *
     * @return the status given by the specified numeric code value
     */
    public static Status fromCode(int code)
    {
      switch (code)
      {
        case 0:
          return Status.INITIALISED;

        case 1:
          return Status.QUEUED_FOR_SENDING;

        case 2:
          return Status.QUEUED_FOR_PROCESSING;

        case 3:
          return Status.ABORTED;

        case 4:
          return Status.FAILED;

        case 5:
          return Status.PROCESSING;

        case 6:
          return Status.SENDING;

        case 7:
          return Status.QUEUED_FOR_DOWNLOAD;

        case 8:
          return Status.DOWNLOADING;

        case 10:
          return Status.PROCESSED;

        default:
          return Status.UNKNOWN;
      }
    }

    /**
     * Returns the numeric code value identifying the status.
     *
     * @return the numeric code value identifying the status
     */
    public int getCode()
    {
      return code;
    }

    /**
     * Returns the name of the status.
     *
     * @return the name of the status
     */
    public String getName()
    {
      return name;
    }

    /**
     * Return the string representation of the status enumeration value.
     *
     * @return the string representation of the status enumeration value
     */
    public String toString()
    {
      return name;
    }
  }

  /**
   * Returns <code>true</code> if the WBXML document contains valid message information or
   * <code>false</code> otherwise.
   *
   * @param document the WBXML document to validate
   *
   * @return <code>true</code> if the WBXML document contains valid message information or
   *         <code>false</code> otherwise
   */
  public static boolean isValidWBXML(Document document)
  {
    Element rootElement = document.getRootElement();

    return rootElement.getName().equals("Message")
        && !((!rootElement.hasAttribute("id")) || (!rootElement.hasAttribute("user"))
          || (!rootElement.hasAttribute("organisation")) || (!rootElement.hasAttribute("device"))
            || (!rootElement.hasAttribute("priority")) || (!rootElement.hasAttribute("type"))
              || (!rootElement.hasAttribute("typeVersion"))
                || (!rootElement.hasAttribute("correlationId"))
                  || (!rootElement.hasAttribute("created"))
                    || (!rootElement.hasAttribute("sendAttempts"))
                      || (!rootElement.hasAttribute("dataHash"))
                        || (!rootElement.hasAttribute("encryptionScheme"))
                          || (!rootElement.hasAttribute("encryptionIV")));
  }

  /**
   * Returns the Universally Unique Identifier (UUID) used to correlate the message.
   *
   * @return the Universally Unique Identifier (UUID) used to correlate the message
   */
  public String getCorrelationId()
  {
    return correlationId;
  }

  /**
   * Returns the date and time the message was created.
   *
   * @return the date and time the message was created
   */
  public Date getCreated()
  {
    return created;
  }

  /**
   * Returns the data for the message which may be encrypted.
   *
   * @return the data for the message which may be encrypted
   */
  public byte[] getData()
  {
    return data;
  }

  /**
   * Returns the hash of the unencrypted data for the message.
   *
   * @return the hash of the unencrypted data for the message
   */
  public String getDataHash()
  {
    return dataHash;
  }

  /**
   * The device ID identifying the device the message originated from.
   *
   * @return the device ID identifying the device the message originated from
   */
  public String getDevice()
  {
    return device;
  }

  /**
   * Returns the number of times that downloading of the message was attempted.
   *
   * @return the number of times that downloading of the message was attempted
   */
  public int getDownloadAttempts()
  {
    return downloadAttempts;
  }

  /**
   * Returns the base-64 encoded initialisation vector for the encryption scheme for the message.
   *
   * @return the base-64 encoded initialisation vector for the encryption scheme for the message
   */
  public String getEncryptionIV()
  {
    return encryptionIV;
  }

  /**
   * Returns the encryption scheme used to secure the message.
   *
   * @return the encryption scheme used to secure the message
   */
  public EncryptionScheme getEncryptionScheme()
  {
    return encryptionScheme;
  }

  /**
   * Returns the Universally Unique Identifier (UUID) used to uniquely identify the message.
   *
   * @return the Universally Unique Identifier (UUID) used to uniquely identify the message
   */
  public String getId()
  {
    return id;
  }

  /**
   * Returns the date and time the last attempt was made to process the message.
   *
   * @return the date and time the last attempt was made to process the message
   */
  public Date getLastProcessed()
  {
    return lastProcessed;
  }

  /**
   * Returns the name of the entity that has locked this message for processing or
   * <code>null</code> if the message is not being processed.
   *
   * @return the name of the entity that has locked this message for processing or
   *         <code>null</code> if the message is not being processed
   */
  public String getLockName()
  {
    return lockName;
  }

  /**
   * Returns the organisation code identifying the organisation associated with the message.
   *
   * @return the organisation code identifying the organisation associated with the message
   */
  public String getOrganisation()
  {
    return organisation;
  }

  /**
   * Returns the date and time the message was persisted.
   *
   * @return the date and time the message was persisted
   */
  public Date getPersisted()
  {
    return persisted;
  }

  /**
   * Returns the message priority.
   *
   * "Out of Order" processing is usually applied to messages so that messages with a higher
   * priority value are processed before messages with a lower priority value.
   *
   * @return the message priority
   */
  public Priority getPriority()
  {
    return priority;
  }

  /**
   * Returns the number of times that the processing of the message was attempted.
   *
   * @return the number of times that the processing of the message was attempted
   */
  public int getProcessAttempts()
  {
    return processAttempts;
  }

  /**
   * Returns the number of times that the sending of the message was attempted.
   *
   * @return the number of times that the sending of the message was attempted
   */
  public int getSendAttempts()
  {
    return sendAttempts;
  }

  /**
   * Returns the message status e.g. Initialised, Sending, etc.
   *
   * @return the message status e.g. Initialised, Sending, etc
   */
  public Status getStatus()
  {
    return status;
  }

  /**
   * Returns the UUID identifying the type of message.
   *
   * @return the UUID identifying the type of message
   */
  public String getType()
  {
    return type;
  }

  /**
   * Returns the version of the message type.
   *
   * @return the version of the message type
   */
  public int getTypeVersion()
  {
    return typeVersion;
  }

  /**
   * Returns the date and time the message was updated.
   *
   * @return the date and time the message was updated
   */
  public Date getUpdated()
  {
    return updated;
  }

  /**
   * Returns the username identifying the user associated with the message.
   *
   * @return the username identifying the user associated with the message
   */
  public String getUser()
  {
    return user;
  }

  /**
   * Returns <code>true</code> if the data for the message is encrypted or <code>false</code>
   * otherwise.
   *
   * @return <code>true</code> if the data for the message is encrypted or <code>false</code>
   *         otherwise
   */
  public boolean isEncrypted()
  {
    return ((dataHash != null) && (dataHash.length() > 0));
  }

  /**
   * Returns <code>true</code> if encryption is disabled for the message or <code>false</code>
   * otherwise.
   *
   * @return <code>true</code> if encryption is disabled for the message or <code>false</code>
   *         otherwise
   */
  public boolean isEncryptionDisabled()
  {
    return isEncryptionDisabled;
  }

  /**
   * Set the Universally Unique Identifier (UUID) used to correlate the message.
   *
   * @param correlationId the Universally Unique Identifier (UUID) used to correlate the message
   */
  public void setCorrelationId(String correlationId)
  {
    this.correlationId = correlationId;
  }

  /**
   * Set the date and time the message was created.
   *
   * @param created the date and time the message was created
   */
  public void setCreated(Date created)
  {
    this.created = created;
  }

  /**
   * Set the data for the message which may be encrypted.
   *
   * @param data the data for the message which may be encrypted
   */
  public void setData(byte[] data)
  {
    this.data = data;
  }

  /**
   * Set the hash of the unencrypted data for the message.
   *
   * @param dataHash the hash of the unencrypted data for the message
   */
  public void setDataHash(String dataHash)
  {
    this.dataHash = dataHash;
  }

  /**
   * Set the device ID identifying the device the message originated from.
   *
   * @param device the device ID identifying the device the message originated from
   */
  public void setDevice(String device)
  {
    this.device = device;
  }

  /**
   * Set the number of times that downloading of the message was attempted.
   *
   * @param downloadAttempts the number of times that downloading of the message was attempted
   */
  public void setDownloadAttempts(int downloadAttempts)
  {
    this.downloadAttempts = downloadAttempts;
  }

  /**
   * Set the base-64 encoded initialisation vector for the encryption scheme for the message.
   *
   * @param encryptionIV the base-64 encoded initialisation vector for the encryption scheme for
   *                     the message
   */
  public void setEncryptionIV(String encryptionIV)
  {
    this.encryptionIV = encryptionIV;
  }

  /**
   * Set the encryption scheme used to secure the message.
   *
   * @param encryptionScheme the encryption scheme used to secure the message
   */
  public void setEncryptionScheme(EncryptionScheme encryptionScheme)
  {
    this.encryptionScheme = encryptionScheme;
  }

  /**
   * Set the Universally Unique Identifier (UUID) used to uniquely identify the message.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the message
   */
  public void setId(String id)
  {
    this.id = id;
  }

  /**
   * Set whether encryption is disabled for the message.
   *
   * @param isEncryptionDisabled <code>true</code> if encryption is disabled for the message or
   *                             <code>false</code> otherwise
   */
  public void setIsEncryptionDisabled(boolean isEncryptionDisabled)
  {
    this.isEncryptionDisabled = isEncryptionDisabled;
  }

  /**
   * Set the date and time the last attempt was made to process the message.
   *
   * @param lastProcessed the date and time the last attempt was made to process the message
   */
  public void setLastProcessed(Date lastProcessed)
  {
    this.lastProcessed = lastProcessed;
  }

  /**
   * Set the name of the entity that has locked this message for processing or <code>null</code>
   * if the message is not being processed.
   *
   * @param lockName the name of the entity that has locked this message for processing or
   *                 <code>null</code> if the message is not being processed
   */
  public void setLockName(String lockName)
  {
    this.lockName = lockName;
  }

  /**
   * Set the organisation code identifying the organisation associated with the message.
   *
   * @param organisation the organisation code identifying the organisation associated with the
   *                     message
   */
  public void setOrganisation(String organisation)
  {
    this.organisation = organisation;
  }

  /**
   * Set the date and time the message was persisted.
   *
   * @param persisted the date and time the message was persisted
   */
  public void setPersisted(Date persisted)
  {
    this.persisted = persisted;
  }

  /**
   * Set the message priority.
   *
   * "Out of Order" processing is usually applied to messages so that messages with a higher
   * priority value are processed before messages with a lower priority value.
   *
   * @param priority the message priority
   */
  public void setPriority(Priority priority)
  {
    this.priority = priority;
  }

  /**
   * Set the number of times that the processing of the message was attempted.
   *
   * @param processAttempts the number of times that the processing of the message was attempted
   */
  public void setProcessAttempts(int processAttempts)
  {
    this.processAttempts = processAttempts;
  }

  /**
   * Set the number of times that the sending of the message was attempted.
   *
   * @param sendAttempts the number of times that the sending of the message was attempted
   */
  public void setSendAttempts(int sendAttempts)
  {
    this.sendAttempts = sendAttempts;
  }

  /**
   * Set the message status e.g. Initialised, Sending, etc.
   *
   * @param status the message status e.g. Initialised, Sending, etc
   */
  public void setStatus(Status status)
  {
    this.status = status;
  }

  /**
   * Set the UUID identifying the type of message.
   *
   * @param type the UUID identifying the type of message
   */
  public void setType(String type)
  {
    this.type = type;
  }

  /**
   * Set the version of the message type.
   *
   * @param typeVersion the version of the message type
   */
  public void setTypeVersion(int typeVersion)
  {
    this.typeVersion = typeVersion;
  }

  /**
   * Set the date and time the message was updated.
   *
   * @param updated the date and time the message was updated
   */
  public void setUpdated(Date updated)
  {
    this.updated = updated;
  }

  /**
   * Set the username identifying the user associated with the message.
   *
   * @param user the username identifying the user associated with the message
   */
  public void setUser(String user)
  {
    this.user = user;
  }

  /**
   * Returns the String representation of the message.
   *
   * @return the String representation of the message
   */
  @Override
  public String toString()
  {
    StringBuilder buffer = new StringBuilder("<Message");

    buffer.append(" id=\"").append(id).append("\"");
    buffer.append(" user=\"").append(user).append("\"");
    buffer.append(" organisation=\"").append(organisation).append("\"");
    buffer.append(" device=\"").append(device).append("\"");
    buffer.append(" type=\"").append(type).append("\"");
    buffer.append(" typeVersion=\"").append(typeVersion).append("\"");
    buffer.append(" correlationId=\"").append(correlationId).append("\"");
    buffer.append(" priority=\"").append(priority).append("\"");
    buffer.append(" status=\"").append(status).append("\"");
    buffer.append(" created=\"").append(ISO8601.fromDate(created)).append("\"");

    if (persisted != null)
    {
      buffer.append(" persisted=\"").append(ISO8601.fromDate(persisted)).append("\"");
    }
    else
    {
      buffer.append(" persisted=\"Never\"");
    }

    if (updated != null)
    {
      buffer.append(" updated=\"").append(ISO8601.fromDate(updated)).append("\"");
    }
    else
    {
      buffer.append(" updated=\"Never\"");
    }

    buffer.append(" sendAttempts=\"").append(sendAttempts).append("\"");
    buffer.append(" processAttempts=\"").append(processAttempts).append("\"");
    buffer.append(" downloadAttempts=\"").append(downloadAttempts).append("\"");

    if (lockName != null)
    {
      buffer.append(" lockName=\"").append(lockName).append("\"");
    }
    else
    {
      buffer.append(" lockName=\"None\"");
    }

    if (lastProcessed != null)
    {
      buffer.append(" lastProcessed=\"").append(ISO8601.fromDate(lastProcessed)).append("\"");
    }
    else
    {
      buffer.append(" lastProcessed=\"Never\"");
    }

    buffer.append(" dataHash=\"").append(dataHash).append("\"");
    buffer.append(" encryptionScheme=\"").append(encryptionScheme.toString()).append("\"");
    buffer.append(" encryptionIV=\"").append(encryptionIV).append("\"");

    if (isEncrypted())
    {
      buffer.append(">").append(data.length).append(" bytes of opaque encrypted data</Message>");
    }
    else
    {
      buffer.append(">").append(data.length).append(" bytes of opaque data</Message>");
    }

    return buffer.toString();
  }

  /**
   * Returns the WBXML representation of the message.
   *
   * @return the WBXML representation of the message
   */
  public byte[] toWBXML()
  {
    Element rootElement = new Element("Message");

    rootElement.setAttribute("id", id);
    rootElement.setAttribute("user", user);
    rootElement.setAttribute("organisation", organisation);
    rootElement.setAttribute("device", device);
    rootElement.setAttribute("type", type);
    rootElement.setAttribute("typeVersion", Integer.toString(typeVersion));
    rootElement.setAttribute("correlationId", correlationId);
    rootElement.setAttribute("priority", Integer.toString(priority.getCode()));
    rootElement.setAttribute("created", ISO8601.fromDate(created));
    rootElement.setAttribute("sendAttempts", Integer.toString(sendAttempts));

    if (dataHash != null)
    {
      rootElement.setAttribute("dataHash", dataHash);
    }
    else
    {
      rootElement.setAttribute("dataHash", "");
    }

    rootElement.setAttribute("encryptionScheme", Integer.toString(encryptionScheme.getCode()));
    rootElement.setAttribute("encryptionIV", encryptionIV);

    rootElement.addContent(data);

    Encoder encoder = new Encoder(new Document(rootElement));

    return encoder.getData();
  }
}

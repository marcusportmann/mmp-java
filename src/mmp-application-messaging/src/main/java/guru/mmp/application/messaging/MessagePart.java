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
 * The <code>MessagePart</code> class stores the information for a message part that is
 * processed by the messaging infrastructure.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public class MessagePart
{
  /**
   * The maximum size of a message part in bytes.
   */
  public static final int MAX_MESSAGE_PART_SIZE = 40960;

  /**
   * The binary data for the message part.
   */
  private byte[] data;

  /**
   * The number of times that downloading of the message part was attempted.
   */
  private int downloadAttempts;

  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the message part.
   */
  private String id;

  /**
   * The name of the entity that has locked the message part for processing.
   */
  private String lockName;

  /**
   * The checksum for the original message.
   */
  private String messageChecksum;

  /**
   * The Universally Unique Identifier (UUID) used to correlate the original message.
   */
  private String messageCorrelationId;

  /**
   * The date and time the original message was created.
   */
  private Date messageCreated;

  /**
   * The hash of the data for the original message.
   */
  private String messageDataHash;

  /**
   * The device ID identifying the device the original message originated from.
   */
  private String messageDevice;

  /**
   * The base-64 encoded initialisation vector for the encryption scheme for the original message.
   */
  private String messageEncryptionIV;

  /**
   * The encryption scheme used to secure the original message.
   */
  private EncryptionScheme messageEncryptionScheme;

  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the original message.
   */
  private String messageId;

  /**
   * The organisation code identifying the organisation associated with the original message.
   */
  private String messageOrganisation;

  /**
   *  The priority for the original message.
   */
  private Message.Priority messagePriority;

  /**
   * The UUID identifying the type of the original message.
   */
  private String messageType;

  /**
   * The version of the original message type.
   */
  private int messageTypeVersion;

  /**
   * The username identifying the user associated with the original message.
   */
  private String messageUser;

  /**
   * The number of the message part in the set of message parts for the original message.
   */
  private int partNo;

  /**
   * The date and time the message part was persisted.
   */
  private Date persisted;

  /**
   * The number of times that the sending of the message part was attempted.
   */
  private int sendAttempts;

  /**
   * The message part status e.g. Initialised, Sending, etc
   */
  private Status status;

  /**
   * The total number of parts in the set of message parts for the original message.
   */
  private int totalParts;

  /**
   * The date and time the message part was last updated.
   */
  private Date updated;

  /**
   * Constructs a new <code>MessagePart</code> and populates it from the message information stored
   * in the specified WBXML document.
   *
   * @param document the WBXML document containing the message information
   *
   * @throws MessagingException
   */
  public MessagePart(Document document)
    throws MessagingException
  {
    Element rootElement = document.getRootElement();

    this.id = rootElement.getAttributeValue("id");
    this.partNo = Integer.parseInt(rootElement.getAttributeValue("partNo"));
    this.totalParts = Integer.parseInt(rootElement.getAttributeValue("totalParts"));
    this.sendAttempts = Integer.parseInt(rootElement.getAttributeValue("sendAttempts"));
    this.status = Status.INITIALISED;

    this.messageId = rootElement.getAttributeValue("messageId");
    this.messageUser = rootElement.getAttributeValue("messageUser");
    this.messageOrganisation = rootElement.getAttributeValue("messageOrganisation");
    this.messageDevice = rootElement.getAttributeValue("messageDevice");
    this.messageType = rootElement.getAttributeValue("messageType");
    this.messageTypeVersion = Integer.parseInt(rootElement.getAttributeValue("messageTypeVersion"));
    this.messageCorrelationId = rootElement.getAttributeValue("messageCorrelationId");
    this.messagePriority =
      Message.Priority.fromCode(Integer.parseInt(rootElement.getAttributeValue("messagePriority")));

    String messageCreatedAttributeValue = rootElement.getAttributeValue("messageCreated");

    try
    {
      this.messageCreated = ISO8601.toDate(messageCreatedAttributeValue);
    }
    catch (Throwable e)
    {
      throw new MessagingException("Failed to parse the messageCreated ISO8601 timestamp ("
          + messageCreatedAttributeValue + ") for the message part", e);
    }

    this.messageDataHash = rootElement.getAttributeValue("messageDataHash");
    this.messageEncryptionScheme = EncryptionScheme.fromCode(
      Integer.parseInt(rootElement.getAttributeValue("messageEncryptionScheme")));
    this.messageEncryptionIV = rootElement.getAttributeValue("messageEncryptionIV");
    this.messageChecksum = rootElement.getAttributeValue("messageChecksum");

    this.data = rootElement.getOpaque();
  }

  /**
   * Constructs a new <code>MessagePart</code>.
   *
   * @param partNo                  the number of the message part in the set of message parts for
   *                                the original message
   * @param totalParts              the total number of parts in the set of message parts for the
   *                                original message
   * @param messageId               the Universally Unique Identifier (UUID) used to uniquely
   *                                identify the original message
   * @param messageUser             the username identifying the user associated with the original
   *                                message
   * @param messageOrganisation     the organisation code identifying the organisation associated
   *                                with the original message
   * @param messageDevice           the device ID identifying the device the original message
   *                                originated from
   * @param messageType             the UUID identifying the type of the original message
   * @param messageTypeVersion      the version of the original message type
   * @param messageCorrelationId    the Universally Unique Identifier (UUID) used to correlate the
   *                                original message
   * @param messagePriority         the priority for the original message
   * @param messageCreated          the date and time the original message was created
   * @param messageDataHash         the hash of the unencrypted data for the original message
   * @param messageEncryptionScheme the encryption scheme used to secure the original message
   * @param messageEncryptionIV     the base-64 encoded initialisation vector for the encryption
   *                                scheme for the original message
   * @param messageChecksum         the checksum for the original message
   * @param data                    the binary data for the message part
   */
  public MessagePart(int partNo, int totalParts, String messageId, String messageUser,
      String messageOrganisation, String messageDevice, String messageType, int messageTypeVersion,
      String messageCorrelationId, Message.Priority messagePriority, Date messageCreated,
      String messageDataHash, EncryptionScheme messageEncryptionScheme, String messageEncryptionIV,
      String messageChecksum, byte[] data)
  {
    this.id = UUID.randomUUID().toString();
    this.partNo = partNo;
    this.totalParts = totalParts;
    this.sendAttempts = 0;
    this.status = Status.INITIALISED;
    this.messageId = messageId;
    this.messageUser = messageUser;
    this.messageOrganisation = messageOrganisation;
    this.messageDevice = messageDevice;
    this.messageType = messageType;
    this.messageTypeVersion = messageTypeVersion;
    this.messageCorrelationId = messageCorrelationId;
    this.messagePriority = messagePriority;
    this.messageCreated = messageCreated;
    this.messageDataHash = messageDataHash;
    this.messageEncryptionScheme = messageEncryptionScheme;
    this.messageEncryptionIV = messageEncryptionIV;
    this.messageChecksum = messageChecksum;
    this.data = data;
  }

  /**
   * Constructs a new <code>MessagePart</code>.
   *
   * @param id                      the Universally Unique Identifier (UUID) used to uniquely
   *                                identify the message part
   * @param partNo                  the number of the message part in the set of message parts for
   *                                the original message
   * @param totalParts              total number of parts in the set of message parts for the
   *                                original message
   * @param sendAttempts            the number of times that the sending of the message part was
   *                                attempted
   * @param downloadAttempts        the number of times that downloading of the message part was
   *                                attempted
   * @param status                  the message part status e.g. Initialised, Sending, etc
   * @param persisted               the date and time the message part was persisted
   * @param updated                 the date and time the message part was last updated
   * @param messageId               the Universally Unique Identifier (UUID) used to uniquely
   *                                identify the original message
   * @param messageUser             the username identifying the user associated with the original
   *                                message
   * @param messageOrganisation     the organisation code identifying the organisation associated
   *                                with the original message
   * @param messageDevice           the device ID identifying the device the original message
   *                                originated from
   * @param messageType             the UUID identifying the type of the original message
   * @param messageTypeVersion      the version of the original message type
   * @param messageCorrelationId    the Universally Unique Identifier (UUID) used to correlate the
   *                                original message
   * @param messagePriority         the priority for the original message
   * @param messageCreated          the date and time the original message was created
   * @param messageDataHash         the hash of the unencrypted data for the original message
   * @param messageEncryptionScheme the encryption scheme used to secure the original message
   * @param messageEncryptionIV     the base-64 encoded initialisation vector for the encryption
   *                                scheme for the original message
   * @param messageChecksum         the checksum for the original message
   * @param lockName                the name of the entity that has locked the message part for
   *                                processing
   * @param data                    the binary data for the message part
   */
  public MessagePart(String id, int partNo, int totalParts, int sendAttempts, int downloadAttempts,
      Status status, Date persisted, Date updated, String messageId, String messageUser,
      String messageOrganisation, String messageDevice, String messageType, int messageTypeVersion,
      String messageCorrelationId, Message.Priority messagePriority, Date messageCreated,
      String messageDataHash, EncryptionScheme messageEncryptionScheme, String messageEncryptionIV,
      String messageChecksum, String lockName, byte[] data)
  {
    this.id = id;
    this.partNo = partNo;
    this.totalParts = totalParts;
    this.sendAttempts = sendAttempts;
    this.downloadAttempts = downloadAttempts;
    this.status = status;
    this.persisted = persisted;
    this.updated = updated;
    this.messageId = messageId;
    this.messageUser = messageUser;
    this.messageOrganisation = messageOrganisation;
    this.messageDevice = messageDevice;
    this.messageType = messageType;
    this.messageTypeVersion = messageTypeVersion;
    this.messageCorrelationId = messageCorrelationId;
    this.messagePriority = messagePriority;
    this.messageCreated = messageCreated;
    this.messageDataHash = messageDataHash;
    this.messageEncryptionScheme = messageEncryptionScheme;
    this.messageEncryptionIV = messageEncryptionIV;
    this.messageChecksum = messageChecksum;
    this.lockName = lockName;
    this.data = data;
  }

  /**
   * The enumeration giving the possible statuses for a message part.
   */
  public enum Status
  {
    INITIALISED(0, "Initialised"), QUEUED_FOR_SENDING(1, "QueuedForSending"),
    SENDING(2, "Sending"), QUEUED_FOR_ASSEMBLY(3, "QueuedForAssembly"),
    ASSEMBLING(4, "Assembling"), QUEUED_FOR_DOWNLOAD(5, "QueuedForDownload"),
    DOWNLOADING(6, "Downloading"), ABORTED(7, "Aborted"), FAILED(8, "Failed"),
    UNKNOWN(-1, "Unknown");

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
          return Status.SENDING;

        case 3:
          return Status.QUEUED_FOR_ASSEMBLY;

        case 4:
          return Status.ASSEMBLING;

        case 5:
          return Status.QUEUED_FOR_DOWNLOAD;

        case 6:
          return Status.DOWNLOADING;

        case 7:
          return Status.ABORTED;

        case 8:
          return Status.FAILED;

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
   * Returns <code>true</code> if the WBXML document contains valid message part information or
   * <code>false</code> otherwise.
   *
   * @param document the WBXML document to validate
   *
   * @return <code>true</code> if the WBXML document contains valid message part information or
   *         <code>false</code> otherwise
   */
  public static boolean isValidWBXML(Document document)
  {
    Element rootElement = document.getRootElement();

    return rootElement.getName().equals("MessagePart")
        && !((!rootElement.hasAttribute("id")) || (!rootElement.hasAttribute("partNo"))
          || (!rootElement.hasAttribute("totalParts"))
            || (!rootElement.hasAttribute("sendAttempts"))
              || (!rootElement.hasAttribute("messageId"))
                || (!rootElement.hasAttribute("messageUser"))
                  || (!rootElement.hasAttribute("messageOrganisation"))
                    || (!rootElement.hasAttribute("messageDevice"))
                      || (!rootElement.hasAttribute("messageType"))
                        || (!rootElement.hasAttribute("messageTypeVersion"))
                          || (!rootElement.hasAttribute("messageCorrelationId"))
                            || (!rootElement.hasAttribute("messagePriority"))
                              || (!rootElement.hasAttribute("messageCreated"))
                                || (!rootElement.hasAttribute("messageDataHash"))
                                  || (!rootElement.hasAttribute("messageEncryptionScheme"))
                                    || (!rootElement.hasAttribute("messageEncryptionIV"))
                                      || (!rootElement.hasAttribute("messageChecksum")));
  }

  /**
   * Returns the binary data for the message part.
   *
   * @return the binary data for the message part
   */
  public byte[] getData()
  {
    return data;
  }

  /**
   * Returns the number of times that downloading of the message part was attempted.
   *
   * @return the number of times that downloading of the message part was attempted
   */
  public int getDownloadAttempts()
  {
    return downloadAttempts;
  }

  /**
   * Returns the Universally Unique Identifier (UUID) used to uniquely identify the message part.
   *
   * @return the Universally Unique Identifier (UUID) used to uniquely identify the message part
   */
  public String getId()
  {
    return id;
  }

  /**
   * Returns the name of the entity that has locked the message part for processing.
   *
   * @return the name of the entity that has locked the message part for processing
   */
  public String getLockName()
  {
    return lockName;
  }

  /**
   * Returns the checksum for the original message.
   *
   * @return the checksum for the original message
   */
  public String getMessageChecksum()
  {
    return messageChecksum;
  }

  /**
   * Returns the Universally Unique Identifier (UUID) used to correlate the original message.
   *
   * @return the Universally Unique Identifier (UUID) used to correlate the original message
   */
  public String getMessageCorrelationId()
  {
    return messageCorrelationId;
  }

  /**
   * Returns the date and time the original message was created.
   *
   * @return the date and time the original message was created
   */
  public Date getMessageCreated()
  {
    return messageCreated;
  }

  /**
   * Returns the hash of the unencrypted data for the original message.
   *
   * @return the hash of the unencrypted data for the original message
   */
  public String getMessageDataHash()
  {
    return messageDataHash;
  }

  /**
   * Returns the device ID identifying the device the original message originated from.
   *
   * @return the device ID identifying the device the original message originated from
   */
  public String getMessageDevice()
  {
    return messageDevice;
  }

  /**
   * Returns the base-64 encoded initialisation vector for the encryption scheme for the original
   * message.
   *
   * @return the base-64 encoded initialisation vector for the encryption scheme for the original
   *         message
   */
  public String getMessageEncryptionIV()
  {
    return messageEncryptionIV;
  }

  /**
   * Returns the encryption scheme used to secure the original message.
   *
   * @return the encryption scheme used to secure the original message
   */
  public EncryptionScheme getMessageEncryptionScheme()
  {
    return messageEncryptionScheme;
  }

  /**
   * Returns the Universally Unique Identifier (UUID) used to uniquely identify the original
   * message.
   *
   * @return the Universally Unique Identifier (UUID) used to uniquely identify the original message
   */
  public String getMessageId()
  {
    return messageId;
  }

  /**
   * Returns the organisation code identifying the organisation associated with the original
   * message.
   *
   * @return the organisation code identifying the organisation associated with the original message
   */
  public String getMessageOrganisation()
  {
    return messageOrganisation;
  }

  /**
   * Returns the priority for the original message.
   *
   * @return the priority for the original message
   */
  public Message.Priority getMessagePriority()
  {
    return messagePriority;
  }

  /**
   * Returns the UUID identifying the type of the original message.
   *
   * @return the UUID identifying the type of the original message
   */
  public String getMessageType()
  {
    return messageType;
  }

  /**
   * Returns the version of the original message type.
   *
   * @return the version of the original message type
   */
  public int getMessageTypeVersion()
  {
    return messageTypeVersion;
  }

  /**
   * Returns the username identifying the user associated with the original message.
   *
   * @return the username identifying the user associated with the original message
   */
  public String getMessageUser()
  {
    return messageUser;
  }

  /**
   * Returns the number of the message part in the set of message parts for the original message.
   *
   * @return the number of the message part in the set of message parts for the original message
   */
  public int getPartNo()
  {
    return partNo;
  }

  /**
   * Returns the date and time the message part was persisted.
   *
   * @return the date and time the message part was persisted
   */
  public Date getPersisted()
  {
    return persisted;
  }

  /**
   * Returns the number of times that the sending of the message part was attempted.
   *
   * @return the number of times that the sending of the message part was attempted
   */
  public int getSendAttempts()
  {
    return sendAttempts;
  }

  /**
   * Returns the message part status e.g. Initialised, Sending, etc.
   *
   * @return the message part status e.g. Initialised, Sending, etc
   */
  public Status getStatus()
  {
    return status;
  }

  /**
   * Returns the total number of parts in the set of message parts for the original message.
   *
   * @return the total number of parts in the set of message parts for the original message
   */
  public int getTotalParts()
  {
    return totalParts;
  }

  /**
   * Returns the date and time the message part was last updated.
   *
   * @return the date and time the message part was last updated
   */
  public Date getUpdated()
  {
    return updated;
  }

  /**
   * Returns <code>true</code> if the data for the original message is encrypted or
   * <code>false</code> otherwise.
   *
   * @return <code>true</code> if the data for the original message is encrypted or
   *         <code>false</code> otherwise
   */
  public boolean messageIsEncrypted()
  {
    return ((messageDataHash != null) && (messageDataHash.length() > 0));
  }

  /**
   * Set the binary data for the message part.
   *
   * @param data the binary data for the message part
   */
  public void setData(byte[] data)
  {
    this.data = data;
  }

  /**
   * Set the number of times that downloading of the message part was attempted.
   *
   * @param downloadAttempts the number of times that downloading of the message part was attempted
   */
  public void setDownloadAttempts(int downloadAttempts)
  {
    this.downloadAttempts = downloadAttempts;
  }

  /**
   * Set the Universally Unique Identifier (UUID) used to uniquely identify the message part.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the message part
   */
  public void setId(String id)
  {
    this.id = id;
  }

  /**
   * Set the name of the entity that has locked the message part for processing.
   *
   * @param lockName the name of the entity that has locked the message part for processing
   */
  public void setLockName(String lockName)
  {
    this.lockName = lockName;
  }

  /**
   * Set the checksum for the original message.
   *
   * @param messageChecksum the checksum for the original message
   */
  public void setMessageChecksum(String messageChecksum)
  {
    this.messageChecksum = messageChecksum;
  }

  /**
   * Set the Universally Unique Identifier (UUID) used to correlate the original message.
   *
   * @param messageCorrelationId the Universally Unique Identifier (UUID) used to correlate the
   *                             original message
   */
  public void setMessageCorrelationId(String messageCorrelationId)
  {
    this.messageCorrelationId = messageCorrelationId;
  }

  /**
   * Set the date and time the original message was created.
   *
   * @param messageCreated the date and time the original message was created
   */
  public void setMessageCreated(Date messageCreated)
  {
    this.messageCreated = messageCreated;
  }

  /**
   * Set the hash of the unencrypted data for the original message.
   *
   * @param messageDataHash the hash of the unencrypted data for the original message
   */
  public void setMessageDataHash(String messageDataHash)
  {
    this.messageDataHash = messageDataHash;
  }

  /**
   * Set the device ID identifying the device the original message originated from.
   *
   * @param messageDevice the device ID identifying the device the original message originated from
   */
  public void setMessageDevice(String messageDevice)
  {
    this.messageDevice = messageDevice;
  }

  /**
   * Set the Universally Unique Identifier (UUID) used to uniquely identify the original message.
   *
   * @param messageId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                  original message
   */
  public void setMessageId(String messageId)
  {
    this.messageId = messageId;
  }

  /**
   * Set the organisation code identifying the organisation associated with the original message.
   *
   * @param messageOrganisation the organisation code identifying the organisation associated with
   *                            the original message
   */
  public void setMessageOrganisation(String messageOrganisation)
  {
    this.messageOrganisation = messageOrganisation;
  }

  /**
   * Set the priority for the original message.
   *
   * @param messagePriority the priority for the original message
   */
  public void setMessagePriority(Message.Priority messagePriority)
  {
    this.messagePriority = messagePriority;
  }

  /**
   * Set the UUID identifying the type of the original message.
   *
   * @param messageType the UUID identifying the type of the original message
   */
  public void setMessageType(String messageType)
  {
    this.messageType = messageType;
  }

  /**
   * Set the version of the original message type.
   *
   * @param messageTypeVersion the version of the original message type
   */
  public void setMessageTypeVersion(int messageTypeVersion)
  {
    this.messageTypeVersion = messageTypeVersion;
  }

  /**
   * Set the username identifying the user associated with the original message.
   *
   * @param messageUser the username identifying the user associated with the original message
   */
  public void setMessageUser(String messageUser)
  {
    this.messageUser = messageUser;
  }

  /**
   * Set the number of the message part in the set of message parts for the original message.
   *
   * @param partNo the number of the message part in the set of message parts for the original
   *               message
   */
  public void setPartNo(int partNo)
  {
    this.partNo = partNo;
  }

  /**
   * Set the date and time the message part was persisted.
   *
   * @param persisted the date and time the message part was persisted
   */
  public void setPersisted(Date persisted)
  {
    this.persisted = persisted;
  }

  /**
   * Set the number of times that the sending of the message part was attempted.
   *
   * @param sendAttempts the number of times that the sending of the message part was attempted
   */
  public void setSendAttempts(int sendAttempts)
  {
    this.sendAttempts = sendAttempts;
  }

  /**
   * Set the message part status e.g. Initialised, Sending, etc.
   *
   * @param status the message part status e.g. Initialised, Sending, etc
   */
  public void setStatus(Status status)
  {
    this.status = status;
  }

  /**
   * Set the total number of parts in the set of message parts for the original message.
   *
   * @param totalParts the total number of parts in the set of message parts for the original
   *                   message
   */
  public void setTotalParts(int totalParts)
  {
    this.totalParts = totalParts;
  }

  /**
   * Set the date and time the message part was last updated.
   *
   * @param updated the date and time the message part was last updated
   */
  public void setUpdated(Date updated)
  {
    this.updated = updated;
  }

  /**
   * Set the base-64 encoded initialisation vector for the encryption scheme for the original
   * message.
   *
   * @param messageEncryptionIV the base-64 encoded initialisation vector for the encryption scheme
   *                            for the original message
   */
  public void setmessageEncryptionIV(String messageEncryptionIV)
  {
    this.messageEncryptionIV = messageEncryptionIV;
  }

  /**
   * Set the encryption scheme used to secure the original message.
   *
   * @param messageEncryptionScheme the encryption scheme used to secure the original message
   */
  public void setmessageEncryptionScheme(EncryptionScheme messageEncryptionScheme)
  {
    this.messageEncryptionScheme = messageEncryptionScheme;
  }

  /**
   * Returns the String representation of the message part.
   *
   * @return the String representation of the message part
   */
  @Override
  public String toString()
  {
    StringBuilder buffer = new StringBuilder("<MessagePart");

    buffer.append(" id=\"").append(id).append("\"");
    buffer.append(" partNo=\"").append(partNo).append("\"");
    buffer.append(" totalParts=\"").append(totalParts).append("\"");
    buffer.append(" sendAttempts=\"").append(sendAttempts).append("\"");
    buffer.append(" status=\"").append(status).append("\"");

    if (persisted != null)
    {
      buffer.append(" persisted=\"").append(ISO8601.fromDate(persisted)).append("\"");
    }
    else
    {
      buffer.append(" persisted=\"Never\"");
    }

    buffer.append(" messageId=\"").append(messageId).append("\"");
    buffer.append(" messageUser=\"").append(messageUser).append("\"");
    buffer.append(" messageOrganisation=\"").append(messageOrganisation).append("\"");
    buffer.append(" messageDevice=\"").append(messageDevice).append("\"");
    buffer.append(" messageType=\"").append(messageType).append("\"");
    buffer.append(" messageTypeVersion=\"").append(messageTypeVersion).append("\"");
    buffer.append(" messageCorrelationId=\"").append(messageCorrelationId).append("\"");
    buffer.append(" messagePriority=\"").append(messagePriority).append("\"");
    buffer.append(" messageCreated=\"").append(ISO8601.fromDate(messageCreated)).append("\"");
    buffer.append(" messageDataHash=\"").append(messageDataHash).append("\"");
    buffer.append(" messageEncryptionScheme=\"").append(messageEncryptionScheme.toString()).append(
        "\"");
    buffer.append(" messageEncryptionIV=\"").append(messageEncryptionIV).append("\"");
    buffer.append(" messageChecksum=\"").append(messageChecksum).append("\"");

    buffer.append(">").append(data.length).append(" bytes of data</MessagePart>");

    return buffer.toString();
  }

  /**
   * Returns the WBXML representation of the message part.
   *
   * @return the WBXML representation of the message part
   */
  public byte[] toWBXML()
  {
    Element rootElement = new Element("MessagePart");

    rootElement.setAttribute("id", id);
    rootElement.setAttribute("partNo", Integer.toString(partNo));
    rootElement.setAttribute("totalParts", Integer.toString(totalParts));
    rootElement.setAttribute("sendAttempts", Integer.toString(sendAttempts));
    rootElement.setAttribute("messageId", messageId);
    rootElement.setAttribute("messageUser", messageUser);
    rootElement.setAttribute("messageOrganisation", messageOrganisation);
    rootElement.setAttribute("messageDevice", messageDevice);
    rootElement.setAttribute("messageType", messageType);
    rootElement.setAttribute("messageTypeVersion", Integer.toString(messageTypeVersion));
    rootElement.setAttribute("messageCorrelationId", messageCorrelationId);
    rootElement.setAttribute("messagePriority", Integer.toString(messagePriority.getCode()));
    rootElement.setAttribute("messageCreated", ISO8601.fromDate(messageCreated));
    rootElement.setAttribute("messageDataHash", messageDataHash);
    rootElement.setAttribute("messageEncryptionScheme",
        Integer.toString(messageEncryptionScheme.getCode()));
    rootElement.setAttribute("messageEncryptionIV", messageEncryptionIV);
    rootElement.setAttribute("messageChecksum", messageChecksum);
    rootElement.addContent(data);

    Encoder encoder = new Encoder(new Document(rootElement));

    return encoder.getData();
  }
}

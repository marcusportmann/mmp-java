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

package guru.mmp.application.test;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.messaging.Message;
import guru.mmp.application.messaging.MessagePart;
import guru.mmp.common.crypto.CryptoUtils;
import guru.mmp.common.util.Base64;
import org.junit.Test;

import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>PojoTest</code> class contains the implementation of the JUnit
 * tests for the POJOs that form part of the messaging infrastructure.
 *
 * @author Marcus Portmann
 */
public class PojoTest
{
  private static final UUID MESSAGE_ID = UUID.randomUUID();
  private static final String USERNAME = "Administrator";
  private static final UUID DEVICE_ID = UUID.randomUUID();
  private static final UUID MESSAGE_TYPE_ID = UUID.randomUUID();
  private static final UUID MESSAGE_CORRELATION_ID = UUID.randomUUID();
  private static final Message.Priority MESSAGE_PRIORITY = Message.Priority.HIGH;
  private static final Message.Status MESSAGE_STATUS = Message.Status.INITIALISED;
  private static final Date MESSAGE_CREATED = new Date(0);
  private static final Date MESSAGE_PERSISTED = new Date(10000);
  private static final Date MESSAGE_UPDATED = new Date(80000);
  private static final Date MESSAGE_PART_PERSISTED = new Date(10000);
  private static final Date MESSAGE_PART_UPDATED = new Date(80000);
  private static final int SEND_ATTEMPTS = 1;
  private static final int PROCESS_ATTEMPTS = 2;
  private static final int DOWNLOAD_ATTEMPTS = 3;
  private static final String LOCK_NAME = "Lock Name";
  private static final Date LAST_PROCESSED = new Date();
  private static final String MESSAGE_DATA_HASH = "DataHash";
  private static final byte[] MESSAGE_DATA = "Message Data".getBytes();
  private static final byte[] MESSAGE_PART_DATA = "Message Part Data".getBytes();
  private static final String MESSAGE_ENCRYPTION_IV = Base64.encodeBytes(
      CryptoUtils.createRandomEncryptionIV(CryptoUtils.AES_BLOCK_SIZE));
  private static final UUID MESSAGE_PART_ID = UUID.randomUUID();
  private static final int PART_NO = 7;
  private static final int TOTAL_PARTS = 8;
  private static final MessagePart.Status MESSAGE_PART_STATUS = MessagePart.Status.DOWNLOADING;
  private static final String MESSAGE_CHECKSUM = "MessageChecksum";

  /**
   * Test the <code>MessagePart</code> POJO.
   */
  @Test
  public void messagePartTest()
  {
    MessagePart messagePart = new MessagePart(MESSAGE_PART_ID, PART_NO, TOTAL_PARTS, SEND_ATTEMPTS,
        DOWNLOAD_ATTEMPTS, MESSAGE_PART_STATUS, MESSAGE_PART_PERSISTED, MESSAGE_PART_UPDATED,
        MESSAGE_ID, USERNAME, DEVICE_ID, MESSAGE_TYPE_ID, MESSAGE_CORRELATION_ID, MESSAGE_PRIORITY,
        MESSAGE_CREATED, MESSAGE_DATA_HASH, MESSAGE_ENCRYPTION_IV, MESSAGE_CHECKSUM, LOCK_NAME,
        MESSAGE_PART_DATA);

    assertEquals(MESSAGE_PART_ID, messagePart.getId());
    assertEquals(PART_NO, messagePart.getPartNo());
    assertEquals(TOTAL_PARTS, messagePart.getTotalParts());
    assertEquals(SEND_ATTEMPTS, messagePart.getSendAttempts());
    assertEquals(DOWNLOAD_ATTEMPTS, messagePart.getDownloadAttempts());
    assertEquals(MESSAGE_PART_STATUS, messagePart.getStatus());
    assertEquals(MESSAGE_PART_PERSISTED, messagePart.getPersisted());
    assertEquals(MESSAGE_PART_UPDATED, messagePart.getUpdated());
    assertEquals(MESSAGE_ID, messagePart.getMessageId());
    assertEquals(USERNAME, messagePart.getMessageUsername());
    assertEquals(DEVICE_ID, messagePart.getMessageDeviceId());
    assertEquals(MESSAGE_TYPE_ID, messagePart.getMessageTypeId());
    assertEquals(MESSAGE_CORRELATION_ID, messagePart.getMessageCorrelationId());
    assertEquals(MESSAGE_PRIORITY, messagePart.getMessagePriority());
    assertEquals(MESSAGE_CREATED, messagePart.getMessageCreated());
    assertEquals(MESSAGE_DATA_HASH, messagePart.getMessageDataHash());
    assertEquals(MESSAGE_ENCRYPTION_IV, messagePart.getMessageEncryptionIV());
    assertEquals(MESSAGE_CHECKSUM, messagePart.getMessageChecksum());
    assertEquals(LOCK_NAME, messagePart.getLockName());
    assertEquals(MESSAGE_PART_DATA, messagePart.getData());
    assertEquals(true, messagePart.messageIsEncrypted());

    MessagePart anotherMessagePart = new MessagePart();

    anotherMessagePart.setId(MESSAGE_PART_ID);
    anotherMessagePart.setPartNo(PART_NO);
    anotherMessagePart.setTotalParts(TOTAL_PARTS);
    anotherMessagePart.setSendAttempts(SEND_ATTEMPTS);
    anotherMessagePart.setDownloadAttempts(DOWNLOAD_ATTEMPTS);
    anotherMessagePart.setStatus(MESSAGE_PART_STATUS);
    anotherMessagePart.setPersisted(MESSAGE_PART_PERSISTED);
    anotherMessagePart.setUpdated(MESSAGE_PART_UPDATED);
    anotherMessagePart.setMessageId(MESSAGE_ID);
    anotherMessagePart.setMessageUsername(USERNAME);
    anotherMessagePart.setMessageDeviceId(DEVICE_ID);
    anotherMessagePart.setMessageTypeId(MESSAGE_TYPE_ID);
    anotherMessagePart.setMessageCorrelationId(MESSAGE_CORRELATION_ID);
    anotherMessagePart.setMessagePriority(MESSAGE_PRIORITY);
    anotherMessagePart.setMessageCreated(MESSAGE_CREATED);
    anotherMessagePart.setMessageDataHash(MESSAGE_DATA_HASH);
    anotherMessagePart.setMessageEncryptionIV(MESSAGE_ENCRYPTION_IV);
    anotherMessagePart.setMessageChecksum(MESSAGE_CHECKSUM);
    anotherMessagePart.setLockName(LOCK_NAME);
    anotherMessagePart.setData(MESSAGE_PART_DATA);

    assertEquals(MESSAGE_PART_ID, anotherMessagePart.getId());
    assertEquals(PART_NO, anotherMessagePart.getPartNo());
    assertEquals(TOTAL_PARTS, anotherMessagePart.getTotalParts());
    assertEquals(SEND_ATTEMPTS, anotherMessagePart.getSendAttempts());
    assertEquals(DOWNLOAD_ATTEMPTS, anotherMessagePart.getDownloadAttempts());
    assertEquals(MESSAGE_PART_STATUS, anotherMessagePart.getStatus());
    assertEquals(MESSAGE_PART_PERSISTED, anotherMessagePart.getPersisted());
    assertEquals(MESSAGE_PART_UPDATED, anotherMessagePart.getUpdated());
    assertEquals(MESSAGE_ID, anotherMessagePart.getMessageId());
    assertEquals(USERNAME, anotherMessagePart.getMessageUsername());
    assertEquals(DEVICE_ID, anotherMessagePart.getMessageDeviceId());
    assertEquals(MESSAGE_TYPE_ID, anotherMessagePart.getMessageTypeId());
    assertEquals(MESSAGE_CORRELATION_ID, anotherMessagePart.getMessageCorrelationId());
    assertEquals(MESSAGE_PRIORITY, anotherMessagePart.getMessagePriority());
    assertEquals(MESSAGE_CREATED, anotherMessagePart.getMessageCreated());
    assertEquals(MESSAGE_DATA_HASH, anotherMessagePart.getMessageDataHash());
    assertEquals(MESSAGE_ENCRYPTION_IV, anotherMessagePart.getMessageEncryptionIV());
    assertEquals(MESSAGE_CHECKSUM, anotherMessagePart.getMessageChecksum());
    assertEquals(LOCK_NAME, anotherMessagePart.getLockName());
    assertEquals(MESSAGE_PART_DATA, anotherMessagePart.getData());
    assertEquals(true, anotherMessagePart.messageIsEncrypted());
  }

  /**
   * Test the <code>Message</code> POJO.
   */
  @Test
  public void messageTest()
  {
    Message message = new Message(MESSAGE_ID, USERNAME, DEVICE_ID, MESSAGE_TYPE_ID,
        MESSAGE_CORRELATION_ID, MESSAGE_PRIORITY, MESSAGE_STATUS, MESSAGE_CREATED,
        MESSAGE_PERSISTED, MESSAGE_UPDATED, SEND_ATTEMPTS, PROCESS_ATTEMPTS, DOWNLOAD_ATTEMPTS,
        LOCK_NAME, LAST_PROCESSED, MESSAGE_DATA, MESSAGE_DATA_HASH, MESSAGE_ENCRYPTION_IV);

    assertEquals(MESSAGE_ID, message.getId());
    assertEquals(USERNAME, message.getUsername());
    assertEquals(DEVICE_ID, message.getDeviceId());
    assertEquals(MESSAGE_TYPE_ID, message.getTypeId());
    assertEquals(MESSAGE_CORRELATION_ID, message.getCorrelationId());
    assertEquals(MESSAGE_PRIORITY, message.getPriority());
    assertEquals(MESSAGE_STATUS, message.getStatus());
    assertEquals(MESSAGE_CREATED, message.getCreated());
    assertEquals(MESSAGE_PERSISTED, message.getPersisted());
    assertEquals(MESSAGE_UPDATED, message.getUpdated());
    assertEquals(SEND_ATTEMPTS, message.getSendAttempts());
    assertEquals(PROCESS_ATTEMPTS, message.getProcessAttempts());
    assertEquals(DOWNLOAD_ATTEMPTS, message.getDownloadAttempts());
    assertEquals(LOCK_NAME, message.getLockName());
    assertEquals(LAST_PROCESSED, message.getLastProcessed());
    assertArrayEquals(MESSAGE_DATA, message.getData());
    assertEquals(MESSAGE_ENCRYPTION_IV, message.getEncryptionIV());
    assertEquals(MESSAGE_DATA_HASH, message.getDataHash());

    Message anotherMessage = new Message();

    anotherMessage.setId(MESSAGE_ID);
    anotherMessage.setUsername(USERNAME);
    anotherMessage.setDeviceId(DEVICE_ID);
    anotherMessage.setTypeId(MESSAGE_TYPE_ID);
    anotherMessage.setCorrelationId(MESSAGE_CORRELATION_ID);
    anotherMessage.setPriority(MESSAGE_PRIORITY);
    anotherMessage.setStatus(MESSAGE_STATUS);
    anotherMessage.setCreated(MESSAGE_CREATED);
    anotherMessage.setPersisted(MESSAGE_PERSISTED);
    anotherMessage.setUpdated(MESSAGE_UPDATED);
    anotherMessage.setSendAttempts(SEND_ATTEMPTS);
    anotherMessage.setProcessAttempts(PROCESS_ATTEMPTS);
    anotherMessage.setDownloadAttempts(DOWNLOAD_ATTEMPTS);
    anotherMessage.setLockName(LOCK_NAME);
    anotherMessage.setLastProcessed(LAST_PROCESSED);
    anotherMessage.setData(MESSAGE_DATA);
    anotherMessage.setDataHash(MESSAGE_DATA_HASH);
    anotherMessage.setEncryptionIV(MESSAGE_ENCRYPTION_IV);

    assertEquals(MESSAGE_ID, anotherMessage.getId());
    assertEquals(USERNAME, anotherMessage.getUsername());
    assertEquals(DEVICE_ID, anotherMessage.getDeviceId());
    assertEquals(MESSAGE_TYPE_ID, anotherMessage.getTypeId());
    assertEquals(MESSAGE_CORRELATION_ID, anotherMessage.getCorrelationId());
    assertEquals(MESSAGE_PRIORITY, anotherMessage.getPriority());
    assertEquals(MESSAGE_STATUS, anotherMessage.getStatus());
    assertEquals(MESSAGE_CREATED, anotherMessage.getCreated());
    assertEquals(MESSAGE_PERSISTED, anotherMessage.getPersisted());
    assertEquals(MESSAGE_UPDATED, anotherMessage.getUpdated());
    assertEquals(SEND_ATTEMPTS, anotherMessage.getSendAttempts());
    assertEquals(PROCESS_ATTEMPTS, anotherMessage.getProcessAttempts());
    assertEquals(DOWNLOAD_ATTEMPTS, anotherMessage.getDownloadAttempts());
    assertEquals(LOCK_NAME, anotherMessage.getLockName());
    assertEquals(LAST_PROCESSED, anotherMessage.getLastProcessed());
    assertArrayEquals(MESSAGE_DATA, anotherMessage.getData());
    assertEquals(MESSAGE_ENCRYPTION_IV, anotherMessage.getEncryptionIV());
    assertEquals(MESSAGE_DATA_HASH, anotherMessage.getDataHash());
  }
}

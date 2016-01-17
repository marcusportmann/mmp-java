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

package guru.mmp.application.messaging.messages;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.messaging.Message;
import guru.mmp.application.messaging.MessagingException;
import guru.mmp.application.messaging.WbxmlMessageData;
import guru.mmp.common.util.ISO8601;
import guru.mmp.common.wbxml.Document;
import guru.mmp.common.wbxml.Element;
import guru.mmp.common.wbxml.Encoder;

import java.util.Date;
import java.util.UUID;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>GetCodeCategoryRequestData</code> class manages the data for a
 * "Get Code Category Request" message.
 * <p/>
 * This is a synchronous message.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public class GetCodeCategoryRequestData extends WbxmlMessageData
{
  /**
   * The UUID for the "Get Code Category Request" message.
   */
  public static final UUID MESSAGE_TYPE_ID = UUID.fromString(
      "94d60eb6-a062-492d-b5e7-9fb1f05cf088");

  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the code category to
   * retrieve.
   */
  private UUID id;

  /**
   * The date and time the code category was last retrieved.
   */
  private Date lastRetrieved;

  /**
   * Should the codes for the code category be returned if the code category is current?
   */
  private boolean returnCodesIfCurrent;

  /**
   * Constructs a new <code>GetCodeCategoryRequestData</code>.
   */
  public GetCodeCategoryRequestData()
  {
    super(MESSAGE_TYPE_ID, Message.Priority.HIGH);
  }

  /**
   * Constructs a new <code>GetCodeCategoryRequestData</code>.
   *
   * @param id                   the Universally Unique Identifier (UUID) used to uniquely identify
   *                             the code category to retrieve
   * @param lastRetrieved        the date and time the code category was last retrieved
   * @param returnCodesIfCurrent should the codes for the code category be returned if the code
   *                             category is current
   */
  public GetCodeCategoryRequestData(UUID id, Date lastRetrieved, boolean returnCodesIfCurrent)
  {
    super(MESSAGE_TYPE_ID, Message.Priority.HIGH);

    this.id = id;
    this.lastRetrieved = lastRetrieved;
    this.returnCodesIfCurrent = returnCodesIfCurrent;
  }

  /**
   * Extract the message data from the WBXML data for a message.
   *
   * @param messageData the WBXML data for the message
   *
   * @return <code>true</code> if the message data was extracted successfully from the WBXML data or
   * <code>false</code> otherwise
   *
   * @throws MessagingException
   */
  public boolean fromMessageData(byte[] messageData)
    throws MessagingException
  {
    Element rootElement = parseWBXML(messageData).getRootElement();

    if (!rootElement.getName().equals("GetCodeCategoryRequest"))
    {
      return false;
    }

    if ((!rootElement.hasChild("Id"))
        || (!rootElement.hasChild("LastRetrieved"))
        || (!rootElement.hasChild("ReturnCodesIfCurrent")))
    {
      return false;
    }

    this.id = UUID.fromString(rootElement.getChildText("Id"));

    String lastRetrievedValue = rootElement.getChildText("LastRetrieved");

    if (lastRetrievedValue.contains("T"))
    {
      try
      {
        this.lastRetrieved = ISO8601.toDate(lastRetrievedValue);
      }
      catch (Throwable e)
      {
        throw new RuntimeException("Failed to parse the LastRetrieved ISO8601 timestamp ("
            + lastRetrievedValue + ") for" + " the \"Get Code Category Request\" message", e);
      }
    }
    else
    {
      this.lastRetrieved = new Date(Long.parseLong(lastRetrievedValue));
    }

    this.returnCodesIfCurrent = Boolean.parseBoolean(rootElement.getChildText(
        "ReturnCodesIfCurrent"));

    return true;
  }

  /**
   * Returns the Universally Unique Identifier (UUID) uniquely identifying the code category
   * to retrieve.
   *
   * @return the Universally Unique Identifier (UUID) uniquely identify the code category to
   * retrieve
   */
  public UUID getId()
  {
    return id;
  }

  /**
   * Returns the date and time the code category was last retrieved.
   *
   * @return the date and time the code category was last retrieved
   */
  public Date getLastRetrieved()
  {
    return lastRetrieved;
  }

  /**
   * Returns <code>true</code> if the codes for the code category be returned if the code category
   * is current or <code>false</code> otherwise.
   *
   * @return <code>true</code> if the codes for the code category be returned if the code category
   * is current or <code>false</code> otherwise
   */
  public boolean getReturnCodesIfCurrent()
  {
    return returnCodesIfCurrent;
  }

  /**
   * Set the Universally Unique Identifier (UUID) uniquely identifying the code category to
   * retrieve.
   *
   * @param id the Universally Unique Identifier (UUID) uniquely identifying the code category to
   *           retrieve
   */
  public void setId(UUID id)
  {
    this.id = id;
  }

  /**
   * Set the date and time the code category was last retrieved.
   *
   * @param lastRetrieved the date and time the code category was last retrieved
   */
  public void setLastRetrieved(Date lastRetrieved)
  {
    this.lastRetrieved = lastRetrieved;
  }

  /**
   * Set whether the codes for the code category should be returned if the code category is current.
   *
   * @param returnCodesIfCurrent <code>true</code> if the codes for the code category be returned
   *                             if the code category is current or <code>false</code> otherwise
   */
  public void setReturnCodesIfCurrent(boolean returnCodesIfCurrent)
  {
    this.returnCodesIfCurrent = returnCodesIfCurrent;
  }

  /**
   * Returns the WBXML data representation of the message data that will be sent as part of a
   * message.
   *
   * @return the WBXML data representation of the message data that will be sent as part of a
   * message
   *
   * @throws MessagingException
   */
  public byte[] toMessageData()
    throws MessagingException
  {
    Element rootElement = new Element("GetCodeCategoryRequest");

    rootElement.addContent(new Element("Id", id.toString()));
    rootElement.addContent(new Element("LastRetrieved", (lastRetrieved == null)
        ? ISO8601.now()
        : ISO8601.fromDate(lastRetrieved)));
    rootElement.addContent(new Element("ReturnCodesIfCurrent", String.valueOf(
        returnCodesIfCurrent)));

    Encoder encoder = new Encoder(new Document(rootElement));

    return encoder.getData();
  }
}

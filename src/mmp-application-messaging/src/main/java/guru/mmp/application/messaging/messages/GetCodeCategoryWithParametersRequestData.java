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

package guru.mmp.application.messaging.messages;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.messaging.Message;
import guru.mmp.application.messaging.MessagingException;
import guru.mmp.application.messaging.WbxmlMessageData;
import guru.mmp.common.util.ISO8601;
import guru.mmp.common.wbxml.Document;
import guru.mmp.common.wbxml.Element;
import guru.mmp.common.wbxml.Encoder;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>GetCodeCategoryWithParametersRequestData</code> class manages the data for a
 * "Get Code Category With Parameters Request" message.
 * <p/>
 * This is a synchronous message.
 *
 * @author Marcus Portmann
 */
public class GetCodeCategoryWithParametersRequestData extends WbxmlMessageData
{
  /**
   * The UUID for the "Get Code Category With Parameters Request" message.
   */
  public static final UUID MESSAGE_TYPE_ID = UUID.fromString(
      "3500a28a-6a2c-482b-b81f-a849c9c3ef79");

  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the code category to
   * retrieve.
   */
  private UUID id;

  /**
   * The date and time the code category was last retrieved.
   */
  private LocalDateTime lastRetrieved;

  /**
   * The parameters.
   */
  private Map<String, String> parameters;

  /**
   * Should the codes for the code category be returned if the code category is current?
   */
  private boolean returnCodesIfCurrent;

  /**
   * Constructs a new <code>GetCodeCategoryWithParametersRequestData</code>.
   */
  public GetCodeCategoryWithParametersRequestData()
  {
    super(MESSAGE_TYPE_ID, Message.Priority.HIGH);

    parameters = new HashMap<>();
  }

  /**
   * Constructs a new <code>GetCodeCategoryWithParametersRequestData</code>.
   *
   * @param id                   the Universally Unique Identifier (UUID) used to uniquely identify
   *                             the code category to retrieve
   * @param lastRetrieved        the date and time the code category was last retrieved
   * @param parameters           the parameters
   * @param returnCodesIfCurrent should the codes for the code category be returned if the code
   *                             category is current
   */
  public GetCodeCategoryWithParametersRequestData(UUID id, LocalDateTime lastRetrieved, Map<String,
      String> parameters, boolean returnCodesIfCurrent)
  {
    super(MESSAGE_TYPE_ID, Message.Priority.HIGH);

    this.id = id;
    this.lastRetrieved = lastRetrieved;
    this.parameters = parameters;
    this.returnCodesIfCurrent = returnCodesIfCurrent;
  }

  /**
   * Extract the message data from the WBXML data for a message.
   *
   * @param messageData the WBXML data for the message
   *
   * @return <code>true</code> if the message data was extracted successfully from the WBXML data or
   * <code>false</code> otherwise
   */
  public boolean fromMessageData(byte[] messageData)
    throws MessagingException
  {
    Element rootElement = parseWBXML(messageData).getRootElement();

    if (!rootElement.getName().equals("GetCodeCategoryWithParametersRequest"))
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
        this.lastRetrieved = ISO8601.toLocalDateTime(lastRetrievedValue);
      }
      catch (Throwable e)
      {
        throw new RuntimeException("Failed to parse the LastRetrieved ISO8601 timestamp ("
            + lastRetrievedValue + ") for" + " the \"Get Code Category Request\" message", e);
      }
    }
    else
    {
      this.lastRetrieved = LocalDateTime.ofInstant(Instant.ofEpochSecond(Long.parseLong(
          lastRetrievedValue)), ZoneId.systemDefault());
    }

    this.returnCodesIfCurrent = Boolean.parseBoolean(rootElement.getChildText(
        "ReturnCodesIfCurrent"));

    this.parameters = new HashMap<>();

    List<Element> parameterElements = rootElement.getChildren("Parameter");

    for (Element parameterElement : parameterElements)
    {
      String parameterName = parameterElement.getAttributeValue("name");
      String parameterValue = parameterElement.getAttributeValue("value");

      this.parameters.put(parameterName, parameterValue);
    }

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
  public LocalDateTime getLastRetrieved()
  {
    return lastRetrieved;
  }

  /**
   * Returns the parameters.
   *
   * @return the parameters
   */
  public Map<String, String> getParameters()
  {
    return parameters;
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
   * Returns the WBXML data representation of the message data that will be sent as part of a
   * message.
   *
   * @return the WBXML data representation of the message data that will be sent as part of a
   * message
   */
  public byte[] toMessageData()
    throws MessagingException
  {
    Element rootElement = new Element("GetCodeCategoryWithParametersRequest");

    rootElement.addContent(new Element("Id", id.toString()));
    rootElement.addContent(new Element("LastRetrieved",
        (lastRetrieved == null)
        ? ISO8601.now()
        : ISO8601.fromLocalDateTime(lastRetrieved)));
    rootElement.addContent(new Element("ReturnCodesIfCurrent", String.valueOf(
        returnCodesIfCurrent)));

    for (String parameterName : parameters.keySet())
    {
      String parameterValue = parameters.get(parameterName);

      Element parameterElement = new Element("Parameter");

      parameterElement.setAttribute("name", parameterName);
      parameterElement.setAttribute("value", parameterValue);

      rootElement.addContent(parameterElement);
    }

    Encoder encoder = new Encoder(new Document(rootElement));

    return encoder.getData();
  }
}

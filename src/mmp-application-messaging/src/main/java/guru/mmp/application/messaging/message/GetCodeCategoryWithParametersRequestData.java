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

package guru.mmp.application.messaging.message;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.messaging.Message;
import guru.mmp.application.messaging.MessagingException;
import guru.mmp.application.messaging.WbxmlMessageData;
import guru.mmp.common.util.ISO8601;
import guru.mmp.common.util.StringUtil;
import guru.mmp.common.wbxml.Document;
import guru.mmp.common.wbxml.Element;
import guru.mmp.common.wbxml.Encoder;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
  public static final String MESSAGE_TYPE = "3500a28a-6a2c-482b-b81f-a849c9c3ef79";

  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the code category to
   * retrieve.
   */
  private String id;

  /**
   * The date and time the code category was last retrieved.
   */
  private Date lastRetrieved;

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
    super(MESSAGE_TYPE, 1, Message.Priority.HIGH);

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
  public GetCodeCategoryWithParametersRequestData(String id, Date lastRetrieved,
      Map<String, String> parameters, boolean returnCodesIfCurrent)
  {
    super(MESSAGE_TYPE, 1, Message.Priority.HIGH);

    this.id = id;
    this.lastRetrieved = lastRetrieved;
    this.parameters = parameters;
    this.returnCodesIfCurrent = returnCodesIfCurrent;
  }

  /**
   * Add the parameter.
   *
   * @param name  the name of the parameter
   * @param value the value for the parameter
   */
  public void addParameter(String name, String value)
  {
    parameters.put(name, value);
  }

  /**
   * Extract the message data from the WBXML data for a message.
   *
   * @param messageType        the UUID identifying the type of message the message data is
   *                           associated with
   * @param messageTypeVersion the version of the message type the message data is associated with
   * @param messageData        the WBXML data for the message
   *
   * @return <code>true</code> if the message data was extracted successfully from the
   *         WBXML data or <code>false</code> otherwise
   *
   * @throws MessagingException
   */
  public boolean fromMessageData(String messageType, int messageTypeVersion, byte[] messageData)
    throws MessagingException
  {
    Element rootElement = parseWBXML(messageData).getRootElement();

    if (!rootElement.getName().equals("GetCodeCategoryWithParametersRequest"))
    {
      return false;
    }

    if ((!rootElement.hasChild("Id")) || (!rootElement.hasChild("LastRetrieved"))
        || (!rootElement.hasChild("ReturnCodesIfCurrent")))
    {
      return false;
    }

    this.id = rootElement.getChildText("Id");

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
            + lastRetrievedValue + ") for the \"Get Code Category Request\" message", e);
      }
    }
    else
    {
      this.lastRetrieved = new Date(Long.parseLong(lastRetrievedValue));
    }

    this.returnCodesIfCurrent =
      Boolean.parseBoolean(rootElement.getChildText("ReturnCodesIfCurrent"));

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
   *         retrieve
   */
  public String getId()
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
   *         is current or <code>false</code> otherwise
   */
  public boolean getReturnCodesIfCurrent()
  {
    return returnCodesIfCurrent;
  }

  /**
   * Remove the parameter.
   *
   * @param name the name of the parameter to remove
   */
  public void removeParameter(String name)
  {
    parameters.remove(name);
  }

  /**
   * Set the Universally Unique Identifier (UUID) uniquely identifying the code category to
   * retrieve.
   *
   * @param id the Universally Unique Identifier (UUID) uniquely identifying the code category to
   *           retrieve
   */
  public void setId(String id)
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
   * Set the parameters.
   *
   * @param parameters the parameters
   */
  public void setParameters(Map<String, String> parameters)
  {
    this.parameters = parameters;
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
   *         message
   *
   * @throws MessagingException
   */
  public byte[] toMessageData()
    throws MessagingException
  {
    Element rootElement = new Element("GetCodeCategoryWithParametersRequest");

    rootElement.addContent(new Element("Id", StringUtil.notNull(id)));
    rootElement.addContent(new Element("LastRetrieved", (lastRetrieved == null)
        ? ISO8601.now()
        : ISO8601.fromDate(lastRetrieved)));
    rootElement.addContent(new Element("ReturnCodesIfCurrent",
        String.valueOf(returnCodesIfCurrent)));

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

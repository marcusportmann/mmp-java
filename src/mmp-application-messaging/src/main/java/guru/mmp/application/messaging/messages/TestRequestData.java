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
import guru.mmp.common.util.StringUtil;
import guru.mmp.common.wbxml.Document;
import guru.mmp.common.wbxml.Element;
import guru.mmp.common.wbxml.Encoder;

import java.util.UUID;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>TestRequestData</code> class manages the data for a
 * "Test Request" message.
 * <p/>
 * This is a synchronous message.
 *
 * @author Marcus Portmann
 */
public class TestRequestData extends WbxmlMessageData
{
  /**
   * The UUID for the "Test Request" message.
   */
  public static final UUID MESSAGE_TYPE_ID = UUID.fromString(
      "a589dc87-2328-4a9b-bdb6-970e55ca2323");

  /**
   * The test value.
   */
  private String testValue;

  /**
   * Constructs a new <code>TestRequestData</code>.
   */
  public TestRequestData()
  {
    super(MESSAGE_TYPE_ID, Message.Priority.HIGH);
  }

  /**
   * Constructs a new <code>TestRequestData</code>.
   *
   * @param testValue the test value
   */
  public TestRequestData(String testValue)
  {
    super(MESSAGE_TYPE_ID, Message.Priority.HIGH);

    this.testValue = testValue;
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
    Document document = parseWBXML(messageData);

    Element rootElement = document.getRootElement();

    if (!rootElement.getName().equals("TestRequest"))
    {
      return false;
    }

    if (!rootElement.hasChild("TestValue"))
    {
      return false;
    }

    this.testValue = rootElement.getChildText("TestValue");

    return true;
  }

  /**
   * Returns the test value.
   *
   * @return the test value
   */
  public String getTestValue()
  {
    return testValue;
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
    Element rootElement = new Element("TestRequest");

    rootElement.addContent(new Element("TestValue", StringUtil.notNull(testValue)));

    Document document = new Document(rootElement);

    Encoder encoder = new Encoder(document);

    return encoder.getData();
  }
}

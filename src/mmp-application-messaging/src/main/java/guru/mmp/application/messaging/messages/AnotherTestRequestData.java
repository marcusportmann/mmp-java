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
import guru.mmp.common.util.StringUtil;
import guru.mmp.common.wbxml.Document;
import guru.mmp.common.wbxml.Element;
import guru.mmp.common.wbxml.Encoder;

import java.util.UUID;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>AnotherTestRequestData</code> class manages the data for a
 * "Another Test Request" message.
 * <p/>
 * This is an asynchronous message.
 *
 * @author Marcus Portmann
 */
public class AnotherTestRequestData extends WbxmlMessageData
{
  /**
   * The UUID for the "Another Test Request" message.
   */
  public static final UUID MESSAGE_TYPE_ID = UUID.fromString(
      "e9918051-8ebc-48f1-bad7-13c59b550e1a");

  /**
   * The test data.
   */
  private byte[] testData;

  /**
   * The test value.
   */
  private String testValue;

  /**
   * Constructs a new <code>AnotherTestRequestData</code>.
   */
  public AnotherTestRequestData()
  {
    super(MESSAGE_TYPE_ID, Message.Priority.HIGH);
  }

  /**
   * Constructs a new <code>AnotherTestRequestData</code>.
   *
   * @param testValue the test value
   * @param testData  the test data
   */
  public AnotherTestRequestData(String testValue, byte[] testData)
  {
    super(MESSAGE_TYPE_ID, Message.Priority.HIGH);

    this.testValue = testValue;
    this.testData = testData;
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
    Document document = parseWBXML(messageData);

    Element rootElement = document.getRootElement();

    if (!rootElement.getName().equals("AnotherTestRequest"))
    {
      return false;
    }

    if ((!rootElement.hasChild("TestValue")) || (!rootElement.hasChild("TestData")))
    {
      return false;
    }

    this.testValue = rootElement.getChildText("TestValue");
    this.testData = rootElement.getChildOpaque("TestData");

    return true;
  }

  /**
   * Returns the test data.
   *
   * @return the test data
   */
  public byte[] getTestData()
  {
    return testData;
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
   *
   * @throws MessagingException
   */
  public byte[] toMessageData()
    throws MessagingException
  {
    Element rootElement = new Element("AnotherTestRequest");

    rootElement.addContent(new Element("TestValue", StringUtil.notNull(testValue)));
    rootElement.addContent(new Element("TestData", testData));

    Document document = new Document(rootElement);

    Encoder encoder = new Encoder(document);

    return encoder.getData();
  }
}

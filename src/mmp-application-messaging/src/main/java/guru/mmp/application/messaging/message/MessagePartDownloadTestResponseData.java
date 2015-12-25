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
import guru.mmp.common.wbxml.Document;
import guru.mmp.common.wbxml.Element;
import guru.mmp.common.wbxml.Encoder;

//~--- JDK imports ------------------------------------------------------------

import java.util.UUID;

/**
 * The <code>MessagePartDownloadTestResponseData</code> class manages the data for a
 * "Message Part Download Test Response" message.
 * <p/>
 * This is an asynchronous message.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public class MessagePartDownloadTestResponseData extends WbxmlMessageData
{
  /**
   * The UUID for the "Message Part Download Test Response" message.
   */
  public static final UUID MESSAGE_TYPE_ID =
    UUID.fromString("132c818c-49cf-42f2-b939-50360b847fed");

  /**
   * The test data.
   */
  private byte[] testData;

  /**
   * Constructs a new <code>MessagePartDownloadTestResponseData</code>.
   */
  public MessagePartDownloadTestResponseData()
  {
    super(MESSAGE_TYPE_ID, Message.Priority.HIGH);
  }

  /**
   * Constructs a new <code>MessagePartDownloadTestResponseData</code>.
   *
   * @param testData the test data
   */
  public MessagePartDownloadTestResponseData(byte[] testData)
  {
    super(MESSAGE_TYPE_ID, Message.Priority.HIGH);

    this.testData = testData;
  }

  /**
   * Extract the message data from the WBXML data for a message.
   *
   * @param messageData the WBXML data for the message
   *
   * @return <code>true</code> if the message data was extracted successfully from the WBXML data or
   *         <code>false</code> otherwise
   *
   * @throws MessagingException
   */
  public boolean fromMessageData(byte[] messageData)
    throws MessagingException
  {
    Document document = parseWBXML(messageData);

    Element rootElement = document.getRootElement();

    if (!rootElement.getName().equals("MessagePartDownloadTestResponse"))
    {
      return false;
    }

    if (!rootElement.hasChild("TestData"))
    {
      return false;
    }

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
   * Set the test data.
   *
   * @param testData the test data
   */
  public void setTestValue(byte[] testData)
  {
    this.testData = testData;
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
    Element rootElement = new Element("MessagePartDownloadTestResponse");

    rootElement.addContent(new Element("TestData", testData));

    Document document = new Document(rootElement);

    Encoder encoder = new Encoder(document);

    return encoder.getData();
  }
}

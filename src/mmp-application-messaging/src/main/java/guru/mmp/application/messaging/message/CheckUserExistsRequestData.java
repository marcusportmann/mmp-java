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
import guru.mmp.common.util.StringUtil;
import guru.mmp.common.wbxml.Document;
import guru.mmp.common.wbxml.Element;
import guru.mmp.common.wbxml.Encoder;

/**
 * The <code>CheckUserExistsRequestData</code> class manages the data for a
 * "Check User Exists Request" message.
 * <p/>
 * This is a synchronous message.
 *
 * @author Marcus Portmann
 */
public class CheckUserExistsRequestData extends WbxmlMessageData
{
  /**
   * The UUID for the "Check User Exists Request" message.
   */
  public static final String MESSAGE_TYPE = "cc005e6a-b01b-48eb-98a0-026297be69f3";

  /**
   * The organisation code identifying the organisation the user is associated with.
   */
  private String organisation;

  /**
   * The username identifying the user.
   */
  private String user;

  /**
   * Constructs a new <code>CheckUserExistsRequestData</code>.
   */
  public CheckUserExistsRequestData()
  {
    super(MESSAGE_TYPE, 1, Message.Priority.HIGH);
  }

  /**
   * Constructs a new <code>CheckUserExistsRequestData</code>.
   *
   * @param user         the username identifying the user
   * @param organisation the organisation code identifying the organisation the user is associated
   *                     with
   */
  public CheckUserExistsRequestData(String user, String organisation)
  {
    super(MESSAGE_TYPE, 1, Message.Priority.HIGH);

    this.user = user;
    this.organisation = organisation;
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
    Document document = parseWBXML(messageData);

    Element rootElement = document.getRootElement();

    if (!rootElement.getName().equals("CheckUserExistsRequest"))
    {
      return false;
    }

    if (!rootElement.hasChild("User"))
    {
      return false;
    }

    this.user = rootElement.getChildText("User");

    if (rootElement.hasChild("Organisation"))
    {
      this.organisation = rootElement.getChildText("Organisation");
    }
    else
    {
      this.organisation = "MMP";
    }

    return true;
  }

  /**
   * Returns the organisation code identifying the organisation the user is associated with.
   *
   * @return the organisation code identifying the organisation the user is associated with
   */
  public String getOrganisation()
  {
    return organisation;
  }

  /**
   * Returns the username identifying the user.
   *
   * @return the username identifying the user
   */
  public String getUser()
  {
    return user;
  }

  /**
   * Set the organisation code identifying the organisation the user is associated with.
   *
   * @param organisation the organisation code identifying the organisation the user is associated
   *        with
   */
  public void setOrganisation(String organisation)
  {
    this.organisation = organisation;
  }

  /**
   * Set the username identifying the user.
   *
   * @param user the username identifying the user
   */
  public void setUser(String user)
  {
    this.user = user;
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
    Element rootElement = new Element("CheckUserExistsRequest");

    rootElement.addContent(new Element("User", StringUtil.notNull(user)));
    rootElement.addContent(new Element("Organisation", StringUtil.notNull(organisation)));

    Document document = new Document(rootElement);

    Encoder encoder = new Encoder(document);

    return encoder.getData();
  }
}

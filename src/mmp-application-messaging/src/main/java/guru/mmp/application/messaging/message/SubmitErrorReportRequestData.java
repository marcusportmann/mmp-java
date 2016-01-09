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

package guru.mmp.application.messaging.message;

import guru.mmp.application.messaging.Message;
import guru.mmp.application.messaging.MessagingException;
import guru.mmp.application.messaging.WbxmlMessageData;
import guru.mmp.common.util.ISO8601;
import guru.mmp.common.util.StringUtil;
import guru.mmp.common.wbxml.Document;
import guru.mmp.common.wbxml.Element;
import guru.mmp.common.wbxml.Encoder;

import java.util.Date;
import java.util.UUID;

/**
 * The <code>SubmitErrorReportRequestData</code> class manages the data for a
 * "Submit Error Report Request" message.
 * <p/>
 * This is an asynchronous message.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public class SubmitErrorReportRequestData
  extends WbxmlMessageData
{
  /**
   * The UUID for the "Submit Error Report Request" message.
   */
  public static final UUID MESSAGE_TYPE_ID = UUID.fromString(
    "ff638c33-b4f1-4e79-804c-9560da2543d6");

  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the application that
   * generated the error report.
   */
  private UUID applicationId;

  /**
   * The version of the application that generated the error report.
   */
  private int applicationVersion;

  /**
   * The data associated with the error report e.g. the application XML.
   */
  private byte[] data;

  /**
   * The description of the error.
   */
  private String description;

  /**
   * The error detail e.g. a stack trace.
   */
  private String detail;

  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the device the error report
   * originated from.
   */
  private UUID deviceId;

  /**
   * The feedback provided by the user for the error.
   */
  private String feedback;

  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the error report.
   */
  private UUID id;

  /**
   * The date and time the error report was created.
   */
  private Date when;

  /**
   * The username identifying the user associated with the error report.
   */
  private String who;

  /**
   * Constructs a new <code>SubmitErrorReportRequestData</code>.
   */
  public SubmitErrorReportRequestData()
  {
    super(MESSAGE_TYPE_ID, Message.Priority.HIGH);
  }

  /**
   * Constructs a new <code>SubmitErrorReportRequestData</code>.
   *
   * @param id                 the Universally Unique Identifier (UUID) used to uniquely identify
   *                           the error report
   * @param applicationId      the Universally Unique Identifier (UUID) used to uniquely identify
   *                           the application that generated the error report
   * @param applicationVersion the version of the application that generated the error report
   * @param description        the description of the error
   * @param detail             the error detail e.g. a stack trace
   * @param feedback           the feedback provided by the user for the error
   * @param when               the date and time the error report was created
   * @param who                the username identifying the user associated with the error report
   * @param deviceId           the Universally Unique Identifier (UUID) used to uniquely identify
   *                           the device the error report originated from
   * @param data               the data associated with the error report e.g. the application XML
   */
  public SubmitErrorReportRequestData(
    UUID id, UUID applicationId, int applicationVersion, String description, String detail,
    String feedback, Date when, String who, UUID deviceId, byte[] data)
  {
    super(MESSAGE_TYPE_ID, Message.Priority.HIGH);

    this.id = id;
    this.applicationId = applicationId;
    this.applicationVersion = applicationVersion;
    this.description = description;
    this.detail = detail;
    this.feedback = feedback;
    this.when = when;
    this.who = who;
    this.deviceId = deviceId;
    this.data = data;
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

    if (!rootElement.getName().equals("SubmitErrorReportRequest"))
    {
      return false;
    }

    if ((!rootElement.hasChild("Id")) || (!rootElement.hasChild("ApplicationId")) ||
      (!rootElement.hasChild("ApplicationVersion")) || (!rootElement.hasChild("Description")) ||
      (!rootElement.hasChild("Detail")) || (!rootElement.hasChild("Feedback")) ||
      (!rootElement.hasChild("When")) || (!rootElement.hasChild("Who")) || (!rootElement.hasChild(
      "DeviceId")) || (!rootElement.hasChild("Data")))
    {
      return false;
    }

    this.id = UUID.fromString(rootElement.getChildText("Id"));
    this.applicationId = UUID.fromString(rootElement.getChildText("ApplicationId"));
    this.applicationVersion = Integer.parseInt(rootElement.getChildText("ApplicationVersion"));
    this.description = rootElement.getChildText("Description");
    this.detail = rootElement.getChildText("Detail");
    this.feedback = rootElement.getChildText("Feedback");

    String whenValue = rootElement.getChildText("When");

    if (whenValue.contains("T"))
    {
      try
      {
        this.when = ISO8601.toDate(whenValue);
      }
      catch (Throwable e)
      {
        throw new RuntimeException(
          "Failed to parse the When ISO8601 timestamp (" + whenValue + ") for the \"Submit " +
            "Error Report Request\" message", e);
      }
    }
    else
    {
      this.when = new Date(Long.parseLong(whenValue));
    }

    this.who = rootElement.getChildText("Who");
    this.deviceId = UUID.fromString(rootElement.getChildText("DeviceId"));
    this.data = rootElement.getChildOpaque("Data");

    return true;
  }

  /**
   * Returns the Universally Unique Identifier (UUID) used to uniquely identify the application
   * that generated the error report.
   *
   * @return the Universally Unique Identifier (UUID) used to uniquely identify the application
   * that generated the error report
   */
  public UUID getApplicationId()
  {
    return applicationId;
  }

  /**
   * Returns the version of the application that generated the error report.
   *
   * @return the version of the application that generated the error report
   */
  public int getApplicationVersion()
  {
    return applicationVersion;
  }

  /**
   * Returns the data associated with the error report e.g. the application XML.
   *
   * @return the data associated with the error report e.g. the application XML
   */
  public byte[] getData()
  {
    return data;
  }

  /**
   * Returns the description of the error.
   *
   * @return the description of the error
   */
  public String getDescription()
  {
    return description;
  }

  /**
   * Returns the error detail e.g. a stack trace.
   *
   * @return the error detail e.g. a stack trace
   */
  public String getDetail()
  {
    return detail;
  }

  /**
   * Returns the Universally Unique Identifier (UUID) used to uniquely identify the device the error
   * report originated from.
   *
   * @return the Universally Unique Identifier (UUID) used to uniquely identify the device the error
   * report originated from
   */
  public UUID getDeviceId()
  {
    return deviceId;
  }

  /**
   * Returns the feedback provided by the user for the error.
   *
   * @return the feedback provided by the user for the error
   */
  public String getFeedback()
  {
    return feedback;
  }

  /**
   * Returns the Universally Unique Identifier (UUID) used to uniquely identify the error report.
   *
   * @return the Universally Unique Identifier (UUID) used to uniquely identify the error report
   */
  public UUID getId()
  {
    return id;
  }

  /**
   * Returns the date and time the error report was created.
   *
   * @return the date and time the error report was created
   */
  public Date getWhen()
  {
    return when;
  }

  /**
   * Returns the username identifying the user associated with the error report.
   *
   * @return the username identifying the user associated with the error report
   */
  public String getWho()
  {
    return who;
  }

  /**
   * Set the Universally Unique Identifier (UUID) used to uniquely identify the application that
   * generated the error report.
   *
   * @param applicationId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                      application that generated the error report
   */
  public void setApplicationId(UUID applicationId)
  {
    this.applicationId = applicationId;
  }

  /**
   * Set the version of the application that generated the error report.
   *
   * @param applicationVersion the version of the application that generated the error report
   */
  public void setApplicationVersion(int applicationVersion)
  {
    this.applicationVersion = applicationVersion;
  }

  /**
   * Set the data associated with the error report e.g. the application XML.
   *
   * @param data the data associated with the error report e.g. the application XML
   */
  public void setData(byte[] data)
  {
    this.data = data;
  }

  /**
   * Set the description of the error.
   *
   * @param description the description of the error
   */
  public void setDescription(String description)
  {
    this.description = description;
  }

  /**
   * Set the error detail e.g. a stack trace.
   *
   * @param detail the error detail e.g. a stack trace
   */
  public void setDetail(String detail)
  {
    this.detail = detail;
  }

  /**
   * Set the Universally Unique Identifier (UUID) used to uniquely identify the device the error
   * report originated from.
   *
   * @param device the Universally Unique Identifier (UUID) used to uniquely identify the device the
   *               error report originated from
   */
  public void setDeviceId(UUID device)
  {
    this.deviceId = deviceId;
  }

  /**
   * Set the feedback provided by the user for the error.
   *
   * @param feedback the feedback provided by the user for the error
   */
  public void setFeedback(String feedback)
  {
    this.feedback = feedback;
  }

  /**
   * Set the Universally Unique Identifier (UUID) used to uniquely identify the error report.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the error report
   */
  public void setId(UUID id)
  {
    this.id = id;
  }

  /**
   * Set the date and time the error report was created.
   *
   * @param when the date and time the error report was created
   */
  public void setWhen(Date when)
  {
    this.when = when;
  }

  /**
   * Set the username identifying the user associated with the error report.
   *
   * @param who the username identifying the user associated with the error report
   */
  public void setWho(String who)
  {
    this.who = who;
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
    Element rootElement = new Element("SubmitErrorReportRequest");

    rootElement.addContent(new Element("Id", id.toString()));
    rootElement.addContent(new Element("ApplicationId", applicationId.toString()));
    rootElement.addContent(new Element("ApplicationVersion", String.valueOf(applicationVersion)));
    rootElement.addContent(new Element("Description", StringUtil.notNull(description)));
    rootElement.addContent(new Element("Detail", StringUtil.notNull(detail)));
    rootElement.addContent(new Element("Feedback", StringUtil.notNull(feedback)));
    rootElement.addContent(
      new Element("When", (when == null) ? ISO8601.now() : ISO8601.fromDate(when)));
    rootElement.addContent(new Element("Who", StringUtil.notNull(who)));
    rootElement.addContent(new Element("DeviceId", deviceId.toString()));
    rootElement.addContent(new Element("Data", (data != null) ? data : new byte[0]));

    Document document = new Document(rootElement);

    Encoder encoder = new Encoder(document);

    return encoder.getData();
  }
}

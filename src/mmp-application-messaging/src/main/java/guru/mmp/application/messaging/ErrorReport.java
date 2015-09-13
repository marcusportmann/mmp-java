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

//~--- JDK imports ------------------------------------------------------------

import java.io.Serializable;
import java.util.Date;

/**
 * The <code>ErrorReport</code> class holds the information for an error report that was submitted
 * using the messaging infrastructure.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public class ErrorReport
  implements Serializable
{
  private static final long serialVersionUID = 1000000;

  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the application that
   * generated the error report.
   */
  private String applicationId;

  /**
   * The version of the application that generated the error report.
   */
  private int applicationVersion;

  /**
   * The date and time the error report was created.
   */
  private Date created;

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
   * The device ID identifying the device the error report originated from.
   */
  private String device;

  /**
   * The feedback provided by the user for the error.
   */
  private String feedback;

  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the error report.
   */
  private String id;

  /**
   * The username identifying the user associated with the error report.
   */
  private String who;

  /**
   * Constructs a new <code>ErrorReport</code>.
   *
   * @param id                 the Universally Unique Identifier (UUID) used to uniquely identify
   *                           the error report
   * @param applicationId      the Universally Unique Identifier (UUID) used to uniquely identify
   *                           the application that generated the error report
   * @param applicationVersion the version of the application that generated the error report
   * @param description        the description of the error
   * @param detail             the error detail e.g. a stack trace
   * @param feedback           the feedback provided by the user for the error
   * @param created            the date and time the error report was created
   * @param who                the username identifying the user associated with the error report
   * @param device             the device ID identifying the device the error report originated
   *                           from
   * @param data               the data associated with the error report e.g. the application XML
   */
  public ErrorReport(String id, String applicationId, int applicationVersion, String description,
      String detail, String feedback, Date created, String who, String device, byte[] data)
  {
    this.id = id;
    this.applicationId = applicationId;
    this.applicationVersion = applicationVersion;
    this.description = description;
    this.detail = detail;
    this.feedback = feedback;
    this.created = created;
    this.who = who;
    this.device = device;
    this.data = data;
  }

  /**
   * Returns the Universally Unique Identifier (UUID) used to uniquely identify the application
   * that generated the error report.
   *
   * @return the Universally Unique Identifier (UUID) used to uniquely identify the application
   *         that generated the error report
   */
  public String getApplicationId()
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
   * Returns the date and time the error report was created.
   *
   * @return the date and time the error report was created
   */
  public Date getCreated()
  {
    return created;
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
   * Returns the device ID identifying the device the error report originated from.
   *
   * @return the device ID identifying the device the error report originated from
   */
  public String getDevice()
  {
    return device;
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
  public String getId()
  {
    return id;
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
  public void setApplicationId(String applicationId)
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
   * Set the date and time the error report was created.
   *
   * @param created the date and time the error report was created
   */
  public void setCreated(Date created)
  {
    this.created = created;
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
   * Set the device ID identifying the device the error report originated from.
   *
   * @param device the device ID identifying the device the error report originated from
   */
  public void setDevice(String device)
  {
    this.device = device;
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
  public void setId(String id)
  {
    this.id = id;
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
}

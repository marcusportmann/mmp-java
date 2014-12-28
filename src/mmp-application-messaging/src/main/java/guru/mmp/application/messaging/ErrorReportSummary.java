/*
 * Copyright 2014 Marcus Portmann
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
 * The <code>ErrorReportSummary</code> class holds the summary information for an error report that
 * was submitted using the messaging infrastructure.
 *
 * @author Marcus Portmann
 */
public class ErrorReportSummary
  implements Serializable
{
  private static final long serialVersionUID = 1000000;

  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the application that
   * generated the error report.
   */
  private String applicationId;

  /**
   * The name of the application that generated the error report.
   */
  private String applicationName;

  /**
   * The version of the application that generated the error report.
   */
  private int applicationVersion;

  /**
   * The date and time the error report was created.
   */
  private Date created;

  /**
   * The device ID identifying the device the error report originated from.
   */
  private String device;

  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the error report.
   */
  private String id;

  /**
   * The username identifying the user associated with the error report.
   */
  private String who;

  /**
   * Constructs a new <code>ErrorReportSummary</code>.
   *
   * @param id                 the Universally Unique Identifier (UUID) used to uniquely identify
   *                           the error report
   * @param applicationId      the Universally Unique Identifier (UUID) used to uniquely identify
   *                           the application that generated the error report
   * @param applicationName    the name of the application that generated the error report
   * @param applicationVersion the version of the application that generated the error report
   * @param created            the date and time the error report was created
   * @param who                the username identifying the user associated with the error report
   * @param device             the device ID identifying the device the error report originated
   *                           from
   */
  public ErrorReportSummary(String id, String applicationId, String applicationName,
      int applicationVersion, Date created, String who, String device)
  {
    this.id = id;
    this.applicationId = applicationId;
    this.applicationVersion = applicationVersion;
    this.applicationName = applicationName;
    this.created = created;
    this.who = who;
    this.device = device;
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
   * Returns the name of the application that generated the error report.
   *
   * @return the name of the application that generated the error report
   */
  public String getApplicationName()
  {
    return applicationName;
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
   * Returns the device ID identifying the device the error report originated from.
   *
   * @return the device ID identifying the device the error report originated from
   */
  public String getDevice()
  {
    return device;
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
   * Set the name of the application that generated the error report.
   *
   * @param applicationName the name of the application that generated the error report
   */
  public void setApplicationName(String applicationName)
  {
    this.applicationName = applicationName;
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
   * Set the device ID identifying the device the error report originated from.
   *
   * @param device the device ID identifying the device the error report originated from
   */
  public void setDevice(String device)
  {
    this.device = device;
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

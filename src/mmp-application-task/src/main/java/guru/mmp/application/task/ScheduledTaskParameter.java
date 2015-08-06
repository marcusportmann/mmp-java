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

package guru.mmp.application.task;

//~--- JDK imports ------------------------------------------------------------

import java.io.Serializable;

/**
 * The <code>ScheduledTaskParameter</code> class holds the information for a scheduled task
 * parameter.
 *
 * @author Marcus Portmann
 */
public class ScheduledTaskParameter
  implements Serializable
{
  private static final long serialVersionUID = 1000000;

  /**
   * The ID uniquely identifying the scheduled task parameter.
   */
  private long id;

  /**
   * The name of the scheduled task parameter.
   */
  private String name;

  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the scheduled task the
   * scheduled task parameter is associated with.
   */
  private String scheduledTaskId;

  /**
   * The value of the scheduled task parameter.
   */
  private String value;

  /**
   * Constructs a new <code>ScheduledTaskParameter</code>.
   *
   * @param id              the ID uniquely identifying the scheduled task parameter
   * @param scheduledTaskId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        scheduled task the scheduled task parameter is associated with
   * @param name            the name of the scheduled task parameter
   * @param value           the value of the scheduled task parameter
   */
  public ScheduledTaskParameter(long id, String scheduledTaskId, String name, String value)
  {
    this.id = id;
    this.scheduledTaskId = scheduledTaskId;
    this.name = name;
    this.value = value;
  }

  /**
   * Returns the ID uniquely identifying the scheduled task parameter.
   *
   * @return the ID uniquely identifying the scheduled task parameter
   */
  public long getId()
  {
    return id;
  }

  /**
   * Returns the name of the scheduled task parameter.
   *
   * @return the name of the scheduled task parameter
   */
  public String getName()
  {
    return name;
  }

  /**
   * Returns the Universally Unique Identifier (UUID) used to uniquely identify the scheduled task
   * the scheduled task parameter is associated with.
   *
   * @return the Universally Unique Identifier (UUID) used to uniquely identify the scheduled task
   *         the scheduled task parameter is associated with
   */
  public String getScheduledTaskId()
  {
    return scheduledTaskId;
  }

  /**
   * Returns the value of the scheduled task parameter.
   *
   * @return the value of the scheduled task parameter
   */
  public String getValue()
  {
    return value;
  }

  /**
   * Set the ID uniquely identifying the scheduled task parameter.
   *
   * @param id the ID uniquely identifying the scheduled task parameter
   */
  public void setId(long id)
  {
    this.id = id;
  }

  /**
   * Set the name of the scheduled task parameter.
   *
   * @param name the name of the scheduled task parameter
   */
  public void setName(String name)
  {
    this.name = name;
  }

  /**
   * Set the Universally Unique Identifier (UUID) used to uniquely identify the scheduled task the
   * scheduled task parameter is associated with.
   *
   * @param scheduledTaskId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                        scheduled task the scheduled task parameter is associated with
   */
  public void setScheduledTaskId(String scheduledTaskId)
  {
    this.scheduledTaskId = scheduledTaskId;
  }

  /**
   * Set the value of the scheduled task parameter.
   *
   * @param value the value of the scheduled task parameter
   */
  public void setValue(String value)
  {
    this.value = value;
  }
}

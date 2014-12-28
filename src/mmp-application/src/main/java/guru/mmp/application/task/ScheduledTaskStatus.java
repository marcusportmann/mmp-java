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

package guru.mmp.application.task;

/**
 * The enumeration giving the possible statuses for a scheduled task.
 *
 * @author Marcus Portmann
 */
public enum ScheduledTaskStatus
{
  UNKNOWN(0, "Unknown"), SCHEDULED(1, "Scheduled"), EXECUTING(2, "Executing"),
  EXECUTED(3, "Executed"), ABORTED(4, "Aborted"), FAILED(5, "Failed");

  private int code;
  private String name;

  ScheduledTaskStatus(int code, String name)
  {
    this.code = code;
    this.name = name;
  }

  /**
   * Returns the status given by the specified numeric code value.
   *
   * @param code the numeric code value identifying the status
   * @return the status given by the specified numeric code value
   */
  public static ScheduledTaskStatus fromCode(int code)
  {
    switch (code)
    {
      case 1:
        return ScheduledTaskStatus.SCHEDULED;

      case 2:
        return ScheduledTaskStatus.EXECUTING;

      case 3:
        return ScheduledTaskStatus.EXECUTED;

      case 4:
        return ScheduledTaskStatus.ABORTED;

      case 5:
        return ScheduledTaskStatus.FAILED;

      default:
        return ScheduledTaskStatus.UNKNOWN;
    }
  }

  /**
   * Returns the numeric code value identifying the status.
   *
   * @return the numeric code value identifying the status
   */
  public int getCode()
  {
    return code;
  }

  /**
   * Returns the <code>String</code> value of the numeric code value identifying the status.
   *
   * @return the <code>String</code> value of the numeric code value identifying the status
   */
  public String getCodeAsString()
  {
    return String.valueOf(code);
  }

  /**
   * Returns the name of the status.
   *
   * @return the name of the status
   */
  public String getName()
  {
    return name;
  }

  /**
   * Return the string representation of the status enumeration value.
   *
   * @return the string representation of the status enumeration value
   */
  public String toString()
  {
    return name;
  }
}

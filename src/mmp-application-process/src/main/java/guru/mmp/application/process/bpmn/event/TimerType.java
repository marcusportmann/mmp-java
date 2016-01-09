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

package guru.mmp.application.process.bpmn.event;

/**
 * The <code>TimerType</code> enumeration defines the possible types for a Timer.
 *
 * @author Marcus Portmann
 */
public enum TimerType
{
  DATE(0, "Date"), CYCLE(1, "Cycle"), DURATION(2, "Duration");

  private int code;

  private String name;

  /**
   * Returns the Timer type given by the specified numeric code value.
   *
   * @param code the numeric code value identifying the Timer type
   *
   * @return the Timer type given by the specified numeric code value
   */
  public static TimerType fromCode(int code)
  {
    switch (code)
    {
      case 0:
        return TimerType.DATE;

      case 1:
        return TimerType.CYCLE;

      case 2:
        return TimerType.DURATION;

      default:
        throw new RuntimeException("Unknown Timer type (" + code + ")");
    }
  }

  TimerType(int code, String name)
  {
    this.code = code;
    this.name = name;
  }

  /**
   * Returns the numeric code value identifying the Timer type.
   *
   * @return the numeric code value identifying the Timer type
   */
  public int getCode()
  {
    return code;
  }

  /**
   * Returns the <code>String</code> code value identifying the Timer type.
   *
   * @return the <code>String</code> code value identifying the Timer type
   */
  public String getCodeAsString()
  {
    return String.valueOf(code);
  }

  /**
   * Returns the name of the Timer type.
   *
   * @return the name of the Timer type
   */
  public String getName()
  {
    return name;
  }

  /**
   * Return the string representation of the <code>TimerType</code>
   * enumeration value.
   *
   * @return the string representation of the <code>TimerType</code>
   * enumeration value
   */
  public String toString()
  {
    return name;
  }
}

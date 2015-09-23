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

package guru.mmp.application.process.bpmn.event;

/**
 * The <code>EndEventType</code> enumeration defines the possible types for a Business Process
 * Model and Notation (BPMN) end event.
 * <p>
 * There are eight types of end events:
 * <ul>
 *   <li>
 *     <b>None</b>: The none end event indicates that there is no result when the process finishes.
 *   </li>
 *   <li>
 *     <b>Message</b>: A message end event indicates that a message is sent to another participant
 *     when the process finishes.
 *     <p>
 *     If there are multiple incoming sequence flows from multiple parallel paths to the message
 *     end event event, the message is sent multiple times.
 *   </li>
 *   <li>
 *     <b>Error</b>: .
 *   </li>
 *   <li>
 *     <b>Cancel</b>: .
 *   </li>
 *   <li>
 *     <b>Compensation</b>: .
 *   </li>
 *   <li>
 *     <b>Signal</b>: A signal end event indicates that a process sends a broadcast signal (a
 *     signal to all participants) when it finishes.
 *   </li>
 *   <li>
 *     <b>Terminate</b>: A terminate end event immediately ends the process or subprocess, even if
 *     other paths are active at the time.
 *   </li>
 *   <li>
 *     <b>Multiple</b>: A multiple end event indicates that more than one result is generated when
 *     the process finishes.
 *   </li>
 *   <li>
 *     <b>Escalation</b>: .
 *   </li>
 * </ul>
 *
 * @author Marcus Portmann
 */
public enum EndEventType
{
  NONE(0, "None"), MESSAGE(1, "Message"), ERROR(2, "Error"), CANCEL(3, "Cancel"),
  COMPENSATION(4, "Compensation"), SIGNAL(5, "Signal"), TERMINATE(6, "Terminate"),
  MULTIPLE(7, "Multiple"), ESCALATION(8, "Escalation");

  private int code;
  private String name;

  EndEventType(int code, String name)
  {
    this.code = code;
    this.name = name;
  }

  /**
   * Returns the end event type given by the specified numeric code value.
   *
   * @param code the numeric code value identifying the end event type
   *
   * @return the end event type given by the specified numeric code value
   */
  public static EndEventType fromCode(int code)
  {
    switch (code)
    {
      case 0:
        return EndEventType.NONE;

      case 1:
        return EndEventType.MESSAGE;

      case 2:
        return EndEventType.ERROR;

      case 3:
        return EndEventType.CANCEL;

      case 4:
        return EndEventType.COMPENSATION;

      case 5:
        return EndEventType.SIGNAL;

      case 6:
        return EndEventType.TERMINATE;

      case 7:
        return EndEventType.MULTIPLE;

      case 8:
        return EndEventType.ESCALATION;

      default:
        return EndEventType.NONE;
    }
  }

  /**
   * Returns the numeric code value identifying the end event type.
   *
   * @return the numeric code value identifying the end event type
   */
  public int getCode()
  {
    return code;
  }

  /**
   * Returns the <code>String</code> code value identifying the end event type.
   *
   * @return the <code>String</code> code value identifying the end event type
   */
  public String getCodeAsString()
  {
    return String.valueOf(code);
  }

  /**
   * Returns the name of the end event type.
   *
   * @return the name of the end event type
   */
  public String getName()
  {
    return name;
  }

  /**
   * Return the string representation of the <code>EventType</code>
   * enumeration value.
   *
   * @return the string representation of the <code>EventType</code>
   *         enumeration value
   */
  public String toString()
  {
    return name;
  }
}

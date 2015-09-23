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
 * The <code>StartEventType</code> enumeration defines the possible types for a Business Process
 * Model and Notation (BPMN) start event.
 * <p>
 * There are eight types of start events:
 * <ul>
 *   <li>
 *     <b>None</b>: In this case the trigger (the cause of the event) is not specified. This type
 *     of start event is also used to mark the start of a subprocess, since a subprocess is not
 *     actually started by an event but by a sequence flow.
 *   </li>
 *   <li>
 *     <b>Message</b>: The process is started by the reception of a message, which is a form of
 *     communication between business participants. This message is treated as an external request
 *     and the process starts by handling that request.
 *   </li>
 *   <li>
 *     <b>Timer</b>: The process is started when a specific time condition occurs. This can be a
 *     specific date and time or a recurring time.
 *   </li>
 *   <li>
 *     <b>Multiple</b>: The process can be started by any of the multiple triggers it defines (like
 *     a message or timer). Each trigger represents an alternative way to start the process. Once
 *     triggered, the process will ignore other triggers received.
 *   </li>
 *   <li>
 *     <b>Multiple-Parallel</b>: The process is started when all the multiple triggers it defines
 *     have occurred.
 *   </li>
 *   <li>
 *     <b>Conditional</b>: The process is started when a condition becomes true.
 *   </li>
 *   <li>
 *     <b>Signal</b>: The process is started when a signal is received. A signal is defined as a
 *     type of communication from a business participant that has no specific target, meaning that
 *     all participants can see it and choose to respond to it.
 *   </li>
 *   <li>
 *     <b>Error</b>: The process is started when an error occurrs.
 *   </li>
 * </ul>
 *
 * @author Marcus Portmann
 */
public enum StartEventType
{
  NONE(0, "None"), MESSAGE(1, "Message"), TIMER(2, "Timer"), MULTIPLE(3, "Multiple"),
  MULTIPLE_PARALLEL(4, "Multiple-Parallel"), CONDITIONAL(5, "Conditional"), SIGNAL(6, "Signal"),
  ERROR(7, "Error");

  private int code;
  private String name;

  StartEventType(int code, String name)
  {
    this.code = code;
    this.name = name;
  }

  /**
   * Returns the start event type given by the specified numeric code value.
   *
   * @param code the numeric code value identifying the start event type
   *
   * @return the start event type given by the specified numeric code value
   */
  public static StartEventType fromCode(int code)
  {
    switch (code)
    {
      case 0:
        return StartEventType.NONE;

      case 1:
        return StartEventType.MESSAGE;

      case 2:
        return StartEventType.TIMER;

      case 3:
        return StartEventType.MULTIPLE;

      case 4:
        return StartEventType.MULTIPLE_PARALLEL;

      case 5:
        return StartEventType.CONDITIONAL;

      case 6:
        return StartEventType.SIGNAL;

      case 7:
        return StartEventType.ERROR;

      default:
        return StartEventType.NONE;
    }
  }

  /**
   * Returns the numeric code value identifying the start event type.
   *
   * @return the numeric code value identifying the start event type
   */
  public int getCode()
  {
    return code;
  }

  /**
   * Returns the <code>String</code> code value identifying the start event type.
   *
   * @return the <code>String</code> code value identifying the start event type
   */
  public String getCodeAsString()
  {
    return String.valueOf(code);
  }

  /**
   * Returns the name of the start event type.
   *
   * @return the name of the start event type
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

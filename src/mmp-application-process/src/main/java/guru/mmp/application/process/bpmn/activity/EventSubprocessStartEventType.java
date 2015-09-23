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

package guru.mmp.application.process.bpmn.activity;

/**
 * The <code>EventSubprocessStartEventType</code> enumeration defines the possible start event types
 * for a Business Process Model and Notation (BPMN) event subprocess.
 * <p>
 * There are eight types of start events:
 * <ul>
 *   <li>
 *     <b>Message</b>: The subprocess is started by the reception of a message, which is a form of
 *     communication between business participants. This message is treated as an external request
 *     and the process starts by handling that request.
 *   </li>
 *   <li>
 *     <b>Timer</b>: The subprocess is started when a specific time condition occurs. This can be a
 *     specific date and time or a recurring time.
 *   </li>
 *   <li>
 *     <b>Multiple</b>: The subprocess can be started by any of the multiple triggers it defines
 *     (like a message or timer). Each trigger represents an alternative way to start the
 *     subprocess. Once triggered, the process will ignore other triggers received.
 *   </li>
 *   <li>
 *     <b>Multiple-Parallel</b>: The subprocess is started when all the multiple triggers it defines
 *     have occurred.
 *   </li>
 *   <li>
 *     <b>Conditional</b>: The subprocess is started when a condition becomes true.
 *   </li>
 *   <li>
 *     <b>Signal</b>: The subprocess is started when a signal is received. A signal is defined as a
 *     type of communication from a business participant that has no specific target, meaning that
 *     all participants can see it and choose to respond to it.
 *   </li>
 *   <li>
 *     <b>Escalation</b>: The subprocess is started as a result of an escalation.
 *   </li>
 * </ul>
 *
 * @author Marcus Portmann
 */
public enum EventSubprocessStartEventType
{
  MESSAGE(0, "Message"), TIMER(1, "Timer"), MULTIPLE(2, "Multiple"),
  MULTIPLE_PARALLEL(3, "Multiple-Parallel"), CONDITIONAL(4, "Conditional"), SIGNAL(5, "Signal"),
  ESCALATION(6, "Escalation");

  private int code;
  private String name;

  EventSubprocessStartEventType(int code, String name)
  {
    this.code = code;
    this.name = name;
  }

  /**
   * Returns the event subprocess start event type given by the specified numeric code value.
   *
   * @param code the numeric code value identifying the event subprocess start event type
   *
   * @return the event subprocess start event type given by the specified numeric code value
   */
  public static EventSubprocessStartEventType fromCode(int code)
  {
    switch (code)
    {
      case 0:
        return EventSubprocessStartEventType.MESSAGE;

      case 1:
        return EventSubprocessStartEventType.TIMER;

      case 2:
        return EventSubprocessStartEventType.MULTIPLE;

      case 3:
        return EventSubprocessStartEventType.MULTIPLE_PARALLEL;

      case 4:
        return EventSubprocessStartEventType.CONDITIONAL;

      case 5:
        return EventSubprocessStartEventType.SIGNAL;

      case 6:
        return EventSubprocessStartEventType.ESCALATION;

      default:
        throw new RuntimeException("Invalid event subprocess start event type (" + code + ")");
    }
  }

  /**
   * Returns the numeric code value identifying the event subprocess start event type.
   *
   * @return the numeric code value identifying the event subprocess start event type
   */
  public int getCode()
  {
    return code;
  }

  /**
   * Returns the <code>String</code> code value identifying the event subprocess start event type.
   *
   * @return the <code>String</code> code value identifying the event subprocess start event type
   */
  public String getCodeAsString()
  {
    return String.valueOf(code);
  }

  /**
   * Returns the name of the event subprocess start event type.
   *
   * @return the name of the event subprocess start event type
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

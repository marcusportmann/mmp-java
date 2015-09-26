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
 * The <code>EventType</code> enumeration defines the possible types for a Business
 * Process Model and Notation (BPMN) event.
 * <p>
 * There are thirteen types of events:
 * <ul>
 *   <li>
 *     <b>None</b>: The none event type indicates that the event does not require specific
 *     processing that needs to be performed when the event is thrown or caught.
 *   </li>
 *   <li>
 *     <b>Cancel</b>: The cancel event type throws or catches a cancellation.
 *     <p>
 *     Cancel events work in throw/catch pairs and are interrupting.
 *     <p>
 *     Cancel events are only used with transactional subprocesses. Since it works in the context of
 *     a transaction, the cancel event implies that the trasaction is rolled-back and may result in
 *     (or execute) the compensation of some activities of the transaction.
 *     <p>
 *     A cancel catch event can only be attached to the boundary of a transaction subprocess and it
 *     is triggered by a cancel throw event within the subprocess.
 *   </li>
 *   <li>
 *     <b>Compensation</b>: The compensation event type is used to undo changes.
 *     <p>
 *     The compensation mechanism is as follows:
 *     <ol>
 *       <li>Using a gateway, detect the need for compensation.</li>
 *       <li>
 *         Throw a compensation event, either by using a compensation end event or a compensation
 *         throw event.
 *       </li>
 *       <li>
 *         Execute the appropriate compensation handler. A compensation handler is a special
 *         activity that undoes the work done by the activity that it is associated with. It can
 *         have the form of an event subprocess (if a compensation start event is used) or can
 *         be associated with an activity using a catching compensation boundary event.
 *       </li>
 *     </ol>
 *   </li>
 *   <li>
 *     <b>Conditional</b>: The conditional event type waits for a condition to become true.
 *   </li>
 *   <li>
 *     <b>Error</b>: The error event type throws or catches an error.
 *     <p>
 *     When there's a critical problem in an activity, an error is generated and all work will stop.
 *     Because of that, error events are only interrupting.
 *     <p>
 *     To handle an error we need a throw/catch pair:
 *     <ul>
 *       <li>
 *         If the error is somehow expected, i.e. its a known error generated under certain
 *         conditions, throw the error with an error end event to the end of the activity in which
 *         it occurred.
 *       </li>
 *       <li>
 *         You can catch the error at two levels:
 *         <ol>
 *           <li>At the same process level with an error start event.</li>
 *           <li>
 *             With an error boundary event attached to the activity that generates the error.
 *           </li>
 *         </ol>
 *       </li>
 *       <li>
 *         The catching event emits a token to the activity that handles the error.
 *       </li>
 *     </ul>
 *     <p>
 *   </li>
 *   <li>
 *     <b>Escalation</b>: The escalation event type throws or catches an escalation.
 *     <p>
 *     Escalations and errors are similar in that they are both a deviation from the normal process
 *     and they both work in throw/catch pairs. Escalations however are a non-interrupting kind of
 *     error that occurs inside an activity.
 *     <p>
 *     An escalation event is commonly used as a boundary event and in cases where a separate
 *     parallel path of action needs to be initiated in an activity.
 *   </li>
 *   <li>
 *     <b>Link</b>: The link event type throws or catches a link.
 *     <p>
 *     Link events work in throw/catch pairs and are used as page connectors and "go-to" objects.
 *   </li>
 *   <li>
 *     <b>Message</b>: The message event type sends or receives a message, which is a form of
 *     communication between two business participants.
 *   </li>
 *   <li>
 *     <b>Multiple</b>: The multiple event type throws or catches all the events on its list.
 *   </li>
 *   <li>
 *     <b>MultipleParallel</b>: The multiple parallel event type throws or catches all events on
 *     its list.
 *   </li>
 *   <li>
 *     <b>Signal</b>: The signal event throws or catches a broadcast signal.
 *   </li>
 *   <li>
 *     <b>Terminate</b>: A terminate event type immediately ends the process or subprocess, even if
 *     other paths are active at the time.
 *   </li>
 *   <li>
 *     <b>Timer</b>: The timer boundary event waits for time conditions to become true.
 *   </li>
 * </ul>
 *
 * @author Marcus Portmann
 */
public enum EventType
{
  CANCEL(0, "Cancel"), COMPENSATION(1, "Compensation"), CONDITIONAL(2, "Conditional"),
  ERROR(3, "Error"), ESCALATION(4, "Escalation"), LINK(5, "Link"), MESSAGE(6, "Message"),
  MULTIPLE(7, "Multiple"), MULTIPLE_PARALLEL(8, "Multiple Parallel"), NONE(9, "None"),
  SIGNAL(10, "Signal"), TERMINATE(11, "Terminate"), TIMER(12, "Timer");

  private int code;
  private String name;

  EventType(int code, String name)
  {
    this.code = code;
    this.name = name;
  }

  /**
   * Returns the event type given by the specified numeric code value.
   *
   * @param code the numeric code value identifying the event type
   *
   * @return the event type given by the specified numeric code value
   */
  public static EventType fromCode(int code)
  {
    switch (code)
    {
      case 0:
        return EventType.CANCEL;

      case 1:
        return EventType.COMPENSATION;

      case 2:
        return EventType.CONDITIONAL;

      case 3:
        return EventType.ERROR;

      case 4:
        return EventType.ESCALATION;

      case 5:
        return EventType.LINK;

      case 6:
        return EventType.MESSAGE;

      case 7:
        return EventType.MULTIPLE;

      case 8:
        return EventType.MULTIPLE_PARALLEL;

      case 9:
        return EventType.NONE;

      case 10:
        return EventType.SIGNAL;

      case 11:
        return EventType.TERMINATE;

      case 12:
        return EventType.TIMER;

      default:
        throw new RuntimeException("Unknown event type (" + code + ")");
    }
  }

  /**
   * Returns the numeric code value identifying the event type.
   *
   * @return the numeric code value identifying the event type
   */
  public int getCode()
  {
    return code;
  }

  /**
   * Returns the <code>String</code> code value identifying the event type.
   *
   * @return the <code>String</code> code value identifying the event type
   */
  public String getCodeAsString()
  {
    return String.valueOf(code);
  }

  /**
   * Returns the name of the event type.
   *
   * @return the name of the event type
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

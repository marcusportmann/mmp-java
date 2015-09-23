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
 * The <code>IntermediateEventType</code> enumeration defines the possible types for a Business
 * Process Model and Notation (BPMN) intermediate event.
 * <p>
 * There are eight types of intermediate events:
 * <ul>
 *   <li>
 *     <b>MessageThrow</b>: .
 *   </li>
 *   <li>
 *     <b>MessageCatch</b>: .
 *   </li>
 *   <li>
 *     <b>Timer</b>: .
 *   </li>
 *   <li>
 *     <b>Error</b>: .
 *   </li>
 *   <li>
 *     <b>Cancel</b>: .
 *   </li>
 *   <li>
 *     <b>CompensationThrow</b>: .
 *   </li>
 *   <li>
 *     <b>CompensationCatch</b>: .
 *   </li>
 *   <li>
 *     <b>Conditional</b>: .
 *   </li>
 *   <li>
 *     <b>LinkThrow</b>: .
 *   </li>
 *   <li>
 *     <b>LinkCatch</b>: .
 *   </li>
 *   <li>
 *     <b>SignalThrow</b>: .
 *   </li>
 *   <li>
 *     <b>SignalCatch</b>: .
 *   </li>
 *   <li>
 *     <b>MultipleThrow</b>: .
 *   </li>
 *   <li>
 *     <b>MultipleCatch</b>: .
 *   </li>
 *   <li>
 *     <b>MultipleParallel</b>: .
 *   </li>
 * </ul>
 *
 * @author Marcus Portmann
 */
public enum IntermediateEventType
{
  MESSAGE_THROW(0, "Message Throw"), MESSAGE_CATCH(1, "Message Catch"), TIMER(2, "Timer"),
  ERROR(3, "Error"), CANCEL(4, "Cancel"), COMPENSATION_THROW(5, "Compensation Throw"),
  COMPENSATION_CATCH(6, "Compensation Catch"), CONDITIONAL(7, "Conditional"),
  LINK_THROW(8, "Link Throw"), LINK_CATCH(9, "Link Catch"), SIGNAL_THROW(10, "Signal Throw"),
  SIGNAL_CATCH(11, "Escalation"), MULTIPLE_THROW(12, "Multiple Throw"),
  MULTIPLE_CATCH(13, "Multiple Catch"), MULTIPLE_PARALLEL(14, "Multiple Parallel");

  private int code;
  private String name;

  IntermediateEventType(int code, String name)
  {
    this.code = code;
    this.name = name;
  }

  /**
   * Returns the intermediate event type given by the specified numeric code value.
   *
   * @param code the numeric code value identifying the intermediate event type
   *
   * @return the intermediate event type given by the specified numeric code value
   */
  public static IntermediateEventType fromCode(int code)
  {
    switch (code)
    {
      case 0:
        return IntermediateEventType.MESSAGE_THROW;

      case 1:
        return IntermediateEventType.MESSAGE_CATCH;

      case 2:
        return IntermediateEventType.TIMER;

      case 3:
        return IntermediateEventType.ERROR;

      case 4:
        return IntermediateEventType.CANCEL;

      case 5:
        return IntermediateEventType.COMPENSATION_THROW;

      case 6:
        return IntermediateEventType.COMPENSATION_CATCH;

      case 7:
        return IntermediateEventType.CONDITIONAL;

      case 8:
        return IntermediateEventType.LINK_THROW;

      case 9:
        return IntermediateEventType.LINK_CATCH;

      case 10:
        return IntermediateEventType.SIGNAL_THROW;

      case 11:
        return IntermediateEventType.SIGNAL_CATCH;

      case 12:
        return IntermediateEventType.MULTIPLE_THROW;

      case 13:
        return IntermediateEventType.MULTIPLE_CATCH;

      case 14:
        return IntermediateEventType.MULTIPLE_PARALLEL;

      default:
        throw new RuntimeException("Unknown intermediate event type (" + code + ")");
    }
  }

  /**
   * Returns the numeric code value identifying the intermediate event type.
   *
   * @return the numeric code value identifying the intermediate event type
   */
  public int getCode()
  {
    return code;
  }

  /**
   * Returns the <code>String</code> code value identifying the intermediate event type.
   *
   * @return the <code>String</code> code value identifying the intermediate event type
   */
  public String getCodeAsString()
  {
    return String.valueOf(code);
  }

  /**
   * Returns the name of the intermediate event type.
   *
   * @return the name of the intermediate event type
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

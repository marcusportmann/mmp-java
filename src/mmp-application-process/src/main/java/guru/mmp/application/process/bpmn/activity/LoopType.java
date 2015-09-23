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
 * The <code>LoopType</code> enumeration defines the possible ways that a Business Process Model and
 * Notation (BPMN) activity can be executed multiple times.
 * <p>
 * There are three types of loops:
 * <ul>
 *   <li>
 *     <b>Standard</b>: There are two variations of the standard loop:
 *     <ul>
 *       <li>
 *         <b>While</b> loop: First, the loop condition is checked and if it evaluates to true, the
 *         activity is performed. Otherwise, the activity is not executed and the process continues.
 *         Each time the activity finishes executing, the condition is evaluated again until it
 *         becomes false. There is a chance that the activity will never be performed.
 *       </li>
 *       <li>
 *         <b>Until</b> loop: First, the activity is performed and then, the loop condition is
 *         checked. If it evaluates to true the activity is performed again, if not, the process
 *         continues. The activity is always performed at least once.
 *       </li>
 *     </ul>
 *   </li>
 *   <li>
 *     <b>Parallel Multi-Instance</b>: The parallel multi-instance loop operates on some type of
 *     collection. The instance of the activity is performed in parallel once for each item in the
 *     collection.
 *   </li>
 *   <li>
 *     <b>Sequential Multi-Instance</b>: The sequential multi-instance loop operates on some type of
 *     collection. The instance of the activity is performed once for each item in the collection
 *     sequentially.
 *   </li>
 * </ul>
 *
 * @author Marcus Portmann
 */
public enum LoopType
{
  NONE(0, "None"), STANDARD(1, "Standard"), PARALLEL_MULTI_INSTANCE(2, "Parallel Multi-Instance"),
  SEQUENTIAL_MULTI_INSTANCE(4, "Sequential Multi-Instance");

  private int code;
  private String name;

  LoopType(int code, String name)
  {
    this.code = code;
    this.name = name;
  }

  /**
   * Returns the loop type given by the specified numeric code value.
   *
   * @param code the numeric code value identifying the loop type
   *
   * @return the loop type given by the specified numeric code value
   */
  public static LoopType fromCode(int code)
  {
    switch (code)
    {
      case 0:
        return LoopType.NONE;

      case 1:
        return LoopType.STANDARD;

      case 2:
        return LoopType.PARALLEL_MULTI_INSTANCE;

      case 3:
        return LoopType.SEQUENTIAL_MULTI_INSTANCE;

      default:
        return LoopType.NONE;
    }
  }

  /**
   * Returns the numeric code value identifying the loop type.
   *
   * @return the numeric code value identifying the loop type
   */
  public int getCode()
  {
    return code;
  }

  /**
   * Returns the <code>String</code> code value identifying the loop type.
   *
   * @return the <code>String</code> code value identifying the loop type
   */
  public String getCodeAsString()
  {
    return String.valueOf(code);
  }

  /**
   * Returns the name of the loop type.
   *
   * @return the name of the loop type
   */
  public String getName()
  {
    return name;
  }

  /**
   * Return the string representation of the <code>LoopType</code>
   * enumeration value.
   *
   * @return the string representation of the <code>LoopType</code>
   *         enumeration value
   */
  public String toString()
  {
    return name;
  }
}

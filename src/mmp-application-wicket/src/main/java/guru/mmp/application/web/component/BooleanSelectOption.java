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

package guru.mmp.application.web.component;

//~--- JDK imports ------------------------------------------------------------

import java.io.Serializable;

/**
 * The <code>BooleanSelectOption</code> class stores a string name and boolean value pair for a
 * select option.
 *
 * @author Marcus Portmann
 */
public class BooleanSelectOption
  implements Serializable
{
  private static final long serialVersionUID = 1000000;

  /**
   * The user-friendly name for the select option.
   */
  private String name;

  /**
   * The value for the select option.
   */
  private boolean value;

  /**
   * Constructs a new <code>BooleanSelectOption</code>.
   *
   * @param name  the user-friendly name for the select option
   * @param value the value for the select option
   */
  public BooleanSelectOption(String name, boolean value)
  {
    this.name = name;
    this.value = value;
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   *
   * @param obj the reference object with which to compare
   *
   * @return <code>true</code> if this object is the same as the <code>obj</code> argument;
   *         <code>false</code> otherwise
   */
  @Override
  public boolean equals(Object obj)
  {
    return (obj != null)
        && ((obj == this)
          || ((obj instanceof BooleanSelectOption)
            && (((BooleanSelectOption) obj).value == value)));
  }

  /**
   * Returns the user-friendly name for the select option.
   *
   * @return the user-friendly name for the select option
   */
  public String getName()
  {
    return name;
  }

  /**
   * Returns the value for the select option.
   *
   * @return the value for the select option
   */
  public boolean getValue()
  {
    return value;
  }

  /**
   * Set the user-friendly name for the select option.
   *
   * @param name the user-friendly name for the select option
   */
  public void setName(String name)
  {
    this.name = name;
  }

  /**
   * Set the value for the select option.
   *
   * @param value the value for the select option
   */
  public void setValue(boolean value)
  {
    this.value = value;
  }

  /**
   * Returns a string representation of the object.
   *
   * @return a string representation of the object
   */
  @Override
  public String toString()
  {
    return String.valueOf(value);
  }
}

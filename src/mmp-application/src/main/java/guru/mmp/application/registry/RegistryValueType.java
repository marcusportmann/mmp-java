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

package guru.mmp.application.registry;

/**
 * The enumeration giving the possible types of values for a registry entry.
 *
 * @author Marcus Portmann
 */
public enum RegistryValueType
{
  NONE(-1, "None"), STRING(1, "String"), INTEGER(2, "Integer"), DECIMAL(3, "Decimal"),
  BINARY(4, "Binary");

  private int code;
  private String name;

  RegistryValueType(int code, String name)
  {
    this.code = code;
    this.name = name;
  }

  /**
   * Returns the registry value type given by the specified numeric code value.
   *
   * @param code the numeric code value identifying the registry value type
   *
   * @return the registry value type given by the specified numeric code value
   */
  public static RegistryValueType fromCode(int code)
  {
    switch (code)
    {
      case 1:
        return RegistryValueType.STRING;

      case 2:
        return RegistryValueType.INTEGER;

      case 3:
        return RegistryValueType.DECIMAL;

      case 4:
        return RegistryValueType.BINARY;

      default:
        return RegistryValueType.NONE;
    }
  }

  /**
   * Returns the numeric code value identifying the registry value type.
   *
   * @return the numeric code value identifying the registry value type
   */
  public int getCode()
  {
    return code;
  }

  /**
   * Returns the name of the registry value type.
   *
   * @return the name of the registry value type
   */
  public String getName()
  {
    return name;
  }

  /**
   * Return the string representation of the <code>RegistryValueType</code> enumeration value.
   *
   * @return the string representation of the <code>RegistryValueType</code> enumeration value
   */
  public String toString()
  {
    return name;
  }
}

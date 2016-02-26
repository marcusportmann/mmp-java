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

package guru.mmp.application.customer;

/**
 * The enumeration giving the possible customer address types.
 *
 * @author Marcus Portmann
 */
public enum CustomerAddressType
{
  PHYSICAL_ADDRESS(0, "Physical Address"), POSTAL_ADDRESS(1, "Postal Address"), BILLING_ADDRESS(2,
      "Billing Address");

  private int code;
  private String name;

  CustomerAddressType(int code, String name)
  {
    this.code = code;
    this.name = name;
  }

  /**
   * Returns the customer address type given by the specified numeric code value.
   *
   * @param code the numeric code value identifying the customer address type
   *
   * @return the customer address type given by the specified numeric code value
   */
  public static CustomerAddressType fromCode(int code)
  {
    switch (code)
    {
      case 0:
        return CustomerAddressType.PHYSICAL_ADDRESS;

      case 1:
        return CustomerAddressType.POSTAL_ADDRESS;

      case 2:
        return CustomerAddressType.BILLING_ADDRESS;

      default:
        return CustomerAddressType.PHYSICAL_ADDRESS;
    }
  }

  /**
   * Returns the numeric code value identifying the customer address type.
   *
   * @return the numeric code value identifying the customer address type
   */
  public int getCode()
  {
    return code;
  }

  /**
   * Returns the <code>String</code> code value identifying the customer address type.
   *
   * @return the <code>String</code> code value identifying the customer address type
   */
  public String getCodeAsString()
  {
    return String.valueOf(code);
  }

  /**
   * Returns the name of the customer address type.
   *
   * @return the name of the customer address type
   */
  public String getName()
  {
    return name;
  }

  /**
   * Return the string representation of the <code>CustomerAddressType</code> enumeration value.
   *
   * @return the string representation of the <code>CustomerAddressType</code> enumeration value
   */
  public String toString()
  {
    return name;
  }
}

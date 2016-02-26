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

//~--- JDK imports ------------------------------------------------------------

import javax.persistence.AttributeConverter;

/**
 * The <code>CustomerAddressConverter</code> class converts a <code>CustomerAddressType</code>
 * enumeration value into a database column representation and back again.
 *
 * @author Marcus Portmann
 */
public class CustomerAddressTypeConverter
  implements AttributeConverter<CustomerAddressType, Integer>
{
  @Override
  public Integer convertToDatabaseColumn(CustomerAddressType customerAddressType)
  {
    return customerAddressType.getCode();
  }

  @Override
  public CustomerAddressType convertToEntityAttribute(Integer dbData)
  {
    return CustomerAddressType.fromCode(dbData);
  }
}

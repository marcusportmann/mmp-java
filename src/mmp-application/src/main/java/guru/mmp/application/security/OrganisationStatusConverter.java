/*
 * Copyright 2017 Marcus Portmann
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

package guru.mmp.application.security;

//~--- JDK imports ------------------------------------------------------------

import javax.persistence.AttributeConverter;

/**
 * The <code>OrganisationStatusConverter</code> class implements the custom JPA converter for the
 * <code>OrganisationStatus</code> enumeration.
 *
 * @author Marcus Portmann
 */
public class OrganisationStatusConverter
  implements AttributeConverter<OrganisationStatus, Integer>
{
  /**
   * Converts the value stored in the entity attribute into the data representation to be stored in
   * the database.
   *
   * @param attribute the entity attribute value to be converted
   *
   * @return the converted data to be stored in the database column
   */
  @Override
  public Integer convertToDatabaseColumn(OrganisationStatus attribute)
  {
    return attribute.code();
  }

  /**
   * Converts the data stored in the database column into the value to be stored in the entity
   * attribute. Note that it is the responsibility of the converter writer to specify the correct
   * dbData type for the corresponding column for use by the JDBC driver: i.e., persistence
   * providers are not expected to do such type conversion.
   *
   * @param dbData the data from the database column to be converted
   *
   * @return the converted value to be stored in the entity attribute
   */
  @Override
  public OrganisationStatus convertToEntityAttribute(Integer dbData)
  {
    return OrganisationStatus.fromCode(dbData);
  }
}

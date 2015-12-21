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

package guru.mmp.common.persistence;

//~--- JDK imports ------------------------------------------------------------

import javax.persistence.AttributeConverter;

/**
 * The <code>BooleanToYNStringConverter</code> implements a JPA attribute converter that converts a
 * <code>Boolean</code> entity attribute to a single-character Y/N <code>String</code> that will be
 * stored in the database, and vice-versa
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public class BooleanToYNStringConverter
  implements AttributeConverter<Boolean, String>
{
  /**
   * This implementation will return "Y" if the parameter is Boolean.TRUE,
   * otherwise it will return "N" when the parameter is Boolean.FALSE.
   * A null input value will yield a null return value.
   *
   * @param b Boolean
   *
   * @return "Y" if the parameter is Boolean.TRUE, otherwise it will return
   *         "N" when the parameter is Boolean.FALSE
   */
  @Override
  public String convertToDatabaseColumn(Boolean b)
  {
    if (b == null)
    {
      return null;
    }

    if (b)
    {
      return "Y";
    }

    return "N";
  }

  /**
   * This implementation will return Boolean.TRUE if the string
   * is "Y" or "y", otherwise it will ignore the value and return
   * Boolean.FALSE (it does not actually look for "N") for any
   * other non-null string. A null input value will yield a null
   * return value.
   *
   * @param s String
   *
   * @return Boolean.TRUE if the string is "Y" or "y", otherwise it
   *         will ignore the value and return Boolean.FALSE
   */
  @Override
  public Boolean convertToEntityAttribute(String s)
  {
    if (s == null)
    {
      return null;
    }

    if (s.equals("Y") || s.equals("y"))
    {
      return Boolean.TRUE;
    }

    return Boolean.FALSE;
  }
}

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

package guru.mmp.common.service.ws;

import guru.mmp.common.util.ISO8601;

import java.util.Calendar;

/**
 * The <code>JaxbDateTimeAdapter</code> class implements a JAXB 2.0 adapter used to convert between
 * <code>String</code> and <code>Calendar</code> types.
 * <br>
 * Can be used when customizing XML Schema to Java Representation Binding (XJC).
 */
public class JaxbDateTimeAdapter
{
  /**
   * Marshals the <code>java.util.Calendar</code> value as an ISO8601 string.
   *
   * @param value the value to marshal
   *
   * @return the <code>java.util.Calendar</code> value as an ISO8601 string
   */
  public static String marshal(Calendar value)
  {
    if (value == null)
    {
      return null;
    }

    return ISO8601.fromCalendar(value);
  }

  /**
   * Unmarshals the ISO8601 string value as a <code>java.util.Calendar</code>.
   *
   * @param value the ISO8601 string value
   *
   * @return the ISO8601 string value as a <code>java.util.Calendar</code>
   */
  public static Calendar unmarshal(String value)
  {
    if (value == null)
    {
      return null;
    }

    try
    {
      return ISO8601.toCalendar(value);
    }
    catch (Throwable e)
    {
      throw new RuntimeException("Failed to parse the xs:dateTime value (" + value + ")");
    }
  }
}

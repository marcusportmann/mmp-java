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

package guru.mmp.common.xml;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.common.util.ISO8601;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDateTime;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>LocalDateTimeAdapter</code> class implements a JAXB 2.0 adapter used to convert between
 * <code>String</code> and <code>LocalDateTime</code> types.
 * <br>
 * Can be used when customizing XML Schema to Java Representation Binding (XJC).
 */
public class LocalDateTimeAdapter extends XmlAdapter<String, LocalDateTime>
{
  /**
   * Marshals the <code>java.time.LocalDateTime</code> value as an ISO8601 string.
   *
   * @param value the value to marshal
   *
   * @return the <code>java.time.LocalDateTime</code> value as an ISO8601 string
   */
  @Override
  public String marshal(LocalDateTime value)
  {
    if (value == null)
    {
      return null;
    }

    return ISO8601.fromLocalDateTime(value);
  }

  /**
   * Unmarshals the ISO8601 string value as a <code>java.time.LocalDateTime</code>.
   *
   * @param value the ISO8601 string value
   *
   * @return the ISO8601 string value as a <code>java.time.LocalDateTime</code>
   */
  @Override
  public LocalDateTime unmarshal(String value)
  {
    if (value == null)
    {
      return null;
    }

    try
    {
      return ISO8601.toLocalDateTime(value);
    }
    catch (Throwable e)
    {
      throw new RuntimeException("Failed to parse the xs:dateTime value (" + value + ")");
    }
  }
}

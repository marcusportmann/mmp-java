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

package guru.mmp.application.converter;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.common.util.ISO8601;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>StringToZonedDateTimeConverter</code> class implements the Spring converter that
 * converts a <code>String</code> type into a <code>ZonedDateTime</code> type.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
@Component
public final class StringToZonedDateTimeConverter
  implements Converter<String, ZonedDateTime>
{
  /**
   * Constructs a new <code>StringToZonedDateTimeConverter</code>.
   */
  public StringToZonedDateTimeConverter() {}

  @Override
  public ZonedDateTime convert(String source)
  {
    if ((source == null) || source.isEmpty())
    {
      return null;
    }

    try
    {
      return ISO8601.toZonedDateTime(source);
    }
    catch (Throwable e)
    {
      throw new RuntimeException("Failed to parse the ISO8601 date time value (" + source + ")", e);
    }
  }
}

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

import java.time.LocalDate;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>LocalDateToStringConverter</code> class implements the Spring converter that
 * converts a <code>LocalDate</code> type into a <code>String</code> type.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
@Component
public final class LocalDateToStringConverter
  implements Converter<LocalDate, String>
{
  /**
   * Constructs a new <code>LocalDateToStringConverter</code>.
   */
  public LocalDateToStringConverter() {}

  @Override
  public String convert(LocalDate source)
  {
    if (source == null)
    {
      return null;
    }

    return ISO8601.fromLocalDate(source);
  }
}

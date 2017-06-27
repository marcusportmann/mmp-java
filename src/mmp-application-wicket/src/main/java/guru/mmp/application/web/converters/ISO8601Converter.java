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

package guru.mmp.application.web.converters;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.common.util.ISO8601;
import org.apache.wicket.util.convert.ConversionException;
import org.apache.wicket.util.convert.IConverter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Locale;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>ISO8601Converter</code> class implements the Wicket ISO8601 converter.
 *
 * @author Marcus Portmann
 */
public class ISO8601Converter
  implements IConverter<LocalDateTime>
{
  @Override
  public LocalDateTime convertToObject(String value, Locale locale)
    throws ConversionException
  {
    try
    {
      return ISO8601.toLocalDateTime(value);
    }
    catch (Throwable e)
    {
      throw new ConversionException(String.format(
          "Failed to convert the ISO8601 timestamp (%s) to a Date", value), e);
    }
  }

  @Override
  public String convertToString(LocalDateTime value, Locale locale)
  {
    if (value == null)
    {
      return "N/A";
    }
    else if (value instanceof LocalDateTime)
    {
      return ISO8601.fromLocalDateTime(value);
    }
    else
    {
      return "N/A";
    }
  }
}

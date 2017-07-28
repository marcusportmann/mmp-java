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

package guru.mmp.common.util;

//~--- JDK imports ------------------------------------------------------------

import java.text.ParseException;
import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * The <code>ISO8601</code> class provides a helper class for handling ISO 8601 strings of the
 * following format: "2008-03-01T13:00:00+01:00". It also supports parsing the "Z" timezone.
 *
 * @author Marcus Portmann
 */
public final class ISO8601
{
  private static final ThreadLocal<DateTimeFormatter> threadLocalDateTimeFormatter =
      new ThreadLocal<DateTimeFormatter>()
  {
    @Override
    protected DateTimeFormatter initialValue()
    {
      return DateTimeFormatter.ISO_DATE_TIME.withZone(ZoneId.of("UTC"));
    }
  };
  private static final ThreadLocal<DateTimeFormatter> threadLocalDateFormatter =
      new ThreadLocal<DateTimeFormatter>()
  {
    @Override
    protected DateTimeFormatter initialValue()
    {
      return DateTimeFormatter.ISO_DATE;
    }
  };
  private static final ThreadLocal<DateTimeFormatter> threadLocalTimeFormatter =
      new ThreadLocal<DateTimeFormatter>()
  {
    @Override
    protected DateTimeFormatter initialValue()
    {
      return DateTimeFormatter.ISO_TIME;
    }
  };

  /**
   * Transform the <code>LocalDate</code> instance into an ISO 8601 string.
   *
   * @param localDate the <code>LocalDate</code> instance to transform into an ISO 8601 string
   *
   * @return the ISO 8601 string for the <code>LocalDate</code> instance
   */
  public static String fromLocalDate(LocalDate localDate)
  {
    return localDate.format(threadLocalDateFormatter.get());
  }

  /**
   * Transform the <code>LocalDateTime</code> instance into an ISO 8601 string.
   *
   * @param localDateTime the <code>LocalDateTime</code> instance to transform into an ISO 8601 string
   *
   * @return the ISO 8601 string for the <code>LocalDateTime</code> instance
   */
  public static String fromLocalDateTime(LocalDateTime localDateTime)
  {
    return localDateTime.atZone(ZoneId.systemDefault()).format(threadLocalDateTimeFormatter.get());
  }

  /**
   * Transform the <code>LocalTime</code> instance into an ISO 8601 string.
   *
   * @param localTime the <code>LocalTime</code> instance to transform into an ISO 8601 string
   *
   * @return the ISO 8601 string for the <code>LocalTime</code> instance
   */
  public static String fromLocalTime(LocalTime localTime)
  {
    return localTime.format(threadLocalTimeFormatter.get());
  }

  /**
   * Get current date and time formatted as ISO 8601 string.
   *
   * @return the current date and time formatted as ISO 8601 string
   */
  public static String now()
  {
    return fromLocalDateTime(LocalDateTime.now());
  }

  /**
   * Transform ISO 8601 string into a <code>LocalDate</code> instance.
   *
   * @param iso8601string the ISO 8601 string to transform
   *
   * @return the <code>LocalDate</code> instance for the ISO 8601 string
   *
   * @throws ParseException
   */
  public static LocalDate toLocalDate(String iso8601string)
    throws ParseException
  {
    return LocalDate.parse(iso8601string, threadLocalDateFormatter.get());
  }

  /**
   * Transform ISO 8601 string into a <code>LocalDateTime</code> instance.
   *
   * @param iso8601string the ISO 8601 string to transform
   *
   * @return the <code>LocalDateTime</code> instance for the ISO 8601 string
   *
   * @throws ParseException
   */
  public static LocalDateTime toLocalDateTime(String iso8601string)
    throws ParseException
  {
    return ZonedDateTime.parse(iso8601string, threadLocalDateTimeFormatter.get())
        .withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();
  }

  /**
   * Transform ISO 8601 string into a <code>LocalTime</code> instance.
   *
   * @param iso8601string the ISO 8601 string to transform
   *
   * @return the <code>LocalTime</code> instance for the ISO 8601 string
   *
   * @throws ParseException
   */
  public static LocalTime toLocalTime(String iso8601string)
    throws ParseException
  {
    return LocalTime.parse(iso8601string, threadLocalTimeFormatter.get());
  }
}

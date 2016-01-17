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

package guru.mmp.common.util;

//~--- JDK imports ------------------------------------------------------------

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * The <code>ISO8601</code> class provides a helper clas for handling ISO 8601 strings of the
 * following format: "2008-03-01T13:00:00+01:00". It also supports parsing the "Z" timezone.
 *
 * @author Marcus Portmann
 */
public final class ISO8601
{
  private static final ThreadLocal<SimpleDateFormat> threadLocalSimpleDateFormat =
      new ThreadLocal<SimpleDateFormat>()
  {
    @Override
    protected SimpleDateFormat initialValue()
    {
      return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
    }
  };

  /**
   * Transform the <code>Calendar</code> instance into an ISO 8601 string.
   *
   * @param calendar the <code>Calendar</code> instance to transform into an ISO 8601 string
   *
   * @return the ISO 8601 string for the <code>Calendar</code> instance
   */
  public static String fromCalendar(Calendar calendar)
  {
    Date date = calendar.getTime();
    String formatted = threadLocalSimpleDateFormat.get().format(date);

    return formatted.substring(0, 22) + ":" + formatted.substring(22);
  }

  /**
   * Transform the <code>Date</code> instance into an ISO 8601 string.
   *
   * @param date the <code>Date</code> instance to transform into an ISO 8601 string
   *
   * @return the ISO 8601 string for the <code>Date</code> instance
   */
  public static String fromDate(Date date)
  {
    String formatted = threadLocalSimpleDateFormat.get().format(date);

    return formatted.substring(0, 22) + ":" + formatted.substring(22);
  }

  /**
   * Get current date and time formatted as ISO 8601 string.
   *
   * @return the current date and time formatted as ISO 8601 string
   */
  public static String now()
  {
    return fromCalendar(GregorianCalendar.getInstance());
  }

  /**
   * Transform ISO 8601 string into a <code>Calendar</code> instance.
   *
   * @param iso8601string the ISO 8601 string to transform
   *
   * @return the ISO 8601 string for the <code>Calendar</code> instance
   *
   * @throws ParseException
   */
  public static Calendar toCalendar(String iso8601string)
    throws ParseException
  {
    Calendar calendar = GregorianCalendar.getInstance();
    String s = iso8601string.replace("Z", "+00:00");

    try
    {
      s = s.substring(0, 22) + s.substring(23);
    }
    catch (IndexOutOfBoundsException e)
    {
      throw new ParseException("Invalid length", 0);
    }

    Date date = threadLocalSimpleDateFormat.get().parse(s);

    calendar.setTime(date);

    return calendar;
  }

  /**
   * Transform ISO 8601 string into a <code>Date</code> instance.
   *
   * @param iso8601string the ISO 8601 string to transform
   *
   * @return the ISO 8601 string for the <code>Date</code> instance
   *
   * @throws ParseException
   */
  public static Date toDate(String iso8601string)
    throws ParseException
  {
    String s = iso8601string.replace("Z", "+00:00");

    try
    {
      s = s.substring(0, 22) + s.substring(23);
    }
    catch (IndexOutOfBoundsException e)
    {
      throw new ParseException("Invalid length", 0);
    }

    return threadLocalSimpleDateFormat.get().parse(s);
  }
}

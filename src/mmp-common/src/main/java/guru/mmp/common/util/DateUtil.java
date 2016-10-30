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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * The <code>StringUtil</code> class is a utility class which provides methods for manipulating
 * <code>Date</code> and <code>Calendar</code> objects.
 *
 * @author Marcus Portmann
 */
public final class DateUtil
{
  private static ThreadLocal<SimpleDateFormat> yyyymmddFormat = new ThreadLocal<SimpleDateFormat>()
  {
    @Override
    protected SimpleDateFormat initialValue()
    {
      return new SimpleDateFormat("yyyy-MM-dd");
    }
  };
  private static ThreadLocal<SimpleDateFormat> yyyymmddWithTimeFormat =
      new ThreadLocal<SimpleDateFormat>()
  {
    @Override
    protected SimpleDateFormat initialValue()
    {
      return new SimpleDateFormat("yyyy-MM-dd HH:mm");
    }
  };

  /**
   * Returns the <b>yyyy-MM-dd</b> thread-local <code>SimpleDateFormat</code>.
   *
   * @return the <b>yyyy-MM-dd</b> thread-local <code>SimpleDateFormat</code>
   */
  public static SimpleDateFormat getYYYYMMDDFormat()
  {
    return yyyymmddFormat.get();
  }

  /**
   * Returns the <b>yyyy-MM-dd HH:mm</b> thread-local <code>SimpleDateFormat</code>.
   *
   * @return the <b>yyyy-MM-dd HH:mm</b> thread-local <code>SimpleDateFormat</code>
   */
  public static SimpleDateFormat getYYYYMMDDWithTimeFormat()
  {
    return yyyymmddWithTimeFormat.get();
  }

  /**
   * Converts a <code>java.util.Date</code> to a <code>java.util.Calendar</code>.
   *
   * @param date the <code>java.util.Date</code> to convert
   *
   * @return the converted <code>java.util.Calendar</code>
   */
  public static Calendar toCalendar(Date date)
  {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);

    return calendar;
  }

  /**
   * Converts a <code>java.util.Calendar</code> to a <code>java.util.Date</code>.
   *
   * @param calendar the <code>java.util.Calendar</code> to convert
   *
   * @return the converted <code>java.util.Date</code>
   */
  public static Date toDate(Calendar calendar)
  {
    return calendar.getTime();
  }
}

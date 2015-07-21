/*
 * Copyright 2015 Marcus Portmann
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

//~--- JDK imports ------------------------------------------------------------

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.GregorianCalendar;

/**
 * The <code>XmlConversionUtil</code> class provides utility methods for converting objects between
 * <code>java.util.Date</code> and <code>javax.xml.datatype.XMLGregorianCalendar</code> types.
 *
 * @author Marcus Portmann
 */
public class XmlConversionUtil
{
  /**
   * The <code>DatatypeFactory</code> instance used to convert java.xml.datatype object that map
   * XML to/from Java objects.
   */
  private static DatatypeFactory datatypeFactory = null;

  static
  {
    try
    {
      datatypeFactory = DatatypeFactory.newInstance();
    }
    catch (DatatypeConfigurationException e)
    {
      throw new IllegalStateException("Failed to create a new DatatypeFactory instance", e);
    }
  }

  /**
   * Converts a <code>javax.xml.datatype.XMLGregorianCalendar</code> instance to a
   * <code>java.util.Date</code> instance.
   *
   * @param calendar the <code>javax.xml.datatype.XMLGregorianCalendar</code> instance to convert
   *
   * @return the converted <code>java.util.Date</code> instance
   */
  public static java.util.Date asDate(XMLGregorianCalendar calendar)
  {
    if (calendar == null)
    {
      return null;
    }
    else
    {
      return calendar.toGregorianCalendar().getTime();
    }
  }

  /**
   * Converts a <code>java.util.Date</code> instance to a
   * <code>javax.xml.datatype.XMLGregorianCalendar</code> instance.
   *
   * @param date the <code>java.util.Date</code> instance to convert
   *
   * @return the converted <code>javax.xml.datatype.XMLGregorianCalendar</code> instance
   */
  public static XMLGregorianCalendar asXMLGregorianCalendar(java.util.Date date)
  {
    if (date == null)
    {
      return null;
    }
    else
    {
      GregorianCalendar calendar = new GregorianCalendar();

      calendar.setTimeInMillis(date.getTime());

      return datatypeFactory.newXMLGregorianCalendar(calendar);
    }
  }
}

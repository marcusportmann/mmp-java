package org.w3._2001.xmlschema;

//~--- JDK imports ------------------------------------------------------------

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.Calendar;

/**
 * Class description
 *
 * @author Marcus Portmann
 * @version 1.0.0, 2014-12-19
 */
public class Adapter1 extends XmlAdapter<String, Calendar>
{
  /**
   * Method description
   *
   * @param value
   *
   * @return
   */
  public String marshal(Calendar value)
  {
    return (guru.mmp.common.service.ws.JaxbDateTimeAdapter.marshal(value));
  }

  /**
   * Method description
   *
   * @param value
   *
   * @return
   */
  public Calendar unmarshal(String value)
  {
    return (guru.mmp.common.service.ws.JaxbDateTimeAdapter.unmarshal(value));
  }
}

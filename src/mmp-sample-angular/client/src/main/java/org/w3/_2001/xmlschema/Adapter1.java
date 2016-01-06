package org.w3._2001.xmlschema;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.Calendar;

public class Adapter1
  extends XmlAdapter<String, Calendar>
{

  public String marshal(Calendar value)
  {
    return (guru.mmp.common.service.ws.JaxbDateTimeAdapter.marshal(value));
  }

  public Calendar unmarshal(String value)
  {
    return (guru.mmp.common.service.ws.JaxbDateTimeAdapter.unmarshal(value));
  }
}

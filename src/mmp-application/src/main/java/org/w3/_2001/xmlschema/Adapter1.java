
package org.w3._2001.xmlschema;

import java.util.Calendar;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public class Adapter1
    extends XmlAdapter<String, Calendar>
{


    public Calendar unmarshal(String value) {
        return (guru.mmp.common.service.ws.JaxbDateTimeAdapter.unmarshal(value));
    }

    public String marshal(Calendar value) {
        return (guru.mmp.common.service.ws.JaxbDateTimeAdapter.marshal(value));
    }

}

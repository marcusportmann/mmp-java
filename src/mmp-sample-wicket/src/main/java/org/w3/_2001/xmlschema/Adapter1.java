
package org.w3._2001.xmlschema;

import java.time.LocalDateTime;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public class Adapter1
    extends XmlAdapter<String, LocalDateTime>
{


    public LocalDateTime unmarshal(String value) {
        return (guru.mmp.common.ws.JaxbDateTimeAdapter.unmarshal(value));
    }

    public String marshal(LocalDateTime value) {
        return (guru.mmp.common.ws.JaxbDateTimeAdapter.marshal(value));
    }

}


package guru.mmp.service.sample.ws;

import javax.jws.WebMethod;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * The Sample service.
 * 
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.8
 * Generated source version: 2.1
 * 
 */
@WebService(name = "ISampleService", targetNamespace = "http://ws.sample.service.mmp.guru")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface ISampleService {


    /**
     * 
     * @return
     *     returns java.lang.String
     * @throws SampleServiceFault
     */
    @WebMethod(operationName = "GetVersion", action = "http://ws.sample.service.mmp.guru/ISampleService/GetVersion")
    @WebResult(name = "out", targetNamespace = "http://ws.sample.service.mmp.guru")
    @RequestWrapper(localName = "GetVersion", targetNamespace = "http://ws.sample.service.mmp.guru", className = "guru.mmp.service.sample.ws.GetVersion")
    @ResponseWrapper(localName = "GetVersionResponse", targetNamespace = "http://ws.sample.service.mmp.guru", className = "guru.mmp.service.sample.ws.GetVersionResponse")
    public String getVersion()
        throws SampleServiceFault
    ;

}

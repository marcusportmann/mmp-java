
package guru.mmp.service.sample.ws;

import javax.xml.ws.WebFault;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.8
 * Generated source version: 2.1
 * 
 */
@WebFault(name = "SampleServiceFaultInfo", targetNamespace = "http://ws.sample.service.mmp.guru")
public class SampleServiceFault
    extends Exception
{

    /**
     * Java type that goes as soapenv:Fault detail element.
     * 
     */
    private SampleServiceFaultInfo faultInfo;

    /**
     * 
     * @param faultInfo
     * @param message
     */
    public SampleServiceFault(String message, SampleServiceFaultInfo faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @param faultInfo
     * @param cause
     * @param message
     */
    public SampleServiceFault(String message, SampleServiceFaultInfo faultInfo, Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @return
     *     returns fault bean: guru.mmp.service.sample.ws.SampleServiceFaultInfo
     */
    public SampleServiceFaultInfo getFaultInfo() {
        return faultInfo;
    }

}

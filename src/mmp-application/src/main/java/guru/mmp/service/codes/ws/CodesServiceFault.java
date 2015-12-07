
package guru.mmp.service.codes.ws;

import javax.xml.ws.WebFault;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.8
 * Generated source version: 2.2
 * 
 */
@WebFault(name = "CodesServiceFaultInfo", targetNamespace = "http://ws.codes.service.mmp.guru")
public class CodesServiceFault
    extends Exception
{

    /**
     * Java type that goes as soapenv:Fault detail element.
     * 
     */
    private CodesServiceFaultInfo faultInfo;

    /**
     * 
     * @param faultInfo
     * @param message
     */
    public CodesServiceFault(String message, CodesServiceFaultInfo faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @param faultInfo
     * @param cause
     * @param message
     */
    public CodesServiceFault(String message, CodesServiceFaultInfo faultInfo, Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @return
     *     returns fault bean: guru.mmp.service.codes.ws.CodesServiceFaultInfo
     */
    public CodesServiceFaultInfo getFaultInfo() {
        return faultInfo;
    }

}

package vim25service;

import javax.xml.ws.WebFault;
import vim25.DvsNotAuthorized;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebFault(name = "DvsNotAuthorizedFault", targetNamespace = "urn:vim25")
public class DvsNotAuthorizedFaultMsg
    extends Exception
{

    /**
     * Java type that goes as soapenv:Fault detail element.
     * 
     */
    private DvsNotAuthorized faultInfo;

    /**
     * 
     * @param faultInfo
     * @param message
     */
    public DvsNotAuthorizedFaultMsg(String message, DvsNotAuthorized faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @param faultInfo
     * @param cause
     * @param message
     */
    public DvsNotAuthorizedFaultMsg(String message, DvsNotAuthorized faultInfo, Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @return
     *     returns fault bean: vim25.DvsNotAuthorized
     */
    public DvsNotAuthorized getFaultInfo() {
        return faultInfo;
    }

}
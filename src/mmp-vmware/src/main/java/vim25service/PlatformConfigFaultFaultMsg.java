
package vim25service;

import javax.xml.ws.WebFault;
import vim25.PlatformConfigFault;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebFault(name = "PlatformConfigFaultFault", targetNamespace = "urn:vim25")
public class PlatformConfigFaultFaultMsg
    extends Exception
{

    /**
     * Java type that goes as soapenv:Fault detail element.
     * 
     */
    private PlatformConfigFault faultInfo;

    /**
     * 
     * @param faultInfo
     * @param message
     */
    public PlatformConfigFaultFaultMsg(String message, PlatformConfigFault faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @param faultInfo
     * @param cause
     * @param message
     */
    public PlatformConfigFaultFaultMsg(String message, PlatformConfigFault faultInfo, Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @return
     *     returns fault bean: vim25.PlatformConfigFault
     */
    public PlatformConfigFault getFaultInfo() {
        return faultInfo;
    }

}

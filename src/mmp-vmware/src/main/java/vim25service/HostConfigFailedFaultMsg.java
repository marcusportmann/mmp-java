
package vim25service;

import javax.xml.ws.WebFault;
import vim25.HostConfigFailed;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebFault(name = "HostConfigFailedFault", targetNamespace = "urn:vim25")
public class HostConfigFailedFaultMsg
    extends Exception
{

    /**
     * Java type that goes as soapenv:Fault detail element.
     * 
     */
    private HostConfigFailed faultInfo;

    /**
     * 
     * @param faultInfo
     * @param message
     */
    public HostConfigFailedFaultMsg(String message, HostConfigFailed faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @param faultInfo
     * @param cause
     * @param message
     */
    public HostConfigFailedFaultMsg(String message, HostConfigFailed faultInfo, Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @return
     *     returns fault bean: vim25.HostConfigFailed
     */
    public HostConfigFailed getFaultInfo() {
        return faultInfo;
    }

}

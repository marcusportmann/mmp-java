
package vim25service;

import javax.xml.ws.WebFault;
import vim25.EVCConfigFault;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.8
 * Generated source version: 2.2
 * 
 */
@WebFault(name = "EVCConfigFaultFault", targetNamespace = "urn:vim25")
public class EVCConfigFaultFaultMsg
    extends Exception
{

    /**
     * Java type that goes as soapenv:Fault detail element.
     * 
     */
    private EVCConfigFault faultInfo;

    /**
     * 
     * @param faultInfo
     * @param message
     */
    public EVCConfigFaultFaultMsg(String message, EVCConfigFault faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @param faultInfo
     * @param cause
     * @param message
     */
    public EVCConfigFaultFaultMsg(String message, EVCConfigFault faultInfo, Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @return
     *     returns fault bean: vim25.EVCConfigFault
     */
    public EVCConfigFault getFaultInfo() {
        return faultInfo;
    }

}

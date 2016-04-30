
package vim25service;

import javax.xml.ws.WebFault;
import vim25.HostConnectFault;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebFault(name = "HostConnectFaultFault", targetNamespace = "urn:vim25")
public class HostConnectFaultFaultMsg
    extends Exception
{

    /**
     * Java type that goes as soapenv:Fault detail element.
     * 
     */
    private HostConnectFault faultInfo;

    /**
     * 
     * @param faultInfo
     * @param message
     */
    public HostConnectFaultFaultMsg(String message, HostConnectFault faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @param faultInfo
     * @param cause
     * @param message
     */
    public HostConnectFaultFaultMsg(String message, HostConnectFault faultInfo, Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @return
     *     returns fault bean: vim25.HostConnectFault
     */
    public HostConnectFault getFaultInfo() {
        return faultInfo;
    }

}

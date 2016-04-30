
package vim25service;

import javax.xml.ws.WebFault;
import vim25.VAppConfigFault;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebFault(name = "VAppConfigFaultFault", targetNamespace = "urn:vim25")
public class VAppConfigFaultFaultMsg
    extends Exception
{

    /**
     * Java type that goes as soapenv:Fault detail element.
     * 
     */
    private VAppConfigFault faultInfo;

    /**
     * 
     * @param faultInfo
     * @param message
     */
    public VAppConfigFaultFaultMsg(String message, VAppConfigFault faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @param faultInfo
     * @param cause
     * @param message
     */
    public VAppConfigFaultFaultMsg(String message, VAppConfigFault faultInfo, Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @return
     *     returns fault bean: vim25.VAppConfigFault
     */
    public VAppConfigFault getFaultInfo() {
        return faultInfo;
    }

}

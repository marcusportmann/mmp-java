
package vim25service;

import javax.xml.ws.WebFault;
import vim25.FcoeFaultPnicHasNoPortSet;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.8
 * Generated source version: 2.2
 * 
 */
@WebFault(name = "FcoeFaultPnicHasNoPortSetFault", targetNamespace = "urn:vim25")
public class FcoeFaultPnicHasNoPortSetFaultMsg
    extends Exception
{

    /**
     * Java type that goes as soapenv:Fault detail element.
     * 
     */
    private FcoeFaultPnicHasNoPortSet faultInfo;

    /**
     * 
     * @param faultInfo
     * @param message
     */
    public FcoeFaultPnicHasNoPortSetFaultMsg(String message, FcoeFaultPnicHasNoPortSet faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @param faultInfo
     * @param cause
     * @param message
     */
    public FcoeFaultPnicHasNoPortSetFaultMsg(String message, FcoeFaultPnicHasNoPortSet faultInfo, Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @return
     *     returns fault bean: vim25.FcoeFaultPnicHasNoPortSet
     */
    public FcoeFaultPnicHasNoPortSet getFaultInfo() {
        return faultInfo;
    }

}

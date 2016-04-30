
package vim25service;

import javax.xml.ws.WebFault;
import vim25.IscsiFaultVnicAlreadyBound;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebFault(name = "IscsiFaultVnicAlreadyBoundFault", targetNamespace = "urn:vim25")
public class IscsiFaultVnicAlreadyBoundFaultMsg
    extends Exception
{

    /**
     * Java type that goes as soapenv:Fault detail element.
     * 
     */
    private IscsiFaultVnicAlreadyBound faultInfo;

    /**
     * 
     * @param faultInfo
     * @param message
     */
    public IscsiFaultVnicAlreadyBoundFaultMsg(String message, IscsiFaultVnicAlreadyBound faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @param faultInfo
     * @param cause
     * @param message
     */
    public IscsiFaultVnicAlreadyBoundFaultMsg(String message, IscsiFaultVnicAlreadyBound faultInfo, Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @return
     *     returns fault bean: vim25.IscsiFaultVnicAlreadyBound
     */
    public IscsiFaultVnicAlreadyBound getFaultInfo() {
        return faultInfo;
    }

}

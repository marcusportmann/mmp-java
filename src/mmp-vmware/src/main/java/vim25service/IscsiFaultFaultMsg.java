
package vim25service;

import javax.xml.ws.WebFault;
import vim25.IscsiFault;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebFault(name = "IscsiFaultFault", targetNamespace = "urn:vim25")
public class IscsiFaultFaultMsg
    extends Exception
{

    /**
     * Java type that goes as soapenv:Fault detail element.
     * 
     */
    private IscsiFault faultInfo;

    /**
     * 
     * @param faultInfo
     * @param message
     */
    public IscsiFaultFaultMsg(String message, IscsiFault faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @param faultInfo
     * @param cause
     * @param message
     */
    public IscsiFaultFaultMsg(String message, IscsiFault faultInfo, Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @return
     *     returns fault bean: vim25.IscsiFault
     */
    public IscsiFault getFaultInfo() {
        return faultInfo;
    }

}

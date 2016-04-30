
package vim25service;

import javax.xml.ws.WebFault;
import vim25.NotSupported;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebFault(name = "NotSupportedFault", targetNamespace = "urn:vim25")
public class NotSupportedFaultMsg
    extends Exception
{

    /**
     * Java type that goes as soapenv:Fault detail element.
     * 
     */
    private NotSupported faultInfo;

    /**
     * 
     * @param faultInfo
     * @param message
     */
    public NotSupportedFaultMsg(String message, NotSupported faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @param faultInfo
     * @param cause
     * @param message
     */
    public NotSupportedFaultMsg(String message, NotSupported faultInfo, Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @return
     *     returns fault bean: vim25.NotSupported
     */
    public NotSupported getFaultInfo() {
        return faultInfo;
    }

}

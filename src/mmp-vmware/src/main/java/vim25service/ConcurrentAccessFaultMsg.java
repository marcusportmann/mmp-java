
package vim25service;

import javax.xml.ws.WebFault;
import vim25.ConcurrentAccess;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebFault(name = "ConcurrentAccessFault", targetNamespace = "urn:vim25")
public class ConcurrentAccessFaultMsg
    extends Exception
{

    /**
     * Java type that goes as soapenv:Fault detail element.
     * 
     */
    private ConcurrentAccess faultInfo;

    /**
     * 
     * @param faultInfo
     * @param message
     */
    public ConcurrentAccessFaultMsg(String message, ConcurrentAccess faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @param faultInfo
     * @param cause
     * @param message
     */
    public ConcurrentAccessFaultMsg(String message, ConcurrentAccess faultInfo, Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @return
     *     returns fault bean: vim25.ConcurrentAccess
     */
    public ConcurrentAccess getFaultInfo() {
        return faultInfo;
    }

}


package vim25service;

import javax.xml.ws.WebFault;
import vim25.InsufficientResourcesFault;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.8
 * Generated source version: 2.2
 * 
 */
@WebFault(name = "InsufficientResourcesFaultFault", targetNamespace = "urn:vim25")
public class InsufficientResourcesFaultFaultMsg
    extends Exception
{

    /**
     * Java type that goes as soapenv:Fault detail element.
     * 
     */
    private InsufficientResourcesFault faultInfo;

    /**
     * 
     * @param faultInfo
     * @param message
     */
    public InsufficientResourcesFaultFaultMsg(String message, InsufficientResourcesFault faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @param faultInfo
     * @param cause
     * @param message
     */
    public InsufficientResourcesFaultFaultMsg(String message, InsufficientResourcesFault faultInfo, Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @return
     *     returns fault bean: vim25.InsufficientResourcesFault
     */
    public InsufficientResourcesFault getFaultInfo() {
        return faultInfo;
    }

}

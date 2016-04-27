
package vim25service;

import javax.xml.ws.WebFault;
import vim25.InvalidArgument;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.8
 * Generated source version: 2.2
 * 
 */
@WebFault(name = "InvalidArgumentFault", targetNamespace = "urn:vim25")
public class InvalidArgumentFaultMsg
    extends Exception
{

    /**
     * Java type that goes as soapenv:Fault detail element.
     * 
     */
    private InvalidArgument faultInfo;

    /**
     * 
     * @param faultInfo
     * @param message
     */
    public InvalidArgumentFaultMsg(String message, InvalidArgument faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @param faultInfo
     * @param cause
     * @param message
     */
    public InvalidArgumentFaultMsg(String message, InvalidArgument faultInfo, Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @return
     *     returns fault bean: vim25.InvalidArgument
     */
    public InvalidArgument getFaultInfo() {
        return faultInfo;
    }

}

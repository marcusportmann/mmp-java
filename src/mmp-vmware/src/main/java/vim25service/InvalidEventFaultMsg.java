
package vim25service;

import javax.xml.ws.WebFault;
import vim25.InvalidEvent;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebFault(name = "InvalidEventFault", targetNamespace = "urn:vim25")
public class InvalidEventFaultMsg
    extends Exception
{

    /**
     * Java type that goes as soapenv:Fault detail element.
     * 
     */
    private InvalidEvent faultInfo;

    /**
     * 
     * @param faultInfo
     * @param message
     */
    public InvalidEventFaultMsg(String message, InvalidEvent faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @param faultInfo
     * @param cause
     * @param message
     */
    public InvalidEventFaultMsg(String message, InvalidEvent faultInfo, Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @return
     *     returns fault bean: vim25.InvalidEvent
     */
    public InvalidEvent getFaultInfo() {
        return faultInfo;
    }

}
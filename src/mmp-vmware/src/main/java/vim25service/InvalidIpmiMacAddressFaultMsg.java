
package vim25service;

import javax.xml.ws.WebFault;
import vim25.InvalidIpmiMacAddress;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebFault(name = "InvalidIpmiMacAddressFault", targetNamespace = "urn:vim25")
public class InvalidIpmiMacAddressFaultMsg
    extends Exception
{

    /**
     * Java type that goes as soapenv:Fault detail element.
     * 
     */
    private InvalidIpmiMacAddress faultInfo;

    /**
     * 
     * @param faultInfo
     * @param message
     */
    public InvalidIpmiMacAddressFaultMsg(String message, InvalidIpmiMacAddress faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @param faultInfo
     * @param cause
     * @param message
     */
    public InvalidIpmiMacAddressFaultMsg(String message, InvalidIpmiMacAddress faultInfo, Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @return
     *     returns fault bean: vim25.InvalidIpmiMacAddress
     */
    public InvalidIpmiMacAddress getFaultInfo() {
        return faultInfo;
    }

}

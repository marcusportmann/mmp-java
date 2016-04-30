
package vim25service;

import javax.xml.ws.WebFault;
import vim25.IscsiFaultVnicHasNoUplinks;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebFault(name = "IscsiFaultVnicHasNoUplinksFault", targetNamespace = "urn:vim25")
public class IscsiFaultVnicHasNoUplinksFaultMsg
    extends Exception
{

    /**
     * Java type that goes as soapenv:Fault detail element.
     * 
     */
    private IscsiFaultVnicHasNoUplinks faultInfo;

    /**
     * 
     * @param faultInfo
     * @param message
     */
    public IscsiFaultVnicHasNoUplinksFaultMsg(String message, IscsiFaultVnicHasNoUplinks faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @param faultInfo
     * @param cause
     * @param message
     */
    public IscsiFaultVnicHasNoUplinksFaultMsg(String message, IscsiFaultVnicHasNoUplinks faultInfo, Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @return
     *     returns fault bean: vim25.IscsiFaultVnicHasNoUplinks
     */
    public IscsiFaultVnicHasNoUplinks getFaultInfo() {
        return faultInfo;
    }

}

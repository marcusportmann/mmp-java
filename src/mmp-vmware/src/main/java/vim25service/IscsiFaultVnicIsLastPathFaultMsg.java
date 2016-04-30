
package vim25service;

import javax.xml.ws.WebFault;
import vim25.IscsiFaultVnicIsLastPath;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebFault(name = "IscsiFaultVnicIsLastPathFault", targetNamespace = "urn:vim25")
public class IscsiFaultVnicIsLastPathFaultMsg
    extends Exception
{

    /**
     * Java type that goes as soapenv:Fault detail element.
     * 
     */
    private IscsiFaultVnicIsLastPath faultInfo;

    /**
     * 
     * @param faultInfo
     * @param message
     */
    public IscsiFaultVnicIsLastPathFaultMsg(String message, IscsiFaultVnicIsLastPath faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @param faultInfo
     * @param cause
     * @param message
     */
    public IscsiFaultVnicIsLastPathFaultMsg(String message, IscsiFaultVnicIsLastPath faultInfo, Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @return
     *     returns fault bean: vim25.IscsiFaultVnicIsLastPath
     */
    public IscsiFaultVnicIsLastPath getFaultInfo() {
        return faultInfo;
    }

}
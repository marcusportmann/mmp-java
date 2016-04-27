
package vim25service;

import javax.xml.ws.WebFault;
import vim25.NoDiskFound;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.8
 * Generated source version: 2.2
 * 
 */
@WebFault(name = "NoDiskFoundFault", targetNamespace = "urn:vim25")
public class NoDiskFoundFaultMsg
    extends Exception
{

    /**
     * Java type that goes as soapenv:Fault detail element.
     * 
     */
    private NoDiskFound faultInfo;

    /**
     * 
     * @param faultInfo
     * @param message
     */
    public NoDiskFoundFaultMsg(String message, NoDiskFound faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @param faultInfo
     * @param cause
     * @param message
     */
    public NoDiskFoundFaultMsg(String message, NoDiskFound faultInfo, Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @return
     *     returns fault bean: vim25.NoDiskFound
     */
    public NoDiskFound getFaultInfo() {
        return faultInfo;
    }

}

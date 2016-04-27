
package vim25service;

import javax.xml.ws.WebFault;
import vim25.ManagedObjectNotFound;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.8
 * Generated source version: 2.2
 * 
 */
@WebFault(name = "ManagedObjectNotFoundFault", targetNamespace = "urn:vim25")
public class ManagedObjectNotFoundFaultMsg
    extends Exception
{

    /**
     * Java type that goes as soapenv:Fault detail element.
     * 
     */
    private ManagedObjectNotFound faultInfo;

    /**
     * 
     * @param faultInfo
     * @param message
     */
    public ManagedObjectNotFoundFaultMsg(String message, ManagedObjectNotFound faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @param faultInfo
     * @param cause
     * @param message
     */
    public ManagedObjectNotFoundFaultMsg(String message, ManagedObjectNotFound faultInfo, Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @return
     *     returns fault bean: vim25.ManagedObjectNotFound
     */
    public ManagedObjectNotFound getFaultInfo() {
        return faultInfo;
    }

}

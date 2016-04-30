
package vim25service;

import javax.xml.ws.WebFault;
import vim25.CannotCreateFile;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebFault(name = "CannotCreateFileFault", targetNamespace = "urn:vim25")
public class CannotCreateFileFaultMsg
    extends Exception
{

    /**
     * Java type that goes as soapenv:Fault detail element.
     * 
     */
    private CannotCreateFile faultInfo;

    /**
     * 
     * @param faultInfo
     * @param message
     */
    public CannotCreateFileFaultMsg(String message, CannotCreateFile faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @param faultInfo
     * @param cause
     * @param message
     */
    public CannotCreateFileFaultMsg(String message, CannotCreateFile faultInfo, Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @return
     *     returns fault bean: vim25.CannotCreateFile
     */
    public CannotCreateFile getFaultInfo() {
        return faultInfo;
    }

}
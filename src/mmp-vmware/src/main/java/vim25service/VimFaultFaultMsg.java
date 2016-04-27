
package vim25service;

import javax.xml.ws.WebFault;
import vim25.VimFault;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.8
 * Generated source version: 2.2
 * 
 */
@WebFault(name = "VimFaultFault", targetNamespace = "urn:vim25")
public class VimFaultFaultMsg
    extends Exception
{

    /**
     * Java type that goes as soapenv:Fault detail element.
     * 
     */
    private VimFault faultInfo;

    /**
     * 
     * @param faultInfo
     * @param message
     */
    public VimFaultFaultMsg(String message, VimFault faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @param faultInfo
     * @param cause
     * @param message
     */
    public VimFaultFaultMsg(String message, VimFault faultInfo, Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @return
     *     returns fault bean: vim25.VimFault
     */
    public VimFault getFaultInfo() {
        return faultInfo;
    }

}

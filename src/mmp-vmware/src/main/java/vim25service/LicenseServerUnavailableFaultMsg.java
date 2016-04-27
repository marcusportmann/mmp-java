
package vim25service;

import javax.xml.ws.WebFault;
import vim25.LicenseServerUnavailable;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.8
 * Generated source version: 2.2
 * 
 */
@WebFault(name = "LicenseServerUnavailableFault", targetNamespace = "urn:vim25")
public class LicenseServerUnavailableFaultMsg
    extends Exception
{

    /**
     * Java type that goes as soapenv:Fault detail element.
     * 
     */
    private LicenseServerUnavailable faultInfo;

    /**
     * 
     * @param faultInfo
     * @param message
     */
    public LicenseServerUnavailableFaultMsg(String message, LicenseServerUnavailable faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @param faultInfo
     * @param cause
     * @param message
     */
    public LicenseServerUnavailableFaultMsg(String message, LicenseServerUnavailable faultInfo, Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @return
     *     returns fault bean: vim25.LicenseServerUnavailable
     */
    public LicenseServerUnavailable getFaultInfo() {
        return faultInfo;
    }

}


package vim25service;

import javax.xml.ws.WebFault;
import vim25.VmFaultToleranceIssue;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebFault(name = "VmFaultToleranceIssueFault", targetNamespace = "urn:vim25")
public class VmFaultToleranceIssueFaultMsg
    extends Exception
{

    /**
     * Java type that goes as soapenv:Fault detail element.
     * 
     */
    private VmFaultToleranceIssue faultInfo;

    /**
     * 
     * @param faultInfo
     * @param message
     */
    public VmFaultToleranceIssueFaultMsg(String message, VmFaultToleranceIssue faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @param faultInfo
     * @param cause
     * @param message
     */
    public VmFaultToleranceIssueFaultMsg(String message, VmFaultToleranceIssue faultInfo, Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @return
     *     returns fault bean: vim25.VmFaultToleranceIssue
     */
    public VmFaultToleranceIssue getFaultInfo() {
        return faultInfo;
    }

}


package guru.mmp.service.codes.ws;

import javax.xml.ws.WebFault;


/**
 * This class was generated by Apache CXF 3.1.12
 * 2017-10-01T13:51:50.766+02:00
 * Generated source version: 3.1.12
 */

@WebFault(name = "CodesServiceFaultInfo", targetNamespace = "http://ws.codes.service.mmp.guru")
public class CodesServiceFault extends Exception {
    
    private guru.mmp.service.codes.ws.CodesServiceFaultInfo codesServiceFaultInfo;

    public CodesServiceFault() {
        super();
    }
    
    public CodesServiceFault(String message) {
        super(message);
    }
    
    public CodesServiceFault(String message, Throwable cause) {
        super(message, cause);
    }

    public CodesServiceFault(String message, guru.mmp.service.codes.ws.CodesServiceFaultInfo codesServiceFaultInfo) {
        super(message);
        this.codesServiceFaultInfo = codesServiceFaultInfo;
    }

    public CodesServiceFault(String message, guru.mmp.service.codes.ws.CodesServiceFaultInfo codesServiceFaultInfo, Throwable cause) {
        super(message, cause);
        this.codesServiceFaultInfo = codesServiceFaultInfo;
    }

    public guru.mmp.service.codes.ws.CodesServiceFaultInfo getFaultInfo() {
        return this.codesServiceFaultInfo;
    }
}


package guru.mmp.service.sample;

import javax.xml.ws.WebFault;


/**
 * This class was generated by Apache CXF 3.1.12
 * 2017-08-15T23:27:37.800+02:00
 * Generated source version: 3.1.12
 */

@WebFault(name = "SampleServiceException", targetNamespace = "http://sample.service.mmp.guru")
public class SampleServiceException extends Exception {
    
    private guru.mmp.model.application.ServiceError sampleServiceException;

    public SampleServiceException() {
        super();
    }
    
    public SampleServiceException(String message) {
        super(message);
    }
    
    public SampleServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public SampleServiceException(String message, guru.mmp.model.application.ServiceError sampleServiceException) {
        super(message);
        this.sampleServiceException = sampleServiceException;
    }

    public SampleServiceException(String message, guru.mmp.model.application.ServiceError sampleServiceException, Throwable cause) {
        super(message, cause);
        this.sampleServiceException = sampleServiceException;
    }

    public guru.mmp.model.application.ServiceError getFaultInfo() {
        return this.sampleServiceException;
    }
}

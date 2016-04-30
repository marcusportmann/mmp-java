
package guru.mmp.service.codes.ws;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebServiceClient(name = "CodesService", targetNamespace = "http://ws.codes.service.mmp.guru", wsdlLocation = "file:/Volumes/Data/Develop/Projects/mmp-java/src/mmp-application/src/main/resources/META-INF/wsdl/CodesService.wsdl")
public class CodesService
    extends Service
{

    private final static URL CODESSERVICE_WSDL_LOCATION;
    private final static WebServiceException CODESSERVICE_EXCEPTION;
    private final static QName CODESSERVICE_QNAME = new QName("http://ws.codes.service.mmp.guru", "CodesService");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("file:/Volumes/Data/Develop/Projects/mmp-java/src/mmp-application/src/main/resources/META-INF/wsdl/CodesService.wsdl");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        CODESSERVICE_WSDL_LOCATION = url;
        CODESSERVICE_EXCEPTION = e;
    }

    public CodesService() {
        super(__getWsdlLocation(), CODESSERVICE_QNAME);
    }

    public CodesService(WebServiceFeature... features) {
        super(__getWsdlLocation(), CODESSERVICE_QNAME, features);
    }

    public CodesService(URL wsdlLocation) {
        super(wsdlLocation, CODESSERVICE_QNAME);
    }

    public CodesService(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, CODESSERVICE_QNAME, features);
    }

    public CodesService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public CodesService(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns ICodesService
     */
    @WebEndpoint(name = "CodesService")
    public ICodesService getCodesService() {
        return super.getPort(new QName("http://ws.codes.service.mmp.guru", "CodesService"), ICodesService.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns ICodesService
     */
    @WebEndpoint(name = "CodesService")
    public ICodesService getCodesService(WebServiceFeature... features) {
        return super.getPort(new QName("http://ws.codes.service.mmp.guru", "CodesService"), ICodesService.class, features);
    }

    private static URL __getWsdlLocation() {
        if (CODESSERVICE_EXCEPTION!= null) {
            throw CODESSERVICE_EXCEPTION;
        }
        return CODESSERVICE_WSDL_LOCATION;
    }

}

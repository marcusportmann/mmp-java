
package guru.mmp.sample.ws;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the guru.mmp.sample.ws package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _InvalidArgumentFaultInfo_QNAME = new QName("http://ws.sample.mmp.guru", "InvalidArgumentFaultInfo");
    private final static QName _ServiceUnavailableFaultInfo_QNAME = new QName("http://ws.sample.mmp.guru", "ServiceUnavailableFaultInfo");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: guru.mmp.sample.ws
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link FaultInfo }
     * 
     */
    public FaultInfo createFaultInfo() {
        return new FaultInfo();
    }

    /**
     * Create an instance of {@link GetVersion }
     * 
     */
    public GetVersion createGetVersion() {
        return new GetVersion();
    }

    /**
     * Create an instance of {@link GetVersionOutput }
     * 
     */
    public GetVersionOutput createGetVersionOutput() {
        return new GetVersionOutput();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FaultInfo }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.sample.mmp.guru", name = "InvalidArgumentFaultInfo")
    public JAXBElement<FaultInfo> createInvalidArgumentFaultInfo(FaultInfo value) {
        return new JAXBElement<FaultInfo>(_InvalidArgumentFaultInfo_QNAME, FaultInfo.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FaultInfo }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.sample.mmp.guru", name = "ServiceUnavailableFaultInfo")
    public JAXBElement<FaultInfo> createServiceUnavailableFaultInfo(FaultInfo value) {
        return new JAXBElement<FaultInfo>(_ServiceUnavailableFaultInfo_QNAME, FaultInfo.class, null, value);
    }

}

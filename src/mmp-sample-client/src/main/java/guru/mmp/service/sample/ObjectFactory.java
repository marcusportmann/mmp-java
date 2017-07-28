
package guru.mmp.service.sample;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the guru.mmp.service.sample package. 
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

    private final static QName _GetVersionResponse_QNAME = new QName("http://sample.service.mmp.guru", "GetVersionResponse");
    private final static QName _TestExceptionHandling_QNAME = new QName("http://sample.service.mmp.guru", "TestExceptionHandling");
    private final static QName _GetDataResponse_QNAME = new QName("http://sample.service.mmp.guru", "GetDataResponse");
    private final static QName _SampleServiceFault_QNAME = new QName("http://sample.service.mmp.guru", "SampleServiceFault");
    private final static QName _GetData_QNAME = new QName("http://sample.service.mmp.guru", "GetData");
    private final static QName _GetVersion_QNAME = new QName("http://sample.service.mmp.guru", "GetVersion");
    private final static QName _TestExceptionHandlingResponse_QNAME = new QName("http://sample.service.mmp.guru", "TestExceptionHandlingResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: guru.mmp.service.sample
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link TestExceptionHandlingResponse }
     * 
     */
    public TestExceptionHandlingResponse createTestExceptionHandlingResponse() {
        return new TestExceptionHandlingResponse();
    }

    /**
     * Create an instance of {@link GetData }
     * 
     */
    public GetData createGetData() {
        return new GetData();
    }

    /**
     * Create an instance of {@link GetVersion }
     * 
     */
    public GetVersion createGetVersion() {
        return new GetVersion();
    }

    /**
     * Create an instance of {@link TestExceptionHandling }
     * 
     */
    public TestExceptionHandling createTestExceptionHandling() {
        return new TestExceptionHandling();
    }

    /**
     * Create an instance of {@link GetDataResponse }
     * 
     */
    public GetDataResponse createGetDataResponse() {
        return new GetDataResponse();
    }

    /**
     * Create an instance of {@link FaultInfo }
     * 
     */
    public FaultInfo createFaultInfo() {
        return new FaultInfo();
    }

    /**
     * Create an instance of {@link GetVersionResponse }
     * 
     */
    public GetVersionResponse createGetVersionResponse() {
        return new GetVersionResponse();
    }

    /**
     * Create an instance of {@link Data }
     * 
     */
    public Data createData() {
        return new Data();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetVersionResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://sample.service.mmp.guru", name = "GetVersionResponse")
    public JAXBElement<GetVersionResponse> createGetVersionResponse(GetVersionResponse value) {
        return new JAXBElement<GetVersionResponse>(_GetVersionResponse_QNAME, GetVersionResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TestExceptionHandling }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://sample.service.mmp.guru", name = "TestExceptionHandling")
    public JAXBElement<TestExceptionHandling> createTestExceptionHandling(TestExceptionHandling value) {
        return new JAXBElement<TestExceptionHandling>(_TestExceptionHandling_QNAME, TestExceptionHandling.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetDataResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://sample.service.mmp.guru", name = "GetDataResponse")
    public JAXBElement<GetDataResponse> createGetDataResponse(GetDataResponse value) {
        return new JAXBElement<GetDataResponse>(_GetDataResponse_QNAME, GetDataResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FaultInfo }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://sample.service.mmp.guru", name = "SampleServiceFault")
    public JAXBElement<FaultInfo> createSampleServiceFault(FaultInfo value) {
        return new JAXBElement<FaultInfo>(_SampleServiceFault_QNAME, FaultInfo.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetData }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://sample.service.mmp.guru", name = "GetData")
    public JAXBElement<GetData> createGetData(GetData value) {
        return new JAXBElement<GetData>(_GetData_QNAME, GetData.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetVersion }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://sample.service.mmp.guru", name = "GetVersion")
    public JAXBElement<GetVersion> createGetVersion(GetVersion value) {
        return new JAXBElement<GetVersion>(_GetVersion_QNAME, GetVersion.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TestExceptionHandlingResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://sample.service.mmp.guru", name = "TestExceptionHandlingResponse")
    public JAXBElement<TestExceptionHandlingResponse> createTestExceptionHandlingResponse(TestExceptionHandlingResponse value) {
        return new JAXBElement<TestExceptionHandlingResponse>(_TestExceptionHandlingResponse_QNAME, TestExceptionHandlingResponse.class, null, value);
    }

}

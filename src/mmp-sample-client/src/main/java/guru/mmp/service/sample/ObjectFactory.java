
package guru.mmp.service.sample;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;
import guru.mmp.model.application.ServiceError;


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

    private final static QName _GetAllData_QNAME = new QName("http://sample.service.mmp.guru", "GetAllData");
    private final static QName _GetAllDataResponse_QNAME = new QName("http://sample.service.mmp.guru", "GetAllDataResponse");
    private final static QName _GetData_QNAME = new QName("http://sample.service.mmp.guru", "GetData");
    private final static QName _GetDataResponse_QNAME = new QName("http://sample.service.mmp.guru", "GetDataResponse");
    private final static QName _GetVersion_QNAME = new QName("http://sample.service.mmp.guru", "GetVersion");
    private final static QName _GetVersionResponse_QNAME = new QName("http://sample.service.mmp.guru", "GetVersionResponse");
    private final static QName _TestExceptionHandling_QNAME = new QName("http://sample.service.mmp.guru", "TestExceptionHandling");
    private final static QName _TestExceptionHandlingResponse_QNAME = new QName("http://sample.service.mmp.guru", "TestExceptionHandlingResponse");
    private final static QName _TestLocalDateTime_QNAME = new QName("http://sample.service.mmp.guru", "TestLocalDateTime");
    private final static QName _TestLocalDateTimeResponse_QNAME = new QName("http://sample.service.mmp.guru", "TestLocalDateTimeResponse");
    private final static QName _TestZonedDateTime_QNAME = new QName("http://sample.service.mmp.guru", "TestZonedDateTime");
    private final static QName _TestZonedDateTimeResponse_QNAME = new QName("http://sample.service.mmp.guru", "TestZonedDateTimeResponse");
    private final static QName _Validate_QNAME = new QName("http://sample.service.mmp.guru", "Validate");
    private final static QName _ValidateResponse_QNAME = new QName("http://sample.service.mmp.guru", "ValidateResponse");
    private final static QName _SampleServiceException_QNAME = new QName("http://sample.service.mmp.guru", "SampleServiceException");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: guru.mmp.service.sample
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetAllData }
     * 
     */
    public GetAllData createGetAllData() {
        return new GetAllData();
    }

    /**
     * Create an instance of {@link GetAllDataResponse }
     * 
     */
    public GetAllDataResponse createGetAllDataResponse() {
        return new GetAllDataResponse();
    }

    /**
     * Create an instance of {@link GetData }
     * 
     */
    public GetData createGetData() {
        return new GetData();
    }

    /**
     * Create an instance of {@link GetDataResponse }
     * 
     */
    public GetDataResponse createGetDataResponse() {
        return new GetDataResponse();
    }

    /**
     * Create an instance of {@link GetVersion }
     * 
     */
    public GetVersion createGetVersion() {
        return new GetVersion();
    }

    /**
     * Create an instance of {@link GetVersionResponse }
     * 
     */
    public GetVersionResponse createGetVersionResponse() {
        return new GetVersionResponse();
    }

    /**
     * Create an instance of {@link TestExceptionHandling }
     * 
     */
    public TestExceptionHandling createTestExceptionHandling() {
        return new TestExceptionHandling();
    }

    /**
     * Create an instance of {@link TestExceptionHandlingResponse }
     * 
     */
    public TestExceptionHandlingResponse createTestExceptionHandlingResponse() {
        return new TestExceptionHandlingResponse();
    }

    /**
     * Create an instance of {@link TestLocalDateTime }
     * 
     */
    public TestLocalDateTime createTestLocalDateTime() {
        return new TestLocalDateTime();
    }

    /**
     * Create an instance of {@link TestLocalDateTimeResponse }
     * 
     */
    public TestLocalDateTimeResponse createTestLocalDateTimeResponse() {
        return new TestLocalDateTimeResponse();
    }

    /**
     * Create an instance of {@link TestZonedDateTime }
     * 
     */
    public TestZonedDateTime createTestZonedDateTime() {
        return new TestZonedDateTime();
    }

    /**
     * Create an instance of {@link TestZonedDateTimeResponse }
     * 
     */
    public TestZonedDateTimeResponse createTestZonedDateTimeResponse() {
        return new TestZonedDateTimeResponse();
    }

    /**
     * Create an instance of {@link Validate }
     * 
     */
    public Validate createValidate() {
        return new Validate();
    }

    /**
     * Create an instance of {@link ValidateResponse }
     * 
     */
    public ValidateResponse createValidateResponse() {
        return new ValidateResponse();
    }

    /**
     * Create an instance of {@link Data }
     * 
     */
    public Data createData() {
        return new Data();
    }

    /**
     * Create an instance of {@link ZonedDateTime }
     * 
     */
    public ZonedDateTime createZonedDateTime() {
        return new ZonedDateTime();
    }

    /**
     * Create an instance of {@link LocalDateTime }
     * 
     */
    public LocalDateTime createLocalDateTime() {
        return new LocalDateTime();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAllData }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://sample.service.mmp.guru", name = "GetAllData")
    public JAXBElement<GetAllData> createGetAllData(GetAllData value) {
        return new JAXBElement<GetAllData>(_GetAllData_QNAME, GetAllData.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAllDataResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://sample.service.mmp.guru", name = "GetAllDataResponse")
    public JAXBElement<GetAllDataResponse> createGetAllDataResponse(GetAllDataResponse value) {
        return new JAXBElement<GetAllDataResponse>(_GetAllDataResponse_QNAME, GetAllDataResponse.class, null, value);
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
     * Create an instance of {@link JAXBElement }{@code <}{@link GetDataResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://sample.service.mmp.guru", name = "GetDataResponse")
    public JAXBElement<GetDataResponse> createGetDataResponse(GetDataResponse value) {
        return new JAXBElement<GetDataResponse>(_GetDataResponse_QNAME, GetDataResponse.class, null, value);
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
     * Create an instance of {@link JAXBElement }{@code <}{@link TestExceptionHandlingResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://sample.service.mmp.guru", name = "TestExceptionHandlingResponse")
    public JAXBElement<TestExceptionHandlingResponse> createTestExceptionHandlingResponse(TestExceptionHandlingResponse value) {
        return new JAXBElement<TestExceptionHandlingResponse>(_TestExceptionHandlingResponse_QNAME, TestExceptionHandlingResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TestLocalDateTime }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://sample.service.mmp.guru", name = "TestLocalDateTime")
    public JAXBElement<TestLocalDateTime> createTestLocalDateTime(TestLocalDateTime value) {
        return new JAXBElement<TestLocalDateTime>(_TestLocalDateTime_QNAME, TestLocalDateTime.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TestLocalDateTimeResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://sample.service.mmp.guru", name = "TestLocalDateTimeResponse")
    public JAXBElement<TestLocalDateTimeResponse> createTestLocalDateTimeResponse(TestLocalDateTimeResponse value) {
        return new JAXBElement<TestLocalDateTimeResponse>(_TestLocalDateTimeResponse_QNAME, TestLocalDateTimeResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TestZonedDateTime }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://sample.service.mmp.guru", name = "TestZonedDateTime")
    public JAXBElement<TestZonedDateTime> createTestZonedDateTime(TestZonedDateTime value) {
        return new JAXBElement<TestZonedDateTime>(_TestZonedDateTime_QNAME, TestZonedDateTime.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TestZonedDateTimeResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://sample.service.mmp.guru", name = "TestZonedDateTimeResponse")
    public JAXBElement<TestZonedDateTimeResponse> createTestZonedDateTimeResponse(TestZonedDateTimeResponse value) {
        return new JAXBElement<TestZonedDateTimeResponse>(_TestZonedDateTimeResponse_QNAME, TestZonedDateTimeResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Validate }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://sample.service.mmp.guru", name = "Validate")
    public JAXBElement<Validate> createValidate(Validate value) {
        return new JAXBElement<Validate>(_Validate_QNAME, Validate.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ValidateResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://sample.service.mmp.guru", name = "ValidateResponse")
    public JAXBElement<ValidateResponse> createValidateResponse(ValidateResponse value) {
        return new JAXBElement<ValidateResponse>(_ValidateResponse_QNAME, ValidateResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ServiceError }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://sample.service.mmp.guru", name = "SampleServiceException")
    public JAXBElement<ServiceError> createSampleServiceException(ServiceError value) {
        return new JAXBElement<ServiceError>(_SampleServiceException_QNAME, ServiceError.class, null, value);
    }

}

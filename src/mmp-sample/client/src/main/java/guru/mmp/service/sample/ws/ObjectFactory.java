package guru.mmp.service.sample.ws;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the guru.mmp.service.sample.ws package.
 * <p>An ObjectFactory allows you to programatically
 * construct new instances of the Java representation
 * for XML content. The Java representation of XML
 * content can consist of schema derived interfaces
 * and classes representing the binding of schema
 * type definitions, element declarations and model
 * groups.  Factory methods for each of these are
 * provided in this class.
 */
@XmlRegistry
public class ObjectFactory
{

  private final static QName _SampleServiceFaultInfoDetail_QNAME = new QName(
    "http://ws.sample.service.mmp.guru", "Detail");

  private final static QName _SampleServiceFaultInfoMessage_QNAME = new QName(
    "http://ws.sample.service.mmp.guru", "Message");

  private final static QName _SampleServiceFaultInfo_QNAME = new QName(
    "http://ws.sample.service.mmp.guru", "SampleServiceFaultInfo");

  /**
   * Create a new ObjectFactory that can be used to create new instances of schema derived
   * classes for package: guru.mmp.service.sample.ws
   */
  public ObjectFactory()
  {
  }

  /**
   * Create an instance of {@link GetVersion }
   */
  public GetVersion createGetVersion()
  {
    return new GetVersion();
  }

  /**
   * Create an instance of {@link GetVersionResponse }
   */
  public GetVersionResponse createGetVersionResponse()
  {
    return new GetVersionResponse();
  }

  /**
   * Create an instance of {@link SampleServiceFaultInfo }
   */
  public SampleServiceFaultInfo createSampleServiceFaultInfo()
  {
    return new SampleServiceFaultInfo();
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}{@link SampleServiceFaultInfo }{@code >}}
   */
  @XmlElementDecl(namespace = "http://ws.sample.service.mmp.guru", name = "SampleServiceFaultInfo")
  public JAXBElement<SampleServiceFaultInfo> createSampleServiceFaultInfo(
    SampleServiceFaultInfo value)
  {
    return new JAXBElement<SampleServiceFaultInfo>(_SampleServiceFaultInfo_QNAME,
      SampleServiceFaultInfo.class, null, value);
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
   */
  @XmlElementDecl(namespace = "http://ws.sample.service.mmp.guru", name = "Detail", scope =
    SampleServiceFaultInfo.class)
  public JAXBElement<String> createSampleServiceFaultInfoDetail(String value)
  {
    return new JAXBElement<String>(_SampleServiceFaultInfoDetail_QNAME, String.class,
      SampleServiceFaultInfo.class, value);
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
   */
  @XmlElementDecl(namespace = "http://ws.sample.service.mmp.guru", name = "Message", scope =
    SampleServiceFaultInfo.class)
  public JAXBElement<String> createSampleServiceFaultInfoMessage(String value)
  {
    return new JAXBElement<String>(_SampleServiceFaultInfoMessage_QNAME, String.class,
      SampleServiceFaultInfo.class, value);
  }
}

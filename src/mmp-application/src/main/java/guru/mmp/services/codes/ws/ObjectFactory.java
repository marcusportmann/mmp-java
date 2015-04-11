
package guru.mmp.services.codes.ws;

//~--- JDK imports ------------------------------------------------------------

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the guru.mmp.services.codes.ws package.
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
  private final static QName _CodesServiceFaultInfo_QNAME =
    new QName("http://ws.codes.services.mmp.guru", "CodesServiceFaultInfo");
  private final static QName _CodesServiceFaultInfoMessage_QNAME =
    new QName("http://ws.codes.services.mmp.guru", "Message");
  private final static QName _CodesServiceFaultInfoDetail_QNAME =
    new QName("http://ws.codes.services.mmp.guru", "Detail");

  /**
   * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: guru.mmp.services.codes.ws
   */
  public ObjectFactory() {}

  /**
   * Create an instance of {@link Code }
   *
   * @return
   */
  public Code createCode()
  {
    return new Code();
  }

  /**
   * Create an instance of {@link CodeCategory }
   *
   * @return
   */
  public CodeCategory createCodeCategory()
  {
    return new CodeCategory();
  }

  /**
   * Create an instance of {@link CodesServiceFaultInfo }
   *
   * @return
   */
  public CodesServiceFaultInfo createCodesServiceFaultInfo()
  {
    return new CodesServiceFaultInfo();
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}{@link CodesServiceFaultInfo }{@code >}}
   *
   * @param value
   * @return
   */
  @XmlElementDecl(namespace = "http://ws.codes.services.mmp.guru", name = "CodesServiceFaultInfo")
  public JAXBElement<CodesServiceFaultInfo> createCodesServiceFaultInfo(CodesServiceFaultInfo value)
  {
    return new JAXBElement<CodesServiceFaultInfo>(_CodesServiceFaultInfo_QNAME,
        CodesServiceFaultInfo.class, null, value);
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
   *
   * @param value
   * @return
   */
  @XmlElementDecl(namespace = "http://ws.codes.services.mmp.guru", name = "Detail",
      scope = CodesServiceFaultInfo.class)
  public JAXBElement<String> createCodesServiceFaultInfoDetail(String value)
  {
    return new JAXBElement<String>(_CodesServiceFaultInfoDetail_QNAME, String.class,
        CodesServiceFaultInfo.class, value);
  }

  /**
   * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
   *
   * @param value
   * @return
   */
  @XmlElementDecl(namespace = "http://ws.codes.services.mmp.guru", name = "Message",
      scope = CodesServiceFaultInfo.class)
  public JAXBElement<String> createCodesServiceFaultInfoMessage(String value)
  {
    return new JAXBElement<String>(_CodesServiceFaultInfoMessage_QNAME, String.class,
        CodesServiceFaultInfo.class, value);
  }

  /**
   * Create an instance of {@link GetCodeCategory }
   *
   * @return
   */
  public GetCodeCategory createGetCodeCategory()
  {
    return new GetCodeCategory();
  }

  /**
   * Create an instance of {@link GetCodeCategoryResponse }
   *
   * @return
   */
  public GetCodeCategoryResponse createGetCodeCategoryResponse()
  {
    return new GetCodeCategoryResponse();
  }

  /**
   * Create an instance of {@link GetCodeCategoryWithParameters }
   *
   * @return
   */
  public GetCodeCategoryWithParameters createGetCodeCategoryWithParameters()
  {
    return new GetCodeCategoryWithParameters();
  }

  /**
   * Create an instance of {@link GetCodeCategoryWithParametersResponse }
   *
   * @return
   */
  public GetCodeCategoryWithParametersResponse createGetCodeCategoryWithParametersResponse()
  {
    return new GetCodeCategoryWithParametersResponse();
  }

  /**
   * Create an instance of {@link GetVersion }
   *
   * @return
   */
  public GetVersion createGetVersion()
  {
    return new GetVersion();
  }

  /**
   * Create an instance of {@link GetVersionResponse }
   *
   * @return
   */
  public GetVersionResponse createGetVersionResponse()
  {
    return new GetVersionResponse();
  }

  /**
   * Create an instance of {@link Parameter }
   *
   * @return
   */
  public Parameter createParameter()
  {
    return new Parameter();
  }
}

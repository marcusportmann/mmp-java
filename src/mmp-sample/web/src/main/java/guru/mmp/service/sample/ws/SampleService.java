package guru.mmp.service.sample.ws;

import javax.xml.namespace.QName;
import javax.xml.ws.*;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.8
 * Generated source version: 2.1
 */
@WebServiceClient(name = "SampleService", targetNamespace = "http://ws.sample.service.mmp.guru",
  wsdlLocation = "file:/C:/Data/Develop/MMP/mmp-java/src/mmp-sample/web/src/main/webapp/WEB-INF" +
    "/wsdl/SampleService.wsdl")
public class SampleService
  extends Service
{

  private final static WebServiceException SAMPLESERVICE_EXCEPTION;

  private final static QName SAMPLESERVICE_QNAME = new QName("http://ws.sample.service.mmp.guru",
    "SampleService");

  private final static URL SAMPLESERVICE_WSDL_LOCATION;

  static
  {
    URL url = null;
    WebServiceException e = null;
    try
    {
      url = new URL(
        "file:/C:/Data/Develop/MMP/mmp-java/src/mmp-sample/web/src/main/webapp/WEB-INF/wsdl" +
          "/SampleService.wsdl");
    }
    catch (MalformedURLException ex)
    {
      e = new WebServiceException(ex);
    }
    SAMPLESERVICE_WSDL_LOCATION = url;
    SAMPLESERVICE_EXCEPTION = e;
  }

  private static URL __getWsdlLocation()
  {
    if (SAMPLESERVICE_EXCEPTION != null)
    {
      throw SAMPLESERVICE_EXCEPTION;
    }
    return SAMPLESERVICE_WSDL_LOCATION;
  }

  public SampleService()
  {
    super(__getWsdlLocation(), SAMPLESERVICE_QNAME);
  }

  public SampleService(URL wsdlLocation, QName serviceName)
  {
    super(wsdlLocation, serviceName);
  }

  /**
   * @return returns ISampleService
   */
  @WebEndpoint(name = "SampleService")
  public ISampleService getSampleService()
  {
    return super.getPort(new QName("http://ws.sample.service.mmp.guru", "SampleService"),
      ISampleService.class);
  }

  /**
   * @param features A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy
   *                 .  Supported features not in the <code>features</code> parameter will have
   *                 their default values.
   *
   * @return returns ISampleService
   */
  @WebEndpoint(name = "SampleService")
  public ISampleService getSampleService(WebServiceFeature... features)
  {
    return super.getPort(new QName("http://ws.sample.service.mmp.guru", "SampleService"),
      ISampleService.class, features);
  }
}

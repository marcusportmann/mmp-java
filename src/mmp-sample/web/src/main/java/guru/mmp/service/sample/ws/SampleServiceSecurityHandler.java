package guru.mmp.service.sample.ws;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.common.service.ws.security.WebServiceSecurityHandler;

/**
 * The <code>SampleServiceSecurityHandler</code> provides the JAX-WS security handler
 * for the Sample Service.
 */
public class SampleServiceSecurityHandler extends WebServiceSecurityHandler
{
  /**
   * Constructs a new <code>SampleServiceSecurityHandler</code>.
   */
  public SampleServiceSecurityHandler()
  {
    super("SampleService");
  }
}

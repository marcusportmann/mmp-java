/*
 * Copyright 2017 Marcus Portmann
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package guru.mmp.sample.client;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.common.security.context.ApplicationSecurityContext;
import guru.mmp.common.util.StringUtil;
import guru.mmp.common.ws.security.WebServiceClientSecurityHelper;
import guru.mmp.sample.ws.ISampleService;
import guru.mmp.sample.ws.SampleService;
import guru.mmp.sample.ws.ServiceUnavailableFault;

/**
 * The <code>SampleClient</code> class implements the Sample Web Service client.
 */
public class SampleClient
{
  /**
   * The Sample Service endpoint.
   */
  public static final String SAMPLE_SERVICE_ENDPOINT =
      "https://localhost:8443/service/SampleService";

  /**
   * The path to the classpath resource containing the WSDL for the Sample Service.
   */
  public static final String SAMPLE_SERVICE_WSDL = "META-INF/wsdl/SampleService.wsdl";

  /**
   * The main method.
   *
   * @param args the command-line arguments
   */
  public static void main(String[] args)
  {
    try
    {
      // Initialise the application security context
      ApplicationSecurityContext applicationSecurityContext =
          ApplicationSecurityContext.getContext();

      applicationSecurityContext.init("Sample");

      ISampleService sampleService = WebServiceClientSecurityHelper.getMutualSSLServiceProxy(
          SampleService.class, ISampleService.class, SAMPLE_SERVICE_WSDL, SAMPLE_SERVICE_ENDPOINT);

      System.out.println("sampleService.getVersion() = " + sampleService.getVersion());
    }
    catch (ServiceUnavailableFault e)
    {
      System.out.println("[ERROR] " + e.getMessage());
      e.printStackTrace(System.out);

      if (!StringUtil.isNullOrEmpty(e.getFaultInfo().getMessage()))
      {
        System.out.println();
        System.out.println(e.getFaultInfo().getMessage());
      }

      if (!StringUtil.isNullOrEmpty(e.getFaultInfo().getDetail()))
      {
        System.out.println();
        System.out.println(e.getFaultInfo().getDetail());
      }
    }
    catch (Throwable e)
    {
      System.out.println("[ERROR] " + e.getMessage());
      e.printStackTrace(System.out);
    }
  }
}

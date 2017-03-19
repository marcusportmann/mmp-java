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

package guru.mmp.service.sample.ws;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.common.service.ws.security.WebServiceSecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jws.HandlerChain;
import javax.jws.WebMethod;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>SampleServiceBean</code> class implements the Sample Service.
 */
@HandlerChain(file = "/META-INF/SampleServiceSecurity.xml")
@WebService(name = "ISampleService", portName = "SampleService", serviceName = "SampleService",
    targetNamespace = "http://ws.sample.services.mmp.guru")
@SuppressWarnings("unused")
public class SampleServiceBean
{
  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(SampleServiceBean.class);

  /**
   * Returns the version for the web service.
   *
   * @return the version for the web service
   */
  @WebMethod(operationName = "GetVersion",
      action = "http://ws.sample.service.mmp.guru/ISampleService/GetVersion")
  @WebResult(name = "out", targetNamespace = "http://ws.sample.service.mmp.guru")
  @RequestWrapper(localName = "GetVersion", targetNamespace = "http://ws.sample.service.mmp.guru",
      className = "guru.mmp.service.sample.ws.GetVersion")
  @ResponseWrapper(localName = "GetVersionResponse",
      targetNamespace = "http://ws.sample.service.mmp.guru",
      className = "guru.mmp.service.sample.ws.GetVersionResponse")
  public String getVersion()
  {
    if (WebServiceSecurityContext.getContext().getClientCertificate() != null)
    {
      logger.info("Found client certificate (" + WebServiceSecurityContext.getContext()
          .getClientCertificate().getSubjectDN().toString() + ")");
    }
    else
    {
      logger.warn("Failed to find the client certificate");
    }

    return "1.0.0";
  }
}

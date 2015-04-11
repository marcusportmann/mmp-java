/*
 * Copyright 2014 Marcus Portmann
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

package guru.mmp.common.service.ws.security;

//~--- JDK imports ------------------------------------------------------------

import java.security.cert.X509Certificate;

/**
 * The <code>SecureWebService</code> class provides a common base class for all secure web services.
 * This class provides utility methods which return information related to the MMP Web Service
 * Security Model.
 *
 * @author Marcus Portmann
 */
public class SecureWebService
{
  /**
   * Returns the X509 certificate identifying the web service client that executed the secure web
   * service operation.
   *
   * @return the X509 certificate identifying the web service client that executed the secure web
   *         service operation
   */
  public X509Certificate getClientCertificate()
  {
    WebServiceSecurityContext securityContext = WebServiceSecurityContext.getContext();

    return securityContext.getClientCertificate();
  }
}

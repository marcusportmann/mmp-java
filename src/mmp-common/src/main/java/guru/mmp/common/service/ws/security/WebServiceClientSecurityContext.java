/*
 * Copyright 2015 Marcus Portmann
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
 * The <code>WebServiceSecurityClientContext</code> class is used to store the security information
 * for a web service client invoking a secure web service operation.
 *
 * @author Marcus Portmann
 */
public class WebServiceClientSecurityContext
{
  private static ThreadLocal<WebServiceClientSecurityContext> threadLocalWebServiceClientSecurityContext =
    new ThreadLocal<WebServiceClientSecurityContext>()
  {
    @Override
    protected WebServiceClientSecurityContext initialValue()
    {
      return new WebServiceClientSecurityContext();
    }

  };

  /**
   * The X509 certificate identifying the web service that provides the secure web service
   * operation.
   */
  private X509Certificate serviceCertificate;

  /**
   * Returns the web service client security context stored in thread-local storage.
   *
   * @return the web service client security context stored in thread-local storage
   */
  public static WebServiceClientSecurityContext getContext()
  {
    return threadLocalWebServiceClientSecurityContext.get();
  }

  /**
   * Returns the X509 certificate identifying the web service that provides the secure web service
   * operation.
   *
   * @return the X509 certificate identifying the web service that provides the secure web service
   *         operation
   */
  public X509Certificate getServiceCertificate()
  {
    return serviceCertificate;
  }

  /**
   * Reset the web service client security context removing all the security information associated
   * with the execution of a secure web service operation.
   */
  public void reset()
  {
    serviceCertificate = null;
  }

  /**
   * Set the X509 certificate identifying the web service that provides the secure web service
   * operation.
   *
   * @param serviceCertificate the X509 certificate identifying the web service that provides the
   *                           secure web service operation
   */
  public void setServiceCertificate(X509Certificate serviceCertificate)
  {
    this.serviceCertificate = serviceCertificate;
  }
}

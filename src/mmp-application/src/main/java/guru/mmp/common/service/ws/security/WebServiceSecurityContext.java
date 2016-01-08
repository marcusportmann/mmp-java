/*
 * Copyright 2016 Marcus Portmann
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

import java.security.cert.X509Certificate;

/**
 * The <code>WebServiceSecurityContext</code> class is used to store the security information
 * associated with the execution of a secure web service operation. The information is stored in a
 * thread-local variable.
 *
 * @author Marcus Portmann
 */
public class WebServiceSecurityContext
{
  private static ThreadLocal<WebServiceSecurityContext> threadLocalWebServiceSecurityContext =
    new ThreadLocal<WebServiceSecurityContext>()
  {
    @Override
    protected WebServiceSecurityContext initialValue()
    {
      return new WebServiceSecurityContext();
    }
  };

  /**
   * The X509 certificate identifying the web service client that executed the secure web service
   * operation.
   */
  private X509Certificate clientCertificate;

  /**
   * Returns the web service security context stored in thread-local storage.
   *
   * @return the web service security context stored in thread-local storage
   */
  public static WebServiceSecurityContext getContext()
  {
    return threadLocalWebServiceSecurityContext.get();
  }

  /**
   * Returns the X509 certificate identifying the web service client that executed the secure web
   * service operation.
   *
   * @return the X509 certificate identifying the web service client that executed the secure web
   * service operation
   */
  public X509Certificate getClientCertificate()
  {
    return clientCertificate;
  }

  /**
   * Reset the web service security context removing all the security information associated
   * with the execution of a secure web service operation.
   */
  public void reset()
  {
    clientCertificate = null;
  }

  /**
   * Set the X509 certificate identifying the remote application that executed the secure web
   * service operation.
   *
   * @param clientCertificate the X509 certificate identifying the remote application that executed
   *                          the secure web service operation
   */
  public void setClientCertificate(X509Certificate clientCertificate)
  {
    this.clientCertificate = clientCertificate;
  }
}

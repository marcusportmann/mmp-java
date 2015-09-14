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

//~--- non-JDK imports --------------------------------------------------------

import org.apache.ws.security.WSSecurityException;
import org.apache.ws.security.components.crypto.Crypto;
import org.apache.ws.security.handler.RequestData;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//~--- JDK imports ------------------------------------------------------------

import java.security.cert.X509Certificate;

import java.util.Collection;
import java.util.regex.Pattern;

/**
 * The <code>SignatureTrustValidator</code> class implements a custom signature trust validator.
 *
 * @author Marcus Portmann
 */
public class WebServiceSignatureTrustValidator
  extends org.apache.ws.security.validate.SignatureTrustValidator
{
  /* Logger */
  private static final Logger logger =
    LoggerFactory.getLogger(WebServiceSignatureTrustValidator.class);

  /**
   * Returns <code>true</code> if the certificate's SubjectDN matches the constraints defined in
   * the subject DNConstraints; <code>false</code>, otherwise. The certificate subject DN only has
   * to match ONE of the subject cert constraints (not all).
   *
   * @return <code>true</code> if the certificate's SubjectDN matches the constraints defined in
   *         the subject DNConstraints; <code>false</code>, otherwise. The certificate subject DN
   *         only has to match ONE of the subject cert constraints (not all)
   */

  @Override
  protected boolean matches(java.security.cert.X509Certificate cert,
      Collection<Pattern> subjectDNPatterns)
  {
    return (subjectDNPatterns == null) || (subjectDNPatterns.size() == 0)
        || super.matches(cert, subjectDNPatterns);
  }

  @Override
  protected boolean verifyTrustInCert(X509Certificate cert, Crypto crypto, RequestData data,
      boolean enableRevocation)
    throws WSSecurityException
  {
    if (super.verifyTrustInCert(cert, crypto, data, enableRevocation))
    {
      /*
       * Retrieve the web service security context associated with the current thread of execution
       * and reset it. The context is stored in thread-local storage.
       */
      WebServiceSecurityContext securityContext = WebServiceSecurityContext.getContext();

      // Save the client's certificate on the web service security context
      securityContext.setClientCertificate(cert);

      if (logger.isDebugEnabled())
      {
        logger.debug("Successfully verified the trust for the client certificate ("
            + cert.getSubjectDN().getName() + ")");
      }

      return true;
    }
    else
    {
      logger.warn("Failed to verify the trust for the client certificate ("
          + cert.getSubjectDN().getName() + ")");

      return false;
    }
  }

  @Override
  protected boolean verifyTrustInCerts(X509Certificate[] certificates, Crypto crypto,
      RequestData data, boolean enableRevocation)
    throws WSSecurityException
  {
    if ((certificates == null) || (certificates.length == 0))
    {
      logger.warn("Failed to verify the trust for the invalid client certificate chain");

      return false;
    }

    if (super.verifyTrustInCerts(certificates, crypto, data, enableRevocation))
    {
      /*
       * Retrieve the web service security context associated with the current thread of execution
       * and reset it. The context is stored in thread-local storage.
       */
      WebServiceSecurityContext securityContext = WebServiceSecurityContext.getContext();

      // Save the client's certificate on the web service security context
      securityContext.setClientCertificate(certificates[0]);

      if (logger.isDebugEnabled())
      {
        logger.debug("Successfully verified the trust for the client certificate ("
            + certificates[0].getSubjectDN().getName() + ")");
      }

      return true;
    }
    else
    {
      logger.warn("Failed to verify the trust for the client certificate ("
          + certificates[0].getSubjectDN().getName() + ")");

      return false;
    }
  }
}

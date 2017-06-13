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

package guru.mmp.common.ws.security;

//~--- non-JDK imports --------------------------------------------------------

import org.apache.ws.security.WSSecurityException;
import org.apache.ws.security.components.crypto.Crypto;
import org.apache.ws.security.handler.RequestData;

import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.regex.Pattern;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>SignatureTrustValidator</code> class implements a custom signature trust validator.
 *
 * @author Marcus Portmann
 */
public class WebServiceClientSignatureTrustValidator extends org.apache.ws.security.validate
    .SignatureTrustValidator
{
  /**
   * Returns <code>true</code> if the certificate's SubjectDN matches the constraints defined in
   * the subject DNConstraints; <code>false</code>, otherwise. The certificate subject DN only has
   * to match ONE of the subject cert constraints (not all).
   *
   * @return <code>true</code> if the certificate's SubjectDN matches the constraints defined in
   * the subject DNConstraints; <code>false</code>, otherwise. The certificate subject DN
   * only has to match ONE of the subject cert constraints (not all)
   */
  @Override
  protected boolean matches(java.security.cert.X509Certificate cert,
      Collection<Pattern> subjectDNPatterns)
  {
    return (subjectDNPatterns == null)
        || (subjectDNPatterns.size() == 0)
        || super.matches(cert, subjectDNPatterns);
  }

  @Override
  protected boolean verifyTrustInCert(X509Certificate cert, Crypto crypto, RequestData data,
      boolean enableRevocation)
    throws WSSecurityException
  {
    return super.verifyTrustInCert(cert, crypto, data, enableRevocation);
  }

  @Override
  protected boolean verifyTrustInCerts(X509Certificate[] certificates, Crypto crypto,
      RequestData data, boolean enableRevocation)
    throws WSSecurityException
  {
    return super.verifyTrustInCerts(certificates, crypto, data, enableRevocation);
  }
}

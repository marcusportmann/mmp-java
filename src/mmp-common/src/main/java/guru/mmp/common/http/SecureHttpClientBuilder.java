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

package guru.mmp.common.http;

//~--- non-JDK imports --------------------------------------------------------

import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//~--- JDK imports ------------------------------------------------------------

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.*;

/**
 * The <code>SecureHttpClient</code> class implements a secure HTTP client builder.
 *
 * @author Marcus Portmann
 */
public class SecureHttpClientBuilder extends HttpClientBuilder
{
  /* Logger */
  @SuppressWarnings("unused")
  private static final Logger logger = LoggerFactory.getLogger(SecureHttpClientBuilder.class);
  private SSLConnectionSocketFactory sslSocketFactory;

  /**
   * Constructs a new <code>SecureHttpClient</code>.
   *
   */
  public SecureHttpClientBuilder()
  {
    SSLConnectionSocketFactory sslConnectionSocketFactory = getSSLConnectionSocketFactory();

    Registry<ConnectionSocketFactory> socketFactoryRegistry =
      RegistryBuilder.<ConnectionSocketFactory>create().register("https",
        sslConnectionSocketFactory).register("http", new PlainConnectionSocketFactory()).build();

    PoolingHttpClientConnectionManager connectionManager =
      new PoolingHttpClientConnectionManager(socketFactoryRegistry);

    setConnectionManager(connectionManager);
  }

  private synchronized SSLConnectionSocketFactory getSSLConnectionSocketFactory()
  {
    if (sslSocketFactory == null)
    {
      try
      {
        SSLContext sslContext = SSLContext.getInstance("TLS");

        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager()
        {
          public void checkClientTrusted(X509Certificate[] chain, String authType)
            throws CertificateException
          {
            // Skip client verification step
          }

          public void checkServerTrusted(X509Certificate[] chain, String authType)
            throws CertificateException
          {
            // Skip server verification step
          }

          public X509Certificate[] getAcceptedIssuers()
          {
            return new X509Certificate[0];
          }
        } };

        sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

        sslSocketFactory = new SSLConnectionSocketFactory(sslContext.getSocketFactory(),
            new HostnameVerifier()
        {
          @Override
          public boolean verify(String hostname, SSLSession sslSession)
          {
            // TODO: Implement proper verification of the server identity -- MARCUS

            return true;

//          if (hostname.equalsIgnoreCase(sslSession.getPeerHost()))
//          {
//            return true;
//          }
//          else
//          {
//            logger.error("Failed to verify the SSL connection to the host ("
// + hostname + ") which returned a certificate for the host (" + sslSession.getPeerHost() + ")");
//
//            return false;
//          }
          }
        });
      }
      catch (Throwable e)
      {
        throw new RuntimeException("Failed to create the no-trust SSL socket factory", e);
      }
    }

    return sslSocketFactory;
  }
}

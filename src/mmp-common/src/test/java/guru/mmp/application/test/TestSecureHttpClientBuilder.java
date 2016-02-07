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

package guru.mmp.application.test;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.common.http.SecureHttpClientBuilder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//~--- JDK imports ------------------------------------------------------------

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * The <code>SecurityHttpClientBuilderTest</code> class contains the implementation of the JUnit
 * tests that test the secure HTTP client functionality.
 *
 * @author Marcus Portmann
 */
public class TestSecureHttpClientBuilder
{
  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(TestSecureHttpClientBuilder.class);

  /**
   * Test the secure HTTP client functionality.
   *
   * @throws Exception
   */

  // @Test
  public void securityHttpClientBuilderTest()
    throws Exception
  {
    String endPoint = "https://www.google.com";

    HttpClient httpClient = null;

    // Configure the connection
    SecureHttpClientBuilder secureHttpClientBuilder = new SecureHttpClientBuilder();

    RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(30000)
        .setConnectTimeout(30000).setSocketTimeout(30000).build();

    secureHttpClientBuilder.setDefaultRequestConfig(requestConfig);

    httpClient = secureHttpClientBuilder.build();

    HttpGet httpGet = new HttpGet(endPoint);

    HttpResponse response = httpClient.execute(httpGet);

    HttpEntity entity = response.getEntity();

    if (entity != null)
    {
      InputStream in = entity.getContent();

      ByteArrayOutputStream baos = new ByteArrayOutputStream();

      int numberOfBytesRead = 0;

      byte[] readBuffer = new byte[2048];

      while ((numberOfBytesRead = in.read(readBuffer)) != -1)
      {
        baos.write(readBuffer, 0, numberOfBytesRead);
      }

      System.out.println(new String(baos.toByteArray()));

      return;
    }
    else
    {
      throw new RuntimeException("No HTTP response entity found");
    }
  }

  /**
   * Returns the logger for the derived JUnit tests class.
   *
   * @return the logger for the derived JUnit tests class
   */
  protected Logger getLogger()
  {
    return logger;
  }
}

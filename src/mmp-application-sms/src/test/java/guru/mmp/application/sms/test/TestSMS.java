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

package guru.mmp.application.sms.test;

//~--- non-JDK imports --------------------------------------------------------

import com.mymobileapi.api5.API;
import com.mymobileapi.api5.APISoap;
import guru.mmp.common.util.StringUtil;
import guru.mmp.common.xml.XmlParserErrorHandler;
import guru.mmp.common.xml.XmlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.ws.BindingProvider;
import java.io.StringReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>TestSMS</code> class contains the implementation of the SMS JUnit tests.
 *
 * @author Marcus Portmann
 */
public class TestSMS
{
  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(TestSMS.class);

  /**
   * Test the send SMS functionality.
   *
   * @throws Exception
   */

  // @Test
  public void sendSMSTest()
    throws Exception
  {
    String myMobileAPIUsername = "USERNAME";
    String myMobileAPIPassword = "PASSWORD";
    long smsId = System.currentTimeMillis();
    String mobileNumber = "+27832763107";
    String message = "Testing 1.. 2.. 3..";

    try
    {
      logger.info("Sending an SMS to " + mobileNumber);

      String sendXML = buildSendDataXml(smsId, mobileNumber, message);

      APISoap myMobileAPIService = getMyMobileAPIService();

      String apiResultXml = myMobileAPIService.sendSTRSTR(myMobileAPIUsername, myMobileAPIPassword,
        sendXML);

      // Retrieve a document builder instance using the factory
      DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();

      builderFactory.setValidating(false);
      builderFactory.setNamespaceAware(false);

      // Create the document builder
      DocumentBuilder builder = builderFactory.newDocumentBuilder();

      builder.setErrorHandler(new XmlParserErrorHandler());

      // Parse the XML
      InputSource inputSource = new InputSource(new StringReader(apiResultXml));
      Document document = builder.parse(inputSource);
      Element apiResultElement = document.getDocumentElement();

      if (!apiResultElement.getNodeName().equals("api_result"))
      {
        throw new RuntimeException("Invalid API result XML: api_result element not found");
      }

      Element callResultElement = XmlUtils.getChildElement(apiResultElement, "call_result");

      if (callResultElement == null)
      {
        throw new RuntimeException("Invalid API result XML: call_result element not found");
      }

      Boolean result = XmlUtils.getChildElementBoolean(callResultElement, "result");

      if (result == null)
      {
        throw new RuntimeException("Invalid API result XML: result element not found");
      }

      if (!result.booleanValue())
      {
        String error = XmlUtils.getChildElementText(callResultElement, "error");

        // If the SMS cannot be sent...
        if (error.equalsIgnoreCase("No data to send"))
        {
          return;
        }

        throw new RuntimeException("The MyMobileAPI service returned an error: "
            + (StringUtil.isNullOrEmpty(error)
            ? "UNKNOWN"
            : error));
      }

      Element sendInfoElement = XmlUtils.getChildElement(apiResultElement, "send_info");

      if (sendInfoElement == null)
      {
        throw new RuntimeException("Invalid API result XML: send_info element not found");
      }

      int credits = 0;

      try
      {
        credits = Integer.parseInt(XmlUtils.getChildElementText(sendInfoElement, "credits"));
      }
      catch (Throwable e)
      {
        throw new RuntimeException("Invalid API result XML: "
            + "Failed to retrieve and parse the value of the credits element", e);
      }

      logger.info("Successfully sent a SMS using the mobile number (" + mobileNumber + ")");
      logger.info("There are " + credits + " SMS credits remaining");
    }
    catch (Throwable e)
    {
      logger.error("Failed to send the SMS", e);
    }
  }

  private String buildSendDataXml(long smsId, String mobileNumber, String message)
  {
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MMM/yyyy");
    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

    Date now = new Date();

    StringBuilder buffer = new StringBuilder();

    buffer.append("<senddata>");
    buffer.append("<settings>");
    buffer.append("<live>True</live>");
    buffer.append("<return_credits>True</return_credits>");
    buffer.append("<default_date>").append(dateFormat.format(now)).append("</default_date>");
    buffer.append("<default_time>").append(timeFormat.format(now)).append("</default_time>");
    buffer.append("<default_curdate>").append(dateFormat.format(now)).append("</default_curdate>");
    buffer.append("<default_curtime>").append(timeFormat.format(now)).append("</default_curtime>");
    buffer.append("<mo_forwardemail>").append("sms-reply@mmp.guru").append("</mo_forwardemail>");
    buffer.append("</settings>");
    buffer.append("<entries>");
    buffer.append("<numto>").append(mobileNumber).append("</numto>");
    buffer.append("<customerid>").append(smsId).append("</customerid>");
    buffer.append("<data1>").append(message).append("</data1>");
    buffer.append("<type>").append("SMS").append("</type>");

    // buffer.append("<validityperiod>").append("48").append("</validityperiod>");
    buffer.append("</entries>");
    buffer.append("</senddata>");

    return buffer.toString();
  }

  private APISoap getMyMobileAPIService()
  {
    // Retrieve the proxy for the MyMobileAPI service
    URL wsdlLocation =
      Thread.currentThread().getContextClassLoader().getResource("META-INF/wsdl/MyMobileAPI.wsdl");

    API api = new API(wsdlLocation, new QName("http://www.mymobileapi.com/api5", "API"));

    APISoap apiSoap = api.getAPISoap();

    BindingProvider bindingProvider = ((BindingProvider) apiSoap);

    // Set the endpoint for the service
    bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
        "http://www.mymobileapi.com/api5/api.asmx");

    return apiSoap;
  }
}

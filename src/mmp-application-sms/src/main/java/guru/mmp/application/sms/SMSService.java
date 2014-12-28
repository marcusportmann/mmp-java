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

package guru.mmp.application.sms;

//~--- non-JDK imports --------------------------------------------------------

import com.mymobileapi.api5.API;
import com.mymobileapi.api5.APISoap;
import guru.mmp.application.Debug;
import guru.mmp.application.registry.IRegistry;
import guru.mmp.common.util.StringUtil;
import guru.mmp.common.xml.XmlParserErrorHandler;
import guru.mmp.common.xml.XmlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import javax.annotation.PostConstruct;
import javax.ejb.AsyncResult;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.naming.InitialContext;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.ws.BindingProvider;
import java.io.StringReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Future;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>SMSService</code> class provides the SMS Service implementation.
 *
 * @author Marcus Portmann
 */
@ApplicationScoped
@Default
public class SMSService
  implements ISMSService
{
  /**
   * The maximum SMS length.
   */
  private static final int MAXIMUM_SMS_LENGTH = 160;

  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(SMSService.class);

  /* Background SMS Sender */
  @Inject
  BackgroundSMSSender backgroundSMSSender;

  /* The name of the SMS service instance. */
  private String instanceName;

  /* The maximum number of times sending will be attempted for a SMS. */
  private int maximumSendAttempts;
  private String myMobileAPIEndPoint;
  private String myMobileAPIPassword;
  private String myMobileAPIUsername;

  /* Registry */
  @Inject
  private IRegistry registry;

  /* The delay in milliseconds to wait before re-attempting to send a SMS. */
  private int sendRetryDelay;

  /* The result of sending the SMSs. */
  private Future<Boolean> sendSMSsResult;

  /* The DAO providing persistence capabilities for the SMS infrastructure. */
  @Inject
  private ISMSDAO smsDAO;

  /**
   * Constructs a new <code>SMSService</code>.
   */
  public SMSService() {}

  /**
   * Delete the existing SMS.
   *
   * @param id the ID uniquely identifying the SMS
   *
   * @return <code>true</code> if the SMS was deleted or <code>false</code> otherwise
   *
   * @throws SMSServiceException
   */
  public boolean deleteSMS(long id)
    throws SMSServiceException
  {
    try
    {
      return smsDAO.deleteSMS(id);
    }
    catch (Throwable e)
    {
      throw new SMSServiceException("Failed to delete the SMS with ID (" + id + ")", e);
    }
  }

  /**
   * Returns the maximum number of send attempts for a SMS.
   *
   * @return the maximum number of send attempts for a SMS
   */
  public int getMaximumSendAttempts()
  {
    return maximumSendAttempts;
  }

  /**
   * Retrieve the next SMS that has been queued for sending.
   * <p/>
   * The SMS will be locked to prevent duplicate sending.
   *
   * @return the next SMS that has been queued for sending or <code>null</code> if no SMSs are
   *         currently queued for sending
   *
   * @throws SMSServiceException
   */
  public SMS getNextSMSQueuedForSending()
    throws SMSServiceException
  {
    try
    {
      return smsDAO.getNextSMSQueuedForSending(sendRetryDelay, getInstanceName());
    }
    catch (Throwable e)
    {
      throw new SMSServiceException("Failed to retrieve the next SMS queued for sending", e);
    }
  }

  /**
   * Returns the number of SMS credits remaining.
   *
   * @return the number of SMS credits remaining
   *
   * @throws SMSServiceException
   */
  public int getNumberOfSMSCreditsRemaining()
    throws SMSServiceException
  {
    try
    {
      APISoap myMobileAPIService = getMyMobileAPIService();

      String apiResultXml = myMobileAPIService.creditsSTR(myMobileAPIUsername, myMobileAPIPassword);

      Element apiResultElement = parseAPIResultXML(apiResultXml);

      Element dataElement = XmlUtils.getChildElement(apiResultElement, "data");

      if (dataElement == null)
      {
        throw new RuntimeException("Invalid API result XML: data element not found");
      }

      return Integer.parseInt(XmlUtils.getChildElementText(dataElement, "credits"));
    }
    catch (Throwable e)
    {
      throw new SMSServiceException("Failed to retrieve the number of SMS credits remaining", e);

    }
  }

  /**
   * Retrieve the SMS with the specified ID.
   *
   * @param id the ID uniquely identifying the SMS
   *
   * @return the SMS with the specified ID or <code>null</code> if the SMS could not be found
   *
   * @throws SMSServiceException
   */
  public SMS getSMS(long id)
    throws SMSServiceException
  {
    try
    {
      return smsDAO.getSMS(id);
    }
    catch (Throwable e)
    {
      throw new SMSServiceException("Failed to retrieve the SMS with ID (" + id + ")", e);
    }
  }

  /**
   * Increment the send attempts for the SMS.
   *
   * @param sms the SMS whose send attempts should be incremented
   *
   * @throws SMSServiceException
   */
  public void incrementSMSSendAttempts(SMS sms)
    throws SMSServiceException
  {
    try
    {
      smsDAO.incrementSMSSendAttempts(sms);
    }
    catch (Throwable e)
    {
      throw new SMSServiceException("Failed to increment the send attempts for the SMS ("
          + sms.getId() + ")", e);
    }
  }

  /**
   * Initialise the SMSService instance.
   */
  @PostConstruct
  public void init()
  {
    logger.info("Initialising the SMSService instance");

    sendSMSsResult = new AsyncResult<>(false);

    try
    {
      // Initialise the configuration for the SMSService instance
      initConfiguration();
    }
    catch (Exception e)
    {
      throw new RuntimeException("Failed to initialise the SMSService instance", e);
    }
    catch (Throwable e)
    {
      throw new RuntimeException("Failed to initialise the SMSService instance: " + e.getMessage());
    }
  }

  /**
   * Reset the SMS locks.
   *
   * @param status    the current status of the SMSs that have been locked
   * @param newStatus the new status for the SMSs that have been unlocked
   *
   * @throws SMSServiceException
   */
  public void resetSMSLocks(SMS.Status status, SMS.Status newStatus)
    throws SMSServiceException
  {
    try
    {
      smsDAO.resetSMSLocks(getInstanceName(), status, newStatus);
    }
    catch (Throwable e)
    {
      throw new SMSServiceException("Failed to reset the locks for the SMSs with status (" + status
          + ") locked using the lock name (" + getInstanceName() + ")", e);
    }
  }

  /**
   * Send the SMS.
   * <p/>
   * NOTE: This will queue the SMS for sending. The SMS will actually be sent asynchronously.
   *
   * @param mobileNumber the mobile number
   * @param message      the message
   *
   * @throws SMSServiceException
   */
  public void sendSMS(String mobileNumber, String message)
    throws SMSServiceException
  {
    try
    {
      SMS sms = new SMS(mobileNumber, message, SMS.Status.QUEUED_FOR_SENDING);

      smsDAO.createSMS(sms);
    }
    catch (Throwable e)
    {
      throw new SMSServiceException("Failed to queue the SMS for the mobile number ("
          + mobileNumber + ") for sending", e);
    }
  }

  /**
   * Send the SMS synchronously.
   * <p/>
   * NOTE: This will NOT queue the SMS for sending. The SMS will actually be sent synchronously.
   *
   * @param smsId        the ID of the SMS
   * @param mobileNumber the mobile number
   * @param message      the message
   *
   * @return <code>true</code> if the SMS was sent successfully or <code>false</code> otherwise
   *
   * @throws SMSServiceException
   */
  public boolean sendSMSSynchronously(long smsId, String mobileNumber, String message)
    throws SMSServiceException
  {
    try
    {
      if (StringUtil.isNullOrEmpty(message))
      {
        logger.info("Failed to send the empty SMS message to (" + mobileNumber + ")");

        return true;
      }

      mobileNumber = formatMobileNumber(mobileNumber);

      if (message.length() > MAXIMUM_SMS_LENGTH)
      {
        message = message.substring(0, MAXIMUM_SMS_LENGTH);
      }

      if (logger.isDebugEnabled())
      {
        logger.debug("Attempting to send a SMS using the mobile number (" + mobileNumber + ")");
      }

      if (Debug.inDebugMode())
      {
        logger.info("Skipping sending of SMS (" + message + ") to mobile number (" + mobileNumber
            + ") in DEBUG mode");

        return true;
      }

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

      if (!result)
      {
        String error = XmlUtils.getChildElementText(callResultElement, "error");

        // If the SMS cannot be sent...
        if (error.equalsIgnoreCase("No data to send"))
        {
          return false;
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

      int credits;

      try
      {
        credits = Integer.parseInt(XmlUtils.getChildElementText(sendInfoElement, "credits"));
      }
      catch (Throwable e)
      {
        throw new RuntimeException("Invalid API result XML: "
            + "Failed to retrieve and parse the value of the credits element", e);
      }

      if (credits < 100)
      {
        logger.warn("There are " + credits + " SMS credits remaining");
      }

      if (logger.isDebugEnabled())
      {
        logger.debug("Successfully sent a SMS using the mobile number (" + mobileNumber + ")");
      }

      return true;
    }
    catch (Throwable e)
    {
      throw new SMSServiceException("Failed to send the SMS to the mobile number (" + mobileNumber
          + ")", e);
    }
  }

  /**
   * Send all the SMSs queued for sending asynchronously.
   */
  public synchronized void sendSMSs()
  {
    if (sendSMSsResult.isDone())
    {
      /*
       * Asynchronously inform the background SMS sender that all pending SMSs should be
       * sent.
       */
      try
      {
        sendSMSsResult = backgroundSMSSender.send();
      }
      catch (Throwable e)
      {
        logger.error("Failed to invoke the background SMS sender to asynchronously send "
            + "all queued SMSs", e);
      }
    }
  }

  /**
   * Set the status for the SMS.
   *
   * @param id        the ID uniquely identifying the SMS
   * @param status    the new status for the SMS
   *
   * @throws SMSServiceException
   */
  public void setSMSStatus(long id, SMS.Status status)
    throws SMSServiceException
  {
    try
    {
      smsDAO.setSMSStatus(id, status);
    }
    catch (Throwable e)
    {
      throw new SMSServiceException("Failed to set the status for the SMS (" + id + ") to ("
          + status.getName() + ")", e);
    }
  }

  /**
   * Unlock the SMS.
   *
   * @param id     the ID uniquely identifying the SMS
   * @param status the new status for the unlocked SMS
   *
   * @throws SMSServiceException
   */
  public void unlockSMS(long id, SMS.Status status)
    throws SMSServiceException
  {
    try
    {
      smsDAO.unlockSMS(id, status);
    }
    catch (Throwable e)
    {
      throw new SMSServiceException("Failed to unlock the SMS (" + id + ") and set its status to ("
          + status.getName() + ")", e);
    }
  }

  private String buildSendDataXml(long smsId, String mobileNumber, String message)
  {
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MMM/yyyy");
    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

    Date now = new Date();

    // buffer.append("<validityperiod>").append("48").append("</validityperiod>");

    return "<senddata>" + "<settings>" + "<live>True</live>"
        + "<return_credits>True</return_credits>" + "<default_date>" + dateFormat.format(now)
        + "</default_date>" + "<default_time>" + timeFormat.format(now) + "</default_time>"
        + "<default_curdate>" + dateFormat.format(now) + "</default_curdate>" + "<default_curtime>"
        + timeFormat.format(now) + "</default_curtime>" + "<mo_forwardemail>"
        + "sms-reply@mmp.guru" + "</mo_forwardemail>" + "</settings>" + "<entries>" + "<numto>"
        + mobileNumber + "</numto>" + "<customerid>" + smsId + "</customerid>" + "<data1>"
        + message + "</data1>" + "<type>" + "SMS" + "</type>" + "</entries>" + "</senddata>";
  }

  private String formatMobileNumber(String mobileNumber)
  {
    mobileNumber = StringUtil.notNull(mobileNumber);

    // Remove whitespace
    mobileNumber = mobileNumber.trim();
    mobileNumber = StringUtil.replace(mobileNumber, " ", "");
    mobileNumber = StringUtil.replace(mobileNumber, "\t", "");

    if (mobileNumber.length() > 30)
    {
      mobileNumber = mobileNumber.substring(0, 30);
    }

    if (mobileNumber.startsWith("0") && (mobileNumber.length() > 1))
    {
      mobileNumber = "27" + mobileNumber.substring(1);
    }

    if (!mobileNumber.startsWith("+"))
    {
      mobileNumber = "+" + mobileNumber;
    }

    return mobileNumber;
  }

  /**
   * Retrieves the name of the SMS service instance.
   */
  private String getInstanceName()
  {
    if (instanceName == null)
    {
      String applicationName = null;

      try
      {
        applicationName = InitialContext.doLookup("java:app/AppName");
      }
      catch (Throwable ignored) {}

      if (applicationName == null)
      {
        logger.error("Failed to retrieve the application name from JNDI using the path ("
            + "java:app/AppName) while constructing the SMS service instance name");

        applicationName = "Unknown";
      }

      instanceName = applicationName + "::";

      try
      {
        java.net.InetAddress localMachine = java.net.InetAddress.getLocalHost();

        instanceName += localMachine.getHostName().toLowerCase();
      }
      catch (Throwable e)
      {
        logger.error("Failed to retrieve the server hostname while constructing the "
            + "SMS service instance name", e);
        instanceName = "Unknown";
      }

      // Check if we are running under JBoss and if so retrieve the server name
      if (System.getProperty("jboss.server.name") != null)
      {
        instanceName = instanceName + "::" + System.getProperty("jboss.server.name");
      }

      // Check if we are running under Glassfish and if so retrieve the server name
      else if (System.getProperty("glassfish.version") != null)
      {
        instanceName = instanceName + "::" + System.getProperty("com.sun.aas.instanceName");
      }

      // Check if we are running under WebSphere Application Server Community Edition (Geronimo)
      else if (System.getProperty("org.apache.geronimo.server.dir") != null)
      {
        instanceName = instanceName + "::Geronimo";
      }

      // Check if we are running under WebSphere Application Server Liberty Profile
      else if (System.getProperty("wlp.user.dir") != null)
      {
        instanceName = instanceName + "::WLP";
      }

      /*
       * Check if we are running under WebSphere and if so execute the code below to retrieve the
       * server name.
       */
      else
      {
        try
        {
          instanceName = instanceName + "::" + InitialContext.doLookup("servername").toString();
        }
        catch (Throwable e)
        {
          logger.error("Failed to retrieve the name of the WebSphere server instance from JNDI"
              + " while constructing the SMS service instance name", e);
          instanceName = instanceName + "::Unknown";
        }
      }
    }

    return instanceName;
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
        myMobileAPIEndPoint);

    return apiSoap;
  }

  /**
   * Initialise the configuration for the SMSService instance.
   *
   * @throws SMSServiceException
   */
  private void initConfiguration()
    throws SMSServiceException
  {
    try
    {
      // Initialise the configuration
      if (!registry.integerValueExists("/SMSService", "SendRetryDelay"))
      {
        registry.setIntegerValue("/SMSService", "SendRetryDelay", 600000);
      }

      if (!registry.integerValueExists("/SMSService", "MaximumSendAttempts"))
      {
        registry.setIntegerValue("/SMSService", "MaximumSendAttempts", 100);
      }

      if (!registry.stringValueExists("/SMSService", "MyMobileAPIUsername"))
      {
        registry.setStringValue("/SMSService", "MyMobileAPIUsername", "MyMobileAPIUsername");
      }

      if (!registry.stringValueExists("/SMSService", "MyMobileAPIPassword"))
      {
        registry.setStringValue("/SMSService", "MyMobileAPIPassword", "MyMobileAPIPassword");
      }

      if (!registry.stringValueExists("/SMSService", "MyMobileAPIEndPoint"))
      {
        registry.setStringValue("/SMSService", "MyMobileAPIEndPoint",
            "http://www.mymobileapi.com/api5/api.asmx");
      }

      sendRetryDelay = registry.getIntegerValue("/SMSService", "SendRetryDelay", 600000);

      maximumSendAttempts = registry.getIntegerValue("/SMSService", "MaximumSendAttempts", 100);

      myMobileAPIUsername = registry.getStringValue("/SMSService", "MyMobileAPIUsername",
          "MyMobileAPIUsername");

      myMobileAPIPassword = registry.getStringValue("/SMSService", "MyMobileAPIPassword",
          "MyMobileAPIPassword");

      myMobileAPIEndPoint = registry.getStringValue("/SMSService", "MyMobileAPIEndPoint",
          "http://www.mymobileapi.com/api5/api.asmx");
    }
    catch (Throwable e)
    {
      throw new SMSServiceException(
          "Failed to initialise the configuration for the SMSService instance", e);
    }
  }

  private Element parseAPIResultXML(String xml)
  {
    try
    {
      // Retrieve a document builder instance using the factory
      DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();

      builderFactory.setValidating(false);
      builderFactory.setNamespaceAware(false);

      // Create the document builder
      DocumentBuilder builder = builderFactory.newDocumentBuilder();

      builder.setErrorHandler(new XmlParserErrorHandler());

      // Parse the XML
      InputSource inputSource = new InputSource(new StringReader(xml));
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

      if (!result)
      {
        String error = XmlUtils.getChildElementText(callResultElement, "error");

        throw new RuntimeException("The MyMobileAPI service returned an error: "
            + (StringUtil.isNullOrEmpty(error)
            ? "UNKNOWN"
            : error));
      }

      return apiResultElement;
    }
    catch (Throwable e)
    {
      throw new RuntimeException("Failed to parse the API result XML", e);
    }
  }
}

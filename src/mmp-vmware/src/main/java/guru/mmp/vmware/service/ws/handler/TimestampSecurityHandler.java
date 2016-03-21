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

package guru.mmp.vmware.service.ws.handler;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.vmware.service.ws.VMwareWebServiceUtil;

import org.oasis_open.docs.wss._2004._01.oasis_200401_wss_wssecurity_utility_1_0.AttributedDateTime;
import org.oasis_open.docs.wss._2004._01.oasis_200401_wss_wssecurity_utility_1_0.TimestampType;

import org.w3c.dom.Node;

//~--- JDK imports ------------------------------------------------------------

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

/**
 * The <code>TimestampSecurityHandler</code> class implements the JAX-WS handler that adds the
 * Timestamp element to the SOAP security header for a SOAP request.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public class TimestampSecurityHandler
  implements SOAPHandler<SOAPMessageContext>
{
  private static final int REQUEST_VALIDITY_IN_MINUTES = 10;
  private static final String XML_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

  /** Field description */
  public static final String GMT = "GMT";
  private static final ThreadLocal<SimpleDateFormat> threadLocalSimpleDateFormat =
      new ThreadLocal<SimpleDateFormat>()
  {
    @Override
    protected SimpleDateFormat initialValue()
    {
      SimpleDateFormat dateFormat = new SimpleDateFormat(XML_DATE_FORMAT);
      dateFormat.setTimeZone(TimeZone.getTimeZone(GMT));

      return dateFormat;
    }
  };

  /**
   * Called at the conclusion of a message exchange pattern just prior to the JAX-WS runtime
   * dispatching a message, fault or exception. Refer to the description of the handler framework
   * in the JAX-WS specification for full details.
   *
   * @param messageContext the message context
   *
   * @see javax.xml.ws.handler.Handler#close(javax.xml.ws.handler.MessageContext)
   */
  @Override
  public void close(MessageContext messageContext) {}

  /**
   * Gets the header blocks that can be processed by this Handler instance.
   *
   * @return Set of <code>QNames</code> of header blocks processed by this handler instance.
   *         <code>QName</code> is the qualified name of the outermost element of the Header block.
   *
   * @see javax.xml.ws.handler.soap.SOAPHandler#getHeaders()
   */
  @Override
  public Set<QName> getHeaders()
  {
    return null;
  }

  /**
   * The <code>handleFault</code> method is invoked for fault message processing. Refer to the
   * description of the handler framework in the JAX-WS specification for full details.
   *
   * @param messageContext the message context
   *
   * @return <code>true</code> if handler fault processing should continue for the current message
   *         or <code>false</code> if fault processing should stop
   *
   * @see javax.xml.ws.handler.Handler#handleFault(MessageContext)
   */
  @Override
  public boolean handleFault(SOAPMessageContext messageContext)
  {
    return false;
  }

  /**
   * The <code>handleMessage</code> method is invoked for normal processing of inbound and outbound
   * messages. Refer to the description of the handler framework in the JAX-WS specification for
   * full details.
   *
   * @param messageContext the message context
   *
   * @return <code>true</code> if handler processing should continue for the current message
   *         or <code>false</code> if processing should stop
   */
  @Override
  public boolean handleMessage(SOAPMessageContext messageContext)
  {
    if (VMwareWebServiceUtil.isOutgoingMessage(messageContext))
    {
      try
      {
        Node securityNode = VMwareWebServiceUtil.getSecurityElement(
            VMwareWebServiceUtil.getSOAPHeader(messageContext));
        Node timeStampNode = VMwareWebServiceUtil.marshalJaxbElement(createTimestamp())
            .getDocumentElement();
        securityNode.appendChild(securityNode.getOwnerDocument().importNode(timeStampNode, true));
      }
      catch (Throwable e)
      {
        throw new HandlerException(
            "The timestamp security handler failed to handle the SOAP message", e);
      }
    }

    return true;

  }

  /**
   * Creates a timestamp WS-Security element.
   *
   * @return timestamp element issued with start date = NOW and
   *         expiration date = NOW + REQUEST_VALIDITY_IN_MINUTES
   */
  private JAXBElement<TimestampType> createTimestamp()
  {
    org.oasis_open.docs.wss._2004._01.oasis_200401_wss_wssecurity_utility_1_0.ObjectFactory wssuObjFactory =
        new org.oasis_open.docs.wss._2004._01.oasis_200401_wss_wssecurity_utility_1_0
        .ObjectFactory();

    TimestampType timestamp = wssuObjFactory.createTimestampType();

    final long now = System.currentTimeMillis();
    Date createDate = new Date(now);
    Date expirationDate = new Date(now + TimeUnit.MINUTES.toMillis(REQUEST_VALIDITY_IN_MINUTES));

    DateFormat wssDateFormat = threadLocalSimpleDateFormat.get();
    AttributedDateTime createTime = wssuObjFactory.createAttributedDateTime();
    createTime.setValue(wssDateFormat.format(createDate));

    AttributedDateTime expirationTime = wssuObjFactory.createAttributedDateTime();
    expirationTime.setValue(wssDateFormat.format(expirationDate));

    timestamp.setCreated(createTime);
    timestamp.setExpires(expirationTime);

    return wssuObjFactory.createTimestamp(timestamp);
  }
}

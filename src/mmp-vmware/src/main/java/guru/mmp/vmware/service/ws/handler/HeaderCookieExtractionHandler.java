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

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

/**
 * The <code>HeaderCookieExtractionHandler</code> class implements the JAX-WS handler that extracts
 * the cookies returned as part of the HTTP response for a web service invocation.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public class HeaderCookieExtractionHandler
  implements SOAPHandler<SOAPMessageContext>
{
  private List<String> cookies = new ArrayList<>();

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
   * Returns the cookies returned as part of the HTTP response for a web service request.
   *
   * @return the cookies returned as part of the HTTP response for a web service request
   */
  public List<String> getCookies()
  {
    return cookies;
  }

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
    try
    {
      // Only interested in the Response
      if (!VMwareWebServiceUtil.isOutgoingMessage(messageContext))
      {
        @SuppressWarnings("unchecked")
        Map<String, List<String>> headers = (Map<String, List<String>>) messageContext.get(
            MessageContext.HTTP_RESPONSE_HEADERS);

        if (headers != null)
        {
          cookies = headers.get("Set-Cookie");
        }
      }

      return true;
    }
    catch (Throwable e)
    {
      throw new HandlerException(
          "The header cookie extraction handler failed to handle the SOAP message", e);
    }
  }
}

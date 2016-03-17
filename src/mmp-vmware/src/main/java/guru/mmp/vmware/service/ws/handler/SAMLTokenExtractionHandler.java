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

import org.w3c.dom.Element;
import org.w3c.dom.Node;

//~--- JDK imports ------------------------------------------------------------

import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPBody;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

/**
 * The <code>SAMLTokenExtractionHandler</code> class implements the JAX-WS handler that allows the
 * SAML token to be extracted from the SOAP response message in its raw form before JAX-WS
 * deserializes it.
 * <p>
 * This is needed because the default deserializer with JAX-WS does not maintain the line
 * separators present inside the token while deserializing and later serializing the SAML token.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public class SAMLTokenExtractionHandler
  implements SOAPHandler<SOAPMessageContext>
{
  private Node token;

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
   * Returns the SAML token extracted from the SOAP message.
   *
   * @return the SAML token extracted from the SOAP message
   */
  public Element getToken()
  {
    return (Element) token;
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
    if (!VMwareWebServiceUtil.isOutgoingMessage(messageContext))
    {
      try
      {
        // Extract the Token
        SOAPBody responseBody = messageContext.getMessage().getSOAPBody();
        Node firstChild = responseBody.getFirstChild();
        if ((firstChild != null)
            && "RequestSecurityTokenResponseCollection".equalsIgnoreCase(firstChild.getLocalName()))
        {
          if ((firstChild.getFirstChild() != null)
              && "RequestSecurityTokenResponse".equalsIgnoreCase(firstChild.getFirstChild()
                  .getLocalName()))
          {
            Node rstrNode = firstChild.getFirstChild();
            if ((rstrNode.getFirstChild() != null)
                && "RequestedSecurityToken".equalsIgnoreCase(rstrNode.getFirstChild()
                    .getLocalName()))
            {
              Node rstNode = rstrNode.getFirstChild();
              if ((rstNode.getFirstChild() != null)
                  && "Assertion".equalsIgnoreCase(rstNode.getFirstChild().getLocalName()))
              {
                token = rstNode.getFirstChild();
              }
            }
          }
        }
        else
        {
          if ((firstChild != null)
              && "RequestSecurityTokenResponse".equalsIgnoreCase(firstChild.getLocalName()))
          {
            if ((firstChild.getFirstChild() != null)
                && "RequestedSecurityToken".equalsIgnoreCase(firstChild.getFirstChild()
                    .getLocalName()))
            {
              Node rstNode = firstChild.getFirstChild();
              if ((rstNode.getFirstChild() != null)
                  && "Assertion".equalsIgnoreCase(rstNode.getFirstChild().getLocalName()))
              {
                token = rstNode.getFirstChild();
              }
            }
          }
        }
      }
      catch (Throwable e)
      {
        throw new HandlerException(
            "The SAML token extraction handler failed to handle the SOAP message", e);
      }
    }

    return true;
  }
}

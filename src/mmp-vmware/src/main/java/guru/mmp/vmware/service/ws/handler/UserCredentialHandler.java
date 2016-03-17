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

import org.oasis_open.docs.wss._2004._01.oasis_200401_wss_wssecurity_secext_1_0.AttributedString;
import org.oasis_open.docs.wss._2004._01.oasis_200401_wss_wssecurity_secext_1_0.PasswordString;
import org.oasis_open.docs.wss._2004._01.oasis_200401_wss_wssecurity_secext_1_0.UsernameTokenType;

import org.w3c.dom.Node;

//~--- JDK imports ------------------------------------------------------------

import java.util.Set;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

/**
 * The <code>UserCredentialHandler</code> class.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public class UserCredentialHandler
  implements SOAPHandler<SOAPMessageContext>
{
  private final String username;
  private final String password;

  /**
   * Constructs a new <code>UserCredentialHandler</code>.
   *
   * @param username the username
   * @param password the password
   */
  public UserCredentialHandler(String username, String password)
  {
    this.username = username;
    this.password = password;
  }

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
        Node usernameNode = VMwareWebServiceUtil.marshalJaxbElement(createUsernameToken())
            .getDocumentElement();
        securityNode.appendChild(securityNode.getOwnerDocument().importNode(usernameNode, true));
      }
      catch (Throwable e)
      {
        throw new HandlerException("The user credential handler failed to handle the SOAP message",
            e);
      }
    }

    return true;
  }

  /**
   * Creates a WS-Security UsernameToken element.
   *
   * @return UsernameToken
   */
  final JAXBElement<UsernameTokenType> createUsernameToken()
  {
    org.oasis_open.docs.wss._2004._01.oasis_200401_wss_wssecurity_secext_1_0.ObjectFactory objFactory =
        new org.oasis_open.docs.wss._2004._01.oasis_200401_wss_wssecurity_secext_1_0
        .ObjectFactory();

    UsernameTokenType userNameToken = objFactory.createUsernameTokenType();
    AttributedString user = objFactory.createAttributedString();
    user.setValue(username);
    userNameToken.setUsername(user);

    if (password != null)
    {
      /*
       * NOTE: If the password is not specified (i.e. requesting a solution token) do not create
       * the password element.
       */
      PasswordString pass = objFactory.createPasswordString();
      pass.setValue(password);

      userNameToken.setPassword(pass);
    }

    return objFactory.createUsernameToken(userNameToken);
  }
}

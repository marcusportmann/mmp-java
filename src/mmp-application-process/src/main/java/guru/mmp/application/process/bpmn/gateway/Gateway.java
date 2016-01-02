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

package guru.mmp.application.process.bpmn.gateway;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.process.bpmn.BaseElement;
import guru.mmp.application.process.bpmn.FlowElement;
import guru.mmp.application.process.bpmn.ParserException;
import guru.mmp.common.util.StringUtil;

import org.w3c.dom.Element;

/**
 * The <code>Gateway</code> class provides the base class that all Gateway subclasses should be
 * derived from.
 * <p>
 * Gateways are objects that control the flow of the process instead of performing some action.
 * <p>
 * Gateways have two behaviors:
 * <ul>
 *   <li><b>Converging</b>, that refers to what they do to the incoming flows.</li>
 *   <li><b>Diverging</b>, what they do to outgoing flows.</li>
 * </ul>
 * There are five types of gateways:
 * <ol>
 *   <li>Exclusive</li>
 *   <li>Parallel</li>
 *   <li>Event</li>
 *   <li>Inclusive</li>
 *   <li>Complex</li>
 * </ol>
 * <p>
 * <b>Gateway</b> XML schema:
 * <pre>
 * &lt;xsd:element name="gateway" type="tGateway" abstract="true"/&gt;
 * &lt;xsd:complexType name="tGateway"&gt;
 *   &lt;xsd:complexContent&gt;
 *     &lt;xsd:extension base="tFlowElement"&gt;
 *       &lt;xsd:attribute name="gatewayDirection" type="tGatewayDirection"
 *                         default="Unspecified"/&gt;
 *     &lt;/xsd:extension&gt;
 *   &lt;/xsd:complexContent&gt;
 * &lt;/xsd:complexType&gt;
 * </pre>
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
abstract class Gateway extends FlowElement
{
  /**
   * The constraints on the incoming and outgoing Sequence Flows.
   * <p>
   * <ul>
   *   <li>
   *     <b>Unspecified:</b> There are no constraints. The Gateway MAY have any number of incoming
   *     and outgoing Sequence Flows.
   *   </li>
   *   <li>
   *     <b>Converging:</b> This Gateway MAY have multiple incoming Sequence Flows but MUST have no
   *     more than one (1) outgoing Sequence Flow.
   *   </li>
   *   <li>
   *     <b>Diverging:</b> This Gateway MAY have multiple outgoing Sequence Flows but MUST have no
   *     more than one (1) incoming Sequence Flow.
   *   </li>
   *   <li>
   *     <b>Mixed:</b> This Gateway contains multiple outgoing and multiple incoming Sequence Flows.
   *   </li>
   * </ul>
   */
  private GatewayDirection gatewayDirection;

  /**
   * Constructs a new <code>ThrowEvent</code>.
   *
   * @param parent  the BPMN element that is the parent of this Gateway
   * @param element the XML element containing the Gateway information
   */
  protected Gateway(BaseElement parent, Element element)
  {
    super(parent, element);

    try
    {
      if (StringUtil.isNullOrEmpty(element.getAttribute("gatewayDirection")))
      {
        this.gatewayDirection = GatewayDirection.UNSPECIFIED;
      }
      else
      {
        this.gatewayDirection = GatewayDirection.fromId(element.getAttribute("gatewayDirection"));
      }
    }
    catch (Throwable e)
    {
      throw new ParserException("Failed to parse the Gateway XML data", e);
    }
  }

  /**
   * Returns the constraints on the incoming and outgoing Sequence Flows.
   * <p>
   * <ul>
   *   <li>
   *     <b>Unspecified:</b> There are no constraints. The Gateway MAY have any number of incoming
   *     and outgoing Sequence Flows.
   *   </li>
   *   <li>
   *     <b>Converging:</b> This Gateway MAY have multiple incoming Sequence Flows but MUST have no
   *     more than one (1) outgoing Sequence Flow.
   *   </li>
   *   <li>
   *     <b>Diverging:</b> This Gateway MAY have multiple outgoing Sequence Flows but MUST have no
   *     more than one (1) incoming Sequence Flow.
   *   </li>
   *   <li>
   *     <b>Mixed:</b> This Gateway contains multiple outgoing and multiple incoming Sequence Flows.
   *   </li>
   * </ul>
   *
   * @return the constraints on the incoming and outgoing Sequence Flows
   */
  public GatewayDirection getGatewayDirection()
  {
    return gatewayDirection;
  }
}

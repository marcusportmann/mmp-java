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

package guru.mmp.application.process.bpmn.gateway;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.process.bpmn.BaseElement;
import guru.mmp.application.process.bpmn.ProcessExecutionContext;
import guru.mmp.application.process.bpmn.Token;

import org.w3c.dom.Element;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.List;

/**
 * The <code>ComplexGateway</code> class represents a Complex Gateway that forms part of a Process.
 * <p>
 * <b>Complex Gateway</b> XML schema:
 * <pre>
 * &lt;xsd:element name="complexGateway" type="tComplexGateway" substitutionGroup="flowElement"/&gt;
 * &lt;xsd:complexType name="tComplexGateway"&gt;
 *   &lt;xsd:complexContent&gt;
 *     &lt;xsd:extension base="tGateway"&gt;
 *       &lt;xsd:sequence&gt;
 *         &lt;xsd:element name="activationCondition" type="tExpression" minOccurs="0"
 *                         maxOccurs="1"/&gt;
 *       &lt;/xsd:sequence&gt;
 *       &lt;xsd:attribute name="default" type="xsd:IDREF"/&gt;
 *     &lt;/xsd:extension&gt;
 *   &lt;/xsd:complexContent&gt;
 * &lt;/xsd:complexType&gt;
 * </pre>
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public final class ComplexGateway extends Gateway
{
  /**
   * Constructs a new <code>ComplexGateway</code>.
   *
   * @param parent  the BPMN element that is the parent of this Complex Gateway
   * @param element the XML element containing the Complex Gateway information
   */
  protected ComplexGateway(BaseElement parent, Element element)
  {
    super(parent, element);

    if (true)
    {
      throw new RuntimeException("TODO: IMPLEMENT ME");
    }
  }

  /**
   * Execute the Complex Gateway.
   *
   * @param context the execution context for the Process
   *
   * @return the list of tokens generated as a result of executing the Complex Gateway
   */
  @Override
  public List<Token> execute(ProcessExecutionContext context)
  {
    return new ArrayList<>();
  }
}

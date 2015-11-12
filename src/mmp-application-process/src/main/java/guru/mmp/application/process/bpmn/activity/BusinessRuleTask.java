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

package guru.mmp.application.process.bpmn.activity;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.process.bpmn.BaseElement;
import guru.mmp.application.process.bpmn.ParserException;
import guru.mmp.application.process.bpmn.ProcessExecutionContext;
import guru.mmp.application.process.bpmn.Token;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>BusinessRuleTask</code> class represents a Business Rule Task that forms part of a
 * Process.
 * <p>
 * This task represents work executed at run-time in a business rule engine, generally a complex
 * decision.
 * <p>
 * <b>Business Rule Task</b> XML schema:
 * <pre>
 * &lt;xsd:element name="businessRuleTask" type="tBusinessRuleTask"
 *                 substitutionGroup="flowElement"/&gt;
 * &lt;xsd:complexType name="tBusinessRuleTask"&gt;
 *   &lt;xsd:complexContent&gt;
 *     &lt;xsd:extension base="tTask"&gt;
 *       &lt;xsd:attribute name="implementation" type="tImplementation" default="##unspecified"/&gt;
 *     &lt;/xsd:extension&gt;
 *   &lt;/xsd:complexContent&gt;
 * &lt;/xsd:complexType&gt;
 * </pre>
 *
 * @author Marcus Portmann
 */
public final class BusinessRuleTask extends Task
{
  /**
   * The technology that the Business Rule Task will use to send and receive messages.
   */
  private Implementation implementation;

  /**
   * Constructs a new <code>BusinessRuleTask</code>.
   *
   * @param parent  the BPMN element that is the parent of this Business Rule Task
   * @param element the XML element containing the Business Rule Task information
   */
  public BusinessRuleTask(BaseElement parent, Element element)
  {
    super(parent, element);

    try
    {
      this.implementation = Implementation.fromId(element.getAttribute("implementation"));
    }
    catch (Throwable e)
    {
      throw new ParserException("Failed to parse the Business Rule Task XML data", e);
    }
  }

  /**
   * Execute the Business Rule Task.
   *
   * @param context the execution context for the Process
   *
   * @return the list of tokens generated as a result of executing the Business Rule Task
   */
  @Override
  public List<Token> execute(ProcessExecutionContext context)
  {
    return new ArrayList<>();
  }

  /**
   * Returns the technology that the Business Rule Task will use to send and receive messages.
   *
   * @return the technology that the Business Rule Task will use to send and receive messages
   */
  public Implementation getImplementation()
  {
    return implementation;
  }
}
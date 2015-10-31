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

import guru.mmp.application.process.bpmn.ParserException;
import guru.mmp.application.process.bpmn.ProcessExecutionContext;
import guru.mmp.application.process.bpmn.Token;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>BusinessRuleTask</code> class represents a BPMN
 * business rule task that forms part of a Process.
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
   * The technology that the business rule task will use to send and receive messages.
   */
  private Implementation implementation;

  /**
   * Constructs a new <code>BusinessRuleTask</code>.
   *
   * @param element the XML element containing the business rule task information
   */
  public BusinessRuleTask(Element element)
  {
    super(element);

    try
    {
      this.implementation = Implementation.fromId(element.getAttribute("implementation"));
    }
    catch (Throwable e)
    {
      throw new ParserException("Failed to parse the business rule task XML data", e);
    }
  }

  /**
   * Execute the BPMN task.
   *
   * @param context the execution context for the Process
   *
   * @return the list of tokens generated as a result of executing the Business Process Model and
   *         Notation (BPMN) task
   */
  @Override
  public List<Token> execute(ProcessExecutionContext context)
  {
    return new ArrayList<>();
  }

  /**
   * Returns the technology that the business rule task will use to send and receive messages.
   *
   * @return the technology that the business rule task will use to send and receive messages
   */
  public Implementation getImplementation()
  {
    return implementation;
  }
}

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

package guru.mmp.application.process.bpmn.event;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.process.bpmn.ProcessExecutionContext;
import guru.mmp.application.process.bpmn.Token;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.List;

/**
 * The <code>CatchingEvent</code> class represents a Business Process Model and Notation (BPMN)
 * catching event that forms part of a BPMN process.
 * <p>
 * <b>Catching Event</b> XML schema:
 * <pre>
 * &lt;xsd:element name="catchEvent" type="tCatchEvent"/&gt;
 * &lt;xsd:complexType name="tCatchEvent" abstract="true"&gt;
 *   &lt;xsd:complexContent&gt;
 *     &lt;xsd:extension base="tEvent"&gt;
 *       &lt;xsd:sequence&gt;
 *         &lt;xsd:element ref="dataOutput" minOccurs="0" maxOccurs="unbounded"/&gt;
 *         &lt;xsd:element ref="dataOutputAssociation" minOccurs="0" maxOccurs="unbounded"/&gt;
 *         &lt;xsd:element ref="outputSet" minOccurs="0" maxOccurs="1"/&gt;
 *         &lt;xsd:element ref="eventDefinition" minOccurs="0" maxOccurs="unbounded"/&gt;
 *         &lt;xsd:element name="eventDefinitionRef" type="xsd:QName" minOccurs="0" 
 *                      maxOccurs="unbounded"/&gt;
 *       &lt;/xsd:sequence&gt;
 *       &lt;xsd:attribute name="parallelMultiple" type="xsd:boolean" default="false"/&gt;
 *     &lt;/xsd:extension&gt;
 *   &lt;/xsd:complexContent&gt;
 * &lt;/xsd:complexType&gt;
 * </pre>
 *
 * @author Marcus Portmann
 */
abstract class CatchingEvent extends Event
{
  /**
   * Constructs a new <code>CatchingEvent</code>.
   *
   * @param id   the ID uniquely identifying the catching event
   * @param name the name of the catching event
   */
  protected CatchingEvent(String id, String name)
  {
    super(id, name);
  }

  /**
   * Execute the Business Process Model and Notation (BPMN) catching event.
   *
   * @param context the execution context for the Business Process Model and Notation (BPMN) process
   *
   * @return the list of tokens generated as a result of executing the Business Process Model and
   *         Notation (BPMN) catching event
   */
  @Override
  public List<Token> execute(ProcessExecutionContext context)
  {
    return new ArrayList<>();
  }
}

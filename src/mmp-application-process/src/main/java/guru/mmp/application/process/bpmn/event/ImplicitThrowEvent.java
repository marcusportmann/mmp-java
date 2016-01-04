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

package guru.mmp.application.process.bpmn.event;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.process.bpmn.BaseElement;
import guru.mmp.application.process.bpmn.ProcessExecutionContext;
import guru.mmp.application.process.bpmn.Token;

import org.w3c.dom.Element;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;

/**
 * The <code>ImplicitThrowEvent</code> class represents an Implicit Throw Event that forms part of
 * a Process.
 * <p>
 * <b>Implicit Throw Event</b> XML schema:
 * <pre>
 * &lt;xsd:element name="implicitThrowEvent" type="tImplicitThrowEvent"
 *                 substitutionGroup="flowElement"/&gt;
 * &lt;xsd:complexType name="tImplicitThrowEvent"&gt;
 *   &lt;xsd:complexContent&gt;
 *     &lt;xsd:extension base="tThrowEvent"/&gt;
 *   &lt;/xsd:complexContent&gt;
 * &lt;/xsd:complexType&gt;
 * </pre>
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public final class ImplicitThrowEvent extends ThrowEvent
{
  /**
   * Constructs a new <code>ImplicitThrowEvent</code>.
   *
   * @param parent  the BPMN element that is the parent of this Implicit Throw Event
   * @param element the XML element containing the Implicit Throw Event information
   */
  public ImplicitThrowEvent(BaseElement parent, Element element)
  {
    super(parent, element);
  }

  /**
   * Execute the Implicit Throw Event.
   *
   * @param context the execution context for the Process
   *
   * @return the list of tokens generated as a result of executing the Implicit Throw Event
   */
  @Override
  public List<Token> execute(ProcessExecutionContext context)
  {
    return super.execute(context);
  }
}

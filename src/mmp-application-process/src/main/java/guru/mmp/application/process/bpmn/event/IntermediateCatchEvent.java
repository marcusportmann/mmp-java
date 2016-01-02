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

import java.util.List;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>IntermediateCatchEvent</code> class represents an Intermediate Catch Event that forms
 * part of a Process.
 * <p>
 * <b>Intermediate Catch Event</b> XML schema:
 * <pre>
 * &lt;xsd:element name="intermediateCatchEvent" type="tIntermediateCatchEvent"
 *              substitutionGroup="flowElement"/ &gt;
 * &lt;xsd:complexType name="tIntermediateCatchEvent"&gt;
 *   &lt;xsd:complexContent&gt;
 *     &lt;xsd:extension base="tCatchEvent"/&gt;
 *   &lt;/xsd:complexContent&gt;
 * &lt;/xsd:complexType&gt;
 * </pre>
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public final class IntermediateCatchEvent
  extends CatchEvent
{
  /**
   * Constructs a new <code>IntermediateCatchEvent</code>.
   *
   * @param parent  the BPMN element that is the parent of this Intermediate Catch Event
   * @param element the XML element containing the Intermediate Catch Event information
   */
  public IntermediateCatchEvent(BaseElement parent, Element element)
  {
    super(parent, element);
  }

  /**
   * Execute the Intermediate Catch Event.
   *
   * @param context the execution context for the Process
   *
   * @return the list of tokens generated as a result of executing the Intermediate Catch Event
   */
  @Override
  public List<Token> execute(ProcessExecutionContext context)
  {
    return super.execute(context);
  }
}

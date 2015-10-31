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

import guru.mmp.application.process.bpmn.ParserException;
import guru.mmp.common.util.StringUtil;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * The <code>SignalEventDefinition</code> class stores the details for a Business Process
 * Model and Notation (BPMN) signal event that forms part of a Process.
 * <p>
 * <b>Signal Event Definition</b> XML schema:
 * <pre>
 * &lt;xsd:element name="signalEventDefinition" type="tSignalEventDefinition"
 *              substitutionGroup="eventDefinition"/&gt;
 * &lt;xsd:complexType name="tSignalEventDefinition"&gt;
 *   &lt;xsd:complexContent&gt;
 *     &lt;xsd:extension base="tEventDefinition"&gt;
 *       &lt;xsd:attribute name="signalRef" type="xsd:QName"/&gt;
 *     &lt;/xsd:extension&gt;
 *   &lt;/xsd:complexContent&gt;
 * &lt;/xsd:complexType&gt;
 * </pre>
 *
 * @author Marcus Portmann
 */
public final class SignalEventDefinition extends EventDefinition
{
  signalRef

  /**
   * Constructs a new <code>SignalEventDefinition</code>.
   *
   * @param element the XML element containing the signal event definition information
   */
  public SignalEventDefinition(Element element)
  {
    super(element);

    try
    {
      this.signalRef = StringUtil.notNull(element.getAttribute("signalRef"));
    }
    catch (Throwable e)
    {
      throw new ParserException("Failed to parse the signal event definition XML data", e);
    }
  }
}

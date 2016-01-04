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
import guru.mmp.application.process.bpmn.ParserException;
import guru.mmp.common.util.StringUtil;
import guru.mmp.common.xml.XmlUtils;

import org.w3c.dom.Element;

//~--- JDK imports ------------------------------------------------------------

import javax.xml.namespace.QName;

/**
 * The <code>SignalEventDefinition</code> class represents a Signal Event Definition that forms
 * part of a Process.
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
@SuppressWarnings("unused")
public final class SignalEventDefinition extends EventDefinition
{
  /**
   * The reference to the Signal for the Singal Event Definition.
   */
  private QName signalRef;

  /**
   * Constructs a new <code>SignalEventDefinition</code>.
   *
   * @param parent  the BPMN element that is the parent of this Signal Event Definition
   * @param element the XML element containing the Signal Event Definition information
   */
  public SignalEventDefinition(BaseElement parent, Element element)
  {
    super(parent, element);

    try
    {
      if (!StringUtil.isNullOrEmpty(element.getAttribute("signalRef")))
      {
        this.signalRef = XmlUtils.getQName(element, element.getAttribute("signalRef"));
      }
    }
    catch (Throwable e)
    {
      throw new ParserException("Failed to parse the Signal Event Definition XML data", e);
    }
  }

  /**
   * Returns the reference to the Signal for the Singal Event Definition.
   *
   * @return the reference to the Signal for the Singal Event Definition
   */
  public QName getSignalRef()
  {
    return signalRef;
  }
}

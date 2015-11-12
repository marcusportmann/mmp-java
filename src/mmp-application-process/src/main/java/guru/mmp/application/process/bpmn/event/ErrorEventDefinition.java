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

import guru.mmp.application.process.bpmn.BaseElement;
import guru.mmp.application.process.bpmn.ParserException;
import guru.mmp.common.util.StringUtil;
import guru.mmp.common.xml.XmlUtils;

import org.w3c.dom.Element;

//~--- JDK imports ------------------------------------------------------------

import javax.xml.namespace.QName;

/**
 * The <code>ErrorEventDefinition</code> class represents an Error Event Definition that forms part
 * of a Process.
 * <p>
 * <b>Error Event Definition</b> XML schema:
 * <pre>
 * &lt;xsd:element name="errorEventDefinition" type="tErrorEventDefinition"
 *                 substitutionGroup="eventDefinition"/&gt;
 * &lt;xsd:complexType name="tErrorEventDefinition"&gt;
 *   &lt;xsd:complexContent&gt;
 *     &lt;xsd:extension base="tEventDefinition"&gt;
 *       &lt;xsd:attribute name="errorRef" type="xsd:QName"/&gt;
 *     &lt;/xsd:extension&gt;
 *   &lt;/xsd:complexContent&gt;
 * &lt;/xsd:complexType&gt;
 * </pre>
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public final class ErrorEventDefinition extends EventDefinition
{
  /**
   * The reference to the error associated with this Error Event Definition.
   */
  private QName errorRef;

  /**
   * Constructs a new <code>ErrorEventDefinition</code>.
   *
   * @param parent  the BPMN element that is the parent of this Error Event Definition
   * @param element the XML element containing the Error Event Definition information
   */
  public ErrorEventDefinition(BaseElement parent, Element element)
  {
    super(parent, element);

    try
    {
      if (!StringUtil.isNullOrEmpty(element.getAttribute("errorRef")))
      {
        errorRef = XmlUtils.getQName(element, element.getAttribute("errorRef"));
      }
    }
    catch (Throwable e)
    {
      throw new ParserException("Failed to parse the Error Event Definition XML data", e);
    }
  }

  /**
   * Returns the reference to the error associated with this Error Event Definition.
   *
   * @return reference to the error associated with this Error Event Definition
   */
  public QName getErrorRef()
  {
    return errorRef;
  }
}

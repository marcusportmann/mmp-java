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

package guru.mmp.application.process.bpmn;

import org.w3c.dom.Element;

/**
 * The <code>RootElement</code> class provides the base class that all RootElements that form part
 * of a Process should be derived from.
 * <p/>
 * <b>RootElement</b> XML schema:
 * <pre>
 * &lt;xsd:element name="rootElement" type="tRootElement"/&gt;
 * &lt;xsd:complexType name="tRootElement" abstract="true"&gt;
 *   &lt;xsd:complexContent&gt;
 *     &lt;xsd:extension base="tBaseElement"/&gt;
 *   &lt;/xsd:complexContent&gt;
 * &lt;/xsd:complexType&gt;
 * </pre>
 *
 * @author Marcus Portmann
 */
public abstract class RootElement
  extends BaseElement
{
  /**
   * Constructs a new <code>RootElement</code>.
   *
   * @param parent  the BPMN element that is the parent of this RootElement
   * @param element the XML element containing the RootElement information
   */
  protected RootElement(BaseElement parent, Element element)
  {
    super(parent, element);
  }
}

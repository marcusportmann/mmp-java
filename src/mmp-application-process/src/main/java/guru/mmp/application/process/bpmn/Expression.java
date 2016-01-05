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
 * The <code>Expression</code> class represents a Expression that forms part of a Process.
 * <p/>
 * <b>Expression</b> XML schema:
 * <pre>
 * &lt;xsd:element name="expression" type="tExpression"/&gt;
 * &lt;xsd:complexType name="tExpression"&gt;
 *   &lt;xsd:complexContent&gt;
 *     &lt;xsd:extension base="tBaseElementWithMixedContent"/&gt;
 *   &lt;/xsd:complexContent&gt;
 * &lt;/xsd:complexType&gt;
 * </pre>
 *
 * @author Marcus Portmann
 */
public abstract class Expression
  extends BaseElementWithMixedContent
{
  /**
   * Constructs a new <code>Expression</code>.
   *
   * @param element the XML element containing the Expression information
   */
  protected Expression(Element element)
  {
    super(element);
  }
}

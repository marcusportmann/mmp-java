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

package guru.mmp.application.process.bpmn;

/**
 * The <code>BaseElement</code> class provides the base class that all elements that form part of
 * a Business Process Model and Notation (BPMN) process should be derived from.
 * <p>
 * <b>Base Element</b> XML schema:
 * <pre>
 * &lt;xsd:element name="baseElement" type="tBaseElement"/&gt;
 * &lt;xsd:complexType name="tBaseElement" abstract="true"&gt;
 *   &lt;xsd:sequence&gt;
 *     &lt;xsd:element ref="documentation" minOccurs="0" maxOccurs="unbounded"/&gt;
 *     &lt;xsd:element ref="extensionElements" minOccurs="0" maxOccurs="1"/&gt;
 *   &lt;/xsd:sequence&gt;
 *   &lt;xsd:attribute name="id" type="xsd:ID" use="optional"/&gt;
 *   &lt;xsd:anyAttribute namespace="##other" processContents="lax"/&gt;
 * &lt;/xsd:complexType&gt;
 * </pre>
 *
 * @author Marcus Portmann
 */
public abstract class BaseElement
{
  /**
   * The ID uniquely identifying the element.
   */
  private String id;

  /**
   * Constructs a new <code>FlowElement</code>.
   *
   * @param id the ID uniquely identifying element
   */
  public BaseElement(String id)
  {
    this.id = id;
  }

  /**
   * Returns the ID uniquely identifying the element.
   *
   * @return the ID uniquely identifying the element
   */
  public String getId()
  {
    return id;
  }
}

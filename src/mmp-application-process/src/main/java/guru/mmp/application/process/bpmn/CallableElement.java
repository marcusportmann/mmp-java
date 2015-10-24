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
 * The <code>CallableElement</code> class provides the base class that all callable elements that
 * form part of a Business Process Model and Notation (BPMN) process should be derived from.
 * <p>
 * <b>Callable Element</b> XML schema:
 * <pre>
 * &lt;xsd:element name="callableElement" type="tCallableElement"/&gt;
 * &lt;xsd:complexType name="tCallableElement"&gt;
 *   &lt;xsd:complexContent&gt;
 *     &lt;xsd:extension base="tRootElement"&gt;
 *       &lt;xsd:sequence&gt;
 *         &lt;xsd:element name="supportedInterfaceRef" type="xsd:QName" minOccurs="0"
 *                      maxOccurs="unbounded"/&gt;
 *         &lt;xsd:element ref="ioSpecification" minOccurs="0" maxOccurs="1"/&gt;
 *         &lt;xsd:element ref="ioBinding" minOccurs="0" maxOccurs="unbounded"/&gt;
 *       &lt;/xsd:sequence&gt;
 *       &lt;xsd:attribute name="name" type="xsd:string"/&gt;
 *     &lt;/xsd:extension&gt;
 *   &lt;/xsd:complexContent&gt;
 * &lt;/xsd:complexType&gt;
 * </pre>
 *
 * @author Marcus Portmann
 */
public abstract class CallableElement extends RootElement
{
  /**
   * The name of the callable element.
   */
  private String name;

  /**
   * Constructs a new <code>CallableElement</code>.
   *
   * @param id   the ID uniquely identifying callable element
   * @param name the name of the callable element
   */
  public CallableElement(String id, String name)
  {
    super(id);

    this.name = name;
  }

  /**
   * Returns the name of the callable element.
   *
   * @return the name of the callable element
   */
  public String getName()
  {
    return name;
  }
}

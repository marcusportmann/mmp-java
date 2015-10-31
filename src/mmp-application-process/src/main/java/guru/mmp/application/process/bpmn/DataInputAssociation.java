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

//~--- non-JDK imports --------------------------------------------------------

import org.w3c.dom.Element;

/**
 * The <code>DataInputAssociation</code> class represents a DataInputAssociation that forms part of
 * a Process.
 * <p>
 * The DataInputAssociation can be used to associate an ItemAwareElement element with a DataInput
 * contained in an Activity. The source of such a DataAssociation can be every ItemAwareElement
 * accessible in the current scope, e.g., a Data Object, a Property, or an Expression.
 * <p>
 * <b>DataInputAssociation</b> XML schema:
 * <pre>
 * &lt;xsd:element name="dataInputAssociation" type="tDataInputAssociation" /&gt;
 * &lt;xsd:complexType name="tDataInputAssociation"&gt;
 *   &lt;xsd:complexContent&gt;
 *     &lt;xsd:extension base="tDataAssociation"/&gt;
 *   &lt;/xsd:complexContent&gt;
 * &lt;/xsd:complexType&gt;
 * </pre>
 *
 * @author Marcus Portmann
 */
public final class DataInputAssociation extends DataAssociation
{
  /**
   * Constructs a new <code>DataInputAssociation</code>.
   *
   * @param parent  the BPMN element that is the parent of this BPMN element
   * @param element the XML element containing the DataInputAssociation information
   */
  public DataInputAssociation(BaseElement parent, Element element)
  {
    super(parent, element);
  }
}

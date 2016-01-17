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

package guru.mmp.application.process.bpmn.activity;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.process.bpmn.BaseElement;
import guru.mmp.application.process.bpmn.ParserException;
import guru.mmp.application.process.bpmn.ProcessExecutionContext;
import guru.mmp.application.process.bpmn.Token;
import guru.mmp.common.util.StringUtil;
import guru.mmp.common.xml.XmlUtils;
import org.w3c.dom.Element;

import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.List;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>ServiceTask</code> class represents a Service Task that forms part of a Process.
 * <p/>
 * This task represents work that is performed by an external system where there is no human
 * intervention, like a web service.
 * <p/>
 * <b>Service Task</b> XML schema:
 * <pre>
 * &lt;xsd:element name="serviceTask" type="tServiceTask" substitutionGroup="flowElement"/&gt;
 * &lt;xsd:complexType name="tServiceTask"&gt;
 *   &lt;xsd:complexContent&gt;
 *     &lt;xsd:extension base="tTask"&gt;
 *       &lt;xsd:attribute name="implementation" type="tImplementation" default="##WebService"/&gt;
 *       &lt;xsd:attribute name="operationRef" type="xsd:QName" use="optional"/&gt;
 *     &lt;/xsd:extension&gt;
 *   &lt;/xsd:complexContent&gt;
 * &lt;/xsd:complexType&gt;
 * </pre>
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public final class ServiceTask extends Task
{
  /**
   * The technology that the Service Task will use to send and receive messages.
   */
  private Implementation implementation;

  /**
   * The reference to the optional operation associated with the Service Task.
   */
  private QName operationRef;

  /**
   * Constructs a new <code>ServiceTask</code>.
   *
   * @param parent  the BPMN element that is the parent of this Service Task
   * @param element the XML element containing the Service Task information
   */
  public ServiceTask(BaseElement parent, Element element)
  {
    super(parent, element);

    try
    {
      this.implementation = Implementation.fromId(element.getAttribute("implementation"));

      if (!StringUtil.isNullOrEmpty("operationRef"))
      {
        this.operationRef = XmlUtils.getQName(element, element.getAttribute("operationRef"));
      }
    }
    catch (Throwable e)
    {
      throw new ParserException("Failed to parse the Service Task XML data", e);
    }
  }

  /**
   * Execute the Service Task.
   *
   * @param context the execution context for the Process
   *
   * @return the list of tokens generated as a result of executing the Service Task
   */
  @Override
  public List<Token> execute(ProcessExecutionContext context)
  {
    return new ArrayList<>();
  }

  /**
   * Returns technology that the Service Task will use to send and receive messages.
   *
   * @return the technology that the Service Task will use to send and receive messages
   */
  public Implementation getImplementation()
  {
    return implementation;
  }

  /**
   * Returns the reference to the optional operation associated with the Service Task.
   *
   * @return the reference to the optional operation associated with the Service Task
   */
  public QName getOperationRef()
  {
    return operationRef;
  }
}

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

package guru.mmp.application.process.bpmn.activity;

//~--- non-JDK imports --------------------------------------------------------

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
 * The <code>ReceiveTask</code> class represents a BPMN
 * receive task that forms part of a Process.
 * <p>
 * This task waits for the arrival of a message from an external participant. Once received, the
 * task is completed.
 * <p>
 * <b>Receive Task</b> XML schema:
 * <pre>
 * &lt;xsd:element name="receiveTask" type="tReceiveTask" substitutionGroup="flowElement"/&gt;
 * &lt;xsd:complexType name="tReceiveTask"&gt;
 *   &lt;xsd:complexContent&gt;
 *     &lt;xsd:extension base="tTask"&gt;
 *       &lt;xsd:attribute name="implementation" type="tImplementation" default="##WebService"/&gt;
 *       &lt;xsd:attribute name="instantiate" type="xsd:boolean" default="false"/&gt;
 *       &lt;xsd:attribute name="messageRef" type="xsd:QName" use="optional"/&gt;
 *       &lt;xsd:attribute name="operationRef" type="xsd:QName" use="optional"/&gt;
 *     &lt;/xsd:extension&gt;
 *   &lt;/xsd:complexContent&gt;
 * &lt;/xsd:complexType&gt;
 * </pre>
 *
 * @author Marcus Portmann
 */
public final class ReceiveTask extends Task
{
  /**
   * The technology that the receive task will use to send and receive messages.
   */
  private Implementation implementation;

  /**
   * Can the receive task be used as the instantiation mechanism for the process?
   */
  private boolean instantiate;

  /**
   * The QName for the optional message associated with the receive task.
   */
  private QName messageRef;

  /**
   * The QName for the optional operation associated with the receive task.
   */
  private QName operationRef;

  /**
   * Constructs a new <code>ReceiveTask</code>.
   *
   * @param element the XML element containing the receive task information
   */
  public ReceiveTask(Element element)
  {
    super(element);

    try
    {
      this.implementation = Implementation.fromId(element.getAttribute("implementation"));

      this.instantiate = Boolean.parseBoolean(element.getAttribute("instantiate"));

      if (!StringUtil.isNullOrEmpty("messageRef"))
      {
        this.messageRef = XmlUtils.getQName(element, element.getAttribute("messageRef"));
      }

      if (!StringUtil.isNullOrEmpty("operationRef"))
      {
        this.operationRef = XmlUtils.getQName(element, element.getAttribute("operationRef"));
      }
    }
    catch (Throwable e)
    {
      throw new ParserException("Failed to parse the receive task XML data", e);
    }
  }

  /**
   * Execute the BPMN task.
   *
   * @param context the execution context for the Process
   *
   * @return the list of tokens generated as a result of executing the Business Process Model and
   *         Notation (BPMN) task
   */
  @Override
  public List<Token> execute(ProcessExecutionContext context)
  {
    return new ArrayList<>();
  }

  /**
   * Returns the technology that the receive task will use to send and receive messages.
   *
   * @return the technology that the receive task will use to send and receive messages
   */
  public Implementation getImplementation()
  {
    return implementation;
  }

  /**
   * Returns whether the receive task be used as the instantiation mechanism for the process.
   *
   * @return <code>true</code> if the receive task be used as the instantiation mechanism for the
   *         process or <code>false</code> otherwise
   */
  public boolean getInstantiate()
  {
    return instantiate;
  }

  /**
   * Returns the QName for the optional message associated with the receive task.
   *
   * @return the QName for the optional message associated with the receive task
   */
  public QName getMessageRef()
  {
    return messageRef;
  }

  /**
   * Returns the QName for the optional operation associated with the receive task.
   *
   * @return the QName for the optional operation associated with the receive task
   */
  public QName getOperationRef()
  {
    return operationRef;
  }
}

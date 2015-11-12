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
 * The <code>SendTask</code> class represents a Send Task that forms part of a Process.
 * <p>
 * This task represents sending a message to an external participant. Once sent, the task is
 * completed. A message can only be sent between different roles.
 * <p>
 * <b>Send Task</b> XML schema:
 * <pre>
 * &lt;xsd:element name="sendTask" type="tSendTask" substitutionGroup="flowElement"/&gt;
 * &lt;xsd:complexType name="tSendTask"&gt;
 *   &lt;xsd:complexContent&gt;
 *     &lt;xsd:extension base="tTask"&gt;
 *       &lt;xsd:attribute name="implementation" type="tImplementation" default="##WebService"/&gt;
 *       &lt;xsd:attribute name="messageRef" type="xsd:QName" use="optional"/&gt;
 *       &lt;xsd:attribute name="operationRef" type="xsd:QName" use="optional"/&gt;
 *     &lt;/xsd:extension&gt;
 *   &lt;/xsd:complexContent&gt;
 * &lt;/xsd:complexType&gt;
 * </pre>
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public final class SendTask extends Task
{
  /**
   * The technology that the Send Task will use to send and receive messages.
   */
  private Implementation implementation;

  /**
   * The reference to the optional message associated with the Send Task.
   */
  private QName messageRef;

  /**
   * The reference to the optional operation associated with the Send Task.
   */
  private QName operationRef;

  /**
   * Constructs a new <code>SendTask</code>.
   *
   * @param parent  the BPMN element that is the parent of this Send Task
   * @param element the XML element containing the Send Task information
   */
  public SendTask(BaseElement parent, Element element)
  {
    super(parent, element);

    try
    {
      this.implementation = Implementation.fromId(element.getAttribute("implementation"));

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
      throw new ParserException("Failed to parse the SendTask XML data", e);
    }
  }

  /**
   * Execute the Send Task.
   *
   * @param context the execution context for the Process
   *
   * @return the list of tokens generated as a result of executing the Send Task
   */
  @Override
  public List<Token> execute(ProcessExecutionContext context)
  {
    return new ArrayList<>();
  }

  /**
   * Returns the technology that the Send Task will use to send and receive messages.
   *
   * @return the technology that the Send Task will use to send and receive messages
   */
  public Implementation getImplementation()
  {
    return implementation;
  }

  /**
   * Returns the reference to the optional message associated with the Send Task.
   *
   * @return the reference to the optional message associated with the Send Task
   */
  public QName getMessageRef()
  {
    return messageRef;
  }

  /**
   * Returns the reference to the optional operation associated with the Send Task.
   *
   * @return the reference to the optional operation associated with the Send Task
   */
  public QName getOperationRef()
  {
    return operationRef;
  }
}

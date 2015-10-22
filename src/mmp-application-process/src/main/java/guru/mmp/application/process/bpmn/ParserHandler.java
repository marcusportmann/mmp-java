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

import guru.mmp.application.process.bpmn.activity.*;
import guru.mmp.application.process.bpmn.event.EndEvent;
import guru.mmp.application.process.bpmn.event.StartEvent;
import guru.mmp.application.process.bpmn.gateway.GatewayDirection;
import guru.mmp.application.process.bpmn.gateway.ParallelGateway;
import guru.mmp.common.util.StringUtil;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * The <code>ParserHandler</code> class implements the SAX handler that provides the capability to
 * parse Business Process Model and Notation (BPMN) format files.
 *
 * @author Marcus Portmann
 */
public class ParserHandler extends DefaultHandler
{
  /**
   * The Business Process Model and Notation (BPMN) flow element currently being parsed.
   */
  private FlowElement currentFlowElement;

  /**
   * The Business Process Model and Notation (BPMN) process currently being parsed.
   */
  private Process currentProcess;

  /**
   * The Business Process Model and Notation (BPMN) sub-process stack (sub-processes can be nested).
   */
  private Stack<SubProcess> subProcessStack = new Stack<>();

  /**
   * The parsed Business Process Model and Notation (BPMN) processes.
   */
  private List<Process> processes = new ArrayList<>();

  /**
   * The character data inside the elements currently being parsed.
   */
  private Stack<StringBuilder> elementsCharacters = new Stack<>();

  /**
   * Constructs a new <code>ParserHandler</code>.
   */
  public ParserHandler() {}

  /**
   * Receive notification of character data inside an element.
   *
   * @param ch     the characters
   * @param start  the start position in the character array
   * @param length the number of characters to use from the character array
   *
   * @throws SAXException
   */
  @Override
  public void characters(char[] ch, int start, int length)
    throws SAXException
  {
    if (!elementsCharacters.isEmpty())
    {
      elementsCharacters.peek().append(String.copyValueOf(ch, start, length));
    }
  }

  /**
   * Receive notification of the end of an element.
   *
   * @param uri       the Namespace URI, or the empty string if the element has no Namespace URI or
   *                  if Namespace processing is not being performed.
   * @param localName the local name (without prefix), or the empty string if Namespace processing
   *                  is not being performed
   * @param qName     the qualified name (with prefix), or the empty string if qualified names are
   *                  not available
   *
   * @throws SAXException
   */
  @Override
  public void endElement(String uri, String localName, String qName)
    throws SAXException
  {
    StringBuilder elementCharacters = elementsCharacters.pop();

    switch (qName)
    {
      case "businessRuleTask":
      {
        addCurrentFlowElementToProcessOrSubProcess();

        break;
      }

      case "definitions":
      {
        break;
      }

      case "endEvent":
      {
        addCurrentFlowElementToProcessOrSubProcess();

        break;
      }

      case "extensionElements":
      {
        break;
      }

      case "incoming":
      {
        if (currentFlowElement instanceof FlowNode)
        {
          ((FlowNode) currentFlowElement).addIncomingFlowElement(elementCharacters.toString());
        }

        break;
      }

      case "manualTask":
      {
        addCurrentFlowElementToProcessOrSubProcess();

        break;
      }

      case "outgoing":
      {
        if (currentFlowElement instanceof FlowNode)
        {
          ((FlowNode) currentFlowElement).addOutgoingFlowElement(elementCharacters.toString());
        }

        break;
      }

      case "parallelGateway":
      {
        addCurrentFlowElementToProcessOrSubProcess();

        break;
      }

      case "process":
      {
        processes.add(currentProcess);

        currentProcess = null;

        break;
      }

      case "receiveTask":
      {
        addCurrentFlowElementToProcessOrSubProcess();

        break;
      }

      case "script":
      {
        if ((currentFlowElement != null) && (currentFlowElement instanceof Task))
        {
          TaskBehavior taskBehavior = ((Task) currentFlowElement).getTaskBehavior();

          if (taskBehavior instanceof ScriptTaskBehavior)
          {
            ((ScriptTaskBehavior) taskBehavior).setScript(elementCharacters.toString());
          }
          else
          {
            throw new ParserException("Failed to parse the \"script\" node: "
                + "The task behavior is not a ScriptTaskBehavior");
          }
        }
        else
        {
          throw new ParserException("Failed to parse the \"script\" node: "
              + "The current flow node is not a task");
        }

        break;
      }

      case "scriptTask":
      {
        addCurrentFlowElementToProcessOrSubProcess();

        break;
      }

      case "sendTask":
      {
        addCurrentFlowElementToProcessOrSubProcess();

        break;
      }

      case "sequenceFlow":
      {
        addCurrentFlowElementToProcessOrSubProcess();

        break;
      }

      case "serviceTask":
      {
        addCurrentFlowElementToProcessOrSubProcess();

        break;
      }

      case "standardLoopCharacteristics":
      {
        // TODO: IMPLEMENT THIS -- MARCUS

        break;
      }

      case "startEvent":
      {
        addCurrentFlowElementToProcessOrSubProcess();

        break;
      }

      case "subProcess":
      {
        currentFlowElement = subProcessStack.pop();

        addCurrentFlowElementToProcessOrSubProcess();

        break;
      }

      case "task":
      {
        addCurrentFlowElementToProcessOrSubProcess();

        break;
      }

      case "transaction":
      {
        currentFlowElement = subProcessStack.pop();

        addCurrentFlowElementToProcessOrSubProcess();

        break;
      }

      case "userTask":
      {
        addCurrentFlowElementToProcessOrSubProcess();

        break;
      }
    }
  }

  /**
   * Handle the SAX parsing error.
   *
   * @param e the error
   *
   * @throws SAXException
   */
  @Override
  public void error(SAXParseException e)
    throws SAXException
  {
    throw e;
  }

  /**
   * Handle the SAX parsing fatal error.
   *
   * @param e the fatal error
   *
   * @throws SAXException
   */
  @Override
  public void fatalError(SAXParseException e)
    throws SAXException
  {
    throw e;
  }

  /**
   * Returns the parsed Business Process Model and Notation (BPMN) processes.
   *
   * @return the parsed Business Process Model and Notation (BPMN) processes
   */
  public List<Process> getProcesses()
  {
    return processes;
  }

  /**
   * Receive notification of the start of an element.
   *
   * @param uri        the Namespace URI, or the empty string if the element has no Namespace URI
   *                   or if Namespace processing is not being performed
   * @param localName  the local name (without prefix), or the empty string if Namespace processing
   *                   is not being performed
   * @param qName      the qualified name (with prefix), or the empty string if qualified names are
   *                   not available
   * @param attributes The attributes attached to the element. If there are no attributes, it shall
   *                   be an empty Attributes object.
   *
   * @throws SAXException
   */
  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes)
    throws SAXException
  {
    elementsCharacters.push(new StringBuilder());

    switch (qName)
    {
      case "businessRuleTask":
      {
        parseBusinessRuleTaskNode(attributes);

        break;
      }

      case "definitions":
      {
        break;
      }

      case "endEvent":
      {
        parseEndEventNode(attributes);

        break;
      }

      case "extensionElements":
      {
        break;
      }

      case "incoming":
      {
        /*
         * Do nothing as the tag has no attributes and we are interested in the content of the
         * element which is the ID of the incoming flow element for the current flow node.
         */

        break;
      }

      case "manualTask":
      {
        parseManualTaskNode(attributes);

        break;
      }

      case "outgoing":
      {
        /*
         * Do nothing as the tag has no attributes and we are interested in the content of the
         * element which is the ID of the outgoing flow element for the current flow node.
         */

        break;
      }

      case "parallelGateway":
      {
        parseParallelGatewayNode(attributes);

        break;
      }

      case "process":
      {
        parseProcessNode(attributes);

        break;
      }

      case "receiveTask":
      {
        parseReceiveTaskNode(attributes);

        break;
      }

      case "script":
      {
        /*
         * Do nothing as the tag has no attributes and we are interested in the content of the
         * element which is the script.
         */

        break;
      }

      case "scriptTask":
      {
        parseScriptTaskNode(attributes);

        break;
      }

      case "sendTask":
      {
        parseSendTaskNode(attributes);

        break;
      }

      case "sequenceFlow":
      {
        parseSequenceFlowNode(attributes);

        break;
      }

      case "serviceTask":
      {
        parseServiceTaskNode(attributes);

        break;
      }

      case "standardLoopCharacteristics":
      {
        // TODO: IMPLEMENT THIS -- MARCUS

        break;
      }

      case "startEvent":
      {
        parseStartEventNode(attributes);

        break;
      }

      case "subProcess":
      {
        parseSubProcessNode(attributes);

        break;
      }

      case "task":
      {
        parseDefaultTaskNode(attributes);

        break;
      }

      case "transaction":
      {
        parseTransactionNode(attributes);

        break;
      }

      case "userTask":
      {
        parseUserTaskNode(attributes);

        break;
      }

      default:
      {
        if (qName.startsWith("yaoqiang:"))
        {
          break;
        }

        throw new ParserException("Failed to parse the unknown node (" + qName + ")");
      }
    }
  }

  /**
   * Handle the SAX parsing warning.
   *
   * @param e the warning
   *
   * @throws SAXException
   */
  @Override
  public void warning(SAXParseException e)
    throws SAXException
  {
    throw e;
  }

  private void addCurrentFlowElementToProcessOrSubProcess()
  {
    if (currentFlowElement != null)
    {
      if (subProcessStack.isEmpty())
      {
        currentProcess.addFlowElement(currentFlowElement);
      }
      else
      {
        subProcessStack.peek().addFlowElement(currentFlowElement);
      }

      currentFlowElement = null;
    }
  }

  private void parseBusinessRuleTaskNode(Attributes attributes)
  {
    try
    {
      ImplementationType implementationType =
        ImplementationType.fromId(attributes.getValue("implementation"));

      BusinessRuleTaskBehavior businessRuleTaskBehavior =
        new BusinessRuleTaskBehavior(implementationType);

      parseTask("businessRuleTask", attributes, businessRuleTaskBehavior);
    }
    catch (Throwable e)
    {
      throw new ParserException("Failed to parse the \"businessRuleTask\" node", e);
    }
  }

  private void parseDefaultTaskNode(Attributes attributes)
  {
    try
    {
      DefaultTaskBehavior defaultTaskBehavior = new DefaultTaskBehavior();

      parseTask("task", attributes, defaultTaskBehavior);
    }
    catch (Throwable e)
    {
      throw new ParserException("Failed to parse the \"task\" node", e);
    }
  }

  private void parseEndEventNode(Attributes attributes)
  {
    try
    {
      String id = attributes.getValue("id");

      String name = attributes.getValue("name");

      currentFlowElement = new EndEvent(id, name);
    }
    catch (Throwable e)
    {
      throw new ParserException("Failed to parse the \"endEvent\" node", e);
    }
  }

  private void parseGlobalTask(String nodeName, Attributes attributes, TaskBehavior taskBehavior)
  {
    try
    {
      String id = attributes.getValue("id");

      String name = attributes.getValue("name");

      currentFlowElement = new GlobalTask(id, name, taskBehavior);
    }
    catch (Throwable e)
    {
      throw new ParserException("Failed to parse the generic task attributes for the \"" + nodeName
          + "\" task node");
    }
  }

  private void parseManualTaskNode(Attributes attributes)
  {
    try
    {
      ManualTaskBehavior manualTaskBehavior = new ManualTaskBehavior();

      parseTask("manualTask", attributes, manualTaskBehavior);
    }
    catch (Throwable e)
    {
      throw new ParserException("Failed to parse the \"manualTask\" node", e);
    }
  }

  private void parseParallelGatewayNode(Attributes attributes)
  {
    try
    {
      String id = attributes.getValue("id");

      String name = attributes.getValue("name");

      GatewayDirection gatewayDirection =
        GatewayDirection.fromId(attributes.getValue("gatewayDirection"));

      currentFlowElement = new ParallelGateway(id, name, gatewayDirection);
    }
    catch (Throwable e)
    {
      throw new ParserException("Failed to parse the \"parallelGateway\" node", e);
    }
  }

  private void parseProcessNode(Attributes attributes)
  {
    try
    {
      String id = attributes.getValue("id");

      boolean isClosed = Boolean.parseBoolean(attributes.getValue("isClosed"));

      boolean isExecutable = Boolean.parseBoolean(attributes.getValue("isExecutable"));

      String processType = attributes.getValue("processType");

      if (!processType.equals("None"))
      {
        throw new ParserException("Failed to parse the unsupported process type (" + processType
            + "");
      }

      currentProcess = new Process(id, isClosed, isExecutable);
    }
    catch (Throwable e)
    {
      throw new ParserException("Failed to parse the \"process\" node", e);
    }
  }

  private void parseReceiveTaskNode(Attributes attributes)
  {
    try
    {
      ImplementationType implementationType =
        ImplementationType.fromId(attributes.getValue("implementation"));

      boolean instantiateProcess = Boolean.parseBoolean("instantiate");

      ReceiveTaskBehavior receiveTaskBehavior = new ReceiveTaskBehavior(implementationType,
        instantiateProcess);

      parseTask("receiveTask", attributes, receiveTaskBehavior);
    }
    catch (Throwable e)
    {
      throw new ParserException("Failed to parse the \"receiveTask\" node", e);
    }
  }

  private void parseScriptTaskNode(Attributes attributes)
  {
    try
    {
      ScriptTaskBehavior.ScriptFormat scriptFormat = ScriptTaskBehavior.ScriptFormat.fromMimeType(
          StringUtil.notNull(attributes.getValue("scriptFormat")));

      ScriptTaskBehavior scriptTaskBehavior = new ScriptTaskBehavior(scriptFormat);

      parseTask("scriptTask", attributes, scriptTaskBehavior);
    }
    catch (Throwable e)
    {
      throw new ParserException("Failed to parse the \"scriptTask\" node", e);
    }
  }

  private void parseSendTaskNode(Attributes attributes)
  {
    try
    {
      ImplementationType implementationType =
        ImplementationType.fromId(attributes.getValue("implementation"));

      SendTaskBehavior sendTaskBehavior = new SendTaskBehavior(implementationType);

      parseTask("sendTask", attributes, sendTaskBehavior);
    }
    catch (Throwable e)
    {
      throw new ParserException("Failed to parse the \"sendTask\" node", e);
    }
  }

  private void parseSequenceFlowNode(Attributes attributes)
  {
    try
    {
      String id = attributes.getValue("id");

      String sourceRef = attributes.getValue("sourceRef");

      String targetRef = attributes.getValue("targetRef");

      currentFlowElement = new SequenceFlow(id, sourceRef, targetRef);
    }
    catch (Throwable e)
    {
      throw new ParserException("Failed to parse the \"sequenceFlow\" node", e);
    }
  }

  private void parseServiceTaskNode(Attributes attributes)
  {
    try
    {
      ImplementationType implementationType =
        ImplementationType.fromId(attributes.getValue("implementation"));

      ServiceTaskBehavior serviceTaskBehavior = new ServiceTaskBehavior(implementationType);

      parseTask("serviceTask", attributes, serviceTaskBehavior);
    }
    catch (Throwable e)
    {
      throw new ParserException("Failed to parse the \"serviceTask\" node", e);
    }
  }

  private void parseStartEventNode(Attributes attributes)
  {
    try
    {
      String id = attributes.getValue("id");

      String name = attributes.getValue("name");

      boolean isInterrupting = Boolean.parseBoolean(attributes.getValue("isInterrupting"));

      boolean parallelMultiple = Boolean.parseBoolean(attributes.getValue("parallelMultiple"));

      currentFlowElement = new StartEvent(id, name, isInterrupting);
    }
    catch (Throwable e)
    {
      throw new ParserException("Failed to parse the \"startEvent\" node", e);
    }
  }

  private void parseSubProcessNode(Attributes attributes)
  {
    try
    {
      String id = attributes.getValue("id");

      String name = attributes.getValue("name");

      boolean forCompensation = Boolean.parseBoolean(attributes.getValue("isForCompensation"));

      int startQuantity = Integer.parseInt(attributes.getValue("startQuantity"));

      int completionQuantity = Integer.parseInt(attributes.getValue("completionQuantity"));

      boolean triggeredByEvent = Boolean.parseBoolean(attributes.getValue("triggeredByEvent"));

      subProcessStack.push(new SubProcess(id, name, forCompensation, startQuantity,
          completionQuantity, triggeredByEvent));
    }
    catch (Throwable e)
    {
      throw new ParserException("Failed to parse the \"subProcess\" node", e);
    }
  }

  private void parseTask(String nodeName, Attributes attributes, TaskBehavior taskBehavior)
  {
    try
    {
      String id = attributes.getValue("id");

      String name = attributes.getValue("name");

      boolean forCompensation = Boolean.parseBoolean(attributes.getValue("isForCompensation"));

      int startQuantity = Integer.parseInt(attributes.getValue("startQuantity"));

      int completionQuantity = Integer.parseInt(attributes.getValue("completionQuantity"));

      currentFlowElement = new Task(id, name, forCompensation, startQuantity, completionQuantity,
          taskBehavior);
    }
    catch (Throwable e)
    {
      throw new ParserException("Failed to parse the generic task attributes for the \"" + nodeName
          + "\" task node");
    }
  }

  private void parseTransactionNode(Attributes attributes)
  {
    try
    {
      // <transaction method="##Compensate"

      String id = attributes.getValue("id");

      String name = attributes.getValue("name");

      String method = attributes.getValue("method");

      boolean forCompensation = Boolean.parseBoolean(attributes.getValue("isForCompensation"));

      int startQuantity = Integer.parseInt(attributes.getValue("startQuantity"));

      int completionQuantity = Integer.parseInt(attributes.getValue("completionQuantity"));

      boolean triggeredByEvent = Boolean.parseBoolean(attributes.getValue("triggeredByEvent"));

      subProcessStack.push(new TransactionSubProcess(id, name, forCompensation, startQuantity,
          completionQuantity, triggeredByEvent));
    }
    catch (Throwable e)
    {
      throw new ParserException("Failed to parse the \"transaction\" node", e);
    }
  }

  private void parseUserTaskNode(Attributes attributes)
  {
    try
    {
      ImplementationType implementationType =
        ImplementationType.fromId(attributes.getValue("implementation"));

      UserTaskBehavior userTaskBehavior = new UserTaskBehavior(implementationType);

      parseTask("userTask", attributes, userTaskBehavior);
    }
    catch (Throwable e)
    {
      throw new ParserException("Failed to parse the \"userTask\" node", e);
    }
  }
}

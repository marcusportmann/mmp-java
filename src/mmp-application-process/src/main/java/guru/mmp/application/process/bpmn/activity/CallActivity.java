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
 * The <code>CallActivity</code> class represents a BPMN
 * call activity that forms part of a Process.
 * <p>
 * A call activity is just a reusable activity. If a sub-process is referenced in more than one
 * process (diagram), it can be defined in its own diagram and "called" from each process that
 * uses it.
 * <p>
 * <b>Call Activity</b> XML schema:
 * <pre>
 * &lt;xsd:element name="callActivity" type="tCallActivity" substitutionGroup="flowElement"/&gt;
 * &lt;xsd:complexType name="tCallActivity"&gt;
 *   &lt;xsd:complexContent&gt;
 *     &lt;xsd:extension base="tActivity"&gt;
 *       &lt;xsd:attribute name="calledElement" type="xsd:QName" use="optional"/&gt;
 *     &lt;/xsd:extension&gt;
 *   &lt;/xsd:complexContent&gt;
 * &lt;/xsd:complexType&gt;
 * </pre>
 *
 * @author Marcus Portmann
 */
public final class CallActivity extends Activity
{
  /**
   * The QName for the element to be called, which will be either a process or a global task.
   */
  private QName calledElement;

  /**
   * Constructs a new <code>CallActivity</code>.
   *
   * @param element the XML element containing the call activity information
   */
  public CallActivity(Element element)
  {
    super(element);

    try
    {
      if (!StringUtil.isNullOrEmpty("calledElement"))
      {
        this.calledElement = XmlUtils.getQName(element, element.getAttribute("calledElement"));
      }
    }
    catch (Throwable e)
    {
      throw new ParserException("Failed to parse the call activity XML data", e);
    }
  }

  /**
   * Execute the BPMN call activity.
   *
   * @param context the execution context for the Process
   *
   * @return the list of tokens generated as a result of executing the Business Process Model and
   *         Notation (BPMN) call activity
   */
  @Override
  public List<Token> execute(ProcessExecutionContext context)
  {
    return new ArrayList<>();
  }

  /**
   * Returns the QName for the element to be called, which will be either a process or a global
   * task.
   *
   * @return the QName for the element to be called, which will be either a process or a global task
   */
  public QName getCalledElement()
  {
    return calledElement;
  }
}

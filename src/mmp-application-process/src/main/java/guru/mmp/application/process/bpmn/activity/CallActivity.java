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

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

/**
 * The <code>CallActivity</code> class represents a Call Activity that forms part of a Process.
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
@SuppressWarnings("unused")
public final class CallActivity extends Activity
{
  /**
   * The reference to the element to be called, which will be either a Process or a Global Task.
   */
  private QName calledElementRef;

  /**
   * Constructs a new <code>CallActivity</code>.
   *
   * @param parent  the BPMN element that is the parent of this Call Activity
   * @param element the XML element containing the Call Activity information
   */
  public CallActivity(BaseElement parent, Element element)
  {
    super(parent, element);

    try
    {
      if (!StringUtil.isNullOrEmpty("calledElement"))
      {
        this.calledElementRef = XmlUtils.getQName(element, element.getAttribute("calledElement"));
      }
    }
    catch (Throwable e)
    {
      throw new ParserException("Failed to parse the Call Activity XML data", e);
    }
  }

  /**
   * Execute the Call Activity.
   *
   * @param context the execution context for the Process
   *
   * @return the list of tokens generated as a result of executing the Call Activity
   */
  @Override
  public List<Token> execute(ProcessExecutionContext context)
  {
    return new ArrayList<>();
  }

  /**
   * Returns the reference to the element to be called, which will be either a Process or a Global
   * Task.
   *
   * @return the reference to the element to be called, which will be either a Process or a Global
   *         Task
   */
  public QName getCalledElementRef()
  {
    return calledElementRef;
  }
}

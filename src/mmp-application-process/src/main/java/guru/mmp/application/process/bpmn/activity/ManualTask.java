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
import guru.mmp.application.process.bpmn.ProcessExecutionContext;
import guru.mmp.application.process.bpmn.Token;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>ManualTask</code> class represents a Manual Task that forms part of a Process.
 * <p>
 * This task represents work that is not automated and is performed outside the control of the
 * BPM engine.
 * <p>
 * <b>Manual Task</b> XML schema:
 * <pre>
 * &lt;xsd:element name="manualTask" type="tManualTask" substitutionGroup="flowElement"/&gt;
 * &lt;xsd:complexType name="tManualTask"&gt;
 *   &lt;xsd:complexContent&gt;
 *     &lt;xsd:extension base="tTask"/&gt;
 *   &lt;/xsd:complexContent&gt;
 * &lt;/xsd:complexType&gt;
 * </pre>
 *
 * @author Marcus Portmann
 */
public final class ManualTask extends Task
{
  /**
   * Constructs a new <code>ManualTask</code>.
   *
   * @param parent  the BPMN element that is the parent of this Manual Task
   * @param element the XML element containing the Manual Task information
   */
  public ManualTask(BaseElement parent, Element element)
  {
    super(parent, element);
  }

  /**
   * Execute the Manual Task.
   *
   * @param context the execution context for the Process
   *
   * @return the list of tokens generated as a result of executing the Manual Task
   */
  @Override
  public List<Token> execute(ProcessExecutionContext context)
  {
    return new ArrayList<>();
  }
}

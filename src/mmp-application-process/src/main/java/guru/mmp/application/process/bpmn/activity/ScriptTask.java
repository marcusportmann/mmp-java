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
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>ScriptTask</code> class represents a BPMN
 * script task that forms part of a Process.
 * <p>
 * This task represents work that is performed by the BPM engine as an automated function written
 * in a script language like JavaScript.
 * <p>
 * <b>Script Task</b> XML schema:
 * <pre>
 * &lt;xsd:element name="scriptTask" type="tScriptTask" substitutionGroup="flowElement"/&gt;
 * &lt;xsd:complexType name="tScriptTask"&gt;
 *   &lt;xsd:complexContent&gt;
 *     &lt;xsd:extension base="tTask"&gt;
 *       &lt;xsd:sequence&gt;
 *         &lt;xsd:element ref="script" minOccurs="0" maxOccurs="1"/&gt;
 *       &lt;/xsd:sequence&gt;
 *       &lt;xsd:attribute name="scriptFormat" type="xsd:anyURI"/&gt;
 *     &lt;/xsd:extension&gt;
 *   &lt;/xsd:complexContent&gt;
 * &lt;/xsd:complexType&gt;
 * </pre>
 *
 * @author Marcus Portmann
 */
public final class ScriptTask extends Task
{
  /**
   * The script.
   */
  private String script;

  /**
   * The script format.
   */
  private ScriptFormat scriptFormat;

  /**
   * Constructs a new <code>ScriptTask</code>.
   *
   * @param element the XML element containing the script task information
   */
  public ScriptTask(Element element)
  {
    super(element);

    try
    {
      this.scriptFormat =
        ScriptFormat.fromMimeType(StringUtil.notNull(element.getAttribute("scriptFormat")));

      NodeList scriptElements = element.getElementsByTagName("script");

      if (scriptElements.getLength() > 0)
      {
        this.script = ((Element) scriptElements.item(0)).getTextContent();

        System.out.println("[DEBUG] script = " + script);
      }
    }
    catch (Throwable e)
    {
      throw new ParserException("Failed to parse the script task XML data", e);
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
   * Returns the script.
   *
   * @return the script
   */
  public String getScript()
  {
    return script;
  }

  /**
   * Returns the script format.
   *
   * @return the script format
   */
  public ScriptFormat getScriptFormat()
  {
    return scriptFormat;
  }

  /**
   * Set the script.
   *
   * @param script the script
   */
  public void setScript(String script)
  {
    this.script = script;
  }
}

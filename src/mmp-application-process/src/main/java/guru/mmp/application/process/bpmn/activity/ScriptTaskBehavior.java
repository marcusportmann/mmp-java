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

import guru.mmp.application.process.bpmn.ProcessExecutionContext;
import guru.mmp.application.process.bpmn.Token;
import guru.mmp.common.util.StringUtil;

import sun.font.Script;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.List;

/**
 * The <code>ScriptTaskBehavior</code> class implements the behavior for a Business Process Model
 * and Notation (BPMN) script task that forms part of a BPMN process.
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
public final class ScriptTaskBehavior extends TaskBehavior
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
   * Constructs a new <code>ScriptTaskBehavior</code>.
   *
   * @param scriptFormat the script format
   */
  public ScriptTaskBehavior(ScriptFormat scriptFormat)
  {
    this.scriptFormat = scriptFormat;
  }

  /**
   * The enumeration giving the possible script formats for a script task.
   */
  public enum ScriptFormat
  {
    JAVASCRIPT("text/javascript", "JavaScript"), GROOVY("text/x-groovy", "Groovy"),
    JAVA("http://www.java.com/java", "Java"), MVEL("http://www.mvel.org/2.0", "MVEL");

    private String mimeType;
    private String name;

    ScriptFormat(String mimeType, String name)
    {
      this.mimeType = mimeType;
      this.name = name;
    }

    /**
     * Returns the script format given by the specified mime type.
     *
     * @param mimeType the mime type identifying the script format
     *
     * @return the script format given by the specified mime type
     */
    public static ScriptFormat fromMimeType(String mimeType)
    {
      if (StringUtil.isNullOrEmpty(mimeType))
      {
        throw new RuntimeException("Invalid mime type for script format (" + mimeType + ")");
      }

      mimeType = mimeType.toLowerCase().trim();

      switch (mimeType)
      {
        case "text/javascript":
          return ScriptFormat.JAVASCRIPT;

        case "text/x-groovy":
          return ScriptFormat.GROOVY;

        case "http://www.java.com/java":
          return ScriptFormat.JAVA;

        case "http://www.mvel.org/2.0":
          return ScriptFormat.MVEL;

        default:

          throw new RuntimeException("Invalid mime type for script format (" + mimeType + ")");
      }
    }

    /**
     * Returns the mime type identifying the script format.
     *
     * @return the mime type identifying the script format
     */
    public String getMimeType()
    {
      return mimeType;
    }

    /**
     * Returns the name of the script format.
     *
     * @return the name of the script format
     */
    public String getName()
    {
      return name;
    }

    /**
     * Return the string representation of the script format enumeration value.
     *
     * @return the string representation of the script format enumeration value
     */
    public String toString()
    {
      return mimeType;
    }
  }

  /**
   * Execute the behavior for the Business Process Model and Notation (BPMN) script task.
   *
   * @param context the execution context for the Business Process Model and Notation (BPMN) process
   *
   * @return the list of tokens generated as a result of executing the behavior for the
   *         Business Process Model and Notation (BPMN) script task
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

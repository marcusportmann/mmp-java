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

/**
 * The <code>ProcessType</code> enumeration defines the possible types of Processes.
 * <p>
 * <b>Process Type</b> XML schema:
 * <pre>
 * &lt;xsd:simpleType name="tProcessType"&gt;
 *   &lt;xsd:restriction base="xsd:string"&gt;
 *     &lt;xsd:enumeration value="None"/&gt;
 *     &lt;xsd:enumeration value="Public"/&gt;
 *     &lt;xsd:enumeration value="Private"/&gt;
 *   &lt;/xsd:restriction&gt;
 * &lt;/xsd:simpleType&gt;
 * </pre>
 *
 * @author Marcus Portmann
 */
public enum ProcessType
{
  NONE("None", "None"), PUBLIC("Public", "Public"), PRIVATE("Private", "Private");

  private String id;
  private String name;

  ProcessType(String id, String name)
  {
    this.id = id;
    this.name = name;
  }

  /**
   * Returns the Process type given by the specified ID.
   *
   * @param id the ID identifying the Process type
   *
   * @return the Process type given by the specified ID
   */
  public static ProcessType fromId(String id)
  {
    switch (id)
    {
      case "None":
        return ProcessType.NONE;

      case "Public":
        return ProcessType.PUBLIC;

      case "Private":
        return ProcessType.PRIVATE;

      default:
        throw new RuntimeException("Invalid ID for process type (" + id + ")");
    }
  }

  /**
   * Returns the ID identifying the Process type.
   *
   * @return the ID identifying the Process type
   */
  public String getId()
  {
    return id;
  }

  /**
   * Returns the name of the Process type.
   *
   * @return the name of the Process type
   */
  public String getName()
  {
    return name;
  }

  /**
   * Return the string representation of the <code>ProcessType</code>
   * enumeration value.
   *
   * @return the string representation of the <code>ProcessType</code>
   *         enumeration value
   */
  public String toString()
  {
    return id;
  }
}

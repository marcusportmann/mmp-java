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

/**
 * The <code>ImplementationType</code> enumeration defines the possible ways that a Business Process
 * Model and Notation (BPMN) send task or service task can be implemented.
 *
 * @author Marcus Portmann
 */
public enum ImplementationType
{
  UNSPECIFIED("##unspecified", "Unspecified"), WEB_SERVICE("##WebService", "WebService");

  private String id;
  private String name;

  ImplementationType(String id, String name)
  {
    this.id = id;
    this.name = name;
  }

  /**
   * Returns the implementation type given by the specified ID.
   *
   * @param id the ID identifying the implementation type
   *
   * @return the implementation type given by the specified ID
   */
  public static ImplementationType fromId(String id)
  {
    switch (id)
    {
      case "##unspecified":
        return ImplementationType.UNSPECIFIED;

      case "##WebService":
        return ImplementationType.WEB_SERVICE;

      default:
        throw new RuntimeException("Invalid ID for implementation type (" + id + ")");
    }
  }

  /**
   * Returns the ID identifying the implementation type.
   *
   * @return the ID identifying the implementation type
   */
  public String getId()
  {
    return id;
  }

  /**
   * Returns the name of the implementation type.
   *
   * @return the name of the implementation type
   */
  public String getName()
  {
    return name;
  }

  /**
   * Return the string representation of the <code>ImplementationType</code>
   * enumeration value.
   *
   * @return the string representation of the <code>ImplementationType</code>
   *         enumeration value
   */
  public String toString()
  {
    return id;
  }
}

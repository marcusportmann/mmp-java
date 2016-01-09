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

package guru.mmp.application.reporting;

import java.io.Serializable;
import java.util.UUID;

/**
 * The <code>ReportDefinitionSummary</code> class holds the summary information for a report
 * definition.
 *
 * @author Marcus Portmann
 */
public class ReportDefinitionSummary
  implements Serializable
{
  private static final long serialVersionUID = 1000000;

  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the report definition.
   */
  private UUID id;

  /**
   * The name of the report definition.
   */
  private String name;

  /**
   * Constructs a new <code>ReportDefinitionSummary</code>.
   *
   * @param id   the Universally Unique Identifier (UUID) used to uniquely identify the report
   *             definition
   * @param name the name of the report definition
   */
  public ReportDefinitionSummary(UUID id, String name)
  {
    this.id = id;
    this.name = name;
  }

  /**
   * Constructs a new <code>ReportDefinitionSummary</code>.
   */
  @SuppressWarnings("unused")
  private ReportDefinitionSummary() {}

  /**
   * Returns the Universally Unique Identifier (UUID) used to uniquely identify the report
   * definition.
   *
   * @return the Universally Unique Identifier (UUID) used to uniquely identify the report
   * definition
   */
  public UUID getId()
  {
    return id;
  }

  /**
   * Returns the name of the report definition.
   *
   * @return the name of the report definition
   */
  public String getName()
  {
    return name;
  }

  /**
   * Returns a string representation of the object.
   *
   * @return a string representation of the object
   */
  @Override
  public String toString()
  {
    return "ReportDefinitionSummary {" + "id=\"" + getId() + "\", name=\"" + getName() + "\"}";
  }
}

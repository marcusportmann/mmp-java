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

package guru.mmp.application.security;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.List;

/**
 * The <code>FunctionTemplate</code> class stores the information for a function template that is
 * used to group a number of related <code>Function</code>s.
 *
 * @author Marcus Portmann
 */
public class FunctionTemplate
  implements java.io.Serializable
{
  private static final long serialVersionUID = 1000000;
  private String code;
  private String description;
  private List<Function> functions;
  private long id;
  private String name;

  /**
   * Constructs a new <code>FunctionTemplate</code>.
   *
   * @param code the code for the function template
   */
  public FunctionTemplate(String code)
  {
    this.code = code;
    this.functions = new ArrayList<>();
  }

  /**
   * Constructs a new <code>FunctionTemplate</code>.
   *
   * @param code        the code for the function template
   * @param name        the name of the function template
   * @param description the description for the function template
   */
  public FunctionTemplate(String code, String name, String description)
  {
    this.code = code;
    this.name = name;
    this.description = description;
    this.functions = new ArrayList<>();
  }

  /**
   * Returns the code for the function template.
   *
   * @return the code for the function template
   */
  public String getCode()
  {
    return code;
  }

  /**
   * Returns the description for the function template.
   *
   * @return the description for the function template
   */
  public String getDescription()
  {
    return description;
  }

  /**
   * Returns the functions associated with the function template.
   *
   * @return the functions associated with the function template
   */
  public List<Function> getFunctions()
  {
    return functions;
  }

  /**
   * Returns the unique numeric ID for the function template.
   *
   * @return the unique numeric ID for the function template
   */
  public long getId()
  {
    return id;
  }

  /**
   * Returns the name of the function template.
   *
   * @return the name of the function template
   */
  public String getName()
  {
    return name;
  }

  /**
   * Set the code for the function template.
   *
   * @param code the code for the function template
   */
  public void setCode(String code)
  {
    this.code = code;
  }

  /**
   * Set the description for the function template.
   *
   * @param description the description for the function template
   */
  public void setDescription(String description)
  {
    this.description = description;
  }

  /**
   * Set the functions associated with the function template.
   *
   * @param functions the functions associated with the function template
   */
  public void setFunctions(List<Function> functions)
  {
    this.functions = functions;
  }

  /**
   * Set the unique numeric ID for the function template.
   *
   * @param id the unique numeric ID for the function template
   */
  public void setId(long id)
  {
    this.id = id;
  }

  /**
   * Set the name of the function template.
   *
   * @param name the name of the function template
   */
  public void setName(String name)
  {
    this.name = name;
  }
}

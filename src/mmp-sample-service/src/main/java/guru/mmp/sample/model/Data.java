/*
 * Copyright 2017 Marcus Portmann
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

package guru.mmp.sample.model;

//~--- JDK imports ------------------------------------------------------------

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * The <code>Data</code> class.
 *
 * @author Marcus Portmann
 */
@Entity
@Table(schema = "SAMPLE", name = "DATA")
public class Data
  implements Serializable
{
  private static final long serialVersionUID = 1000000;

  /**
   * The ID used to uniquely identify the data.
   */
  @Id
  @Column(name = "ID", nullable = false)
  private long id;

  /**
   * The .
   */
  @Column(name = "NAME", nullable = false)
  private String name;

  /**
   * The value for the data.
   */
  @Column(name = "VALUE", nullable = false)
  private String value;

  /**
   * Constructs a new <code>Evaluation</code>.
   * </p>
   * Protected default constructor for JPA.
   */
  protected Data() {}

  /**
   * Constructs a new <code>Evaluation</code>.
   *
   * @param id    the ID used to uniquely identify the data
   * @param name  the name for the data
   * @param value the value for the data
   */
  public Data(long id, String name, String value)
  {
    this.id = id;
    this.name = name;
    this.value = value;
  }

  /**
   * Returns the ID used to uniquely identify the data.
   *
   * @return the ID used to uniquely identify the data
   */
  public long getId()
  {
    return id;
  }

  /**
   * Returns the name for the data.
   *
   * @return the name for the data
   */
  public String getName()
  {
    return name;
  }

  /**
   * Returns the value for the data.
   *
   * @return the value for the data
   */
  public String getValue()
  {
    return value;
  }

  /**
   * Set the ID used to uniquely identify the data.
   *
   * @param id the ID used to uniquely identify the data
   */
  public void setId(long id)
  {
    this.id = id;
  }

  /**
   * Set the name for the data.
   *
   * @param name the name for the data
   */
  public void setName(String name)
  {
    this.name = name;
  }

  /**
   * Set the value for the data.
   *
   * @param value the value for the data
   */
  public void setValue(String value)
  {
    this.value = value;
  }

  /**
   * Returns a string representation of the data.
   *
   * @return a string representation of the data
   */
  @Override
  public String toString()
  {
    return String.format("Data {id=\"%d\", name=\"%s\", value=\"%s\"}", getId(), getName(),
        getValue());
  }
}

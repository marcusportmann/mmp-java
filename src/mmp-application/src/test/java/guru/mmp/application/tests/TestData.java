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

package guru.mmp.application.tests;

/**
 * The <code>TestData</code> class.
 *
 * @author Marcus Portmann
 */
public class TestData
{
  private String id;
  private String name;
  private String value;

  /**
   * Constructs a new <code>TestData</code>.
   *
   * @param id    the ID
   * @param name  the name
   * @param value the value
   */
  TestData(String id, String name, String value)
  {
    this.id = id;
    this.name = name;
    this.value = value;
  }

  /**
   * Returns the ID.
   *
   * @return the ID
   */
  String getId()
  {
    return id;
  }

  /**
   * Returns the name.
   *
   * @return the name
   */
  String getName()
  {
    return name;
  }

  /**
   * Returns the value.
   *
   * @return the value
   */
  String getValue()
  {
    return value;
  }
}

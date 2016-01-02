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

package guru.mmp.common.model;

/**
 * The <code>BusinessObject</code> class is the common base class which all business objects must
 * extend.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public abstract class BusinessObject
  implements IBusinessObject
{
  /**
   * Constructs a new <code>BusinessObject</code>.
   */
  public BusinessObject() {}
}

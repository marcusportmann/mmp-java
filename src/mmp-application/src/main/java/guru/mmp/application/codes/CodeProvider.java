/*
 * Copyright 2014 Marcus Portmann
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

package guru.mmp.application.codes;

/**
 * The <code>CodeProvider</code> class provides the base class that all code providers should
 * be derived from.
 *
 * @author Marcus Portmann
 */
public abstract class CodeProvider
  implements ICodeProvider
{
  /**
   * The configuration information for this code provider.
   */
  private CodeProviderConfig codeProviderConfig;

  /**
   * The name of the code provider.
   */
  private String name;

  /**
   * Constructs a new <code>CodeProvider</code>.
   *
   * @param name               the name of the code provider
   * @param codeProviderConfig the configuration information for the code provider
   */
  public CodeProvider(String name, CodeProviderConfig codeProviderConfig)
  {
    this.name = name;
    this.codeProviderConfig = codeProviderConfig;
  }

  /**
   * Constructs a new <code>CodeProvider</code>.
   * <p/>
   * Hidden default constructor to support CDI.
   */
  protected CodeProvider() {}

  /**
   * Returns the configuration information for the code provider
   *
   * @return the configuration information for the code provider
   */
  public CodeProviderConfig getCodeProviderConfig()
  {
    return codeProviderConfig;
  }

  /**
   * Returns the name of the code provider.
   *
   * @return the name of the code provider
   */
  public String getName()
  {
    return name;
  }
}

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

package guru.mmp.common.corba;

import org.jacorb.config.Configuration;

/**
 * The <code>LoggingInitializer</code> class provides a "null" implementation of a JacORB logging
 * initialiser.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public class LoggingInitializer
  extends org.jacorb.config.LoggingInitializer
{
  /**
   * Initialise the JacORB logging.
   *
   * @param config the JacORB configuration used to initialise the logging
   */
  @Override
  public void init(Configuration config) {}
}

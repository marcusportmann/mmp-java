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

//~--- non-JDK imports --------------------------------------------------------

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The <code>UserDirectoryType</code> class stores the information for a user directory type.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public class UserDirectoryType
  implements java.io.Serializable
{
  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(UserDirectoryType.class);
  private static final long serialVersionUID = 1000000;
  private Class administrationClass;
  private String name;
  private Class userDirectoryClass;

  /**
   * Constructs a new <code>UserDirectoryType</code>.
   *
   * @param name                    the name of the user directory
   * @param userDirectoryClassName  the Java class that implements the user directory
   * @param administrationClassName the Java class that implements the Wicket component used to
   *                                administer the configuration for the user directory
   */
  public UserDirectoryType(String name, String userDirectoryClassName,
      String administrationClassName)
  {
    this.name = name;

    try
    {
      userDirectoryClass =
        Thread.currentThread().getContextClassLoader().loadClass(userDirectoryClassName);
    }
    catch (Throwable e)
    {
      logger.warn("Failed to load the user directory class (" + userDirectoryClassName
          + ") for the user directory (" + name + ")");
    }

    try
    {
      administrationClass =
        Thread.currentThread().getContextClassLoader().loadClass(administrationClassName);
    }
    catch (Throwable e)
    {
      logger.warn("Failed to load the administration class (" + administrationClassName
          + ") for the user directory (" + name + ")");
    }
  }

  /**
   * Returns the Java class that implements the Wicket component used to administer the
   * configuration for the user directory.
   *
   * @return the Java class that implements the Wicket component used to administer the
   *         configuration for the user directory
   */
  public Class getAdministrationClass()
  {
    return administrationClass;
  }

  /**
   * Returns the name of the user directory.
   *
   * @return the name of the user directory
   */
  public String getName()
  {
    return name;
  }

  /**
   * Returns the Java class that implements the user directory.
   *
   * @return the Java class that implements the user directory
   */
  public Class getUserDirectoryClass()
  {
    return userDirectoryClass;
  }
}

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

package guru.mmp.application.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

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

  private transient Class administrationClass;

  private String administrationClassName;

  private UUID id;

  private String name;

  private transient Class userDirectoryClass;

  private String userDirectoryClassName;

  /**
   * Constructs a new <code>UserDirectoryType</code>.
   *
   * @param id                      the Universally Unique Identifier (UUID) used to uniquely
   *                                identify the user directory type
   * @param name                    the name of the user directory type
   * @param userDirectoryClassName  the fully qualified name of the Java class that implements the
   *                                user directory type
   * @param administrationClassName the fully qualified name of the Java class that implements the
   *                                Wicket component used to administer the configuration for the
   *                                user directory type
   */
  public UserDirectoryType(
    UUID id, String name, String userDirectoryClassName, String administrationClassName)
  {
    this.id = id;
    this.name = name;
    this.userDirectoryClassName = userDirectoryClassName;
    this.administrationClassName = administrationClassName;
  }

  /**
   * Returns the Java class that implements the Wicket component used to administer the
   * configuration for the user directory type.
   *
   * @return the Java class that implements the Wicket component used to administer the
   * configuration for the user directory type
   *
   * @throws SecurityException
   */
  public Class<?> getAdministrationClass()
    throws SecurityException
  {
    if (administrationClass == null)
    {
      try
      {
        administrationClass = Thread.currentThread().getContextClassLoader().loadClass(
          administrationClassName);
      }
      catch (Throwable e)
      {
        throw new SecurityException(
          String.format("Failed to load the administration class (%s) for the user directory (%s)",
            administrationClassName, name), e);
      }
    }

    return administrationClass;
  }

  /**
   * Returns the fully qualified name of the Java class that implements the Wicket component used
   * to administer the configuration for the user directory type.
   *
   * @return the fully qualified name of the Java class that implements the Wicket component used
   * to administer the configuration for the user directory type
   */
  public String getAdministrationClassName()
  {
    return administrationClassName;
  }

  /**
   * Returns the Universally Unique Identifier (UUID) used to uniquely identify the user directory
   * type.
   *
   * @return the Universally Unique Identifier (UUID) used to uniquely identify the user directory
   * type
   */
  public UUID getId()
  {
    return id;
  }

  /**
   * Returns the name of the user directory type.
   *
   * @return the name of the user directory type
   */
  public String getName()
  {
    return name;
  }

  /**
   * Returns the Java class that implements the user directory type.
   *
   * @return the Java class that implements the user directory type
   *
   * @throws SecurityException
   */
  public Class getUserDirectoryClass()
    throws SecurityException
  {
    if (userDirectoryClass == null)
    {
      try
      {
        userDirectoryClass = Thread.currentThread().getContextClassLoader().loadClass(
          userDirectoryClassName);

        if (!IUserDirectory.class.isAssignableFrom(userDirectoryClass))
        {
          throw new SecurityException(
            "The user directory class does not implement the IUserDirectory interface");
        }
      }
      catch (Throwable e)
      {
        throw new SecurityException(
          String.format("Failed to load the user directory class (%s) for the user directory (%s)",
            userDirectoryClassName, name), e);
      }
    }

    return userDirectoryClass;
  }

  /**
   * Returns the fully qualified name of the Java class that implements the user directory type.
   *
   * @return the fully qualified name of the Java class that implements the user directory type
   */
  public String getUserDirectoryClassName()
  {
    return userDirectoryClassName;
  }
}

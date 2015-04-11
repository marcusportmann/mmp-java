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

package guru.mmp.common.tomcat;

/**
 * The <code>Version</code> class contains the version information for the mmp-tomcat library.
 *
 * @author Marcus Portmann
 */
public class Version
{
  /** the version */
  public static final String VERSION = "1.0.0";

  /** the incremental version number */
  public static final int VERSION_INCREMENTAL = 0;

  /** the major version number */
  public static final int VERSION_MAJOR = 1;

  /** the minor version number */
  public static final int VERSION_MINOR = 0;

  /**
   * Returns the version number.
   *
   * @return the version number
   */
  public static String getVersion()
  {
    return VERSION;
  }

  /**
   * Returns the incremental version number.
   *
   * @return the incremental version number
   */
  public static int getVersionIncremental()
  {
    return VERSION_INCREMENTAL;
  }

  /**
   * Returns the major version number.
   *
   * @return the major version number
   */
  public static int getVersionMajor()
  {
    return VERSION_MAJOR;
  }

  /**
   * Returns the minor version number.
   *
   * @return the minor version number
   */
  public static int getVersionMinor()
  {
    return VERSION_MINOR;
  }
}

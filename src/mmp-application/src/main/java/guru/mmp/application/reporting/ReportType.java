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

package guru.mmp.application.reporting;

/**
 * The <code>ReportType</code> enumeration defines the different types of reports e.g. a local
 * report under the WEB-INF/report folder for the web project or a report stored in the database.
 * <p/>
 * NOTE: This class is contained in the <code>mmp-application</code> project so that it can be
 * indirectly referenced by the <code>WebSession</code> class.
 *
 * @author Marcus Portmann
 */
public enum ReportType
{
  LOCAL(0, "Local Report"), DATABASE(1, "Database Report");

  private int code;
  private String name;

  ReportType(int code, String name)
  {
    this.code = code;
    this.name = name;
  }

  /**
   * Returns the report type given by the specified numeric code value.
   *
   * @param code the numeric code value identifying the report type
   *
   * @return the report type given by the specified numeric code value
   */
  public static ReportType fromCode(int code)
  {
    switch (code)
    {
      case 0:
        return ReportType.LOCAL;

      case 1:
        return ReportType.DATABASE;

      default:
        return ReportType.LOCAL;
    }
  }

  /**
   * Returns the numeric code value identifying the report type.
   *
   * @return the numeric code value identifying the report type
   */
  public int getCode()
  {
    return code;
  }

  /**
   * Returns the name of the report type.
   *
   * @return the name of the report type
   */
  public String getName()
  {
    return name;
  }

  /**
   * Return the string representation of the <code>ReportType</code> enumeration value.
   *
   * @return the string representation of the <code>ReportType</code> enumeration value
   */
  public String toString()
  {
    return name;
  }
}

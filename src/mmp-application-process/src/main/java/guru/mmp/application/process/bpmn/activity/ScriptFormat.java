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

package guru.mmp.application.process.bpmn.activity;

/**
 * The <code>ScriptFormat</code> enumeration defines the possible script formats for a script task.
 *
 * @author Marcus Portmann
 */
public enum ScriptFormat
{
  NONE("", "None"), JAVASCRIPT("text/javascript", "JavaScript"), GROOVY("text/x-groovy", "Groovy"),
  JAVA("http://www.java.com/java", "Java"), MVEL("http://www.mvel.org/2.0", "MVEL");

  private String mimeType;
  private String name;

  ScriptFormat(String mimeType, String name)
  {
    this.mimeType = mimeType;
    this.name = name;
  }

  /**
   * Returns the script format given by the specified mime type.
   *
   * @param mimeType the mime type identifying the script format
   *
   * @return the script format given by the specified mime type
   */
  public static ScriptFormat fromMimeType(String mimeType)
  {
    if (mimeType == null)
    {
      throw new RuntimeException("Invalid mime type for script format (" + mimeType + ")");
    }

    mimeType = mimeType.toLowerCase().trim();

    switch (mimeType)
    {
      case "":
        return ScriptFormat.NONE;

      case "text/javascript":
        return ScriptFormat.JAVASCRIPT;

      case "text/x-groovy":
        return ScriptFormat.GROOVY;

      case "http://www.java.com/java":
        return ScriptFormat.JAVA;

      case "http://www.mvel.org/2.0":
        return ScriptFormat.MVEL;

      default:

        throw new RuntimeException("Invalid mime type for script format (" + mimeType + ")");
    }
  }

  /**
   * Returns the mime type identifying the script format.
   *
   * @return the mime type identifying the script format
   */
  public String getMimeType()
  {
    return mimeType;
  }

  /**
   * Returns the name of the script format.
   *
   * @return the name of the script format
   */
  public String getName()
  {
    return name;
  }

  /**
   * Return the string representation of the script format enumeration value.
   *
   * @return the string representation of the script format enumeration value
   */
  public String toString()
  {
    return mimeType;
  }
}

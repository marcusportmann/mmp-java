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

package guru.mmp.common.test;

//~--- JDK imports ------------------------------------------------------------

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * The <code>DatabaseTest</code> class provides a base class that contains functionality common to
 * all JUnit database test classes.
 *
 * @author Marcus Portmann
 */
public abstract class DatabaseTest extends Test
{
  protected String cleanSQL(String text)
  {
    if (text == null)
    {
      throw new NullPointerException("Failed to clean the null SQL string");
    }

    // Strip whitespace from the beginning and end of the text
    text = text.trim();

    // If this is an empty string then stop here
    if (text.length() == 0)
    {
      return text;
    }

    // First remove the new line characters
    int index = text.length() - 1;

    while ((index >= 0) && ((text.charAt(index) == '\r') || (text.charAt(index) == '\n')))
    {
      index--;
    }

    if (index < 0)
    {
      return "";
    }

    text = text.substring(0, index + 1);

    // Replace multiple spaces with a single space
    while (text.contains("  "))
    {
      text = text.replaceAll("  ", " ");
    }

    // Replace tabs with a single space
    text = text.replaceAll("\t", " ");

    return text;
  }

  protected List<String> loadSQL(String resourcePath)
    throws IOException
  {
    List<String> sqlStatements = new ArrayList<>();
    BufferedReader reader = null;

    try
    {
      reader = new BufferedReader(
          new InputStreamReader(
            Thread.currentThread().getContextClassLoader().getResourceAsStream(resourcePath)));

      StringBuilder multiLineBuffer = null;
      String line;

      while ((line = reader.readLine()) != null)
      {
        /*
         * Remove any whitespace at the beginning or end of the line, any newline characters and
         * any multiple spaces.
         */
        line = cleanSQL(line);

        if (line.length() == 0)
        {
          continue;
        }

        // Only process the line if it is not a SQL comment
        if (!line.startsWith("--"))
        {
          /*
           * If we have already built up part of the multi-line SQL statement then add spacing
           * between this and the next part of the SQL statement on the current line.
           */
          if (multiLineBuffer != null)
          {
            multiLineBuffer.append(" ");
          }

          /*
           * If the line contains a ';' then one of the following is true:
           * - The line contains one or more single-line SQL statements
           * - The line contains the end of a multi-line SQL statement
           * - The line contains both of the above
           */
          if (line.contains(";"))
          {
            StringTokenizer tokens = new StringTokenizer(line, ";");

            while (tokens.hasMoreTokens())
            {
              String token = tokens.nextToken().trim();

              // If we are currently processing a multi-line buffer
              if (multiLineBuffer != null)
              {
                multiLineBuffer.append(token);
                sqlStatements.add(multiLineBuffer.toString());
                multiLineBuffer = null;
              }
              else
              {
                if (tokens.hasMoreTokens())
                {
                  sqlStatements.add(token);
                }
                else
                {
                  if (line.endsWith(";"))
                  {
                    sqlStatements.add(token);
                  }
                  else
                  {
                    multiLineBuffer = new StringBuilder();
                    multiLineBuffer.append(token);
                  }
                }
              }
            }
          }

          /*
           * The line does not contain the end of a SQL statement which means it is either
           * the start of a new multi-line SQL statement or the continuation of an existing
           * multi-line SQL statement.
           */
          else
          {
            /*
             * If this is a new multi-line SQL statement then initialise the buffer
             * that will be used to concatenate the individual lines of the statement into
             * a single-line SQL statement.
             */
            if (multiLineBuffer == null)
            {
              multiLineBuffer = new StringBuilder();
            }

            multiLineBuffer.append(line);
          }
        }
      }

      if (multiLineBuffer != null)
      {
        getLogger().warn("Failed to process the last SQL statement from the file (" + resourcePath
            + ") since it was not terminated by a ';'");
      }

      return sqlStatements;
    }
    finally
    {
      try
      {
        if (reader != null)
        {
          reader.close();
        }
      }
      catch (Throwable ignored) {}
    }
  }
}

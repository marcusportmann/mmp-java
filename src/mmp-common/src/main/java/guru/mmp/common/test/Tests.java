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

package guru.mmp.common.test;

//~--- non-JDK imports --------------------------------------------------------

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>Tests</code> class provides a base class that contains functionality common to all
 * JUnit test classes.
 *
 * @author Marcus Portmann
 */
public abstract class Tests
{
  private Logger logger;

  protected byte[] getClasspathResource(String path)
  {
    InputStream is = null;

    try
    {
      is = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);

      ByteArrayOutputStream baos = new ByteArrayOutputStream();

      byte[] buffer = new byte[4096];
      int numberOfBytesRead;

      while ((numberOfBytesRead = is.read(buffer)) != -1)
      {
        baos.write(buffer, 0, numberOfBytesRead);
      }

      return baos.toByteArray();
    }
    catch (Throwable e)
    {
      throw new RuntimeException("Failed to read the classpath resource (" + path + ")", e);
    }
    finally
    {
      try
      {
        if (is != null)
        {
          is.close();
        }
      }
      catch (Throwable e)
      {
        logger.error("Failed to close the input stream for the classpath resource (" + path + ")",
            e);
      }
    }
  }

  protected Logger getLogger()
  {
    if (logger == null)
    {
      logger = LoggerFactory.getLogger(getClass());
    }

    return logger;
  }
}

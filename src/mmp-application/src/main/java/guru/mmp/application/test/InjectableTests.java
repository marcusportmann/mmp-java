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

package guru.mmp.application.test;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.cdi.CDIUtil;
import guru.mmp.common.test.Tests;

import org.slf4j.Logger;

//~--- JDK imports ------------------------------------------------------------

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * The <code>InjectableTests</code> class provides a base class for JUnit tests that wish to use
 * JEE 6 Contexts and Dependency Injection (CDI).
 *
 * @author Marcus Portmann
 */
public abstract class InjectableTests extends Tests
{
  /**
   * Constructs a new <code>InjectableTests</code>.
   */
  public InjectableTests()
  {
    CDIUtil.inject(this);
  }

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
        getLogger().error("Failed to close the input stream for the classpath resource (" + path
            + ")", e);
      }
    }
  }

  /**
   * Returns the logger for the derived JUnit tests class.
   *
   * @return the logger for the derived JUnit tests class
   */
  protected abstract Logger getLogger();
}

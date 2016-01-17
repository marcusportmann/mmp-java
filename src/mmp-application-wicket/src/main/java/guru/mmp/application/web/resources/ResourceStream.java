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

package guru.mmp.application.web.resources;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.web.WebApplicationException;
import org.apache.wicket.util.resource.IResourceStream;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Locale;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>ResourceStream</code> class provides a base class for all resource steams.
 *
 * @author Marcus Portmann
 */
public abstract class ResourceStream
  implements IResourceStream
{
  private static final long serialVersionUID = 1000000;
  private Locale locale;
  private String style;
  private String variation;

  /**
   * Constructs a new <code>ResourceStream</code>.
   */
  public ResourceStream() {}

  /**
   * Returns the locale for the resource stream.
   *
   * @return the locale for the resource stream
   */
  public Locale getLocale()
  {
    return locale;
  }

  /**
   * Returns the style for the resource stream.
   *
   * @return the style for the resource stream
   */
  public String getStyle()
  {
    return style;
  }

  /**
   * Returns the variation for the resource stream.
   *
   * @return the variation for the resource stream
   */
  public String getVariation()
  {
    return variation;
  }

  /**
   * Set the locale for the resource stream.
   *
   * @param locale the locale for the resource stream
   */
  public void setLocale(Locale locale)
  {
    this.locale = locale;
  }

  /**
   * Set the style for the resource stream.
   *
   * @param style the style for the resource stream
   */
  public void setStyle(String style)
  {
    this.style = style;
  }

  /**
   * Set the variation for the resource stream.
   *
   * @param variation the variation for the resource stream
   */
  public void setVariation(String variation)
  {
    this.variation = variation;
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
      throw new WebApplicationException(String.format("Failed to read the classpath resource (%s)",
          path), e);
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
        // Do nothing
      }
    }
  }
}

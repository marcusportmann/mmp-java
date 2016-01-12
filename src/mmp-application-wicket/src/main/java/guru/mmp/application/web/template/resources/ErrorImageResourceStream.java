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

package guru.mmp.application.web.template.resources;

import guru.mmp.application.web.resources.ResourceStream;
import org.apache.wicket.util.lang.Bytes;
import org.apache.wicket.util.resource.ResourceStreamNotFoundException;
import org.apache.wicket.util.time.Time;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * The <code>ErrorImageResourceStream</code> class provides an <code>IResourceStream</code>
 * implementation that allows the retrieval of an error image as a streamed resource.
 *
 * @author Marcus Portmann
 */
public class ErrorImageResourceStream
  extends ResourceStream
{
  private static final long serialVersionUID = 1000000;

  private static byte[] errorImageData;

  private InputStream errorImageInputStream;

  /**
   * Constructs a new <code>ErrorImageResourceStream</code>.
   */
  public ErrorImageResourceStream() {}

  /**
   * Close the resource stream.
   *
   * @throws IOException
   */
  @Override
  public void close()
    throws IOException
  {
    if (errorImageInputStream != null)
    {
      try
      {
        errorImageInputStream.close();
      }
      catch (Throwable ignored)
      {
      }

      errorImageInputStream = null;
    }
  }

  /**
   * Returns the content type for the resource stream.
   *
   * @return the content type for the resource stream
   */
  @Override
  public String getContentType()
  {
    return "image/png";
  }

  /**
   * Returns the <code>InputStream</code> for the resource stream.
   *
   * @return the <code>InputStream</code> for the resource stream
   *
   * @throws ResourceStreamNotFoundException
   */
  @Override
  public InputStream getInputStream()
    throws ResourceStreamNotFoundException
  {
    errorImageInputStream = new ByteArrayInputStream(getErrorImageData());

    return errorImageInputStream;
  }

  /**
   * Returns the last modified time for the resource stream.
   *
   * @return the last modified time for the resource stream
   */
  @Override
  public Time lastModifiedTime()
  {
    return Time.millis(0);
  }

  /**
   * Returns the length of the resource stream.
   *
   * @return the length of the resource stream
   */
  @Override
  public Bytes length()
  {
    return Bytes.bytes(getErrorImageData().length);
  }

  private synchronized byte[] getErrorImageData()
  {
    if (errorImageData == null)
    {
      errorImageData = getClasspathResource(
        "guru/mmp/application/web/template/resource/theme/mmp/error.png");
    }

    return errorImageData;
  }
}

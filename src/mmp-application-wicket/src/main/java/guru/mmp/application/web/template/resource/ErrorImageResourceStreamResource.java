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

package guru.mmp.application.web.template.resource;

//~--- non-JDK imports --------------------------------------------------------

import org.apache.wicket.request.resource.ResourceStreamResource;
import org.apache.wicket.request.resource.caching.IResourceCachingStrategy;
import org.apache.wicket.request.resource.caching.NoOpResourceCachingStrategy;

/**
 * The <code>ErrorImageResourceStreamResource</code> class provides a
 * <code>ResourceStreamResource</code> implementation that allows the retrieval of an error image
 * a streamed resource.
 *
 * @author Marcus Portmann
 */
public class ErrorImageResourceStreamResource extends ResourceStreamResource
{
  private static final long serialVersionUID = 1000000;

  /**
   * Constructs a new <code>ErrorImageResourceStreamResource</code>.
   */
  public ErrorImageResourceStreamResource()
  {
    super(new ErrorImageResourceStream());
  }

  /**
   * Returns the caching strategy for the resource stream resource.
   *
   * @return the caching strategy for the resource stream resource
   */
  @Override
  protected IResourceCachingStrategy getCachingStrategy()
  {
    return NoOpResourceCachingStrategy.INSTANCE;
  }

//@Override
//protected void setResponseHeaders(ResourceResponse data, Attributes attributes)
//{
//super.setResponseHeaders(data, attributes);
//
//data.setContentDisposition(ContentDisposition.ATTACHMENT);
//}
}

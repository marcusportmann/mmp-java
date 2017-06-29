/*
 * Copyright 2017 Marcus Portmann
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

package guru.mmp.sample.ws;

//~--- JDK imports ------------------------------------------------------------

import javax.inject.Inject;

/**
 * The <code>SampleServiceEndpoint</code> class implements the Sample Service.
 *
 * @author Marcus Portmann
 */
public class SampleServiceEndpoint
  implements guru.mmp.sample.ws.ISampleService
{
  @Inject
  private guru.mmp.sample.model.ISampleService sampleService;

  @Override
  public String getVersion()
    throws ServiceUnavailableFault
  {
    try
    {
      if (true)
        throw new RuntimeException("Testing 1.. 2.. 3..");

      return sampleService.getVersion();
    }
    catch (Throwable e)
    {
      throw new ServiceUnavailableFault("Failed to retrieve the version",
          SampleServiceTranslator.translate(e), e);
    }
  }
}

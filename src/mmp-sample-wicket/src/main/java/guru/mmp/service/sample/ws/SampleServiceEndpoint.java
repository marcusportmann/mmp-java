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

package guru.mmp.service.sample.ws;

/**
 * The <code>SampleServiceEndpoint</code> class implements the Sample Service.
 */
public class SampleServiceEndpoint
  implements ISampleService
{
  @Override
  public String getVersion()
    throws SampleServiceFault
  {
    return "1.0.0";
  }
}

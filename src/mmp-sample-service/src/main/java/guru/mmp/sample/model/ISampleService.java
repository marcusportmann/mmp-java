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

package guru.mmp.sample.model;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;

/**
 * The <code>ISampleService</code> interface defines the functionality that must be provided by
 * a Sample Service implementation.
 *
 * @author Marcus Portmann
 */
public interface ISampleService
{
  /**
   * Add the data.
   *
   * @throws SampleServiceException
   */
  void addData()
    throws SampleServiceException;

  /**
   * Returns the data.
   *
   * @return the data
   *
   * @throws SampleServiceException
   */
  List<Data> getData()
    throws SampleServiceException;

  /**
   * Returns the version of the service.
   *
   * @return the version of the service
   */
  String getVersion();

  /**
   * The test method.
   */
  void testMethod();
}

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

package guru.mmp.sample.api;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.sample.model.Data;
import guru.mmp.sample.model.ISampleService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.LocalDateTime;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>HomeController</code> class.
 *
 * @author Marcus Portmann
 */
@RestController
@RequestMapping(value = "/api")
public class HomeController
{
  @Inject
  private ISampleService sampleService;

  /**
   * The data RESTful web service.
   *
   * @return the data
   */
  @RequestMapping(value = "/data", method = RequestMethod.GET, produces = "application/json")
  public Data data()
  {
    Data data = new Data(666, "Test Name", 777, "Test Value", LocalDate.now(), LocalDateTime.now());

    return data;
  }

  /**
   * The version RESTful web service.
   *
   * @return the version
   */
  @RequestMapping(value = "/version", method = RequestMethod.GET, produces = "application/json")
  public String version()
  {
    return sampleService.getVersion();
  }
}

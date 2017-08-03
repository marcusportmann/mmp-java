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

import guru.mmp.application.model.ValidationError;
import guru.mmp.sample.model.Data;
import guru.mmp.sample.model.ISampleService;
import guru.mmp.sample.model.SampleServiceException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>SampleServiceController</code> class.
 *
 * @author Marcus Portmann
 */
@RestController
@RequestMapping(value = "/api")
@WebService(serviceName = "SampleService", name = "ISampleService",
    targetNamespace = "http://sample.mmp.guru")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL,
    parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public class SampleServiceController
{
  @Inject
  private ISampleService sampleService;

  /**
   * Returns all the data.
   *
   * @return all the data
   */
  @RequestMapping(value = "/allData", method = RequestMethod.GET, produces = "application/json")
  @WebMethod(operationName = "GetAllData")
  @WebResult(name = "Data")
  public List<Data> allData()
    throws SampleServiceException
  {
    return sampleService.getAllData();
  }

  /**
   * The data RESTful web service.
   *
   * @return the data
   */
  @RequestMapping(value = "/data", method = RequestMethod.GET, produces = "application/json")
  @WebMethod(operationName = "GetData")
  @WebResult(name = "Data")
  public Data data()
    throws SampleServiceException
  {
    long id = System.currentTimeMillis();

    Data data = new Data(id, "Test Name " + id, 777, "Test Value " + id, LocalDate.now(),
        LocalDateTime.now());

    sampleService.addData(data);

    data = sampleService.getData(data.getId());

    return data;
  }

  /**
   * Test the exception handling.
   *
   * @throws SampleServiceException
   */
  @RequestMapping(value = "/testExceptionHandling", method = RequestMethod.GET)
  @WebMethod(operationName = "TestExceptionHandling")
  public void testExceptionHandling()
    throws SampleServiceException
  {
    throw new SampleServiceException("Testing 1.. 2.. 3..");
  }

  /**
   * Validate the data.
   */
  @ApiOperation(value = "Validate the data", notes = "Validate the data")
  @ApiResponses(value = { @ApiResponse(code = 200, message = "OK", response = ValidationError.class,
      responseContainer = "List") })
  @RequestMapping(value = "/validate", method = RequestMethod.POST, produces = "application/json")
  @WebMethod(operationName = "Validate")
  @WebResult(name = "ValidationError")
  public List<ValidationError> validate(@ApiParam(name = "data", value = "The data",
      required = true)
  @RequestBody
  @WebParam(name = "data") Data data)
    throws SampleServiceException
  {
    return sampleService.validate(data);
  }

  /**
   * The version RESTful web service.
   *
   * @return the version
   */
  @RequestMapping(value = "/version", method = RequestMethod.GET)
  @WebMethod(operationName = "GetVersion")
  @WebResult(name = "Version")
  public String version()
  {
    return sampleService.getVersion();
  }
}

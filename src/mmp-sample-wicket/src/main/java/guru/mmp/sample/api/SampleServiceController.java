/*
 * Copyright 2017 Discovery Bank
 * All rights reserved.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package guru.mmp.sample.api;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.sample.model.Data;
import guru.mmp.sample.model.ISampleService;
import guru.mmp.sample.model.SampleServiceException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.jws.WebMethod;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import java.time.LocalDate;
import java.time.LocalDateTime;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>SampleServiceController</code> class.
 *
 * @author Marcus Portmann
 */
@RestController
@RequestMapping(value = "/api")
@WebService(serviceName = "SampleService", name = "ISampleService",
    targetNamespace = "http://sample.service.mmp.guru")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL,
    parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public class SampleServiceController
{
  @Inject
  private ISampleService sampleService;

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

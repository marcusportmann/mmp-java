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
    try
    {
      long id = System.currentTimeMillis();

      Data data = new Data(id, "Test Name " + id, 777, "Test Value " + id, LocalDate.now(),
          LocalDateTime.now());

      sampleService.addData(data);

      data = sampleService.getData(data.getId());

      return data;
    }
    catch (Throwable e)
    {
      throw new RuntimeException("Failed to retrieve the data", e);
    }
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

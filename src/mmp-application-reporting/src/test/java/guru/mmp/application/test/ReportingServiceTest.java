/*
 * Copyright 2015 Marcus Portmann
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

package guru.mmp.application.test;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.reporting.IReportingService;
import guru.mmp.application.reporting.ReportDefinition;
import guru.mmp.common.test.ApplicationJUnit4ClassRunner;
import guru.mmp.common.util.ResourceUtil;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

//~--- JDK imports ------------------------------------------------------------

import java.util.*;

import javax.inject.Inject;

/**
 * The <code>CodesServiceTest</code> class contains the implementation of the JUnit
 * tests for the <code>CodesService</code> class.
 *
 * @author Marcus Portmann
 */
@RunWith(ApplicationJUnit4ClassRunner.class)
public class ReportingServiceTest
{
  private static int reportDefinitionCount;
  @Inject
  private IReportingService reportingService;

  /**
   * Test the report definition functionality
   *
   * @throws Exception
   */
  @Test
  public void reportDefinitionTest()
    throws Exception
  {
    ReportDefinition reportDefinition = getTestReportDefinitionDetails();
  }

  private static synchronized ReportDefinition getTestReportDefinitionDetails()
    throws Exception
  {
    reportDefinitionCount++;

    byte[] testReportTemplate =
      ResourceUtil.getClasspathResource("guru/mmp/application/test/TestReport.jasper");

    ReportDefinition reportDefinition = new ReportDefinition();
    reportDefinition.setId(UUID.randomUUID());
    reportDefinition.setName("Test Report Definition " + reportDefinitionCount);
    reportDefinition.setTemplate(testReportTemplate);

    return reportDefinition;
  }
}

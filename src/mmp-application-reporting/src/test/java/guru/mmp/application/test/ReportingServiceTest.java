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

package guru.mmp.application.test;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.reporting.IReportingService;
import guru.mmp.application.reporting.ReportDefinition;
import guru.mmp.application.reporting.ReportDefinitionSummary;
import guru.mmp.common.util.ResourceUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import javax.inject.Inject;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.Connection;
import java.util.*;

import static org.junit.Assert.assertEquals;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>ReportingServiceTest</code> class contains the implementation of the JUnit
 * tests for the <code>ReportingService</code> class.
 *
 * @author Marcus Portmann
 */
@RunWith(ApplicationClassRunner.class)
@ContextConfiguration(classes = { ApplicationConfiguration.class })
@TestExecutionListeners(listeners = { DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class, TransactionalTestExecutionListener.class })
public class ReportingServiceTest
{
  private static int reportDefinitionCount;
  @Autowired
  private IReportingService reportingService;

  /**
   * The data source used to provide connections to the application database.
   */
  @Autowired
  @Qualifier("applicationDataSource")
  private DataSource dataSource;

  /**
   * Test the create report PDF functionality.
   *
   * @throws Exception
   */
  @Test
  public void createReportPDFTest()
    throws Exception
  {
    ReportDefinition reportDefinition = getTestReportDefinitionDetails();

    reportingService.saveReportDefinition(reportDefinition);

    Map<String, Object> parameters = new HashMap<>();

    try (Connection connection = dataSource.getConnection())
    {
      reportingService.createReportPDF(reportDefinition.getId(), parameters, connection);
    }

    reportingService.deleteReportDefinition(reportDefinition.getId());
  }

  /**
   * Test the report definition functionality.
   *
   * @throws Exception
   */
  @Test
  public void reportDefinitionTest()
    throws Exception
  {
    ReportDefinition reportDefinition = getTestReportDefinitionDetails();

    reportingService.saveReportDefinition(reportDefinition);

    ReportDefinition retrievedReportDefinition = reportingService.getReportDefinition(
        reportDefinition.getId());

    compareReportDefinitions(reportDefinition, retrievedReportDefinition);

    boolean reportDefinitionExists = reportingService.reportDefinitionExists(
        reportDefinition.getId());

    assertEquals("The report definition that was saved does not exist", true,
        reportDefinitionExists);

    reportDefinition.setName("Updated " + reportDefinition.getName());

    reportingService.saveReportDefinition(reportDefinition);

    retrievedReportDefinition = reportingService.getReportDefinition(reportDefinition.getId());

    compareReportDefinitions(reportDefinition, retrievedReportDefinition);

    reportDefinitionExists = reportingService.reportDefinitionExists(reportDefinition.getId());

    assertEquals("The updated report definition that was saved does not exist", true,
        reportDefinitionExists);

    int numberOfReportDefinitions = reportingService.getNumberOfReportDefinitions();

    assertEquals("The correct number of report definitions (1) was not retrieved", 1,
        numberOfReportDefinitions);

    List<ReportDefinition> reportDefinitions = reportingService.getReportDefinitions();

    assertEquals("The correct number of report definitions (1) was not retrieved", 1,
        reportDefinitions.size());

    compareReportDefinitions(reportDefinition, reportDefinitions.get(0));

    ReportDefinitionSummary retrievedReportDefinitionSummary =
        reportingService.getReportDefinitionSummary(reportDefinition.getId());

    compareReportDefinitionToReportDefinitionSummary(reportDefinition,
        retrievedReportDefinitionSummary);

    List<ReportDefinitionSummary> reportDefinitionSummaries =
        reportingService.getReportDefinitionSummaries();

    assertEquals("The correct number of report definition summaries (1) was not retrieved", 1,
        reportDefinitionSummaries.size());

    compareReportDefinitionToReportDefinitionSummary(reportDefinition,
        reportDefinitionSummaries.get(0));

    reportingService.deleteReportDefinition(reportDefinition.getId());

    retrievedReportDefinition = reportingService.getReportDefinition(reportDefinition.getId());

    assertEquals("The report definition that should have been deleted was retrieved successfully",
        null, retrievedReportDefinition);
  }

  private static synchronized ReportDefinition getTestReportDefinitionDetails()
    throws Exception
  {
    reportDefinitionCount++;

    byte[] testReportTemplate = ResourceUtil.getClasspathResource(
        "guru/mmp/application/test/TestReport.jasper");

    ReportDefinition reportDefinition = new ReportDefinition();
    reportDefinition.setId(UUID.randomUUID());
    reportDefinition.setName("Test Report Definition " + reportDefinitionCount);
    reportDefinition.setTemplate(testReportTemplate);

    return reportDefinition;
  }

  private void compareReportDefinitionToReportDefinitionSummary(ReportDefinition reportDefinition,
      ReportDefinitionSummary reportDefinitionSummary)
  {
    assertEquals("The ID values for the two report definitions do not match",
        reportDefinition.getId(), reportDefinitionSummary.getId());
    assertEquals("The name values for the two report definitions do not match",
        reportDefinition.getName(), reportDefinitionSummary.getName());
  }

  private void compareReportDefinitions(ReportDefinition reportDefinition1,
      ReportDefinition reportDefinition2)
  {
    assertEquals("The ID values for the two report definitions do not match",
        reportDefinition1.getId(), reportDefinition2.getId());
    assertEquals("The name values for the two report definitions do not match",
        reportDefinition1.getName(), reportDefinition2.getName());
    assertEquals("The template values for the two report definitions do not match", true,
        Arrays.equals(reportDefinition1.getTemplate(), reportDefinition2.getTemplate()));
  }
}

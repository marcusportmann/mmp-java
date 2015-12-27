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

package guru.mmp.application.web.template.data;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.reporting.IReportingService;
import guru.mmp.application.reporting.ReportDefinitionSummary;
import guru.mmp.application.web.WebApplicationException;
import guru.mmp.application.web.data.InjectableDataProvider;

import org.apache.wicket.model.IModel;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

/**
 * The <code>ReportDefinitionSummaryDataProvider</code> class provides an <code>IDataProvider</code>
 * implementation that retrieves <code>ReportDefinitionSummary</code> instances from the database.
 *
 * @author Marcus Portmann
 */
public class ReportDefinitionSummaryDataProvider
  extends InjectableDataProvider<ReportDefinitionSummary>
{
  private static final long serialVersionUID = 1000000;

  /* Reporting Service */
  @Inject
  private IReportingService reportingService;

  /**
   * Constructs a new <code>ReportDefinitionSummaryDataProvider</code>.
   */
  public ReportDefinitionSummaryDataProvider() {}

  /**
   * @see org.apache.wicket.model.IDetachable#detach()
   */
  public void detach() {}

  /**
   * Retrieves the summaries for the matching report definitions from the database starting with
   * index <code>first</code> and ending with <code>first+count</code>.
   *
   * @see org.apache.wicket.markup.repeater.data.IDataProvider#iterator(long, long)
   *
   * @param first the index of the first entry to return
   * @param count the number of the entries to return
   *
   * @return the summaries for the report definitions from the database starting with
   *         index <code>first</code> and ending with <code>first+count</code>
   */
  public Iterator<ReportDefinitionSummary> iterator(long first, long count)
  {
    try
    {
      List<ReportDefinitionSummary> allReportDefinitionSummaries =
        reportingService.getReportDefinitionSummaries();

      List<ReportDefinitionSummary> reportDefinitionSummaries = new ArrayList<>();

      long end = first + count;

      for (long i = first; ((i < end) && (i < allReportDefinitionSummaries.size())); i++)
      {
        reportDefinitionSummaries.add(allReportDefinitionSummaries.get((int) i));
      }

      return reportDefinitionSummaries.iterator();
    }
    catch (Throwable e)
    {
      throw new WebApplicationException(
          "Failed to load the summaries for the report definitions from index (" + first + ") to ("
          + (first + count) + ")", e);
    }
  }

  /**
   * Wraps the retrieved <code>ReportDefinitionSummary</code> POJO with a Wicket model.
   *
   * @see org.apache.wicket.markup.repeater.data.IDataProvider#model(java.lang.Object)
   *
   * @param reportDefinitionSummary the <code>ReportDefinitionSummary</code> instance to wrap
   *
   * @return the Wicket model wrapping the <code>ReportDefinitionSummary</code> instance
   */
  public IModel<ReportDefinitionSummary> model(ReportDefinitionSummary reportDefinitionSummary)
  {
    return new DetachableReportDefinitionSummaryModel(reportDefinitionSummary);
  }

  /**
   * Returns the total number of report definitions.
   *
   * @see org.apache.wicket.markup.repeater.data.IDataProvider#size()
   *
   * @return the total number of report definitions
   */
  public long size()
  {
    try
    {
      return reportingService.getNumberOfReportDefinitions();
    }
    catch (Throwable e)
    {
      throw new WebApplicationException("Failed to retrieve the number of report definitions", e);
    }
  }
}

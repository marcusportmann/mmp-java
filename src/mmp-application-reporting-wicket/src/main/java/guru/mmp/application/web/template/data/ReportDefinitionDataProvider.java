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

package guru.mmp.application.web.template.data;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.reporting.IReportingService;
import guru.mmp.application.reporting.ReportDefinition;
import guru.mmp.application.web.WebApplicationException;
import guru.mmp.application.web.data.InjectableDataProvider;
import org.apache.wicket.model.IModel;
import org.springframework.beans.factory.annotation.Autowired;

import javax.inject.Inject;
import java.util.Iterator;
import java.util.List;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>ReportDefinitionDataProvider</code> class provides an <code>IDataProvider</code>
 * implementation that retrieves <code>ReportDefinition</code> instances from the database.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public class ReportDefinitionDataProvider extends InjectableDataProvider<ReportDefinition>
{
  private static final long serialVersionUID = 1000000;

  /* Reporting Service */
  @Inject
  private IReportingService reportingService;

  /**
   * Constructs a new <code>ReportDefinitionDataProvider</code>.
   */
  public ReportDefinitionDataProvider() {}

  /**
   * @see org.apache.wicket.model.IDetachable#detach()
   */
  public void detach() {}

  /**
   * Retrieves the matching report definitions from the database starting with
   * index <code>first</code> and ending with <code>first+count</code>.
   *
   * @param first the index of the first entry to return
   * @param count the number of the entries to return
   *
   * @return the report definitions retrieved from the database starting with
   * index <code>first</code> and ending with <code>first+count</code>
   *
   * @see org.apache.wicket.markup.repeater.data.IDataProvider#iterator(long, long)
   */
  public Iterator<ReportDefinition> iterator(long first, long count)
  {
    try
    {
      List<ReportDefinition> allReportDefinitions = reportingService.getReportDefinitions();

      return allReportDefinitions.subList((int) first, (int) Math.min(first + count,
          allReportDefinitions.size())).iterator();
    }
    catch (Throwable e)
    {
      throw new WebApplicationException(String.format(
          "Failed to load the report definitions from index (%d) to (%d)", first, first + count
          - 1), e);
    }
  }

  /**
   * Wraps the retrieved <code>ReportDefinition</code> POJO with a Wicket model.
   *
   * @param reportDefinition the <code>ReportDefinition</code> instance to wrap
   *
   * @return the Wicket model wrapping the <code>ReportDefinition</code> instance
   *
   * @see org.apache.wicket.markup.repeater.data.IDataProvider#model(java.lang.Object)
   */
  public IModel<ReportDefinition> model(ReportDefinition reportDefinition)
  {
    return new DetachableReportDefinitionModel(reportDefinition);
  }

  /**
   * Returns the total number of report definitions.
   *
   * @return the total number of report definitions
   *
   * @see org.apache.wicket.markup.repeater.data.IDataProvider#size()
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

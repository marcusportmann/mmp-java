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
import guru.mmp.application.reporting.ReportDefinition;
import guru.mmp.application.web.WebApplicationException;
import guru.mmp.application.web.data.InjectableDataProvider;

import org.apache.wicket.model.IModel;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

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

  /**
   * The organisation code identifying the organisation the report definitions are associated with.
   */
  private String organisation;

  /* Reporting Service */
  @Inject
  private IReportingService reportingService;

  /**
   * Constructs a new <code>ReportDefinitionDataProvider</code>.
   *
   * @param organisation the organisation code identifying the organisation the report definitions
   *                     are associated with
   */
  public ReportDefinitionDataProvider(String organisation)
  {
    this.organisation = organisation;
  }

  /**
   * Constructs a new <code>ReportDefinitionDataProvider</code>.
   * <p/>
   * Hidden default constructor to support CDI.
   */
  protected ReportDefinitionDataProvider() {}

  /**
   * @see org.apache.wicket.model.IDetachable#detach()
   */
  public void detach() {}

  /**
   * Retrieves the matching report definitions from the database starting with
   * index <code>first</code> and ending with <code>first+count</code>.
   *
   * @see org.apache.wicket.markup.repeater.data.IDataProvider#iterator(long, long)
   *
   * @param first the index of the first entry to return
   * @param count the number of the entries to return
   *
   * @return the report definitions from the database starting with index <code>first</code> and
   *         ending with <code>first+count</code>
   */
  public Iterator<ReportDefinition> iterator(long first, long count)
  {
    try
    {
      List<ReportDefinition> allReportDefinitions =
        reportingService.getReportDefinitionsForOrganisation(organisation);

      List<ReportDefinition> reportDefinitions = new ArrayList<>();

      long end = first + count;

      for (long i = first; ((i < end) && (i < allReportDefinitions.size())); i++)
      {
        reportDefinitions.add(allReportDefinitions.get((int) i));
      }

      return reportDefinitions.iterator();
    }
    catch (Throwable e)
    {
      throw new WebApplicationException("Failed to load the report definitions from index ("
          + first + ") to (" + (first + count) + ")", e);
    }
  }

  /**
   * Wraps the retrieved <code>ReportDefinition</code> POJO with a Wicket model.
   *
   * @see org.apache.wicket.markup.repeater.data.IDataProvider#model(java.lang.Object)
   *
   * @param reportDefinition the <code>ReportDefinition</code> instance to wrap
   *
   * @return the Wicket model wrapping the <code>ReportDefinition</code> instance
   */
  public IModel<ReportDefinition> model(ReportDefinition reportDefinition)
  {
    return new DetachableReportDefinitionModel(reportDefinition);
  }

  /**
   * Returns the total number of matching report definitions in the database.
   *
   * @see org.apache.wicket.markup.repeater.data.IDataProvider#size()
   *
   * @return the total number of matching report definitions in the database
   */
  public long size()
  {
    try
    {
      return reportingService.getNumberOfReportDefinitionsForOrganisation(organisation);
    }
    catch (Throwable e)
    {
      throw new WebApplicationException("Failed to retrieve the number of report definitions", e);
    }
  }
}

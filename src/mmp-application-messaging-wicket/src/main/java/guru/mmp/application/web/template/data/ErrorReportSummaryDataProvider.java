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

import guru.mmp.application.messaging.ErrorReportSummary;
import guru.mmp.application.messaging.IMessagingService;
import guru.mmp.application.web.WebApplicationException;
import guru.mmp.application.web.data.InjectableDataProvider;

import org.apache.wicket.model.IModel;

//~--- JDK imports ------------------------------------------------------------

import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

/**
 * The <code>ErrorReportSummaryDataProvider</code> class provides an
 * <code>IDataProvider</code> implementation that retrieves <code>ErrorReportSummary</code>
 * instances from the database.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public class ErrorReportSummaryDataProvider extends InjectableDataProvider<ErrorReportSummary>
{
  private static final long serialVersionUID = 1000000;

  /**
   * The maximum number of summaries for the most recent error reports to retrieve.
   */
  int maximumNumberOfEntries;

  /* Messaging Service */
  @Inject
  private IMessagingService messagingService;

  /**
   * Constructs a new <code>ErrorReportSummaryDataProvider</code>.
   *
   * @param maximumNumberOfEntries the maximum number of summaries for the most recent error
   *                               reports to retrieve
   */
  public ErrorReportSummaryDataProvider(int maximumNumberOfEntries)
  {
    this.maximumNumberOfEntries = maximumNumberOfEntries;
  }

  /**
   * Constructs a new <code>ErrorReportSummaryDataProvider</code>.
   * <p/>
   * Hidden default constructor to support CDI.
   */
  protected ErrorReportSummaryDataProvider() {}

  /**
   * @see org.apache.wicket.model.IDetachable#detach()
   */
  public void detach() {}

  /**
   * Retrieves the summaries for the most recent error reports from the database starting
   * with index <code>first</code> and ending with <code>first+count</code>.
   *
   * @see org.apache.wicket.markup.repeater.data.IDataProvider#iterator(long, long)
   *
   * @param first the index of the first entry to return
   * @param count the number of the entries to return
   *
   * @return the summaries for the most recent error reports from the database starting with
   *         index <code>first</code> and ending with <code>first+count</code>
   */
  public Iterator<ErrorReportSummary> iterator(long first, long count)
  {
    try
    {
      List<ErrorReportSummary> allErrorReportSummaries =
        messagingService.getMostRecentErrorReportSummaries(maximumNumberOfEntries);

      return allErrorReportSummaries.subList((int) first,
          (int) Math.min(first + count, allErrorReportSummaries.size())).iterator();
    }
    catch (Throwable e)
    {
      throw new WebApplicationException(
          "Failed to load the summaries for the error reports from index (" + first + ") to ("
          + (first + count) + ")", e);
    }
  }

  /**
   * Wraps the retrieved <code>ErrorReportSummary</code> POJO with a Wicket model.
   *
   * @see org.apache.wicket.markup.repeater.data.IDataProvider#model(java.lang.Object)
   *
   * @param errorReportSummary the <code>ErrorReportSummary</code> instance to wrap
   *
   * @return the Wicket model wrapping the <code>ErrorReportSummary</code> instance
   */
  public IModel<ErrorReportSummary> model(ErrorReportSummary errorReportSummary)
  {
    return new DetachableErrorReportSummaryModel(errorReportSummary);
  }

  /**
   * Returns the total number of summaries for the error reports.
   *
   * @see org.apache.wicket.markup.repeater.data.IDataProvider#size()
   *
   * @return the total number of summaries for the error reports
   */
  public long size()
  {
    try
    {
      int numberOfErrorReports = messagingService.getNumberOfErrorReports();

      if (numberOfErrorReports > maximumNumberOfEntries)
      {
        return maximumNumberOfEntries;
      }
      else
      {
        return numberOfErrorReports;
      }
    }
    catch (Throwable e)
    {
      throw new WebApplicationException(
          "Failed to retrieve the total number of summaries for the error"
          + " reports in the database", e);
    }
  }
}

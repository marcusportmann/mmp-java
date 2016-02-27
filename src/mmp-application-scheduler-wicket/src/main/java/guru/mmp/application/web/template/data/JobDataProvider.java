/*
 * Copyright 2016 Marcus Portmann
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

import guru.mmp.application.scheduler.ISchedulerService;
import guru.mmp.application.scheduler.Job;
import guru.mmp.application.web.WebApplicationException;
import guru.mmp.application.web.data.InjectableDataProvider;

import org.apache.wicket.model.IModel;

//~--- JDK imports ------------------------------------------------------------

import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

/**
 * The <code>JobDataProvider</code> class provides an <code>IDataProvider</code>
 * implementation that retrieves <code>Job</code> instances from the database.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public class JobDataProvider extends InjectableDataProvider<Job>
{
  private static final long serialVersionUID = 1000000;

  /* Scheduler Service */
  @Inject
  private ISchedulerService schedulerService;

  /**
   * Constructs a new <code>JobDataProvider</code>.
   */
  public JobDataProvider() {}

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
  public Iterator<Job> iterator(long first, long count)
  {
    try
    {
      List<Job> allJobs = schedulerService.getJobs();

      return allJobs.subList((int) first, (int) Math.min(first + count, allJobs.size())).iterator();
    }
    catch (Throwable e)
    {
      throw new WebApplicationException(String.format(
          "Failed to load the jobs from index (%d) to (%d)", first, first + count - 1), e);
    }
  }

  /**
   * Wraps the retrieved <code>Job</code> POJO with a Wicket model.
   *
   * @param reportDefinition the <code>Job</code> instance to wrap
   *
   * @return the Wicket model wrapping the <code>Job</code> instance
   *
   * @see org.apache.wicket.markup.repeater.data.IDataProvider#model(java.lang.Object)
   */
  public IModel<Job> model(Job reportDefinition)
  {
    return new DetachableJobModel(reportDefinition);
  }

  /**
   * Returns the total number of jobs.
   *
   * @return the total number of jobs
   *
   * @see org.apache.wicket.markup.repeater.data.IDataProvider#size()
   */
  public long size()
  {
    try
    {
      return schedulerService.getNumberOfJobs();
    }
    catch (Throwable e)
    {
      throw new WebApplicationException("Failed to retrieve the number of jobs", e);
    }
  }
}

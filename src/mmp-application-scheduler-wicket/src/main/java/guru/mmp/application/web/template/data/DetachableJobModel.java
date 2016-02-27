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
import guru.mmp.application.web.data.InjectableLoadableDetachableModel;

//~--- JDK imports ------------------------------------------------------------

import java.util.UUID;

import javax.inject.Inject;

/**
 * The <code>DetachableJobModel</code> class provides a detachable model
 * implementation for the <code>Job</code> model class.
 *
 * @author Marcus Portmann
 */
public class DetachableJobModel extends InjectableLoadableDetachableModel<Job>
{
  private static final long serialVersionUID = 1000000;

  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the job.
   */
  private UUID id;

  /* Scheduler Service */
  @Inject
  private ISchedulerService schedulerService;

  /**
   * Constructs a new <code>DetachableJobModel</code>.
   * <p/>
   * Hidden default constructor to support CDI.
   */
  @SuppressWarnings("unused")
  protected DetachableJobModel() {}

  /**
   * Constructs a new <code>DetachableJobModel</code>.
   *
   * @param reportDefinition the <code>Job</code> instance
   */
  public DetachableJobModel(Job reportDefinition)
  {
    this(reportDefinition.getId());

    setObject(reportDefinition);
  }

  /**
   * Constructs a new <code>DetachableJobModel</code>.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the job
   */
  public DetachableJobModel(UUID id)
  {
    this.id = id;
  }

  /**
   * @see org.apache.wicket.model.LoadableDetachableModel#load()
   */
  @Override
  protected Job load()
  {
    try
    {
      return schedulerService.getJob(id);
    }
    catch (Throwable e)
    {
      throw new WebApplicationException(String.format("Failed to load the job (%s)", id), e);
    }
  }

  /**
   * Invoked when the model is detached after use.
   */
  @Override
  protected void onDetach()
  {
    super.onDetach();

    setObject(null);
  }
}

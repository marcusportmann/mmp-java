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

package guru.mmp.application.web.template.components;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.scheduler.Job;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;

import java.util.List;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>JobStatusChoiceRenderer</code> class implements a <code>ChoiceRenderer</code>
 * for <code>JobStatus</code> instances.
 *
 * @author Marcus Portmann
 */
public class JobStatusChoiceRenderer
  implements IChoiceRenderer<Job.Status>
{
  private static final long serialVersionUID = 1000000;

  /**
   * Constructs a new <code>JobStatusChoiceRenderer</code>.
   */
  public JobStatusChoiceRenderer() {}

  /**
   * Get the value for displaying to an end user.
   *
   * @param jobStatus the job status
   *
   * @return the value meant for displaying to an end user
   */
  public Object getDisplayValue(Job.Status jobStatus)
  {
    return jobStatus.getName();
  }

  /**
   * This method is called to get the id value of a job status (used as the value attribute
   * of a choice element).
   *
   * @param jobStatus the job status for which the id should be generated
   * @param index     the index of the object in the choices list
   *
   * @return the id value of the object
   */
  public String getIdValue(Job.Status jobStatus, int index)
  {
    return String.valueOf(jobStatus.getCode());
  }

  /**
   * This method is called to get an object back from its id representation. The id may be used to
   * find/load the object in a more efficient way than loading all choices and find the one with
   * the same id in the list.
   *
   * @param id      the id representation of the object
   * @param choices the model providing the list of all rendered choices
   *
   * @return a choice from the list that has this id
   */
  public Job.Status getObject(String id,
    IModel<? extends List<? extends Job.Status>> choices)
  {
    return Job.Status.fromCode(Integer.valueOf(id));
  }
}

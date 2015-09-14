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

import guru.mmp.application.process.IProcessService;
import guru.mmp.application.process.ProcessDefinition;
import guru.mmp.application.web.WebApplicationException;
import guru.mmp.application.web.data.InjectableLoadableDetachableModel;

import javax.inject.Inject;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>DetachableProcessDefinitionModel</code> class provides a detachable model
 * implementation for the <code>ProcessDefinition</code> model class.
 *
 * @author Marcus Portmann
 */
public class DetachableProcessDefinitionModel
  extends InjectableLoadableDetachableModel<ProcessDefinition>
{
  private static final long serialVersionUID = 1000000;

  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the process definition.
   */
  private String id;

  /* Process Service */
  @Inject
  private IProcessService processService;

  /**
   * Constructs a new <code>DetachableProcessDefinitionModel</code>.
   *
   * @param processDefinition the <code>ProcessDefinition</code> instance
   */
  public DetachableProcessDefinitionModel(ProcessDefinition processDefinition)
  {
    this(processDefinition.getId());

    setObject(processDefinition);
  }

  /**
   * Constructs a new <code>DetachableProcessDefinitionModel</code>.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the process
   *           definition
   */
  public DetachableProcessDefinitionModel(String id)
  {
    this.id = id;
  }

  /**
   * Constructs a new <code>DetachableProcessDefinitionModel</code>.
   * <p/>
   * Hidden default constructor to support CDI.
   */
  @SuppressWarnings("unused")
  protected DetachableProcessDefinitionModel() {}

  /**
   * @see org.apache.wicket.model.LoadableDetachableModel#load()
   */
  @Override
  protected ProcessDefinition load()
  {
    try
    {
      return processService.getProcessDefinition(id);
    }
    catch (Throwable e)
    {
      throw new WebApplicationException("Failed to load the process definition (" + id + ")", e);
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

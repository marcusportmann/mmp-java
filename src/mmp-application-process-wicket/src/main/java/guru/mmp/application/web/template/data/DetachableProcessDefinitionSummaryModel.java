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
import guru.mmp.application.process.ProcessDefinitionSummary;
import guru.mmp.application.web.WebApplicationException;
import guru.mmp.application.web.data.InjectableLoadableDetachableModel;

//~--- JDK imports ------------------------------------------------------------

import javax.inject.Inject;

/**
 * The <code>DetachableProcessDefinitionSummaryModel</code> class provides a detachable model
 * implementation for the <code>ProcessDefinitionSummary</code> model class.
 *
 * @author Marcus Portmann
 */
public class DetachableProcessDefinitionSummaryModel
  extends InjectableLoadableDetachableModel<ProcessDefinitionSummary>
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
   * The version of the process definition.
   */
  private int version;

  /**
   * Constructs a new <code>DetachableProcessDefinitionSummaryModel</code>.
   *
   * @param processDefinition the <code>ProcessDefinitionSummary</code> instance
   */
  public DetachableProcessDefinitionSummaryModel(ProcessDefinitionSummary processDefinition)
  {
    this(processDefinition.getId(), processDefinition.getVersion());

    setObject(processDefinition);
  }

  /**
   * Constructs a new <code>DetachableProcessDefinitionSummaryModel</code>.
   *
   * @param id      the Universally Unique Identifier (UUID) used to uniquely identify the process
   *                definition
   * @param version the version of the process definition
   */
  public DetachableProcessDefinitionSummaryModel(String id, int version)
  {
    this.id = id;
    this.version = version;
  }

  /**
   * Constructs a new <code>DetachableProcessDefinitionSummaryModel</code>.
   * <p/>
   * Hidden default constructor to support CDI.
   */
  @SuppressWarnings("unused")
  protected DetachableProcessDefinitionSummaryModel() {}

  /**
   * @see org.apache.wicket.model.LoadableDetachableModel#load()
   */
  @Override
  protected ProcessDefinitionSummary load()
  {
    try
    {
      return processService.getProcessDefinitionSummary(id, version);
    }
    catch (Throwable e)
    {
      throw new WebApplicationException(
          "Failed to load the summary for the process definition with ID (" + id
          + ") and version (" + version + ")", e);
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

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
import guru.mmp.application.web.data.InjectableDataProvider;

import org.apache.wicket.model.IModel;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

/**
 * The <code>ProcessDefinitionSummaryDataProvider</code> class provides an
 * <code>IDataProvider</code> implementation that retrieves <code>ProcessDefinitionSummary</code>
 * instances from the database.
 *
 * @author Marcus Portmann
 */
public class ProcessDefinitionSummaryDataProvider
  extends InjectableDataProvider<ProcessDefinitionSummary>
{
  private static final long serialVersionUID = 1000000;

  /* Process Service */
  @Inject
  private IProcessService processService;

  /**
   * Constructs a new <code>ProcessDefinitionSummaryDataProvider</code>.
   */
  public ProcessDefinitionSummaryDataProvider() {}

  /**
   * @see org.apache.wicket.model.IDetachable#detach()
   */
  public void detach() {}

  /**
   * Retrieves the summaries for the matching process definitions from the database starting with
   * index <code>first</code> and ending with <code>first+count</code>.
   *
   * @see org.apache.wicket.markup.repeater.data.IDataProvider#iterator(long, long)
   *
   * @param first the index of the first entry to return
   * @param count the number of the entries to return
   *
   * @return the summaries for the process definitions retrieved from the database starting with
   *         index <code>first</code> and ending with <code>first+count</code>
   */
  public Iterator<ProcessDefinitionSummary> iterator(long first, long count)
  {
    try
    {
      List<ProcessDefinitionSummary> allProcessDefinitionSummaries =
        processService.getCurrentProcessDefinitionSummaries();

      List<ProcessDefinitionSummary> processDefinitionSummaries = new ArrayList<>();

      long end = first + count;

      for (long i = first; ((i < end) && (i < allProcessDefinitionSummaries.size())); i++)
      {
        processDefinitionSummaries.add(allProcessDefinitionSummaries.get((int) i));
      }

      return processDefinitionSummaries.iterator();
    }
    catch (Throwable e)
    {
      throw new WebApplicationException(
          "Failed to load the summaries for the process definitions from index (" + first
          + ") to (" + (first + count) + ")", e);
    }
  }

  /**
   * Wraps the retrieved <code>ProcessDefinitionSummary</code> POJO with a Wicket model.
   *
   * @see org.apache.wicket.markup.repeater.data.IDataProvider#model(java.lang.Object)
   *
   * @param processDefinition the <code>ProcessDefinitionSummary</code> instance to wrap
   *
   * @return the Wicket model wrapping the <code>ProcessDefinitionSummary</code> instance
   */
  public IModel<ProcessDefinitionSummary> model(ProcessDefinitionSummary processDefinition)
  {
    return new DetachableProcessDefinitionSummaryModel(processDefinition);
  }

  /**
   * Returns the total number of process definitions.
   *
   * @see org.apache.wicket.markup.repeater.data.IDataProvider#size()
   *
   * @return the total number of process definitions
   */
  public long size()
  {
    try
    {
      return processService.getNumberOfProcessDefinitions();
    }
    catch (Throwable e)
    {
      throw new WebApplicationException("Failed to retrieve the number of process definitions", e);
    }
  }
}

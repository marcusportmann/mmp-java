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
import guru.mmp.application.web.data.InjectableDataProvider;

import org.apache.wicket.model.IModel;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

/**
 * The <code>ProcessDefinitionDataProvider</code> class provides an <code>IDataProvider</code>
 * implementation that retrieves <code>ProcessDefinition</code> instances from the database.
 *
 * @author Marcus Portmann
 */
public class ProcessDefinitionDataProvider extends InjectableDataProvider<ProcessDefinition>
{
  private static final long serialVersionUID = 1000000;

  /**
   * The organisation code identifying the organisation the process definitions are associated with.
   */
  private String organisation;

  /* Process Service */
  @Inject
  private IProcessService processService;

  /**
   * Constructs a new <code>ProcessDefinitionDataProvider</code>.
   *
   * @param organisation the organisation code identifying the organisation the process definitions
   *                     are associated with
   */
  public ProcessDefinitionDataProvider(String organisation)
  {
    this.organisation = organisation;
  }

  /**
   * Constructs a new <code>ProcessDefinitionDataProvider</code>.
   * <p/>
   * Hidden default constructor to support CDI.
   */
  @SuppressWarnings("unused")
  protected ProcessDefinitionDataProvider() {}

  /**
   * @see org.apache.wicket.model.IDetachable#detach()
   */
  public void detach() {}

  /**
   * Retrieves the matching process definitions from the database starting with
   * index <code>first</code> and ending with <code>first+count</code>.
   *
   * @see org.apache.wicket.markup.repeater.data.IDataProvider#iterator(long, long)
   *
   * @param first the index of the first entry to return
   * @param count the number of the entries to return
   *
   * @return the process definitions retrieved from the database starting with
   *         index <code>first</code> and ending with <code>first+count</code>
   */
  public Iterator<ProcessDefinition> iterator(long first, long count)
  {
    try
    {
      List<ProcessDefinition> allProcessDefinitions =
        processService.getCurrentProcessDefinitionsForOrganisation(organisation);

      List<ProcessDefinition> processDefinitions = new ArrayList<>();

      long end = first + count;

      for (long i = first; ((i < end) && (i < allProcessDefinitions.size())); i++)
      {
        processDefinitions.add(allProcessDefinitions.get((int) i));
      }

      return processDefinitions.iterator();
    }
    catch (Throwable e)
    {
      throw new WebApplicationException("Failed to load the process definitions from index ("
          + first + ") to (" + (first + count) + ")", e);
    }
  }

  /**
   * Wraps the retrieved <code>ProcessDefinition</code> POJO with a Wicket model.
   *
   * @see org.apache.wicket.markup.repeater.data.IDataProvider#model(java.lang.Object)
   *
   * @param processDefinition the <code>ProcessDefinition</code> instance to wrap
   *
   * @return the Wicket model wrapping the <code>ProcessDefinition</code> instance
   */
  public IModel<ProcessDefinition> model(ProcessDefinition processDefinition)
  {
    return new DetachableProcessDefinitionModel(processDefinition);
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
      return processService.getNumberOfProcessDefinitionsForOrganisation(organisation);
    }
    catch (Throwable e)
    {
      throw new WebApplicationException("Failed to retrieve the number of process definitions", e);
    }
  }
}

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

import guru.mmp.application.security.ISecurityService;
import guru.mmp.application.security.Organisation;
import guru.mmp.application.web.WebApplicationException;
import guru.mmp.application.web.data.InjectableDataProvider;
import org.apache.wicket.model.IModel;
import org.springframework.beans.factory.annotation.Autowired;

import javax.inject.Inject;
import java.util.Iterator;
import java.util.List;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>FilteredOrganisationDataProvider</code> class provides an <code>IDataProvider</code>
 * implementation that retrieves a filtered list of <code>Organisation</code> instances from the
 * database.
 *
 * @author Marcus Portmann
 */
public class FilteredOrganisationDataProvider extends InjectableDataProvider<Organisation>
{
  private static final long serialVersionUID = 1000000;

  /**
   * The filter used to limit the matching organisations.
   */
  private String filter;

  /* Security Service */
  @Inject
  private ISecurityService securityService;

  /**
   * Constructs a new <code>FilteredOrganisationDataProvider</code>.
   */
  public FilteredOrganisationDataProvider() {}

  /**
   * @see org.apache.wicket.model.IDetachable#detach()
   */
  public void detach() {}

  /**
   * Returns the filter used to limit the matching organisations.
   *
   * @return the filter used to limit the matching organisations
   */
  public String getFilter()
  {
    return filter;
  }

  /**
   * Retrieves the matching organisations from the database starting with
   * index <code>first</code> and ending with <code>first+count</code>.
   *
   * @param first the index of the first entry to return
   * @param count the number of the entries to return
   *
   * @return the organisations retrieved from the database starting with index
   *         <code>first</code> and ending with <code>first+count</code>
   *
   * @see org.apache.wicket.markup.repeater.data.IDataProvider#iterator(long, long)
   */
  public Iterator<Organisation> iterator(long first, long count)
  {
    try
    {
      List<Organisation> allOrganisations = securityService.getFilteredOrganisations(filter);

      return allOrganisations.subList((int) first, (int) Math.min(first + count,
          allOrganisations.size())).iterator();
    }
    catch (Throwable e)
    {
      throw new WebApplicationException(String.format(
          "Failed to load the organisations from index (%d) to (%d) matching the filter (%s)",
          first, first + count - 1, filter), e);
    }
  }

  /**
   * Wraps the retrieved <code>Organisation</code> POJO with a Wicket model.
   *
   * @param organisation the <code>Organisation</code> instance to wrap
   *
   * @return the Wicket model wrapping the <code>Organisation</code> instance
   *
   * @see org.apache.wicket.markup.repeater.data.IDataProvider#model(java.lang.Object)
   */
  public IModel<Organisation> model(Organisation organisation)
  {
    return new DetachableOrganisationModel(organisation);
  }

  /**
   * Set the filter used to limit the matching organisations.
   *
   * @param filter the filter used to limit the matching organisations
   */
  public void setFilter(String filter)
  {
    this.filter = filter;
  }

  /**
   * Returns the total number of organisations.
   *
   * @return the total number of organisations
   *
   * @see org.apache.wicket.markup.repeater.data.IDataProvider#size()
   */
  public long size()
  {
    try
    {
      return securityService.getNumberOfFilteredOrganisations(filter);
    }
    catch (Throwable e)
    {
      throw new WebApplicationException(String.format(
          "Failed to retrieve the number of organisations matching the filter (%s)", filter), e);
    }
  }
}

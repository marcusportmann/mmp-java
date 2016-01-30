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

import guru.mmp.application.configuration.ConfigurationValue;
import guru.mmp.application.configuration.IConfigurationService;
import guru.mmp.application.web.WebApplicationException;
import guru.mmp.application.web.data.InjectableDataProvider;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

//~--- JDK imports ------------------------------------------------------------

import java.util.*;

import javax.inject.Inject;

/**
 * The <code>FilteredConfigurationDataProvider</code> class provides an
 * <code>IDataProvider</code> implementation that retrieves a filtered list of
 * <code>ConfigurationValue</code> instances for the configuration values managed by the
 * Configuration Service.
 *
 * @author Marcus Portmann
 */
public class FilteredConfigurationValueDataProvider
    extends InjectableDataProvider<ConfigurationValue>
{
  private static final long serialVersionUID = 1000000;

  /**
   * The filter used to limit the matching configuration values.
   */
  private String filter;

  /* Configuration Service */
  @Inject
  private IConfigurationService configurationService;

  /**
   * Constructs a new <code>FilteredConfigurationDataProvider</code>.
   */
  public FilteredConfigurationValueDataProvider() {}

  /**
   * @see org.apache.wicket.model.IDetachable#detach()
   */
  public void detach() {}

  /**
   * Returns the filter used to limit the matching configuration values.
   *
   * @return the filter used to limit the matching configuration values
   */
  public String getFilter()
  {
    return filter;
  }

  /**
   * Retrieves the matching configuration values from the Configuration Service starting with
   * index <code>first</code> and ending with <code>first+count</code>.
   *
   * @param first the index of the first entry to return
   * @param count the number of the entries to return
   *
   * @return the configuration values retrieved from the Configuration Service starting with index
   * <code>first</code> and ending with <code>first+count</code>
   *
   * @see org.apache.wicket.markup.repeater.data.IDataProvider#iterator(long, long)
   */
  public Iterator<ConfigurationValue> iterator(long first, long count)
  {
    try
    {
      List<ConfigurationValue> allConfigurationEntries =
          configurationService.getFilteredConfigurationValues(filter);

      return allConfigurationEntries.subList((int) first, (int) Math.min(first + count,
          allConfigurationEntries.size())).iterator();
    }
    catch (Throwable e)
    {
      throw new WebApplicationException(String.format("Failed to load the configuration values"
          + " from index (%d) to (%d) matching the filter (%s)", first, first + count - 1, filter),
          e);
    }
  }

  /**
   * Wraps the retrieved <code>Configuration</code> POJO with a Wicket model.
   *
   * @param configurationValue the <code>Configuration</code> instance to wrap
   *
   * @return the Wicket model wrapping the <code>Configuration</code> instance
   *
   * @see org.apache.wicket.markup.repeater.data.IDataProvider#model(java.lang.Object)
   */
  public IModel<ConfigurationValue> model(ConfigurationValue configurationValue)
  {
    return new Model<>(configurationValue);
  }

  /**
   * Set the filter used to limit the matching configuration values.
   *
   * @param filter the filter used to limit the matching configuration values
   */
  public void setFilter(String filter)
  {
    this.filter = filter;
  }

  /**
   * Returns the total number of filtered configuration values.
   *
   * @return the total number of filtered configuration values
   *
   * @see org.apache.wicket.markup.repeater.data.IDataProvider#size()
   */
  public long size()
  {
    try
    {
      return configurationService.getNumberOfFilteredConfigurationValues(filter);
    }
    catch (Throwable e)
    {
      throw new WebApplicationException(String.format("Failed to retrieve the number of"
          + " configuration values matching the filter (%s)", filter), e);
    }
  }
}

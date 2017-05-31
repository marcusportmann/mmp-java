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

import guru.mmp.application.reporting.IReportingService;
import guru.mmp.application.reporting.ReportDefinition;
import guru.mmp.application.web.WebApplicationException;
import guru.mmp.application.web.data.InjectableLoadableDetachableModel;
import org.springframework.beans.factory.annotation.Autowired;

import javax.inject.Inject;
import java.util.UUID;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>DetachableReportDefinitionModel</code> class provides a detachable model
 * implementation for the <code>ReportDefinition</code> model class.
 *
 * @author Marcus Portmann
 */
class DetachableReportDefinitionModel extends InjectableLoadableDetachableModel<ReportDefinition>
{
  private static final long serialVersionUID = 1000000;

  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the report definition.
   */
  private UUID id;

  /* Reporting Service */
  @Inject
  private IReportingService reportingService;

  /**
   * Constructs a new <code>DetachableReportDefinitionModel</code>.
   * <p/>
   * Hidden default constructor to support CDI.
   */
  @SuppressWarnings("unused")
  protected DetachableReportDefinitionModel() {}

  /**
   * Constructs a new <code>DetachableReportDefinitionModel</code>.
   *
   * @param reportDefinition the <code>ReportDefinition</code> instance
   */
  DetachableReportDefinitionModel(ReportDefinition reportDefinition)
  {
    this(reportDefinition.getId());

    setObject(reportDefinition);
  }

  /**
   * Constructs a new <code>DetachableReportDefinitionModel</code>.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the report
   *           definition
   */
  private DetachableReportDefinitionModel(UUID id)
  {
    this.id = id;
  }

  /**
   * @see org.apache.wicket.model.LoadableDetachableModel#load()
   */
  @Override
  protected ReportDefinition load()
  {
    try
    {
      return reportingService.getReportDefinition(id);
    }
    catch (Throwable e)
    {
      throw new WebApplicationException(String.format("Failed to load the report definition (%s)",
          id), e);
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

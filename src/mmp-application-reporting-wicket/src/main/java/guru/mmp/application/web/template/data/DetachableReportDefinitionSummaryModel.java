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
import guru.mmp.application.reporting.ReportDefinitionSummary;
import guru.mmp.application.web.WebApplicationException;
import guru.mmp.application.web.data.InjectableLoadableDetachableModel;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>DetachableReportDefinitionSummaryModel</code> class provides a detachable model
 * implementation for the <code>ReportDefinitionSummary</code> model class.
 *
 * @author Marcus Portmann
 */
class DetachableReportDefinitionSummaryModel
    extends InjectableLoadableDetachableModel<ReportDefinitionSummary>
{
  private static final long serialVersionUID = 1000000;

  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the report definition.
   */
  private UUID id;

  /* Reporting Service */
  @Autowired
  private IReportingService reportingService;

  /**
   * Constructs a new <code>DetachableReportDefinitionSummaryModel</code>.
   * <p/>
   * Hidden default constructor to support CDI.
   */
  @SuppressWarnings("unused")
  protected DetachableReportDefinitionSummaryModel() {}

  /**
   * Constructs a new <code>DetachableReportDefinitionSummaryModel</code>.
   *
   * @param reportDefinitionSummary the <code>ReportDefinitionSummary</code> instance
   */
  DetachableReportDefinitionSummaryModel(ReportDefinitionSummary reportDefinitionSummary)
  {
    this(reportDefinitionSummary.getId());

    setObject(reportDefinitionSummary);
  }

  /**
   * Constructs a new <code>DetachableReportDefinitionSummaryModel</code>.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the report
   *           definition
   */
  private DetachableReportDefinitionSummaryModel(UUID id)
  {
    this.id = id;
  }

  /**
   * @see org.apache.wicket.model.LoadableDetachableModel#load()
   */
  @Override
  protected ReportDefinitionSummary load()
  {
    try
    {
      return reportingService.getReportDefinitionSummary(id);
    }
    catch (Throwable e)
    {
      throw new WebApplicationException(String.format(
          "Failed to load the summary for the report definition (%s)", id), e);
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

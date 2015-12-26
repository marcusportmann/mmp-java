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

import guru.mmp.application.messaging.ErrorReportSummary;
import guru.mmp.application.messaging.IMessagingService;
import guru.mmp.application.web.WebApplicationException;
import guru.mmp.application.web.data.InjectableLoadableDetachableModel;

//~--- JDK imports ------------------------------------------------------------

import java.util.UUID;

import javax.inject.Inject;

/**
 * The <code>DetachableErrorReportSummaryModel</code> class provides a detachable model
 * implementation for the <code>ErrorReportSummary</code> model class.
 *
 * @author Marcus Portmann
 */
public class DetachableErrorReportSummaryModel
  extends InjectableLoadableDetachableModel<ErrorReportSummary>
{
  private static final long serialVersionUID = 1000000;

  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the error report.
   */
  private UUID id;

  /* Messaging Service */
  @Inject
  private IMessagingService messagingService;

  /**
   * Constructs a new <code>DetachableErrorReportSummaryModel</code>.
   *
   * @param errorReportSummary the <code>ErrorReportSummary</code> instance
   */
  public DetachableErrorReportSummaryModel(ErrorReportSummary errorReportSummary)
  {
    this(errorReportSummary.getId());

    setObject(errorReportSummary);
  }

  /**
   * Constructs a new <code>DetachableErrorReportSummaryModel</code>.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the
   *           error report
   */
  public DetachableErrorReportSummaryModel(UUID id)
  {
    this.id = id;
  }

  /**
   * Constructs a new <code>DetachableErrorReportSummaryModel</code>.
   * <p/>
   * Hidden default constructor to support CDI.
   */
  @SuppressWarnings("unused")
  protected DetachableErrorReportSummaryModel() {}

  /**
   * @see org.apache.wicket.model.LoadableDetachableModel#load()
   */
  @Override
  protected ErrorReportSummary load()
  {
    try
    {
      return messagingService.getErrorReportSummary(id);
    }
    catch (Throwable e)
    {
      throw new WebApplicationException("Failed to load the summary for the error report (" + id
          + ")", e);
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

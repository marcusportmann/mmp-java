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

package guru.mmp.application.messaging;

//~--- non-JDK imports --------------------------------------------------------

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.*;
import javax.inject.Inject;
import java.util.concurrent.Future;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>BackgroundMessageProcessorTimer</code> class implements the timer for the background
 * message processor.
 *
 * @author Marcus Portmann
 */
@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
@TransactionManagement(TransactionManagementType.BEAN)
public class BackgroundMessageProcessorTimer
{
  /* Logger */
  private static Logger logger = LoggerFactory.getLogger(BackgroundMessageProcessorTimer.class);

  /* Background Message Processor */
  @Inject
  private BackgroundMessageProcessor backgroundMessageProcessor;

  /* The result of processing the messages. */
  private Future<Boolean> processMessagesResult;

  /**
   * Constructs a new <code>BackgroundMessageProcessorTimer</code>.
   */
  public BackgroundMessageProcessorTimer()
  {
    processMessagesResult = new AsyncResult<>(false);
  }

  /**
   * Process.
   */
  @Schedule(hour = "*", minute = "*", second = "*/30", persistent = false)
  public void process()
  {
    if (processMessagesResult.isDone())
    {
      /*
       * Asynchronously inform the background message processor that all pending messages should be
       * processed.
       */
      try
      {
        processMessagesResult = backgroundMessageProcessor.process();
      }
      catch (Throwable e)
      {
        logger.error("Failed to invoke the background message processor to asynchronously process"
            + " all pending messages", e);
      }
    }
  }
}

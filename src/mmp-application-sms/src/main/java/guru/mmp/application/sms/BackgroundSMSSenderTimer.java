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

package guru.mmp.application.sms;

//~--- JDK imports ------------------------------------------------------------

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

/**
 * The <code>BackgroundSMSSenderTimer</code> class implements the timer for the background
 * SMS sender.
 *
 * @author Marcus Portmann
 */
@Singleton
@TransactionManagement(TransactionManagementType.BEAN)
public class BackgroundSMSSenderTimer
{
  /* SMS Service */
  @Inject
  private ISMSService smsService;

  /**
   * Send SMSs.
   */
  @Schedule(hour = "*", minute = "*", second = "*/30", persistent = false)
  public void sendSMSs()
  {
    smsService.sendSMSs();
  }
}

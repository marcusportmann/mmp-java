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

package guru.mmp.application.test;

import org.hibernate.engine.transaction.jta.platform.internal.AbstractJtaPlatform;

import javax.naming.InitialContext;
import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;

/**
 * The <code>ApplicationJUnit4JtaPlatform</code> class.
 *
 * @author Marcus Portmann
 */
public class ApplicationJUnit4JtaPlatform
  extends AbstractJtaPlatform
{
  @Override
  protected TransactionManager locateTransactionManager()
  {
    try
    {
      return (TransactionManager)InitialContext.doLookup("java:comp/TransactionManager");
    }
    catch (Throwable e)
    {
      throw new RuntimeException("Failed to lookup the TransactionManager", e);
    }
  }

  @Override
  protected UserTransaction locateUserTransaction()
  {
    try
    {
      return (UserTransaction)InitialContext.doLookup("java:comp/UserTransaction");
    }
    catch (Throwable e)
    {
      throw new RuntimeException("Failed to lookup the UserTransaction", e);
    }
  }
}

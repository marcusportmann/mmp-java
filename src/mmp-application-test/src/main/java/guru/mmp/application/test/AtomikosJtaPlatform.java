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

package guru.mmp.application.test;

//~--- non-JDK imports --------------------------------------------------------

import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import org.hibernate.engine.transaction.jta.platform.internal.AbstractJtaPlatform;

import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>AtomikosJtaPlatform</code> class exposes the Atomikos transaction management
 * capabilities to Hibernate.
 *
 * @author Marcus Portmann
 */
public class AtomikosJtaPlatform extends AbstractJtaPlatform
{
  static UserTransactionManager atomikosTransactionManager;
  static UserTransactionImp atomikosUserTransaction;

  @Override
  protected TransactionManager locateTransactionManager()
  {
    return atomikosTransactionManager;
  }

  @Override
  protected UserTransaction locateUserTransaction()
  {
    return atomikosUserTransaction;
  }
}

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

package guru.mmp.sample.model;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.common.persistence.TransactionManager;

import net.sf.jasperreports.engine.util.JRStyledText;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import javax.transaction.Transaction;
import javax.transaction.Transactional;

/**
 * The <code>SampleService</code> class provides the Sample Service implementation.
 *
 * @author Marcus Portmann
 */
@ApplicationScoped
@Default
public class SampleService
  implements ISampleService
{
  /* Entity Manager */
  @PersistenceContext(unitName = "Sample")
  private EntityManager entityManager;

  /**
   * Add the data.
   *
   * @throws SampleServiceException
   */
  @Transactional
  public void addData()
    throws SampleServiceException
  {
    try
    {
      Data newData = new Data();
      newData.setId(666);
      newData.setName("New Name");
      newData.setValue("New Value");

      entityManager.persist(newData);
    }
    catch (Throwable e)
    {
      throw new SampleServiceException("Failed to retrieve the data", e);
    }
  }

  /**
   * Returns the data.
   *
   * @return the data
   *
   * @throws SampleServiceException
   */
  @Transactional
  public List<Data> getData()
    throws SampleServiceException
  {
    try
    {
      TypedQuery<Data> query = entityManager.createQuery("SELECT d FROM Data d", Data.class);

      return query.getResultList();
    }
    catch (Throwable e)
    {
      throw new SampleServiceException("Failed to retrieve the data", e);
    }
  }

  /**
   * The test method.
   */
  public void testMethod()
  {
    System.out.println("[DEBUG] Hello world from the test method!!!");
  }

  private void validateTransaction(Transaction transaction)
    throws SampleServiceException
  {
    try
    {
      Transaction currentTransaction = TransactionManager.getTransactionManager().getTransaction();

      if (currentTransaction != transaction)
      {
        throw new RuntimeException(
            "The current transaction does not match the transaction to validate");
      }
    }
    catch (Throwable e)
    {
      throw new SampleServiceException("Failed to validate the transaction (" + transaction + ")",
          e);
    }
  }
}
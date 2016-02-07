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

//~--- JDK imports ------------------------------------------------------------

import guru.mmp.common.persistence.TransactionManager;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.transaction.Transaction;

/**
 * The <code>NonTransactionalEntityManagerTracker</code> class.
 *
 * @author Marcus Portmann
 */
public class EntityManagerTracker
{


  ADD TRACKING FOR TRANSACTIONAL ENTITY MANAGERS





  private EntityManager createEntityManager(String persistenceUnitName)
  {
    try
    {
      Map<String, String> properties = new HashMap<>();

      properties.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
      properties.put("hibernate.transaction.auto_close_session", "true");
      properties.put("hibernate.current_session_context_class", "jta");
      properties.put("hibernate.transaction.jta.platform",
        "guru.mmp.common.test.ApplicationJUnit4JtaPlatform");

      EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(
        persistenceUnitName, properties);

      return entityManagerFactory.createEntityManager();
    }
    catch (Throwable e)
    {
      throw new RuntimeException("Failed to create the entity manager for the persistence unit ("
        + persistenceUnitName + ")", e);

    }
  }


  private EntityManager getEntityManager()
  {
    try
    {
      TransactionManager transactionManager = TransactionManager.getTransactionManager();

      Transaction transaction = transactionManager.getTransaction();

      if (transaction == null)
      {
        EntityManager entityManager = EntityManagerTracker.getNonTransactionalEntityManager(persistenceUnitName);

        if (entityManager != null)
        {
          return entityManager;
        }
        else
        {
          entityManager = createEntityManager(persistenceUnitName);

          EntityManagerTracker.addNonTransactionalEntityManager(persistenceUnitName,
            entityManager);

          return entityManager;
        }
      }
      else
      {
        return null;

      }




      //TransactionSynchronizationRegistry




      return null;
    }
    catch (Throwable e)
    {
      throw new RuntimeException("Failed to retrieve the entity manager for the persistence unit (" + persistenceUnitName + ")", e);
    }
  }













  private static ThreadLocal<Map<String, EntityManager>> nonTransactionalEntityManagers =
      new ThreadLocal<Map<String, EntityManager>>()
  {
    @Override
    protected Map<String, EntityManager> initialValue()
    {
      return new HashMap<>();
    }
  };

  /**
   * Add the the non-transactional <code>EntityManager</code> instance for the persistence unit
   * with the specified name.
   *
   * @param persistenceUnitName the name of the persistence unit
   * @param entityManager       the non-transactional     <code>EntityManager</code> instance
   */
  public static void addNonTransactionalEntityManager(String persistenceUnitName,
      EntityManager entityManager)
  {
    nonTransactionalEntityManagers.get().put(persistenceUnitName, entityManager);
  }

  /**
   * Returns the transactional or non-transactional <code>EntityManager</code> instance for the
   * persistence unit with the specified name.
   * <p/>
   * If an existing transactional or non-transactional <code>EntityManager</code> instance cannot
   * be found then a new one will be created.
   *
   * @param persistenceUnitName the name of the persistence unit
   *
   * @return the transactional or non-transactional <code>EntityManager</code> instance for the
   *         persistence unit with the specified name
   */
  public static EntityManager getEntityManager(String persistenceUnitName)
  {
    // Retrieve the existing transaction if one exists



    /*
     * If there is an existing transaction, attempt to retrieve the transactional entity manager for
     * the persistence unit with the specified name. If an existing entity manager cannot be found
     * then create a new entity manager for the persistence context with the specified name and
     * associate it with the existing transaction.
     */
    TODO



    /*
     * If there is not an existing transaction, attempt to retrieve the entity manager for the
     * persistence unit with the specified name. If an existing entity manager cannot be found then
     * create a new entity manager for the persistence context.
     */
    TODO




    return nonTransactionalEntityManagers.get().get(persistenceUnitName);
  }

  /**
   * Returns the non-transactional <code>EntityManager</code> instances associated with the current
   * thread.
   *
   * @return the the non-transactional <code>EntityManager</code> instances associated with the
   *         current thread
   */
  public static List<EntityManager> getNonTransactionalEntityManagers()
  {
    return new ArrayList<>(nonTransactionalEntityManagers.get().values());
  }
}

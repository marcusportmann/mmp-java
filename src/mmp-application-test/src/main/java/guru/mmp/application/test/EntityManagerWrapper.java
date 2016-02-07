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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.metamodel.Metamodel;
import javax.transaction.Transaction;

/**
 * The <code>EntityManagerWrapper</code> class.
 *
 * @author Marcus Portmann
 */
public class EntityManagerWrapper
  implements EntityManager
{
  private String persistenceUnitName;

  /**
   * Constructs a new <code>ApplicationJUnit4EntityManagerWrapper</code>.
   *
   * @param persistenceUnitName the name of the persistence unit
   */
  public EntityManagerWrapper(String persistenceUnitName)
  {
    this.persistenceUnitName = persistenceUnitName;
  }

  @Override
  public void clear()
  {
    EntityManagerTracker.getEntityManager(persistenceUnitName).clear();
  }

  @Override
  public void close() {}

  @Override
  public boolean contains(Object entity)
  {
    return false;
  }

  @Override
  public <T> EntityGraph<T> createEntityGraph(Class<T> rootType)
  {
    return null;
  }

  @Override
  public EntityGraph<?> createEntityGraph(String graphName)
  {
    return null;
  }

  @Override
  public Query createNamedQuery(String name)
  {
    return null;
  }

  @Override
  public <T> TypedQuery<T> createNamedQuery(String name, Class<T> resultClass)
  {
    return null;

    TypedQuery<T> typedQuery = getEntityManager().createNamedQuery(name, resultClass);

    return new TypedQueryNonTxInvocationDetacher<>(getEntityManager(), typedQuery);
  }

  @Override
  public StoredProcedureQuery createNamedStoredProcedureQuery(String name)
  {
    return null;
  }

  @Override
  public Query createNativeQuery(String sqlString)
  {
    return null;
  }

  @Override
  public Query createNativeQuery(String sqlString, Class resultClass)
  {
    return null;
  }

  @Override
  public Query createNativeQuery(String sqlString, String resultSetMapping)
  {
    return null;
  }

  @Override
  public Query createQuery(CriteriaDelete deleteQuery)
  {
    return null;
  }

  @Override
  public <T> TypedQuery<T> createQuery(CriteriaQuery<T> criteriaQuery)
  {
    return null;
  }

  @Override
  public Query createQuery(CriteriaUpdate updateQuery)
  {
    return null;
  }

  @Override
  public Query createQuery(String qlString)
  {
    return null;
  }

  @Override
  public <T> TypedQuery<T> createQuery(String qlString, Class<T> resultClass)
  {
    return null;
  }

  @Override
  public StoredProcedureQuery createStoredProcedureQuery(String procedureName)
  {
    return null;
  }

  @Override
  public StoredProcedureQuery createStoredProcedureQuery(String procedureName,
      Class... resultClasses)
  {
    return null;
  }

  @Override
  public StoredProcedureQuery createStoredProcedureQuery(String procedureName,
      String... resultSetMappings)
  {
    return null;
  }

  @Override
  public void detach(Object entity) {}

  @Override
  public <T> T find(Class<T> entityClass, Object primaryKey)
  {
    return null;
  }

  @Override
  public <T> T find(Class<T> entityClass, Object primaryKey, LockModeType lockMode)
  {
    return null;
  }

  @Override
  public <T> T find(Class<T> entityClass, Object primaryKey, Map<String, Object> properties)
  {
    return null;
  }

  @Override
  public <T> T find(Class<T> entityClass, Object primaryKey, LockModeType lockMode, Map<String,
      Object> properties)
  {
    return null;
  }

  @Override
  public void flush() {}

  @Override
  public CriteriaBuilder getCriteriaBuilder()
  {
    return null;
  }

  @Override
  public Object getDelegate()
  {
    return null;
  }

  @Override
  public EntityGraph<?> getEntityGraph(String graphName)
  {
    return null;
  }

  @Override
  public <T> List<EntityGraph<? super T>> getEntityGraphs(Class<T> entityClass)
  {
    return null;
  }

  @Override
  public EntityManagerFactory getEntityManagerFactory()
  {
    return null;
  }

  @Override
  public FlushModeType getFlushMode()
  {
    return null;
  }

  @Override
  public LockModeType getLockMode(Object entity)
  {
    return null;
  }

  @Override
  public Metamodel getMetamodel()
  {
    return null;
  }

  @Override
  public Map<String, Object> getProperties()
  {
    return null;
  }

  @Override
  public <T> T getReference(Class<T> entityClass, Object primaryKey)
  {
    return null;
  }

  @Override
  public EntityTransaction getTransaction()
  {
    return null;
  }

  @Override
  public boolean isJoinedToTransaction()
  {
    return false;
  }

  @Override
  public boolean isOpen()
  {
    return false;
  }

  @Override
  public void joinTransaction() {}

  @Override
  public void lock(Object entity, LockModeType lockMode) {}

  @Override
  public void lock(Object entity, LockModeType lockMode, Map<String, Object> properties) {}

  @Override
  public <T> T merge(T entity)
  {
    return null;
  }

  @Override
  public void persist(Object entity) {}

  @Override
  public void refresh(Object entity) {}

  @Override
  public void refresh(Object entity, LockModeType lockMode) {}

  @Override
  public void refresh(Object entity, Map<String, Object> properties) {}

  @Override
  public void refresh(Object entity, LockModeType lockMode, Map<String, Object> properties) {}

  @Override
  public void remove(Object entity) {}

  @Override
  public void setFlushMode(FlushModeType flushMode) {}

  @Override
  public void setProperty(String propertyName, Object value) {}

  @Override
  public <T> T unwrap(Class<T> cls)
  {
    return null;
  }





}

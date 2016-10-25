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

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.common.persistence.TransactionManager;

//~--- JDK imports ------------------------------------------------------------

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.metamodel.Metamodel;

import javax.transaction.Status;
import javax.transaction.Synchronization;
import javax.transaction.Transaction;

/**
 * The <code>EntityManagerWrapper</code> class.
 *
 * @author Marcus Portmann
 */
public class EntityManagerWrapper
  implements EntityManager
{
  private static Map<String, EntityManagerFactory> entityManagerFactories =
      new ConcurrentHashMap<>();
  private static ThreadLocal<Map<Transaction, Map<String, EntityManager>>> transactionalEntityManagers =
      new ThreadLocal<Map<Transaction, Map<String, EntityManager>>>()
  {
    @Override
    protected Map<Transaction, Map<String, EntityManager>> initialValue()
    {
      return new HashMap<>();
    }
  };
  private static ThreadLocal<Map<String, EntityManager>> nonTransactionalEntityManagers =
      new ThreadLocal<Map<String, EntityManager>>()
  {
    @Override
    protected Map<String, EntityManager> initialValue()
    {
      return new HashMap<>();
    }
  };
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

  /**
   * Returns the non-transactional entity managers associated with the current thread.
   *
   * @return the the non-transactional entity manager associated with the current thread
   */
  public static Map<String, EntityManager> getNonTransactionalEntityManagers()
  {
    return nonTransactionalEntityManagers.get();
  }

  @Override
  public void clear()
  {
    getEntityManager(persistenceUnitName).clear();
  }

  @Override
  public void close()
  {
    if (isInTransaction())
    {
      throw new RuntimeException(
          "Cannot close the entity manager associated with an active transaction");

    }

    getEntityManager(persistenceUnitName).close();
  }

  @Override
  public boolean contains(Object entity)
  {
    return getEntityManager(persistenceUnitName).contains(entity);
  }

  @Override
  public <T> EntityGraph<T> createEntityGraph(Class<T> rootType)
  {
    return getEntityManager(persistenceUnitName).createEntityGraph(rootType);
  }

  @Override
  public EntityGraph<?> createEntityGraph(String graphName)
  {
    return getEntityManager(persistenceUnitName).createEntityGraph(graphName);
  }

  @Override
  public Query createNamedQuery(String name)
  {
    return getEntityManager(persistenceUnitName).createNamedQuery(name);
  }

  @Override
  public <T> TypedQuery<T> createNamedQuery(String name, Class<T> resultClass)
  {
    EntityManager entityManager = getEntityManager(persistenceUnitName);

    TypedQuery<T> typedQuery = entityManager.createNamedQuery(name, resultClass);

    return new TypedQueryNonTxInvocationDetacher<>(entityManager, typedQuery);
  }

  @Override
  public StoredProcedureQuery createNamedStoredProcedureQuery(String name)
  {
    EntityManager entityManager = getEntityManager(persistenceUnitName);

    StoredProcedureQuery storedProcedureQuery = entityManager.createNamedStoredProcedureQuery(name);

    return new StoredProcedureQueryNonTxInvocationDetacher(entityManager, storedProcedureQuery);
  }

  @Override
  public Query createNativeQuery(String sqlString)
  {
    return getEntityManager(persistenceUnitName).createNativeQuery(sqlString);
  }

  @SuppressWarnings("rawtypes")
  @Override
  public Query createNativeQuery(String sqlString, Class resultClass)
  {
    return getEntityManager(persistenceUnitName).createNativeQuery(sqlString, resultClass);
  }

  @Override
  public Query createNativeQuery(String sqlString, String resultSetMapping)
  {
    return getEntityManager(persistenceUnitName).createNativeQuery(sqlString, resultSetMapping);
  }

  @Override
  public Query createQuery(CriteriaDelete deleteQuery)
  {
    return getEntityManager(persistenceUnitName).createQuery(deleteQuery);
  }

  @Override
  public <T> TypedQuery<T> createQuery(CriteriaQuery<T> criteriaQuery)
  {
    EntityManager entityManager = getEntityManager(persistenceUnitName);

    TypedQuery<T> typedQuery = entityManager.createQuery(criteriaQuery);

    return new TypedQueryNonTxInvocationDetacher<>(entityManager, typedQuery);
  }

  @Override
  public Query createQuery(CriteriaUpdate updateQuery)
  {
    return getEntityManager(persistenceUnitName).createQuery(updateQuery);
  }

  @Override
  public Query createQuery(String qlString)
  {
    return getEntityManager(persistenceUnitName).createQuery(qlString);
  }

  @Override
  public <T> TypedQuery<T> createQuery(String qlString, Class<T> resultClass)
  {
    EntityManager entityManager = getEntityManager(persistenceUnitName);

    TypedQuery<T> typedQuery = entityManager.createQuery(qlString, resultClass);

    return new TypedQueryNonTxInvocationDetacher<>(entityManager, typedQuery);
  }

  @Override
  public StoredProcedureQuery createStoredProcedureQuery(String procedureName)
  {
    EntityManager entityManager = getEntityManager(persistenceUnitName);

    StoredProcedureQuery storedProcedureQuery = entityManager.createStoredProcedureQuery(
        procedureName);

    return new StoredProcedureQueryNonTxInvocationDetacher(entityManager, storedProcedureQuery);
  }

  @SuppressWarnings("rawtypes")
  @Override
  public StoredProcedureQuery createStoredProcedureQuery(String procedureName,
      Class... resultClasses)
  {
    EntityManager entityManager = getEntityManager(persistenceUnitName);

    StoredProcedureQuery storedProcedureQuery = entityManager.createStoredProcedureQuery(
        procedureName, resultClasses);

    return new StoredProcedureQueryNonTxInvocationDetacher(entityManager, storedProcedureQuery);
  }

  @Override
  public StoredProcedureQuery createStoredProcedureQuery(String procedureName,
      String... resultSetMappings)
  {
    EntityManager entityManager = getEntityManager(persistenceUnitName);

    StoredProcedureQuery storedProcedureQuery = entityManager.createStoredProcedureQuery(
        procedureName, resultSetMappings);

    return new StoredProcedureQueryNonTxInvocationDetacher(entityManager, storedProcedureQuery);
  }

  @Override
  public void detach(Object entity)
  {
    getEntityManager(persistenceUnitName).detach(entity);
  }

  @Override
  public <T> T find(Class<T> entityClass, Object primaryKey)
  {
    return getEntityManager(persistenceUnitName).find(entityClass, primaryKey);
  }

  @Override
  public <T> T find(Class<T> entityClass, Object primaryKey, LockModeType lockMode)
  {
    return getEntityManager(persistenceUnitName).find(entityClass, primaryKey, lockMode);
  }

  @Override
  public <T> T find(Class<T> entityClass, Object primaryKey, Map<String, Object> properties)
  {
    return getEntityManager(persistenceUnitName).find(entityClass, primaryKey, properties);
  }

  @Override
  public <T> T find(Class<T> entityClass, Object primaryKey, LockModeType lockMode, Map<String,
      Object> properties)
  {
    return getEntityManager(persistenceUnitName).find(entityClass, primaryKey, lockMode,
        properties);
  }

  @Override
  public void flush()
  {
    getEntityManager(persistenceUnitName).flush();
  }

  @Override
  public CriteriaBuilder getCriteriaBuilder()
  {
    return getEntityManager(persistenceUnitName).getCriteriaBuilder();
  }

  @Override
  public Object getDelegate()
  {
    return getEntityManager(persistenceUnitName).getDelegate();
  }

  @Override
  public EntityGraph<?> getEntityGraph(String graphName)
  {
    return getEntityManager(persistenceUnitName).getEntityGraph(graphName);
  }

  @Override
  public <T> List<EntityGraph<? super T>> getEntityGraphs(Class<T> entityClass)
  {
    return getEntityManager(persistenceUnitName).getEntityGraphs(entityClass);
  }

  @Override
  public EntityManagerFactory getEntityManagerFactory()
  {
    return getEntityManager(persistenceUnitName).getEntityManagerFactory();
  }

  @Override
  public FlushModeType getFlushMode()
  {
    return getEntityManager(persistenceUnitName).getFlushMode();
  }

  @Override
  public LockModeType getLockMode(Object entity)
  {
    return getEntityManager(persistenceUnitName).getLockMode(entity);
  }

  @Override
  public Metamodel getMetamodel()
  {
    return getEntityManager(persistenceUnitName).getMetamodel();
  }

  @Override
  public Map<String, Object> getProperties()
  {
    return getEntityManager(persistenceUnitName).getProperties();
  }

  @Override
  public <T> T getReference(Class<T> entityClass, Object primaryKey)
  {
    return getEntityManager(persistenceUnitName).getReference(entityClass, primaryKey);
  }

  @Override
  public EntityTransaction getTransaction()
  {
    return getEntityManager(persistenceUnitName).getTransaction();
  }

  @Override
  public boolean isJoinedToTransaction()
  {
    return getEntityManager(persistenceUnitName).isJoinedToTransaction();
  }

  @Override
  public boolean isOpen()
  {
    return getEntityManager(persistenceUnitName).isOpen();
  }

  @Override
  public void joinTransaction()
  {
    getEntityManager(persistenceUnitName).joinTransaction();
  }

  @Override
  public void lock(Object entity, LockModeType lockMode)
  {
    getEntityManager(persistenceUnitName).lock(entity, lockMode);
  }

  @Override
  public void lock(Object entity, LockModeType lockMode, Map<String, Object> properties)
  {
    getEntityManager(persistenceUnitName).lock(entity, lockMode, properties);
  }

  @Override
  public <T> T merge(T entity)
  {
    return getEntityManager(persistenceUnitName).merge(entity);
  }

  @Override
  public void persist(Object entity)
  {
    getEntityManager(persistenceUnitName).persist(entity);
  }

  @Override
  public void refresh(Object entity)
  {
    getEntityManager(persistenceUnitName).refresh(entity);
  }

  @Override
  public void refresh(Object entity, LockModeType lockMode)
  {
    getEntityManager(persistenceUnitName).refresh(entity, lockMode);
  }

  @Override
  public void refresh(Object entity, Map<String, Object> properties)
  {
    getEntityManager(persistenceUnitName).refresh(entity, properties);
  }

  @Override
  public void refresh(Object entity, LockModeType lockMode, Map<String, Object> properties)
  {
    getEntityManager(persistenceUnitName).refresh(entity, lockMode, properties);
  }

  @Override
  public void remove(Object entity)
  {
    getEntityManager(persistenceUnitName).remove(entity);
  }

  @Override
  public void setFlushMode(FlushModeType flushMode)
  {
    getEntityManager(persistenceUnitName).setFlushMode(flushMode);
  }

  @Override
  public void setProperty(String propertyName, Object value)
  {
    getEntityManager(persistenceUnitName).setProperty(propertyName, value);
  }

  @Override
  public <T> T unwrap(Class<T> cls)
  {
    return getEntityManager(persistenceUnitName).unwrap(cls);
  }

  /**
   * Create an entity manager for the persistence unit with the specified name.
   *
   * @param persistenceUnitName the name of the persistence unit
   *
   * @return the entity manager
   */
  private static EntityManager createEntityManager(String persistenceUnitName)
  {
    try
    {
      return getEntityManagerFactory(persistenceUnitName).createEntityManager();
    }
    catch (Throwable e)
    {
      throw new RuntimeException("Failed to create the entity manager for the persistence unit ("
          + persistenceUnitName + ")", e);

    }
  }

  /**
   * Returns the transactional or non-transactional entity manager for the persistence unit with the
   * specified name.
   * <p/>
   * If an existing transactional or non-transactional entity manager cannot be found then a new one
   * will be created.
   *
   * @param persistenceUnitName the name of the persistence unit
   *
   * @return the transactional or non-transactional entity manager for the persistence unit with the
   *         specified name
   */
  private static EntityManager getEntityManager(String persistenceUnitName)
  {
    try
    {
      // Retrieve the existing transaction if one exists
      TransactionManager transactionManager = TransactionManager.getTransactionManager();

      Transaction transaction = transactionManager.getTransaction();

      /*
       * If there is an existing transaction, attempt to retrieve the transactional entity manager
       * for the persistence unit with the specified name. If an existing entity manager cannot be
       * found then create a new entity manager for the persistence context with the specified name
       * and associate it with the existing transaction.
       */
      if ((transaction != null) && (transaction.getStatus() == Status.STATUS_ACTIVE))
      {
        Map<String, EntityManager> entityManagersForTransaction = getEntityManagersForTransaction(
            transaction);

        EntityManager entityManager = entityManagersForTransaction.get(persistenceUnitName);

        if (entityManager == null)
        {
          entityManager = createEntityManager(persistenceUnitName);

          if (!entityManager.isJoinedToTransaction())
          {
            entityManager.joinTransaction();
          }

          transaction.registerSynchronization(new AutoCloseEntityManager(persistenceUnitName,
              entityManager));

          entityManagersForTransaction.put(persistenceUnitName, entityManager);
        }

        return entityManager;
      }

      /*
       * If there is not an existing transaction, attempt to retrieve the non-transactional entity
       * manager for the persistence unit with the specified name. If an existing non-transactional
       * entity manager cannot be found then a new entity manager will be creatd for the persistence
       * context.
       */
      else
      {
        EntityManager entityManager = nonTransactionalEntityManagers.get().get(persistenceUnitName);

        if (entityManager == null)
        {
          entityManager = createEntityManager(persistenceUnitName);

          nonTransactionalEntityManagers.get().put(persistenceUnitName, entityManager);
        }

        return entityManager;
      }
    }
    catch (Throwable e)
    {
      throw new RuntimeException("Failed to retrieve the entity manager for the persistence unit ("
          + persistenceUnitName + ")", e);
    }
  }

  /**
   * Get the entity manager factory for the persistence unit with the specified name.
   *
   * @param persistenceUnitName the name of the persistence unit
   *
   * @return the entity manager factory
   */
  private static EntityManagerFactory getEntityManagerFactory(String persistenceUnitName)
  {
    EntityManagerFactory entityManagerFactory = entityManagerFactories.get(persistenceUnitName);

    if (entityManagerFactory == null)
    {
      try
      {
        Map<String, String> properties = new HashMap<>();

        properties.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        //properties.put("hibernate.transaction.auto_close_session", "true");
        properties.put("hibernate.current_session_context_class", "jta");
        properties.put("hibernate.transaction.jta.platform",
            "guru.mmp.application.test.ApplicationClassRunnerJtaPlatform");

        entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName,
            properties);

        entityManagerFactories.put(persistenceUnitName, entityManagerFactory);
      }
      catch (Throwable e)
      {
        throw new RuntimeException(
            "Failed to create the entity manager factory for the persistence unit ("
            + persistenceUnitName + ")", e);
      }
    }

    return entityManagerFactory;
  }

  /**
   * Get the entity managers for the persistence units associated with the* specified transaction.
   *
   * @param transaction the transaction
   *
   * @return the entity managers for the persistence units associated with the specified transaction
   */
  private static Map<String, EntityManager> getEntityManagersForTransaction(Transaction transaction)
  {
    Map<String, EntityManager> entityManagersForTransaction = transactionalEntityManagers.get().get(
        transaction);

    if (entityManagersForTransaction == null)
    {
      entityManagersForTransaction = new HashMap<>();

      transactionalEntityManagers.get().put(transaction, entityManagersForTransaction);
    }

    return entityManagersForTransaction;
  }

  /**
   * Is there an active transaction?
   *
   * @return <code>true</code> if there is an active transaction or <code>false</code> otherwise
   */
  private static boolean isInTransaction()
  {
    try
    {
      TransactionManager transactionManager = TransactionManager.getTransactionManager();

      Transaction transaction = transactionManager.getTransaction();

      return (transaction != null) && (transaction.getStatus() == Status.STATUS_ACTIVE);
    }
    catch (Throwable e)
    {
      throw new RuntimeException("Failed to check whether there is an active transaction", e);
    }
  }

  private static class AutoCloseEntityManager
    implements Synchronization
  {
    private String persistenceUnitName;
    private EntityManager entityManager;

    AutoCloseEntityManager(String persistenceUnitName, EntityManager entityManager)
    {
      this.persistenceUnitName = persistenceUnitName;

      this.entityManager = entityManager;
    }

    @Override
    public void afterCompletion(int i)
    {
      try
      {
        entityManager.close();
      }
      catch (Throwable e)
      {
        throw new RuntimeException("Failed to close the entity manager for the persistence unit ("
            + persistenceUnitName + ")", e);
      }
    }

    @Override
    public void beforeCompletion() {}
  }


  private class StoredProcedureQueryNonTxInvocationDetacher
    implements StoredProcedureQuery
  {
    private final EntityManager underlyingEntityManager;
    private final StoredProcedureQuery underlyingStoredProcedureQuery;

    StoredProcedureQueryNonTxInvocationDetacher(EntityManager underlyingEntityManager,
        StoredProcedureQuery underlyingStoredProcedureQuery)
    {
      this.underlyingEntityManager = underlyingEntityManager;
      this.underlyingStoredProcedureQuery = underlyingStoredProcedureQuery;
    }

    @Override
    public boolean execute()
    {
      return underlyingStoredProcedureQuery.execute();
    }

    @Override
    public int executeUpdate()
    {
      return underlyingStoredProcedureQuery.executeUpdate();
    }

    @Override
    public int getFirstResult()
    {
      return underlyingStoredProcedureQuery.getFirstResult();
    }

    @Override
    public FlushModeType getFlushMode()
    {
      return underlyingStoredProcedureQuery.getFlushMode();
    }

    @Override
    public Map<String, Object> getHints()
    {
      return underlyingStoredProcedureQuery.getHints();
    }

    @Override
    public LockModeType getLockMode()
    {
      return underlyingStoredProcedureQuery.getLockMode();
    }

    @Override
    public int getMaxResults()
    {
      return underlyingStoredProcedureQuery.getMaxResults();
    }

    @Override
    public Object getOutputParameterValue(int position)
    {
      return underlyingStoredProcedureQuery.getOutputParameterValue(position);
    }

    @Override
    public Object getOutputParameterValue(String parameterName)
    {
      return underlyingStoredProcedureQuery.getOutputParameterValue(parameterName);
    }

    @Override
    public Parameter<?> getParameter(int position)
    {
      return underlyingStoredProcedureQuery.getParameter(position);
    }

    @Override
    public Parameter<?> getParameter(String name)
    {
      return underlyingStoredProcedureQuery.getParameter(name);
    }

    @Override
    public <T> Parameter<T> getParameter(int position, Class<T> type)
    {
      return underlyingStoredProcedureQuery.getParameter(position, type);
    }

    @Override
    public <T> Parameter<T> getParameter(String name, Class<T> type)
    {
      return underlyingStoredProcedureQuery.getParameter(name, type);
    }

    @Override
    public Object getParameterValue(int position)
    {
      return underlyingStoredProcedureQuery.getParameterValue(position);
    }

    @Override
    public <T> T getParameterValue(Parameter<T> param)
    {
      return underlyingStoredProcedureQuery.getParameterValue(param);
    }

    @Override
    public Object getParameterValue(String name)
    {
      return underlyingStoredProcedureQuery.getParameterValue(name);
    }

    @Override
    public Set<Parameter<?>> getParameters()
    {
      return underlyingStoredProcedureQuery.getParameters();
    }

    @Override
    public List getResultList()
    {
      try
      {
        return underlyingStoredProcedureQuery.getResultList();
      }
      finally
      {
        underlyingEntityManager.clear();
      }
    }

    @Override
    public Object getSingleResult()
    {
      try
      {
        return underlyingStoredProcedureQuery.getSingleResult();
      }
      finally
      {
        underlyingEntityManager.clear();
      }
    }

    @Override
    public int getUpdateCount()
    {
      return underlyingStoredProcedureQuery.getUpdateCount();
    }

    @Override
    public boolean hasMoreResults()
    {
      return underlyingStoredProcedureQuery.hasMoreResults();
    }

    @Override
    public boolean isBound(Parameter<?> param)
    {
      return underlyingStoredProcedureQuery.isBound(param);
    }

    @Override
    public StoredProcedureQuery registerStoredProcedureParameter(int position, Class type,
        ParameterMode mode)
    {
      return underlyingStoredProcedureQuery.registerStoredProcedureParameter(position, type, mode);
    }

    @Override
    public StoredProcedureQuery registerStoredProcedureParameter(String parameterName, Class type,
        ParameterMode mode)
    {
      return underlyingStoredProcedureQuery.registerStoredProcedureParameter(parameterName, type,
          mode);
    }

    @Override
    public Query setFirstResult(int startPosition)
    {
      return underlyingStoredProcedureQuery.setFirstResult(startPosition);
    }

    @Override
    public StoredProcedureQuery setFlushMode(FlushModeType flushMode)
    {
      return underlyingStoredProcedureQuery.setFlushMode(flushMode);
    }

    @Override
    public StoredProcedureQuery setHint(String hintName, Object value)
    {
      return underlyingStoredProcedureQuery.setHint(hintName, value);
    }

    @Override
    public Query setLockMode(LockModeType lockMode)
    {
      return underlyingStoredProcedureQuery.setLockMode(lockMode);
    }

    @Override
    public Query setMaxResults(int maxResult)
    {
      return underlyingStoredProcedureQuery.setMaxResults(maxResult);
    }

    @Override
    public StoredProcedureQuery setParameter(int position, Object value)
    {
      return underlyingStoredProcedureQuery.setParameter(position, value);
    }

    @Override
    public <T> StoredProcedureQuery setParameter(Parameter<T> param, T value)
    {
      return underlyingStoredProcedureQuery.setParameter(param, value);
    }

    @Override
    public StoredProcedureQuery setParameter(String name, Object value)
    {
      return underlyingStoredProcedureQuery.setParameter(name, value);
    }

    @Override
    public StoredProcedureQuery setParameter(int position, Calendar value,
        TemporalType temporalType)
    {
      return underlyingStoredProcedureQuery.setParameter(position, value, temporalType);
    }

    @Override
    public StoredProcedureQuery setParameter(int position, Date value, TemporalType temporalType)
    {
      return underlyingStoredProcedureQuery.setParameter(position, value, temporalType);
    }

    @Override
    public StoredProcedureQuery setParameter(Parameter<Calendar> param, Calendar value,
        TemporalType temporalType)
    {
      return underlyingStoredProcedureQuery.setParameter(param, value, temporalType);
    }

    @Override
    public StoredProcedureQuery setParameter(Parameter<Date> param, Date value,
        TemporalType temporalType)
    {
      return underlyingStoredProcedureQuery.setParameter(param, value, temporalType);
    }

    @Override
    public StoredProcedureQuery setParameter(String name, Calendar value, TemporalType temporalType)
    {
      return underlyingStoredProcedureQuery.setParameter(name, value, temporalType);
    }

    @Override
    public StoredProcedureQuery setParameter(String name, Date value, TemporalType temporalType)
    {
      return underlyingStoredProcedureQuery.setParameter(name, value, temporalType);
    }

    @Override
    public <T> T unwrap(Class<T> cls)
    {
      return underlyingStoredProcedureQuery.unwrap(cls);
    }
  }


  private class TypedQueryNonTxInvocationDetacher<X>
    implements TypedQuery<X>
  {
    private final TypedQuery<X> underlyingQuery;
    private final EntityManager underlyingEntityManager;

    TypedQueryNonTxInvocationDetacher(EntityManager underlyingEntityManager,
        TypedQuery<X> underlyingQuery)
    {
      this.underlyingQuery = underlyingQuery;
      this.underlyingEntityManager = underlyingEntityManager;
    }

    @Override
    public int executeUpdate()
    {
      return underlyingQuery.executeUpdate();
    }

    @Override
    public int getFirstResult()
    {
      return underlyingQuery.getFirstResult();
    }

    @Override
    public FlushModeType getFlushMode()
    {
      return underlyingQuery.getFlushMode();
    }

    @Override
    public Map<String, Object> getHints()
    {
      return underlyingQuery.getHints();
    }

    @Override
    public LockModeType getLockMode()
    {
      return underlyingQuery.getLockMode();
    }

    @Override
    public int getMaxResults()
    {
      return underlyingQuery.getMaxResults();
    }

    @Override
    public Parameter<?> getParameter(int position)
    {
      return underlyingQuery.getParameter(position);
    }

    @Override
    public Parameter<?> getParameter(String name)
    {
      return underlyingQuery.getParameter(name);
    }

    @Override
    public <T> Parameter<T> getParameter(int position, Class<T> type)
    {
      return underlyingQuery.getParameter(position, type);
    }

    @Override
    public <T> Parameter<T> getParameter(String name, Class<T> type)
    {
      return underlyingQuery.getParameter(name, type);
    }

    @Override
    public Object getParameterValue(int position)
    {
      return underlyingQuery.getParameterValue(position);
    }

    @Override
    public <T> T getParameterValue(Parameter<T> param)
    {
      return underlyingQuery.getParameterValue(param);
    }

    @Override
    public Object getParameterValue(String name)
    {
      return underlyingQuery.getParameterValue(name);
    }

    @Override
    public Set<Parameter<?>> getParameters()
    {
      return underlyingQuery.getParameters();
    }

    @Override
    public List<X> getResultList()
    {
      List<X> result = underlyingQuery.getResultList();

      /*
       * The purpose of this wrapper class is so that we can detach the returned entities from this
       * method. Call EntityManager.clear will accomplish that.
       */
      underlyingEntityManager.clear();

      return result;
    }

    @Override
    public X getSingleResult()
    {
      X result = underlyingQuery.getSingleResult();

      /*
       * The purpose of this wrapper class is so that we can detach the returned entities from this
       * method. Call EntityManager.clear will accomplish that.
       */
      underlyingEntityManager.clear();

      return result;
    }

    @Override
    public boolean isBound(Parameter<?> param)
    {
      return underlyingQuery.isBound(param);
    }

    @Override
    public TypedQuery<X> setFirstResult(int startPosition)
    {
      underlyingQuery.setFirstResult(startPosition);

      return this;
    }

    @Override
    public TypedQuery<X> setFlushMode(FlushModeType flushMode)
    {
      underlyingQuery.setFlushMode(flushMode);

      return this;
    }

    @Override
    public TypedQuery<X> setHint(String hintName, Object value)
    {
      underlyingQuery.setHint(hintName, value);

      return this;
    }

    @Override
    public TypedQuery<X> setLockMode(LockModeType lockMode)
    {
      underlyingQuery.setLockMode(lockMode);

      return this;
    }

    @Override
    public TypedQuery<X> setMaxResults(int maxResult)
    {
      underlyingQuery.setMaxResults(maxResult);

      return this;
    }

    @Override
    public TypedQuery<X> setParameter(int position, Object value)
    {
      underlyingQuery.setParameter(position, value);

      return this;
    }

    @Override
    public <T> TypedQuery<X> setParameter(Parameter<T> param, T value)
    {
      underlyingQuery.setParameter(param, value);

      return this;
    }

    @Override
    public TypedQuery<X> setParameter(String name, Object value)
    {
      underlyingQuery.setParameter(name, value);

      return this;
    }

    @Override
    public TypedQuery<X> setParameter(int position, Calendar value, TemporalType temporalType)
    {
      underlyingQuery.setParameter(position, value, temporalType);

      return this;
    }

    @Override
    public TypedQuery<X> setParameter(int position, Date value, TemporalType temporalType)
    {
      underlyingQuery.setParameter(position, value, temporalType);

      return this;
    }

    @Override
    public TypedQuery<X> setParameter(Parameter<Calendar> param, Calendar value,
        TemporalType temporalType)
    {
      underlyingQuery.setParameter(param, value, temporalType);

      return this;
    }

    @Override
    public TypedQuery<X> setParameter(Parameter<Date> param, Date value, TemporalType temporalType)
    {
      underlyingQuery.setParameter(param, value, temporalType);

      return this;
    }

    @Override
    public TypedQuery<X> setParameter(String name, Calendar value, TemporalType temporalType)
    {
      underlyingQuery.setParameter(name, value, temporalType);

      return this;
    }

    @Override
    public TypedQuery<X> setParameter(String name, Date value, TemporalType temporalType)
    {
      underlyingQuery.setParameter(name, value, temporalType);

      return this;
    }

    @Override
    public <T> T unwrap(Class<T> cls)
    {
      return underlyingQuery.unwrap(cls);
    }
  }
}

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

package guru.mmp.common.test;

//~--- JDK imports ------------------------------------------------------------

import org.hibernate.Session;
import org.hibernate.jpa.HibernateEntityManager;

import java.lang.reflect.Field;

import java.util.*;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;

/**
 * The <code>ApplicationJUnit4EntityManagerInjector</code> class implements the Weld extension,
 * which processes <code>PersistenceContext</code> annotations on CDI beans and injects
 * the appropriate <code>EntityManager</code> instances into these beans.
 *
 * @author Marcus Portmann
 */
public class ApplicationJUnit4EntityManagerInjector
  implements Extension
{
  <T> void processInjectionTarget(@Observes ProcessInjectionTarget<T> processInjectionTarget)
  {
    AnnotatedType<T> annotatedType = processInjectionTarget.getAnnotatedType();

    boolean hasPersistenceContext = false;

    for (Field field : annotatedType.getJavaClass().getDeclaredFields())
    {
      if (field.isAnnotationPresent(PersistenceContext.class))
      {
        hasPersistenceContext = true;

        break;
      }
    }

    if (!hasPersistenceContext)
    {
      return;
    }

    final InjectionTarget<T> injectionTarget = processInjectionTarget.getInjectionTarget();

    InjectionTarget<T> wrapper = new InjectionTarget<T>()
    {
      @Override
      public void postConstruct(T instance)
      {
        injectionTarget.postConstruct(instance);
      }

      @Override
      public void inject(T instance, CreationalContext<T> context)
      {
        for (Field field : annotatedType.getJavaClass().getDeclaredFields())
        {
          PersistenceContext persistenceContext = field.getAnnotation(PersistenceContext.class);

          if (persistenceContext != null)
          {
            try
            {
              Map<String, String> properties = new HashMap<>();

              properties.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
              properties.put("hibernate.transaction.auto_close_session", "true");
              properties.put("hibernate.current_session_context_class",
                  "jta");
              properties.put("hibernate.transaction.jta.platform",
                  "guru.mmp.common.test.ApplicationJUnit4JtaPlatform");

              EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(
                  persistenceContext.unitName(), properties);

              EntityManager entityManager = entityManagerFactory.createEntityManager();

            HibernateEntityManager hibernateEntityManager = entityManager.unwrap(
                HibernateEntityManager.class);

            Session session = hibernateEntityManager.getSession();

              field.setAccessible(true);
              field.set(instance, entityManager);

              EntityManagerTracker.getActiveEntityManagers().add(entityManager);
            }
            catch (Throwable e)
            {
              throw new RuntimeException("Failed to inject the PersistenceContext ("
                  + persistenceContext.unitName() + ")", e);
            }
          }
        }

        injectionTarget.inject(instance, context);
      }

      @Override
      public void dispose(T instance)
      {
        injectionTarget.dispose(instance);
      }

      @Override
      public T produce(CreationalContext<T> context)
      {
        return injectionTarget.produce(context);
      }

      @Override
      public void preDestroy(T instance)
      {
        injectionTarget.preDestroy(instance);
      }

      @Override
      public Set<InjectionPoint> getInjectionPoints()
      {
        return injectionTarget.getInjectionPoints();
      }

    };

    processInjectionTarget.setInjectionTarget(wrapper);
  }
}

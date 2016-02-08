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

import guru.mmp.common.cdi.AnnotatedTypeWrapper;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.*;
import javax.persistence.PersistenceContext;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Set;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>EntityManagerInjectorExtension</code> class.
 *
 * @author Marcus Portmann
 */
public class EntityManagerInjectorExtension
  implements Extension
{
  /**
   * Process the annotated type.
   *
   * @param processAnnotatedType the process annotated type event
   *
   * @param <T>
   */
  public <T> void processAnnotatedType(@Observes ProcessAnnotatedType<T> processAnnotatedType)
  {
    AnnotatedType<T> annotatedType = processAnnotatedType.getAnnotatedType();

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

    Annotation annotation = () -> NonTransactionalEntityManagerCleanup.class;

    AnnotatedTypeWrapper<T> wrapper = new AnnotatedTypeWrapper<T>(annotatedType,
        annotatedType.getAnnotations());
    wrapper.addAnnotation(annotation);

    processAnnotatedType.setAnnotatedType(wrapper);
  }

  /**
   * Process the injection target.
   *
   * @param processInjectionTarget the process injection target event
   *
   * @param <T>
   */
  public <T> void processInjectionTarget(@Observes ProcessInjectionTarget<T> processInjectionTarget)
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
              EntityManagerWrapper entityManager = new EntityManagerWrapper(
                  persistenceContext.unitName());

              field.setAccessible(true);
              field.set(instance, entityManager);
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

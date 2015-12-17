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

package guru.mmp.application.cdi;

//~--- non-JDK imports --------------------------------------------------------

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//~--- JDK imports ------------------------------------------------------------

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.ejb.EJB;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.InjectionTarget;

import javax.inject.Inject;

import javax.naming.InitialContext;

/**
 * The <code>CDIUtil</code> class is a utility class which provides utility methods related to
 * container-based dependency injection (CDI).
 *
 * @author Marcus Portmann
 */
public class CDIUtil
{
  private static BeanManager cachedBeanManager;
  private static CreationalContext<Object> cachedCreationalContext;

  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(CDIUtil.class);

  /* The cached injection targets. */
  private static final Map<Class<?>, InjectionTarget<?>> cachedInjectionTargets =
    new ConcurrentHashMap<>();

  /* The cached list of non-injectable classes. */
  private static final List<Class<?>> nonInjectableClasses = new ArrayList<>();

  /* The cached list of injectable classes. */
  private static final List<Class<?>> injectableClasses = new ArrayList<>();

  /**
   * Perform container-based dependency injection on the target.
   *
   * @param target the object to inject
   *
   * @throws CDIException
   */
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public static void inject(Object target)
    throws CDIException
  {
    if (cachedBeanManager == null)
    {
      // Lookup the CDI injector
      try
      {
        cachedBeanManager = InitialContext.doLookup("java:comp/BeanManager");
      }
      catch (Throwable ignored) {}

      if (cachedBeanManager == null)
      {
        throw new CDIException("Failed to inject the object of type (" + target.getClass() + "):"
          + " Failed to retrieve the CDI BeanManager from JNDI");
      }
    }


  }

  /**
   * Perform container-based dependency injection on the target using the spe.
   *
   * @param target the object to inject
   *
   * @throws CDIException
   */
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public static void inject(Object target)
    throws CDIException
  {
    if (cachedBeanManager == null)
    {
      // Lookup the CDI injector
      try
      {
        cachedBeanManager = InitialContext.doLookup("java:comp/BeanManager");
      }
      catch (Throwable ignored) {}

      if (cachedBeanManager == null)
      {
        throw new CDIException("Failed to inject the object of type (" + target.getClass() + "):"
            + " Failed to retrieve the CDI BeanManager from JNDI");
      }
    }

    if (cachedCreationalContext == null)
    {
      try
      {
        cachedCreationalContext = cachedBeanManager.createCreationalContext(null);
      }
      catch (Throwable e)
      {
        throw new CDIException("Failed to inject the object of type (" + target.getClass() + "):"
            + " Failed to create the CDI CreationalContext", e);
      }
    }

    try
    {
      Class<?> clazz = target.getClass();

      // If we have already confirmed that object's class is not injectable then stop here
      if (nonInjectableClasses.contains(clazz))
      {
        return;
      }

      // If we have not already confirmed that object's class is injectable then check that now
      else if (!injectableClasses.contains(clazz))
      {
        synchronized (injectableClasses)
        {
          if (!isInjectable(target))
          {
            if (logger.isDebugEnabled())
            {
              logger.info("The class (" + clazz.getName()
                  + ") is not injectable will be ignored in future");
            }

            if (!nonInjectableClasses.contains(clazz))
            {
              nonInjectableClasses.add(clazz);
            }

            return;
          }

          if (logger.isDebugEnabled())
          {
            logger.debug("The class (" + clazz.getName()
                + ") is injectable and will be injected in future");
          }

          if (!injectableClasses.contains(clazz))
          {
            injectableClasses.add(clazz);
          }
        }
      }

      /*
       * If we get here we have already confirmed or just confirmed that the object's class is
       * injectable so inject it.
       */
      InjectionTarget<?> injectionTarget;

      if (cachedInjectionTargets.containsKey(clazz))
      {
        injectionTarget = cachedInjectionTargets.get(clazz);
      }
      else
      {
        AnnotatedType<?> annotatedType = cachedBeanManager.createAnnotatedType(clazz);

        injectionTarget = cachedBeanManager.createInjectionTarget(annotatedType);

        cachedInjectionTargets.put(clazz, injectionTarget);
      }

      if (injectionTarget != null)
      {
        ((InjectionTarget) injectionTarget).inject(target, cachedCreationalContext);
      }
    }
    catch (Throwable e)
    {
      throw new CDIException("Failed to inject the object of type (" + target.getClass() + ")", e);
    }
  }

  /**
   * Returns <code>true</code> if the object is injectable or <code>false</code> otherwise.
   *
   * @param object the object to check
   *
   * @return <code>true</code> if the object is injectable or <code>false</code> otherwise
   */
  public static boolean isInjectable(Object object)
  {
    Class<?> current = object.getClass();

    do
    {
      Field[] currentFields = current.getDeclaredFields();

      for (Field field : currentFields)
      {
        if (!Modifier.isStatic(field.getModifiers()))
        {
          Inject injectAnnotation = field.getAnnotation(Inject.class);

          if (injectAnnotation != null)
          {
            return true;
          }

          EJB ejbAnnotation = field.getAnnotation(EJB.class);

          if (ejbAnnotation != null)
          {
            return true;
          }
        }
      }

      current = current.getSuperclass();
    }

    // Do a null check in case Object isn't in the current classloader.
    while ((current != null) && (current != Object.class));

    return false;
  }
}

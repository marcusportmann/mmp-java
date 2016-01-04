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

package guru.mmp.application.web;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.web.cdi.CDIProxyTargetLocator;
import org.apache.wicket.Component;
import org.apache.wicket.application.IComponentInstantiationListener;
import org.apache.wicket.proxy.LazyInitProxyFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>WebApplicationInjector</code> class provides CDI-based injection for a Wicket
 * web application.
 *
 * @author Marcus Portmann
 */
public class WebApplicationInjector
  implements IComponentInstantiationListener
{
  /* Logger */
  @SuppressWarnings("unused")
  private static final Logger logger = LoggerFactory.getLogger(WebApplicationInjector.class);

  /* Injectable classes. */
  private Map<Class<?>, List<InjectedField>> injectableClasses;

  /* Non-injectable classes. */
  private List<Class<?>> nonInjectableClasses;

  /**
   * Constructs a new <code>WebApplicationInjector</code>.
   */
  public WebApplicationInjector()
  {
    this.nonInjectableClasses = Collections.synchronizedList(new ArrayList<>());
    this.injectableClasses = new ConcurrentHashMap<>();
  }

  /**
   * Perform dependency injection on the object.
   *
   * @param object the object to perform dependency injection on
   */
  public void inject(Object object)
  {
    Class<?> clazz = object.getClass();
    List<InjectedField> injectedFields;

    if (nonInjectableClasses.contains(clazz))
    {
      return;
    }
    else if (injectableClasses.containsKey(clazz))
    {
      injectedFields = injectableClasses.get(clazz);
    }
    else
    {
      injectedFields = new ArrayList<>();

      Class<?> current = clazz;

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
              // Retrieve all qualifier annotations
              List<Annotation> annotations = new ArrayList<>(Arrays.asList(field.getAnnotations()));

              annotations.remove(injectAnnotation);

              if (!field.isAccessible())
              {
                field.setAccessible(true);
              }

              injectedFields.add(new InjectedField(field, field.getType(),
                  annotations.toArray(new Annotation[annotations.size()])));

//            logger.info("Found injected field (" + field.getName() + ") with type ("
//              + field.getType().getName() + ") on class (" + object.getClass() + ")");
            }
          }
        }

        current = current.getSuperclass();
      }
      while ((current != null) && (current != Object.class));

      // Cache the list of injected fields for this class
      if (injectedFields.size() == 0)
      {
        nonInjectableClasses.add(object.getClass());

        return;
      }
      else
      {
        injectableClasses.put(object.getClass(), injectedFields);
      }
    }

    for (InjectedField injectedField : injectedFields)
    {
      try
      {
//      logger.info("Injecting field (" + injectedField.field.getName() + ") with type ("
//          + injectedField.type.getName() + ") on class (" + object.getClass() + ")");

        // TODO: Check whether we can cache and re-use the proxies for singletons
        Object beanProxy = LazyInitProxyFactory.createProxy(injectedField.type,
          new CDIProxyTargetLocator(injectedField.type, injectedField.annotations));

        injectedField.field.set(object, beanProxy);
      }
      catch (Throwable e)
      {
        throw new WebApplicationException("Failed to inject the field ("
            + injectedField.field.getName() + ") for the object with class ("
            + object.getClass().getName() + ")", e);
      }
    }

  }

  /**
   * Perform dependency injection on the component.
   *
   * @param component the component to perform dependency injection on
   */
  public void onInstantiation(Component component)
  {
    inject(component);
  }

  /**
   * The <code>InjectedField</code> holds the information for an injected field.
   */
  private class InjectedField
  {
    /** The qualifier annotations. */
    public Annotation[] annotations;

    /** The field. */
    public Field field;

    /** The type of the injected field. */
    public Class<?> type;

    /**
     * Constructs a new <code>InjectedField</code>.
     *
     * @param field       the field
     * @param type        the type of the injected field
     * @param annotations the qualifier annotations
     */
    public InjectedField(Field field, Class<?> type, Annotation[] annotations)
    {
      this.field = field;
      this.type = type;
      this.annotations = annotations;
    }
  }
}



//EJB ejbAnnotation = field.getAnnotation(EJB.class);
//
//if (ejbAnnotation != null)
//{
//foundEjbAnnotation = true;
//
//// Retrieve all qualifier annotations
//List<Annotation> annotations =
//  new ArrayList<Annotation>(Arrays.asList(field.getAnnotations()));
//
//annotations.remove(ejbAnnotation);
//
//try
//{
//  Object beanProxy = LazyInitProxyFactory.createProxy(field.getType(),
//    new CDIProxyTargetLocator(field.getType(),
//      annotations.toArray(new Annotation[annotations.size()]), true));
//
//  if (!field.isAccessible())
//  {
//    field.setAccessible(true);
//  }
//
//  field.set(object, beanProxy);
//}
//catch (IllegalAccessException e)
//{
//  throw new WebApplicationException("Failed to inject the EJB field ("
//      + field.getName() + ") associated with the class ("
//      + current.getClass().getName() + ") for the object with class ("
//      + object.getClass().getName() + ")", e);
//}
//}
//

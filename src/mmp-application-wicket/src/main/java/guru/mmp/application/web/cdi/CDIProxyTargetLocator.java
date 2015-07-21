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

package guru.mmp.application.web.cdi;

//~--- non-JDK imports --------------------------------------------------------

import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.proxy.IProxyTargetLocator;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.naming.InitialContext;
import java.lang.annotation.Annotation;
import java.util.Set;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>CDIProxyTargetLocator</code> class provides a <code>IProxyTargetLocator</code>
 * implementation that resolves Wicket proxy targets using the CDI bean manager.
 *
 * @author Marcus Portmann
 */
public class CDIProxyTargetLocator
  implements IProxyTargetLocator
{
  private static final long serialVersionUID = 1000000;

  /**
   * The qualifiers that should be applied when resolving the bean using the CDI bean manager.
   */
  private Annotation[] qualifiers;

  /**
   * The bean type.
   */
  private Class<?> type;

  /**
   * Constructs a new <code>CDIProxyTargetLocator</code>.
   *
   * @param type       the bean type
   * @param qualifiers the qualifiers that should be applied when resolving the bean using the CDI
   *                   bean manager
   */
  public CDIProxyTargetLocator(final Class<?> type, final Annotation[] qualifiers)
  {
    this.type = type;
    this.qualifiers = qualifiers;
  }

  /**
   * Returns the object that will be used as target object for a lazily initialised proxy.
   *
   * @return the object that will be used as target object for a lazily initialised proxy
   */
  public Object locateProxyTarget()
  {
    BeanManager beanManager;

    try
    {
      beanManager = InitialContext.doLookup("java:comp/BeanManager");
    }
    catch (Throwable e)
    {
      throw new WicketRuntimeException("Failed to locate the proxy target:"
          + " Failed to resolve the CDI bean (" + type.getName() + ") with qualifiers ("
          + getQualifiersAsString() + "): Failed to retrieve the CDI BeanManager from JNDI", e);
    }

    if (beanManager == null)
    {
      throw new WicketRuntimeException("Failed to locate the proxy target:"
          + " Failed to resolve the CDI bean (" + type.getName() + ") with qualifiers ("
          + getQualifiersAsString() + "): Failed to retrieve the CDI BeanManager from JNDI");
    }

    CreationalContext<Object> creationalContext;

    try
    {
      creationalContext = beanManager.createCreationalContext(null);
    }
    catch (Throwable e)
    {
      throw new WicketRuntimeException("Failed to locate the proxy target:"
          + " Failed to create the CDI CreationalContext", e);
    }

    Set<Bean<?>> beans = beanManager.getBeans(type, qualifiers);

    if (beans.size() == 1)
    {
      return beanManager.getReference(beans.iterator().next(), type, creationalContext);
    }
    else if (beans.size() == 0)
    {
      throw new WicketRuntimeException("Failed to locate the proxy target:"
          + " Failed to resolve the CDI bean (" + type.getName() + ") with qualifiers ("
          + getQualifiersAsString() + "): No matching bean found");
    }

//  for (Bean<?> bean : beans)
//  {
//    System.err.println("  --------> bean.getName() = " + bean.getName());
//    System.err.println("  --------> getClass() = " + bean.getClass());
//    System.err.println("  --------> getBeanClass() = " + bean.getBeanClass());
//    
//    for (java.lang.reflect.Type type : bean.getTypes())
//    {
//      System.err.println("   --------> " + type.toString());
//    }
//    
//    
//    for (Class<? extends Annotation> annotation : bean.getStereotypes())
//    {
//      System.err.println("   --------> " + annotation.getName());
//    }
//  }

    StringBuilder buffer = new StringBuilder();

    for (Bean<?> bean : beans)
    {
      if (buffer.length() > 0)
      {
        buffer.append(", ");
      }

      buffer.append(bean.toString());
    }

    throw new WicketRuntimeException("Failed to locate the proxy target:"
        + " Failed to resolve the CDI bean (" + type.getName() + ") with qualifiers ("
        + getQualifiersAsString() + "): Unable to determine which bean to use from ["
        + buffer.toString() + "]");
  }

  private String getQualifiersAsString()
  {
    StringBuilder buffer = new StringBuilder();

    buffer.append("[");

    for (int i = 0; i < qualifiers.length; i++)
    {
      if (i > 0)
      {
        buffer.append(" ");
      }

      buffer.append("@").append(qualifiers[i].annotationType().getName());
    }

    buffer.append("]");

    return buffer.toString();
  }
}

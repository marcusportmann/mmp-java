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

package guru.mmp.common.tomcat;

import com.atomikos.beans.PropertyUtils;
import com.atomikos.jdbc.AtomikosDataSourceBean;
import org.apache.naming.ResourceRef;
import org.apache.naming.factory.Constants;

import javax.naming.*;
import javax.naming.spi.ObjectFactory;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The <code>BeanFactory</code> class provides a custom implementation of a bean factory using the
 * Atomikos Transaction Library.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public class BeanFactory
  implements ObjectFactory
{
  Logger logger = Logger.getLogger(BeanFactory.class.getName());

  /**
   * Creates an object using the location or reference information specified.
   * <p/>
   * Special requirements of this object are supplied using <code>environment</code>. An example of
   * such an environment property is user identity information.
   * <p/>
   * <code>NamingManager.getObjectInstance()</code> successively loads in object factories and
   * invokes this method on them until one produces a non-null answer. When an exception is thrown
   * by an object factory, the exception is passed on to the caller of
   * <code>NamingManager.getObjectInstance()</code> (and no search is made for other factories that
   * may produce a non-null answer). An object factory should only throw an exception if it is sure
   * that it is the only intended factory and that no other object factories should be tried. If
   * this factory cannot create an object using the arguments supplied, it should return null.
   *
   * @param obj         the possibly <code>null</code> object containing location or reference
   *                    information that can be used in creating an object
   * @param name        the name of this object relative to <code>nameCtx</code>, or
   *                    <code>null</code> if no name is specified
   * @param nameCtx     the context relative to which the <code>name</code> parameter is specified,
   *                    or <code>null</code> if <code>name</code> is relative to the default
   *                    initial context.
   * @param environment possibly <code>null</code> environment that is used in creating the object
   *
   * @return the object created; <code>null</code> if an object cannot be created
   *
   * @throws NamingException
   */
  @SuppressWarnings({"rawtypes"})
  public Object getObjectInstance(Object obj, Name name, Context nameCtx, Hashtable environment)
    throws NamingException
  {
    if (obj instanceof ResourceRef)
    {
      try
      {
        Reference ref = (Reference) obj;
        String beanClassName = ref.getClassName();
        Class beanClass = null;
        ClassLoader threadClassLoader = Thread.currentThread().getContextClassLoader();

        if (threadClassLoader != null)
        {
          try
          {
            beanClass = threadClassLoader.loadClass(beanClassName);
          }
          catch (ClassNotFoundException ignored)
          {
          }
        }
        else
        {
          try
          {
            beanClass = Class.forName(beanClassName);
          }
          catch (ClassNotFoundException e)
          {
            e.printStackTrace();
          }
        }

        if (beanClass == null)
        {
          throw new NamingException(
            "Failed to find the bean class (" + beanClassName + ") while creating the object " +
              "instance (" + name.toString() + ")");
        }

        if (!AtomikosDataSourceBean.class.isAssignableFrom(beanClass))
        {
          throw new NamingException(
            "Failed to create the object instance (" + name.toString() + ") since the class (" +
              beanClassName + ") is not an AtomikosDataSourceBean");
        }

        if (logger.isLoggable(Level.FINE))
        {
          logger.fine("Instantiating an AtomikosDataSourceBean of class " + beanClass.getName());
        }

        AtomikosDataSourceBean bean = (AtomikosDataSourceBean) beanClass.newInstance();
        int i = 0;
        Enumeration en = ref.getAll();

        while (en.hasMoreElements())
        {
          RefAddr ra = (RefAddr) en.nextElement();
          String propName = ra.getType();

          if (propName.equals(Constants.FACTORY) || propName.equals("scope") || propName.equals(
            "auth"))
          {
            continue;
          }

          String value = (String) ra.getContent();

          if (logger.isLoggable(Level.FINE))
          {
            logger.fine("Setting the property '" + propName + "' to '" + value + "' for the " +
              "AtomikosDataSourceBean (" + beanClass.getName() + ")");
          }

          if (propName.equals("xaProperties"))
          {
            Properties xaProperties = new Properties();
            StringTokenizer st = new StringTokenizer(value, ";");

            while (st.hasMoreTokens())
            {
              String token = st.nextToken();

              if (token.indexOf("=") > 0)
              {
                String propertyName = token.substring(0, token.indexOf("="));
                String propertyValue = token.substring(token.indexOf("=") + 1);

                xaProperties.put(propertyName, propertyValue);

                if (logger.isLoggable(Level.FINE))
                {
                  logger.fine(
                    "Found xaProperty '" + propertyName + "' with value '" + propertyValue + "'" +
                      " for the AtomikosDataSourceBean (" + beanClass.getName() + ")");
                }
              }
            }

            PropertyUtils.setProperty(bean, propName, xaProperties);

            continue;
          }

          PropertyUtils.setProperty(bean, propName, value);
          i++;
        }

        if (logger.isLoggable(Level.FINE))
        {
          logger.fine("Finished setting " + i + " property(ies) for the AtomikosDataSourceBean (" +
            beanClass.getName() + "), now initializing bean");
        }

        bean.init();

        return bean;
      }
      catch (Throwable e)
      {
        NamingException ne = new NamingException(
          "Failed to create the object instance (" + name.toString() + ") using the " +
            "AtomikosDataSourceBean (" + ((ResourceRef) obj).getClassName() + "): " +
            e.getMessage());

        ne.initCause(e);

        throw ne;
      }
    }
    else
    {
      return null;
    }
  }
}

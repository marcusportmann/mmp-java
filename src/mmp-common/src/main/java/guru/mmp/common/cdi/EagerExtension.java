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

package guru.mmp.common.cdi;

//~--- JDK imports ------------------------------------------------------------

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The <code>EagerExtension</code> class implements a CDI extension that together with the
 * <code>Eager</code> annotation provides support for eagerly initialising a CDI-enabled bean.
 *
 * @author Marcus Portmann
 */
public class EagerExtension
  implements Extension
{
  private List<Bean<?>> eagerBeansList = new ArrayList<>();

  /**
   * Build the list of CDI-enabled beans that have been flagged for eager initialisation by
   * being annotated with the <code>Eager</code> annotation.
   *
   * @param event the event
   * @param <T>   the class of the bean
   */
  public <T> void collect(@Observes ProcessBean<T> event)
  {
    if (event.getAnnotated().isAnnotationPresent(Eager.class)
        && event.getAnnotated().isAnnotationPresent(ApplicationScoped.class))
    {
      eagerBeansList.add(event.getBean());
    }
  }

  /**
   * Eagerly initialise the CDI-enabled beans that have been flagged for eager initialisation.
   *
   * @param event       the <code>AfterDeploymentValidation</code> event
   * @param beanManager the bean manager
   */
  @SuppressWarnings("ResultOfMethodCallIgnored")
  public void load(@SuppressWarnings("unused")
  @Observes AfterDeploymentValidation event, BeanManager beanManager)
  {
    for (Bean<?> bean : eagerBeansList)
    {
      // Note: toString() is important to instantiate the bean
      beanManager.getReference(bean, bean.getBeanClass(),
          beanManager.createCreationalContext(bean)).toString();
    }
  }
}
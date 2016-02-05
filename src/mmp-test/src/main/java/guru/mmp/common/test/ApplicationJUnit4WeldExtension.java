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

import guru.mmp.common.cdi.CDIException;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AnnotatedField;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessInjectionTarget;

import javax.persistence.PersistenceContext;

/**
 * The <code>ApplicationJUnit4ClassRunnerJtaPlatform</code> class.
 *
 * @author Marcus Portmann
 */
public class ApplicationJUnit4WeldExtension
  implements Extension
{
  <T> void processInjectionTarget(@Observes ProcessInjectionTarget<T> processInjectionTarget)
  {
    AnnotatedType<T> annotatedType = processInjectionTarget.getAnnotatedType();

    for (AnnotatedField<? super T> field : annotatedType.getFields())
    {
      if (field.isAnnotationPresent(PersistenceContext.class))
      {
        try
        {

        }
        catch (Throwable e)
        {
          throw new RuntimeException("Failed to inject the PersistenceContext", e);
        }


        System.out.println(
          "[DEBUG] processInjectionTarget.getAnnotatedType().getJavaClass().getName() = "
            + processInjectionTarget.getAnnotatedType().getJavaClass().getName());


        return;
      }


    }








  }
}

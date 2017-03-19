///*
// * Copyright 2017 Marcus Portmann
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *   http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package guru.mmp.application.test;
//
////~--- non-JDK imports --------------------------------------------------------
//
//import guru.mmp.common.cdi.AnnotatedTypeWrapper;
//
////~--- JDK imports ------------------------------------------------------------
//
//import java.lang.annotation.Annotation;
//import java.lang.reflect.Method;
//
//import javax.enterprise.event.Observes;
//import javax.enterprise.inject.spi.AnnotatedType;
//import javax.enterprise.inject.spi.Extension;
//import javax.enterprise.inject.spi.ProcessAnnotatedType;
//
//import javax.transaction.Transactional;
//
///**
// * The <code>JTATransactionalExtension</code> class.
// *
// * @author Marcus Portmann
// */
//public class JTATransactionalExtension
//  implements Extension
//{
//  /**
//   * Process the annotated type.
//   *
//   * @param processAnnotatedType the process annotated type event
//   *
//   * @param <T> the type
//   */
//  public <T> void processAnnotatedType(@Observes ProcessAnnotatedType<T> processAnnotatedType)
//  {
//    AnnotatedType<T> annotatedType = processAnnotatedType.getAnnotatedType();
//
//    boolean hasTransactionalAnnotation = false;
//
//    for (Method method : annotatedType.getJavaClass().getDeclaredMethods())
//    {
//      if (method.isAnnotationPresent(Transactional.class))
//      {
//        hasTransactionalAnnotation = true;
//
//        break;
//      }
//    }
//
//    if (!hasTransactionalAnnotation)
//    {
//      return;
//    }
//
//    Annotation annotation = () -> JTATransactional.class;
//
//    AnnotatedTypeWrapper<T> wrapper = new AnnotatedTypeWrapper<>(annotatedType,
//        annotatedType.getAnnotations());
//    wrapper.addAnnotation(annotation);
//
//    processAnnotatedType.setAnnotatedType(wrapper);
//  }
//}

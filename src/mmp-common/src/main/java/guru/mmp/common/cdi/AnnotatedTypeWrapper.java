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

package guru.mmp.common.cdi;

//~--- JDK imports ------------------------------------------------------------

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import java.util.HashSet;
import java.util.Set;

import javax.enterprise.inject.spi.AnnotatedConstructor;
import javax.enterprise.inject.spi.AnnotatedField;
import javax.enterprise.inject.spi.AnnotatedMethod;
import javax.enterprise.inject.spi.AnnotatedType;

/**
 * The <code>AnnotatedTypeWrapper</code> class.
 *
 * @param <T>
 *
 * @author Marcus Portmann
 */
public class AnnotatedTypeWrapper<T>
  implements AnnotatedType<T>
{
  private final AnnotatedType<T> wrapped;
  private final Set<Annotation> annotations;

  /**
   * Constructs a new <code>AnnotatedTypeWrapper</code>.
   *
   * @param wrapped
   * @param annotations
   */
  public AnnotatedTypeWrapper(AnnotatedType<T> wrapped, Set<Annotation> annotations)
  {
    this.wrapped = wrapped;
    this.annotations = new HashSet<>(annotations);
  }

  /**
   * Add the annotation to the annotated type.
   *
   * @param annotation the annotated type
   */
  public void addAnnotation(Annotation annotation)
  {
    annotations.add(annotation);
  }

  @Override
  public <A extends Annotation> A getAnnotation(Class<A> annotationType)
  {
    return wrapped.getAnnotation(annotationType);
  }

  @Override
  public Set<Annotation> getAnnotations()
  {
    return annotations;
  }

  @Override
  public Type getBaseType()
  {
    return wrapped.getBaseType();
  }

  @Override
  public Set<AnnotatedConstructor<T>> getConstructors()
  {
    return wrapped.getConstructors();
  }

  @Override
  public Set<AnnotatedField<? super T>> getFields()
  {
    return wrapped.getFields();
  }

  @Override
  public Class<T> getJavaClass()
  {
    return wrapped.getJavaClass();
  }

  @Override
  public Set<AnnotatedMethod<? super T>> getMethods()
  {
    return wrapped.getMethods();
  }

  @Override
  public Set<Type> getTypeClosure()
  {
    return wrapped.getTypeClosure();
  }

  @Override
  public boolean isAnnotationPresent(Class<? extends Annotation> annotationType)
  {
    for (Annotation annotation : annotations)
    {
      if (annotationType.isInstance(annotation))
      {
        return true;
      }
    }

    return false;
  }
}

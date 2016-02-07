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

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.interceptor.InterceptorBinding;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;

/**
 * The <code>ApplicationJUnitEntityManagerCleanup</code> annotation.
 * <p/>
 * This annotation is programmatically applied to CDI beans that a <code>EntityManager</code>
 * instance is injected into by the <code>ApplicationJUnit4EntityManagerInjector</code> Weld
 * extension. CDI beans that are annotated in this way will be processed by the
 * <code>EntityManagerCleanupInterceptor</code>. The interceptor is responsible for cleaning up
 * the <code>EntityManager</code> instances that are not associated with a transaction.
 *
 * @author Marcus Portmann
 */
@Inherited
@InterceptorBinding
@Retention(RetentionPolicy.RUNTIME)
@Target({ METHOD, TYPE })
public @interface ApplicationJUnit4EntityManagerCleanup
{}

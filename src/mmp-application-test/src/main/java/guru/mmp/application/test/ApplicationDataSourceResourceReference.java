/*
 * Copyright 2017 Marcus Portmann
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

//~--- JDK imports ------------------------------------------------------------

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The <code>ApplicationDataSourceResourceReference</code> annotation is used in conjunction
 * with the <code>ApplicationClassRunner</code> JUnit runner to add a JNDI resource reference to
 * the application data source provided by the runner.
 * <p/>
 * This is required when the tests being run make use of JPA. It allows the global JNDI name
 * specified for the JTA data source (jta-data-source) for a persistence unit, in the
 * persistence.xml file for the persistence unit, to be mapped to the application data source.
 *
 * @author Marcus Portmann
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ApplicationDataSourceResourceReference
{
  String name();
}

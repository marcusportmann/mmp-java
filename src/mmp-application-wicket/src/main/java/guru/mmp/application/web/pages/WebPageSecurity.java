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

package guru.mmp.application.web.pages;

//~--- JDK imports ------------------------------------------------------------

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation type that is used to identify secure web pages. The value specified for the
 * annotation is the function code that uniquely identifies the function associated with the
 * web page e.g. Application.Security.CreateUser, etc.
 * <p/>
 * Function codes can refer to a specific "function" e.g. Application.Security.CreateUser or a
 * "functionality grouping" e.g. Application.Security.UserManagement. The decision
 * on whether to use a "function" or "functionality grouping" is dependent on the granularity
 * of the application's access control.
 *
 * @author Marcus Portmann
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface WebPageSecurity
{
  String value();
}

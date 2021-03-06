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

package guru.mmp.application.sms.test;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.test.TestConfiguration;
import org.springframework.context.annotation.Configuration;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>MessagingTestConfiguration</code> class provides the Spring configuration for the
 * JUnit test classes that test the SMS capabilities provided by the
 * <b>mmp-java (Open Source Java and JEE Development Framework)</b>.
 *
 * @author Marcus Portmann
 */
@Configuration
public class SMSTestConfiguration extends TestConfiguration {}

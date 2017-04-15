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

package guru.mmp.sample;

//~--- non-JDK imports --------------------------------------------------------

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The <code>Sample</code> class implements the main Spring Boot application class for the Sample
 * application.
 *
 * @author Marcus Portmann
 */
@SpringBootApplication
public class Sample
{
  /**
   * The main method.
   *
   * @param args the command-line arguments
   */
  public static void main(String[] args)
    throws Exception
  {
    SpringApplication.run(Sample.class, args);
  }
}

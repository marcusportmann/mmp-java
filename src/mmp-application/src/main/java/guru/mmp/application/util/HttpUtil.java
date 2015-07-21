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

package guru.mmp.application.util;

//~--- JDK imports ------------------------------------------------------------

import javax.servlet.ServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * The HttpUtil class provides utility methods that are useful when working with the HTTP protocol.
 *
 * @author Marcus Portmann
 */
public class HttpUtil
{
  /**
   * Private default constructor to enforce utility pattern.
   */
  private HttpUtil() {}

  /**
   * Sort the names of the parameters in the request.
   *
   * @param request the servlet request
   *
   * @return the sorted array containing the names of the request parameters
   */
  public static String[] sortParameterNames(ServletRequest request)
  {
    List<String> list = new ArrayList<>();
    Enumeration<?> names = request.getParameterNames();

    while (names.hasMoreElements())
    {
      String name = (String) names.nextElement();

      list.add(name);
    }

    Collections.sort(list);

    return list.toArray(new String[list.size()]);
  }
}

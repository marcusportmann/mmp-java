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

package guru.mmp.sample.ws;

//~--- non-JDK imports --------------------------------------------------------

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>SampleServiceTranslator</code> class is responsible for translating model
 * objects to web service objects generated from the SampleService.wsdl and vice-versa.
 *
 * @author Marcus Portmann
 */
public class SampleServiceTranslator
{
  /**
   * Translate the exception information into an <code>FaultInfo</code> object.
   *
   * @param throwable the <code>Throwable</code> to translate
   *
   * @return the <code>FaultInfo</code> object
   */
  public static FaultInfo translate(Throwable throwable)
  {
    FaultInfo faultInfo = new FaultInfo();

    faultInfo.setMessage(throwable.getMessage());

    try
    {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      PrintWriter pw = new PrintWriter(baos);

      pw.println(throwable.getMessage());
      pw.println();
      throwable.printStackTrace(pw);
      pw.flush();
      faultInfo.setDetail(baos.toString());
    }
    catch (Throwable e)
    {
      faultInfo.setDetail("Failed to dump the stack for the exception (" + throwable + "): "
          + e.getMessage());
    }

    return faultInfo;
  }
}

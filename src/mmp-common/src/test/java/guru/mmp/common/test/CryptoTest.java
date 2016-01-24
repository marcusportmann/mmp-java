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

import guru.mmp.common.crypto.CryptoUtils;
import guru.mmp.common.util.Base64;
import org.junit.Test;

/**
 * The <code>CryptoTest</code> class.
 */
public class CryptoTest
{
  @Test
  public void aesTest()
  {
    byte[] aesKey = CryptoUtils.getRandomAESKey();

    System.out.println(Base64.encodeBytes(aesKey));



  }

}

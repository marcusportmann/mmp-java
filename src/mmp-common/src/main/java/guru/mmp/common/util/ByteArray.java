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

package guru.mmp.common.util;

//~--- JDK imports ------------------------------------------------------------

import java.io.Serializable;

/**
 * The <code>ByteArray</code> class provides a wrapper class for an array of bytes.
 *
 * @author Marcus Portmann
 */
public class ByteArray
  implements Serializable
{
  private static final long serialVersionUID = 1000000;
  private byte[] bytes;

  /**
   * Constructs a new <code>ByteArray</code>.
   *
   * @param bytes the byte array to wrap
   */
  public ByteArray(byte[] bytes)
  {
    this.bytes = bytes;
  }

  /**
   * Returns the wrapped byte array.
   *
   * @return the wrapped byte array
   */
  public byte[] getBytes()
  {
    return bytes;
  }

  /**
   * Set the wrapped byte array.
   *
   * @param bytes the byte array to wrap
   */
  public void setBytes(byte[] bytes)
  {
    this.bytes = bytes;
  }
}

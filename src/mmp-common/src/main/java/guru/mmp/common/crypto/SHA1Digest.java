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

package guru.mmp.common.crypto;

/**
 * Implementation of SHA-1 as outlined in "Handbook of Applied Cryptography", pages 346 - 349.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public final class SHA1Digest
{
  // Additive constants
  private static final int Y1 = 0x5a827999;
  private static final int Y2 = 0x6ed9eba1;
  private static final int Y3 = 0x8f1bbcdc;
  private static final int Y4 = 0xca62c1d6;
  private int H1, H2, H3, H4, H5;
  private int[] X = new int[80];
  private long byteCount;
  private byte[] xBuf;
  private int xBufOff;
  private int xOff;

  /**
   * Constructs a new <code>SHA1Digest</code>.
   */
  public SHA1Digest()
  {
    xBuf = new byte[4];
    xBufOff = 0;
    reset();
  }

  /**
   * Finishes a multiple-part SHA-1 operation.
   *
   * @param out    the byte array to write the SHA-1 hash to
   * @param outOff the offset in the byte array to write the SHA-1 hash to
   *
   * @return the result of the multiple-part SHA-1 operation
   */
  public int doFinal(byte[] out, int outOff)
  {
    finish();
    unpackWord(H1, out, outOff);
    unpackWord(H2, out, outOff + 4);
    unpackWord(H3, out, outOff + 8);
    unpackWord(H4, out, outOff + 12);
    unpackWord(H5, out, outOff + 16);
    reset();

    return 20;
  }

  /**
   * Complete the SHA-1 hash operation.
   */
  public void finish()
  {
    long bitLength = (byteCount << 3);

    // Add the pad bytes.
    update((byte) 128);

    while (xBufOff != 0)
    {
      update((byte) 0);
    }

    processLength(bitLength);
    processBlock();
  }

  /**
   * Reset the chaining variables.
   */
  public void reset()
  {
    byteCount = 0;
    xBufOff = 0;

    for (int i = 0; i < xBuf.length; i++)
    {
      xBuf[i] = 0;
    }

    H1 = 0x67452301;
    H2 = 0xefcdab89;
    H3 = 0x98badcfe;
    H4 = 0x10325476;
    H5 = 0xc3d2e1f0;
    xOff = 0;

    for (int i = 0; i != X.length; i++)
    {
      X[i] = 0;
    }
  }

  /**
   * Include the specified value in the data being hashed.
   *
   * @param in the value to include
   */
  public void update(byte in)
  {
    xBuf[xBufOff++] = in;

    if (xBufOff == xBuf.length)
    {
      processWord(xBuf, 0);
      xBufOff = 0;
    }

    byteCount++;
  }

  /**
   * Include the specified data in the data being hashed.
   *
   * @param in    the byte array containing the data to hash
   * @param inOff the offset in the byte array containing the data to hash
   * @param len   the length of the data to hash
   */
  public void update(byte[] in, int inOff, int len)
  {
    // Fill the current word
    while ((xBufOff != 0) && (len > 0))
    {
      update(in[inOff]);
      inOff++;
      len--;
    }

    // Process whole words.
    while (len > xBuf.length)
    {
      processWord(in, inOff);
      inOff += xBuf.length;
      len -= xBuf.length;
      byteCount += xBuf.length;
    }

    // Load in the remainder.
    while (len > 0)
    {
      update(in[inOff]);
      inOff++;
      len--;
    }
  }

  protected void processBlock()
  {
    // Expand 16 word block into 80 word block.
    for (int i = 16; i <= 79; i++)
    {
      X[i] = rotateLeft((X[i - 3] ^ X[i - 8] ^ X[i - 14] ^ X[i - 16]), 1);
    }

    // Set up working variables.
    int A = H1;
    int B = H2;
    int C = H3;
    int D = H4;
    int E = H5;

    // Round 1
    for (int j = 0; j <= 19; j++)
    {
      int t = rotateLeft(A, 5) + f(B, C, D) + E + X[j] + Y1;

      E = D;
      D = C;
      C = rotateLeft(B, 30);
      B = A;
      A = t;
    }

    // Round 2
    for (int j = 20; j <= 39; j++)
    {
      int t = rotateLeft(A, 5) + h(B, C, D) + E + X[j] + Y2;

      E = D;
      D = C;
      C = rotateLeft(B, 30);
      B = A;
      A = t;
    }

    // Round 3
    for (int j = 40; j <= 59; j++)
    {
      int t = rotateLeft(A, 5) + g(B, C, D) + E + X[j] + Y3;

      E = D;
      D = C;
      C = rotateLeft(B, 30);
      B = A;
      A = t;
    }

    // Round 4
    for (int j = 60; j <= 79; j++)
    {
      int t = rotateLeft(A, 5) + h(B, C, D) + E + X[j] + Y4;

      E = D;
      D = C;
      C = rotateLeft(B, 30);
      B = A;
      A = t;
    }

    H1 += A;
    H2 += B;
    H3 += C;
    H4 += D;
    H5 += E;

    // Reset the offset and clean out the word buffer.
    xOff = 0;

    for (int i = 0; i != X.length; i++)
    {
      X[i] = 0;
    }
  }

  private int f(int u, int v, int w)
  {
    return ((u & v) | ((~u) & w));
  }

  private int g(int u, int v, int w)
  {
    return ((u & v) | (u & w) | (v & w));
  }

  private int h(int u, int v, int w)
  {
    return (u ^ v ^ w);
  }

  private void processLength(long bitLength)
  {
    if (xOff > 14)
    {
      processBlock();
    }

    X[14] = (int) (bitLength >>> 32);
    X[15] = (int) (bitLength);
  }

  private void processWord(byte[] in, int inOff)
  {
    X[xOff++] = ((in[inOff] & 0xff) << 24) | ((in[inOff + 1] & 0xff) << 16)
        | ((in[inOff + 2] & 0xff) << 8) | ((in[inOff + 3] & 0xff));

    if (xOff == 16)
    {
      processBlock();
    }
  }

  private int rotateLeft(int x, int n)
  {
    return (x << n) | (x >>> (32 - n));
  }

  private void unpackWord(int word, byte[] out, int outOff)
  {
    out[outOff] = (byte) (word >>> 24);
    out[outOff + 1] = (byte) (word >>> 16);
    out[outOff + 2] = (byte) (word >>> 8);
    out[outOff + 3] = (byte) word;
  }
}

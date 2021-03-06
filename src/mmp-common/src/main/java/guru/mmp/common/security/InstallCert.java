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

package guru.mmp.common.security;

//~--- JDK imports ------------------------------------------------------------

import java.io.*;

import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.*;

/*
* Copyright 2006 Sun Microsystems, Inc.  All Rights Reserved.
*
* Redistribution and use in source and binary forms, with or without
* modification, are permitted provided that the following conditions
* are met:
*
*   - Redistributions of source code must retain the above copyright
*     notice, this list of conditions and the disclaimer below.
*
*   - Redistributions in binary form must reproduce the above copyright
*     notice, this list of conditions and the following disclaimer in the
*     documentation and/or other materials provided with the distribution.
*
*   - Neither the name of Sun Microsystems nor the names of its
*     contributors may be used to endorse or promote products derived
*     from this software without specific prior written permission.
*
* THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
* IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
* THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
* PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
* CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
* EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
* PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
* PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
* LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
* NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
* SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/**
 * Originally from:
 * http://blogs.sun.com/andreas/resource/InstallCert.java
 * Use:
 * java InstallCert hostname
 * Example:
 * % java InstallCert ecc.fedora.redhat.com
 */

/**
 * Class used to add the server's certificate to the KeyStore
 * with your trusted certificates.
 */
public class InstallCert
{
  private static final char[] HEXDIGITS = "0123456789abcdef".toCharArray();

  /**
   * The main method.
   *
   * @param args the command line arguments
   *
   * @throws Exception
   */
  public static void main(String[] args)
    throws Exception
  {
    String host;
    int port;
    char[] passphrase;

    if ((args.length == 1) || (args.length == 2))
    {
      String[] c = args[0].split(":");

      host = c[0];
      port = (c.length == 1)
          ? 443
          : Integer.parseInt(c[1]);

      String p = (args.length == 1)
          ? "changeit"
          : args[1];

      passphrase = p.toCharArray();
    }
    else
    {
      System.out.println("Usage: java InstallCert [:port] [passphrase]");

      return;
    }

    File file = new File("jssecacerts");

    if (!file.isFile())
    {
      char SEP = File.separatorChar;
      File dir = new File(System.getProperty("java.home") + SEP + "lib" + SEP + "security");

      file = new File(dir, "jssecacerts");

      if (!file.isFile())
      {
        file = new File(dir, "cacerts");
      }
    }

    System.out.println("Loading KeyStore " + file + "...");

    InputStream in = new FileInputStream(file);
    KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());

    ks.load(in, passphrase);
    in.close();

    SSLContext context = SSLContext.getInstance("TLS");
    TrustManagerFactory tmf = TrustManagerFactory.getInstance(
        TrustManagerFactory.getDefaultAlgorithm());

    tmf.init(ks);

    X509TrustManager defaultTrustManager = (X509TrustManager) tmf.getTrustManagers()[0];
    SavingTrustManager tm = new SavingTrustManager(defaultTrustManager);

    context.init(null, new TrustManager[] { tm }, null);

    SSLSocketFactory factory = context.getSocketFactory();

    System.out.println("Opening connection to " + host + ":" + port + "...");

    SSLSocket socket = (SSLSocket) factory.createSocket(host, port);

    socket.setSoTimeout(10000);

    try
    {
      System.out.println("Starting SSL handshake...");
      socket.startHandshake();
      socket.close();
      System.out.println();
      System.out.println("No errors, certificate is already trusted");
    }
    catch (SSLException e)
    {
      System.out.println();
      e.printStackTrace(System.out);
    }

    X509Certificate[] chain = tm.chain;

    if (chain == null)
    {
      System.out.println("Could not obtain server certificate chain");

      return;
    }

    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    System.out.println();
    System.out.println("Server sent " + chain.length + " certificate(s):");
    System.out.println();

    MessageDigest sha1 = MessageDigest.getInstance("SHA1");
    MessageDigest md5 = MessageDigest.getInstance("MD5");

    for (int i = 0; i < chain.length; i++)
    {
      X509Certificate cert = chain[i];

      System.out.println(" " + (i + 1) + " Subject " + cert.getSubjectDN());
      System.out.println("   Issuer  " + cert.getIssuerDN());
      sha1.update(cert.getEncoded());
      System.out.println("   sha1    " + toHexString(sha1.digest()));
      md5.update(cert.getEncoded());
      System.out.println("   md5     " + toHexString(md5.digest()));
      System.out.println();
    }

    System.out.println("Enter certificate to add to trusted key store or 'q' to quit: [1]");

    String line = reader.readLine().trim();
    int k;

    try
    {
      k = (line.length() == 0)
          ? 0
          : Integer.parseInt(line) - 1;
    }
    catch (NumberFormatException e)
    {
      System.out.println("KeyStore not changed");

      return;
    }

    X509Certificate cert = chain[k];
    String alias = host + "-" + (k + 1);

    ks.setCertificateEntry(alias, cert);

    OutputStream out = new FileOutputStream("jssecacerts");

    ks.store(out, passphrase);
    out.close();

    System.out.println();
    System.out.println(cert);
    System.out.println();
    System.out.println("Added certificate to key store 'jssecacerts' using alias '" + alias + "'");
  }

  private static String toHexString(byte[] bytes)
  {
    StringBuilder sb = new StringBuilder(bytes.length * 3);

    for (int b : bytes)
    {
      b &= 0xff;
      sb.append(HEXDIGITS[b >> 4]);
      sb.append(HEXDIGITS[b & 15]);
      sb.append(' ');
    }

    return sb.toString();
  }

  private static class SavingTrustManager
    implements X509TrustManager
  {
    private X509Certificate[] chain;
    private X509TrustManager tm;

    SavingTrustManager(X509TrustManager tm)
    {
      this.tm = tm;
    }

    /**
     * Check whether the client is trusted.
     *
     * @param chain    the X509 certificate chain
     * @param authType the auth type
     *
     * @throws CertificateException
     */
    public void checkClientTrusted(X509Certificate[] chain, String authType)
      throws CertificateException
    {
      throw new UnsupportedOperationException();
    }

    /**
     * Check whether the server is trusted.
     *
     * @param chain    the X509 certificate chain
     * @param authType the auth type
     *
     * @throws CertificateException
     */
    public void checkServerTrusted(X509Certificate[] chain, String authType)
      throws CertificateException
    {
      this.chain = chain;
      tm.checkServerTrusted(chain, authType);
    }

    /**
     * Returns the list of accepted certificate issuers.
     *
     * @return the list of accepted certificate issuers
     */
    public X509Certificate[] getAcceptedIssuers()
    {
      throw new UnsupportedOperationException();
    }
  }
}

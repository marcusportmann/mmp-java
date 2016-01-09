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

package guru.mmp.common.corba;

import guru.mmp.common.security.context.ApplicationSecurityContext;
import org.omg.CORBA.TIMEOUT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.SocketFactory;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManagerFactory;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * The <code>SSLSocketFactory</code> provides the capability to create SSL socket connections
 * for CORBA clients.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public class SSLSocketFactory
  implements org.jacorb.orb.factory.SocketFactory
{
  private static final Logger logger = LoggerFactory.getLogger(SSLSocketFactory.class);

  private SocketFactory socketFactory;

  /**
   * Constructs a new <code>SSLSocketFactory</code>.
   */
  public SSLSocketFactory() {}

  /**
   * Create a connected stream Socket.
   *
   * @param host the host name
   * @param port the port number
   *
   * @return a connected stream Socket
   *
   * @throws IOException
   */
  public Socket createSocket(String host, int port)
    throws IOException
  {
    SocketFactory socketFactory = getSocketFactory();

    if (logger.isDebugEnabled())
    {
      logger.debug(
        "Attempting to open a SSL socket connection to the remote CORBA ORB (" + host + ":" +
          port + ")");
    }

    try
    {
      return socketFactory.createSocket(host, port);
    }
    catch (UnknownHostException e)
    {
      logger.error(
        "Failed to open a SSL socket connection to the remote CORBA ORB (" + host + ":" + port +
          ") since the host could not be resolved");

      throw e;
    }
    catch (IOException e)
    {
      logger.error(
        "Failed to open a SSL socket connection to the remote CORBA ORB (" + host + ":" + port +
          "): " + e.getMessage(), e);

      throw e;
    }
    catch (Throwable e)
    {
      logger.error(
        "Failed to open a SSL socket connection to the remote CORBA ORB (" + host + ":" + port +
          "): " + e.getMessage(), e);

      throw new IOException(
        "Failed to open a SSL socket connection to the remote CORBA ORB (" + host + ":" + port +
          "): " + e.getMessage());
    }
  }

  /**
   * Create a connected stream Socket.
   * </p>
   * Compliant implementations must ensure to throw org.omg.CORBA.TIMEOUT in case a timeout occurs
   * instead of the SocketTimeoutException thats available in the JDK.
   *
   * @param host    the host name
   * @param port    the port number
   * @param timeout the timeout value to be used in milliseconds
   *
   * @return a connected stream Socket
   *
   * @throws IOException
   * @throws TIMEOUT     if a timeout occurs during connect.
   */
  public Socket createSocket(String host, int port, int timeout)
    throws IOException, TIMEOUT
  {
    // TODO: Implement proper timeout management

    return createSocket(host, port);
  }

  /**
   * Returns <code>true</code> if the specified socket is an SSL socket or <code>false</code>
   * otherwise.
   *
   * @param socket the socket to check
   *
   * @return <code>true</code> if the specified socket is an SSL socket or <code>false</code>
   * otherwise
   */
  public boolean isSSL(java.net.Socket socket)
  {
    return (socket instanceof SSLSocket);
  }

  private SocketFactory getSocketFactory()
    throws IOException
  {
    if (socketFactory != null)
    {
      return socketFactory;
    }

    try
    {
      ApplicationSecurityContext applicationSecurityContext = ApplicationSecurityContext
        .getContext();

      // Initialize the key manager factory
      KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(
        KeyManagerFactory.getDefaultAlgorithm());

      keyManagerFactory.init(applicationSecurityContext.getKeyStore(),
        applicationSecurityContext.getKeyStorePassword().toCharArray());

      // Initialize the truest manager factory

      TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
        TrustManagerFactory.getDefaultAlgorithm());

      trustManagerFactory.init(applicationSecurityContext.getKeyStore());

      // Create the SSLContext
      SSLContext sslContext = SSLContext.getInstance("TLS");

      sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(),
        null);

      // Retrieve the SSL socket factory
      socketFactory = sslContext.getSocketFactory();

      return socketFactory;
    }
    catch (Throwable e)
    {
      throw new IOException("Failed to initialize the SSL socket factory: " + e.getMessage(), e);
    }
  }
}

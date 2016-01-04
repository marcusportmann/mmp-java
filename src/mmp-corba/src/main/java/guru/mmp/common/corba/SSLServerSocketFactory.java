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

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.common.security.context.ServiceSecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ServerSocketFactory;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>SSLServerSocketFactory</code> provides the capability to create SSL server sockets
 * for CORBA servers.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public class SSLServerSocketFactory
  implements org.jacorb.orb.factory.ServerSocketFactory

{
  private static final Logger logger = LoggerFactory.getLogger(SSLServerSocketFactory.class);
  private ServerSocketFactory serverSocketFactory;

  /**
   * Constructs a new <code>SSLServerSocketFactory</code>.
   */
  public SSLServerSocketFactory() {}

  /**
   * Returns a server socket which uses all network interfaces on the host, and is bound to the
   * specified port.
   *
   * @param port the port to listen to
   *
   * @return a server socket
   *
   * @exception IOException
   */
  @Override
  public ServerSocket createServerSocket(int port)
    throws IOException
  {
    try
    {
      return getServerSocketFactory().createServerSocket(port);
    }
    catch (Throwable e)
    {
      logger.error("Failed to create a SSL server socket for the CORBA ORB on port (" + port
          + "): " + e.getMessage(), e);

      throw new IOException("Failed to create a SSL server socket for the CORBA ORB on port ("
          + port + "): " + e.getMessage(), e);
    }
  }

  /**
   * Returns a server socket which uses all network interfaces on the host, and is bound to the
   * specified port.
   *
   * @param port    the port to listen on
   * @param backlog how many connections are queued
   *
   * @return a server socket
   *
   * @exception IOException
   */
  @Override
  public ServerSocket createServerSocket(int port, int backlog)
    throws IOException
  {
    try
    {
      return getServerSocketFactory().createServerSocket(port, backlog);
    }
    catch (Throwable e)
    {
      logger.error("Failed to create a SSL server socket for the CORBA ORB on port (" + port
          + "): " + e.getMessage(), e);

      throw new IOException("Failed to create a SSL server socket for the CORBA ORB on port ("
          + port + "): " + e.getMessage(), e);
    }
  }

  /**
   * Returns a server socket which is bound to the specified port and network interface.
   *
   * @param port      the port to listen on
   * @param backlog   how many connections are queued
   * @param ifAddress the network interface to listen on
   *
   * @return a server socket
   *
   * @exception IOException
   */
  @Override
  public ServerSocket createServerSocket(int port, int backlog, InetAddress ifAddress)
    throws IOException
  {
    try
    {
      return getServerSocketFactory().createServerSocket(port, backlog, ifAddress);
    }
    catch (Throwable e)
    {
      logger.error("Failed to create a SSL server socket for the CORBA ORB on port (" + port
          + "): " + e.getMessage(), e);

      throw new IOException("Failed to create a SSL server socket for the CORBA ORB on port ("
          + port + "): " + e.getMessage(), e);
    }
  }

  private ServerSocketFactory getServerSocketFactory()
    throws IOException
  {
    if (serverSocketFactory != null)
    {
      return serverSocketFactory;
    }

    try
    {
      ServiceSecurityContext serviceSecurityContext = ServiceSecurityContext.getContext("ORB");

      // Initialize the key manager factory
      KeyManagerFactory keyManagerFactory =
        KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());

      keyManagerFactory.init(serviceSecurityContext.getKeyStore(),
          serviceSecurityContext.getKeyStorePassword().toCharArray());

      // Initialize the truest manager factory

      TrustManagerFactory trustManagerFactory =
        TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());

      trustManagerFactory.init(serviceSecurityContext.getKeyStore());

      // Create the SSLContext
      SSLContext sslContext = SSLContext.getInstance("TLS");

      sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(),
          null);

      // Retrieve the SSL server socket factory
      serverSocketFactory = sslContext.getServerSocketFactory();

      return serverSocketFactory;

    }
    catch (Throwable e)
    {
      throw new IOException("Failed to initialize the SSL server socket factory: "
          + e.getMessage(), e);
    }

  }
}

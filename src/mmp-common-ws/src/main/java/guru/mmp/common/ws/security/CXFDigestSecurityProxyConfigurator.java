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

package guru.mmp.common.ws.security;

//~--- JDK imports ------------------------------------------------------------

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * The <code>CXFDigestSecurityProxyConfigurator</code> class provides the capability to configure
 * a CXF web service proxy for digest authentication using Java reflection.
 *
 * @author Marcus Portmann
 */
public class CXFDigestSecurityProxyConfigurator
{
  private static Method cxfAuthorizationPolicySetAuthorizationMethod;
  private static Method cxfAuthorizationPolicySetAuthorizationTypeMethod;
  private static Method cxfAuthorizationPolicySetPasswordMethod;
  private static Method cxfAuthorizationPolicySetUserNameMethod;
  private static Method cxfClientGetConduitMethod;
  private static Method cxfHTTPClientPolicySetAllowChunkingMethod;
  private static Method cxfHTTPConduitGetAuthorizationMethod;
  private static Method cxfHTTPConduitGetClientMethod;
  private static Method cxfHTTPConduitSetAuthSupplierMethod;
  private static Method cxfProxyGetClientMethod;
  private static Class<?> digestAuthSupplierClass;
  private static boolean initialised;

  /**
   * Configure the CXF web service proxy to support digest authentication.
   *
   * @param proxy    the CXF web service proxy to configure
   * @param username the username to use when authenticating using digest authentication
   * @param password the password to use when authenticating using digest authentication
   */
  public static void configureProxy(Object proxy, String username, String password)
    throws Exception
  {
    if (!initialised)
    {
      initialise(proxy);
    }

    // Get the CXF HTTPConduit
    InvocationHandler invocationHandler = Proxy.getInvocationHandler(proxy);

    Object client = cxfProxyGetClientMethod.invoke(invocationHandler);

    Object httpConduit = cxfClientGetConduitMethod.invoke(client);

    // Configure the CXF AuthorizationPolicy
    Object authorizationPolicy = cxfHTTPConduitGetAuthorizationMethod.invoke(httpConduit);

    cxfAuthorizationPolicySetAuthorizationMethod.invoke(authorizationPolicy, "Digest");

    cxfAuthorizationPolicySetAuthorizationTypeMethod.invoke(authorizationPolicy, "Digest");

    cxfAuthorizationPolicySetUserNameMethod.invoke(authorizationPolicy, username);

    cxfAuthorizationPolicySetPasswordMethod.invoke(authorizationPolicy, password);

    // Configure the CXF AuthSupplier
    Object authSupplier = digestAuthSupplierClass.newInstance();

    cxfHTTPConduitSetAuthSupplierMethod.invoke(httpConduit, authSupplier);

    // Configure the CXF HTTPClientPolicy
    Object httpClientPolicy = cxfHTTPConduitGetClientMethod.invoke(httpConduit);

    cxfHTTPClientPolicySetAllowChunkingMethod.invoke(httpClientPolicy, false);
  }

  private static synchronized void initialise(Object proxy)
    throws Exception
  {
    if (initialised)
    {
      return;
    }

    ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();

    // Get the CXF HTTPConduit
    InvocationHandler invocationHandler = Proxy.getInvocationHandler(proxy);

    cxfProxyGetClientMethod = invocationHandler.getClass().getMethod("getClient");

    Object client = cxfProxyGetClientMethod.invoke(invocationHandler);

    cxfClientGetConduitMethod = client.getClass().getMethod("getConduit");

    Object httpConduit = cxfClientGetConduitMethod.invoke(client);

    cxfHTTPConduitGetAuthorizationMethod = httpConduit.getClass().getMethod("getAuthorization");

    Object authorizationPolicy = cxfHTTPConduitGetAuthorizationMethod.invoke(httpConduit);

    cxfAuthorizationPolicySetAuthorizationMethod = authorizationPolicy.getClass().getMethod(
        "setAuthorization", String.class);

    cxfAuthorizationPolicySetAuthorizationTypeMethod = authorizationPolicy.getClass().getMethod(
        "setAuthorizationType", String.class);

    cxfAuthorizationPolicySetUserNameMethod = authorizationPolicy.getClass().getMethod(
        "setUserName", String.class);

    cxfAuthorizationPolicySetPasswordMethod = authorizationPolicy.getClass().getMethod(
        "setPassword", String.class);

    digestAuthSupplierClass = contextClassLoader.loadClass(
        "org.apache.cxf.transport.http.auth.DigestAuthSupplier");

    Class<?> authSupplierClass = contextClassLoader.loadClass(
        "org.apache.cxf.transport.http.auth.HttpAuthSupplier");

    cxfHTTPConduitSetAuthSupplierMethod = httpConduit.getClass().getMethod("setAuthSupplier",
        authSupplierClass);

    cxfHTTPConduitGetClientMethod = httpConduit.getClass().getMethod("getClient");

    Object httpClientPolicy = cxfHTTPConduitGetClientMethod.invoke(httpConduit);

    cxfHTTPClientPolicySetAllowChunkingMethod = httpClientPolicy.getClass().getMethod(
        "setAllowChunking", boolean.class);

    initialised = true;
  }
}

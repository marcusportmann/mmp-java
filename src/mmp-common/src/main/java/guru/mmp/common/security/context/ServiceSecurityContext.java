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

package guru.mmp.common.security.context;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.common.xml.DtdJarResolver;
import guru.mmp.common.xml.XmlParserErrorHandler;
import guru.mmp.common.xml.XmlUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import org.xml.sax.InputSource;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.InputStream;

import java.net.MalformedURLException;
import java.net.URL;

import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * The <code>ServiceSecurityContext</code> class holds the security information for a service.
 * <p/>
 * This information includes the path to the key store that holds the service's private/public
 * key pair, the alias of the public/private key pair in the key store and the password used to
 * retrieve the public/private key pair.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public class ServiceSecurityContext
{
  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(ServiceSecurityContext.class);

  /**
   * The <code>ServiceSecurityContext</code> instances that have been initialised.
   */
  private static ConcurrentMap<String, ServiceSecurityContext> serviceSecurityContexts =
      new ConcurrentHashMap<>();

  /**
   * The key store containing the service's private/public key pair.
   */
  private KeyStore keyStore;

  /**
   * The alias that identifies the service's private/public key pair in the key store.
   */
  private String keyStoreAlias;

  /**
   * The certificates loaded from the service's key store.
   */
  private Map<String, Certificate> keyStoreCertificates;

  /**
   * The name of the key store file containing the service's private/public key pair.
   */
  private String keyStoreName;

  /**
   * The password used to retrieve the service's private/public key pair from the key store.
   */
  private String keyStorePassword;

  /**
   * The certificate for the service retrieved from the service's key store.
   */
  private X509Certificate serviceCertificate;

  /**
   * The private key for the service retrieved from the service's key store.
   */
  private Key servicePrivateKey;

  private ServiceSecurityContext(String serviceName)
  {
    try
    {
      if (!exists(serviceName))
      {
        throw new ServiceSecurityContextException("Failed to find the configuration file ("
            + serviceName + ".ServiceSecurity)");
      }

      boolean serviceSecurityConfigLoaded = false;

      /*
       * Retrieve the configuration file containing the security configuration for the service.
       * This file contains the name of the key store for the service, the alias of the
       * public/private key pair for the service in the key store and the password used to
       * access the key store.
       *
       * The following locations are searched to find the configuration file:
       *
       * 1. The ${was.install.root}/certificates directory when running under WebSphere.
       * 2. The ${jboss.home.dir}/certificates directory when running under JBoss.
       * 3. The ${catalina.home}/certificates directory when running under Tomcat.
       * 4. Under the "/" path in the CLASSPATH.
       * 5. Under the "/META-INF" path in the CLASSPATH.
       */
      if (!serviceSecurityConfigLoaded)
      {
        String wasInstallRoot = System.getProperty("was.install.root");

        if (wasInstallRoot != null)
        {
          String configurationFilePath = wasInstallRoot + File.separator + "certificates" + File
              .separator + serviceName + ".ServiceSecurity";
          File configurationFile = new File(configurationFilePath);

          if (configurationFile.exists())
          {
            try
            {
              loadWebServicesSecurityConfig(serviceName, configurationFile.toURI().toURL());
              serviceSecurityConfigLoaded = true;
            }
            catch (MalformedURLException e)
            {
              throw new ServiceSecurityContextException("Failed to load the security"
                  + " configuration for the service (" + serviceName + ") from the file ("
                  + configurationFilePath + ")", e);
            }
          }
          else
          {
            if (logger.isDebugEnabled())
            {
              logger.debug("The service security configuration file (" + configurationFilePath
                  + ") for " + "the service (" + serviceName + ") does not exist");
            }
          }
        }
      }

      if (!serviceSecurityConfigLoaded)
      {
        String jbossServerBaseDir = System.getProperty("jboss.home.dir");

        if (jbossServerBaseDir != null)
        {
          String configurationFilePath = jbossServerBaseDir + File.separator + "certificates" + File
              .separator + serviceName + ".ServiceSecurity";
          File configurationFile = new File(configurationFilePath);

          if (configurationFile.exists())
          {
            try
            {
              loadWebServicesSecurityConfig(serviceName, configurationFile.toURI().toURL());
              serviceSecurityConfigLoaded = true;
            }
            catch (MalformedURLException e)
            {
              throw new ServiceSecurityContextException("Failed to load the security"
                  + " configuration for the service (" + serviceName + ") from the file ("
                  + configurationFilePath + ")", e);
            }
          }
          else
          {
            if (logger.isDebugEnabled())
            {
              logger.debug("The service security configuration file (" + configurationFilePath
                  + ") for " + "the service (" + serviceName + ") does not exist");
            }
          }
        }
      }

      if (!serviceSecurityConfigLoaded)
      {
        String catalinaHome = System.getProperty("catalina.home");

        if (catalinaHome != null)
        {
          String configurationFilePath = catalinaHome + File.separator + "certificates" + File
              .separator + serviceName + ".ServiceSecurity";
          File configurationFile = new File(configurationFilePath);

          if (configurationFile.exists())
          {
            try
            {
              loadWebServicesSecurityConfig(serviceName, configurationFile.toURI().toURL());
              serviceSecurityConfigLoaded = true;
            }
            catch (MalformedURLException e)
            {
              throw new ServiceSecurityContextException("Failed to load the security"
                  + " configuration for the service (" + serviceName + ") from the file ("
                  + configurationFilePath + ")", e);
            }
          }
          else
          {
            if (logger.isDebugEnabled())
            {
              logger.debug("The service security configuration file (" + configurationFilePath
                  + ") for " + "the service (" + serviceName + ") does not exist");
            }
          }
        }
      }

      // Attempt to find the service security configuration file using the class loader
      if (!serviceSecurityConfigLoaded)
      {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL configurationFileUrl = classLoader.getResource(serviceName + ".ServiceSecurity");

        if (configurationFileUrl == null)
        {
          configurationFileUrl = classLoader.getResource("META-INF/" + serviceName
              + ".ServiceSecurity");
        }

        if (configurationFileUrl != null)
        {
          loadWebServicesSecurityConfig(serviceName, configurationFileUrl);
          serviceSecurityConfigLoaded = true;
        }
      }

      if (serviceSecurityConfigLoaded)
      {
        init(keyStoreName, keyStoreAlias, keyStorePassword);
      }
    }
    catch (Throwable e)
    {
      throw new ServiceSecurityContextException(
          "Failed to initialise the ServiceSecurityContext for the service (" + serviceName + ")");
    }
  }

  /**
   * Returns the <code>ServiceSecurityContext</code> instance for the service with the specified
   * name that holds the security information for the service.
   * <p>
   * This information includes the path to the key store that holds the service's private/public
   * key pair, the alias of the public/private key pair in the key store and the password used to
   * retrieve the public/private key pair.
   * </p>
   * <p>
   * The following locations are searched to find the file <b>serviceName.ServiceSecurity</b>
   * in order:
   * <p/>
   * <ol>
   * <li>The ${was.install.root}/certificates directory when running under WebSphere.</li>
   * <li>The ${jboss.home.dir}/certificates directory when running under JBoss.</li>
   * <li>The ${catalina.home}/certificates directory when running under Tomcat.</li>
   * <li>Under the "/" path in the CLASSPATH.</li>
   * <li>Under the "/META-INF" path in the CLASSPATH.</li>
   * </ol>
   * </p>
   *
   * @param serviceName the name of the service used to derive the name of the security
   *                    configuration file
   *
   * @return the service security context for the service with the specified name
   */
  public static synchronized ServiceSecurityContext getContext(String serviceName)
  {
    ServiceSecurityContext serviceSecurityContext = serviceSecurityContexts.get(serviceName);

    if (serviceSecurityContext == null)
    {
      serviceSecurityContext = new ServiceSecurityContext(serviceName);

      serviceSecurityContexts.put(serviceName, serviceSecurityContext);
    }

    return serviceSecurityContext;
  }

  /**
   * Returns the key store containing the service's private/public key pair.
   *
   * @return the key store containing the service's private/public key pair
   */
  public KeyStore getKeyStore()
  {
    return keyStore;
  }

  /**
   * Returns the alias that identifies the service's private/public key pair in the key store.
   *
   * @return the alias that identifies the service's private/public key pair in the key store
   */
  public String getKeyStoreAlias()
  {
    return keyStoreAlias;
  }

  /**
   * Returns the certificates loaded from the service's key store.
   *
   * @return the certificates loaded from the service's key store
   */
  public List<Certificate> getKeyStoreCertificates()
  {
    return new ArrayList<>(keyStoreCertificates.values());
  }

  /**
   * Returns the name of the key store file containing the service's private/public key pair.
   *
   * @return the name of the key store file containing the service's private/public key pair
   */
  public String getKeyStoreName()
  {
    return keyStoreName;
  }

  /**
   * Returns the password used to retrieve the service's private/public key pair from the
   * key store.
   *
   * @return the password used to retrieve the service's private/public key pair from the
   * key store
   */
  public String getKeyStorePassword()
  {
    return keyStorePassword;
  }

  /**
   * Returns the certificate for the service retrieved from the service's key store.
   *
   * @return the certificate for the service retrieved from the service's key store
   */
  public Certificate getServiceCertificate()
  {
    return serviceCertificate;
  }

  /**
   * Returns the DN for the service specified in the certificate for the service
   * retrieved from the service's key store.
   *
   * @return the DN for the service specified in the certificate for the service
   * retrieved from the service's key store
   */
  public String getServiceDN()
  {
    return serviceCertificate.getSubjectDN().getName();
  }

  /**
   * Returns the private key for the service retrieved from the service's key store.
   *
   * @return the private key for the service retrieved from the service's key store
   */
  public Key getServicePrivateKey()
  {
    return servicePrivateKey;
  }

  /**
   * Check whether the configuration information required to initialise the service security
   * context exists.
   *
   * @param serviceName the name of the service used to derive the name of the security
   *                    configuration file
   *
   * @return <code>true</code> if the configuration information exists or <code>false</code>
   * otherwise
   */
  private static boolean exists(String serviceName)
  {
    /*
     * The following locations are searched to find the configuration file:
     *
     * 1. The ${was.install.root}/certificates directory when running under WebSphere.
     * 2. The ${jboss.home.dir}/certificates directory when running under JBoss.
     * 3. The ${catalina.home}/certificates directory when running under Tomcat.
     * 4. Under the "/" path in the CLASSPATH.
     * 5. Under the "/META-INF" path in the CLASSPATH.
     */
    String wasInstallRoot = System.getProperty("was.install.root");

    if (wasInstallRoot != null)
    {
      String configurationFilePath = wasInstallRoot + File.separator + "certificates" + File
          .separator + serviceName + ".ServiceSecurity";
      File configurationFile = new File(configurationFilePath);

      if (configurationFile.exists())
      {
        return true;
      }
    }

    String jbossServerBaseDir = System.getProperty("jboss.home.dir");

    if (jbossServerBaseDir != null)
    {
      String configurationFilePath = jbossServerBaseDir + File.separator + "certificates" + File
          .separator + serviceName + ".ServiceSecurity";
      File configurationFile = new File(configurationFilePath);

      if (configurationFile.exists())
      {
        return true;
      }
    }

    String catalinaHome = System.getProperty("catalina.home");

    if (catalinaHome != null)
    {
      String configurationFilePath = catalinaHome + File.separator + "certificates" + File
          .separator + serviceName + ".ServiceSecurity";
      File configurationFile = new File(configurationFilePath);

      if (configurationFile.exists())
      {
        return true;
      }
    }

    // Attempt to find the application security configuration file using the class loader
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    URL configurationFileUrl = classLoader.getResource(serviceName + ".ServiceSecurity");

    if (configurationFileUrl == null)
    {
      configurationFileUrl = classLoader.getResource("META-INF/" + serviceName
          + ".ServiceSecurity");
    }

    return configurationFileUrl != null;
  }

  /**
   * Initialise the service security context.
   *
   * @param keyStoreName     the name of the key store containing the service's private/public
   *                         key pair
   * @param keyStoreAlias    the alias that identifies the service's private/public key pair in
   *                         the key store
   * @param keyStorePassword the password used to retrieve the service's private/public key
   *                         pair from the key store
   *
   * @throws ServiceSecurityContextException
   */
  private void init(String keyStoreName, String keyStoreAlias, String keyStorePassword)
    throws ServiceSecurityContextException
  {
    boolean keyStoreLoaded = false;

    this.keyStoreName = keyStoreName;
    this.keyStoreAlias = keyStoreAlias;
    this.keyStorePassword = keyStorePassword;
    this.keyStoreCertificates = new HashMap<>();

    // Attempt to work out the type of key store we are dealing with from its file extension
    String keyStoreType;

    if (keyStoreName.toLowerCase().endsWith(".jks"))
    {
      keyStoreType = "jks";
    }
    else if (keyStoreName.toLowerCase().endsWith(".p12"))
    {
      keyStoreType = "pkcs12";
    }
    else
    {
      throw new ServiceSecurityContextException("Failed to initialise the service"
          + " security context: Unable to determine the key " + "store type for the service"
          + " key store (" + keyStoreName + ")");
    }

    /*
     * If we are running under WebSphere, attempt to find the key store file under the
     * ${was.install.root}/certificates directory.
     */
    if (!keyStoreLoaded)
    {
      String wasInstallRoot = System.getProperty("was.install.root");

      if (wasInstallRoot != null)
      {
        String keyStorePath = wasInstallRoot + File.separator + "certificates" + File.separator
            + keyStoreName;
        File keyStoreFile = new File(keyStorePath);

        if (keyStoreFile.exists())
        {
          try
          {
            this.keyStore = loadServiceKeyStore(keyStoreFile.toURI().toURL(), keyStoreName,
                keyStorePassword, keyStorePath, null, keyStoreType);
            keyStoreLoaded = true;
          }
          catch (Throwable e)
          {
            throw new ServiceSecurityContextException("Failed to initialise the service"
                + " security context: " + e.getMessage(), e);
          }
        }
        else
        {
          if (logger.isDebugEnabled())
          {
            logger.debug("The service key store (" + keyStorePath + ") does not exist");
          }
        }
      }
    }

    /*
     * If we are running under JBoss, attempt to find the key store file under the
     * ${jboss.home.dir}/certificates directory.
     */
    if (!keyStoreLoaded)
    {
      String jbossServerBaseDir = System.getProperty("jboss.home.dir");

      if (jbossServerBaseDir != null)
      {
        String keyStorePath = jbossServerBaseDir + File.separator + "certificates" + File.separator
            + keyStoreName;
        File keyStoreFile = new File(keyStorePath);

        if (keyStoreFile.exists())
        {
          try
          {
            this.keyStore = loadServiceKeyStore(keyStoreFile.toURI().toURL(), keyStoreName,
                keyStorePassword, keyStorePath, null, keyStoreType);
            keyStoreLoaded = true;
          }
          catch (Throwable e)
          {
            throw new ServiceSecurityContextException("Failed to initialise the service"
                + " security context: " + e.getMessage(), e);
          }
        }
        else
        {
          if (logger.isDebugEnabled())
          {
            logger.debug("The service key store (" + keyStorePath + ") does not exist");
          }
        }
      }
    }

    /*
     * If we are running under Tomcat, attempt to find the key store file under the
     * ${catalina.home}/certificates directory.
     */
    if (!keyStoreLoaded)
    {
      String catalinaHome = System.getProperty("catalina.home");

      if (catalinaHome != null)
      {
        String keyStorePath = catalinaHome + File.separator + "certificates" + File.separator
            + keyStoreName;
        File keyStoreFile = new File(keyStorePath);

        if (keyStoreFile.exists())
        {
          try
          {
            this.keyStore = loadServiceKeyStore(keyStoreFile.toURI().toURL(), keyStoreName,
                keyStorePassword, keyStorePath, null, keyStoreType);
            keyStoreLoaded = true;
          }
          catch (Throwable e)
          {
            throw new ServiceSecurityContextException("Failed to initialise the service"
                + " security context: " + e.getMessage(), e);
          }
        }
        else
        {
          if (logger.isDebugEnabled())
          {
            logger.debug("The service key store (" + keyStorePath + ") does not exist");
          }
        }
      }
    }

    // Attempt to find the key store file using the class loader
    if (!keyStoreLoaded)
    {
      ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
      URL keyStoreUrl = classLoader.getResource(keyStoreName);

      if (keyStoreUrl == null)
      {
        keyStoreUrl = classLoader.getResource("META-INF/" + keyStoreName);
      }

      if (keyStoreUrl != null)
      {
        try
        {
          this.keyStore = loadServiceKeyStore(keyStoreUrl, keyStoreName, keyStorePassword,
              keyStoreUrl.toString(), null, keyStoreType);
          keyStoreLoaded = true;
        }
        catch (Throwable e)
        {
          throw new ServiceSecurityContextException("Failed to initialise the service"
              + " security context", e);
        }
      }
    }

    // If we could not retrieve the keystore for the service using any of the methods above
    if (!keyStoreLoaded)
    {
      throw new ServiceSecurityContextException("Failed to initialise the service"
          + " security context: Unable to find the service key" + " store (" + keyStoreName + ")");
    }
    else
    {
      logger.info("Successfully initialised the service security context using the key store ("
          + keyStoreName + ")");
    }
  }

  /**
   * Loads the key store from the file given by the specified <code>URL</code>.
   *
   * @param keyStoreUrl      the <code>URL</code> giving the location of the keystore to load
   * @param keyStoreName     the name of the key store
   * @param keyStorePassword the key store password
   * @param keyStorePath     the path to the key store
   * @param provider         the crypto provider to use
   * @param type             the type of key store e.g.
   *
   * @return the key store that was loaded from the <code>InputStream </code>
   *
   * @throws GeneralSecurityException
   */
  private KeyStore loadServiceKeyStore(URL keyStoreUrl, String keyStoreName,
      String keyStorePassword, String keyStorePath, String provider, String type)
    throws GeneralSecurityException
  {
    KeyStore ks;

    if (logger.isDebugEnabled())
    {
      logger.debug("Loading the service key (" + keyStoreAlias + ") from the keystore ("
          + keyStorePath + ")");
    }

    InputStream input = null;

    try
    {
      if ((provider == null) || (provider.length() == 0))
      {
        ks = KeyStore.getInstance(type);
      }
      else
      {
        ks = KeyStore.getInstance(type, provider);
      }

      input = keyStoreUrl.openStream();
      ks.load(input,
          ((keyStorePassword == null) || (keyStorePassword.length() == 0))
          ? new char[0]
          : keyStorePassword.toCharArray());

      // Attempt to retrieve the private key for the service from the key store
      servicePrivateKey = ks.getKey(keyStoreAlias, keyStorePassword.toCharArray());

      if (servicePrivateKey == null)
      {
        throw new GeneralSecurityException("A private key for the service with alias ("
            + keyStoreAlias + ") could not be found " + "in the key store (" + keyStorePath + ")");
      }

      // Attempt to retrieve the certificate for the service from the key store
      Certificate tmpCertificate = ks.getCertificate(keyStoreAlias);

      if (tmpCertificate == null)
      {
        throw new GeneralSecurityException("A certificate for the service with alias ("
            + keyStoreAlias + ") could not be found " + "in the key store (" + keyStorePath + ")");
      }

      if (!(tmpCertificate instanceof X509Certificate))
      {
        throw new GeneralSecurityException("The certificate for the service with alias ("
            + keyStoreAlias + ") is not an X509 " + "certificate");
      }

      serviceCertificate = (X509Certificate) tmpCertificate;

      // Retrieve the other certificates from the key store that will be used to verify trust
      Enumeration<String> aliases = ks.aliases();

      while (aliases.hasMoreElements())
      {
        String alias = aliases.nextElement();

        /*
         * If the alias identifies the private key and certificate for the service then
         * save them now for later reference.
         */
        if (!alias.equalsIgnoreCase(keyStoreAlias))
        {
          // If this is a certificate, save it in the list of certificates
          if (ks.isCertificateEntry(alias))
          {
            keyStoreCertificates.put(alias, ks.getCertificate(alias));
          }
        }
      }

      return ks;
    }
    catch (Throwable e)
    {
      throw new GeneralSecurityException("Failed to load and query the service key store ("
          + keyStoreName + ")", e);
    }
    finally
    {
      try
      {
        if (input != null)
        {
          input.close();
        }
      }
      catch (Throwable ignored) {}
    }
  }

  /**
   * Load the security configuration information for the service from the file given by the
   * specified URL.
   *
   * @param serviceName the name of the service whose security configuration information
   *                    will be loaded
   * @param url         the URL giving the location of the configuration file
   *
   * @throws ServiceSecurityContextException
   */
  private void loadWebServicesSecurityConfig(String serviceName, URL url)
    throws ServiceSecurityContextException
  {
    if (logger.isDebugEnabled())
    {
      logger.debug("Reading the service security configuration information for the service ("
          + serviceName + ") from the file (" + url.toString() + ")");
    }

    try
    {
      // Retrieve a document builder instance using the factory
      DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();

      builderFactory.setValidating(true);

      // builderFactory.setNamespaceAware(true);
      DocumentBuilder builder = builderFactory.newDocumentBuilder();

      builder.setEntityResolver(new DtdJarResolver("ServiceSecurity.dtd",
          "META-INF/ServiceSecurity.dtd"));
      builder.setErrorHandler(new XmlParserErrorHandler());

      // Parse the XML service security configuration file using the document builder
      InputSource inputSource = new InputSource(url.openStream());
      Document document = builder.parse(inputSource);
      Element rootElement = document.getDocumentElement();

      this.keyStoreName = XmlUtils.getChildElementText(rootElement, "keyStoreName");
      this.keyStoreAlias = XmlUtils.getChildElementText(rootElement, "keyStoreAlias");
      this.keyStorePassword = XmlUtils.getChildElementText(rootElement, "keyStorePassword");
    }
    catch (Throwable e)
    {
      throw new ServiceSecurityContextException("Failed to load the service security"
          + " configuration for the service (" + serviceName + ") from the file (" + url.toString()
          + ")", e);
    }
  }
}

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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * The <code>ApplicationSecurityContext</code> class implements the singleton that holds the
 * security information for an application. This information includes the path to the
 * key store that holds the application's private/public key pair, the alias of the public/private
 * key pair in the key store and the password used to retrieve the public/private key.
 *
 * The <code>ApplicationSecurityContext</code> MUST be initialised BEFORE any secured operations
 * are performed by the application e.g. secure web service operations are invoked, secure CORBA
 * services are invoked, etc.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public class ApplicationSecurityContext
{
  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(ApplicationSecurityContext.class);

  /**
   * The application security context singleton.
   */
  private static ApplicationSecurityContext singleton;

  /**
   * The certificate for the application retrieved from the application's key store.
   */
  private X509Certificate applicationCertificate;

  /**
   * The private key for the application retrieved from the application's key store.
   */
  private Key applicationPrivateKey;

  /**
   * Has the application security context been initialised?
   */
  private boolean initialised;

  /**
   * The key store containing the application's private/public key pair.
   */
  private KeyStore keyStore;

  /**
   * The alias that identifies the application's private/public key pair in the key store.
   */
  private String keyStoreAlias;

  /**
   * The certificates loaded from the application's key store.
   */
  private Map<String, Certificate> keyStoreCertificates;

  /**
   * The name of the key store file containing the application's private/public key pair.
   */
  private String keyStoreName;

  /**
   * The password used to retrieve the application's private/public key pair from the key store.
   */
  private String keyStorePassword;

  /**
   * Singleton accessor.
   *
   * @return the application security context singleton
   */
  public static synchronized ApplicationSecurityContext getContext()
  {
    if (singleton == null)
    {
      singleton = new ApplicationSecurityContext();
    }

    return singleton;
  }

  /**
   * Check whether the configuration information required to initialise the application security
   * context exists.
   *
   * @param applicationName the name of the application used to derive the name of the security
   *                        configuration file for the application
   *
   * @return <code>true</code> if the configuration information exists or <code>false</code>
   *         otherwise
   */
  public boolean exists(String applicationName)
  {
    if (applicationName == null)
    {
      return false;
    }

    /*
     * The following locations are searched to find the configuration file:
     *
     * 1. The ${was.install.root}/certificates directory when running under WebSphere.
     * 2. The ${jboss.home.dir}/certificates directory when running under JBoss.
     * 3. The ${catalina.home}/certificates directory when running under Tomcat.
     * 4. The ${org.apache.geronimo.server.dir}/certificates directory when running under Geronimo.
     * 5. The ${wlp.user.dir}/certificates directory when running under the WAS Liberty profile.
     * 6. Under the "/" path in the CLASSPATH.
     * 7. Under the "/META-INF" path in the CLASSPATH.
     */
    String wasInstallRoot = System.getProperty("was.install.root");

    if (wasInstallRoot != null)
    {
      String configurationFilePath = wasInstallRoot + File.separator + "certificates"
        + File.separator + applicationName + ".ApplicationSecurity";
      File configurationFile = new File(configurationFilePath);

      if (configurationFile.exists())
      {
        return true;
      }
    }

    String jbossServerBaseDir = System.getProperty("jboss.home.dir");

    if (jbossServerBaseDir != null)
    {
      String configurationFilePath = jbossServerBaseDir + File.separator + "certificates"
        + File.separator + applicationName + ".ApplicationSecurity";
      File configurationFile = new File(configurationFilePath);

      if (configurationFile.exists())
      {
        return true;
      }
    }

    String catalinaHome = System.getProperty("catalina.home");

    if (catalinaHome != null)
    {
      String configurationFilePath = catalinaHome + File.separator + "certificates"
        + File.separator + applicationName + ".ApplicationSecurity";
      File configurationFile = new File(configurationFilePath);

      if (configurationFile.exists())
      {
        return true;
      }
    }

    String geronimoServerDir = System.getProperty("org.apache.geronimo.server.dir");

    if (geronimoServerDir != null)
    {
      String configurationFilePath = geronimoServerDir + File.separator + "certificates"
        + File.separator + applicationName + ".ApplicationSecurity";
      File configurationFile = new File(configurationFilePath);

      if (configurationFile.exists())
      {
        return true;
      }
    }

    String wlpUserDir = System.getProperty("wlp.user.dir");

    if (wlpUserDir != null)
    {
      String configurationFilePath = wlpUserDir + File.separator + "certificates" + File.separator
        + applicationName + ".ApplicationSecurity";
      File configurationFile = new File(configurationFilePath);

      if (configurationFile.exists())
      {
        return true;
      }
    }

    // Attempt to find the application security configuration file using the class loader
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    URL configurationFileUrl = classLoader.getResource(applicationName + ".ApplicationSecurity");

    if (configurationFileUrl == null)
    {
      configurationFileUrl = classLoader.getResource("META-INF/" + applicationName
          + ".ApplicationSecurity");
    }

    return configurationFileUrl != null;
  }

  /**
   * Returns the certificate for the application retrieved from the application's key store.
   *
   * @return the certificate for the application retrieved from the application's key store
   */
  public Certificate getApplicationCertificate()
  {
    return applicationCertificate;
  }

  /**
   * Returns the DN for the application specified in the certificate for the application
   * retrieved from the application's key store.
   *
   * @return the DN for the application specified in the certificate for the application
   *         retrieved from the application's key store
   */
  public String getApplicationDN()
  {
    return applicationCertificate.getSubjectDN().getName();
  }

  /**
   * Returns the private key for the application retrieved from the application's key store.
   *
   * @return the private key for the application retrieved from the application's key store
   */
  public Key getApplicationPrivateKey()
  {
    return applicationPrivateKey;
  }

  /**
   * Returns the key store containing the application's private/public key pair.
   *
   * @return the key store containing the application's private/public key pair
   */
  public KeyStore getKeyStore()
  {
    return keyStore;
  }

  /**
   * Returns the alias that identifies the application's private/public key pair in the key store.
   *
   * @return the alias that identifies the application's private/public key pair in the key store
   */
  public String getKeyStoreAlias()
  {
    return keyStoreAlias;
  }

  /**
   * Returns the certificates loaded from the application's key store.
   *
   * @return the certificates loaded from the application's key store
   */
  public List<Certificate> getKeyStoreCertificates()
  {
    return new ArrayList<>(keyStoreCertificates.values());
  }

  /**
   * Returns the name of the key store file containing the application's private/public key pair.
   *
   * @return the name of the key store file containing the application's private/public key pair
   */
  public String getKeyStoreName()
  {
    return keyStoreName;
  }

  /**
   * Returns the password used to retrieve the application's private/public key pair from the
   * key store.
   *
   * @return the password used to retrieve the application's private/public key pair from the
   *         key store
   */
  public String getKeyStorePassword()
  {
    return keyStorePassword;
  }

  /**
   * Initialise the application security context.
   *
   * @param applicationName the name of the application used to derive the name of the security
   *                        configuration file for the application
   *
   * @throws ApplicationSecurityContextException
   */
  public void init(String applicationName)
    throws ApplicationSecurityContextException
  {
    boolean applicationSecurityConfigLoaded = false;

    /*
     * Retrieve the configuration file containing the security configuration for the application.
     * This file contains the name of the key store for the application, the alias of the
     * public/private key pair for the application in the key store and the password used to
     * access the key store.
     *
     * The following locations are searched to find the configuration file:
     *
     * 1. The ${was.install.root}/certificates directory when running under WebSphere.
     * 2. The ${jboss.home.dir}/certificates directory when running under JBoss.
     * 3. The ${catalina.home}/certificates directory when running under Tomcat.
     * 4. The ${org.apache.geronimo.server.dir}/certificates directory when running under Geronimo.
     * 5. The ${wlp.user.dir}/certificates directory when running under the WAS Liberty profile.
     * 6. Under the "/" path in the CLASSPATH.
     * 7. Under the "/META-INF" path in the CLASSPATH.
     */
    if (!applicationSecurityConfigLoaded)
    {
      String wasInstallRoot = System.getProperty("was.install.root");

      if (wasInstallRoot != null)
      {
        String configurationFilePath = wasInstallRoot + File.separator + "certificates"
          + File.separator + applicationName + ".ApplicationSecurity";
        File configurationFile = new File(configurationFilePath);

        if (configurationFile.exists())
        {
          try
          {
            loadWebServicesSecurityConfig(applicationName, configurationFile.toURI().toURL());
            applicationSecurityConfigLoaded = true;
          }
          catch (MalformedURLException e)
          {
            throw new ApplicationSecurityContextException("Failed to load the security"
                + " configuration for the application (" + applicationName + ") from the file ("
                + configurationFilePath + ")", e);
          }
        }
        else
        {
          if (logger.isDebugEnabled())
          {
            logger.debug("The application security configuration file (" + configurationFilePath
                + ") for the application (" + applicationName + ") does not exist");
          }
        }
      }
    }

    if (!applicationSecurityConfigLoaded)
    {
      String jbossServerBaseDir = System.getProperty("jboss.home.dir");

      if (jbossServerBaseDir != null)
      {
        String configurationFilePath = jbossServerBaseDir + File.separator + "certificates"
          + File.separator + applicationName + ".ApplicationSecurity";
        File configurationFile = new File(configurationFilePath);

        if (configurationFile.exists())
        {
          try
          {
            loadWebServicesSecurityConfig(applicationName, configurationFile.toURI().toURL());
            applicationSecurityConfigLoaded = true;
          }
          catch (MalformedURLException e)
          {
            throw new ApplicationSecurityContextException("Failed to load the security"
                + " configuration for the application (" + applicationName + ") from the file ("
                + configurationFilePath + ")", e);
          }
        }
        else
        {
          if (logger.isDebugEnabled())
          {
            logger.debug("The application security configuration file (" + configurationFilePath
                + ") for the application (" + applicationName + ") does not exist");
          }
        }
      }
    }

    if (!applicationSecurityConfigLoaded)
    {
      String catalinaHome = System.getProperty("catalina.home");

      if (catalinaHome != null)
      {
        String configurationFilePath = catalinaHome + File.separator + "certificates"
          + File.separator + applicationName + ".ApplicationSecurity";
        File configurationFile = new File(configurationFilePath);

        if (configurationFile.exists())
        {
          try
          {
            loadWebServicesSecurityConfig(applicationName, configurationFile.toURI().toURL());
            applicationSecurityConfigLoaded = true;
          }
          catch (MalformedURLException e)
          {
            throw new ApplicationSecurityContextException("Failed to load the security"
                + " configuration for the application (" + applicationName + ") from the file ("
                + configurationFilePath + ")", e);
          }
        }
        else
        {
          if (logger.isDebugEnabled())
          {
            logger.debug("The application security configuration file (" + configurationFilePath
                + ") for the application (" + applicationName + ") does not exist");
          }
        }
      }
    }

    if (!applicationSecurityConfigLoaded)
    {
      String geronimoServerDir = System.getProperty("org.apache.geronimo.server.dir");

      if (geronimoServerDir != null)
      {
        String configurationFilePath = geronimoServerDir + File.separator + "certificates"
          + File.separator + applicationName + ".ApplicationSecurity";
        File configurationFile = new File(configurationFilePath);

        if (configurationFile.exists())
        {
          try
          {
            loadWebServicesSecurityConfig(applicationName, configurationFile.toURI().toURL());
            applicationSecurityConfigLoaded = true;
          }
          catch (MalformedURLException e)
          {
            throw new ApplicationSecurityContextException("Failed to load the security"
                + " configuration for the application (" + applicationName + ") from the file ("
                + configurationFilePath + ")", e);
          }
        }
        else
        {
          if (logger.isDebugEnabled())
          {
            logger.debug("The application security configuration file (" + configurationFilePath
                + ") for the application (" + applicationName + ") does not exist");
          }
        }
      }
    }

    if (!applicationSecurityConfigLoaded)
    {
      String wlpUserDir = System.getProperty("wlp.user.dir");

      if (wlpUserDir != null)
      {
        String configurationFilePath = wlpUserDir + File.separator + "certificates"
          + File.separator + applicationName + ".ApplicationSecurity";
        File configurationFile = new File(configurationFilePath);

        if (configurationFile.exists())
        {
          try
          {
            loadWebServicesSecurityConfig(applicationName, configurationFile.toURI().toURL());
            applicationSecurityConfigLoaded = true;
          }
          catch (MalformedURLException e)
          {
            throw new ApplicationSecurityContextException("Failed to load the security"
                + " configuration for the application (" + applicationName + ") from the file ("
                + configurationFilePath + ")", e);
          }
        }
        else
        {
          if (logger.isDebugEnabled())
          {
            logger.debug("The application security configuration file (" + configurationFilePath
                + ") for the application (" + applicationName + ") does not exist");
          }
        }
      }
    }

    // Attempt to find the application security configuration file using the class loader
    if (!applicationSecurityConfigLoaded)
    {
      ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
      URL configurationFileUrl = classLoader.getResource(applicationName + ".ApplicationSecurity");

      if (configurationFileUrl == null)
      {
        configurationFileUrl = classLoader.getResource("META-INF/" + applicationName
            + ".ApplicationSecurity");
      }

      if (configurationFileUrl != null)
      {
        loadWebServicesSecurityConfig(applicationName, configurationFileUrl);
        applicationSecurityConfigLoaded = true;
      }
    }

    if (applicationSecurityConfigLoaded)
    {
      init(keyStoreName, keyStoreAlias, keyStorePassword);
    }
  }

  /**
   * Initialise the application security context.
   *
   * @param keyStoreName     the name of the key store containing the application's private/public
   *                         key pair
   * @param keyStoreAlias    the alias that identifies the application's private/public key pair in
   *                         the key store
   * @param keyStorePassword the password used to retrieve the application's private/public key
   *                         pair from the key store
   *
   * @throws ApplicationSecurityContextException
   */
  public void init(String keyStoreName, String keyStoreAlias, String keyStorePassword)
    throws ApplicationSecurityContextException
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
      throw new ApplicationSecurityContextException("Failed to initialise the application"
          + " security context: Unable to determine the key store type for the application"
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
            this.keyStore = loadApplicationKeyStore(keyStoreFile.toURI().toURL(), keyStoreName,
                keyStorePassword, keyStorePath, null, keyStoreType);
            keyStoreLoaded = true;
          }
          catch (Throwable e)
          {
            throw new ApplicationSecurityContextException("Failed to initialise the application"
                + " security context: " + e.getMessage(), e);
          }
        }
        else
        {
          if (logger.isDebugEnabled())
          {
            logger.debug("The application key store (" + keyStorePath + ") does not exist");
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
            this.keyStore = loadApplicationKeyStore(keyStoreFile.toURI().toURL(), keyStoreName,
                keyStorePassword, keyStorePath, null, keyStoreType);
            keyStoreLoaded = true;
          }
          catch (Throwable e)
          {
            throw new ApplicationSecurityContextException("Failed to initialise the application"
                + " security context: " + e.getMessage(), e);
          }
        }
        else
        {
          if (logger.isDebugEnabled())
          {
            logger.debug("The application key store (" + keyStorePath + ") does not exist");
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
            this.keyStore = loadApplicationKeyStore(keyStoreFile.toURI().toURL(), keyStoreName,
                keyStorePassword, keyStorePath, null, keyStoreType);
            keyStoreLoaded = true;
          }
          catch (Throwable e)
          {
            throw new ApplicationSecurityContextException("Failed to initialise the application"
                + " security context: " + e.getMessage(), e);
          }
        }
        else
        {
          if (logger.isDebugEnabled())
          {
            logger.debug("The application key store (" + keyStorePath + ") does not exist");
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
          this.keyStore = loadApplicationKeyStore(keyStoreUrl, keyStoreName, keyStorePassword,
              keyStoreUrl.toString(), null, keyStoreType);
          keyStoreLoaded = true;
        }
        catch (Throwable e)
        {
          throw new ApplicationSecurityContextException("Failed to initialise the application"
              + " security context", e);
        }
      }
    }

    // If we could not retrieve the keystore for the application using any of the methods above
    if (!keyStoreLoaded)
    {
      throw new ApplicationSecurityContextException("Failed to initialise the application"
          + " security context: Unable to find the application key store (" + keyStoreName + ")");
    }
    else
    {
      logger.info("Successfully initialised the application security context using the key store ("
          + keyStoreName + ")");
    }

    this.initialised = true;
  }

  /**
   * Initialise the application security context.
   *
   * @param keyStoreName           the name of the key store file containing the application's
   *                               private/public key pair
   * @param keyStore               the key store containing the application's private/public key
   *                               pair
   * @param keyStorePassword       the password used to retrieve the application's private/public
   *                               key pair from the key store
   * @param keyStoreAlias          the alias that identifies the application's private/public key
   *                               pair in the key store
   * @param applicationCertificate certificate for the application retrieved from the application's
   *                               key store
   * @param applicationPrivateKey  the private key for the application retrieved from the
   *                               application's key store
   * @param keyStoreCertificates   the certificates loaded from the application's key store.
   *
   * @throws ApplicationSecurityContextException
   */
  public void init(String keyStoreName, KeyStore keyStore, String keyStorePassword,
      String keyStoreAlias, X509Certificate applicationCertificate, Key applicationPrivateKey,
      Map<String, Certificate> keyStoreCertificates)
    throws ApplicationSecurityContextException
  {
    this.keyStoreName = keyStoreName;
    this.keyStore = keyStore;
    this.keyStorePassword = keyStorePassword;
    this.keyStoreAlias = keyStoreAlias;
    this.applicationCertificate = applicationCertificate;
    this.applicationPrivateKey = applicationPrivateKey;
    this.keyStoreCertificates = keyStoreCertificates;

    initialised = true;
  }

  /**
   * Returns <code>true</code> if the application security context has been initialised or
   * <code>false</code> otherwise.
   *
   * @return <code>true</code> if the application security context has been initialised or
   *         <code>false</code> otherwise
   */
  public boolean isInitialised()
  {
    return initialised;
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
  private KeyStore loadApplicationKeyStore(URL keyStoreUrl, String keyStoreName,
      String keyStorePassword, String keyStorePath, String provider, String type)
    throws GeneralSecurityException
  {
    KeyStore ks;

    if (logger.isDebugEnabled())
    {
      logger.debug("Loading the application key (" + keyStoreAlias + ") from the keystore ("
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
      ks.load(input, ((keyStorePassword == null) || (keyStorePassword.length() == 0))
          ? new char[0]
          : keyStorePassword.toCharArray());

      // Attempt to retrieve the private key for the application from the key store
      applicationPrivateKey = ks.getKey(keyStoreAlias, keyStorePassword.toCharArray());

      if (applicationPrivateKey == null)
      {
        throw new GeneralSecurityException("A private key for the application with alias ("
            + keyStoreAlias + ") could not be found in the key store (" + keyStorePath + ")");
      }

      // Attempt to retrieve the certificate for the application from the key store
      Certificate tmpCertificate = ks.getCertificate(keyStoreAlias);

      if (tmpCertificate == null)
      {
        throw new GeneralSecurityException("A certificate for the application with alias ("
            + keyStoreAlias + ") could not be found in the key store (" + keyStorePath + ")");
      }

      if (!(tmpCertificate instanceof X509Certificate))
      {
        throw new GeneralSecurityException("The certificate for the application with alias ("
            + keyStoreAlias + ") is not an X509 certificate");
      }

      applicationCertificate = (X509Certificate) tmpCertificate;

      // Retrieve the other certificates from the key store that will be used to verify trust
      Enumeration<String> aliases = ks.aliases();

      while (aliases.hasMoreElements())
      {
        String alias = aliases.nextElement();

        /*
         * If the alias identifies the private key and certificate for the application then
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
      throw new GeneralSecurityException("Failed to load and query the application key store ("
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
   * Load the security configuration information for the application from the file given by the
   * specified URL.
   *
   * @param applicationName the name of the application whose security configuration information
   *                        will be loaded
   * @param url             the URL giving the location of the configuration file
   *
   * @throws ApplicationSecurityContextException
   */
  private void loadWebServicesSecurityConfig(String applicationName, URL url)
    throws ApplicationSecurityContextException
  {
    if (logger.isDebugEnabled())
    {
      logger.debug("Reading the application security configuration information for the"
          + " application (" + applicationName + ") from the file (" + url.toString() + ")");
    }

    try
    {
      // Retrieve a document builder instance using the factory
      DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();

      builderFactory.setValidating(true);

      // builderFactory.setNamespaceAware(true);
      DocumentBuilder builder = builderFactory.newDocumentBuilder();

      builder.setEntityResolver(new DtdJarResolver("ApplicationSecurity.dtd",
          "META-INF/ApplicationSecurity.dtd"));
      builder.setErrorHandler(new XmlParserErrorHandler());

      // Parse the XML application security configuration file using the document builder
      InputSource inputSource = new InputSource(url.openStream());
      Document document = builder.parse(inputSource);
      Element rootElement = document.getDocumentElement();

      this.keyStoreName = XmlUtils.getChildElementText(rootElement, "keystore-name");
      this.keyStoreAlias = XmlUtils.getChildElementText(rootElement, "keystore-alias");
      this.keyStorePassword = XmlUtils.getChildElementText(rootElement, "keystore-password");
    }
    catch (Throwable e)
    {
      throw new ApplicationSecurityContextException("Failed to load the application security"
          + " configuration for the application (" + applicationName + ") from the file ("
          + url.toString() + ")", e);
    }
  }
}

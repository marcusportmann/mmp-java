/*
 * Copyright 2014 Marcus Portmann
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

package guru.mmp.common.service.ws.security;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.common.security.context.ServiceSecurityContext;
import org.apache.ws.security.WSPasswordCallback;
import org.apache.ws.security.WSSecurityException;
import org.apache.ws.security.components.crypto.Crypto;
import org.apache.ws.security.components.crypto.CryptoType;
import org.apache.ws.security.components.crypto.DERDecoder;
import org.apache.ws.security.components.crypto.X509SubjectPublicKeyInfo;
import org.apache.ws.security.util.WSSecurityUtil;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.x500.X500Principal;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.*;
import java.security.cert.Certificate;
import java.util.*;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>WebServiceCrypto</code> class implements the WSS4J crypto operations for
 * a web service.
 */
public class WebServiceCrypto
  implements Crypto
{
  /**
   * The OID for the NameConstraints Extension to X.509
   * <p/>
   * http://java.sun.com/j2se/1.4.2/docs/api
   * http://www.ietf.org/rfc/rfc3280.txt (s. 4.2.1.11)
   */
  public static final String NAME_CONSTRAINTS_OID = "2.5.29.30";

  /**
   * The OID for the SubjectKeyIdentifier Extension to X.509.
   */
  public static final String SKI_OID = "2.5.29.14";

  /* The Bouncy Castle <code>X509Name</code> constructor. */
  static Constructor<?> BouncyCastleX509NameConstructor = null;

  static
  {
    Constructor<?> cons = null;

    try
    {
      Class<?> c = Class.forName("org.bouncycastle.asn1.x509.X509Name");

      cons = c.getConstructor(String.class);
    }
    catch (Exception e)
    {
      // ignore
    }

    BouncyCastleX509NameConstructor = cons;
  }

  /* The certificate factories. */
  protected Map<String, CertificateFactory> certificateFactoryMap = new HashMap<>();

  /**
   * The crypto provider associated with this implementation.
   */
  private String cryptoProvider = null;
  private KeyStore keyStore = null;

  /**
   * Constructs a new <code>WebServiceCrypto</code>.
   */
  public WebServiceCrypto()
  {
    ServiceSecurityContext serviceSecurityContext = ServiceSecurityContext.getContext();

    this.keyStore = serviceSecurityContext.getKeyStore();
  }

///**
// * Constructs a new <code>WebServiceCrypto</code>.
// *
// * @param properties the properties used to configure the <code>Crypto</code implementation
// *
// * @throws CredentialException
// * @throws IOException
// */
//public WebServiceCrypto(Properties properties)
//  throws CredentialException, IOException
//{
//  this(properties, Loader.getClassLoader(WebServiceCrypto.class));
//}
//
///**
// * Constructs a new <code>WebServiceCrypto</code>.
// *
// * @param properties the properties used to configure the <code>Crypto</code implementation
// * @param loader     the Java class loader
// *
// * @throws CredentialException
// * @throws IOException
// */
//public WebServiceCrypto(Properties properties, ClassLoader loader)
//  throws CredentialException, IOException
//
//{
//}

  /**
   * Get a byte array given an array of X509 certificates.
   *
   * @param certs the certificates to convert
   * @return the byte array for the certificates
   * @throws WSSecurityException
   */
  @Override
  public byte[] getBytesFromCertificates(X509Certificate[] certs)
    throws WSSecurityException
  {
    try
    {
      CertPath path = getCertificateFactory().generateCertPath(Arrays.asList(certs));

      return path.getEncoded();
    }
    catch (CertificateEncodingException e)
    {
      throw new WSSecurityException(WSSecurityException.SECURITY_TOKEN_UNAVAILABLE, "encodeError",
          null, e);
    }
    catch (CertificateException e)
    {
      throw new WSSecurityException(WSSecurityException.SECURITY_TOKEN_UNAVAILABLE, "parseError",
          null, e);
    }
  }

  /**
   * Returns the <code>CertificateFactory</code> instance for the <code>Crypto</code> instance.
   *
   * @return the <code>CertificateFactory</code> instance for the <code>Crypto</code> instance
   * @throws WSSecurityException
   */
  @Override
  public CertificateFactory getCertificateFactory()
    throws WSSecurityException
  {
    String provider = getCryptoProvider();

    /*
     * Try to find a CertificateFactory that generates certs that are fully compatible with the
     * certificates in the KeyStore  (Sun -> Sun, BC -> BC, etc...)
     */
    CertificateFactory factory;

    if ((provider != null) && (provider.length() != 0))
    {
      factory = certificateFactoryMap.get(provider);
    }
    else
    {
      factory = certificateFactoryMap.get("DEFAULT");
    }

    if (factory == null)
    {
      try
      {
        if ((provider == null) || (provider.length() == 0))
        {
          factory = CertificateFactory.getInstance("X.509");
          certificateFactoryMap.put("DEFAULT", factory);
        }
        else
        {
          factory = CertificateFactory.getInstance("X.509", provider);
          certificateFactoryMap.put(provider, factory);
        }

        certificateFactoryMap.put(factory.getProvider().getName(), factory);
      }
      catch (CertificateException e)
      {
        throw new WSSecurityException(WSSecurityException.SECURITY_TOKEN_UNAVAILABLE,
            "unsupportedCertType", null, e);
      }
      catch (NoSuchProviderException e)
      {
        throw new WSSecurityException(WSSecurityException.SECURITY_TOKEN_UNAVAILABLE,
            "noSecProvider", null, e);
      }
    }

    return factory;
  }

  /**
   * Construct an array of X509Certificate's from the byte array.
   *
   * @param data the <code>byte</code> array containing the X509 data
   * @return an array of X509 certificates
   * @throws WSSecurityException
   */
  @Override
  public X509Certificate[] getCertificatesFromBytes(byte[] data)
    throws WSSecurityException
  {
    InputStream in = new ByteArrayInputStream(data);
    CertPath path;

    try
    {
      path = getCertificateFactory().generateCertPath(in);
    }
    catch (CertificateException e)
    {
      throw new WSSecurityException(WSSecurityException.SECURITY_TOKEN_UNAVAILABLE, "parseError",
          null, e);
    }

    return (X509Certificate[])path.getCertificates().toArray();
  }

  /**
   * Get the crypto provider associated with this implementation.
   *
   * @return the crypto provider associated with this implementation
   */
  @Override
  public String getCryptoProvider()
  {
    return cryptoProvider;
  }

  /**
   * Retrieves the identifier name of the default certificate.
   * <p/>
   * This should be the certificate that is used for signature and encryption. This identifier
   * corresponds to the certificate that should be used whenever <code>KeyInfo</code> is not
   * present in a signed or an encrypted message. May return <code>null</code>. The identifier is
   * implementation specific, e.g. it could be the KeyStore alias.
   *
   * @return the identifier name of the default certificate
   * @throws WSSecurityException
   */
  @Override
  public String getDefaultX509Identifier()
    throws WSSecurityException
  {
    ServiceSecurityContext serviceSecurityContext = ServiceSecurityContext.getContext();

    return serviceSecurityContext.getKeyStoreAlias();
  }

  /**
   * Returns the key store associated with the crypto provider.
   *
   * @return the key store associated with the crypto provider
   */
  public KeyStore getKeyStore()
  {
    return this.keyStore;
  }

  /**
   * Gets the private key corresponding to the identifier.
   *
   * @param identifier the implementation-specific identifier corresponding to the key
   * @param password   the password needed to get the key
   * @return the private key corresponding to the identifier
   * @throws WSSecurityException
   */
  @Override
  public PrivateKey getPrivateKey(String identifier, String password)
    throws WSSecurityException
  {
    if ((identifier == null) || (identifier.length() == 0))
    {
      throw new WSSecurityException(
          "Failed to find the private key for a NULL or empty identifier");
    }

    try
    {
      Enumeration<String> keyStoreAliases = keyStore.aliases();

      while (keyStoreAliases.hasMoreElements())
      {
        String keyStoreAlias = keyStoreAliases.nextElement();

        /*
         *  Attempt to work around the JVM PKCS12 bug where the alias names retrieved from the
         *  key store are null '\0' terminated.
         */
        String fixedKeyStoreAlias = keyStoreAlias;
        char[] keyStoreAliasChars = keyStoreAlias.toCharArray();

        if (keyStoreAliasChars[keyStoreAliasChars.length - 1] == '\0')
        {
          fixedKeyStoreAlias = new String(keyStoreAliasChars, 0, keyStoreAliasChars.length - 1);
        }

        if (fixedKeyStoreAlias.equalsIgnoreCase(identifier))
        {
          if (!keyStore.isKeyEntry(keyStoreAlias))
          {
            throw new WSSecurityException("Failed to find the private key with the alias ("
                + identifier + "):" + " The key store entry with the alias (" + identifier
                + ") is not a private key");
          }
          else
          {
            Key keyTmp = keyStore.getKey(keyStoreAlias, password.toCharArray());

            if (!(keyTmp instanceof PrivateKey))
            {
              throw new WSSecurityException("Failed to find the private key with the alias ("
                  + identifier + "):" + " The key store entry with the alias (" + identifier
                  + ") is not a private key");
            }
            else
            {
              return (PrivateKey) keyTmp;
            }
          }
        }
      }
    }
    catch (Throwable e)
    {
      throw new WSSecurityException("Failed to find the private key with the alias (" + identifier
          + ") in the key store", e);
    }

    throw new WSSecurityException("Failed to find the private key with the alias (" + identifier
        + ") in the key store");
  }

  /**
   * Gets the private key corresponding to the certificate.
   *
   * @param certificate     the X509Certificate corresponding to the private key
   * @param callbackHandler the callbackHandler needed to get the password
   * @return the private key corresponding to the certificate
   * @throws WSSecurityException
   */
  @Override
  public PrivateKey getPrivateKey(X509Certificate certificate, CallbackHandler callbackHandler)
    throws WSSecurityException
  {
    String identifier = getIdentifier(certificate, keyStore);

    try
    {
      if ((identifier == null) || !keyStore.isKeyEntry(identifier))
      {
        throw new WSSecurityException("Failed to find the private key for the certificate ("
            + certificate.getSubjectDN().getName() + ") in the key store");
      }

      String password = getPassword(identifier, callbackHandler);
      Key keyTmp = keyStore.getKey(identifier, (password == null)
          ? new char[] {}
          : password.toCharArray());

      if (!(keyTmp instanceof PrivateKey))
      {
        throw new WSSecurityException("Failed to find the private key for the certificate ("
            + certificate.getSubjectDN().getName() + ") in the key store: Key with alias ("
            + identifier + ") is not a private key");
      }

      return (PrivateKey) keyTmp;
    }
    catch (Throwable e)
    {
      throw new WSSecurityException(WSSecurityException.FAILURE, "noPrivateKey",
          new Object[] { e.getMessage() }, e);
    }
  }

  /**
   * Reads the SubjectKeyIdentifier information from the certificate.
   * <p/>
   * If the certificate does not contain a SKI extension then try to compute the SKI according
   * to RFC3280 using the SHA-1 hash value of the public key. The second method described in
   * RFC3280 is not support. Also only RSA public keys are supported. If we cannot compute the SKI
   * throw a WSSecurityException.
   *
   * @param cert the certificate to read SKI
   * @return the <code>byte</code> array containing the binary SKI data
   * @throws WSSecurityException
   */
  @Override
  public byte[] getSKIBytesFromCert(X509Certificate cert)
    throws WSSecurityException
  {
    /*
     * Gets the DER-encoded OCTET string for the extension value (extnValue) identified by the
     * passed-in OID String. The OID string is represented by a set of positive whole numbers
     * separated by periods.
     */
    byte[] derEncodedValue = cert.getExtensionValue(SKI_OID);

    if ((cert.getVersion() < 3) || (derEncodedValue == null))
    {
      X509SubjectPublicKeyInfo spki = new X509SubjectPublicKeyInfo(cert.getPublicKey());
      byte[] value = spki.getSubjectPublicKey();

      try
      {
        return WSSecurityUtil.generateDigest(value);
      }
      catch (WSSecurityException ex)
      {
        throw new WSSecurityException(WSSecurityException.UNSUPPORTED_SECURITY_TOKEN,
            "noSKIHandling",
            new Object[] { "No SKI certificate extension and no SHA1 message digest available" },
            ex);
      }
    }

    /*
     * Strip away first (four) bytes from the DerValue (tag and length of ExtensionValue
     * OCTET STRING and KeyIdentifier OCTET STRING)
     */
    DERDecoder extVal = new DERDecoder(derEncodedValue);

    extVal.expect(DERDecoder.TYPE_OCTET_STRING);  // ExtensionValue OCTET STRING
    extVal.getLength();
    extVal.expect(DERDecoder.TYPE_OCTET_STRING);  // KeyIdentifier OCTET STRING

    int keyIDLen = extVal.getLength();

    return extVal.getBytes(keyIDLen);
  }

  /**
   * Get an <code>X509Certificate<code> (chain) corresponding to the <code>CryptoType</code>
   * argument.
   * <p/>
   * The supported types are as follows:
   * <p/>
   * TYPE.ISSUER_SERIAL - A certificate (chain) is located by the issuer name and serial number
   * TYPE.THUMBPRINT_SHA1 - A certificate (chain) is located by the SHA1 of the (root) cert
   * TYPE.SKI_BYTES - A certificate (chain) is located by the SKI bytes of the (root) cert
   * TYPE.SUBJECT_DN - A certificate (chain) is located by the Subject DN of the (root) cert
   * TYPE.ALIAS - A certificate (chain) is located by an alias, which for this implementation
   * means an alias of the keystore or truststore.
   *
   * @param cryptoType the crypto type
   * @return the <code>X509Certificate<code> or <code>null</code> if not found
   * @throws WSSecurityException
   */
  public X509Certificate[] getX509Certificates(CryptoType cryptoType)
    throws WSSecurityException
  {
    if (cryptoType == null)
    {
      return null;
    }

    CryptoType.TYPE type = cryptoType.getType();
    X509Certificate[] certs = null;

    switch (type)
    {
      case ISSUER_SERIAL:
      {
        certs = getX509Certificates(cryptoType.getIssuer(), cryptoType.getSerial());

        break;
      }

      case THUMBPRINT_SHA1:
      {
        certs = getX509Certificates(cryptoType.getBytes());

        break;
      }

      case SKI_BYTES:
      {
        certs = getX509CertificatesSKI(cryptoType.getBytes());

        break;
      }

      case SUBJECT_DN:
      {
        certs = getX509CertificatesSubjectDN(cryptoType.getSubjectDN());

        break;
      }

      case ALIAS:
      {
        certs = getX509Certificates(cryptoType.getAlias());

        break;
      }
    }

    return certs;
  }

  /**
   * Get the implementation-specific identifier for the certificate.
   * <p/>
   * In this case, the identifier corresponds to a <code>KeyStore</code> alias.
   *
   * @param certificate the X509 certificate for which to search for an identifier
   * @return the implementation-specific identifier for the certificate
   * @throws WSSecurityException
   */
  public String getX509Identifier(X509Certificate certificate)
    throws WSSecurityException
  {
    String identifier = null;

    if (keyStore != null)
    {
      identifier = getIdentifier(certificate, keyStore);
    }

    return identifier;
  }

  /**
   * Load an X509Certificate from the input stream.
   *
   * @param in the <code>InputStream</code> containing the X509 data
   * @return the X509 certificate
   * @throws WSSecurityException
   */
  @Override
  public X509Certificate loadCertificate(InputStream in)
    throws WSSecurityException
  {
    try
    {
      CertificateFactory certFactory = getCertificateFactory();

      return (X509Certificate) certFactory.generateCertificate(in);
    }
    catch (CertificateException e)
    {
      throw new WSSecurityException(WSSecurityException.SECURITY_TOKEN_UNAVAILABLE, "parseError",
          null, e);
    }
  }

  /**
   * Set the <code>CertificateFactory</code> instance for this <code>Crypto</code> instance.
   *
   * @param provider    the <code>CertificateFactory</code> provider name
   * @param certFactory the <code>CertificateFactory<code> instance to set
   */
  @Override
  public void setCertificateFactory(String provider, CertificateFactory certFactory)
  {
    if ((provider == null) || (provider.length() == 0))
    {
      certificateFactoryMap.put(certFactory.getProvider().getName(), certFactory);
    }
    else
    {
      certificateFactoryMap.put(provider, certFactory);
    }
  }

  /**
   * Set the crypto provider associated with this implementation.
   *
   * @param cryptoProvider the crypto provider associated with this implementation
   */
  @Override
  public void setCryptoProvider(String cryptoProvider)
  {
    this.cryptoProvider = cryptoProvider;
  }

  /**
   * Sets the identifier name of the default certificate.
   * <p/>
   * This should be the certificate that is used for signature and encryption. This identifier
   * corresponds to the certificate that should be used whenever <code>KeyInfo</code> is not
   * present in a signed or an encrypted message. The identifier is implementation specific, e.g.
   * it could be the <code>KeyStore</code> alias.
   *
   * @param identifier the identifier name of the default certificate
   */
  @Override
  public void setDefaultX509Identifier(String identifier)
  {
    // Do nothing the alias defined by the ApplicationSecurityContext cannot be overridden
  }

  /**
   * Evaluate whether a given public key should be trusted.
   *
   * @param publicKey the public key to be evaluated
   * @return <code>true</code> if the public key is trusted or <code>false</code> otherwise
   * @throws WSSecurityException
   */
  @Override
  public boolean verifyTrust(PublicKey publicKey)
    throws WSSecurityException
  {
    // If the public key is null, do not trust the signature
    if (publicKey == null)
    {
      return false;
    }

    // Search the keystore for the transmitted public key (direct trust)
    return isPublicKeyInKeyStore(publicKey, keyStore);
  }

  /**
   * Evaluate whether the given certificate chain should be trusted.
   * <p/>
   * Uses the CertPath API to validate a given certificate chain.
   *
   * @param certificateChain the certificate chain to validate
   * @return <code>true</code> if the certificate chain is valid, <code>false</code> otherwise
   * @throws WSSecurityException
   */
  @Deprecated
  @Override
  public boolean verifyTrust(X509Certificate[] certificateChain)
    throws WSSecurityException
  {
    return verifyTrust(certificateChain, false);
  }

  /**
   * Evaluate whether the given certificate chain should be trusted.
   * <p/>
   * Uses the CertPath API to validate a given certificate chain.
   *
   * @param certificateChain the certificate chain to validate
   * @param enableRevocation whether to enable CRL verification or not
   * @return <code>true</code> if the certificate chain is valid, <code>false</code> otherwise
   * @throws WSSecurityException
   */
  @Override
  public boolean verifyTrust(X509Certificate[] certificateChain, boolean enableRevocation)
    throws WSSecurityException
  {
    try
    {
      // Generate certificate path
      CertPath path = getCertificateFactory().generateCertPath(Arrays.asList(certificateChain));

      Set<TrustAnchor> set = new HashSet<>();

      // Add certificates from the key store associated with the application security context
      if (keyStore != null)
      {
        Enumeration<String> aliases = keyStore.aliases();

        while (aliases.hasMoreElements())
        {
          String alias = aliases.nextElement();
          X509Certificate cert = (X509Certificate) keyStore.getCertificate(alias);

          if (cert != null)
          {
            TrustAnchor anchor = new TrustAnchor(cert,
              cert.getExtensionValue(NAME_CONSTRAINTS_OID));

            set.add(anchor);
          }
        }
      }

      PKIXParameters param = new PKIXParameters(set);

      param.setRevocationEnabled(false);

//    if (enableRevocation && crlCertStore != null) {
//        param.addCertStore(crlCertStore);
//    }

      // Verify the trust path using the above settings
      String provider = getCryptoProvider();
      CertPathValidator validator;

      if ((provider == null) || (provider.length() == 0))
      {
        validator = CertPathValidator.getInstance("PKIX");
      }
      else
      {
        validator = CertPathValidator.getInstance("PKIX", provider);
      }

      validator.validate(path, param);

      return true;
    }
    catch (Throwable e)
    {
      throw new WSSecurityException(WSSecurityException.FAILURE, "certpath",
          new Object[] { e.getMessage() }, e);
    }
  }

  private Object createBCX509Name(String s)
  {
    if (BouncyCastleX509NameConstructor != null)
    {
      try
      {
        return BouncyCastleX509NameConstructor.newInstance(s);
      }
      catch (Exception e)
      {
        // ignore
      }
    }

    return new X500Principal(s);
  }

  private Certificate[] getCertificates(byte[] skiBytes, KeyStore store)
    throws WSSecurityException
  {
    try
    {
      for (Enumeration<String> e = store.aliases(); e.hasMoreElements(); )
      {
        String alias = e.nextElement();
        Certificate cert;
        Certificate[] certs = store.getCertificateChain(alias);

        if ((certs == null) || (certs.length == 0))
        {
          // No cert chain, so lets check if getCertificate gives us a result.
          cert = store.getCertificate(alias);

          if (cert == null)
          {
            continue;
          }

          certs = new Certificate[] { cert };
        }
        else
        {
          cert = certs[0];
        }

        if (cert instanceof X509Certificate)
        {
          X509Certificate x509cert = (X509Certificate) cert;
          byte[] data = getSKIBytesFromCert(x509cert);

          if ((data.length == skiBytes.length) && Arrays.equals(data, skiBytes))
          {
            return certs;
          }
        }
      }
    }
    catch (KeyStoreException e)
    {
      throw new WSSecurityException(WSSecurityException.FAILURE, "keystore", null, e);
    }

    return new Certificate[] {};
  }

  /**
   * Get an X509 certificate (chain) of the X500Principal argument in the supplied key store.
   *
   * @param subjectRDN the <code>X500Principal</code> or a BouncyCastle <code>X509Name</code>
   *                   instance
   * @param store      the key store
   * @return the X509 certificate (chain)
   * @throws WSSecurityException
   */
  private Certificate[] getCertificates(Object subjectRDN, KeyStore store)
    throws WSSecurityException
  {
    try
    {
      for (Enumeration<String> e = store.aliases(); e.hasMoreElements(); )
      {
        String alias = e.nextElement();
        Certificate cert;
        Certificate[] certs = store.getCertificateChain(alias);

        if ((certs == null) || (certs.length == 0))
        {
          // no cert chain, so lets check if getCertificate gives us a result.
          cert = store.getCertificate(alias);

          if (cert == null)
          {
            continue;
          }

          certs = new Certificate[] { cert };
        }
        else
        {
          cert = certs[0];
        }

        if (cert instanceof X509Certificate)
        {
          X500Principal foundRDN = ((X509Certificate) cert).getSubjectX500Principal();
          Object certName = createBCX509Name(foundRDN.getName());

          if (subjectRDN.equals(certName))
          {
            return certs;
          }
        }
      }
    }
    catch (KeyStoreException e)
    {
      throw new WSSecurityException(WSSecurityException.FAILURE, "keystore", null, e);
    }

    return new Certificate[] {};
  }

  private Certificate[] getCertificates(byte[] thumbprint, KeyStore store, MessageDigest sha)
    throws WSSecurityException
  {
    try
    {
      for (Enumeration<String> e = store.aliases(); e.hasMoreElements(); )
      {
        String alias = e.nextElement();
        Certificate cert;
        Certificate[] certs = store.getCertificateChain(alias);

        if ((certs == null) || (certs.length == 0))
        {
          // no cert chain, so lets check if getCertificate gives us a result.
          cert = store.getCertificate(alias);

          if (cert == null)
          {
            continue;
          }

          certs = new Certificate[] { cert };
        }
        else
        {
          cert = certs[0];
        }

        if (cert instanceof X509Certificate)
        {
          X509Certificate x509cert = (X509Certificate) cert;

          try
          {
            sha.update(x509cert.getEncoded());
          }
          catch (CertificateEncodingException ex)
          {
            throw new WSSecurityException(WSSecurityException.SECURITY_TOKEN_UNAVAILABLE,
                "encodeError", null, ex);
          }

          byte[] data = sha.digest();

          if (Arrays.equals(data, thumbprint))
          {
            return certs;
          }
        }
      }
    }
    catch (KeyStoreException e)
    {
      throw new WSSecurityException(WSSecurityException.FAILURE, "keystore", null, e);
    }

    return new Certificate[] {};
  }

  /**
   * Retrieve the X509 certificate (chain) of the X500Principal argument in the supplied
   * key store.
   *
   * @param issuerRDN    either an X500Principal or a BouncyCastle X509Name instance
   * @param serialNumber the certificate's serial number
   * @param store        the key store
   * @return the X509 certificate (chain) corresponding to the found certificate(s)
   * @throws WSSecurityException
   */
  private Certificate[] getCertificates(Object issuerRDN, BigInteger serialNumber, KeyStore store)
    throws WSSecurityException
  {
    try
    {
      for (Enumeration<String> e = store.aliases(); e.hasMoreElements(); )
      {
        String alias = e.nextElement();
        Certificate cert;
        Certificate[] certs = store.getCertificateChain(alias);

        if ((certs == null) || (certs.length == 0))
        {
          // no cert chain, so lets check if getCertificate gives us a result.
          cert = store.getCertificate(alias);

          if (cert == null)
          {
            continue;
          }

          certs = new Certificate[] { cert };
        }
        else
        {
          cert = certs[0];
        }

        if (cert instanceof X509Certificate)
        {
          X509Certificate x509cert = (X509Certificate) cert;

          if (x509cert.getSerialNumber().compareTo(serialNumber) == 0)
          {
            Object certName = createBCX509Name(x509cert.getIssuerX500Principal().getName());

            if (certName.equals(issuerRDN))
            {
              return certs;
            }
          }
        }
      }
    }
    catch (KeyStoreException e)
    {
      throw new WSSecurityException(WSSecurityException.FAILURE, "keystore", null, e);
    }

    return new Certificate[] {};
  }

  /**
   * Get an implementation-specific identifier that corresponds to the X509 certificate.
   * <p/>
   * In this case, the identifier is the key store alias.
   *
   * @param cert  the X509 certificate corresponding to the returned identifier
   * @param store the key store to search
   * @return the implementation-specific identifier that corresponds to the X509 ertificate
   */
  private String getIdentifier(X509Certificate cert, KeyStore store)
    throws WSSecurityException
  {
    try
    {
      for (Enumeration<String> e = store.aliases(); e.hasMoreElements(); )
      {
        String alias = e.nextElement();

        Certificate[] certs = store.getCertificateChain(alias);
        Certificate retrievedCert;

        if ((certs == null) || (certs.length == 0))
        {
          // no cert chain, so lets check if getCertificate gives us a  result.
          retrievedCert = store.getCertificate(alias);

          if (retrievedCert == null)
          {
            continue;
          }
        }
        else
        {
          retrievedCert = certs[0];
        }

        if (!(retrievedCert instanceof X509Certificate))
        {
          continue;
        }

        if ((retrievedCert != null) && retrievedCert.equals(cert))
        {
          return alias;
        }
      }
    }
    catch (KeyStoreException e)
    {
      throw new WSSecurityException(WSSecurityException.FAILURE, "keystore", null, e);
    }

    return null;
  }

  /**
   * Get a password from the <code>CallbackHandler</code>.
   *
   * @param identifier the identifier to give to the Callback
   * @param cb         the <code>CallbackHandler</code>
   * @return the password retrieved from the <code>CallbackHandler</code>
   * @throws WSSecurityException
   */
  private String getPassword(String identifier, CallbackHandler cb)
    throws WSSecurityException
  {
    WSPasswordCallback passwordCallback = new WSPasswordCallback(identifier,
      WSPasswordCallback.DECRYPT);

    try
    {
      Callback[] callbacks = new Callback[] { passwordCallback };

      cb.handle(callbacks);
    }
    catch (Throwable e)
    {
      throw new WSSecurityException(WSSecurityException.FAILURE, "noPassword",
          new Object[] { identifier }, e);
    }

    return passwordCallback.getPassword();
  }

  /**
   * Get an X509 certificate (chain) according to a given Thumbprint.
   *
   * @param thumbprint the SHA1 thumbprint info bytes
   * @return the X509 certificate (chain) corresponding to the found certificate(s)
   * @throws WSSecurityException
   */
  private X509Certificate[] getX509Certificates(byte[] thumbprint)
    throws WSSecurityException
  {
    MessageDigest sha;

    try
    {
      sha = MessageDigest.getInstance("SHA1");
    }
    catch (NoSuchAlgorithmException e)
    {
      throw new WSSecurityException(WSSecurityException.FAILURE, "noSHA1availabe", null, e);
    }

    Certificate[] certs = null;

    if (keyStore != null)
    {
      certs = getCertificates(thumbprint, keyStore, sha);
    }

    if (((certs == null) || (certs.length == 0)))
    {
      return null;
    }

    X509Certificate[] x509certs = new X509Certificate[certs.length];

    for (int i = 0; i < certs.length; i++)
    {
      x509certs[i] = (X509Certificate) certs[i];
    }

    return x509certs;
  }

  /**
   * Get an X509 certificate (chain) that correspond to the identifier.
   * <p/>
   * For this implementation, the identifier corresponds to the KeyStore alias.
   *
   * @param identifier the identifier that corresponds to the returned certificate (chain)
   * @return the X509 certificate (chain) corresponding to the found certificate(s)
   */
  private X509Certificate[] getX509Certificates(String identifier)
    throws WSSecurityException
  {
    Certificate[] certs = null;

    try
    {
      if (keyStore != null)
      {
        // There's a chance that there can only be a set of trust stores
        certs = keyStore.getCertificateChain(identifier);

        if ((certs == null) || (certs.length == 0))
        {
          // No certificate chain, so lets check if getCertificate gives us a result.
          Certificate cert = keyStore.getCertificate(identifier);

          if (cert != null)
          {
            certs = new Certificate[] { cert };
          }
        }
      }

      if (certs == null)
      {
        return null;
      }
    }
    catch (KeyStoreException e)
    {
      throw new WSSecurityException(WSSecurityException.FAILURE, "keystore", null, e);
    }

    X509Certificate[] x509certs = new X509Certificate[certs.length];

    for (int i = 0; i < certs.length; i++)
    {
      x509certs[i] = (X509Certificate) certs[i];
    }

    return x509certs;
  }

  /**
   * Get an X509 certificate (chain) according to a given serial number and issuer string.
   *
   * @param issuer       the issuer string
   * @param serialNumber the serial number of the certificate
   * @return the X509 certificate (chain) corresponding to the found certificate(s)
   * @throws WSSecurityException
   */
  private X509Certificate[] getX509Certificates(String issuer, BigInteger serialNumber)
    throws WSSecurityException
  {
    //
    // Convert the subject DN to a java X500Principal object first. This is to ensure
    // interop with a DN constructed from .NET, where e.g. it uses "S" instead of "ST".
    // Then convert it to a BouncyCastle X509Name, which will order the attributes of
    // the DN in a particular way (see WSS-168). If the conversion to an X500Principal
    // object fails (e.g. if the DN contains "E" instead of "EMAILADDRESS"), then fall
    // back on a direct conversion to a BC X509Name
    //
    Object issuerName;

    try
    {
      X500Principal issuerRDN = new X500Principal(issuer);

      issuerName = createBCX509Name(issuerRDN.getName());
    }
    catch (java.lang.IllegalArgumentException ex)
    {
      issuerName = createBCX509Name(issuer);
    }

    Certificate[] certs = null;

    if (keyStore != null)
    {
      certs = getCertificates(issuerName, serialNumber, keyStore);
    }

    if (((certs == null) || (certs.length == 0)))
    {
      return null;
    }

    X509Certificate[] x509certs = new X509Certificate[certs.length];

    for (int i = 0; i < certs.length; i++)
    {
      x509certs[i] = (X509Certificate) certs[i];
    }

    return x509certs;
  }

  /**
   * Get an X509 certificate (chain) according to a given SubjectKeyIdentifier.
   *
   * @param skiBytes the SKI bytes
   * @return the X509 certificate (chain) corresponding to the found certificate(s)
   */
  private X509Certificate[] getX509CertificatesSKI(byte[] skiBytes)
    throws WSSecurityException
  {
    Certificate[] certs = null;

    if (keyStore != null)
    {
      certs = getCertificates(skiBytes, keyStore);
    }

    if (((certs == null) || (certs.length == 0)))
    {
      return null;
    }

    X509Certificate[] x509certs = new X509Certificate[certs.length];

    for (int i = 0; i < certs.length; i++)
    {
      x509certs[i] = (X509Certificate) certs[i];
    }

    return x509certs;
  }

  /**
   * Get an X509 certificate (chain) according to a given DN of the subject of the certificate
   *
   * @param subjectDN The DN of subject to look for
   * @return the X509 certificate (chain) corresponding to the found certificate(s)
   * @throws WSSecurityException
   */
  private X509Certificate[] getX509CertificatesSubjectDN(String subjectDN)
    throws WSSecurityException
  {
    //
    // Convert the subject DN to a java X500Principal object first. This is to ensure
    // interop with a DN constructed from .NET, where e.g. it uses "S" instead of "ST".
    // Then convert it to a BouncyCastle X509Name, which will order the attributes of
    // the DN in a particular way (see WSS-168). If the conversion to an X500Principal
    // object fails (e.g. if the DN contains "E" instead of "EMAILADDRESS"), then fall
    // back on a direct conversion to a BC X509Name
    //
    Object subject;

    try
    {
      X500Principal subjectRDN = new X500Principal(subjectDN);

      subject = createBCX509Name(subjectRDN.getName());
    }
    catch (java.lang.IllegalArgumentException ex)
    {
      subject = createBCX509Name(subjectDN);
    }

    Certificate[] certs = null;

    if (keyStore != null)
    {
      certs = getCertificates(subject, keyStore);
    }

    if (((certs == null) || (certs.length == 0)))
    {
      return null;
    }

    X509Certificate[] x509certs = new X509Certificate[certs.length];

    for (int i = 0; i < certs.length; i++)
    {
      x509certs[i] = (X509Certificate) certs[i];
    }

    return x509certs;
  }

  /**
   * Check whether the public key is in the key store.
   *
   * @param publicKey        the public key to search for
   * @param keyStoreToSearch the key store to search
   * @return <code>true</code> if the public key is found in the key store or <code>false</code>
   * otherwise
   */
  private boolean isPublicKeyInKeyStore(PublicKey publicKey, KeyStore keyStoreToSearch)
  {
    try
    {
      for (Enumeration<String> e = keyStoreToSearch.aliases(); e.hasMoreElements(); )
      {
        String alias = e.nextElement();
        Certificate[] certs = keyStoreToSearch.getCertificateChain(alias);
        Certificate cert;

        if ((certs == null) || (certs.length == 0))
        {
          // no cert chain, so lets check if getCertificate gives us a result.
          cert = keyStoreToSearch.getCertificate(alias);

          if (cert == null)
          {
            continue;
          }
        }
        else
        {
          cert = certs[0];
        }

        if (!(cert instanceof X509Certificate))
        {
          continue;
        }

        X509Certificate x509cert = (X509Certificate) cert;

        if (publicKey.equals(x509cert.getPublicKey()))
        {
          return true;
        }
      }
    }
    catch (KeyStoreException e)
    {
      return false;
    }

    return false;
  }
}

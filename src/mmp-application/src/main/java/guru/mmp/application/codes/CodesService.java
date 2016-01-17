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

package guru.mmp.application.codes;

import guru.mmp.common.cdi.CDIUtil;
import guru.mmp.common.service.ws.security.WebServiceClientSecurityHandlerResolver;
import guru.mmp.common.util.DateUtil;
import guru.mmp.common.xml.DtdJarResolver;
import guru.mmp.common.xml.XmlParserErrorHandler;
import guru.mmp.common.xml.XmlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.ws.BindingProvider;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.*;

/**
 * The <code>CodesService</code> class provides the Codes Service implementation.
 *
 * @author Marcus Portmann
 */
@ApplicationScoped
@Default
public class CodesService
  implements ICodesService
{
  /**
   * The path to the codes configuration files (META-INF/CodesConfig.xml) on the
   * classpath.
   */
  public static final String CODES_CONFIGURATION_PATH = "META-INF/CodesConfig.xml";

  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(CodesService.class);

  /* The code providers. */
  private List<ICodeProvider> codeProviders;

  /**
   * The configuration information for the code providers read from the codes configuration
   * files (META-INF/CodesConfig.xml) on the classpath.
   */
  private List<CodeProviderConfig> codeProvidersConfig;

  /* Codes DAO */
  @Inject
  private ICodesDAO codesDAO;

  /**
   * Constructs a new <code>CodesService</code>.
   */
  public CodesService() {}

  /**
   * Check whether the cached code category exists.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the cached code
   *           category
   *
   * @return <code>true</code> if the cached code category exists or <code>false</code> otherwise
   *
   * @throws CodesServiceException
   */
  public boolean cachedCodeCategoryExists(UUID id)
    throws CodesServiceException
  {
    try
    {
      return codesDAO.cachedCodeCategoryExists(id);
    }
    catch (Throwable e)
    {
      throw new CodesServiceException(
        String.format("Failed to check whether the cached code category (%s) exists", id), e);
    }
  }

  /**
   * Check whether the code category exists.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the code category
   *
   * @return <code>true</code> if the code category exists or <code>false</code> otherwise
   *
   * @throws CodesServiceException
   */
  public boolean codeCategoryExists(UUID id)
    throws CodesServiceException
  {
    try
    {
      return codesDAO.codeCategoryExists(id);
    }
    catch (Throwable e)
    {
      throw new CodesServiceException(
        String.format("Failed to check whether the code category (%s) exists", id), e);
    }
  }

  /**
   * Create the new cached code.
   *
   * @param code the <code>Code</code> instance containing the information for the new cached code
   *
   * @throws CodesServiceException
   */
  public void createCachedCode(Code code)
    throws CodesServiceException
  {
    try
    {
      codesDAO.createCachedCode(code);
    }
    catch (Throwable e)
    {
      throw new CodesServiceException(
        String.format("Failed to create the cached code (%s) for the cached code category (%s)",
          code.getName(), code.getCategoryId()), e);
    }
  }

  /**
   * Create the new cached code category.
   *
   * @param cachedCodeCategory the <code>CachedCodeCategory</code> instance containing the
   *                           information for the new cached code category
   *
   * @throws CodesServiceException
   */
  public void createCachedCodeCategory(CachedCodeCategory cachedCodeCategory)
    throws CodesServiceException
  {
    try
    {
      codesDAO.createCachedCodeCategory(cachedCodeCategory);
    }
    catch (Throwable e)
    {
      throw new CodesServiceException(
        String.format("Failed to create the cached code category (%s)", cachedCodeCategory.getId()),
        e);
    }
  }

  /**
   * Create the new code.
   *
   * @param code the <code>Code</code> instance containing the information for the new code
   *
   * @throws CodesServiceException
   */
  public void createCode(Code code)
    throws CodesServiceException
  {
    try
    {
      codesDAO.createCode(code);
    }
    catch (Throwable e)
    {
      throw new CodesServiceException(
        String.format("Failed to create the code (%s) for the code category (%s)", code.getName(),
          code.getCategoryId()), e);
    }
  }

  /**
   * Create the new code category.
   *
   * @param codeCategory the <code>CodeCategory</code> instance containing the information for the
   *                     new code category
   *
   * @throws CodesServiceException
   */
  public void createCodeCategory(CodeCategory codeCategory)
    throws CodesServiceException
  {
    try
    {
      codesDAO.createCodeCategory(codeCategory);
    }
    catch (Throwable e)
    {
      throw new CodesServiceException(
        String.format("Failed to create the code category (%s)", codeCategory.getId()), e);
    }
  }

  /**
   * Delete the cached code category.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the cached code
   *           category
   *
   * @throws CodesServiceException
   */
  public void deleteCachedCodeCategory(UUID id)
    throws CodesServiceException
  {
    try
    {
      codesDAO.deleteCachedCodeCategory(id);
    }
    catch (Throwable e)
    {
      throw new CodesServiceException(
        String.format("Failed to delete the cached code category (%s)", id), e);
    }
  }

  /**
   * Delete the code.
   *
   * @param codeCategoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                       code category
   * @param id             the ID uniquely identifying the code
   *
   * @throws CodesServiceException
   */
  public void deleteCode(UUID codeCategoryId, String id)
    throws CodesServiceException
  {
    try
    {
      codesDAO.deleteCode(codeCategoryId, id);
    }
    catch (Throwable e)
    {
      throw new CodesServiceException(
        String.format("Failed to delete the code (%s) for the code category (%s)", id,
          codeCategoryId), e);
    }
  }

  /**
   * Delete the code category.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the code category
   *
   * @throws CodesServiceException
   */
  public void deleteCodeCategory(UUID id)
    throws CodesServiceException
  {
    try
    {
      codesDAO.deleteCodeCategory(id);
    }
    catch (Throwable e)
    {
      throw new CodesServiceException(String.format("Failed to delete the code category (%s)", id),
        e);
    }
  }

  /**
   * Retrieve the cached code category.
   *
   * @param id            the Universally Unique Identifier (UUID) used to uniquely identify the
   *                      cached code category
   * @param retrieveCodes retrieve the codes and/or code data for the cached code category
   *
   * @return the cached code category or <code>null</code> if the cached code category could not be
   * found
   *
   * @throws CodesServiceException
   */

  public CachedCodeCategory getCachedCodeCategory(UUID id, boolean retrieveCodes)
    throws CodesServiceException
  {
    try
    {
      CachedCodeCategory cachedCodeCategory = codesDAO.getCachedCodeCategory(id);

      if (retrieveCodes)
      {
        List<Code> codes = codesDAO.getCachedCodesForCachedCodeCategory(id);

        cachedCodeCategory.setCodes(codes);
      }

      return cachedCodeCategory;
    }
    catch (Throwable e)
    {
      throw new CodesServiceException(
        String.format("Failed to retrieve the cached code category (%s)", id), e);
    }
  }

  /**
   * Returns all the cached codes for the cached code category.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the cached code
   *           category
   *
   * @return all the cached codes for the cached code category
   *
   * @throws CodesServiceException
   */
  public List<Code> getCachedCodesForCachedCodeCategory(UUID id)
    throws CodesServiceException
  {
    try
    {
      return codesDAO.getCachedCodesForCachedCodeCategory(id);
    }
    catch (Throwable e)
    {
      throw new CodesServiceException(
        String.format("Failed to retrieve the cached codes for the cached code category (%s)", id),
        e);
    }
  }

  /**
   * Retrieve the code.
   *
   * @param codeCategoryId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                       code category
   * @param id             the ID uniquely identifying the code
   *
   * @return the code or <code>null</code> if the code could not be found
   *
   * @throws CodesServiceException
   */
  public Code getCode(UUID codeCategoryId, String id)
    throws CodesServiceException
  {
    try
    {
      return codesDAO.getCode(codeCategoryId, id);
    }
    catch (Throwable e)
    {
      throw new CodesServiceException(
        String.format("Failed to retrieve the code (%s) for the code category (%s)", id,
          codeCategoryId), e);
    }
  }

  /**
   * Returns all the code categories.
   *
   * @param retrieveCodes retrieve the codes and/or code data for the code categories
   *
   * @return all the code categories
   *
   * @throws CodesServiceException
   */
  public List<CodeCategory> getCodeCategories(boolean retrieveCodes)
    throws CodesServiceException
  {
    try
    {
      return codesDAO.getCodeCategories(retrieveCodes);
    }
    catch (Throwable e)
    {
      throw new CodesServiceException("Failed to retrieve the code categories", e);
    }
  }

  /**
   * Retrieve the code category.
   *
   * @param id            the Universally Unique Identifier (UUID) used to uniquely identify the
   *                      code category
   * @param retrieveCodes retrieve the codes and/or code data for the code categories
   *
   * @return the code category or <code>null</code> if the code category could not be found
   *
   * @throws CodesServiceException
   */
  public CodeCategory getCodeCategory(UUID id, boolean retrieveCodes)
    throws CodesServiceException
  {
    try
    {
      CodeCategory codeCategory = codesDAO.getCodeCategory(id);

      if (codeCategory != null)
      {
        if (codeCategory.getCategoryType() == CodeCategoryType.LOCAL_STANDARD)
        {
          if (retrieveCodes)
          {
            codeCategory.setCodes(getCodesForCodeCategory(id));
          }
        }
      }

      return codeCategory;
    }
    catch (Throwable e)
    {
      throw new CodesServiceException(
        String.format("Failed to retrieve the code category (%s)", id), e);
    }
  }

  /**
   * Retrieve the code category using the specified parameters.
   *
   * @param id            the Universally Unique Identifier (UUID) used to uniquely identify the
   *                      code category
   * @param parameters    the parameters
   * @param retrieveCodes retrieve the codes and/or code data for the code category
   *
   * @return the code category or <code>null</code> if the code category
   * could not be found
   *
   * @throws CodesServiceException
   */
  public CodeCategory getCodeCategoryWithParameters(
    UUID id, Map<String, String> parameters, boolean retrieveCodes)
    throws CodesServiceException
  {
    try
    {
      CodeCategory codeCategory = codesDAO.getCodeCategory(id);

      if (codeCategory != null)
      {
        if (codeCategory.getCategoryType() == CodeCategoryType.LOCAL_STANDARD)
        {
          if (retrieveCodes)
          {
            codeCategory.setCodes(getCodesForCodeCategory(id));
          }
        }
      }

      return codeCategory;
    }
    catch (Throwable e)
    {
      throw new CodesServiceException(
        String.format("Failed to retrieve the code category (%s)", id), e);
    }
  }

  /**
   * Retrieve the code category from the appropriate code provider that has been registered with
   * the Codes Service in the <code>META-INF/CodesConfig.xml</code> configuration file.
   *
   * @param codeCategory         the code provider code category
   * @param lastRetrieved        the date and time the code category was last retrieved
   * @param returnCodesIfCurrent should the the <b>Standard</b> codes and/or <b>Custom</b> code
   *                             data be retrieved even if the code category has not been
   *                             updated after the date and time specified by the
   *                             <code>lastRetrieved</code> parameter
   *
   * @return the code provider code category including the <b>Standard</b> codes and/or
   * <b>Custom</b> code data or <code>null</code> if the code category could not be found
   *
   * @throws CodesServiceException
   */
  public CodeCategory getCodeProviderCodeCategory(
    CodeCategory codeCategory, Date lastRetrieved, boolean returnCodesIfCurrent)
    throws CodesServiceException
  {
    try
    {
      for (ICodeProvider codeProvider : codeProviders)
      {
        CodeCategory codeProviderCodeCategory = codeProvider.getCodeCategory(codeCategory,
          lastRetrieved, returnCodesIfCurrent);

        if (codeProviderCodeCategory != null)
        {
          return codeProviderCodeCategory;
        }
      }

      return null;
    }
    catch (Throwable e)
    {
      throw new CodesServiceException(
        String.format("Failed to retrieve the code provider code category (%s)",
          codeCategory.getId()), e);
    }
  }

  /**
   * Retrieve the code category from the appropriate code provider that has been registered with
   * the Codes Service in the <code>META-INF/CodesConfig.xml</code> configuration file using the
   * specified parameters.
   *
   * @param codeCategory         the code provider code category
   * @param parameters           the parameters
   * @param lastRetrieved        the date and time the code category was last retrieved
   * @param returnCodesIfCurrent should the the <b>Standard</b> codes and/or <b>Custom</b> code
   *                             data be retrieved even if the code category has not been
   *                             updated after the date and time specified by the
   *                             <code>lastRetrieved</code> parameter
   *
   * @return the code provider code category including the <b>Standard</b> codes and/or
   * <b>Custom</b> code data or <code>null</code> if the code category could not be found
   *
   * @throws CodesServiceException
   */
  public CodeCategory getCodeProviderCodeCategoryWithParameters(
    CodeCategory codeCategory, Map<String, String> parameters, Date lastRetrieved,
    boolean returnCodesIfCurrent)
    throws CodesServiceException
  {
    try
    {
      for (ICodeProvider codeProvider : codeProviders)
      {
        CodeCategory codeProviderCodeCategory = codeProvider.getCodeCategoryWithParameters(
          codeCategory, parameters, lastRetrieved, returnCodesIfCurrent);

        if (codeProviderCodeCategory != null)
        {
          return codeProviderCodeCategory;
        }
      }

      return null;
    }
    catch (Throwable e)
    {
      throw new CodesServiceException(
        String.format("Failed to retrieve the code provider code category (%s) with parameters",
          codeCategory.getId()), e);
    }
  }

  /**
   * Returns all the codes for the code category.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the code category
   *
   * @return all the codes for the code category
   *
   * @throws CodesServiceException
   */
  public List<Code> getCodesForCodeCategory(UUID id)
    throws CodesServiceException
  {
    try
    {
      return codesDAO.getCodesForCodeCategory(id);
    }
    catch (Throwable e)
    {
      throw new CodesServiceException(
        String.format("Failed to retrieve the codes for the code category (%s)", id), e);
    }
  }

  /**
   * Returns the number of code categories.
   *
   * @return the number of code categories
   *
   * @throws CodesServiceException
   */
  public int getNumberOfCodeCategories()
    throws CodesServiceException
  {
    try
    {
      return codesDAO.getNumberOfCodeCategories();
    }
    catch (Throwable e)
    {
      throw new CodesServiceException("Failed to retrieve the number of code categories", e);
    }
  }

  /**
   * Returns the number of codes for the code category.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the code category
   *
   * @return the number of codes for the code category
   *
   * @throws CodesServiceException
   */
  public int getNumberOfCodesForCodeCategory(UUID id)
    throws CodesServiceException
  {
    try
    {
      return codesDAO.getNumberOfCodesForCodeCategory(id);
    }
    catch (Throwable e)
    {
      throw new CodesServiceException(
        String.format("Failed to retrieve the number of codes for the code category (%s)", id), e);
    }
  }

  /**
   * Retrieve the remote code category including the <b>Standard</b> codes and/or <b>Custom</b>
   * code data.
   *
   * @param codeCategory         the remote code category
   * @param lastRetrieved        the date and time the remote code category was last retrieved
   * @param returnCodesIfCurrent should the the <b>Standard</b> codes and/or <b>Custom</b> code
   *                             data be retrieved even if the remote code category has not been
   *                             updated after the date and time specified by the
   *                             <code>lastRetrieved</code> parameter
   *
   * @return the remote code category including the <b>Standard</b> codes and/or <b>Custom</b>
   * code data
   *
   * @throws CodesServiceException
   */
  public CodeCategory getRemoteCodeCategory(
    CodeCategory codeCategory, Date lastRetrieved, boolean returnCodesIfCurrent)
    throws CodesServiceException
  {
    try
    {
      // Is the remote code category cacheable?
      if (codeCategory.getIsCacheable())
      {
        // If the cached data for the remote code category is "current" then return it
        if (isCachedCodeCategoryCurrent(codeCategory.getId()))
        {
          CachedCodeCategory cachedCodeCategory = getCachedCodeCategory(codeCategory.getId(), true);

          codeCategory.setUpdated(cachedCodeCategory.getLastUpdated());
          codeCategory.setCodes(cachedCodeCategory.getCodes());
          codeCategory.setCodeData(cachedCodeCategory.getCodeData());

          return codeCategory;
        }

        // If the cached data for the remote code category is not "current" then delete it
        else
        {
          if (cachedCodeCategoryExists(codeCategory.getId()))
          {
            deleteCachedCodeCategory(codeCategory.getId());
          }
        }
      }

      if (codeCategory.getCategoryType() == CodeCategoryType.REMOTE_WEB_SERVICE)
      {
        CodeCategory webServiceRemoteCodeCategory = getRemoteWebServiceCodeCategory(codeCategory,
          lastRetrieved, returnCodesIfCurrent);

        // If the remote code category is cacheable then cache it now
        if (codeCategory.getIsCacheable())
        {
          cacheRemoteCodeCategory(webServiceRemoteCodeCategory);
        }

        return webServiceRemoteCodeCategory;
      }
      else if (codeCategory.getCategoryType() == CodeCategoryType.REMOTE_HTTP_SERVICE)
      {
        throw new CodesServiceException(String.format(
          "The code category type (%s) for the remote code category (%s) is not supported",
          codeCategory.getCategoryType(), codeCategory.getId()));
      }
      else
      {
        throw new CodesServiceException(String.format(
          "The code category type (%s) for the remote code category (%s) is not supported",
          codeCategory.getCategoryType(), codeCategory.getId()));
      }
    }
    catch (Throwable e)
    {
      throw new CodesServiceException(
        String.format("Failed to retrieve the remote code category (%s)", codeCategory.getId()), e);
    }
  }

  /**
   * Retrieve the remote code category including the <b>Standard</b> codes and/or <b>Custom</b>
   * code data using the specified parameters.
   *
   * @param codeCategory         the remote code category
   * @param parameters           the parameters
   * @param lastRetrieved        the date and time the remote code category was last retrieved
   * @param returnCodesIfCurrent should the the <b>Standard</b> codes and/or <b>Custom</b> code
   *                             data be retrieved even if the remote code category has not been
   *                             updated after the date and time specified by the
   *                             <code>lastRetrieved</code> parameter
   *
   * @return the remote code category including the <b>Standard</b> codes and/or <b>Custom</b>
   * code data
   *
   * @throws CodesServiceException
   */
  public CodeCategory getRemoteCodeCategoryWithParameters(
    CodeCategory codeCategory, Map<String, String> parameters, Date lastRetrieved,
    boolean returnCodesIfCurrent)
    throws CodesServiceException
  {
    try
    {
      // NOTE: Remote code categories retrieved with parameters are NOT cacheable -- MARCUS

      if (codeCategory.getCategoryType() == CodeCategoryType.REMOTE_WEB_SERVICE)
      {
        return getRemoteWebServiceCodeCategoryWithParameters(codeCategory, parameters,
          lastRetrieved, returnCodesIfCurrent);
      }
      else if (codeCategory.getCategoryType() == CodeCategoryType.REMOTE_HTTP_SERVICE)
      {
        throw new CodesServiceException(String.format(
          "The code category type (%s) for the remote code category (%s) is not supported",
          codeCategory.getCategoryType(), codeCategory.getId()));
      }
      else
      {
        throw new CodesServiceException(String.format(
          "The code category type (%s) for the remote code category (%s) is not supported",
          codeCategory.getCategoryType(), codeCategory.getId()));
      }
    }
    catch (Throwable e)
    {
      throw new CodesServiceException(
        String.format("Failed to retrieve the remote code category (%s) with parameters",
          codeCategory.getId()), e);
    }
  }

  /**
   * Initialise the Codes Service instance.
   */
  @PostConstruct
  public void init()
  {
    logger.info("Initialising the Codes Service");

    codeProviders = new ArrayList<>();

    try
    {
      // Initialise the configuration for the Messaging Service
      initConfiguration();

      // Read the codes configuration
      readCodesConfig();

      // Initialise the code providers
      initCodeProviders();
    }
    catch (Throwable e)
    {
      throw new RuntimeException("Failed to initialise the Codes Service", e);
    }
  }

  /**
   * Is the cached code category current?
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the cached code
   *           category
   *
   * @return <code>true</code> if the cached code category is current or <code>false</code>
   * otherwise
   *
   * @throws CodesServiceException
   */
  public boolean isCachedCodeCategoryCurrent(UUID id)
    throws CodesServiceException
  {
    try
    {
      return codesDAO.isCachedCodeCategoryCurrent(id);
    }
    catch (Throwable e)
    {
      throw new CodesServiceException(
        String.format("Failed to check whether the cached code category (%s) is current", id), e);
    }
  }

  /**
   * Update the existing cached code category.
   *
   * @param cachedCodeCategory the <code>CachedCodeCategory</code> instance containing the updated
   *                           information for the cached code category
   *
   * @return the updated cached code category
   *
   * @throws CodesServiceException
   */
  public CachedCodeCategory updateCachedCodeCategory(CachedCodeCategory cachedCodeCategory)
    throws CodesServiceException
  {
    try
    {
      return codesDAO.updateCachedCodeCategory(cachedCodeCategory);
    }
    catch (Throwable e)
    {
      throw new CodesServiceException(
        String.format("Failed to update the cached code category (%s)", cachedCodeCategory.getId()),
        e);
    }
  }

  /**
   * Update the existing code.
   *
   * @param code the <code>Code</code> instance containing the updated information for the code
   *
   * @return the updated code
   *
   * @throws CodesServiceException
   */
  public Code updateCode(Code code)
    throws CodesServiceException
  {
    try
    {
      return codesDAO.updateCode(code);
    }
    catch (Throwable e)
    {
      throw new CodesServiceException(String.format("Failed to update the code (%s)", code.getId()),
        e);
    }
  }

  /**
   * Update the existing code category.
   *
   * @param codeCategory the <code>CodeCategory</code> instance containing the updated information
   *                     for the code category
   *
   * @return the updated code category
   *
   * @throws CodesServiceException
   */
  public CodeCategory updateCodeCategory(CodeCategory codeCategory)
    throws CodesServiceException
  {
    try
    {
      // Always delete the cached data for the code category when the code category is updated
      if (cachedCodeCategoryExists(codeCategory.getId()))
      {
        deleteCachedCodeCategory(codeCategory.getId());
      }

      return codesDAO.updateCodeCategory(codeCategory);
    }
    catch (Throwable e)
    {
      throw new CodesServiceException(
        String.format("Failed to update the code category (%s)", codeCategory.getId()), e);
    }
  }

  /**
   * Cache the remote code category including the <b>Standard</b> codes and/or <b>Custom</b>
   * code data.
   *
   * @param codeCategory the remote code category
   *
   * @throws CodesServiceException
   */
  private synchronized void cacheRemoteCodeCategory(CodeCategory codeCategory)
    throws CodesServiceException
  {
    try
    {
      // If the cached code category already exists then delete it
      if (cachedCodeCategoryExists(codeCategory.getId()))
      {
        deleteCachedCodeCategory(codeCategory.getId());
      }

      // Create the cached code category
      CachedCodeCategory cachedCodeCategory = new CachedCodeCategory(codeCategory.getId(),
        codeCategory.getCodeData(), codeCategory.getUpdated(), new Date());

      createCachedCodeCategory(cachedCodeCategory);

      // Cache the codes for the code category
      if (codeCategory.getCodes() != null)
      {
        for (Code code : codeCategory.getCodes())
        {
          createCachedCode(code);
        }
      }
    }
    catch (Throwable e)
    {
      throw new CodesServiceException(
        String.format("Failed to cache the remote code category (%s)", codeCategory.getId()), e);
    }
  }

  private CodeCategory getRemoteWebServiceCodeCategory(
    CodeCategory codeCategory, Date lastRetrieved, boolean returnCodesIfCurrent)
    throws CodesServiceException
  {
    try
    {
      URL wsdlLocation = Thread.currentThread().getContextClassLoader().getResource(
        "META-INF/wsdl/CodesService.wsdl");

      guru.mmp.service.codes.ws.CodesService service = new guru.mmp.service.codes.ws.CodesService(
        wsdlLocation, new QName("http://ws.codes.service.mmp.guru", "CodesService"));

      // Setup the JAX-WS handlers that implement the MMP Web Service Security model
      if (codeCategory.getIsEndPointSecure())
      {
        service.setHandlerResolver(new WebServiceClientSecurityHandlerResolver());
      }

      // Retrieve the web service proxy
      guru.mmp.service.codes.ws.ICodesService codesService = service.getCodesService();

      // Set the endpoint for the web service
      BindingProvider bindingProvider = ((BindingProvider) codesService);

      bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
        codeCategory.getEndPoint());

      guru.mmp.service.codes.ws.CodeCategory remoteCodeCategory = codesService.getCodeCategory(
        codeCategory.getId().toString(), DateUtil.toCalendar(lastRetrieved), returnCodesIfCurrent);

      codeCategory.setUpdated(DateUtil.toDate(remoteCodeCategory.getLastUpdated()));

      List<Code> codes = new ArrayList<>();

      for (guru.mmp.service.codes.ws.Code remoteCode : remoteCodeCategory.getCodes())
      {
        codes.add(new Code(remoteCode.getId(), codeCategory.getId(), remoteCode.getName(),
          remoteCode.getValue()));
      }

      codeCategory.setCodes(codes);
      codeCategory.setCodeData(remoteCodeCategory.getCodeData());

      return codeCategory;
    }
    catch (Throwable e)
    {
      throw new CodesServiceException(
        String.format("Failed to retrieve the remote code category (%s) from the web service (%s)",
          codeCategory.getId(), codeCategory.getEndPoint()), e);
    }
  }

  private CodeCategory getRemoteWebServiceCodeCategoryWithParameters(
    CodeCategory codeCategory, Map<String, String> parameters, Date lastRetrieved,
    boolean returnCodesIfCurrent)
    throws CodesServiceException
  {
    try
    {
      URL wsdlLocation = Thread.currentThread().getContextClassLoader().getResource(
        "META-INF/wsdl/CodesService.wsdl");

      guru.mmp.service.codes.ws.CodesService service = new guru.mmp.service.codes.ws.CodesService(
        wsdlLocation, new QName("http://ws.codes.service.mmp.guru", "CodesService"));

      // Setup the JAX-WS handlers that implement the MMP Web Service Security model
      if (codeCategory.getIsEndPointSecure())
      {
        service.setHandlerResolver(new WebServiceClientSecurityHandlerResolver());
      }

      // Retrieve the web service proxy
      guru.mmp.service.codes.ws.ICodesService codesService = service.getCodesService();

      // Set the endpoint for the web service
      BindingProvider bindingProvider = ((BindingProvider) codesService);

      bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
        codeCategory.getEndPoint());

      List<guru.mmp.service.codes.ws.Parameter> wsParameters = new ArrayList<>();

      for (String parameterName : parameters.keySet())
      {
        String parameterValue = parameters.get(parameterName);

        guru.mmp.service.codes.ws.Parameter wsParameter = new guru.mmp.service.codes.ws.Parameter();

        wsParameter.setName(parameterName);
        wsParameter.setValue(parameterValue);

        wsParameters.add(wsParameter);
      }

      guru.mmp.service.codes.ws.CodeCategory remoteCodeCategory = codesService
        .getCodeCategoryWithParameters(
        codeCategory.getId().toString(), wsParameters, DateUtil.toCalendar(lastRetrieved),
        returnCodesIfCurrent);

      codeCategory.setUpdated(DateUtil.toDate(remoteCodeCategory.getLastUpdated()));

      List<Code> codes = new ArrayList<>();

      for (guru.mmp.service.codes.ws.Code remoteCode : remoteCodeCategory.getCodes())
      {
        codes.add(new Code(remoteCode.getId(), codeCategory.getId(), remoteCode.getName(),
          remoteCode.getValue()));
      }

      codeCategory.setCodes(codes);
      codeCategory.setCodeData(remoteCodeCategory.getCodeData());

      return codeCategory;
    }
    catch (Throwable e)
    {
      throw new CodesServiceException(String.format(
        "Failed to retrieve the remote code category (%s) with parameters from the web service " +
          "(%s)", codeCategory.getId(), codeCategory.getEndPoint()), e);
    }
  }

  /**
   * Initialise the code providers.
   *
   * @throws CodesServiceException
   */
  private void initCodeProviders()
    throws CodesServiceException
  {
    // Initialise each code provider
    for (CodeProviderConfig codeProviderConfig : codeProvidersConfig)
    {
      try
      {
        logger.info(String.format("Initialising the code provider (%s) with class (%s)",
          codeProviderConfig.getName(), codeProviderConfig.getClassName()));

        Class<?> clazz = Thread.currentThread().getContextClassLoader().loadClass(
          codeProviderConfig.getClassName());

        Constructor<?> constructor = clazz.getConstructor(CodeProviderConfig.class);

        if (constructor != null)
        {
          // Create an instance of the code provider
          ICodeProvider codeProvider = (ICodeProvider) constructor.newInstance(codeProviderConfig);

          // Perform container-based dependency injection on the code provider
          CDIUtil.inject(codeProvider);

          codeProviders.add(codeProvider);
        }
        else
        {
          logger.error(String.format("Failed to register the code provider (%s): " +
              "The code provider class does not provide a constructor with the required signature",
            codeProviderConfig.getClassName()));
        }
      }
      catch (Throwable e)
      {
        logger.error(String.format("Failed to initialise the code provider (%s) with class (%s)",
          codeProviderConfig.getName(), codeProviderConfig.getClassName()), e);
      }
    }
  }

  /**
   * Initialise the configuration for the Codes Service instance.
   *
   * @throws CodesServiceException
   */
  private void initConfiguration()
    throws CodesServiceException
  {}

  /**
   * Read the codes configuration from all the <i>META-INF/CodesConfig.xml</i>
   * configuration files that can be found on the classpath.
   *
   * @throws CodesServiceException
   */
  private void readCodesConfig()
    throws CodesServiceException
  {
    try
    {
      codeProvidersConfig = new ArrayList<>();

      ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

      // Load the codes configuration files from the classpath
      Enumeration<URL> configurationFiles = classLoader.getResources(CODES_CONFIGURATION_PATH);

      while (configurationFiles.hasMoreElements())
      {
        URL configurationFile = configurationFiles.nextElement();

        if (logger.isDebugEnabled())
        {
          logger.debug(String.format("Reading the codes configuration file (%s)",
            configurationFile.toURI().toString()));
        }

        // Retrieve a document builder instance using the factory
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();

        builderFactory.setValidating(true);

        // builderFactory.setNamespaceAware(true);
        DocumentBuilder builder = builderFactory.newDocumentBuilder();

        builder.setEntityResolver(
          new DtdJarResolver("CodesConfig.dtd", "META-INF/CodesConfig.dtd"));
        builder.setErrorHandler(new XmlParserErrorHandler());

        // Parse the XML messaging configuration file using the document builder
        InputSource inputSource = new InputSource(configurationFile.openStream());
        Document document = builder.parse(inputSource);
        Element rootElement = document.getDocumentElement();

        List<Element> codeProviderElements = XmlUtils.getChildElements(rootElement, "codeProvider");

        for (Element codeProviderElement : codeProviderElements)
        {
          // Read the handler configuration
          String name = XmlUtils.getChildElementText(codeProviderElement, "name");
          String className = XmlUtils.getChildElementText(codeProviderElement, "class");

          CodeProviderConfig codeProviderConfig = new CodeProviderConfig(name, className);

          codeProvidersConfig.add(codeProviderConfig);
        }
      }
    }
    catch (Throwable e)
    {
      throw new CodesServiceException("Failed to read the codes configuration", e);
    }
  }
}

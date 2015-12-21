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

package guru.mmp.application.codes;

//~--- non-JDK imports --------------------------------------------------------

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

//~--- JDK imports ------------------------------------------------------------

import java.lang.reflect.Constructor;

import java.net.URL;

import java.util.*;

import javax.annotation.PostConstruct;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;

import javax.inject.Inject;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.ws.BindingProvider;

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
   * Check whether the cached code category with the specified ID exists.
   *
   * @param id the ID uniquely identifying the cached code category
   *
   * @return <code>true</code> if the cached code category exists or <code>false</code> otherwise
   *
   * @throws CodesServiceException
   */
  public boolean cachedCodeCategoryExists(String id)
    throws CodesServiceException
  {
    try
    {
      return codesDAO.cachedCodeCategoryExists(id);
    }
    catch (Throwable e)
    {
      throw new CodesServiceException("Failed to check whether the cached code category (" + id
          + ") exists", e);
    }
  }

  /**
   * Check whether the code category with the specified ID exists.
   *
   * @param id the ID uniquely identifying the code category
   *
   * @return <code>true</code> if the code category exists or <code>false</code> otherwise
   *
   * @throws CodesServiceException
   */
  public boolean codeCategoryExists(String id)
    throws CodesServiceException
  {
    try
    {
      return codesDAO.codeCategoryExists(id);
    }
    catch (Throwable e)
    {
      throw new CodesServiceException("Failed to check whether the code category (" + id
          + ") exists", e);
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
      throw new CodesServiceException("Failed to create the cached code (" + code.getName()
          + ") for the cached code category (" + code.getCategoryId() + ")", e);
    }
  }

  /**
   * Create the new cached code category.
   *
   * @param cachedCodeCategory the <code>CachedCodeCategory</code> instance containing the
   *                           information for the new cached code category
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
      throw new CodesServiceException("Failed to create the cached code category ("
          + cachedCodeCategory.getId() + ")", e);
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
      throw new CodesServiceException("Failed to create the code (" + code.getName()
          + ") for the code category (" + code.getCategoryId() + ")", e);
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
      throw new CodesServiceException("Failed to create the code category (" + codeCategory.getId()
          + ")", e);
    }
  }

  /**
   * Delete the cached code category.
   *
   * @param id the ID uniquely identifying the cached code category
   *
   * @throws CodesServiceException
   */
  public void deleteCachedCodeCategory(String id)
    throws CodesServiceException
  {
    try
    {
      codesDAO.deleteCachedCodeCategory(id);
    }
    catch (Throwable e)
    {
      throw new CodesServiceException("Failed to delete the cached code category (" + id + ")", e);
    }
  }

  /**
   * Delete the code.
   *
   * @param id the ID uniquely identifying the code
   *
   * @throws CodesServiceException
   */
  public void deleteCode(String id)
    throws CodesServiceException
  {
    try
    {
      codesDAO.deleteCode(id);
    }
    catch (Throwable e)
    {
      throw new CodesServiceException("Failed to delete the code (" + id + ")", e);
    }
  }

  /**
   * Delete the code category.
   *
   * @param id the ID uniquely identifying the code category
   *
   * @throws CodesServiceException
   */
  public void deleteCodeCategory(String id)
    throws CodesServiceException
  {
    try
    {
      codesDAO.deleteCodeCategory(id);
    }
    catch (Throwable e)
    {
      throw new CodesServiceException("Failed to delete the code category (" + id + ")", e);
    }
  }

  /**
   * Retrieve the cached code category with the specified ID.
   *
   * @param id            the ID uniquely identifying the cached code category
   * @param retrieveCodes retrieve the codes and/or code data for the cached code category
   *
   * @return the cached code category with the specified ID or <code>null</code> if the cached code
   *         category could not be found
   *
   * @throws CodesServiceException
   */
  public CachedCodeCategory getCachedCodeCategory(String id, boolean retrieveCodes)
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
      throw new CodesServiceException("Failed to retrieve the cached code category (" + id + ")",
          e);
    }
  }

  /**
   * Returns all the cached codes for the cached code category with the specified ID.
   *
   * @param cachedCodeCategoryId the ID uniquely identifying the cached code category
   *
   * @return all the cached codes for the cached code category with the specified ID
   *
   * @throws CodesServiceException
   */
  @SuppressWarnings("unused")
  public List<Code> getCachedCodesForCachedCodeCategory(String cachedCodeCategoryId)
    throws CodesServiceException
  {
    try
    {
      return codesDAO.getCachedCodesForCachedCodeCategory(cachedCodeCategoryId);
    }
    catch (Throwable e)
    {
      throw new CodesServiceException("Failed to retrieve the cached codes for the cached code"
          + " category (" + cachedCodeCategoryId + ")", e);
    }
  }

  /**
   * Retrieve the code with the specified ID.
   *
   * @param id the ID uniquely identifying the code
   *
   * @return the code with the specified ID or <code>null</code> if the code could not be found
   *
   * @throws CodesServiceException
   */
  public Code getCode(String id)
    throws CodesServiceException
  {
    try
    {
      return codesDAO.getCode(id);
    }
    catch (Throwable e)
    {
      throw new CodesServiceException("Failed to retrieve the code (" + id + ")", e);
    }
  }

  /**
   * Returns all the code categories associated with the organisation identified by the
   * specified organisation code.
   *
   * @param organisation  the organisation code identifying the organisation
   * @param retrieveCodes retrieve the codes and/or code data for the code categories
   *
   * @return all the code categories associated with the organisation identified by the
   *         specified organisation code
   *
   * @throws CodesServiceException
   */
  public List<CodeCategory> getCodeCategoriesForOrganisation(String organisation,
      boolean retrieveCodes)
    throws CodesServiceException
  {
    try
    {
      return codesDAO.getCodeCategoriesForOrganisation(organisation, retrieveCodes);
    }
    catch (Throwable e)
    {
      throw new CodesServiceException(
          "Failed to retrieve the code categories for the organisation (" + organisation + ")", e);
    }
  }

  /**
   * Retrieve the code category with the specified ID.
   *
   * @param id            the ID uniquely identifying the code category
   * @param retrieveCodes retrieve the codes and/or code data for the code category
   *
   * @return the code category with the specified ID or <code>null</code> if the code category
   *         could not be found
   *
   * @throws CodesServiceException
   */
  public CodeCategory getCodeCategory(String id, boolean retrieveCodes)
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
      throw new CodesServiceException("Failed to retrieve the code category (" + id + ")", e);
    }
  }

  /**
   * Retrieve the code category with the specified ID using the specified parameters.
   *
   * @param id            the ID uniquely identifying the code category
   * @param parameters    the parameters
   * @param retrieveCodes retrieve the codes and/or code data for the code category
   *
   * @return the code category with the specified ID or <code>null</code> if the code category
   *         could not be found
   *
   * @throws CodesServiceException
   */
  public CodeCategory getCodeCategoryWithParameters(String id, Map<String, String> parameters,
      boolean retrieveCodes)
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
      throw new CodesServiceException("Failed to retrieve the code category (" + id + ")", e);
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
   *         <b>Custom</b> code data or <code>null</code> if the code category could not be found
   *
   * @throws CodesServiceException
   */
  public CodeCategory getCodeProviderCodeCategory(CodeCategory codeCategory, Date lastRetrieved,
      boolean returnCodesIfCurrent)
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
      throw new CodesServiceException("Failed to retrieve the code provider code category ("
          + codeCategory.getId() + ")", e);
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
   *         <b>Custom</b> code data or <code>null</code> if the code category could not be found
   *
   * @throws CodesServiceException
   */
  public CodeCategory getCodeProviderCodeCategoryWithParameters(CodeCategory codeCategory,
      Map<String, String> parameters, Date lastRetrieved, boolean returnCodesIfCurrent)
    throws CodesServiceException
  {
    try
    {
      for (ICodeProvider codeProvider : codeProviders)
      {
        CodeCategory codeProviderCodeCategory =
          codeProvider.getCodeCategoryWithParameters(codeCategory, parameters, lastRetrieved,
            returnCodesIfCurrent);

        if (codeProviderCodeCategory != null)
        {
          return codeProviderCodeCategory;
        }
      }

      return null;
    }
    catch (Throwable e)
    {
      throw new CodesServiceException("Failed to retrieve the code provider code category ("
          + codeCategory.getId() + ") with parameters", e);
    }
  }

  /**
   * Returns all the codes for the code category with the specified ID.
   *
   * @param codeCategoryId the ID uniquely identifying the code category
   *
   * @return all the codes for the code category with the specified ID
   *
   * @throws CodesServiceException
   */
  public List<Code> getCodesForCodeCategory(String codeCategoryId)
    throws CodesServiceException
  {
    try
    {
      return codesDAO.getCodesForCodeCategory(codeCategoryId);
    }
    catch (Throwable e)
    {
      throw new CodesServiceException("Failed to retrieve the codes for the code category ("
          + codeCategoryId + ")", e);
    }
  }

  /**
   * Returns the number of code categories associated with the organisation identified by the
   * specified organisation code.
   *
   * @param organisation the organisation code identifying the organisation
   *
   * @return the number of code categories associated with the organisation identified by the
   *         specified organisation code
   *
   * @throws CodesServiceException
   */
  public int getNumberOfCodeCategoriesForOrganisation(String organisation)
    throws CodesServiceException
  {
    try
    {
      return codesDAO.getNumberOfCodeCategoriesForOrganisation(organisation);
    }
    catch (Throwable e)
    {
      throw new CodesServiceException(
          "Failed to retrieve the number of code categories for the organisation (" + organisation
          + ")", e);
    }
  }

  /**
   * Returns the number of codes for the code category with the specified ID.
   *
   * @param codeCategoryId the ID uniquely identifying the code category
   *
   * @return the number of codes for the code category with the specified ID
   *
   * @throws CodesServiceException
   */
  public int getNumberOfCodesForCodeCategory(String codeCategoryId)
    throws CodesServiceException
  {
    try
    {
      return codesDAO.getNumberOfCodesForCodeCategory(codeCategoryId);
    }
    catch (Throwable e)
    {
      throw new CodesServiceException(
          "Failed to retrieve the number of codes for the code category (" + codeCategoryId + ")",
          e);
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
   *         code data
   *
   * @throws CodesServiceException
   */
  public CodeCategory getRemoteCodeCategory(CodeCategory codeCategory, Date lastRetrieved,
      boolean returnCodesIfCurrent)
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
        throw new CodesServiceException("The code category type (" + codeCategory.getCategoryType()
            + ") for the remote code category (" + codeCategory.getId() + ") is not supported");
      }
      else
      {
        throw new CodesServiceException("The code category type (" + codeCategory.getCategoryType()
            + ") for the remote code category (" + codeCategory.getId() + ") is not supported");
      }
    }
    catch (Throwable e)
    {
      throw new CodesServiceException("Failed to retrieve the remote code category ("
          + codeCategory.getId() + ")", e);
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
   *         code data
   *
   * @throws CodesServiceException
   */
  public CodeCategory getRemoteCodeCategoryWithParameters(CodeCategory codeCategory,
      Map<String, String> parameters, Date lastRetrieved, boolean returnCodesIfCurrent)
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
        throw new CodesServiceException("The code category type (" + codeCategory.getCategoryType()
            + ") for the remote code category (" + codeCategory.getId() + ") is not supported");
      }
      else
      {
        throw new CodesServiceException("The code category type (" + codeCategory.getCategoryType()
            + ") for the remote code category (" + codeCategory.getId() + ") is not supported");
      }
    }
    catch (Throwable e)
    {
      throw new CodesServiceException("Failed to retrieve the remote code category ("
          + codeCategory.getId() + ") with parameters", e);
    }
  }

  /**
   * Initialise the Codes Service instance.
   *
   * @throws CodesServiceException
   */
  @PostConstruct
  public void init()
    throws CodesServiceException
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
      throw new CodesServiceException("Failed to initialise the Codes Service", e);
    }
  }

  /**
   * Is the cached code category current?
   *
   * @param id the ID uniquely identifying the cached code category
   *
   * @return <code>true</code> if the cached code category is current or <code>false</code>
   *         otherwise
   *
   * @throws CodesServiceException
   */
  public boolean isCachedCodeCategoryCurrent(String id)
    throws CodesServiceException
  {
    try
    {
      return codesDAO.isCachedCodeCategoryCurrent(id);
    }
    catch (Throwable e)
    {
      throw new CodesServiceException("Failed to check whether the cached code category (" + id
          + ") is current", e);
    }
  }

  /**
   * Update the existing cached code category.
   *
   * @param cachedCodeCategory the <code>CachedCodeCategory</code> instance containing the updated
   *                           information for the cached code category
   * @param updatedBy          the username identifying the user that updated the cached code
   *                           category
   *
   * @return the updated cached code category
   *
   * @throws CodesServiceException
   */
  public CachedCodeCategory updateCachedCodeCategory(CachedCodeCategory cachedCodeCategory,
      String updatedBy)
    throws CodesServiceException
  {
    try
    {
      return codesDAO.updateCachedCodeCategory(cachedCodeCategory);
    }
    catch (Throwable e)
    {
      throw new CodesServiceException("Failed to update the cached code category ("
          + cachedCodeCategory.getId() + ")", e);
    }
  }

  /**
   * Update the existing code.
   *
   * @param code      the <code>Code</code> instance containing the updated information for the code
   * @param updatedBy the username identifying the user that updated the code
   *
   * @return the updated code
   *
   * @throws CodesServiceException
   */
  public Code updateCode(Code code, String updatedBy)
    throws CodesServiceException
  {
    try
    {
      return codesDAO.updateCode(code);
    }
    catch (Throwable e)
    {
      throw new CodesServiceException("Failed to update the code (" + code.getId() + ")", e);
    }
  }

  /**
   * Update the existing code category.
   *
   * @param codeCategory the <code>CodeCategory</code> instance containing the updated information
   *                     for the code category
   * @param updatedBy    the username identifying the user that updated the code category
   *
   * @return the updated code category
   *
   * @throws CodesServiceException
   */
  public CodeCategory updateCodeCategory(CodeCategory codeCategory, String updatedBy)
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
      throw new CodesServiceException("Failed to update the code category (" + codeCategory.getId()
          + ")", e);
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
      throw new CodesServiceException("Failed to cache the remote code category ("
          + codeCategory.getId() + ")", e);
    }
  }

  private CodeCategory getRemoteWebServiceCodeCategory(CodeCategory codeCategory,
      Date lastRetrieved, boolean returnCodesIfCurrent)
    throws CodesServiceException
  {
    try
    {
      URL wsdlLocation = Thread.currentThread().getContextClassLoader().getResource(
          "META-INF/wsdl/CodesService.wsdl");

      guru.mmp.service.codes.ws.CodesService service =
        new guru.mmp.service.codes.ws.CodesService(wsdlLocation,
          new QName("http://ws.codes.service.mmp.guru", "CodesService"));

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

      guru.mmp.service.codes.ws.CodeCategory remoteCodeCategory =
        codesService.getCodeCategory(codeCategory.getId(), DateUtil.toCalendar(lastRetrieved),
          returnCodesIfCurrent);

      codeCategory.setUpdated(DateUtil.toDate(remoteCodeCategory.getLastUpdated()));

      List<Code> codes = new ArrayList<>();

      for (guru.mmp.service.codes.ws.Code remoteCode : remoteCodeCategory.getCodes())
      {
        codes.add(new Code(remoteCode.getId(), codeCategory.getId(), remoteCode.getName(),
            remoteCode.getDescription(), remoteCode.getValue()));
      }

      codeCategory.setCodes(codes);
      codeCategory.setCodeData(remoteCodeCategory.getCodeData());

      return codeCategory;
    }
    catch (Throwable e)
    {
      throw new CodesServiceException("Failed to retrieve the remote code category ("
          + codeCategory.getId() + ") from the web service (" + codeCategory.getEndPoint()
          + ")", e);
    }
  }

  private CodeCategory getRemoteWebServiceCodeCategoryWithParameters(CodeCategory codeCategory,
      Map<String, String> parameters, Date lastRetrieved, boolean returnCodesIfCurrent)
    throws CodesServiceException
  {
    try
    {
      URL wsdlLocation = Thread.currentThread().getContextClassLoader().getResource(
          "META-INF/wsdl/CodesService.wsdl");

      guru.mmp.service.codes.ws.CodesService service =
        new guru.mmp.service.codes.ws.CodesService(wsdlLocation,
          new QName("http://ws.codes.service.mmp.guru", "CodesService"));

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

      guru.mmp.service.codes.ws.CodeCategory remoteCodeCategory =
        codesService.getCodeCategoryWithParameters(codeCategory.getId(), wsParameters,
          DateUtil.toCalendar(lastRetrieved), returnCodesIfCurrent);

      codeCategory.setUpdated(DateUtil.toDate(remoteCodeCategory.getLastUpdated()));

      List<Code> codes = new ArrayList<>();

      for (guru.mmp.service.codes.ws.Code remoteCode : remoteCodeCategory.getCodes())
      {
        codes.add(new Code(remoteCode.getId(), codeCategory.getId(), remoteCode.getName(),
            remoteCode.getDescription(), remoteCode.getValue()));
      }

      codeCategory.setCodes(codes);
      codeCategory.setCodeData(remoteCodeCategory.getCodeData());

      return codeCategory;
    }
    catch (Throwable e)
    {
      throw new CodesServiceException("Failed to retrieve the remote code category ("
          + codeCategory.getId() + ") with parameters from the web service ("
          + codeCategory.getEndPoint() + ")", e);
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
        logger.info("Initialising the code provider (" + codeProviderConfig.getName()
            + ") with class (" + codeProviderConfig.getClassName() + ")");

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
          logger.error("Failed to register the code provider (" + codeProviderConfig.getClassName()
              + ") since the code provider class"
              + " does not provide a constructor with the required signature");
        }
      }
      catch (Throwable e)
      {
        logger.error("Failed to initialise the code provider (" + codeProviderConfig.getName()
            + ") with class (" + codeProviderConfig.getClassName() + ")", e);
      }
    }
  }

  /**
   * Initialise the configuration for the Codes Service instance.
   *
   * @throws CodesServiceException
   */
  private void initConfiguration()
    throws CodesServiceException {}

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
          logger.debug("Reading the codes configuration file ("
              + configurationFile.toURI().toString() + ")");
        }

        // Retrieve a document builder instance using the factory
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();

        builderFactory.setValidating(true);

        // builderFactory.setNamespaceAware(true);
        DocumentBuilder builder = builderFactory.newDocumentBuilder();

        builder.setEntityResolver(new DtdJarResolver("CodesConfig.dtd",
            "META-INF/CodesConfig.dtd"));
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

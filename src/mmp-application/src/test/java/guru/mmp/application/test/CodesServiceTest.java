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

package guru.mmp.application.test;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.codes.Code;
import guru.mmp.application.codes.CodeCategory;
import guru.mmp.application.codes.CodeCategoryType;
import guru.mmp.application.codes.ICodesService;
import guru.mmp.common.test.ApplicationJUnit4ClassRunner;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

//~--- JDK imports ------------------------------------------------------------

import java.util.*;

import javax.inject.Inject;

/**
 * The <code>CodesServiceTest</code> class contains the implementation of the JUnit
 * tests for the <code>CodesService</code> class.
 *
 * @author Marcus Portmann
 */
@RunWith(ApplicationJUnit4ClassRunner.class)
public class CodesServiceTest
{
  private static int codeCategoryCount;
  private static int codeCount;
  @Inject
  private ICodesService codesService;

  /**
   * Test the code functionality.
   *
   * @throws Exception
   */
  @Test
  public void codeTest()
    throws Exception
  {
    CodeCategory codeCategory = getTestLocalCustomCodeCategoryDetails();

    codesService.createCodeCategory(codeCategory);

    CodeCategory retrievedCodeCategory = codesService.getCodeCategory(codeCategory.getId(), false);

    compareCodeCategories(codeCategory, retrievedCodeCategory, true);

    Code code = getTestCodeDetails(codeCategory.getId());

    codesService.createCode(code);

    Code retrievedCode = codesService.getCode(codeCategory.getId(), code.getId());

    compareCodes(code, retrievedCode);

    code.setName("Updated " + code.getName());
    code.setDescription("Updated " + code.getDescription());
    code.setValue("Updated " + code.getValue());

    codesService.updateCode(code);

    retrievedCode = codesService.getCode(codeCategory.getId(), code.getId());

    compareCodes(code, retrievedCode);

    codesService.deleteCode(codeCategory.getId(), code.getId());

    retrievedCode = codesService.getCode(codeCategory.getId(), code.getId());

    if (retrievedCode != null)
    {
      fail("Retrieved the code (" + code.getId() + ") for the code category ("
          + codeCategory.getId() + ") that should have been deleted");
    }
  }

  /**
   * Test the retrieve code categories for organisation functionality.
   *
   * @throws Exception
   */
  @Test
  public void getCodeCategoriesTest()
    throws Exception
  {
    CodeCategory codeCategory = getTestLocalCustomCodeCategoryDetails();

    codesService.createCodeCategory(codeCategory);

    List<CodeCategory> retrievedCodeCategories = codesService.getCodeCategories(false);

    assertEquals("The correct number of code categories (1) was not retrieved", 1,
        retrievedCodeCategories.size());

    compareCodeCategories(codeCategory, retrievedCodeCategories.get(0), false);

    retrievedCodeCategories = codesService.getCodeCategories(true);

    assertEquals("The correct number of code categories (1) was not retrieved", 1,
        retrievedCodeCategories.size());

    compareCodeCategories(codeCategory, retrievedCodeCategories.get(0), true);
  }

  /**
   * Test the local custom code category functionality.
   *
   * @throws Exception
   */
  @Test
  public void localCustomCodeCategoryTest()
    throws Exception
  {
    CodeCategory codeCategory = getTestLocalCustomCodeCategoryDetails();

    codesService.createCodeCategory(codeCategory);

    CodeCategory retrievedCodeCategory = codesService.getCodeCategory(codeCategory.getId(), false);

    compareCodeCategories(codeCategory, retrievedCodeCategory, true);
  }

  /**
   * Test the remote HTTP service code category functionality.
   *
   * @throws Exception
   */
  @Test
  public void localRemoteHttpServiceCategoryTest()
    throws Exception
  {
    CodeCategory codeCategory = getTestRemoteHttpServiceCodeCategoryDetails();

    codesService.createCodeCategory(codeCategory);

    CodeCategory retrievedCodeCategory = codesService.getCodeCategory(codeCategory.getId(), false);

    compareCodeCategories(codeCategory, retrievedCodeCategory, true);
  }

  /**
   * Test the remote web service code category functionality.
   *
   * @throws Exception
   */
  @Test
  public void localRemoteWebServiceCategoryTest()
    throws Exception
  {
    CodeCategory codeCategory = getTestRemoteWebServiceCodeCategoryDetails();

    codesService.createCodeCategory(codeCategory);

    CodeCategory retrievedCodeCategory = codesService.getCodeCategory(codeCategory.getId(), false);

    compareCodeCategories(codeCategory, retrievedCodeCategory, true);
  }

  /**
   * Test the local standard code category functionality.
   *
   * @throws Exception
   */
  @Test
  public void localStandardCodeCategoryTest()
    throws Exception
  {
    CodeCategory codeCategory = getTestLocalStandardCodeCategoryDetails();

    codesService.createCodeCategory(codeCategory);

    CodeCategory retrievedCodeCategory = codesService.getCodeCategory(codeCategory.getId(), false);

    compareCodeCategories(codeCategory, retrievedCodeCategory, true);

    codeCategory.setCategoryType(CodeCategoryType.REMOTE_WEB_SERVICE);
    codeCategory.setName("Updated " + codeCategory.getName());
    codeCategory.setDescription("Updated " + codeCategory.getDescription());
    codeCategory.setCodeData("Updated Code Data");
    codeCategory.setEndPoint("Updated End Point");
    codeCategory.setIsEndPointSecure(true);
    codeCategory.setIsCacheable(true);
    codeCategory.setCacheExpiry(777);
    codeCategory.setUpdated(new Date());

    codesService.updateCodeCategory(codeCategory);

    retrievedCodeCategory = codesService.getCodeCategory(codeCategory.getId(), false);

    compareCodeCategories(codeCategory, retrievedCodeCategory, true);

    codesService.deleteCodeCategory(codeCategory.getId());

    retrievedCodeCategory = codesService.getCodeCategory(codeCategory.getId(), false);

    if (retrievedCodeCategory != null)
    {
      fail("Retrieved the code category (" + codeCategory.getId()
          + ") that should have been deleted");
    }
  }

  /**
   * Test the local standard codes functionality.
   *
   * @throws Exception
   */
  @Test
  public void localStandardCodesTest()
    throws Exception
  {
    CodeCategory codeCategory = getTestLocalStandardCodeCategoryDetails();

    codesService.createCodeCategory(codeCategory);

    CodeCategory retrievedCodeCategory = codesService.getCodeCategory(codeCategory.getId(), false);

    compareCodeCategories(codeCategory, retrievedCodeCategory, true);

    List<Code> codes = new ArrayList<>();

    for (int i = 0; i < 10; i++)
    {
      codes.add(getTestCodeDetails(codeCategory.getId()));
    }

    for (Code code : codes)
    {
      codesService.createCode(code);
    }

    int numberOfCodes = codesService.getNumberOfCodesForCodeCategory(codeCategory.getId());

    assertEquals("The correct number of codes (" + codes.size() + ") was not retrieved",
        codes.size(), numberOfCodes);

    List<Code> retrievedCodes = codesService.getCodesForCodeCategory(codeCategory.getId());

    compareCodes(codes, retrievedCodes);
  }

  private static synchronized Code getTestCodeDetails(UUID codeCategoryId)
  {
    codeCount++;

    Code code = new Code();
    code.setId("Test Code Id " + codeCount);
    code.setCategoryId(codeCategoryId);
    code.setName("Test Code Name " + codeCount);
    code.setDescription("Test Code Description " + codeCount);
    code.setValue("Test Code Value " + codeCount);

    return code;
  }

  private static synchronized CodeCategory getTestLocalCustomCodeCategoryDetails()
  {
    codeCategoryCount++;

    CodeCategory codeCategory = new CodeCategory();

    codeCategory.setId(UUID.randomUUID());
    codeCategory.setCategoryType(CodeCategoryType.LOCAL_CUSTOM);
    codeCategory.setName("Test Code Category Name " + codeCategoryCount);
    codeCategory.setDescription("Test Code Category Description " + codeCategoryCount);
    codeCategory.setCodeData("THIS IS THE CODE DATA");
    codeCategory.setEndPoint(null);
    codeCategory.setIsEndPointSecure(false);
    codeCategory.setIsCacheable(false);
    codeCategory.setCacheExpiry(null);
    codeCategory.setUpdated(null);

    return codeCategory;
  }

  private static synchronized CodeCategory getTestLocalStandardCodeCategoryDetails()
  {
    codeCategoryCount++;

    CodeCategory codeCategory = new CodeCategory();

    codeCategory.setId(UUID.randomUUID());
    codeCategory.setCategoryType(CodeCategoryType.LOCAL_STANDARD);
    codeCategory.setName("Test Code Category Name " + codeCategoryCount);
    codeCategory.setDescription("Test Code Category Description " + codeCategoryCount);
    codeCategory.setCodeData(null);
    codeCategory.setEndPoint(null);
    codeCategory.setIsEndPointSecure(false);
    codeCategory.setIsCacheable(false);
    codeCategory.setCacheExpiry(null);
    codeCategory.setUpdated(null);

    return codeCategory;
  }

  private static synchronized CodeCategory getTestRemoteHttpServiceCodeCategoryDetails()
  {
    codeCategoryCount++;

    CodeCategory codeCategory = new CodeCategory();

    codeCategory.setId(UUID.randomUUID());
    codeCategory.setCategoryType(CodeCategoryType.REMOTE_HTTP_SERVICE);
    codeCategory.setName("Test Code Category Name " + codeCategoryCount);
    codeCategory.setDescription("Test Code Category Description " + codeCategoryCount);
    codeCategory.setCodeData(null);
    codeCategory.setEndPoint("This is the end point");
    codeCategory.setIsEndPointSecure(true);
    codeCategory.setIsCacheable(true);
    codeCategory.setCacheExpiry(60 * 60 * 24 * 7);
    codeCategory.setUpdated(null);

    return codeCategory;
  }

  private static synchronized CodeCategory getTestRemoteWebServiceCodeCategoryDetails()
  {
    codeCategoryCount++;

    CodeCategory codeCategory = new CodeCategory();

    codeCategory.setId(UUID.randomUUID());
    codeCategory.setCategoryType(CodeCategoryType.REMOTE_WEB_SERVICE);
    codeCategory.setName("Test Code Category Name " + codeCategoryCount);
    codeCategory.setDescription("Test Code Category Description " + codeCategoryCount);
    codeCategory.setCodeData(null);
    codeCategory.setEndPoint("This is the end point");
    codeCategory.setIsEndPointSecure(true);
    codeCategory.setIsCacheable(true);
    codeCategory.setCacheExpiry(60 * 60 * 24 * 7);
    codeCategory.setUpdated(null);

    return codeCategory;
  }

  private void compareCodeCategories(CodeCategory codeCategory1, CodeCategory codeCategory2,
      boolean checkCodeData)
  {
    assertEquals("The ID values for the two code categories do not match", codeCategory1.getId(),
        codeCategory2.getId());
    assertEquals("The category type values for the two code categories do not match",
        codeCategory1.getCategoryType(), codeCategory2.getCategoryType());
    assertEquals("The name values for the two code categories do not match",
        codeCategory1.getName(), codeCategory2.getName());
    assertEquals("The description values for the two code categories do not match",
        codeCategory1.getDescription(), codeCategory2.getDescription());

    if (checkCodeData)
    {
      assertEquals("The code data values for the two code categories do not match",
          codeCategory1.getCodeData(), codeCategory2.getCodeData());
    }

    assertEquals("The end point values for the two code categories do not match",
        codeCategory1.getEndPoint(), codeCategory2.getEndPoint());
    assertEquals("The is end point secure values for the two code categories do not match",
        codeCategory1.getIsEndPointSecure(), codeCategory2.getIsEndPointSecure());
    assertEquals("The is cacheable values for the two code categories do not match",
        codeCategory1.getIsCacheable(), codeCategory2.getIsCacheable());
    assertEquals("The cache expiry values for the two code categories do not match",
        codeCategory1.getCacheExpiry(), codeCategory2.getCacheExpiry());
    assertEquals("The updated values for the two code categories do not match",
        codeCategory1.getUpdated(), codeCategory2.getUpdated());
  }

  private void compareCodes(Code code1, Code code2)
  {
    assertEquals("The ID values for the two codes do not match", code1.getId(), code2.getId());
    assertEquals("The category ID values for the two codes do not match", code1.getCategoryId(),
        code2.getCategoryId());
    assertEquals("The name values for the two codes do not match", code1.getName(),
        code2.getName());
    assertEquals("The description values for the two codes do not match", code1.getDescription(),
        code2.getDescription());
    assertEquals("The value values for the two codes do not match", code1.getValue(),
        code2.getValue());
  }

  private void compareCodes(List<Code> codes1, List<Code> codes2)
  {
    for (Code code1 : codes1)
    {
      boolean foundCode = false;

      for (Code code2 : codes2)
      {
        if (code1.getId().equals(code2.getId()))
        {
          compareCodes(code1, code2);
          foundCode = true;

          break;
        }
      }

      if (!foundCode)
      {
        fail("Failed to find the code (" + code1.getId() + ") in the list of codes");
      }
    }
  }
}

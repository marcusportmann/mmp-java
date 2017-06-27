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

package guru.mmp.application.messaging.messages;

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.application.codes.CodeCategory;
import guru.mmp.application.codes.CodeCategoryType;
import guru.mmp.common.util.ISO8601;
import guru.mmp.common.util.StringUtil;
import guru.mmp.common.wbxml.Element;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>CodeCategoryData</code> class stores the information for a code category.
 *
 * @author Marcus Portmann
 */
public class CodeCategoryData
  implements Serializable
{
  private static final long serialVersionUID = 1000000;

  /**
   * The custom code data for the category if the code data type is "Custom".
   */
  private byte[] codeData;

  /**
   * The type of code data for the code category e.g. Standard, Custom, etc.
   */
  private CodeDataType codeDataType;

  /**
   * The codes for the code category if the code data type is "Standard".
   */
  private List<CodeData> codes;

  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the code category.
   */
  private UUID id;

  /**
   * The date and time the code category was last updated.
   */
  private LocalDateTime lastUpdated;

  /**
   * The name of the code category.
   */
  private String name;

  /**
   * Constructs a new <code>CodeCategoryData</code>.
   *
   * @param codeCategory the code category
   */
  public CodeCategoryData(CodeCategory codeCategory)
  {
    if (codeCategory.getCategoryType() == CodeCategoryType.LOCAL_STANDARD)
    {
      this.codeDataType = CodeDataType.STANDARD;
    }
    else if (codeCategory.getCategoryType() == CodeCategoryType.LOCAL_CUSTOM)
    {
      this.codeDataType = CodeDataType.CUSTOM;
    }
    else if (codeCategory.getCategoryType() == CodeCategoryType.REMOTE_HTTP_SERVICE)
    {
      this.codeDataType = CodeDataType.REMOTE;
    }
    else if (codeCategory.getCategoryType() == CodeCategoryType.REMOTE_WEB_SERVICE)
    {
      this.codeDataType = CodeDataType.REMOTE;
    }

    this.id = codeCategory.getId();
    this.name = codeCategory.getName();
    this.lastUpdated = codeCategory.getUpdated();

    this.codes = new ArrayList<>();

    if (codeCategory.getCodes() != null)
    {
      this.codes.addAll(codeCategory.getCodes().stream().map(CodeData::new).collect(
          Collectors.toList()));
    }

    if (codeCategory.getCodeData() != null)
    {
      try
      {
        this.codeData = codeCategory.getCodeData().getBytes("UTF-8");
      }
      catch (Throwable e)
      {
        throw new RuntimeException(
            "Failed to convert the binary code data for the code category to a UTF-8 string", e);
      }
    }
    else
    {
      this.codeData = new byte[0];
    }
  }

  /**
   * Constructs a new <code>CodeCategoryData</code>.
   *
   * @param element the WBXML element containing the code category data
   */
  CodeCategoryData(Element element)
  {
    this.codeDataType = CodeDataType.fromCode(Integer.parseInt(element.getChildText(
        "CodeDataType")));
    this.id = UUID.fromString(element.getChildText("Id"));
    this.name = element.getChildText("Name");

    String lastUpdatedValue = element.getChildText("LastUpdated");

    if (lastUpdatedValue.contains("T"))
    {
      try
      {
        this.lastUpdated = ISO8601.toLocalDateTime(lastUpdatedValue);
      }
      catch (Throwable e)
      {
        throw new RuntimeException("Failed to parse the LastUpdated ISO8601 timestamp ("
            + lastUpdatedValue + ") for the" + " code category data", e);
      }
    }
    else
    {
      this.lastUpdated = LocalDateTime.ofInstant(Instant.ofEpochSecond(Long.parseLong(
          lastUpdatedValue)), ZoneId.systemDefault());
    }

    if (element.hasChild("CodeData"))
    {
      this.codeData = element.getChildOpaque("CodeData");
    }

    this.codes = new ArrayList<>();

    if (element.hasChild("Codes"))
    {
      List<Element> codeElements = element.getChild("Codes").getChildren("Code");

      this.codes.addAll(codeElements.stream().map(CodeData::new).collect(Collectors.toList()));

      this.codeData = element.getChildOpaque("CodeData");
    }
  }

  /**
   * The enumeration giving the possible code data types for a code category.
   */
  public enum CodeDataType
  {
    STANDARD(0, "Standard"), CUSTOM(1, "Custom"), REMOTE(2, "Remote");

    private int code;
    private String name;

    CodeDataType(int code, String name)
    {
      this.code = code;
      this.name = name;
    }

    /**
     * Returns the type of code data given by the specified numeric code value.
     *
     * @param code the numeric code value identifying the type of code data
     *
     * @return the type of code data given by the specified numeric code value
     */
    public static CodeDataType fromCode(int code)
    {
      switch (code)
      {
        case 0:
          return CodeDataType.STANDARD;

        case 1:
          return CodeDataType.CUSTOM;

        case 2:
          return CodeDataType.REMOTE;

        default:
          return CodeDataType.STANDARD;
      }
    }

    /**
     * Returns the numeric code value identifying the type of code data.
     *
     * @return the numeric code value identifying the type of code data
     */
    public int getCode()
    {
      return code;
    }

    /**
     * Returns the name of the type of code data.
     *
     * @return the name of the type of code data
     */
    public String getName()
    {
      return name;
    }

    /**
     * Return the string representation of the <code>CodeDataType</code>
     * enumeration value.
     *
     * @return the string representation of the <code>CodeDataType</code>
     * enumeration value
     */
    public String toString()
    {
      return name;
    }
  }

  /**
   * Returns the custom code data for the category if the code data type is "Custom".
   *
   * @return the custom code data for the category if the code data type is "Custom"
   */
  public byte[] getCodeData()
  {
    return codeData;
  }

  /**
   * Returns the type of code data for the code category e.g. Standard, Custom, etc.
   *
   * @return the type of code data for the code category e.g. Standard, Custom, etc
   */
  public CodeDataType getCodeDataType()
  {
    return codeDataType;
  }

  /**
   * Returns the codes for the code category if the code data type is "Standard".
   *
   * @return the codes for the code category if the code data type is "Standard"
   */
  public List<CodeData> getCodes()
  {
    return codes;
  }

  /**
   * Returns the Universally Unique Identifier (UUID) used to uniquely identify the code category.
   *
   * @return the Universally Unique Identifier (UUID) used to uniquely identify the code category
   */
  public UUID getId()
  {
    return id;
  }

  /**
   * Returns the date and time the code category was last updated.
   *
   * @return the date and time the code category was last updated
   */
  public LocalDateTime getLastUpdated()
  {
    return lastUpdated;
  }

  /**
   * Returns the name of the code category.
   *
   * @return the name of the code category
   */
  public String getName()
  {
    return name;
  }

  /**
   * Returns a string representation of the object.
   *
   * @return a string representation of the object
   */
  @Override
  public String toString()
  {
    StringBuilder buffer = new StringBuilder();

    buffer.append("CodeCategory {");
    buffer.append("id=\"").append(getId()).append("\", ");
    buffer.append("name=\"").append(getName()).append("\", ");
    buffer.append("lastUpdated=\"").append(ISO8601.fromLocalDateTime(getLastUpdated())).append(
        "\", ");
    buffer.append("codeData=\"").append((getCodeData() != null)
        ? getCodeData().length
        : 0).append(" bytes of custom code data\"");

    if ((getCodes() != null) && (getCodes().size() > 0))
    {
      buffer.append(", codes = {");

      int count = 0;

      for (CodeData code : getCodes())
      {
        if (count > 0)
        {
          buffer.append(", Code {");
        }
        else
        {
          buffer.append("Code {");
        }

        buffer.append("id=\"").append(code.getId()).append("\", ");
        buffer.append("categoryId=\"").append(code.getCategoryId()).append("\", ");
        buffer.append("name=\"").append(code.getName()).append("\", ");
        buffer.append("value=\"").append(code.getValue()).append("\"");

        buffer.append("}");

        count++;
      }

      buffer.append("}");
    }

    buffer.append("}");

    return buffer.toString();
  }

  /**
   * Returns the WBXML element containing the code category data.
   *
   * @return the WBXML element containing the code category data
   */
  Element toElement()
  {
    Element codeCategoryElement = new Element("CodeCategory");

    codeCategoryElement.addContent(new Element("CodeDataType", String.valueOf(
        codeDataType.getCode())));
    codeCategoryElement.addContent(new Element("Id", id.toString()));
    codeCategoryElement.addContent(new Element("Name", StringUtil.notNull(name)));
    codeCategoryElement.addContent(new Element("LastUpdated",
        (lastUpdated == null)
        ? ISO8601.now()
        : ISO8601.fromLocalDateTime(lastUpdated)));

    if (codeData != null)
    {
      codeCategoryElement.addContent(new Element("CodeData", codeData));
    }

    Element codesElement = new Element("Codes");

    if (codes != null)
    {
      for (CodeData code : codes)
      {
        codesElement.addContent(code.toElement());
      }
    }

    codeCategoryElement.addContent(codesElement);

    return codeCategoryElement;
  }
}

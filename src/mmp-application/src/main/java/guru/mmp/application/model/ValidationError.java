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

package guru.mmp.application.model;

//~--- non-JDK imports --------------------------------------------------------

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.ConstraintViolation;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.HashMap;
import java.util.Map;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>ValidationError</code> class represents a validation error that occurred while
 * validating an entity.
 *
 * @author Marcus Portmann
 */
@ApiModel(value = "ValidationError")
@JsonPropertyOrder({ "property", "message", "value" })
@XmlType(name = "ValidationError", namespace = "http://model.application.mmp.guru",
    propOrder = { "property", "message", "value" })
@XmlAccessorType(XmlAccessType.FIELD)
public class ValidationError
{
  /**
   * The path for the property that resulted in the validation error.
   */
  @ApiModelProperty(value = "The path for the property that resulted in the validation error",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Property", required = true)
  @NotNull
  private String property;

  /**
   * The error message for the validation error.
   */
  @ApiModelProperty(value = "The error message for the validation error", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Message", required = true)
  @NotNull
  private String message;

  /**
   * The error message template for the validation error.
   */
  @ApiModelProperty(value = "The error message template for the validation error")

  @JsonProperty
  @XmlElement(name = "Template")
  @NotNull
  private String template;

  /**
   * The attributes applied to the error message template associated with the validation error.
   */
  @ApiModelProperty(
      value = "The attributes applied to the error message template associated with the validation error")
  @JsonProperty
  @XmlElement(name = "Attributes")
  @NotNull
  private Map<String, String> attributes;

  /**
   * The invalid value for the property that resulted in the validation error.
   */
  @ApiModelProperty(
      value = "The invalid value for the property that resulted in the validation error",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Value")
  @NotNull
  private String value;

  /**
   * Constructs a new <code>ValidationError</code>.
   *
   * @param constraintViolation
   */
  public ValidationError(ConstraintViolation<?> constraintViolation)
  {
    this.property = constraintViolation.getPropertyPath().toString();
    this.message = constraintViolation.getMessage();
    this.template = constraintViolation.getMessageTemplate();
    this.value = (constraintViolation.getInvalidValue() == null)
        ? "null"
        : constraintViolation.getInvalidValue().toString();

    this.attributes = new HashMap<>();

    Map<String, Object> attributes = constraintViolation.getConstraintDescriptor().getAttributes();

    for (String attributeName : attributes.keySet())
    {
      Object attributeValue = attributes.get(attributeName);

      this.attributes.put(attributeName, attributeValue.toString());
    }
  }

  /**
   * Constructs a new <code>ValidationError</code>.
   *
   * @param property the path for the property that resulted in the validation error
   * @param message  the error message for the validation error
   * @param value    the invalid value for the property that resulted in the validation error
   */
  public ValidationError(String property, String message, String value)
  {
    this.property = property;
    this.message = message;
    this.value = value;
  }

  /**
   * Returns the error message for the validation error.
   *
   * @return the error message for the validation error
   */
  public String getMessage()
  {
    return message;
  }

  /**
   * Returns the path for the property that resulted in the validation error.
   *
   * @return the path for the property that resulted in the validation error
   */
  public String getProperty()
  {
    return property;
  }

  /**
   * Returns the invalid value for the property that resulted in the validation error.
   *
   * @return the invalid value for the property that resulted in the validation error
   */
  public String getValue()
  {
    return value;
  }

  /**
   * Set the error message for the validation error.
   *
   * @param message the error message for the validation error
   */
  public void setMessage(String message)
  {
    this.message = message;
  }

  /**
   * Set the path for the property that resulted in the validation error.
   *
   * @param property the path for the property that resulted in the validation error
   */
  public void setProperty(String property)
  {
    this.property = property;
  }

  /**
   * Set the invalid value for the property that resulted in the validation error.
   *
   * @param value the invalid value for the property that resulted in the validation error
   */
  public void setValue(String value)
  {
    this.value = value;

  }
}

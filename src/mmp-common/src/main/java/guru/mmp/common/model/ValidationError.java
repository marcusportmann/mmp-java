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

package guru.mmp.common.model;

import java.util.ArrayList;
import java.util.List;

/**
 * The <code>ValidationError</code> class stores the validation error information for a particular
 * entity e.g. a method parameter.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public class ValidationError
  implements java.io.Serializable
{
  private static final long serialVersionUID = 1000000;

  private List<ValidationErrorDetail> details = new ArrayList<>();

  private String name;

  /**
   * Constructs a <code>ValidationError</code> for an entity with the specified name.
   *
   * @param name the name of the entity associated with this validation error
   */
  public ValidationError(String name)
  {
    this.name = name;
  }

  /**
   * Constructs a <code>ValidationError</code> for an entity with the specified name using the
   * specified details.
   *
   * @param name    the name of the entity associated with this validation error
   * @param details the validation error details
   */
  public ValidationError(String name, List<ValidationErrorDetail> details)
  {
    this.name = name;
    this.details = details;
  }

  /**
   * Constructs a <code>ValidationError</code> for an entity with the specified name using the
   * specified error message.
   *
   * @param name    the name of the entity associated with this validation error
   * @param message the error message
   */
  public ValidationError(String name, String message)
  {
    this.name = name;
    details.add(new ValidationErrorDetail(message));
  }

  /**
   * Constructs a <code>ValidationError</code> for an entity with the specified name using the
   * specified cause.
   *
   * @param name  the name of the entity associated with this validation error
   * @param cause the <code>Exception</code> detailing the cause of the error
   */
  public ValidationError(String name, Throwable cause)
  {
    this.name = name;
    details.add(new ValidationErrorDetail(cause));
  }

  /**
   * Constructs a <code>ValidationError</code> for an entity with the specified name using the
   * specified error code and error message.
   *
   * @param name    the name of the entity associated with this validation error
   * @param code    the error code
   * @param message the error message
   */
  public ValidationError(String name, String code, String message)
  {
    this.name = name;
    details.add(new ValidationErrorDetail(code, message));
  }

  /**
   * Constructs a <code>ValidationError</code> for an entity with the specified name using the
   * specified error message and cause.
   *
   * @param name    the name of the entity associated with this validation error
   * @param message the error message
   * @param cause   the <code>Exception</code> detailing the cause of the error
   */
  public ValidationError(String name, String message, Throwable cause)
  {
    this.name = name;
    details.add(new ValidationErrorDetail(message, cause));
  }

  /**
   * Constructs a <code>ValidationError</code> for an entity with the specified name using the
   * specified error code and error message
   * and cause.
   *
   * @param name    the name of the entity associated with this validation error
   * @param code    the error code
   * @param message the error message
   * @param cause   the <code>Exception</code> detailing the cause of the error
   */
  public ValidationError(String name, String code, String message, Throwable cause)
  {
    this.name = name;
    details.add(new ValidationErrorDetail(code, message, cause));
  }

  /**
   * Add the error message as validation error detail information to this validation error.
   *
   * @param message the error message
   */
  public void addDetail(String message)
  {
    details.add(new ValidationErrorDetail(message));
  }

  /**
   * Add the validation error detail information to this validation error.
   *
   * @param detail the validation error detail information
   */
  public void addDetail(ValidationErrorDetail detail)
  {
    details.add(detail);
  }

  /**
   * Add the error code and error message as validation error detail information to this validation
   * error.
   *
   * @param code    the error code
   * @param message the error message
   */
  public void addDetail(String code, String message)
  {
    details.add(new ValidationErrorDetail(code, message));
  }

  /**
   * Add the error message and cause as validation error detail information to this validation
   * error.
   *
   * @param message the error message
   * @param cause   the <code>Exception</code> detailing the cause of the error
   */
  public void addDetail(String message, Throwable cause)
  {
    details.add(new ValidationErrorDetail(message, cause));
  }

  /**
   * Add the error code, error message and cause as validation error detail information to this
   * validation error.
   *
   * @param code    the error code
   * @param message the error message
   * @param cause   the <code>Exception</code> detailing the cause of the error
   */
  public void addDetail(String code, String message, Throwable cause)
  {
    details.add(new ValidationErrorDetail(code, message, cause));
  }

  /**
   * Returns the validation error detail information for this validation error.
   *
   * @return the validation error detail information
   */
  public List<ValidationErrorDetail> getDetails()
  {
    return details;
  }

  /**
   * Returns the name of the entity associated with this validation error.
   *
   * @return the name of the entity associated with this validation error
   */
  public String getName()
  {
    return name;
  }

  /**
   * Set the validation error detail information for this validation error.
   *
   * @param details the validation error detail information
   */
  public void setDetails(List<ValidationErrorDetail> details)
  {
    this.details = details;
  }

  /**
   * Set the name of the entity associated with this validation error. The entity could be a method
   * parameter, UI component, etc.
   *
   * @param name the name of the entity associated with this validation error
   */
  public void setName(String name)
  {
    this.name = name;
  }
}

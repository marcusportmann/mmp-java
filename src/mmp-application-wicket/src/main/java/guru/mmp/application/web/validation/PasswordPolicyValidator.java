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

package guru.mmp.application.web.validation;

//~--- non-JDK imports --------------------------------------------------------

import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;

//~--- JDK imports ------------------------------------------------------------

import java.util.regex.Pattern;

/**
 * The <code>PasswordPolicyValidator</code> class implements a Wicket validator that provides
 * password policy validation.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("deprecation")
public class PasswordPolicyValidator extends Behavior
  implements IValidator<String>
{
  private static final long serialVersionUID = 1000000;
  private static final Pattern UPPER = Pattern.compile("[A-Z]");
  private static final Pattern NUMBER = Pattern.compile("[0-9]");
  private static final Pattern LOWER = Pattern.compile("[a-z]");

  /**
   * Validates the IValidatable instance.
   *
   * Validation errors should be reported using the
   * <code>IValidatable.error(org.apache.wicket.validation.IValidationError)</code> method.
   *
   * @param validatable the <code>IValidatable</code> instance being validated
   */
  @Override
  public void validate(IValidatable<String> validatable)
  {
    String password = validatable.getValue();

    if (!NUMBER.matcher(password).find())
    {
      ValidationError error = new ValidationError().addKey("PasswordPolicyValidator.no-digit");

      validatable.error(error);
    }

    if (!LOWER.matcher(password).find())
    {
      ValidationError error = new ValidationError().addKey("PasswordPolicyValidator.no-lower");

      validatable.error(error);
    }

    if (!UPPER.matcher(password).find())
    {
      ValidationError error = new ValidationError().addKey("PasswordPolicyValidator.no-upper");

      validatable.error(error);
    }
  }
}

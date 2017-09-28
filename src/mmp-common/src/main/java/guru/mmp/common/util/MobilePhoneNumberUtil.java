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

package guru.mmp.common.util;

/**
 * The <code>MobilePhoneNumberUtil</code> class is a utility class which provides methods for
 * working with mobile phone numbers.
 *
 * @author Marcus Portmann
 */
public final class MobilePhoneNumberUtil
{
  /**
   * Main.
   *
   * @param args the command line arguments
   */
  public static void main(String[] args)
  {
    System.out.println("+27 (0) 83 276 3107 = " + mobilePhoneNumberToMSISDN("+27 (0) 83 276 3107",
        "27"));

    System.out.println("0832763107          = " + mobilePhoneNumberToMSISDN("0832763107", "27"));

    System.out.println("083 276 3107        = " + mobilePhoneNumberToMSISDN("083 276 3107", "27"));

    System.out.println("083-276-3107        = " + mobilePhoneNumberToMSISDN("083-276-3107", "27"));

    System.out.println("+27832763107        = " + mobilePhoneNumberToMSISDN("+27832763107", "27"));

    System.out.println("+27 83 276 3107     = " + mobilePhoneNumberToMSISDN("+27 83 276 3107",
        "27"));

    System.out.println("27832763107         = " + mobilePhoneNumberToMSISDN("27832763107", "27"));

    System.out.println("+36 55 002 709      = " + mobilePhoneNumberToMSISDN("+36 55 002 709",
        "27"));

    System.out.println("+353 20 910 6402    = " + mobilePhoneNumberToMSISDN("+353 20 910 6402",
        "27"));

    System.out.println("+1-202-555-0197     = " + mobilePhoneNumberToMSISDN("+1-202-555-0197",
        "27"));

    System.out.println("+1-202-555-0197     = " + mobilePhoneNumberToMSISDN("+1-202-555-0197",
        "27"));
  }

  /**
   * Returns the MSISDN for the specified mobile phone number.
   *
   * @param mobilePhoneNumber the mobile phone number
   *
   * @return the MSISDN for the mobile phone number
   */
  public static String mobilePhoneNumberToMSISDN(String mobilePhoneNumber,
      String defaultInternationalPrefix)
  {
    // Remove any plus signs, white space (tabs and spaces) and dashes
    mobilePhoneNumber = mobilePhoneNumber.replaceAll("(\\+|\\t| |-)", "");

    // Remove area code prefix e.g. the '(0)' in  +27(0)832763107
    mobilePhoneNumber = mobilePhoneNumber.replaceFirst("(\\([0-9]*\\))", "");

    // Ensure that the default international prefix is applied
    mobilePhoneNumber = mobilePhoneNumber.replaceFirst("^(?:" + defaultInternationalPrefix + "|\\+"
        + defaultInternationalPrefix + "|0)", defaultInternationalPrefix);

    return mobilePhoneNumber;
  }
}

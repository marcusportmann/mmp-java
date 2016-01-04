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

package guru.mmp.common.wbxml;

/**
 * The <code>WBXML</code> class contains the WBXML constants.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public class WBXML
{
  /** The ID for the ISO 8895-1 character set. */

  // public static final int CHARSET_ISO_8859_1 = 0x04;

  /** The ID for the UTF-16 character set. */
  public static final int CHARSET_UTF_16 = 0x03F7;

  /** The ID for the UTF-8 character set. */
  public static final int CHARSET_UTF_8 = 106;

  /** The value signifying an end token. */
  public static final int TOKEN_END = 0x01;

  /** The value signifying an entity token */
  public static final int TOKEN_ENTITY = 0x02;

  /** Field description */
  public static final int TOKEN_EXT_0 = 0xC0;

  /** Field description */
  public static final int TOKEN_EXT_1 = 0xC1;

  /** Field description */
  public static final int TOKEN_EXT_2 = 0xC2;

  /** Field description */
  public static final int TOKEN_EXT_I_0 = 0x40;

  /** Field description */
  public static final int TOKEN_EXT_I_1 = 0x41;

  /** Field description */
  public static final int TOKEN_EXT_I_2 = 0x42;

  /** Field description */
  public static final int TOKEN_EXT_T_0 = 0x80;

  /** Field description */
  public static final int TOKEN_EXT_T_1 = 0x81;

  /** Field description */
  public static final int TOKEN_EXT_T_2 = 0x82;

  /** Field description */
  public static final int TOKEN_LITERAL = 0x04;

  /** Field description */
  public static final int TOKEN_LITERAL_A = 0x84;

  /** Field description */
  public static final int TOKEN_LITERAL_AC = 0xC4;

  /** Field description */
  public static final int TOKEN_LITERAL_C = 0x44;

  /** The value signifying an opaque content token */
  public static final int TOKEN_OPAQUE = 0xC3;

  /** Field description */
  public static final int TOKEN_PI = 0x43;

  /** Field description */
  public static final int TOKEN_STR_I = 0x03;

  /** Field description */
  public static final int TOKEN_STR_T = 0x83;

  /** The value signifying a switch page token. */
  public static final int TOKEN_SWITCH_PAGE = 0x00;

  /** The supported WBXML version. */
  public static final int WBXML_VERSION = 0x01;
}

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

package guru.mmp.application.web.resource.thirdparty.moment;

import guru.mmp.application.Debug;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.request.resource.JavaScriptResourceReference;

/**
 * The <code>MomentJavaScriptResourceReference</code> class implements the JavaScript resource
 * reference for the Moment thirdparty resource bundled with the Web Application Template.
 *
 * @author Marcus Portmann
 */
public class MomentJavaScriptResourceReference
  extends JavaScriptResourceReference
{
  private static final MomentJavaScriptResourceReference INSTANCE = new
    MomentJavaScriptResourceReference();

  private static final JavaScriptHeaderItem JAVA_SCRIPT_HEADER_ITEM = JavaScriptHeaderItem
    .forReference(
    new MomentJavaScriptResourceReference());

  private static final long serialVersionUID = 1000000;

  /**
   * Returns the single instance of the JavaScript resource reference for the Moment
   * thirdparty resource bundled with the Web Application Template.
   *
   * @return the single instance of the JavaScript resource reference for the Moment
   * thirdparty resource bundled with the Web Application Template
   */
  public static MomentJavaScriptResourceReference get()
  {
    return INSTANCE;
  }

  /**
   * Returns the JavaScript header item for the JavaScript resource reference.
   *
   * @return the JavaScript header item for the JavaScript resource reference
   */
  public static JavaScriptHeaderItem getJavaScriptHeaderItem()
  {
    return JAVA_SCRIPT_HEADER_ITEM;
  }

  private MomentJavaScriptResourceReference()
  {
    super(MomentJavaScriptResourceReference.class,
      Debug.inDebugMode() ? "moment.js" : "moment.min.js");
  }
}

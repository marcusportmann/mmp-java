/*
 * Copyright 2014 Marcus Portmann
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

package guru.mmp.common.xml;

//~--- non-JDK imports --------------------------------------------------------

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

/**
 * The <code>DdtJarResolver</code> allows the SAX parser to resolve DTD's stored in a JAR file.
 *
 * @author Marcus Portmann
 */
public class DtdJarResolver
  implements EntityResolver
{
  private String dtdPath;
  private String publicId;
  private String systemId;

  /**
   * Constructs a new <code>DTDJarResolver</code>.
   *
   * @param systemId the system ID for the DTD
   * @param dtdPath  the path on the classpath to the DTD
   */
  public DtdJarResolver(String systemId, String dtdPath)
  {
    this.systemId = systemId;
    this.dtdPath = dtdPath;
  }

  /**
   * Constructs a new <code>DTDJarResolver</code>.
   *
   * @param publicId the public ID for the DTD
   * @param systemId the system ID for the DTD
   * @param dtdPath  the path on the classpath to the DTD
   */
  public DtdJarResolver(String publicId, String systemId, String dtdPath)
  {
    this.publicId = publicId;
    this.systemId = systemId;
    this.dtdPath = dtdPath;
  }

  /**
   * Returns the public ID for the DTD.
   *
   * @return the public ID for the DTD
   */
  public String getPublicId()
  {
    return publicId;
  }

  /**
   * Returns the system ID for the DTD.
   *
   * @return the system ID for the DTD
   */
  public String getSystemId()
  {
    return systemId;
  }

  /**
   * Resolve the XML entity with the specified public ID and system ID. This method returns an
   * InputSource that can be used to read the data for the DTD.
   *
   * @param publicId the public ID for the DTD
   * @param systemId the system ID for the DTD
   *
   * @return an InputSource that can be used to read the data for the DTD
   */
  public InputSource resolveEntity(String publicId, String systemId)
  {
    return new InputSource(
        Thread.currentThread().getContextClassLoader().getResourceAsStream(dtdPath));
  }
}

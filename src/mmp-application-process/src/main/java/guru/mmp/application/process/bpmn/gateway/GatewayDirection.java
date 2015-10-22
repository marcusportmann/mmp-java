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

package guru.mmp.application.process.bpmn.gateway;

/**
 * The <code>GatewayDirection</code> enumeration defines the possible directions for a Business
 * Process Model and Notation (BPMN) gateway.
 * <p>
 * <b>Gateway Direction</b> XML schema:
 * <pre>
 * &lt;xsd:simpleType name="tGatewayDirection"&gt;
 *   &lt;xsd:restriction base="xsd:string"&gt;
 *     &lt;xsd:enumeration value="Unspecified"/&gt;
 *     &lt;xsd:enumeration value="Converging"/&gt;
 *     &lt;xsd:enumeration value="Diverging"/&gt;
 *     &lt;xsd:enumeration value="Mixed"/&gt;
 *   &lt;/xsd:restriction&gt;
 * &lt;/xsd:simpleType&gt;
 * </pre>
 *
 * @author Marcus Portmann
 */
public enum GatewayDirection
{
  UNSPECIFIED("Unspecified", "Unspecified"), CONVERGING("Converging", "Converging"),
  DIVERGING("Diverging", "Diverging"), MIXED("Mixed", "Mixed");

  private String id;
  private String name;

  GatewayDirection(String id, String name)
  {
    this.id = id;
    this.name = name;
  }

  /**
   * Returns the gateway direction given by the specified ID.
   *
   * @param id the ID identifying the gateway direction
   *
   * @return the gateway direction given by the specified ID
   */
  public static GatewayDirection fromId(String id)
  {
    switch (id)
    {
      case "Unspecified":
        return GatewayDirection.UNSPECIFIED;

      case "Converging":
        return GatewayDirection.CONVERGING;

      case "Diverging":
        return GatewayDirection.DIVERGING;

      case "Mixed":
        return GatewayDirection.MIXED;

      default:
        throw new RuntimeException("Invalid ID for gateway direction (" + id + ")");
    }
  }

  /**
   * Returns the ID identifying the gateway direction.
   *
   * @return the ID identifying the gateway direction
   */
  public String getId()
  {
    return id;
  }

  /**
   * Returns the name of the gateway direction.
   *
   * @return the name of the gateway direction
   */
  public String getName()
  {
    return name;
  }

  /**
   * Return the string representation of the <code>GatewayDirection</code>
   * enumeration value.
   *
   * @return the string representation of the <code>GatewayDirection</code>
   *         enumeration value
   */
  public String toString()
  {
    return id;
  }
}

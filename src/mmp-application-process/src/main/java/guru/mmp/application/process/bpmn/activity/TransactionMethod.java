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

package guru.mmp.application.process.bpmn.activity;

/**
 * The <code>TransactionMethod</code> enumeration defines the transaction methods for a
 * BPMN transaction sub-process.
 * <p/>
 * <b>Transaction Sub-Process</b> XML schema:
 * <pre>
 * &lt;xsd:simpleType name="tTransactionMethod"&gt;
 *   &lt;xsd:restriction base="xsd:string"&gt;
 *     &lt;xsd:enumeration value="Compensate"/&gt;
 *     &lt;xsd:enumeration value="Image"/&gt;
 *     &lt;xsd:enumeration value="Store"/&gt;
 *   &lt;/xsd:restriction&gt;
 * &lt;/xsd:simpleType&gt;
 * </pre>
 *
 * @author Marcus Portmann
 */
public enum TransactionMethod
{
  COMPENSATE("Compensate", "Compensate"), IMAGE("Image", "Image"), STORE("Store", "Store");

  private String id;
  private String name;

  TransactionMethod(String id, String name)
  {
    this.id = id;
    this.name = name;
  }

  /**
   * Returns the transaction method given by the specified ID.
   *
   * @param id the ID identifying the transaction method
   *
   * @return the transaction method given by the specified ID
   */
  public static TransactionMethod fromId(String id)
  {
    switch (id)
    {
      case "Compensate":
        return TransactionMethod.COMPENSATE;

      case "Image":
        return TransactionMethod.IMAGE;

      case "Store":
        return TransactionMethod.STORE;

      default:
        throw new RuntimeException("Invalid ID for transaction method (" + id + ")");
    }
  }

  /**
   * Returns the ID identifying the transaction method.
   *
   * @return the ID identifying the transaction method
   */
  public String getId()
  {
    return id;
  }

  /**
   * Returns the name of the transaction method.
   *
   * @return the name of the transaction method
   */
  public String getName()
  {
    return name;
  }

  /**
   * Return the string representation of the <code>TransactionMethod</code>
   * enumeration value.
   *
   * @return the string representation of the <code>TransactionMethod</code>
   * enumeration value
   */
  public String toString()
  {
    return id;
  }
}

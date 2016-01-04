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

package guru.mmp.sharepoint;

//~--- non-JDK imports --------------------------------------------------------

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.Map;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>SharePointListModificationRequest</code> class represents a request to modify a
 * SharePoint list.
 * <p/>
 * It encapsulates the logic to create the XML representations for the "New", "Update" and "Delete"
 * requests used to modify a SharePoint list (CAML query).
 * <p/>
 * For an example of a CAML query see
 * http://msdn.microsoft.com/en-us/library/lists.lists.updatelistitems.aspx.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public class SharePointListModificationRequest
{
  /**
   * The XML document that describes the list modification request.
   */
  private Document document;

  /**
   * The "Method" element in the XML document that describes the list modification request.
   */
  private Element methodElement;

  /**
   * Constructs a new <code>SharePointListModificationRequest</code>.
   *
   * @param requestType the request type
   */
  public SharePointListModificationRequest(RequestType requestType)
  {
    try
    {
      Element batchElement;
      DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

      document = documentBuilder.newDocument();

      // Create the "Batch" element
      batchElement = document.createElement("Batch");

      // batchElement.setAttribute("ListVersion", "1");
      batchElement.setAttribute("OnError", "Continue");

      // Create the "Method" element
      methodElement = document.createElement("Method");
      methodElement.setAttribute("Cmd", requestType.toString());
      methodElement.setAttribute("ID", "1");

      batchElement.appendChild(methodElement);

      document.appendChild(batchElement);
    }
    catch (Throwable e)
    {
      throw new RuntimeException("Failed to initialise the SharePointListModificationRequest", e);
    }
  }

  /**
   * The enumeration giving the possible list modification request types.
   */
  public enum RequestType
  {
    NEW("New"), UPDATE("Update"), DELETE("Delete");

    private String name;

    RequestType(String name)
    {
      this.name = name;
    }

    /**
     * Returns the name of the request type.
     *
     * @return the name of the request type
     */
    public String getName()
    {
      return name;
    }

    /**
     * Return the string representation of the <code>RequestType</code> enumeration value.
     *
     * @return the string representation of the <code>RequestType</code> enumeration value
     */
    public String toString()
    {
      return name;
    }
  }

  /**
   * Creates a <code>SharePointListModificationRequest</code> to create a new SharePoint list item.
   * <p/>
   *
   * @param fields The fields for the new list item specified in the form of the name of each
   *               column to be populated in the SharePoint list and the value for that column.
   *
   * @return the <code>SharePointListModificationRequest</code> to create the new SharePoint list
   *         item
   */
  public static SharePointListModificationRequest createNewListItemRequest(Map<String,
      String> fields)
  {
    SharePointListModificationRequest request =
      new SharePointListModificationRequest(RequestType.NEW);

    for (Map.Entry<String, String> field : fields.entrySet())
    {
      Element fieldElement = request.getDocument().createElement("Field");

      fieldElement.setAttribute("Name", field.getKey());

      Text attributeValue = request.getDocument().createTextNode(field.getValue());

      fieldElement.appendChild(attributeValue);

      request.getMethodElement().appendChild(fieldElement);
    }

    return request;
  }

  /**
   * Returns the XML document that describes the list modification request.
   *
   * @return the XML document that describes the list modification request
   */
  public Document getDocument()
  {
    return document;
  }

  /**
   * Returns the "Method" element in the XML document that describes the list modification request.
   *
   * @return the "Method" element in the XML document that describes the list modification request.
   */
  public Element getMethodElement()
  {
    return methodElement;
  }
}

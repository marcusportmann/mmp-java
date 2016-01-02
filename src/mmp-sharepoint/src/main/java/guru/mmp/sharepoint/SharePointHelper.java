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

import com.microsoft.schemas.sharepoint.soap.ListsSoap;
import com.microsoft.schemas.sharepoint.soap.UpdateListItems;
import com.microsoft.schemas.sharepoint.soap.UpdateListItems.Updates;
import com.microsoft.schemas.sharepoint.soap.UpdateListItemsResponse.UpdateListItemsResult;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.List;
import java.util.Map;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>SharePointHelper</code> class provides utility methods to assist in working with
 * SharePoint.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public class SharePointHelper
{
  private static final String SHAREPOINT_ROWSET_SCHEMA_NAMESPACE = "#RowsetSchema";
  private static final String SHAREPOINT_SOAP_NAMESPACE =
    "http://schemas.microsoft.com/sharepoint/soap/";

  /**
   * Add an attachment to a SharePoint list item.
   *
   * @param listsService   the SharePoint "Lists" web service proxy
   * @param listName       the name of the SharePoint list or the GUID uniquely identifying the
   *                       SharePoint list
   * @param itemId         the ID uniquely identifing the list item
   * @param attachmentName the file name for the attachment
   * @param attachmentData the data for the attachment
   *
   * @throws SharePointException
   */
  public static void addAttachmentToListItem(ListsSoap listsService, String listName,
      String itemId, String attachmentName, byte[] attachmentData)
    throws SharePointException

  {
    try
    {
      listsService.addAttachment(listName, itemId, attachmentName, attachmentData);
    }
    catch (Throwable e)
    {
      throw new SharePointException("Failed to add the attachment to the SharePoint list item", e);
    }
  }

  /**
   * Create a new SharePoint list item.
   *
   * @param listsService the SharePoint "Lists" web service proxy
   * @param listName     the name of the SharePoint list or the GUID uniquely identifying the
   *                     SharePoint list
   * @param fields       The fields for the new list item specified in the form of the name of each
   *                     column to be populated in the SharePoint list and the value for that
   *                     column.
   *
   * @return the ID of the new list item or <code>null</code> if the ID could not be determined
   *
   * @throws SharePointException
   */
  public static String createListItem(ListsSoap listsService, String listName,
      Map<String, String> fields)
    throws SharePointException
  {
    try
    {
      // Create the SharePoint list modification request to create a new list item
      SharePointListModificationRequest listModificationRequest =
        SharePointListModificationRequest.createNewListItemRequest(fields);

      // Add the SharePoint list modification request to the list of updates
      Updates updates = new UpdateListItems.Updates();

      updates.getContent().add(0, listModificationRequest.getDocument().getDocumentElement());

      // Invoke the SharePoint "Lists.UpdateListItems" web service method
      UpdateListItemsResult result = listsService.updateListItems(listName, updates);

      List<Object> content = result.getContent();

      if (content != null)
      {
        if (content.size() > 0)
        {
          Object contentObject = content.get(0);

          if (contentObject instanceof Element)
          {
            Element resultsElement = (Element) contentObject;

            if (resultsElement.getLocalName().equals("Results"))
            {
              NodeList resultElements =
                resultsElement.getElementsByTagNameNS(SHAREPOINT_SOAP_NAMESPACE, "Result");

              if (resultElements.getLength() > 0)
              {
                Element resultElement = (Element) resultElements.item(0);

                String errorCodeString = getChildElementText(resultElement,
                  SHAREPOINT_SOAP_NAMESPACE, "ErrorCode");

                long errorCode = Long.decode(errorCodeString);

                if (errorCode == 0)
                {
                  NodeList rowElements =
                    resultsElement.getElementsByTagNameNS(SHAREPOINT_ROWSET_SCHEMA_NAMESPACE,
                      "row");

                  if (rowElements.getLength() > 0)
                  {
                    Element rowElement = (Element) rowElements.item(0);

                    return rowElement.getAttribute("ows_ID");
                  }
                }
              }
            }
          }
        }
      }

      // Could not retrieve ID of new list item
      return null;
    }
    catch (Throwable e)
    {
      throw new SharePointException("Failed to create the SharePoint list item", e);
    }
  }

  private static String getChildElementText(Element element, String namespaceURI, String localName)
  {
    NodeList resultElements = element.getElementsByTagNameNS(namespaceURI, localName);

    if (resultElements.getLength() > 0)
    {
      Element resultElement = (Element) resultElements.item(0);

      return resultElement.getTextContent();
    }

    return null;
  }
}

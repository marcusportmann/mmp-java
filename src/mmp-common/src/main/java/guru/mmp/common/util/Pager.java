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

package guru.mmp.common.util;

//~--- JDK imports ------------------------------------------------------------

import java.io.Serializable;
import java.util.List;

/**
 * The Pager class provides a generic pager for a list of items.
 *
 * @author Marcus Portmann
 *
 * @param <T>
 */
@SuppressWarnings("unused")
public class Pager<T>
  implements Serializable
{
  private static final long serialVersionUID = 1000000;
  private int currentPage;
  private int firstPage;
  private List<T> items;
  private int itemsPerPage;
  private int pageFirstItemIndex;
  private int pageLastItemIndex;
  private int pagesPerGroup;
  private int totalItems;
  private int totalPages;

  /**
   * Constructor.
   *
   * @param items         the list of items to page
   * @param itemsPerPage  the number of items per page
   * @param pagesPerGroup the number of pages per page group
   */
  public Pager(List<T> items, int itemsPerPage, int pagesPerGroup)
  {
    this.itemsPerPage = itemsPerPage;
    this.pagesPerGroup = pagesPerGroup;
    pageFirstItemIndex = 1;
    pageLastItemIndex = itemsPerPage;
    totalItems = items.size();

    if (this.itemsPerPage > totalItems)
    {
      pageLastItemIndex = totalItems;
    }

    totalPages = (int) ((double) (totalItems) / (double) (this.itemsPerPage));

    if ((totalItems % this.itemsPerPage) > 0)
    {
      totalPages++;
    }

    currentPage = 1;
    firstPage = 1;
    this.items = items;
  }

  /**
   * Retrieve the current page.
   *
   * @return the current page
   */
  public int getCurrentPage()
  {
    return currentPage;
  }

  /**
   * Retrieve the list of items on the current page.
   *
   * @return the list of items on the current page
   */
  public List<T> getCurrentPageOfItems()
  {
    if (totalItems == 0)
    {
      return items;
    }

    return items.subList(pageFirstItemIndex - 1, pageLastItemIndex - 1);
  }

  /**
   * Retrieve the first page in the current page group.
   *
   * @return the first page in the current page group
   */
  public int getFirstPage()
  {
    return firstPage;
  }

  /**
   * Get the number of items per page.
   *
   * @return the number of items per page
   */
  public int getItemsPerPage()
  {
    return itemsPerPage;
  }

  /**
   * Retrieve the list of items on the page after the current page.
   *
   * @return the list of items on the page after the current page
   */
  public List<T> getNextPageOfItems()
  {
    if (totalItems == 0)
    {
      return items;
    }

    if ((pageFirstItemIndex + itemsPerPage) < totalItems)
    {
      pageFirstItemIndex += itemsPerPage;
      pageLastItemIndex = pageFirstItemIndex + (itemsPerPage - 1);

      if (pageLastItemIndex > totalItems)
      {
        pageLastItemIndex = totalItems;
      }
    }

    return items.subList(pageFirstItemIndex - 1, pageLastItemIndex - 1);
  }

  /**
   * Get the index of the first item for the current page.
   *
   * @return the index of the first item for the current page
   */
  public int getPageFirstItemIndex()
  {
    return pageFirstItemIndex;
  }

  /**
   * Get the index of the last item for the current page.
   *
   * @return the index of the last item for the current page
   */
  public int getPageLastItemIndex()
  {
    return pageLastItemIndex;
  }

  /**
   * Get the number of pages per group.
   *
   * @return the number of pages per group
   */
  public int getPagesPerGroup()
  {
    return pagesPerGroup;
  }

  /**
   * Retrieve the list of items on the page before the current page.
   *
   * @return the list of items on the page before the current page
   */
  public List<T> getPreviousPageOfItems()
  {
    if (totalItems == 0)
    {
      return items;
    }

    if ((pageFirstItemIndex - itemsPerPage) > 0)
    {
      pageFirstItemIndex -= itemsPerPage;
      pageLastItemIndex = pageFirstItemIndex + (itemsPerPage - 1);

      if (pageLastItemIndex > totalItems)
      {
        pageLastItemIndex = totalItems;
      }
    }

    return items.subList(pageFirstItemIndex - 1, pageLastItemIndex - 1);
  }

  /**
   * Get the total number of items.
   *
   * @return the total number of items
   */
  public int getTotalItems()
  {
    return totalItems;
  }

  /**
   * Retrieve the total number of pages.
   *
   * @return the total number of pages
   */
  public int getTotalPages()
  {
    return totalPages;
  }

  /**
   * Is there a group of pages after the current page group.
   *
   * @return true if there is a group of pages after the current page group or false otherwise
   */
  public boolean hasNextPageGroup()
  {
    return (firstPage + pagesPerGroup) < totalPages;
  }

  /**
   * Is there a group of pages before the current page group.
   *
   * @return true if there is a group of pages before the current page group or false otherwise
   */
  public boolean hasPreviousPageGroup()
  {
    return (firstPage - pagesPerGroup) > 0;
  }

  /**
   * Move the pager to the next group of pages.
   */
  public void nextPageGroup()
  {
    firstPage += pagesPerGroup;
    currentPage = firstPage;
    pageFirstItemIndex = ((currentPage - 1) * itemsPerPage) + 1;
    pageLastItemIndex = pageFirstItemIndex + (itemsPerPage - 1);

    if (pageLastItemIndex > totalItems)
    {
      pageLastItemIndex = totalItems;
    }
  }

  /**
   * Move the pager to the previous group of pages.
   */
  public void previousPageGroup()
  {
    firstPage -= pagesPerGroup;
    currentPage = firstPage;
    pageFirstItemIndex = ((currentPage - 1) * itemsPerPage) + 1;
    pageLastItemIndex = pageFirstItemIndex + (itemsPerPage - 1);

    if (pageLastItemIndex > totalItems)
    {
      pageLastItemIndex = totalItems;
    }
  }

  /**
   * Set the current page.
   *
   * @param currentPage the current page
   */
  public void setCurrentPage(int currentPage)
  {
    this.currentPage = currentPage;

    if (this.currentPage > totalPages)
    {
      this.currentPage = totalPages;
    }

    pageFirstItemIndex = ((this.currentPage - 1) * itemsPerPage) + 1;
    pageLastItemIndex = pageFirstItemIndex + (itemsPerPage - 1);

    if (pageLastItemIndex > totalItems)
    {
      pageLastItemIndex = totalItems;
    }
  }
}

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

package guru.mmp.application.web.template.component;

//~--- non-JDK imports --------------------------------------------------------

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.navigation.paging.IPageable;

/**
 * The <code>PagingNavigationLink</code> class provides a Wicket component that provides a link
 * to a page of a <code>PageableListView</code>.
 *
 * @param <T> type of model object
 *
 * @author Marcus Portmann
 */
public class PagingNavigationLink<T> extends Link<T>
{
  private static final long serialVersionUID = 1000000;
  protected IPageable pageable;
  private long pageNumber;

  /**
   * Constructor.
   *
   * @param id         the non-null id of this component
   * @param pageable   the pageable component for this page link
   * @param pageNumber The page number in the <code>PageableListView</code> that this link links to.
   *                   Negative pageNumbers are relative to the end of the list.
   */
  public PagingNavigationLink(String id, IPageable pageable, long pageNumber)
  {
    super(id);
    setAutoEnable(true);
    this.pageNumber = pageNumber;
    this.pageable = pageable;
  }

  /**
   * Returns the page number in the <code>PageableListView</code> that this link links to.
   *
   * @return the page number in the <code>PageableListView</code> that this link links to
   */
  public long getPageNumber()
  {
    return cullPageNumber(pageNumber);
  }

  /**
   * Returns <code>true</code> if this page is the first page of the containing
   * <code>PageableListView</code> or <code>false</code> otherwise.
   *
   * @return <code>true</code> if this page is the first page of the containing
   *         <code>PageableListView</code> or <code>false</code> otherwise
   */
  public boolean isFirst()
  {
    return getPageNumber() == 0;
  }

  /**
   * Returns <code>true</code> if this page is the last page of the containing
   * <code>PageableListView</code> or <code>false</code> otherwise.
   *
   * @return <code>true</code> if this page is the first page of the containing
   *         <code>PageableListView</code> or <code>false</code> otherwise
   */
  public boolean isLast()
  {
    return getPageNumber() == (pageable.getPageCount() - 1);
  }

  /**
   * Returns <code>true</code> if this <code>PageableListView</code> navigation link links to the
   * specified page or <code>false</code> otherwise.
   *
   * @param page the page
   *
   * @return <code>true</code> if this <code>PageableListView</code> navigation link links to the
   *         specified page or <code>false</code> otherwise
   *
   * @see org.apache.wicket.markup.html.link.Link#linksTo(org.apache.wicket.Page)
   */
  @Override
  public boolean linksTo(Page page)
  {
    return getPageNumber() == pageable.getCurrentPage();
  }

  /**
   * @see org.apache.wicket.markup.html.link.Link#onClick()
   */
  @Override
  public void onClick()
  {
    pageable.setCurrentPage(getPageNumber());
  }

  /**
   * Allows the link to cull the page number to the valid range before it is retrieved from the
   * link
   *
   * @param pageNumber the page number to cull
   *
   * @return the culled page number
   */
  protected long cullPageNumber(long pageNumber)
  {
    long idx = pageNumber;

    if (idx < 0)
    {
      idx = pageable.getPageCount() + idx;
    }

    if (idx > (pageable.getPageCount() - 1))
    {
      idx = pageable.getPageCount() - 1;
    }

    if (idx < 0)
    {
      idx = 0;
    }

    return idx;
  }
}

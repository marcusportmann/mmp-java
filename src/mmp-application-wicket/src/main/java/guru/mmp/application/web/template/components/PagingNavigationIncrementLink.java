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

package guru.mmp.application.web.template.components;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.navigation.paging.IPageable;

/**
 * The <code>PagingNavigationIncrementLink</code> class provides a Wicket component that provides
 * an incremental link to a page of a <code>PageableListView</code>.
 *
 * @param <T> type of model object
 *
 * @author Marcus Portmann
 */
public class PagingNavigationIncrementLink<T>
  extends Link<T>
{
  private static final long serialVersionUID = 1000000;

  protected IPageable pageable;

  private int increment;

  /**
   * Constructor.
   *
   * @param id        the non-null id of this component
   * @param pageable  the pageable component the page links are referring to
   * @param increment the increment
   */
  public PagingNavigationIncrementLink(String id, IPageable pageable, int increment)
  {
    super(id);
    setAutoEnable(true);
    this.increment = increment;
    this.pageable = pageable;
  }

  /**
   * Calculate the next page number for the pageable component.
   *
   * @return the next page number for the pageable component
   */
  public long calculateNextPageNumber()
  {
    // Determine the page number based on the current PageableListView page and the increment
    long idx = pageable.getCurrentPage() + increment;

    // make sure the index lies between 0 and the last page
    return Math.max(0, Math.min(pageable.getPageCount() - 1, idx));
  }

  /**
   * Returns <code>true</code> if the link refers to the first page of the underlying
   * <code>PageableListView</code> or <code>false</code> otherwise.
   *
   * @return <code>true</code> if the link refers to the first page of the underlying
   * <code>PageableListView</code> or <code>false</code> otherwise
   */
  public boolean isFirst()
  {
    return pageable.getCurrentPage() <= 0;
  }

  /**
   * Returns <code>true</code> if the link refers to the last page of the underlying
   * <code>PageableListView</code> or <code>false</code> otherwise
   *
   * @return <code>true</code> if the link refers to the first page of the underlying
   * <code>PageableListView</code> or <code>false</code> otherwise.
   */
  public boolean isLast()
  {
    return pageable.getCurrentPage() >= (pageable.getPageCount() - 1);
  }

  /**
   * Returns <code>true</code> if this link links to the given page or <code>false</code> otherwise.
   *
   * @param page ignored
   *
   * @return <code>true</code> if this link links to the given page or <code>false</code> otherwise
   *
   * @see org.apache.wicket.markup.html.link.BookmarkablePageLink#linksTo(org.apache.wicket.Page)
   */
  @Override
  public boolean linksTo(Page page)
  {
    pageable.getCurrentPage();

    return ((increment < 0) && isFirst()) || ((increment > 0) && isLast());
  }

  /**
   * @see org.apache.wicket.markup.html.link.Link#onClick()
   */
  @Override
  public void onClick()
  {
    // Tell the PageableListView which page to print next
    pageable.setCurrentPage(calculateNextPageNumber());

    // Return the current page.
    setResponsePage(getPage());
  }
}

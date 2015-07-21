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

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.list.Loop;
import org.apache.wicket.markup.html.list.LoopItem;
import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.model.Model;

/**
 * The <code>PagingNavigation</code> class provides a Wicket component that provides the
 * navigation for a <code>PageableListView</code> that holds links to other pages of the
 * <code>PageableListView</code>.
 *
 * @author Marcus Portmann
 */
public class PagingNavigation extends Loop
{
  /**
   * The maximum number of page links to show.
   */
  private static final int MAXIMUM_NUMBER_PAGE_LINKS = 5;
  private static final long serialVersionUID = 1000000;

  /**
   * The <code>AttributeModifier</code> used to apply the "active" CSS class to a link.
   */
  public static final AttributeModifier ACTIVE_CSS_CLASS_MODIFIER =
    AttributeModifier.replace("class", "active");
  protected IPageable pageable;

  /**
   * The number of links on the left and/or right to keep the current page link somewhere near the
   * middle.
   */
  private long margin = -1;

  /**
   * The start index.
   */
  private long startIndex;

  /**
   * Constructor.
   *
   * @param id       the non-null id of this component
   * @param pageable the underlying pageable component to navigate
   */
  public PagingNavigation(final String id, final IPageable pageable)
  {
    super(id, null);
    this.pageable = pageable;
    startIndex = 0;
  }

  /**
   * Returns the margin.
   *
   * The default value is half the view size, unless explicitly set.
   *
   * @return the margin
   */
  public long getMargin()
  {
    if ((margin == -1))
    {
      return MAXIMUM_NUMBER_PAGE_LINKS / 2;
    }

    return margin;
  }

  /**
   * Sets the margin.
   *
   * @param margin the margin
   */
  public void setMargin(final int margin)
  {
    this.margin = margin;
  }

  /**
   * Allow subclasses replacing populateItem to calculate the current page number
   *
   * @return start index
   */
  protected final long getStartIndex()
  {
    return startIndex;
  }

  /**
   * Factory method for creating page number links.
   *
   * @param id        the non-null component id
   * @param pageable  the pageable for the link
   * @param pageIndex the page index the link points to
   *
   * @return the page navigation link.
   */
  protected AbstractLink newPagingNavigationLink(String id, IPageable pageable, long pageIndex)
  {
    return new PagingNavigationLink<Void>(id, pageable, pageIndex);
  }

  @Override
  protected void onConfigure()
  {
    super.onConfigure();
    setDefaultModel(new Model<>((int) Math.max(Integer.MAX_VALUE, pageable.getPageCount())));

    // PagingNavigation itself (as well as the PageableListView) may have pages.

    // The index of the first page link depends on the PageableListView's page currently printed.
    calculateStartIndex();
  }

  /**
   * Populate the current cell with a page link (PagingNavigationLink) enclosing the page number
   * the link is pointing to. Subclasses may provide there own implementation adding more
   * sophisticated page links.
   *
   * @see org.apache.wicket.markup.html.list.Loop#populateItem(
   *         org.apache.wicket.markup.html.list.LoopItem)
   */
  @Override
  protected void populateItem(final LoopItem loopItem)
  {
    // Get the index of page this link shall point to
    final long pageIndex = getStartIndex() + loopItem.getIndex();

    // Add a page link pointing to the page
    final AbstractLink link = newPagingNavigationLink("pageLink", pageable, pageIndex);

    if (pageable.getCurrentPage() == pageIndex)
    {
      link.add(ACTIVE_CSS_CLASS_MODIFIER);
    }

    // link.add(new TitleAppender(pageIndex));
    loopItem.add(link);

    // Add a page number label to the list which is enclosed by the link
    link.add(new Label("pageNumber", String.valueOf(pageIndex + 1)));
  }

  /**
   * Determine the first page link to render based on the current <code>PageableListView</code>
   * page.
   */
  private void calculateStartIndex()
  {
    // Which startIndex are we currently using
    long firstListItem = startIndex;

    // How many page links shall be displayed
    int viewSize = (int) Math.min(MAXIMUM_NUMBER_PAGE_LINKS, pageable.getPageCount());
    long margin = getMargin();

    // What is the PageableListView's page index to be displayed
    long currentPage = pageable.getCurrentPage();

    /*
     * Make sure the current page link index is within the current window taking the left and right
     * margin into account.
     */
    if (currentPage < (firstListItem + margin))
    {
      firstListItem = currentPage - margin;
    }
    else if ((currentPage >= (firstListItem + viewSize - margin)))
    {
      firstListItem = (currentPage + margin + 1) - viewSize;
    }

    // Make sure the first index is >= 0 and the last index is <=/ than the last page link index
    if ((firstListItem + viewSize) >= pageable.getPageCount())
    {
      firstListItem = pageable.getPageCount() - viewSize;
    }

    if (firstListItem < 0)
    {
      firstListItem = 0;
    }

    if ((viewSize != getIterations()) || (startIndex != firstListItem))
    {
      modelChanging();

      // Tell the list view what the new start index should be
      addStateChange();
      startIndex = firstListItem;

      setIterations((int) Math.min(viewSize, pageable.getPageCount()));

      modelChanged();

      // Force all children to be re-rendered
      removeAll();
    }
  }

  /**
   * Set the number of iterations.
   *
   * @param iterations the number of iterations
   */
  private void setIterations(int iterations)
  {
    setDefaultModelObject(iterations);
  }
}

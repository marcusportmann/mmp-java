/*
 * Copyright 2017 Marcus Portmann
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

//~--- non-JDK imports --------------------------------------------------------

import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * The <code>PagingNavigator</code> class provides a Wicket component to draw and maintain a
 * complete page navigator.
 *
 * @author Marcus Portmann
 */
public class PagingNavigator extends Panel
{
  private static final long serialVersionUID = 1000000;
  private boolean isVisible;
  private IPageable pageable;

  /**
   * Constructor.
   *
   * @param id       the non-null id of this component
   * @param pageable the pageable component the page links are referring to
   */
  public PagingNavigator(String id, IPageable pageable)
  {
    super(id);
    this.pageable = pageable;
    setRenderBodyOnly(false);
  }

  /**
   * @return <code>true</code> if the component is visible or <code>false</code> otherwise
   *
   * @see org.apache.wicket.Component#isVisible()
   */
  @Override
  public boolean isVisible()
  {
    return isVisible;
  }

  /**
   * Create a new <code>PagingNavigation</code> instance.
   * <p/>
   * May be subclassed to make use of specialized <code>PagingNavigation</code>.
   *
   * @param id       the id of the navigation component
   * @param pageable the pageable component
   *
   * @return the navigation object
   */
  protected PagingNavigation newNavigation(String id, IPageable pageable)
  {
    return new PagingNavigation(id, pageable);
  }

  /**
   * Create a new increment link.
   * <p/>
   * May be sub-classed to make use of specialized links, e.g. Ajaxian links.
   *
   * @param id        the link id
   * @param pageable  the pageable to control
   * @param increment the increment
   *
   * @return the increment link
   */
  protected AbstractLink newPagingNavigationIncrementLink(String id, IPageable pageable,
      int increment)
  {
    return new PagingNavigationIncrementLink<Void>(id, pageable, increment);
  }

  /**
   * Create a new page number link.
   * <p/>
   * May be sub-classed to make use of specialized links, e.g. Ajax-based links.
   *
   * @param id         the link id
   * @param pageable   the pageable to control
   * @param pageNumber the page to jump to
   *
   * @return the page number link
   */
  protected AbstractLink newPagingNavigationLink(String id, IPageable pageable, int pageNumber)
  {
    return new PagingNavigationLink<Void>(id, pageable, pageNumber);
  }

  /**
   * @see org.apache.wicket.Component#onConfigure()
   */
  @Override
  protected void onConfigure()
  {
    super.onConfigure();

    isVisible = pageable.getPageCount() > 1;
  }

  /**
   * @see org.apache.wicket.Component#onInitialize()
   */
  @Override
  protected void onInitialize()
  {
    super.onInitialize();

    // Setup the navigation bar and add it to the hierarchy
    PagingNavigation pagingNavigation = newNavigation("navigation", pageable);

    add(pagingNavigation);

    // Add the additional navigation links
    AbstractLink firstLink = newPagingNavigationLink("first", pageable, 0);

    add(firstLink);

    AbstractLink previousLink = newPagingNavigationIncrementLink("prev", pageable, -1);

    add(previousLink);

    AbstractLink nextLink = newPagingNavigationIncrementLink("next", pageable, 1);

    add(nextLink);

    AbstractLink lastLink = newPagingNavigationLink("last", pageable, -1);

    add(lastLink);
  }
}

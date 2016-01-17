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

//~--- non-JDK imports --------------------------------------------------------

import guru.mmp.common.util.StringUtil;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.Loop;
import org.apache.wicket.markup.html.list.LoopItem;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.util.ListModel;

import java.util.List;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>DropdownMenu</code> class provides a Wicket component that renders a dropdown menu.
 *
 * @param <T>
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public abstract class DropdownMenu<T> extends Panel
{
  private static final long serialVersionUID = 1000000;
  private String dropDownMenuText;
  private String iconClass;
  private IModel<? extends List<? extends T>> menuItems;

  /**
   * Constructs a new <code>DropdownButton</code>.
   *
   * @param id        the non-null id of this component
   * @param model     the model for the component
   * @param menuItems the model providing the list of all rendered menu items
   */
  public DropdownMenu(String id, IModel<T> model, IModel<? extends List<? extends T>> menuItems)
  {
    this(id, model, menuItems, null);
  }

  /**
   * Constructs a new <code>DropdownButton</code>.
   *
   * @param id        the non-null id of this component
   * @param model     the model for the component
   * @param menuItems the menuItems
   */
  public DropdownMenu(String id, IModel<T> model, List<? extends T> menuItems)
  {
    this(id, model, new ListModel<>(menuItems), null);
  }

  /**
   * Constructs a new <code>DropdownButton</code>.
   *
   * @param id        the non-null id of this component
   * @param model     the model for the component
   * @param menuItems the model providing the list of all rendered menuItems
   * @param iconClass the CSS class for the icon for the dropdown
   */
  public DropdownMenu(String id, IModel<T> model, IModel<? extends List<? extends T>> menuItems,
      String iconClass)
  {
    super(id, model);

    this.menuItems = menuItems;
    this.iconClass = iconClass;

    add(new AttributeAppender("class", new Model<>("dropdown-menu-holder")));

    if (model.getObject() != null)
    {
      dropDownMenuText = getDisplayValue(model.getObject());
    }
    else
    {
      dropDownMenuText = "Select an option...";
    }

    Label dropDownMenuIconLabel = new Label("dropdownMenuIcon");
    if (!StringUtil.isNullOrEmpty(iconClass))
    {
      dropDownMenuIconLabel.add(new AttributeModifier("class", new Model<>(iconClass)));
    }
    else
    {
      dropDownMenuIconLabel.setVisible(false);
    }

    add(dropDownMenuIconLabel);

    Label dropDownMenuTextLabel = new Label("dropdownMenuText", new PropertyModel(this,
        "dropDownMenuText"));
    dropDownMenuTextLabel.setRenderBodyOnly(true);

    add(dropDownMenuTextLabel);

    add(new Loop("dropdownMenuItem", menuItems.getObject().size())
        {
          @Override
          protected void populateItem(LoopItem loopItem)
          {
            int loopItemIndex = loopItem.getIndex();

            AjaxFallbackLink dropdownMenuItemLink = new AjaxFallbackLink("dropdownMenuItemLink")
            {
              @Override
              public void onClick(AjaxRequestTarget target)
              {
                T menuItem = menuItems.getObject().get(loopItemIndex);

                DropdownMenu.this.setDefaultModelObject(menuItem);

                dropDownMenuText = getDisplayValue(menuItem);

                onMenuItemSelected(target, menuItem);

                if (target != null)
                {
                  target.add(DropdownMenu.this);
                }
              }
            };

            dropdownMenuItemLink.add(new Label("dropdownMenuItemText", new Model<>(getDisplayValue(
                menuItems.getObject().get(loopItemIndex)))));

            loopItem.add(dropdownMenuItemLink);
          }
        });

    setOutputMarkupId(true);
  }

  /**
   * Constructs a new <code>DropdownButton</code>.
   *
   * @param id        the non-null id of this component
   * @param model     the model for the component
   * @param menuItems the menu items
   * @param iconClass the CSS class for the icon for the dropdown
   */
  public DropdownMenu(String id, IModel<T> model, List<? extends T> menuItems, String iconClass)
  {
    this(id, model, new ListModel<>(menuItems), iconClass);
  }

  /**
   * Returns the display value for the specified menu item.
   *
   * @param menuItemModel the model for the menu item
   */
  protected String getDisplayValue(IModel<T> menuItemModel)
  {
    T menuItem = menuItemModel.getObject();

    if (menuItem != null)
    {
      return getDisplayValue(menuItem);
    }
    else
    {
      return "";
    }
  }

  /**
   * Returns the display value for the specified menu item.
   *
   * @param menuItem the menu item
   */
  protected abstract String getDisplayValue(T menuItem);

  /**
   * Called when a menu item is selected.
   *
   * @param target   the AJAX request target
   * @param menuItem the menu item
   */
  protected abstract void onMenuItemSelected(AjaxRequestTarget target, T menuItem);
}

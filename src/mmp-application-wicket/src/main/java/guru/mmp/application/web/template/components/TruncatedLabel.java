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

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import java.io.Serializable;

/**
 * The <code>TruncatedLabel</code> class extends the Wicket <code>Label</code> component to provide
 * support for truncating the label text.
 */
public class TruncatedLabel extends Label
{
  private static final long serialVersionUID = 1000000;

  /**
   * Constructs a new <code>TruncatedLabel</code>.
   *
   * @param id the non-null id of this component
   */
  public TruncatedLabel(String id)
  {
    super(id);
  }

  /**
   * Constructs a new <code>TruncatedLabel</code>.
   *
   * @param id    the non-null id of this component
   * @param model the model for this component
   */
  public TruncatedLabel(String id, IModel<?> model)
  {
    super(id, model);
  }

  /**
   * Constructs a new <code>TruncatedLabel</code>.
   *
   * @param id    the non-null id of this component
   * @param label the label text or object, converted to a string via the
   *              {@link org.apache.wicket.util.convert.IConverter}.
   */
  public TruncatedLabel(String id, Serializable label)
  {
    this(id, Model.of(label));
  }




}

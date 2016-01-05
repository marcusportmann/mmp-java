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

package guru.mmp.application.web.template.data;

import guru.mmp.application.security.ISecurityService;
import guru.mmp.application.security.Organisation;
import guru.mmp.application.security.OrganisationNotFoundException;
import guru.mmp.application.web.WebApplicationException;
import guru.mmp.application.web.data.InjectableLoadableDetachableModel;

import javax.inject.Inject;
import java.util.UUID;

/**
 * The <code>DetachableOrganisationModel</code> class provides a detachable model
 * implementation for the <code>Organisation</code> model class.
 *
 * @author Marcus Portmann
 */
public class DetachableOrganisationModel
  extends InjectableLoadableDetachableModel<Organisation>
{
  private static final long serialVersionUID = 1000000;

  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the organisation.
   */
  private UUID id;

  /* Security Service */
  @Inject
  private ISecurityService securityService;

  /**
   * Constructs a new <code>DetachableOrganisationModel</code>.
   *
   * @param organisation the <code>Organisation</code> instance
   */
  public DetachableOrganisationModel(Organisation organisation)
  {
    this(organisation.getId());

    setObject(organisation);
  }

  /**
   * Constructs a new <code>DetachableOrganisationModel</code>.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the organisation
   */
  public DetachableOrganisationModel(UUID id)
  {
    this.id = id;
  }

  /**
   * Constructs a new <code>DetachableOrganisationModel</code>.
   * <p/>
   * Hidden default constructor to support CDI.
   */
  @SuppressWarnings("unused")
  protected DetachableOrganisationModel() {}

  /**
   * @see org.apache.wicket.model.LoadableDetachableModel#load()
   */
  @Override
  protected Organisation load()
  {
    try
    {
      return securityService.getOrganisation(id);
    }
    catch (OrganisationNotFoundException e)
    {
      return null;
    }
    catch (Throwable e)
    {
      throw new WebApplicationException(String.format("Failed to load the organisation (%s)", id),
        e);
    }
  }

  /**
   * Invoked when the model is detached after use.
   */
  @Override
  protected void onDetach()
  {
    super.onDetach();

    setObject(null);
  }
}

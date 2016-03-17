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

package guru.mmp.common.persistence;

//~--- JDK imports ------------------------------------------------------------

import java.io.Serializable;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The <code>DatedId</code> class is a JPA embeddable class that stores a composite key with
 * an ID and archived date component.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
@Embeddable
public class ArchivedId
  implements Serializable
{
  private static final long serialVersionUID = 1000000;

  /**
   * The archived date component of the archived ID.
   */
  @Column(name = "ARCHIVED", nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date archived;

  /**
   * The Universally Unique Identifier (UUID) component of the archived ID.
   */
  @Column(name = "ID", nullable = false, length = 40)
  private String id;

  /**
   * Constructs a new <code>ArchivedId</code>.
   */
  public ArchivedId()
  {
    this.id = UUID.randomUUID().toString();
    this.archived = new Date();
  }

  /**
   * Constructs a new <code>ArchivedId</code>.
   *
   * @param id       the Universally Unique Identifier (UUID) component of the archived ID
   * @param archived the archived date component of the archived ID
   */
  public ArchivedId(String id, Date archived)
  {
    this.id = id;
    this.archived = archived;
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   *
   * @param obj the reference object with which to compare
   *
   * @return <code>true</code> if this object is the same as the obj argument otherwise
   *         <code>false</code>
   */
  @Override
  public boolean equals(Object obj)
  {
    if (this == obj)
    {
      return true;
    }

    if (obj == null)
    {
      return false;
    }

    if (getClass() != obj.getClass())
    {
      return false;
    }

    ArchivedId other = (ArchivedId) obj;

    if (archived == null)
    {
      if (other.archived != null)
      {
        return false;
      }
    }
    else if (!archived.equals(other.archived))
    {
      return false;
    }

    if (id == null)
    {
      if (other.id != null)
      {
        return false;
      }
    }
    else if (!id.equals(other.id))
    {
      return false;
    }

    return true;
  }

  /**
   * Returns the archived date component of the archived ID.
   *
   * @return the archived date component of the archived ID
   */
  public Date getArchived()
  {
    return archived;
  }

  /**
   * Returns the Universally Unique Identifier (UUID) component of the archived ID.
   *
   * @return the Universally Unique Identifier (UUID) component of the archived ID
   */
  public String getId()
  {
    return id;
  }

  /**
   * Returns a hash code value for the object.
   *
   * @return a hash code value for the object
   */
  @Override
  public int hashCode()
  {
    return ((id == null)
        ? 0
        : id.hashCode()) + ((archived == null)
        ? 0
        : archived.hashCode());
  }

  /**
   * Set the archived date component of the archived ID.
   *
   * @param archived the archived date component of the archived ID
   */
  public void setArchived(Date archived)
  {
    this.archived = archived;
  }

  /**
   * Set the Universally Unique Identifier (UUID) component of the archived ID.
   *
   * @param id the Universally Unique Identifier (UUID) component of the archived ID
   */
  public void setId(String id)
  {
    this.id = id;
  }
}

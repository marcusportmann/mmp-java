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

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;

/**
 * The <code>VersionedId</code> class is a JPA embeddable class that stores a composite key with
 * an ID and version component.
 *
 * @author Marcus Portmann
 */
@Embeddable
public class VersionedId
  implements Serializable
{
  private static final long serialVersionUID = 1000000;

  /**
   * The Universally Unique Identifier (UUID) component of the versioned ID.
   */
  @Column(name = "ID", nullable = false, length = 40)
  private String id;

  /**
   * The version component of the versioned ID.
   */
  @Column(name = "VERSION", nullable = false)
  private int version;

  /**
   * Constructs a new <code>VersionedId</code>.
   */
  public VersionedId()
  {
    this.id = UUID.randomUUID().toString();
    this.version = 1;
  }

  /**
   * Constructs a new <code>VersionedId</code>.
   *
   * @param id      the Universally Unique Identifier (UUID) component of the versioned ID
   * @param version the version component of the versioned ID
   */
  public VersionedId(String id, int version)
  {
    this.id = id;
    this.version = version;
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

    VersionedId other = (VersionedId) obj;

    if (version != other.version)
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
   * Returns the Universally Unique Identifier (UUID) component of the versioned ID.
   *
   * @return the Universally Unique Identifier (UUID) component of the versioned ID
   */
  public String getId()
  {
    return id;
  }

  /**
   * Returns the version component of the versioned ID.
   *
   * @return the version component of the versioned ID
   */
  public int getVersion()
  {
    return version;
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
        : id.hashCode()) + version;
  }

  /**
   * Set the Universally Unique Identifier (UUID) component of the versioned ID.
   *
   * @param id the Universally Unique Identifier (UUID) component of the versioned ID
   */
  public void setId(String id)
  {
    this.id = id;
  }

  /**
   * Set the version component of the versioned ID.
   *
   * @param version the version component of the versioned ID
   */
  public void setVersion(int version)
  {
    this.version = version;
  }
}

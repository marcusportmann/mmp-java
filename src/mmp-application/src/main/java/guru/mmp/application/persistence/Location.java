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

package guru.mmp.application.persistence;

//~--- JDK imports ------------------------------------------------------------

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * The <code>Location</code> class is a JPA embeddable class that stores a GPS location.
 *
 * @author Marcus Portmann
 */
@Embeddable
public class Location
  implements Serializable
{
  private static final long serialVersionUID = 1000000;

  /**
   * The latitude for the GPS location.
   */
  @Column(name = "LOCATION_LATITUDE", precision = 12, scale = 8)
  private double latitude;

  /**
   * The longitude for the GPS location.
   */
  @Column(name = "LOCATION_LONGITUDE", precision = 12, scale = 8)
  private double longitude;

  /**
   * The precision for the GPS location.
   */
  @Column(name = "LOCATION_PRECISION", precision = 12, scale = 8)
  private double precision;

  /**
   * Constructs a new <code>Location</code>.
   */
  public Location() {}

  /**
   * Constructs a new <code>Location</code>.
   *
   * @param latitude  the latitude for the GPS location
   * @param longitude the longitude for the GPS location
   * @param precision the precision for the GPS location
   */
  public Location(double latitude, double longitude, double precision)
  {
    this.latitude = latitude;
    this.longitude = longitude;
    this.precision = precision;
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

    Location other = (Location) obj;

    return (latitude == other.latitude) && (longitude == other.longitude)
        && (precision == other.precision);

  }

  /**
   * Returns the latitude for the GPS location.
   *
   * @return the latitude for the GPS location
   */
  public double getLatitude()
  {
    return latitude;
  }

  /**
   * Returns the longitude for the GPS location.
   *
   * @return the longitude for the GPS location
   */
  public double getLongitude()
  {
    return longitude;
  }

  /**
   * Returns the precision for the GPS location.
   *
   * @return the precision for the GPS location
   */
  public double getPrecision()
  {
    return precision;
  }

  /**
   * Returns a hash code value for the object.
   *
   * @return a hash code value for the object
   */
  @Override
  public int hashCode()
  {
    return ((int) (longitude + latitude + precision));
  }

  /**
   * Set the latitude for the GPS location.
   *
   * @param latitude the latitude for the GPS location
   */
  public void setLatitude(double latitude)
  {
    this.latitude = latitude;
  }

  /**
   * Set the longitude for the GPS location.
   *
   * @param longitude the longitude for the GPS location
   */
  public void setLongitude(double longitude)
  {
    this.longitude = longitude;
  }

  /**
   * Set the precision for the GPS location.
   *
   * @param precision the precision for the GPS location
   */
  public void setPrecision(double precision)
  {
    this.precision = precision;
  }
}

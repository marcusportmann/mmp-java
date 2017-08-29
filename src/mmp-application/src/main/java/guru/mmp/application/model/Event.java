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

package guru.mmp.application.model;

//~--- non-JDK imports --------------------------------------------------------

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import guru.mmp.common.xml.LocalDateTimeAdapter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDateTime;
import java.util.UUID;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>Event</code> class implements the Event entity, which represents an event.
 *
 * @author Marcus Portmann
 */
@ApiModel(value = "Event")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "id", "type", "version", "reference", "system", "timestamp", "data" })
@XmlType(name = "Event", namespace = "http://application.model.mmp.guru",
  propOrder = { "id", "type", "version", "reference", "system", "timestamp" })
@XmlAccessorType(XmlAccessType.FIELD)
public class Event
{
  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the event.
   */
  @ApiModelProperty(
    value = "The Universally Unique Identifier (UUID) used to uniquely identify the event",
    required = true)
  @JsonProperty(required = true)
  @XmlAttribute(name = "id", required = true)
  @NotNull
  private UUID id;

  /**
   * The OID that identifies the event type under the 1.3.6.1.4.1.50603.2 OID prefix.
   */
  @ApiModelProperty(
    value = "The OID that identifies the event type under the 1.3.6.1.4.1.50603.2 prefix",
    required = true)
  @JsonProperty(required = true)
  @XmlAttribute(name = "type", required = true)
  @NotNull
  @Pattern(message = "invalid event type OID", regexp = "^1\\.3\\.6\\.1\\.4\\.1\\..+$")
  private String type;

  /**
   * The version of the event type.
   */
  @ApiModelProperty(value = "The version of the event type", required = true)
  @JsonProperty(required = true)
  @XmlAttribute(name = "version", required = true)
  @NotNull
  private int version;

  /**
   * The optional reference to the business transaction or business event that  this event is
   * associated with.
   */
  @ApiModelProperty(
    value = "The optional reference to the business transaction or business event that this event is associated with")
  @JsonProperty
  @XmlAttribute(name = "reference")
  private String reference;

  /**
   * The OID that identifies the originating system under the 1.3.6.1.4.1.50603.3 OID prefix.
   */
  @ApiModelProperty(
    value = "The OID that identifies the originating system under the 1.3.6.1.4.1.50603.3 OID prefix",
    required = true)
  @JsonProperty(required = true)
  @XmlAttribute(name = "system", required = true)
  @NotNull
  @Pattern(message = "invalid event type OID", regexp = "^1\\.3\\.6\\.1\\.4\\.1\\..+$")
  private String system;

  /**
   * The date and time the event occurred.
   */
  @ApiModelProperty(value = "The date and time the event occurred", required = true)
  @JsonProperty(required = true)
  @XmlAttribute(name = "timestamp", required = true)
  @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
  @XmlSchemaType(name = "dateTime")
  @NotNull
  private LocalDateTime timestamp;

  /**
   * The XML, JSON or base64 encoded binary event data.
   */
  @ApiModelProperty(value = "The XML, JSON or base64 encoded binary event data", required = true)
  @JsonProperty(required = true)
  @XmlValue
  @NotNull
  private String data;

  /**
   * Constructs a new <code>Event</code>.
   */
  Event() {}

  /**
   * Constructs a new <code>Event</code>.
   *
   * @param id        the Universally Unique Identifier (UUID) used to uniquely identify the event
   * @param type      the OID that identifies the event type under the 1.3.6.1.4.1.50603.2 OID
   *                  prefix
   * @param version   the version of the event type
   * @param reference the optional reference to the business transaction or business event that this
   *                  event is associated with
   * @param system    the OID that identifies the originating system under the 1.3.6.1.4.1.50603.3
   *                  OID prefix
   * @param timestamp the date and time the event occurred
   * @param data      the XML, JSON or base64 encoded binary event data
   */
  public Event(UUID id, String type, int version, String reference, String system,
    LocalDateTime timestamp, String data)
  {
    this.id = id;
    this.type = type;
    this.version = version;
    this.reference = reference;
    this.system = system;
    this.timestamp = timestamp;
    this.data = data;
  }

  /**
   * Returns the XML, JSON or base64 encoded binary event data.
   *
   * @return the XML, JSON or base64 encoded binary event data
   */
  public String getData()
  {
    return data;
  }

  /**
   * Returns the Universally Unique Identifier (UUID) used to uniquely identify the event.
   *
   * @return the Universally Unique Identifier (UUID) used to uniquely identify the event
   */
  public UUID getId()
  {
    return id;
  }

  /**
   * Returns the optional reference to the business transaction or business event that  this event
   * is associated with.
   *
   * @return the optional reference to the business transaction or business event that  this event
   *         is associated with
   */
  public String getReference()
  {
    return reference;
  }

  /**
   * Returns the OID that identifies the originating system under the 1.3.6.1.4.1.50603.3 OID
   * prefix.
   *
   * @return the OID that identifies the originating system under the 1.3.6.1.4.1.50603.3 OID prefix
   */
  public String getSystem()
  {
    return system;
  }

  /**
   * Returns the date and time the event occurred.
   *
   * @return the date and time the event occurred
   */
  public LocalDateTime getTimestamp()
  {
    return timestamp;
  }

  /**
   * Returns the OID that identifies the event type under the 1.3.6.1.4.1.50603.2 OID prefix.
   *
   * @return the OID that identifies the event type under the 1.3.6.1.4.1.50603.2 OID prefix
   */
  public String getType()
  {
    return type;
  }

  /**
   * Returns the version of the event type.
   *
   * @return the version of the event type
   */
  public int getVersion()
  {
    return version;
  }

  /**
   * Set the XML, JSON or base64 encoded binary event data.
   *
   * @param data the XML, JSON or base64 encoded binary event data
   */
  public void setData(String data)
  {
    this.data = data;
  }

  /**
   * Set the Universally Unique Identifier (UUID) used to uniquely identify the event.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the event
   */
  public void setId(UUID id)
  {
    this.id = id;
  }

  /**
   * Set the optional reference to the business transaction or business event that  this event is
   * associated with.
   *
   * @param reference the optional reference to the business transaction or business event that this
   *                  event is associated with
   */
  public void setReference(String reference)
  {
    this.reference = reference;
  }

  /**
   * Set the OID that identifies the originating system under the 1.3.6.1.4.1.50603.3 OID prefix.
   *
   * @param system the OID that identifies the originating system under the 1.3.6.1.4.1.50603.3 OID
   *               prefix
   */
  public void setSystem(String system)
  {
    this.system = system;
  }

  /**
   * Set the date and time the event occurred.
   *
   * @param timestamp the date and time the event occurred
   */
  public void setTimestamp(LocalDateTime timestamp)
  {
    this.timestamp = timestamp;
  }

  /**
   * Set the OID that identifies the event type under the 1.3.6.1.4.1.50603.2 OID prefix.
   *
   * @param type the OID that identifies the event type under the 1.3.6.1.4.1.50603.2 OID prefix
   */
  public void setType(String type)
  {
    this.type = type;
  }

  /**
   * Set the version of the event type.
   *
   * @param version the version of the event type
   */
  public void setVersion(int version)
  {
    this.version = version;
  }
}

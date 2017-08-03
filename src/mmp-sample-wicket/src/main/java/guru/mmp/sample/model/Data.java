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

package guru.mmp.sample.model;

//~--- JDK imports ------------------------------------------------------------

import guru.mmp.common.xml.LocalDateAdapter;
import guru.mmp.common.xml.LocalDateTimeAdapter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * The <code>Data</code> class.
 *
 * @author Marcus Portmann
 */
@ApiModel(value = "Data")
@JsonPropertyOrder({ "id", "name", "stringValue", "integerValue", "dateValue", "timestampValue" })
@XmlType(name = "Data", namespace = "http://sample.mmp.guru",
  propOrder = { "id", "name", "stringValue", "integerValue", "dateValue", "timestampValue" })
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(schema = "SAMPLE", name = "DATA")
public class Data
  implements Serializable
{
  private static final long serialVersionUID = 1000000;

  /**
   * The ID used to uniquely identify the data.
   */
  @ApiModelProperty(value = "The ID used to uniquely identify the data", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Id", required = true)
  @Id
  @NotNull
  @Column(name = "ID", nullable = false)
  private long id;

  /**
   * The name for the data.
   */
  @ApiModelProperty(value = "The name for the data", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Name", required = true)
  @NotNull
  @Column(name = "NAME", nullable = false)
  private String name;

  /**
   * The string string value for the data.
   */
  @ApiModelProperty(value = "The string string value for the data")
  @JsonProperty
  @XmlElement(name = "StringValue")
  @Column(name = "STRING_VALUE")
  private String stringValue;

  /**
   * The integer value for the data.
   */
  @ApiModelProperty(value = "The integer value for the data")
  @JsonProperty
  @XmlElement(name = "IntegerValue")
  @Column(name = "INTEGER_VALUE")
  private Integer integerValue;

  /**
   * The date value for the data.
   */
  @ApiModelProperty(value = "The date value for the data")
  @JsonProperty
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  @XmlElement(name = "DateValue")
  @XmlJavaTypeAdapter(LocalDateAdapter.class)
  @XmlSchemaType(name="date")
  @Column(name = "DATE_VALUE")
  private LocalDate dateValue;

  /**
   * The timestamp value for the data.
   */
  @ApiModelProperty(value = "The timestamp value for the data")
  @JsonProperty
  @XmlElement(name = "TimestampValue")
  @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
  @XmlSchemaType(name="dateTime")
  @Column(name = "TIMESTAMP_VALUE")
  public LocalDateTime timestampValue;

  /**
   * Constructs a new <code>Data</code>.
   * </p>
   * Protected default constructor for JPA.
   */
  protected Data() {}

  /**
   * Constructs a new <code>Data</code>.
   *
   * @param id             the ID used to uniquely identify the data
   * @param name           the name for the data
   * @param integerValue   the integer value
   * @param stringValue    the string value for the data
   * @param dateValue      the date value for the data
   * @param timestampValue the timestamp value for the data
   */
  public Data(long id, String name, Integer integerValue, String stringValue, LocalDate dateValue,
    LocalDateTime timestampValue)
  {
    this.id = id;
    this.name = name;
    this.integerValue = integerValue;
    this.stringValue = stringValue;
    this.dateValue = dateValue;
    this.timestampValue = timestampValue;
  }

  /**
   * Returns the date value for the data.
   *
   * @return the date value for the data
   */
  public LocalDate getDateValue()
  {
    return dateValue;
  }

  /**
   * Returns the ID used to uniquely identify the data.
   *
   * @return the ID used to uniquely identify the data
   */
  public long getId()
  {
    return id;
  }

  /**
   * Returns the integer value for the data.
   *
   * @return the integer value for the data
   */
  public Integer getIntegerValue()
  {
    return integerValue;
  }

  /**
   * Returns the name for the data.
   *
   * @return the name for the data
   */
  public String getName()
  {
    return name;
  }

  /**
   * Returns the string value for the data.
   *
   * @return the string value for the data
   */
  public String getStringValue()
  {
    return stringValue;
  }

  /**
   * Returns the timestamp value for the data.
   *
   * @return the timestamp value for the data
   */
  public LocalDateTime getTimestampValue()
  {
    return timestampValue;
  }

  /**
   * Set the date value for the data.
   *
   * @param dateValue the date value for the data
   */
  public void setDateValue(LocalDate dateValue)
  {
    this.dateValue = dateValue;
  }

  /**
   * Set the ID used to uniquely identify the data.
   *
   * @param id the ID used to uniquely identify the data
   */
  public void setId(long id)
  {
    this.id = id;
  }

  /**
   * Set the integer value for the data.
   *
   * @param integerValue the integer value for the data
   */
  public void setIntegerValue(Integer integerValue)
  {
    this.integerValue = integerValue;
  }

  /**
   * Set the name for the data.
   *
   * @param name the name for the data
   */
  public void setName(String name)
  {
    this.name = name;
  }

  /**
   * Set the string value for the data.
   *
   * @param stringValue the string value for the data
   */
  public void setStringValue(String stringValue)
  {
    this.stringValue = stringValue;
  }

  /**
   * Set the timestamp value for the data.
   *
   * @param timestampValue the timestamp value for the data
   */
  public void setTimestampValue(LocalDateTime timestampValue)
  {
    this.timestampValue = timestampValue;
  }

  /**
   * Returns a string representation of the data.
   *
   * @return a string representation of the data
   */
  @Override
  public String toString()
  {
    return "Data {id=\"" + id + "\", name=\"" + name + "\", integerValue=\"" + integerValue
      + "\", stringValue=\"" + stringValue + "\", dateValue=\"" + dateValue
      + "\", timestampValue=\"" + timestampValue + "\"}";
  }
}

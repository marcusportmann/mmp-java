
package com.mymobileapi.api5;

//~--- JDK imports ------------------------------------------------------------

import javax.xml.bind.annotation.*;

/**
 * <p>Java class for anonymous complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Groups_List_STR_STRResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "groupsListSTRSTRResult" })
@XmlRootElement(name = "Groups_List_STR_STRResponse")
public class GroupsListSTRSTRResponse
{
  @XmlElement(name = "Groups_List_STR_STRResult")
  protected String groupsListSTRSTRResult;

  /**
   * Gets the value of the groupsListSTRSTRResult property.
   *
   * @return
   *     possible object is
   *     {@link String }
   *
   */
  public String getGroupsListSTRSTRResult()
  {
    return groupsListSTRSTRResult;
  }

  /**
   * Sets the value of the groupsListSTRSTRResult property.
   *
   * @param value
   *     allowed object is
   *     {@link String }
   *
   */
  public void setGroupsListSTRSTRResult(String value)
  {
    this.groupsListSTRSTRResult = value;
  }
}

package com.mymobileapi.api5;

import javax.xml.bind.annotation.*;

/**
 * <p>Java class for anonymous complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Groups_List_DS_STRResult" type="{http://www.w3
 *         .org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"groupsListDSSTRResult"})
@XmlRootElement(name = "Groups_List_DS_STRResponse")
public class GroupsListDSSTRResponse
{

  @XmlElement(name = "Groups_List_DS_STRResult")
  protected String groupsListDSSTRResult;

  /**
   * Gets the value of the groupsListDSSTRResult property.
   *
   * @return possible object is
   * {@link String }
   */
  public String getGroupsListDSSTRResult()
  {
    return groupsListDSSTRResult;
  }

  /**
   * Sets the value of the groupsListDSSTRResult property.
   *
   * @param value allowed object is
   *              {@link String }
   */
  public void setGroupsListDSSTRResult(String value)
  {
    this.groupsListDSSTRResult = value;
  }
}

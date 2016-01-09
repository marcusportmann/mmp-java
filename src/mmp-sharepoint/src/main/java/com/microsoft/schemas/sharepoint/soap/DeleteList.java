package com.microsoft.schemas.sharepoint.soap;

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
 *         &lt;element name="listName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"listName"})
@XmlRootElement(name = "DeleteList")
public class DeleteList
{

  @XmlElement(required = true)
  protected String listName;

  /**
   * Gets the value of the listName property.
   *
   * @return possible object is
   * {@link String }
   */
  public String getListName()
  {
    return listName;
  }

  /**
   * Sets the value of the listName property.
   *
   * @param value allowed object is
   *              {@link String }
   */
  public void setListName(String value)
  {
    this.listName = value;
  }
}

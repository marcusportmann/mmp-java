
package com.microsoft.schemas.sharepoint.soap;

//~--- JDK imports ------------------------------------------------------------

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

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
 *         &lt;element name="listName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="displayName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="parentType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fields" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;any/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="contentTypeProperties" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;any/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="addToView" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "",
    propOrder = { "listName", "displayName", "parentType", "fields", "contentTypeProperties",
    "addToView" })
@XmlRootElement(name = "CreateContentType")
public class CreateContentType
{
  protected String addToView;
  protected CreateContentType.ContentTypeProperties contentTypeProperties;
  protected String displayName;
  protected CreateContentType.Fields fields;
  protected String listName;
  protected String parentType;

  /**
   * Gets the value of the addToView property.
   *
   * @return
   *     possible object is
   *     {@link String }
   *
   */
  public String getAddToView()
  {
    return addToView;
  }

  /**
   * Gets the value of the contentTypeProperties property.
   *
   * @return
   *     possible object is
   *     {@link CreateContentType.ContentTypeProperties }
   *
   */
  public CreateContentType.ContentTypeProperties getContentTypeProperties()
  {
    return contentTypeProperties;
  }

  /**
   * Gets the value of the displayName property.
   *
   * @return
   *     possible object is
   *     {@link String }
   *
   */
  public String getDisplayName()
  {
    return displayName;
  }

  /**
   * Gets the value of the fields property.
   *
   * @return
   *     possible object is
   *     {@link CreateContentType.Fields }
   *
   */
  public CreateContentType.Fields getFields()
  {
    return fields;
  }

  /**
   * Gets the value of the listName property.
   *
   * @return
   *     possible object is
   *     {@link String }
   *
   */
  public String getListName()
  {
    return listName;
  }

  /**
   * Gets the value of the parentType property.
   *
   * @return
   *     possible object is
   *     {@link String }
   *
   */
  public String getParentType()
  {
    return parentType;
  }

  /**
   * Sets the value of the addToView property.
   *
   * @param value
   *     allowed object is
   *     {@link String }
   *
   */
  public void setAddToView(String value)
  {
    this.addToView = value;
  }

  /**
   * Sets the value of the contentTypeProperties property.
   *
   * @param value
   *     allowed object is
   *     {@link CreateContentType.ContentTypeProperties }
   *
   */
  public void setContentTypeProperties(CreateContentType.ContentTypeProperties value)
  {
    this.contentTypeProperties = value;
  }

  /**
   * Sets the value of the displayName property.
   *
   * @param value
   *     allowed object is
   *     {@link String }
   *
   */
  public void setDisplayName(String value)
  {
    this.displayName = value;
  }

  /**
   * Sets the value of the fields property.
   *
   * @param value
   *     allowed object is
   *     {@link CreateContentType.Fields }
   *
   */
  public void setFields(CreateContentType.Fields value)
  {
    this.fields = value;
  }

  /**
   * Sets the value of the listName property.
   *
   * @param value
   *     allowed object is
   *     {@link String }
   *
   */
  public void setListName(String value)
  {
    this.listName = value;
  }

  /**
   * Sets the value of the parentType property.
   *
   * @param value
   *     allowed object is
   *     {@link String }
   *
   */
  public void setParentType(String value)
  {
    this.parentType = value;
  }

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
   *         &lt;any/>
   *       &lt;/sequence>
   *     &lt;/restriction>
   *   &lt;/complexContent>
   * &lt;/complexType>
   * </pre>
   *
   *
   */
  @XmlAccessorType(XmlAccessType.FIELD)
  @XmlType(name = "", propOrder = { "content" })
  public static class ContentTypeProperties
  {
    @XmlMixed
    @XmlAnyElement(lax = true)
    protected List<Object> content;

    /**
     * Gets the value of the content property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the content property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getContent().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Object }
     * {@link String }
     *
     *
     *
     * @return
     */
    public List<Object> getContent()
    {
      if (content == null)
      {
        content = new ArrayList<Object>();
      }

      return this.content;
    }
  }


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
   *         &lt;any/>
   *       &lt;/sequence>
   *     &lt;/restriction>
   *   &lt;/complexContent>
   * &lt;/complexType>
   * </pre>
   *
   *
   */
  @XmlAccessorType(XmlAccessType.FIELD)
  @XmlType(name = "", propOrder = { "content" })
  public static class Fields
  {
    @XmlMixed
    @XmlAnyElement(lax = true)
    protected List<Object> content;

    /**
     * Gets the value of the content property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the content property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getContent().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Object }
     * {@link String }
     *
     *
     *
     * @return
     */
    public List<Object> getContent()
    {
      if (content == null)
      {
        content = new ArrayList<Object>();
      }

      return this.content;
    }
  }
}


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
 *         &lt;element name="listProperties" minOccurs="0">
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
 *         &lt;element name="newFields" minOccurs="0">
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
 *         &lt;element name="updateFields" minOccurs="0">
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
 *         &lt;element name="deleteFields" minOccurs="0">
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
 *         &lt;element name="listVersion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    propOrder = { "listName", "listProperties", "newFields", "updateFields", "deleteFields",
    "listVersion" })
@XmlRootElement(name = "UpdateList")
public class UpdateList
{
  protected UpdateList.DeleteFields deleteFields;
  protected String listName;
  protected UpdateList.ListProperties listProperties;
  protected String listVersion;
  protected UpdateList.NewFields newFields;
  protected UpdateList.UpdateFields updateFields;

  /**
   * Gets the value of the deleteFields property.
   *
   * @return
   *     possible object is
   *     {@link UpdateList.DeleteFields }
   *
   */
  public UpdateList.DeleteFields getDeleteFields()
  {
    return deleteFields;
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
   * Gets the value of the listProperties property.
   *
   * @return
   *     possible object is
   *     {@link UpdateList.ListProperties }
   *
   */
  public UpdateList.ListProperties getListProperties()
  {
    return listProperties;
  }

  /**
   * Gets the value of the listVersion property.
   *
   * @return
   *     possible object is
   *     {@link String }
   *
   */
  public String getListVersion()
  {
    return listVersion;
  }

  /**
   * Gets the value of the newFields property.
   *
   * @return
   *     possible object is
   *     {@link UpdateList.NewFields }
   *
   */
  public UpdateList.NewFields getNewFields()
  {
    return newFields;
  }

  /**
   * Gets the value of the updateFields property.
   *
   * @return
   *     possible object is
   *     {@link UpdateList.UpdateFields }
   *
   */
  public UpdateList.UpdateFields getUpdateFields()
  {
    return updateFields;
  }

  /**
   * Sets the value of the deleteFields property.
   *
   * @param value
   *     allowed object is
   *     {@link UpdateList.DeleteFields }
   *
   */
  public void setDeleteFields(UpdateList.DeleteFields value)
  {
    this.deleteFields = value;
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
   * Sets the value of the listProperties property.
   *
   * @param value
   *     allowed object is
   *     {@link UpdateList.ListProperties }
   *
   */
  public void setListProperties(UpdateList.ListProperties value)
  {
    this.listProperties = value;
  }

  /**
   * Sets the value of the listVersion property.
   *
   * @param value
   *     allowed object is
   *     {@link String }
   *
   */
  public void setListVersion(String value)
  {
    this.listVersion = value;
  }

  /**
   * Sets the value of the newFields property.
   *
   * @param value
   *     allowed object is
   *     {@link UpdateList.NewFields }
   *
   */
  public void setNewFields(UpdateList.NewFields value)
  {
    this.newFields = value;
  }

  /**
   * Sets the value of the updateFields property.
   *
   * @param value
   *     allowed object is
   *     {@link UpdateList.UpdateFields }
   *
   */
  public void setUpdateFields(UpdateList.UpdateFields value)
  {
    this.updateFields = value;
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
  public static class DeleteFields
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
  public static class ListProperties
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
  public static class NewFields
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
  public static class UpdateFields
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

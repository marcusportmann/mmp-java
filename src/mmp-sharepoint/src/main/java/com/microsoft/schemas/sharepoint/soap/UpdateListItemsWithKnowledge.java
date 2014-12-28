
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
 *         &lt;element name="listName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="updates">
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
 *         &lt;element name="syncScope" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="knowledge" minOccurs="0">
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
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "listName", "updates", "syncScope", "knowledge" })
@XmlRootElement(name = "UpdateListItemsWithKnowledge")
public class UpdateListItemsWithKnowledge
{
  protected UpdateListItemsWithKnowledge.Knowledge knowledge;
  @XmlElement(required = true)
  protected String listName;
  protected String syncScope;
  @XmlElement(required = true)
  protected UpdateListItemsWithKnowledge.Updates updates;

  /**
   * Gets the value of the knowledge property.
   *
   * @return
   *     possible object is
   *     {@link UpdateListItemsWithKnowledge.Knowledge }
   *
   */
  public UpdateListItemsWithKnowledge.Knowledge getKnowledge()
  {
    return knowledge;
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
   * Gets the value of the syncScope property.
   *
   * @return
   *     possible object is
   *     {@link String }
   *
   */
  public String getSyncScope()
  {
    return syncScope;
  }

  /**
   * Gets the value of the updates property.
   *
   * @return
   *     possible object is
   *     {@link UpdateListItemsWithKnowledge.Updates }
   *
   */
  public UpdateListItemsWithKnowledge.Updates getUpdates()
  {
    return updates;
  }

  /**
   * Sets the value of the knowledge property.
   *
   * @param value
   *     allowed object is
   *     {@link UpdateListItemsWithKnowledge.Knowledge }
   *
   */
  public void setKnowledge(UpdateListItemsWithKnowledge.Knowledge value)
  {
    this.knowledge = value;
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
   * Sets the value of the syncScope property.
   *
   * @param value
   *     allowed object is
   *     {@link String }
   *
   */
  public void setSyncScope(String value)
  {
    this.syncScope = value;
  }

  /**
   * Sets the value of the updates property.
   *
   * @param value
   *     allowed object is
   *     {@link UpdateListItemsWithKnowledge.Updates }
   *
   */
  public void setUpdates(UpdateListItemsWithKnowledge.Updates value)
  {
    this.updates = value;
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
  public static class Knowledge
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
  public static class Updates
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

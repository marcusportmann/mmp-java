
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
 *         &lt;element name="viewName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="query" minOccurs="0">
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
 *         &lt;element name="viewFields" minOccurs="0">
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
 *         &lt;element name="rowLimit" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="queryOptions" minOccurs="0">
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
 *         &lt;element name="changeToken" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="contains" minOccurs="0">
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
@XmlType(name = "",
    propOrder = { "listName", "viewName", "query", "viewFields", "rowLimit", "queryOptions",
    "changeToken", "contains" })
@XmlRootElement(name = "GetListItemChangesSinceToken")
public class GetListItemChangesSinceToken
{
  protected String changeToken;
  protected GetListItemChangesSinceToken.Contains contains;
  protected String listName;
  protected GetListItemChangesSinceToken.Query query;
  protected GetListItemChangesSinceToken.QueryOptions queryOptions;
  protected String rowLimit;
  protected GetListItemChangesSinceToken.ViewFields viewFields;
  protected String viewName;

  /**
   * Gets the value of the changeToken property.
   *
   * @return
   *     possible object is
   *     {@link String }
   *
   */
  public String getChangeToken()
  {
    return changeToken;
  }

  /**
   * Gets the value of the contains property.
   *
   * @return
   *     possible object is
   *     {@link GetListItemChangesSinceToken.Contains }
   *
   */
  public GetListItemChangesSinceToken.Contains getContains()
  {
    return contains;
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
   * Gets the value of the query property.
   *
   * @return
   *     possible object is
   *     {@link GetListItemChangesSinceToken.Query }
   *
   */
  public GetListItemChangesSinceToken.Query getQuery()
  {
    return query;
  }

  /**
   * Gets the value of the queryOptions property.
   *
   * @return
   *     possible object is
   *     {@link GetListItemChangesSinceToken.QueryOptions }
   *
   */
  public GetListItemChangesSinceToken.QueryOptions getQueryOptions()
  {
    return queryOptions;
  }

  /**
   * Gets the value of the rowLimit property.
   *
   * @return
   *     possible object is
   *     {@link String }
   *
   */
  public String getRowLimit()
  {
    return rowLimit;
  }

  /**
   * Gets the value of the viewFields property.
   *
   * @return
   *     possible object is
   *     {@link GetListItemChangesSinceToken.ViewFields }
   *
   */
  public GetListItemChangesSinceToken.ViewFields getViewFields()
  {
    return viewFields;
  }

  /**
   * Gets the value of the viewName property.
   *
   * @return
   *     possible object is
   *     {@link String }
   *
   */
  public String getViewName()
  {
    return viewName;
  }

  /**
   * Sets the value of the changeToken property.
   *
   * @param value
   *     allowed object is
   *     {@link String }
   *
   */
  public void setChangeToken(String value)
  {
    this.changeToken = value;
  }

  /**
   * Sets the value of the contains property.
   *
   * @param value
   *     allowed object is
   *     {@link GetListItemChangesSinceToken.Contains }
   *
   */
  public void setContains(GetListItemChangesSinceToken.Contains value)
  {
    this.contains = value;
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
   * Sets the value of the query property.
   *
   * @param value
   *     allowed object is
   *     {@link GetListItemChangesSinceToken.Query }
   *
   */
  public void setQuery(GetListItemChangesSinceToken.Query value)
  {
    this.query = value;
  }

  /**
   * Sets the value of the queryOptions property.
   *
   * @param value
   *     allowed object is
   *     {@link GetListItemChangesSinceToken.QueryOptions }
   *
   */
  public void setQueryOptions(GetListItemChangesSinceToken.QueryOptions value)
  {
    this.queryOptions = value;
  }

  /**
   * Sets the value of the rowLimit property.
   *
   * @param value
   *     allowed object is
   *     {@link String }
   *
   */
  public void setRowLimit(String value)
  {
    this.rowLimit = value;
  }

  /**
   * Sets the value of the viewFields property.
   *
   * @param value
   *     allowed object is
   *     {@link GetListItemChangesSinceToken.ViewFields }
   *
   */
  public void setViewFields(GetListItemChangesSinceToken.ViewFields value)
  {
    this.viewFields = value;
  }

  /**
   * Sets the value of the viewName property.
   *
   * @param value
   *     allowed object is
   *     {@link String }
   *
   */
  public void setViewName(String value)
  {
    this.viewName = value;
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
  public static class Contains
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
  public static class Query
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
  public static class QueryOptions
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
  public static class ViewFields
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

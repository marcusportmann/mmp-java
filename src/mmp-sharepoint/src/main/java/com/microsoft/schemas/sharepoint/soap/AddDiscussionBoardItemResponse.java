package com.microsoft.schemas.sharepoint.soap;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

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
 *         &lt;element name="AddDiscussionBoardItemResult" minOccurs="0">
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
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"addDiscussionBoardItemResult"})
@XmlRootElement(name = "AddDiscussionBoardItemResponse")
public class AddDiscussionBoardItemResponse
{

  @XmlElement(name = "AddDiscussionBoardItemResult")
  protected AddDiscussionBoardItemResponse.AddDiscussionBoardItemResult
    addDiscussionBoardItemResult;

  /**
   * Gets the value of the addDiscussionBoardItemResult property.
   *
   * @return possible object is
   * {@link AddDiscussionBoardItemResponse.AddDiscussionBoardItemResult }
   */
  public AddDiscussionBoardItemResponse.AddDiscussionBoardItemResult
  getAddDiscussionBoardItemResult()
  {
    return addDiscussionBoardItemResult;
  }

  /**
   * Sets the value of the addDiscussionBoardItemResult property.
   *
   * @param value allowed object is
   *              {@link AddDiscussionBoardItemResponse.AddDiscussionBoardItemResult }
   */
  public void setAddDiscussionBoardItemResult(
    AddDiscussionBoardItemResponse.AddDiscussionBoardItemResult value)
  {
    this.addDiscussionBoardItemResult = value;
  }

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
   *         &lt;any/>
   *       &lt;/sequence>
   *     &lt;/restriction>
   *   &lt;/complexContent>
   * &lt;/complexType>
   * </pre>
   */
  @XmlAccessorType(XmlAccessType.FIELD)
  @XmlType(name = "", propOrder = {"content"})
  public static class AddDiscussionBoardItemResult
  {

    @XmlMixed
    @XmlAnyElement(lax = true)
    protected List<Object> content;

    /**
     * Gets the value of the content property.
     * <p/>
     * <p/>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the content property.
     * <p/>
     * <p/>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getContent().add(newItem);
     * </pre>
     * <p/>
     * <p/>
     * <p/>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * {@link Object }
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

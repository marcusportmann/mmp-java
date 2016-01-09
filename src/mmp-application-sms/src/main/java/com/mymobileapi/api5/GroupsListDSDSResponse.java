package com.mymobileapi.api5;

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
 *         &lt;element name="Groups_List_DS_DSResult" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;any maxOccurs="2"/>
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
@XmlType(name = "", propOrder = {"groupsListDSDSResult"})
@XmlRootElement(name = "Groups_List_DS_DSResponse")
public class GroupsListDSDSResponse
{

  @XmlElement(name = "Groups_List_DS_DSResult")
  protected GroupsListDSDSResponse.GroupsListDSDSResult groupsListDSDSResult;

  /**
   * Gets the value of the groupsListDSDSResult property.
   *
   * @return possible object is
   * {@link GroupsListDSDSResponse.GroupsListDSDSResult }
   */
  public GroupsListDSDSResponse.GroupsListDSDSResult getGroupsListDSDSResult()
  {
    return groupsListDSDSResult;
  }

  /**
   * Sets the value of the groupsListDSDSResult property.
   *
   * @param value allowed object is
   *              {@link GroupsListDSDSResponse.GroupsListDSDSResult }
   */
  public void setGroupsListDSDSResult(GroupsListDSDSResponse.GroupsListDSDSResult value)
  {
    this.groupsListDSDSResult = value;
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
   *         &lt;any maxOccurs="2"/>
   *       &lt;/sequence>
   *     &lt;/restriction>
   *   &lt;/complexContent>
   * &lt;/complexType>
   * </pre>
   */
  @XmlAccessorType(XmlAccessType.FIELD)
  @XmlType(name = "", propOrder = {"any"})
  public static class GroupsListDSDSResult
  {

    @XmlAnyElement(lax = true)
    protected List<Object> any;

    /**
     * Gets the value of the any property.
     * <p/>
     * <p/>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the any property.
     * <p/>
     * <p/>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAny().add(newItem);
     * </pre>
     * <p/>
     * <p/>
     * <p/>
     * Objects of the following type(s) are allowed in the list
     * {@link Object }
     */
    public List<Object> getAny()
    {
      if (any == null)
      {
        any = new ArrayList<Object>();
      }
      return this.any;
    }
  }
}

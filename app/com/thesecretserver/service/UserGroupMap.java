
package com.thesecretserver.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for UserGroupMap complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="UserGroupMap">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Id" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="UserGroupMapType" type="{urn:thesecretserver.com}UserGroupMapType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UserGroupMap", propOrder = {
    "id",
    "userGroupMapType"
})
public class UserGroupMap {

    @XmlElement(name = "Id")
    protected int id;
    @XmlElement(name = "UserGroupMapType", required = true)
    @XmlSchemaType(name = "string")
    protected UserGroupMapType userGroupMapType;

    /**
     * Gets the value of the id property.
     * 
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     */
    public void setId(int value) {
        this.id = value;
    }

    /**
     * Gets the value of the userGroupMapType property.
     * 
     * @return
     *     possible object is
     *     {@link UserGroupMapType }
     *     
     */
    public UserGroupMapType getUserGroupMapType() {
        return userGroupMapType;
    }

    /**
     * Sets the value of the userGroupMapType property.
     * 
     * @param value
     *     allowed object is
     *     {@link UserGroupMapType }
     *     
     */
    public void setUserGroupMapType(UserGroupMapType value) {
        this.userGroupMapType = value;
    }

}

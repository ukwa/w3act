
package com.thesecretserver.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SshCommandMenuGroupMap complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SshCommandMenuGroupMap">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SshCommandMenuId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="UserGroupMap" type="{urn:thesecretserver.com}UserGroupMap" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SshCommandMenuGroupMap", propOrder = {
    "sshCommandMenuId",
    "userGroupMap"
})
public class SshCommandMenuGroupMap {

    @XmlElement(name = "SshCommandMenuId", required = true, type = Integer.class, nillable = true)
    protected Integer sshCommandMenuId;
    @XmlElement(name = "UserGroupMap")
    protected UserGroupMap userGroupMap;

    /**
     * Gets the value of the sshCommandMenuId property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getSshCommandMenuId() {
        return sshCommandMenuId;
    }

    /**
     * Sets the value of the sshCommandMenuId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setSshCommandMenuId(Integer value) {
        this.sshCommandMenuId = value;
    }

    /**
     * Gets the value of the userGroupMap property.
     * 
     * @return
     *     possible object is
     *     {@link UserGroupMap }
     *     
     */
    public UserGroupMap getUserGroupMap() {
        return userGroupMap;
    }

    /**
     * Sets the value of the userGroupMap property.
     * 
     * @param value
     *     allowed object is
     *     {@link UserGroupMap }
     *     
     */
    public void setUserGroupMap(UserGroupMap value) {
        this.userGroupMap = value;
    }

}

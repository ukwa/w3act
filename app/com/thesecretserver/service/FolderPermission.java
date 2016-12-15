
package com.thesecretserver.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for FolderPermission complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FolderPermission">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="UserOrGroup" type="{urn:thesecretserver.com}GroupOrUserRecord" minOccurs="0"/>
 *         &lt;element name="FolderAccessRoleName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="FolderAccessRoleId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="SecretAccessRoleName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SecretAccessRoleId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FolderPermission", propOrder = {
    "userOrGroup",
    "folderAccessRoleName",
    "folderAccessRoleId",
    "secretAccessRoleName",
    "secretAccessRoleId"
})
public class FolderPermission {

    @XmlElement(name = "UserOrGroup")
    protected GroupOrUserRecord userOrGroup;
    @XmlElement(name = "FolderAccessRoleName")
    protected String folderAccessRoleName;
    @XmlElement(name = "FolderAccessRoleId", required = true, type = Integer.class, nillable = true)
    protected Integer folderAccessRoleId;
    @XmlElement(name = "SecretAccessRoleName")
    protected String secretAccessRoleName;
    @XmlElement(name = "SecretAccessRoleId", required = true, type = Integer.class, nillable = true)
    protected Integer secretAccessRoleId;

    /**
     * Gets the value of the userOrGroup property.
     * 
     * @return
     *     possible object is
     *     {@link GroupOrUserRecord }
     *     
     */
    public GroupOrUserRecord getUserOrGroup() {
        return userOrGroup;
    }

    /**
     * Sets the value of the userOrGroup property.
     * 
     * @param value
     *     allowed object is
     *     {@link GroupOrUserRecord }
     *     
     */
    public void setUserOrGroup(GroupOrUserRecord value) {
        this.userOrGroup = value;
    }

    /**
     * Gets the value of the folderAccessRoleName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFolderAccessRoleName() {
        return folderAccessRoleName;
    }

    /**
     * Sets the value of the folderAccessRoleName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFolderAccessRoleName(String value) {
        this.folderAccessRoleName = value;
    }

    /**
     * Gets the value of the folderAccessRoleId property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getFolderAccessRoleId() {
        return folderAccessRoleId;
    }

    /**
     * Sets the value of the folderAccessRoleId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setFolderAccessRoleId(Integer value) {
        this.folderAccessRoleId = value;
    }

    /**
     * Gets the value of the secretAccessRoleName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSecretAccessRoleName() {
        return secretAccessRoleName;
    }

    /**
     * Sets the value of the secretAccessRoleName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSecretAccessRoleName(String value) {
        this.secretAccessRoleName = value;
    }

    /**
     * Gets the value of the secretAccessRoleId property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getSecretAccessRoleId() {
        return secretAccessRoleId;
    }

    /**
     * Sets the value of the secretAccessRoleId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setSecretAccessRoleId(Integer value) {
        this.secretAccessRoleId = value;
    }

}

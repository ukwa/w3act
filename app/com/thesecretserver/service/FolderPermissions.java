
package com.thesecretserver.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for FolderPermissions complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FolderPermissions">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="IsChangeToPermissions" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="InheritPermissionsEnabled" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="Permissions" type="{urn:thesecretserver.com}ArrayOfFolderPermission" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FolderPermissions", propOrder = {
    "isChangeToPermissions",
    "inheritPermissionsEnabled",
    "permissions"
})
public class FolderPermissions {

    @XmlElement(name = "IsChangeToPermissions", required = true, type = Boolean.class, nillable = true)
    protected Boolean isChangeToPermissions;
    @XmlElement(name = "InheritPermissionsEnabled", required = true, type = Boolean.class, nillable = true)
    protected Boolean inheritPermissionsEnabled;
    @XmlElement(name = "Permissions")
    protected ArrayOfFolderPermission permissions;

    /**
     * Gets the value of the isChangeToPermissions property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsChangeToPermissions() {
        return isChangeToPermissions;
    }

    /**
     * Sets the value of the isChangeToPermissions property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsChangeToPermissions(Boolean value) {
        this.isChangeToPermissions = value;
    }

    /**
     * Gets the value of the inheritPermissionsEnabled property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isInheritPermissionsEnabled() {
        return inheritPermissionsEnabled;
    }

    /**
     * Sets the value of the inheritPermissionsEnabled property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setInheritPermissionsEnabled(Boolean value) {
        this.inheritPermissionsEnabled = value;
    }

    /**
     * Gets the value of the permissions property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfFolderPermission }
     *     
     */
    public ArrayOfFolderPermission getPermissions() {
        return permissions;
    }

    /**
     * Sets the value of the permissions property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfFolderPermission }
     *     
     */
    public void setPermissions(ArrayOfFolderPermission value) {
        this.permissions = value;
    }

}

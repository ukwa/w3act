
package com.thesecretserver.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SecretPermissions complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SecretPermissions">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CurrentUserHasView" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="CurrentUserHasEdit" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="CurrentUserHasOwner" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="InheritPermissionsEnabled" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="IsChangeToPermissions" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="Permissions" type="{urn:thesecretserver.com}ArrayOfPermission" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SecretPermissions", propOrder = {
    "currentUserHasView",
    "currentUserHasEdit",
    "currentUserHasOwner",
    "inheritPermissionsEnabled",
    "isChangeToPermissions",
    "permissions"
})
public class SecretPermissions {

    @XmlElement(name = "CurrentUserHasView")
    protected boolean currentUserHasView;
    @XmlElement(name = "CurrentUserHasEdit")
    protected boolean currentUserHasEdit;
    @XmlElement(name = "CurrentUserHasOwner")
    protected boolean currentUserHasOwner;
    @XmlElement(name = "InheritPermissionsEnabled", required = true, type = Boolean.class, nillable = true)
    protected Boolean inheritPermissionsEnabled;
    @XmlElement(name = "IsChangeToPermissions")
    protected boolean isChangeToPermissions;
    @XmlElement(name = "Permissions")
    protected ArrayOfPermission permissions;

    /**
     * Gets the value of the currentUserHasView property.
     * 
     */
    public boolean isCurrentUserHasView() {
        return currentUserHasView;
    }

    /**
     * Sets the value of the currentUserHasView property.
     * 
     */
    public void setCurrentUserHasView(boolean value) {
        this.currentUserHasView = value;
    }

    /**
     * Gets the value of the currentUserHasEdit property.
     * 
     */
    public boolean isCurrentUserHasEdit() {
        return currentUserHasEdit;
    }

    /**
     * Sets the value of the currentUserHasEdit property.
     * 
     */
    public void setCurrentUserHasEdit(boolean value) {
        this.currentUserHasEdit = value;
    }

    /**
     * Gets the value of the currentUserHasOwner property.
     * 
     */
    public boolean isCurrentUserHasOwner() {
        return currentUserHasOwner;
    }

    /**
     * Sets the value of the currentUserHasOwner property.
     * 
     */
    public void setCurrentUserHasOwner(boolean value) {
        this.currentUserHasOwner = value;
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
     * Gets the value of the isChangeToPermissions property.
     * 
     */
    public boolean isIsChangeToPermissions() {
        return isChangeToPermissions;
    }

    /**
     * Sets the value of the isChangeToPermissions property.
     * 
     */
    public void setIsChangeToPermissions(boolean value) {
        this.isChangeToPermissions = value;
    }

    /**
     * Gets the value of the permissions property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfPermission }
     *     
     */
    public ArrayOfPermission getPermissions() {
        return permissions;
    }

    /**
     * Sets the value of the permissions property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfPermission }
     *     
     */
    public void setPermissions(ArrayOfPermission value) {
        this.permissions = value;
    }

}


package com.thesecretserver.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für SecretPermissions complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
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
     * Ruft den Wert der currentUserHasView-Eigenschaft ab.
     * 
     */
    public boolean isCurrentUserHasView() {
        return currentUserHasView;
    }

    /**
     * Legt den Wert der currentUserHasView-Eigenschaft fest.
     * 
     */
    public void setCurrentUserHasView(boolean value) {
        this.currentUserHasView = value;
    }

    /**
     * Ruft den Wert der currentUserHasEdit-Eigenschaft ab.
     * 
     */
    public boolean isCurrentUserHasEdit() {
        return currentUserHasEdit;
    }

    /**
     * Legt den Wert der currentUserHasEdit-Eigenschaft fest.
     * 
     */
    public void setCurrentUserHasEdit(boolean value) {
        this.currentUserHasEdit = value;
    }

    /**
     * Ruft den Wert der currentUserHasOwner-Eigenschaft ab.
     * 
     */
    public boolean isCurrentUserHasOwner() {
        return currentUserHasOwner;
    }

    /**
     * Legt den Wert der currentUserHasOwner-Eigenschaft fest.
     * 
     */
    public void setCurrentUserHasOwner(boolean value) {
        this.currentUserHasOwner = value;
    }

    /**
     * Ruft den Wert der inheritPermissionsEnabled-Eigenschaft ab.
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
     * Legt den Wert der inheritPermissionsEnabled-Eigenschaft fest.
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
     * Ruft den Wert der isChangeToPermissions-Eigenschaft ab.
     * 
     */
    public boolean isIsChangeToPermissions() {
        return isChangeToPermissions;
    }

    /**
     * Legt den Wert der isChangeToPermissions-Eigenschaft fest.
     * 
     */
    public void setIsChangeToPermissions(boolean value) {
        this.isChangeToPermissions = value;
    }

    /**
     * Ruft den Wert der permissions-Eigenschaft ab.
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
     * Legt den Wert der permissions-Eigenschaft fest.
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


package com.thesecretserver.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für SecretSettings complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="SecretSettings">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="AutoChangeEnabled" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="RequiresApprovalForAccess" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="RequiresComment" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="CheckOutEnabled" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="CheckOutChangePasswordEnabled" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="PrivilegedSecretId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="AssociatedSecretIds" type="{urn:thesecretserver.com}ArrayOfInt" minOccurs="0"/>
 *         &lt;element name="Approvers" type="{urn:thesecretserver.com}ArrayOfGroupOrUserRecord" minOccurs="0"/>
 *         &lt;element name="IsChangeToSettings" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SecretSettings", propOrder = {
    "autoChangeEnabled",
    "requiresApprovalForAccess",
    "requiresComment",
    "checkOutEnabled",
    "checkOutChangePasswordEnabled",
    "privilegedSecretId",
    "associatedSecretIds",
    "approvers",
    "isChangeToSettings"
})
public class SecretSettings {

    @XmlElement(name = "AutoChangeEnabled", required = true, type = Boolean.class, nillable = true)
    protected Boolean autoChangeEnabled;
    @XmlElement(name = "RequiresApprovalForAccess", required = true, type = Boolean.class, nillable = true)
    protected Boolean requiresApprovalForAccess;
    @XmlElement(name = "RequiresComment", required = true, type = Boolean.class, nillable = true)
    protected Boolean requiresComment;
    @XmlElement(name = "CheckOutEnabled", required = true, type = Boolean.class, nillable = true)
    protected Boolean checkOutEnabled;
    @XmlElement(name = "CheckOutChangePasswordEnabled", required = true, type = Boolean.class, nillable = true)
    protected Boolean checkOutChangePasswordEnabled;
    @XmlElement(name = "PrivilegedSecretId", required = true, type = Integer.class, nillable = true)
    protected Integer privilegedSecretId;
    @XmlElement(name = "AssociatedSecretIds")
    protected ArrayOfInt associatedSecretIds;
    @XmlElement(name = "Approvers")
    protected ArrayOfGroupOrUserRecord approvers;
    @XmlElement(name = "IsChangeToSettings")
    protected boolean isChangeToSettings;

    /**
     * Ruft den Wert der autoChangeEnabled-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isAutoChangeEnabled() {
        return autoChangeEnabled;
    }

    /**
     * Legt den Wert der autoChangeEnabled-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setAutoChangeEnabled(Boolean value) {
        this.autoChangeEnabled = value;
    }

    /**
     * Ruft den Wert der requiresApprovalForAccess-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isRequiresApprovalForAccess() {
        return requiresApprovalForAccess;
    }

    /**
     * Legt den Wert der requiresApprovalForAccess-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setRequiresApprovalForAccess(Boolean value) {
        this.requiresApprovalForAccess = value;
    }

    /**
     * Ruft den Wert der requiresComment-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isRequiresComment() {
        return requiresComment;
    }

    /**
     * Legt den Wert der requiresComment-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setRequiresComment(Boolean value) {
        this.requiresComment = value;
    }

    /**
     * Ruft den Wert der checkOutEnabled-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isCheckOutEnabled() {
        return checkOutEnabled;
    }

    /**
     * Legt den Wert der checkOutEnabled-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setCheckOutEnabled(Boolean value) {
        this.checkOutEnabled = value;
    }

    /**
     * Ruft den Wert der checkOutChangePasswordEnabled-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isCheckOutChangePasswordEnabled() {
        return checkOutChangePasswordEnabled;
    }

    /**
     * Legt den Wert der checkOutChangePasswordEnabled-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setCheckOutChangePasswordEnabled(Boolean value) {
        this.checkOutChangePasswordEnabled = value;
    }

    /**
     * Ruft den Wert der privilegedSecretId-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getPrivilegedSecretId() {
        return privilegedSecretId;
    }

    /**
     * Legt den Wert der privilegedSecretId-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setPrivilegedSecretId(Integer value) {
        this.privilegedSecretId = value;
    }

    /**
     * Ruft den Wert der associatedSecretIds-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfInt }
     *     
     */
    public ArrayOfInt getAssociatedSecretIds() {
        return associatedSecretIds;
    }

    /**
     * Legt den Wert der associatedSecretIds-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfInt }
     *     
     */
    public void setAssociatedSecretIds(ArrayOfInt value) {
        this.associatedSecretIds = value;
    }

    /**
     * Ruft den Wert der approvers-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfGroupOrUserRecord }
     *     
     */
    public ArrayOfGroupOrUserRecord getApprovers() {
        return approvers;
    }

    /**
     * Legt den Wert der approvers-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfGroupOrUserRecord }
     *     
     */
    public void setApprovers(ArrayOfGroupOrUserRecord value) {
        this.approvers = value;
    }

    /**
     * Ruft den Wert der isChangeToSettings-Eigenschaft ab.
     * 
     */
    public boolean isIsChangeToSettings() {
        return isChangeToSettings;
    }

    /**
     * Legt den Wert der isChangeToSettings-Eigenschaft fest.
     * 
     */
    public void setIsChangeToSettings(boolean value) {
        this.isChangeToSettings = value;
    }

}

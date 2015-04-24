
package com.thesecretserver.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SecretSettings complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
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
     * Gets the value of the autoChangeEnabled property.
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
     * Sets the value of the autoChangeEnabled property.
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
     * Gets the value of the requiresApprovalForAccess property.
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
     * Sets the value of the requiresApprovalForAccess property.
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
     * Gets the value of the requiresComment property.
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
     * Sets the value of the requiresComment property.
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
     * Gets the value of the checkOutEnabled property.
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
     * Sets the value of the checkOutEnabled property.
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
     * Gets the value of the checkOutChangePasswordEnabled property.
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
     * Sets the value of the checkOutChangePasswordEnabled property.
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
     * Gets the value of the privilegedSecretId property.
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
     * Sets the value of the privilegedSecretId property.
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
     * Gets the value of the associatedSecretIds property.
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
     * Sets the value of the associatedSecretIds property.
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
     * Gets the value of the approvers property.
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
     * Sets the value of the approvers property.
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
     * Gets the value of the isChangeToSettings property.
     * 
     */
    public boolean isIsChangeToSettings() {
        return isChangeToSettings;
    }

    /**
     * Sets the value of the isChangeToSettings property.
     * 
     */
    public void setIsChangeToSettings(boolean value) {
        this.isChangeToSettings = value;
    }

}

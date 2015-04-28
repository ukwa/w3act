
package com.thesecretserver.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Secret complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Secret">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Items" type="{urn:thesecretserver.com}ArrayOfSecretItem" minOccurs="0"/>
 *         &lt;element name="Id" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="SecretTypeId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="FolderId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="IsWebLauncher" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="Active" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="CheckOutEnabled" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="CheckOutMinutesRemaining" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="IsCheckedOut" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="CheckOutUserDisplayName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CheckOutUserId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="IsOutOfSync" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="OutOfSyncReason" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Secret", propOrder = {
    "name",
    "items",
    "id",
    "secretTypeId",
    "folderId",
    "isWebLauncher",
    "active",
    "checkOutEnabled",
    "checkOutMinutesRemaining",
    "isCheckedOut",
    "checkOutUserDisplayName",
    "checkOutUserId",
    "isOutOfSync",
    "outOfSyncReason"
})
public class Secret {

    @XmlElement(name = "Name")
    protected String name;
    @XmlElement(name = "Items")
    protected ArrayOfSecretItem items;
    @XmlElement(name = "Id")
    protected int id;
    @XmlElement(name = "SecretTypeId")
    protected int secretTypeId;
    @XmlElement(name = "FolderId")
    protected int folderId;
    @XmlElement(name = "IsWebLauncher")
    protected boolean isWebLauncher;
    @XmlElement(name = "Active")
    protected boolean active;
    @XmlElement(name = "CheckOutEnabled")
    protected boolean checkOutEnabled;
    @XmlElement(name = "CheckOutMinutesRemaining", required = true, type = Integer.class, nillable = true)
    protected Integer checkOutMinutesRemaining;
    @XmlElement(name = "IsCheckedOut", required = true, type = Boolean.class, nillable = true)
    protected Boolean isCheckedOut;
    @XmlElement(name = "CheckOutUserDisplayName")
    protected String checkOutUserDisplayName;
    @XmlElement(name = "CheckOutUserId", required = true, type = Integer.class, nillable = true)
    protected Integer checkOutUserId;
    @XmlElement(name = "IsOutOfSync", required = true, type = Boolean.class, nillable = true)
    protected Boolean isOutOfSync;
    @XmlElement(name = "OutOfSyncReason")
    protected String outOfSyncReason;

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the items property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfSecretItem }
     *     
     */
    public ArrayOfSecretItem getItems() {
        return items;
    }

    /**
     * Sets the value of the items property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfSecretItem }
     *     
     */
    public void setItems(ArrayOfSecretItem value) {
        this.items = value;
    }

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
     * Gets the value of the secretTypeId property.
     * 
     */
    public int getSecretTypeId() {
        return secretTypeId;
    }

    /**
     * Sets the value of the secretTypeId property.
     * 
     */
    public void setSecretTypeId(int value) {
        this.secretTypeId = value;
    }

    /**
     * Gets the value of the folderId property.
     * 
     */
    public int getFolderId() {
        return folderId;
    }

    /**
     * Sets the value of the folderId property.
     * 
     */
    public void setFolderId(int value) {
        this.folderId = value;
    }

    /**
     * Gets the value of the isWebLauncher property.
     * 
     */
    public boolean isIsWebLauncher() {
        return isWebLauncher;
    }

    /**
     * Sets the value of the isWebLauncher property.
     * 
     */
    public void setIsWebLauncher(boolean value) {
        this.isWebLauncher = value;
    }

    /**
     * Gets the value of the active property.
     * 
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Sets the value of the active property.
     * 
     */
    public void setActive(boolean value) {
        this.active = value;
    }

    /**
     * Gets the value of the checkOutEnabled property.
     * 
     */
    public boolean isCheckOutEnabled() {
        return checkOutEnabled;
    }

    /**
     * Sets the value of the checkOutEnabled property.
     * 
     */
    public void setCheckOutEnabled(boolean value) {
        this.checkOutEnabled = value;
    }

    /**
     * Gets the value of the checkOutMinutesRemaining property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getCheckOutMinutesRemaining() {
        return checkOutMinutesRemaining;
    }

    /**
     * Sets the value of the checkOutMinutesRemaining property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setCheckOutMinutesRemaining(Integer value) {
        this.checkOutMinutesRemaining = value;
    }

    /**
     * Gets the value of the isCheckedOut property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsCheckedOut() {
        return isCheckedOut;
    }

    /**
     * Sets the value of the isCheckedOut property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsCheckedOut(Boolean value) {
        this.isCheckedOut = value;
    }

    /**
     * Gets the value of the checkOutUserDisplayName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCheckOutUserDisplayName() {
        return checkOutUserDisplayName;
    }

    /**
     * Sets the value of the checkOutUserDisplayName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCheckOutUserDisplayName(String value) {
        this.checkOutUserDisplayName = value;
    }

    /**
     * Gets the value of the checkOutUserId property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getCheckOutUserId() {
        return checkOutUserId;
    }

    /**
     * Sets the value of the checkOutUserId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setCheckOutUserId(Integer value) {
        this.checkOutUserId = value;
    }

    /**
     * Gets the value of the isOutOfSync property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsOutOfSync() {
        return isOutOfSync;
    }

    /**
     * Sets the value of the isOutOfSync property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsOutOfSync(Boolean value) {
        this.isOutOfSync = value;
    }

    /**
     * Gets the value of the outOfSyncReason property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOutOfSyncReason() {
        return outOfSyncReason;
    }

    /**
     * Sets the value of the outOfSyncReason property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOutOfSyncReason(String value) {
        this.outOfSyncReason = value;
    }

}

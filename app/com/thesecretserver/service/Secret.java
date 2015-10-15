
package com.thesecretserver.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für Secret complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
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
 *         &lt;element name="CheckOutMinutesRemaining" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="IsCheckedOut" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="CheckOutUserDisplayName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CheckOutUserId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="IsOutOfSync" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="IsRestricted" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="OutOfSyncReason" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SecretSettings" type="{urn:thesecretserver.com}SecretSettings" minOccurs="0"/>
 *         &lt;element name="SecretPermissions" type="{urn:thesecretserver.com}SecretPermissions" minOccurs="0"/>
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
    "checkOutMinutesRemaining",
    "isCheckedOut",
    "checkOutUserDisplayName",
    "checkOutUserId",
    "isOutOfSync",
    "isRestricted",
    "outOfSyncReason",
    "secretSettings",
    "secretPermissions"
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
    @XmlElement(name = "IsRestricted", required = true, type = Boolean.class, nillable = true)
    protected Boolean isRestricted;
    @XmlElement(name = "OutOfSyncReason")
    protected String outOfSyncReason;
    @XmlElement(name = "SecretSettings")
    protected SecretSettings secretSettings;
    @XmlElement(name = "SecretPermissions")
    protected SecretPermissions secretPermissions;

    /**
     * Ruft den Wert der name-Eigenschaft ab.
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
     * Legt den Wert der name-Eigenschaft fest.
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
     * Ruft den Wert der items-Eigenschaft ab.
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
     * Legt den Wert der items-Eigenschaft fest.
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
     * Ruft den Wert der id-Eigenschaft ab.
     * 
     */
    public int getId() {
        return id;
    }

    /**
     * Legt den Wert der id-Eigenschaft fest.
     * 
     */
    public void setId(int value) {
        this.id = value;
    }

    /**
     * Ruft den Wert der secretTypeId-Eigenschaft ab.
     * 
     */
    public int getSecretTypeId() {
        return secretTypeId;
    }

    /**
     * Legt den Wert der secretTypeId-Eigenschaft fest.
     * 
     */
    public void setSecretTypeId(int value) {
        this.secretTypeId = value;
    }

    /**
     * Ruft den Wert der folderId-Eigenschaft ab.
     * 
     */
    public int getFolderId() {
        return folderId;
    }

    /**
     * Legt den Wert der folderId-Eigenschaft fest.
     * 
     */
    public void setFolderId(int value) {
        this.folderId = value;
    }

    /**
     * Ruft den Wert der isWebLauncher-Eigenschaft ab.
     * 
     */
    public boolean isIsWebLauncher() {
        return isWebLauncher;
    }

    /**
     * Legt den Wert der isWebLauncher-Eigenschaft fest.
     * 
     */
    public void setIsWebLauncher(boolean value) {
        this.isWebLauncher = value;
    }

    /**
     * Ruft den Wert der active-Eigenschaft ab.
     * 
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Legt den Wert der active-Eigenschaft fest.
     * 
     */
    public void setActive(boolean value) {
        this.active = value;
    }

    /**
     * Ruft den Wert der checkOutMinutesRemaining-Eigenschaft ab.
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
     * Legt den Wert der checkOutMinutesRemaining-Eigenschaft fest.
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
     * Ruft den Wert der isCheckedOut-Eigenschaft ab.
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
     * Legt den Wert der isCheckedOut-Eigenschaft fest.
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
     * Ruft den Wert der checkOutUserDisplayName-Eigenschaft ab.
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
     * Legt den Wert der checkOutUserDisplayName-Eigenschaft fest.
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
     * Ruft den Wert der checkOutUserId-Eigenschaft ab.
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
     * Legt den Wert der checkOutUserId-Eigenschaft fest.
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
     * Ruft den Wert der isOutOfSync-Eigenschaft ab.
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
     * Legt den Wert der isOutOfSync-Eigenschaft fest.
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
     * Ruft den Wert der isRestricted-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsRestricted() {
        return isRestricted;
    }

    /**
     * Legt den Wert der isRestricted-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsRestricted(Boolean value) {
        this.isRestricted = value;
    }

    /**
     * Ruft den Wert der outOfSyncReason-Eigenschaft ab.
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
     * Legt den Wert der outOfSyncReason-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOutOfSyncReason(String value) {
        this.outOfSyncReason = value;
    }

    /**
     * Ruft den Wert der secretSettings-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SecretSettings }
     *     
     */
    public SecretSettings getSecretSettings() {
        return secretSettings;
    }

    /**
     * Legt den Wert der secretSettings-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SecretSettings }
     *     
     */
    public void setSecretSettings(SecretSettings value) {
        this.secretSettings = value;
    }

    /**
     * Ruft den Wert der secretPermissions-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SecretPermissions }
     *     
     */
    public SecretPermissions getSecretPermissions() {
        return secretPermissions;
    }

    /**
     * Legt den Wert der secretPermissions-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SecretPermissions }
     *     
     */
    public void setSecretPermissions(SecretPermissions value) {
        this.secretPermissions = value;
    }

}

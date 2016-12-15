
package com.thesecretserver.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SshCommandMenuAccessPermission complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SshCommandMenuAccessPermission">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="GroupOrUserRecord" type="{urn:thesecretserver.com}GroupOrUserRecord" minOccurs="0"/>
 *         &lt;element name="SecretId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="ConcurrencyId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DisplayName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SshCommandMenuName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="IsUnrestricted" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="SshCommandMenuId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SshCommandMenuAccessPermission", propOrder = {
    "groupOrUserRecord",
    "secretId",
    "concurrencyId",
    "displayName",
    "sshCommandMenuName",
    "isUnrestricted",
    "sshCommandMenuId"
})
public class SshCommandMenuAccessPermission {

    @XmlElement(name = "GroupOrUserRecord")
    protected GroupOrUserRecord groupOrUserRecord;
    @XmlElement(name = "SecretId")
    protected int secretId;
    @XmlElement(name = "ConcurrencyId")
    protected String concurrencyId;
    @XmlElement(name = "DisplayName")
    protected String displayName;
    @XmlElement(name = "SshCommandMenuName")
    protected String sshCommandMenuName;
    @XmlElement(name = "IsUnrestricted")
    protected boolean isUnrestricted;
    @XmlElement(name = "SshCommandMenuId", required = true, type = Integer.class, nillable = true)
    protected Integer sshCommandMenuId;

    /**
     * Gets the value of the groupOrUserRecord property.
     * 
     * @return
     *     possible object is
     *     {@link GroupOrUserRecord }
     *     
     */
    public GroupOrUserRecord getGroupOrUserRecord() {
        return groupOrUserRecord;
    }

    /**
     * Sets the value of the groupOrUserRecord property.
     * 
     * @param value
     *     allowed object is
     *     {@link GroupOrUserRecord }
     *     
     */
    public void setGroupOrUserRecord(GroupOrUserRecord value) {
        this.groupOrUserRecord = value;
    }

    /**
     * Gets the value of the secretId property.
     * 
     */
    public int getSecretId() {
        return secretId;
    }

    /**
     * Sets the value of the secretId property.
     * 
     */
    public void setSecretId(int value) {
        this.secretId = value;
    }

    /**
     * Gets the value of the concurrencyId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConcurrencyId() {
        return concurrencyId;
    }

    /**
     * Sets the value of the concurrencyId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConcurrencyId(String value) {
        this.concurrencyId = value;
    }

    /**
     * Gets the value of the displayName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Sets the value of the displayName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDisplayName(String value) {
        this.displayName = value;
    }

    /**
     * Gets the value of the sshCommandMenuName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSshCommandMenuName() {
        return sshCommandMenuName;
    }

    /**
     * Sets the value of the sshCommandMenuName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSshCommandMenuName(String value) {
        this.sshCommandMenuName = value;
    }

    /**
     * Gets the value of the isUnrestricted property.
     * 
     */
    public boolean isIsUnrestricted() {
        return isUnrestricted;
    }

    /**
     * Sets the value of the isUnrestricted property.
     * 
     */
    public void setIsUnrestricted(boolean value) {
        this.isUnrestricted = value;
    }

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

}

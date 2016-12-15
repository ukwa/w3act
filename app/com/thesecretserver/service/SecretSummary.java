
package com.thesecretserver.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SecretSummary complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SecretSummary">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SecretId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="SecretName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SecretTypeName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SecretTypeId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="FolderId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="IsRestricted" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SecretSummary", propOrder = {
    "secretId",
    "secretName",
    "secretTypeName",
    "secretTypeId",
    "folderId",
    "isRestricted"
})
public class SecretSummary {

    @XmlElement(name = "SecretId")
    protected int secretId;
    @XmlElement(name = "SecretName")
    protected String secretName;
    @XmlElement(name = "SecretTypeName")
    protected String secretTypeName;
    @XmlElement(name = "SecretTypeId")
    protected int secretTypeId;
    @XmlElement(name = "FolderId")
    protected int folderId;
    @XmlElement(name = "IsRestricted")
    protected boolean isRestricted;

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
     * Gets the value of the secretName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSecretName() {
        return secretName;
    }

    /**
     * Sets the value of the secretName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSecretName(String value) {
        this.secretName = value;
    }

    /**
     * Gets the value of the secretTypeName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSecretTypeName() {
        return secretTypeName;
    }

    /**
     * Sets the value of the secretTypeName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSecretTypeName(String value) {
        this.secretTypeName = value;
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
     * Gets the value of the isRestricted property.
     * 
     */
    public boolean isIsRestricted() {
        return isRestricted;
    }

    /**
     * Sets the value of the isRestricted property.
     * 
     */
    public void setIsRestricted(boolean value) {
        this.isRestricted = value;
    }

}

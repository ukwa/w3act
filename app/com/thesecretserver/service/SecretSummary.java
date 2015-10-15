
package com.thesecretserver.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für SecretSummary complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
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
     * Ruft den Wert der secretId-Eigenschaft ab.
     * 
     */
    public int getSecretId() {
        return secretId;
    }

    /**
     * Legt den Wert der secretId-Eigenschaft fest.
     * 
     */
    public void setSecretId(int value) {
        this.secretId = value;
    }

    /**
     * Ruft den Wert der secretName-Eigenschaft ab.
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
     * Legt den Wert der secretName-Eigenschaft fest.
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
     * Ruft den Wert der secretTypeName-Eigenschaft ab.
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
     * Legt den Wert der secretTypeName-Eigenschaft fest.
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
     * Ruft den Wert der isRestricted-Eigenschaft ab.
     * 
     */
    public boolean isIsRestricted() {
        return isRestricted;
    }

    /**
     * Legt den Wert der isRestricted-Eigenschaft fest.
     * 
     */
    public void setIsRestricted(boolean value) {
        this.isRestricted = value;
    }

}


package com.thesecretserver.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für SecretField complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="SecretField">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DisplayName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Id" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="IsPassword" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="IsUrl" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="IsNotes" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="IsFile" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SecretField", propOrder = {
    "displayName",
    "id",
    "isPassword",
    "isUrl",
    "isNotes",
    "isFile"
})
public class SecretField {

    @XmlElement(name = "DisplayName")
    protected String displayName;
    @XmlElement(name = "Id")
    protected int id;
    @XmlElement(name = "IsPassword")
    protected boolean isPassword;
    @XmlElement(name = "IsUrl")
    protected boolean isUrl;
    @XmlElement(name = "IsNotes")
    protected boolean isNotes;
    @XmlElement(name = "IsFile")
    protected boolean isFile;

    /**
     * Ruft den Wert der displayName-Eigenschaft ab.
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
     * Legt den Wert der displayName-Eigenschaft fest.
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
     * Ruft den Wert der isPassword-Eigenschaft ab.
     * 
     */
    public boolean isIsPassword() {
        return isPassword;
    }

    /**
     * Legt den Wert der isPassword-Eigenschaft fest.
     * 
     */
    public void setIsPassword(boolean value) {
        this.isPassword = value;
    }

    /**
     * Ruft den Wert der isUrl-Eigenschaft ab.
     * 
     */
    public boolean isIsUrl() {
        return isUrl;
    }

    /**
     * Legt den Wert der isUrl-Eigenschaft fest.
     * 
     */
    public void setIsUrl(boolean value) {
        this.isUrl = value;
    }

    /**
     * Ruft den Wert der isNotes-Eigenschaft ab.
     * 
     */
    public boolean isIsNotes() {
        return isNotes;
    }

    /**
     * Legt den Wert der isNotes-Eigenschaft fest.
     * 
     */
    public void setIsNotes(boolean value) {
        this.isNotes = value;
    }

    /**
     * Ruft den Wert der isFile-Eigenschaft ab.
     * 
     */
    public boolean isIsFile() {
        return isFile;
    }

    /**
     * Legt den Wert der isFile-Eigenschaft fest.
     * 
     */
    public void setIsFile(boolean value) {
        this.isFile = value;
    }

}

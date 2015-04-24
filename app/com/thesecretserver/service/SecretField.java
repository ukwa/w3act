
package com.thesecretserver.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SecretField complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
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
     * Gets the value of the isPassword property.
     * 
     */
    public boolean isIsPassword() {
        return isPassword;
    }

    /**
     * Sets the value of the isPassword property.
     * 
     */
    public void setIsPassword(boolean value) {
        this.isPassword = value;
    }

    /**
     * Gets the value of the isUrl property.
     * 
     */
    public boolean isIsUrl() {
        return isUrl;
    }

    /**
     * Sets the value of the isUrl property.
     * 
     */
    public void setIsUrl(boolean value) {
        this.isUrl = value;
    }

    /**
     * Gets the value of the isNotes property.
     * 
     */
    public boolean isIsNotes() {
        return isNotes;
    }

    /**
     * Sets the value of the isNotes property.
     * 
     */
    public void setIsNotes(boolean value) {
        this.isNotes = value;
    }

    /**
     * Gets the value of the isFile property.
     * 
     */
    public boolean isIsFile() {
        return isFile;
    }

    /**
     * Sets the value of the isFile property.
     * 
     */
    public void setIsFile(boolean value) {
        this.isFile = value;
    }

}

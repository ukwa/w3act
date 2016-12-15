
package com.thesecretserver.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SecretItem complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SecretItem">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Value" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Id" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="FieldId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="FieldName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="IsFile" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="IsNotes" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="IsPassword" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="FieldDisplayName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SecretItem", propOrder = {
    "value",
    "id",
    "fieldId",
    "fieldName",
    "isFile",
    "isNotes",
    "isPassword",
    "fieldDisplayName"
})
public class SecretItem {

    @XmlElement(name = "Value")
    protected String value;
    @XmlElement(name = "Id", required = true, type = Integer.class, nillable = true)
    protected Integer id;
    @XmlElement(name = "FieldId", required = true, type = Integer.class, nillable = true)
    protected Integer fieldId;
    @XmlElement(name = "FieldName")
    protected String fieldName;
    @XmlElement(name = "IsFile")
    protected boolean isFile;
    @XmlElement(name = "IsNotes")
    protected boolean isNotes;
    @XmlElement(name = "IsPassword")
    protected boolean isPassword;
    @XmlElement(name = "FieldDisplayName")
    protected String fieldDisplayName;

    /**
     * Gets the value of the value property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setId(Integer value) {
        this.id = value;
    }

    /**
     * Gets the value of the fieldId property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getFieldId() {
        return fieldId;
    }

    /**
     * Sets the value of the fieldId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setFieldId(Integer value) {
        this.fieldId = value;
    }

    /**
     * Gets the value of the fieldName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * Sets the value of the fieldName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFieldName(String value) {
        this.fieldName = value;
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
     * Gets the value of the fieldDisplayName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFieldDisplayName() {
        return fieldDisplayName;
    }

    /**
     * Sets the value of the fieldDisplayName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFieldDisplayName(String value) {
        this.fieldDisplayName = value;
    }

}

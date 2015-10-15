
package com.thesecretserver.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für SecretItem complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
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
     * Ruft den Wert der value-Eigenschaft ab.
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
     * Legt den Wert der value-Eigenschaft fest.
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
     * Ruft den Wert der id-Eigenschaft ab.
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
     * Legt den Wert der id-Eigenschaft fest.
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
     * Ruft den Wert der fieldId-Eigenschaft ab.
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
     * Legt den Wert der fieldId-Eigenschaft fest.
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
     * Ruft den Wert der fieldName-Eigenschaft ab.
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
     * Legt den Wert der fieldName-Eigenschaft fest.
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
     * Ruft den Wert der fieldDisplayName-Eigenschaft ab.
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
     * Legt den Wert der fieldDisplayName-Eigenschaft fest.
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


package com.thesecretserver.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für anonymous complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="token" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fieldName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="searchTerm" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="showDeleted" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="showPartialMatches" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "token",
    "fieldName",
    "searchTerm",
    "showDeleted",
    "showPartialMatches"
})
@XmlRootElement(name = "GetSecretsByExposedFieldValue")
public class GetSecretsByExposedFieldValue {

    protected String token;
    protected String fieldName;
    protected String searchTerm;
    protected boolean showDeleted;
    protected boolean showPartialMatches;

    /**
     * Ruft den Wert der token-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getToken() {
        return token;
    }

    /**
     * Legt den Wert der token-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setToken(String value) {
        this.token = value;
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
     * Ruft den Wert der searchTerm-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSearchTerm() {
        return searchTerm;
    }

    /**
     * Legt den Wert der searchTerm-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSearchTerm(String value) {
        this.searchTerm = value;
    }

    /**
     * Ruft den Wert der showDeleted-Eigenschaft ab.
     * 
     */
    public boolean isShowDeleted() {
        return showDeleted;
    }

    /**
     * Legt den Wert der showDeleted-Eigenschaft fest.
     * 
     */
    public void setShowDeleted(boolean value) {
        this.showDeleted = value;
    }

    /**
     * Ruft den Wert der showPartialMatches-Eigenschaft ab.
     * 
     */
    public boolean isShowPartialMatches() {
        return showPartialMatches;
    }

    /**
     * Legt den Wert der showPartialMatches-Eigenschaft fest.
     * 
     */
    public void setShowPartialMatches(boolean value) {
        this.showPartialMatches = value;
    }

}

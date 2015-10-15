
package com.thesecretserver.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
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
 *         &lt;element name="searchTerm" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="folderId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="includeSubFolders" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="includeDeleted" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="includeRestricted" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
    "searchTerm",
    "folderId",
    "includeSubFolders",
    "includeDeleted",
    "includeRestricted"
})
@XmlRootElement(name = "SearchSecretsByFolder")
public class SearchSecretsByFolder {

    protected String token;
    protected String searchTerm;
    @XmlElement(required = true, type = Integer.class, nillable = true)
    protected Integer folderId;
    protected boolean includeSubFolders;
    @XmlElement(required = true, type = Boolean.class, nillable = true)
    protected Boolean includeDeleted;
    @XmlElement(required = true, type = Boolean.class, nillable = true)
    protected Boolean includeRestricted;

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
     * Ruft den Wert der folderId-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getFolderId() {
        return folderId;
    }

    /**
     * Legt den Wert der folderId-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setFolderId(Integer value) {
        this.folderId = value;
    }

    /**
     * Ruft den Wert der includeSubFolders-Eigenschaft ab.
     * 
     */
    public boolean isIncludeSubFolders() {
        return includeSubFolders;
    }

    /**
     * Legt den Wert der includeSubFolders-Eigenschaft fest.
     * 
     */
    public void setIncludeSubFolders(boolean value) {
        this.includeSubFolders = value;
    }

    /**
     * Ruft den Wert der includeDeleted-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIncludeDeleted() {
        return includeDeleted;
    }

    /**
     * Legt den Wert der includeDeleted-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIncludeDeleted(Boolean value) {
        this.includeDeleted = value;
    }

    /**
     * Ruft den Wert der includeRestricted-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIncludeRestricted() {
        return includeRestricted;
    }

    /**
     * Legt den Wert der includeRestricted-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIncludeRestricted(Boolean value) {
        this.includeRestricted = value;
    }

}

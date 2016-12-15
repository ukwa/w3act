
package com.thesecretserver.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="token" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="searchTerm" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="showDeleted" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="showRestricted" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
    "searchTerm",
    "showDeleted",
    "showRestricted",
    "showPartialMatches"
})
@XmlRootElement(name = "SearchSecretsByExposedValues")
public class SearchSecretsByExposedValues {

    protected String token;
    protected String searchTerm;
    protected boolean showDeleted;
    protected boolean showRestricted;
    protected boolean showPartialMatches;

    /**
     * Gets the value of the token property.
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
     * Sets the value of the token property.
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
     * Gets the value of the searchTerm property.
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
     * Sets the value of the searchTerm property.
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
     * Gets the value of the showDeleted property.
     * 
     */
    public boolean isShowDeleted() {
        return showDeleted;
    }

    /**
     * Sets the value of the showDeleted property.
     * 
     */
    public void setShowDeleted(boolean value) {
        this.showDeleted = value;
    }

    /**
     * Gets the value of the showRestricted property.
     * 
     */
    public boolean isShowRestricted() {
        return showRestricted;
    }

    /**
     * Sets the value of the showRestricted property.
     * 
     */
    public void setShowRestricted(boolean value) {
        this.showRestricted = value;
    }

    /**
     * Gets the value of the showPartialMatches property.
     * 
     */
    public boolean isShowPartialMatches() {
        return showPartialMatches;
    }

    /**
     * Sets the value of the showPartialMatches property.
     * 
     */
    public void setShowPartialMatches(boolean value) {
        this.showPartialMatches = value;
    }

}


package com.thesecretserver.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
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
 *         &lt;element name="SearchSecretsLegacyResult" type="{urn:thesecretserver.com}SearchSecretsResult" minOccurs="0"/>
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
    "searchSecretsLegacyResult"
})
@XmlRootElement(name = "SearchSecretsLegacyResponse")
public class SearchSecretsLegacyResponse {

    @XmlElement(name = "SearchSecretsLegacyResult")
    protected SearchSecretsResult searchSecretsLegacyResult;

    /**
     * Gets the value of the searchSecretsLegacyResult property.
     * 
     * @return
     *     possible object is
     *     {@link SearchSecretsResult }
     *     
     */
    public SearchSecretsResult getSearchSecretsLegacyResult() {
        return searchSecretsLegacyResult;
    }

    /**
     * Sets the value of the searchSecretsLegacyResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link SearchSecretsResult }
     *     
     */
    public void setSearchSecretsLegacyResult(SearchSecretsResult value) {
        this.searchSecretsLegacyResult = value;
    }

}

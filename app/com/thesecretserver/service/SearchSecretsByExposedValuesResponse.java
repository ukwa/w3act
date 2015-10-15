
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
 *         &lt;element name="SearchSecretsByExposedValuesResult" type="{urn:thesecretserver.com}SearchSecretsResult" minOccurs="0"/>
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
    "searchSecretsByExposedValuesResult"
})
@XmlRootElement(name = "SearchSecretsByExposedValuesResponse")
public class SearchSecretsByExposedValuesResponse {

    @XmlElement(name = "SearchSecretsByExposedValuesResult")
    protected SearchSecretsResult searchSecretsByExposedValuesResult;

    /**
     * Gets the value of the searchSecretsByExposedValuesResult property.
     * 
     * @return
     *     possible object is
     *     {@link SearchSecretsResult }
     *     
     */
    public SearchSecretsResult getSearchSecretsByExposedValuesResult() {
        return searchSecretsByExposedValuesResult;
    }

    /**
     * Sets the value of the searchSecretsByExposedValuesResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link SearchSecretsResult }
     *     
     */
    public void setSearchSecretsByExposedValuesResult(SearchSecretsResult value) {
        this.searchSecretsByExposedValuesResult = value;
    }

}


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
 *         &lt;element name="SearchSecretsByExposedFieldValueResult" type="{urn:thesecretserver.com}SearchSecretsResult" minOccurs="0"/>
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
    "searchSecretsByExposedFieldValueResult"
})
@XmlRootElement(name = "SearchSecretsByExposedFieldValueResponse")
public class SearchSecretsByExposedFieldValueResponse {

    @XmlElement(name = "SearchSecretsByExposedFieldValueResult")
    protected SearchSecretsResult searchSecretsByExposedFieldValueResult;

    /**
     * Gets the value of the searchSecretsByExposedFieldValueResult property.
     * 
     * @return
     *     possible object is
     *     {@link SearchSecretsResult }
     *     
     */
    public SearchSecretsResult getSearchSecretsByExposedFieldValueResult() {
        return searchSecretsByExposedFieldValueResult;
    }

    /**
     * Sets the value of the searchSecretsByExposedFieldValueResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link SearchSecretsResult }
     *     
     */
    public void setSearchSecretsByExposedFieldValueResult(SearchSecretsResult value) {
        this.searchSecretsByExposedFieldValueResult = value;
    }

}


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
 *         &lt;element name="SearchSecretPoliciesResult" type="{urn:thesecretserver.com}SearchSecretPoliciesResult" minOccurs="0"/>
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
    "searchSecretPoliciesResult"
})
@XmlRootElement(name = "SearchSecretPoliciesResponse")
public class SearchSecretPoliciesResponse {

    @XmlElement(name = "SearchSecretPoliciesResult")
    protected SearchSecretPoliciesResult searchSecretPoliciesResult;

    /**
     * Gets the value of the searchSecretPoliciesResult property.
     * 
     * @return
     *     possible object is
     *     {@link SearchSecretPoliciesResult }
     *     
     */
    public SearchSecretPoliciesResult getSearchSecretPoliciesResult() {
        return searchSecretPoliciesResult;
    }

    /**
     * Sets the value of the searchSecretPoliciesResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link SearchSecretPoliciesResult }
     *     
     */
    public void setSearchSecretPoliciesResult(SearchSecretPoliciesResult value) {
        this.searchSecretPoliciesResult = value;
    }

}

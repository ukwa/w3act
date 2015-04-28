
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
 *         &lt;element name="SearchWebPasswordsForUrlResult" type="{urn:thesecretserver.com}GetWebPasswordResult" minOccurs="0"/>
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
    "searchWebPasswordsForUrlResult"
})
@XmlRootElement(name = "SearchWebPasswordsForUrlResponse")
public class SearchWebPasswordsForUrlResponse {

    @XmlElement(name = "SearchWebPasswordsForUrlResult")
    protected GetWebPasswordResult searchWebPasswordsForUrlResult;

    /**
     * Gets the value of the searchWebPasswordsForUrlResult property.
     * 
     * @return
     *     possible object is
     *     {@link GetWebPasswordResult }
     *     
     */
    public GetWebPasswordResult getSearchWebPasswordsForUrlResult() {
        return searchWebPasswordsForUrlResult;
    }

    /**
     * Sets the value of the searchWebPasswordsForUrlResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link GetWebPasswordResult }
     *     
     */
    public void setSearchWebPasswordsForUrlResult(GetWebPasswordResult value) {
        this.searchWebPasswordsForUrlResult = value;
    }

}

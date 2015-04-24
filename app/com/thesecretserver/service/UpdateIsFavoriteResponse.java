
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
 *         &lt;element name="UpdateIsFavoriteResult" type="{urn:thesecretserver.com}WebServiceResult" minOccurs="0"/>
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
    "updateIsFavoriteResult"
})
@XmlRootElement(name = "UpdateIsFavoriteResponse")
public class UpdateIsFavoriteResponse {

    @XmlElement(name = "UpdateIsFavoriteResult")
    protected WebServiceResult updateIsFavoriteResult;

    /**
     * Gets the value of the updateIsFavoriteResult property.
     * 
     * @return
     *     possible object is
     *     {@link WebServiceResult }
     *     
     */
    public WebServiceResult getUpdateIsFavoriteResult() {
        return updateIsFavoriteResult;
    }

    /**
     * Sets the value of the updateIsFavoriteResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link WebServiceResult }
     *     
     */
    public void setUpdateIsFavoriteResult(WebServiceResult value) {
        this.updateIsFavoriteResult = value;
    }

}

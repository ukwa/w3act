
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
 *         &lt;element name="GetFavoritesResult" type="{urn:thesecretserver.com}GetFavoritesResult" minOccurs="0"/>
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
    "getFavoritesResult"
})
@XmlRootElement(name = "GetFavoritesResponse")
public class GetFavoritesResponse {

    @XmlElement(name = "GetFavoritesResult")
    protected GetFavoritesResult getFavoritesResult;

    /**
     * Gets the value of the getFavoritesResult property.
     * 
     * @return
     *     possible object is
     *     {@link GetFavoritesResult }
     *     
     */
    public GetFavoritesResult getGetFavoritesResult() {
        return getFavoritesResult;
    }

    /**
     * Sets the value of the getFavoritesResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link GetFavoritesResult }
     *     
     */
    public void setGetFavoritesResult(GetFavoritesResult value) {
        this.getFavoritesResult = value;
    }

}

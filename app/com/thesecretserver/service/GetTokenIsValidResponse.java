
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
 *         &lt;element name="GetTokenIsValidResult" type="{urn:thesecretserver.com}TokenIsValidResult" minOccurs="0"/>
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
    "getTokenIsValidResult"
})
@XmlRootElement(name = "GetTokenIsValidResponse")
public class GetTokenIsValidResponse {

    @XmlElement(name = "GetTokenIsValidResult")
    protected TokenIsValidResult getTokenIsValidResult;

    /**
     * Gets the value of the getTokenIsValidResult property.
     * 
     * @return
     *     possible object is
     *     {@link TokenIsValidResult }
     *     
     */
    public TokenIsValidResult getGetTokenIsValidResult() {
        return getTokenIsValidResult;
    }

    /**
     * Sets the value of the getTokenIsValidResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link TokenIsValidResult }
     *     
     */
    public void setGetTokenIsValidResult(TokenIsValidResult value) {
        this.getTokenIsValidResult = value;
    }

}

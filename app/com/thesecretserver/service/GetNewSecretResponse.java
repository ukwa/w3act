
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
 *         &lt;element name="GetNewSecretResult" type="{urn:thesecretserver.com}GetSecretResult" minOccurs="0"/>
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
    "getNewSecretResult"
})
@XmlRootElement(name = "GetNewSecretResponse")
public class GetNewSecretResponse {

    @XmlElement(name = "GetNewSecretResult")
    protected GetSecretResult getNewSecretResult;

    /**
     * Gets the value of the getNewSecretResult property.
     * 
     * @return
     *     possible object is
     *     {@link GetSecretResult }
     *     
     */
    public GetSecretResult getGetNewSecretResult() {
        return getNewSecretResult;
    }

    /**
     * Sets the value of the getNewSecretResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link GetSecretResult }
     *     
     */
    public void setGetNewSecretResult(GetSecretResult value) {
        this.getNewSecretResult = value;
    }

}

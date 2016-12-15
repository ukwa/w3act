
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
 *         &lt;element name="AuthenticateResult" type="{urn:thesecretserver.com}AuthenticateResult" minOccurs="0"/>
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
    "authenticateResult"
})
@XmlRootElement(name = "AuthenticateResponse")
public class AuthenticateResponse {

    @XmlElement(name = "AuthenticateResult")
    protected AuthenticateResult authenticateResult;

    /**
     * Gets the value of the authenticateResult property.
     * 
     * @return
     *     possible object is
     *     {@link AuthenticateResult }
     *     
     */
    public AuthenticateResult getAuthenticateResult() {
        return authenticateResult;
    }

    /**
     * Sets the value of the authenticateResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link AuthenticateResult }
     *     
     */
    public void setAuthenticateResult(AuthenticateResult value) {
        this.authenticateResult = value;
    }

}

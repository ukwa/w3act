
package com.thesecretserver.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für anonymous complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="AuthenticateRADIUSResult" type="{urn:thesecretserver.com}AuthenticateResult" minOccurs="0"/>
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
    "authenticateRADIUSResult"
})
@XmlRootElement(name = "AuthenticateRADIUSResponse")
public class AuthenticateRADIUSResponse {

    @XmlElement(name = "AuthenticateRADIUSResult")
    protected AuthenticateResult authenticateRADIUSResult;

    /**
     * Ruft den Wert der authenticateRADIUSResult-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link AuthenticateResult }
     *     
     */
    public AuthenticateResult getAuthenticateRADIUSResult() {
        return authenticateRADIUSResult;
    }

    /**
     * Legt den Wert der authenticateRADIUSResult-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link AuthenticateResult }
     *     
     */
    public void setAuthenticateRADIUSResult(AuthenticateResult value) {
        this.authenticateRADIUSResult = value;
    }

}

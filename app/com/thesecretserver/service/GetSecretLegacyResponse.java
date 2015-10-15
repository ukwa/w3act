
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
 *         &lt;element name="GetSecretLegacyResult" type="{urn:thesecretserver.com}GetSecretResult" minOccurs="0"/>
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
    "getSecretLegacyResult"
})
@XmlRootElement(name = "GetSecretLegacyResponse")
public class GetSecretLegacyResponse {

    @XmlElement(name = "GetSecretLegacyResult")
    protected GetSecretResult getSecretLegacyResult;

    /**
     * Ruft den Wert der getSecretLegacyResult-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link GetSecretResult }
     *     
     */
    public GetSecretResult getGetSecretLegacyResult() {
        return getSecretLegacyResult;
    }

    /**
     * Legt den Wert der getSecretLegacyResult-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link GetSecretResult }
     *     
     */
    public void setGetSecretLegacyResult(GetSecretResult value) {
        this.getSecretLegacyResult = value;
    }

}


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
     * Ruft den Wert der getNewSecretResult-Eigenschaft ab.
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
     * Legt den Wert der getNewSecretResult-Eigenschaft fest.
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

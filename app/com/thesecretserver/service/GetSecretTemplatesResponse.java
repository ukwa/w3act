
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
 *         &lt;element name="GetSecretTemplatesResult" type="{urn:thesecretserver.com}GetSecretTemplatesResult" minOccurs="0"/>
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
    "getSecretTemplatesResult"
})
@XmlRootElement(name = "GetSecretTemplatesResponse")
public class GetSecretTemplatesResponse {

    @XmlElement(name = "GetSecretTemplatesResult")
    protected GetSecretTemplatesResult getSecretTemplatesResult;

    /**
     * Ruft den Wert der getSecretTemplatesResult-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link GetSecretTemplatesResult }
     *     
     */
    public GetSecretTemplatesResult getGetSecretTemplatesResult() {
        return getSecretTemplatesResult;
    }

    /**
     * Legt den Wert der getSecretTemplatesResult-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link GetSecretTemplatesResult }
     *     
     */
    public void setGetSecretTemplatesResult(GetSecretTemplatesResult value) {
        this.getSecretTemplatesResult = value;
    }

}

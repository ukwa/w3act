
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
 *         &lt;element name="GetSecretItemHistoryByFieldNameResult" type="{urn:thesecretserver.com}SecretItemHistoryResult" minOccurs="0"/>
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
    "getSecretItemHistoryByFieldNameResult"
})
@XmlRootElement(name = "GetSecretItemHistoryByFieldNameResponse")
public class GetSecretItemHistoryByFieldNameResponse {

    @XmlElement(name = "GetSecretItemHistoryByFieldNameResult")
    protected SecretItemHistoryResult getSecretItemHistoryByFieldNameResult;

    /**
     * Ruft den Wert der getSecretItemHistoryByFieldNameResult-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link SecretItemHistoryResult }
     *     
     */
    public SecretItemHistoryResult getGetSecretItemHistoryByFieldNameResult() {
        return getSecretItemHistoryByFieldNameResult;
    }

    /**
     * Legt den Wert der getSecretItemHistoryByFieldNameResult-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link SecretItemHistoryResult }
     *     
     */
    public void setGetSecretItemHistoryByFieldNameResult(SecretItemHistoryResult value) {
        this.getSecretItemHistoryByFieldNameResult = value;
    }

}

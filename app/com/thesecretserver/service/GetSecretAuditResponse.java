
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
 *         &lt;element name="GetSecretAuditResult" type="{urn:thesecretserver.com}GetSecretAuditResult" minOccurs="0"/>
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
    "getSecretAuditResult"
})
@XmlRootElement(name = "GetSecretAuditResponse")
public class GetSecretAuditResponse {

    @XmlElement(name = "GetSecretAuditResult")
    protected GetSecretAuditResult getSecretAuditResult;

    /**
     * Ruft den Wert der getSecretAuditResult-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link GetSecretAuditResult }
     *     
     */
    public GetSecretAuditResult getGetSecretAuditResult() {
        return getSecretAuditResult;
    }

    /**
     * Legt den Wert der getSecretAuditResult-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link GetSecretAuditResult }
     *     
     */
    public void setGetSecretAuditResult(GetSecretAuditResult value) {
        this.getSecretAuditResult = value;
    }

}

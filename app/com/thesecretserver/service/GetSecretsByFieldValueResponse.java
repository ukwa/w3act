
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
 *         &lt;element name="GetSecretsByFieldValueResult" type="{urn:thesecretserver.com}GetSecretsByFieldValueResult" minOccurs="0"/>
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
    "getSecretsByFieldValueResult"
})
@XmlRootElement(name = "GetSecretsByFieldValueResponse")
public class GetSecretsByFieldValueResponse {

    @XmlElement(name = "GetSecretsByFieldValueResult")
    protected GetSecretsByFieldValueResult getSecretsByFieldValueResult;

    /**
     * Ruft den Wert der getSecretsByFieldValueResult-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link GetSecretsByFieldValueResult }
     *     
     */
    public GetSecretsByFieldValueResult getGetSecretsByFieldValueResult() {
        return getSecretsByFieldValueResult;
    }

    /**
     * Legt den Wert der getSecretsByFieldValueResult-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link GetSecretsByFieldValueResult }
     *     
     */
    public void setGetSecretsByFieldValueResult(GetSecretsByFieldValueResult value) {
        this.getSecretsByFieldValueResult = value;
    }

}

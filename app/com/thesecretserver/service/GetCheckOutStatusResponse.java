
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
 *         &lt;element name="GetCheckOutStatusResult" type="{urn:thesecretserver.com}GetCheckOutStatusResult" minOccurs="0"/>
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
    "getCheckOutStatusResult"
})
@XmlRootElement(name = "GetCheckOutStatusResponse")
public class GetCheckOutStatusResponse {

    @XmlElement(name = "GetCheckOutStatusResult")
    protected GetCheckOutStatusResult getCheckOutStatusResult;

    /**
     * Ruft den Wert der getCheckOutStatusResult-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link GetCheckOutStatusResult }
     *     
     */
    public GetCheckOutStatusResult getGetCheckOutStatusResult() {
        return getCheckOutStatusResult;
    }

    /**
     * Legt den Wert der getCheckOutStatusResult-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link GetCheckOutStatusResult }
     *     
     */
    public void setGetCheckOutStatusResult(GetCheckOutStatusResult value) {
        this.getCheckOutStatusResult = value;
    }

}


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
 *         &lt;element name="GetUserResult" type="{urn:thesecretserver.com}GetUserResult" minOccurs="0"/>
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
    "getUserResult"
})
@XmlRootElement(name = "GetUserResponse")
public class GetUserResponse {

    @XmlElement(name = "GetUserResult")
    protected GetUserResult getUserResult;

    /**
     * Ruft den Wert der getUserResult-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link GetUserResult }
     *     
     */
    public GetUserResult getGetUserResult() {
        return getUserResult;
    }

    /**
     * Legt den Wert der getUserResult-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link GetUserResult }
     *     
     */
    public void setGetUserResult(GetUserResult value) {
        this.getUserResult = value;
    }

}

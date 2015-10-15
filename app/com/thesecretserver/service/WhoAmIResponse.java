
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
 *         &lt;element name="WhoAmIResult" type="{urn:thesecretserver.com}UserInfoResult" minOccurs="0"/>
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
    "whoAmIResult"
})
@XmlRootElement(name = "WhoAmIResponse")
public class WhoAmIResponse {

    @XmlElement(name = "WhoAmIResult")
    protected UserInfoResult whoAmIResult;

    /**
     * Ruft den Wert der whoAmIResult-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link UserInfoResult }
     *     
     */
    public UserInfoResult getWhoAmIResult() {
        return whoAmIResult;
    }

    /**
     * Legt den Wert der whoAmIResult-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link UserInfoResult }
     *     
     */
    public void setWhoAmIResult(UserInfoResult value) {
        this.whoAmIResult = value;
    }

}

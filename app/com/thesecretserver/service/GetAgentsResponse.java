
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
 *         &lt;element name="GetAgentsResult" type="{urn:thesecretserver.com}GetAgentsResult" minOccurs="0"/>
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
    "getAgentsResult"
})
@XmlRootElement(name = "GetAgentsResponse")
public class GetAgentsResponse {

    @XmlElement(name = "GetAgentsResult")
    protected GetAgentsResult getAgentsResult;

    /**
     * Ruft den Wert der getAgentsResult-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link GetAgentsResult }
     *     
     */
    public GetAgentsResult getGetAgentsResult() {
        return getAgentsResult;
    }

    /**
     * Legt den Wert der getAgentsResult-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link GetAgentsResult }
     *     
     */
    public void setGetAgentsResult(GetAgentsResult value) {
        this.getAgentsResult = value;
    }

}

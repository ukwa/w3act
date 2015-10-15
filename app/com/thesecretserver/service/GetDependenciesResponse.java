
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
 *         &lt;element name="GetDependenciesResult" type="{urn:thesecretserver.com}GetDependenciesResult" minOccurs="0"/>
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
    "getDependenciesResult"
})
@XmlRootElement(name = "GetDependenciesResponse")
public class GetDependenciesResponse {

    @XmlElement(name = "GetDependenciesResult")
    protected GetDependenciesResult getDependenciesResult;

    /**
     * Ruft den Wert der getDependenciesResult-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link GetDependenciesResult }
     *     
     */
    public GetDependenciesResult getGetDependenciesResult() {
        return getDependenciesResult;
    }

    /**
     * Legt den Wert der getDependenciesResult-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link GetDependenciesResult }
     *     
     */
    public void setGetDependenciesResult(GetDependenciesResult value) {
        this.getDependenciesResult = value;
    }

}

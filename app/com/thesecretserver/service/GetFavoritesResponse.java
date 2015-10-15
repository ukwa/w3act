
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
 *         &lt;element name="GetFavoritesResult" type="{urn:thesecretserver.com}GetFavoritesResult" minOccurs="0"/>
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
    "getFavoritesResult"
})
@XmlRootElement(name = "GetFavoritesResponse")
public class GetFavoritesResponse {

    @XmlElement(name = "GetFavoritesResult")
    protected GetFavoritesResult getFavoritesResult;

    /**
     * Ruft den Wert der getFavoritesResult-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link GetFavoritesResult }
     *     
     */
    public GetFavoritesResult getGetFavoritesResult() {
        return getFavoritesResult;
    }

    /**
     * Legt den Wert der getFavoritesResult-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link GetFavoritesResult }
     *     
     */
    public void setGetFavoritesResult(GetFavoritesResult value) {
        this.getFavoritesResult = value;
    }

}

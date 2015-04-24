
package com.thesecretserver.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
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
     * Gets the value of the getSecretsByFieldValueResult property.
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
     * Sets the value of the getSecretsByFieldValueResult property.
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


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
 *         &lt;element name="GetSecretTemplateFieldsResult" type="{urn:thesecretserver.com}GetSecretTemplateFieldsResult" minOccurs="0"/>
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
    "getSecretTemplateFieldsResult"
})
@XmlRootElement(name = "GetSecretTemplateFieldsResponse")
public class GetSecretTemplateFieldsResponse {

    @XmlElement(name = "GetSecretTemplateFieldsResult")
    protected GetSecretTemplateFieldsResult getSecretTemplateFieldsResult;

    /**
     * Gets the value of the getSecretTemplateFieldsResult property.
     * 
     * @return
     *     possible object is
     *     {@link GetSecretTemplateFieldsResult }
     *     
     */
    public GetSecretTemplateFieldsResult getGetSecretTemplateFieldsResult() {
        return getSecretTemplateFieldsResult;
    }

    /**
     * Sets the value of the getSecretTemplateFieldsResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link GetSecretTemplateFieldsResult }
     *     
     */
    public void setGetSecretTemplateFieldsResult(GetSecretTemplateFieldsResult value) {
        this.getSecretTemplateFieldsResult = value;
    }

}


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
 *         &lt;element name="GetSecretItemHistoryByFieldNameResult" type="{urn:thesecretserver.com}SecretItemHistoryResult" minOccurs="0"/>
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
    "getSecretItemHistoryByFieldNameResult"
})
@XmlRootElement(name = "GetSecretItemHistoryByFieldNameResponse")
public class GetSecretItemHistoryByFieldNameResponse {

    @XmlElement(name = "GetSecretItemHistoryByFieldNameResult")
    protected SecretItemHistoryResult getSecretItemHistoryByFieldNameResult;

    /**
     * Gets the value of the getSecretItemHistoryByFieldNameResult property.
     * 
     * @return
     *     possible object is
     *     {@link SecretItemHistoryResult }
     *     
     */
    public SecretItemHistoryResult getGetSecretItemHistoryByFieldNameResult() {
        return getSecretItemHistoryByFieldNameResult;
    }

    /**
     * Sets the value of the getSecretItemHistoryByFieldNameResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link SecretItemHistoryResult }
     *     
     */
    public void setGetSecretItemHistoryByFieldNameResult(SecretItemHistoryResult value) {
        this.getSecretItemHistoryByFieldNameResult = value;
    }

}

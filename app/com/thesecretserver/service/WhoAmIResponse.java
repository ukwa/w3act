
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
     * Gets the value of the whoAmIResult property.
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
     * Sets the value of the whoAmIResult property.
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


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
 *         &lt;element name="CheckInByKeyResult" type="{urn:thesecretserver.com}WebServiceResult" minOccurs="0"/>
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
    "checkInByKeyResult"
})
@XmlRootElement(name = "CheckInByKeyResponse")
public class CheckInByKeyResponse {

    @XmlElement(name = "CheckInByKeyResult")
    protected WebServiceResult checkInByKeyResult;

    /**
     * Gets the value of the checkInByKeyResult property.
     * 
     * @return
     *     possible object is
     *     {@link WebServiceResult }
     *     
     */
    public WebServiceResult getCheckInByKeyResult() {
        return checkInByKeyResult;
    }

    /**
     * Sets the value of the checkInByKeyResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link WebServiceResult }
     *     
     */
    public void setCheckInByKeyResult(WebServiceResult value) {
        this.checkInByKeyResult = value;
    }

}

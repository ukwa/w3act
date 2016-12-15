
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
 *         &lt;element name="GetTicketSystemsResult" type="{urn:thesecretserver.com}GetTicketSystemsResult" minOccurs="0"/>
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
    "getTicketSystemsResult"
})
@XmlRootElement(name = "GetTicketSystemsResponse")
public class GetTicketSystemsResponse {

    @XmlElement(name = "GetTicketSystemsResult")
    protected GetTicketSystemsResult getTicketSystemsResult;

    /**
     * Gets the value of the getTicketSystemsResult property.
     * 
     * @return
     *     possible object is
     *     {@link GetTicketSystemsResult }
     *     
     */
    public GetTicketSystemsResult getGetTicketSystemsResult() {
        return getTicketSystemsResult;
    }

    /**
     * Sets the value of the getTicketSystemsResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link GetTicketSystemsResult }
     *     
     */
    public void setGetTicketSystemsResult(GetTicketSystemsResult value) {
        this.getTicketSystemsResult = value;
    }

}

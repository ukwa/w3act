
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
     * Gets the value of the getAgentsResult property.
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
     * Sets the value of the getAgentsResult property.
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

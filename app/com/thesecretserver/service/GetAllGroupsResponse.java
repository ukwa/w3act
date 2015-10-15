
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
 *         &lt;element name="GetAllGroupsResult" type="{urn:thesecretserver.com}GetAllGroupsResult" minOccurs="0"/>
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
    "getAllGroupsResult"
})
@XmlRootElement(name = "GetAllGroupsResponse")
public class GetAllGroupsResponse {

    @XmlElement(name = "GetAllGroupsResult")
    protected GetAllGroupsResult getAllGroupsResult;

    /**
     * Gets the value of the getAllGroupsResult property.
     * 
     * @return
     *     possible object is
     *     {@link GetAllGroupsResult }
     *     
     */
    public GetAllGroupsResult getGetAllGroupsResult() {
        return getAllGroupsResult;
    }

    /**
     * Sets the value of the getAllGroupsResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link GetAllGroupsResult }
     *     
     */
    public void setGetAllGroupsResult(GetAllGroupsResult value) {
        this.getAllGroupsResult = value;
    }

}

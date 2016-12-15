
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
 *         &lt;element name="GetAllSSHCommandMenusResult" type="{urn:thesecretserver.com}GetSshCommandMenusResult" minOccurs="0"/>
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
    "getAllSSHCommandMenusResult"
})
@XmlRootElement(name = "GetAllSSHCommandMenusResponse")
public class GetAllSSHCommandMenusResponse {

    @XmlElement(name = "GetAllSSHCommandMenusResult")
    protected GetSshCommandMenusResult getAllSSHCommandMenusResult;

    /**
     * Gets the value of the getAllSSHCommandMenusResult property.
     * 
     * @return
     *     possible object is
     *     {@link GetSshCommandMenusResult }
     *     
     */
    public GetSshCommandMenusResult getGetAllSSHCommandMenusResult() {
        return getAllSSHCommandMenusResult;
    }

    /**
     * Sets the value of the getAllSSHCommandMenusResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link GetSshCommandMenusResult }
     *     
     */
    public void setGetAllSSHCommandMenusResult(GetSshCommandMenusResult value) {
        this.getAllSSHCommandMenusResult = value;
    }

}

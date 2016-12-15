
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
 *         &lt;element name="GetSSHCommandMenuResult" type="{urn:thesecretserver.com}GetSshCommandMenuResult" minOccurs="0"/>
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
    "getSSHCommandMenuResult"
})
@XmlRootElement(name = "GetSSHCommandMenuResponse")
public class GetSSHCommandMenuResponse {

    @XmlElement(name = "GetSSHCommandMenuResult")
    protected GetSshCommandMenuResult getSSHCommandMenuResult;

    /**
     * Gets the value of the getSSHCommandMenuResult property.
     * 
     * @return
     *     possible object is
     *     {@link GetSshCommandMenuResult }
     *     
     */
    public GetSshCommandMenuResult getGetSSHCommandMenuResult() {
        return getSSHCommandMenuResult;
    }

    /**
     * Sets the value of the getSSHCommandMenuResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link GetSshCommandMenuResult }
     *     
     */
    public void setGetSSHCommandMenuResult(GetSshCommandMenuResult value) {
        this.getSSHCommandMenuResult = value;
    }

}

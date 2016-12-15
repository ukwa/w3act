
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
 *         &lt;element name="DeleteSSHCommandMenuResult" type="{urn:thesecretserver.com}WebServiceResult" minOccurs="0"/>
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
    "deleteSSHCommandMenuResult"
})
@XmlRootElement(name = "DeleteSSHCommandMenuResponse")
public class DeleteSSHCommandMenuResponse {

    @XmlElement(name = "DeleteSSHCommandMenuResult")
    protected WebServiceResult deleteSSHCommandMenuResult;

    /**
     * Gets the value of the deleteSSHCommandMenuResult property.
     * 
     * @return
     *     possible object is
     *     {@link WebServiceResult }
     *     
     */
    public WebServiceResult getDeleteSSHCommandMenuResult() {
        return deleteSSHCommandMenuResult;
    }

    /**
     * Sets the value of the deleteSSHCommandMenuResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link WebServiceResult }
     *     
     */
    public void setDeleteSSHCommandMenuResult(WebServiceResult value) {
        this.deleteSSHCommandMenuResult = value;
    }

}

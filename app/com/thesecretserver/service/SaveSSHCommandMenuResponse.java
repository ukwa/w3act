
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
 *         &lt;element name="SaveSSHCommandMenuResult" type="{urn:thesecretserver.com}GetSshCommandMenuResult" minOccurs="0"/>
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
    "saveSSHCommandMenuResult"
})
@XmlRootElement(name = "SaveSSHCommandMenuResponse")
public class SaveSSHCommandMenuResponse {

    @XmlElement(name = "SaveSSHCommandMenuResult")
    protected GetSshCommandMenuResult saveSSHCommandMenuResult;

    /**
     * Gets the value of the saveSSHCommandMenuResult property.
     * 
     * @return
     *     possible object is
     *     {@link GetSshCommandMenuResult }
     *     
     */
    public GetSshCommandMenuResult getSaveSSHCommandMenuResult() {
        return saveSSHCommandMenuResult;
    }

    /**
     * Sets the value of the saveSSHCommandMenuResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link GetSshCommandMenuResult }
     *     
     */
    public void setSaveSSHCommandMenuResult(GetSshCommandMenuResult value) {
        this.saveSSHCommandMenuResult = value;
    }

}


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
 *         &lt;element name="AddSecretResult" type="{urn:thesecretserver.com}AddSecretResult" minOccurs="0"/>
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
    "addSecretResult"
})
@XmlRootElement(name = "AddSecretResponse")
public class AddSecretResponse {

    @XmlElement(name = "AddSecretResult")
    protected AddSecretResult addSecretResult;

    /**
     * Gets the value of the addSecretResult property.
     * 
     * @return
     *     possible object is
     *     {@link AddSecretResult }
     *     
     */
    public AddSecretResult getAddSecretResult() {
        return addSecretResult;
    }

    /**
     * Sets the value of the addSecretResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link AddSecretResult }
     *     
     */
    public void setAddSecretResult(AddSecretResult value) {
        this.addSecretResult = value;
    }

}


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
 *         &lt;element name="GeneratePasswordResult" type="{urn:thesecretserver.com}GeneratePasswordResult" minOccurs="0"/>
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
    "generatePasswordResult"
})
@XmlRootElement(name = "GeneratePasswordResponse")
public class GeneratePasswordResponse {

    @XmlElement(name = "GeneratePasswordResult")
    protected GeneratePasswordResult generatePasswordResult;

    /**
     * Gets the value of the generatePasswordResult property.
     * 
     * @return
     *     possible object is
     *     {@link GeneratePasswordResult }
     *     
     */
    public GeneratePasswordResult getGeneratePasswordResult() {
        return generatePasswordResult;
    }

    /**
     * Sets the value of the generatePasswordResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link GeneratePasswordResult }
     *     
     */
    public void setGeneratePasswordResult(GeneratePasswordResult value) {
        this.generatePasswordResult = value;
    }

}


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
 *         &lt;element name="UpdateScriptResult" type="{urn:thesecretserver.com}UpdateUserScriptResult" minOccurs="0"/>
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
    "updateScriptResult"
})
@XmlRootElement(name = "UpdateScriptResponse")
public class UpdateScriptResponse {

    @XmlElement(name = "UpdateScriptResult")
    protected UpdateUserScriptResult updateScriptResult;

    /**
     * Gets the value of the updateScriptResult property.
     * 
     * @return
     *     possible object is
     *     {@link UpdateUserScriptResult }
     *     
     */
    public UpdateUserScriptResult getUpdateScriptResult() {
        return updateScriptResult;
    }

    /**
     * Sets the value of the updateScriptResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link UpdateUserScriptResult }
     *     
     */
    public void setUpdateScriptResult(UpdateUserScriptResult value) {
        this.updateScriptResult = value;
    }

}

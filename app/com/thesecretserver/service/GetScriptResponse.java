
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
 *         &lt;element name="GetScriptResult" type="{urn:thesecretserver.com}GetUserScriptResult" minOccurs="0"/>
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
    "getScriptResult"
})
@XmlRootElement(name = "GetScriptResponse")
public class GetScriptResponse {

    @XmlElement(name = "GetScriptResult")
    protected GetUserScriptResult getScriptResult;

    /**
     * Gets the value of the getScriptResult property.
     * 
     * @return
     *     possible object is
     *     {@link GetUserScriptResult }
     *     
     */
    public GetUserScriptResult getGetScriptResult() {
        return getScriptResult;
    }

    /**
     * Sets the value of the getScriptResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link GetUserScriptResult }
     *     
     */
    public void setGetScriptResult(GetUserScriptResult value) {
        this.getScriptResult = value;
    }

}

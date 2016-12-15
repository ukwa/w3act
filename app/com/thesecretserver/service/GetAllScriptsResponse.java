
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
 *         &lt;element name="GetAllScriptsResult" type="{urn:thesecretserver.com}GetUserScriptsResult" minOccurs="0"/>
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
    "getAllScriptsResult"
})
@XmlRootElement(name = "GetAllScriptsResponse")
public class GetAllScriptsResponse {

    @XmlElement(name = "GetAllScriptsResult")
    protected GetUserScriptsResult getAllScriptsResult;

    /**
     * Gets the value of the getAllScriptsResult property.
     * 
     * @return
     *     possible object is
     *     {@link GetUserScriptsResult }
     *     
     */
    public GetUserScriptsResult getGetAllScriptsResult() {
        return getAllScriptsResult;
    }

    /**
     * Sets the value of the getAllScriptsResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link GetUserScriptsResult }
     *     
     */
    public void setGetAllScriptsResult(GetUserScriptsResult value) {
        this.getAllScriptsResult = value;
    }

}

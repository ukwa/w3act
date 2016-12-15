
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
 *         &lt;element name="RunActiveDirectorySynchronizationResult" type="{urn:thesecretserver.com}WebServiceResult" minOccurs="0"/>
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
    "runActiveDirectorySynchronizationResult"
})
@XmlRootElement(name = "RunActiveDirectorySynchronizationResponse")
public class RunActiveDirectorySynchronizationResponse {

    @XmlElement(name = "RunActiveDirectorySynchronizationResult")
    protected WebServiceResult runActiveDirectorySynchronizationResult;

    /**
     * Gets the value of the runActiveDirectorySynchronizationResult property.
     * 
     * @return
     *     possible object is
     *     {@link WebServiceResult }
     *     
     */
    public WebServiceResult getRunActiveDirectorySynchronizationResult() {
        return runActiveDirectorySynchronizationResult;
    }

    /**
     * Sets the value of the runActiveDirectorySynchronizationResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link WebServiceResult }
     *     
     */
    public void setRunActiveDirectorySynchronizationResult(WebServiceResult value) {
        this.runActiveDirectorySynchronizationResult = value;
    }

}

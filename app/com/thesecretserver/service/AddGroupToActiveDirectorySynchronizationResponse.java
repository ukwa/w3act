
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
 *         &lt;element name="AddGroupToActiveDirectorySynchronizationResult" type="{urn:thesecretserver.com}WebServiceResult" minOccurs="0"/>
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
    "addGroupToActiveDirectorySynchronizationResult"
})
@XmlRootElement(name = "AddGroupToActiveDirectorySynchronizationResponse")
public class AddGroupToActiveDirectorySynchronizationResponse {

    @XmlElement(name = "AddGroupToActiveDirectorySynchronizationResult")
    protected WebServiceResult addGroupToActiveDirectorySynchronizationResult;

    /**
     * Gets the value of the addGroupToActiveDirectorySynchronizationResult property.
     * 
     * @return
     *     possible object is
     *     {@link WebServiceResult }
     *     
     */
    public WebServiceResult getAddGroupToActiveDirectorySynchronizationResult() {
        return addGroupToActiveDirectorySynchronizationResult;
    }

    /**
     * Sets the value of the addGroupToActiveDirectorySynchronizationResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link WebServiceResult }
     *     
     */
    public void setAddGroupToActiveDirectorySynchronizationResult(WebServiceResult value) {
        this.addGroupToActiveDirectorySynchronizationResult = value;
    }

}

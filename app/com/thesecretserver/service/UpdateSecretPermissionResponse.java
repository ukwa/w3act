
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
 *         &lt;element name="UpdateSecretPermissionResult" type="{urn:thesecretserver.com}WebServiceResult" minOccurs="0"/>
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
    "updateSecretPermissionResult"
})
@XmlRootElement(name = "UpdateSecretPermissionResponse")
public class UpdateSecretPermissionResponse {

    @XmlElement(name = "UpdateSecretPermissionResult")
    protected WebServiceResult updateSecretPermissionResult;

    /**
     * Gets the value of the updateSecretPermissionResult property.
     * 
     * @return
     *     possible object is
     *     {@link WebServiceResult }
     *     
     */
    public WebServiceResult getUpdateSecretPermissionResult() {
        return updateSecretPermissionResult;
    }

    /**
     * Sets the value of the updateSecretPermissionResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link WebServiceResult }
     *     
     */
    public void setUpdateSecretPermissionResult(WebServiceResult value) {
        this.updateSecretPermissionResult = value;
    }

}

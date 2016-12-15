
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
 *         &lt;element name="GetSSHLoginCredentialsResult" type="{urn:thesecretserver.com}SSHCredentialsResult" minOccurs="0"/>
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
    "getSSHLoginCredentialsResult"
})
@XmlRootElement(name = "GetSSHLoginCredentialsResponse")
public class GetSSHLoginCredentialsResponse {

    @XmlElement(name = "GetSSHLoginCredentialsResult")
    protected SSHCredentialsResult getSSHLoginCredentialsResult;

    /**
     * Gets the value of the getSSHLoginCredentialsResult property.
     * 
     * @return
     *     possible object is
     *     {@link SSHCredentialsResult }
     *     
     */
    public SSHCredentialsResult getGetSSHLoginCredentialsResult() {
        return getSSHLoginCredentialsResult;
    }

    /**
     * Sets the value of the getSSHLoginCredentialsResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link SSHCredentialsResult }
     *     
     */
    public void setGetSSHLoginCredentialsResult(SSHCredentialsResult value) {
        this.getSSHLoginCredentialsResult = value;
    }

}
